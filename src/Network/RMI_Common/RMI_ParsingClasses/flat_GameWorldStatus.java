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
public final class flat_GameWorldStatus extends Table {
  public static flat_GameWorldStatus getRootAsflat_GameWorldStatus(ByteBuffer _bb) { return getRootAsflat_GameWorldStatus(_bb, new flat_GameWorldStatus()); }
  public static flat_GameWorldStatus getRootAsflat_GameWorldStatus(ByteBuffer _bb, flat_GameWorldStatus obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_GameWorldStatus __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public long currentGameworldTime() { int o = __offset(4); return o != 0 ? bb.getLong(o + bb_pos) : 0L; }
  public int entireWaveMobCount() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int currentAliveMobCount() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_GameWorldStatus(FlatBufferBuilder builder,
      long currentGameworldTime,
      int entireWaveMobCount,
      int currentAliveMobCount) {
    builder.startObject(3);
    flat_GameWorldStatus.addCurrentGameworldTime(builder, currentGameworldTime);
    flat_GameWorldStatus.addCurrentAliveMobCount(builder, currentAliveMobCount);
    flat_GameWorldStatus.addEntireWaveMobCount(builder, entireWaveMobCount);
    return flat_GameWorldStatus.endflat_GameWorldStatus(builder);
  }

  public static void startflat_GameWorldStatus(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addCurrentGameworldTime(FlatBufferBuilder builder, long currentGameworldTime) { builder.addLong(0, currentGameworldTime, 0L); }
  public static void addEntireWaveMobCount(FlatBufferBuilder builder, int entireWaveMobCount) { builder.addInt(1, entireWaveMobCount, 0); }
  public static void addCurrentAliveMobCount(FlatBufferBuilder builder, int currentAliveMobCount) { builder.addInt(2, currentAliveMobCount, 0); }
  public static int endflat_GameWorldStatus(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_GameWorldStatus(FlatBufferBuilder builder,
 GameWorldStatus data) {
        return createflat_GameWorldStatus(builder , data.currentGameworldTime, data.entireWaveMobCount, data.currentAliveMobCount);
    }

    public static byte[] createflat_GameWorldStatus(GameWorldStatus data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_GameWorldStatus.createflat_GameWorldStatus(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static GameWorldStatus getRootAsflat_GameWorldStatus(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        GameWorldStatus result = new GameWorldStatus(flat_GameWorldStatus.getRootAsflat_GameWorldStatus( buf ) );
        buf = null;
        return result;
    }

}