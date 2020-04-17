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
public final class flat_userDisconnected extends Table {
  public static flat_userDisconnected getRootAsflat_userDisconnected(ByteBuffer _bb) { return getRootAsflat_userDisconnected(_bb, new flat_userDisconnected()); }
  public static flat_userDisconnected getRootAsflat_userDisconnected(ByteBuffer _bb, flat_userDisconnected obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_userDisconnected __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int userEntityID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_userDisconnected(FlatBufferBuilder builder,
      int userEntityID) {
    builder.startObject(1);
    flat_userDisconnected.addUserEntityID(builder, userEntityID);
    return flat_userDisconnected.endflat_userDisconnected(builder);
  }

  public static void startflat_userDisconnected(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addUserEntityID(FlatBufferBuilder builder, int userEntityID) { builder.addInt(0, userEntityID, 0); }
  public static int endflat_userDisconnected(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_userDisconnected(FlatBufferBuilder builder,
 userDisconnected data) {
        return createflat_userDisconnected(builder , data.userEntityID);
    }

    public static byte[] createflat_userDisconnected(userDisconnected data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_userDisconnected.createflat_userDisconnected(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static userDisconnected getRootAsflat_userDisconnected(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        userDisconnected result = new userDisconnected(flat_userDisconnected.getRootAsflat_userDisconnected( buf ) );
        buf = null;
        return result;
    }

}