package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class requestReconnectWorldMap {

    public int worldMapID;
    public String googleIDToken;

    public requestReconnectWorldMap() { }

    public requestReconnectWorldMap(flat_requestReconnectWorldMap data) {
        this.worldMapID = data.worldMapID();
        this.googleIDToken = data.googleIDToken();
    }

    public static requestReconnectWorldMap createrequestReconnectWorldMap(byte[] data)
    {
        return flat_requestReconnectWorldMap.getRootAsflat_requestReconnectWorldMap( data );
    }

    public static byte[] getBytes(requestReconnectWorldMap data)
    {
        return flat_requestReconnectWorldMap.createflat_requestReconnectWorldMap( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}