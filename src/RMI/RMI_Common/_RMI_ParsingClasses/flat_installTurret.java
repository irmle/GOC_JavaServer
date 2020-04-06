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
public final class flat_installTurret extends Table {
  public static flat_installTurret getRootAsflat_installTurret(ByteBuffer _bb) { return getRootAsflat_installTurret(_bb, new flat_installTurret()); }
  public static flat_installTurret getRootAsflat_installTurret(ByteBuffer _bb, flat_installTurret obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_installTurret __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int worldMapID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int userEntityID() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int turretType() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int areaNumber() { int o = __offset(10); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_installTurret(FlatBufferBuilder builder,
      int worldMapID,
      int userEntityID,
      int turretType,
      int areaNumber) {
    builder.startObject(4);
    flat_installTurret.addAreaNumber(builder, areaNumber);
    flat_installTurret.addTurretType(builder, turretType);
    flat_installTurret.addUserEntityID(builder, userEntityID);
    flat_installTurret.addWorldMapID(builder, worldMapID);
    return flat_installTurret.endflat_installTurret(builder);
  }

  public static void startflat_installTurret(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addWorldMapID(FlatBufferBuilder builder, int worldMapID) { builder.addInt(0, worldMapID, 0); }
  public static void addUserEntityID(FlatBufferBuilder builder, int userEntityID) { builder.addInt(1, userEntityID, 0); }
  public static void addTurretType(FlatBufferBuilder builder, int turretType) { builder.addInt(2, turretType, 0); }
  public static void addAreaNumber(FlatBufferBuilder builder, int areaNumber) { builder.addInt(3, areaNumber, 0); }
  public static int endflat_installTurret(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_installTurret(FlatBufferBuilder builder,
 installTurret data) {
        return createflat_installTurret(builder , data.worldMapID, data.userEntityID, data.turretType, data.areaNumber);
    }

    public static byte[] createflat_installTurret(installTurret data) {
        FlatBufferBuilder fbb = PooledFlatBufferBuilder.DEFAULT.poll();
        fbb.finish(flat_installTurret.createflat_installTurret(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb.clear(); PooledFlatBufferBuilder.DEFAULT.offer(fbb);
        return result;
    }

    public static installTurret getRootAsflat_installTurret(byte[] data) {
        ByteBuf readData = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
        readData.writeBytes(data);
        installTurret result = new installTurret(flat_installTurret.getRootAsflat_installTurret( readData.nioBuffer() ) );
        readData.release();
        return result;
    }

}