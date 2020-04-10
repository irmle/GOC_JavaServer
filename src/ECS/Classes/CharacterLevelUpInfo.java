package ECS.Classes;
/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 16 월
 * 업뎃날짜 :
 * 목    적 :
 *      캐릭터 레벨 업 시, 캐릭터 타입별로 변화하는 스탯 정보를 담아두기 위해 만든 클래스.
 *      현재, Game / GameDataManager 파일에서 이 클래스를 활용한 데이터테이블을 관리하도록 되어있다.
 */
public class CharacterLevelUpInfo {

    /** 멤버 변수 */

    //기본 최대체력
    public float maxHP;

    //기본 최대마나
    public float maxMP;

    //기본 공격력
    public float attackDamage;

    //기본 공격속도 (공격시 쿨타임. 1초당 공격가능 횟수)
    public float attackSpeed;

    //기본 1초당 체력 회복량
    public float recoveryRateHP;

    //기본 1초당 마나 회복량
    public float recoveryRateMP;

    //기본 방어력
    public float defense;

    // 치명타 확률
    public float criticalProbRate;

    // 치명타 데미지
    public float criticalDamage;

    //기본 이동속도
    public float moveSpeed;

    //기본 공격 사거리
    public float attackRange;

    //기본 시야 거리
    public float lookRadius;

    /** 생성자 */
    public CharacterLevelUpInfo(float maxHP, float maxMP, float attackDamage, float attackSpeed, float recoveryRateHP, float recoveryRateMP, float defense, float criticalProbRate, float criticalDamage, float moveSpeed, float attackRange, float lookRadius) {
        this.maxHP = maxHP;
        this.maxMP = maxMP;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.recoveryRateHP = recoveryRateHP;
        this.recoveryRateMP = recoveryRateMP;
        this.defense = defense;
        this.criticalProbRate = criticalProbRate;
        this.criticalDamage = criticalDamage;
        this.moveSpeed = moveSpeed;
        this.attackRange = attackRange;
        this.lookRadius = lookRadius;

        System.out.println("크리티걸 발생 확률 증가량 : " + criticalProbRate );
        System.out.println("크리티걸 데미지 증가량 : " + criticalDamage );
    }
}
