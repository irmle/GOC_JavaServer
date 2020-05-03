package Network.RMI_Common.RMI_ParsingClasses;

public class userFailedUpgradeBuilding {

    public int errorCode;

    public userFailedUpgradeBuilding() { }

    public userFailedUpgradeBuilding(flat_userFailedUpgradeBuilding data) {
        this.errorCode = data.errorCode();
    }

    public static userFailedUpgradeBuilding createuserFailedUpgradeBuilding(byte[] data)
    {
        return flat_userFailedUpgradeBuilding.getRootAsflat_userFailedUpgradeBuilding( data );
    }

    public static byte[] getBytes(userFailedUpgradeBuilding data)
    {
        return flat_userFailedUpgradeBuilding.createflat_userFailedUpgradeBuilding( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}