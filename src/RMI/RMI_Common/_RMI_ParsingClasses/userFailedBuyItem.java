package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
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