package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class motionCharacterUseSkill {

    public int characterEntityID;
    public SkillInfoData usedSkill;

    public motionCharacterUseSkill() { }

    public motionCharacterUseSkill(flat_motionCharacterUseSkill data) {
        this.characterEntityID = data.characterEntityID();
        this.usedSkill = new SkillInfoData(data.usedSkill());
    }

    public static motionCharacterUseSkill createmotionCharacterUseSkill(byte[] data)
    {
        return flat_motionCharacterUseSkill.getRootAsflat_motionCharacterUseSkill( data );
    }

    public static byte[] getBytes(motionCharacterUseSkill data)
    {
        return flat_motionCharacterUseSkill.createflat_motionCharacterUseSkill( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}