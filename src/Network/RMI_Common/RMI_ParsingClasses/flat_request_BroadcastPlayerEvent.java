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
public final class flat_request_BroadcastPlayerEvent extends Table {
  public static flat_request_BroadcastPlayerEvent getRootAsflat_request_BroadcastPlayerEvent(ByteBuffer _bb) { return getRootAsflat_request_BroadcastPlayerEvent(_bb, new flat_request_BroadcastPlayerEvent()); }
  public static flat_request_BroadcastPlayerEvent getRootAsflat_request_BroadcastPlayerEvent(ByteBuffer _bb, flat_request_BroadcastPlayerEvent obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_request_BroadcastPlayerEvent __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int messageType() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public String broadcastingDataJS() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer broadcastingDataJSAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer broadcastingDataJSInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }

  public static int createflat_request_BroadcastPlayerEvent(FlatBufferBuilder builder,
      int messageType,
      int broadcastingDataJSOffset) {
    builder.startObject(2);
    flat_request_BroadcastPlayerEvent.addBroadcastingDataJS(builder, broadcastingDataJSOffset);
    flat_request_BroadcastPlayerEvent.addMessageType(builder, messageType);
    return flat_request_BroadcastPlayerEvent.endflat_request_BroadcastPlayerEvent(builder);
  }

  public static void startflat_request_BroadcastPlayerEvent(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addMessageType(FlatBufferBuilder builder, int messageType) { builder.addInt(0, messageType, 0); }
  public static void addBroadcastingDataJS(FlatBufferBuilder builder, int broadcastingDataJSOffset) { builder.addOffset(1, broadcastingDataJSOffset, 0); }
  public static int endflat_request_BroadcastPlayerEvent(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_request_BroadcastPlayerEvent(FlatBufferBuilder builder,
 request_BroadcastPlayerEvent data) {
        int broadcastingDataJSOffset = builder.createString(data.broadcastingDataJS);
        return createflat_request_BroadcastPlayerEvent(builder , data.messageType, broadcastingDataJSOffset);
    }

    public static byte[] createflat_request_BroadcastPlayerEvent(request_BroadcastPlayerEvent data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_request_BroadcastPlayerEvent.createflat_request_BroadcastPlayerEvent(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static request_BroadcastPlayerEvent getRootAsflat_request_BroadcastPlayerEvent(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        request_BroadcastPlayerEvent result = new request_BroadcastPlayerEvent(flat_request_BroadcastPlayerEvent.getRootAsflat_request_BroadcastPlayerEvent( buf ) );
        buf = null;
        return result;
    }

}