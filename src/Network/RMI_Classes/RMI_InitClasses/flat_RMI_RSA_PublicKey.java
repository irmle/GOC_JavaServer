// automatically generated by the FlatBuffers compiler, do not modify

package Network.RMI_Classes.RMI_InitClasses;

import java.nio.*;
import java.lang.*;

import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class flat_RMI_RSA_PublicKey extends Table {
  public static flat_RMI_RSA_PublicKey getRootAsflat_RMI_RSA_PublicKey(ByteBuffer _bb) { return getRootAsflat_RMI_RSA_PublicKey(_bb, new flat_RMI_RSA_PublicKey()); }
  public static flat_RMI_RSA_PublicKey getRootAsflat_RMI_RSA_PublicKey(ByteBuffer _bb, flat_RMI_RSA_PublicKey obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_RMI_RSA_PublicKey __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String base64EncodedPublicKey() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer base64EncodedPublicKeyAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer base64EncodedPublicKeyInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }

  public static int createflat_RMI_RSA_PublicKey(FlatBufferBuilder builder,
      int base64Encoded_publicKeyOffset) {
    builder.startObject(1);
    flat_RMI_RSA_PublicKey.addBase64EncodedPublicKey(builder, base64Encoded_publicKeyOffset);
    return flat_RMI_RSA_PublicKey.endflat_RMI_RSA_PublicKey(builder);
  }

  public static void startflat_RMI_RSA_PublicKey(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addBase64EncodedPublicKey(FlatBufferBuilder builder, int base64EncodedPublicKeyOffset) { builder.addOffset(0, base64EncodedPublicKeyOffset, 0); }
  public static int endflat_RMI_RSA_PublicKey(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_RMI_RSA_PublicKey(FlatBufferBuilder builder,
 RMI_RSA_PublicKey data) {
        int base64Encoded_publicKeyOffset = builder.createString(data.base64Encoded_publicKey);
        return createflat_RMI_RSA_PublicKey(builder , base64Encoded_publicKeyOffset);
    }

    public static byte[] createflat_RMI_RSA_PublicKey(RMI_RSA_PublicKey data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_RMI_RSA_PublicKey.createflat_RMI_RSA_PublicKey(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static RMI_RSA_PublicKey getRootAsflat_RMI_RSA_PublicKey(byte[] data) {
        ByteBuffer readData = ByteBuffer.wrap(data);
        RMI_RSA_PublicKey result = new RMI_RSA_PublicKey(flat_RMI_RSA_PublicKey.getRootAsflat_RMI_RSA_PublicKey( readData ) );
        readData = null;
        return result;
    }
}