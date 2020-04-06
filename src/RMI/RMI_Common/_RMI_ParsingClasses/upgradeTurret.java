package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class upgradeTurret {

    public int worldMapID;
    public int userEntityID;
    public int turretEntityID;
    public int turretType;

    public upgradeTurret() { }

    public upgradeTurret(flat_upgradeTurret data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.turretEntityID = data.turretEntityID();
        this.turretType = data.turretType();
    }

    public static upgradeTurret createupgradeTurret(byte[] data)
    {
        return flat_upgradeTurret.getRootAsflat_upgradeTurret( data );
    }

    public static byte[] getBytes(upgradeTurret data)
    {
        return flat_upgradeTurret.createflat_upgradeTurret( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}