// automatically generated by the FlatBuffers compiler, do not modify

package Network.RMI_Classes.RMI_InitClasses;

import java.nio.*;
import java.lang.*;

import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class flat_RMI_Send_EncryptedAES_Key extends Table {
  public static flat_RMI_Send_EncryptedAES_Key getRootAsflat_RMI_Send_EncryptedAES_Key(ByteBuffer _bb) { return getRootAsflat_RMI_Send_EncryptedAES_Key(_bb, new flat_RMI_Send_EncryptedAES_Key()); }
  public static flat_RMI_Send_EncryptedAES_Key getRootAsflat_RMI_Send_EncryptedAES_Key(ByteBuffer _bb, flat_RMI_Send_EncryptedAES_Key obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_RMI_Send_EncryptedAES_Key __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public byte RSAEncryptedAESKey(int j) { int o = __offset(4); return o != 0 ? bb.get(__vector(o) + j * 1) : 0; }
  public int RSAEncryptedAESKeyLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer RSAEncryptedAESKeyAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer RSAEncryptedAESKeyInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public byte RSAEncryptedAESIV(int j) { int o = __offset(6); return o != 0 ? bb.get(__vector(o) + j * 1) : 0; }
  public int RSAEncryptedAESIVLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public ByteBuffer RSAEncryptedAESIVAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer RSAEncryptedAESIVInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }

  public static int createflat_RMI_Send_EncryptedAES_Key(FlatBufferBuilder builder,
      int RSAEncrypted_AESKeyOffset,
      int RSAEncrypted_AESIVOffset) {
    builder.startObject(2);
    flat_RMI_Send_EncryptedAES_Key.addRSAEncryptedAESIV(builder, RSAEncrypted_AESIVOffset);
    flat_RMI_Send_EncryptedAES_Key.addRSAEncryptedAESKey(builder, RSAEncrypted_AESKeyOffset);
    return flat_RMI_Send_EncryptedAES_Key.endflat_RMI_Send_EncryptedAES_Key(builder);
  }

  public static void startflat_RMI_Send_EncryptedAES_Key(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addRSAEncryptedAESKey(FlatBufferBuilder builder, int RSAEncryptedAESKeyOffset) { builder.addOffset(0, RSAEncryptedAESKeyOffset, 0); }
  public static int createRSAEncryptedAESKeyVector(FlatBufferBuilder builder, byte[] data) { builder.startVector(1, data.length, 1); for (int i = data.length - 1; i >= 0; i--) builder.addByte(data[i]); return builder.endVector(); }
  public static void startRSAEncryptedAESKeyVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static void addRSAEncryptedAESIV(FlatBufferBuilder builder, int RSAEncryptedAESIVOffset) { builder.addOffset(1, RSAEncryptedAESIVOffset, 0); }
  public static int createRSAEncryptedAESIVVector(FlatBufferBuilder builder, byte[] data) { builder.startVector(1, data.length, 1); for (int i = data.length - 1; i >= 0; i--) builder.addByte(data[i]); return builder.endVector(); }
  public static void startRSAEncryptedAESIVVector(FlatBufferBuilder builder, int numElems) { builder.startVector(1, numElems, 1); }
  public static int endflat_RMI_Send_EncryptedAES_Key(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_RMI_Send_EncryptedAES_Key(FlatBufferBuilder builder,
 RMI_Send_EncryptedAES_Key data) {
        int RSAEncrypted_AESKeyOffset = createRSAEncryptedAESKeyVector(builder, data.RSAEncrypted_AESKey);
        int RSAEncrypted_AESIVOffset = createRSAEncryptedAESIVVector(builder, data.RSAEncrypted_AESIV);
        return createflat_RMI_Send_EncryptedAES_Key(builder , RSAEncrypted_AESKeyOffset, RSAEncrypted_AESIVOffset);
    }

    public static byte[] createflat_RMI_Send_EncryptedAES_Key(RMI_Send_EncryptedAES_Key data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_RMI_Send_EncryptedAES_Key.createflat_RMI_Send_EncryptedAES_Key(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static RMI_Send_EncryptedAES_Key getRootAsflat_RMI_Send_EncryptedAES_Key(byte[] data) {
        ByteBuffer readData = ByteBuffer.wrap(data);
        RMI_Send_EncryptedAES_Key result = new RMI_Send_EncryptedAES_Key(flat_RMI_Send_EncryptedAES_Key.getRootAsflat_RMI_Send_EncryptedAES_Key( readData ) );
        readData = null;
        return result;
    }

}