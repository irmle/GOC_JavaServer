package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class userDisconnected {

    public int userEntityID;

    public userDisconnected() { }

    public userDisconnected(flat_userDisconnected data) {
        this.userEntityID = data.userEntityID();
    }

    public static userDisconnected createuserDisconnected(byte[] data)
    {
        return flat_userDisconnected.getRootAsflat_userDisconnected( data );
    }

    public static byte[] getBytes(userDisconnected data)
    {
        return flat_userDisconnected.createflat_userDisconnected( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}