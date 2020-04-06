package RMI.AutoCreatedClass;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

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