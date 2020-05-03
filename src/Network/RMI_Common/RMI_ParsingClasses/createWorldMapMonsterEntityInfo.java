package Network.RMI_Common.RMI_ParsingClasses;

import Network.AutoCreatedClass.*;

import java.util.LinkedList;

public class createWorldMapMonsterEntityInfo {

    public LinkedList <MonsterData> monsterList = new LinkedList<>();

    public createWorldMapMonsterEntityInfo() { }

    public createWorldMapMonsterEntityInfo(flat_createWorldMapMonsterEntityInfo data) {
        for(int i = 0;i < data.monsterListLength();i++) {
            this.monsterList.addLast(new MonsterData(data.monsterList(i)));
        }
    }

    public static createWorldMapMonsterEntityInfo createcreateWorldMapMonsterEntityInfo(byte[] data)
    {
        return flat_createWorldMapMonsterEntityInfo.getRootAsflat_createWorldMapMonsterEntityInfo( data );
    }

    public static byte[] getBytes(createWorldMapMonsterEntityInfo data)
    {
        return flat_createWorldMapMonsterEntityInfo.createflat_createWorldMapMonsterEntityInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}