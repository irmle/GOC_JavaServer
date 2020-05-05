package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class LoadingPlayerData {

    public String tokenID;
    public int guardianID;
    public int characterType;
    public String characterName;
    public float currentProgressPercentage;

    public LoadingPlayerData() { }

    public LoadingPlayerData(flat_LoadingPlayerData data) {
        this.tokenID = data.tokenID();
        this.guardianID = data.guardianID();
        this.characterType = data.characterType();
        this.characterName = data.characterName();
        this.currentProgressPercentage = data.currentProgressPercentage();
    }

    public static LoadingPlayerData createLoadingPlayerData(byte[] data)
    {
        return flat_LoadingPlayerData.getRootAsflat_LoadingPlayerData( data );
    }

    public static byte[] getBytes(LoadingPlayerData data)
    {
        return flat_LoadingPlayerData.createflat_LoadingPlayerData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}