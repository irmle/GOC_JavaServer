package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pickLogicUserOnReady {

    public String googleIDToken;

    public pickLogicUserOnReady() { }

    public pickLogicUserOnReady(flat_pickLogicUserOnReady data) {
        this.googleIDToken = data.googleIDToken();
    }

    public static pickLogicUserOnReady createpickLogicUserOnReady(byte[] data)
    {
        return flat_pickLogicUserOnReady.getRootAsflat_pickLogicUserOnReady( data );
    }

    public static byte[] getBytes(pickLogicUserOnReady data)
    {
        return flat_pickLogicUserOnReady.createflat_pickLogicUserOnReady( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}