package RMI.AutoCreatedClass;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class SkillSlotData {

    public int slotNum;
    public SkillInfoData skillInfo;
    public int skillLevel;
    public float remainCoolTime;

    public SkillSlotData() { }

    public SkillSlotData(flat_SkillSlotData data) {
        this.slotNum = data.slotNum();
        this.skillInfo = new SkillInfoData(data.skillInfo());
        this.skillLevel = data.skillLevel();
        this.remainCoolTime = data.remainCoolTime();
    }

    public static SkillSlotData createSkillSlotData(byte[] data)
    {
        return flat_SkillSlotData.getRootAsflat_SkillSlotData( data );
    }

    public static byte[] getBytes(SkillSlotData data)
    {
        return flat_SkillSlotData.createflat_SkillSlotData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}