package Network._NettyHandlerClass;

import java.net.InetSocketAddress;

import Network.RMI;
import Network.RMI_Classes.RMI_ID;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;

public class UDP_InBoundHandler extends ChannelInboundHandlerAdapter
{
    //Netty Channel에 RMI_ID객체를 세팅하기 위한 AttributeKey 객체.
    private static final AttributeKey<RMI_ID> AttrKey_UDP = AttributeKey.newInstance("UDP_InBoundHandler");

    //Channel의 AttributeKey에 RMI_ID객체를 세팅하기 위한 AttributeKey 호출 메소드.
    public static AttributeKey<RMI_ID> getAttrKey()
    {
        return AttrKey_UDP;
    }

    //ChannelHandlerContext
    private ChannelHandlerContext ctx;

    //채널이 처음으로 Netty의 EventLoop에 등록되려고 할때 호출. 그 후 활성화 되면 이어서 channelActive가 호출됨.
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("UDP channelRegistered ");
        this.ctx = ctx;
    }

    //채널이 최종적으로 Netty의 EventLoop에서 제거되었을 때 호출.
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("UDP channelUnregistered ");
        this.ctx = null;
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

        //System.out.println("received_datagram = ["+received_datagram.sender()+"]["+received_datagram.recipient()+"]");

        //받아온 데이터그램에서 ByteBuf를 가져온다.
        ByteBuf receive_data = received_datagram.content();

        //앞으로 받아올 패킷크기를 불러온다!
        int packet_size = receive_data.readIntLE();

        //패킷 사이즈 체크
        if(32768 < packet_size || packet_size < 0)
        {
            //비정상적인 데이터를 보냈으므로 IPBan처리
            InetSocketAddress IllegalIPAddress = received_datagram.sender();
            IPFilterHandler.addIPBanList(IllegalIPAddress);

            //사용된 메모리를 반환한다!
            received_datagram.release();
            return;
        }

        //데이터 그램 깨짐 체크
        if((packet_size + 8) != receive_data.readableBytes())
        {
            System.out.println("[UDP_InBoundHandler] Packet손상!! (packet_size + 8) = " + (packet_size + 8) + " / readableBytes = " + receive_data.readableBytes());

            //사용된 메모리를 반환한다!
            received_datagram.release();
            return;
        }

        //헤더 데이터 파싱!
        //rmi host id(int)
        int rmi_id = receive_data.readIntLE();
        //rmi context (short)
        short rmi_ctx = receive_data.readShortLE();
        //rmi packet type (short)
        short packet_type = receive_data.readShortLE();

        //받아온 데이터.
        byte[] packet_data = new byte[packet_size];
        receive_data.readBytes(packet_data, 0, packet_size);

        //recvPacket recvPacket = new recvPacket(rmi_id, rmi_ctx, packet_type, packet_data, received_datagram.sender(), ctx);
        //testUserList.recvActorUDP.tell(recvPacket, null);

        //RMI 수신로직 처리 부분.
        RMI.recvByte(rmi_id, rmi_ctx, packet_type, packet_data, received_datagram.sender(), ctx);

        //사용한 버퍼를 반환한다!
        received_datagram.release();
    } //channelRead 종료 부분!

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();

        if(!channel.isWritable())
            System.out.println(channel.remoteAddress()+" / UDPchannel bytesBeforeWritable() = "+channel.bytesBeforeWritable());
        else
            System.out.println(channel.remoteAddress()+" / UDPchannel.isWritable(true)");

    }

    //에러 발생시 처리!
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        System.out.println("[UDP_InBoundHandler] exceptionCaught 발생! "+cause.toString());
    }

} //class 종료 부분!
