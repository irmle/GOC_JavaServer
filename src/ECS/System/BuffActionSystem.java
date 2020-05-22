package ECS.System;


import ECS.Components.*;
import ECS.Classes.Type.*;
import ECS.Classes.*;
import ECS.Entity.*;
import ECS.Game.*;
import Network.RMI_Classes.RMI_ID;

import java.util.HashMap;
import java.util.List;

/**
 * 업뎃날짜 ; 2020 05 05 화 권령희 추가
 * 업뎃내용 :
 *
 *          -- CharacterEntity에 shielAmount 처리 추가.
 *
 *
 *
 *          ================================================================================================
 *
 *          귀환 상태 케이스 추가
 *          ㄴ 귀환 지속시간이 끝날 경우, 스폰 지점으로 위치를 이동시켜 줌.
 *
 *          =================================================================================================
 *
 *              캐릭터 타입의 버프 처리에서, 흡혈 처리항목 추가함. (전사 버서커 스킬 처리때문)
 *              캐릭터 이외에 다른 앤티티들에 대해서는 아직 추가하지 않음.
 *
 *             최대체력증가 비율 등에 따른 처리 추가함. >> 최종적으로 계산된 Condition을 Entity에 적용 후,
 *             그 값에 따라 바꿔줘야 할 값들이 있으면 바꿔주는 처리를 하는.. 걸 따로 추가해야 할 듯??
 *             이거는 매서드로 하는게 좋겠다.
 *             그리고 일단은 최대체력만 적용하는데, 더 필요한 처리들이 있다면 이 매서드 내에서 하도록.
 *
 *             캐릭터에만 무적 처리 추가.
 *             캐릭터에게만 크리티컬 추가.
 *
 *             궁수 난사 스킬 상태 추가.
 *
 *          =====================================================================================================
 *
 *             "CriticalDamageAmount" 버프 타입에 대한 처리 추가,
 *             회복인 경우, 평타인 경우, 치명타인 경우에 대해 처리 각각 수정 및 추가함.
 *
 *             일단 HP에 대해서만 작성. 마력... 데미지 다는거는 왜 없나 모르겠는데, 나중에 추가하던지 수정하던지 하는걸로 하고 MP 처리는 안함
 *             hp 성공하면 추가하는 걸로.
 *
 *             1. 평타인 경우 :
 *                  1) BuffAction 객체의 데미지량에서 대상(entity)의 방어력 값 등을 뺀 최종 피해량을 계산함
 *                      ㄴ 공식이 틀릴지도 모르는데..
 *                          어쨌든 핵심은 entity의 상태값? 스탯을 활용해 최종적으로 받을 데미지를 도출하는 것임
 *                  2) 그 피해량을 데미지 히스토리 객체로 만들어 넣어주고
 *                  3) 평타 중계 RMI를 호출함
 *             2. 치명타인 경우 :
 *                 1) BuffAction 객체의 데미지량에서 대상(entity)의 방어력 값 등을 뺀 최종 피해량을 계산함
 *                 2) 그 피해량을 데미지 히스토리 객체로 만들어 넣어주고
 *                 3) 치명타 중계 RMI를 호출함
 *             3. 회복인 경우 :
 *                  1) BuffAction 객체의 회복량에서 대상(entity)의 상태값 및 스탯 등을 활용해 최종 회복량을 계산함
 *                  2) 그 회복량을 데미지 히스토리로 만들어 넣어주고
 *                  3) 회복량 중계 RMI를 호출함
 *
 *          =====================================================================================================
 *          ㄴ 위 처리는 캐릭터 뿐만 아니라, 몬스터 등 다른 Entity에도 적용되어야 함;
 *
 */
public class BuffActionSystem {

    WorldMap worldMap;

    public BuffActionSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime)
    {

        /*
         각 앤티티 종류에 대해 : 캐릭터, 몬스터, 바리케이드, 공격 포탑, 버프 포탑

         앤티티 리스트를 얻는다.
         리스트 크기만큼 반복() {
            앤티티(i) 각각의 남은 지속시간을 체크한다.
            지속 시간 여부(끝났는지??)에 따른 처리를 한다.
         */

        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());


        /** 캐릭터 */
        for(HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()){

            CharacterEntity character = characterEntity.getValue();
            List<BuffAction> buffActionList = character.buffActionHistoryComponent.conditionHistory;
            List<DamageHistory> hpDamageHistory = character.hpHistoryComponent.hpHistory;
            List<DamageHistory> mpDamageHistory = character.mpHistoryComponent.mpHistory;

            /** 각 캐릭터 Entity에 최종적으로 반영될 Condition */
            ConditionComponent newCondition = new ConditionComponent();

            /** 오후 8:05 2020-05-05 추가 **/

            /*이전 상태 */
            ConditionComponent oldCondition = character.conditionComponent;

            /**********************************************************************/



            /* 버프액션 리스트 갯수만큼 반복한다 */
            for(int j=0; j<buffActionList.size(); j++){

                ConditionData tempValue = new ConditionData();

                BuffAction buffAction = buffActionList.get(j);

                /** 버프액션의 남은 시간을 계산 후 깎는다 */
                buffAction.remainTime -= deltaTime;

                if(buffAction.remainTime <= 0f){
                    /** 지속시간이 끝났으므로, 현 캐릭터에서 기존에 적용하던 효과를 제거한다. */

                    //System.out.println("버프를 제거합니다.");

                    /** 2020 03 20 권령희 추가 */
                    /**
                     * 귀환 상태를 나타내주는 버프인 경우의 예외처리
                     *      ; 이동 처리
                     */
                    switch (buffAction.skillType){

                        case SkillType.RECALL :

                            character.positionComponent.position.set(new Vector3(3f, 0f, -3f));

                            /* 모션 중계.. 필요?? */

                            break;

                        /**
                         * 오후 8:20 2020-05-05 추가
                         * 쉴드 버프 지속시간 끝났을 때 처리
                         */
                        case SkillType.MAGICIAN_SHIELD :

                            if((buffAction.floatParam.size() > 0)
                                    && (buffAction.floatParam.get(0).type == ConditionType.shieldAmount)){

                                character.hpComponent.shieldAmount = 0f;

                            }

                            break;


                        case SkillType.KNIGHT_INCR_HP :
                        case SkillType.JUNGLE_BUFF_LIZARD :

                            HPComponent hpComponent = character.hpComponent;
                            hpComponent.maxHP = hpComponent.originalMaxHp;
                            break;

                        case SkillType.JUNGLE_BUFF_FAIRY :

                            MPComponent mpComponent = character.mpComponent;
                            mpComponent.maxMP = mpComponent.originalMaxMP;
                            break;

                        default:

                            break;
                    }


                    /************************************************/

                    /* 현 버프액션을 버프액션 리스트에서 삭제한다. */
                    buffActionList.remove(j);
                    j--;    // 인덱스 제어.

                }
                else{
                    /** 여전히 효과가 지속중이므로, 그에 따른 처리(?)를 한다 */

                    /* 쿨타임을 갖는지 여부를 판별한다 */
                    //System.out.println("남은 지속시간 : " + buffAction.remainTime);

                    if(buffAction.coolTime > 0){

                        /* 남은 쿨타임을 계산한다. */
                        // 2020 01 24 금 수정.
                        buffAction.remainCoolTime -= deltaTime; // 이걸 여기서 먼저 까주는게 맞는지 모르겠네.
                        if(buffAction.remainCoolTime <= 0f){ /* 쿨타임이 끝나, 새로 효과를 넣음  */

                            /** 버프 효과를 발동한다 */
                            /* bool 파라미터들 처리 */
                            for(int k=0; k<buffAction.boolParam.size(); k++){

                                ConditionBoolParam condition = buffAction.boolParam.get(k);

                                if(condition.value == true){    // 아 근데 생각해보니까, 어차피 이쪽 상태들은.. 얘네를 false로 만드는 애들이 없을텐데..
                                    switch (condition.type){
                                        case ConditionType.isDisableMove:
                                            newCondition.isDisableMove = true;
                                            break;
                                        case ConditionType.isDisableAttack:
                                            newCondition.isDisableAttack = true;
                                            break;
                                        case ConditionType.isDisableSkill:
                                            newCondition.isDisableSkill = true;
                                            break;
                                        case ConditionType.isDisableItem:
                                            newCondition.isDisableItem = true;
                                            break;
                                        case ConditionType.isDamageImmunity:
                                            newCondition.isDamageImmunity = true;
                                            break;
                                        case ConditionType.isUnTargetable:
                                            newCondition.isUnTargetable = true;
                                            break;
                                        case ConditionType.isTargetingInvincible :
                                            newCondition.isTargetingInvincible = true;
                                            break;
                                        case ConditionType.isArcherFireActivated :
                                            newCondition.isArcherFireActivated = true;
                                            break;

                                        case ConditionType.isStunned :
                                            newCondition.isStunned =  true;
                                            newCondition.isDisableAttack = true;
                                            newCondition.isDisableMove = true;
                                            newCondition.isDisableSkill = true;
                                            break;


                                        case ConditionType.isSlow :
                                            newCondition.isSlow = true;
                                            break;

                                        case ConditionType.isFreezing :
                                            newCondition.isFreezing = true;
                                            newCondition.isDisableAttack = true;
                                            newCondition.isDisableMove = true;
                                            newCondition.isDisableSkill = true;
                                            break;


                                        default:
                                            break;
                                    }
                                }

                            }

                            /* float 파라미터들 처리. */
                            for(int k=0; k<buffAction.floatParam.size(); k++){
                                ConditionFloatParam condition = buffAction.floatParam.get(k);

                                switch (condition.type){

                                    /* 비율 변화 버프일 경우 */
                                    case ConditionType.moveSpeedRate:
                                        newCondition.moveSpeedRate += (condition.value * 0.01);

                                        /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                        if(condition.value < 0f){
                                            newCondition.isSlow = true;
                                        }

                                        break;
                                    case ConditionType.attackSpeedRate:
                                        newCondition.attackSpeedRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.hpRecoveryRate:
                                        newCondition.hpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.mpRecoveryRate:
                                        newCondition.mpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.goldGainRate:
                                        newCondition.goldGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.expGainRate:
                                        newCondition.expGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.buffDurationRate:
                                        newCondition.buffDurationRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.attackDamageRate:
                                        newCondition.attackDamageRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.defenseRate:
                                        newCondition.defenseRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.maxHPRate:
                                        newCondition.maxHPRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.maxMPRate:
                                        newCondition.maxMPRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.coolTimeReduceRate:
                                        newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                        break;
                                    // 2020 01 24 금 새벽 추가 권령희
                                    case ConditionType.bloodSuckingRate :
                                        newCondition.bloodSuckingRate += (condition.value * 0.01);
                                        break;
                                    // 2020 01 29 수 새벽 추가 권령희
                                    case ConditionType.criticalChanceRate :
                                        newCondition.criticalChanceRate += (condition.value );
                                        break;
                                    case ConditionType.criticalDamageRate :
                                        newCondition.criticalDamageRate += (condition.value * 0.01);
                                        break;


                                    /* 값 추가 버프일 경우 */
                                    case ConditionType.moveSpeedBonus:
                                        newCondition.moveSpeedBonus += condition.value;
                                        break;
                                    case ConditionType.attackDamageBonus:
                                        newCondition.attackDamageBonus += condition.value;
                                        break;
                                    case ConditionType.defenseBonus:
                                        newCondition.defenseBonus += condition.value;
                                        break;
                                    case ConditionType.maxHPBonus:
                                        newCondition.maxHPBonus += condition.value;
                                        break;
                                    case ConditionType.maxMPBonus:
                                        newCondition.maxMPBonus += condition.value;
                                        break;

                                    /** 데미지 또는 회복 버프일 경우 */
                                    case ConditionType.damageAmount:

                                        /* 최종 데미지 계산  */
                                        float flatDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                        // >> 이거.. 걍 hp 시스템으로 옮길거임
                                        break;

                                    case ConditionType.criticalDamageAmount :

                                        /* 최종 데미지 계산  */
                                        float criticalDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                        break;

                                    case ConditionType.hpRecoveryAmount:

                                        /* 최종 회복량 계산  */
                                        float recoveryAmount = condition.value;
                                        //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                        if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                            recoveryAmount = 0f;
                                        }

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                        break;
                                    case ConditionType.mpRecoveryAmount:

                                        mpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, condition.value));
                                        break;

                                    default:
                                        break;
                                }
                            }


                            /* 남은 쿨타임을 초기화한다 */
                            buffAction.remainCoolTime = buffAction.coolTime;

                        }
                        else{
                            /* 남은 쿨타임을 계산해준다. */
                            //buffAction.remainCoolTime -= deltaTime;
                        }

                    }
                    else { /** 쿨타임을 갖는 버프액션이 아니다 */

                        /** 버프 효과를 발동한다 */
                        /* bool 파라미터들 처리 */
                        for(int k=0; k<buffAction.boolParam.size(); k++){

                            ConditionBoolParam condition = buffAction.boolParam.get(k);

                            if(condition.value == true){
                                switch (condition.type){
                                    case ConditionType.isDisableMove:
                                        newCondition.isDisableMove = true;
                                        break;
                                    case ConditionType.isDisableAttack:
                                        newCondition.isDisableAttack = true;
                                        break;
                                    case ConditionType.isDisableSkill:
                                        newCondition.isDisableSkill = true;
                                        break;
                                    case ConditionType.isDisableItem:
                                        newCondition.isDisableItem = true;
                                        break;
                                    case ConditionType.isDamageImmunity:
                                        newCondition.isDamageImmunity = true;
                                        break;
                                    case ConditionType.isUnTargetable:
                                        newCondition.isUnTargetable = true;
                                        break;
                                    case ConditionType.isTargetingInvincible :
                                        newCondition.isTargetingInvincible = true;
                                        break;
                                    case ConditionType.isArcherFireActivated :
                                        newCondition.isArcherFireActivated = true;
                                        break;
                                    case ConditionType.isArcherHeadShotActivated :
                                        newCondition.isArcherHeadShotActivated = true;
                                        break;

                                    case ConditionType.isStunned :
                                        newCondition.isStunned =  true;
                                        newCondition.isDisableAttack = true;
                                        newCondition.isDisableMove = true;
                                        newCondition.isDisableSkill = true;
                                        break;

                                    case ConditionType.isSlow :
                                        newCondition.isSlow = true;

                                    case ConditionType.isFreezing :
                                        newCondition.isFreezing = true;
                                        newCondition.isDisableAttack = true;
                                        newCondition.isDisableMove = true;
                                        newCondition.isDisableSkill = true;
                                        break;

                                        /** 2020 03 20 권령희 추가, 귀환 */
                                    case ConditionType.isReturning :
                                        newCondition.isReturning = true;
                                        break;


                                    default:
                                        break;
                                }
                            }

                        }

                        /* float 파라미터들 처리. */
                        for(int k=0; k<buffAction.floatParam.size(); k++){
                            ConditionFloatParam condition = buffAction.floatParam.get(k);

                            switch (condition.type){

                                /* 비율 변화 버프일 경우 */
                                case ConditionType.moveSpeedRate:
                                    newCondition.moveSpeedRate += (condition.value * 0.01);

                                    /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                    if(condition.value < 0f){
                                        newCondition.isSlow = true;
                                    }

                                    break;
                                case ConditionType.attackSpeedRate:
                                    newCondition.attackSpeedRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.hpRecoveryRate:
                                    newCondition.hpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.mpRecoveryRate:
                                    newCondition.mpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.goldGainRate:
                                    newCondition.goldGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.expGainRate:
                                    newCondition.expGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.buffDurationRate:
                                    newCondition.buffDurationRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.attackDamageRate:
                                    newCondition.attackDamageRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.defenseRate:
                                    newCondition.defenseRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.maxHPRate:
                                    newCondition.maxHPRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.maxMPRate:
                                    newCondition.maxMPRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.coolTimeReduceRate:
                                    newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                    break;
                                // 2020 01 24 금 새벽 추가 권령희
                                case ConditionType.bloodSuckingRate :
                                    newCondition.bloodSuckingRate += (condition.value * 0.01);
                                    break;
                                // 2020 01 29 수 새벽 추가 권령희
                                case ConditionType.criticalChanceRate :
                                    newCondition.criticalChanceRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.criticalDamageRate :
                                    newCondition.criticalDamageRate += (condition.value * 0.01);
                                    break;


                                /* 값 추가 버프일 경우 */
                                case ConditionType.moveSpeedBonus:
                                    newCondition.moveSpeedBonus += condition.value;
                                    break;
                                case ConditionType.attackDamageBonus:
                                    newCondition.attackDamageBonus += condition.value;
                                    break;
                                case ConditionType.defenseBonus:
                                    newCondition.defenseBonus += condition.value;
                                    break;
                                case ConditionType.maxHPBonus:
                                    newCondition.maxHPBonus += condition.value;
                                    break;
                                case ConditionType.maxMPBonus:
                                    newCondition.maxMPBonus += condition.value;
                                    break;

                                /** 데미지 또는 회복 버프일 경우 */
                                case ConditionType.damageAmount:

                                    /* 최종 데미지 계산  */
                                    float flatDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                    // >> 이거.. 걍 hp 시스템으로 옮길거임
                                    break;

                                case ConditionType.criticalDamageAmount :

                                    /* 최종 데미지 계산  */
                                    float criticalDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                    break;

                                case ConditionType.hpRecoveryAmount:

                                    /* 최종 회복량 계산  */
                                    float recoveryAmount = condition.value;
                                    //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                    if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                        recoveryAmount = 0f;
                                    }

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                    break;
                                case ConditionType.mpRecoveryAmount:

                                    mpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, condition.value));
                                    break;

                                case ConditionType.shieldAmount :

                                    /* 쉴드값을 처음 한 번만 적용해준다 */
                                    if(oldCondition.isShieldActivated == false){

                                        character.hpComponent.shieldAmount += condition.value;
                                    }

                                    newCondition.isShieldActivated = true;
                                    break;

                                default:
                                    break;
                            }
                        }
                    } // 쿨타임을 갖는 버프액션이 아님 끝

                } // 지속시간이 남아있는 버프액션에 대한 처리 끝

            } // 버프액션 한 개에 대한 처리 끝

            /* 지금까지 누적된 컴포넌트 정보로 캐릭터의 상태를 업데이트해준다. */
            character.conditionComponent = newCondition;  // 얉은복사(?)로 문제없으려나.. => 나중에 문제생기면, clone() 쓰지머.

            HPComponent hpComponent = character.hpComponent;
            if(hpComponent.originalMaxHp == hpComponent.maxHP){
                applyNewCharCondValue_HP(character);
            }

            MPComponent mpComponent = character.mpComponent;
            if(mpComponent.originalMaxMP == mpComponent.maxMP){
                applyNewCharCondValue_MP(character);
            }


            /** 테스트용 로그 출력 */

            /*System.out.println("isDisableMove : " + character.conditionComponent.isDisableMove);
            System.out.println("isDisableAttack : " + character.conditionComponent.isDisableAttack);
            System.out.println("isDisableSkill : " + character.conditionComponent.isDisableSkill);
            System.out.println("isDisableItem : " + character.conditionComponent.isDisableItem);
            System.out.println("isDamageImmunity : " + character.conditionComponent.isDamageImmunity);
            System.out.println("isUnTargetable : " + character.conditionComponent.isUnTargetable);
            System.out.println("archerFireActivated : " + character.conditionComponent.isArcherFireActivated);
            System.out.println("invincible : " + character.conditionComponent.isTargetingInvincible);*/

            /*System.out.println("moveSpeedRate : " + character.conditionComponent.moveSpeedRate);
            System.out.println("attackSpeedRate : " + character.conditionComponent.attackSpeedRate);
            System.out.println("hpRecoveryRate : " + character.conditionComponent.hpRecoveryRate);
            System.out.println("mpRecoveryRate : " + character.conditionComponent.mpRecoveryRate);
            System.out.println("goldGainRate : " + character.conditionComponent.goldGainRate);
            System.out.println("expGainRate : " + character.conditionComponent.expGainRate);
            System.out.println("buffDrationRate : " + character.conditionComponent.buffDurationRate);
            System.out.println("attackDamageRate : " + character.conditionComponent.attackDamageRate);
            System.out.println("defenseRate : " + character.conditionComponent.defenseRate);
            System.out.println("maxHPRate : " + character.conditionComponent.maxHPRate);
            System.out.println("maxMPRate : " + character.conditionComponent.maxMPRate);
            System.out.println("coolTimeReduceRate : " + character.conditionComponent.coolTimeReduceRate);
            System.out.println("moveSpeedBonus : " + character.conditionComponent.moveSpeedBonus);
            System.out.println("attackDamageBonus : " + character.conditionComponent.attackDamageBonus);
            System.out.println("defenseBouns : " + character.conditionComponent.defenseBonus);
            System.out.println("maxHPBonus : " + character.conditionComponent.maxHPBonus);
            System.out.println("maxMPBonus  : " + character.conditionComponent.maxMPBonus);


            System.out.println("현재 최대 체력  : " + character.hpComponent.maxHP);
            System.out.println("현재 체력  : " + character.hpComponent.currentHP);

            System.out.println("현재 최대 마력  : " + character.mpComponent.maxMP);
            System.out.println("현재 마력  : " + character.mpComponent.currentMP);

            System.out.println("현재 공격속도  : " + character.attackComponent.attackSpeed);
            System.out.println("현재 이동속도  : " + character.velocityComponent.moveSpeed);
            System.out.println("현재 방어력  : " + character.defenseComponent.defense);
            System.out.println("현재 치뎀  : " + character.attackComponent.criticalDamage);
            System.out.println("현재 치명확률   : " + character.attackComponent.criticalChance);*/

            /**************************/

        } // 캐릭터 엔티티 하나에 대한 처리 끝

        /** 몬스터 */
        for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()){

            MonsterEntity monster = monsterEntity.getValue();
            List<BuffAction> buffActionList = monster.buffActionHistoryComponent.conditionHistory;
            List<DamageHistory> hpDamageHistory = monster.hpHistoryComponent.hpHistory;

            if( (monster.hpComponent.currentHP <= 0)){
                continue;
            }

            /** 각 몬스터 Entity에 최종적으로 반영될 Condition */
            ConditionComponent newCondition = new ConditionComponent();

            /* 버프액션 리스트 갯수만큼 반복한다 */
            for(int j=0; j<buffActionList.size(); j++){

                ConditionData tempValue = new ConditionData();

                BuffAction buffAction = buffActionList.get(j);

                /** 버프액션의 남은 시간을 계산 후 깎는다 */
                buffAction.remainTime -= deltaTime;

                //System.out.println("남은  지속시간 : " + buffAction.remainTime);

                if(buffAction.remainTime < 0f){
                    /** 지속시간이 끝났으므로, 현 캐릭터에서 기존에 적용하던 효과를 제거한다. */

                    /**
                     * 에어본 효과를 받고있는 몬스터의 위치를 원상복귀 해준다.
                     */
                    switch (buffAction.skillType){
                        case SkillType.KNIGHT_TORNADO :

                            if((buffAction.boolParam.size() > 0) && buffAction.boolParam.get(0).type == ConditionType.isAirborne){

                                // 위치 원상복귀 해준다.
                                Vector3 monsterPos = monster.positionComponent.position;
                                monsterPos.set(monsterPos.x(), 0f, monsterPos.z());

                            }
                            break;

                        default :
                            break;

                    }


                    /* 현 버프액션을 버프액션 리스트에서 삭제한다. */
                    buffActionList.remove(j);
                    j--;    // 인덱스 제어.

                }
                else{
                    /** 여전히 효과가 지속중이므로, 그에 따른 처리(?)를 한다 */

                    //System.out.println("버프가 지속중이므로, 상태에 반영합니다. ");

                    /* 쿨타임을 갖는지 여부를 판별한다 */

                    if(buffAction.coolTime > 0f){

                        //System.out.println("쿨타임을 갖는 버프를 처리 합니다..");

                        /* 남은 쿨타임을 계산한다. */
                        buffAction.remainCoolTime -= deltaTime; // 이걸 여기서 먼저 까주는게 맞는지 모르겠네.
                        if(buffAction.remainCoolTime <= 0f){ /* 쿨타임이 끝나, 새로 효과를 넣음  */

                            /** 버프 효과를 발동한다 */
                            /* bool 파라미터들 처리 */
                            for(int k=0; k<buffAction.boolParam.size(); k++){

                                ConditionBoolParam condition = buffAction.boolParam.get(k);

                                if(condition.value == true){    // 아 근데 생각해보니까, 어차피 이쪽 상태들은.. 얘네를 false로 만드는 애들이 없을텐데..
                                    switch (condition.type){
                                        case ConditionType.isDisableMove:
                                            newCondition.isDisableMove = true;
                                            break;
                                        case ConditionType.isDisableAttack:
                                            newCondition.isDisableAttack = true;
                                            break;
                                        case ConditionType.isDisableSkill:
                                            newCondition.isDisableSkill = true;
                                            break;
                                        case ConditionType.isDisableItem:
                                            newCondition.isDisableItem = true;
                                            break;
                                        case ConditionType.isDamageImmunity:
                                            newCondition.isDamageImmunity = true;
                                            break;
                                        case ConditionType.isUnTargetable:
                                            newCondition.isUnTargetable = true;
                                            break;
                                        case ConditionType.isAirborne :
                                            newCondition.isAriborne = true;

                                            Vector3 monsterPos = monster.positionComponent.position;
                                            if(monsterPos.y() >= 0.4){

                                                monsterPos.set(monsterPos.x(), 0.2f, monsterPos.z());
                                            }
                                            else{

                                                monsterPos.set(monsterPos.x(), 0.4f, monsterPos.z());
                                            }

                                            break;

                                        case ConditionType.isStunned :
                                            newCondition.isStunned =  true;
                                            newCondition.isDisableAttack = true;
                                            newCondition.isDisableMove = true;
                                            newCondition.isDisableSkill = true;
                                            break;

                                        case ConditionType.isSlow :
                                            newCondition.isSlow = true;

                                        case ConditionType.isFreezing :
                                            newCondition.isFreezing = true;
                                            newCondition.isDisableAttack = true;
                                            newCondition.isDisableMove = true;
                                            newCondition.isDisableSkill = true;
                                            break;

                                        default:
                                            break;
                                    }
                                }

                            }

                            /* float 파라미터들 처리. */
                            for(int k=0; k<buffAction.floatParam.size(); k++){
                                ConditionFloatParam condition = buffAction.floatParam.get(k);

                                switch (condition.type){

                                    /* 비율 변화 버프일 경우 */
                                    case ConditionType.moveSpeedRate:
                                        newCondition.moveSpeedRate += (condition.value * 0.01);

                                        /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                        if(condition.value < 0f){
                                            newCondition.isSlow = true;
                                        }

                                        break;
                                    case ConditionType.attackSpeedRate:
                                        newCondition.attackSpeedRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.hpRecoveryRate:
                                        newCondition.hpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.mpRecoveryRate:
                                        newCondition.mpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.goldGainRate:
                                        newCondition.goldGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.expGainRate:
                                        newCondition.expGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.buffDurationRate:
                                        newCondition.buffDurationRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.attackDamageRate:
                                        newCondition.attackDamageRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.defenseRate:
                                        newCondition.defenseRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.maxHPRate:
                                        newCondition.maxHPRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.maxMPRate:
                                        newCondition.maxMPRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.coolTimeReduceRate:
                                        newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                        break;

                                    /* 값 추가 버프일 경우 */
                                    case ConditionType.moveSpeedBonus:
                                        newCondition.moveSpeedBonus += condition.value;
                                        break;
                                    case ConditionType.attackDamageBonus:
                                        newCondition.attackDamageBonus += condition.value;
                                        break;
                                    case ConditionType.defenseBonus:
                                        newCondition.defenseBonus += condition.value;
                                        break;
                                    case ConditionType.maxHPBonus:
                                        newCondition.maxHPBonus += condition.value;
                                        break;
                                    case ConditionType.maxMPBonus:
                                        newCondition.maxMPBonus += condition.value;
                                        break;

                                    /** 데미지 또는 회복 버프일 경우 */
                                    case ConditionType.damageAmount:

                                        /* 최종 데미지 계산  */
                                        float flatDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                        // >> 이거.. 걍 hp 시스템으로 옮길거임
                                        break;

                                    case ConditionType.criticalDamageAmount :

                                        /* 최종 데미지 계산  */
                                        float criticalDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                        break;

                                    case ConditionType.hpRecoveryAmount:

                                        /* 최종 회복량 계산  */
                                        float recoveryAmount = condition.value;
                                        //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                        if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                            recoveryAmount = 0f;
                                        }

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                        break;

                                    default:
                                        break;
                                }
                            }

                            /* 남은 쿨타임을 초기화한다 */
                            buffAction.remainCoolTime = buffAction.coolTime;

                        }
                        else{
                            /* 남은 쿨타임을 계산해준다. */
                            //buffAction.remainCoolTime -= deltaTime;
                            // 2020 03 12 위에서 쿨타임 빼주는걸로 바꿈.
                        }

                    }
                    else { /** 쿨타임을 갖는 버프액션이 아니다 */

                        //System.out.println("쿨타임을 갖지 않는 버프 액션에 대해 처리합니다. ");

                        /** 버프 효과를 발동한다 */
                        /* bool 파라미터들 처리 */
                        for(int k=0; k<buffAction.boolParam.size(); k++){

                            ConditionBoolParam condition = buffAction.boolParam.get(k);

                            if(condition.value == true){
                                switch (condition.type){
                                    case ConditionType.isDisableMove:
                                        newCondition.isDisableMove = true;
                                        break;
                                    case ConditionType.isDisableAttack:
                                        newCondition.isDisableAttack = true;
                                        break;
                                    case ConditionType.isDisableSkill:
                                        newCondition.isDisableSkill = true;
                                        break;
                                    case ConditionType.isDisableItem:
                                        newCondition.isDisableItem = true;
                                        break;
                                    case ConditionType.isDamageImmunity:
                                        newCondition.isDamageImmunity = true;
                                        break;
                                    case ConditionType.isUnTargetable:
                                        newCondition.isUnTargetable = true;
                                        break;
                                    case ConditionType.isAirborne :
                                        newCondition.isAriborne = true;

                                        // 2020 04 13 주석처리함.. 머지...
                                        //Vector3 monsterPos = monster.positionComponent.position;
                                        //monsterPos.set(monsterPos.x(), 10f, monsterPos.z());

                                        Vector3 monsterPos = monster.positionComponent.position;
                                        if(monsterPos.y() >= 0.4){

                                            monsterPos.set(monsterPos.x(), 0.2f, monsterPos.z());
                                        }
                                        else{

                                            monsterPos.set(monsterPos.x(), 0.4f, monsterPos.z());
                                        }

                                        break;


                                    case ConditionType.isStunned :
                                        newCondition.isStunned =  true;
                                        newCondition.isDisableAttack = true;
                                        newCondition.isDisableMove = true;
                                        newCondition.isDisableSkill = true;
                                        break;

                                    case ConditionType.isSlow :
                                        newCondition.isSlow = true;
                                        break;

                                    case ConditionType.isFreezing :
                                        newCondition.isFreezing = true;
                                        newCondition.isDisableAttack = true;
                                        newCondition.isDisableMove = true;
                                        newCondition.isDisableSkill = true;
                                        break;

                                    default:
                                        break;
                                }
                            }

                        }

                        /* float 파라미터들 처리. */
                        for(int k=0; k<buffAction.floatParam.size(); k++){
                            ConditionFloatParam condition = buffAction.floatParam.get(k);

                            switch (condition.type){

                                /* 비율 변화 버프일 경우 */
                                case ConditionType.moveSpeedRate:
                                    newCondition.moveSpeedRate += (condition.value * 0.01);

                                    /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                    if(condition.value < 0f){
                                        newCondition.isSlow = true;
                                    }

                                    break;
                                case ConditionType.attackSpeedRate:
                                    newCondition.attackSpeedRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.hpRecoveryRate:
                                    newCondition.hpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.mpRecoveryRate:
                                    newCondition.mpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.goldGainRate:
                                    newCondition.goldGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.expGainRate:
                                    newCondition.expGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.buffDurationRate:
                                    newCondition.buffDurationRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.attackDamageRate:
                                    newCondition.attackDamageRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.defenseRate:
                                    newCondition.defenseRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.maxHPRate:
                                    newCondition.maxHPRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.maxMPRate:
                                    newCondition.maxMPRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.coolTimeReduceRate:
                                    newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                    break;

                                /* 값 추가 버프일 경우 */
                                case ConditionType.moveSpeedBonus:
                                    newCondition.moveSpeedBonus += condition.value;
                                    break;
                                case ConditionType.attackDamageBonus:
                                    newCondition.attackDamageBonus += condition.value;
                                    break;
                                case ConditionType.defenseBonus:
                                    newCondition.defenseBonus += condition.value;
                                    break;
                                case ConditionType.maxHPBonus:
                                    newCondition.maxHPBonus += condition.value;
                                    break;
                                case ConditionType.maxMPBonus:
                                    newCondition.maxMPBonus += condition.value;
                                    break;

                                /** 데미지 또는 회복 버프일 경우 */
                                case ConditionType.damageAmount:

                                    /* 최종 데미지 계산  */
                                    float flatDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                    // >> 이거.. 걍 hp 시스템으로 옮길거임
                                    break;

                                case ConditionType.criticalDamageAmount :

                                    /* 최종 데미지 계산  */
                                    float criticalDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                    break;

                                case ConditionType.hpRecoveryAmount:

                                    /* 최종 회복량 계산  */
                                    float recoveryAmount = condition.value;
                                    //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                    if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                        recoveryAmount = 0f;
                                    }

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                    break;

                                default:
                                    break;
                            }
                        }
                    } // 쿨타임을 갖는 버프액션이 아님 끝

                } // 지속시간이 남아있는 버프액션에 대한 처리 끝

            } // 버프액션 한 개에 대한 처리 끝

            /* 지금까지 누적된 컴포넌트 정보로 몬스터의 상태를 업데이트해준다. */
            monster.conditionComponent = newCondition;  // 얉은복사(?)로 문제없으려나.. => 나중에 문제생기면, clone() 쓰지머.

            /** 테스트용 로그 출력 */

            /*System.out.println("몹" + monster.entityID + "의 상태 : ");
            System.out.println("isDisableMove : " + monster.conditionComponent.isDisableMove);
            System.out.println("isDisableAttack : " + monster.conditionComponent.isDisableAttack);
            System.out.println("isDisableSkill : " + monster.conditionComponent.isDisableSkill);
            System.out.println("isDisableItem : " + monster.conditionComponent.isDisableItem);
            System.out.println("isDamageImmunity : " + monster.conditionComponent.isDamageImmunity);
            System.out.println("isUnTargetable : " + monster.conditionComponent.isUnTargetable);*/
            /*System.out.println("moveSpeedRate : " + monster.conditionComponent.moveSpeedRate);
            System.out.println("attackSpeedRate : " + monster.conditionComponent.attackSpeedRate);
            System.out.println("hpRecoveryRate : " + monster.conditionComponent.hpRecoveryRate);
            System.out.println("mpRecoveryRate : " + monster.conditionComponent.mpRecoveryRate);
            System.out.println("goldGainRate : " + monster.conditionComponent.goldGainRate);
            System.out.println("expGainRate : " + monster.conditionComponent.expGainRate);
            System.out.println("buffDrationRate : " + monster.conditionComponent.buffDurationRate);
            System.out.println("attackDamageRate : " + monster.conditionComponent.attackDamageRate);
            System.out.println("defenseRate : " + monster.conditionComponent.defenseRate);
            System.out.println("maxHPRate : " + monster.conditionComponent.maxHPRate);
            System.out.println("maxMPRate : " + monster.conditionComponent.maxMPRate);
            System.out.println("coolTimeReduceRate : " + monster.conditionComponent.coolTimeReduceRate);
            System.out.println("moveSpeedBonus : " + monster.conditionComponent.moveSpeedBonus);
            System.out.println("attackDamageBonus : " + monster.conditionComponent.attackDamageBonus);
            System.out.println("defenseBouns : " + monster.conditionComponent.defenseBonus);
            System.out.println("maxHPBonus : " + monster.conditionComponent.maxHPBonus);
            System.out.println("maxMPBonus  : " + monster.conditionComponent.maxMPBonus);*/

            /**************************/


        } // 몬스터터 엔티티 하나에 대한 처리 끝


        /** 바리케이드 */
        for(HashMap.Entry<Integer, BarricadeEntity> barricadeEntity : worldMap.barricadeEntity.entrySet()){

            BarricadeEntity barricade = barricadeEntity.getValue();
            List<BuffAction> buffActionList = barricade.buffActionHistoryComponent.conditionHistory;
            List<DamageHistory> hpDamageHistory = barricade.hpHistoryComponent.hpHistory;

            if( (barricade.hpComponent.currentHP <= 0)){
                continue;
            }

            /** 각 바리케이드 Entity에 최종적으로 반영될 Condition */
            ConditionComponent newCondition = new ConditionComponent();

            /* 버프액션 리스트 갯수만큼 반복한다 */
            for(int j=0; j<buffActionList.size(); j++){

                ConditionData tempValue = new ConditionData();

                BuffAction buffAction = buffActionList.get(j);

                /** 버프액션의 남은 시간을 계산 후 깎는다 */
                buffAction.remainTime -= deltaTime;

                if(buffAction.remainTime <= 0f){
                    /** 지속시간이 끝났으므로, 현 캐릭터에서 기존에 적용하던 효과를 제거한다. */

                    /* 현 버프액션을 버프액션 리스트에서 삭제한다. */
                    buffActionList.remove(j);
                    j--;    // 인덱스 제어.

                }
                else{
                    /** 여전히 효과가 지속중이므로, 그에 따른 처리(?)를 한다 */

                    /* 쿨타임을 갖는지 여부를 판별한다 */

                    if(buffAction.coolTime > 0){

                        /* 남은 쿨타임을 계산한다. */
                        buffAction.remainCoolTime -= deltaTime; // 이걸 여기서 먼저 까주는게 맞는지 모르겠네.
                        if(buffAction.remainCoolTime <= 0f){ /* 쿨타임이 끝나, 새로 효과를 넣음  */

                            /** 버프 효과를 발동한다 */
                            /* bool 파라미터들 처리 */
                            for(int k=0; k<buffAction.boolParam.size(); k++){

                                ConditionBoolParam condition = buffAction.boolParam.get(k);

                                if(condition.value == true){    // 아 근데 생각해보니까, 어차피 이쪽 상태들은.. 얘네를 false로 만드는 애들이 없을텐데..
                                    switch (condition.type){
                                        case ConditionType.isDisableMove:
                                            newCondition.isDisableMove = true;
                                            break;
                                        case ConditionType.isDisableAttack:
                                            newCondition.isDisableAttack = true;
                                            break;
                                        case ConditionType.isDisableSkill:
                                            newCondition.isDisableSkill = true;
                                            break;
                                        case ConditionType.isDisableItem:
                                            newCondition.isDisableItem = true;
                                            break;
                                        case ConditionType.isDamageImmunity:
                                            newCondition.isDamageImmunity = true;
                                            break;
                                        case ConditionType.isUnTargetable:
                                            newCondition.isUnTargetable = true;
                                            break;
                                        default:
                                            break;
                                    }
                                }

                            }

                            /* float 파라미터들 처리. */
                            for(int k=0; k<buffAction.floatParam.size(); k++){
                                ConditionFloatParam condition = buffAction.floatParam.get(k);

                                switch (condition.type){

                                    /* 비율 변화 버프일 경우 */
                                    case ConditionType.moveSpeedRate:
                                        newCondition.moveSpeedRate += (condition.value * 0.01);

                                        /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                        if(condition.value < 0f){
                                            newCondition.isSlow = true;
                                        }

                                        break;
                                    case ConditionType.attackSpeedRate:
                                        newCondition.attackSpeedRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.hpRecoveryRate:
                                        newCondition.hpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.mpRecoveryRate:
                                        newCondition.mpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.goldGainRate:
                                        newCondition.goldGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.expGainRate:
                                        newCondition.expGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.buffDurationRate:
                                        newCondition.buffDurationRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.attackDamageRate:
                                        newCondition.attackDamageRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.defenseRate:
                                        newCondition.defenseRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.maxHPRate:
                                        newCondition.maxHPRate += (condition.value * 0.01);
                                        break;

                                    case ConditionType.coolTimeReduceRate:
                                        newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                        break;

                                    /* 값 추가 버프일 경우 */
                                    case ConditionType.moveSpeedBonus:
                                        newCondition.moveSpeedBonus += condition.value;
                                        break;
                                    case ConditionType.attackDamageBonus:
                                        newCondition.attackDamageBonus += condition.value;
                                        break;
                                    case ConditionType.defenseBonus:
                                        newCondition.defenseBonus += condition.value;
                                        break;
                                    case ConditionType.maxHPBonus:
                                        newCondition.maxHPBonus += condition.value;
                                        break;


                                    /** 데미지 또는 회복 버프일 경우 */
                                    case ConditionType.damageAmount:

                                        /* 최종 데미지 계산  */
                                        float flatDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                        // >> 이거.. 걍 hp 시스템으로 옮길거임
                                        break;

                                    case ConditionType.criticalDamageAmount :

                                        /* 최종 데미지 계산  */
                                        float criticalDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                        break;

                                    case ConditionType.hpRecoveryAmount:

                                        /* 최종 회복량 계산  */
                                        float recoveryAmount = condition.value;
                                        //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                        if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                            recoveryAmount = 0f;
                                        }

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                        break;

                                    default:
                                        break;
                                }
                            }


                            /* 남은 쿨타임을 초기화한다 */
                            buffAction.remainCoolTime = buffAction.coolTime;

                        }
                        else{
                            /* 남은 쿨타임을 계산해준다. */
                            buffAction.remainCoolTime -= deltaTime;
                        }

                    }
                    else { /** 쿨타임을 갖는 버프액션이 아니다 */

                        /** 버프 효과를 발동한다 */
                        /* bool 파라미터들 처리 */
                        for(int k=0; k<buffAction.boolParam.size(); k++){

                            ConditionBoolParam condition = buffAction.boolParam.get(k);

                            if(condition.value == true){
                                switch (condition.type){
                                    case ConditionType.isDisableMove:
                                        newCondition.isDisableMove = true;
                                        break;
                                    case ConditionType.isDisableAttack:
                                        newCondition.isDisableAttack = true;
                                        break;
                                    case ConditionType.isDisableSkill:
                                        newCondition.isDisableSkill = true;
                                        break;
                                    case ConditionType.isDisableItem:
                                        newCondition.isDisableItem = true;
                                        break;
                                    case ConditionType.isDamageImmunity:
                                        newCondition.isDamageImmunity = true;
                                        break;
                                    case ConditionType.isUnTargetable:
                                        newCondition.isUnTargetable = true;
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }

                        /* float 파라미터들 처리. */
                        for(int k=0; k<buffAction.floatParam.size(); k++){
                            ConditionFloatParam condition = buffAction.floatParam.get(k);

                            switch (condition.type){

                                /* 비율 변화 버프일 경우 */
                                case ConditionType.moveSpeedRate:
                                    newCondition.moveSpeedRate += (condition.value * 0.01);

                                    /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                    if(condition.value < 0f){
                                        newCondition.isSlow = true;
                                    }

                                    break;
                                case ConditionType.attackSpeedRate:
                                    newCondition.attackSpeedRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.hpRecoveryRate:
                                    newCondition.hpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.mpRecoveryRate:
                                    newCondition.mpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.goldGainRate:
                                    newCondition.goldGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.expGainRate:
                                    newCondition.expGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.buffDurationRate:
                                    newCondition.buffDurationRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.attackDamageRate:
                                    newCondition.attackDamageRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.defenseRate:
                                    newCondition.defenseRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.maxHPRate:
                                    newCondition.maxHPRate += (condition.value * 0.01);
                                    break;

                                case ConditionType.coolTimeReduceRate:
                                    newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                    break;

                                /* 값 추가 버프일 경우 */
                                case ConditionType.moveSpeedBonus:
                                    newCondition.moveSpeedBonus += condition.value;
                                    break;
                                case ConditionType.attackDamageBonus:
                                    newCondition.attackDamageBonus += condition.value;
                                    break;
                                case ConditionType.defenseBonus:
                                    newCondition.defenseBonus += condition.value;
                                    break;
                                case ConditionType.maxHPBonus:
                                    newCondition.maxHPBonus += condition.value;
                                    break;

                                /** 데미지 또는 회복 버프일 경우 */
                                case ConditionType.damageAmount:

                                    /* 최종 데미지 계산  */
                                    float flatDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                    // >> 이거.. 걍 hp 시스템으로 옮길거임
                                    break;

                                case ConditionType.criticalDamageAmount :

                                    /* 최종 데미지 계산  */
                                    float criticalDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                    break;

                                case ConditionType.hpRecoveryAmount:

                                    /* 최종 회복량 계산  */
                                    float recoveryAmount = condition.value;
                                    //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                    if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                        recoveryAmount = 0f;
                                    }

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                    break;

                                default:
                                    break;
                            }
                        }
                    } // 쿨타임을 갖는 버프액션이 아님 끝

                } // 지속시간이 남아있는 버프액션에 대한 처리 끝

            } // 버프액션 한 개에 대한 처리 끝

            /* 지금까지 누적된 컴포넌트 정보로 캐릭터의 상태를 업데이트해준다. */
            barricade.conditionComponent = newCondition;  // 얉은복사(?)로 문제없으려나.. => 나중에 문제생기면, clone() 쓰지머.

            /** 테스트용 로그 출력 */

            /*System.out.println("isDisableMove : " + character.conditionComponent.isDisableMove);
            System.out.println("isDisableAttack : " + character.conditionComponent.isDisableAttack);
            System.out.println("isDisableSkill : " + character.conditionComponent.isDisableSkill);
            System.out.println("isDisableItem : " + character.conditionComponent.isDisableItem);
            System.out.println("isDamageImmunity : " + character.conditionComponent.isDamageImmunity);
            System.out.println("isUnTargetable : " + character.conditionComponent.isUnTargetable);
            System.out.println("moveSpeedRate : " + character.conditionComponent.moveSpeedRate);
            System.out.println("attackSpeedRate : " + character.conditionComponent.attackSpeedRate);
            System.out.println("hpRecoveryRate : " + character.conditionComponent.hpRecoveryRate);
            System.out.println("mpRecoveryRate : " + character.conditionComponent.mpRecoveryRate);
            System.out.println("goldGainRate : " + character.conditionComponent.goldGainRate);
            System.out.println("expGainRate : " + character.conditionComponent.expGainRate);
            System.out.println("buffDrationRate : " + character.conditionComponent.buffDurationRate);
            System.out.println("attackDamageRate : " + character.conditionComponent.attackDamageRate);
            System.out.println("defenseRate : " + character.conditionComponent.defenseRate);
            System.out.println("maxHPRate : " + character.conditionComponent.maxHPRate);
            System.out.println("maxMPRate : " + character.conditionComponent.maxMPRate);
            System.out.println("coolTimeReduceRate : " + character.conditionComponent.coolTimeReduceRate);
            System.out.println("moveSpeedBonus : " + character.conditionComponent.moveSpeedBonus);
            System.out.println("attackDamageBonus : " + character.conditionComponent.attackDamageBonus);
            System.out.println("defenseBouns : " + character.conditionComponent.defenseBonus);
            System.out.println("maxHPBonus : " + character.conditionComponent.maxHPBonus);
            System.out.println("maxMPBonus  : " + character.conditionComponent.maxMPBonus);*/

            /**************************/

        } // 바리케이드 엔티티 하나에 대한 처리 끝






        /** 공격 포탑 */
        for(HashMap.Entry<Integer, AttackTurretEntity> attackTurretEntity : worldMap.attackTurretEntity.entrySet()){

            AttackTurretEntity attackTurret = attackTurretEntity.getValue();
            List<BuffAction> buffActionList = attackTurret.buffActionHistoryComponent.conditionHistory;
            List<DamageHistory> hpDamageHistory = attackTurret.hpHistoryComponent.hpHistory;

            if( (attackTurret.hpComponent.currentHP <= 0)){
                continue;
            }

            /** 각 캐릭터 Entity에 최종적으로 반영될 Condition */
            ConditionComponent newCondition = new ConditionComponent();

            /* 버프액션 리스트 갯수만큼 반복한다 */
            for(int j=0; j<buffActionList.size(); j++){

                ConditionData tempValue = new ConditionData();

                BuffAction buffAction = buffActionList.get(j);

                /** 버프액션의 남은 시간을 계산 후 깎는다 */
                buffAction.remainTime -= deltaTime;

                if(buffAction.remainTime <= 0f){
                    /** 지속시간이 끝났으므로, 현 캐릭터에서 기존에 적용하던 효과를 제거한다. */

                    //System.out.println("버프를 제거합니다.");

                    /* 현 버프액션을 버프액션 리스트에서 삭제한다. */
                    buffActionList.remove(j);
                    j--;    // 인덱스 제어.

                }
                else{
                    /** 여전히 효과가 지속중이므로, 그에 따른 처리(?)를 한다 */

                    /* 쿨타임을 갖는지 여부를 판별한다 */
                    //System.out.println("남은 지속시간 : " + buffAction.remainTime);

                    if(buffAction.coolTime > 0){

                        /* 남은 쿨타임을 계산한다. */
                        //buffAction.remainCoolTime -= deltaTime; // 이걸 여기서 먼저 까주는게 맞는지 모르겠네.
                        if(buffAction.remainCoolTime <= 0f){ /* 쿨타임이 끝나, 새로 효과를 넣음  */

                            /** 버프 효과를 발동한다 */
                            /* bool 파라미터들 처리 */
                            for(int k=0; k<buffAction.boolParam.size(); k++){

                                ConditionBoolParam condition = buffAction.boolParam.get(k);

                                if(condition.value == true){    // 아 근데 생각해보니까, 어차피 이쪽 상태들은.. 얘네를 false로 만드는 애들이 없을텐데..
                                    switch (condition.type){
                                        case ConditionType.isDisableMove:
                                            newCondition.isDisableMove = true;
                                            break;
                                        case ConditionType.isDisableAttack:
                                            newCondition.isDisableAttack = true;
                                            break;
                                        case ConditionType.isDisableSkill:
                                            newCondition.isDisableSkill = true;
                                            break;
                                        case ConditionType.isDisableItem:
                                            newCondition.isDisableItem = true;
                                            break;
                                        case ConditionType.isDamageImmunity:
                                            newCondition.isDamageImmunity = true;
                                            break;
                                        case ConditionType.isUnTargetable:
                                            newCondition.isUnTargetable = true;
                                            break;
                                        default:
                                            break;
                                    }
                                }

                            }

                            /* float 파라미터들 처리. */
                            for(int k=0; k<buffAction.floatParam.size(); k++){
                                ConditionFloatParam condition = buffAction.floatParam.get(k);

                                switch (condition.type){

                                    /* 비율 변화 버프일 경우 */
                                    case ConditionType.moveSpeedRate:
                                        newCondition.moveSpeedRate += (condition.value * 0.01);

                                        /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                        if(condition.value < 0f){
                                            newCondition.isSlow = true;
                                        }

                                        break;
                                    case ConditionType.attackSpeedRate:
                                        newCondition.attackSpeedRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.hpRecoveryRate:
                                        newCondition.hpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.mpRecoveryRate:
                                        newCondition.mpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.goldGainRate:
                                        newCondition.goldGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.expGainRate:
                                        newCondition.expGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.buffDurationRate:
                                        newCondition.buffDurationRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.attackDamageRate:
                                        newCondition.attackDamageRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.defenseRate:
                                        newCondition.defenseRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.maxHPRate:
                                        newCondition.maxHPRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.coolTimeReduceRate:
                                        newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                        break;

                                    /* 값 추가 버프일 경우 */
                                    case ConditionType.moveSpeedBonus:
                                        newCondition.moveSpeedBonus += condition.value;
                                        break;
                                    case ConditionType.attackDamageBonus:
                                        newCondition.attackDamageBonus += condition.value;
                                        break;
                                    case ConditionType.defenseBonus:
                                        newCondition.defenseBonus += condition.value;
                                        break;
                                    case ConditionType.maxHPBonus:
                                        newCondition.maxHPBonus += condition.value;
                                        break;

                                    /** 데미지 또는 회복 버프일 경우 */
                                    case ConditionType.damageAmount:

                                        /* 최종 데미지 계산  */
                                        float flatDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                        // >> 이거.. 걍 hp 시스템으로 옮길거임
                                        break;

                                    case ConditionType.criticalDamageAmount :

                                        /* 최종 데미지 계산  */
                                        float criticalDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                        break;

                                    case ConditionType.hpRecoveryAmount:

                                        /* 최종 회복량 계산  */
                                        float recoveryAmount = condition.value;
                                        //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                        if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                            recoveryAmount = 0f;
                                        }

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                        break;

                                    default:
                                        break;
                                }
                            }


                            /* 남은 쿨타임을 초기화한다 */
                            buffAction.remainCoolTime = buffAction.coolTime;

                        }
                        else{
                            /* 남은 쿨타임을 계산해준다. */
                            buffAction.remainCoolTime -= deltaTime;
                        }

                    }
                    else { /** 쿨타임을 갖는 버프액션이 아니다 */

                        /** 버프 효과를 발동한다 */
                        /* bool 파라미터들 처리 */
                        for(int k=0; k<buffAction.boolParam.size(); k++){

                            ConditionBoolParam condition = buffAction.boolParam.get(k);

                            if(condition.value == true){
                                switch (condition.type){
                                    case ConditionType.isDisableMove:
                                        newCondition.isDisableMove = true;
                                        break;
                                    case ConditionType.isDisableAttack:
                                        newCondition.isDisableAttack = true;
                                        break;
                                    case ConditionType.isDisableSkill:
                                        newCondition.isDisableSkill = true;
                                        break;
                                    case ConditionType.isDisableItem:
                                        newCondition.isDisableItem = true;
                                        break;
                                    case ConditionType.isDamageImmunity:
                                        newCondition.isDamageImmunity = true;
                                        break;
                                    case ConditionType.isUnTargetable:
                                        newCondition.isUnTargetable = true;
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }

                        /* float 파라미터들 처리. */
                        for(int k=0; k<buffAction.floatParam.size(); k++){
                            ConditionFloatParam condition = buffAction.floatParam.get(k);

                            switch (condition.type){

                                /* 비율 변화 버프일 경우 */
                                case ConditionType.moveSpeedRate:
                                    newCondition.moveSpeedRate += (condition.value * 0.01);

                                    /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                    if(condition.value < 0f){
                                        newCondition.isSlow = true;
                                    }

                                    break;
                                case ConditionType.attackSpeedRate:
                                    newCondition.attackSpeedRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.hpRecoveryRate:
                                    newCondition.hpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.mpRecoveryRate:
                                    newCondition.mpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.goldGainRate:
                                    newCondition.goldGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.expGainRate:
                                    newCondition.expGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.buffDurationRate:
                                    newCondition.buffDurationRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.attackDamageRate:
                                    newCondition.attackDamageRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.defenseRate:
                                    newCondition.defenseRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.maxHPRate:
                                    newCondition.maxHPRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.coolTimeReduceRate:
                                    newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                    break;

                                /* 값 추가 버프일 경우 */
                                case ConditionType.moveSpeedBonus:
                                    newCondition.moveSpeedBonus += condition.value;
                                    break;
                                case ConditionType.attackDamageBonus:
                                    newCondition.attackDamageBonus += condition.value;
                                    break;
                                case ConditionType.defenseBonus:
                                    newCondition.defenseBonus += condition.value;
                                    break;
                                case ConditionType.maxHPBonus:
                                    newCondition.maxHPBonus += condition.value;
                                    break;

                                /** 데미지 또는 회복 버프일 경우 */
                                case ConditionType.damageAmount:

                                    /* 최종 데미지 계산  */
                                    float flatDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                    // >> 이거.. 걍 hp 시스템으로 옮길거임
                                    break;

                                case ConditionType.criticalDamageAmount :

                                    /* 최종 데미지 계산  */
                                    float criticalDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                    break;

                                case ConditionType.hpRecoveryAmount:

                                    /* 최종 회복량 계산  */
                                    float recoveryAmount = condition.value;
                                    //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                    if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                        recoveryAmount = 0f;
                                    }

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                    break;

                                default:
                                    break;
                            }
                        }
                    } // 쿨타임을 갖는 버프액션이 아님 끝

                } // 지속시간이 남아있는 버프액션에 대한 처리 끝

            } // 버프액션 한 개에 대한 처리 끝

            /* 지금까지 누적된 컴포넌트 정보로 캐릭터의 상태를 업데이트해준다. */
            attackTurret.conditionComponent = newCondition;  // 얉은복사(?)로 문제없으려나.. => 나중에 문제생기면, clone() 쓰지머.

            /** 테스트용 로그 출력 */

            /*System.out.println("isDisableMove : " + character.conditionComponent.isDisableMove);
            System.out.println("isDisableAttack : " + character.conditionComponent.isDisableAttack);
            System.out.println("isDisableSkill : " + character.conditionComponent.isDisableSkill);
            System.out.println("isDisableItem : " + character.conditionComponent.isDisableItem);
            System.out.println("isDamageImmunity : " + character.conditionComponent.isDamageImmunity);
            System.out.println("isUnTargetable : " + character.conditionComponent.isUnTargetable);
            System.out.println("moveSpeedRate : " + character.conditionComponent.moveSpeedRate);
            System.out.println("attackSpeedRate : " + character.conditionComponent.attackSpeedRate);
            System.out.println("hpRecoveryRate : " + character.conditionComponent.hpRecoveryRate);
            System.out.println("mpRecoveryRate : " + character.conditionComponent.mpRecoveryRate);
            System.out.println("goldGainRate : " + character.conditionComponent.goldGainRate);
            System.out.println("expGainRate : " + character.conditionComponent.expGainRate);
            System.out.println("buffDrationRate : " + character.conditionComponent.buffDurationRate);
            System.out.println("attackDamageRate : " + character.conditionComponent.attackDamageRate);
            System.out.println("defenseRate : " + character.conditionComponent.defenseRate);
            System.out.println("maxHPRate : " + character.conditionComponent.maxHPRate);
            System.out.println("maxMPRate : " + character.conditionComponent.maxMPRate);
            System.out.println("coolTimeReduceRate : " + character.conditionComponent.coolTimeReduceRate);
            System.out.println("moveSpeedBonus : " + character.conditionComponent.moveSpeedBonus);
            System.out.println("attackDamageBonus : " + character.conditionComponent.attackDamageBonus);
            System.out.println("defenseBouns : " + character.conditionComponent.defenseBonus);
            System.out.println("maxHPBonus : " + character.conditionComponent.maxHPBonus);
            System.out.println("maxMPBonus  : " + character.conditionComponent.maxMPBonus);*/

            /**************************/

        } // 캐릭터 엔티티 하나에 대한 처리 끝





        /** 버프 포탑 */
        for(HashMap.Entry<Integer, BuffTurretEntity> buffTurretEntity : worldMap.buffTurretEntity.entrySet()){

            BuffTurretEntity buffTurret = buffTurretEntity.getValue();
            List<BuffAction> buffActionList = buffTurret.buffActionHistoryComponent.conditionHistory;
            List<DamageHistory> hpDamageHistory = buffTurret.hpHistoryComponent.hpHistory;

            if( (buffTurret.hpComponent.currentHP <= 0)){
                continue;
            }

            /** 각 캐릭터 Entity에 최종적으로 반영될 Condition */
            ConditionComponent newCondition = new ConditionComponent();

            /* 버프액션 리스트 갯수만큼 반복한다 */
            for(int j=0; j<buffActionList.size(); j++){

                ConditionData tempValue = new ConditionData();

                BuffAction buffAction = buffActionList.get(j);

                /** 버프액션의 남은 시간을 계산 후 깎는다 */
                buffAction.remainTime -= deltaTime;

                if(buffAction.remainTime <= 0f){
                    /** 지속시간이 끝났으므로, 현 캐릭터에서 기존에 적용하던 효과를 제거한다. */

                    //System.out.println("버프를 제거합니다.");

                    /* 현 버프액션을 버프액션 리스트에서 삭제한다. */
                    buffActionList.remove(j);
                    j--;    // 인덱스 제어.

                }
                else{
                    /** 여전히 효과가 지속중이므로, 그에 따른 처리(?)를 한다 */

                    /* 쿨타임을 갖는지 여부를 판별한다 */
                    //System.out.println("남은 지속시간 : " + buffAction.remainTime);

                    if(buffAction.coolTime > 0){

                        /* 남은 쿨타임을 계산한다. */
                        //buffAction.remainCoolTime -= deltaTime; // 이걸 여기서 먼저 까주는게 맞는지 모르겠네.
                        if(buffAction.remainCoolTime <= 0f){ /* 쿨타임이 끝나, 새로 효과를 넣음  */

                            /** 버프 효과를 발동한다 */
                            /* bool 파라미터들 처리 */
                            for(int k=0; k<buffAction.boolParam.size(); k++){

                                ConditionBoolParam condition = buffAction.boolParam.get(k);

                                if(condition.value == true){    // 아 근데 생각해보니까, 어차피 이쪽 상태들은.. 얘네를 false로 만드는 애들이 없을텐데..
                                    switch (condition.type){
                                        case ConditionType.isDisableMove:
                                            newCondition.isDisableMove = true;
                                            break;
                                        case ConditionType.isDisableAttack:
                                            newCondition.isDisableAttack = true;
                                            break;
                                        case ConditionType.isDisableSkill:
                                            newCondition.isDisableSkill = true;
                                            break;
                                        case ConditionType.isDisableItem:
                                            newCondition.isDisableItem = true;
                                            break;
                                        case ConditionType.isDamageImmunity:
                                            newCondition.isDamageImmunity = true;
                                            break;
                                        case ConditionType.isUnTargetable:
                                            newCondition.isUnTargetable = true;
                                            break;
                                        default:
                                            break;
                                    }
                                }

                            }

                            /* float 파라미터들 처리. */
                            for(int k=0; k<buffAction.floatParam.size(); k++){
                                ConditionFloatParam condition = buffAction.floatParam.get(k);

                                switch (condition.type){

                                    /* 비율 변화 버프일 경우 */
                                    case ConditionType.moveSpeedRate:
                                        newCondition.moveSpeedRate += (condition.value * 0.01);

                                        /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                        if(condition.value < 0f){
                                            newCondition.isSlow = true;
                                        }

                                        break;
                                    case ConditionType.attackSpeedRate:
                                        newCondition.attackSpeedRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.hpRecoveryRate:
                                        newCondition.hpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.mpRecoveryRate:
                                        newCondition.mpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.goldGainRate:
                                        newCondition.goldGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.expGainRate:
                                        newCondition.expGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.buffDurationRate:
                                        newCondition.buffDurationRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.attackDamageRate:
                                        newCondition.attackDamageRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.defenseRate:
                                        newCondition.defenseRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.maxHPRate:
                                        newCondition.maxHPRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.coolTimeReduceRate:
                                        newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                        break;

                                    /* 값 추가 버프일 경우 */
                                    case ConditionType.moveSpeedBonus:
                                        newCondition.moveSpeedBonus += condition.value;
                                        break;
                                    case ConditionType.attackDamageBonus:
                                        newCondition.attackDamageBonus += condition.value;
                                        break;
                                    case ConditionType.defenseBonus:
                                        newCondition.defenseBonus += condition.value;
                                        break;
                                    case ConditionType.maxHPBonus:
                                        newCondition.maxHPBonus += condition.value;
                                        break;

                                    /** 데미지 또는 회복 버프일 경우 */
                                    case ConditionType.damageAmount:

                                        /* 최종 데미지 계산  */
                                        float flatDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                        // >> 이거.. 걍 hp 시스템으로 옮길거임
                                        break;

                                    case ConditionType.criticalDamageAmount :

                                        /* 최종 데미지 계산  */
                                        float criticalDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                        break;

                                    case ConditionType.hpRecoveryAmount:

                                        /* 최종 회복량 계산  */
                                        float recoveryAmount = condition.value;
                                        //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                        if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                            recoveryAmount = 0f;
                                        }

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                        break;

                                    default:
                                        break;
                                }
                            }


                            /* 남은 쿨타임을 초기화한다 */
                            buffAction.remainCoolTime = buffAction.coolTime;

                        }
                        else{
                            /* 남은 쿨타임을 계산해준다. */
                            buffAction.remainCoolTime -= deltaTime;
                        }

                    }
                    else { /** 쿨타임을 갖는 버프액션이 아니다 */

                        /** 버프 효과를 발동한다 */
                        /* bool 파라미터들 처리 */
                        for(int k=0; k<buffAction.boolParam.size(); k++){

                            ConditionBoolParam condition = buffAction.boolParam.get(k);

                            if(condition.value == true){
                                switch (condition.type){
                                    case ConditionType.isDisableMove:
                                        newCondition.isDisableMove = true;
                                        break;
                                    case ConditionType.isDisableAttack:
                                        newCondition.isDisableAttack = true;
                                        break;
                                    case ConditionType.isDisableSkill:
                                        newCondition.isDisableSkill = true;
                                        break;
                                    case ConditionType.isDisableItem:
                                        newCondition.isDisableItem = true;
                                        break;
                                    case ConditionType.isDamageImmunity:
                                        newCondition.isDamageImmunity = true;
                                        break;
                                    case ConditionType.isUnTargetable:
                                        newCondition.isUnTargetable = true;
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }

                        /* float 파라미터들 처리. */
                        for(int k=0; k<buffAction.floatParam.size(); k++){
                            ConditionFloatParam condition = buffAction.floatParam.get(k);

                            switch (condition.type){

                                /* 비율 변화 버프일 경우 */
                                case ConditionType.moveSpeedRate:
                                    newCondition.moveSpeedRate += (condition.value * 0.01);

                                    /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                    if(condition.value < 0f){
                                        newCondition.isSlow = true;
                                    }

                                    break;
                                case ConditionType.attackSpeedRate:
                                    newCondition.attackSpeedRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.hpRecoveryRate:
                                    newCondition.hpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.mpRecoveryRate:
                                    newCondition.mpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.goldGainRate:
                                    newCondition.goldGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.expGainRate:
                                    newCondition.expGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.buffDurationRate:
                                    newCondition.buffDurationRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.attackDamageRate:
                                    newCondition.attackDamageRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.defenseRate:
                                    newCondition.defenseRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.maxHPRate:
                                    newCondition.maxHPRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.coolTimeReduceRate:
                                    newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                    break;

                                /* 값 추가 버프일 경우 */
                                case ConditionType.moveSpeedBonus:
                                    newCondition.moveSpeedBonus += condition.value;
                                    break;
                                case ConditionType.attackDamageBonus:
                                    newCondition.attackDamageBonus += condition.value;
                                    break;
                                case ConditionType.defenseBonus:
                                    newCondition.defenseBonus += condition.value;
                                    break;
                                case ConditionType.maxHPBonus:
                                    newCondition.maxHPBonus += condition.value;
                                    break;

                                /** 데미지 또는 회복 버프일 경우 */
                                case ConditionType.damageAmount:

                                    /* 최종 데미지 계산  */
                                    float flatDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                    // >> 이거.. 걍 hp 시스템으로 옮길거임
                                    break;

                                case ConditionType.criticalDamageAmount :

                                    /* 최종 데미지 계산  */
                                    float criticalDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                    break;

                                case ConditionType.hpRecoveryAmount:

                                    /* 최종 회복량 계산  */
                                    float recoveryAmount = condition.value;
                                    //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                    if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                        recoveryAmount = 0f;
                                    }

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                    break;

                                default:
                                    break;
                            }
                        }
                    } // 쿨타임을 갖는 버프액션이 아님 끝

                } // 지속시간이 남아있는 버프액션에 대한 처리 끝

            } // 버프액션 한 개에 대한 처리 끝

            /* 지금까지 누적된 컴포넌트 정보로 캐릭터의 상태를 업데이트해준다. */
            buffTurret.conditionComponent = newCondition;  // 얉은복사(?)로 문제없으려나.. => 나중에 문제생기면, clone() 쓰지머.

            /** 테스트용 로그 출력 */

            /*System.out.println("isDisableMove : " + character.conditionComponent.isDisableMove);
            System.out.println("isDisableAttack : " + character.conditionComponent.isDisableAttack);
            System.out.println("isDisableSkill : " + character.conditionComponent.isDisableSkill);
            System.out.println("isDisableItem : " + character.conditionComponent.isDisableItem);
            System.out.println("isDamageImmunity : " + character.conditionComponent.isDamageImmunity);
            System.out.println("isUnTargetable : " + character.conditionComponent.isUnTargetable);
            System.out.println("moveSpeedRate : " + character.conditionComponent.moveSpeedRate);
            System.out.println("attackSpeedRate : " + character.conditionComponent.attackSpeedRate);
            System.out.println("hpRecoveryRate : " + character.conditionComponent.hpRecoveryRate);
            System.out.println("mpRecoveryRate : " + character.conditionComponent.mpRecoveryRate);
            System.out.println("goldGainRate : " + character.conditionComponent.goldGainRate);
            System.out.println("expGainRate : " + character.conditionComponent.expGainRate);
            System.out.println("buffDrationRate : " + character.conditionComponent.buffDurationRate);
            System.out.println("attackDamageRate : " + character.conditionComponent.attackDamageRate);
            System.out.println("defenseRate : " + character.conditionComponent.defenseRate);
            System.out.println("maxHPRate : " + character.conditionComponent.maxHPRate);
            System.out.println("maxMPRate : " + character.conditionComponent.maxMPRate);
            System.out.println("coolTimeReduceRate : " + character.conditionComponent.coolTimeReduceRate);
            System.out.println("moveSpeedBonus : " + character.conditionComponent.moveSpeedBonus);
            System.out.println("attackDamageBonus : " + character.conditionComponent.attackDamageBonus);
            System.out.println("defenseBouns : " + character.conditionComponent.defenseBonus);
            System.out.println("maxHPBonus : " + character.conditionComponent.maxHPBonus);
            System.out.println("maxMPBonus  : " + character.conditionComponent.maxMPBonus);*/

            /**************************/

        } // 캐릭터 엔티티 하나에 대한 처리 끝


        /** 크리스탈 */
        for(HashMap.Entry<Integer, CrystalEntity> crystalEntity : worldMap.crystalEntity.entrySet()){

            CrystalEntity crystal = crystalEntity.getValue();
            List<BuffAction> buffActionList = crystal.buffActionHistoryComponent.conditionHistory;
            List<DamageHistory> hpDamageHistory = crystal.hpHistoryComponent.hpHistory;

            if( (crystal.hpComponent.currentHP <= 0)){
                continue;
            }

            /** 각 캐릭터 Entity에 최종적으로 반영될 Condition */
            ConditionComponent newCondition = new ConditionComponent();

            /* 버프액션 리스트 갯수만큼 반복한다 */
            for(int j=0; j<buffActionList.size(); j++){

                ConditionData tempValue = new ConditionData();

                BuffAction buffAction = buffActionList.get(j);

                /** 버프액션의 남은 시간을 계산 후 깎는다 */
                buffAction.remainTime -= deltaTime;

                if(buffAction.remainTime <= 0f){
                    /** 지속시간이 끝났으므로, 현 캐릭터에서 기존에 적용하던 효과를 제거한다. */

                    //System.out.println("버프를 제거합니다.");

                    /* 현 버프액션을 버프액션 리스트에서 삭제한다. */
                    buffActionList.remove(j);
                    j--;    // 인덱스 제어.

                }
                else{
                    /** 여전히 효과가 지속중이므로, 그에 따른 처리(?)를 한다 */

                    /* 쿨타임을 갖는지 여부를 판별한다 */
                    //System.out.println("남은 지속시간 : " + buffAction.remainTime);

                    if(buffAction.coolTime > 0){

                        /* 남은 쿨타임을 계산한다. */
                        //buffAction.remainCoolTime -= deltaTime; // 이걸 여기서 먼저 까주는게 맞는지 모르겠네.
                        if(buffAction.remainCoolTime <= 0f){ /* 쿨타임이 끝나, 새로 효과를 넣음  */

                            /** 버프 효과를 발동한다 */
                            /* bool 파라미터들 처리 */
                            for(int k=0; k<buffAction.boolParam.size(); k++){

                                ConditionBoolParam condition = buffAction.boolParam.get(k);

                                if(condition.value == true){    // 아 근데 생각해보니까, 어차피 이쪽 상태들은.. 얘네를 false로 만드는 애들이 없을텐데..
                                    switch (condition.type){
                                        case ConditionType.isDisableMove:
                                            newCondition.isDisableMove = true;
                                            break;
                                        case ConditionType.isDisableAttack:
                                            newCondition.isDisableAttack = true;
                                            break;
                                        case ConditionType.isDisableSkill:
                                            newCondition.isDisableSkill = true;
                                            break;
                                        case ConditionType.isDisableItem:
                                            newCondition.isDisableItem = true;
                                            break;
                                        case ConditionType.isDamageImmunity:
                                            newCondition.isDamageImmunity = true;
                                            break;
                                        case ConditionType.isUnTargetable:
                                            newCondition.isUnTargetable = true;
                                            break;
                                        default:
                                            break;
                                    }
                                }

                            }

                            /* float 파라미터들 처리. */
                            for(int k=0; k<buffAction.floatParam.size(); k++){
                                ConditionFloatParam condition = buffAction.floatParam.get(k);

                                switch (condition.type){

                                    /* 비율 변화 버프일 경우 */
                                    case ConditionType.moveSpeedRate:
                                        newCondition.moveSpeedRate += (condition.value * 0.01);

                                        /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                        if(condition.value < 0f){
                                            newCondition.isSlow = true;
                                        }

                                        break;
                                    case ConditionType.attackSpeedRate:
                                        newCondition.attackSpeedRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.hpRecoveryRate:
                                        newCondition.hpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.mpRecoveryRate:
                                        newCondition.mpRecoveryRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.goldGainRate:
                                        newCondition.goldGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.expGainRate:
                                        newCondition.expGainRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.buffDurationRate:
                                        newCondition.buffDurationRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.attackDamageRate:
                                        newCondition.attackDamageRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.defenseRate:
                                        newCondition.defenseRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.maxHPRate:
                                        newCondition.maxHPRate += (condition.value * 0.01);
                                        break;
                                    case ConditionType.coolTimeReduceRate:
                                        newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                        break;

                                    /* 값 추가 버프일 경우 */
                                    case ConditionType.moveSpeedBonus:
                                        newCondition.moveSpeedBonus += condition.value;
                                        break;
                                    case ConditionType.attackDamageBonus:
                                        newCondition.attackDamageBonus += condition.value;
                                        break;
                                    case ConditionType.defenseBonus:
                                        newCondition.defenseBonus += condition.value;
                                        break;
                                    case ConditionType.maxHPBonus:
                                        newCondition.maxHPBonus += condition.value;
                                        break;

                                    /** 데미지 또는 회복 버프일 경우 */
                                    case ConditionType.damageAmount:

                                        /* 최종 데미지 계산  */
                                        float flatDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                        // >> 이거.. 걍 hp 시스템으로 옮길거임
                                        break;

                                    case ConditionType.criticalDamageAmount :

                                        /* 최종 데미지 계산  */
                                        float criticalDmgAmount = condition.value;

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                        break;

                                    case ConditionType.hpRecoveryAmount:

                                        /* 최종 회복량 계산  */
                                        float recoveryAmount = condition.value;
                                        //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                        if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                            recoveryAmount = 0f;
                                        }

                                        /* 데미지 적용 */
                                        hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                        /* 데미지량 중계 */
                                        // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                        // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                        break;

                                    default:
                                        break;
                                }
                            }


                            /* 남은 쿨타임을 초기화한다 */
                            buffAction.remainCoolTime = buffAction.coolTime;

                        }
                        else{
                            /* 남은 쿨타임을 계산해준다. */
                            buffAction.remainCoolTime -= deltaTime;
                        }

                    }
                    else { /** 쿨타임을 갖는 버프액션이 아니다 */

                        /** 버프 효과를 발동한다 */
                        /* bool 파라미터들 처리 */
                        for(int k=0; k<buffAction.boolParam.size(); k++){

                            ConditionBoolParam condition = buffAction.boolParam.get(k);

                            if(condition.value == true){
                                switch (condition.type){
                                    case ConditionType.isDisableMove:
                                        newCondition.isDisableMove = true;
                                        break;
                                    case ConditionType.isDisableAttack:
                                        newCondition.isDisableAttack = true;
                                        break;
                                    case ConditionType.isDisableSkill:
                                        newCondition.isDisableSkill = true;
                                        break;
                                    case ConditionType.isDisableItem:
                                        newCondition.isDisableItem = true;
                                        break;
                                    case ConditionType.isDamageImmunity:
                                        newCondition.isDamageImmunity = true;
                                        break;
                                    case ConditionType.isUnTargetable:
                                        newCondition.isUnTargetable = true;
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }

                        /* float 파라미터들 처리. */
                        for(int k=0; k<buffAction.floatParam.size(); k++){
                            ConditionFloatParam condition = buffAction.floatParam.get(k);

                            switch (condition.type){

                                /* 비율 변화 버프일 경우 */
                                case ConditionType.moveSpeedRate:
                                    newCondition.moveSpeedRate += (condition.value * 0.01);

                                    /** 2020 04 27 슬로우/이속증가 구분 필요 예외처리 */
                                    if(condition.value < 0f){
                                        newCondition.isSlow = true;
                                    }

                                    break;
                                case ConditionType.attackSpeedRate:
                                    newCondition.attackSpeedRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.hpRecoveryRate:
                                    newCondition.hpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.mpRecoveryRate:
                                    newCondition.mpRecoveryRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.goldGainRate:
                                    newCondition.goldGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.expGainRate:
                                    newCondition.expGainRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.buffDurationRate:
                                    newCondition.buffDurationRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.attackDamageRate:
                                    newCondition.attackDamageRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.defenseRate:
                                    newCondition.defenseRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.maxHPRate:
                                    newCondition.maxHPRate += (condition.value * 0.01);
                                    break;
                                case ConditionType.coolTimeReduceRate:
                                    newCondition.coolTimeReduceRate += (condition.value * 0.01);
                                    break;

                                /* 값 추가 버프일 경우 */
                                case ConditionType.moveSpeedBonus:
                                    newCondition.moveSpeedBonus += condition.value;
                                    break;
                                case ConditionType.attackDamageBonus:
                                    newCondition.attackDamageBonus += condition.value;
                                    break;
                                case ConditionType.defenseBonus:
                                    newCondition.defenseBonus += condition.value;
                                    break;
                                case ConditionType.maxHPBonus:
                                    newCondition.maxHPBonus += condition.value;
                                    break;

                                /** 데미지 또는 회복 버프일 경우 */
                                case ConditionType.damageAmount:

                                    /* 최종 데미지 계산  */
                                    float flatDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, flatDmgAmount, DamageType.FLAT_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingFlatDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, flatDmgAmount);
                                    // >> 이거.. 걍 hp 시스템으로 옮길거임
                                    break;

                                case ConditionType.criticalDamageAmount :

                                    /* 최종 데미지 계산  */
                                    float criticalDmgAmount = condition.value;

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, true, criticalDmgAmount, DamageType.CRITICAL_DAMAGE));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 치명타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingCriticalDamageAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, criticalDmgAmount);

                                    break;

                                case ConditionType.hpRecoveryAmount:

                                    /* 최종 회복량 계산  */
                                    float recoveryAmount = condition.value;
                                    //float recoveryAmount = condition.value * character.conditionComponent.hpRecoveryRate;
                                    if(recoveryAmount < 0f){ // 이런 경우가 있기는 할까??
                                        recoveryAmount = 0f;
                                    }

                                    /* 데미지 적용 */
                                    hpDamageHistory.add(new DamageHistory(buffAction.skillUserID, false, recoveryAmount));

                                    /* 데미지량 중계 */
                                    // 여기에 서버에서 클라이언트로 평타 데미지를 중계하는 RMI 호출문을 작성. 아래는 예시.
                                    // Logic_broadcastingHpRecoveryAmount(TARGET, rmi_ctx, EntityTye.CHARACTER, character.entityID, recoveryAmount);

                                    break;

                                default:
                                    break;
                            }
                        }
                    } // 쿨타임을 갖는 버프액션이 아님 끝

                } // 지속시간이 남아있는 버프액션에 대한 처리 끝

            } // 버프액션 한 개에 대한 처리 끝

            /* 지금까지 누적된 컴포넌트 정보로 캐릭터의 상태를 업데이트해준다. */
            crystal.conditionComponent = newCondition;  // 얉은복사(?)로 문제없으려나.. => 나중에 문제생기면, clone() 쓰지머.

            /** 테스트용 로그 출력 */

            /*System.out.println("isDisableMove : " + character.conditionComponent.isDisableMove);
            System.out.println("isDisableAttack : " + character.conditionComponent.isDisableAttack);
            System.out.println("isDisableSkill : " + character.conditionComponent.isDisableSkill);
            System.out.println("isDisableItem : " + character.conditionComponent.isDisableItem);
            System.out.println("isDamageImmunity : " + character.conditionComponent.isDamageImmunity);
            System.out.println("isUnTargetable : " + character.conditionComponent.isUnTargetable);
            System.out.println("moveSpeedRate : " + character.conditionComponent.moveSpeedRate);
            System.out.println("attackSpeedRate : " + character.conditionComponent.attackSpeedRate);
            System.out.println("hpRecoveryRate : " + character.conditionComponent.hpRecoveryRate);
            System.out.println("mpRecoveryRate : " + character.conditionComponent.mpRecoveryRate);
            System.out.println("goldGainRate : " + character.conditionComponent.goldGainRate);
            System.out.println("expGainRate : " + character.conditionComponent.expGainRate);
            System.out.println("buffDrationRate : " + character.conditionComponent.buffDurationRate);
            System.out.println("attackDamageRate : " + character.conditionComponent.attackDamageRate);
            System.out.println("defenseRate : " + character.conditionComponent.defenseRate);
            System.out.println("maxHPRate : " + character.conditionComponent.maxHPRate);
            System.out.println("maxMPRate : " + character.conditionComponent.maxMPRate);
            System.out.println("coolTimeReduceRate : " + character.conditionComponent.coolTimeReduceRate);
            System.out.println("moveSpeedBonus : " + character.conditionComponent.moveSpeedBonus);
            System.out.println("attackDamageBonus : " + character.conditionComponent.attackDamageBonus);
            System.out.println("defenseBouns : " + character.conditionComponent.defenseBonus);
            System.out.println("maxHPBonus : " + character.conditionComponent.maxHPBonus);
            System.out.println("maxMPBonus  : " + character.conditionComponent.maxMPBonus);*/

            /**************************/

        } // 크리스탈 엔티티 하나에 대한 처리 끝








    }

    /*


   앤티티 리스트 = 버프액션 히스토리 컴포넌트 목록 다 가져옴 (플레이어, 몬스터, 각종 월드맵의 Entity 등)

   앤티티 리스트만큼 반복{
    ----------------------------------------------
    앤티티의 버프액션 히스토리 리스트를 가져옴.

    버프액션 리스트 갯수만큼 반복{
    +++++++++++++++++++++++++++++++++++++++++++++
        버프액션 하나를 가져옴.

        버프액션의 남은 시간을 계산후 깎는다

        if( 남은 시간 < 0){
            기존에 적용중이던, 효과를 제거해야함.
            제거될 버프액션 객체에 있는 아래의 리스트에 접근 후,
            List< ConditionFloatParam > boolParam;
            List< ConditionBoolParam > floatParam;

            현재 엔티티의 Condition 객체에 위의 효과를 제거한다.


            그 후, 버프액션 리스트에서 해당 항목 제거를 하지 않는다.
            여기서 바로 지우지 않고, 제거 리스트에 추가만 해둔다.

            //아래 내용은 실행하지 않는다.
            해당 인덱스의 for문 i값을 --i 해준다. (다음 for문시 i++되면서 다음 요소에 접근 가능)
            continue
        }
        else{
        ===================================================
           쿨타임을 갖는 버프액션인지 구분한다
           if( 맞다면){

            남은 쿨타임을 계산한다.
            if(남은 쿨타임 <= 0){
                // 효과 발동
                // 현 버프액션의 boolParam 리스트를 돌면서, 데미지 또는 회복 등을 적용한다.

                현 버프액션의 floatParam 리스트를 돌면서, 데미지 또는 회복 등을 적용한다.
                for( 리스트 항목 갯수만큼 ){

                    현재 엔티티의 데미지 히스토리 리스트 객체를 불러와서 해당 데미지, 회복만큼 리스트에 추가해줌.
                    남은 효과발동 쿨타임을 다시, 원래 버프 효과발동 쿨타임 만큼 초기화 함.
             }
             //아직 효과발동 쿨타임이 남았다면,
             else{
                  틱 시간만큼 쿨타임 감소.
             }
            } //쿨타임을 갖는지 여부.

            //쿨타임이 아닌 버프 액션이라면
            else
            {
                현재 엔티티의 Condition 객체에 접근하여, 아래 리스트의 값들을 다 꺼내서 적용한다.
                //대입하는식으로 덮어씌우는게 아닌, 합연산으로 가야한다.
                List< ConditionFloatParam > boolParam;
                List< ConditionBoolParam > floatParam;

                for( List< ConditionFloatParam > boolParam, List< ConditionBoolParam > floatParam 반복)
                {
                    switch( ConditionType.컨디션값 )
                    {
                        case 항목:
                            현재 엔티티의 Condition 객체. moveSpeedBonus += boolParam.value;
                            or
                            현재 엔티티의 Condition 객체. moveSpeedBonus += floatParam.value;
                        break;
                    }
                }

            }
        =======================================================
        } //버프액션 else 종료.
        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    } //버프액션 리스트에서 1개의 버프액션 처리 종료.
        -------------------------------------------------------
}
    */

    /**
     * 아 이거는.. 각자 컨디션 컴포넌트라던가 뭐 앤티티에 있어야 할 거같은데..........
     * 또, 일단은 임시로?? 캐릭터에만 적용함 .
     * 아 이거 공식이.. 어느쪽을 먼저 적용해야 되는거지...
     * @param character
     */
    public void applyNewCharCondValue_HP(CharacterEntity character){

        ConditionComponent condition = character.conditionComponent;
        HPComponent hpComponent = character.hpComponent;

        System.out.println("최대체력 비율 : " + condition.maxHPRate);
        System.out.println("최대체력 보너스 : " + condition.maxHPBonus);
        System.out.println("오리지널 최대체력 : " + hpComponent.originalMaxHp);
        System.out.println("현재 최대체력 : " + hpComponent.maxHP);
        System.out.println("현재 체력 : " + hpComponent.currentHP);

        /** 최대 체력 처리 */
        float maxHpRate = condition.maxHPRate;
        hpComponent.maxHP = hpComponent.originalMaxHp + condition.maxHPBonus;
        hpComponent.maxHP *= maxHpRate;
        hpComponent.currentHP += (hpComponent.maxHP - hpComponent.originalMaxHp);
        if(hpComponent.currentHP > hpComponent.maxHP){
            hpComponent.currentHP = hpComponent.maxHP;
        }

        System.out.println("증가량 : " + (hpComponent.maxHP - hpComponent.originalMaxHp));
        System.out.println("현재 최대체력 : " + hpComponent.maxHP);
        System.out.println("현재 체력 : " + hpComponent.currentHP);

        System.out.println("===============================");

    }

    public void applyNewCharCondValue_MP(CharacterEntity character){

        ConditionComponent condition = character.conditionComponent;
        MPComponent mpComponent = character.mpComponent;

        /** 최대 마력 처리 */
        float maxMpRate = condition.maxMPRate;
        mpComponent.maxMP = mpComponent.originalMaxMP + condition.maxMPBonus;
        mpComponent.maxMP *= maxMpRate;
        mpComponent.currentMP += (mpComponent.maxMP - mpComponent.originalMaxMP);
        if(mpComponent.currentMP > mpComponent.maxMP){
            mpComponent.currentMP = mpComponent.maxMP;
        }

    }







}
