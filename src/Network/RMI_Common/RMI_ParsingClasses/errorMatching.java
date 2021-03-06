package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class errorMatching {

    public int errorCode;
    public String errorReason;

    public errorMatching() { }

    public errorMatching(flat_errorMatching data) {
        this.errorCode = data.errorCode();
        this.errorReason = data.errorReason();
    }

    public static errorMatching createerrorMatching(byte[] data)
    {
        return flat_errorMatching.getRootAsflat_errorMatching( data );
    }

    public static byte[] getBytes(errorMatching data)
    {
        return flat_errorMatching.createflat_errorMatching( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}