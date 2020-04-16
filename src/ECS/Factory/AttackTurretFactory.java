package ECS.Factory;

import ECS.Classes.*;
import ECS.Classes.Type.*;
import ECS.Components.*;
import ECS.Entity.AttackTurretEntity;
import ECS.Entity.CharacterEntity;
import ECS.Game.GameDataManager;

import java.util.HashMap;

/**
 * 클래스명 : AttackTurretFactory.class
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 11월 19일 오후
 *
 * 목    적 :
 *      - 공격 포탑의 정보가 담긴 파일로부터 정보를 읽어와 초기화 작업을 한다.
 *      - 초기화된 정보를 바탕으로, 게임 내에서 특정 터렛 생성 요청이 있을 경우
 *      해당 터렛을 생성하여 반환해주는 역할을 한다
 *      - 업그레이드도 처리할 예정.
 *
 * 사용예시 :
 *
 * 이력사항 :
 *
 */
public class AttackTurretFactory {

    /** 멤버 변수 */
    public static HashMap<Integer, AttackTurretInfo> attackTurretInfoTable;
    public static HashMap<Integer, AttackTurretEntity> attackTurretEntityTable;

    /* 2020 04 03 추가 */
    /* 공격 포탑 타입별 효과 목록 */
    public static HashMap<Integer, HashMap<String, BuffInfo>> turretAttackEffectInfoLIST;


    /** 생성자 */


    /** 매서드 */

    public static void initFactory(){

        System.out.println("AttackTurret Factory 초기화중...");

        attackTurretInfoTable = new HashMap<>();
        attackTurretEntityTable = new HashMap<>();

        /* 파일을 읽어, 위 테이블을 채우는 처리를 작성할 것 */
        /* 위 처리 로직이 완성되기 전까지는, 필요한 스킬을 하드코딩해서 테이블에 넣어줄 것 */

        /** 파일로부터 정보 읽기 */
        readAttackTurretInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        convertAttackTurretInfoToEntity();

        turretAttackEffectInfoLIST = GameDataManager.effectInfoList.get(EffectCauseType.TURRET);

        System.out.println("AttackTurret Factory 초기화 완료");
    }

    public static void readAttackTurretInfo(){

        AttackTurretInfo attackTurretInfo;

        /* 공격포탑 기본 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_DEFAULT, 250, 5f, 500, 0f,
                         52f * 0.1f, 0.83f, 15f, 1);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_DEFAULT, attackTurretInfo);

        /* 공격포탑 타입1 *//*
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE1_UPGRADE1, 500, 5f, 5000, 0f,
                         300f, 0.83f, 0.5f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE1_UPGRADE1, attackTurretInfo);*/
        /* 공격포탑 타입1 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE1_UPGRADE1, 500, 5f, 5000, 0f,
                        30f, 0.83f, 15f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE1_UPGRADE1, attackTurretInfo);

        /* 공격포탑 타입1-2 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE1_UPGRADE2, 1000, 5f, 10000, 0f,
                         60f, 0.83f, 15f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE1_UPGRADE2, attackTurretInfo);

        /* 공격포탑 타입1-3 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE1_UPGRADE3, 2000, 5f, 5000, 0f,
                         90f, 0.83f, 15f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE1_UPGRADE3, attackTurretInfo);

        /* 공격포탑 타입2 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE2_UPGRADE1, 500, 5f, 5000, 0f,
                         20f, 0.83f, 21f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE2_UPGRADE1, attackTurretInfo);

        /* 공격포탑 타입2-2 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE2_UPGRADE2, 1000, 5f, 10000, 0f,
                         40f, 0.83f, 21f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE2_UPGRADE2, attackTurretInfo);

        /* 공격포탑 타입2-3 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE2_UPGRADE3, 2000, 5f, 5000, 0f,
                         60f, 0.83f, 21f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE2_UPGRADE3, attackTurretInfo);

        /* 공격포탑 타입3 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE3_UPGRADE1, 500, 5f, 5000, 0f,
                         15f, 1.13f, 18f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE3_UPGRADE1, attackTurretInfo);

        /* 공격포탑 타입3-2 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE3_UPGRADE2, 1000, 5f, 10000, 0f,
                         30f, 1.13f, 18f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE3_UPGRADE2, attackTurretInfo);

        /* 공격포탑 타입3-3 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE3_UPGRADE3, 2000, 5f, 15000, 0f,
                         45f, 1.13f, 18f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE3_UPGRADE3, attackTurretInfo);


    }

    /**
     * 2020 04 01 작성
     * 기존의 하드코딩 데이터읽기 매서드를 대체해, GDM가 파일로부터 읽어온 데이터를 활용해 Info를 구성하도록 함.
     */
    public static void readAttackTurretInfoFromFile(){

        AttackTurretInfo attackTurretInfo;

        /** GDMA의 공격포탑 목록을 하나씩 가져와 처리한다 */
        for ( HashMap.Entry<Integer, AttackTurretInfo> attackTurretInfoEntry
                : GameDataManager.attackTurretInfoList.entrySet()){

            attackTurretInfo = attackTurretInfoEntry.getValue();
            attackTurretInfoTable.put(attackTurretInfo.turretType, attackTurretInfo);

            //attackTurretInfo.printInfo();

        }

    }



    public static void convertAttackTurretInfoToEntity(){

        for( HashMap.Entry<Integer, AttackTurretInfo> attackTurretInfoEntry : attackTurretInfoTable.entrySet() ){

            AttackTurretInfo attackTurretInfo = attackTurretInfoEntry.getValue();

            /** 공격 타워 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Turret Component */
            TurretComponent turretComponent
                    = new TurretComponent(attackTurretInfo.turretType, attackTurretInfo.costTime, attackTurretInfo.costGold);

            /* Position Component */
            PositionComponent positionComponent
                    = new PositionComponent(0f, 0f, 0f);

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(attackTurretInfo.maxHp, attackTurretInfo.recoveryRateHP);

            /* Attack Component */




            AttackComponent attackComponent
                    = new AttackComponent(attackTurretInfo.attackDamage, attackTurretInfo.attackSpeed, attackTurretInfo.attackRange);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(attackTurretInfo.defense);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();


            /** 생성된 컴포넌트들을 가지고, 공격포탑 Entity 객체를 만든다 */

            /* AttackTurret Entity*/
            AttackTurretEntity attackTurretEntity
                    = new AttackTurretEntity(positionComponent, turretComponent, hpComponent,
                    attackComponent, defenseComponent,
                    buffActionHistoryComponent, hpHistoryComponent, conditionComponent);

            if(attackTurretEntity.conditionComponent == null){
                //System.out.println("어택터렛 팩토린데 널임 헐;; ");
            }
            else{
                //System.out.println("어택터렛 팩토리인데 널 아님");
            }


            /** 목록에 추가 */

            attackTurretEntityTable.put(attackTurretInfo.turretType, attackTurretEntity);

        }

    }

    public static AttackTurretEntity createAttackTurret(int requestedTurretType){

        AttackTurretEntity newTurret;

        newTurret = (AttackTurretEntity) ( attackTurretEntityTable.get(requestedTurretType) ).clone();

        //newTurret.attackComponent.printAttackInfo();


        return newTurret;
    }


    /**
     *      Aim : 스킬에서 적용하고자 하는 효과를 생성할 때 호출하면 된다!
     *    Input :
     *   Output :
     *  Process :
     *
     */
    public static BuffAction createAttackTurretEffect(int turretType, String effectName, AttackTurretEntity attackTurret, int effectEntityID){

        /** 터렛 효과 목록에서, 생성하고자 하는 effect 를 검색한다 */
        BuffInfo effectInfo = turretAttackEffectInfoLIST.get(turretType).get(effectName);

        /*System.out.println("이펙트 엔티티 ID : " + effectEntityID);
        System.out.println("공격터렛의 공격 효과에 대한 출력. ");
        */
        //effectInfo.printEffectInfo();

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


        /* 별도 예외처리가 필요하다면?? */


        /** 효과 객체를 생성한다 (틀) */
        // 효과정보 객체에 들어있는 정보를 바탕으로, BuffAction 객체를 생성한다.
        BuffAction newEffect = new BuffAction(ConditionType.damageAmount, effectDurationTime, effectInfo.remainCoolTime, effectInfo.effectCoolTime);


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
            ConditionFloatParam valueEffect = createEffectParam(turretType, effectInfo, attackTurret);
            newEffect.addEffect(valueEffect);

        }

        // 나중에.. 근거리 공격용? 매서드도 하나 만들자..
        newEffect.unitID = effectEntityID;
        newEffect.skillUserID = newEffect.unitID;
        newEffect.skillType = SkillType.MAGICIAN_FIREBALL;  // ;

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
    public static ConditionFloatParam createEffectParam(int turretType, BuffInfo effectInfo, AttackTurretEntity attackTurret){

        /* Input */
        int effectType = GameDataManager.getEffectTypeByParsingString(effectInfo.effectTypeName);
        String effectValueStr = effectInfo.effectValue;

        /* Output */
        float effectValue = 0f;
        ConditionFloatParam valueEffect;

        /* 효과값을 결정한다 */
        switch (effectValueStr){

            case "공격력" :

                /* 터렛의 공격력 값을 가져와 적용한다 */
                effectValue = attackTurret.attackComponent.attackDamage;

                //System.out.println("효과파람 생성 매서드 ; 공격력 타입 ;  " + effectValueStr);
                break;

            default :

                effectValue = Float.parseFloat( GameDataManager.removePercentage(effectValueStr) );

                //System.out.println("그 외 ; 이미 값이 정해져 있음. %나 파싱해");
                break;

        }

        /* 예외처리
            ; 일반 '데미지' 타입인 경우, 해당 공격이 평탄지, 크리티컬인지 판정도 거처야 한다. */

        switch (effectType){    // 효과타입Name == "데미지"로 하는게 의미상 더 정확하긴 할텐데..

            case ConditionType.damageAmount :
                valueEffect = SkillFactory.createDamageParam(effectValue, attackTurret.attackComponent, attackTurret.conditionComponent);
                break;

            default:

                valueEffect = new ConditionFloatParam(effectType, effectValue);
                break;
        }


        return valueEffect;

    }






    /*******************************************************************************************************************/




}
