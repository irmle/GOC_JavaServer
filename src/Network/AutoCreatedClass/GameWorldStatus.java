package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;

public class GameWorldStatus {

    public long currentGameworldTime;

    public GameWorldStatus() { }

    public GameWorldStatus(flat_GameWorldStatus data) {
        this.currentGameworldTime = data.currentGameworldTime();
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