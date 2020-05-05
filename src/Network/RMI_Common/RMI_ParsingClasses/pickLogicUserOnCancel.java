package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class pickLogicUserOnCancel {

    public String googleIDToken;

    public pickLogicUserOnCancel() { }

    public pickLogicUserOnCancel(flat_pickLogicUserOnCancel data) {
        this.googleIDToken = data.googleIDToken();
    }

    public static pickLogicUserOnCancel createpickLogicUserOnCancel(byte[] data)
    {
        return flat_pickLogicUserOnCancel.getRootAsflat_pickLogicUserOnCancel( data );
    }

    public static byte[] getBytes(pickLogicUserOnCancel data)
    {
        return flat_pickLogicUserOnCancel.createflat_pickLogicUserOnCancel( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}