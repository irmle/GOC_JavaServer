package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class cancelMatching {

    public String googleIDToken;

    public cancelMatching() { }

    public cancelMatching(flat_cancelMatching data) {
        this.googleIDToken = data.googleIDToken();
    }

    public static cancelMatching createcancelMatching(byte[] data)
    {
        return flat_cancelMatching.getRootAsflat_cancelMatching( data );
    }

    public static byte[] getBytes(cancelMatching data)
    {
        return flat_cancelMatching.createflat_cancelMatching( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}