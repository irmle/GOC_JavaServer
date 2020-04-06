package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class initializeMyselfCharacterInfo {

    public int ownUserEntityID;

    public initializeMyselfCharacterInfo() { }

    public initializeMyselfCharacterInfo(flat_initializeMyselfCharacterInfo data) {
        this.ownUserEntityID = data.ownUserEntityID();
    }

    public static initializeMyselfCharacterInfo createinitializeMyselfCharacterInfo(byte[] data)
    {
        return flat_initializeMyselfCharacterInfo.getRootAsflat_initializeMyselfCharacterInfo( data );
    }

    public static byte[] getBytes(initializeMyselfCharacterInfo data)
    {
        return flat_initializeMyselfCharacterInfo.createflat_initializeMyselfCharacterInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}