package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class motionMonsterDoAttack {

    public int monsterEntityID;
    public short targetEntityType;
    public int targetEntityID;

    public motionMonsterDoAttack() { }

    public motionMonsterDoAttack(flat_motionMonsterDoAttack data) {
        this.monsterEntityID = data.monsterEntityID();
        this.targetEntityType = data.targetEntityType();
        this.targetEntityID = data.targetEntityID();
    }

    public static motionMonsterDoAttack createmotionMonsterDoAttack(byte[] data)
    {
        return flat_motionMonsterDoAttack.getRootAsflat_motionMonsterDoAttack( data );
    }

    public static byte[] getBytes(motionMonsterDoAttack data)
    {
        return flat_motionMonsterDoAttack.createflat_motionMonsterDoAttack( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}