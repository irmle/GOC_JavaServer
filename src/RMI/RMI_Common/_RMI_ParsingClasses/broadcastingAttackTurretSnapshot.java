package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class broadcastingAttackTurretSnapshot {

    public LinkedList <AttackTurretData> attackTurretSnapshot = new LinkedList<>();

    public broadcastingAttackTurretSnapshot() { }

    public broadcastingAttackTurretSnapshot(flat_broadcastingAttackTurretSnapshot data) {
        for(int i = 0;i < data.attackTurretSnapshotLength();i++) {
            this.attackTurretSnapshot.addLast(new AttackTurretData(data.attackTurretSnapshot(i)));
        }
    }

    public static broadcastingAttackTurretSnapshot createbroadcastingAttackTurretSnapshot(byte[] data)
    {
        return flat_broadcastingAttackTurretSnapshot.getRootAsflat_broadcastingAttackTurretSnapshot( data );
    }

    public static byte[] getBytes(broadcastingAttackTurretSnapshot data)
    {
        return flat_broadcastingAttackTurretSnapshot.createflat_broadcastingAttackTurretSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}