package Network._NettyHandlerClass;

import java.net.InetSocketAddress;
import java.util.List;

import Network.RMI;
import Network.RMI_Classes.RMI_ID;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;

public class TCP_InBoundHandler extends ByteToMessageDecoder {

    //Netty Channel에 RMI_ID객체를 세팅하기 위한 AttributeKey 객체.
    private static final AttributeKey<RMI_ID> AttrKey_TCP = AttributeKey.newInstance("TCP_InBoundHandler");

    //Channel의 AttributeKey에 RMI_ID객체를 세팅하기 위한 AttributeKey 호출 메소드.
    public static AttributeKey<RMI_ID> getAttrKey()
    {
        return AttrKey_TCP;
    }

    //ChannelHandlerContext
    private ChannelHandlerContext ctx;

    //채널이 처음으로 Netty의 EventLoop에 등록되려고 할때 호출. 그 후 활성화 되면 이어서 channelActive가 호출됨.
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channelRegistered ");
        this.ctx = ctx;
    }

    //채널이 최종적으로 Netty의 EventLoop에서 제거되었을 때 호출.
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channelUnregistered ");
        this.ctx = null;
    }

    //채널이 등록되고 실제로 통신이 가능해지는 시점. 이때 로그인이 완료되었다고 판단하고 처리한다.
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //유저가 접속했을때.
        RMI.OnConnected(ctx);
    }

    //막 Disconnect된 시점. 아직 EventLoop에 남아있지만 아무런 조치를 취하지 않을 경우 channelUnregistered로 이행하며 완전 제거됨.
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        //유저가 접속을 끊었을때.
        RMI.OnDisConnected(ctx);
    }

    //데이터를 한창 주고받을 때 호출이 되는 지점.
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> list) {

        //헤더부분(12byte)도 다 못읽어왔을 경우!
        if(msg.readableBytes() < 12)
        {
            return; //다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
        }

        //readerIndex 저장.
        msg.markReaderIndex();

        //앞으로 받아올 패킷크기를 불러온다!
        int packet_size = msg.readIntLE(); //readerIndex 0 -> 4로 증가함.

        //비정상적인 데이터가 도달하였다면.
        if(!isAvailablePacketSize(packet_size))
        {
            //System.out.println("[TCP_InBoundHandler] Packet손상!! (packet_size + 12) = " + (packet_size + 12) + " / readableBytes = " + (msg.readableBytes() + 4));

            //비정상적인 데이터를 보냈으므로 IPBan처리
            InetSocketAddress IllegalIPAddress = (InetSocketAddress)ctx.channel().remoteAddress();
            IPFilterHandler.addIPBanList(IllegalIPAddress);

            //연결 종료
            ctx.close(); //채널이 닫힌다면 msg는 release 된다.
            return;
        }

        //아직 버퍼에 데이터가 충분하지 않다면 다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
        if (msg.readableBytes()< packet_size + 8)
        {
            //정상적으로 데이터를 다 읽은것이 아니므로 readerIndex 4 -> 0로 초기화함.
            msg.resetReaderIndex();
            return; //다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
        }
        //패킷을 완성할만큼 데이터를 받아온 경우
        else
        {
            //패킷 완성후, 다음 데이터가 없는지 체크
            if(!isCompleteSizePacket(packet_size, ctx, msg))
            {
                //다음 데이터가 없다면 다시 이어서 받는다.
                return;
            }
            else //발생해서는 안되는 부분.
            {
                System.out.println("Error!! isCompleteSizePacket() == true");
            }
        }
    } //decode 종료 부분!


    //패킷 완성후, 다음 데이터가 없는지 체크하는 함수.
    //패킷 완성후, 다시 자기자신을 호출하여 다음 데이터가 없을때까지 계속 자기자신을 호출한다.
    boolean isCompleteSizePacket(int packet_size, ChannelHandlerContext ctx,ByteBuf msg)
    {
        //아직 버퍼에 데이터가 충분하지 않다면 다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
        if (msg.readableBytes() < packet_size + 8)
        {
            //정상적으로 데이터를 다 읽은것이 아니므로 readerIndex 4 -> 0로 초기화함.
            msg.resetReaderIndex();
            return false; //다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
        }
        //패킷이 뭉쳐서 데이터를 더 받아왔을 경우! 원래 받아야 할 부분만 받고, 나머지는 다시 되돌려서 뒤이어 오는 패킷과 합쳐서 받을 것!
        else if(msg.readableBytes() > packet_size + 8)
        {
            //헤더 데이터 파싱!
            //data total size(int)
            //packet_size = msg.readIntLE();
            //rmi host id(int)
            int rmi_id = msg.readIntLE();
            //rmi context (short)
            short rmi_ctx = msg.readShortLE();
            //rmi packet type (short)
            short packet_type = msg.readShortLE();

            //받아온 데이터.
            byte[] packet_data = new byte[packet_size];
            msg.readBytes(packet_data, 0, packet_size);

            //RMI 수신로직 처리 부분.
            RMI.recvByte(rmi_id, rmi_ctx, packet_type, packet_data, null, ctx); //TCP데이터이므로 UDP Data는 null값!

            //버퍼의 현재 용량에서 추가로 기록가능한 영역이 10KB 이하라면,
            //이미 읽은 분량만큼은 필요없으므로 Index 0의 위치로 데이터를 옮겨 추가로 기록가능한 영역을 늘린다.
            if(msg.capacity() - msg.writerIndex() < 10000)
                msg.discardReadBytes();

            //System.out.println("Over msg.readableBytes()="+msg.readableBytes()+" msg.writableBytes()="+msg.writableBytes()+"\nmsg.readerIndex()="+msg.readerIndex()+" msg.writerIndex()="+msg.writerIndex()+" msg.capacity()="+msg.capacity());

            //recvPacket recvPacket = new recvPacket(rmi_id, rmi_ctx, packet_type, packet_data, null, ctx);
            //testUserList.recvActorTCP.tell(recvPacket, null);

            //패킷 완성후 남은 데이터가 헤더크기인 12byte보다 작을 경우.
            if (msg.readableBytes() < 12)
            {
                return false;
            }
            else
            {
                msg.markReaderIndex();
                int amountSize = msg.readIntLE();

                //정해진 헤더규격이 아닌, 비정상적인 데이터가 도달하였다면.
                if(!isAvailablePacketSize(amountSize))
                {
                    //연결 종료
                    ctx.close(); //채널이 닫힌다면 msg는 release 된다.
                    return false;
                }

                return isCompleteSizePacket(amountSize, ctx, msg); //재귀함수 처리
                //패킷 완성후, 남은데이터가 없을때까지 계속 반복한다.
            }
        }
        else //정확한 데이터일 경우. [msg.readableBytes() + 4 == packet_size + 12] 또는 [msg.readableBytes() == packet_size + 8]
        {
            //헤더 데이터 파싱!
            //data total size(int)
            //packet_size = msg.readIntLE();
            //rmi host id(int)
            int rmi_id = msg.readIntLE();
            //rmi context (short)
            short rmi_ctx = msg.readShortLE();
            //rmi packet type (short)
            short packet_type = msg.readShortLE();

            //받아온 데이터.
            byte[] packet_data = new byte[packet_size];
            msg.readBytes(packet_data, 0, packet_size);

            //System.out.println("Before msg.readableBytes()="+msg.readableBytes()+" msg.writableBytes()="+msg.writableBytes()+"\nmsg.readerIndex()="+msg.readerIndex()+" msg.writerIndex()="+msg.writerIndex()+" msg.capacity()="+msg.capacity());

            //버퍼에 읽어와야 할 데이터가 남아있지 않으므로 readerIndex, writerIndex를 각각 0으로 초기화 한다.
            //내부 버퍼 데이터를 변경하지 않고 index값만 변경하므로 자원을 적게 소모한다.
            msg.clear();

            //System.out.println("After msg.readableBytes()="+msg.readableBytes()+" msg.writableBytes()="+msg.writableBytes()+"\nmsg.readerIndex()="+msg.readerIndex()+" msg.writerIndex()="+msg.writerIndex()+" msg.capacity()="+msg.capacity());

            //RMI 수신로직 처리 부분.
            RMI.recvByte(rmi_id, rmi_ctx, packet_type, packet_data, null, ctx); //TCP데이터이므로 UDP Data는 null값!

            //recvPacket recvPacket = new recvPacket(rmi_id, rmi_ctx, packet_type, packet_data, null, ctx);
            //testUserList.recvActorTCP.tell(recvPacket, null);

            //다시 되돌려서 패킷을 이어서 더 받아오게끔 하는 부분!
            return false;
        }
    }

    //정상적인 패킷인지 검증.
    boolean isAvailablePacketSize(int packet_size) {

        //비정상적인 패킷이라면 false
        if (0 >= packet_size || packet_size > 32768)
        {
            return false;
        }
        //정상적인 패킷이라면 true
        else
            return true;
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();

        if(!channel.isWritable())
            System.out.println(channel.remoteAddress()+" / TCPchannel bytesBeforeWritable() = "+channel.bytesBeforeWritable());
        else
            System.out.println(channel.remoteAddress()+" / TCPchannel.isWritable(true)");
    }

    //에러 발생시 처리!
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("[TCP_InBoundHandler] exceptionCaught 발생! "+cause.toString());
        cause.printStackTrace();
        ctx.close();
    }

} //class 종료 부분!