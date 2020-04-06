package ECS.System;

import ECS.Classes.*;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Type.MonsterActionType;
import ECS.Classes.Type.PathType;
import ECS.Components.*;
import ECS.Entity.*;
import ECS.Factory.MapFactory;
import ECS.Game.WorldMap;
import RMI.RMI_Classes.RMI_Context;
import RMI.RMI_Classes.RMI_ID;
import RMI.RMI_Common._RMI_ParsingClasses.EntityType;
import RMI.RMI_Common.server_to_client;
import Enum.MapComponents;
import ECS.Classes.MapInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 19 수 오후 18:30
 * 업뎃날짜 : 2020 02 28 금
 */
public class MonsterSystem3 {

    WorldMap worldMap;

    public MonsterSystem3(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    /* 매서드 */

    /**
     * 몬스터 로직 수행 및 상태 결정을 위해 호출하는 함수
     * 몹시2와 달라진 점 :
     *      이동 처리 시, 몹끼리 충돌하는 것을 막는 처리가 추가되었음. 테스트해보고 잘 동작하면 몹시2를 이걸로 대체할 것.
     *
     */
    public void onUpdate(float deltaTime){

        boolean testMode = true;

        /* 모든 몬스터에 대해 반복한다 */
        for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()){

            int toDoAction = MonsterActionType.DO_NOTHING;

            /** 0. 몬스터 정보 */
            MonsterEntity monster = monsterEntity.getValue();

            /* 2020 02 28 정글 몬스터 넘어가고 */
            if(worldMap.jungleMonsterSlotList.containsKey(monster.entityID)){
                continue;
            }

            // 죽은 몬스터 넘어가고
            if( (monster.hpComponent.currentHP <= 0)){
                continue;
            }

            if(testMode){
                System.out.println("몬스터 " + monster.entityID + " 의 로직을 처리합니다.");
            }

            float mobMoveSpeed = monster.velocityComponent.moveSpeed;
            float moveSpeedBonus = monster.conditionComponent.moveSpeedBonus;
            float moveSpeedRate = monster.conditionComponent.moveSpeedRate;

            Vector3 monsterPos = monster.positionComponent.position;
            System.out.println("몬스터 좌표 :  "
                    + monsterPos.x() + ", " + monsterPos.z());


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

                System.out.println("몹" + monster.entityID + "의 현재 타겟 : " + targetType);
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
                            continue;
                        }
                        targetHP = ((BarricadeEntity) currentTarget).hpComponent;
                        if(targetHP.currentHP <= 0) {
                            System.out.println("바리케이드는 죽었어");
                            continue;
                        }
                        targetCondition = ((BarricadeEntity) currentTarget).conditionComponent;
                        if(targetCondition == null){
                            System.out.println("바리케이드는 컨디션이 없나?? 왜지");
                        }
                        targetDefense = ((BarricadeEntity) currentTarget).defenseComponent;
                        targetHpHistory = ((BarricadeEntity) currentTarget).hpHistoryComponent;
                        targetBuff = ((BarricadeEntity) currentTarget).buffActionHistoryComponent;
                        break;

                    /* 객체가 몬스터, 스킬 오브젝트, 투사체인 경우는 패스한다 */
                    case EntityType.MonsterEntity :
                    case EntityType.SkillObjectEntity :
                    case EntityType.FlyingObjectEntity :
                        continue;

                }

                /* 타겟과의 거리를 계산한다 */
                Vector3 monsterPosition = monster.positionComponent.position;
                Vector3 targetPosition = currentTarget.positionComponent.position;
                float currentTargetDistance = Vector3.distance(monsterPosition, targetPosition);

                System.out.println("타겟 좌표 :  "
                        + targetPosition.x() + ", " + targetPosition.z());
                System.out.println("타겟과의 거리 :  " + currentTargetDistance);

                /* 타겟이 인식 대상에 들어갈 수 있는지 판별한다 */
                // 현재까지 검색된 범위 내 타겟보다 더 가까이 있어야 하고
                // 대상의 상태가 타겟지정 불가능 상태가 아니며, 죽은 상태도 아니어야 한다 // 애초에 죽은상태면 언타게터블이긴 하지만..
                boolean isMinDistance = (currentTargetDistance < minDistance) ? true : false;
                boolean isTargetable
                        = ( (!targetCondition.isUnTargetable)
                        && (targetHP.currentHP > 0) ) ? true : false;

                System.out.println("최소 거리인가? :  " + isMinDistance);
                System.out.println("타겟지정이 가능한 상대인가? :  " + isTargetable);

                boolean isRecognizable = (isMinDistance && isTargetable) ? true : false;

                System.out.println("인식 가능한가? :  " + isRecognizable);

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

            System.out.println("타겟 지정 되었는가? :  " + targetHasSelected);

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

            System.out.println("쿨타임제로? :  " + isRemainCoolTimeZero + " , " + monster.attackComponent.remainCoolTime);
            System.out.println("몹이 공격 가능한 상태인가? :  " + isAbleToAttack);
            System.out.println("타겟이 범위 내에 있는가? :  " + isTargetWithinAttackRange);
            System.out.println("공격하기로 했는가? :  " + decidedToAttack);


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

                if(ableToMove){
                    if(targetHasSelected){ //인식범위 내에는 들어있는데, 공격 범위내에 있는 건 아님 => 쫒아간다
                        toDoAction = MonsterActionType.CHASE_TARGET;
                    }
                    else {   // 인식된 타겟이 존재하지 않음 -->> 이동 지점을 따라 이동한다

                        // toDoAction = MonsterActionType.MOVE;
                        if(monster.monsterComponent.movePathType != PathType.NONE){
                            toDoAction = MonsterActionType.MOVE;
                        }
                        else{
                            toDoAction = MonsterActionType.DO_NOTHING;
                        }

                    }
                }
                else{
                    toDoAction = MonsterActionType.DO_NOTHING;
                }
            }


            // 테스트
            if(true){
                System.out.println("DO_NOTHING = 0, ATTACK_TARGET = 1, CHASE_TARGET = 2, MOVE = 3");
                System.out.println("몬스터 " + monster.entityID + "의 행동 판정 : " + toDoAction);
            }

            Vector3 movedPosition = new Vector3();

            /** 2. 타겟 존재 여부 및 결정된 행동에 따라 행동한다 */

            switch (toDoAction){

                case MonsterActionType.DO_NOTHING :

                    System.out.println("아무것도 하지 않는다");

                    /* 공격 쿨타임을 업데이트한다 */
                    if(monster.attackComponent.remainCoolTime > 0){
                        monster.attackComponent.remainCoolTime -= deltaTime;

                        System.out.println("업데이트된 공격 쿨타임 : " + monster.attackComponent.remainCoolTime);
                    }
                    continue;

                case MonsterActionType.ATTACK_TARGET :

                    System.out.println("타겟을 공격한다");

                    /* 모션 중계 */
                    server_to_client.motionMonsterDoAttack(
                            RMI_ID.getArray(worldMap.worldMapRMI_IDList.values()), RMI_Context.Reliable_Public_AES128,
                            monster.entityID, (short)finalTargetType, finalTargetID);

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



                    /* 공격 쿨타임을 초기화한다 */
                    monster.attackComponent.remainCoolTime = 1 / monster.attackComponent.attackSpeed;

                    System.out.println("초기화된 공격 쿨타임 : " + monster.attackComponent.remainCoolTime);

                    /* 현 몹에 대한 처리를 종료하고, 다음 몹으로 넘어간다 */
                    continue;

                case MonsterActionType.CHASE_TARGET :

                    System.out.println("타겟을 쫒아간다 ");

                    /** 타겟을 향해 이동할 지점을 구한다  */

                    Vector3 directionToTarget
                            = Vector3.normalizeVector(monsterPos, finalTargetPosition);
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

                    System.out.println("몬스터 이동 처리 연산중... ");

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

                                System.out.println("크리스탈 찾기 ");

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
                                System.out.println("크리스탈이 아닌 타겟");

                        }
                    }
                    else if( pathType == PathType.TO_MP){    /** 2020 01 14 경로 이탈 케이스 */

                        System.out.println("경로 이탈 케이스");

                        try{
                            targetMovePoint = worldMap.mpPathList.get(monster.entityID).get(pathIndex);
                        } catch(IndexOutOfBoundsException e){
                            System.out.println("배열 바운드 오류 발생!");
                        }


                        if(targetMovePoint == null){
                            System.out.println("널");
                        }
                        targetMovePos = targetMovePoint.getPixelPosition();

                        if(testMode){
                            System.out.println("몬스터의 경로 타입 :  " + pathType);

                            System.out.println("몬스터가 이동해야 할 이동 포인트 :  "
                                    + targetMovePoint.arrayX + ", " + targetMovePoint.arrayY);

                            System.out.println("몬스터가 이동해야 할 이동 포인트의 좌표 :  "
                                    + targetMovePos.x() + ", " + targetMovePos.z());
                        }


                    }
                    else{

                        targetMovePoint = worldMap.pathList.get(pathType).get(pathIndex);
                        targetMovePos = targetMovePoint.getPixelPosition();

                        if(testMode){
                            System.out.println("몬스터의 경로 타입 :  " + pathType);

                            System.out.println("몬스터가 이동해야 할 이동 포인트 :  "
                                    + targetMovePoint.arrayX + ", " + targetMovePoint.arrayY);

                            System.out.println("몬스터가 이동해야 할 이동 포인트의 좌표 :  "
                                    + targetMovePos.x() + ", " + targetMovePos.z());
                        }
                    }

                    /* 몹이 이동할 지점을 계산한다 */
                    Vector3 directionToMovePoint
                            = Vector3.normalizeVector(monsterPos, targetMovePos);
                    float movementSpeedToMovePoint
                            = deltaTime * (mobMoveSpeed + moveSpeedBonus) * moveSpeedRate;
                    Vector3 moveVectorToMovePoint
                            = directionToMovePoint.setSpeed(movementSpeedToMovePoint);


                    // 이동할 지점. 충돌판정 후 이동할 것임.
                    Vector3 movementPosToMovePoint = (Vector3) monsterPos.clone();
                    movementPosToMovePoint.movePosition(movementPosToMovePoint, moveVectorToMovePoint);

                    movedPosition.set(movementPosToMovePoint);

                    monster.velocityComponent.velocity = moveVectorToMovePoint; // 헐.. 이거 안해주니까 안됐었다니...

                    System.out.println("몬스터가 이동해야 할 좌표 :  "
                            + movedPosition.x() + ", " + movedPosition.z());

                    break;

            }


            /** 3. 이동한 경우, 이동 위치에 대해 충돌 판정 및 처리를 한다 */

            System.out.println("이동 판정처리 합니다 ");
            /* 계산된 좌표가 속한 타일의 타입을 확인, 이동 가능한 영역인지 불가능한 영역인지 판단한다 */
            boolean isMovableTile = MapFactory.moveCheck(worldMap.gameMap, movedPosition.x(), movedPosition.z());

            /** 2020 02 19 수정, 권령희 */
            if(isMovableTile){

                System.out.println("이동 가능한 타일이다 ");

                /** 타일은 밟고있는 다른 몬스터가 있는지 확인한다 */

                MapInfo currentTile = MapFactory.findTileByPosition(worldMap.gameMap, movedPosition.x(), movedPosition.z());

                boolean tileIsEmpty = isTileEmpty(worldMap, currentTile, monster.entityID);
                System.out.println("타일이 비어있는가? " + tileIsEmpty);

                if(tileIsEmpty){
                    /* 비어있는 타일의 경우, 기존 처리 그대로 해주면 됨 */
                    System.out.println("이동 가능하므로, 좌표에 반영합니다 ");

                    /* 계산된 좌표를 몹의 위치에 반영한다 */
                    monsterPos.set(movedPosition);

                    System.out.println("몬스터 좌표 :  "
                            + monsterPos.x() + ", " + monsterPos.z());

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

                            System.out.println("이동 포인트 타일에 도달함");


                            monster.monsterComponent.movePointIndex++;
                            if(pathType == PathType.TO_MP){
                                if(monster.monsterComponent.movePointIndex >= (worldMap.mpPathList.get(monster.entityID).size()) ){
                                    System.out.println("원래 경로로 돌아감");

                                    // 도달한 타겟이 속한 경로 및 인덱스를 찾는다
                                    int nextPathType = MapFactory.findPathTypeByMapInfo(worldMap, targetTile);
                                    int nextPathIndex = worldMap.pathList.get(nextPathType).indexOf(targetTile);

                                    // 몬스터 경로 업뎃
                                    monster.monsterComponent.movePathType = nextPathType;
                                    monster.monsterComponent.movePointIndex = nextPathIndex;

                                    System.out.println("돌아간 경로 타입 : " + nextPathType );
                                }

                            } else{
                                if(monster.monsterComponent.movePointIndex >= (worldMap.pathList.get(pathType).size()) ){
                                    // 마지막 path까지 다 왔다면, 목적지를..?? pathType NONE 으로 한다
                                    monster.monsterComponent.movePathType = PathType.NONE;
                                }
                            }

                        }
                    }
                }
                else{
                    /* 다른 몹이 밟고있는 경우 */

                    /** 현재 몹 위치의 비어있는 근처 타일을 찾는다 */

                    MapInfo mobPosTile = MapFactory.findTileByPosition(worldMap.gameMap, monsterPos.x(), monsterPos.z());
                    MapInfo nearEmptyTile = getNearEmptyTile(worldMap, mobPosTile, monsterPos);

                    if(nearEmptyTile == null){
                        /* 근처에 비어있는 타일이 없는 경우 */
                        // 가만히 있는다..
                    }
                    else {
                        /* 비어있는 타일이 존재하는 경우 */

                        /** 해당 타일을 시작으로, 기존 목적지까지 가는 길을 찾는다 */

                        // 기존 목적지 가져오기
                        int pathTypeBefore = monster.monsterComponent.movePathType;
                        int pathIndex = monster.monsterComponent.movePointIndex;
                        MapInfo targetTile = null;
                        switch (pathTypeBefore) {

                            case PathType.TO_MP:

                                int lastIndex = worldMap.mpPathList.get(monster.entityID).size() -1;
                                targetTile = worldMap.mpPathList.get(monster.entityID).get(lastIndex);

                                System.out.println("MP패스... , 인덱스 : " + lastIndex);
                                break;

                            case PathType.TOP:
                            case PathType.MIDDLE:
                            case PathType.BOTTOM:

                                targetTile = worldMap.pathList.get(pathTypeBefore).get(pathIndex);

                                System.out.println("3개패스... , 인덱스 : " + pathIndex);
                                break;
                            default:
                                System.out.println("논패스... , 인덱스 : " + pathIndex);
                        }

                        if(targetTile == null){
                            System.out.println("널");

                            Vector3 crystalPos = worldMap.crystalEntity.get(worldMap.crystalID).positionComponent.position;
                            targetTile = MapFactory.findTileByPosition(worldMap.gameMap, crystalPos.x(), crystalPos.z());
                        }
                        ArrayList<MapInfo> path = MapFactory.pathFind(worldMap, nearEmptyTile, targetTile);

                        /** 새로 얻은 경로와 목적지(MP)를 업데이트해준다*/

                        worldMap.mpPathList.put(monster.entityID, path);
                        monster.monsterComponent.movePathType = PathType.TO_MP;
                        monster.monsterComponent.movePointIndex = 0;

                    }

                }

            }
            else{

                // 2020 01 14 버전

                System.out.println("가까운 이동포인트 찾기");

                /* 현 위치를 기준으로 가까운 MOVE POINT를 검색한다 */
                MapInfo nearMovePoint = MapFactory.findNearMovePointVer20200213(worldMap, monsterPos);

                /* 몬스터의 MOVE POINT(Path Type, index)를 갱신한다 */
                /* A* 알고리즘으로 경로를 찾는다... */
                // 몹이 현재 속한 타일..

                MapInfo currentTilePoint = MapFactory.findTileByPosition(worldMap.gameMap, monsterPos.x(), monsterPos.z());
                ArrayList<MapInfo> tempPath = MapFactory.pathFind(worldMap, currentTilePoint, nearMovePoint);

                // 월드에 경로 등록
                worldMap.mpPathList.put(monster.entityID, tempPath);
                System.out.println("경로 수 : " + tempPath.size());

                // nearMovePoint 가 속한 경로 및 그 인덱스를 찾는다
                int pathType = PathType.TO_MP;
                int pathIndex = 0;

                System.out.println("근처 타일 :  " + nearMovePoint.arrayX + ", " + nearMovePoint.arrayY);
                System.out.println("근처 타일 경로 타입" + pathType);
                System.out.println("근처 타일 인덱스 :  " + pathIndex);

                // 갱신
                monster.monsterComponent.movePathType = pathType;
                monster.monsterComponent.movePointIndex = pathIndex;

            }


            /* 공격 쿨타임을 업데이트한다 */
            if(monster.attackComponent.remainCoolTime > 0){
                monster.attackComponent.remainCoolTime -= deltaTime;

                System.out.println("업데이트된 공격 쿨타임 : " + monster.attackComponent.remainCoolTime);
            }

        }

    }



    /** 2020 02 19 수, 권령희 */

    public static MapInfo getNearEmptyTile(WorldMap worldMap, MapInfo currentTile, Vector3 mobPos){

        MapInfo nearEmptyTile = null;

        /** 근처 타일들을 찾는다 */
        ArrayList<MapInfo> nearTiles = findNearTileList(worldMap, currentTile);

        /** 다른 몹이 위치해있는 타일들은 제거한다 */
        removeNonEmptyTile(worldMap, nearTiles);

        /** 전방 방향이 아닌 타일들도 제거한다 */
        //removeBackwardTile(worldMap, nearTiles, mobPos);

        /** 비어있는 주변 타일이 존재한다면, 그중 하나(첫번째)거를 골라 지정해준다 */
        if ( !nearTiles.isEmpty() ){
            nearEmptyTile = nearTiles.get(0);
        }

        return nearEmptyTile;
    }

    public static ArrayList<MapInfo> findNearTileList(WorldMap worldMap, MapInfo currentTile){

        ArrayList<MapInfo> nearTileList = new ArrayList<>();

        /* 주변타일의 인덱스를 구한다 */
        // X
        int startIndex_X = (int)currentTile.arrayX -1;
        int endIndex_X = (int)currentTile.arrayX +1;
        if(startIndex_X < 0){
            startIndex_X = 0;
        }
        if(endIndex_X > 99){
            endIndex_X = 99;
        }

        // Y
        int startIndex_Y = (int)currentTile.arrayY -1;
        int endIndex_Y = (int)currentTile.arrayY +1;
        if(startIndex_Y < 0){
            startIndex_Y = 0;
        }
        if(endIndex_Y > 99){
            endIndex_Y = 99;
        }

        /** 정해진 주변 타일 범위들을 돌면서.. ?? */
        for(int i=startIndex_X; i<=endIndex_X; i++){

            for(int j=startIndex_Y; j<=endIndex_Y; j++){

                if( (i==(int)currentTile.arrayX) && (j==(int)currentTile.arrayY) ) {
                    continue;
                }

                MapInfo mapInfo = worldMap.gameMap[i].mapInfos.get(j);
                if(mapInfo.canMove){
                    nearTileList.add(mapInfo);
                }

            }
        }

        return nearTileList;
    }

    public static void removeNonEmptyTile(WorldMap worldMap, ArrayList<MapInfo> nearTileList){

        for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()) {

            MonsterEntity monster = monsterEntity.getValue();
            if ((monster.hpComponent.currentHP <= 0)) {
                continue;
            }

            Vector3 mobPos = monster.positionComponent.position;
            MapInfo mobPosTile = MapFactory.findTileByPosition(worldMap.gameMap, mobPos.x(), mobPos.z());

            if (nearTileList.contains(mobPosTile)){
                nearTileList.remove(mobPosTile);
            }
        }

    }

    public static void removeBackwardTile(WorldMap worldMap, ArrayList<MapInfo> nearTileList, Vector3 mobPos){

        for(int i=0; i<nearTileList.size(); i++){

            MapInfo nearMap = nearTileList.get(i);
            Vector3 nearTilePos = nearMap.getPixelPosition();

            float betweenAngle = Vector3.getAngle(mobPos, Vector3.getTargetDirection( new Vector3(), nearTilePos));
            if(betweenAngle > 90){
                nearTileList.remove(nearMap);
                i--;
            }
        }

        System.out.println("최종 결과 사이즈 :" + nearTileList.size());

    }

    public static boolean isTileEmpty(WorldMap worldMap, MapInfo targetTile, int monsterEntityID){

        boolean isEmpty = true;

        for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()) {

            /** 0. 몬스터 정보 */
            MonsterEntity monster = monsterEntity.getValue();
            if ((monster.hpComponent.currentHP <= 0) || monster.entityID == monsterEntityID) {
                continue;
            }

            Vector3 mobPos = monster.positionComponent.position;
            MapInfo mobPosTile = MapFactory.findTileByPosition(worldMap.gameMap, mobPos.x(), mobPos.z());

            if (mobPosTile == targetTile) {
                isEmpty = false;
            }
        }

        return isEmpty;

    }


}
