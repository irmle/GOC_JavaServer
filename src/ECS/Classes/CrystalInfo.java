package ECS.Classes;

public class CrystalInfo implements Cloneable {

    //크리스탈 이름
    public String crystalName = "방호벽"; //

    //크리스탈 강화 레벨
    public float level = 0f;

    //크리스탈 기본 최대체력
    public float maxHP = 0f;

    //크리스탈 기본 체력 회복량. 포탑은 0이다.
    public float recoveryRateHP = 0f;

    //크리스탈 기본 시야 거리
    public float lookRadius = 0f;

    //크리스탈 기본 방어력
    public float defense = 0f;


    @Override
    public Object clone()  {
        CrystalInfo clone;
        try {
            clone = (CrystalInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
