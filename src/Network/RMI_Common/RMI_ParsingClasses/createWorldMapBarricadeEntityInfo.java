package Network.RMI_Common.RMI_ParsingClasses;

import Network.AutoCreatedClass.*;

import java.util.LinkedList;

public class createWorldMapBarricadeEntityInfo {

    public LinkedList <BarricadeData> barricadeList = new LinkedList<>();

    public createWorldMapBarricadeEntityInfo() { }

    public createWorldMapBarricadeEntityInfo(flat_createWorldMapBarricadeEntityInfo data) {
        for(int i = 0;i < data.barricadeListLength();i++) {
            this.barricadeList.addLast(new BarricadeData(data.barricadeList(i)));
        }
    }

    public static createWorldMapBarricadeEntityInfo createcreateWorldMapBarricadeEntityInfo(byte[] data)
    {
        return flat_createWorldMapBarricadeEntityInfo.getRootAsflat_createWorldMapBarricadeEntityInfo( data );
    }

    public static byte[] getBytes(createWorldMapBarricadeEntityInfo data)
    {
        return flat_createWorldMapBarricadeEntityInfo.createflat_createWorldMapBarricadeEntityInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}