// automatically generated by the FlatBuffers compiler, do not modify

package Network.RMI_Common.RMI_ParsingClasses;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class flat_StartGame extends Table {
  public static flat_StartGame getRootAsflat_StartGame(ByteBuffer _bb) { return getRootAsflat_StartGame(_bb, new flat_StartGame()); }
  public static flat_StartGame getRootAsflat_StartGame(ByteBuffer _bb, flat_StartGame obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_StartGame __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }


  public static void startflat_StartGame(FlatBufferBuilder builder) { builder.startObject(0); }
  public static int endflat_StartGame(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

