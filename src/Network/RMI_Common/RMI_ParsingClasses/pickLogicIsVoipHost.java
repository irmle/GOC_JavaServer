package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class pickLogicIsVoipHost {

    public boolean isVoipHost;
    public int worldMapID;

    public pickLogicIsVoipHost() { }

    public pickLogicIsVoipHost(flat_pickLogicIsVoipHost data) {
        this.isVoipHost = data.isVoipHost();
        this.worldMapID = data.worldMapID();
    }

    public static pickLogicIsVoipHost createpickLogicIsVoipHost(byte[] data)
    {
        return flat_pickLogicIsVoipHost.getRootAsflat_pickLogicIsVoipHost( data );
    }

    public static byte[] getBytes(pickLogicIsVoipHost data)
    {
        return flat_pickLogicIsVoipHost.createflat_pickLogicIsVoipHost( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}