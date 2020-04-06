package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class motionCharacterCancelSkill {

    public int characterEntityID;
    public SkillInfoData usedSkill;

    public motionCharacterCancelSkill() { }

    public motionCharacterCancelSkill(flat_motionCharacterCancelSkill data) {
        this.characterEntityID = data.characterEntityID();
        this.usedSkill = new SkillInfoData(data.usedSkill());
    }

    public static motionCharacterCancelSkill createmotionCharacterCancelSkill(byte[] data)
    {
        return flat_motionCharacterCancelSkill.getRootAsflat_motionCharacterCancelSkill( data );
    }

    public static byte[] getBytes(motionCharacterCancelSkill data)
    {
        return flat_motionCharacterCancelSkill.createflat_motionCharacterCancelSkill( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}