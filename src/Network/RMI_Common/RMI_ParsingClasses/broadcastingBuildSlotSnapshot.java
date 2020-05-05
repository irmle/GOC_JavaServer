package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class broadcastingBuildSlotSnapshot {

    public LinkedList <BuildSlotData> buildSlotList = new LinkedList<>();

    public broadcastingBuildSlotSnapshot() { }

    public broadcastingBuildSlotSnapshot(flat_broadcastingBuildSlotSnapshot data) {
        for(int i = 0;i < data.buildSlotListLength();i++) {
            this.buildSlotList.addLast(new BuildSlotData(data.buildSlotList(i)));
        }
    }

    public static broadcastingBuildSlotSnapshot createbroadcastingBuildSlotSnapshot(byte[] data)
    {
        return flat_broadcastingBuildSlotSnapshot.getRootAsflat_broadcastingBuildSlotSnapshot( data );
    }

    public static byte[] getBytes(broadcastingBuildSlotSnapshot data)
    {
        return flat_broadcastingBuildSlotSnapshot.createflat_broadcastingBuildSlotSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}