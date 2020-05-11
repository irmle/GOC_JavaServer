package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class request_SendChattingMessage {

    public int messageType;
    public String messageData;

    public request_SendChattingMessage() { }

    public request_SendChattingMessage(flat_request_SendChattingMessage data) {
        this.messageType = data.messageType();
        this.messageData = data.messageData();
    }

    public static request_SendChattingMessage createrequest_SendChattingMessage(byte[] data)
    {
        return flat_request_SendChattingMessage.getRootAsflat_request_SendChattingMessage( data );
    }

    public static byte[] getBytes(request_SendChattingMessage data)
    {
        return flat_request_SendChattingMessage.createflat_request_SendChattingMessage( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}