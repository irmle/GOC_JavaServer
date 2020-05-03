package Network.RMI_Common.RMI_ParsingClasses;

public class buyItem {

    public int worldMapID;
    public int userEntityID;
    public short itemSlotNum;
    public int itemType;
    public short itemCount;

    public buyItem() { }

    public buyItem(flat_buyItem data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.itemSlotNum = data.itemSlotNum();
        this.itemType = data.itemType();
        this.itemCount = data.itemCount();
    }

    public static buyItem createbuyItem(byte[] data)
    {
        return flat_buyItem.getRootAsflat_buyItem( data );
    }

    public static byte[] getBytes(buyItem data)
    {
        return flat_buyItem.createflat_buyItem( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}