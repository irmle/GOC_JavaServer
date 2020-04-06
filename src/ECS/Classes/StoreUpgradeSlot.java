package ECS.Classes;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 01 07 화
 * 업뎃날짜 :
 * 목    적 :
 *      상점에서 업그레이드 가능한 요소들을 관리하기 위한 클래스.
 */
public class StoreUpgradeSlot {

    /* 멤버 변수 */
    public int slotNum;
    public int upgradeType;
    public int upgradeLevel;

    /* 생성자 */
    public StoreUpgradeSlot(int upgradeType) {
        this.upgradeType = upgradeType;

        this.upgradeLevel = 0;
    }

    public StoreUpgradeSlot(int slotNum, int upgradeType) {
        this.slotNum = slotNum;
        this.upgradeType = upgradeType;

        this.upgradeLevel = 0;
    }




}
