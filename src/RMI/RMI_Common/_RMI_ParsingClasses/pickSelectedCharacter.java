package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pickSelectedCharacter {

    public String googleIDToken;
    public int characterType;

    public pickSelectedCharacter() { }

    public pickSelectedCharacter(flat_pickSelectedCharacter data) {
        this.googleIDToken = data.googleIDToken();
        this.characterType = data.characterType();
    }

    public static pickSelectedCharacter createpickSelectedCharacter(byte[] data)
    {
        return flat_pickSelectedCharacter.getRootAsflat_pickSelectedCharacter( data );
    }

    public static byte[] getBytes(pickSelectedCharacter data)
    {
        return flat_pickSelectedCharacter.createflat_pickSelectedCharacter( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}