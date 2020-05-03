package Network.RMI_Common.RMI_ParsingClasses;

public class pickSelectedCharacter {

    public String googleIDToken;
    public int characterType;

    public pickSelectedCharacter() { }

    public pickSelectedCharacter(flat_pickSelectedCharacter data) {
        this.googleIDToken = data.googleIDToken();
        this.characterType = data.characterType();
    }

    public static pickSelectedCharacter createpickSelectedCharacter(byte[] data)
    {
        return flat_pickSelectedCharacter.getRootAsflat_pickSelectedCharacter( data );
    }

    public static byte[] getBytes(pickSelectedCharacter data)
    {
        return flat_pickSelectedCharacter.createflat_pickSelectedCharacter( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}