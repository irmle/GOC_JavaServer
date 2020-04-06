package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class broadcastingMonsterSnapshot {

    public LinkedList <MonsterData> monsterSnapshot = new LinkedList<>();

    public broadcastingMonsterSnapshot() { }

    public broadcastingMonsterSnapshot(flat_broadcastingMonsterSnapshot data) {
        for(int i = 0;i < data.monsterSnapshotLength();i++) {
            this.monsterSnapshot.addLast(new MonsterData(data.monsterSnapshot(i)));
        }
    }

    public static broadcastingMonsterSnapshot createbroadcastingMonsterSnapshot(byte[] data)
    {
        return flat_broadcastingMonsterSnapshot.getRootAsflat_broadcastingMonsterSnapshot( data );
    }

    public static byte[] getBytes(broadcastingMonsterSnapshot data)
    {
        return flat_broadcastingMonsterSnapshot.createflat_broadcastingMonsterSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}