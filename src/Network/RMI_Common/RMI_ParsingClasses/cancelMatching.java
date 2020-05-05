package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class cancelMatching {

    public String googleIDToken;

    public cancelMatching() { }

    public cancelMatching(flat_cancelMatching data) {
        this.googleIDToken = data.googleIDToken();
    }

    public static cancelMatching createcancelMatching(byte[] data)
    {
        return flat_cancelMatching.getRootAsflat_cancelMatching( data );
    }

    public static byte[] getBytes(cancelMatching data)
    {
        return flat_cancelMatching.createflat_cancelMatching( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}