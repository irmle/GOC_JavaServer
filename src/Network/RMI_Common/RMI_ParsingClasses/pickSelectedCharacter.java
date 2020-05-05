package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class pickSelectedCharacter {

    public String googleIDToken;
    public int characterType;
    public int guardianID;

    public pickSelectedCharacter() { }

    public pickSelectedCharacter(flat_pickSelectedCharacter data) {
        this.googleIDToken = data.googleIDToken();
        this.characterType = data.characterType();
        this.guardianID = data.guardianID();
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