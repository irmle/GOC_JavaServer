package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class moveCharacter {

    public int worldMapID;
    public CharacterMoveData characterMoveData;

    public moveCharacter() { }

    public moveCharacter(flat_moveCharacter data) {
        this.worldMapID = data.worldMapID();
        this.characterMoveData = new CharacterMoveData(data.characterMoveData());
    }

    public static moveCharacter createmoveCharacter(byte[] data)
    {
        return flat_moveCharacter.getRootAsflat_moveCharacter( data );
    }

    public static byte[] getBytes(moveCharacter data)
    {
        return flat_moveCharacter.createflat_moveCharacter( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}