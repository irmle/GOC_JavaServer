package Network.RMI_Common.RMI_ParsingClasses;

public class userReconnected {

    public int userEntityID;

    public userReconnected() { }

    public userReconnected(flat_userReconnected data) {
        this.userEntityID = data.userEntityID();
    }

    public static userReconnected createuserReconnected(byte[] data)
    {
        return flat_userReconnected.getRootAsflat_userReconnected( data );
    }

    public static byte[] getBytes(userReconnected data)
    {
        return flat_userReconnected.createflat_userReconnected( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}