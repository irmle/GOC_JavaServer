package ECS.System;

import ECS.Classes.*;
import ECS.Classes.Type.*;
import ECS.Entity.*;
import ECS.Factory.SkillFactory;
import ECS.Game.GameDataManager;
import ECS.Game.WorldMap;

import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 26 목 21:30
 * 업뎃날짜 :
 * 목    적 :
 *      캐릭터 및 기타 자가회복 속성을 갖는 앤티티들의 회복처리를 위한 시스템.
 *      현재 자가회복 속성은 체력 과 마력 두 가지가 있다.
 *          체력 회복의 경우, 캐릭터와 버프 포탑이 초당 N의 체력회복 속성을 가지고 있고,
 *          마력 회복의 경우, 캐릭터만이 초당 N의 회복 속성을 갖는다.
 *              그마저도 캐릭터 속성에 따라 마력회복을 하지 않는 타입도 있다 ; 전사 및 랜서류
 *
 */
public class SelfRecoverySystem {

    /* 멤버 변수 */
    WorldMap worldMap;
    float remainCoolTime;
    public static final float COOL_TIME = 1f;    // 자가회복 쿨타임 ; 1초

    /* 생성자 */
    public SelfRecoverySystem(WorldMap worldMap) {
        this.worldMap = worldMap;

        remainCoolTime = 0f;
    }

    /* 매서드 */
    public void onUpdate(float deltaTime){

        /** 회복효과 발동 가능 여부를 체크한다 */
        boolean isCoolTimeRemained = (remainCoolTime > 0) ? true: false;

        if(isCoolTimeRemained){ /* 쿨타임이 아직 남아있다면 */
            remainCoolTime -= deltaTime;
        }
        else{   /* 쿨타임이 끝났다면 */

            /* 모든 캐릭터에 대해 처리한다 */
            for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

                /* 캐릭터 정보 */
                CharacterEntity character = characterEntity.getValue();
                int characterID = character.entityID;

                /** 죽은 사람은 제외한다 */
                float currentHP = character.hpComponent.currentHP;
                if(currentHP <= 0){
                    continue;
                }

                /** 1초동안 지속되는 회복 버프를 넣어준다 */

                /*BuffAction recoveryBuff = new BuffAction();
                recoveryBuff.unitID = characterID;
                recoveryBuff.skillUserID = characterID;

                recoveryBuff.remainTime = 1f;
                recoveryBuff.remainCoolTime = 0f;
                recoveryBuff.coolTime = 1f;

                float hpRecoveryAmount = character.hpComponent.recoveryRateHP * character.conditionComponent.hpRecoveryRate;
                System.out.println("초당 체력회복량 : " + hpRecoveryAmount);
                recoveryBuff.floatParam.add(new ConditionFloatParam(ConditionType.hpRecoveryAmount, hpRecoveryAmount));

                float mpRecoveryAmount = character.mpComponent.recoveryRateMP * character.conditionComponent.mpRecoveryRate;
                System.out.println("초당 마력회복량 : " + mpRecoveryAmount);
                recoveryBuff.floatParam.add(new ConditionFloatParam(ConditionType.mpRecoveryAmount, mpRecoveryAmount));

                character.buffActionHistoryComponent.conditionHistory.add(recoveryBuff);*/


                character.buffActionHistoryComponent.conditionHistory.add(
                        createSystemActionEffect(
                                SystemEffectType.SELF_RECOVERY, "체력회복", character, characterID));

                character.buffActionHistoryComponent.conditionHistory.add(
                        createSystemActionEffect(
                                SystemEffectType.SELF_RECOVERY, "마력회복", character, characterID));


            }


            /* 크리스탈에 대해 처리한다 */
            for (HashMap.Entry<Integer, CrystalEntity> crystalEntity : worldMap.crystalEntity.entrySet()) {

                /* 캐릭터 정보 */
                CrystalEntity crystal = crystalEntity.getValue();
                int crystalID = crystal.entityID;

                /** 죽은 크리스탈은(..) 제외한다 */
                float currentHP = crystal.hpComponent.currentHP;
                if(currentHP <= 0){
                    continue;
                }

                /** 1초동안 지속되는 회복 버프를 넣어준다 */

                crystal.buffActionHistoryComponent.conditionHistory.add(
                        createSystemActionEffect(
                                SystemEffectType.SELF_RECOVERY, "체력회복", crystal.hpComponent.recoveryRateHP, crystalID));


            }

            /* 버프포탑에 대해 처리한다 */
            for (HashMap.Entry<Integer, BuffTurretEntity> buffTurretEntity : worldMap.buffTurretEntity.entrySet()) {

                /* 포탑 정보 */
                BuffTurretEntity buffTurret = buffTurretEntity.getValue();
                int turretID = buffTurret.entityID;

                /** 죽은 포탑 제외한다 */
                float currentHP = buffTurret.hpComponent.currentHP;
                if(currentHP <= 0){
                    continue;
                }

                /** 1초동안 지속되는 회복 버프를 넣어준다 */

                buffTurret.buffActionHistoryComponent.conditionHistory.add(
                        createSystemActionEffect(
                                SystemEffectType.SELF_RECOVERY, "체력회복", buffTurret.hpComponent.recoveryRateHP, turretID));


            }

            /* 바리케이드에 대해 처리한다 */
            for (HashMap.Entry<Integer, BarricadeEntity> barricadeEntity : worldMap.barricadeEntity.entrySet()) {

                /* 바리케이듣 정보 */
                BarricadeEntity barricade = barricadeEntity.getValue();
                int barricadeID = barricade.entityID;

                /** 죽은 바리케이드는 제외한다 */
                float currentHP = barricade.hpComponent.currentHP;
                if(currentHP <= 0){
                    continue;
                }

                /** 1초동안 지속되는 회복 버프를 넣어준다 */

                barricade.buffActionHistoryComponent.conditionHistory.add(
                        createSystemActionEffect(
                                SystemEffectType.SELF_RECOVERY, "체력회복", barricade.hpComponent.recoveryRateHP, barricadeID));


            }




            remainCoolTime = COOL_TIME;
        }
    }



    public static BuffAction createSystemActionEffect(int type, String effectName, CharacterEntity character, int effectEntityID){

        HashMap<Integer, HashMap<String, BuffInfo>> systemEffectList = GameDataManager.effectInfoList.get(EffectCauseType.SYSTEM);

        /** 시스템 효과 목록에서, 생성하고자 하는 effect 를 검색한다 */
        BuffInfo effectInfo = systemEffectList.get(type).get(effectName);
        //effectInfo.printEffectInfo();

        /** 효과의 지속시간을 구한다 (필요하다면) */
        /*
         * 조건 : 효과의 적중 타입이 '지속'이면서 효과정보 객체에 들어있는 지속시간 값이 0 이하인 경우
         *
         * 참고)) 현재, 몬스터 공격에 의한 효과(사실상 그냥 데미지)의 경우, 데미지 한 종류밖에 존재하지 않음..
         *          나중에, 몬스터 공격에 의한 다른 효과 타입 등이 추가될 수 있으므로,
         *          캐릭터 스킬 효과 생성 처리에서 사용했던 틀은 남겨두도록 함.
         */
        float effectDurationTime;
        boolean needToGetDurationTime =
                (( effectInfo.effectAppicationType == EffectApplicationType.지속)
                        && ( effectInfo.effectDurationTime <= 0f)) ? true : false;
        if(needToGetDurationTime){

            effectDurationTime = effectInfo.effectDurationTime;
        }
        else{

            effectDurationTime = effectInfo.effectDurationTime;
        }



        // 하드코딩 타입 보정....ㅜ
        int skillType = 0;
        if(type == SystemEffectType.WELL){
            skillType = SkillType.WELL_RECOVERY;
        }
        else if(type == SystemEffectType.SELF_RECOVERY){

            skillType = SkillType.NONE;
        }




        /** 효과 객체를 생성한다 (틀) */
        // 효과정보 객체에 들어있는 정보를 바탕으로, BuffAction 객체를 생성한다.
        BuffAction newEffect = new BuffAction(skillType, effectDurationTime, effectInfo.remainCoolTime, effectInfo.effectCoolTime);


        /** 효과 내용을 채운다 */
        // BuffAction 객체에, 실제 효과를 부여하기 위한 처리를 한다. 경우에 따라, 공격자 정보를 참조해야 한다.

        int effectType = GameDataManager.getEffectTypeByParsingString(effectName);
        boolean isConditionEffect = checkIsConditionEffect(effectType);
        if(isConditionEffect){

            /* 상태이상을 결정하는 효과 타입인 경우, boolParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionBoolParam conditionEffect = new ConditionBoolParam(effectType, true);
            newEffect.addEffect(conditionEffect);
        }
        else{

            /* 기존 스탯 등에 영향을 미치는 버프 OR 디버프 효과 타입인 경우, floatParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionFloatParam valueEffect = createEffectParam(type, effectInfo, character);
            newEffect.addEffect(valueEffect);

        }

        // 나중에.. 근거리 공격용? 매서드도 하나 만들자..
        newEffect.unitID = effectEntityID;
        newEffect.skillUserID = newEffect.unitID;

        /* Output */
        return newEffect;

    }

    public static BuffAction createSystemActionEffect(int type, String effectName, float recoveryAmount, int effectEntityID){

        HashMap<Integer, HashMap<String, BuffInfo>> systemEffectList = GameDataManager.effectInfoList.get(EffectCauseType.SYSTEM);

        /** 시스템 효과 목록에서, 생성하고자 하는 effect 를 검색한다 */
        BuffInfo effectInfo = systemEffectList.get(type).get(effectName);
        //effectInfo.printEffectInfo();

        /** 효과의 지속시간을 구한다 (필요하다면) */
        /*
         * 조건 : 효과의 적중 타입이 '지속'이면서 효과정보 객체에 들어있는 지속시간 값이 0 이하인 경우
         *
         * 참고)) 현재, 몬스터 공격에 의한 효과(사실상 그냥 데미지)의 경우, 데미지 한 종류밖에 존재하지 않음..
         *          나중에, 몬스터 공격에 의한 다른 효과 타입 등이 추가될 수 있으므로,
         *          캐릭터 스킬 효과 생성 처리에서 사용했던 틀은 남겨두도록 함.
         */
        float effectDurationTime;
        boolean needToGetDurationTime =
                (( effectInfo.effectAppicationType == EffectApplicationType.지속)
                        && ( effectInfo.effectDurationTime <= 0f)) ? true : false;
        if(needToGetDurationTime){

            effectDurationTime = effectInfo.effectDurationTime;
        }
        else{

            effectDurationTime = effectInfo.effectDurationTime;
        }



        // 하드코딩 타입 보정....ㅜ
        int skillType = 0;
        if(type == SystemEffectType.WELL){
            skillType = SkillType.WELL_RECOVERY;
        }
        else if(type == SystemEffectType.SELF_RECOVERY){

            skillType = SkillType.NONE;
        }




        /** 효과 객체를 생성한다 (틀) */
        // 효과정보 객체에 들어있는 정보를 바탕으로, BuffAction 객체를 생성한다.
        BuffAction newEffect = new BuffAction(skillType, effectDurationTime, effectInfo.remainCoolTime, effectInfo.effectCoolTime);


        /** 효과 내용을 채운다 */
        // BuffAction 객체에, 실제 효과를 부여하기 위한 처리를 한다. 경우에 따라, 공격자 정보를 참조해야 한다.

        int effectType = GameDataManager.getEffectTypeByParsingString(effectName);
        boolean isConditionEffect = checkIsConditionEffect(effectType);
        if(isConditionEffect){

            /* 상태이상을 결정하는 효과 타입인 경우, boolParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionBoolParam conditionEffect = new ConditionBoolParam(effectType, true);
            newEffect.addEffect(conditionEffect);
        }
        else{

            /* 기존 스탯 등에 영향을 미치는 버프 OR 디버프 효과 타입인 경우, floatParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionFloatParam valueEffect = new ConditionFloatParam(ConditionType.hpRecoveryAmount, recoveryAmount);
            newEffect.addEffect(valueEffect);

        }

        // 나중에.. 근거리 공격용? 매서드도 하나 만들자..
        newEffect.unitID = effectEntityID;
        newEffect.skillUserID = newEffect.unitID;

        /* Output */
        return newEffect;

    }

    /**
     * 넘겨받은 효과가 상태 이상 타입의 효과인지 여부를 판단하는 매서드
     * @return
     */
    public static boolean checkIsConditionEffect(int effectType){

        boolean isConditionEffect = false;

        switch (effectType){

            case ConditionType.isDisableMove :
            case ConditionType.isDisableAttack :
            case ConditionType.isDisableSkill :
            case ConditionType.isDisableItem :
            case ConditionType.isDamageImmunity :
            case ConditionType.isUnTargetable :

            case ConditionType.isAirborneImmunity :
            case ConditionType.isAirborne :
            case ConditionType.isGarrenQApplied :
            case ConditionType.isTargetingInvincible :
            case ConditionType.isArcherFireActivated :
            case ConditionType.isStunned :
            case ConditionType.isArcherHeadShotActivated :
            case ConditionType.isFreezing :
            case ConditionType.isSlow :
            case ConditionType.isSilence :
            case ConditionType.isBlind :
            case ConditionType.isSightBlocked :
            case ConditionType.isGrounding :
            case ConditionType.isPolymorph :
            case ConditionType.isDisarmed :
            case ConditionType.isSnare :
            case ConditionType.isKnockedAirborne :
            case ConditionType.isKnockback :
            case ConditionType.isSuspension :
            case ConditionType.isTaunt :
            case ConditionType.isCharm :
            case ConditionType.isFlee :
            case ConditionType.isSuppressed :
            case ConditionType.isSleep :
            case ConditionType.isReturning :

                isConditionEffect = true;
                break;

            default:
                isConditionEffect = false;
        }

        return isConditionEffect;
    }

    /**
     * 상태이상이 아닌 타입의 스킬 효과 이펙트를 생성하는 매서드
     *
     * 아 이름짓는거때문에 먼가 통일하고싶은데.. bool 이랑 param 이랑.. 그럴 여유는 없겟지..
     */
    public static ConditionFloatParam createEffectParam(int type,  BuffInfo effectInfo, CharacterEntity character){

        /* Input */
        int effectType = GameDataManager.getEffectTypeByParsingString(effectInfo.effectTypeName);
        String effectValueStr = effectInfo.effectValue;

        /* Output */
        float effectValue = 0f;
        ConditionFloatParam valueEffect;

        /* 효과값을 결정한다 */
        switch (effectValueStr){

            case "스탯" :

                /* 캐릭터의 회복 값을 가져와 적용한다 */
                effectValue = getProperEffectValue(type, effectInfo.effectTypeName, effectType, character);

                //System.out.println("효과파람 생성 매서드 ; 스탯 타입 ");
                break;

            default :

                effectValue = Float.parseFloat( GameDataManager.removePercentage(effectValueStr) );

                //System.out.println("그 외 ; 이미 값이 정해져 있음. %나 파싱해");
                break;

        }

        /* 예외처리
            ; 일반 '데미지' 타입인 경우, 해당 공격이 평탄지, 크리티컬인지 판정도 거처야 한다. */

        switch (effectType){    // 효과타입Name == "데미지"로 하는게 의미상 더 정확하긴 할텐데..

            default:

                valueEffect = new ConditionFloatParam(effectType, effectValue);
                break;
        }


        return valueEffect;

    }


    /**
     * 일단 껍데기만 남겨둠.
     * 몬스터의 경우, 아직까지는 데미지 이외에 별도 버프/디버프/상태이상을 주는 효과가 존재하지 않기 때문에.
     */
    public static float getProperEffectValue(int type, String effectName, int effectType, CharacterEntity character){

        /* Output */
        float effectValue = 0f;

        switch (effectType){

            case ConditionType.hpRecoveryAmount :

                effectValue = character.hpComponent.recoveryRateHP * character.conditionComponent.hpRecoveryRate;
                break;

            case ConditionType.mpRecoveryAmount :

                effectValue = character.mpComponent.recoveryRateMP * character.conditionComponent.mpRecoveryRate;
                break;


        }

        return effectValue;

    }

    /*******************************************************************************************************************/
    /**
     * 2020 04 18 새벽 작성
     * 필요 데이터를 GDM으로부터 클론하여 사용하게끔, 초반에 미리 복사해두는 처리
     */

    /**
     * 기    능 : 템슬롯 시스템에서 필요로 하는 데이터를, GDM에서 복사해온다.
     * 처    리 :
     *      ItemSlotSystem에서 필요로 하는 GDM 데이터는 다음과 같다
     *      -- (아이템) 효과 정보 목록
     *      -- 파싱, %제거 매서드... >> 뭐 길지도 않고.. 중요한 처리도 아니니까? 그냥 일단 복붙해다가 쓰지머.
     *
     */
    public void getNeedDataFromGDM(){

        /* 초기화 처리 */


        /* 템 효과 정보 목록을 복사한다 */
        bringItemEffectInfoListFromGDM();


    }


    public void bringItemEffectInfoListFromGDM(){

        HashMap<Integer, HashMap<String, BuffInfo>> itemEffectInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, HashMap<String, BuffInfo>> itemEffectInfo
                : GameDataManager.effectInfoList.get(EffectCauseType.ITEM).entrySet()){

            int itemKey = itemEffectInfo.getKey();

            HashMap<String, BuffInfo> itemEffectValue = new HashMap<>();
            for( HashMap.Entry<String, BuffInfo> effectInfo : itemEffectInfo.getValue().entrySet()){

                String effectKey = effectInfo.getKey();
                BuffInfo effectValue = effectInfo.getValue();

                itemEffectValue.put(effectKey, effectValue);

            }

            itemEffectInfoList.put(itemKey, itemEffectValue);

        }

    }


    /**
     * 효과 타입 구하기
     * @param str
     * @return
     */
    public int getEffectTypeByParsingString(String str){

        int effectType;
        switch (str){

            case "데미지":
                effectType = ConditionType.damageAmount;
                break;
            case "이동속도":
            case "이동속도증가":
                effectType = ConditionType.moveSpeedRate;
                break;
            case "흡혈":
                effectType = ConditionType.bloodSuckingRate;
                break;
            case "공격속도":
                effectType = ConditionType.attackSpeedRate;
                break;
            case "최대체력증가":
                effectType = ConditionType.maxHPRate;
                break;
            case "에어본":
                effectType = ConditionType.isAirborne;
                break;
            case "무적":
                effectType = ConditionType.isTargetingInvincible;
                break;
            case "슬로우":
                effectType = ConditionType.moveSpeedRate;
                break;
            case "체력회복":
                effectType = ConditionType.hpRecoveryAmount;
                break;
            case "방어력증가":
                effectType = ConditionType.defenseBonus;
                break;
            case "장판 데미지":
                effectType = ConditionType.damageAmount;
                break;
            case "스턴":
                effectType = ConditionType.isStunned;
                break;
            case "빙결":
                effectType = ConditionType.isFreezing;
                break;
            case "헤드샷활성화":
                effectType = ConditionType.isArcherHeadShotActivated;
                break;
            case "크리뎀":
                effectType = ConditionType.criticalDamageAmount;
                break;
            case "치명확률":
                effectType = ConditionType.criticalChanceRate;
                break;
            case "치명데미지증가":
                effectType = ConditionType.criticalDamageRate;
                break;
            case "난사활성화":
                effectType = ConditionType.isArcherFireActivated;
                break;
            case "1차 데미지":
            case "2차 데미지":
            case "보스 1차데미지":
            case "보스 2차데미지":
                effectType = ConditionType.criticalDamageAmount;
                break;
            case "귀환상태":
                effectType = ConditionType.isReturning;
                break;
            case "마력회복":
                effectType = ConditionType.mpRecoveryAmount;
                break;
            case "최대마력증가":
                effectType = ConditionType.maxMPRate;
                break;
            case "추가 데미지" :
                effectType = ConditionType.damageAmount;
                break;

            case "체력회복속도" :
            case "체력회복 속도증가" :
                effectType = ConditionType.hpRecoveryRate;
                break;

            case "마력회복속도" :
            case "마력회복 속도증가" :
                effectType = ConditionType.mpRecoveryRate;
                break;

            default:
                effectType = -1;
                break;

        }

        return effectType;

    }

    /**
     * 퍼센테이지 값을 갖는 문자열들에서 '%'를 제거해줌.
     * 일부 float 값들을 파싱할 때에 사용.
     * @param str
     * @return
     */
    public String removePercentage(String str){

        if(str.contains("%")){
            str = str.substring(0, str.indexOf("%"));
        }

        return str;
    }


}
