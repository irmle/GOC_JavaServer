package ECS.Components;

/**
 * 업뎃날짜 : 2020 03 20 금요일  권령희
 * 업뎃 내용 :
 *      귀환중 상태 추가.
 *
 */
public class ConditionComponent implements Cloneable {

    public boolean isDisableMove; //이동 불가능
    public boolean isDisableAttack; //공격 불가능
    public boolean isDisableSkill; //스킬사용불가능
    public boolean isDisableItem; //아이템사용불가능
    public boolean isDamageImmunity; //데미지면역 (단, 회복효과는 적용됨)
    public boolean isUnTargetable; //타겟지정불가능

    public boolean isAirborneImmunity;
    public boolean isAriborne;

    // 2020 01 24
    public boolean isGarrenQApplied;
    public boolean isTargetingInvincible;

    // 2020 01 30
    public boolean isArcherFireActivated;

    // 2020 02 05
    public boolean isStunned;

    // 2020 02 21
    public boolean isArcherHeadShotActivated;

    // 2020 03 12
    // 빙결
    public boolean isFreezing;

    // 둔화
    public boolean isSlow;

    // 침묵
    public boolean isSilence;

    // 실명
    public boolean isBlind;

    // 시야 차단
    public boolean isSightBlocked;

    // 고정
    public boolean isGrounding;

    // 변이
    public boolean isPolymorph;

    // 무장해제
    public boolean isDisarmed;

    // 속박
    public boolean isSnare;

    // 공중에 띄움
    public boolean isKnockedAirborne;

    // 밀쳐냄
    public boolean isKnockback;

    // 서스펜션
    public boolean isSuspension;

    // 도발
    public boolean isTaunt;

    // 매혹
    public boolean isCharm;

    // 공포
    public boolean isFlee;

    // 제압
    public boolean isSuppressed;

    // 수면
    public boolean isSleep;

    /** 2020 03 20 권령희 추가 */
    // 귀환 (스킬)
    public boolean isReturning;


    //일정비율 증감 -> 일정수치 증감 연산후에 적용 할 것.
    public float moveSpeedRate; //이동속도 비율 제어 (기본값 1. 30% 느려진다면 0.7)
    public float attackSpeedRate; //공격속도 비율 제어 (기본값 1. 25% 빨라진다면 1.25)
    public float hpRecoveryRate; //체력 회복량 비율 제어 (기본값 1. 회복량 15%감소시 0.85)
    public float mpRecoveryRate; //마나 회복량 비율 제어 (기본값 1. 회복량 15%감소시 0.85)
    public float goldGainRate; //골드 획득량 비율 제어 (기본값 1. 10%증가시 1.10)
    public float expGainRate; //경험치 획득량 비율 제어(기본값 1)
    public float buffDurationRate; //버프 지속시간 비율 제어 (기본값 1)
    public float attackDamageRate; //공격력 비율 제어 (기본값 1)
    public float defenseRate; //방어력 비율 제어 (기본값 1)
    public float maxHPRate; //최대체력 비율 제어 (기본값 1)
    public float maxMPRate; //최대마나 비율 제어 (기본값 1)
    public float coolTimeReduceRate; //쿨타임 감소비율 제어 (기본값 1)

    public float bloodSuckingRate;  // 흡혈 비율. (기본값 0. 10% 증가시 0.10 -->> 아니면.. 기본값 1로 한 다음에, 계산할 때 1 빼주던가?? )

    //일정수치 증감
    public float moveSpeedBonus; //이동속도 추가 (기본값 0.  50추가시 +50, 60감소시 -60.)
    public float attackDamageBonus; //공격력 추가 (기본값 0.  10추가시 +10, 30감소시 -30)
    public float defenseBonus; //방어력 추가 (기본값 0.)
    public float maxHPBonus; //최대 체력 추가 (기본값 0.)
    public float maxMPBonus; //최대 마나 추가 (기본값 0.)

    // 크리티컬 2020 01 29
    // 크리티컬 발생 시, 기존 대미지 * 크리티컬 적용 비율 값이 최종 대미지가 될 듯.
    // 크리티컬의 기본값은 1인데(나중에 바뀔수도..?? ), 버프를 받아 10퍼 가 적용된다고 하면
    // 크리의 값은 1.1 뭐 이렇게 될거고.
    // 아 실제 겜에서 보통 어느정도 하는지를 보고싶어지네..
    // 크리 계산식은 나중에 따져보자. 다른 겜에서는 어케 적용되는지도.
    public float criticalChanceRate;    // 헐 이거는.. 기본값을 얼마로 해야?? 0 < X < 100 이어야 할거같은데..
    public float criticalDamageRate;    // 크리티컬 적용 대미지 비율. (기본값 1)



    public ConditionComponent() {
        //
        this.isDisableMove = false;
        this.isDisableAttack = false;
        this.isDisableSkill = false;
        this.isDisableItem = false;
        this.isDamageImmunity = false;
        this.isUnTargetable = false;
        this.isAirborneImmunity = false;

        this.isAriborne = false;
        this.isGarrenQApplied = false;
        this.isTargetingInvincible = false;

        this.isArcherFireActivated = false;
        this.isArcherHeadShotActivated = false;

        this.moveSpeedRate = 1f;
        this.attackSpeedRate = 1f;
        this.hpRecoveryRate = 1f;
        this.mpRecoveryRate = 1f;
        this.goldGainRate = 1f;
        this.expGainRate = 1f;
        this.buffDurationRate = 1f;
        this.attackDamageRate = 1f;
        this.defenseRate = 1f;
        this.maxHPRate = 1f;
        this.maxMPRate = 1f;
        this.coolTimeReduceRate = 1f;

        this.moveSpeedBonus = 0f;
        this.attackDamageBonus = 0f;
        this.defenseBonus = 0f;
        this.maxHPBonus = 0f;
        this.maxMPBonus = 0f;

        this.bloodSuckingRate = 0f;

        this.criticalChanceRate = 1f;
        this.criticalDamageRate = 1f;


        // 2020 03 12
        this.isStunned = false;
        this.isFreezing = false;
        this.isSlow = false;
        this.isSilence = false;
        this.isBlind = false;
        this.isSightBlocked = false;
        this.isGrounding = false;
        this.isPolymorph = false;
        this.isDisarmed = false;
        this.isSnare = false;
        this.isKnockedAirborne = false;
        this.isKnockback = false;
        this.isSuspension = false;
        this.isTaunt = false;
        this.isCharm = false;
        this.isFlee = false;
        this.isSuppressed = false;
        this.isSleep = false;

        this.isReturning = false;

    }

    public ConditionComponent(boolean isDisableMove, boolean isDisableAttack, boolean isDisableSkill, boolean isDisableItem, boolean isDamageImmunity, boolean isUnTargetable, float moveSpeedRate, float attackSpeedRate, float hpRecoveryRate, float mpRecoveryRate, float goldGainRate, float expGainRate, float buffDurationRate, float attackDamageRate, float defenseRate, float maxHPRate, float maxMPRate, float coolTimeReduceRate, float moveSpeedBonus, float attackDamageBonus, float defenseBonus, float maxHPBonus, float maxMPBonus) {
        this.isDisableMove = isDisableMove;
        this.isDisableAttack = isDisableAttack;
        this.isDisableSkill = isDisableSkill;
        this.isDisableItem = isDisableItem;
        this.isDamageImmunity = isDamageImmunity;
        this.isUnTargetable = isUnTargetable;
        this.moveSpeedRate = moveSpeedRate;
        this.attackSpeedRate = attackSpeedRate;
        this.hpRecoveryRate = hpRecoveryRate;
        this.mpRecoveryRate = mpRecoveryRate;
        this.goldGainRate = goldGainRate;
        this.expGainRate = expGainRate;
        this.buffDurationRate = buffDurationRate;
        this.attackDamageRate = attackDamageRate;
        this.defenseRate = defenseRate;
        this.maxHPRate = maxHPRate;
        this.maxMPRate = maxMPRate;
        this.coolTimeReduceRate = coolTimeReduceRate;
        this.moveSpeedBonus = moveSpeedBonus;
        this.attackDamageBonus = attackDamageBonus;
        this.defenseBonus = defenseBonus;
        this.maxHPBonus = maxHPBonus;
        this.maxMPBonus = maxMPBonus;

        this.isGarrenQApplied = false;
        this.bloodSuckingRate = 0f;
        this.isTargetingInvincible = false;


        this.criticalChanceRate = 1f;
        this.criticalDamageRate = 1f;

        this.isArcherFireActivated = false;
        this.isArcherHeadShotActivated = false;


        /* 2020 03 12 */
        this.isStunned = false;
        this.isFreezing = false;
        this.isSlow = false;
        this.isSilence = false;
        this.isBlind = false;
        this.isSightBlocked = false;
        this.isGrounding = false;
        this.isPolymorph = false;
        this.isDisarmed = false;
        this.isSnare = false;
        this.isKnockedAirborne = false;
        this.isKnockback = false;
        this.isSuspension = false;
        this.isTaunt = false;
        this.isCharm = false;
        this.isFlee = false;
        this.isSuppressed = false;
        this.isSleep = false;
    }

    public ConditionComponent(boolean isDisableMove, boolean isDisableAttack, boolean isDisableSkill, boolean isDisableItem, boolean isDamageImmunity, boolean isUnTargetable, boolean isAirborneImmunity, boolean isAriborne, float moveSpeedRate, float attackSpeedRate, float hpRecoveryRate, float mpRecoveryRate, float goldGainRate, float expGainRate, float buffDurationRate, float attackDamageRate, float defenseRate, float maxHPRate, float maxMPRate, float coolTimeReduceRate, float moveSpeedBonus, float attackDamageBonus, float defenseBonus, float maxHPBonus, float maxMPBonus) {
        this.isDisableMove = isDisableMove;
        this.isDisableAttack = isDisableAttack;
        this.isDisableSkill = isDisableSkill;
        this.isDisableItem = isDisableItem;
        this.isDamageImmunity = isDamageImmunity;
        this.isUnTargetable = isUnTargetable;
        this.isAirborneImmunity = isAirborneImmunity;
        this.isAriborne = isAriborne;
        this.moveSpeedRate = moveSpeedRate;
        this.attackSpeedRate = attackSpeedRate;
        this.hpRecoveryRate = hpRecoveryRate;
        this.mpRecoveryRate = mpRecoveryRate;
        this.goldGainRate = goldGainRate;
        this.expGainRate = expGainRate;
        this.buffDurationRate = buffDurationRate;
        this.attackDamageRate = attackDamageRate;
        this.defenseRate = defenseRate;
        this.maxHPRate = maxHPRate;
        this.maxMPRate = maxMPRate;
        this.coolTimeReduceRate = coolTimeReduceRate;
        this.moveSpeedBonus = moveSpeedBonus;
        this.attackDamageBonus = attackDamageBonus;
        this.defenseBonus = defenseBonus;
        this.maxHPBonus = maxHPBonus;
        this.maxMPBonus = maxMPBonus;

        this.isGarrenQApplied = false;
        this.bloodSuckingRate = 0f;
        this.isTargetingInvincible = false;


        this.criticalChanceRate = 1f;
        this.criticalDamageRate = 1f;

        this.isArcherFireActivated = false;
        this.isArcherHeadShotActivated = false;

        /* 2020 03 12 */
        this.isStunned = false;
        this.isFreezing = false;
        this.isSlow = false;
        this.isSilence = false;
        this.isBlind = false;
        this.isSightBlocked = false;
        this.isGrounding = false;
        this.isPolymorph = false;
        this.isDisarmed = false;
        this.isSnare = false;
        this.isKnockedAirborne = false;
        this.isKnockback = false;
        this.isSuspension = false;
        this.isTaunt = false;
        this.isCharm = false;
        this.isFlee = false;
        this.isSuppressed = false;
        this.isSleep = false;


        this.isReturning = false;

    }

    public ConditionComponent(boolean isDisableMove, boolean isDisableAttack, boolean isDisableSkill, boolean isDisableItem, boolean isDamageImmunity, boolean isUnTargetable, boolean isAirborneImmunity, boolean isAriborne, boolean isGarrenQApplied, boolean isTargetingInvincible, boolean isArcherFireActivated, boolean isStunned, boolean isArcherHeadShotActivated, boolean isFreezing, boolean isSlow, boolean isSilence, boolean isBlind, boolean isSightBlocked, boolean isGrounding, boolean isPolymorph, boolean isDisarmed, boolean isSnare, boolean isKnockedAirborne, boolean isKnockback, boolean isSuspension, boolean isTaunt, boolean isCharm, boolean isFlee, boolean isSuppressed, boolean isSleep, float moveSpeedRate, float attackSpeedRate, float hpRecoveryRate, float mpRecoveryRate, float goldGainRate, float expGainRate, float buffDurationRate, float attackDamageRate, float defenseRate, float maxHPRate, float maxMPRate, float coolTimeReduceRate, float bloodSuckingRate, float moveSpeedBonus, float attackDamageBonus, float defenseBonus, float maxHPBonus, float maxMPBonus, float criticalChanceRate, float criticalDamageRate) {
        this.isDisableMove = isDisableMove;
        this.isDisableAttack = isDisableAttack;
        this.isDisableSkill = isDisableSkill;
        this.isDisableItem = isDisableItem;
        this.isDamageImmunity = isDamageImmunity;
        this.isUnTargetable = isUnTargetable;
        this.isAirborneImmunity = isAirborneImmunity;
        this.isAriborne = isAriborne;
        this.isGarrenQApplied = isGarrenQApplied;
        this.isTargetingInvincible = isTargetingInvincible;
        this.isArcherFireActivated = isArcherFireActivated;
        this.isStunned = isStunned;
        this.isArcherHeadShotActivated = isArcherHeadShotActivated;
        this.isFreezing = isFreezing;
        this.isSlow = isSlow;
        this.isSilence = isSilence;
        this.isBlind = isBlind;
        this.isSightBlocked = isSightBlocked;
        this.isGrounding = isGrounding;
        this.isPolymorph = isPolymorph;
        this.isDisarmed = isDisarmed;
        this.isSnare = isSnare;
        this.isKnockedAirborne = isKnockedAirborne;
        this.isKnockback = isKnockback;
        this.isSuspension = isSuspension;
        this.isTaunt = isTaunt;
        this.isCharm = isCharm;
        this.isFlee = isFlee;
        this.isSuppressed = isSuppressed;
        this.isSleep = isSleep;
        this.moveSpeedRate = moveSpeedRate;
        this.attackSpeedRate = attackSpeedRate;
        this.hpRecoveryRate = hpRecoveryRate;
        this.mpRecoveryRate = mpRecoveryRate;
        this.goldGainRate = goldGainRate;
        this.expGainRate = expGainRate;
        this.buffDurationRate = buffDurationRate;
        this.attackDamageRate = attackDamageRate;
        this.defenseRate = defenseRate;
        this.maxHPRate = maxHPRate;
        this.maxMPRate = maxMPRate;
        this.coolTimeReduceRate = coolTimeReduceRate;
        this.bloodSuckingRate = bloodSuckingRate;
        this.moveSpeedBonus = moveSpeedBonus;
        this.attackDamageBonus = attackDamageBonus;
        this.defenseBonus = defenseBonus;
        this.maxHPBonus = maxHPBonus;
        this.maxMPBonus = maxMPBonus;
        this.criticalChanceRate = criticalChanceRate;
        this.criticalDamageRate = criticalDamageRate;


        this.isReturning = false;
    }

    @Override
    public Object clone() {
        ConditionComponent conditionComponent = null;
        try{
            conditionComponent = (ConditionComponent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return conditionComponent;
    }
}
