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
public final class flat_userFailedUpgradeBuilding extends Table {
  public static flat_userFailedUpgradeBuilding getRootAsflat_userFailedUpgradeBuilding(ByteBuffer _bb) { return getRootAsflat_userFailedUpgradeBuilding(_bb, new flat_userFailedUpgradeBuilding()); }
  public static flat_userFailedUpgradeBuilding getRootAsflat_userFailedUpgradeBuilding(ByteBuffer _bb, flat_userFailedUpgradeBuilding obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_userFailedUpgradeBuilding __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int errorCode() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_userFailedUpgradeBuilding(FlatBufferBuilder builder,
      int errorCode) {
    builder.startObject(1);
    flat_userFailedUpgradeBuilding.addErrorCode(builder, errorCode);
    return flat_userFailedUpgradeBuilding.endflat_userFailedUpgradeBuilding(builder);
  }

  public static void startflat_userFailedUpgradeBuilding(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addErrorCode(FlatBufferBuilder builder, int errorCode) { builder.addInt(0, errorCode, 0); }
  public static int endflat_userFailedUpgradeBuilding(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_userFailedUpgradeBuilding(FlatBufferBuilder builder,
 userFailedUpgradeBuilding data) {
        return createflat_userFailedUpgradeBuilding(builder , data.errorCode);
    }

    public static byte[] createflat_userFailedUpgradeBuilding(userFailedUpgradeBuilding data) {
        FlatBufferBuilder fbb = PooledFlatBufferBuilder.DEFAULT.poll();
        fbb.finish(flat_userFailedUpgradeBuilding.createflat_userFailedUpgradeBuilding(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb.clear(); PooledFlatBufferBuilder.DEFAULT.offer(fbb);
        return result;
    }

    public static userFailedUpgradeBuilding getRootAsflat_userFailedUpgradeBuilding(byte[] data) {
        ByteBuf readData = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
        readData.writeBytes(data);
        userFailedUpgradeBuilding result = new userFailedUpgradeBuilding(flat_userFailedUpgradeBuilding.getRootAsflat_userFailedUpgradeBuilding( readData.nioBuffer() ) );
        readData.release();
        return result;
    }

}