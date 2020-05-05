package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class broadcastingGameWorldStatusSnapshot {

    public GameWorldStatus gameworldStatusSnapshot;

    public broadcastingGameWorldStatusSnapshot() { }

    public broadcastingGameWorldStatusSnapshot(flat_broadcastingGameWorldStatusSnapshot data) {
        this.gameworldStatusSnapshot = new GameWorldStatus(data.gameworldStatusSnapshot());
    }

    public static broadcastingGameWorldStatusSnapshot createbroadcastingGameWorldStatusSnapshot(byte[] data)
    {
        return flat_broadcastingGameWorldStatusSnapshot.getRootAsflat_broadcastingGameWorldStatusSnapshot( data );
    }

    public static byte[] getBytes(broadcastingGameWorldStatusSnapshot data)
    {
        return flat_broadcastingGameWorldStatusSnapshot.createflat_broadcastingGameWorldStatusSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}