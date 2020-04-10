package ECS.Classes;

import ECS.Classes.Type.Upgrade.StoreUpgradeType;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2002 03 31 화
 * 업뎃날짜 :
 * 업뎃내용 :
 *
 *
 */
public class StoreUpgradeInfoPerLevel {

    public int upgradeType = StoreUpgradeType.NONE;

    public String upgradeName = "업그레이드 타입명";

    public int upgradeLevel = 0;

    public int upgradeCost = 0;

    public float effectValue = 0f;

    public StoreUpgradeInfoPerLevel(int upgradeType, String upgradeName, int upgradeLevel, int upgradeCost, float effectValue) {
        this.upgradeType = upgradeType;
        this.upgradeName = upgradeName;
        this.upgradeLevel = upgradeLevel;
        this.upgradeCost = upgradeCost;
        this.effectValue = effectValue;

        System.out.println("업그레이드 금액 : " + upgradeCost);
    }
}
