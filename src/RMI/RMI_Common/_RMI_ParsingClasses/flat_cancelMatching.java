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
public final class flat_cancelMatching extends Table {
  public static flat_cancelMatching getRootAsflat_cancelMatching(ByteBuffer _bb) { return getRootAsflat_cancelMatching(_bb, new flat_cancelMatching()); }
  public static flat_cancelMatching getRootAsflat_cancelMatching(ByteBuffer _bb, flat_cancelMatching obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_cancelMatching __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String googleIDToken() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer googleIDTokenAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer googleIDTokenInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }

  public static int createflat_cancelMatching(FlatBufferBuilder builder,
      int googleIDTokenOffset) {
    builder.startObject(1);
    flat_cancelMatching.addGoogleIDToken(builder, googleIDTokenOffset);
    return flat_cancelMatching.endflat_cancelMatching(builder);
  }

  public static void startflat_cancelMatching(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addGoogleIDToken(FlatBufferBuilder builder, int googleIDTokenOffset) { builder.addOffset(0, googleIDTokenOffset, 0); }
  public static int endflat_cancelMatching(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_cancelMatching(FlatBufferBuilder builder,
 cancelMatching data) {
        int googleIDTokenOffset = builder.createString(data.googleIDToken);
        return createflat_cancelMatching(builder , googleIDTokenOffset);
    }

    public static byte[] createflat_cancelMatching(cancelMatching data) {
        FlatBufferBuilder fbb = PooledFlatBufferBuilder.DEFAULT.poll();
        fbb.finish(flat_cancelMatching.createflat_cancelMatching(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb.clear(); PooledFlatBufferBuilder.DEFAULT.offer(fbb);
        return result;
    }

    public static cancelMatching getRootAsflat_cancelMatching(byte[] data) {
        ByteBuf readData = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
        readData.writeBytes(data);
        cancelMatching result = new cancelMatching(flat_cancelMatching.getRootAsflat_cancelMatching( readData.nioBuffer() ) );
        readData.release();
        return result;
    }

}