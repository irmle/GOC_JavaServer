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
public final class flat_broadcastingBuildSlotSnapshot extends Table {
  public static flat_broadcastingBuildSlotSnapshot getRootAsflat_broadcastingBuildSlotSnapshot(ByteBuffer _bb) { return getRootAsflat_broadcastingBuildSlotSnapshot(_bb, new flat_broadcastingBuildSlotSnapshot()); }
  public static flat_broadcastingBuildSlotSnapshot getRootAsflat_broadcastingBuildSlotSnapshot(ByteBuffer _bb, flat_broadcastingBuildSlotSnapshot obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_broadcastingBuildSlotSnapshot __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public flat_BuildSlotData buildSlotList(int j) { return buildSlotList(new flat_BuildSlotData(), j); }
  public flat_BuildSlotData buildSlotList(flat_BuildSlotData obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int buildSlotListLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createflat_broadcastingBuildSlotSnapshot(FlatBufferBuilder builder,
      int buildSlotListOffset) {
    builder.startObject(1);
    flat_broadcastingBuildSlotSnapshot.addBuildSlotList(builder, buildSlotListOffset);
    return flat_broadcastingBuildSlotSnapshot.endflat_broadcastingBuildSlotSnapshot(builder);
  }

  public static void startflat_broadcastingBuildSlotSnapshot(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addBuildSlotList(FlatBufferBuilder builder, int buildSlotListOffset) { builder.addOffset(0, buildSlotListOffset, 0); }
  public static int createBuildSlotListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startBuildSlotListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endflat_broadcastingBuildSlotSnapshot(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_broadcastingBuildSlotSnapshot(FlatBufferBuilder builder,
 broadcastingBuildSlotSnapshot data) {
        int size0 = data.buildSlotList.size();
        int[] buildSlotList_ = new int[size0];
        for (int x = 0; x < size0; x++) {
        BuildSlotData aa = data.buildSlotList.poll();
        buildSlotList_[x] = flat_BuildSlotData.createflat_BuildSlotData(builder, aa);
        }
        int buildSlotListOffset = flat_broadcastingBuildSlotSnapshot.createBuildSlotListVector(builder, buildSlotList_);
        return createflat_broadcastingBuildSlotSnapshot(builder , buildSlotListOffset);
    }

    public static byte[] createflat_broadcastingBuildSlotSnapshot(broadcastingBuildSlotSnapshot data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_broadcastingBuildSlotSnapshot.createflat_broadcastingBuildSlotSnapshot(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static broadcastingBuildSlotSnapshot getRootAsflat_broadcastingBuildSlotSnapshot(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        broadcastingBuildSlotSnapshot result = new broadcastingBuildSlotSnapshot(flat_broadcastingBuildSlotSnapshot.getRootAsflat_broadcastingBuildSlotSnapshot( buf ) );
        buf = null;
        return result;
    }

}