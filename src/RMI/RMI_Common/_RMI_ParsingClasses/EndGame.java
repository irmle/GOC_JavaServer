package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class EndGame {

    public int resultCode;
    public String resultJS;

    public EndGame() { }

    public EndGame(flat_EndGame data) {
        this.resultCode = data.resultCode();
        this.resultJS = data.resultJS();
    }

    public static EndGame createEndGame(byte[] data)
    {
        return flat_EndGame.getRootAsflat_EndGame( data );
    }

    public static byte[] getBytes(EndGame data)
    {
        return flat_EndGame.createflat_EndGame( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}