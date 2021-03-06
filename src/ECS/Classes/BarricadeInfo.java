package ECS.Classes;

public class BarricadeInfo implements Cloneable {

    //방호벽 타입
    public int barricadeType = 0;

    //방호벽 이름
    public String barricadeName = "방호벽"; //

    //방호벽 기본 최대체력
    public float maxHP = 0f;

    //방호벽 기본 체력 회복량. 포탑은 0이다.
    public float recoveryRateHP = 0f;

    //방호벽 기본 시야 거리
    public float lookRadius = 0f;

    //방호벽 기본 방어력
    public float defense = 0f;

    //방호벽 건설시 드는 시간
    public float costTime = 0f;

    //방호벽 건설시 드는 골드
    public int costGold = 0;

    public BarricadeInfo(int barricadeType, String barricadeName, float maxHP, float recoveryRateHP, float lookRadius, float defense, float costTime, int costGold) {
        this.barricadeType = barricadeType;
        this.barricadeName = barricadeName;
        this.maxHP = maxHP;
        this.recoveryRateHP = recoveryRateHP;
        this.lookRadius = lookRadius;
        this.defense = defense;
        this.costTime = costTime;
        this.costGold = costGold;
    }

    @Override
    public BarricadeInfo clone()  {
        BarricadeInfo clone;
        try {
            clone = (BarricadeInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
