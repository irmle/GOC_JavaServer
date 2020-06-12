package ECS.System;

import ECS.Classes.*;
import ECS.Classes.Type.*;
import ECS.Components.*;
import ECS.Entity.*;
import ECS.Factory.MapFactory;
import ECS.Factory.MonsterFactory;
import ECS.Factory.SkillFactory;
import ECS.Game.WorldMap;
import Network.RMI_Classes.RMI_Context;
import Network.RMI_Classes.RMI_ID;
import Network.RMI_Common.RMI_ParsingClasses.EntityType;
import Network.RMI_Common.server_to_client;
import Enum.MapComponents;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 25 수 오후 17:30
 * 업뎃날짜 :
 * 목    적 :
 *      몬스터 AI 로직 수정중
 *      몹이 이동 불가능한 지점 밟았을 때 어케 처리할지에 대한 로직을 구상했는데,
 *      그거 검증용으로 만들었음.
 *      일단은 테스트용이고, 로직 검증되면
 *          이걸 메인으로 쓰던지, 기존 시스템으로 옮기던지 할 것.
 *
 */
public class MonsterSystem2 {

    /* 멤버 변수 */
    WorldMap worldMap;
    int tickRateCount;

    /* 생성자 */
    public MonsterSystem2(WorldMap worldMap) {

        this.worldMap = worldMap;
        tickRateCount = 0;
    }

    /* 매서드 */

    /**
     * 몬스터 로직 수행 및 상태 결정을 위해 호출하는 함수
     * 주요 로직 :
     *      1) 타겟 검색 처리 후, 그 결과에 따라 몬스터가 어떤 행동을 할지 결정한다
     *          현재 월드 내 존재하는 모든 객체에 대해
     *              몬스터의 시야 및 공격 반경에 들어있는지를 검사한다.
     *          IF 시야에 들어있는 경우
     *              IF 공격 가능 및 공격범위 내에 들어있다면
     *                  타겟을 공격하기로 한다
     *              ELSE
     *                  타겟을 쫒아가기로 한다
     *          ELSE
     *              목적지(MOVE POINT)로 이동하기로 한다
     *
     *      2) 위에서 결정된 행동(상태?)에 따라 처리를 수행한다
     *          CASE 타겟 공격 :
     *              타겟 공격 처리 후 continue (다음 몹 처리 로직으로 넘어감)
     *          CASE 타겟 추적 :
     *              타겟의 위치를 기준으로 한 다음 이동 위치를 계산한다. (바로 몹의 위치로 반영 NO)
     *          CASE 논타겟/이동 :
     *              기존에 지정되어 있던 MOVE POINT를 기준으로 한 다음 이동 위치를 계산한다. (바로 몹의 위치로 반영 NO)
     *
     *      3) 위에서 계산한 이동 위치에 대해 충돌 판정을 한다
     *
     *           계산된 좌표가 속한 타일의 타입을 확인한다.
     *           해당 타일이 이동 가능한 영역인지, 불가능한 영역인지 등을 판단한다. (뭔가.. 타입 이외에도 추가로 확인할 게 있을수도)
     *
     *           IF 이동 가능한 영역이라면
     *              계산된 좌표를 몹의 위치에 반영한다
     *              MOVE POINT에 도달했는지? 여부를 확인한다.
     *                  도달했다면, 다음 MOVE POINT로 갱신한다
     *           ELSE // 이동 불가능한 영역이라면
     *              계산된 좌표를 반영하지 않음
     *              현 위치를 기준으로 가까운 MOVE POINT를 검색한다
     *              MOVE POINT를 갱신한다
     *
     *
     * @param deltaTime
     */
    public void onUpdate(float deltaTime){

        //tickRateCount = ((int)worldMap.totalPlayTime / 100) % 10;
        tickRateCount++;

        boolean testMode = false;
        boolean doOldVersion = false;

        /* 모든 몬스터에 대해 반복한다 */
        for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()){

            int toDoAction = MonsterActionType.DO_NOTHING;

            /** 0. 몬스터 정보 */
            MonsterEntity monster = monsterEntity.getValue();

            /* 2020 02 28 정글 몬스터 넘어가고 */
            if(worldMap.jungleMonsterSlotHashMap.containsKey(monster.entityID)){
                continue;
            }

            // 죽은 몬스터 넘어가고
            if( (monster.hpComponent.currentHP <= 0)){
                continue;
            }


<<<<<<< HEAD
            if(false){
=======
            if(true){
>>>>>>> da989e90291b1041aa7163869fd981e0a9f8608c
                System.out.println("몬스터 " + monster.entityID + " 의 로직을 처리합니다.");
            }

            float mobMoveSpeed = monster.velocityComponent.moveSpeed;
            float moveSpeedBonus = monster.conditionComponent.moveSpeedBonus;
            float moveSpeedRate = monster.conditionComponent.moveSpeedRate;

            Vector3 monsterPos = monster.positionComponent.position;
/*
            System.out.println("몬스터 좌표 :  "
                    + monsterPos.x() + ", " + monsterPos.z());
*/


            /** 1. 타겟을 찾는다 */

            Entity currentTarget = null;
            float minDistance = monster.sightComponent.lookRadius;   // 몬스터의 최대 시야 거리를 초기값으로 설정

            Vector3 finalTargetPosition = null;
            HpHistoryComponent finalTargetHpHistory = null;
            ConditionComponent finalTargetCondition = null;
            HPComponent finalTargetHP = null;
            DefenseComponent finalTargetDefense = null;
            int finalTargetID = -1;
            int finalTargetType = -1;

            /* 2020 01 30 추가 */
            BuffActionHistoryComponent finalTargetBuffComponent = null;


            /* 월드 내 모든 객체에 대해 반복한다 */
            for(HashMap.Entry<Integer, Short> entity : worldMap.entityMappingList.entrySet()){

                /* 객체 하나를 검색 */
                int targetType = entity.getValue();
                int targetID = entity.getKey();

                HpHistoryComponent targetHpHistory = null;
                ConditionComponent targetCondition = null;
                HPComponent targetHP = null;
                DefenseComponent targetDefense = null;
                BuffActionHistoryComponent targetBuff = null;


//                System.out.println("몹" + monster.entityID + "의 현재 타겟 : " + targetType);
                switch (targetType){

                    case EntityType.CrystalEntity:
                        currentTarget = worldMap.crystalEntity.get(targetID);
                        if(currentTarget == null){
                            continue;
                        }
                        targetCondition = ((CrystalEntity) currentTarget).conditionComponent;
                        targetHP = ((CrystalEntity) currentTarget).hpComponent;
                        targetDefense = ((CrystalEntity) currentTarget).defenseComponent;
                        targetHpHistory = ((CrystalEntity) currentTarget).hpHistoryComponent;
                        targetBuff = ((CrystalEntity) currentTarget).buffActionHistoryComponent;
                        break;
                    case EntityType.CharacterEntity :
                        currentTarget = worldMap.characterEntity.get(targetID);
                        if(currentTarget == null){
                            continue;
                        }
                        targetCondition = ((CharacterEntity) currentTarget).conditionComponent;
                        targetHP = ((CharacterEntity) currentTarget).hpComponent;
                        targetDefense = ((CharacterEntity) currentTarget).defenseComponent;
                        targetHpHistory = ((CharacterEntity) currentTarget).hpHistoryComponent;
                        targetBuff = ((CharacterEntity) currentTarget).buffActionHistoryComponent;
                        break;
                    case EntityType.AttackTurretEntity :
                        currentTarget = worldMap.attackTurretEntity.get(targetID);
                        if(currentTarget == null){
                            continue;
                        }
                        targetCondition = ((AttackTurretEntity) currentTarget).conditionComponent;
                        targetHP = ((AttackTurretEntity) currentTarget).hpComponent;
                        targetDefense = ((AttackTurretEntity) currentTarget).defenseComponent;
                        targetHpHistory = ((AttackTurretEntity) currentTarget).hpHistoryComponent;
                        targetBuff = ((AttackTurretEntity) currentTarget).buffActionHistoryComponent;
                        break;
                    case EntityType.BuffTurretEntity :
                        currentTarget = worldMap.buffTurretEntity.get(targetID);
                        if(currentTarget == null){
                            continue;
                        }
                        targetCondition = ((BuffTurretEntity) currentTarget).conditionComponent;
                        targetHP = ((BuffTurretEntity) currentTarget).hpComponent;
                        targetDefense = ((BuffTurretEntity) currentTarget).defenseComponent;
                        targetHpHistory = ((BuffTurretEntity) currentTarget).hpHistoryComponent;
                        targetBuff = ((BuffTurretEntity) currentTarget).buffActionHistoryComponent;
                        break;
                    case  EntityType.BarricadeEntity :
                        currentTarget = worldMap.barricadeEntity.get(targetID);
                        if(currentTarget == null){
                            System.out.println("바리케이드 : " + targetID + " 널널");
                            continue;
                        }
                        targetCondition = ((BarricadeEntity) currentTarget).conditionComponent;
                        targetHP = ((BarricadeEntity) currentTarget).hpComponent;
                        targetDefense = ((BarricadeEntity) currentTarget).defenseComponent;
                        targetHpHistory = ((BarricadeEntity) currentTarget).hpHistoryComponent;
                        targetBuff = ((BarricadeEntity) currentTarget).buffActionHistoryComponent;

                        //System.out.println("바리케이드 : " + targetID);
                        break;

                    /* 객체가 몬스터, 스킬 오브젝트, 투사체인 경우는 패스한다 */
                    case EntityType.MonsterEntity :
                    case EntityType.SkillObjectEntity :
                    case EntityType.FlyingObjectEntity :
                    //case EntityType.BuffTurretEntity :
                        continue;

                }

                /* 타겟과의 거리를 계산한다 */
                Vector3 monsterPosition = monster.positionComponent.position;
                Vector3 targetPosition = currentTarget.positionComponent.position;
                float currentTargetDistance = Vector3.distance(monsterPosition, targetPosition);

  /*              System.out.println("타겟 좌표 :  "
                        + targetPosition.x() + ", " + targetPosition.z());
                System.out.println("타겟과의 거리 :  " + currentTargetDistance);
*/
                /* 타겟이 인식 대상에 들어갈 수 있는지 판별한다 */
                // 현재까지 검색된 범위 내 타겟보다 더 가까이 있어야 하고
                // 대상의 상태가 타겟지정 불가능 상태가 아니며, 죽은 상태도 아니어야 한다 // 애초에 죽은상태면 언타게터블이긴 하지만..
                boolean isMinDistance = (currentTargetDistance < minDistance) ? true : false;
                boolean isTargetable
                        = ( (!targetCondition.isUnTargetable)
                        && (targetHP.currentHP > 0) ) ? true : false;
/*

                System.out.println("최소 거리인가? :  " + isMinDistance);
                System.out.println("타겟지정이 가능한 상대인가? :  " + isTargetable);
*/

                boolean isRecognizable = (isMinDistance && isTargetable) ? true : false;

//                System.out.println("인식 가능한가? :  " + isRecognizable);

                if(isRecognizable){

                    minDistance = currentTargetDistance;
                    finalTargetID = targetID;
                    finalTargetType = targetType;
                    finalTargetCondition = targetCondition;
                    finalTargetDefense = targetDefense;
                    finalTargetHP = targetHP;
                    finalTargetHpHistory = targetHpHistory;
                    finalTargetPosition = targetPosition;
                    finalTargetBuffComponent = targetBuff;

                }

            }   // 월드 내 모든 객체에 대해 검색 종료



            /** 검색 결과를 바탕으로, 몬스터가 수행할 행동을 결정한다 */

            /* 타겟 지정 여부 */
            boolean targetHasSelected = (finalTargetID > 0) ? true : false;

            //System.out.println("타겟 지정 되었는가? :  " + targetHasSelected);

            /* 타겟이 지정된 경우, 해당 타겟을 공격할 수 있는지 판별한다 */
            boolean isTargetWithinAttackRange
                    = (targetHasSelected && (minDistance <= (monster.attackComponent.attackRange * 1.5f)) ) ? true : false;
            boolean isAbleToAttack = !(monster.conditionComponent.isDisableAttack);
            boolean isRemainCoolTimeZero = (monster.attackComponent.remainCoolTime <=0) ? true : false;
            boolean ableToMove = !(monster.conditionComponent.isDisableMove);

            boolean decidedToAttack;
            decidedToAttack
                    = (isTargetWithinAttackRange
                    && isAbleToAttack && isRemainCoolTimeZero) ? true : false;

            /*System.out.println("쿨타임제로? :  " + isRemainCoolTimeZero + " , " + monster.attackComponent.remainCoolTime);
            System.out.println("몹이 공격 가능한 상태인가? :  " + isAbleToAttack);
            System.out.println("타겟이 범위 내에 있는가? :  " + isTargetWithinAttackRange);
            System.out.println("공격하기로 했는가? :  " + decidedToAttack);
*/

            /* 행동 결정 */
            if(isTargetWithinAttackRange){

                if(decidedToAttack){
                    toDoAction = MonsterActionType.ATTACK_TARGET;
                }
                else{    // 쿨타임이 안됐거나, 몹 본인이 공격 불가능한 상태
                    toDoAction = MonsterActionType.DO_NOTHING;   // 애매한데.. 일단은 이렇게 해보고, 이상하면 얘 빼고 기존 체제로 고고
                }

            }
            else{

                //System.out.println("타겟이 공격범위 내에 있지 않아");

                if(ableToMove){
                    if(targetHasSelected){ //인식범위 내에는 들어있는데, 공격 범위내에 있는 건 아님 => 쫒아간다

                        toDoAction = MonsterActionType.CHASE_TARGET;

                        /**
                         * 2020 04 16
                         */
                        monster.monsterComponent.movePathType = PathType.NONE;

                        //System.out.println("타겟은 선택되었는데, 공격 범위 내에 있는 건 아니다.. 쫒아가야 함");

                    }
                    else {   // 인식된 타겟이 존재하지 않음 -->> 이동 지점을 따라 이동한다

                        // toDoAction = MonsterActionType.MOVE;
                        if(monster.monsterComponent.movePathType != PathType.NONE){
                            toDoAction = MonsterActionType.MOVE;

                            //System.out.println("타겟은 선택되지 않았음, 지정된 Path타입에 따라 이동해야 함.");
                        }
                        else{
                            //toDoAction = MonsterActionType.DO_NOTHING;


                            /**
                             * 2020 05 28
                             */

                            int mobIDRM = (monster.entityID % 5);
                            int tickCountRM = (tickRateCount % 5);
                            boolean tooManyMob = (worldMap.monsterEntity.size() >= 10) ? true : false;
                            boolean rmIsDiff = (mobIDRM != tickCountRM) ? true : false;

                            if(tooManyMob && rmIsDiff){

                                toDoAction = MonsterActionType.DO_NOTHING;
                                System.out.println("부하처리용 딴짓");
                            }
                            else{

                                /* 현 위치를 기준으로 가까운 MOVE POINT를 검색한다 */
                                MapInfo nearMovePoint = MapFactory.findNearMovePointVer20200213(worldMap, monsterPos);
                                if(nearMovePoint == null){

                                    System.out.println("타겟은 선택되지 않았음, 근처 이동포인트가 null임, 그래서 아무것도 안하려고..");

                                    toDoAction = MonsterActionType.DO_NOTHING;
<<<<<<< HEAD

                                    /**
                                     * 2020 05 28
                                     * 왜 멍때리냐!
                                     */
                                    MapInfo currentTilePoint = MapFactory.findTileByPosition(worldMap.gameMap, monsterPos.x(), monsterPos.z());

                                    Vector3 crystalPos = monster.positionComponent.crystalTargetPosition;
                                    MapInfo crystalTilePoint = MapFactory.findTileByPosition(worldMap.gameMap, crystalPos.x(), crystalPos.z());

                                    ArrayList<MapInfo> crystalPath = MapFactory.pathFind(worldMap, currentTilePoint, crystalTilePoint);


                                    // 월드에 경로 등록
                                    worldMap.mpPathList.put(monster.entityID, crystalPath);

                                    // nearMovePoint 가 속한 경로 및 그 인덱스를 찾는다
                                    int pathType = PathType.TO_MP;
                                    int pathIndex = 0;

                                    // 갱신
                                    monster.monsterComponent.movePathType = pathType;
                                    monster.monsterComponent.movePointIndex = pathIndex;

                                    toDoAction = MonsterActionType.MOVE;

                                    //System.out.println("타겟은 선택되지 않았음, 근처 이동포인트를 찾아가지고.. 이동포인트를 따라서 이동하려고 함.");



                                }
                                else{

                                    /* 몬스터의 MOVE POINT(Path Type, index)를 갱신한다 */

                                    MapInfo currentTilePoint = MapFactory.findTileByPosition(worldMap.gameMap, monsterPos.x(), monsterPos.z());
                                    ArrayList<MapInfo> tempPath = MapFactory.pathFind(worldMap, currentTilePoint, nearMovePoint);

                                    // 월드에 경로 등록
                                    worldMap.mpPathList.put(monster.entityID, tempPath);

=======

                                    /**
                                     * 2020 05 28
                                     * 왜 멍때리냐!
                                     */
                                    MapInfo currentTilePoint = MapFactory.findTileByPosition(worldMap.gameMap, monsterPos.x(), monsterPos.z());

                                    Vector3 crystalPos = monster.positionComponent.crystalTargetPosition;
                                    MapInfo crystalTilePoint = MapFactory.findTileByPosition(worldMap.gameMap, crystalPos.x(), crystalPos.z());

                                    ArrayList<MapInfo> crystalPath = MapFactory.pathFind(worldMap, currentTilePoint, crystalTilePoint);


                                    // 월드에 경로 등록
                                    worldMap.mpPathList.put(monster.entityID, crystalPath);

                                    // nearMovePoint 가 속한 경로 및 그 인덱스를 찾는다
                                    int pathType = PathType.TO_MP;
                                    int pathIndex = 0;

                                    // 갱신
                                    monster.monsterComponent.movePathType = pathType;
                                    monster.monsterComponent.movePointIndex = pathIndex;

                                    toDoAction = MonsterActionType.MOVE;

                                    //System.out.println("타겟은 선택되지 않았음, 근처 이동포인트를 찾아가지고.. 이동포인트를 따라서 이동하려고 함.");



                                }
                                else{

                                    /* 몬스터의 MOVE POINT(Path Type, index)를 갱신한다 */

                                    MapInfo currentTilePoint = MapFactory.findTileByPosition(worldMap.gameMap, monsterPos.x(), monsterPos.z());
                                    ArrayList<MapInfo> tempPath = MapFactory.pathFind(worldMap, currentTilePoint, nearMovePoint);

                                    // 월드에 경로 등록
                                    worldMap.mpPathList.put(monster.entityID, tempPath);

>>>>>>> da989e90291b1041aa7163869fd981e0a9f8608c
                                    // nearMovePoint 가 속한 경로 및 그 인덱스를 찾는다
                                    int pathType = PathType.TO_MP;
                                    int pathIndex = 0;

                                    // 갱신
                                    monster.monsterComponent.movePathType = pathType;
                                    monster.monsterComponent.movePointIndex = pathIndex;

                                    toDoAction = MonsterActionType.MOVE;

                                    //System.out.println("타겟은 선택되지 않았음, 근처 이동포인트를 찾아가지고.. 이동포인트를 따라서 이동하려고 함.");


                                }


                            }





                        }

                    }
                }
                else{
                    toDoAction = MonsterActionType.DO_NOTHING;

                    System.out.println("이동이 불가능함.");
                }
            }


            // 테스트
<<<<<<< HEAD
            if(false){
=======
            if(true){
>>>>>>> da989e90291b1041aa7163869fd981e0a9f8608c
                //System.out.println("DO_NOTHING = 0, ATTACK_TARGET = 1, CHASE_TARGET = 2, MOVE = 3");
                System.out.println("몬스터 " + monster.entityID + "의 행동 판정 : " + toDoAction);
                System.out.println("PathType : " + monster.monsterComponent.movePathType
                        + ", " + monster.monsterComponent.movePointIndex);
            }

            Vector3 movedPosition = new Vector3();

            /** 2. 타겟 존재 여부 및 결정된 행동에 따라 행동한다 */

            switch (toDoAction){

                case MonsterActionType.DO_NOTHING :

                    //System.out.println("아무것도 하지 않는다");

                    /* 공격 쿨타임을 업데이트한다 */
                    if(monster.attackComponent.remainCoolTime > 0){
                        monster.attackComponent.remainCoolTime -= deltaTime;

                        //System.out.println("업데이트된 공격 쿨타임 : " + monster.attackComponent.remainCoolTime);
                    }
                    continue;

                case MonsterActionType.ATTACK_TARGET :

                    //System.out.println("타겟을 공격한다");


                    Vector3 targetDir = Vector3.getTargetDirection(monsterPos, finalTargetPosition);
                    targetDir.normalize();

                    monster.rotationComponent.y = targetDir.y();
                    monster.rotationComponent.z = targetDir.z();


                    /* 모션 중계 */
                    server_to_client.motionMonsterDoAttack(
                            RMI_ID.getArray(worldMap.worldMapRMI_IDList.values()), RMI_Context.Reliable_Public_AES128,
                            monster.entityID, (short)finalTargetType, finalTargetID);


                    if (doOldVersion){

                        /* 타겟에게 입힐 데미지를 계산한다 */
                        float damageAmount = 0f;

                        AttackComponent mobAttack = monster.attackComponent;
                        ConditionComponent mobCondition = monster.conditionComponent;
                        damageAmount
                                = ( mobAttack.attackDamage + mobCondition.attackDamageBonus ) * mobCondition.attackDamageRate;


                        /** 2020 01 30 수정 ; 데미지가 아니라, 버프로 처리하게끔 */
                        /* 타겟을 찾고, 타겟의 데미지 목록에 넣어준다 */

                        BuffAction dmgBuff = new BuffAction();
                        dmgBuff.unitID = monster.entityID;
                        dmgBuff.skillUserID = monster.entityID;
                        dmgBuff.remainCoolTime = -1f;
                        dmgBuff.coolTime = -1f;
                        dmgBuff.remainTime = 0.15f;

                        dmgBuff.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, damageAmount));

                        // 버프 목록에 추가!!
                        finalTargetBuffComponent.conditionHistory.add(dmgBuff);

                    }
                    else {

                        /** 2020 04 03 작성 */

                        /*finalTargetBuffComponent.conditionHistory.add(
                                MonsterFactory.createMonsterActionEffect(
                                        MonsterActionType_ForEffect.MONSTER_ATTACK, "데미지", monster, monster.entityID));*/

                        /**
                         * 원거리 공격 추가
                         */
                        boolean isLongDistanceAttack = (minDistance >= 9f) ? true : false;
                        if(isLongDistanceAttack){

                            /* 투사체를 생성한다 */

                            FlyingObjectComponent flyingObject = new FlyingObjectComponent();
                            flyingObject.userEntityID = monster.entityID;
                            flyingObject.targetEntityID = finalTargetID;

                            flyingObject.createdSkillType = SkillType.MAGICIAN_NORMAL_ATTACK;    // "포탑 공격" 타입

                            flyingObject.flyingObjectRemainDistance = 0f; // 타겟이 정해져 있으므로, 목적지를 따로 정하지 않음

                            flyingObject.flyingSpeed = 15f;
                            flyingObject.flyingObjectRadius = 0.75f;

                            flyingObject.direction = new Vector3(0f, 0f, 0f);

                            flyingObject.buffAction = new BuffAction();
                            flyingObject.buffAction.unitID = monster.entityID;

                            FlyingObjectEntity flyingObjectEntity = new FlyingObjectEntity(flyingObject);
                            flyingObjectEntity.team = Team.RED;
                            flyingObjectEntity.entityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

                            flyingObjectEntity.positionComponent.position = (Vector3)monster.positionComponent.position.clone();
                            flyingObjectEntity.positionComponent.position.set(
                                    flyingObjectEntity.positionComponent.position.x(),
                                    flyingObjectEntity.positionComponent.position.y() + 1.3f,
                                    flyingObjectEntity.positionComponent.position.z()
                            );

                            flyingObjectEntity.flyingObjectComponent = flyingObject;
                            flyingObject.startPosition = flyingObjectEntity.positionComponent.position;

                            /* 월드 내 Entity 생성 요청 큐에 추가한다 */
                            worldMap.requestCreateQueue.add(flyingObjectEntity);

                        }
                        else{
                            finalTargetBuffComponent.conditionHistory.add(
                                    MonsterFactory.createMonsterActionEffect(
                                            MonsterActionType_ForEffect.MONSTER_ATTACK, "데미지", monster, monster.entityID));


                        }

                    }



                    /* 공격 쿨타임을 초기화한다 */
                    monster.attackComponent.remainCoolTime = 1 / monster.attackComponent.attackSpeed;

                    //System.out.println("초기화된 공격 쿨타임 : " + monster.attackComponent.remainCoolTime);

                    /* 현 몹에 대한 처리를 종료하고, 다음 몹으로 넘어간다 */
                    continue;

                case MonsterActionType.CHASE_TARGET :

                    //System.out.println("타겟을 쫒아간다 ");

                    /* 타겟을 향해 이동할 지점을 구한다  */


                    /* 이동 방향 구하기 */
                    Vector3 directionToTarget
                            = Vector3.normalizeVector(monsterPos, finalTargetPosition);

                    /*directionToTarget
                            = Vector3.getTargetDirection(monsterPos, finalTargetPosition).normalize();*/

                    /**
                     * 작성날짜 : 오후 10:43 2020-04-21
                     * 기    능 : 제일 가까운 충돌을 감지하고, 순간 방향을 틀음.
                     * 처    리 :
                     *      1) 충돌 체크를 한다( )
                     *      2) if 충돌객체(몹)이 존재한다면,
                     *              이동 방향을 틀어준다( )
                     *         else
                     *              기존 목적지 방향으로 간다
                     *
                     *     ==============================================
                     *     충돌체크 로직 :
                     *
                     *          1. 충돌 거리에 존재하는, 제일 가까운 충돌타겟을 검색함
                     *              모든 몹에 대해 반복(본인 제외, 죽은애 제외)
                     *                  대상과의 거리를 구한다
                     *                  if 대상이 충돌 범위 내 존재할 때,
                     *                      제일 가까운지 확인하여 업데이트
                     *                  else면 걍 continue임
                     *          2. 충돌 여부를 판단하여 리턴
                     *              if 존재하면
                     *                  true 리턴
                     *              else
                     *                  false 리턴
                     *
                     *     ==============================================
                     *     이동방향 틀기 로직 :
                     *
                     *          1. 목적 방향 벡터와 충돌 방향 벡터를 구한다.
                     *          2. 두 벡터의 사잇각을 구한다
                     *          3. 두 벡터의 외적을 구한다
                     *          4. 외적(3)의 y값을 가지고, 현 몹에 대한 충돌 몹의 상대적인 방향(왼쪽/오른쪽..)을 구한다.
                     *              if y > 0
                     *                  // 충돌 대상이, 몬스터의 왼쪽에 있는 것임(반시계 방향)
                     *                  본래 목적 방향 벡터로부터, + (사잇각) 만큼 회전한 방향을 구해 리턴한다
                     *              else if y < 0
                     *                  // 충돌 대상이, 몬스터의 오른쪽에 있는 것임(시계 방향)
                     *                  본래 목적 방향 벡터로부터, - (사잇각) 만큼 회전한 방향을 구해 리턴한다
                     *              else
                     *                  // 일단 패스. 평행.
                     *
                     *      ==============================================
                     *
                     */


                    /** 충돌 체크를 한다 */



                    MonsterEntity crashMob = checkCollisionWithOtherMonsters(monster, directionToTarget, 3f);
                    boolean willBeCrash = (!(crashMob == null)) ? true : false;

                    /** 이동 방향을 결정한다 */
                    //willBeCrash = false;
                    if(willBeCrash){

                        Vector3 crashMobPos = crashMob.positionComponent.position;

                        /* 이동 방향을 틀어준다 */
                        directionToTarget
                                = turnDirectionToAvoidCollision(monsterPos,crashMobPos, directionToTarget);

                    }
                    else{

                        /* 본래 목적 방향으로 향한다 */
                        /*directionToTarget
                                = Vector3.normalizeVector(monsterPos, finalTargetPosition);*/
                    }


                    /** 방향결정 로직의 끝 */
                    /***************************************************************************************************/


                    monster.rotationComponent.y = directionToTarget.y();
                    monster.rotationComponent.z = directionToTarget.z();



                    /* 이동 지점 구하기  */

                    float movementSpeedToTarget
                            = deltaTime * (mobMoveSpeed + moveSpeedBonus) * moveSpeedRate;
                    Vector3 moveVectorToTarget
                            = directionToTarget.setSpeed(movementSpeedToTarget);



                    // 이동할 지점. 충돌판정 후 이동할 것임.
                    Vector3 movementPosToTarget = (Vector3) monsterPos.clone();
                    movementPosToTarget.movePosition(movementPosToTarget, moveVectorToTarget);

                    movedPosition.set(movementPosToTarget);

                    break;

                case MonsterActionType.MOVE :

                    //System.out.println("몬스터 이동 처리 연산중... ");

                    /** 지정된 path의 move point를 따라 이동 처리를 한다  */

                    /* 몹에 지정되어있는 이동 포인트를 찾는다 */
                    int pathType = monster.monsterComponent.movePathType;
                    int pathIndex = monster.monsterComponent.movePointIndex;


                    MapInfo targetMovePoint = null;
                    Vector3 targetMovePos = new Vector3();
                    if(pathType == PathType.NONE){

                        // 타겟이 크리스탈인 경우와 그렇지 않은 경우를 구분해야 함..
                        switch (finalTargetType){

                            case EntityType.CrystalEntity :
                                /** 크리스탈의 타일 위치(중점)을 설정해준다. */

                                //System.out.println("크리스탈 찾기 ");

                                // 크리스탈 찾기
                                MapComponentUnit unit;
                                Vector3 crystalPosition = null;
                                for(int i=0; i<worldMap.mapComponentUnitList.size(); i++){

                                    unit = worldMap.mapComponentUnitList.get(i);
                                    if(unit.componentType == MapComponents.CRYSTAL_AREA){
                                        crystalPosition = unit.getCenterPositionFromMapArea();

                                    }
                                }

                                // 세팅
                                targetMovePos = crystalPosition;

                            default:
                                //System.out.println("크리스탈이 아닌 타겟");

                        }
                    }
                    else if( pathType == PathType.TO_MP){    /** 2020 01 14 경로 이탈 케이스 */

                        //System.out.println("경로 이탈 케이스");

                        try{
                            targetMovePoint = worldMap.mpPathList.get(monster.entityID).get(pathIndex);
                        } catch(IndexOutOfBoundsException e){
                            //System.out.println("배열 바운드 오류 발생!");
                        }


                        targetMovePos = targetMovePoint.getPixelPosition();

                        if(testMode){
/*
                            System.out.println("몬스터의 경로 타입 :  " + pathType);

                            System.out.println("몬스터가 이동해야 할 이동 포인트 :  "
                                    + targetMovePoint.arrayX + ", " + targetMovePoint.arrayY);

                            System.out.println("몬스터가 이동해야 할 이동 포인트의 좌표 :  "
                                    + targetMovePos.x() + ", " + targetMovePos.z());
*/
                        }


                    }
                    else{

                        targetMovePoint = worldMap.pathList.get(pathType).get(pathIndex);
                        targetMovePos = targetMovePoint.getPixelPosition();

                        if(testMode){
/*                            System.out.println("몬스터의 경로 타입 :  " + pathType);

                            System.out.println("몬스터가 이동해야 할 이동 포인트 :  "
                                    + targetMovePoint.arrayX + ", " + targetMovePoint.arrayY);

                            System.out.println("몬스터가 이동해야 할 이동 포인트의 좌표 :  "
                                    + targetMovePos.x() + ", " + targetMovePos.z());*/
                        }
                    }

                    /**  몹이 이동할 지점을 계산한다 */

                    /* 이동 방향 구하기 */
                    Vector3 directionToMovePoint
                            = Vector3.normalizeVector(monsterPos, targetMovePos);

                    /*directionToMovePoint
                            = Vector3.getTargetDirection(monsterPos, targetMovePos).normalize();*/


                    /**
                     * 작    성 : 오후 10:43 2020-04-21
                     * 기    능 : 제일 가까운 충돌을 감지하고, 순간 방향을 틀음.
                     * 처    리 :
                     *      1) 충돌 체크를 한다( )
                     *      2) if 충돌객체(몹)이 존재한다면,
                     *              이동 방향을 틀어준다( )
                     *         else
                     *              기존 목적지 방향으로 간다
                     *
                     *     ==============================================
                     *     충돌체크 로직 :
                     *
                     *          1. 충돌 거리에 존재하는, 제일 가까운 충돌타겟을 검색함
                     *              모든 몹에 대해 반복(본인 제외, 죽은애 제외)
                     *                  대상과의 거리를 구한다
                     *                  if 대상이 충돌 범위 내 존재할 때,
                     *                      제일 가까운지 확인하여 업데이트
                     *                  else면 걍 continue임
                     *          2. 충돌 여부를 판단하여 리턴
                     *              if 존재하면
                     *                  true 리턴
                     *              else
                     *                  false 리턴
                     *
                     *     ==============================================
                     *     이동방향 틀기 로직 :
                     *
                     *          1. 목적 방향 벡터와 충돌 방향 벡터를 구한다.
                     *          2. 두 벡터의 사잇각을 구한다
                     *          3. 두 벡터의 외적을 구한다
                     *          4. 외적(3)의 y값을 가지고, 현 몹에 대한 충돌 몹의 상대적인 방향(왼쪽/오른쪽..)을 구한다.
                     *              if y > 0
                     *                  // 충돌 대상이, 몬스터의 왼쪽에 있는 것임(반시계 방향)
                     *                  본래 목적 방향 벡터로부터, + (사잇각) 만큼 회전한 방향을 구해 리턴한다
                     *              else if y < 0
                     *                  // 충돌 대상이, 몬스터의 오른쪽에 있는 것임(시계 방향)
                     *                  본래 목적 방향 벡터로부터, - (사잇각) 만큼 회전한 방향을 구해 리턴한다
                     *              else
                     *                  // 일단 패스. 평행.
                     *
                     *      ==============================================
                     *
                     */


                    /** 충돌 체크를 한다 */

                    crashMob = checkCollisionWithOtherMonsters(monster, directionToMovePoint, 2f);
                    willBeCrash = (!(crashMob == null)) ? true : false;

                    //willBeCrash = false;

                    /** 이동 방향을 결정한다 */
                    if(willBeCrash){

                        //System.out.println("충돌이다");

                        Vector3 crashMobPos = crashMob.positionComponent.position;

                        /* 이동 방향을 틀어준다 */
                        directionToMovePoint
                                = turnDirectionToAvoidCollision(monsterPos,crashMobPos, directionToMovePoint);

                    }
                    else{

                        /* 본래 목적 방향으로 향한다 */
                        /*directionToMovePoint
                                = Vector3.normalizeVector(monsterPos, targetMovePos);*/
                    }


                    /** 방향결정 로직의 끝 */
                    /***************************************************************************************************/


                    monster.rotationComponent.y = directionToMovePoint.y();
                    monster.rotationComponent.z = directionToMovePoint.z();




                    /* 이동 지점 구하기  */

                    float movementSpeedToMovePoint
                            = deltaTime * (mobMoveSpeed + moveSpeedBonus) * moveSpeedRate;
                    Vector3 moveVectorToMovePoint
                            = directionToMovePoint.setSpeed(movementSpeedToMovePoint);


                    // 이동할 지점. 충돌판정 후 이동할 것임.
                    Vector3 movementPosToMovePoint = (Vector3) monsterPos.clone();
                    movementPosToMovePoint.movePosition(movementPosToMovePoint, moveVectorToMovePoint);

                    movedPosition.set(movementPosToMovePoint);

                    monster.velocityComponent.velocity = moveVectorToMovePoint; // 헐.. 이거 안해주니까 안됐었다니...

  /*                  System.out.println("몬스터가 이동해야 할 좌표 :  "
                            + movedPosition.x() + ", " + movedPosition.z());
*/
                    break;

            }


            /** 3. 이동한 경우, 이동 위치에 대해 충돌 판정 및 처리를 한다 */

            //System.out.println("이동 판정처리 합니다 ");
            /* 계산된 좌표가 속한 타일의 타입을 확인, 이동 가능한 영역인지 불가능한 영역인지 판단한다 */
            boolean isMovableTile = MapFactory.moveCheck(worldMap.gameMap, movedPosition.x(), movedPosition.z());
            /*if(monster.monsterComponent.movePathType == PathType.TO_MP){
                isMovableTile = true;
            }*/

            //System.out.println("이동가능한 타일인가 : " + isMovableTile);
            if(isMovableTile){

                //System.out.println("이동 가능하므로, 좌표에 반영합니다 ");

                /* 계산된 좌표를 몹의 위치에 반영한다 */
                monsterPos.set(movedPosition);
/*

                System.out.println("몬스터 좌표 :  "
                        + monsterPos.x() + ", " + monsterPos.z());
*/

                if(monster.monsterComponent.movePathType != PathType.NONE){

                    /* 현재 설정된 MOVE POINT에 도달했는지 확인한다 */
                    // 도달했다면, 다음 Move Point로 갱신한다..

                    int pathType = monster.monsterComponent.movePathType;
                    int pathIndex = monster.monsterComponent.movePointIndex;

                    MapInfo targetTile;
                    if(pathType == PathType.TO_MP){
                        targetTile = worldMap.mpPathList.get(monster.entityID).get(pathIndex);
                    }
                    else{
                        targetTile = worldMap.pathList.get(pathType).get(pathIndex);
                    }

                    if(MapFactory.checkTile(targetTile, monsterPos.x(), monsterPos.z())){

//                        System.out.println("이동 포인트 타일에 도달함");

                        /*monster.monsterComponent.movePointIndex++;
                        if(monster.monsterComponent.movePointIndex >= (worldMap.pathList.get(pathType).size()) ){

                            *//* 2020 01 14 *//*
                            if(pathType == PathType.TO_MP){

                                System.out.println("원래 경로로 돌아감");

                                // 도달한 타겟이 속한 경로 및 인덱스를 찾는다
                                int nextPathType = MapFactory.findPathTypeByMapInfo(worldMap, targetTile);
                                int nextPathIndex = worldMap.pathList.get(nextPathType).indexOf(targetTile);

                                // 몬스터 경로 업뎃
                                monster.monsterComponent.movePathType = nextPathType;
                                monster.monsterComponent.movePointIndex = nextPathIndex;

                                System.out.println("돌아간 경로 타입 : " + nextPathType );

                            } else{

                                // 마지막 path까지 다 왔다면, 목적지를..?? pathType NONE 으로 한다
                                monster.monsterComponent.movePathType = PathType.NONE;

                            }


                        }*/

                        //


                        monster.monsterComponent.movePointIndex++;
                        if(pathType == PathType.TO_MP){
                            if(monster.monsterComponent.movePointIndex >= (worldMap.mpPathList.get(monster.entityID).size()) ){
  //                              System.out.println("원래 경로로 돌아감");

                                // 도달한 타겟이 속한 경로 및 인덱스를 찾는다
                                int nextPathType = MapFactory.findPathTypeByMapInfo(worldMap, targetTile);
                                int nextPathIndex = worldMap.pathList.get(nextPathType).indexOf(targetTile);

                                // 몬스터 경로 업뎃
                                monster.monsterComponent.movePathType = nextPathType;
                                monster.monsterComponent.movePointIndex = nextPathIndex;

    //                            System.out.println("돌아간 경로 타입 : " + nextPathType );
                            }

                        } else{
                            if(monster.monsterComponent.movePointIndex >= (worldMap.pathList.get(pathType).size()) ){
                                // 마지막 path까지 다 왔다면, 목적지를..?? pathType NONE 으로 한다
                                monster.monsterComponent.movePathType = PathType.NONE;
                            }
                        }


                        /*if(monster.monsterComponent.movePointIndex >= (worldMap.pathList.get(pathType).size()) ){

                         *//* 2020 01 14 *//*
                            if(pathType == PathType.TO_MP){

                                System.out.println("원래 경로로 돌아감");

                                // 도달한 타겟이 속한 경로 및 인덱스를 찾는다
                                int nextPathType = MapFactory.findPathTypeByMapInfo(worldMap, targetTile);
                                int nextPathIndex = worldMap.pathList.get(nextPathType).indexOf(targetTile);

                                // 몬스터 경로 업뎃
                                monster.monsterComponent.movePathType = nextPathType;
                                monster.monsterComponent.movePointIndex = nextPathIndex;

                                System.out.println("돌아간 경로 타입 : " + nextPathType );

                            } else{

                                // 마지막 path까지 다 왔다면, 목적지를..?? pathType NONE 으로 한다
                                monster.monsterComponent.movePathType = PathType.NONE;

                            }


                        }*/



                    }
                }

            }
            else{

                if(false){   // 기존 버전
                    System.out.println("가까운 이동포인트 찾기");

                    /* 현 위치를 기준으로 가까운 MOVE POINT를 검색한다 */
                    MapInfo nearMovePoint = MapFactory.findNearMovePoint(worldMap, monsterPos);

                    /* 몬스터의 MOVE POINT(Path Type, index)를 갱신한다 */
                    // nearMovePoint 가 속한 경로 및 그 인덱스를 찾는다
                    int pathType = MapFactory.findPathTypeByMapInfo(worldMap, nearMovePoint);
                    int pathIndex = worldMap.pathList.get(pathType).indexOf(nearMovePoint);

                    System.out.println("근처 타일 :  " + nearMovePoint.arrayX + ", " + nearMovePoint.arrayY);
                    System.out.println("근처 타일 경로 타입" + pathType);
                    System.out.println("근처 타일 인덱스 :  " + pathIndex);

                    // 갱신
                    monster.monsterComponent.movePathType = pathType;
                    monster.monsterComponent.movePointIndex = pathIndex;
                }
                else{    // 2020 01 14 버전

      //              System.out.println("가까운 이동포인트 찾기");

                    /* 현 위치를 기준으로 가까운 MOVE POINT를 검색한다 */
                    MapInfo nearMovePoint = MapFactory.findNearMovePointVer20200213(worldMap, monsterPos);
                    if(nearMovePoint == null){

                        Vector3 crystalPos = monster.positionComponent.crystalTargetPosition;
                        MapInfo crystalTilePoint = MapFactory.findTileByPosition(worldMap.gameMap, crystalPos.x(), crystalPos.z());

                        nearMovePoint = crystalTilePoint;
                    }

                    /* 몬스터의 MOVE POINT(Path Type, index)를 갱신한다 */
                    /* A* 알고리즘으로 경로를 찾는다... */
                    // 몹이 현재 속한 타일..

                    MapInfo currentTilePoint = MapFactory.findTileByPosition(worldMap.gameMap, monsterPos.x(), monsterPos.z());
                    ArrayList<MapInfo> tempPath = MapFactory.pathFind(worldMap, currentTilePoint, nearMovePoint);

                    // 월드에 경로 등록
                    worldMap.mpPathList.put(monster.entityID, tempPath);
        //            System.out.println("경로 수 : " + tempPath.size());

                    // nearMovePoint 가 속한 경로 및 그 인덱스를 찾는다
                    int pathType = PathType.TO_MP;
                    int pathIndex = 0;
/*

                    System.out.println("근처 타일 :  " + nearMovePoint.arrayX + ", " + nearMovePoint.arrayY);
                    System.out.println("근처 타일 경로 타입" + pathType);
                    System.out.println("근처 타일 인덱스 :  " + pathIndex);
*/

                    // 갱신
                    monster.monsterComponent.movePathType = pathType;
                    monster.monsterComponent.movePointIndex = pathIndex;

                }

            }


            /* 공격 쿨타임을 업데이트한다 */
            if(monster.attackComponent.remainCoolTime > 0){
                monster.attackComponent.remainCoolTime -= deltaTime;

//                System.out.println("업데이트된 공격 쿨타임 : " + monster.attackComponent.remainCoolTime);
            }

        }

    }

    /*******************************************************************************************************************/

    /**
     * 작성날짜 : 오후 11:11 2020-04-21
     * 기    능 : 근처에 충돌가능성 있는 몬스터가 있는지 여부를 판단한다
     * 처    리 :
     *          1. 충돌 거리에 존재하는, 제일 가까운 충돌타겟을 검색함
     *               모든 몹에 대해 반복(본인 제외, 죽은애 제외)
     *                   대상과의 거리를 구한다
     *                   if 대상이 충돌 범위 내 존재할 때,
     *                       제일 가까운지 확인하여 업데이트
     *                   else면 걍 continue임
     *           2. 충돌 여부를 판단하여 리턴
     *               if 존재하면
     *                   true 리턴
     *              else
     *                  false 리턴
     *
     */
    public MonsterEntity checkCollisionWithOtherMonsters(MonsterEntity monster, Vector3 targetDir, float distance){

        MonsterEntity beCrashedMob = null;

        float COLLISION_DISTANCE = distance;
        float minDis = COLLISION_DISTANCE;

        int crushCount = 0;
        for (MonsterEntity monsterEntity : worldMap.monsterEntity.values()){

            MonsterEntity target = monsterEntity;

            /* pass 조건 */
            boolean isMe = (target.entityID == monster.entityID) ? true : false;
            boolean isDeadTarget = (target.hpComponent.currentHP <= 0f) ? true : false;

            boolean pass = ( isMe || isDeadTarget );
            if(pass)
                continue;

            /* 참조 */
            Vector3 myPos = monster.positionComponent.position;
            Vector3 targetPos = target.positionComponent.position;

            /* 타겟과의 거리를 구한다 */
            float currentDis = Vector3.distance(myPos, targetPos);

            boolean isOutOfRange = (currentDis > COLLISION_DISTANCE) ? true : false;
            if(isOutOfRange)
                continue;

            /* 뒤에 있는 애 패스 */
            float betweenAngle = Vector3.getAngle(targetDir, Vector3.getTargetDirection(myPos, targetPos));
            boolean isBehindMe = (betweenAngle > 90) ? true : false;
            if(isBehindMe){
                continue;
            }

            // 범위영역에 따라.. 다른 기준을..
            if(betweenAngle >= 80){

                // 수평에 가까운 영역에 있다면, 비교적 가까이 있어도, 충돌 아닌걸로 하자..
                if((currentDis < 0.15f) ){
                    continue;
                }
            }

            //
            crushCount++;
            if(crushCount >= 5){
                //beCrashedMob = null;
                //break;

            }


            /* 최소 거리 업데이트 */
            boolean isMinDis = (currentDis <= minDis) ? true : false;
            if(isMinDis){

                minDis = currentDis;
                beCrashedMob = target;
            }

        }

        monster.monsterComponent.monsterCollisionCount = crushCount;

        return beCrashedMob;
    }

    /**
     * 작성날짜 : 오후 11:11 2020-04-21
     * 기    능 : 충돌을 피하기 위해 틀어줄 방향을 구한다
     * 처    리 :
     *          1. 목적 방향 벡터와 충돌 방향 벡터를 구한다.
     *          2. 두 벡터의 사잇각을 구한다
     *          3. 두 벡터의 외적을 구한다
     *          4. 외적(3)의 y값을 가지고, 현 몹에 대한 충돌 몹의 상대적인 방향(왼쪽/오른쪽..)을 구한다.
     *               if y > 0
     *                   // 충돌 대상이, 몬스터의 왼쪽에 있는 것임(반시계 방향)
     *                   본래 목적 방향 벡터로부터, + (사잇각) 만큼 회전한 방향을 구해 리턴한다
     *               else if y < 0
     *                   // 충돌 대상이, 몬스터의 오른쪽에 있는 것임(시계 방향)
     *                   본래 목적 방향 벡터로부터, - (사잇각) 만큼 회전한 방향을 구해 리턴한다
     *               else
     *                   // 일단 패스. 평행.
     *
     */
    public Vector3 turnDirectionToAvoidCollision(Vector3 monsterPos, Vector3 crashMobPos, Vector3 targetDir){

        Vector3 turnedDirection = null;

        /** 충돌방향 백터 구하기 */
        Vector3 crashDir = Vector3.getTargetDirection(monsterPos, crashMobPos);
        crashDir = Vector3.normalizeVector(monsterPos, crashMobPos);

        /** 목적방향 벡터와 충돌방향 벡터의 사잇각 구하기 */
        float betweenAngle = Vector3.getAngle(targetDir, crashDir);

        // 겹침 방지용 응급처치; 타겟과 수직 관계에 있을 경우, 사잇각을 구해봤자,, 0이라 회전률도 0
        betweenAngle += 20f;


        /** 두 벡터의 외적을 구함 */
        Vector3 cross = Vector3.getCrossProduct(targetDir, crashDir);

        /** 충돌 대상의 상대적 방향 판단 */
        float y = cross.y();

        if( y < 0){

            turnedDirection = Vector3.rotateVector3ByAngleAxis(targetDir, new Vector3(0,1,0), betweenAngle);
        }
        else if (y > 0){

            turnedDirection = Vector3.rotateVector3ByAngleAxis(targetDir, new Vector3(0,1,0), -(betweenAngle));
        }
        else {

            //System.out.println("평행임... 어카지... 왠지그냥 아무 방향으로 줘도 될 것 같은데.. ");
            turnedDirection = targetDir;

        }

        //turnedDirection.printVectorInfo();

        return turnedDirection;
    }


















}
