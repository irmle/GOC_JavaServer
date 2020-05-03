package Network.RMI_Common.RMI_ParsingClasses;

public class heartBeatCheck_Request {

    public float timeData;

    public heartBeatCheck_Request() { }

    public heartBeatCheck_Request(flat_heartBeatCheck_Request data) {
        this.timeData = data.timeData();
    }

    public static heartBeatCheck_Request createheartBeatCheck_Request(byte[] data)
    {
        return flat_heartBeatCheck_Request.getRootAsflat_heartBeatCheck_Request( data );
    }

    public static byte[] getBytes(heartBeatCheck_Request data)
    {
        return flat_heartBeatCheck_Request.createflat_heartBeatCheck_Request( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}