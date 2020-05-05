package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class createWorldMapCharacterEntityInfo {

    public LinkedList <CharacterData> characterList = new LinkedList<>();

    public createWorldMapCharacterEntityInfo() { }

    public createWorldMapCharacterEntityInfo(flat_createWorldMapCharacterEntityInfo data) {
        for(int i = 0;i < data.characterListLength();i++) {
            this.characterList.addLast(new CharacterData(data.characterList(i)));
        }
    }

    public static createWorldMapCharacterEntityInfo createcreateWorldMapCharacterEntityInfo(byte[] data)
    {
        return flat_createWorldMapCharacterEntityInfo.getRootAsflat_createWorldMapCharacterEntityInfo( data );
    }

    public static byte[] getBytes(createWorldMapCharacterEntityInfo data)
    {
        return flat_createWorldMapCharacterEntityInfo.createflat_createWorldMapCharacterEntityInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}