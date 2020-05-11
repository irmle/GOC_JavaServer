package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class response_sessionChannelReleased {

    public MessageData message;

    public response_sessionChannelReleased() { }

    public response_sessionChannelReleased(flat_response_sessionChannelReleased data) {
        this.message = new MessageData(data.message());
    }

    public static response_sessionChannelReleased createresponse_sessionChannelReleased(byte[] data)
    {
        return flat_response_sessionChannelReleased.getRootAsflat_response_sessionChannelReleased( data );
    }

    public static byte[] getBytes(response_sessionChannelReleased data)
    {
        return flat_response_sessionChannelReleased.createflat_response_sessionChannelReleased( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}