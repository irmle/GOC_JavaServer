package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class broadcastNoticeMessage {

    public MessageData message;

    public broadcastNoticeMessage() { }

    public broadcastNoticeMessage(flat_broadcastNoticeMessage data) {
        this.message = new MessageData(data.message());
    }

    public static broadcastNoticeMessage createbroadcastNoticeMessage(byte[] data)
    {
        return flat_broadcastNoticeMessage.getRootAsflat_broadcastNoticeMessage( data );
    }

    public static byte[] getBytes(broadcastNoticeMessage data)
    {
        return flat_broadcastNoticeMessage.createflat_broadcastNoticeMessage( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}