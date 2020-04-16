package ECS.Factory;

import ECS.Classes.*;
import ECS.Classes.Type.*;
import ECS.Classes.Type.BalanceData.BalanceDataType;
import ECS.Classes.Type.Jungle.JungleMobType;
import ECS.Entity.AttackTurretEntity;
import ECS.Game.GameDataManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 12월 16일 월요일
 * 업뎃날짜 : 오전 12:33 2020-04-04
 * 목    적 :
 *      - 각종 보상 처리와 관련된 데이터를 파일로부터 읽어온다.
 *      - 유저 인터랙션에 따라 특정 보상이 주어질 필요가 있을 경우,
 *          읽어온 데이터를 기반으로 적절한 보상 객체를 만들어 제공하는 역할을 한다.
 *      - 보상 타입별 보상 생성 매서드 작성 예정. 현재는 몹 사냥에 대한 보상만 존재한다.
 *
 * 업뎃내용 :
 *      하드코딩 보상 테이블 >> 엑셀에 있는 보상 데이터 테이블 계산하는 매서드 만들고 적용
 */
public class RewardFactory {

    /** 멤버 변수 */

    // 몬스터 레벨에 따른 보상 테이블
    public static HashMap<Integer, RewardInfo> rewardInfoByMobLevelTable;

    /** 2020 04 03 */
    /* 정글몹 타입별 효과 목록 */
    public static HashMap<Integer, HashMap<String, BuffInfo>> jungleBuffEffectInfoLIST;

    public static HashMap<Integer, MonsterInfo> jungleMobInfoList = GameDataManager.jungleMonsterInfoList;


    /** 생성자 */


    /** 매서드 */

    public static void initFactory(){

        System.out.println("Reward Factory 초기화중... ");

        // 각 멤버변수 생성 및 초기화
        rewardInfoByMobLevelTable = new HashMap<>();

        // 파일로부터 정보 읽기
        readRewardInfoFromFile();

        //printInfo();

        jungleBuffEffectInfoLIST = GameDataManager.effectInfoList.get(EffectCauseType.JUNGLE);


        System.out.println("Reward Factory 초기화 완료 ");
    }

    /**
     * 각종 보상 정보를 읽어오는 매서드들을 모두 호출하는 함수
     */
    public static void readRewardInfoFromFile(){

        readMonsterRewardInfoFromFile();

    }

    /**
     * 특정 몬스터를 죽였을 때 받을 수 있는 보상 정보가 몬스터 타입별로 정리되어 있는 파일을 읽어온다! 라고 가정
     * 현재는 구현된 몹 종류가 없어서, 일단은 전부 다 하드코딩으로 동일하게 둠.
     * 현재 동일하게 나타나는 리치 몬스터의 경우, MonsterType = NONE으로 두고 진행할 것.
     */
    public static void readMonsterRewardInfoFromFile(){

        // 나중에.. 별도 설정파일 읽어서 처리하던가 해야지.. 이건 임시
        int MOB_MAX_LEVEL = GameDataManager.balanceDataInfoList.get(BalanceDataType.REWARD_EXP_PER_MONSTER_LEVEL).maxLevel;

        RewardInfo rewardInfo;
        for(int i=1; i<=MOB_MAX_LEVEL; i++ ){  // 해시맵이니까 1부터
            float exp = calculateRewardExpByLevel(i);
            int gold = calculateRewardGoldByLevel(i);

            rewardInfo = new RewardInfo(exp, gold);
            rewardInfoByMobLevelTable.put(i, rewardInfo);
        }
    }


    /**
     * 특정 몬스터를 죽인 것에 대한 보상을 생성할 때 호출되는 함수
     * - 나중에, createReward()라는 상위 매서드를 만들어서,
     *      인자로 받은 타입에 따라 하위 create함수를 호출하게끔 바꿀 것. (가능할지?)
     *
     *  2019 12 26 목 변경
     *      몹 살해에 대한 보상을.. 살해한 몹의 정보에 따라 주는것이 아닌, 캐릭터 레벨이 따라 지급
     *      나중에 함수를 분리하던가 다시 정리하던가 해야될거같다.
     */
    public static Reward createRewardByKillMonster(int deadEntityType, int mobLevel){

        Reward reward;

        int rewardType = RewardType.KILL_MONSTER;
        float rewardExp = rewardInfoByMobLevelTable.get(mobLevel).exp;
        int rewardGold = rewardInfoByMobLevelTable.get(mobLevel).gold;

        reward = new Reward(rewardType, rewardExp, rewardGold);

        return reward;
    }

    /**
     * 현재는.. 일반 캐릭터가 바로 몹을 죽일 때와 똑같음
     * @param deadEntityType
     * @param mobLevel
     * @return
     */
    public static Reward createRewardOfKillMonsterByTurret(int deadEntityType, int mobLevel){

        Reward reward;

        /* 읽어온 리워드 파일 및 인자를 참고해서, 넘겨줄 리워드를 생성하는 처리 */
        // 나중에.. 더 복잡하고 상세한 처리를 할 것.

        int rewardType = RewardType.KILL_MONSTER_BY_TURRET;
        float rewardExp = rewardInfoByMobLevelTable.get(mobLevel).exp;
        int rewardGold = rewardInfoByMobLevelTable.get(mobLevel).gold;

        reward = new Reward(rewardType, rewardExp, rewardGold);

        return reward;
    }


    /**
     * 주어진 (몬스터) 레벨에 따라 받을 수 있는 경험치를 계산해주는 함수
     */
    public static float calculateRewardExpByLevel(int currentLevel){

        final float EXP_WEIGHT_VALUE = GameDataManager.balanceDataInfoList.get(BalanceDataType.REWARD_EXP_PER_MONSTER_LEVEL).weightValue;
        final float EXP_ADJUSTMENT_VALUE = GameDataManager.balanceDataInfoList.get(BalanceDataType.REWARD_EXP_PER_MONSTER_LEVEL).adjustmentValue;

        float exp = (float) (EXP_WEIGHT_VALUE * Math.pow(currentLevel, 2) + EXP_ADJUSTMENT_VALUE );

        return exp;
    }

    /**
     * 주어진  (몬스터) 레벨에 받을 수 있는 골드를 계산해주는 함수
     * 최종결과에 올림 해줘야 함
     */
    public static int calculateRewardGoldByLevel(int currentLevel){

        final float GOLD_WEIGHT_VALUE = GameDataManager.balanceDataInfoList.get(BalanceDataType.REWARD_GOLD_PER_MONSTER_LEVEL).weightValue;
        final float GOLD_ADJUSTMENT_VALUE = GameDataManager.balanceDataInfoList.get(BalanceDataType.REWARD_GOLD_PER_MONSTER_LEVEL).adjustmentValue;

        double calculGold = (GOLD_WEIGHT_VALUE * Math.pow(currentLevel, 2) + GOLD_ADJUSTMENT_VALUE );
        int gold = (int) Math.ceil(calculGold);

        return gold;
    }

    /**
     * 테스트용 출력 함수
     */
    public static void printInfo(){

        for(int i=1; i<=rewardInfoByMobLevelTable.size(); i++){

            RewardInfo info = rewardInfoByMobLevelTable.get(i);
            //System.out.println("레벨" + i + "의 보상 경험치 : " + info.exp + ", 보상 골드 : " + info.gold);
        }

    }



    /**
     *
     * @param deadEntityType
     * @param deadEntityID
     * @return
     */
    public static Reward createRewardByKillJungleMonster(int deadEntityType, int deadEntityID, int deadEntityLevel){

        Reward reward;

        int rewardType = RewardType.KILL_MONSTER_JUNGLE_MONSTER;
        float rewardExp = 0f;
        int rewardGold = 0;

        ArrayList<BuffAction> rewardBuffList = new ArrayList<>();

        BuffAction rewardBuff = new BuffAction();
        rewardBuff.unitID = deadEntityID;
        rewardBuff.skillUserID = deadEntityID;
        rewardBuff.coolTime = -1;
        rewardBuff.remainCoolTime = -1;
        rewardBuff.remainTime = 0f;

        /* 일단 하드코딩으로 */
        switch (deadEntityType){

            /*
            case JungleMobType.HUMAN1 :
            case JungleMobType.HUMAN2 :
            case JungleMobType.HUMAN3 :
            case JungleMobType.HUMAN4 :

                break;
            */

            case  JungleMobType.LIZARD :

                /** 2020 04 03 */
                rewardBuffList.add(
                        createJungleBuffEffect(deadEntityType, "최대체력증가", deadEntityID) );

                //System.out.println("리자드 죽임");

                break;

            case JungleMobType.FAIRY :

                /** 2020 04 03 */
                rewardBuffList.add(
                        createJungleBuffEffect(deadEntityType, "최대마력증가", deadEntityID) );

                //System.out.println("요정 죽임");
                break;

            case JungleMobType.DRAGON :

                /** 2020 04 03 */
                rewardBuffList.add(
                        createJungleBuffEffect(deadEntityType, "공격력증가", deadEntityID) );

                rewardBuffList.add(
                        createJungleBuffEffect(deadEntityType, "공격속도증가", deadEntityID) );

                rewardBuffList.add(
                        createJungleBuffEffect(deadEntityType, "이동속도증가", deadEntityID) );


                //System.out.println("드래곤 죽임");
                break;

            case JungleMobType.DEVIL :

                /** 2020 04 03 */
                rewardBuffList.add(
                        createJungleBuffEffect(deadEntityType, "치명데미지증가", deadEntityID) );

                rewardBuffList.add(
                        createJungleBuffEffect(deadEntityType, "치명확률", deadEntityID) );

                rewardBuffList.add(
                        createJungleBuffEffect(deadEntityType, "방어력증가", deadEntityID) );


                //System.out.println("악마 죽임");
                break;

        }


        rewardExp = getJungleMonsterRewardExp(rewardType, deadEntityLevel);
        rewardGold = getJungleMonsterRewardGold(rewardType, deadEntityLevel);
        reward = new Reward(rewardType, rewardExp, rewardGold, rewardBuffList);

        return reward;
    }


    public static float getJungleMonsterRewardExp(int jungleMobType, int jungleMobLevel){

        MonsterInfo jungleMobInfo = jungleMobInfoList.get(jungleMobType);

        float expValue = jungleMobInfo.rewardEXP;
        float expIncrValuePerLV = jungleMobInfo.rewardExpIncrValue;


        float rewardExp = expValue + (expIncrValuePerLV * jungleMobLevel);

        return rewardExp;

    }

    public static int getJungleMonsterRewardGold(int jungleMobType, int jungleMobLevel){

        MonsterInfo jungleMobInfo = jungleMobInfoList.get(jungleMobType);

        float goldValue = jungleMobInfo.rewardGold;
        float goldIncrValuePerLV = jungleMobInfo.rewardGoldIncrValue;


        double calculGold = goldValue + (goldIncrValuePerLV * jungleMobLevel);

        int rewardGold = (int) Math.ceil(calculGold);
        return rewardGold;

    }





    /**
     *      Aim : 스킬에서 적용하고자 하는 효과를 생성할 때 호출하면 된다!
     *    Input :
     *   Output :
     *  Process :
     *
     */
    public static BuffAction createJungleBuffEffect(int jungleMobType, String effectName, int effectEntityID){

        /** 터렛 효과 목록에서, 생성하고자 하는 effect 를 검색한다 */
        BuffInfo effectInfo = jungleBuffEffectInfoLIST.get(jungleMobType).get(effectName);

        /** 효과의 지속시간을 구한다 (필요하다면) */
        /*
         * 조건 : 효과의 적중 타입이 '지속'이면서 효과정보 객체에 들어있는 지속시간 값이 0 이하인 경우
         *
         */
        float effectDurationTime;
        boolean needToGetDurationTime =
                (( effectInfo.effectAppicationType == EffectApplicationType.지속)
                        && ( effectInfo.effectDurationTime <= 0f)) ? true : false;
        if(needToGetDurationTime){

            effectDurationTime = effectInfo.effectDurationTime;
        }
        else{

            /* 지속시간 값을 별도로 구해 줄 필요가 없다면, 기존에 들어있는 값을 가져와 그대로 적용하면 된다. */
            effectDurationTime = effectInfo.effectDurationTime;
        }


        /* 별도 예외처리가 필요하다면?? */


        /** 효과 객체를 생성한다 (틀) */
        // 효과정보 객체에 들어있는 정보를 바탕으로, BuffAction 객체를 생성한다.
        BuffAction newEffect = new BuffAction(jungleMobType, effectDurationTime, effectInfo.remainCoolTime, effectInfo.effectCoolTime);


        /** 효과 내용을 채운다 */
        // BuffAction 객체에, 실제 효과를 부여하기 위한 처리를 한다. 경우에 따라, 스킬 시전자 정보 혹은 스킬레벨 정보를 참조해야 한다.

        int effectType = GameDataManager.getEffectTypeByParsingString(effectName);
        boolean isConditionEffect = checkIsConditionEffect(effectType);
        if(isConditionEffect){

            /* 상태이상을 결정하는 효과 타입인 경우, boolParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionBoolParam conditionEffect = new ConditionBoolParam(effectType, true);
            newEffect.addEffect(conditionEffect);
        }
        else{

            /* 기존 스탯 등에 영향을 미치는 버프 OR 디버프 효과 타입인 경우, floatParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionFloatParam valueEffect = createEffectParam(jungleMobType, effectInfo);
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
    public static ConditionFloatParam createEffectParam(int jungleMobType, BuffInfo effectInfo){

        /* Input */
        int effectType = GameDataManager.getEffectTypeByParsingString(effectInfo.effectTypeName);
        String effectValueStr = effectInfo.effectValue;

        /* Output */
        float effectValue = 0f;
        ConditionFloatParam valueEffect;

        /* 효과값을 결정한다 */
        switch (effectValueStr){

            default :

                effectValue = Float.parseFloat( GameDataManager.removePercentage(effectValueStr) );

                //System.out.println("그 외 ; 이미 값이 정해져 있음. %나 파싱해");
                break;

        }

        /* 예외처리
            ; 일반 '데미지' 타입인 경우, 해당 공격이 평탄지, 크리티컬인지 판정도 거처야 한다.
            그리고, 버프에는 데미지가 없다.. */

        switch (effectType){    // 효과타입Name == "데미지"로 하는게 의미상 더 정확하긴 할텐데..


            default:

                valueEffect = new ConditionFloatParam(effectType, effectValue);
                break;
        }


        return valueEffect;

    }






    /*******************************************************************************************************************/




}
