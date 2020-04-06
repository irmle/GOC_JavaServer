package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class pingCheck_Response {

    public float timeData;

    public pingCheck_Response() { }

    public pingCheck_Response(flat_pingCheck_Response data) {
        this.timeData = data.timeData();
    }

    public static pingCheck_Response createpingCheck_Response(byte[] data)
    {
        return flat_pingCheck_Response.getRootAsflat_pingCheck_Response( data );
    }

    public static byte[] getBytes(pingCheck_Response data)
    {
        return flat_pingCheck_Response.createflat_pingCheck_Response( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}