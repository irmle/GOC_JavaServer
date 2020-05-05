package Network.RMI_Classes.RMI_InitClasses;

public class RMI_RSA_PublicKey {

    public String base64Encoded_publicKey;

    public RMI_RSA_PublicKey() { }

    public RMI_RSA_PublicKey(flat_RMI_RSA_PublicKey data) {
        this.base64Encoded_publicKey = data.base64EncodedPublicKey();
    }

    public static RMI_RSA_PublicKey createRMI_RSA_PublicKey(byte[] data)
    {
        return flat_RMI_RSA_PublicKey.getRootAsflat_RMI_RSA_PublicKey( data );
    }

    public static byte[] getBytes(RMI_RSA_PublicKey data)
    {
        return flat_RMI_RSA_PublicKey.createflat_RMI_RSA_PublicKey( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}