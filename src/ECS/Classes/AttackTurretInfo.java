package ECS.Classes;

/**
 * 클래스명 : AttackTurretInfo.class
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 11월 19일 오후
 *
 * 목    적 :
 *      - 공격 포탑의 각 종류, 단계별 스펙이 담긴 파일로부터 정보를 읽어올 때, 이 클래스에 담는다.
 *
 * 사용예시 :
 *
 * 이력사항 :
 *
 */
public class AttackTurretInfo implements Cloneable {

    /** 멤버 변수 */

    /* Turret Component */
    public int turretType;
    public String turretName = "공격 포탑 - 기본"; // 공격 포탑 - 타입1 Lv.1 , 버프 포탑 - 타입3 Lv.2

    public int costGold;
    public float costTime;

    /* HP Component */
    public float maxHp;
    public float recoveryRateHP;

    /* Attack Component */
    public float attackDamage;
    public float attackSpeed;
    public float attackRange;

    /* Defense Component */
    public float defense;

    //포탑 기본 시야 거리
    public float lookRadius;

    /** 생성자 */

    public AttackTurretInfo(int turretType, int costGold, float costTime, float maxHp, float recoveryRateHP, float attackDamage, float attackSpeed, float attackRange, float defense) {
        this.turretType = turretType;
        this.costGold = costGold;
        this.costTime = costTime;
        this.maxHp = maxHp;
        this.recoveryRateHP = recoveryRateHP;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.defense = defense;
    }

    @Override
    public Object clone()  {
        AttackTurretInfo clone;
        try {
            clone = (AttackTurretInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return clone;
    }
}
