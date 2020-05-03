package Network.RMI_Common.RMI_ParsingClasses;

import Network.AutoCreatedClass.*;

import java.util.LinkedList;

public class destroyWorldMapEntityInfo {

    public LinkedList <DestroyEntityData> destroyedEntityList = new LinkedList<>();

    public destroyWorldMapEntityInfo() { }

    public destroyWorldMapEntityInfo(flat_destroyWorldMapEntityInfo data) {
        for(int i = 0;i < data.destroyedEntityListLength();i++) {
            this.destroyedEntityList.addLast(new DestroyEntityData(data.destroyedEntityList(i)));
        }
    }

    public static destroyWorldMapEntityInfo createdestroyWorldMapEntityInfo(byte[] data)
    {
        return flat_destroyWorldMapEntityInfo.getRootAsflat_destroyWorldMapEntityInfo( data );
    }

    public static byte[] getBytes(destroyWorldMapEntityInfo data)
    {
        return flat_destroyWorldMapEntityInfo.createflat_destroyWorldMapEntityInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}