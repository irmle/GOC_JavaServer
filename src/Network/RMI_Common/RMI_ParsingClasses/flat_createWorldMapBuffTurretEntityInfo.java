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
public final class flat_createWorldMapBuffTurretEntityInfo extends Table {
  public static flat_createWorldMapBuffTurretEntityInfo getRootAsflat_createWorldMapBuffTurretEntityInfo(ByteBuffer _bb) { return getRootAsflat_createWorldMapBuffTurretEntityInfo(_bb, new flat_createWorldMapBuffTurretEntityInfo()); }
  public static flat_createWorldMapBuffTurretEntityInfo getRootAsflat_createWorldMapBuffTurretEntityInfo(ByteBuffer _bb, flat_createWorldMapBuffTurretEntityInfo obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_createWorldMapBuffTurretEntityInfo __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public flat_BuffTurretData buffTurretList(int j) { return buffTurretList(new flat_BuffTurretData(), j); }
  public flat_BuffTurretData buffTurretList(flat_BuffTurretData obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int buffTurretListLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createflat_createWorldMapBuffTurretEntityInfo(FlatBufferBuilder builder,
      int buffTurretListOffset) {
    builder.startObject(1);
    flat_createWorldMapBuffTurretEntityInfo.addBuffTurretList(builder, buffTurretListOffset);
    return flat_createWorldMapBuffTurretEntityInfo.endflat_createWorldMapBuffTurretEntityInfo(builder);
  }

  public static void startflat_createWorldMapBuffTurretEntityInfo(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addBuffTurretList(FlatBufferBuilder builder, int buffTurretListOffset) { builder.addOffset(0, buffTurretListOffset, 0); }
  public static int createBuffTurretListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startBuffTurretListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endflat_createWorldMapBuffTurretEntityInfo(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_createWorldMapBuffTurretEntityInfo(FlatBufferBuilder builder,
 createWorldMapBuffTurretEntityInfo data) {
        int size0 = data.buffTurretList.size();
        int[] buffTurretList_ = new int[size0];
        for (int x = 0; x < size0; x++) {
        BuffTurretData aa = data.buffTurretList.poll();
        buffTurretList_[x] = flat_BuffTurretData.createflat_BuffTurretData(builder, aa);
        }
        int buffTurretListOffset = flat_createWorldMapBuffTurretEntityInfo.createBuffTurretListVector(builder, buffTurretList_);
        return createflat_createWorldMapBuffTurretEntityInfo(builder , buffTurretListOffset);
    }

    public static byte[] createflat_createWorldMapBuffTurretEntityInfo(createWorldMapBuffTurretEntityInfo data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_createWorldMapBuffTurretEntityInfo.createflat_createWorldMapBuffTurretEntityInfo(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static createWorldMapBuffTurretEntityInfo getRootAsflat_createWorldMapBuffTurretEntityInfo(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        createWorldMapBuffTurretEntityInfo result = new createWorldMapBuffTurretEntityInfo(flat_createWorldMapBuffTurretEntityInfo.getRootAsflat_createWorldMapBuffTurretEntityInfo( buf ) );
        buf = null;
        return result;
    }

}