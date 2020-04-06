package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class heartBeatCheck_Response {

    public float timeData;

    public heartBeatCheck_Response() { }

    public heartBeatCheck_Response(flat_heartBeatCheck_Response data) {
        this.timeData = data.timeData();
    }

    public static heartBeatCheck_Response createheartBeatCheck_Response(byte[] data)
    {
        return flat_heartBeatCheck_Response.getRootAsflat_heartBeatCheck_Response( data );
    }

    public static byte[] getBytes(heartBeatCheck_Response data)
    {
        return flat_heartBeatCheck_Response.createflat_heartBeatCheck_Response( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}