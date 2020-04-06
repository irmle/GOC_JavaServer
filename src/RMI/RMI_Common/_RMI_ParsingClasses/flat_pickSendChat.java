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
public final class flat_pickSendChat extends Table {
  public static flat_pickSendChat getRootAsflat_pickSendChat(ByteBuffer _bb) { return getRootAsflat_pickSendChat(_bb, new flat_pickSendChat()); }
  public static flat_pickSendChat getRootAsflat_pickSendChat(ByteBuffer _bb, flat_pickSendChat obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_pickSendChat __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String googleIDToken() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer googleIDTokenAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer googleIDTokenInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public String chatMessage() { int o = __offset(6); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer chatMessageAsByteBuffer() { return __vector_as_bytebuffer(6, 1); }
  public ByteBuffer chatMessageInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 6, 1); }

  public static int createflat_pickSendChat(FlatBufferBuilder builder,
      int googleIDTokenOffset,
      int chatMessageOffset) {
    builder.startObject(2);
    flat_pickSendChat.addChatMessage(builder, chatMessageOffset);
    flat_pickSendChat.addGoogleIDToken(builder, googleIDTokenOffset);
    return flat_pickSendChat.endflat_pickSendChat(builder);
  }

  public static void startflat_pickSendChat(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addGoogleIDToken(FlatBufferBuilder builder, int googleIDTokenOffset) { builder.addOffset(0, googleIDTokenOffset, 0); }
  public static void addChatMessage(FlatBufferBuilder builder, int chatMessageOffset) { builder.addOffset(1, chatMessageOffset, 0); }
  public static int endflat_pickSendChat(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_pickSendChat(FlatBufferBuilder builder,
 pickSendChat data) {
        int googleIDTokenOffset = builder.createString(data.googleIDToken);
        int chatMessageOffset = builder.createString(data.chatMessage);
        return createflat_pickSendChat(builder , googleIDTokenOffset, chatMessageOffset);
    }

    public static byte[] createflat_pickSendChat(pickSendChat data) {
        FlatBufferBuilder fbb = PooledFlatBufferBuilder.DEFAULT.poll();
        fbb.finish(flat_pickSendChat.createflat_pickSendChat(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb.clear(); PooledFlatBufferBuilder.DEFAULT.offer(fbb);
        return result;
    }

    public static pickSendChat getRootAsflat_pickSendChat(byte[] data) {
        ByteBuf readData = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
        readData.writeBytes(data);
        pickSendChat result = new pickSendChat(flat_pickSendChat.getRootAsflat_pickSendChat( readData.nioBuffer() ) );
        readData.release();
        return result;
    }

}