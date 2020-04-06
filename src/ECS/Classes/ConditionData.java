package ECS.Classes;

public class ConditionData {

    public boolean isDisableMove;  //이동 불가능
    public boolean isDisableAttack;  //공격 불가능
    public boolean isDisableSkill;  //스킬사용불가능
    public boolean isDisableItem;  //아이템사용불가능
    public boolean isDamageImmunity;  //데미지면역 (단, 회복효과는 적용됨)
    public boolean isUnTargetable;  //타겟지정불가능

    //일정비율 증감 -> 일정수치 증감 연산후에 적용 할 것.
    public float moveSpeedRate;  //이동속도 비율 제어 (기본값 1. 30% 느려진다면 0.7)
    public float attackSpeedRate;  //공격속도 비율 제어 (기본값 1. 25% 빨라진다면 1.25)
    public float hpRecoveryRate;  //체력 회복량 비율 제어 (기본값 1. 회복량 15%감소시 0.85)
    public float mpRecoveryRate;  //마나 회복량 비율 제어 (기본값 1. 회복량 15%감소시 0.85)
    public float goldGainRate;  //골드 획득량 비율 제어 (기본값 1. 10%증가시 1.10)
    public float expGainRate;  //경험치 획득량 비율 제어(기본값 1)
    public float buffDurationRate;  //버프 지속시간 비율 제어 (기본값 1)
    public float attackDamageRate;  //공격력 비율 제어 (기본값 1)
    public float defenseRate;  //방어력 비율 제어 (기본값 1)
    public float maxHPRate;  //최대체력 비율 제어 (기본값 1)
    public float maxMPRate;  //최대마나 비율 제어 (기본값 1)
    public float coolTimeReduceRate;  //쿨타임 감소비율 제어 (기본값 1)

    //일정수치 증감
    public float moveSpeedBonus;  //이동속도 추가 (기본값 0.  50추가시 +50, 60감소시 -60.)
    public float attackDamageBonus;  //공격력 추가 (기본값 0.  10추가시 +10, 30감소시 -30)
    public float defenseBonus;  //방어력 추가 (기본값 0.)
    public float maxHPBonus;  //최대 체력 추가 (기본값 0.)
    public float maxMPBonus;  //최대 마나 추가 (기본값 0.)
}
