package ECS.Classes;


import ECS.Classes.Type.ElementalType;
import ECS.Classes.Type.GradeType;
import ECS.Classes.Type.MonsterType;

/**
 * 업뎃내용 :
 *  -- 몬스터 레벨에 따른 스탯 증가량을 담기 위한 변수들 추가.
 *  -- 생성자 추가
 */
//몬스터 타입을 기준으로 불러와지는 몬스터 데이터.
public class MonsterInfo implements Cloneable {

    //몬스터 타입
    public int monsterType = MonsterType.GOBLIN;

    //몬스터 등급
    public int monsterGrade = GradeType.NORMAL;

    //몬스터 속성
    public int monsterElemental = ElementalType.BLUE;

    //몬스터 이름
    public String monsterName = "고블린";

    //몬스터 기본 이동속도
    public float moveSpeed = 0f;

    //몬스터 기본 최대체력
    public float maxHP = 0f;

    //몬스터 기본 체력 회복량. 몬스터는 0이다.
    public float recoveryRateHP = 0f;

    //몬스터 기본 공격력
    public float attackDamage = 0f;

    //몬스터 기본 공격속도 (공격시 쿨타임. 1초당 공격가능 횟수)
    public float attackSpeed = 0f;

    //몬스터 기본 공격 사거리
    public float attackRange = 0f;

    //몬스터 기본 방어력
    public float defense = 0f;

    //몬스터 기본 시야 거리
    public float lookRadius = 0f;

    //처치시 보상 EXP
    public int rewardEXP = 0;

    //처치시 보상 Gold
    public int rewardGold = 0;

    /** 2020 02 27 권령희 추가 */
    public float regenTime = 0f;    // 정글몹의 경우에만 적용됨.
                                    // 일단 정글몹을 생성하기 위한 정보를 담는데도 이 클래스를 사용할건데, 후에 분리할 수 있음.

    /** 2020 03 30 권령희 추가 ; 몬스터 레벨에 따라 올라가는 증가량들 */
    public float hpIncrValue;
    public float hpRecoveryIncrValue;
    public float attackDamageIncrValue;
    public float defenseIncrValue;

    /** 2020 03 30 권령희 추가 ;  */
    public int rewardGoldIncrValue = 0;
    public int rewardExpIncrValue = 0;
    public boolean hasBuff = false; // 몹 처치 시 버프를 주는가??

    public float appearProbRate = 0f;



    public MonsterInfo(int monsterType, int monsterGrade, int monsterElemental, String monsterName, float maxHP, float recoveryRateHP, float attackDamage, float attackSpeed, float attackRange, float defense, float lookRadius, float moveSpeed) {
            this.monsterType = monsterType;
            this.monsterGrade = monsterGrade;
            this.monsterElemental = monsterElemental;
            this.monsterName = monsterName;
            this.maxHP = maxHP;
            this.recoveryRateHP = recoveryRateHP;
            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
            this.attackRange = attackRange;
            this.defense = defense;
            this.lookRadius = lookRadius;
            this.moveSpeed = moveSpeed;

            regenTime = 0f;
    }

    public MonsterInfo(int monsterType, int monsterGrade, int monsterElemental, String monsterName, float moveSpeed, float maxHP, float recoveryRateHP, float attackDamage, float attackSpeed, float attackRange, float defense, float lookRadius, float regenTime) {
        this.monsterType = monsterType;
        this.monsterGrade = monsterGrade;
        this.monsterElemental = monsterElemental;
        this.monsterName = monsterName;
        this.moveSpeed = moveSpeed;
        this.maxHP = maxHP;
        this.recoveryRateHP = recoveryRateHP;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.defense = defense;
        this.lookRadius = lookRadius;
        this.regenTime = regenTime;
    }

    /**
     * 2020 03 30 추가
     * csv 파일에서 읽어온 값을 통해 몬스터정보 생성 시 사용.
     *
     * @param monsterType
     * @param monsterGrade
     * @param monsterElemental
     * @param monsterName
     * @param moveSpeed
     * @param maxHP
     * @param recoveryRateHP
     * @param attackDamage
     * @param attackSpeed
     * @param attackRange
     * @param defense
     * @param lookRadius
     * @param hpIncrValue
     * @param hpRecoveryIncrValue
     * @param attackDamageIncrValue
     * @param defenseIncrValue
     */
    public MonsterInfo(int monsterType, int monsterGrade, int monsterElemental, String monsterName, float moveSpeed, float maxHP, float recoveryRateHP, float attackDamage, float attackSpeed, float attackRange, float defense, float lookRadius, float hpIncrValue, float hpRecoveryIncrValue, float attackDamageIncrValue, float defenseIncrValue) {
        this.monsterType = monsterType;
        this.monsterGrade = monsterGrade;
        this.monsterElemental = monsterElemental;
        this.monsterName = monsterName;
        this.moveSpeed = moveSpeed;
        this.maxHP = maxHP;
        this.recoveryRateHP = recoveryRateHP;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.defense = defense;
        this.lookRadius = lookRadius;
        this.hpIncrValue = hpIncrValue;
        this.hpRecoveryIncrValue = hpRecoveryIncrValue;
        this.attackDamageIncrValue = attackDamageIncrValue;
        this.defenseIncrValue = defenseIncrValue;

        this.regenTime = 0; // 정글몹이 아니므로 0
    }

    /**
     * 정글몹 전용 생성자.
     * @param monsterType
     * @param monsterGrade
     * @param monsterElemental
     * @param monsterName
     * @param moveSpeed
     * @param maxHP
     * @param recoveryRateHP
     * @param attackDamage
     * @param attackSpeed
     * @param attackRange
     * @param defense
     * @param lookRadius
     * @param rewardEXP
     * @param rewardGold
     * @param regenTime
     * @param hpIncrValue
     * @param hpRecoveryIncrValue
     * @param attackDamageIncrValue
     * @param defenseIncrValue
     * @param rewardGoldIncrValue
     * @param rewardExpIncrValue
     * @param hasBuff
     * @param appearProbRate
     */
    public MonsterInfo(int monsterType, int monsterGrade, int monsterElemental, String monsterName, float moveSpeed, float maxHP, float recoveryRateHP, float attackDamage, float attackSpeed, float attackRange, float defense, float lookRadius, int rewardEXP, int rewardGold, float regenTime, float hpIncrValue, float hpRecoveryIncrValue, float attackDamageIncrValue, float defenseIncrValue, int rewardGoldIncrValue, int rewardExpIncrValue, boolean hasBuff, float appearProbRate) {
        this.monsterType = monsterType;
        this.monsterGrade = monsterGrade;
        this.monsterElemental = monsterElemental;
        this.monsterName = monsterName;
        this.moveSpeed = moveSpeed;
        this.maxHP = maxHP;
        this.recoveryRateHP = recoveryRateHP;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
        this.defense = defense;
        this.lookRadius = lookRadius;
        this.rewardEXP = rewardEXP;
        this.rewardGold = rewardGold;
        this.regenTime = regenTime;
        this.hpIncrValue = hpIncrValue;
        this.hpRecoveryIncrValue = hpRecoveryIncrValue;
        this.attackDamageIncrValue = attackDamageIncrValue;
        this.defenseIncrValue = defenseIncrValue;
        this.rewardGoldIncrValue = rewardGoldIncrValue;
        this.rewardExpIncrValue = rewardExpIncrValue;
        this.hasBuff = hasBuff;
        this.appearProbRate = appearProbRate;
    }

    public void printMonsterInfo(){

        System.out.println(this.attackDamage);
        System.out.println(this.attackRange);
        System.out.println(this.attackSpeed);
        System.out.println(this.attackDamageIncrValue);
        System.out.println(this.defense);
        System.out.println(this.maxHP);
        System.out.println(this.monsterGrade);
        System.out.println(this.monsterElemental);
        System.out.println(this.defenseIncrValue);

    }



    @Override
    public Object clone()  {
        MonsterInfo clone;
        try {
            clone = (MonsterInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
