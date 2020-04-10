package ECS.Classes;

import java.util.Random;

/**
 * 클래스명 : AttackTurretInfo.class
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 11월 19일 오후
 * 목    적 :
 *      - 공격 포탑의 각 종류, 단계별 스펙이 담긴 파일로부터 정보를 읽어올 때, 이 클래스에 담는다.
 * 업뎃날짜 : 2020 03 31 화요일
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

    /** 2020 04 10 임시로 추가함 */
    public float flyingObjSpeed = 0f;
    public float flyingObjAttackRadius = 0f;

    /** 2020 03 31 추가 */
    public float minAttackDamage = 0f;
    public float maxAttackDamage = 0f;

    /* Defense Component */
    public float defense;

    //포탑 기본 시야 거리
    public float lookRadius = 0f;

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


    /**
     * csv 파일 파싱 전용 생성자
     * @param turretType
     * @param turretName
     * @param costGold
     * @param costTime
     * @param maxHp
     * @param recoveryRateHP
     * @param attackDamage
     * @param attackSpeed
     * @param attackRange
     * @param minAttackDamage
     * @param maxAttackDamage
     * @param defense
     */
    public AttackTurretInfo(int turretType, String turretName, int costGold, float costTime, float maxHp, float recoveryRateHP, float attackDamage, float attackSpeed, float attackRange, float minAttackDamage, float maxAttackDamage, float defense, float flyingObjSpeed, float flyingObjAttackRadius) {
        this.turretType = turretType;
        this.turretName = turretName;
        this.costGold = costGold;
        this.costTime = costTime;
        this.maxHp = maxHp;
        this.recoveryRateHP = recoveryRateHP;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.minAttackDamage = minAttackDamage;
        this.maxAttackDamage = maxAttackDamage;
        this.defense = defense;
        this.flyingObjSpeed = flyingObjSpeed;
        this.flyingObjAttackRadius = flyingObjAttackRadius;

        decideAttackDamage();
    }

    public void decideAttackDamage(){

        if(attackDamage <= 0){

            int attackDam = (int)( (Math.random() * (maxAttackDamage - minAttackDamage) + 1) + minAttackDamage );
            this.attackDamage = attackDam;
        }
        System.out.println("공격력 결정 결과 : " + attackDamage);


    }

    public void printInfo(){

        System.out.println("터렛 타입 "  + turretType);
        System.out.println("터렛 이름 "  + turretName);

        System.out.println("비용 "  + costGold);
        System.out.println("시간 "  + costTime);
        System.out.println("체력 "  + maxHp);

        System.out.println("회복률 "  + recoveryRateHP);
        System.out.println("공격력 "  + attackDamage);
        System.out.println("공속 "  + attackSpeed);

        System.out.println("공격범위 "  + attackRange);
        System.out.println("최소댐 "  + minAttackDamage);
        System.out.println("최대댐 "  + maxAttackDamage);
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
