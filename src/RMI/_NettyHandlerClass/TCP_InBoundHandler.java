package RMI._NettyHandlerClass;

import RMI.RMI_;
import RMI.RMI_Classes.RMI_ID;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;


public class TCP_InBoundHandler extends ChannelInboundHandlerAdapter
{
    //Netty Channel에 RMI_ID객체를 세팅하기 위한 AttributeKey 객체.
    private static final AttributeKey<RMI_ID> AttrKey_TCP = AttributeKey.newInstance("TCP_InBoundHandler");

    //Channel의 AttributeKey에 RMI_ID객체를 세팅하기 위한 AttributeKey 호출 메소드.
    public static AttributeKey<RMI_ID> getAttrKey()
    {
        return AttrKey_TCP;
    }

    //채널이 처음으로 Netty의 EventLoop에 등록되려고 할때 호출. 그 후 활성화 되면 이어서 channelActive가 호출됨.
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channelRegistered ");
    }

    //채널이 최종적으로 Netty의 EventLoop에서 제거되었을 때 호출.
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channelUnregistered ");
    }

    //채널이 등록되고 실제로 통신이 가능해지는 시점. 이때 로그인이 완료되었다고 판단하고 처리한다.
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //유저가 접속했을때.
        RMI_.OnConnected(ctx);
    }

    //막 Disconnect된 시점. 아직 EventLoop에 남아있지만 아무런 조치를 취하지 않을 경우 channelUnregistered로 이행하며 완전 제거됨.
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        //유저가 접속을 끊었을때.
        RMI_.OnDisConnected(ctx);
    }

    //데이터를 한창 주고받을 때 호출이 되는 지점.
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ByteBuf receive_data = ((ByteBuf) msg);

        //앞으로 받아올 패킷크기를 불러온다!
        int packet_size = receive_data.getIntLE(0);

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

        //사용한 버퍼를 반환한다!
        if(receive_data.refCnt()>0)
            ReferenceCountUtil.release(receive_data, receive_data.refCnt());

        //RMI 수신로직 처리 부분.
        RMI_.recvByte(rmi_id, rmi_ctx, packet_type, packet_data, null, ctx); //TCP데이터이므로 UDP Data는 null값!
    } //channelRead 종료 부분!


    //에러 발생시 처리!
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause. printStackTrace();

        //ctx.channel().close();
        System.out.println("exceptionCaught 발생! 현재 연결된 채널 종료! \n"+cause.toString());
    }
} //class 종료 부분!

