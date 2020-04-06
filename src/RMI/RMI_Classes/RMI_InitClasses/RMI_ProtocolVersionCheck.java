package RMI.RMI_Classes.RMI_InitClasses;

public class RMI_ProtocolVersionCheck {

    public int rmi_protocol_version;

    public RMI_ProtocolVersionCheck() { }

    public RMI_ProtocolVersionCheck(flat_RMI_ProtocolVersionCheck data) {
        this.rmi_protocol_version = data.rmiProtocolVersion();
    }

    public static RMI_ProtocolVersionCheck createRMI_ProtocolVersionCheck(byte[] data)
    {
        return flat_RMI_ProtocolVersionCheck.getRootAsflat_RMI_ProtocolVersionCheck( data );
    }

    public static byte[] getBytes(RMI_ProtocolVersionCheck data)
    {
        return flat_RMI_ProtocolVersionCheck.createflat_RMI_ProtocolVersionCheck( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}