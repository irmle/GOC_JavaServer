package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

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