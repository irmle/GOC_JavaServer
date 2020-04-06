package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class upgradeSkill {

    public int worldMapID;
    public int userEntityID;
    public short skillSlotNum;

    public upgradeSkill() { }

    public upgradeSkill(flat_upgradeSkill data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.skillSlotNum = data.skillSlotNum();
    }

    public static upgradeSkill createupgradeSkill(byte[] data)
    {
        return flat_upgradeSkill.getRootAsflat_upgradeSkill( data );
    }

    public static byte[] getBytes(upgradeSkill data)
    {
        return flat_upgradeSkill.createflat_upgradeSkill( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}