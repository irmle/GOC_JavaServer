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
public final class flat_SkillInfoData extends Table {
  public static flat_SkillInfoData getRootAsflat_SkillInfoData(ByteBuffer _bb) { return getRootAsflat_SkillInfoData(_bb, new flat_SkillInfoData()); }
  public static flat_SkillInfoData getRootAsflat_SkillInfoData(ByteBuffer _bb, flat_SkillInfoData obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_SkillInfoData __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int skillType() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_SkillInfoData(FlatBufferBuilder builder,
      int skillType) {
    builder.startObject(1);
    flat_SkillInfoData.addSkillType(builder, skillType);
    return flat_SkillInfoData.endflat_SkillInfoData(builder);
  }

  public static void startflat_SkillInfoData(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addSkillType(FlatBufferBuilder builder, int skillType) { builder.addInt(0, skillType, 0); }
  public static int endflat_SkillInfoData(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_SkillInfoData(FlatBufferBuilder builder,
 SkillInfoData data) {
        return createflat_SkillInfoData(builder , data.skillType);
    }

    public static byte[] createflat_SkillInfoData(SkillInfoData data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_SkillInfoData.createflat_SkillInfoData(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static SkillInfoData getRootAsflat_SkillInfoData(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        SkillInfoData result = new SkillInfoData(flat_SkillInfoData.getRootAsflat_SkillInfoData( buf ) );
        buf = null;
        return result;
    }

}