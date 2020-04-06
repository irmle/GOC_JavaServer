package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
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