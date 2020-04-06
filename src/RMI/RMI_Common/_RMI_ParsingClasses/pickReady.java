package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pickReady {

    public String googleIDToken;

    public pickReady() { }

    public pickReady(flat_pickReady data) {
        this.googleIDToken = data.googleIDToken();
    }

    public static pickReady createpickReady(byte[] data)
    {
        return flat_pickReady.getRootAsflat_pickReady( data );
    }

    public static byte[] getBytes(pickReady data)
    {
        return flat_pickReady.createflat_pickReady( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}