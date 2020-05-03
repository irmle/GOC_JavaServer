package Network.RMI_Common.RMI_ParsingClasses;

public class pingCheck_Request {

    public float timeData;

    public pingCheck_Request() { }

    public pingCheck_Request(flat_pingCheck_Request data) {
        this.timeData = data.timeData();
    }

    public static pingCheck_Request createpingCheck_Request(byte[] data)
    {
        return flat_pingCheck_Request.getRootAsflat_pingCheck_Request( data );
    }

    public static byte[] getBytes(pingCheck_Request data)
    {
        return flat_pingCheck_Request.createflat_pingCheck_Request( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}