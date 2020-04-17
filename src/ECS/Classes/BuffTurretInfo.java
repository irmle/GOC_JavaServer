package ECS.Classes;

/**
 * 클래스명 : BuffTurretInfo.class
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 11월 19일 오후
 *
 * 목    적 :
 *      - 버프 포탑의 각 종류, 단계별 스펙이 담긴 파일로부터 정보를 읽어올 때, 이 클래스에 담는다.
 * 업뎃날짜 : 2020 03 31 화
 *
 *
 */
public class BuffTurretInfo implements Cloneable {

    /** 멤버 변수 */

    /* BuffTurret Component */
    public int turretType;

    public String turretName = "공격 포탑 - 기본"; // 공격 포탑 - 타입1 Lv.1 , 버프 포탑 - 타입3 Lv.2

    public int costGold;
    public float costTime;

    /* HP Component */
    public float maxHp;
    public float recoveryRateHP;

    /* Buff Component */
    public float buffAreaRange;

    /* BuffAction */
    public float remainTime; // 버프의 남은 지속시간.
    public float remainCoolTime; // 버프 효과발동 쿨타임.
    public float coolTime; //원래 버프 효과발동 쿨타임.

    public int buffType;
    public float buffValue;

    /* Defense Component */
    public float defense;

    /** 2020 03 31 화 추가 */
    public float minDefense = 0f;
    public float maxDefense = 0f;

    public float buffSpeed = 0.83f;



    public BuffTurretInfo(int turretType, int costGold, float costTime, float maxHp, float recoveryRateHP, float buffAreaRange, float remainTime, float remainCoolTime, float coolTime, int buffType, float buffValue, float defense) {
        this.turretType = turretType;
        this.costGold = costGold;
        this.costTime = costTime;
        this.maxHp = maxHp;
        this.recoveryRateHP = recoveryRateHP;
        this.buffAreaRange = buffAreaRange;
        this.remainTime = remainTime;
        this.remainCoolTime = remainCoolTime;
        this.coolTime = coolTime;
        this.buffType = buffType;
        this.buffValue = buffValue;
        this.defense = defense;
    }

    /**
     * csv 파일 읽고 파싱, 생성
     * @param turretType
     * @param turretName
     * @param costGold
     * @param costTime
     * @param maxHp
     * @param recoveryRateHP
     * @param buffAreaRange
     * @param defense
     * @param minDefense
     * @param maxDefense
     * @param buffSpeed
     */
    public BuffTurretInfo(int turretType, String turretName, int costGold, float costTime, float maxHp, float recoveryRateHP, float buffAreaRange, float defense, float minDefense, float maxDefense, float buffSpeed) {
        this.turretType = turretType;
        this.turretName = turretName;
        this.costGold = costGold;
        this.costTime = costTime;
        this.maxHp = maxHp;
        this.recoveryRateHP = recoveryRateHP;
        this.buffAreaRange = buffAreaRange;
        this.defense = defense;
        this.minDefense = minDefense;
        this.maxDefense = maxDefense;
        this.buffSpeed = buffSpeed;

        decideDefense();
    }

    public void decideDefense(){

        if(defense <= 0){

            int defense = (int)( (Math.random() * (maxDefense - minDefense) + 1) + minDefense );
            this.defense = defense;
        }
        System.out.println("방어력 결정 결과 : " + this.defense);


    }

    public void printInfo(){

        System.out.println("터렛 타입 "  + turretType);
        System.out.println("터렛 이름 "  + turretName);

        System.out.println("비용 "  + costGold);
        System.out.println("시간 "  + costTime);
        System.out.println("체력 "  + maxHp);

        System.out.println("회복률 "  + recoveryRateHP);
        System.out.println("방어력 "  + defense);
        System.out.println("버프속도 "  + buffSpeed);

        System.out.println("공격범위 "  + buffAreaRange);
        System.out.println("버프타입 "  + buffType);
        System.out.println("버프값 "  + buffValue);
    }

    /**
     * csv파일 파싱용 생성자
     * @return
     */



    @Override
    public BuffTurretInfo clone()  {
        BuffTurretInfo clone;
        try {
            clone = (BuffTurretInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return clone;
    }
}
