package Network.RMI_Common.RMI_ParsingClasses;

public class requestLogin {

    public String googleIDToken;

    public requestLogin() { }

    public requestLogin(flat_requestLogin data) {
        this.googleIDToken = data.googleIDToken();
    }

    public static requestLogin createrequestLogin(byte[] data)
    {
        return flat_requestLogin.getRootAsflat_requestLogin( data );
    }

    public static byte[] getBytes(requestLogin data)
    {
        return flat_requestLogin.createflat_requestLogin( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}