package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class request_BroadcastPlayerEvent {

    public int messageType;
    public String broadcastingDataJS;

    public request_BroadcastPlayerEvent() { }

    public request_BroadcastPlayerEvent(flat_request_BroadcastPlayerEvent data) {
        this.messageType = data.messageType();
        this.broadcastingDataJS = data.broadcastingDataJS();
    }

    public static request_BroadcastPlayerEvent createrequest_BroadcastPlayerEvent(byte[] data)
    {
        return flat_request_BroadcastPlayerEvent.getRootAsflat_request_BroadcastPlayerEvent( data );
    }

    public static byte[] getBytes(request_BroadcastPlayerEvent data)
    {
        return flat_request_BroadcastPlayerEvent.createflat_request_BroadcastPlayerEvent( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}