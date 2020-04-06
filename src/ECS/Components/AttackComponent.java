package ECS.Components;

/** Striking Power Component */

/**
 * 2020 01 31 업뎃 권령희
 * 크리티컬 추가
 */
public class AttackComponent implements Cloneable {

    public float remainCoolTime;
    public float attackDamage;
    public float attackSpeed; //1초에 가능한 공격횟수. 1.3일때, 1초당 1.3회 공격가능. 즉, 1회공격시 1/1.3 초 = 0.769초마다 1회 공격 가능)
    public float attackRange;

    public float criticalChance;    // 100분율.
    public float criticalDamage;    // 100분율.

    public AttackComponent() {

        this.remainCoolTime = 0f;
        this.attackDamage = 0f;
        this.attackSpeed = 0f;
        this.attackRange = 0f;

        // 엑셀파일 초기값으로 설정.
        this.criticalChance = 1f;
        this.criticalDamage = 50f;
    }


    public AttackComponent(float attackDamage, float attackSpeed, float attackRange) {
        this.remainCoolTime = 1/attackSpeed;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;

        // 엑셀파일 초기값으로 설정.
        this.criticalChance = 1f;
        this.criticalDamage = 50f;
    }

    @Override
    public Object clone()  {
        AttackComponent attackComponent;
        try{
            attackComponent = (AttackComponent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return attackComponent;
    }
}
