package Network.RMI_Common.RMI_ParsingClasses;

public class sellItem {

    public int worldMapID;
    public int userEntityID;
    public short itemSlotNum;
    public int itemType;
    public short itemCount;

    public sellItem() { }

    public sellItem(flat_sellItem data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.itemSlotNum = data.itemSlotNum();
        this.itemType = data.itemType();
        this.itemCount = data.itemCount();
    }

    public static sellItem createsellItem(byte[] data)
    {
        return flat_sellItem.getRootAsflat_sellItem( data );
    }

    public static byte[] getBytes(sellItem data)
    {
        return flat_sellItem.createflat_sellItem( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}