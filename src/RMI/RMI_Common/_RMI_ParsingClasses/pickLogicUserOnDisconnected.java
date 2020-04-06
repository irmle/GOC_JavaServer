package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pickLogicUserOnDisconnected {

    public String googleIDToken;

    public pickLogicUserOnDisconnected() { }

    public pickLogicUserOnDisconnected(flat_pickLogicUserOnDisconnected data) {
        this.googleIDToken = data.googleIDToken();
    }

    public static pickLogicUserOnDisconnected createpickLogicUserOnDisconnected(byte[] data)
    {
        return flat_pickLogicUserOnDisconnected.getRootAsflat_pickLogicUserOnDisconnected( data );
    }

    public static byte[] getBytes(pickLogicUserOnDisconnected data)
    {
        return flat_pickLogicUserOnDisconnected.createflat_pickLogicUserOnDisconnected( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}