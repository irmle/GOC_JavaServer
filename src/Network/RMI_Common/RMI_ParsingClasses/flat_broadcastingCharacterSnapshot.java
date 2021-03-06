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
public final class flat_broadcastingCharacterSnapshot extends Table {
  public static flat_broadcastingCharacterSnapshot getRootAsflat_broadcastingCharacterSnapshot(ByteBuffer _bb) { return getRootAsflat_broadcastingCharacterSnapshot(_bb, new flat_broadcastingCharacterSnapshot()); }
  public static flat_broadcastingCharacterSnapshot getRootAsflat_broadcastingCharacterSnapshot(ByteBuffer _bb, flat_broadcastingCharacterSnapshot obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_broadcastingCharacterSnapshot __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public flat_CharacterData characterSnapshot(int j) { return characterSnapshot(new flat_CharacterData(), j); }
  public flat_CharacterData characterSnapshot(flat_CharacterData obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int characterSnapshotLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createflat_broadcastingCharacterSnapshot(FlatBufferBuilder builder,
      int characterSnapshotOffset) {
    builder.startObject(1);
    flat_broadcastingCharacterSnapshot.addCharacterSnapshot(builder, characterSnapshotOffset);
    return flat_broadcastingCharacterSnapshot.endflat_broadcastingCharacterSnapshot(builder);
  }

  public static void startflat_broadcastingCharacterSnapshot(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addCharacterSnapshot(FlatBufferBuilder builder, int characterSnapshotOffset) { builder.addOffset(0, characterSnapshotOffset, 0); }
  public static int createCharacterSnapshotVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startCharacterSnapshotVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endflat_broadcastingCharacterSnapshot(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_broadcastingCharacterSnapshot(FlatBufferBuilder builder,
 broadcastingCharacterSnapshot data) {
        int size0 = data.characterSnapshot.size();
        int[] characterSnapshot_ = new int[size0];
        for (int x = 0; x < size0; x++) {
        CharacterData aa = data.characterSnapshot.poll();
        characterSnapshot_[x] = flat_CharacterData.createflat_CharacterData(builder, aa);
        }
        int characterSnapshotOffset = flat_broadcastingCharacterSnapshot.createCharacterSnapshotVector(builder, characterSnapshot_);
        return createflat_broadcastingCharacterSnapshot(builder , characterSnapshotOffset);
    }

    public static byte[] createflat_broadcastingCharacterSnapshot(broadcastingCharacterSnapshot data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_broadcastingCharacterSnapshot.createflat_broadcastingCharacterSnapshot(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static broadcastingCharacterSnapshot getRootAsflat_broadcastingCharacterSnapshot(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        broadcastingCharacterSnapshot result = new broadcastingCharacterSnapshot(flat_broadcastingCharacterSnapshot.getRootAsflat_broadcastingCharacterSnapshot( buf ) );
        buf = null;
        return result;
    }

}