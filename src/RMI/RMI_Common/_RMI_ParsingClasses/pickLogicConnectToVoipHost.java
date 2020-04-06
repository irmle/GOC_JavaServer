package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pickLogicConnectToVoipHost {

    public boolean isVoipOK;
    public int worldMapID;

    public pickLogicConnectToVoipHost() { }

    public pickLogicConnectToVoipHost(flat_pickLogicConnectToVoipHost data) {
        this.isVoipOK = data.isVoipOK();
        this.worldMapID = data.worldMapID();
    }

    public static pickLogicConnectToVoipHost createpickLogicConnectToVoipHost(byte[] data)
    {
        return flat_pickLogicConnectToVoipHost.getRootAsflat_pickLogicConnectToVoipHost( data );
    }

    public static byte[] getBytes(pickLogicConnectToVoipHost data)
    {
        return flat_pickLogicConnectToVoipHost.createflat_pickLogicConnectToVoipHost( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}