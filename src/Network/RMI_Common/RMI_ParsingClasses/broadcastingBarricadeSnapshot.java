package Network.RMI_Common.RMI_ParsingClasses;

import Network.AutoCreatedClass.*;

import java.util.LinkedList;

public class broadcastingBarricadeSnapshot {

    public LinkedList <BarricadeData> barricadeSnapshot = new LinkedList<>();

    public broadcastingBarricadeSnapshot() { }

    public broadcastingBarricadeSnapshot(flat_broadcastingBarricadeSnapshot data) {
        for(int i = 0;i < data.barricadeSnapshotLength();i++) {
            this.barricadeSnapshot.addLast(new BarricadeData(data.barricadeSnapshot(i)));
        }
    }

    public static broadcastingBarricadeSnapshot createbroadcastingBarricadeSnapshot(byte[] data)
    {
        return flat_broadcastingBarricadeSnapshot.getRootAsflat_broadcastingBarricadeSnapshot( data );
    }

    public static byte[] getBytes(broadcastingBarricadeSnapshot data)
    {
        return flat_broadcastingBarricadeSnapshot.createflat_broadcastingBarricadeSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}