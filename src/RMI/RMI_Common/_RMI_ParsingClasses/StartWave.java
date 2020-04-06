package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
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