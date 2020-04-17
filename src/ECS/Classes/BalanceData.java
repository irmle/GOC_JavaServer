package ECS.Classes;

/**
 * 작성날짜 : 2020 04 03 금요일
 * 업뎃날짜 : 오후 10:44 2020-04-03
 *
 * -- 목적 : 게임 내에서 쓰이는 각종 데이터들의 밸런스를 조정하는 데 쓰이는 값들을 담아두기 위한 클래스임.
 *          예)) 캐릭터가 레벨업을 하는 데 필요한 경험치를 구하는 공식에 사용되는 가중치, 조정치 값 등등
 * --
 * --
 * --
 * --
 *
 */
public class BalanceData implements Cloneable{

    /* 어떤 종류의 밸런스를 위한 것인가?? */
    public int balanceDataType;
    public String balanceDataName;

    /* 관련 값 */
    public int maxLevel;   // 나중에 뺄수도.

    public float adjustmentValue;  // 조정치
    public float weightValue;      // 가중치

    public BalanceData(int balanceDataType, String balanceDataName, int maxLevel, float adjustmentValue, float weightValue) {
        this.balanceDataType = balanceDataType;
        this.balanceDataName = balanceDataName;
        this.maxLevel = maxLevel;
        this.adjustmentValue = adjustmentValue;
        this.weightValue = weightValue;
    }

    @Override
    public BalanceData clone() {

        BalanceData balanceData;

        try{
            balanceData = (BalanceData) super.clone();
        } catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }

        return balanceData;

    }
}
