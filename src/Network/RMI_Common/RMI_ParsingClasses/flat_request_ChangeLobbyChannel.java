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
public final class flat_request_ChangeLobbyChannel extends Table {
  public static flat_request_ChangeLobbyChannel getRootAsflat_request_ChangeLobbyChannel(ByteBuffer _bb) { return getRootAsflat_request_ChangeLobbyChannel(_bb, new flat_request_ChangeLobbyChannel()); }
  public static flat_request_ChangeLobbyChannel getRootAsflat_request_ChangeLobbyChannel(ByteBuffer _bb, flat_request_ChangeLobbyChannel obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_request_ChangeLobbyChannel __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int channelNum() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_request_ChangeLobbyChannel(FlatBufferBuilder builder,
      int channelNum) {
    builder.startObject(1);
    flat_request_ChangeLobbyChannel.addChannelNum(builder, channelNum);
    return flat_request_ChangeLobbyChannel.endflat_request_ChangeLobbyChannel(builder);
  }

  public static void startflat_request_ChangeLobbyChannel(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addChannelNum(FlatBufferBuilder builder, int channelNum) { builder.addInt(0, channelNum, 0); }
  public static int endflat_request_ChangeLobbyChannel(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_request_ChangeLobbyChannel(FlatBufferBuilder builder,
 request_ChangeLobbyChannel data) {
        return createflat_request_ChangeLobbyChannel(builder , data.channelNum);
    }

    public static byte[] createflat_request_ChangeLobbyChannel(request_ChangeLobbyChannel data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_request_ChangeLobbyChannel.createflat_request_ChangeLobbyChannel(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static request_ChangeLobbyChannel getRootAsflat_request_ChangeLobbyChannel(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        request_ChangeLobbyChannel result = new request_ChangeLobbyChannel(flat_request_ChangeLobbyChannel.getRootAsflat_request_ChangeLobbyChannel( buf ) );
        buf = null;
        return result;
    }

}