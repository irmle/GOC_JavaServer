package RMI.AutoCreatedClass;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class ItemSlotData {

    public int slotNum;
    public ItemInfoData itemInfo;
    public int itemCount;
    public float remainCoolTime;

    public ItemSlotData() { }

    public ItemSlotData(flat_ItemSlotData data) {
        this.slotNum = data.slotNum();
        this.itemInfo = new ItemInfoData(data.itemInfo());
        this.itemCount = data.itemCount();
        this.remainCoolTime = data.remainCoolTime();
    }

    public static ItemSlotData createItemSlotData(byte[] data)
    {
        return flat_ItemSlotData.getRootAsflat_ItemSlotData( data );
    }

    public static byte[] getBytes(ItemSlotData data)
    {
        return flat_ItemSlotData.createflat_ItemSlotData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}