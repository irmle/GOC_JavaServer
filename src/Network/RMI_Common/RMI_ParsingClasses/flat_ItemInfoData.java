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
public final class flat_ItemInfoData extends Table {
  public static flat_ItemInfoData getRootAsflat_ItemInfoData(ByteBuffer _bb) { return getRootAsflat_ItemInfoData(_bb, new flat_ItemInfoData()); }
  public static flat_ItemInfoData getRootAsflat_ItemInfoData(ByteBuffer _bb, flat_ItemInfoData obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_ItemInfoData __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int itemType() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_ItemInfoData(FlatBufferBuilder builder,
      int itemType) {
    builder.startObject(1);
    flat_ItemInfoData.addItemType(builder, itemType);
    return flat_ItemInfoData.endflat_ItemInfoData(builder);
  }

  public static void startflat_ItemInfoData(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addItemType(FlatBufferBuilder builder, int itemType) { builder.addInt(0, itemType, 0); }
  public static int endflat_ItemInfoData(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_ItemInfoData(FlatBufferBuilder builder,
 ItemInfoData data) {
        return createflat_ItemInfoData(builder , data.itemType);
    }

    public static byte[] createflat_ItemInfoData(ItemInfoData data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_ItemInfoData.createflat_ItemInfoData(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static ItemInfoData getRootAsflat_ItemInfoData(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        ItemInfoData result = new ItemInfoData(flat_ItemInfoData.getRootAsflat_ItemInfoData( buf ) );
        buf = null;
        return result;
    }

}