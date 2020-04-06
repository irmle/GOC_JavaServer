package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class broadcastingCrystalSnapshot {

    public LinkedList <CrystalData> crystalSnapshot = new LinkedList<>();

    public broadcastingCrystalSnapshot() { }

    public broadcastingCrystalSnapshot(flat_broadcastingCrystalSnapshot data) {
        for(int i = 0;i < data.crystalSnapshotLength();i++) {
            this.crystalSnapshot.addLast(new CrystalData(data.crystalSnapshot(i)));
        }
    }

    public static broadcastingCrystalSnapshot createbroadcastingCrystalSnapshot(byte[] data)
    {
        return flat_broadcastingCrystalSnapshot.getRootAsflat_broadcastingCrystalSnapshot( data );
    }

    public static byte[] getBytes(broadcastingCrystalSnapshot data)
    {
        return flat_broadcastingCrystalSnapshot.createflat_broadcastingCrystalSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}