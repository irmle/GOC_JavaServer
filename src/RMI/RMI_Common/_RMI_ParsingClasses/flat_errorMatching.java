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
public final class flat_errorMatching extends Table {
  public static flat_errorMatching getRootAsflat_errorMatching(ByteBuffer _bb) { return getRootAsflat_errorMatching(_bb, new flat_errorMatching()); }
  public static flat_errorMatching getRootAsflat_errorMatching(ByteBuffer _bb, flat_errorMatching obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_errorMatching __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int errorCode() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public String errorReason() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer errorReasonAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer errorReasonInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }

  public static int createflat_errorMatching(FlatBufferBuilder builder,
      int errorCode,
      int errorReasonOffset) {
    builder.startObject(2);
    flat_errorMatching.addErrorReason(builder, errorReasonOffset);
    flat_errorMatching.addErrorCode(builder, errorCode);
    return flat_errorMatching.endflat_errorMatching(builder);
  }

  public static void startflat_errorMatching(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addErrorCode(FlatBufferBuilder builder, int errorCode) { builder.addInt(0, errorCode, 0); }
  public static void addErrorReason(FlatBufferBuilder builder, int errorReasonOffset) { builder.addOffset(1, errorReasonOffset, 0); }
  public static int endflat_errorMatching(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_errorMatching(FlatBufferBuilder builder,
 errorMatching data) {
        int errorReasonOffset = builder.createString(data.errorReason);
        return createflat_errorMatching(builder , data.errorCode, errorReasonOffset);
    }

    public static byte[] createflat_errorMatching(errorMatching data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_errorMatching.createflat_errorMatching(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static errorMatching getRootAsflat_errorMatching(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        errorMatching result = new errorMatching(flat_errorMatching.getRootAsflat_errorMatching( buf ) );
        buf = null;
        return result;
    }

}