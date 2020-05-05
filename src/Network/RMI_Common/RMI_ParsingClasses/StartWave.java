package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class StartWave {

    public int waveCount;

    public StartWave() { }

    public StartWave(flat_StartWave data) {
        this.waveCount = data.waveCount();
    }

    public static StartWave createStartWave(byte[] data)
    {
        return flat_StartWave.getRootAsflat_StartWave( data );
    }

    public static byte[] getBytes(StartWave data)
    {
        return flat_StartWave.createflat_StartWave( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}