package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class userFailedUseItem {

    public int errorCode;

    public userFailedUseItem() { }

    public userFailedUseItem(flat_userFailedUseItem data) {
        this.errorCode = data.errorCode();
    }

    public static userFailedUseItem createuserFailedUseItem(byte[] data)
    {
        return flat_userFailedUseItem.getRootAsflat_userFailedUseItem( data );
    }

    public static byte[] getBytes(userFailedUseItem data)
    {
        return flat_userFailedUseItem.createflat_userFailedUseItem( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}