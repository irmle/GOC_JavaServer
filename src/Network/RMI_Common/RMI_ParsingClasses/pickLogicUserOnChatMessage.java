package Network.RMI_Common.RMI_ParsingClasses;

public class pickLogicUserOnChatMessage {

    public String googleIDToken;
    public String chatMessage;

    public pickLogicUserOnChatMessage() { }

    public pickLogicUserOnChatMessage(flat_pickLogicUserOnChatMessage data) {
        this.googleIDToken = data.googleIDToken();
        this.chatMessage = data.chatMessage();
    }

    public static pickLogicUserOnChatMessage createpickLogicUserOnChatMessage(byte[] data)
    {
        return flat_pickLogicUserOnChatMessage.getRootAsflat_pickLogicUserOnChatMessage( data );
    }

    public static byte[] getBytes(pickLogicUserOnChatMessage data)
    {
        return flat_pickLogicUserOnChatMessage.createflat_pickLogicUserOnChatMessage( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}