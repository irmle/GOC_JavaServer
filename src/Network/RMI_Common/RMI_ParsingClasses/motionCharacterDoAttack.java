package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class motionCharacterDoAttack {

    public int characterEntityID;
    public int targetEntityID;

    public motionCharacterDoAttack() { }

    public motionCharacterDoAttack(flat_motionCharacterDoAttack data) {
        this.characterEntityID = data.characterEntityID();
        this.targetEntityID = data.targetEntityID();
    }

    public static motionCharacterDoAttack createmotionCharacterDoAttack(byte[] data)
    {
        return flat_motionCharacterDoAttack.getRootAsflat_motionCharacterDoAttack( data );
    }

    public static byte[] getBytes(motionCharacterDoAttack data)
    {
        return flat_motionCharacterDoAttack.createflat_motionCharacterDoAttack( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}