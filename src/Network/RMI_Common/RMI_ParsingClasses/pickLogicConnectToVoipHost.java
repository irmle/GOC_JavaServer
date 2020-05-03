package Network.RMI_Common.RMI_ParsingClasses;

public class pickLogicConnectToVoipHost {

    public boolean isVoipOK;
    public int worldMapID;

    public pickLogicConnectToVoipHost() { }

    public pickLogicConnectToVoipHost(flat_pickLogicConnectToVoipHost data) {
        this.isVoipOK = data.isVoipOK();
        this.worldMapID = data.worldMapID();
    }

    public static pickLogicConnectToVoipHost createpickLogicConnectToVoipHost(byte[] data)
    {
        return flat_pickLogicConnectToVoipHost.getRootAsflat_pickLogicConnectToVoipHost( data );
    }

    public static byte[] getBytes(pickLogicConnectToVoipHost data)
    {
        return flat_pickLogicConnectToVoipHost.createflat_pickLogicConnectToVoipHost( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}