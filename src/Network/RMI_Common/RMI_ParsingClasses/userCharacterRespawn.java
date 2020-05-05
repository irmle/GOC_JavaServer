package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class userCharacterRespawn {

    public int userEntityID;

    public userCharacterRespawn() { }

    public userCharacterRespawn(flat_userCharacterRespawn data) {
        this.userEntityID = data.userEntityID();
    }

    public static userCharacterRespawn createuserCharacterRespawn(byte[] data)
    {
        return flat_userCharacterRespawn.getRootAsflat_userCharacterRespawn( data );
    }

    public static byte[] getBytes(userCharacterRespawn data)
    {
        return flat_userCharacterRespawn.createflat_userCharacterRespawn( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}