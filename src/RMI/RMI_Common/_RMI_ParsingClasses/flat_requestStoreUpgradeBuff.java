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
public final class flat_requestStoreUpgradeBuff extends Table {
  public static flat_requestStoreUpgradeBuff getRootAsflat_requestStoreUpgradeBuff(ByteBuffer _bb) { return getRootAsflat_requestStoreUpgradeBuff(_bb, new flat_requestStoreUpgradeBuff()); }
  public static flat_requestStoreUpgradeBuff getRootAsflat_requestStoreUpgradeBuff(ByteBuffer _bb, flat_requestStoreUpgradeBuff obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_requestStoreUpgradeBuff __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int worldMapID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int userEntityID() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int storeUpgradeType() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int buffLevel() { int o = __offset(10); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_requestStoreUpgradeBuff(FlatBufferBuilder builder,
      int worldMapID,
      int userEntityID,
      int storeUpgradeType,
      int buffLevel) {
    builder.startObject(4);
    flat_requestStoreUpgradeBuff.addBuffLevel(builder, buffLevel);
    flat_requestStoreUpgradeBuff.addStoreUpgradeType(builder, storeUpgradeType);
    flat_requestStoreUpgradeBuff.addUserEntityID(builder, userEntityID);
    flat_requestStoreUpgradeBuff.addWorldMapID(builder, worldMapID);
    return flat_requestStoreUpgradeBuff.endflat_requestStoreUpgradeBuff(builder);
  }

  public static void startflat_requestStoreUpgradeBuff(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addWorldMapID(FlatBufferBuilder builder, int worldMapID) { builder.addInt(0, worldMapID, 0); }
  public static void addUserEntityID(FlatBufferBuilder builder, int userEntityID) { builder.addInt(1, userEntityID, 0); }
  public static void addStoreUpgradeType(FlatBufferBuilder builder, int storeUpgradeType) { builder.addInt(2, storeUpgradeType, 0); }
  public static void addBuffLevel(FlatBufferBuilder builder, int buffLevel) { builder.addInt(3, buffLevel, 0); }
  public static int endflat_requestStoreUpgradeBuff(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_requestStoreUpgradeBuff(FlatBufferBuilder builder,
 requestStoreUpgradeBuff data) {
        return createflat_requestStoreUpgradeBuff(builder , data.worldMapID, data.userEntityID, data.storeUpgradeType, data.buffLevel);
    }

    public static byte[] createflat_requestStoreUpgradeBuff(requestStoreUpgradeBuff data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_requestStoreUpgradeBuff.createflat_requestStoreUpgradeBuff(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static requestStoreUpgradeBuff getRootAsflat_requestStoreUpgradeBuff(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        requestStoreUpgradeBuff result = new requestStoreUpgradeBuff(flat_requestStoreUpgradeBuff.getRootAsflat_requestStoreUpgradeBuff( buf ) );
        buf = null;
        return result;
    }

}