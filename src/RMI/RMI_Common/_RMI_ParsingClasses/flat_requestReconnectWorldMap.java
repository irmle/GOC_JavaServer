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
public final class flat_requestReconnectWorldMap extends Table {
  public static flat_requestReconnectWorldMap getRootAsflat_requestReconnectWorldMap(ByteBuffer _bb) { return getRootAsflat_requestReconnectWorldMap(_bb, new flat_requestReconnectWorldMap()); }
  public static flat_requestReconnectWorldMap getRootAsflat_requestReconnectWorldMap(ByteBuffer _bb, flat_requestReconnectWorldMap obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_requestReconnectWorldMap __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int worldMapID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public String googleIDToken() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer googleIDTokenAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer googleIDTokenInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }

  public static int createflat_requestReconnectWorldMap(FlatBufferBuilder builder,
      int worldMapID,
      int googleIDTokenOffset) {
    builder.startObject(2);
    flat_requestReconnectWorldMap.addGoogleIDToken(builder, googleIDTokenOffset);
    flat_requestReconnectWorldMap.addWorldMapID(builder, worldMapID);
    return flat_requestReconnectWorldMap.endflat_requestReconnectWorldMap(builder);
  }

  public static void startflat_requestReconnectWorldMap(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addWorldMapID(FlatBufferBuilder builder, int worldMapID) { builder.addInt(0, worldMapID, 0); }
  public static void addGoogleIDToken(FlatBufferBuilder builder, int googleIDTokenOffset) { builder.addOffset(1, googleIDTokenOffset, 0); }
  public static int endflat_requestReconnectWorldMap(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_requestReconnectWorldMap(FlatBufferBuilder builder,
 requestReconnectWorldMap data) {
        int googleIDTokenOffset = builder.createString(data.googleIDToken);
        return createflat_requestReconnectWorldMap(builder , data.worldMapID, googleIDTokenOffset);
    }

    public static byte[] createflat_requestReconnectWorldMap(requestReconnectWorldMap data) {
        FlatBufferBuilder fbb = PooledFlatBufferBuilder.DEFAULT.poll();
        fbb.finish(flat_requestReconnectWorldMap.createflat_requestReconnectWorldMap(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb.clear(); PooledFlatBufferBuilder.DEFAULT.offer(fbb);
        return result;
    }

    public static requestReconnectWorldMap getRootAsflat_requestReconnectWorldMap(byte[] data) {
        ByteBuf readData = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
        readData.writeBytes(data);
        requestReconnectWorldMap result = new requestReconnectWorldMap(flat_requestReconnectWorldMap.getRootAsflat_requestReconnectWorldMap( readData.nioBuffer() ) );
        readData.release();
        return result;
    }

}