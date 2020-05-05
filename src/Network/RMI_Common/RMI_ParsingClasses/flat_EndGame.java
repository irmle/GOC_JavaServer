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
public final class flat_EndGame extends Table {
  public static flat_EndGame getRootAsflat_EndGame(ByteBuffer _bb) { return getRootAsflat_EndGame(_bb, new flat_EndGame()); }
  public static flat_EndGame getRootAsflat_EndGame(ByteBuffer _bb, flat_EndGame obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_EndGame __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int resultCode() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public String resultJS() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer resultJSAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer resultJSInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }

  public static int createflat_EndGame(FlatBufferBuilder builder,
      int resultCode,
      int resultJSOffset) {
    builder.startObject(2);
    flat_EndGame.addResultJS(builder, resultJSOffset);
    flat_EndGame.addResultCode(builder, resultCode);
    return flat_EndGame.endflat_EndGame(builder);
  }

  public static void startflat_EndGame(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addResultCode(FlatBufferBuilder builder, int resultCode) { builder.addInt(0, resultCode, 0); }
  public static void addResultJS(FlatBufferBuilder builder, int resultJSOffset) { builder.addOffset(1, resultJSOffset, 0); }
  public static int endflat_EndGame(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_EndGame(FlatBufferBuilder builder,
 EndGame data) {
        int resultJSOffset = builder.createString(data.resultJS);
        return createflat_EndGame(builder , data.resultCode, resultJSOffset);
    }

    public static byte[] createflat_EndGame(EndGame data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_EndGame.createflat_EndGame(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static EndGame getRootAsflat_EndGame(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        EndGame result = new EndGame(flat_EndGame.getRootAsflat_EndGame( buf ) );
        buf = null;
        return result;
    }

}