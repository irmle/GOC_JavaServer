package Network.RMI_Common.RMI_ParsingClasses;

public class userDisconnected {

    public int userEntityID;

    public userDisconnected() { }

    public userDisconnected(flat_userDisconnected data) {
        this.userEntityID = data.userEntityID();
    }

    public static userDisconnected createuserDisconnected(byte[] data)
    {
        return flat_userDisconnected.getRootAsflat_userDisconnected( data );
    }

    public static byte[] getBytes(userDisconnected data)
    {
        return flat_userDisconnected.createflat_userDisconnected( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}