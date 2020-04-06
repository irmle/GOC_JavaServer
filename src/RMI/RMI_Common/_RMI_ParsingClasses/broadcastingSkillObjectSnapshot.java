package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class broadcastingSkillObjectSnapshot {

    public LinkedList <SkillObjectData> skillObjectSnapshot = new LinkedList<>();

    public broadcastingSkillObjectSnapshot() { }

    public broadcastingSkillObjectSnapshot(flat_broadcastingSkillObjectSnapshot data) {
        for(int i = 0;i < data.skillObjectSnapshotLength();i++) {
            this.skillObjectSnapshot.addLast(new SkillObjectData(data.skillObjectSnapshot(i)));
        }
    }

    public static broadcastingSkillObjectSnapshot createbroadcastingSkillObjectSnapshot(byte[] data)
    {
        return flat_broadcastingSkillObjectSnapshot.getRootAsflat_broadcastingSkillObjectSnapshot( data );
    }

    public static byte[] getBytes(broadcastingSkillObjectSnapshot data)
    {
        return flat_broadcastingSkillObjectSnapshot.createflat_broadcastingSkillObjectSnapshot( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}