package RMI.RMI_Classes.RMI_InitClasses;

public class RMI_UDP_ConnectionConfirm {

    public int checkUDP_Connection;

    public RMI_UDP_ConnectionConfirm() { }

    public RMI_UDP_ConnectionConfirm(flat_RMI_UDP_ConnectionConfirm data) {
        this.checkUDP_Connection = data.checkUDPConnection();
    }

    public static RMI_UDP_ConnectionConfirm createRMI_UDP_ConnectionConfirm(byte[] data)
    {
        return flat_RMI_UDP_ConnectionConfirm.getRootAsflat_RMI_UDP_ConnectionConfirm( data );
    }

    public static byte[] getBytes(RMI_UDP_ConnectionConfirm data)
    {
        return flat_RMI_UDP_ConnectionConfirm.createflat_RMI_UDP_ConnectionConfirm( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}