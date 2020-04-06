package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pickLogicIsVoipHostReady {

    public String googleIDToken;
    public boolean isVoipHostReady;

    public pickLogicIsVoipHostReady() { }

    public pickLogicIsVoipHostReady(flat_pickLogicIsVoipHostReady data) {
        this.googleIDToken = data.googleIDToken();
        this.isVoipHostReady = data.isVoipHostReady();
    }

    public static pickLogicIsVoipHostReady createpickLogicIsVoipHostReady(byte[] data)
    {
        return flat_pickLogicIsVoipHostReady.getRootAsflat_pickLogicIsVoipHostReady( data );
    }

    public static byte[] getBytes(pickLogicIsVoipHostReady data)
    {
        return flat_pickLogicIsVoipHostReady.createflat_pickLogicIsVoipHostReady( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}