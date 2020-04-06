package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class broadcastingStoreUpgradeBuffList {

    public LinkedList <StoreUpgradeBuffSlotData> storeUpgradeBuffSlotDataList = new LinkedList<>();

    public broadcastingStoreUpgradeBuffList() { }

    public broadcastingStoreUpgradeBuffList(flat_broadcastingStoreUpgradeBuffList data) {
        for(int i = 0;i < data.storeUpgradeBuffSlotDataListLength();i++) {
            this.storeUpgradeBuffSlotDataList.addLast(new StoreUpgradeBuffSlotData(data.storeUpgradeBuffSlotDataList(i)));
        }
    }

    public static broadcastingStoreUpgradeBuffList createbroadcastingStoreUpgradeBuffList(byte[] data)
    {
        return flat_broadcastingStoreUpgradeBuffList.getRootAsflat_broadcastingStoreUpgradeBuffList( data );
    }

    public static byte[] getBytes(broadcastingStoreUpgradeBuffList data)
    {
        return flat_broadcastingStoreUpgradeBuffList.createflat_broadcastingStoreUpgradeBuffList( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}