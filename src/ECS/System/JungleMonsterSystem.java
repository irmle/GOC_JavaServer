package ECS.System;

import ECS.Classes.*;
import ECS.Classes.Type.MonsterActionType_ForEffect;
import ECS.Components.*;
import ECS.Entity.CharacterEntity;
import ECS.Entity.MonsterEntity;
import ECS.Factory.MapFactory;
import ECS.Factory.MonsterFactory;
import ECS.Game.GameDataManager;
import ECS.Game.WorldMap;
import ECS.Classes.Type.Jungle.*;
import Network.RMI_Classes.RMI_Context;
import Network.RMI_Classes.RMI_ID;
import Network.RMI_Common.RMI_ParsingClasses.EntityType;
import Network.RMI_Common.server_to_client;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 27
 * 업뎃날짜 : 오후 9:51 2020-04-08
 * 목    적 :
 * 업뎃내용 :
 */
public class JungleMonsterSystem {

    WorldMap worldMap;
    float deltaTime;

    public JungleMonsterSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
        deltaTime = 0f;
    }

    public void onUpdate(float deltaTime){

        this.deltaTime = deltaTime;

        for(int i=0; i<worldMap.jungleMonsterSlots.size(); i++){

            /* 정글몹 슬롯 하나 가져오기 */
            JungleMonsterSlot slot = worldMap.jungleMonsterSlots.get(i);
            //MonsterEntity jungleMob = worldMap.jungleMonsterSlotList.get(slot);
            MonsterEntity jungleMob = worldMap.monsterEntity.get(slot.monsterID);

            // 정글몹 종류별로 효과 잘 들어가는지 테스트하기 위함.
            // 2020 03 06, 클라이언트 상황에 맞춰서.. 2~4에 해당하는 몬스터만 일단 처리하도록 하기 위해.
            if(slot.jungleMobType == JungleMobType.DEVIL){
                //continue;
            }

            boolean isReturnCondition;

            int slotState = slot.monsterState;
            switch (slotState) {

                case JungleMobState.EMPTY :
//                    System.out.println(" EMPTY 상태 ");

                    /* 슬롯에 지정된 타입의 몬스터를 생성한다 */
                    createNewMonster(slot);
                    slot.resetPatience();

                    /* 상태 변환 : EMPTY >> IDLE */
                    slot.setMonsterState(JungleMobState.IDLE);

                    break;

                case JungleMobState.IDLE :

//                    System.out.println("IDLE 상태 ");
                    /*
                     * 체력이 MAX 이하라면 체력을 회복한다 // 나중에 변경될지도 오후 9:51 2020-04-08
                     * 만약 IDLE 상태에 있다가 캐릭터로부터 데미지를 입는다면, 상태가 IDLE >> TARGET_INDICATE로 변하게 됨.
                     */
                    recoverHpIfNeed(slot);

                    break;

                case JungleMobState.TARGET_INDICATE :

//                    System.out.println("TARGET_INDICATE 상태 ");
                    /*
                     * 시야에 적이 있는지 검색한다
                     * if 없으면
                     *  상태변환 : INDICATE >> RETURN
                     *  break;
                     * else 있으면
                     *  몹의 타겟을 위에서 찾은 대상으로 지정해준다
                     * 상태변환 : INDICATE >> TRACE;
                     */
                    MonsterEntity mob = worldMap.monsterEntity.get(slot.monsterID);


                    /** 귀환 조건을 먼저 체크 후, 조건을 만족한다면 상태를 변경한다 */
                    isReturnCondition = ckeckMonsterReturningCondition(slot);
                    if(isReturnCondition){

//                        System.out.println("귀환 조건을 만족하여 상태를 변경합니다. ");

                        slot.setMonsterState(JungleMobState.RETURN_TO_SP);
                        break;
                    }


                    /** 시야에 인식가능한 대상이 있는지 검색한다 */
                    CharacterEntity target = findTarget(slot);
                    if(target == null){

//                        System.out.println("시야에 인식 가능한 대상이 없습니다 ");

                        /** 인식 가능한 대상이 없는 경우, 귀환 모드로 들어간다 */
                        slot.setMonsterState(JungleMobState.RETURN_TO_SP);
                        break;
                    }

                    /** 검색된 타겟을 지정해준다 */
                    MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);

                    int oldTarget = monster.monsterComponent.targetID;
                    if((oldTarget != 0) && (oldTarget != target.entityID)){
                        slot.reducePatience();
                    }

                    monster.monsterComponent.targetID = target.entityID;

//                    System.out.println("지정된 타겟 : " + target.entityID);

                    /** 타겟 추적 상태로 이동한다 */
                    slot.setMonsterState(JungleMobState.TARGET_TRACE);
                    // 타겟이 지정된 경우, break를 걸지 않고 바로 아래의 TRACE 처리를 이어나가도록 한다

                case JungleMobState.TARGET_TRACE :
//                    System.out.println(" TARGET_TRACE 상태 ");

                    /**
                     * 지정된 타겟을 추적하는 것이 가능한지 확인한다
                     * ㄴ 대상의 체력
                     * ㄴ 대상과의 거리
                     * if 죽었거나 인식범위 내에 없다면
                     *  상태변환 : TRACE >> INDICATE (다시 올라감)
                     *  break;
                     *
                     * if 대상이 공격 가능한 거리 내에 있다면
                     *  상태변환 : TRACE >> ATTACK
                     * else
                     *  // 아.. 이동 가능여부도 체크해야 할 듯
                     *  현재 위치로부터 타겟까지 이르는 루트를 구한다
                     *  구한 루트의 첫 번째 칸을 목적지로 해서 이동한다
                     *  break;
                     *
                     */

                    /** 귀환 조건을 먼저 체크 후, 조건을 만족한다면 상태를 변경한다 */
                    isReturnCondition = ckeckMonsterReturningCondition(slot);
                    if(isReturnCondition){

//                        System.out.println("귀환 조건을 만족하여 상태를 변경합니다. ");

                        slot.setMonsterState(JungleMobState.RETURN_TO_SP);
                        break;
                    }


                    boolean isAbleToTrace = checkIsAbleToTraceTarget(slot);
                    if( !isAbleToTrace ){

                        /* 인내심 */
                        //slot.reducePatience();

                        slot.setMonsterState(JungleMobState.TARGET_INDICATE);
//                        System.out.println("타겟 추적이 불가능하므로 INDICATE로 상태변환함");
                        break;
                    }

                    boolean targetIsInAttackRange = checkTargetIsInAttackRange(slot);
                    if(targetIsInAttackRange){

                        slot.setMonsterState(JungleMobState.TARGET_ATTACK);
//                        System.out.println("타겟 공격이 가능하므로 ATTACK로 상태변환함");
                        // 타겟이 공격 가능한 범위 안에 있는 경우, 타겟 공격 단계로 바로 넘어간다
                    }
                    else{

//                        System.out.println("아직 공격 불가능하므로, 이동추적 ");
                        /* 아직 타겟이 공격 범위에 들지 않은 경우, 타겟을 향해 이동한다 */
                        moveToTargetPos(slot);  // ㅋㅋ... 왤케 불안하지
                        break;
                    }

                case JungleMobState.TARGET_ATTACK :

//                    System.out.println(" TARGET_ATTACK 상태 ");
                    /**
                     * 대상이 공격 가능한 거리에 있는지, 타게팅 가능한지 죽었는지 살아있는지 등을 체크한다
                     * if 범위 밖이라면
                     *  상태변환 : ATTACK >> TRACE
                     *  break;
                     * else(사실 else 안붙여도 될 듯)
                     *  상대의 타게팅 가능 여부를 판단한다
                     *  몹 자신의 공격 쿨타임 등을 확인한다, 공격 가능 상태인지도..
                     *  if 공격 가능하다면
                     *      공격 처리를 한다
                     *      공격 쿨타임 초기화 처리를 한다
                     *  else
                     *      공격 쿨타임 감소 처리를 한다
                     *  break;
                     */

                    /** 귀환 조건을 먼저 체크 후, 조건을 만족한다면 상태를 변경한다 */
                    isReturnCondition = ckeckMonsterReturningCondition(slot);
                    if(isReturnCondition){

//                        System.out.println("귀환 조건을 만족하여 상태를 변경합니다. ");

                        slot.setMonsterState(JungleMobState.RETURN_TO_SP);
                        break;
                    }

                    boolean isTargetAlive = targetIsAlive(slot);
                    if(!isTargetAlive){
                        slot.setMonsterState(JungleMobState.TARGET_INDICATE);
                        break;
                    }

                    boolean isTargetInAttackRange = checkTargetIsInAttackRange(slot);
                    if(!isTargetInAttackRange){
                        slot.setMonsterState(JungleMobState.TARGET_TRACE);
                        break;
                    }

                    boolean isAbleToAttack = checkIsAbleToAttackTarget(slot);
                    if(isAbleToAttack){

                        /* 공격 처리를 한다 */
                        attackTarget(slot);
                    }
                    else{
                        /* 공격 쿨타임 감소 처리를 한다 */
                        reduceAttackCoolTime(slot, deltaTime);
                    }

                    slot.setMonsterState(JungleMobState.TARGET_INDICATE);
                    break;


                case JungleMobState.RETURN_TO_SP :

//                    System.out.println(" RETURN_TO_SP 상태 ");
                    /**
                     * 현재 위치의 타일을 검사한다
                     * if SP에 속한 타일이라면? (목적지에 도달)
                     *  상태 변화 : RETURN >> IDLE
                     *  // 목적지 중심포인트까지 이동시키기.. 해도 되기는 할 듯.
                     * else
                     *  길찾기 시전
                     *  ..해서 나온 첫 번째 칸을 목적지로 삼고 이동한다
                     *      // 여기는 고정 위치에 대한 길찾기니까..
                     *      // 그냥 루트 딱 박아놓고 따라가다가, 충돌했을 때에만 다시 길찾기 시전하게 해도 되기는 할듯
                     *      // 근데 귀찮으니까 일단은 걍 계속 시전하게 하다가, 만~약에 부하가 걸린다 싶으면 그 때 개선하던지.
                     * break;
                     */

                    recoverHpIfNeed(slot);

                    boolean isArrived = checkIfArrivedSpawnPoint(slot);
                    if(isArrived){
                        slot.setMonsterState(JungleMobState.IDLE);
                        slot.resetPatience();
                        break;
                    }

                    /* 도착하지 않앗다면, 스폰지점을 향해 이동 처리를 함 */
                    moveToSpawnPoint(slot);
                    break;

                case JungleMobState.DIED :

//                    System.out.println(" DIED 상태 ");
                    /**
                     * 부활시간 세팅해주기
                     * 상태변환 : DIED >> REGEN_WAITING
                     * break;
                     */

                    slot.resetRemainedRegenTime();
                    slot.setMonsterState(JungleMobState.REGEN_WAITING);
                    //break;

                case JungleMobState.REGEN_WAITING :

//                    System.out.println(" REGEN_WAITING 상태 ");

                    /**
                     * 리젠 쿨타임 체크함
                     * if 시간 지났으면
                     *      기존에 슬롯에 매칭돼있던 몹 정보 참고해서(??)
                     *      새로 몹 생성하고.
                     *      그거를 슬롯에 새로 매칭해주고.
                     *      상태변환 : REGEN >> IDLE
                     *      break;
                     * else
                     *      리젠타임 깎아주고.
                     */

                    if(slot.remainedRegenTime <= 0){

                        /**
                         * 수정
                         * 오후 6:59 2020-04-08
                         * 몹 리젠 처리를 담당하는, 껍데기 매서드 하나 호출하도록 하기.
                         */
                        regenMonster(slot);
                        slot.setMonsterState(JungleMobState.IDLE);
                    }
                    else {
                        slot.reduceRemainedRegenTime(deltaTime);
//                        System.out.println("남은 리젠시간 : " + slot.remainedRegenTime);
                    }

                    break;

            }


        }



    }

    /**
     * EMPTY 상태일 때 호출하는 함수
     *  ++ 생각해보니까, REGEN 때도 호출하겠다. 그대로 써도 문제 없을 듯.. 일단은 말임.
     *      나중에 뭐 몹이 죽은 횟수에 따라 더 강해진다거나 레벨이 올라간다거나.. 한다면 조금 고치긴 해야겠지만.
     */
    public void createNewMonster(JungleMonsterSlot slot){

//        System.out.println("정글몹을 생성합니다");

        /** 몹을 생성함 */
        MonsterEntity newJungleMob = MonsterFactory.createJungleMonster(slot.jungleMobType, worldMap, slot.regenCount);

        /** 새로 ID 할당받음 */
        int newEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();
        newJungleMob.entityID = newEntityID;

        /** 슬롯이랑도 매핑해줌 */
        slot.monsterID = newEntityID;


        /** 몹 위치를, 슬롯 위치랑 매칭시켜주고 */
        Vector3 spawnPos = slot.slotPoint.getPixelPosition();
        //newJungleMob.positionComponent.position.set((Vector3) spawnPos.clone());
        newJungleMob.positionComponent.position.set(spawnPos.x(), spawnPos.y(), spawnPos.z());

  /*      System.out.println("슬롯 위치 : " + spawnPos.x() + ", " + spawnPos.z());
        System.out.println("몹 위치 : " + newJungleMob.positionComponent.position.x() + ", " + newJungleMob.positionComponent.position.z());
*/
        /** 월드에도 매핑해주고 */
        worldMap.jungleMonsterSlotHashMap.put(newEntityID, slot);
        worldMap.jungleMonsterSlotMonsterEntityHashMap.put(slot, newJungleMob);
        worldMap.requestCreateQueue.add(newJungleMob);

    }

    /**
     * INDICATE 상태일 때, 시야에 타게팅가능한 대상이 있는가?? 검색하기
     */
    public CharacterEntity findTarget(JungleMonsterSlot slot){

        CharacterEntity target = null;

        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        PositionComponent monsterPos = monster.positionComponent;
        float monsterSight = monster.sightComponent.lookRadius;

//        System.out.println("몹의 위치 : " + monsterPos.position.x() + ", " + monsterPos.position.z());

        float minDistance = monsterSight;   // 몬스터가 인지 가능한 거리를 시작으로, 제일 가까이 있는 녀석을 찾을 것임
        for(HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()){

            CharacterEntity currentTarget = characterEntity.getValue();

            /* 죽은 대상은 제외한다 */
            HPComponent targetHP = currentTarget.hpComponent;
            if(targetHP.currentHP <= 0){
                continue;
            }

            /* 대상과의 거리를 구한다 */
            PositionComponent targetPos = currentTarget.positionComponent;
            float currentDistance = Vector3.distance(monsterPos.position, targetPos.position);
  //          System.out.println("타겟의 위치 : " + targetPos.position.x() + ", " + targetPos.position.z());

    //        System.out.println("타겟과의 거리 : " + currentDistance);

            if(currentDistance <= minDistance){

                minDistance = currentDistance;
                target = currentTarget;
            }

        }

        return target;

    }

    /**
     * 지정된 타겟을 (계속) 추적하는 것이 가능한지
     * 1. 대상이 살아있는지
     * 2. 대상이 인식 가능한 거리에 존재하는지
     */
    public boolean checkIsAbleToTraceTarget(JungleMonsterSlot slot){

        //System.out.println("checkIsAbleToTraceTarget");
        boolean ableToTrace = true;

        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        PositionComponent monsterPos = monster.positionComponent;
        float monsterSight = monster.sightComponent.lookRadius;

        CharacterEntity target = worldMap.characterEntity.get(monster.monsterComponent.targetID);

        HPComponent targetHP = target.hpComponent;
        if(targetHP.currentHP <= 0){
            ableToTrace = false;
        }
        else{

            PositionComponent targetPos = target.positionComponent;

            float distance = Vector3.distance(monsterPos.position, targetPos.position);
            if(distance > monsterSight){
                ableToTrace = false;
            }
        }

        //System.out.println("리턴값 : " + ableToTrace);
        return ableToTrace;

    }

    /**
     * 대상이 공격 가능한 거리에 '있는지' 판단
     */
    public boolean checkTargetIsInAttackRange(JungleMonsterSlot slot){

        boolean insideAttackRage = false;

        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        PositionComponent monsterPos = monster.positionComponent;
        float attackRange = monster.attackComponent.attackRange;

        CharacterEntity target = worldMap.characterEntity.get(monster.monsterComponent.targetID);
        PositionComponent targetPos = target.positionComponent;

        float distance = Vector3.distance(monsterPos.position, targetPos.position);
        if(distance <= attackRange){
            insideAttackRage = true;
        }

        return insideAttackRage;

    }


    /**
     * 현재 타일을 구해, 목적지까지의 길을 탐색하고 그쪽으로 이동하는 처리
     */
    public void moveToTargetPos(JungleMonsterSlot slot){

        /* 몹 정보 */
        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        if(monster == null){
            //System.out.println("몹정보가 널.. moveToTargetPos");
        }
        PositionComponent monsterPos = monster.positionComponent;
        Vector3 currentPos = monsterPos.position;

        if(monster.conditionComponent.isStunned || monster.conditionComponent.isDisableMove || monster.conditionComponent.isAriborne){
            return;
        }

        /* 현재 위치가 속한 타일을 구하기 */
        MapInfo currentTile = MapFactory.findTileByPosition(worldMap.gameMap, currentPos.x(), currentPos.z());
/*
        System.out.println("몬스터 위치 : " + currentPos.x() + ", " + currentPos.z());
        System.out.println("몬스터 위치 타일 : " + currentTile.arrayX + ", " + currentTile.arrayY);
*/

        /* 타겟 정보 */
        CharacterEntity target = worldMap.characterEntity.get(monster.monsterComponent.targetID);
        PositionComponent targetPos = target.positionComponent;
        Vector3 targetCurrentPos = targetPos.position;

        /* 타겟이 속한 타일 찾기 */
        MapInfo targetTile = MapFactory.findTileByPosition(worldMap.gameMap, targetCurrentPos.x(), targetCurrentPos.z());
/*
        System.out.println("타겟 위치 : " + targetCurrentPos.x() + ", " + targetCurrentPos.z());
        System.out.println("타겟 위치 타일 : " + targetTile.arrayX + ", " + targetTile.arrayY);
*/

        /* 경로 구하기 */
        ArrayList<MapInfo> pathToTarget = MapFactory.pathFindForJungle(worldMap, currentTile, targetTile, target.entityID);
//        System.out.println("경로 사이즈 : " + pathToTarget.size());
        /*if(pathToTarget.get(1) == null){
            System.out.println("또 널이냐.. moveToTargetPos");
        }*/
        if(pathToTarget.size() == 1){

            /* 이동 처리하기 */
            moveTo(monster, pathToTarget.get(0));

        }
        else{

            /* 이동 처리하기 */
            moveTo(monster, pathToTarget.get(1));

        }


        /* 이동 처리하기 */
        //moveTo(monster, pathToTarget.get(1));


    }


    public void moveTo(MonsterEntity monster, MapInfo targetTile){

        //System.out.println("moveTo");
        /* 몹 정보 */
        PositionComponent monsterPos = monster.positionComponent;
        Vector3 currentPos = monsterPos.position;
        //System.out.println("몬스터 위치 : " + currentPos.x() + ", " + currentPos.z());

        float mobMoveSpeed = monster.velocityComponent.moveSpeed;
        float moveSpeedBonus = monster.conditionComponent.moveSpeedBonus;
        float moveSpeedRate = monster.conditionComponent.moveSpeedRate;

        /*System.out.println("몬스터 속도 : " + mobMoveSpeed);
        System.out.println("몬스터 속도보너스 : " + moveSpeedBonus);
        System.out.println("몬스터 속도비율 : " + moveSpeedRate);

        System.out.println("타겟 위치 : " + targetTile.getPixelPosition().x() + ", " +
                targetTile.getPixelPosition().z());
        System.out.println("타겟 위치 타일 : " + targetTile.arrayX + ", " + targetTile.arrayY);
*/
        Vector3 targetPos = targetTile.getPixelPosition();
        Vector3 directionToTarget
                = Vector3.normalizeVector(currentPos, targetPos);
        //System.out.println("단위백터 : " + directionToTarget.x() + ", " + directionToTarget.z());
        float movementSpeedToTarget
                = 0.1f * (mobMoveSpeed + moveSpeedBonus) * moveSpeedRate;
        Vector3 moveVectorToTarget
                = directionToTarget.setSpeed(movementSpeedToTarget);

        Vector3 movementPosToTarget = (Vector3) currentPos.clone();
        movementPosToTarget.movePosition(movementPosToTarget, moveVectorToTarget);

        //System.out.println("이동 위치 : " + movementPosToTarget.x() + ", " + movementPosToTarget.z());

        currentPos.set(movementPosToTarget);

    }

    /**
     * 현재 타일을 구해, 목적지까지의 길을 탐색하고 그쪽으로 이동하는 처리
     */
    public void moveToSpawnPoint(JungleMonsterSlot slot){

        /* 몹 정보 */
        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        PositionComponent monsterPos = monster.positionComponent;
        Vector3 currentPos = monsterPos.position;

        if(monster.conditionComponent.isDisableMove || monster.conditionComponent.isStunned){
            return;
        }


        /* 현재 위치가 속한 타일을 구하기 */
        MapInfo currentTile = MapFactory.findTileByPosition(worldMap.gameMap, currentPos.x(), currentPos.z());

        /* 스폰지점 타일 */
        MapInfo targetTile = slot.slotPoint;

        /* 경로 구하기 */
        ArrayList<MapInfo> pathToTarget = MapFactory.pathFindForJungle(worldMap, currentTile, targetTile, 0);

        /* 이동 처리하기 */
        moveTo(monster, pathToTarget.get(1));


    }



    public boolean targetIsAlive(JungleMonsterSlot slot){

        boolean isTargetAlive = true;

        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        CharacterEntity target = worldMap.characterEntity.get(monster.monsterComponent.targetID);

        HPComponent targetHP = target.hpComponent;
        if(targetHP.currentHP <= 0){
            isTargetAlive = false;
        }

        return isTargetAlive;
    }


    public boolean checkIsAbleToAttackTarget(JungleMonsterSlot slot){

        boolean ableToAttack = true;

        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        ConditionComponent monsterCond = monster.conditionComponent;
        AttackComponent monsterAttack = monster.attackComponent;

        CharacterEntity target = worldMap.characterEntity.get(monster.monsterComponent.targetID);
        ConditionComponent targetCond = target.conditionComponent;

        if(monsterAttack.remainCoolTime > 0){
            ableToAttack = false;
        }

        if(targetCond.isUnTargetable){
            ableToAttack = false;
        }

        if(monsterCond.isDisableAttack){
            ableToAttack = false;
        }

        return ableToAttack;

    }

    public void attackTarget(JungleMonsterSlot slot){

        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        ConditionComponent monsterCond = monster.conditionComponent;
        AttackComponent monsterAttack = monster.attackComponent;

        CharacterEntity target = worldMap.characterEntity.get(monster.monsterComponent.targetID);

        /* 모션 중계 */
        server_to_client.motionMonsterDoAttack(
                RMI_ID.getArray(worldMap.worldMapRMI_IDList.values()), RMI_Context.Reliable_Public_AES128,
                monster.entityID, (short)EntityType.CharacterEntity, target.entityID);

        /* 대상에게 데미지 버프를 넣어줌 */
        /*ConditionFloatParam damageParam = new ConditionFloatParam(ConditionType.damageAmount, monsterAttack.attackDamage);
        BuffAction damageBuff = SkillFactory.createDamageBuff(damageParam, monster.entityID, monster.entityID);
        target.buffActionHistoryComponent.conditionHistory.add(damageBuff);
*/
        target.buffActionHistoryComponent.conditionHistory.add(
                MonsterFactory.createMonsterActionEffect(
                        MonsterActionType_ForEffect.MONSTER_ATTACK, "데미지", monster, monster.entityID));


        /* 공격 쿨타임을 초기화한다 */
        monster.attackComponent.remainCoolTime = 1 / monster.attackComponent.attackSpeed;


    }

    public void reduceAttackCoolTime(JungleMonsterSlot slot, float deltaTime){

        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        ConditionComponent monsterCond = monster.conditionComponent;
        AttackComponent monsterAttack = monster.attackComponent;

        /* 공격 쿨타임을 감소시킨다 */
        monster.attackComponent.remainCoolTime -= deltaTime;

    }

    public boolean checkIfArrivedSpawnPoint(JungleMonsterSlot slot){

        boolean arrived = false;

        /* 몹 정보 */
        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        PositionComponent monsterPos = monster.positionComponent;
        Vector3 currentPos = monsterPos.position;

        /* 현재 위치가 속한 타일을 구하기 */
        MapInfo currentTile = MapFactory.findTileByPosition(worldMap.gameMap, currentPos.x(), currentPos.z());

        if(currentTile == slot.slotPoint){
            arrived = true;
        }

        return arrived;
    }

    public static JungleMonsterSlot findJungleSlotByMonsterID(WorldMap worldMap, int monsterID){

        JungleMonsterSlot slot = null;

        for(int i=0; i< worldMap.jungleMonsterSlots.size(); i++){

            JungleMonsterSlot currentSlot = worldMap.jungleMonsterSlots.get(i);
            if(currentSlot.monsterID == monsterID){
                slot = currentSlot;
                break;
            }
        }

        return slot;

    }

    /**
     * 주석 작성
     * 오전 1:58 2020-04-07
     * 목적 : 몹 리젠 시 처리를 수행하기위한 틀 매서드
     * 처리 :
     *      다음에 생성될 정글몹을 지정하는 처리를 한다.
     *      ( 현재 월드맵에 있는..) decidedJungleMob() 매서드를 호출하여 랜덤으로 결정한다
     *      결정된 몹의 타입을 슬롯에 지정해준다
     */
    public void regenMonster(JungleMonsterSlot slot){

        //System.out.println("몸 리벤 처리 함 ");

        /** GDM 데이터 참조 */
        HashMap<Integer, MonsterInfo> jungleMonsterInfoList = GameDataManager.jungleMonsterInfoList;
        MonsterInfo jungleInfo;

        /** 다음에 생성될 정글 몬스터를 랜덤으로 결정한다 */
        int newType = worldMap.decideJungleMonsterToBeSpawned();
        //System.out.println("기존 타입 : " + slot.jungleMobType);
        //System.out.println("결정된 타입 : " + newType);

        /** 생성될 몹 타입을 슬롯에 지정한다 */
        jungleInfo = jungleMonsterInfoList.get(newType);
        slot.setJungleMonsterInfo(newType, jungleInfo.regenTime * 60f);

        /** 리젠카운트 세팅 */
        slot.increaseRegenCount();

        /** 지정된 몹을 생성한다 */
        // onUpdate - regenWaiting 케이스의 create~() 매서드를 지우고, 여기서 호출하도록 함.
        createNewMonster(slot);

        slot.resetPatience();

    }

    /**
     * 주석 작성
     * 오전 2:09 2020-04-07
     * 목적 : 몬스터가(IDLE 상태가 아닐 경우, ) 귀환 조건을 만족하는지 검사하는 매서드
     *      이 매서드의 caller 가 true 값을 반환받을 때 마다, 슬롯의 상태를 RETURN으로 변경해주도록 한다
     *
     * 검사 목록 :
     *      1) currentHP가 maxHP의 30% 이하로 남음
     *      2) 몹의 인내심 게이지가 0 이하로 떨어짐
     *      3) 몹이 스폰 지점으로부터 시야 이상으로 멀어짐
     *
     *      위 조건을 하나라도 만족 시, true값을 리턴한다
     *
     * 추가 작성해야 할 것 :
     *      -- 정글몹 슬롯 클래스에, "인내심" 추가 // 일단 디폴트로 5 값을 갖도록 한다. 이거는 기획이 확정되어야 파일에 넣어 불러오든 할 듯. 몹별로 인내심이 다르다던가..
     *      -- ㄴ 인내심을 1씩 깎는 매서드를 하나 추가
     *      -- 정글몹의 target 이 변경되는 시점마다, 위 매서드를 호출하도록 함.
     *
     *      -- 몹이 IDLE, RETURN 상태일 때
     *              체력이 max 이하라면 체력 값을 회복하는 버프를 넣어준다.
     *
     * 2020 04 21 수정 :
     * 체력이 일정이하 낮아지더라도, 귀환하지 않도록 바꿔달라는 요구사항이 있어서 적용.
     *
     */
    public boolean ckeckMonsterReturningCondition(JungleMonsterSlot slot){

        boolean isReturnCondition = false;

        /** 참조 데이터 */
        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        HPComponent monsterHP = monster.hpComponent;
        Vector3 monsterPos = monster.positionComponent.position;
        Vector3 spawnPoint = slot.slotPoint.getPixelPosition();

        float monsterLookRadius = monster.sightComponent.lookRadius;

        /** 체력 검사 */
        //float HP_RATE_FOR_RETURNING = 30f;
        //boolean isHPLow = ( monsterHP.checkCurrentHpPercentage() < HP_RATE_FOR_RETURNING ) ? true : false;
        //System.out.println("남은 체력 : " + monsterHP.checkCurrentHpPercentage());

        /** 인내심 테스트 */
        boolean isNoPatience = slot.checkMonsterIsNotPatient();
        //System.out.println("인내심 : " + slot.patience);

        /** 너무 멀리왔나? */
        float distnaceFromSP =  Vector3.distance(spawnPoint, monsterPos);
        boolean isFarFromHome = (distnaceFromSP > monsterLookRadius) ? true : false;
        //System.out.println("거리 : " + distnaceFromSP + ", 시야 : " + monsterLookRadius);


        /** 결과 판단 */
        isReturnCondition = ( isNoPatience || isFarFromHome );


        return isReturnCondition;

    }

    /**
     * -- 업뎃날짜 :
     *      # 오후 9:33 2020-04-08
     *
     * -- 기    능 :
     *
     *      # 정글 몹의 체력 회복 조건을 검사한 후( MAX 이하라면), 체력을 회복시킨다.

     *
     */
    public void recoverHpIfNeed(JungleMonsterSlot slot){

        /** 몬스터 검색 */
        MonsterEntity monster = worldMap.monsterEntity.get(slot.monsterID);
        BuffActionHistoryComponent monsterBuffList = monster.buffActionHistoryComponent;
        HPComponent monsterHp = monster.hpComponent;

        /** 회복 효과를 받아야 하는 조건인지 검사 */
        boolean needRecovery = monsterHp.checkIfNeedHpRecovery();
        if(needRecovery){

            //System.out.println("정글 회복 효과를 적용합니다 ");

            /** 회복효과 생성 및 적용 */
            monsterBuffList.conditionHistory.add(
                    MonsterFactory.createMonsterActionEffect(
                            MonsterActionType_ForEffect.JUNGLE_MOB_RETURN, "체력회복", monster, monster.entityID));

        }


        return;

    }

    /*******************************************************************************************************************/
    /**
     * 2020 04 18 새벽 작성
     * 필요 데이터를 GDM으로부터 클론하여 사용하게끔, 초반에 미리 복사해두는 처리
     */

    /**
     * 기    능 : 월드맵에서 필요로 하는 데이터를, GDM에서 복사해온다.
     * 처    리 :
     *      WorldMap에서 필요로 하는 GDM 데이터는 다음과 같다
     *      -- 정글몹정보 목록
     *      -- 웨이브별 등장 몹 목록
     *
     */
    public void getNeedDataFromGDM(){

        /* 초기화 처리 */


        /* 정글몹 정보 목록을 복사한다 */
        bringJungleMobInfoListFromGDM();

    }

    public void bringJungleMobInfoListFromGDM(){

        HashMap<Integer, MonsterInfo> jungleMonsterInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, MonsterInfo> jungleMonsterInfo : GameDataManager.jungleMonsterInfoList.entrySet()){

            int jungleKey = jungleMonsterInfo.getKey();
            MonsterInfo jungleValue = jungleMonsterInfo.getValue();
            jungleMonsterInfoList.put(jungleKey, jungleValue.clone());

        }

    }





}
