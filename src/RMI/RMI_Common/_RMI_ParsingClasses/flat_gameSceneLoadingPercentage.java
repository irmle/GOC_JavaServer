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
public final class flat_gameSceneLoadingPercentage extends Table {
  public static flat_gameSceneLoadingPercentage getRootAsflat_gameSceneLoadingPercentage(ByteBuffer _bb) { return getRootAsflat_gameSceneLoadingPercentage(_bb, new flat_gameSceneLoadingPercentage()); }
  public static flat_gameSceneLoadingPercentage getRootAsflat_gameSceneLoadingPercentage(ByteBuffer _bb, flat_gameSceneLoadingPercentage obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_gameSceneLoadingPercentage __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int worldMapID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float percentage() { int o = __offset(6); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }

  public static int createflat_gameSceneLoadingPercentage(FlatBufferBuilder builder,
      int worldMapID,
      float percentage) {
    builder.startObject(2);
    flat_gameSceneLoadingPercentage.addPercentage(builder, percentage);
    flat_gameSceneLoadingPercentage.addWorldMapID(builder, worldMapID);
    return flat_gameSceneLoadingPercentage.endflat_gameSceneLoadingPercentage(builder);
  }

  public static void startflat_gameSceneLoadingPercentage(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addWorldMapID(FlatBufferBuilder builder, int worldMapID) { builder.addInt(0, worldMapID, 0); }
  public static void addPercentage(FlatBufferBuilder builder, float percentage) { builder.addFloat(1, percentage, 0.0f); }
  public static int endflat_gameSceneLoadingPercentage(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_gameSceneLoadingPercentage(FlatBufferBuilder builder,
 gameSceneLoadingPercentage data) {
        return createflat_gameSceneLoadingPercentage(builder , data.worldMapID, data.percentage);
    }

    public static byte[] createflat_gameSceneLoadingPercentage(gameSceneLoadingPercentage data) {
        FlatBufferBuilder fbb = PooledFlatBufferBuilder.DEFAULT.poll();
        fbb.finish(flat_gameSceneLoadingPercentage.createflat_gameSceneLoadingPercentage(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb.clear(); PooledFlatBufferBuilder.DEFAULT.offer(fbb);
        return result;
    }

    public static gameSceneLoadingPercentage getRootAsflat_gameSceneLoadingPercentage(byte[] data) {
        ByteBuf readData = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
        readData.writeBytes(data);
        gameSceneLoadingPercentage result = new gameSceneLoadingPercentage(flat_gameSceneLoadingPercentage.getRootAsflat_gameSceneLoadingPercentage( readData.nioBuffer() ) );
        readData.release();
        return result;
    }

}