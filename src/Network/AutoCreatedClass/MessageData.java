package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class MessageData {

    public String nickName;
    public int	 channelNum;
    public int	 messageType;
    public String messageContent;
    public String sendTime;

    public MessageData() { }

    public MessageData(flat_MessageData data) {
        this.nickName = data.nickName();
        this.channelNum = data.channelNum();
        this.messageType = data.messageType();
        this.messageContent = data.messageContent();
        this.sendTime = data.sendTime();
    }

    public static MessageData createMessageData(byte[] data)
    {
        return flat_MessageData.getRootAsflat_MessageData( data );
    }

    public static byte[] getBytes(MessageData data)
    {
        return flat_MessageData.createflat_MessageData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}