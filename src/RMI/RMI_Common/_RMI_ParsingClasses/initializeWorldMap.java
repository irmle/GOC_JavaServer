package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class initializeWorldMap {

    public LinkedList <CharacterData> characterList = new LinkedList<>();
    public LinkedList <MonsterData> monsterList = new LinkedList<>();
    public LinkedList <BuffTurretData> buffTurretList = new LinkedList<>();
    public LinkedList <AttackTurretData> attackTurretList = new LinkedList<>();
    public LinkedList <BarricadeData> barricadeList = new LinkedList<>();
    public LinkedList <CrystalData> crystalList = new LinkedList<>();
    public LinkedList <SkillObjectData> skillObjectList = new LinkedList<>();
    public LinkedList <FlyingObjectData> flyingObjectList = new LinkedList<>();
    public LinkedList <BuildSlotData> buildSlotList = new LinkedList<>();

    public initializeWorldMap() { }

    public initializeWorldMap(flat_initializeWorldMap data) {
        for(int i = 0;i < data.characterListLength();i++) {
            this.characterList.addLast(new CharacterData(data.characterList(i)));
        }
        for(int i = 0;i < data.monsterListLength();i++) {
            this.monsterList.addLast(new MonsterData(data.monsterList(i)));
        }
        for(int i = 0;i < data.buffTurretListLength();i++) {
            this.buffTurretList.addLast(new BuffTurretData(data.buffTurretList(i)));
        }
        for(int i = 0;i < data.attackTurretListLength();i++) {
            this.attackTurretList.addLast(new AttackTurretData(data.attackTurretList(i)));
        }
        for(int i = 0;i < data.barricadeListLength();i++) {
            this.barricadeList.addLast(new BarricadeData(data.barricadeList(i)));
        }
        for(int i = 0;i < data.crystalListLength();i++) {
            this.crystalList.addLast(new CrystalData(data.crystalList(i)));
        }
        for(int i = 0;i < data.skillObjectListLength();i++) {
            this.skillObjectList.addLast(new SkillObjectData(data.skillObjectList(i)));
        }
        for(int i = 0;i < data.flyingObjectListLength();i++) {
            this.flyingObjectList.addLast(new FlyingObjectData(data.flyingObjectList(i)));
        }
        for(int i = 0;i < data.buildSlotListLength();i++) {
            this.buildSlotList.addLast(new BuildSlotData(data.buildSlotList(i)));
        }
    }

    public static initializeWorldMap createinitializeWorldMap(byte[] data)
    {
        return flat_initializeWorldMap.getRootAsflat_initializeWorldMap( data );
    }

    public static byte[] getBytes(initializeWorldMap data)
    {
        return flat_initializeWorldMap.createflat_initializeWorldMap( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}