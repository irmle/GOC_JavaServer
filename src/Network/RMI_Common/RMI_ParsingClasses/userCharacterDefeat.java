package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class userCharacterDefeat {

    public int userEntityID;
    public int remainTimeMilliSeconds;

    public userCharacterDefeat() { }

    public userCharacterDefeat(flat_userCharacterDefeat data) {
        this.userEntityID = data.userEntityID();
        this.remainTimeMilliSeconds = data.remainTimeMilliSeconds();
    }

    public static userCharacterDefeat createuserCharacterDefeat(byte[] data)
    {
        return flat_userCharacterDefeat.getRootAsflat_userCharacterDefeat( data );
    }

    public static byte[] getBytes(userCharacterDefeat data)
    {
        return flat_userCharacterDefeat.createflat_userCharacterDefeat( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}