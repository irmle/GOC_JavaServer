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
public final class flat_ConditionData extends Table {
  public static flat_ConditionData getRootAsflat_ConditionData(ByteBuffer _bb) { return getRootAsflat_ConditionData(_bb, new flat_ConditionData()); }
  public static flat_ConditionData getRootAsflat_ConditionData(ByteBuffer _bb, flat_ConditionData obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_ConditionData __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int itemType() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int skillType() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float buffDurationTime() { int o = __offset(8); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float buffRemainTime() { int o = __offset(10); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }

  public static int createflat_ConditionData(FlatBufferBuilder builder,
      int itemType,
      int skillType,
      float buffDurationTime,
      float buffRemainTime) {
    builder.startObject(4);
    flat_ConditionData.addBuffRemainTime(builder, buffRemainTime);
    flat_ConditionData.addBuffDurationTime(builder, buffDurationTime);
    flat_ConditionData.addSkillType(builder, skillType);
    flat_ConditionData.addItemType(builder, itemType);
    return flat_ConditionData.endflat_ConditionData(builder);
  }

  public static void startflat_ConditionData(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addItemType(FlatBufferBuilder builder, int itemType) { builder.addInt(0, itemType, 0); }
  public static void addSkillType(FlatBufferBuilder builder, int skillType) { builder.addInt(1, skillType, 0); }
  public static void addBuffDurationTime(FlatBufferBuilder builder, float buffDurationTime) { builder.addFloat(2, buffDurationTime, 0.0f); }
  public static void addBuffRemainTime(FlatBufferBuilder builder, float buffRemainTime) { builder.addFloat(3, buffRemainTime, 0.0f); }
  public static int endflat_ConditionData(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_ConditionData(FlatBufferBuilder builder,
 ConditionData data) {
        return createflat_ConditionData(builder , data.itemType, data.skillType, data.buffDurationTime, data.buffRemainTime);
    }

    public static byte[] createflat_ConditionData(ConditionData data) {
        FlatBufferBuilder fbb = PooledFlatBufferBuilder.DEFAULT.poll();
        fbb.finish(flat_ConditionData.createflat_ConditionData(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb.clear(); PooledFlatBufferBuilder.DEFAULT.offer(fbb);
        return result;
    }

    public static ConditionData getRootAsflat_ConditionData(byte[] data) {
        ByteBuf readData = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
        readData.writeBytes(data);
        ConditionData result = new ConditionData(flat_ConditionData.getRootAsflat_ConditionData( readData.nioBuffer() ) );
        readData.release();
        return result;
    }

}