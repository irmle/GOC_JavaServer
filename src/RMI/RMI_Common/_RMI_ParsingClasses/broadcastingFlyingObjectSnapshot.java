package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class broadcastingFlyingObjectSnapshot {

    public LinkedList <FlyingObjectData> flyingObjectSnapshot = new LinkedList<>();

    public broadcastingFlyingObjectSnapshot() { }

    public broadcastingFlyingObjectSnapshot(flat_broadcastingFlyingObjectSnapshot data) {
        for(int i = 0;i < data.flyingObjectSnapshotLength();i++) {
            this.flyingObjectSnapshot.addLast(new FlyingObjectData(data.flyingObjectSnapshot(i)));
        }
    }

    public static broadcastingFlyingObjectSnapshot createbroadcastingFlyingObjectSnapshot(byte[] data)
    {
        return flat_broadcastingFlyingObjectSnapshot.getRootAsflat_broadcastingFlyingObjectSnapshot( data );
    }

    public static byte[] getBytes(broadcastingFlyingObjectSnapshot data)
    {
        return flat_broadcastingFlyingObjectSnapshot.createflat_broadcastingFlyingObjectSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}