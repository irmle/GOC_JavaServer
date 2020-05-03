package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;

public class DestroyEntityData {

    public short entityType;
    public int destroyedEntityID;

    public DestroyEntityData() { }

    public DestroyEntityData(flat_DestroyEntityData data) {
        this.entityType = data.entityType();
        this.destroyedEntityID = data.destroyedEntityID();
    }

    public static DestroyEntityData createDestroyEntityData(byte[] data)
    {
        return flat_DestroyEntityData.getRootAsflat_DestroyEntityData( data );
    }

    public static byte[] getBytes(DestroyEntityData data)
    {
        return flat_DestroyEntityData.createflat_DestroyEntityData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}