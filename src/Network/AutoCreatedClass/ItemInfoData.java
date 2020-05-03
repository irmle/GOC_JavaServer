package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;

public class ItemInfoData {

    public int itemType;

    public ItemInfoData() { }

    public ItemInfoData(flat_ItemInfoData data) {
        this.itemType = data.itemType();
    }

    public static ItemInfoData createItemInfoData(byte[] data)
    {
        return flat_ItemInfoData.getRootAsflat_ItemInfoData( data );
    }

    public static byte[] getBytes(ItemInfoData data)
    {
        return flat_ItemInfoData.createflat_ItemInfoData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}