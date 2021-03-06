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
public final class flat_pickLogicTime extends Table {
  public static flat_pickLogicTime getRootAsflat_pickLogicTime(ByteBuffer _bb) { return getRootAsflat_pickLogicTime(_bb, new flat_pickLogicTime()); }
  public static flat_pickLogicTime getRootAsflat_pickLogicTime(ByteBuffer _bb, flat_pickLogicTime obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_pickLogicTime __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public float remainCharacterPickTime() { int o = __offset(4); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }

  public static int createflat_pickLogicTime(FlatBufferBuilder builder,
      float remainCharacterPickTime) {
    builder.startObject(1);
    flat_pickLogicTime.addRemainCharacterPickTime(builder, remainCharacterPickTime);
    return flat_pickLogicTime.endflat_pickLogicTime(builder);
  }

  public static void startflat_pickLogicTime(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addRemainCharacterPickTime(FlatBufferBuilder builder, float remainCharacterPickTime) { builder.addFloat(0, remainCharacterPickTime, 0.0f); }
  public static int endflat_pickLogicTime(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_pickLogicTime(FlatBufferBuilder builder,
 pickLogicTime data) {
        return createflat_pickLogicTime(builder , data.remainCharacterPickTime);
    }

    public static byte[] createflat_pickLogicTime(pickLogicTime data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_pickLogicTime.createflat_pickLogicTime(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static pickLogicTime getRootAsflat_pickLogicTime(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        pickLogicTime result = new pickLogicTime(flat_pickLogicTime.getRootAsflat_pickLogicTime( buf ) );
        buf = null;
        return result;
    }

}