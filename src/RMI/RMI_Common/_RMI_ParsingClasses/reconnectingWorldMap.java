package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class reconnectingWorldMap {

    public int worldMapID;
    public LinkedList <LoadingPlayerData> loadingPlayerList = new LinkedList<>();
    public String ipAddress;
    public int port;

    public reconnectingWorldMap() { }

    public reconnectingWorldMap(flat_reconnectingWorldMap data) {
        this.worldMapID = data.worldMapID();
        for(int i = 0;i < data.loadingPlayerListLength();i++) {
            this.loadingPlayerList.addLast(new LoadingPlayerData(data.loadingPlayerList(i)));
        }
        this.ipAddress = data.ipAddress();
        this.port = data.port();
    }

    public static reconnectingWorldMap createreconnectingWorldMap(byte[] data)
    {
        return flat_reconnectingWorldMap.getRootAsflat_reconnectingWorldMap( data );
    }

    public static byte[] getBytes(reconnectingWorldMap data)
    {
        return flat_reconnectingWorldMap.createflat_reconnectingWorldMap( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}