package ECS.System;

import ECS.Classes.BuffAction;
import ECS.Classes.ConditionBoolParam;
import ECS.Classes.ConditionFloatParam;
import ECS.Classes.DamageHistory;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Vector3;
import ECS.Components.AttackComponent;
import ECS.Components.ConditionComponent;
import ECS.Entity.*;
import ECS.Game.WorldMap;
import RMI.RMI_Classes.RMI_Context;
import RMI.RMI_Classes.RMI_ID;
import RMI.RMI_Common._RMI_ParsingClasses.EntityType;
import RMI.RMI_Common.server_to_client;

import java.util.ArrayList;
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

                /*System.out.println("몬스터와 캐릭터의 거리 : " + currentTargetDistance);*/

                /* 인식 대상에 들어갈 수 있는지 판별 */
                boolean isMinDistance = (currentTargetDistance < minDistance) ? true : false;
                boolean isAttackable
                        = ( (!character.conditionComponent.isUnTargetable)
                        && (character.hpComponent.currentHP > 0) ) ;

                boolean isRecognizable = (isMinDistance && isAttackable) ? true : false;


                /*System.out.println("최소거리인가? : " + isMinDistance);

                System.out.println("캐릭터가 공격가능한가? : " + isAttackable);

                System.out.println("캐릭터의 현재 체력 : " + character.hpComponent.currentHP);

                System.out.println("인식가능 여부 : " + isRecognizable);*/



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

                System.out.println("버프터렛 인지는 하나?? : " + buffTurret.entityID);

                /* 대상과의 거리를 계산한다 */
                currentTargetDistance
                        = Vector3.distance(monster.positionComponent.position, buffTurret.positionComponent.position);

                /* 인식 대상에 들어갈 수 있는지 판별 */
                boolean isMinDistance = (currentTargetDistance < minDistance) ? true : false;
                boolean isAttackable
                        = ( (!buffTurret.conditionComponent.isUnTargetable)
                        && (buffTurret.hpComponent.currentHP > 0) ) ;

                System.out.println("버프터렛이 최소거리인가? : " + isMinDistance);
                System.out.println("버프터렛 공격가능한가?? : " + isAttackable);

                boolean isRecognizable = (isMinDistance && isAttackable) ? true : false;

                System.out.println("버프터렛 인지대상인가?? : " + isRecognizable);

                /* 타겟 업데이트 */
                if(isRecognizable){

                    minDistance = currentTargetDistance;
                    targetID = buffTurret.entityID;
                    targetType = EntityType.BuffTurretEntity; // 버프 터렛이다
                    targetPosition = buffTurret.positionComponent.position;
                }

            }

            // 미친 크리스탈 있었네... 어차피 하나밖에 없긴하지만. 깔맞춤. 아니면, 얘는 별도로 비교 진행하던지. 보통 우선순위가 젤 높으니까??
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
                    //targetPosition = crystal.positionComponent.position;
                    targetPosition = newTarget;


                    /*//임시 변수.
                    Vector3 newTarget = new Vector3();
                    newTarget.set(targetPosition.x(),0f,targetPosition.z());
                    targetPosition = newTarget;*/


                    /*//임시 변수.
                    Vector3 direction = Vector3.normalizeVector(crystal.positionComponent.position, monster.positionComponent.position);
                    direction.setSpeed(5f);

                    //크리스탈의 반경만큼 떨어진 위치를 Target으로 잡게끔 할것.
                    Vector3 calculatedPosition = (Vector3)crystal.positionComponent.position.clone();
                    direction.movePosition(calculatedPosition, direction);
                    targetPosition = calculatedPosition;

                    Vector3 newTarget = new Vector3();
                    newTarget.set(calculatedPosition.x(),0f,calculatedPosition.z());
                    targetPosition = newTarget;*/

                }

            }


            /* 타겟 지정 여부 */
            targetHasSelected = (targetID >= 0) ? true : false;


            /*System.out.println("타겟지정 여부 : " + targetHasSelected);*/


            /** 2. 타겟 판정에 따른 처리 */
            boolean decidedToMove = false;
            boolean decidedToAttack = false;
            // boolean decidedToDoNothing;


            boolean isTargetWithinRange = (minDistance <= monster.attackComponent.attackRange) ? true : false;
            boolean ableToAttack = !(monster.conditionComponent.isDisableAttack);
            boolean isRemainCoolTimeZero = ( monster.attackComponent.remainCoolTime <= 0) ? true : false;
            if(targetHasSelected){

                /** 타겟에 대한 행동을 판정한다 ; 쫒아감 & 공격 */

                // 원래 변수들 선언 위치가 여기였는데. 아직 나는 원인을 잘 파악하지 못했지만, 공격하면서 이동하는? 문제 때문에. 위로 올림.
                // 나중에 제대로 원인 파악하고나서 정리할 것.
                /*boolean isTargetWithinRange = (minDistance <= monster.attackComponent.attackRange) ? true : false;
                //boolean isTargetWithinAttackableRange = (minDistance <= monster.attackComponent.attackRange * 2) ? true : false;
                boolean ableToAttack = !(monster.conditionComponent.isDisableAttack);
                boolean isRemainCoolTimeZero = ( monster.attackComponent.remainCoolTime <= 0) ? true : false;*/

                System.out.println("지정된 타겟이 공격범위 내에 있는가?? : " + isTargetWithinRange);

                System.out.println("몬스터가 공격 가능한 상태인가?? : " + ableToAttack);

                System.out.println("몬스터의 공격 쿨타임이 다 되었는가?? : " + isRemainCoolTimeZero);
                System.out.println("몬스터의 공격 쿨타임이 다 되었는가?? : " + monster.attackComponent.remainCoolTime);

                decidedToAttack
                        = (isTargetWithinRange && ableToAttack && isRemainCoolTimeZero) ? true : false;


                System.out.println("몬스터가 타겟을 공격하기로 했는가? : " + decidedToAttack);

                if(decidedToAttack){

                    server_to_client.motionMonsterDoAttack(
                            RMI_ID.getArray(worldMap.worldMapRMI_IDList.values()), RMI_Context.Reliable_Public_AES128, monster.entityID, (short)targetType, targetID);

                    /** 타겟에게 공격 처리를 한다 */

                    System.out.println("타겟에게 공격 처리를 한다.!! ");

                    /* 입힐 데미지를 계산한다 */
                    float damageAmount = 0f;

                    AttackComponent mobAttack = monster.attackComponent;
                    ConditionComponent mobCondition = monster.conditionComponent;
                    damageAmount
                            = ( mobAttack.attackDamage + mobCondition.attackDamageBonus ) * mobCondition.attackDamageRate;


                   /* *//*****  몹의 공격이 적용되는 동안, 몹이 움직이지 않도록 함. 이동불가능 버프를 몬스터에게 추가  *//*
                    BuffAction mobAttackDelay = new BuffAction();
                    mobAttackDelay.unitID = monster.entityID;
                    mobAttackDelay.coolTime = 1/monster.attackComponent.attackSpeed;
                    mobAttackDelay.remainTime = deltaTime;
                    mobAttackDelay.remainCoolTime = 0;
                    mobAttackDelay.boolParam = new ArrayList<>();
                    mobAttackDelay.floatParam = new ArrayList<>();
                    mobAttackDelay.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));

                    monster.buffActionHistoryComponent.conditionHistory.add(mobAttackDelay);


                    *//*****  몹의 공격판단 이후, 실제 데미지가 들어가기까지 딜레이를 조금 준다. 데미지 버프를 공격 대상에게 추가  *//*
                    BuffAction delayedDamage = new BuffAction();
                    delayedDamage.unitID = monster.entityID;
                    delayedDamage.coolTime = deltaTime;
                    delayedDamage.remainTime = deltaTime;
                    delayedDamage.remainCoolTime = deltaTime;
                    delayedDamage.boolParam = new ArrayList<>();
                    delayedDamage.floatParam = new ArrayList<>();
                    delayedDamage.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, damageAmount));

                    *//******** 타입에 맞춰서..캐릭터 예시 *//*
                    worldMap.crystalEntity.get(targetID).buffActionHistoryComponent.conditionHistory.add(delayedDamage);

*/
                    /* 타겟에게 줄 데미지 객체를 생성한다 */
                    DamageHistory newDamage;

                    System.out.println("타겟 대상 타입 : " + targetType);

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

                    System.out.println("공격 쿨타임 :" + monster.attackComponent.remainCoolTime);

                    /* 현 몹에 대한 처리를 종료하고, 다음 몹으로 넘어간다 */
                    continue;

                } else{ /* 공격하지 않는다고 판정된 경우 */

                    /** 타겟을 쫒아가기로 결정 */
                    decidedToMove = true;

                    // ..
                    /*System.out.println(monster.monsterComponent.monsterName + monster.entityID  + "가 타겟을 쫒아가기로 결정했습니다. ");*/
                }

            } else {

                /** 목적지를 크리스탈로 둔다 */

                /*System.out.println(monster.monsterComponent.monsterName + monster.entityID  + "의 목적지는 크리스탈입니다.");*/

                targetID = monster.monsterComponent.targetID;
                targetType = EntityType.CrystalEntity;

                targetPosition = worldMap.crystalEntity.get(targetID).positionComponent.crystalTargetPosition;

               // targetPosition = new Vector3( -70f , 0f, -36f);

                decidedToMove = true;

            }

            /* 공격 쿨타임을 업데이트한다 */
            if(monster.attackComponent.remainCoolTime > 0){
                monster.attackComponent.remainCoolTime -= deltaTime;
            }


            /** 3. 이동 처리 */
            if (decidedToMove && isTargetWithinRange == false){
            //if (decidedToMove){

                /** 이동 가능 여부를 판별한다 */
                boolean ableToMove = !(monster.conditionComponent.isDisableMove);
                if (ableToMove){


                    /** 타겟을 향해 이동 처리를 한다 */

                    float mobMoveSpeed = monster.velocityComponent.moveSpeed;
                    float moveSpeedBonus = monster.conditionComponent.moveSpeedBonus;
                    float moveSpeedRate = monster.conditionComponent.moveSpeedRate;

                    Vector3 mobSourcePos = monster.positionComponent.position;

                    // ..
                    /*System.out.println(monster.monsterComponent.monsterName + monster.entityID + "의 현재 위치 : "
                            + mobSourcePos.x() + "," + mobSourcePos.y() + "," + mobSourcePos.z());*/

                    Vector3 direction = Vector3.normalizeVector(monster.positionComponent.position, targetPosition);

                    /* 방향 확인용 로그 */
                    /*System.out.println(monster.monsterComponent.monsterName + monster.entityID + "의 이번 이동 방향 : "
                            + direction.x() + "," + direction.y() + "," + direction.z());*/


                    float movementSpeed = deltaTime * ( mobMoveSpeed + moveSpeedBonus ) * moveSpeedRate;

                    Vector3 moveVector = direction.setSpeed(movementSpeed);

                    mobSourcePos.movePosition(mobSourcePos, moveVector);

                    monster.velocityComponent.velocity = moveVector;

                    // ..
                    /*System.out.println(monster.monsterComponent.monsterName + monster.entityID + "의 다음 이동 위치 : "
                            + mobSourcePos.x() + "," + mobSourcePos.y() + "," + mobSourcePos.z());*/


                    /* 몹의 타겟 지정 */
                    // monster.monsterComponent.targetID = targetID;

                }

            }

        }

    }



}
