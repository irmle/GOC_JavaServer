package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class response_sessionChannelAllocated {

    public int channelNum;
    public MessageData message;

    public response_sessionChannelAllocated() { }

    public response_sessionChannelAllocated(flat_response_sessionChannelAllocated data) {
        this.channelNum = data.channelNum();
        this.message = new MessageData(data.message());
    }

    public static response_sessionChannelAllocated createresponse_sessionChannelAllocated(byte[] data)
    {
        return flat_response_sessionChannelAllocated.getRootAsflat_response_sessionChannelAllocated( data );
    }

    public static byte[] getBytes(response_sessionChannelAllocated data)
    {
        return flat_response_sessionChannelAllocated.createflat_response_sessionChannelAllocated( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}