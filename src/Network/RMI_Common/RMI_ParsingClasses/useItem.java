package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class useItem {

    public int worldMapID;
    public int userEntityID;
    public short itemSlotNum;
    public short itemCount;

    public useItem() { }

    public useItem(flat_useItem data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.itemSlotNum = data.itemSlotNum();
        this.itemCount = data.itemCount();
    }

    public static useItem createuseItem(byte[] data)
    {
        return flat_useItem.getRootAsflat_useItem( data );
    }

    public static byte[] getBytes(useItem data)
    {
        return flat_useItem.createflat_useItem( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}