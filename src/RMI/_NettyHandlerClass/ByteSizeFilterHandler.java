package RMI._NettyHandlerClass;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ByteSizeFilterHandler extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> list) throws Exception {

        //헤더부분(12byte)도 다 못읽어왔을 경우!
        if(msg.readableBytes()<12)
        {
            return; //다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
        }

        //앞으로 받아올 패킷크기를 불러온다!
        int packet_size = msg.getIntLE(0);

        //packet크기가 0인 경우, 부하가 커서 못받아온것이므로 다음번에 받아오게끔 패스할것.
        if(packet_size == 0)
            return;

        //정해진 헤더규격이 아닌, 비정상적인 데이터 도달하였다면.
        if( !(0 < packet_size && packet_size < 524288) )
        {
            System.out.println("[ByteSizeFilterHandler] Packet손상!! packet_size="+packet_size+"");

            //연결 종료
            ctx.channel().close();

            return;
        }

        //아직 버퍼에 데이터가 충분하지 않다면 다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
        if (msg.readableBytes() < packet_size + 12)
        {
            return; //다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
        }
        //패킷이 뭉쳐서 데이터를 더 받아왔을 경우! 원래 받아야 할 부분만 받고, 나머지는 다시 되돌려서 뒤이어 오는 패킷과 합쳐서 받을 것!
        else if(msg.readableBytes() > packet_size + 12)
        {
            //받아야 할 부분만 먼저 읽어온다.
            ByteBuf perfect_packet = msg.readBytes(packet_size + 12);

            //완성된 패킷을 다음 처리 핸들러로 넘긴다.
            ctx.fireChannelRead(perfect_packet);

            //읽은부분은 버리고, 남은 부분의 데이터를 readerIndex 0 의 위치로 땡겨준다.
            msg.discardReadBytes();

            //다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
            return;
        }
        else //정확한 데이터일 경우. [msg.readableBytes() == packet_size + 12]
        {
            //msg에서 해당 길이만큼 정확하게 읽어온다.
            ByteBuf perfect_packet = msg.readBytes(packet_size + 12);

            //완성된 패킷을 다음 처리 핸들러로 넘긴다.
            ctx.fireChannelRead(perfect_packet);

            //읽은부분은 버린다.
            msg.clear();

            //다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
            return;
        }
    }
}