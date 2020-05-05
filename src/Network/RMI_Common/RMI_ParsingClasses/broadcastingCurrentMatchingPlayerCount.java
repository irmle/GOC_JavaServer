package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class broadcastingCurrentMatchingPlayerCount {

    public int matchingPlayerCount;

    public broadcastingCurrentMatchingPlayerCount() { }

    public broadcastingCurrentMatchingPlayerCount(flat_broadcastingCurrentMatchingPlayerCount data) {
        this.matchingPlayerCount = data.matchingPlayerCount();
    }

    public static broadcastingCurrentMatchingPlayerCount createbroadcastingCurrentMatchingPlayerCount(byte[] data)
    {
        return flat_broadcastingCurrentMatchingPlayerCount.getRootAsflat_broadcastingCurrentMatchingPlayerCount( data );
    }

    public static byte[] getBytes(broadcastingCurrentMatchingPlayerCount data)
    {
        return flat_broadcastingCurrentMatchingPlayerCount.createflat_broadcastingCurrentMatchingPlayerCount( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}