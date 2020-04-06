package RMI.RMI_Classes.RMI_InitClasses;

public class RMI_Send_EncryptedAccept_Data {

    public byte[] AESEncrypted_PublicAESKey;
    public byte[] AESEncrypted_PublicAESIV;
    public int RMI_HostID;
    public int UDP_InitPort;

    public RMI_Send_EncryptedAccept_Data() { }

    public RMI_Send_EncryptedAccept_Data(flat_RMI_Send_EncryptedAccept_Data data) {
        this.AESEncrypted_PublicAESKey = new byte[data.AESEncryptedPublicAESKeyLength()];
        for(int i = 0;i < data.AESEncryptedPublicAESKeyLength();i++) {
            this.AESEncrypted_PublicAESKey[i] = data.AESEncryptedPublicAESKey(i);
        }
        this.AESEncrypted_PublicAESIV = new byte[data.AESEncryptedPublicAESIVLength()];
        for(int i = 0;i < data.AESEncryptedPublicAESIVLength();i++) {
            this.AESEncrypted_PublicAESIV[i] = data.AESEncryptedPublicAESIV(i);
        }
        this.RMI_HostID = data.RMIHostID();
        this.UDP_InitPort = data.UDPInitPort();
    }

    public static RMI_Send_EncryptedAccept_Data createRMI_Send_EncryptedAccept_Data(byte[] data)
    {
        return flat_RMI_Send_EncryptedAccept_Data.getRootAsflat_RMI_Send_EncryptedAccept_Data( data );
    }

    public static byte[] getBytes(RMI_Send_EncryptedAccept_Data data)
    {
        return flat_RMI_Send_EncryptedAccept_Data.createflat_RMI_Send_EncryptedAccept_Data( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}