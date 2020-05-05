package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class StoreUpgradeBuffSlotData {

    public int slotNum;
    public int upgradeType;
    public int upgradeLevel;

    public StoreUpgradeBuffSlotData() { }

    public StoreUpgradeBuffSlotData(flat_StoreUpgradeBuffSlotData data) {
        this.slotNum = data.slotNum();
        this.upgradeType = data.upgradeType();
        this.upgradeLevel = data.upgradeLevel();
    }

    public static StoreUpgradeBuffSlotData createStoreUpgradeBuffSlotData(byte[] data)
    {
        return flat_StoreUpgradeBuffSlotData.getRootAsflat_StoreUpgradeBuffSlotData( data );
    }

    public static byte[] getBytes(StoreUpgradeBuffSlotData data)
    {
        return flat_StoreUpgradeBuffSlotData.createflat_StoreUpgradeBuffSlotData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}