package ECS.Classes;

/**
 * 클래스명 : BuffTurretInfo.class
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 11월 19일 오후
 *
 * 목    적 :
 *      - 버프 포탑의 각 종류, 단계별 스펙이 담긴 파일로부터 정보를 읽어올 때, 이 클래스에 담는다.
 *
 * 사용예시 :
 *
 * 이력사항 :
 *
 */
public class BuffTurretInfo implements Cloneable {

    /** 멤버 변수 */

    /* AttackTurret Component */
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

    @Override
    public Object clone()  {
        BuffTurretInfo clone;
        try {
            clone = (BuffTurretInfo) super.clone();
            //clone.buffAction = (BuffAction)buffAction.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return clone;
    }
}
