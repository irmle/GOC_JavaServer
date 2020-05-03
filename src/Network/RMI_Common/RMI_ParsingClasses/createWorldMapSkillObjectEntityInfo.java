package Network.RMI_Common.RMI_ParsingClasses;

import Network.AutoCreatedClass.*;

import java.util.LinkedList;

public class createWorldMapSkillObjectEntityInfo {

    public LinkedList <SkillObjectData> skillObjectList = new LinkedList<>();

    public createWorldMapSkillObjectEntityInfo() { }

    public createWorldMapSkillObjectEntityInfo(flat_createWorldMapSkillObjectEntityInfo data) {
        for(int i = 0;i < data.skillObjectListLength();i++) {
            this.skillObjectList.addLast(new SkillObjectData(data.skillObjectList(i)));
        }
    }

    public static createWorldMapSkillObjectEntityInfo createcreateWorldMapSkillObjectEntityInfo(byte[] data)
    {
        return flat_createWorldMapSkillObjectEntityInfo.getRootAsflat_createWorldMapSkillObjectEntityInfo( data );
    }

    public static byte[] getBytes(createWorldMapSkillObjectEntityInfo data)
    {
        return flat_createWorldMapSkillObjectEntityInfo.createflat_createWorldMapSkillObjectEntityInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}