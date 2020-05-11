package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class request_ChangeLobbyChannel {

    public int channelNum;

    public request_ChangeLobbyChannel() { }

    public request_ChangeLobbyChannel(flat_request_ChangeLobbyChannel data) {
        this.channelNum = data.channelNum();
    }

    public static request_ChangeLobbyChannel createrequest_ChangeLobbyChannel(byte[] data)
    {
        return flat_request_ChangeLobbyChannel.getRootAsflat_request_ChangeLobbyChannel( data );
    }

    public static byte[] getBytes(request_ChangeLobbyChannel data)
    {
        return flat_request_ChangeLobbyChannel.createflat_request_ChangeLobbyChannel( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}