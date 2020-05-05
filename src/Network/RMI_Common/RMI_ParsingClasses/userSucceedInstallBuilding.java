package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class userSucceedInstallBuilding {

    public int builderEntityID;
    public int buildingType;

    public userSucceedInstallBuilding() { }

    public userSucceedInstallBuilding(flat_userSucceedInstallBuilding data) {
        this.builderEntityID = data.builderEntityID();
        this.buildingType = data.buildingType();
    }

    public static userSucceedInstallBuilding createuserSucceedInstallBuilding(byte[] data)
    {
        return flat_userSucceedInstallBuilding.getRootAsflat_userSucceedInstallBuilding( data );
    }

    public static byte[] getBytes(userSucceedInstallBuilding data)
    {
        return flat_userSucceedInstallBuilding.createflat_userSucceedInstallBuilding( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}