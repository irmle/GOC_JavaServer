package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class requestStoreUpgradeBuff {

    public int worldMapID;
    public int userEntityID;
    public int storeUpgradeType;
    public int buffLevel;

    public requestStoreUpgradeBuff() { }

    public requestStoreUpgradeBuff(flat_requestStoreUpgradeBuff data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.storeUpgradeType = data.storeUpgradeType();
        this.buffLevel = data.buffLevel();
    }

    public static requestStoreUpgradeBuff createrequestStoreUpgradeBuff(byte[] data)
    {
        return flat_requestStoreUpgradeBuff.getRootAsflat_requestStoreUpgradeBuff( data );
    }

    public static byte[] getBytes(requestStoreUpgradeBuff data)
    {
        return flat_requestStoreUpgradeBuff.createflat_requestStoreUpgradeBuff( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}