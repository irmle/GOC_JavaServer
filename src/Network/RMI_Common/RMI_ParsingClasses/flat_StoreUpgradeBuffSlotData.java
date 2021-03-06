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
public final class flat_StoreUpgradeBuffSlotData extends Table {
  public static flat_StoreUpgradeBuffSlotData getRootAsflat_StoreUpgradeBuffSlotData(ByteBuffer _bb) { return getRootAsflat_StoreUpgradeBuffSlotData(_bb, new flat_StoreUpgradeBuffSlotData()); }
  public static flat_StoreUpgradeBuffSlotData getRootAsflat_StoreUpgradeBuffSlotData(ByteBuffer _bb, flat_StoreUpgradeBuffSlotData obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_StoreUpgradeBuffSlotData __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int slotNum() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int upgradeType() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int upgradeLevel() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_StoreUpgradeBuffSlotData(FlatBufferBuilder builder,
      int slotNum,
      int upgradeType,
      int upgradeLevel) {
    builder.startObject(3);
    flat_StoreUpgradeBuffSlotData.addUpgradeLevel(builder, upgradeLevel);
    flat_StoreUpgradeBuffSlotData.addUpgradeType(builder, upgradeType);
    flat_StoreUpgradeBuffSlotData.addSlotNum(builder, slotNum);
    return flat_StoreUpgradeBuffSlotData.endflat_StoreUpgradeBuffSlotData(builder);
  }

  public static void startflat_StoreUpgradeBuffSlotData(FlatBufferBuilder builder) { builder.startObject(3); }
  public static void addSlotNum(FlatBufferBuilder builder, int slotNum) { builder.addInt(0, slotNum, 0); }
  public static void addUpgradeType(FlatBufferBuilder builder, int upgradeType) { builder.addInt(1, upgradeType, 0); }
  public static void addUpgradeLevel(FlatBufferBuilder builder, int upgradeLevel) { builder.addInt(2, upgradeLevel, 0); }
  public static int endflat_StoreUpgradeBuffSlotData(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_StoreUpgradeBuffSlotData(FlatBufferBuilder builder,
 StoreUpgradeBuffSlotData data) {
        return createflat_StoreUpgradeBuffSlotData(builder , data.slotNum, data.upgradeType, data.upgradeLevel);
    }

    public static byte[] createflat_StoreUpgradeBuffSlotData(StoreUpgradeBuffSlotData data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_StoreUpgradeBuffSlotData.createflat_StoreUpgradeBuffSlotData(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static StoreUpgradeBuffSlotData getRootAsflat_StoreUpgradeBuffSlotData(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        StoreUpgradeBuffSlotData result = new StoreUpgradeBuffSlotData(flat_StoreUpgradeBuffSlotData.getRootAsflat_StoreUpgradeBuffSlotData( buf ) );
        buf = null;
        return result;
    }

}