package ECS.System;

import ECS.Classes.DamageHistory;
import ECS.Classes.Vector3;
import ECS.Components.AttackComponent;
import ECS.Components.ConditionComponent;
import ECS.Entity.*;
import ECS.Game.WorldMap;
import Network.RMI_Classes.RMI_Context;
import Network.RMI_Classes.RMI_ID;
import Network.RMI_Common.RMI_ParsingClasses.EntityType;
import Network.RMI_Common.server_to_client;

import java.util.HashMap;

/**
 * 전체 큰 로직 :
 *
 *     for( 몹 갯수만큼 반복 ){
 *         타겟 대상 판정(인식);
 *         if (타겟 있다면){    // 인식
 *             공격 가능 여부 판정;
 *             if( 가능하다면 ) {   // 공격
 *                 공격;
 *             }
 *             else {
 *                 공격 가능 조건 업데이트;
 *             }
 *         }
 *         else {   // 이동
 *             이동 가능 여부 판정;
 *             if( 가능 ){
 *                 타겟을 향해 이동;
 *             }
 *         }
 *     } // 끝
 *
 */
public class MonsterSystem {

    WorldMap worldMap;

    public MonsterSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime){

        /* 몬스터 수만큼 반복한다 */
        for( HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet() ){

            MonsterEntity monster = monsterEntity.getValue();

            /** 1. 타겟 인식 판정 처리 */
            boolean targetHasSelected = false;

            int targetID = -1;
            int targetType = -1;
            float minDistance = monster.sightComponent.lookRadius;    // 본인의 공격 반경으로 초기화

            float currentTargetDistance;
            float targetHP;
            Vector3 targetPosition = new Vector3();

            /* Character Entity */
            for(HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()){

                CharacterEntity character = characterEntity.getValue();

                /* 대상과의 거리를 계산한다 */
                currentTargetDistance
                        = Vector3.distance(monster.positionComponent.position, character.positionComponent.position);

                /* 인식 대상에 들어갈 수 있는지 판별 */
                boolean isMinDistance = (currentTargetDistance < minDistance) ? true : false;
                boolean isAttackable
                        = ( (!character.conditionComponent.isUnTargetable)
                        && (character.hpComponent.currentHP > 0) ) ;

                boolean isRecognizable = (isMinDistance && isAttackable) ? true : false;

                /* 타겟 업데이트 */
                if(isRecognizable){

                    minDistance = currentTargetDistance;
                    targetID = character.entityID;
                    targetType = EntityType.CharacterEntity; // 캐릭터다
                    targetPosition = character.positionComponent.position;

                }

            }


            /* Barricade Entity */
            for(HashMap.Entry<Integer, BarricadeEntity> barricadeEntity : worldMap.barricadeEntity.entrySet()){

                BarricadeEntity barricade = barricadeEntity.getValue();

                /* 대상과의 거리를 계산한다 */
                currentTargetDistance
                        = Vector3.distance(monster.positionComponent.position, barricade.positionComponent.position);

                /* 인식 대상에 들어갈 수 있는지 판별 */
                boolean isMinDistance = (currentTargetDistance < minDistance) ? true : false;
                boolean isAttackable
                        = ( (!barricade.conditionComponent.isUnTargetable)
                        && (barricade.hpComponent.currentHP > 0) ) ;

                boolean isRecognizable = (isMinDistance && isAttackable) ? true : false;

                /* 타겟 업데이트 */
                if(isRecognizable){

                    minDistance = currentTargetDistance;
                    targetID = barricade.entityID;
                    targetType = EntityType.BarricadeEntity; // 바리케이드다
                    targetPosition = barricade.positionComponent.position;

                }

            }

            /* Attack Turret Entity */
            for(HashMap.Entry<Integer, AttackTurretEntity> attackTurretEntity : worldMap.attackTurretEntity.entrySet()){

                AttackTurretEntity attackTurret = attackTurretEntity.getValue();

                /* 대상과의 거리를 계산한다 */
                currentTargetDistance
                        = Vector3.distance(monster.positionComponent.position, attackTurret.positionComponent.position);

                /* 인식 대상에 들어갈 수 있는지 판별 */
                boolean isMinDistance = (currentTargetDistance < minDistance) ? true : false;
                boolean isAttackable
                        = ( (!attackTurret.conditionComponent.isUnTargetable)
                        && (attackTurret.hpComponent.currentHP > 0) ) ;

                boolean isRecognizable = (isMinDistance && isAttackable) ? true : false;

                /* 타겟 업데이트 */
                if(isRecognizable){

                    minDistance = currentTargetDistance;
                    targetID = attackTurret.entityID;
                    targetType = EntityType.AttackTurretEntity; // 공격 터렛이다
                    targetPosition = attackTurret.positionComponent.position;

                }

            }

            /* Buff Turret Entity */
            for(HashMap.Entry<Integer, BuffTurretEntity> buffTurretEntity : worldMap.buffTurretEntity.entrySet()){

                BuffTurretEntity buffTurret = buffTurretEntity.getValue();

                /* 대상과의 거리를 계산한다 */
                currentTargetDistance
                        = Vector3.distance(monster.positionComponent.position, buffTurret.positionComponent.position);

                /* 인식 대상에 들어갈 수 있는지 판별 */
                boolean isMinDistance = (currentTargetDistance < minDistance) ? true : false;
                boolean isAttackable
                        = ( (!buffTurret.conditionComponent.isUnTargetable)
                        && (buffTurret.hpComponent.currentHP > 0) ) ;

                boolean isRecognizable = (isMinDistance && isAttackable) ? true : false;

                /* 타겟 업데이트 */
                if(isRecognizable){

                    minDistance = currentTargetDistance;
                    targetID = buffTurret.entityID;
                    targetType = EntityType.BuffTurretEntity; // 버프 터렛이다
                    targetPosition = buffTurret.positionComponent.position;
                }

            }

            /* Crystal */
            for(HashMap.Entry<Integer, CrystalEntity> crystalEntity : worldMap.crystalEntity.entrySet()){

                CrystalEntity crystal = crystalEntity.getValue();

                //임시 변수.
                Vector3 newTarget = new Vector3();
                newTarget.set(crystal.positionComponent.position.x(),0f,crystal.positionComponent.position.z());

                //대상과의 거리를 계산한다
                currentTargetDistance
                        = Vector3.distance(monster.positionComponent.position, newTarget);

                //인식 대상에 들어갈 수 있는지 판별
                boolean isMinDistance = (currentTargetDistance < minDistance) ? true : false;
                boolean isAttackable
                        = ( (!crystal.conditionComponent.isUnTargetable)
                        && (crystal.hpComponent.currentHP > 0) ) ;

                boolean isRecognizable = (isMinDistance && isAttackable) ? true : false;

                //타겟 업데이트
                if(isRecognizable){

                    minDistance = currentTargetDistance;
                    targetID = crystal.entityID;
                    targetType = EntityType.CrystalEntity; // 크리스탈이다
                    targetPosition = newTarget;

                }

            }

            /* 타겟 지정 여부 */
            targetHasSelected = (targetID >= 0) ? true : false;


            /** 2. 타겟 판정에 따른 처리 */
            boolean decidedToMove = false;
            boolean decidedToAttack = false;


            boolean isTargetWithinRange = (minDistance <= monster.attackComponent.attackRange) ? true : false;
            boolean ableToAttack = !(monster.conditionComponent.isDisableAttack);
            boolean isRemainCoolTimeZero = ( monster.attackComponent.remainCoolTime <= 0) ? true : false;
            if(targetHasSelected){

                /** 타겟에 대한 행동을 판정한다 ; 쫒아감 & 공격 */

                decidedToAttack
                        = (isTargetWithinRange && ableToAttack && isRemainCoolTimeZero) ? true : false;

                if(decidedToAttack){

                    server_to_client.motionMonsterDoAttack(
                            RMI_ID.getArray(worldMap.worldMapRMI_IDList.values()), RMI_Context.Reliable_Public_AES128, monster.entityID, (short)targetType, targetID);

                    /** 타겟에게 공격 처리를 한다 */

                    /* 입힐 데미지를 계산한다 */
                    float damageAmount = 0f;

                    AttackComponent mobAttack = monster.attackComponent;
                    ConditionComponent mobCondition = monster.conditionComponent;
                    damageAmount
                            = ( mobAttack.attackDamage + mobCondition.attackDamageBonus ) * mobCondition.attackDamageRate;


                    /* 타겟에게 줄 데미지 객체를 생성한다 */
                    DamageHistory newDamage;

                    /* 타겟을 찾고, 타겟의 데미지 목록에 넣어준다 */
                    switch ( targetType ){
                        case EntityType.CharacterEntity:
                            damageAmount -= worldMap.characterEntity.get(targetID).defenseComponent.defense;
                            newDamage = new DamageHistory(monster.entityID, true, damageAmount);
                            worldMap.characterEntity.get(targetID).hpHistoryComponent.hpHistory.add(newDamage);
                            break;
                        case EntityType.BarricadeEntity:
                            damageAmount -= worldMap.barricadeEntity.get(targetID).defenseComponent.defense;
                            newDamage = new DamageHistory(monster.entityID, true, damageAmount);
                            worldMap.barricadeEntity.get(targetID).hpHistoryComponent.hpHistory.add(newDamage);;
                            break;
                        case EntityType.AttackTurretEntity:
                            damageAmount -= worldMap.attackTurretEntity.get(targetID).defenseComponent.defense;
                            newDamage = new DamageHistory(monster.entityID, true, damageAmount);
                            worldMap.attackTurretEntity.get(targetID).hpHistoryComponent.hpHistory.add(newDamage);;
                            break;
                        case EntityType.BuffTurretEntity:
                            damageAmount -= worldMap.buffTurretEntity.get(targetID).defenseComponent.defense;
                            newDamage = new DamageHistory(monster.entityID, true, damageAmount);
                            worldMap.buffTurretEntity.get(targetID).hpHistoryComponent.hpHistory.add(newDamage);;
                            break;
                        case EntityType.CrystalEntity:
                            damageAmount -= worldMap.crystalEntity.get(targetID).defenseComponent.defense;
                            newDamage = new DamageHistory(monster.entityID, true, damageAmount);
                            worldMap.crystalEntity.get(targetID).hpHistoryComponent.hpHistory.add(newDamage);;
                            break;

                        default:
                            break;
                    }


                    /* 공격 쿨타임을 초기화한다 */
                    monster.attackComponent.remainCoolTime = 1 / monster.attackComponent.attackSpeed;

                    /* 현 몹에 대한 처리를 종료하고, 다음 몹으로 넘어간다 */
                    continue;

                } else{ /* 공격하지 않는다고 판정된 경우 */

                    /** 타겟을 쫒아가기로 결정 */
                    decidedToMove = true;

                }

            } else {

                /** 목적지를 크리스탈로 둔다 */

                targetID = monster.monsterComponent.targetID;
                targetType = EntityType.CrystalEntity;

                targetPosition = worldMap.crystalEntity.get(targetID).positionComponent.crystalTargetPosition;

                decidedToMove = true;

            }

            /* 공격 쿨타임을 업데이트한다 */
            if(monster.attackComponent.remainCoolTime > 0){
                monster.attackComponent.remainCoolTime -= deltaTime;
            }


            /** 3. 이동 처리 */
            if (decidedToMove && isTargetWithinRange == false){

                /** 이동 가능 여부를 판별한다 */
                boolean ableToMove = !(monster.conditionComponent.isDisableMove);
                if (ableToMove){

                    /** 타겟을 향해 이동 처리를 한다 */

                    float mobMoveSpeed = monster.velocityComponent.moveSpeed;
                    float moveSpeedBonus = monster.conditionComponent.moveSpeedBonus;
                    float moveSpeedRate = monster.conditionComponent.moveSpeedRate;

                    Vector3 mobSourcePos = monster.positionComponent.position;

                    Vector3 direction = Vector3.normalizeVector(monster.positionComponent.position, targetPosition);

                    float movementSpeed = deltaTime * ( mobMoveSpeed + moveSpeedBonus ) * moveSpeedRate;

                    Vector3 moveVector = direction.setSpeed(movementSpeed);

                    mobSourcePos.movePosition(mobSourcePos, moveVector);

                    monster.velocityComponent.velocity = moveVector;
                }
            }
        }
    }
}
