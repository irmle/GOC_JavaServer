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
public final class flat_userCharacterDefeat extends Table {
  public static flat_userCharacterDefeat getRootAsflat_userCharacterDefeat(ByteBuffer _bb) { return getRootAsflat_userCharacterDefeat(_bb, new flat_userCharacterDefeat()); }
  public static flat_userCharacterDefeat getRootAsflat_userCharacterDefeat(ByteBuffer _bb, flat_userCharacterDefeat obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_userCharacterDefeat __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int userEntityID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int remainTimeMilliSeconds() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createflat_userCharacterDefeat(FlatBufferBuilder builder,
      int userEntityID,
      int remainTimeMilliSeconds) {
    builder.startObject(2);
    flat_userCharacterDefeat.addRemainTimeMilliSeconds(builder, remainTimeMilliSeconds);
    flat_userCharacterDefeat.addUserEntityID(builder, userEntityID);
    return flat_userCharacterDefeat.endflat_userCharacterDefeat(builder);
  }

  public static void startflat_userCharacterDefeat(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addUserEntityID(FlatBufferBuilder builder, int userEntityID) { builder.addInt(0, userEntityID, 0); }
  public static void addRemainTimeMilliSeconds(FlatBufferBuilder builder, int remainTimeMilliSeconds) { builder.addInt(1, remainTimeMilliSeconds, 0); }
  public static int endflat_userCharacterDefeat(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_userCharacterDefeat(FlatBufferBuilder builder,
 userCharacterDefeat data) {
        return createflat_userCharacterDefeat(builder , data.userEntityID, data.remainTimeMilliSeconds);
    }

    public static byte[] createflat_userCharacterDefeat(userCharacterDefeat data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_userCharacterDefeat.createflat_userCharacterDefeat(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static userCharacterDefeat getRootAsflat_userCharacterDefeat(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        userCharacterDefeat result = new userCharacterDefeat(flat_userCharacterDefeat.getRootAsflat_userCharacterDefeat( buf ) );
        buf = null;
        return result;
    }

}