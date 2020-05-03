package Network.RMI_Common.RMI_ParsingClasses;

import Network.AutoCreatedClass.*;

import java.util.LinkedList;

public class createWorldMapCrystalDataEntityInfo {

    public LinkedList <CrystalData> crystalList = new LinkedList<>();

    public createWorldMapCrystalDataEntityInfo() { }

    public createWorldMapCrystalDataEntityInfo(flat_createWorldMapCrystalDataEntityInfo data) {
        for(int i = 0;i < data.crystalListLength();i++) {
            this.crystalList.addLast(new CrystalData(data.crystalList(i)));
        }
    }

    public static createWorldMapCrystalDataEntityInfo createcreateWorldMapCrystalDataEntityInfo(byte[] data)
    {
        return flat_createWorldMapCrystalDataEntityInfo.getRootAsflat_createWorldMapCrystalDataEntityInfo( data );
    }

    public static byte[] getBytes(createWorldMapCrystalDataEntityInfo data)
    {
        return flat_createWorldMapCrystalDataEntityInfo.createflat_createWorldMapCrystalDataEntityInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}