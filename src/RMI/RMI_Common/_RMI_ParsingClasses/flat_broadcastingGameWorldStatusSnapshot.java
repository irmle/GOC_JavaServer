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
public final class flat_broadcastingGameWorldStatusSnapshot extends Table {
  public static flat_broadcastingGameWorldStatusSnapshot getRootAsflat_broadcastingGameWorldStatusSnapshot(ByteBuffer _bb) { return getRootAsflat_broadcastingGameWorldStatusSnapshot(_bb, new flat_broadcastingGameWorldStatusSnapshot()); }
  public static flat_broadcastingGameWorldStatusSnapshot getRootAsflat_broadcastingGameWorldStatusSnapshot(ByteBuffer _bb, flat_broadcastingGameWorldStatusSnapshot obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_broadcastingGameWorldStatusSnapshot __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public flat_GameWorldStatus gameworldStatusSnapshot() { return gameworldStatusSnapshot(new flat_GameWorldStatus()); }
  public flat_GameWorldStatus gameworldStatusSnapshot(flat_GameWorldStatus obj) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(o + bb_pos), bb) : null; }

  public static int createflat_broadcastingGameWorldStatusSnapshot(FlatBufferBuilder builder,
      int gameworldStatusSnapshotOffset) {
    builder.startObject(1);
    flat_broadcastingGameWorldStatusSnapshot.addGameworldStatusSnapshot(builder, gameworldStatusSnapshotOffset);
    return flat_broadcastingGameWorldStatusSnapshot.endflat_broadcastingGameWorldStatusSnapshot(builder);
  }

  public static void startflat_broadcastingGameWorldStatusSnapshot(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addGameworldStatusSnapshot(FlatBufferBuilder builder, int gameworldStatusSnapshotOffset) { builder.addOffset(0, gameworldStatusSnapshotOffset, 0); }
  public static int endflat_broadcastingGameWorldStatusSnapshot(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_broadcastingGameWorldStatusSnapshot(FlatBufferBuilder builder,
 broadcastingGameWorldStatusSnapshot data) {
        int gameworldStatusSnapshotOffset = flat_GameWorldStatus.createflat_GameWorldStatus(builder, data.gameworldStatusSnapshot);
        return createflat_broadcastingGameWorldStatusSnapshot(builder , gameworldStatusSnapshotOffset);
    }

    public static byte[] createflat_broadcastingGameWorldStatusSnapshot(broadcastingGameWorldStatusSnapshot data) {
        FlatBufferBuilder fbb = PooledFlatBufferBuilder.DEFAULT.poll();
        fbb.finish(flat_broadcastingGameWorldStatusSnapshot.createflat_broadcastingGameWorldStatusSnapshot(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb.clear(); PooledFlatBufferBuilder.DEFAULT.offer(fbb);
        return result;
    }

    public static broadcastingGameWorldStatusSnapshot getRootAsflat_broadcastingGameWorldStatusSnapshot(byte[] data) {
        ByteBuf readData = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
        readData.writeBytes(data);
        broadcastingGameWorldStatusSnapshot result = new broadcastingGameWorldStatusSnapshot(flat_broadcastingGameWorldStatusSnapshot.getRootAsflat_broadcastingGameWorldStatusSnapshot( readData.nioBuffer() ) );
        readData.release();
        return result;
    }

}