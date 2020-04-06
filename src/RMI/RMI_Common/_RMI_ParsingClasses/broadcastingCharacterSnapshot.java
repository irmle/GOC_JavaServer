package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class broadcastingCharacterSnapshot {

    public LinkedList <CharacterData> characterSnapshot = new LinkedList<>();

    public broadcastingCharacterSnapshot() { }

    public broadcastingCharacterSnapshot(flat_broadcastingCharacterSnapshot data) {
        for(int i = 0;i < data.characterSnapshotLength();i++) {
            this.characterSnapshot.addLast(new CharacterData(data.characterSnapshot(i)));
        }
    }

    public static broadcastingCharacterSnapshot createbroadcastingCharacterSnapshot(byte[] data)
    {
        return flat_broadcastingCharacterSnapshot.getRootAsflat_broadcastingCharacterSnapshot( data );
    }

    public static byte[] getBytes(broadcastingCharacterSnapshot data)
    {
        return flat_broadcastingCharacterSnapshot.createflat_broadcastingCharacterSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}