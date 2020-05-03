package Network.RMI_Common.RMI_ParsingClasses;

import Network.AutoCreatedClass.*;

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