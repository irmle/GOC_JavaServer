package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class installBarricade {

    public int worldMapID;
    public int userEntityID;
    public int barricadeType;
    public int areaNumber;

    public installBarricade() { }

    public installBarricade(flat_installBarricade data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.barricadeType = data.barricadeType();
        this.areaNumber = data.areaNumber();
    }

    public static installBarricade createinstallBarricade(byte[] data)
    {
        return flat_installBarricade.getRootAsflat_installBarricade( data );
    }

    public static byte[] getBytes(installBarricade data)
    {
        return flat_installBarricade.createflat_installBarricade( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}