package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class response_lobbyChannelAllocated {

    public int channelNum;
    public MessageData message;

    public response_lobbyChannelAllocated() { }

    public response_lobbyChannelAllocated(flat_response_lobbyChannelAllocated data) {
        this.channelNum = data.channelNum();
        this.message = new MessageData(data.message());
    }

    public static response_lobbyChannelAllocated createresponse_lobbyChannelAllocated(byte[] data)
    {
        return flat_response_lobbyChannelAllocated.getRootAsflat_response_lobbyChannelAllocated( data );
    }

    public static byte[] getBytes(response_lobbyChannelAllocated data)
    {
        return flat_response_lobbyChannelAllocated.createflat_response_lobbyChannelAllocated( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}