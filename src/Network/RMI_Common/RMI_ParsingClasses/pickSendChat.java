package Network.RMI_Common.RMI_ParsingClasses;

public class pickSendChat {

    public String googleIDToken;
    public String chatMessage;

    public pickSendChat() { }

    public pickSendChat(flat_pickSendChat data) {
        this.googleIDToken = data.googleIDToken();
        this.chatMessage = data.chatMessage();
    }

    public static pickSendChat createpickSendChat(byte[] data)
    {
        return flat_pickSendChat.getRootAsflat_pickSendChat( data );
    }

    public static byte[] getBytes(pickSendChat data)
    {
        return flat_pickSendChat.createflat_pickSendChat( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}