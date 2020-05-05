package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class motionMonsterUseSkill {

    public int monsterEntityID;
    public SkillInfoData usedSkill;

    public motionMonsterUseSkill() { }

    public motionMonsterUseSkill(flat_motionMonsterUseSkill data) {
        this.monsterEntityID = data.monsterEntityID();
        this.usedSkill = new SkillInfoData(data.usedSkill());
    }

    public static motionMonsterUseSkill createmotionMonsterUseSkill(byte[] data)
    {
        return flat_motionMonsterUseSkill.getRootAsflat_motionMonsterUseSkill( data );
    }

    public static byte[] getBytes(motionMonsterUseSkill data)
    {
        return flat_motionMonsterUseSkill.createflat_motionMonsterUseSkill( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}