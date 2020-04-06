package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class upgradeBarricade {

    public int worldMapID;
    public int userEntityID;
    public int barricadeEntityID;
    public int barricadeType;

    public upgradeBarricade() { }

    public upgradeBarricade(flat_upgradeBarricade data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.barricadeEntityID = data.barricadeEntityID();
        this.barricadeType = data.barricadeType();
    }

    public static upgradeBarricade createupgradeBarricade(byte[] data)
    {
        return flat_upgradeBarricade.getRootAsflat_upgradeBarricade( data );
    }

    public static byte[] getBytes(upgradeBarricade data)
    {
        return flat_upgradeBarricade.createflat_upgradeBarricade( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}