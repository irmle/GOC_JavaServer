package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class createWorldMapFlyingObjectEntityInfo {

    public LinkedList <FlyingObjectData> flyingObjectList = new LinkedList<>();

    public createWorldMapFlyingObjectEntityInfo() { }

    public createWorldMapFlyingObjectEntityInfo(flat_createWorldMapFlyingObjectEntityInfo data) {
        for(int i = 0;i < data.flyingObjectListLength();i++) {
            this.flyingObjectList.addLast(new FlyingObjectData(data.flyingObjectList(i)));
        }
    }

    public static createWorldMapFlyingObjectEntityInfo createcreateWorldMapFlyingObjectEntityInfo(byte[] data)
    {
        return flat_createWorldMapFlyingObjectEntityInfo.getRootAsflat_createWorldMapFlyingObjectEntityInfo( data );
    }

    public static byte[] getBytes(createWorldMapFlyingObjectEntityInfo data)
    {
        return flat_createWorldMapFlyingObjectEntityInfo.createflat_createWorldMapFlyingObjectEntityInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}