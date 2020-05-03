package Network.RMI_Common.RMI_ParsingClasses;

public class initializeMyselfCharacterInfo {

    public int ownUserEntityID;

    public initializeMyselfCharacterInfo() { }

    public initializeMyselfCharacterInfo(flat_initializeMyselfCharacterInfo data) {
        this.ownUserEntityID = data.ownUserEntityID();
    }

    public static initializeMyselfCharacterInfo createinitializeMyselfCharacterInfo(byte[] data)
    {
        return flat_initializeMyselfCharacterInfo.getRootAsflat_initializeMyselfCharacterInfo( data );
    }

    public static byte[] getBytes(initializeMyselfCharacterInfo data)
    {
        return flat_initializeMyselfCharacterInfo.createflat_initializeMyselfCharacterInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}