package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class useSkill {

    public int worldMapID;
    public int userEntityID;
    public float directionX;
    public float directionY;
    public float directionZ;
    public float distanceRate;
    public short skillSlotNum;
    public int targetEntityID;

    public useSkill() { }

    public useSkill(flat_useSkill data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.directionX = data.directionX();
        this.directionY = data.directionY();
        this.directionZ = data.directionZ();
        this.distanceRate = data.distanceRate();
        this.skillSlotNum = data.skillSlotNum();
        this.targetEntityID = data.targetEntityID();
    }

    public static useSkill createuseSkill(byte[] data)
    {
        return flat_useSkill.getRootAsflat_useSkill( data );
    }

    public static byte[] getBytes(useSkill data)
    {
        return flat_useSkill.createflat_useSkill( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}