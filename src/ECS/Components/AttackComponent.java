package ECS.Components;

/** Striking Power Component */

/**
 * 2020 01 31 업뎃 권령희
 * 크리티컬 추가
 *
 * 2020 04 07 업뎃
 * ㄴ 밸런스 추가
 */
public class AttackComponent implements Cloneable {

    public float remainCoolTime;
    public float attackDamage;
    public float attackSpeed; //1초에 가능한 공격횟수. 1.3일때, 1초당 1.3회 공격가능. 즉, 1회공격시 1/1.3 초 = 0.769초마다 1회 공격 가능)
    public float attackRange;

    public float criticalChance;    // 100분율.
    public float criticalDamage;    // 100분율.

    public float balance;   // 밸런스.

    public AttackComponent() {

        this.remainCoolTime = 0f;
        this.attackDamage = 0f;
        this.attackSpeed = 0f;
        this.attackRange = 0f;

        // 엑셀파일 초기값으로 설정.
        this.criticalChance = 1f;
        this.criticalDamage = 50f;

        /**
         * 일단. 캐릭터의 경우, 80으로 고정해놓고
         * 나중에 50 ~ 100까지 올릴 수 있게 할 예정인데, 그래서 80으로 하려다가
         * 생각해보니까 몬스터 이런애들도 공격컴포넌트를 가짐. 얘들도 80으로 하게된다면.. 디폴트값을 80으로 하면 되지만
         * 혹시 모르니까 일단은 0으로 두는걸로.
         */
        this.balance = 0f;
    }


    public AttackComponent(float attackDamage, float attackSpeed, float attackRange) {
        this.remainCoolTime = 1/attackSpeed;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;

        // 엑셀파일 초기값으로 설정.
        this.criticalChance = 1f;
        this.criticalDamage = 50f;

        this.balance = 0f;
    }

    public AttackComponent(float attackDamage, float attackSpeed, float attackRange, float balance) {
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.balance = balance;

        // 엑셀파일 초기값으로 설정.
        this.criticalChance = 1f;
        this.criticalDamage = 50f;
    }


    public void printAttackInfo(){

        System.out.println("공격데미지 : " + attackDamage);

        System.out.println("공격속도 : " + attackSpeed);

        System.out.println("공격범위 : " + attackRange);



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
