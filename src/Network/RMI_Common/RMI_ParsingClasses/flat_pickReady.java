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
public final class flat_pickReady extends Table {
  public static flat_pickReady getRootAsflat_pickReady(ByteBuffer _bb) { return getRootAsflat_pickReady(_bb, new flat_pickReady()); }
  public static flat_pickReady getRootAsflat_pickReady(ByteBuffer _bb, flat_pickReady obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_pickReady __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String googleIDToken() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer googleIDTokenAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer googleIDTokenInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }

  public static int createflat_pickReady(FlatBufferBuilder builder,
      int googleIDTokenOffset) {
    builder.startObject(1);
    flat_pickReady.addGoogleIDToken(builder, googleIDTokenOffset);
    return flat_pickReady.endflat_pickReady(builder);
  }

  public static void startflat_pickReady(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addGoogleIDToken(FlatBufferBuilder builder, int googleIDTokenOffset) { builder.addOffset(0, googleIDTokenOffset, 0); }
  public static int endflat_pickReady(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_pickReady(FlatBufferBuilder builder,
 pickReady data) {
        int googleIDTokenOffset = builder.createString(data.googleIDToken);
        return createflat_pickReady(builder , googleIDTokenOffset);
    }

    public static byte[] createflat_pickReady(pickReady data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_pickReady.createflat_pickReady(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static pickReady getRootAsflat_pickReady(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        pickReady result = new pickReady(flat_pickReady.getRootAsflat_pickReady( buf ) );
        buf = null;
        return result;
    }

}