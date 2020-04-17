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
public final class flat_createWorldMapFlyingObjectEntityInfo extends Table {
  public static flat_createWorldMapFlyingObjectEntityInfo getRootAsflat_createWorldMapFlyingObjectEntityInfo(ByteBuffer _bb) { return getRootAsflat_createWorldMapFlyingObjectEntityInfo(_bb, new flat_createWorldMapFlyingObjectEntityInfo()); }
  public static flat_createWorldMapFlyingObjectEntityInfo getRootAsflat_createWorldMapFlyingObjectEntityInfo(ByteBuffer _bb, flat_createWorldMapFlyingObjectEntityInfo obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_createWorldMapFlyingObjectEntityInfo __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public flat_FlyingObjectData flyingObjectList(int j) { return flyingObjectList(new flat_FlyingObjectData(), j); }
  public flat_FlyingObjectData flyingObjectList(flat_FlyingObjectData obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int flyingObjectListLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createflat_createWorldMapFlyingObjectEntityInfo(FlatBufferBuilder builder,
      int flyingObjectListOffset) {
    builder.startObject(1);
    flat_createWorldMapFlyingObjectEntityInfo.addFlyingObjectList(builder, flyingObjectListOffset);
    return flat_createWorldMapFlyingObjectEntityInfo.endflat_createWorldMapFlyingObjectEntityInfo(builder);
  }

  public static void startflat_createWorldMapFlyingObjectEntityInfo(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addFlyingObjectList(FlatBufferBuilder builder, int flyingObjectListOffset) { builder.addOffset(0, flyingObjectListOffset, 0); }
  public static int createFlyingObjectListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startFlyingObjectListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endflat_createWorldMapFlyingObjectEntityInfo(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_createWorldMapFlyingObjectEntityInfo(FlatBufferBuilder builder,
 createWorldMapFlyingObjectEntityInfo data) {
        int size0 = data.flyingObjectList.size();
        int[] flyingObjectList_ = new int[size0];
        for (int x = 0; x < size0; x++) {
        FlyingObjectData aa = data.flyingObjectList.poll();
        flyingObjectList_[x] = flat_FlyingObjectData.createflat_FlyingObjectData(builder, aa);
        }
        int flyingObjectListOffset = flat_createWorldMapFlyingObjectEntityInfo.createFlyingObjectListVector(builder, flyingObjectList_);
        return createflat_createWorldMapFlyingObjectEntityInfo(builder , flyingObjectListOffset);
    }

    public static byte[] createflat_createWorldMapFlyingObjectEntityInfo(createWorldMapFlyingObjectEntityInfo data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_createWorldMapFlyingObjectEntityInfo.createflat_createWorldMapFlyingObjectEntityInfo(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static createWorldMapFlyingObjectEntityInfo getRootAsflat_createWorldMapFlyingObjectEntityInfo(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        createWorldMapFlyingObjectEntityInfo result = new createWorldMapFlyingObjectEntityInfo(flat_createWorldMapFlyingObjectEntityInfo.getRootAsflat_createWorldMapFlyingObjectEntityInfo( buf ) );
        buf = null;
        return result;
    }

}