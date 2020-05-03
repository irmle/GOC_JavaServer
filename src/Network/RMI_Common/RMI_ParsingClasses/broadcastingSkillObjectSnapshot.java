package Network.RMI_Common.RMI_ParsingClasses;

import Network.AutoCreatedClass.*;

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