package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class response_lobbyChannelChanged {

    public int channelNum;
    public MessageData message;

    public response_lobbyChannelChanged() { }

    public response_lobbyChannelChanged(flat_response_lobbyChannelChanged data) {
        this.channelNum = data.channelNum();
        this.message = new MessageData(data.message());
    }

    public static response_lobbyChannelChanged createresponse_lobbyChannelChanged(byte[] data)
    {
        return flat_response_lobbyChannelChanged.getRootAsflat_response_lobbyChannelChanged( data );
    }

    public static byte[] getBytes(response_lobbyChannelChanged data)
    {
        return flat_response_lobbyChannelChanged.createflat_response_lobbyChannelChanged( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}