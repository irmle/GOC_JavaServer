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
public final class flat_DestroyEntityData extends Table {
  public static flat_DestroyEntityData getRootAsflat_DestroyEntityData(ByteBuffer _bb) { return getRootAsflat_DestroyEntityData(_bb, new flat_DestroyEntityData()); }
  public static flat_DestroyEntityData getRootAsflat_DestroyEntityData(ByteBuffer _bb, flat_DestroyEntityData obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_DestroyEntityData __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public short entityType() { int o = __offset(4); return o != 0 ? bb.getShort(o + bb_pos) : 0; }
  public int destroyedEntityID() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_DestroyEntityData(FlatBufferBuilder builder,
      short entityType,
      int destroyedEntityID) {
    builder.startObject(2);
    flat_DestroyEntityData.addDestroyedEntityID(builder, destroyedEntityID);
    flat_DestroyEntityData.addEntityType(builder, entityType);
    return flat_DestroyEntityData.endflat_DestroyEntityData(builder);
  }

  public static void startflat_DestroyEntityData(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addEntityType(FlatBufferBuilder builder, short entityType) { builder.addShort(0, entityType, 0); }
  public static void addDestroyedEntityID(FlatBufferBuilder builder, int destroyedEntityID) { builder.addInt(1, destroyedEntityID, 0); }
  public static int endflat_DestroyEntityData(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_DestroyEntityData(FlatBufferBuilder builder,
 DestroyEntityData data) {
        return createflat_DestroyEntityData(builder , data.entityType, data.destroyedEntityID);
    }

    public static byte[] createflat_DestroyEntityData(DestroyEntityData data) {
        FlatBufferBuilder fbb = PooledFlatBufferBuilder.DEFAULT.poll();
        fbb.finish(flat_DestroyEntityData.createflat_DestroyEntityData(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb.clear(); PooledFlatBufferBuilder.DEFAULT.offer(fbb);
        return result;
    }

    public static DestroyEntityData getRootAsflat_DestroyEntityData(byte[] data) {
        ByteBuf readData = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
        readData.writeBytes(data);
        DestroyEntityData result = new DestroyEntityData(flat_DestroyEntityData.getRootAsflat_DestroyEntityData( readData.nioBuffer() ) );
        readData.release();
        return result;
    }

}