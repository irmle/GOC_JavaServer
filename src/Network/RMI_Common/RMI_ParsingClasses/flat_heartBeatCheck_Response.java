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
public final class flat_heartBeatCheck_Response extends Table {
  public static flat_heartBeatCheck_Response getRootAsflat_heartBeatCheck_Response(ByteBuffer _bb) { return getRootAsflat_heartBeatCheck_Response(_bb, new flat_heartBeatCheck_Response()); }
  public static flat_heartBeatCheck_Response getRootAsflat_heartBeatCheck_Response(ByteBuffer _bb, flat_heartBeatCheck_Response obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_heartBeatCheck_Response __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public float timeData() { int o = __offset(4); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }

  public static int createflat_heartBeatCheck_Response(FlatBufferBuilder builder,
      float timeData) {
    builder.startObject(1);
    flat_heartBeatCheck_Response.addTimeData(builder, timeData);
    return flat_heartBeatCheck_Response.endflat_heartBeatCheck_Response(builder);
  }

  public static void startflat_heartBeatCheck_Response(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addTimeData(FlatBufferBuilder builder, float timeData) { builder.addFloat(0, timeData, 0.0f); }
  public static int endflat_heartBeatCheck_Response(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_heartBeatCheck_Response(FlatBufferBuilder builder,
 heartBeatCheck_Response data) {
        return createflat_heartBeatCheck_Response(builder , data.timeData);
    }

    public static byte[] createflat_heartBeatCheck_Response(heartBeatCheck_Response data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_heartBeatCheck_Response.createflat_heartBeatCheck_Response(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static heartBeatCheck_Response getRootAsflat_heartBeatCheck_Response(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        heartBeatCheck_Response result = new heartBeatCheck_Response(flat_heartBeatCheck_Response.getRootAsflat_heartBeatCheck_Response( buf ) );
        buf = null;
        return result;
    }

}