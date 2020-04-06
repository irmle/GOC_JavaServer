package RMI._NettyHandlerClass;

import RMI.RMI_;
import RMI.RMI_Classes.RMI_ID;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

public class UDP_InBoundHandler extends ChannelInboundHandlerAdapter
{
    //Netty Channel에 RMI_ID객체를 세팅하기 위한 AttributeKey 객체.
    private static final AttributeKey<RMI_ID> AttrKey_UDP = AttributeKey.newInstance("UDP_InBoundHandler");

    //Channel의 AttributeKey에 RMI_ID객체를 세팅하기 위한 AttributeKey 호출 메소드.
    public static AttributeKey<RMI_ID> getAttrKey()
    {
        return AttrKey_UDP;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("UDP channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("UDP channelUnregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("UDP channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("UDP channelInactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        DatagramPacket received_datagram = ((DatagramPacket)msg);

        //System.out.println("received_datagram = "+received_datagram.sender()+"/"+received_datagram.recipient());

        //바이트 버퍼풀을 사용하여 메모리 절약!
        ByteBuf receive_data = received_datagram.content();

        //앞으로 받아올 패킷크기를 불러온다!
        int packet_size = receive_data.getIntLE(0);

        if(65535 < packet_size || packet_size < 0 || (packet_size + 12) != receive_data.readableBytes() )
        {
            System.out.println("[UDP_InBoundHandler] Packet손상!! packet_size="+packet_size+" / receive_data.readableBytes()="+receive_data.readableBytes());

            //사용된 메모리를 반환한다!
            received_datagram.release();
            return;
        }

        //데이터 파싱!
        //rmi host id(int)
        int rmi_id = receive_data.getIntLE(4);
        //rmi context (short)
        short rmi_ctx = receive_data.getShortLE(8);
        //rmi packet type (short)
        short packet_type = receive_data.getShortLE(10);
        //받아온 데이터.
        byte[] packet_data = new byte[packet_size];
        receive_data.getBytes(12, packet_data);

        //RMI 수신로직 처리 부분.
        RMI_.recvByte(rmi_id, rmi_ctx, packet_type, packet_data, received_datagram, ctx);
    } //channelRead 종료 부분!

    //에러 발생시 처리!
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        //ctx.channel().close();
        System.out.println("exceptionCaught 발생! 현재 연결된 채널 종료! \n"+cause.toString());
    }
} //class 종료 부분!
