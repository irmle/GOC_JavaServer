package RMI.RMI_Classes.RMI_InitClasses;

public class RMI_OverConnectionAnnounce {

    public int maxConnection;

    public RMI_OverConnectionAnnounce() { }

    public RMI_OverConnectionAnnounce(flat_RMI_OverConnectionAnnounce data) {
        this.maxConnection = data.maxConnection();
    }

    public static RMI_OverConnectionAnnounce createRMI_OverConnectionAnnounce(byte[] data)
    {
        return flat_RMI_OverConnectionAnnounce.getRootAsflat_RMI_OverConnectionAnnounce( data );
    }

    public static byte[] getBytes(RMI_OverConnectionAnnounce data)
    {
        return flat_RMI_OverConnectionAnnounce.createflat_RMI_OverConnectionAnnounce( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}