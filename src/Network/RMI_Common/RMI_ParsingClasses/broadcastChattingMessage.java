package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class broadcastChattingMessage {

    public MessageData message;

    public broadcastChattingMessage() { }

    public broadcastChattingMessage(flat_broadcastChattingMessage data) {
        this.message = new MessageData(data.message());
    }

    public static broadcastChattingMessage createbroadcastChattingMessage(byte[] data)
    {
        return flat_broadcastChattingMessage.getRootAsflat_broadcastChattingMessage( data );
    }

    public static byte[] getBytes(broadcastChattingMessage data)
    {
        return flat_broadcastChattingMessage.createflat_broadcastChattingMessage( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}