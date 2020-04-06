package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class doAttack {

    public int worldMapID;
    public int attackerEntityID;
    public int targetEntityID;

    public doAttack() { }

    public doAttack(flat_doAttack data) {
        this.worldMapID = data.worldMapID();
        this.attackerEntityID = data.attackerEntityID();
        this.targetEntityID = data.targetEntityID();
    }

    public static doAttack createdoAttack(byte[] data)
    {
        return flat_doAttack.getRootAsflat_doAttack( data );
    }

    public static byte[] getBytes(doAttack data)
    {
        return flat_doAttack.createflat_doAttack( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}