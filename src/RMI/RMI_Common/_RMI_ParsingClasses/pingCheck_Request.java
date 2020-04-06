package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pingCheck_Request {

    public float timeData;

    public pingCheck_Request() { }

    public pingCheck_Request(flat_pingCheck_Request data) {
        this.timeData = data.timeData();
    }

    public static pingCheck_Request createpingCheck_Request(byte[] data)
    {
        return flat_pingCheck_Request.getRootAsflat_pingCheck_Request( data );
    }

    public static byte[] getBytes(pingCheck_Request data)
    {
        return flat_pingCheck_Request.createflat_pingCheck_Request( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}