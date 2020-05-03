package Network.RMI_Common.RMI_ParsingClasses;

public class broadcastingDamageAmount {

    public int damageType;
    public int entityID;
    public float damageAmount;

    public broadcastingDamageAmount() { }

    public broadcastingDamageAmount(flat_broadcastingDamageAmount data) {
        this.damageType = data.damageType();
        this.entityID = data.entityID();
        this.damageAmount = data.damageAmount();
    }

    public static broadcastingDamageAmount createbroadcastingDamageAmount(byte[] data)
    {
        return flat_broadcastingDamageAmount.getRootAsflat_broadcastingDamageAmount( data );
    }

    public static byte[] getBytes(broadcastingDamageAmount data)
    {
        return flat_broadcastingDamageAmount.createflat_broadcastingDamageAmount( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}