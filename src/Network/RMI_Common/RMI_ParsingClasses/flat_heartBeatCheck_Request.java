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
public final class flat_heartBeatCheck_Request extends Table {
  public static flat_heartBeatCheck_Request getRootAsflat_heartBeatCheck_Request(ByteBuffer _bb) { return getRootAsflat_heartBeatCheck_Request(_bb, new flat_heartBeatCheck_Request()); }
  public static flat_heartBeatCheck_Request getRootAsflat_heartBeatCheck_Request(ByteBuffer _bb, flat_heartBeatCheck_Request obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_heartBeatCheck_Request __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public float timeData() { int o = __offset(4); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }

  public static int createflat_heartBeatCheck_Request(FlatBufferBuilder builder,
      float timeData) {
    builder.startObject(1);
    flat_heartBeatCheck_Request.addTimeData(builder, timeData);
    return flat_heartBeatCheck_Request.endflat_heartBeatCheck_Request(builder);
  }

  public static void startflat_heartBeatCheck_Request(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addTimeData(FlatBufferBuilder builder, float timeData) { builder.addFloat(0, timeData, 0.0f); }
  public static int endflat_heartBeatCheck_Request(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_heartBeatCheck_Request(FlatBufferBuilder builder,
 heartBeatCheck_Request data) {
        return createflat_heartBeatCheck_Request(builder , data.timeData);
    }

    public static byte[] createflat_heartBeatCheck_Request(heartBeatCheck_Request data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_heartBeatCheck_Request.createflat_heartBeatCheck_Request(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static heartBeatCheck_Request getRootAsflat_heartBeatCheck_Request(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        heartBeatCheck_Request result = new heartBeatCheck_Request(flat_heartBeatCheck_Request.getRootAsflat_heartBeatCheck_Request( buf ) );
        buf = null;
        return result;
    }

}