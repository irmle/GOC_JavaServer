package Network.RMI_Common.RMI_ParsingClasses;

public class requestMatching {

    public String googleIDToken;
    public int selectedMapType;

    public requestMatching() { }

    public requestMatching(flat_requestMatching data) {
        this.googleIDToken = data.googleIDToken();
        this.selectedMapType = data.selectedMapType();
    }

    public static requestMatching createrequestMatching(byte[] data)
    {
        return flat_requestMatching.getRootAsflat_requestMatching( data );
    }

    public static byte[] getBytes(requestMatching data)
    {
        return flat_requestMatching.createflat_requestMatching( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}