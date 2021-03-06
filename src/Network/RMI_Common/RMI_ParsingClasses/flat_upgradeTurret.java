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
public final class flat_upgradeTurret extends Table {
  public static flat_upgradeTurret getRootAsflat_upgradeTurret(ByteBuffer _bb) { return getRootAsflat_upgradeTurret(_bb, new flat_upgradeTurret()); }
  public static flat_upgradeTurret getRootAsflat_upgradeTurret(ByteBuffer _bb, flat_upgradeTurret obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_upgradeTurret __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int worldMapID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int userEntityID() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int turretEntityID() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int turretType() { int o = __offset(10); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_upgradeTurret(FlatBufferBuilder builder,
      int worldMapID,
      int userEntityID,
      int turretEntityID,
      int turretType) {
    builder.startObject(4);
    flat_upgradeTurret.addTurretType(builder, turretType);
    flat_upgradeTurret.addTurretEntityID(builder, turretEntityID);
    flat_upgradeTurret.addUserEntityID(builder, userEntityID);
    flat_upgradeTurret.addWorldMapID(builder, worldMapID);
    return flat_upgradeTurret.endflat_upgradeTurret(builder);
  }

  public static void startflat_upgradeTurret(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addWorldMapID(FlatBufferBuilder builder, int worldMapID) { builder.addInt(0, worldMapID, 0); }
  public static void addUserEntityID(FlatBufferBuilder builder, int userEntityID) { builder.addInt(1, userEntityID, 0); }
  public static void addTurretEntityID(FlatBufferBuilder builder, int turretEntityID) { builder.addInt(2, turretEntityID, 0); }
  public static void addTurretType(FlatBufferBuilder builder, int turretType) { builder.addInt(3, turretType, 0); }
  public static int endflat_upgradeTurret(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_upgradeTurret(FlatBufferBuilder builder,
 upgradeTurret data) {
        return createflat_upgradeTurret(builder , data.worldMapID, data.userEntityID, data.turretEntityID, data.turretType);
    }

    public static byte[] createflat_upgradeTurret(upgradeTurret data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_upgradeTurret.createflat_upgradeTurret(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static upgradeTurret getRootAsflat_upgradeTurret(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        upgradeTurret result = new upgradeTurret(flat_upgradeTurret.getRootAsflat_upgradeTurret( buf ) );
        buf = null;
        return result;
    }

}