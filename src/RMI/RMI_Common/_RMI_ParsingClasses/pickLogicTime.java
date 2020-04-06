package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pickLogicTime {

    public float remainCharacterPickTime;

    public pickLogicTime() { }

    public pickLogicTime(flat_pickLogicTime data) {
        this.remainCharacterPickTime = data.remainCharacterPickTime();
    }

    public static pickLogicTime createpickLogicTime(byte[] data)
    {
        return flat_pickLogicTime.getRootAsflat_pickLogicTime( data );
    }

    public static byte[] getBytes(pickLogicTime data)
    {
        return flat_pickLogicTime.createflat_pickLogicTime( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}