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
public final class flat_pickCancel extends Table {
  public static flat_pickCancel getRootAsflat_pickCancel(ByteBuffer _bb) { return getRootAsflat_pickCancel(_bb, new flat_pickCancel()); }
  public static flat_pickCancel getRootAsflat_pickCancel(ByteBuffer _bb, flat_pickCancel obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_pickCancel __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String googleIDToken() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer googleIDTokenAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer googleIDTokenInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }

  public static int createflat_pickCancel(FlatBufferBuilder builder,
      int googleIDTokenOffset) {
    builder.startObject(1);
    flat_pickCancel.addGoogleIDToken(builder, googleIDTokenOffset);
    return flat_pickCancel.endflat_pickCancel(builder);
  }

  public static void startflat_pickCancel(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addGoogleIDToken(FlatBufferBuilder builder, int googleIDTokenOffset) { builder.addOffset(0, googleIDTokenOffset, 0); }
  public static int endflat_pickCancel(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_pickCancel(FlatBufferBuilder builder,
 pickCancel data) {
        int googleIDTokenOffset = builder.createString(data.googleIDToken);
        return createflat_pickCancel(builder , googleIDTokenOffset);
    }

    public static byte[] createflat_pickCancel(pickCancel data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_pickCancel.createflat_pickCancel(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static pickCancel getRootAsflat_pickCancel(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        pickCancel result = new pickCancel(flat_pickCancel.getRootAsflat_pickCancel( buf ) );
        buf = null;
        return result;
    }

}