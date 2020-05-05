package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class userFailedBuyItem {

    public int errorCode;

    public userFailedBuyItem() { }

    public userFailedBuyItem(flat_userFailedBuyItem data) {
        this.errorCode = data.errorCode();
    }

    public static userFailedBuyItem createuserFailedBuyItem(byte[] data)
    {
        return flat_userFailedBuyItem.getRootAsflat_userFailedBuyItem( data );
    }

    public static byte[] getBytes(userFailedBuyItem data)
    {
        return flat_userFailedBuyItem.createflat_userFailedBuyItem( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}