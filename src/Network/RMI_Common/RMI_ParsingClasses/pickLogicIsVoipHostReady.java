package Network.RMI_Common.RMI_ParsingClasses;

public class pickLogicIsVoipHostReady {

    public String googleIDToken;
    public boolean isVoipHostReady;

    public pickLogicIsVoipHostReady() { }

    public pickLogicIsVoipHostReady(flat_pickLogicIsVoipHostReady data) {
        this.googleIDToken = data.googleIDToken();
        this.isVoipHostReady = data.isVoipHostReady();
    }

    public static pickLogicIsVoipHostReady createpickLogicIsVoipHostReady(byte[] data)
    {
        return flat_pickLogicIsVoipHostReady.getRootAsflat_pickLogicIsVoipHostReady( data );
    }

    public static byte[] getBytes(pickLogicIsVoipHostReady data)
    {
        return flat_pickLogicIsVoipHostReady.createflat_pickLogicIsVoipHostReady( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}