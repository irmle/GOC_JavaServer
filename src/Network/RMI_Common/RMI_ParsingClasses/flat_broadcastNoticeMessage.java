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
public final class flat_broadcastNoticeMessage extends Table {
  public static flat_broadcastNoticeMessage getRootAsflat_broadcastNoticeMessage(ByteBuffer _bb) { return getRootAsflat_broadcastNoticeMessage(_bb, new flat_broadcastNoticeMessage()); }
  public static flat_broadcastNoticeMessage getRootAsflat_broadcastNoticeMessage(ByteBuffer _bb, flat_broadcastNoticeMessage obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_broadcastNoticeMessage __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public flat_MessageData message() { return message(new flat_MessageData()); }
  public flat_MessageData message(flat_MessageData obj) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(o + bb_pos), bb) : null; }

  public static int createflat_broadcastNoticeMessage(FlatBufferBuilder builder,
      int messageOffset) {
    builder.startObject(1);
    flat_broadcastNoticeMessage.addMessage(builder, messageOffset);
    return flat_broadcastNoticeMessage.endflat_broadcastNoticeMessage(builder);
  }

  public static void startflat_broadcastNoticeMessage(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addMessage(FlatBufferBuilder builder, int messageOffset) { builder.addOffset(0, messageOffset, 0); }
  public static int endflat_broadcastNoticeMessage(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_broadcastNoticeMessage(FlatBufferBuilder builder,
 broadcastNoticeMessage data) {
        int messageOffset = flat_MessageData.createflat_MessageData(builder, data.message);
        return createflat_broadcastNoticeMessage(builder , messageOffset);
    }

    public static byte[] createflat_broadcastNoticeMessage(broadcastNoticeMessage data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_broadcastNoticeMessage.createflat_broadcastNoticeMessage(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static broadcastNoticeMessage getRootAsflat_broadcastNoticeMessage(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        broadcastNoticeMessage result = new broadcastNoticeMessage(flat_broadcastNoticeMessage.getRootAsflat_broadcastNoticeMessage( buf ) );
        buf = null;
        return result;
    }

}