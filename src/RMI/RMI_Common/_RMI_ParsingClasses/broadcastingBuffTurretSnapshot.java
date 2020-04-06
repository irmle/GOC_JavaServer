package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class broadcastingBuffTurretSnapshot {

    public LinkedList <BuffTurretData> buffTurretSnapshot = new LinkedList<>();

    public broadcastingBuffTurretSnapshot() { }

    public broadcastingBuffTurretSnapshot(flat_broadcastingBuffTurretSnapshot data) {
        for(int i = 0;i < data.buffTurretSnapshotLength();i++) {
            this.buffTurretSnapshot.addLast(new BuffTurretData(data.buffTurretSnapshot(i)));
        }
    }

    public static broadcastingBuffTurretSnapshot createbroadcastingBuffTurretSnapshot(byte[] data)
    {
        return flat_broadcastingBuffTurretSnapshot.getRootAsflat_broadcastingBuffTurretSnapshot( data );
    }

    public static byte[] getBytes(broadcastingBuffTurretSnapshot data)
    {
        return flat_broadcastingBuffTurretSnapshot.createflat_broadcastingBuffTurretSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}