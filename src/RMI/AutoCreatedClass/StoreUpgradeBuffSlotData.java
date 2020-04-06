package RMI.AutoCreatedClass;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
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