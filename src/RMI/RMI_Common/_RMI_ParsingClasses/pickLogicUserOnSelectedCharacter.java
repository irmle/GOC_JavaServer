package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pickLogicUserOnSelectedCharacter {

    public String googleIDToken;
    public int characterType;

    public pickLogicUserOnSelectedCharacter() { }

    public pickLogicUserOnSelectedCharacter(flat_pickLogicUserOnSelectedCharacter data) {
        this.googleIDToken = data.googleIDToken();
        this.characterType = data.characterType();
    }

    public static pickLogicUserOnSelectedCharacter createpickLogicUserOnSelectedCharacter(byte[] data)
    {
        return flat_pickLogicUserOnSelectedCharacter.getRootAsflat_pickLogicUserOnSelectedCharacter( data );
    }

    public static byte[] getBytes(pickLogicUserOnSelectedCharacter data)
    {
        return flat_pickLogicUserOnSelectedCharacter.createflat_pickLogicUserOnSelectedCharacter( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}