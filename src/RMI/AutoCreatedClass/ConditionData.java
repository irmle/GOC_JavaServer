package RMI.AutoCreatedClass;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class ConditionData {

    public int itemType;
    public int skillType;
    public float buffDurationTime;
    public float buffRemainTime;

    public ConditionData() { }

    public ConditionData(flat_ConditionData data) {
        this.itemType = data.itemType();
        this.skillType = data.skillType();
        this.buffDurationTime = data.buffDurationTime();
        this.buffRemainTime = data.buffRemainTime();
    }

    public static ConditionData createConditionData(byte[] data)
    {
        return flat_ConditionData.getRootAsflat_ConditionData( data );
    }

    public static byte[] getBytes(ConditionData data)
    {
        return flat_ConditionData.createflat_ConditionData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}