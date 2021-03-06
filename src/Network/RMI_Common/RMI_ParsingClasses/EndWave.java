package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class EndWave {

    public int waveCount;

    public EndWave() { }

    public EndWave(flat_EndWave data) {
        this.waveCount = data.waveCount();
    }

    public static EndWave createEndWave(byte[] data)
    {
        return flat_EndWave.getRootAsflat_EndWave( data );
    }

    public static byte[] getBytes(EndWave data)
    {
        return flat_EndWave.createflat_EndWave( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}