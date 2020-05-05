package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class userFailedInstallBuilding {

    public int errorCode;

    public userFailedInstallBuilding() { }

    public userFailedInstallBuilding(flat_userFailedInstallBuilding data) {
        this.errorCode = data.errorCode();
    }

    public static userFailedInstallBuilding createuserFailedInstallBuilding(byte[] data)
    {
        return flat_userFailedInstallBuilding.getRootAsflat_userFailedInstallBuilding( data );
    }

    public static byte[] getBytes(userFailedInstallBuilding data)
    {
        return flat_userFailedInstallBuilding.createflat_userFailedInstallBuilding( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}