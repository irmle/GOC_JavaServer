package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class userSucceedStoreUpgradeBuff {

    public StoreUpgradeBuffSlotData storeUpgradeBuffSlotData;

    public userSucceedStoreUpgradeBuff() { }

    public userSucceedStoreUpgradeBuff(flat_userSucceedStoreUpgradeBuff data) {
        this.storeUpgradeBuffSlotData = new StoreUpgradeBuffSlotData(data.storeUpgradeBuffSlotData());
    }

    public static userSucceedStoreUpgradeBuff createuserSucceedStoreUpgradeBuff(byte[] data)
    {
        return flat_userSucceedStoreUpgradeBuff.getRootAsflat_userSucceedStoreUpgradeBuff( data );
    }

    public static byte[] getBytes(userSucceedStoreUpgradeBuff data)
    {
        return flat_userSucceedStoreUpgradeBuff.createflat_userSucceedStoreUpgradeBuff( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}