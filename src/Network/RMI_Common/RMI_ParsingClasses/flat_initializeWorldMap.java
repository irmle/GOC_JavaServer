// automatically generated by the FlatBuffers compiler, do not modify

package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;
import io.netty.buffer.*;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class flat_initializeWorldMap extends Table {
  public static flat_initializeWorldMap getRootAsflat_initializeWorldMap(ByteBuffer _bb) { return getRootAsflat_initializeWorldMap(_bb, new flat_initializeWorldMap()); }
  public static flat_initializeWorldMap getRootAsflat_initializeWorldMap(ByteBuffer _bb, flat_initializeWorldMap obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_initializeWorldMap __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public flat_CharacterData characterList(int j) { return characterList(new flat_CharacterData(), j); }
  public flat_CharacterData characterList(flat_CharacterData obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int characterListLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public flat_MonsterData monsterList(int j) { return monsterList(new flat_MonsterData(), j); }
  public flat_MonsterData monsterList(flat_MonsterData obj, int j) { int o = __offset(6); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int monsterListLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public flat_BuffTurretData buffTurretList(int j) { return buffTurretList(new flat_BuffTurretData(), j); }
  public flat_BuffTurretData buffTurretList(flat_BuffTurretData obj, int j) { int o = __offset(8); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int buffTurretListLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }
  public flat_AttackTurretData attackTurretList(int j) { return attackTurretList(new flat_AttackTurretData(), j); }
  public flat_AttackTurretData attackTurretList(flat_AttackTurretData obj, int j) { int o = __offset(10); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int attackTurretListLength() { int o = __offset(10); return o != 0 ? __vector_len(o) : 0; }
  public flat_BarricadeData barricadeList(int j) { return barricadeList(new flat_BarricadeData(), j); }
  public flat_BarricadeData barricadeList(flat_BarricadeData obj, int j) { int o = __offset(12); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int barricadeListLength() { int o = __offset(12); return o != 0 ? __vector_len(o) : 0; }
  public flat_CrystalData crystalList(int j) { return crystalList(new flat_CrystalData(), j); }
  public flat_CrystalData crystalList(flat_CrystalData obj, int j) { int o = __offset(14); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int crystalListLength() { int o = __offset(14); return o != 0 ? __vector_len(o) : 0; }
  public flat_SkillObjectData skillObjectList(int j) { return skillObjectList(new flat_SkillObjectData(), j); }
  public flat_SkillObjectData skillObjectList(flat_SkillObjectData obj, int j) { int o = __offset(16); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int skillObjectListLength() { int o = __offset(16); return o != 0 ? __vector_len(o) : 0; }
  public flat_FlyingObjectData flyingObjectList(int j) { return flyingObjectList(new flat_FlyingObjectData(), j); }
  public flat_FlyingObjectData flyingObjectList(flat_FlyingObjectData obj, int j) { int o = __offset(18); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int flyingObjectListLength() { int o = __offset(18); return o != 0 ? __vector_len(o) : 0; }
  public flat_BuildSlotData buildSlotList(int j) { return buildSlotList(new flat_BuildSlotData(), j); }
  public flat_BuildSlotData buildSlotList(flat_BuildSlotData obj, int j) { int o = __offset(20); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int buildSlotListLength() { int o = __offset(20); return o != 0 ? __vector_len(o) : 0; }

  public static int createflat_initializeWorldMap(FlatBufferBuilder builder,
      int characterListOffset,
      int monsterListOffset,
      int buffTurretListOffset,
      int attackTurretListOffset,
      int barricadeListOffset,
      int crystalListOffset,
      int skillObjectListOffset,
      int flyingObjectListOffset,
      int buildSlotListOffset) {
    builder.startObject(9);
    flat_initializeWorldMap.addBuildSlotList(builder, buildSlotListOffset);
    flat_initializeWorldMap.addFlyingObjectList(builder, flyingObjectListOffset);
    flat_initializeWorldMap.addSkillObjectList(builder, skillObjectListOffset);
    flat_initializeWorldMap.addCrystalList(builder, crystalListOffset);
    flat_initializeWorldMap.addBarricadeList(builder, barricadeListOffset);
    flat_initializeWorldMap.addAttackTurretList(builder, attackTurretListOffset);
    flat_initializeWorldMap.addBuffTurretList(builder, buffTurretListOffset);
    flat_initializeWorldMap.addMonsterList(builder, monsterListOffset);
    flat_initializeWorldMap.addCharacterList(builder, characterListOffset);
    return flat_initializeWorldMap.endflat_initializeWorldMap(builder);
  }

  public static void startflat_initializeWorldMap(FlatBufferBuilder builder) { builder.startObject(9); }
  public static void addCharacterList(FlatBufferBuilder builder, int characterListOffset) { builder.addOffset(0, characterListOffset, 0); }
  public static int createCharacterListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startCharacterListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addMonsterList(FlatBufferBuilder builder, int monsterListOffset) { builder.addOffset(1, monsterListOffset, 0); }
  public static int createMonsterListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startMonsterListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addBuffTurretList(FlatBufferBuilder builder, int buffTurretListOffset) { builder.addOffset(2, buffTurretListOffset, 0); }
  public static int createBuffTurretListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startBuffTurretListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addAttackTurretList(FlatBufferBuilder builder, int attackTurretListOffset) { builder.addOffset(3, attackTurretListOffset, 0); }
  public static int createAttackTurretListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startAttackTurretListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addBarricadeList(FlatBufferBuilder builder, int barricadeListOffset) { builder.addOffset(4, barricadeListOffset, 0); }
  public static int createBarricadeListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startBarricadeListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addCrystalList(FlatBufferBuilder builder, int crystalListOffset) { builder.addOffset(5, crystalListOffset, 0); }
  public static int createCrystalListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startCrystalListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addSkillObjectList(FlatBufferBuilder builder, int skillObjectListOffset) { builder.addOffset(6, skillObjectListOffset, 0); }
  public static int createSkillObjectListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startSkillObjectListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addFlyingObjectList(FlatBufferBuilder builder, int flyingObjectListOffset) { builder.addOffset(7, flyingObjectListOffset, 0); }
  public static int createFlyingObjectListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startFlyingObjectListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addBuildSlotList(FlatBufferBuilder builder, int buildSlotListOffset) { builder.addOffset(8, buildSlotListOffset, 0); }
  public static int createBuildSlotListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startBuildSlotListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endflat_initializeWorldMap(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_initializeWorldMap(FlatBufferBuilder builder,
 initializeWorldMap data) {
        int size0 = data.characterList.size();
        int[] characterList_ = new int[size0];
        for (int x = 0; x < size0; x++) {
        CharacterData aa = data.characterList.poll();
        characterList_[x] = flat_CharacterData.createflat_CharacterData(builder, aa);
        }
        int characterListOffset = flat_initializeWorldMap.createCharacterListVector(builder, characterList_);
        int size1 = data.monsterList.size();
        int[] monsterList_ = new int[size1];
        for (int x = 0; x < size1; x++) {
        MonsterData aa = data.monsterList.poll();
        monsterList_[x] = flat_MonsterData.createflat_MonsterData(builder, aa);
        }
        int monsterListOffset = flat_initializeWorldMap.createMonsterListVector(builder, monsterList_);
        int size2 = data.buffTurretList.size();
        int[] buffTurretList_ = new int[size2];
        for (int x = 0; x < size2; x++) {
        BuffTurretData aa = data.buffTurretList.poll();
        buffTurretList_[x] = flat_BuffTurretData.createflat_BuffTurretData(builder, aa);
        }
        int buffTurretListOffset = flat_initializeWorldMap.createBuffTurretListVector(builder, buffTurretList_);
        int size3 = data.attackTurretList.size();
        int[] attackTurretList_ = new int[size3];
        for (int x = 0; x < size3; x++) {
        AttackTurretData aa = data.attackTurretList.poll();
        attackTurretList_[x] = flat_AttackTurretData.createflat_AttackTurretData(builder, aa);
        }
        int attackTurretListOffset = flat_initializeWorldMap.createAttackTurretListVector(builder, attackTurretList_);
        int size4 = data.barricadeList.size();
        int[] barricadeList_ = new int[size4];
        for (int x = 0; x < size4; x++) {
        BarricadeData aa = data.barricadeList.poll();
        barricadeList_[x] = flat_BarricadeData.createflat_BarricadeData(builder, aa);
        }
        int barricadeListOffset = flat_initializeWorldMap.createBarricadeListVector(builder, barricadeList_);
        int size5 = data.crystalList.size();
        int[] crystalList_ = new int[size5];
        for (int x = 0; x < size5; x++) {
        CrystalData aa = data.crystalList.poll();
        crystalList_[x] = flat_CrystalData.createflat_CrystalData(builder, aa);
        }
        int crystalListOffset = flat_initializeWorldMap.createCrystalListVector(builder, crystalList_);
        int size6 = data.skillObjectList.size();
        int[] skillObjectList_ = new int[size6];
        for (int x = 0; x < size6; x++) {
        SkillObjectData aa = data.skillObjectList.poll();
        skillObjectList_[x] = flat_SkillObjectData.createflat_SkillObjectData(builder, aa);
        }
        int skillObjectListOffset = flat_initializeWorldMap.createSkillObjectListVector(builder, skillObjectList_);
        int size7 = data.flyingObjectList.size();
        int[] flyingObjectList_ = new int[size7];
        for (int x = 0; x < size7; x++) {
        FlyingObjectData aa = data.flyingObjectList.poll();
        flyingObjectList_[x] = flat_FlyingObjectData.createflat_FlyingObjectData(builder, aa);
        }
        int flyingObjectListOffset = flat_initializeWorldMap.createFlyingObjectListVector(builder, flyingObjectList_);
        int size8 = data.buildSlotList.size();
        int[] buildSlotList_ = new int[size8];
        for (int x = 0; x < size8; x++) {
        BuildSlotData aa = data.buildSlotList.poll();
        buildSlotList_[x] = flat_BuildSlotData.createflat_BuildSlotData(builder, aa);
        }
        int buildSlotListOffset = flat_initializeWorldMap.createBuildSlotListVector(builder, buildSlotList_);
        return createflat_initializeWorldMap(builder , characterListOffset, monsterListOffset, buffTurretListOffset, attackTurretListOffset, barricadeListOffset, crystalListOffset, skillObjectListOffset, flyingObjectListOffset, buildSlotListOffset);
    }

    public static byte[] createflat_initializeWorldMap(initializeWorldMap data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_initializeWorldMap.createflat_initializeWorldMap(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static initializeWorldMap getRootAsflat_initializeWorldMap(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        initializeWorldMap result = new initializeWorldMap(flat_initializeWorldMap.getRootAsflat_initializeWorldMap( buf ) );
        buf = null;
        return result;
    }

}