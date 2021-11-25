package ECS.Factory;

import ECS.Classes.*;
import ECS.Classes.Type.*;
import ECS.Classes.Type.ConditionType;

import ECS.Components.*;
import ECS.Entity.AttackTurretEntity;
import ECS.Entity.BuffTurretEntity;
import ECS.Game.GameDataManager;

import javax.lang.model.type.IntersectionType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 클래스명 : BuffTurretFactory.class
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 11월 19일 오후
 *
 * 목    적 :
 *      - 버프 포탑의 정보가 담긴 파일로부터 정보를 읽어와 초기화 작업을 한다.
 *      - 초기화된 정보를 바탕으로, 게임 내에서 특정 터렛 생성 요청이 있을 경우
 *      해당 터렛을 생성하여 반환해주는 역할을 한다
 *      - 업그레이드도 처리할 예정.
 *
 * 사용예시 :
 *
 * 이력사항 :
 *
 */
public class BuffTurretFactory {

    /** 멤버 변수 */
    public static HashMap<Integer, BuffTurretInfo> buffTurretInfoTable;
    public static HashMap<Integer, BuffTurretEntity> buffTurretEntityTable;

    /* 버프 포탑 타입별 효과 목록 */
    public static HashMap<Integer, HashMap<String, BuffInfo>> turretBuffEffectInfoLIST;

    /** 생성자 */


    /** 매서드 */

    public static void initFactory(){

        System.out.println("BuffTurret Factory 초기화중...");

        buffTurretInfoTable = new HashMap<>();
        buffTurretEntityTable = new HashMap<>();

        /* 파일을 읽어, 위 테이블을 채우는 처리를 작성할 것 */
        /* 위 처리 로직이 완성되기 전까지는, 필요한 스킬을 하드코딩해서 테이블에 넣어줄 것 */

        /** 파일로부터 정보 읽기 */
        readBuffTurretInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        createBuffTurretEntityFromInfo();


        turretBuffEffectInfoLIST = GameDataManager.effectInfoList.get(EffectCauseType.TURRET);

        System.out.println("Buff TurretFactory 초기화 완료");
    }

    public static void readBuffTurretInfo(){

        BuffTurretInfo buffTurretInfo;

        /* 버프포탑 기본 (테스트용) */
        //원본
        /*buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_DEFAULT, 300, 5f, 3600f, 3f,
                        10, 15f, 0f, 5f,
                        ConditionType.hpRecoveryAmount, 15f, 5f);*/

        //임시용
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_DEFAULT, 300, 1f, 3600f, 3f,
                        10f, 15f, 0f, 5f,
                        ConditionType.hpRecoveryAmount, 30f, 5f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_DEFAULT, buffTurretInfo);


        /* 버프포탑 기본 *//*
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_DEFAULT, 300, 5000f, 3600f, 3f,
                        5, 15f * 1000, 5f * 1000, 5f * 1000,
                        ConditionType.hpRecoveryAmount, 5f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_DEFAULT, buffTurretInfo);*/

        /* 버프포탑 타입1 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE1_UPGRADE1, 600, 5f, 5400f, 3f,
                        5, 15f, 5f, 5f,
                        ConditionType.mpRecoveryAmount, 10f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE1_UPGRADE1, buffTurretInfo);

        /* 버프포탑 타입1-2 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE1_UPGRADE2, 1200, 5f, 7200f, 3f,
                        5, 15f, 5f, 5f,
                        ConditionType.mpRecoveryAmount, 20f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE1_UPGRADE2, buffTurretInfo);

        /* 버프포탑 타입1-3 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE1_UPGRADE3, 2400, 5f, 9000f, 3f,
                        5, 15f, 5f, 5f,
                        ConditionType.mpRecoveryAmount, 40f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE1_UPGRADE3, buffTurretInfo);

        /* 버프포탑 타입2 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE2_UPGRADE1, 600, 5f, 5400f, 3f,
                        10, 15f, -1, -1,
                        ConditionType.moveSpeedRate, 3f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE2_UPGRADE1, buffTurretInfo);

        /* 버프포탑 타입2-2 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE2_UPGRADE2, 1200, 5f, 7200f, 3f,
                        10, 15f, -1, -1,
                        ConditionType.moveSpeedRate, 6f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE2_UPGRADE2, buffTurretInfo);

        /* 버프포탑 타입2-3 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE2_UPGRADE3, 2400, 5f, 9000f, 3f,
                        10, 15f, -1, -1,
                        ConditionType.moveSpeedRate, 40f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE2_UPGRADE3, buffTurretInfo);

        /* 버프포탑 타입3 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE3_UPGRADE1, 600, 5f, 5400f, 3f,
                        7, 15f, 5f, 5f,
                        ConditionType.hpRecoveryAmount, 10f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE3_UPGRADE1, buffTurretInfo);

        /* 버프포탑 타입3-2 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE3_UPGRADE2, 1200, 5f, 7200f, 3f,
                        7, 15f, 5f, 5f,
                        ConditionType.hpRecoveryAmount, 20f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE3_UPGRADE2, buffTurretInfo);

        /* 버프포탑 타입3-3 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE3_UPGRADE3, 2400, 5f, 9000f, 3f,
                        7, 15f, 5f, 5f,
                        ConditionType.hpRecoveryAmount, 40f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE3_UPGRADE3, buffTurretInfo);

    }


    /**
     * 2020 04 01 작성.
     * GDM에서 파일로부터 읽어 구성한 BuffTurretInfo목록을 참조해,
     * 해당 클래스에서 사용할 터렛목록을 구성한다.
     */
    public static void readBuffTurretInfoFromFile(){

        BuffTurretInfo buffTurretInfo;

        /* GDM의 버프포탑 목록을 하나씩 읽어 처리한다 */
        for ( HashMap.Entry<Integer, BuffTurretInfo> buffTurretInfoEntry
                : GameDataManager.buffTurretInfoList.entrySet()){

            buffTurretInfo = buffTurretInfoEntry.getValue();
            buffTurretInfoTable.put(buffTurretInfo.turretType, buffTurretInfo);

            buffTurretInfo.printInfo();
        }


    }

    public static void convertBuffTurretInfoToEntity(){

        for( HashMap.Entry<Integer, BuffTurretInfo> buffTurretInfoEntry : buffTurretInfoTable.entrySet() ){

            BuffTurretInfo buffTurretInfo = buffTurretInfoEntry.getValue();

            /** 버프 타워 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Turret Component */
            TurretComponent turretComponent
                    = new TurretComponent(buffTurretInfo.turretType, buffTurretInfo.costTime, buffTurretInfo.costGold);

            /* Position Component */
            PositionComponent positionComponent
                    = new PositionComponent(0f, 0f, 0f);

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(buffTurretInfo.maxHp, buffTurretInfo.recoveryRateHP);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(buffTurretInfo.defense);

            /* Buff Component */
            /*      BuffAction      */
            /*          ConditionParam       */
            ConditionFloatParam floatParam = new ConditionFloatParam(buffTurretInfo.buffType, buffTurretInfo.buffValue);
            ArrayList<ConditionFloatParam> floatParams = new ArrayList<>();
            floatParams.add(floatParam);


            //시전자(unitID)는 나중에 채워줄 것, 0은 없다는 의미로 쓸 것. (내 개인적인 의견이고, 수헌씨랑 합의 봐야)
            BuffAction buffAction
                    = new BuffAction
                    (0, 0, buffTurretInfo.remainTime, buffTurretInfo.remainCoolTime,
                            buffTurretInfo.coolTime, new ArrayList<>(), floatParams);

            // 널.. 나중에 자료형을 바꾸거나, 얘를 넘겨주지말고 내부에서 초기화 및 생성하던가 하도록 바꿀 것.
            BuffComponent buffComponent
                    = new BuffComponent
                    (buffAction, buffTurretInfo.buffAreaRange);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();


            /** 생성된 컴포넌트들을 가지고, 공격포탑 Entity 객체를 만든다 */

            BuffTurretEntity buffTurretEntity
                    = new BuffTurretEntity
                    (positionComponent, turretComponent, hpComponent, defenseComponent,
                            buffComponent, buffActionHistoryComponent, hpHistoryComponent, conditionComponent);

            /** 목록에 추가 */

            buffTurretEntityTable.put(buffTurretInfo.turretType, buffTurretEntity);


        }

    }

    public static void createBuffTurretEntityFromInfo(){

        for( HashMap.Entry<Integer, BuffTurretInfo> buffTurretInfoEntry : buffTurretInfoTable.entrySet() ){

            BuffTurretInfo buffTurretInfo = buffTurretInfoEntry.getValue();

            /** 버프 타워 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Turret Component */
            TurretComponent turretComponent
                    = new TurretComponent(buffTurretInfo.turretType, buffTurretInfo.costTime, buffTurretInfo.costGold);

            /* Position Component */
            PositionComponent positionComponent
                    = new PositionComponent(0f, 0f, 0f);

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(buffTurretInfo.maxHp, buffTurretInfo.recoveryRateHP);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(buffTurretInfo.defense);

            /* Buff Component */
            BuffAction buffAction
                    = new BuffAction();

            BuffComponent buffComponent
                    = new BuffComponent
                    (buffAction, buffTurretInfo.buffAreaRange);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();


            /** 생성된 컴포넌트들을 가지고, 버프포탑 Entity 객체를 만든다 */

            BuffTurretEntity buffTurretEntity
                    = new BuffTurretEntity
                    (positionComponent, turretComponent, hpComponent, defenseComponent,
                            buffComponent, buffActionHistoryComponent, hpHistoryComponent, conditionComponent);

            /** 목록에 추가 */

            buffTurretEntityTable.put(buffTurretInfo.turretType, buffTurretEntity);


        }

    }

    public static BuffTurretEntity createBuffTurret(int requestedTurretID){

        BuffTurretEntity newTurret;

        newTurret = (BuffTurretEntity) ( buffTurretEntityTable.get(requestedTurretID) ).clone();

        return newTurret;
    }

    /**
     *      Aim : 스킬에서 적용하고자 하는 효과를 생성할 때 호출하면 된다!
     *    Input :
     *   Output :
     *  Process :
     *
     */
    public static BuffAction createBuffTurretEffect(int turretType, String effectName, BuffTurretEntity buffTurret, int effectEntityID){

        /** 터렛 효과 목록에서, 생성하고자 하는 effect 를 검색한다 */
        BuffInfo effectInfo = turretBuffEffectInfoLIST.get(turretType).get(effectName);

        /** 효과의 지속시간을 구한다 (필요하다면) */
        /*
         * 조건 : 효과의 적중 타입이 '지속'이면서 효과정보 객체에 들어있는 지속시간 값이 0 이하인 경우
         *          터렛정보 혹은 터렛 레벨정보 등을 통해 구해야 한다... 는 사실 지금은
         *          터렛 단계별로 각각 별도 타입인 것으로 분리되어 있지만,
         *              나중에.. 터렛의 단계? 스킬 레벨 처럼 취급되는 식으로 바뀔수도 있으니까.
         *              몬스터와 마찬가지로, 틀만 남겨둠.
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



        /** 효과 객체를 생성한다 (틀) */
        // 효과정보 객체에 들어있는 정보를 바탕으로, BuffAction 객체를 생성한다.
        BuffAction newEffect = new BuffAction(turretType, effectDurationTime, effectInfo.remainCoolTime, effectInfo.effectCoolTime);


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
            ConditionFloatParam valueEffect = createEffectParam(turretType, effectInfo, buffTurret);
            newEffect.addEffect(valueEffect);

        }

        // 나중에.. 근거리 공격용 매서드도 하나 만들자..
        newEffect.unitID = effectEntityID;
        newEffect.skillUserID = newEffect.unitID;
        newEffect.skillType = effectType;


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
     */
    public static ConditionFloatParam createEffectParam(int turretType, BuffInfo effectInfo, BuffTurretEntity buffTurret){

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
            ; 일반 '데미지' 타입인 경우, 해당 공격이 평탄지, 크리티컬인지 판정도 거처야 한다. */

        switch (effectType){    // 효과타입Name == "데미지"로 하는게 의미상 더 정확하긴 할텐데..

            default:

                valueEffect = new ConditionFloatParam(effectType, effectValue);
                break;
        }


        return valueEffect;

    }






    /*******************************************************************************************************************/






}
