package Network.RMI_Common.RMI_ParsingClasses;

public class pickCancel {

    public String googleIDToken;

    public pickCancel() { }

    public pickCancel(flat_pickCancel data) {
        this.googleIDToken = data.googleIDToken();
    }

    public static pickCancel createpickCancel(byte[] data)
    {
        return flat_pickCancel.getRootAsflat_pickCancel( data );
    }

    public static byte[] getBytes(pickCancel data)
    {
        return flat_pickCancel.createflat_pickCancel( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}