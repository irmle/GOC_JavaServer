package RMI.AutoCreatedClass;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class BuildSlotData {

    public int buildType;
    public int slotNum;
    public int slotState;
    public float centerX;
    public float centerY;
    public float centerZ;
    public float remainBuildTime;

    public BuildSlotData() { }

    public BuildSlotData(flat_BuildSlotData data) {
        this.buildType = data.buildType();
        this.slotNum = data.slotNum();
        this.slotState = data.slotState();
        this.centerX = data.centerX();
        this.centerY = data.centerY();
        this.centerZ = data.centerZ();
        this.remainBuildTime = data.remainBuildTime();
    }

    public static BuildSlotData createBuildSlotData(byte[] data)
    {
        return flat_BuildSlotData.getRootAsflat_BuildSlotData( data );
    }

    public static byte[] getBytes(BuildSlotData data)
    {
        return flat_BuildSlotData.createflat_BuildSlotData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}