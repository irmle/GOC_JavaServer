package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class GameWorldStatus {

    public long currentGameworldTime;
    public int entireWaveMobCount;
    public int currentAliveMobCount;

    public GameWorldStatus() { }

    public GameWorldStatus(flat_GameWorldStatus data) {
        this.currentGameworldTime = data.currentGameworldTime();
        this.entireWaveMobCount = data.entireWaveMobCount();
        this.currentAliveMobCount = data.currentAliveMobCount();
    }

    public static GameWorldStatus createGameWorldStatus(byte[] data)
    {
        return flat_GameWorldStatus.getRootAsflat_GameWorldStatus( data );
    }

    public static byte[] getBytes(GameWorldStatus data)
    {
        return flat_GameWorldStatus.createflat_GameWorldStatus( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}