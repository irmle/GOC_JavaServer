package ECS.Classes.Type;

/**
 * 업뎃날짜 : 2020 03 20 권령희
 * 업뎃 내용 :
 *          귀환중 상태 추가.
 */
public class ConditionType {

    public static final int isDisableMove = 0; //이동 불가능
    public static final int isDisableAttack = 1; //공격 불가능
    public static final int isDisableSkill = 2; //스킬사용불가능
    public static final int isDisableItem = 3; //아이템사용불가능
    public static final int isDamageImmunity = 4; //데미지면역 (단, 회복효과는 적용됨)
    public static final int isUnTargetable = 5; //타겟지정불가능

    //일정비율 증감 -> 일정수치 증감 연산후에 적용 할 것.
    public static final int moveSpeedRate = 6; //이동속도 비율 제어 (기본값 1. 30% 느려진다면 0.7)
    public static final int attackSpeedRate = 7; //공격속도 비율 제어 (기본값 1. 25% 빨라진다면 1.25)
    public static final int hpRecoveryRate = 8; //체력 회복량 비율 제어 (기본값 1. 회복량 15%감소시 0.85)
    public static final int mpRecoveryRate = 9; //마나 회복량 비율 제어 (기본값 1. 회복량 15%감소시 0.85)
    public static final int goldGainRate = 10; //골드 획득량 비율 제어 (기본값 1. 10%증가시 1.10)
    public static final int expGainRate = 11; //경험치 획득량 비율 제어(기본값 1)
    public static final int buffDurationRate = 12; //버프 지속시간 비율 제어 (기본값 1)
    public static final int attackDamageRate = 13; //공격력 비율 제어 (기본값 1)
    public static final int defenseRate = 14; //방어력 비율 제어 (기본값 1)
    public static final int maxHPRate = 15; //최대체력 비율 제어 (기본값 1)
    public static final int maxMPRate = 16; //최대마나 비율 제어 (기본값 1)
    public static final int coolTimeReduceRate = 17; //쿨타임 감소비율 제어 (기본값 1)

    //일정수치 증감
    public static final int moveSpeedBonus = 18; //이동속도 추가 (기본값 0  50추가시 +50, 60감소시 -60.)
    public static final int attackDamageBonus = 19; //공격력 추가 (기본값 0  10추가시 +10, 30감소시 -30)
    public static final int defenseBonus = 20; //방어력 추가 (기본값 0)
    public static final int maxHPBonus = 21; //최대 체력 추가 (기본값 0)
    public static final int maxMPBonus = 22; //최대 마나 추가 (기본값 0)

    public static final int damageAmount = 23; //입는 데미지양
    public static final int hpRecoveryAmount = 24; // 체력 회복량
    public static final int mpRecoveryAmount = 25; // 마력 회복량

    // 면역? 추가된 상태
    public static final int isAirborneImmunity = 26;
    public static final int isAirborne = 27;

    // 스킬 효과 지속 여부를 판단하기 위해 추가한 것.

    public static final int isGarrenQApplied = 28; // 안쓸 듯..

    public static final int bloodSuckingRate = 29;

    public static final int isTargetingInvincible = 30;

    // 크리티컬
    public static final int criticalChanceRate = 31;    // 크리티컬이 발생할 비율
    public static final int criticalDamageRate = 32;    // 크리티컬 적용될 대미지 비율

    // 궁수 난사 스킬이 적용중인지 여부
    public static final int isArcherFireActivated = 33;

    // 버프 시스템에서 크리티컬 데미지를 별도로 처리하기 위해 어거지로 끼워넣음
    public static final int criticalDamageAmount = 34;

    // 스턴. 썬더
    public static final int isStunned = 35;

    // 궁수 해드샷
    public static final int isArcherHeadShotActivated = 36;

    // 빙결
    public static final int isFreezing = 37;

    // 둔화
    public static final int isSlow = 38;

    // 침묵
    public static final int isSilence = 39;

    // 실명
    public static final int isBlind = 40;

    // 시야 차단
    public static final int isSightBlocked = 41;

    // 고정
    public static final int isGrounding = 42;

    // 변이
    public static final int isPolymorph = 43;

    // 무장해제
    public static final int isDisarmed = 44;

    // 속박
    public static final int isSnare = 45;

    // 공중에 띄움
    public static final int isKnockedAirborne = 46;

    // 밀쳐냄
    public static final int isKnockback = 47;

    // 서스펜션
    public static final int isSuspension = 48;

    // 도발
    public static final int isTaunt = 49;

    // 매혹
    public static final int isCharm = 50;

    // 공포
    public static final int isFlee = 51;

    // 제압
    public static final int isSuppressed = 52;

    // 수면
    public static final int isSleep = 53;

    // 귀환중
    public static final int isReturning = 54;





}
