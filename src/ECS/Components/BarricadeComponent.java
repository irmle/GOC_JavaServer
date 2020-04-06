package ECS.Components;

/**
 * 업뎃날짜 : 2002 03 03 화 권령희
 * 업뎃내용 :
 *      강화레벨 추가
 *      costGold의 쓰임 ;
 *          최초 건설 후, 다음 강화레벨로 업그레이드 하기 위해 필요한 비용을 세팅해줄 것.
 *                      매 업그레이드 이후에도, 다음 업그레이드를 위해 필요한 비용을 세팅해줄 것.
 */
public class BarricadeComponent implements Cloneable {

    public float costTime; //건설시간
    public int costGold; //건설비용

    public int upgradeLevel;

    public BarricadeComponent() {
        costTime = 5000f;
        costGold = 500;
        upgradeLevel = 0;
    }

    public BarricadeComponent(float costTime, int costGold) {
        this.costTime = costTime;
        this.costGold = costGold;
        upgradeLevel = 0;
    }

    public int getCostGold() {
        return costGold;
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    @Override
    public Object clone() {
        BarricadeComponent barricadeComponent = null;
        try {
            barricadeComponent = (BarricadeComponent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return barricadeComponent;
    }
}