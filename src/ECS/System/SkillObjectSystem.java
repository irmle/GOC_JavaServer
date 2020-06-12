package ECS.System;

import ECS.Classes.*;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Type.SkillAreaType;
import ECS.Classes.Type.SkillType;
import ECS.Components.*;
import ECS.Entity.*;
import ECS.Factory.SkillFactory;
import ECS.Game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 업뎃날짜 : 2020 01 31 금 권령희
 * 업뎃내용 : 가렌E 예외처리 추가.
 *            마법사 라이트닝 스킬을 구현하기 위한 매서드들을 여기에 구현, 여기에 있는 거를 일단은 스킬 시스템에서 사용해보고,
 *            추후 장판으로 바꿔 구현하던지 할 것임.
 *            마
 *            마법사 프로즌빔 콘형 타입의 범위계산 로직 추가
 *
 *            충돌 시 처리 ; 데미지 -> 버프로 변경해줌
 */
public class SkillObjectSystem {

    WorldMap worldMap;

    public SkillObjectSystem(WorldMap worldMap) {

        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime)
    {
        //System.out.println("처리 할 스킬 오브젝트의 갯수 : " + worldMap.skillObjectEntity.size());

        boolean doOldVersion = false;

        /* 스킬 오브젝트 갯수만큼 반복한다 */
        for ( HashMap.Entry<Integer, SkillObjectEntity> skillObject : worldMap.skillObjectEntity.entrySet()){

            /* 스킬 오브젝트 정보 */

            SkillObjectEntity skillObjectEntity = skillObject.getValue();

            SkillObjectComponent skillObjectComponent = skillObject.getValue().skillObjectComponent;

            if(skillObjectComponent.skillObjectDurationTime <= 0){
                //continue;
            }


            /** 2020 04 02 */
            // 스킬 시전자 및 스킬 레벨 정보를 구한다
            CharacterEntity skillUser = worldMap.characterEntity.get(skillObjectComponent.userEntityID);
            SkillSlot skillSlot = SkillFactory.findSkillSlotBySkillType(worldMap, skillUser, skillObjectComponent.createdSkillType);

            int skillLevel = skillSlot.skillLevel;
            int skillType = skillObjectComponent.createdSkillType;

            /************************************************************************************************************/


            /* 스킬 오브젝트의 버프 정보 */
            BuffAction buffInfo = skillObjectComponent.buffAction;

            //System.out.println("스킬 오브젝트의 지속 시간 : " + skillObjectComponent.skillObjectDurationTime);

            /** 스킬 오브젝트의 지속 여부를 판단한다 */
            if(skillObjectComponent.skillObjectDurationTime <= 0f){  /* 오브젝트의 지속 시간이 끝났다면 */

                /* 스킬 오브젝트를 삭제한다 */
              //  System.out.println("스킬 오브젝트의 지속 시간이 다 되어, 오브젝트를 삭제합니다. ");

                /* 오브젝트 앤티티를 삭제하는 요청 큐에 추가 */
                worldMap.requestDeleteQueue.add(skillObject.getValue());    // 여기는. 깊은 복사 필요 없나??


                /** 회오리 */

                if(skillObjectComponent.createdSkillType == SkillType.KNIGHT_TORNADO){

                    for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()) {

                        MonsterEntity monster = monsterEntity.getValue();

                        for (int k = 0; k < monster.buffActionHistoryComponent.conditionHistory.size(); k++) {

                            BuffAction currentBuff = monster.buffActionHistoryComponent.conditionHistory.get(k);

                            if (currentBuff.skillType == skillObject.getValue().skillObjectComponent.createdSkillType) {

                                Vector3 targetPos = monster.positionComponent.position;

                                //targetPos.set(targetPos.x(), 0f, targetPos.z());

                            }

                        }
                    }

                }

                /** 효과들.. 삭제해주는 처리가 있어야 할 듯 ?? */




                continue;

            }
            else { /* 오브젝트의 지속 시간이 남았다면 */

                //System.out.println("스킬 오브젝트의 남은 지속시간을 업데이트합니다 ");
                /* 오브젝트의 지속 시간을 깎는다 */
                skillObjectComponent.skillObjectDurationTime -= deltaTime;

                //System.out.println("남은 지속시간 : " + skillObjectComponent.skillObjectDurationTime);

                /** 메테오 스킬의 경우 예외처리... ==>> 나중에, case문으로 대체할 것 */
                if (skillObjectComponent.createdSkillType == SkillType.MAGICIAN_METEOR) {

                  //  System.out.println("메테오의 지속시간이 남았습니다! ");


                    /* 스킬레벨테이블에서, 스킬의 지속시간을 얻는다 */
                    float skillDuration = SkillFactory.skillInfoPerLevelLIST.get(SkillType.MAGICIAN_METEOR).get(skillLevel).durationTime;

                    /* 판정,, (?) */
                    float elapsedTime = skillObjectComponent.skillObjectDurationTime - skillDuration;

                    //System.out.println("메테오 사용 후 경과 시간 : " + elapsedTime);
                    if (elapsedTime >= 0f) {

                        //System.out.println("아직 1초가 지나지 않아, 다음 오브젝트 처리로 넘어갑니다.");
                        continue;   // 아래 범위판정을 진행하지 않고, 다음 오브젝트로 건너뛴다.
                    }

                }

                /** 전사 가렌E 스킬의 경우 예외처리 ; 장판의 위치를, 시전자의 위치로 변경한다  */
                if (skillObjectComponent.createdSkillType == SkillType.KNIGHT_GARREN_E) {

                    //System.out.println("가렌E ; 장판(?)의 위치를 시전자의 위치로 변경합니다 ");
                    // 어 설마... 그냥 장판의 positionComponent를 시전자의 것과 공유해도 될라나?? 굳이 복사처리 안해주고..

                    /* 시전자 찾기 위치 */

                    Vector3 newPos = skillUser.positionComponent.position;

                    /* 변경 */
                    skillObject.getValue().positionComponent.position.set(newPos);

                }
            }


            /** 오브젝트의 효과를 적용할 대상 집단을 판별한다 */

            boolean skillObjectOwnerIsMonster = false;

            /* 현 스킬 오브젝트의 스킬 시전자가 몬스터인가? */
            if(worldMap.monsterEntity.containsKey(skillObjectComponent.userEntityID)){
                skillObjectOwnerIsMonster = true;
            }


            /** 판별된 대상 집단에 대해, 범위 판정을 통해 효과 적용 타겟을 정한다 */
            //int[] targetList;   // 타겟들의 ID를 담는다.
            ArrayList<Integer> targetList = new ArrayList<>();

            if(skillObjectOwnerIsMonster == true){  /* 몬스터가 시전한 스킬일 경우 */

                // 스킬 오브젝트의 버프 효과 적용 대상이 캐릭터, 바리케이드, 포탑이다.
                // 요 케이스는 다음에 고려.

            } else {    /* 스킬 시전자가 캐릭터, 포탑 등인 경우 */

                // 힐은.. 껍데기용이므로.
                if(skillObjectComponent.createdSkillType == SkillType.MAGICIAN_HEAL)
                    continue;

                // 스킬 오브젝트의 버프 효과 적용 대상이 몬스터다.
                /* 버프대상(몬스터) 갯수만큼 반복한다 */
                for (HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()) {

                    MonsterEntity monster = monsterEntity.getValue();
                    ConditionComponent mobCond = monster.conditionComponent;
                    HPComponent mobHP = monster.hpComponent;

                    /* 범위 판정... */

                    float currentDistance = 0f;

                    Vector3 skillObjectPos = skillObject.getValue().positionComponent.position;
                    Vector3 mobPos = monster.positionComponent.position;

                    currentDistance = Vector3.distance(skillObjectPos, new Vector3(mobPos.x(), 0, mobPos.z()));

                    switch(skillObjectComponent.skillAreaType){

                        case SkillAreaType.RECTANGLE:
                            /* 직선형 */

                            break;
                        case SkillAreaType.CONE:
                            /**** 원뿔형 */

                            /* 몹의 상태 (무적, 타게팅불가능, 죽어있음) */
                            boolean targettingDisable
                                    = ( mobCond.isDamageImmunity || mobCond.isUnTargetable || (mobHP.currentHP <=0) ) ? true : false;
                            if(targettingDisable){
                                continue;
                            }

                      /*      System.out.println("몬스터" + monster.entityID + " 타게팅 가능 ");


                            System.out.println("현재 거리 : " + currentDistance);
                            System.out.println("스킬오브젝트 범위 : " + skillObjectComponent.skillObjectRadius);
                      */
                            /* 범위 거리 */
                            boolean targetIsOutOfRange
                                    = (currentDistance > (skillObjectComponent.skillObjectRadius)) ? true : false;
                            if(targetIsOutOfRange) {
                                continue;
                            }

                            //System.out.println("몬스터" + monster.entityID + " 범위 내에 있음 ");

                            /* 범위 각도 */
                            float betweenAngle =
                                    Vector3.getAngle(skillObjectComponent.direction,
                                                    Vector3.getTargetDirection(skillObjectPos, mobPos) );
                            // ㄴ 스킬이 적용된 방향을 기준으로 했을 때, 몬스터가 범위 각 내에 들어있는지 판단하기 위함

                            boolean targetIsOutOfAngle = ( betweenAngle > skillObjectComponent.skillObjectAngle ) ? true : false;
                            if(targetIsOutOfAngle){
                                continue;
                            }

                            //System.out.println("몬스터" + monster.entityID + " 범위각 내에 있음 ");

                            targetList.add(monster.entityID);

                            break;
                        case SkillAreaType.CIRCLE:
                            /* 원형 */

                            /* 대상이 버프 범위에 있는지 판별한다 */
                            boolean isInTargetRange = false;

                            float targetHP = monster.hpComponent.currentHP;
                            if( (currentDistance < (skillObjectComponent.skillObjectRadius)) && targetHP > 0)
                            {
                                isInTargetRange = true;
                            }

                            /* 대상이 버프 적용 범위에 위치한다면 */
                            if(isInTargetRange) {

                                /* 버프적용 타겟으로 추가한다 */
                                //buff.targetIDList[buff.targetIDList.length] = monster.entityID;
                                targetList.add(monster.entityID);
                            }

                            break;
                        case 4:
                            /* 사각형 */

                            break;
                        default:

                            break;
                    }

                }

            }



            /** 버프 타겟 갯수만큼 반복한다 */
            for(int j=0; j<targetList.size(); j++){

                /* 버프 대상 */
                int targetID = targetList.get(j);
                MonsterEntity target = worldMap.monsterEntity.get(targetID);

                /* 대상이 이미 자신의 버프를 받고 있는가?를 검사한다 */
                List<BuffAction> buffActionList = target.buffActionHistoryComponent.conditionHistory;
                boolean targetHasBuffAlready = false;

                BuffAction targetsBuffAction = null;

                /* 대상의 버프액션 갯수만큼 반복한다 */
                for(int k=0; k<buffActionList.size(); k++){

                    if(skillObject.getValue().entityID == buffActionList.get(k).unitID){

                        targetsBuffAction = buffActionList.get(k);
                        targetHasBuffAlready = true;

                        break;
                    }
                }

                if(targetHasBuffAlready) { /* 대상이 이미 효과를 받고 있다면 */

                    // targetsBuffAction.remainTime = deltaTime;    // 버프 지속 남은 시간을 초기화해준다
                    //targetsBuffAction.remainTime -= deltaTime;


                    /** 회오리 */ // 2020 01 18
                    if(skillObjectComponent.createdSkillType == SkillType.KNIGHT_TORNADO){

                        /*System.out.println("이미 회오리 효과를 받고있음");

                        CharacterEntity character = worldMap.characterEntity.get(skillObjectComponent.userEntityID);

                        Vector3 targetPos = target.positionComponent.position;

                        System.out.println("회오리 타겟 버프 남은 쿨타임 : " + targetsBuffAction.remainCoolTime);

                        if(target.conditionComponent.isAriborne){
                        //if(targetsBuffAction.remainCoolTime <= 0){
                            if(targetPos.y() <= 0.5f){
                                System.out.println("0.5이하다. 1로 바꿔줌");

                                targetPos.set(targetPos.x(), 1f, targetPos.z());
                            }
                            else{
                                System.out.println("0.5 이상이라 0.5로 바꿔줌");
                                targetPos.set(targetPos.x(), 0.5f, targetPos.z());
                            }
                        }*/

                        /**
                         * 2020 04 13
                         */
                        if((targetsBuffAction.floatParam.size() > 0)
                                && targetsBuffAction.floatParam.get(0).type == ConditionType.isAirborne){

                            boolean haveDamage = false;
                            for(int q=0; q<buffActionList.size(); q++){

                                BuffAction buff = buffActionList.get(q);
                                if(buff.unitID == buffActionList.get(q).unitID){


                                    if(buff.floatParam.size() > 0){

                                        if ((buff.floatParam.get(0).type == ConditionType.damageAmount)
                                                ||(buff.floatParam.get(0).type == ConditionType.criticalDamageAmount)) {

                                            haveDamage = true;
                                            break;

                                        }
                                    }

                                }
                            }

                            if(!haveDamage){

                                // 데미지
                                target.buffActionHistoryComponent.conditionHistory.add(
                                        SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, skillObjectEntity.entityID));

                            }
                        }

                    }

                    /**
                     * 아이스필드 류를 위한..zz
                     */

                    else if(targetsBuffAction.skillType == SkillType.MAGICIAN_ICE_FIELD){

                        if(targetsBuffAction.floatParam.size() > 0){

                            if(targetsBuffAction.floatParam.get(0).type == ConditionType.moveSpeedRate){

                                boolean haveDamage = false;
                                for(int q=0; q<buffActionList.size(); q++){

                                    BuffAction buff = buffActionList.get(q);
                                    if(buff.unitID == buffActionList.get(q).unitID){

                                        if(buff.floatParam.size() > 0){

                                            if ((buff.floatParam.get(0).type == ConditionType.damageAmount)
                                                    ||(buff.floatParam.get(0).type == ConditionType.criticalDamageAmount)) {

                                                haveDamage = true;
                                                break;

                                            }
                                        }

                                    }
                                }

                                if(!haveDamage){

                                    // 데미지
                                    target.buffActionHistoryComponent.conditionHistory.add(
                                            SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, skillObjectEntity.entityID));


                                }
                            }

                        }

                    }




                    /**
                     * 썬더 류를 위한..zz
                     */

                    else if(targetsBuffAction.skillType == SkillType.MAGICIAN_THUNDER) {

                        if(targetsBuffAction.boolParam.size() > 0){

                            if (targetsBuffAction.boolParam.get(0).type == ConditionType.isStunned) {

                                boolean haveDamage = false;
                                for (int q = 0; q < buffActionList.size(); q++) {

                                    BuffAction buff = buffActionList.get(q);
                                    if (buff.unitID == buffActionList.get(q).unitID) {

                                        if(buff.floatParam.size() > 0){
                                            if ((buff.floatParam.get(0).type == ConditionType.damageAmount)
                                                ||(buff.floatParam.get(0).type == ConditionType.criticalDamageAmount)) {

                                                haveDamage = true;
                                                break;

                                            }
                                        }
                                    }
                                }

                                if (!haveDamage) {

                                    // 데미지
                                    target.buffActionHistoryComponent.conditionHistory.add(
                                            SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, skillObjectEntity.entityID));


                                }
                            }
                        }

                    }



                }
                else{   /* 기존에 효과를 받고있지 않다면 */

                    //System.out.println("스킬 오브젝트의 버프를 새로 추가합니다. ");


                    /** 회오리 */
                    if(skillObjectComponent.createdSkillType == SkillType.KNIGHT_TORNADO){

                      //  System.out.println("타겟에게 버프를 넣어줌");

                        CharacterEntity character = worldMap.characterEntity.get(skillObjectComponent.userEntityID);

                        Vector3 targetPos = target.positionComponent.position;

                        /** 2020 05 29 주석*/
                        //targetPos.set(targetPos.x(), 2f, targetPos.z());

                        if(doOldVersion){

                            /**
                             * 2020 02 06
                             */
                            BuffAction skillObjBuff = (BuffAction) skillObjectComponent.buffAction.clone();

                            /* 데미지 */
                            ConditionFloatParam damageParam = (ConditionFloatParam) skillObjBuff.floatParam.get(0).clone();
                            BuffAction damageBuff = SkillFactory.createDamageBuff(damageParam, skillObjBuff.unitID, skillObjBuff.skillUserID);

                            damageBuff.remainTime = skillObjectComponent.skillObjectDurationTime;
                            damageBuff.coolTime = 1f;
                            damageBuff.remainCoolTime = 0f;
                            damageBuff.skillType = SkillType.KNIGHT_TORNADO;
                            target.buffActionHistoryComponent.conditionHistory.add(damageBuff);


                            /* 상태 */
                            skillObjBuff.floatParam.clear();
                            skillObjBuff.remainTime = skillObjectComponent.skillObjectDurationTime;
                            skillObjBuff.coolTime = -1f;
                            skillObjBuff.remainCoolTime = -1f;
                            damageBuff.skillType = SkillType.KNIGHT_TORNADO;
                            skillObjBuff.boolParam.clear();
                            skillObjBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
                            skillObjBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
                            target.buffActionHistoryComponent.conditionHistory.add(skillObjBuff);

                            BuffAction airBorneBuff = new BuffAction();
                            airBorneBuff.remainTime = skillObjectComponent.skillObjectDurationTime;
                            skillObjBuff.coolTime = 0.5f;
                            skillObjBuff.remainCoolTime = 0f;
                            skillObjBuff.boolParam.add(new ConditionBoolParam(ConditionType.isAirborne, true));
                            damageBuff.skillType = SkillType.KNIGHT_TORNADO;
                            target.buffActionHistoryComponent.conditionHistory.add(airBorneBuff);

                        }
                        else{

                            // 데미지
                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, skillObjectEntity.entityID));

                            // 에어본
                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "에어본", skillLevel, skillUser, skillObjectEntity.entityID));

                        }


                    }
                    /** 아이스 필드 처리
                     *  스킬 팩토리에서 장판 구성 시, 스킬 오브젝트의 버프로
                     *  floatparam 목록에 [0] =>> 받는 대미지 양 , conditionType.damageAmount
                     *                    [1] =>> 속도 감소비율 값 (0~100), conditionType.moveSpeedRate
                     *             이렇게 넣어줌. 대미지는 장판을 밟을 때 한 번 주고 끝이지만
                     *                      (현재 스펙으로는 그렇다. 1초 뒤에도 장판이라면 똑같은 대미지를 받음)
                     *             속도감소는 1초(효과 지속시간)동안 그 효과가 계속 유지되어야 하는데,
                     *                  그렇게되면 버프 시스템에서 위에 대미지 처리를 할 때, 틱레이트마다 대미지도 같이 넣어주게 된다.
                     *                  (대미지/틱레이트) 만큼 계속 달게 하겠다면야 그렇게 해도 되겠지만 아니라면
                     *
                     *             이렇게 두 개 효과를 같은 BuffAction 안에 담으려면.. 약간의 편법이 필요하다
                     */
                    else if(skillObjectComponent.createdSkillType == SkillType.MAGICIAN_ICE_FIELD){

                        if(doOldVersion){

                            BuffAction newBuff = (BuffAction) buffInfo.clone();


                            /** 2020 01 31 추가, 수정 */
                            /* 데미지 처리 */
                            BuffAction damage = new BuffAction();
                            damage.unitID = skillObject.getValue().entityID;
                            damage.skillUserID = skillObjectComponent.userEntityID;
                            damage.remainTime = 0.15f;
                            damage.coolTime = -1f;
                            damage.remainCoolTime = -1f;
                            damage.floatParam.add( (ConditionFloatParam) newBuff.floatParam.get(0).clone() );

                            target.buffActionHistoryComponent.conditionHistory.add(damage);


                            /* 슬로우 처리 */
                            newBuff.floatParam.clear();
                            buffActionList.add(newBuff);


                        }
                        else{

                            // 데미지
                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, skillObjectEntity.entityID));

                            // 슬로우
                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "슬로우", skillLevel, skillUser, skillObjectEntity.entityID));

                        }

                    }
                    /**
                     * 마법사 썬더 효과 적용하기
                     */
                    else if(skillObjectComponent.createdSkillType == SkillType.MAGICIAN_THUNDER){

                        if(doOldVersion){

                            BuffAction newBuff = (BuffAction) buffInfo.clone();

                            /** 대미지 및 스턴 */

                            /* 대미지 */
                            BuffAction damageBuff = new BuffAction();
                            damageBuff.unitID = newBuff.unitID;
                            damageBuff.skillUserID = newBuff.skillUserID;
                            damageBuff.remainCoolTime = 0.15f;
                            damageBuff.remainTime = 1f;
                            damageBuff.coolTime = 1f;    // 스킬효과가 한 번만 적용되도록
                            damageBuff.floatParam.add( (ConditionFloatParam) newBuff.floatParam.get(0).clone() );// 대미지 들어있음


                            /* 스턴 */
                            BuffAction stunBuff = new BuffAction();
                            stunBuff.unitID = newBuff.unitID;
                            stunBuff.skillUserID = newBuff.skillUserID;
                            stunBuff.remainCoolTime = newBuff.remainCoolTime;
                            stunBuff.remainTime = newBuff.remainTime;
                            stunBuff.coolTime = newBuff.coolTime;
                            stunBuff.boolParam.add( (ConditionBoolParam) newBuff.boolParam.get(0).clone() );// 스턴 들어있음


                            /** 2020 03 12 */
                            stunBuff.skillType = SkillType.MAGICIAN_THUNDER;


                            // 적용
                            target.buffActionHistoryComponent.conditionHistory.add(damageBuff);
                            target.buffActionHistoryComponent.conditionHistory.add(stunBuff);

                        }
                        else{

                            // 데미지
                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, skillObjectEntity.entityID));

                            // 스턴
                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "스턴", skillLevel, skillUser, skillObjectEntity.entityID));


                        }


                    }

                    /**
                     * 마법사 프로즌빔 효과 적용하기
                     *
                     * 버프의 지속시간은 냉각 시간으로 되어있고, 냉각상태에서 풀린 후에 데미지 및 슬로우 효과가 적용됨.
                     */
                    else if(skillObjectComponent.createdSkillType == SkillType.MAGICIAN_FROZEN_BEAM){

                        if(doOldVersion){

                            BuffAction newBuff = (BuffAction) buffInfo.clone();

                            /** 대미지 및 슬로우 ; 냉각이 풀린 후 적용됨 */

                            /* 대미지 */
                            BuffAction damageBuff = new BuffAction();
                            damageBuff.unitID = newBuff.unitID;
                            damageBuff.skillUserID = newBuff.skillUserID;
                            damageBuff.remainCoolTime = newBuff.remainTime; // 냉각 효과가 지속된 이후에 적용되도록
                            damageBuff.remainTime = newBuff.remainTime + skillObjectComponent.skillObjectDurationTime;
                            damageBuff.coolTime = newBuff.remainTime + skillObjectComponent.skillObjectDurationTime;    // 스킬효과가 한 번만 적용되도록
                            damageBuff.floatParam.add( (ConditionFloatParam) newBuff.floatParam.get(0).clone() );// 대미지 들어있음


                            /* 슬로우 */
                            BuffAction slowBuff = new BuffAction();
                            slowBuff.unitID = newBuff.unitID;
                            slowBuff.skillUserID = newBuff.skillUserID;
                            slowBuff.remainCoolTime = newBuff.remainTime; // 냉각 효과가 지속된 이후에 적용되도록
                            slowBuff.remainTime = newBuff.remainTime + skillObjectComponent.skillObjectDurationTime;
                            //slowBuff.coolTime = 0.1f;   // 0.1로 준 이유.........
                            slowBuff.coolTime = worldMap.tickRate * 0.001f;   // 0.1로 준 이유.........
                            slowBuff.floatParam.add( (ConditionFloatParam) newBuff.floatParam.get(1).clone() );// 슬로우 들어있음
                            slowBuff.boolParam.add( new ConditionBoolParam(ConditionType.isSlow, true));


                            /** 2020 03 12 */
                            slowBuff.skillType = SkillType.MAGICIAN_FROZEN_BEAM;


                            /** 냉각 ; 스킬적중 시 바로 적용됨 */
                            newBuff.floatParam.clear(); // 위에서 적용한 후 버프들 지워버리고나면 냉각 효과만 남음. ( 냉각은 boolParam )


                            // 적용
                            target.buffActionHistoryComponent.conditionHistory.add(newBuff);
                            target.buffActionHistoryComponent.conditionHistory.add(damageBuff);
                            target.buffActionHistoryComponent.conditionHistory.add(slowBuff);

                        }
                        else{

                            /** 2020 04 02 ver */
                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "빙결", skillLevel, skillUser, skillObjectEntity.entityID));

                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "슬로우", skillLevel, skillUser, skillObjectEntity.entityID));

                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, skillObjectEntity.entityID));
                        }

                    }
                    else if(skillObjectComponent.createdSkillType == SkillType.MAGICIAN_METEOR){

                        if(doOldVersion){

                            /* 대상의 버프 목록에 추가해준다. */
                            BuffAction newBuff = (BuffAction) buffInfo.clone();

                            /**
                             * 2020 02 06
                             */
                            /* 데미지 */
                            ConditionFloatParam damageParam = (ConditionFloatParam) newBuff.floatParam.get(0).clone();
                            BuffAction damageBuff = SkillFactory.createDamageBuff(damageParam, newBuff.unitID, newBuff.skillUserID);
                            target.buffActionHistoryComponent.conditionHistory.add(damageBuff);

                            /* 상태 */
                            target.buffActionHistoryComponent.conditionHistory.add(newBuff);

                            newBuff.remainTime = skillObjectComponent.skillObjectDurationTime;
                            buffActionList.add(newBuff);


                        }
                        else{

                            /* 스킬레벨테이블에서, 스킬의 지속시간을 얻는다 */
                            float skillDuration = SkillFactory.skillInfoPerLevelLIST.get(SkillType.MAGICIAN_METEOR).get(skillLevel).durationTime;


                            if((skillObjectComponent.skillObjectDurationTime + 0.1f) >= (skillDuration)){

                                target.buffActionHistoryComponent.conditionHistory.add(
                                        SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, skillObjectEntity.entityID));

                                System.out.println("메테오 충돌뎀 : " + skillObjectComponent.skillObjectDurationTime);
                            }
                            else{

                                target.buffActionHistoryComponent.conditionHistory.add(
                                        SkillFactory.createSkillEffect(skillType, "장판 데미지", skillLevel, skillUser, skillObjectEntity.entityID));

                                System.out.println("메테오 장판뎀 : " + skillObjectComponent.skillObjectDurationTime);
                            }

                        }


                    }
                    else {

                        //System.out.println("스킬 오브젝트, 대상에게 버프를 새로 추가해 줌");

                        if (doOldVersion){

                            /* 대상의 버프 목록에 추가해준다. */
                            BuffAction newBuff = (BuffAction) buffInfo.clone();

                            /**
                             * 2020 02 06
                             */
                            /* 데미지 */
                            ConditionFloatParam damageParam = (ConditionFloatParam) newBuff.floatParam.get(0).clone();
                            BuffAction damageBuff = SkillFactory.createDamageBuff(damageParam, newBuff.unitID, newBuff.skillUserID);
                            target.buffActionHistoryComponent.conditionHistory.add(damageBuff);

                            /* 상태 */
                            newBuff.floatParam.clear();
                            target.buffActionHistoryComponent.conditionHistory.add(newBuff);

                            //newBuff.remainTime = skillObjectComponent.skillObjectDurationTime;
                            //buffActionList.add(newBuff);

                        }
                        else{

                            target.buffActionHistoryComponent.conditionHistory.add(
                                    SkillFactory.createSkillEffect(skillType, "데미지", skillLevel, skillUser, skillObjectEntity.entityID));

                        }


                    }


                }

            }   /* 버프타겟 수만큼 반복 끝 */




            //System.out.println("==============================================================");


        }   /* 스킬 오브젝트 수만큼 반복 끝 */

    }










    //스킬로 인해 월드맵에 생성된 오브젝트들을 처리하는 시스템
    //스킬오브젝트의 지속시간을 체크하여, 시간이 다 되었다면 맵에서 제거함.
    //지속시간동안 해당 스킬오브젝트 범위에 들어온 Target들의 목록을 가지고 있음.
    //스킬오브젝트의 타입에 따라서 Target들의 목록에게 데미지를 줄지, 디버프를줄지 등이 결정됨.


    /*

    월드맵 중, SkillObjectEntity 들을 가져옴.
    월드맵 중, 몬스터, 캐릭터엔티티들을 다 가져옴.


    for ( SkillObjectEntity의 갯수만큼 반복함 )
    {
        가져온 스킬오브젝트 엔티티 & 스킬오브젝트 컴포넌트;

        스킬오브젝트의 남은 시간을 계산후 깎는다

        if( 남은 시간 < 0){

            그 후, SkillObjectEntity 리스트에서 이 항목 제거


            여기서 바로 지우지 않고, 제거 리스트에 추가만 해둔다.

            //아래 내용은 실행하지 않는다.
            해당 인덱스의 for문 i값을 --i 해준다.
            (다음 for문시 i++되면서 다음 요소에 접근 가능)
            continue
        }
        else { //남은시간이 존재한다면, Tick시간만큼 깎는다.

            남은 시간 -= deltaTime;
        }





        스킬오브젝트 컴포넌트의 createdSkillID; 값을 통해, 미리 CSV파일을 통해 불러왔던 데이터 모음집에서
        스킬오브젝트의 범위와 모양을 불러온다.


        public int [ ] targetIDList; //대상이 되는 객체들의 ID 배열.


-------------------------------------- 아직 고려가 필요함 19/11/08
        스킬오브젝트 자신의 대상이 되는 객체들을 찾는 부분.
        (거리등을 통해서 범위내에 들어있는지 여부 판별 및, 대상의 종류 판별 (몬스터, 유저, 구조물)
        //클래스 등을 가지고 (몬스터, 유저, 구조물)등의 종류 구분이 가능하다.
        (CSV파일을 통해서 불러온 조건에 따라서, 몬스터에게만 적용된다거나 하는 조건이 정해짐)
        자신의 스킬오브젝트의 위치컴포넌트와, 몬스터 캐릭터 엔티티들의 위치컴포넌트들을 가져옴.
        for( 테스트 )
        {
            float currentDistance = (자신의 스킬오브젝트의 위치컴포넌트 - 몬스터 캐릭터 엔티티 위치컴포넌트의 위치).길이

            if(currentDistance ??? CSV파일에서 불러온 스킬오브젝트의 범위 (임의의 조건))
            {
                //스킬오브젝트의 범위안에 들어가 있다면,
                범위안에 들어가있는 대상의 id를

                스킬오브젝트 의 public int [ ] targetIDList; // 타겟ID 목록 에 추가한다.
            }
        }
-------------------------------------





        //범위안에 들어가있는 타겟목록이 형성이 되었다면 그 타겟들의 버프액션 리스트에 접근한다.
        for ( targetIDList 수만큼 반복함 )
        {
            가져온 타겟 객체의 버프액션 리스트.

            BuffAction ownBuff = null;

            //몬스터, 캐릭터의 BuffActionList에 접근하여, 자신의 버프액션이 있는지 검색하는 부분.
            for(가져온 타겟 객체의 버프액션 리스트의 수 만큼 반복함.)
            {
                //몬스터, 캐릭터의 BuffActionList에 접근하여, 자신의 버프액션이 있는지 확인. (버프액션의 시전자ID를 자신의 ID와 비교함)
                if( 스킬오브젝트 자신의 타겟ID == 대상의 버프액션리스트의 시전자ID )
                {
                    ownBuff = 가져온다.
                }
            }

            //이미 자신의 버프(또는 디버프)가 존재한다면
            if(ownBuff != null)
            {
                ownBuff.remainTime을 초기화 함.
                버프범위에서 벗어난뒤 몇초간 버프를 유지시킬것인지를 기준.
                일반적으로 tick타임인 deltaTime;

                ownBuff.remainTime = deltaTime;
            }
            else
            {
                //몬스터, 캐릭터의 BuffActionList에 접근하여 자신의 버프로 버프액션을 생성후,
                /몬스터, 캐릭터의 BuffActionList에 추가해준다.

                BuffActionList.add ( new BuffAction (시전자id, 지속시간, 효과발동 쿨타임, ~~~~)   );
            }
        }




    }







    몬스터, 캐릭터의 BuffActionList에 접근하여, 자신의 버프액션이 있는지 확인. (버프액션의 시전자ID를 자신의 ID와 비교함)

    없으면 대상 객체 목록에 들어있는 대상 객체의 BuffActionList에 자신의 버프액션을 추가해줌. ->
    있으면 남은 remainTime을 갱신함.




    */

    /**
     * 2020 01 27 월요일
     */
    /*******************************************************************************************************************/













}
