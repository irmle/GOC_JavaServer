// automatically generated by the FlatBuffers compiler, do not modify

package RMI.RMI_Common._RMI_ParsingClasses;
import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import static java.nio.ByteBuffer.wrap;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class flat_getSkill extends Table {
  public static flat_getSkill getRootAsflat_getSkill(ByteBuffer _bb) { return getRootAsflat_getSkill(_bb, new flat_getSkill()); }
  public static flat_getSkill getRootAsflat_getSkill(ByteBuffer _bb, flat_getSkill obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_getSkill __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int worldMapID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int userEntityID() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public short skillSlotNum() { int o = __offset(8); return o != 0 ? bb.getShort(o + bb_pos) : 0; }
  public int skillType() { int o = __offset(10); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_getSkill(FlatBufferBuilder builder,
      int worldMapID,
      int userEntityID,
      short skillSlotNum,
      int skillType) {
    builder.startObject(4);
    flat_getSkill.addSkillType(builder, skillType);
    flat_getSkill.addUserEntityID(builder, userEntityID);
    flat_getSkill.addWorldMapID(builder, worldMapID);
    flat_getSkill.addSkillSlotNum(builder, skillSlotNum);
    return flat_getSkill.endflat_getSkill(builder);
  }

  public static void startflat_getSkill(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addWorldMapID(FlatBufferBuilder builder, int worldMapID) { builder.addInt(0, worldMapID, 0); }
  public static void addUserEntityID(FlatBufferBuilder builder, int userEntityID) { builder.addInt(1, userEntityID, 0); }
  public static void addSkillSlotNum(FlatBufferBuilder builder, short skillSlotNum) { builder.addShort(2, skillSlotNum, 0); }
  public static void addSkillType(FlatBufferBuilder builder, int skillType) { builder.addInt(3, skillType, 0); }
  public static int endflat_getSkill(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_getSkill(FlatBufferBuilder builder,
 getSkill data) {
        return createflat_getSkill(builder , data.worldMapID, data.userEntityID, data.skillSlotNum, data.skillType);
    }

    public static byte[] createflat_getSkill(getSkill data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_getSkill.createflat_getSkill(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static getSkill getRootAsflat_getSkill(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        getSkill result = new getSkill(flat_getSkill.getRootAsflat_getSkill( buf ) );
        buf = null;
        return result;
    }

}