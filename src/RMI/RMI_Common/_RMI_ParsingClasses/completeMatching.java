package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class completeMatching {

    public int worldMapID;
    public String ipAddress;
    public int port;

    public completeMatching() { }

    public completeMatching(flat_completeMatching data) {
        this.worldMapID = data.worldMapID();
        this.ipAddress = data.ipAddress();
        this.port = data.port();
    }

    public static completeMatching createcompleteMatching(byte[] data)
    {
        return flat_completeMatching.getRootAsflat_completeMatching( data );
    }

    public static byte[] getBytes(completeMatching data)
    {
        return flat_completeMatching.createflat_completeMatching( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}