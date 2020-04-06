package ECS.Classes;

import ECS.Classes.Type.ElementalType;
import ECS.Classes.Type.GradeType;
import ECS.Classes.Type.MonsterType;

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
