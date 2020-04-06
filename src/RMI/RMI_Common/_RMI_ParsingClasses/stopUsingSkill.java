package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class stopUsingSkill {

    public int worldMapID;
    public int userEntityID;
    public short skillSlotNum;

    public stopUsingSkill() { }

    public stopUsingSkill(flat_stopUsingSkill data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.skillSlotNum = data.skillSlotNum();
    }

    public static stopUsingSkill createstopUsingSkill(byte[] data)
    {
        return flat_stopUsingSkill.getRootAsflat_stopUsingSkill( data );
    }

    public static byte[] getBytes(stopUsingSkill data)
    {
        return flat_stopUsingSkill.createflat_stopUsingSkill( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}