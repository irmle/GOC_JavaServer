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
public final class flat_useItem extends Table {
  public static flat_useItem getRootAsflat_useItem(ByteBuffer _bb) { return getRootAsflat_useItem(_bb, new flat_useItem()); }
  public static flat_useItem getRootAsflat_useItem(ByteBuffer _bb, flat_useItem obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_useItem __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int worldMapID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int userEntityID() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public short itemSlotNum() { int o = __offset(8); return o != 0 ? bb.getShort(o + bb_pos) : 0; }
  public short itemCount() { int o = __offset(10); return o != 0 ? bb.getShort(o + bb_pos) : 0; }

  public static int createflat_useItem(FlatBufferBuilder builder,
      int worldMapID,
      int userEntityID,
      short itemSlotNum,
      short itemCount) {
    builder.startObject(4);
    flat_useItem.addUserEntityID(builder, userEntityID);
    flat_useItem.addWorldMapID(builder, worldMapID);
    flat_useItem.addItemCount(builder, itemCount);
    flat_useItem.addItemSlotNum(builder, itemSlotNum);
    return flat_useItem.endflat_useItem(builder);
  }

  public static void startflat_useItem(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addWorldMapID(FlatBufferBuilder builder, int worldMapID) { builder.addInt(0, worldMapID, 0); }
  public static void addUserEntityID(FlatBufferBuilder builder, int userEntityID) { builder.addInt(1, userEntityID, 0); }
  public static void addItemSlotNum(FlatBufferBuilder builder, short itemSlotNum) { builder.addShort(2, itemSlotNum, 0); }
  public static void addItemCount(FlatBufferBuilder builder, short itemCount) { builder.addShort(3, itemCount, 0); }
  public static int endflat_useItem(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_useItem(FlatBufferBuilder builder,
 useItem data) {
        return createflat_useItem(builder , data.worldMapID, data.userEntityID, data.itemSlotNum, data.itemCount);
    }

    public static byte[] createflat_useItem(useItem data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_useItem.createflat_useItem(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static useItem getRootAsflat_useItem(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        useItem result = new useItem(flat_useItem.getRootAsflat_useItem( buf ) );
        buf = null;
        return result;
    }

}