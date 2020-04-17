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
public final class flat_createWorldMapAttackTurretEntityInfo extends Table {
  public static flat_createWorldMapAttackTurretEntityInfo getRootAsflat_createWorldMapAttackTurretEntityInfo(ByteBuffer _bb) { return getRootAsflat_createWorldMapAttackTurretEntityInfo(_bb, new flat_createWorldMapAttackTurretEntityInfo()); }
  public static flat_createWorldMapAttackTurretEntityInfo getRootAsflat_createWorldMapAttackTurretEntityInfo(ByteBuffer _bb, flat_createWorldMapAttackTurretEntityInfo obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_createWorldMapAttackTurretEntityInfo __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public flat_AttackTurretData attackTurretList(int j) { return attackTurretList(new flat_AttackTurretData(), j); }
  public flat_AttackTurretData attackTurretList(flat_AttackTurretData obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int attackTurretListLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createflat_createWorldMapAttackTurretEntityInfo(FlatBufferBuilder builder,
      int attackTurretListOffset) {
    builder.startObject(1);
    flat_createWorldMapAttackTurretEntityInfo.addAttackTurretList(builder, attackTurretListOffset);
    return flat_createWorldMapAttackTurretEntityInfo.endflat_createWorldMapAttackTurretEntityInfo(builder);
  }

  public static void startflat_createWorldMapAttackTurretEntityInfo(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addAttackTurretList(FlatBufferBuilder builder, int attackTurretListOffset) { builder.addOffset(0, attackTurretListOffset, 0); }
  public static int createAttackTurretListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startAttackTurretListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endflat_createWorldMapAttackTurretEntityInfo(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_createWorldMapAttackTurretEntityInfo(FlatBufferBuilder builder,
 createWorldMapAttackTurretEntityInfo data) {
        int size0 = data.attackTurretList.size();
        int[] attackTurretList_ = new int[size0];
        for (int x = 0; x < size0; x++) {
        AttackTurretData aa = data.attackTurretList.poll();
        attackTurretList_[x] = flat_AttackTurretData.createflat_AttackTurretData(builder, aa);
        }
        int attackTurretListOffset = flat_createWorldMapAttackTurretEntityInfo.createAttackTurretListVector(builder, attackTurretList_);
        return createflat_createWorldMapAttackTurretEntityInfo(builder , attackTurretListOffset);
    }

    public static byte[] createflat_createWorldMapAttackTurretEntityInfo(createWorldMapAttackTurretEntityInfo data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_createWorldMapAttackTurretEntityInfo.createflat_createWorldMapAttackTurretEntityInfo(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static createWorldMapAttackTurretEntityInfo getRootAsflat_createWorldMapAttackTurretEntityInfo(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        createWorldMapAttackTurretEntityInfo result = new createWorldMapAttackTurretEntityInfo(flat_createWorldMapAttackTurretEntityInfo.getRootAsflat_createWorldMapAttackTurretEntityInfo( buf ) );
        buf = null;
        return result;
    }

}