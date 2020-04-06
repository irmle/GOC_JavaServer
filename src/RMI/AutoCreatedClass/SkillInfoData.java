package RMI.AutoCreatedClass;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class SkillInfoData {

    public int skillType;

    public SkillInfoData() { }

    public SkillInfoData(flat_SkillInfoData data) {
        this.skillType = data.skillType();
    }

    public static SkillInfoData createSkillInfoData(byte[] data)
    {
        return flat_SkillInfoData.getRootAsflat_SkillInfoData( data );
    }

    public static byte[] getBytes(SkillInfoData data)
    {
        return flat_SkillInfoData.createflat_SkillInfoData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}