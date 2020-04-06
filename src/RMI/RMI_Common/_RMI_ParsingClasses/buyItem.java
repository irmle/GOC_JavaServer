package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

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