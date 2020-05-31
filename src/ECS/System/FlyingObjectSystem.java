package ECS.System;

import ECS.Factory.AttackTurretFactory;
import ECS.Factory.MapFactory;
import ECS.Factory.SkillFactory;
import Network.AutoCreatedClass.CharacterData;
import ECS.Classes.*;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Type.SkillType;
import ECS.Components.*;
import ECS.Entity.*;
import ECS.Game.*;
import Network.RMI_Common.RMI_ParsingClasses.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * 업뎃날짜 : 2020 01 31 금 권령희
 * 업뎃내용 :
 *      충돌 시 처리 ; 데미지 -> 버프로 변경해줌
 *
 *
 *
 * FlyingObjectSystem.
 *
 * WorldMap의 FlyingObjectEntity 목록에 존재하는 FlyingObject들의 로직처리를 행하는 클래스.
 * FlyingObject의 이동처리나, 충돌시 처리등이 이루어진다.
 *
 * 타게팅, 논타게팅 연산
 *
 */

public class FlyingObjectSystem {

    WorldMap worldMap;

    public FlyingObjectSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime) {

        boolean doOldVersion = false;

        /* 투사체 갯수만큼 반복한다 */
        if(worldMap.flyingObjectEntity.size() > 0){
            System.out.println("투사체 갯수 : " + worldMap.flyingObjectEntity.size());

        }
        for (HashMap.Entry<Integer, FlyingObjectEntity> flyingObjectEntity : worldMap.flyingObjectEntity.entrySet()) {

            FlyingObjectEntity flyingObject = flyingObjectEntity.getValue();
            FlyingObjectComponent flyingObjectComponent = flyingObject.flyingObjectComponent;


            /** 2020 04 02 */
            // 스킬 시전자 및 스킬 레벨 정보를 구한다
            CharacterEntity skillUser;
            AttackTurretEntity attackTurret;

            SkillSlot skillSlot;
            int skillLevel;
            int skillType;

            boolean turretNotExist = !(worldMap.entityMappingList.containsKey(flyingObjectComponent.userEntityID));
            if(turretNotExist){

                worldMap.requestDeleteQueue.add(flyingObject);
                continue;
            }

            int createdEntityType = worldMap.entityMappingList.get(flyingObjectComponent.userEntityID);
            switch (createdEntityType){

                case EntityType.CharacterEntity :

                    attackTurret = null;
                    skillUser = worldMap.characterEntity.get(flyingObjectComponent.userEntityID);
                    skillType = flyingObjectComponent.createdSkillType;

                    if((skillType == SkillType.ARCHER_NORMAL_ATTACK)
                            || (skillType == SkillType.MAGICIAN_NORMAL_ATTACK) ){

                        skillSlot = null;
                        skillLevel = 1;
                    }
                    else{

                        skillSlot = SkillFactory.findSkillSlotBySkillType(worldMap, skillUser, flyingObjectComponent.createdSkillType);
                        skillLevel = skillSlot.skillLevel;

                    }
                    break;

                case EntityType.AttackTurretEntity :

                    skillUser = null;
                    skillType = flyingObjectComponent.createdSkillType;

                    if(turretNotExist){
                        attackTurret  = null;
                        //System.out.println("투사체를 생성한 포탑이 파괴되었습니다.");
                    }
                    else{
                        attackTurret = worldMap.attackTurretEntity.get(flyingObjectComponent.userEntityID);
                    }

                    skillSlot = null;
                    skillLevel = 1;

                    break;

                default:

                    skillUser = null;
                    skillType = flyingObjectComponent.createdSkillType;
                    attackTurret = null;
                    skillSlot = null;
                    skillLevel = 1;

                    break;


            }



            /************************************************************************************************************/


            boolean hasTarget = false;

            /* 투사체 타입을 판별한다 : 타게팅, 논타게팅 */
            if(flyingObjectComponent.targetEntityID > 0){

                // 0보다 크면 월드 내 생성되어있는 객체가 타겟으로 지정된 것이라고 판단한다
                hasTarget = true;
            }
            //한 월드맵당 EntityID는 0 이상으로 주어지며, 모든 Entity마다 고유하게 부여된다. (최소 0 ~ 최대 21억 범위)

            /* 타겟이 존재하는 타게팅 투사체의 경우 타겟ID에 따라 처리한다 */
            if (hasTarget) {  /* 타게팅인 경우 */

                int targetEntityID = flyingObjectComponent.targetEntityID;

                short targetEntityType;

                if (!worldMap.entityMappingList.containsKey(targetEntityID)) {
//                    System.out.println("투사체의 타겟이 존재하지 않음. 투사체 파괴");
                    worldMap.requestDeleteQueue.add(flyingObject);
                    return;

                    /**
                     * 위에.. 리턴이 아니라 break 라던가 continue 해야하는 거 아님?? 일단 주석만 달아놓고
                     * 내일 테스트하자.
                     */

                }
                else
                    targetEntityType = worldMap.entityMappingList.get(targetEntityID);


                //타겟의 EntityType별로 지정할 것.
                switch (targetEntityType)
                {
                    /**
                     * 왜.. 타겟별로 별도 처리를 해야하는지 아직 모르겠지만,
                     * 일단 현 시점에서 타게팅 투사체 중 캐릭터를 타겟으로 하는 경우는
                     * 존재하지 않으므로, 처리를 수정하지 않음.
                     */
                    case EntityType.CharacterEntity: {

                        CharacterEntity targetEntity = worldMap.characterEntity.get(targetEntityID);

                        float speed = flyingObjectComponent.flyingSpeed;

                        float movedDeltaDistance = speed * deltaTime;

                        Vector3 targetPos = targetEntity.positionComponent.position;

                        targetPos.set(targetPos.x(), 1f, targetPos.z());

                        float distance = Vector3.distance(flyingObject.positionComponent.position, targetPos);

                        //만약 남은 거리가, 이동해야할 거리보다 적은 경우.
                        if(distance <= (movedDeltaDistance * 1.5))
                        {
                            //투사체를 파괴하고, 해당 타겟의 DamageHistory에, FlyingObject에 설정된 효과를 부여한다.

                            /*targetEntity.hpHistoryComponent.hpHistory.add(
                                    new DamageHistory(flyingObjectComponent.userEntityID, true, 50f) );*/


                            /** 2020 01 31 추가, 수정 */
                            /* 데미지 처리 */
                            BuffAction damage = new BuffAction();
                            damage.unitID = flyingObject.entityID;
                            damage.skillUserID = flyingObjectComponent.userEntityID;
                            damage.remainTime = 0.15f;
                            damage.coolTime = -1f;
                            damage.remainCoolTime = -1f;
                            damage.floatParam.add( new ConditionFloatParam(ConditionType.damageAmount, 50f) );
                            targetEntity.buffActionHistoryComponent.conditionHistory.add(damage);


                            //삭제요청Queue에 FlyingObjectEntity를 삽입하여, 삭제요청을 한다.
                            worldMap.requestDeleteQueue.add(flyingObject);
                        }
                        //타겟의 위치를 향하여 이동한다.
                        else
                        {
                            //현재 투사체의 위치와 타겟의 위치를 향하는 크기가 1인 방향 벡터.
                            Vector3 direction = Vector3.normalizeVector(flyingObject.positionComponent.position, targetPos);

                            Vector3 movement = direction.setSpeed(movedDeltaDistance);

                            //position + movement = newposition

                            //이전위치에서 movement만큼 이동시킨 위치를 현재 위치에 반영한다.
                            Vector3.movePosition(flyingObject.positionComponent.position, movement);

                            //투사체의 타겟이 있는 방향으로 지정!
                            flyingObject.flyingObjectComponent.direction = direction;
                        }
                        break;
                    }
                    case EntityType.MonsterEntity: {

                        MonsterEntity targetEntity = worldMap.monsterEntity.get(targetEntityID);

                        float speed = flyingObjectComponent.flyingSpeed;

                        float movedDeltaDistance = speed * deltaTime;

                        Vector3 targetPos = (Vector3)targetEntity.positionComponent.position.clone();
                        targetPos.set(targetPos.x(), 1.5f, targetPos.z());

                        float distance = Vector3.distance(flyingObject.positionComponent.position, targetPos);

                        //만약 남은 거리가, 이동해야할 거리보다 적은 경우.
                        if(distance <= movedDeltaDistance * 2)
                        {
                            BuffAction buff = new BuffAction();

                            /**
                             * 투사체 생성자가 공격터렛인 경우.
                             *
                             * 2020 04 28 수정 사항
                             *  -- 포탑이 생성한 투사체가 타겟에 도달하여 적중 처리를 할 때
                             *      생성한 포탑 Entity 의 정보를 참조하게 되는데,
                             *      이 시점에 포탑이 파괴되어 Entity 가 존재하지 않는 경우를 판별하여
                             *      적중 및 데미지 생성 처리를 생략, 바로 삭제되도록 한다.
                             *
                             *  -- 삭제된 포탑 앤티티에 대한 최초 접근 시점이 여기라고 생각했는데,
                             *      위에서 투사체 생성자 타입을 검사하면서 월드맵의 entityList 를 참조하여
                             *      attackTurret 변수에 대입하고 있다.
                             *
                             *      그래서 그 부분을
                             *          매핑리스트에 존재한다면(containsKey) 참조하여 집어넣고,
                             *          존재하지 않는다면 null 을 넣어주도록 변경함.
                             *
                             *      그리고 아래에서는 attackTurret이 null 이 아닌 경우에 한해
                             *          데미지를 생성하여 처리하도록 함.
                             *
                             */
                            if(createdEntityType == EntityType.AttackTurretEntity){

                                /* 존재하는 포탑인지? 여부에 따른 처리를 수행함 */
                                if(attackTurret != null){

                                    int turretType = attackTurret.turretComponent.turretType;

                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(
                                            AttackTurretFactory.createAttackTurretEffect(
                                                    turretType, "데미지", attackTurret, attackTurret.entityID));
                                }
                                else{
                                    //System.out.println("투사체를 생성한 포탑이 파괴되어, 해당 투사체의 데미지 처리를 하지 않습니다.");
                                }

                            }


                            /** 마법사 아이스볼의 경우 추가 처리
                             *  법사 아이스볼 투사체에 설정된 버프효과는 슬로우(일정시간 이동속도 느려짐)이다.
                             *  현재 시스템으로는 하나의 투사체/장판에 대해, 서로 다른 지속시간이타 쿨타임을 갖는 여러 버프를 동시에 넣어주는 게 불가능해서..
                             *
                             *  설정된 버프액션의 floatParam 리스트에서 첫 번째 항목에 대미지 값을 넣어놨다.
                             *  충돌 처리 시, 이 값을 먼저 확인 후 이걸로 대미지를 만들어 넣어주고, floatParam 목록에서 얘를 삭제한다.
                             *  그 후 남는 버프효과를 대상의 버프 목록에 넣어주는 식으로 처리함 우선.
                             *
                             */
                            else if(flyingObject.flyingObjectComponent.createdSkillType == SkillType.MAGICIAN_ICEBALL){

                                if(doOldVersion){  /** 2020 02 07 */

                                    /* 데미지 처리 */
                                    ConditionFloatParam damageParam = (ConditionFloatParam) buff.floatParam.get(0).clone();
                                    BuffAction damageBuff = SkillFactory.createDamageBuff(damageParam, buff.unitID, buff.skillUserID);
                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(damageBuff);

                                    /* 상태이상 처리 - 슬로우 */
                                    buff.floatParam.remove(0);  // 기존 floatParam 0번에 데미지, 1번에 슬로우 있음.
                                    // 일단 맞았을 때 잠깐 이동불가, 공격불가 하는거는 제외했음..
                                    // 혹시 필요하다면 아래 버프 추가.
                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(buff);

                                    /* *************************************************************************************/

                                }
                                else{

                                    /** 2020 04 02 ver */

                                    // 아이스볼 데미지 효과를 넣어준다.
                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(
                                            SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, flyingObject.entityID));

                                    // 아이스볼 슬로우 효과를 넣어준다
                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(
                                            SkillFactory.createSkillEffect(skillType, "슬로우", skillLevel, skillUser, flyingObject.entityID));

                                }

                            }
                            /**
                             * 궁수 헤드샷의 경우 처리
                             *  ㄴ 효과 적용이 조금 복잡해서..
                             *
                             *  1. 충돌 즉시, 버프의 floatParam의 첫 번째 항목에 들어있는 평타 대미지를 즉시 적용한다
                             *  2. floatParam의 두 번째 항목인 크리티컬 대미지를 적용한다.
                             *          적용 시점은 0.1초 뒤가 되도록 한다.
                             *  3. boolParam에 들어있는 이동불가, 공격불가 등 상태이상 디버프를 적용한다.
                             *          적용 시점은 즉시,  0.3초동안 "지속"되도록 한다.
                             *
                             */
                            else if(flyingObject.flyingObjectComponent.createdSkillType == SkillType.ARCHER_HEAD_SHOT){

                                if(doOldVersion){

                                    //크리티컬댐 적용하기
                                    ConditionFloatParam criDamParam = buff.floatParam.get(0);
                                    BuffAction criticalDamBuff = SkillFactory.createDamageBuff(criDamParam, buff.unitID, buff.skillUserID);
                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(criticalDamBuff);

                                    //상태댐 적용하기
                                    buff.floatParam.clear();
                                    BuffAction condBuff = buff;
                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(condBuff);

                                    /** 기존에 .. 시전자에게 들어있던 버프 제거해주기 */

                                    //SkillFactory.cancelDeBuffEffect(skillUser, ConditionType.isArcherHeadShotActivated);

                                    SkillFactory.cancelSkillBuffEffect(skillUser, SkillType.ARCHER_HEAD_SHOT);

    //                                System.out.println("헤드샷 버프 삭제함");

                                }
                                else{

                                    /** 2020 04 02 ver */

                                    // 크리댐 적용하기
                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(
                                            SkillFactory.createSkillEffect(skillType, "크리뎀", skillLevel, skillUser, flyingObject.entityID));

                                    // 시전자의 헤드샷활성화 버프 제거
                                    /**
                                     * 2020 05 31 >> 헤드샷의 평타강화 처리 취소, 즉발 투사체 처리로 수정함으로 인한 주석 처리.
                                     */
                                    //SkillFactory.cancelSkillBuffEffect(skillUser, SkillType.ARCHER_HEAD_SHOT);

                                }


                            }
                            else {  // 그냥 버프를 그대로 넣어주면 되는 경우.

                                if(doOldVersion){

                                    /**
                                     * 2020 02 06
                                     * buff ; 현 투사체의 buffAction을 복사한 값.
                                     */

                                    /* 데미지 버프 적용 */
                                    BuffAction damageBuff = SkillFactory.createDamageBuff(buff.floatParam.get(0), buff.unitID, buff.skillUserID);
                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(damageBuff);

                                    /* 상태 버프 적용 */
                                    buff.floatParam.clear();// 위의 데미지 효과를 지우고.. // 설마. 클리어 했다고 아예 사라지는건 아니겠지??
                                    BuffAction condBuff = buff; // 먼가 쓸모없는 선언이긴 한데, 나중에 buff가 뭔 버프지.. 하고 헷갈릴 일 없게.
                                    targetEntity.buffActionHistoryComponent.conditionHistory.add(condBuff);

                                    /***************************************************************************************/
                                }
                                else{

                                    /** 2020 04 02 */

                                    // 그 외 모든 타게팅 투사체 충돌 데미지 처리들.

                                    switch (createdEntityType){

                                        case EntityType.CharacterEntity :
                                            targetEntity.buffActionHistoryComponent.conditionHistory.add(
                                                    SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, flyingObject.entityID));

                                            break;

                                        /*case EntityType.AttackTurretEntity :

                                            System.out.println("공격자가 공격터렛이넹~ : " + attackTurret.entityID);

                                            targetEntity.buffActionHistoryComponent.conditionHistory.add(
                                                    AttackTurretFactory.createAttackTurretEffect(attackTurret.turretComponent.turretType, "데미지", attackTurret, attackTurret.entityID));


                                            break;*/
                                    }


                                }

                            }

                            //삭제요청Queue에 FlyingObjectEntity를 삽입하여, 삭제요청을 한다.
                            worldMap.requestDeleteQueue.add(flyingObject);

                        }
                        //타겟의 위치를 향하여 이동한다.
                        else {

                            //현재 투사체의 위치와 타겟의 위치를 향하는 크기가 1인 방향 벡터.
                            Vector3 direction = Vector3.normalizeVector(flyingObject.positionComponent.position, targetPos);

                            Vector3 movement = direction.setSpeed(movedDeltaDistance);

                            //이전위치에서 movement만큼 이동시킨 위치를 현재 위치에 반영한다.
                            Vector3.movePosition(flyingObject.positionComponent.position, movement);


                            //투사체의 타겟이 있는 방향으로 지정!
                            flyingObject.flyingObjectComponent.direction = direction;
                        }
                        break;
                    }
                    case EntityType.AttackTurretEntity: {

                        AttackTurretEntity targetEntity = worldMap.attackTurretEntity.get(targetEntityID);

                        float speed = flyingObjectComponent.flyingSpeed;

                        float movedDeltaDistance = speed * deltaTime;

                        Vector3 targetPos = targetEntity.positionComponent.position;
                        //targetPos.set(targetPos.x(), targetPos.y()+1.6f, targetPos.z());
                        float distance = Vector3.distance(flyingObject.positionComponent.position, targetPos);

                        //만약 남은 거리가, 이동해야할 거리보다 적은 경우.
                        if(distance <= movedDeltaDistance * 2)
                        {

                            /** 2020 01 31 추가, 수정 */
                            /* 데미지 처리 */
                            BuffAction damage = new BuffAction();
                            damage.unitID = flyingObject.entityID;
                            damage.skillUserID = flyingObjectComponent.userEntityID;
                            damage.remainTime = 0.15f;
                            damage.coolTime = -1f;
                            damage.remainCoolTime = -1f;
                            damage.floatParam.add( new ConditionFloatParam(ConditionType.damageAmount, 50f) );
                            targetEntity.buffActionHistoryComponent.conditionHistory.add(damage);

                            //삭제요청Queue에 FlyingObjectEntity를 삽입하여, 삭제요청을 한다.
                            worldMap.requestDeleteQueue.add(flyingObject);
                        }
                        //타겟의 위치를 향하여 이동한다.
                        else
                        {
                            //현재 투사체의 위치와 타겟의 위치를 향하는 크기가 1인 방향 벡터.
                            Vector3 direction = Vector3.normalizeVector(flyingObject.positionComponent.position, targetPos);

                            Vector3 movement = direction.setSpeed(movedDeltaDistance);

                            //이전위치에서 movement만큼 이동시킨 위치를 현재 위치에 반영한다.
                            Vector3.movePosition(flyingObject.positionComponent.position, movement);

                            //투사체의 타겟이 있는 방향으로 지정!
                            flyingObject.flyingObjectComponent.direction = direction;
                        }
                        break;
                    }
                    case EntityType.BuffTurretEntity: {

                        BuffTurretEntity targetEntity = worldMap.buffTurretEntity.get(targetEntityID);

                        float speed = flyingObjectComponent.flyingSpeed;

                        float movedDeltaDistance = speed * deltaTime;

                        Vector3 targetPos = targetEntity.positionComponent.position;
                        //targetPos.set(targetPos.x(), targetPos.y()+1.6f, targetPos.z());
                        float distance = Vector3.distance(flyingObject.positionComponent.position, targetPos);

                        //만약 남은 거리가, 이동해야할 거리보다 적은 경우.
                        if(distance <= movedDeltaDistance * 2)
                        {

                            /** 2020 01 31 추가, 수정 */
                            /* 데미지 처리 */
                            BuffAction damage = new BuffAction();
                            damage.unitID = flyingObject.entityID;
                            damage.skillUserID = flyingObjectComponent.userEntityID;
                            damage.remainTime = 0.15f;
                            damage.coolTime = -1f;
                            damage.remainCoolTime = -1f;
                            damage.floatParam.add( new ConditionFloatParam(ConditionType.damageAmount, 50f) );
                            targetEntity.buffActionHistoryComponent.conditionHistory.add(damage);

                            //삭제요청Queue에 FlyingObjectEntity를 삽입하여, 삭제요청을 한다.
                            worldMap.requestDeleteQueue.add(flyingObject);
                        }
                        //타겟의 위치를 향하여 이동한다.
                        else
                        {
                            //현재 투사체의 위치와 타겟의 위치를 향하는 크기가 1인 방향 벡터.
                            Vector3 direction = Vector3.normalizeVector(flyingObject.positionComponent.position, targetPos);

                            Vector3 movement = direction.setSpeed(movedDeltaDistance);

                            //이전위치에서 movement만큼 이동시킨 위치를 현재 위치에 반영한다.
                            Vector3.movePosition(flyingObject.positionComponent.position, movement);

                            //투사체의 타겟이 있는 방향으로 지정!
                            flyingObject.flyingObjectComponent.direction = direction;
                        }
                        break;
                    }
                    case EntityType.BarricadeEntity: {

                        BarricadeEntity targetEntity = worldMap.barricadeEntity.get(targetEntityID);

                        float speed = flyingObjectComponent.flyingSpeed;

                        float movedDeltaDistance = speed * deltaTime;

                        Vector3 targetPos = targetEntity.positionComponent.position;
                        //targetPos.set(targetPos.x(), targetPos.y()+1.6f, targetPos.z());
                        float distance = Vector3.distance(flyingObject.positionComponent.position, targetPos);

                        //만약 남은 거리가, 이동해야할 거리보다 적은 경우.
                        if(distance <= movedDeltaDistance * 2)
                        {

                            /** 2020 01 31 추가, 수정 */
                            /* 데미지 처리 */
                            BuffAction damage = new BuffAction();
                            damage.unitID = flyingObject.entityID;
                            damage.skillUserID = flyingObjectComponent.userEntityID;
                            damage.remainTime = 0.15f;
                            damage.coolTime = -1f;
                            damage.remainCoolTime = -1f;
                            damage.floatParam.add( new ConditionFloatParam(ConditionType.damageAmount, 50f) );
                            targetEntity.buffActionHistoryComponent.conditionHistory.add(damage);

                            //삭제요청Queue에 FlyingObjectEntity를 삽입하여, 삭제요청을 한다.
                            worldMap.requestDeleteQueue.add(flyingObject);
                        }
                        //타겟의 위치를 향하여 이동한다.
                        else
                        {
                            //현재 투사체의 위치와 타겟의 위치를 향하는 크기가 1인 방향 벡터.
                            Vector3 direction = Vector3.normalizeVector(flyingObject.positionComponent.position, targetPos);

                            Vector3 movement = direction.setSpeed(movedDeltaDistance);

                            //이전위치에서 movement만큼 이동시킨 위치를 현재 위치에 반영한다.
                            Vector3.movePosition(flyingObject.positionComponent.position, movement);

                            //투사체의 타겟이 있는 방향으로 지정!
                            flyingObject.flyingObjectComponent.direction = direction;
                        }
                        break;
                    }
                    case EntityType.CrystalEntity: {

                        CrystalEntity targetEntity = worldMap.crystalEntity.get(targetEntityID);

                        float speed = flyingObjectComponent.flyingSpeed;

                        float movedDeltaDistance = speed * deltaTime;

                        float distance = Vector3.distance(targetEntity.positionComponent.position, flyingObject.positionComponent.position);

                        //만약 남은 거리가, 이동해야할 거리보다 적은 경우.
                        if(distance <= movedDeltaDistance * 2)
                        {

                            /** 2020 01 31 추가, 수정 */
                            /* 데미지 처리 */
                            BuffAction damage = new BuffAction();
                            damage.unitID = flyingObject.entityID;
                            damage.skillUserID = flyingObjectComponent.userEntityID;
                            damage.remainTime = 0.15f;
                            damage.coolTime = -1f;
                            damage.remainCoolTime = -1f;
                            damage.floatParam.add( new ConditionFloatParam(ConditionType.damageAmount, 50f) );
                            targetEntity.buffActionHistoryComponent.conditionHistory.add(damage);

                            //삭제요청Queue에 FlyingObjectEntity를 삽입하여, 삭제요청을 한다.
                            worldMap.requestDeleteQueue.add(flyingObject);
                        }
                        //타겟의 위치를 향하여 이동한다.
                        else
                        {
                            //현재 투사체의 위치와 타겟의 위치를 향하는 크기가 1인 방향 벡터.
                            Vector3 direction = Vector3.normalizeVector(flyingObject.positionComponent.position, targetEntity.positionComponent.position);

                            Vector3 movement = direction.setSpeed(movedDeltaDistance);

                            //이전위치에서 movement만큼 이동시킨 위치를 현재 위치에 반영한다.
                            Vector3.movePosition(flyingObject.positionComponent.position, movement);

                            //투사체의 타겟이 있는 방향으로 지정!
                            flyingObject.flyingObjectComponent.direction = direction;
                        }
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("FlyingObjectSystem 중 비정상적인 값 : "+targetEntityType);
                }

            }
            else {
                /** 논타게팅인 경우 */

                Vector3 flyingObjectPos = flyingObject.positionComponent.position;

                /* 목적지와의 거리를 계산한다 */
                //currentDistance = Vector3.distance(flyingObjectPos, destinationPos);

                /* 목적지 도달 여부를 판단한다 */
                boolean hasArrived = false;
                hasArrived = ( flyingObjectComponent.flyingObjectRemainDistance <= 0 ) ? true : false;

                if (hasArrived) { /* 목적지에 다다른 경우 */

                    /* 삭제 처리 한다 */
                    worldMap.requestDeleteQueue.add(flyingObject);

                    /** 찌르기 스킬의 경우 */
                    if(flyingObjectComponent.createdSkillType == SkillType.KNIGHT_PIERCE){

            //            System.out.println("찌르기 스킬의 삭제 처리 ");

                        CharacterEntity character = worldMap.characterEntity.get(flyingObject.flyingObjectComponent.userEntityID);

              //          System.out.println("캐릭터 ID : " + character.entityID );

                        ArrayList<BuffAction> buffActionList = character.buffActionHistoryComponent.conditionHistory;

                        /* 대상의 버프액션 갯수만큼 반복한다 */
                        for(int k=0; k<buffActionList.size(); k++){

                            if(flyingObject.entityID == buffActionList.get(k).unitID){

                                if(buffActionList.get(k).boolParam.size()>0){
                                    if(buffActionList.get(k).boolParam.get(0).type == ConditionType.isDisableMove){

                                        buffActionList.remove(k);
                                        break;
                                    }
                                }
                            }
                        }

                    }

                }
                else {  /* 목적지에 다다르지 않은 겨우 */

                    /* 다음 이동 위치 계산 후 반영한다 */

                    /* 다음 이동 좌표를 계산한다 */

                    // 다음 이동할 목적지(?) 좌표 구하기
                    Vector3 sourcePos = new Vector3(flyingObjectComponent.startPosition.x(),
                            flyingObjectComponent.startPosition.y(), flyingObjectComponent.startPosition.z());

                    //Vector3 destinationPos = new Vector3(sourcePos.x(), sourcePos.y(), sourcePos.z());
                    //destinationPos.setSpeed(flyingObjectComponent.flyingObjectRemainDistance);


                    // 방향을 구한다
                    //Vector3 direction = Vector3.normalizeVector(flyingObjectPos, destinationPos);
                    Vector3 direction = (Vector3) flyingObjectComponent.direction.clone();
                    direction = direction.normalize();

                    // 이동할 거리(?)를 구한다
                    //Vector3 거리 = direction * flyingObject.flyingObject.flyingSpeed * deltaTime;
                    float temp = flyingObjectComponent.flyingSpeed * deltaTime;

                    Vector3 moveVector = direction.setSpeed(temp);

                    /* 좌표를 반영한다 */
                    // 기존 위치 + 이동할 거리. ??
                    flyingObjectPos.movePosition(flyingObjectPos, moveVector);


                    /** 전사 찌르기 스킬 투사체의 경우 예외처리를 임시로 여기서 함 */
                    if(flyingObjectComponent.createdSkillType == SkillType.KNIGHT_PIERCE) {

                        if (false){ // 이전 버전 {

                            /* 스킬 시전자의 좌표를 투사체와 같이 해준다. */

                            CharacterEntity character = worldMap.characterEntity.get(flyingObject.flyingObjectComponent.userEntityID);

                            character.positionComponent.position.set((Vector3) flyingObjectPos.clone());

                            CharacterData characterData = worldMap.getCharacterDataFromEntity(character);

                        /*RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
                        server_to_client.broadcastingCharacterSnapshot(TARGET, RMI_Context.Reliable_Public_AES256, worldMap.worldEntityData.characterData);*/

                        }
                        else if(false){   /** 2002 02 13, 맵뚫기 보정 버전 */  /** 2020 02 19 주석처리 함 */

                            boolean isMovableTile = MapFactory.moveCheck(worldMap.gameMap, flyingObjectPos.x(), flyingObjectPos.z());
                            if(isMovableTile){

                                CharacterEntity character = worldMap.characterEntity.get(flyingObject.flyingObjectComponent.userEntityID);
                                character.positionComponent.position.set((Vector3) flyingObjectPos.clone());

                            }else {

                                flyingObjectComponent.flyingObjectRemainDistance = 0;

                            }

                        }
                        else{   /** 2020 02 19 수 권령희 추가 */

                            boolean isMovableTile = MapFactory.moveCheck(worldMap.gameMap, flyingObjectPos.x(), flyingObjectPos.z());
                            if(isMovableTile){

                                /* 투사체의 위치를, 월드 내 캐릭터 다음 위치 목록에 업데이트한다 */
                                CharacterEntity character = worldMap.characterEntity.get(flyingObject.flyingObjectComponent.userEntityID);
                                Vector3 changedPos = (Vector3) flyingObjectPos.clone();
                                worldMap.charNextPosList.put(character.entityID, changedPos);

                            }else {

                                flyingObjectComponent.flyingObjectRemainDistance = 0;

                            }
                        }

                    }

                    /** 공격 범위 내에서 가장 가까운 타겟을 찾는다 */

                    float minDistance = flyingObjectComponent.flyingObjectRadius;
                    int targetID = -1;
                    ArrayList<Integer> targetList = new ArrayList<>();
                    MonsterEntity target;   // 굳이..

                    /* 공격 대상(몬스터)의 갯수만큼 반복한다 */
                    for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()){

                        MonsterEntity monster = monsterEntity.getValue();

                        /* 자신과 대상의 거리를 계산한다 */
                        float currentDistance = 0f;

                        Vector3 objectPos = flyingObject.positionComponent.position;
                        Vector3 mobPos = monster.positionComponent.position;

                        currentDistance = Vector3.distance(objectPos, mobPos);



                        /* 대상이 공격 범위에 들어있는지 판별한다 */
                        boolean isInTargetRange = false;

                        // 살아있지 않은 녀석은 애초에 범위 내에 존재하지 않는걸로.
                        float targetHP = monster.hpComponent.currentHP;
                        if( (currentDistance < (flyingObjectComponent.flyingObjectRadius )) && (targetHP > 0) && !(monster.conditionComponent.isUnTargetable))
                        {
                            /* 2020 01 22 권령희 추가. */
                            /* 찌르기의 경우, 범위각도 (뒤에있는 놈은 공격하지 않도록..?? ) 체크하는 처리 추가,, */
                            if(flyingObjectComponent.createdSkillType == SkillType.KNIGHT_PIERCE){

                                // 투사체 방향 백터 구하기
                                Vector3 flyingDirection = flyingObjectComponent.direction;
                                Vector3 monsterDirection = Vector3.getTargetDirection(flyingObjectPos , monster.positionComponent.position);

                                float betweenAngle = Vector3.getAngle(flyingDirection, monsterDirection);

                                isInTargetRange = false;
                                if((betweenAngle <= 90f)){

                                    isInTargetRange = true;
                                }

                            }
                            else{
                                isInTargetRange = true;
                            }

                        }



                        /************************************************************************************************/


                        /* 범위에 들어있다면, 범위 대상 중 가장 가까운 녀석인지 판별한다 */
                        if(isInTargetRange){

  //                          System.out.println("몬스터" + monster.entityID + "를 타겟 목록에 추가합니다.");


                            /** 여기다 해줘서 될 지 모르겠네.. 이미 버프를 받고있는 애들의 경우는 제외하기 */
                            /////if(flyingObjectComponent.createdSkillType == SkillType.KNIGHT_PIERCE){
                            if(true){

                                List<BuffAction> buffActionList = monster.buffActionHistoryComponent.conditionHistory;
                                boolean targetHasBuffAlready = false;

                                //BuffAction targetsBuffAction = new BuffAction();
                                BuffAction targetsBuffAction;

                                /* 대상의 버프액션 갯수만큼 반복한다 */
                                for(int k=0; k<buffActionList.size(); k++){

                                    if(flyingObject.entityID == buffActionList.get(k).unitID){

                                        targetsBuffAction = buffActionList.get(k);

                                        targetHasBuffAlready = true;
                                        break;
                                    }
                                }

                                if(targetHasBuffAlready) { /** 대상이 이미 효과를 받고 있다면 */

                                    // buffInfo.remainTime = deltaTime;    // 버프 지속 남은 시간을 초기화해준다
                                    // targetsBuffAction.remainTime = deltaTime;

                                }
                                else{   /** 기존에 효과를 받고있지 않다면 */

                                    // /* 대상의 버프 목록에 추가해준다. */ << 무슨 생각이지..
                                    targetList.add(monster.entityID);
                                    if( currentDistance <minDistance ){

                                        minDistance = currentDistance;
                                        targetID = monster.entityID;
                                    }
                                }

                            }

                        }



                    } /* 공격 대상 갯수만큼 반복 종료 */


                    /** 지정된 타겟에 대해 공격 처리를 한다 */

                    /* 타겟이 존재한다면 */
                    if(targetID > 0){

                        if(flyingObjectComponent.beDestroyedByCrash){
                            // 한 번 충돌하고 죽는 애라면
                            // 충돌 처리 후 삭제

                            System.out.println("타겟 ID : " + targetID);
                            System.out.println("타겟 타입 : " + worldMap.entityMappingList.get(targetID));
                            target = worldMap.monsterEntity.get(targetID);

                            /** 테스트. 멀티샷에만 별도로 버프 추가 처리 해주기 */
                            if(flyingObjectComponent.createdSkillType == SkillType.ARCHER_MULTI_SHOT){

                                if(doOldVersion){

                                    BuffAction multiShotBuff = (BuffAction) flyingObject.flyingObjectComponent.buffAction.clone();

                                    // 대미지 버프?
                                    ConditionFloatParam damageParam = (ConditionFloatParam) multiShotBuff.floatParam.get(0).clone();
                                    BuffAction damageBuff = SkillFactory.createDamageBuff(damageParam, multiShotBuff.unitID, multiShotBuff.skillUserID);
                                    target.buffActionHistoryComponent.conditionHistory.add(damageBuff);

                                    // 상태 버프?
                                    multiShotBuff.floatParam.clear();
                                    target.buffActionHistoryComponent.conditionHistory.add(multiShotBuff);

                                }
                                else{

                                    /** 2020 04 02 ver */

                                    target.buffActionHistoryComponent.conditionHistory.add(
                                            SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, flyingObject.entityID));


                                }

                            }
                            /**
                             * 2020 01 29 수 저격 처리 추가 권령희
                             *
                             */
                            else if(flyingObjectComponent.createdSkillType == SkillType.ARCHER_SNIPE) {

                                if(doOldVersion){

                                    /** 데미지 처리 */

                                    BuffAction snipeBuff = (BuffAction) flyingObjectComponent.buffAction.clone();

                                    boolean isBoss = false; // 충돌 대상이 보스인지 여부를 체크한다. 나중에 변경
                                    if(isBoss){ /* 보스몹 */

                                        /** 2020 01 31 추가, 수정 */
                                        /* 1차 뎀 */
                                        BuffAction bossCriticalDam = new BuffAction();
                                        bossCriticalDam.unitID = flyingObject.entityID;
                                        bossCriticalDam.skillUserID = flyingObjectComponent.userEntityID;
                                        bossCriticalDam.remainTime = 0.15f;
                                        bossCriticalDam.coolTime = -1f;
                                        bossCriticalDam.remainCoolTime = -1f;
                                        bossCriticalDam.floatParam.add( (ConditionFloatParam) snipeBuff.floatParam.get(2).clone() );

                                        target.buffActionHistoryComponent.conditionHistory.add(bossCriticalDam);


                                        /* 2차 뎀 */
                                        BuffAction bossBonusDam = new BuffAction();
                                        bossBonusDam.unitID = snipeBuff.unitID;
                                        bossBonusDam.skillUserID = snipeBuff.skillUserID;
                                        bossBonusDam.remainCoolTime = 0.1f;
                                        bossBonusDam.remainTime = 0.15f;
                                        bossBonusDam.coolTime = 0.1f;

                                        bossBonusDam.floatParam.add( (ConditionFloatParam) snipeBuff.floatParam.get(3).clone() );

                                        target.buffActionHistoryComponent.conditionHistory.add(bossBonusDam);

                                    }
                                    else{   /* 일반 몹 */

                                        ConditionFloatParam damageParam = (ConditionFloatParam) snipeBuff.floatParam.get(0).clone();
                                        BuffAction criticalDam = SkillFactory.createDamageBuff(damageParam, snipeBuff.unitID, snipeBuff.skillUserID);

                                        target.buffActionHistoryComponent.conditionHistory.add(criticalDam);


                                        /* 2차 뎀 */
                                        ConditionFloatParam criticalBonusParam = (ConditionFloatParam) snipeBuff.floatParam.get(1).clone();
                                        BuffAction criticalBonusDam = SkillFactory.createDamageBuff(criticalBonusParam, snipeBuff.unitID, snipeBuff.skillUserID);
                                        criticalBonusDam.remainCoolTime = 0.1f;
                                        criticalBonusDam.remainTime = 0.15f;
                                        criticalBonusDam.coolTime = 0.1f;

                                        target.buffActionHistoryComponent.conditionHistory.add(criticalDam);

                                    }

                                    /** 상태이상 처리 */

                                    snipeBuff.floatParam.clear();   // 상태이상(bool)만 들어가게끔
                                    target.buffActionHistoryComponent.conditionHistory.add(snipeBuff);



                                }
                                else{

                                    /** 2020 04 02 ver */

                                    boolean isBoss = false; // 충돌 대상이 보스인지 여부를 체크한다. 나중에 변경
                                    if(isBoss){ /* 보스몹 */

                                        /* 1차 뎀 */
                                        target.buffActionHistoryComponent.conditionHistory.add(
                                                SkillFactory.createSkillEffect(skillType, "보스 1차데미지", skillLevel, skillUser, flyingObject.entityID));

                                        /* 2차 뎀 */
                                        target.buffActionHistoryComponent.conditionHistory.add(
                                                SkillFactory.createSkillEffect(skillType, "보스 2차데미지", skillLevel, skillUser, flyingObject.entityID));

                                    }
                                    else{   /* 일반 몹 */

                                        /* 1차 뎀 */
                                        target.buffActionHistoryComponent.conditionHistory.add(
                                                SkillFactory.createSkillEffect(skillType, "1차 데미지", skillLevel, skillUser,flyingObject.entityID));

                                        /* 2차 뎀 */
                                        target.buffActionHistoryComponent.conditionHistory.add(
                                                SkillFactory.createSkillEffect(skillType, "2차 데미지", skillLevel, skillUser, flyingObject.entityID));

                                    }

                                    // 상태. 추가할거라면 여기에.
                                }

                            }

                            else{

                                if(doOldVersion){

                                    /**
                                     * 2020 02 06
                                     * buff ; 현 투사체의 buffAction을 복사한 값.
                                     */

                                    BuffAction buff = (BuffAction) flyingObjectComponent.buffAction.clone();

                                    /* 데미지 버프 적용 */
                                    BuffAction damageBuff = SkillFactory.createDamageBuff(buff.floatParam.get(0), buff.unitID, buff.skillUserID);
                                    target.buffActionHistoryComponent.conditionHistory.add(damageBuff);

                                    /* 상태 버프 적용 */
                                    buff.floatParam.clear();// 위의 데미지 효과를 지우고.. // 설마. 클리어 했다고 아예 사라지는건 아니겠지??
                                    BuffAction condBuff = buff; // 먼가 쓸모없는 선언이긴 한데, 나중에 buff가 뭔 버프지.. 하고 헷갈릴 일 없게.
                                    target.buffActionHistoryComponent.conditionHistory.add(condBuff);

                                    /***************************************************************************************/

                                }
                                else{

                                    target.buffActionHistoryComponent.conditionHistory.add(
                                            SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, flyingObject.entityID));
                                }

                            }

                            flyingObjectComponent.flyingObjectRemainDistance = 0;

                        }
                        else{

                            // 모든 대상에 대해 반복
                            for(int i=0; i<targetList.size(); i++){

                                targetID = targetList.get(i);

                                target = worldMap.monsterEntity.get(targetID);

                                if(doOldVersion){

                                    BuffAction flyingObjBuff = flyingObjectComponent.buffAction;

                                    ConditionFloatParam damageParam = (ConditionFloatParam) flyingObjBuff.floatParam.get(0).clone();
                                    BuffAction damageBuff = SkillFactory.createDamageBuff(damageParam, flyingObjBuff.unitID, flyingObjBuff.skillUserID);
                                    target.buffActionHistoryComponent.conditionHistory.add(damageBuff);

                                }
                                else{

                                    /** 2020 04 02 ver */

                                    target.buffActionHistoryComponent.conditionHistory.add(
                                            SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, flyingObject.entityID));

                                }

                            }

                            flyingObjectComponent.flyingObjectRemainDistance -= moveVector.length();
                        }

                    }
                    else{

                        flyingObjectComponent.flyingObjectRemainDistance -= moveVector.length();
                    }

                }


            } //논타겟 스킬인 경우.

        } //Entitiy목록 반복 부분.

    }








    //시간에 따라서 움직이는 투사체 로직을 다루는 부분.
    //타게팅 투사체 & 논타게팅 투사체로 나뉘어짐.


    /*

    월드에 존재하는 투사체 Entity 목록을 불러옴.

    캐릭터 Entity
    몬스터
    포탑

    for( 투사체 목록 갯수만큼 반복 ){

        투사체 Entity를 가져옴
        투사체 컴포넌트의 createdSkillID; 값을 통해, 미리 CSV파일을 통해 불러왔던 데이터 모음집에서
        투사체Entity의 범위와 모양등의 속성을 불러온다.


        if( 투사체의 목적지가 없다면 ){   // 타게팅

            // 타겟과의 거리를 검사
            // if ( 타겟의 위치와 충분히 가까워지면 ){
                    공격처리를 한다
                        시전자와 타겟의 정보를 가져온다.
                        어느정도 데미지를 줄 것인지 계산
                        타겟의 HP히스토리에 삽입.

                        현재 시점에서 직접적으로  투사체 Entity 목록에서 삭제하지 않고,
                        투사체 Entity 삭제예정 List에 추가 할 것.

                        이후, 모든 로직이 끝나고 몰아서 삭제할 것. (이후, 클라이언트에게 삭제된 목록을 중계함)
                    (--i;)
                    continue;
               }

               else{     // 멀었다면
                    이동처리 후 반영
               }

        }
        else{   // 논타게팅

            자신의 현재위치와 목적지를 비교하여 거리를 비교한다.
            if( 목적지와 충분히 가까워졌다면 ){
                //목적지에 도달할 동안, 충돌하지 않았으므로 데미지등의 처리는 하지 않는다.

                현재 시점에서 직접적으로  투사체 Entity 목록에서 삭제하지 않고,
                투사체 Entity 삭제예정 List에 추가 할 것.

                이후, 모든 로직이 끝나고 몰아서 삭제할 것. (이후, 클라이언트에게 삭제된 목록을 중계함)
                (--i;)
                continue;
            }
            else {
                방향을 구하고,
                Vector3 이동할위치 = 방향.normalize (크기가1인 단위벡터) * 속도 * deltaTime;

                현재위치 = 현재위치 + 이동할위치; //계산된 위치를 현재위치에 반영한다.

                투사체Entity의 범위와 모양등의 속성에 따라서 달라짐.
                ( EX) 도중에 투사체를 지우는 스킬오브젝트가 있다면 충돌시 판별하여 지워짐)
                if( 타겟과 충돌하였을 경우, )
                {
                    충돌한 타겟의 목록을 가져온다. // 투사체Entity의 범위와 모양등의 속성에 따라서 목록이지만 1개만 타겟일수도있음

                    for( 충돌한 타겟의 목록 개수만큼 반복함)
                    {
                        충돌한 타겟의 hpHistory를 가져와서 데미지를 주거나 회복을 하거나 함.

                        속성에 따라서, 계속 진행할지,  한번 충돌하면 목록에서 삭제가 될지 결정됨.

                        또한, 한번 충돌할때마다 효과가 낮아지는경우에는 해당 오브젝트의 수치를 변경한다.

                        삭제가 된다면
                        현재 시점에서 직접적으로 투사체 Entity 목록에서 삭제하지 않고,
                        투사체 Entity 삭제예정 List에 추가 할 것.

                        이후, 모든 로직이 끝나고 몰아서 삭제할 것. (이후, 클라이언트에게 삭제된 목록을 중계함)
                        (--i;)
                        continue;
                    }
                }
            }
        }

    }


     */

}
