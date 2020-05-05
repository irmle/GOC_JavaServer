package RMI;

import RMI.RMI_Common.*;
import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.RMI_Classes.RMI_InitClasses.*;
import RMI.RMI_LogicMessages.*;
import RMI._NettyHandlerClass.*;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;



/*
RMI => Remote Method Invocation. 원격 메소드 호출.

통신 프로토콜 엔디안 방식은 Little Endian으로 고정.
패킷 구조 정의. 헤더 12byte.



Packet = [int size][int rmi_id][short rmi_context][short type][byte[] RMI_Data] 라는 구조로 송수신 예정.



*/


public class RMI_ {


//RMI 설정 부분.
//이 값이 다를 경우, 통신하지 않고 연결을 차단한다.
//=======================================================================================

    //RMI 송수신용 프로토콜 버전.
    //이 값을 기반으로 버전 구분을 한다. 서버, 클라이언트 각각의 rmi_protocol_version 값이 일치해야 한다.
    public static int rmi_protocol_version = 2019111101;

    //송수신 로그 활성화 여부.
    //true로 설정되어있으면 Log 기록용 메소드인 onRMI_Call(송신시), onRMI_Recv(수신시) 메소드가 호출된다.
    //로그를 사용할 경우, onRMI_Call(송신시), onRMI_Recv(수신시) 메소드에 로그 기록 로직을 작성해야 한다.
    public static boolean isLogEnable = true;

//=======================================================================================
//RMI 설정 부분 종료.



//세션 로직관련 부분.
//로그인, 로그아웃시 작업을 지정함.
//=======================================================================================

    //클라이언트와 처음으로 연결 되었을 때 호출되는 부분.
    public static void OnConnected(ChannelHandlerContext ctx)
    {
        //접속제한수를 초과하지 않았는지 체크.
        if(!RMI_NettyServer.checkClient_Max_Connection())
        {
            send_OverConnectionAnnounce(ctx);
        }
        else
        {
            //System.out.println("연결 시도중");
            //추후 UDP포트와의 Binding이 끝나면 OnConnected.OnConnected(RMI_ID); 가 호출됨.
        }
    }

    //클라이언트와 연결이 끊겼을 때 호출되는 부분.
    public static void OnDisConnected(ChannelHandlerContext ctx)
    {
        //접속 종료시, UDP 채널에 등록된 RMI_ID를 초기화.
        RMI_ID DisconnectID = ctx.channel().attr(TCP_InBoundHandler.getAttrKey()).get();
        if(DisconnectID != null)
        {
            //연결 종료시 처리 담당.
            OnDisconnected.OnDisconnected(DisconnectID);

            //접속 종료시, 등록된 RMI_ID를 해제.
            RMI_ID.removeRMI_ID(ctx.channel());

            //UDP 연결 상태를 Disable로 바꿈
            DisconnectID.isUDPConnectionAvailable = false;

            //TCP 채널 초기화!
            ctx.channel().attr(TCP_InBoundHandler.getAttrKey()).set(null);

            //UDP 채널 반환!
            Channel udpChannel = DisconnectID.getUDP_Object();
            udpChannel.attr(UDP_InBoundHandler.getAttrKey()).set(null);

            RMI_NettyServer.pushUDPChannel( udpChannel );

            //최종적으로 접속 종료시 1감소.
            RMI_NettyServer.decrementUserConnection();

            System.out.println("channelOnDisConnected : "+ctx.channel().remoteAddress());
        }
        else
        {
            //System.out.println("OnDisConnected DisconnectID == null\n유효하지 않은 패킷이 보내짐 : ["+ctx.channel().remoteAddress()+"]");
        }
    }

//=======================================================================================
//세션 로직관련 부분 종료.



//직접적으로 네트워크 송수신 함수 부분과 접하는 지점. 사용할 네트워크 프레임워크에 따라
//이 부분을 수동으로 작성할 필요가 있음.
//================================================================================

    //단일 송신 로직. TCP
    public static void sendByte_TCP(RMI_ID rmi_id, short rmi_ctx, short packetType, byte[] data)
    {
        if(rmi_id == null)
        {
            return;
            //throw new IllegalArgumentException("sendByte_TCP()::RMI_ID는 null일 수 없습니다.");
        }

        if(rmi_id.getTCP_Object() == null)
        {
            throw new IllegalArgumentException("sendByte_TCP()::rmi_id.getTCP_Object() == null입니다.");
        }

        if(data == null || data.length <= 0)
        {
            throw new IllegalArgumentException("sendByte_TCP()::길이가 0인 데이터나 null은 전송할 수 없습니다.");
        }

        if (!(RMI_Context.Reliable <= rmi_ctx && rmi_ctx <= RMI_Context.UnReliable_Public_AES256))
        {
            throw new IllegalArgumentException("sendByte_TCP()::RMI_Context 값이 잘못되었습니다. rmi_ctx = " + rmi_ctx);
        }

        if (!(0 < packetType && packetType <= RMI_PacketType.names.length))
        {
            throw new IllegalArgumentException("sendByte_TCP()::RMI_PacketType 값이 잘못되었습니다. packetType = " + packetType);
        }

        if(rmi_id.equals(RMI_ID.NONE))
        {
            System.out.println("sendByte_TCP()::RMI_ID.NONE 으로는 보낼 수 없습니다.");
            return;
        }

        Channel channel = rmi_id.getTCP_Object();

        //channel이 정상적일 경우 데이터 송신.
        if(channel.isActive())
        {
            //channel이 쓰기가능한 상태인지 체크.

            //쓰기 가능하다면 데이터 전송.
            if(channel.isWritable())
            {
                ByteBuf packet = alloc.directBuffer(32768);

                packet.writeIntLE(data.length);
                packet.writeIntLE(RMI_ID.SERVER.rmi_host_id); //보내는 주체의 RMI_ID가 지정됨.
                packet.writeShortLE(rmi_ctx);
                packet.writeShortLE(packetType);
                packet.writeBytes(data);

                rmi_id.getTCP_ObjectHandler().writeAndFlush( packet );
            }
            //가능하지 않다면 Queue에 쌓거나 release 한다.
            else
            {
                //TCP의 송신이 늦는다면 연결을 종료한다.
                //System.out.println("TCP SendBufferOver");
                //rmi_id.getTCP_ObjectHandler().close();
            }
        }
        //System.out.println("sendByte_TCP = "+data.length+" bytes");
    }

    //범위 송신 로직. TCP
    public static void sendByte_TCP(RMI_ID[] rmi_id, short rmi_ctx, short packetType, byte[] data)
    {
        if(rmi_id == null || rmi_id.length <= 0)
        {
            return;
            //throw new IllegalArgumentException("sendByte_TCP[]()::RMI_ID[]는 null이거나 길이가 0일 수 없습니다.");
        }

        if(data == null || data.length <= 0)
        {
            throw new IllegalArgumentException("sendByte_TCP[]()::길이가 0인 데이터나 null은 전송할 수 없습니다.");
        }

        if (!(RMI_Context.Reliable <= rmi_ctx && rmi_ctx <= RMI_Context.UnReliable_Public_AES256))
        {
            throw new IllegalArgumentException("sendByte_TCP[]()::RMI_Context 값이 잘못되었습니다. rmi_ctx = " + rmi_ctx);
        }

        if (!(0 < packetType && packetType <= RMI_PacketType.names.length))
        {
            throw new IllegalArgumentException("sendByte_TCP[]()::RMI_PacketType 값이 잘못되었습니다. packetType = " + packetType);
        }

        ByteBuf packet = alloc.directBuffer(32768);

        packet.writeIntLE(data.length);
        packet.writeIntLE(RMI_ID.SERVER.rmi_host_id); //보내는 주체의 RMI_ID가 지정됨.
        packet.writeShortLE(rmi_ctx);
        packet.writeShortLE(packetType);
        packet.writeBytes(data);

        //하나의 ByteBuf객체를 재활용해서 여러번 사용할 경우, ByteBuf.retainedDuplicate() 메소드를 사용할것!
        //이후, packet ByteBuf객체를 release() 해주면 된다.
        for(int i=0;i<rmi_id.length;i++)
        {
            RMI_ID target = rmi_id[i];

            if(target != null && !target.equals(RMI_ID.NONE))
            {
                Channel channel = target.getTCP_Object();
                if(channel != null && channel.isActive())
                {
                    //channel이 쓰기가능한 상태인지 체크.

                    //쓰기 가능하다면 데이터 전송.
                    if(channel.isWritable())
                    {
                        target.getTCP_ObjectHandler().writeAndFlush( packet.retainedDuplicate() );
                    }
                    //가능하지 않다면 Queue에 쌓거나 release 한다.
                    else
                    {
                        //TCP의 송신이 늦는다면 연결을 종료한다.
                        //System.out.println("TCP[] SendBufferOver");
                        //target.getTCP_ObjectHandler().close();
                        //channel.pipeline().fireUserEventTriggered( packet.retainedDuplicate() );
                    }
                }
            }
        }

        //ByteBuf객체를 해제
        packet.release();

        //System.out.println("sendByte_TCP [] = "+data.length+" bytes");
    }

    //단일 송신 로직. UDP
    public static void sendByte_UDP(RMI_ID rmi_id, short rmi_ctx, short packetType, byte[] data)
    {
        System.out.println("단일 송신 로직 UDP sendByte_UDP 매서드 호출, packetType = " + packetType);

        if(rmi_id == null)
        {

            System.out.println("rmi_id가 널");
            return;
            //throw new IllegalArgumentException("sendByte_UDP()::RMI_ID는 null일 수 없습니다.");
        }

        if(rmi_id.getUDP_Object() == null)
        {
            throw new IllegalArgumentException("sendByte_UDP()::rmi_id.getUDP_Object() == null입니다.");
        }

        if(data == null || data.length <= 0)
        {
            throw new IllegalArgumentException("sendByte_UDP()::길이가 0인 데이터나 null은 전송할 수 없습니다.");
        }

        if (!(RMI_Context.Reliable <= rmi_ctx && rmi_ctx <= RMI_Context.UnReliable_Public_AES256))
        {
            throw new IllegalArgumentException("sendByte_UDP()::RMI_Context 값이 잘못되었습니다. rmi_ctx = " + rmi_ctx);
        }

        if (!(0 < packetType && packetType <= RMI_PacketType.names.length))
        {
            throw new IllegalArgumentException("sendByte_UDP()::RMI_PacketType 값이 잘못되었습니다. packetType = " + packetType);
        }

        if(rmi_id.equals(RMI_ID.NONE))
        {
            System.out.println("sendByte_UDP()::RMI_ID.NONE 으로는 보낼 수 없습니다.");
            return;
        }

        if(!rmi_id.isUDPConnectionAvailable)
        {
            System.out.println("UDP연결이 가능한 상태가 아님. sendByte_UDP 취소");
            return;
        }

        //UDP패킷을 보낼 주소.
        InetSocketAddress remoteAddress = (InetSocketAddress)rmi_id.getUDP_Object().remoteAddress();
        //UDP패킷을 보내는 쪽의 주소.
        InetSocketAddress localAddress = (InetSocketAddress)rmi_id.getUDP_Object().localAddress();


        if(remoteAddress == null || localAddress == null)
        {
            System.out.println("sendByte_UDP()::rmi_id.getUDP_Object() remoteAddress or localAddress==null");
            return;
        }

        Channel channel = rmi_id.getUDP_Object();

        //channel이 정상적일 경우 데이터 송신.
        if(channel.isActive())
        {
            //channel이 쓰기가능한 상태인지 체크.

            //쓰기 가능하다면 데이터 전송.
            if(channel.isWritable())
            {
                ByteBuf packet = alloc.directBuffer(32768);

                packet.writeIntLE(data.length);
                packet.writeIntLE(RMI_ID.SERVER.rmi_host_id); //보내는 주체의 RMI_ID가 지정됨.
                packet.writeShortLE(rmi_ctx);
                packet.writeShortLE(packetType);
                packet.writeBytes(data);

                //정상적일 경우 데이터 송신.
                DatagramPacket udpData = new DatagramPacket(packet, remoteAddress, localAddress);

                rmi_id.getUDP_ObjectHandler().writeAndFlush( udpData );
            }
            //가능하지 않다면 Queue에 쌓거나 release 한다.
            else
            {
                System.out.println("UDP SendBufferOver");
            }
        }

        //System.out.println("sendByte_UDP = "+data.length+" bytes");
    }

    //범위 송신 로직. UDP
    public static void sendByte_UDP(RMI_ID[] rmi_id, short rmi_ctx, short packetType, byte[] data)
    {

        System.out.println("범위 송신 로직 UDP sendByte_UDP 매서드 호출, packetType = " + packetType);

        if(rmi_id == null || rmi_id.length <= 0)
        {
            return;
            //throw new IllegalArgumentException("sendByte_UDP[]()::RMI_ID[]는 null이거나 길이가 0일 수 없습니다.");
        }

        if(data == null || data.length <= 0)
        {
            throw new IllegalArgumentException("sendByte_UDP[]()::길이가 0인 데이터나 null은 전송할 수 없습니다.");
        }

        if (!(RMI_Context.Reliable <= rmi_ctx && rmi_ctx <= RMI_Context.UnReliable_Public_AES256))
        {
            throw new IllegalArgumentException("sendByte_UDP[]()::RMI_Context 값이 잘못되었습니다. rmi_ctx = " + rmi_ctx);
        }

        if (!(0 < packetType && packetType <= RMI_PacketType.names.length))
        {
            throw new IllegalArgumentException("sendByte_UDP[]()::RMI_PacketType 값이 잘못되었습니다. packetType = " + packetType);
        }

        ByteBuf packet = alloc.directBuffer(32768);

        packet.writeIntLE(data.length);
        packet.writeIntLE(RMI_ID.SERVER.rmi_host_id);  //보내는 주체의 RMI_ID가 지정됨.
        packet.writeShortLE(rmi_ctx);
        packet.writeShortLE(packetType);
        packet.writeBytes(data);

        for(int i=0;i<rmi_id.length;i++)
        {
            RMI_ID target = rmi_id[i];
            System.out.println("송신 타겟의 udpConnectionAbailable : " + target.isUDPConnectionAvailable);
            //System.out.println("ㄴ 주소 : " + target.getUDP_Object().remoteAddress().toString());

            if(target != null && !target.equals(RMI_ID.NONE) && target.isUDPConnectionAvailable)
            {
                System.out.println("송신처리 수행");

                Channel channel = target.getUDP_Object();
                if(channel != null && channel.isActive())
                {
                    //UDP패킷 수신자 주소.
                    InetSocketAddress remoteAddress = (InetSocketAddress)target.getUDP_Object().remoteAddress();

                    //UDP패킷 송신자 주소.
                    InetSocketAddress localAddress = (InetSocketAddress)target.getUDP_Object().localAddress();

                    if(remoteAddress == null || localAddress == null)
                    {
                        System.out.println("sendByte_UDP[]()::rmi_id.getUDP_Object() remoteAddress or localAddress==null");
                        continue;
                    }
                    //channel이 쓰기가능한 상태인지 체크.

                    //쓰기 가능하다면 데이터 전송.
                    if(channel.isWritable())
                    {
                        //UDP 송신을 위한 DatagramPacket 생성.
                        DatagramPacket udpData = new DatagramPacket( packet.retainedDuplicate(), remoteAddress, localAddress);

                        target.getUDP_ObjectHandler().writeAndFlush( udpData );
                    }
                    //가능하지 않다면 Queue에 쌓거나 release 한다.
                    else
                    {
                        System.out.println("UDP[] SendBufferOver");
                        //channel.writeAndFlush( udpData );
                    }
                }
                else {
                    System.out.println("채널이 NULL 이거나 활성화되어있지 않음");
                }
            }

            if(target == null){
                System.out.println("target이 NULL");
            }

            else if(!target.isUDPConnectionAvailable){
                System.out.println("isUDPConnectionAvailable이 false");
            }

            else if(target.equals(RMI_ID.NONE)){
                System.out.println("RMI_ID가 NONE");
            }


        }

        //ByteBuf객체를 해제
        packet.release();

        //System.out.println("sendByte_UDP [] = "+data.length+" bytes");
    }



    //수신 로직.
    //암호화 키를 찾기위한 Object, byte[] 로부터 가져온 PacketType, ContextType, 그리고 RMI_Data를 가져옴.
    public static void recvByte(int rmi_host_id, short rmi_ctx, short packetType, byte[] data, InetSocketAddress UDP_Sender, ChannelHandlerContext ctx_handler)
    {

        System.out.println("수신 로직 recvByte 매서드 호출, packetType = " + packetType);

        //적절한 RMI_Context인지 확인.
        if (!(RMI_Context.Reliable <= rmi_ctx && rmi_ctx <= RMI_Context.UnReliable_Public_AES256))
        {
            if(UDP_Sender == null)
            {

                System.out.println("비정상적인 데이터를 보냈으므로 IPBan 처리");

                //비정상적인 데이터를 보냈으므로 IPBan처리
                InetSocketAddress IllegalIPAddress = (InetSocketAddress)ctx_handler.channel().remoteAddress();
                IPFilterHandler.addIPBanList(IllegalIPAddress);

                System.out.println("rmi_ctx 비정상적인 TCP패킷 도달!");
                ctx_handler.close();
            }
            else
            {
                System.out.println("rmi_ctx 비정상적인 UDP패킷 도달!");
                UDP_Sender = null;
            }

            //System.out.println("rmi_ctx = " + (rmi_ctx)+ " / packetType = "+ (packetType));
            data = null;
            return; //비정상 패킷이므로 차단.
        }

        //적절한 RMI_PacketType인지 확인.
        if (!(0 < packetType && packetType <= RMI_PacketType.names.length))
        {
            //RMI_Connection 관련 패킷인지 확인.
            if(RMI_ConnectionPacketType.RMI_ProtocolVersionCheck <= packetType && packetType <= RMI_ConnectionPacketType.RMI_OverConnectionAnnounce)
            {
                System.out.println("정상 패킷");
                //정상 패킷! 아무것도 하지 않는다.
            }
            else
            {

                System.out.println("비정상 패킷");
                //비정상 패킷이므로 차단.
                if(UDP_Sender == null)
                {
                    //비정상적인 데이터를 보냈으므로 IPBan처리
                    InetSocketAddress IllegalIPAddress = (InetSocketAddress)ctx_handler.channel().remoteAddress();
                    IPFilterHandler.addIPBanList(IllegalIPAddress);

                    System.out.println("packetType 비정상적인 TCP패킷 도달!");
                    ctx_handler.close();
                }
                else
                {
                    System.out.println("packetType 비정상적인 UDP패킷 도달!");
                    UDP_Sender = null;
                }

                System.out.println("packetType = "+ (packetType)+ " / rmi_ctx = " + (rmi_ctx));
                data = null;
                return; //비정상 패킷이므로 차단.
            }
        }

        RMI_ID RMI_ID_id = null;

        //이미 연결이 수립된 곳에서 보낸 패킷인지,
        //막 연결을 수립하려고 보낸 패킷인지 판별하는 부분.
        if (UDP_Sender == null) //TCP연결이라면
        {
            //RMI_ID_id = RMI_ID.findRMI_Connection(ctx_handler.channel());

            //네티 Channel의 AttrKey에 등록된 RMI_ID 확인.
            RMI_ID_id = ctx_handler.channel().attr(TCP_InBoundHandler.getAttrKey()).get();
        }
        else //UDP연결이라면
        {
            //RMI_ID_id = RMI_ID.findRMI_HOST_ID(rmi_host_id);

            //네티 Channel의 AttrKey에 등록된 RMI_ID 확인.
            RMI_ID_id = ctx_handler.channel().attr(UDP_InBoundHandler.getAttrKey()).get();
        }

        //처음 접속한 유저라면, RMI 통신을 위한 키교환 과정 진행!
        //정상적인 패킷인지 판별하여 차단여부 결정.
        if(RMI_ID_id == null || RMI_ID_id.equals(RMI_ID.NONE))
        {
            //TCP의 경우.
            if(UDP_Sender == null)
            {
                switch (packetType)
                {
                    case RMI_ConnectionPacketType.RMI_ProtocolVersionCheck: //Protocol Version 체크시.
                        //접속제한수를 초과하지 않았는지 체크.
                        if(!RMI_NettyServer.checkClient_Max_Connection())
                        {
                            //초과한 상태라면 핸드쉐이킹 과정을 중지한다.
                            //이미 연결 끊김이 예약됨
                            break;
                        }

                        RMI_ProtocolVersionCheck recvData = null;
                        try
                        {
                            //고정된 대칭키로 복호화!
                            byte[] recvPacket =
                                    RMI__EncryptManager.decryptAES_256(data, "RMI_Connection_Protocol", "RMI_Connection_Protocol");

                            recvData = RMI_ProtocolVersionCheck.createRMI_ProtocolVersionCheck(recvPacket);
                            recvPacket = null;
                        }
                        catch (Exception e)
                        {
                            System.out.println("Unknown Connection Data:RMI_ProtocolVersionCheck 비정상적인 TCP패킷 도달! from:"+ctx_handler.channel().remoteAddress());
                            //비정상적인 데이터를 보냈으므로 IPBan처리
                            IPFilterHandler.addIPBanList((InetSocketAddress)ctx_handler.channel().remoteAddress());
                            ctx_handler.close();
                            break;
                        }

                        if(recvData != null && rmi_protocol_version != recvData.rmi_protocol_version)
                        {
                            System.out.println("접속한 클라이언트의 버전이 맞지 않습니다. 연결을 해제합니다. from:"+ctx_handler.channel().remoteAddress());
                            ctx_handler.close();
                            recvData = null;
                            break;
                        }//버전체크 종료.

                        //클라이언트에게 접속용 RSA 공개키 전송!
                        RMI_RSA_PublicKey sendData = new RMI_RSA_PublicKey();
                        sendData.base64Encoded_publicKey = RMI__EncryptManager.getPublicKey();
                        //고정된 대칭키로 암호화!
                        byte[] sendData_ = RMI__EncryptManager.encryptAES_256(sendData.getBytes(), "RMI_Connection_Protocol", "RMI_Connection_Protocol");
                        sendData = null;
                        //보낼 데이터 만큼 directBuffer 할당.
                        ByteBuf RMI_RSA_PublicKey = alloc.directBuffer(32768);

                        RMI_RSA_PublicKey.writeIntLE(sendData_.length); //길이값 지정.
                        RMI_RSA_PublicKey.writeIntLE(RMI_ID.SERVER.rmi_host_id); //보내는 측의 rmi_host_id지정
                        RMI_RSA_PublicKey.writeShortLE(RMI_Context.Reliable);
                        RMI_RSA_PublicKey.writeShortLE(RMI_ConnectionPacketType.RMI_RSA_PublicKey);
                        RMI_RSA_PublicKey.writeBytes(sendData_);

                        //클라이언트에게 접속용 RSA 공개키 전송완료!!
                        ctx_handler.writeAndFlush(RMI_RSA_PublicKey);
                        sendData_ = null;
                        break;
                    case RMI_ConnectionPacketType.RMI_RSA_PublicKey: //서버로부터 RSA 공개키 데이터 수신시.
                        //서버에서는 호출될 일 없음. 클라이언트에서만 호출.
                        System.out.println("잘못된 패킷! 연결을 종료합니다. ["+ctx_handler.channel().remoteAddress()+"]");
                        //비정상적인 데이터를 보냈으므로 IPBan처리
                        IPFilterHandler.addIPBanList((InetSocketAddress)ctx_handler.channel().remoteAddress());
                        ctx_handler.close();
                        break;
                    case RMI_ConnectionPacketType.RMI_Send_EncryptedAES_Key: //클라로부터, 서버가 보낸 공개키에 의해 암호화된 AES대칭키 데이터 수신시.

                        RMI_Send_EncryptedAES_Key recvData1 = null;
                        try
                        {
                            //공개키로 암호화된 데이터를 받았으므로, 소유중인 비밀키로 데이터 복호화!
                            byte[] recvPacket1 = RMI__EncryptManager.decryptRSA_private(data, RMI__EncryptManager.getPrivateKey());

                            //복호화된 암호화키로 객체를 생성한다.
                            recvData1 = RMI_Send_EncryptedAES_Key.createRMI_Send_EncryptedAES_Key(recvPacket1);

                            if(recvData1 == null){
                                System.out.println("데이터 복호화 했는데 데이터가 널..?? ");
                            }

                            recvPacket1 = null;
                        }
                        catch (Exception e)
                        {
                            System.out.println("Unknown Connection Data:RMI_Send_EncryptedAES_Key 비정상적인 TCP패킷 도달! from:"+ctx_handler.channel().remoteAddress());
                            //비정상적인 데이터를 보냈으므로 IPBan처리
                            IPFilterHandler.addIPBanList((InetSocketAddress)ctx_handler.channel().remoteAddress());
                            ctx_handler.close();
                        }

                        if(recvData1 != null)
                        {
                            //RMI_ID에 TCP, UDP Channel 등록.
                            Channel udpChannel = RMI_NettyServer.popUDPChannel();
                            if(udpChannel == null)
                            {
                                System.out.println("udp채널이 null.");
                                send_OverConnectionAnnounce(ctx_handler);
                                recvData1 = null;
                                return;
                            }
                            else{
                                System.out.println("채널이 null은 아님;");
                            }

                            //접속에 성공한 클라이언트수 증가.
                            RMI_NettyServer.incrementUserConnection();

                            String aesKey = Base64.getEncoder().encodeToString(recvData1.RSAEncrypted_AESKey);
                            String aesIV = Base64.getEncoder().encodeToString(recvData1.RSAEncrypted_AESIV);

                            //커넥션 할당작업 시작.
                            //HashMap(Connection, HostID) 에 등록 및, 새로운 RMI_ID 발급.  추후 로그인 패킷을 통해 Unique_id 등록 예정.
                            RMI_ID newRMI_ID = RMI_ID.createRMI_ID(ctx_handler.channel());

                            //클라로부터 수신받은 암호화 키 세팅.
                            newRMI_ID.AESKey.setAESKey(aesKey, aesIV);

                            //channel의 AttrKey에 RMI_ID객체 등록!
                            ctx_handler.channel().attr(TCP_InBoundHandler.getAttrKey()).set(newRMI_ID);
                            udpChannel.attr(UDP_InBoundHandler.getAttrKey()).set(newRMI_ID);

                            //RMI_ID_id = ctx_handler.channel().attr(TCP_InBoundHandler.getAttrKey()).get();


                            newRMI_ID.setUDP_Object(udpChannel);
                            newRMI_ID.setTCP_Object(ctx_handler.channel());
                            newRMI_ID.setTCP_ObjectHandler(ctx_handler);


                            //클라이언트로 보낼 데이터 세팅.
                            RMI_Send_EncryptedAccept_Data send_EncryptedAccept_Data = new RMI_Send_EncryptedAccept_Data();

                            //클라이언트에게 할당된 RMI_HostID 정보.
                            send_EncryptedAccept_Data.RMI_HostID = newRMI_ID.rmi_host_id;

                            //서버의 공용 AES키를 지정.
                            send_EncryptedAccept_Data.AESEncrypted_PublicAESKey = RMI__EncryptManager.Public_AESKeys.aesKey.getBytes(StandardCharsets.UTF_8);
                            send_EncryptedAccept_Data.AESEncrypted_PublicAESIV = RMI__EncryptManager.Public_AESKeys.aesIV.getBytes(StandardCharsets.UTF_8);

                            //할당된 UDP채널의 Port 지정.
                            send_EncryptedAccept_Data.UDP_InitPort = (short)((InetSocketAddress)udpChannel.localAddress()).getPort();

                            //송신할 byte[] 데이터!
                            byte[] plainData = send_EncryptedAccept_Data.getBytes();

                            //클라로부터 받은 대칭키로 이를 다시 암호화 한다!
                            byte[] encryptPacket = RMI__EncryptManager.encryptAES_256(plainData, newRMI_ID.AESKey.aesKey, newRMI_ID.AESKey.aesIV);

                            //보낼 데이터 만큼 directBuffer 할당.
                            ByteBuf RMI_Send_EncryptedAccept_Data = alloc.directBuffer(32768);

                            RMI_Send_EncryptedAccept_Data.writeIntLE(encryptPacket.length); //길이값 지정.
                            RMI_Send_EncryptedAccept_Data.writeIntLE(RMI_ID.SERVER.rmi_host_id); //보내는 측의 rmi_host_id지정
                            RMI_Send_EncryptedAccept_Data.writeShortLE(RMI_Context.Reliable);
                            RMI_Send_EncryptedAccept_Data.writeShortLE(RMI_ConnectionPacketType.RMI_Send_EncryptedAccept_Data);
                            RMI_Send_EncryptedAccept_Data.writeBytes(encryptPacket);

                            ctx_handler.writeAndFlush(RMI_Send_EncryptedAccept_Data);

                            send_EncryptedAccept_Data = null;
                            plainData = null;
                            //System.out.println("[Connection Procedure Complete]");
                        }
                        else{

                            System.out.println("revDatal 이 널");
                        }
                        break;
                    case RMI_ConnectionPacketType.RMI_Send_EncryptedAccept_Data: //서버로부터 AES키 및 RMI_HostID, UDP initPort등의 정보를 수신시.
                        //서버에서는 호출될 일 없음. 클라이언트에서만 호출.
                        System.out.println("잘못된 패킷입니다! 연결을 종료합니다. ["+ctx_handler.channel().remoteAddress()+"]");
                        //비정상적인 데이터를 보냈으므로 IPBan처리
                        IPFilterHandler.addIPBanList((InetSocketAddress)ctx_handler.channel().remoteAddress());
                        ctx_handler.close();
                        break;
                    case RMI_ConnectionPacketType.RMI_OverConnectionAnnounce: //최대 접속자수 제한을 초과한 경우.
                        //서버에서는 호출될 일 없음. 클라이언트에서만 호출.
                        System.out.println("잘못된 패킷임! 연결을 종료합니다. ["+ctx_handler.channel().remoteAddress()+"]");
                        //비정상적인 데이터를 보냈으므로 IPBan처리
                        IPFilterHandler.addIPBanList((InetSocketAddress)ctx_handler.channel().remoteAddress());
                        ctx_handler.close();
                        break;
                    default: //잘못된 값이 왔을 경우, 연결 차단!
                        System.out.println("잘못된 패킷! 연결을 종료합니다. ["+ctx_handler.channel().remoteAddress()+"]");
                        //비정상적인 데이터를 보냈으므로 IPBan처리
                        IPFilterHandler.addIPBanList((InetSocketAddress)ctx_handler.channel().remoteAddress());
                        ctx_handler.close();
                        break;
                }
            }
            //UDP
            else
            {
                //이 패킷은 무시한다! 잘못된 값이므로.

                System.out.println("Invalidate Connection UDP 비정상적인 UDP패킷 도달!");
                System.out.println("rmi_ctx = " + RMI_Context.name(rmi_ctx)+ " / packetType = "+ RMI_PacketType.name(packetType));

                //비정상적인 UDP패킷이 도달하였다면 BanList에 추가하여 차단한다!
                IPFilterHandler.addIPBanList(UDP_Sender);
            }
            data = null;
            return;
        }//처음 접속한 유저, RMI 통신을 위한 키교환 과정 진행 부분.

        //기존에 접속중인 유저중.
        else
        {
            System.out.println("기존에 접속중인 유저중");
            System.out.println("패킷 타입 : " + packetType);
            if(UDP_Sender != null)
            {

                System.out.println("UDP sender Null이 아님");
                System.out.println("패킷 타입 : " + packetType);
                switch (packetType)
                {
                    case RMI_ConnectionPacketType.RMI_UDP_ConnectionConfirm:

                        RMI_UDP_ConnectionConfirm recvUDP = null;
                        RMI_ID remote = RMI_ID.findRMI_HOST_ID(rmi_host_id);
                        if(remote != null)
                        {
                            byte[] udpCheck = RMI__EncryptManager.decryptAES_256(data, remote.AESKey.aesKey, remote.AESKey.aesIV);
                            try
                            {
                                recvUDP = RMI_UDP_ConnectionConfirm.createRMI_UDP_ConnectionConfirm(udpCheck);

                                udpCheck = null;
                            }
                            catch (Exception e)
                            {
                                System.out.println("RMI_UDP_ConnectionConfirm 파싱중 에러!");

                                /*//네티 Channel의 AttrKey에 등록된 RMI_ID 확인.
                                RMI_ID disconnectID = ctx_handler.channel().attr(UDP_InBoundHandler.getAttrKey()).get();

                                //데이터 오류시 연결 해제.
                                if(disconnectID != null)
                                    disconnectID.getTCP_Object().close();*/
                            }

                            if(recvUDP != null && remote.getUDP_Object() != null /*&& remote.getUDP_Object().remoteAddress() == null*/)
                            {
                                recvUDP = null;
                                if(remote.isUDPConnectionAvailable)
                                    return;

                                remote.setUDP_ObjectHandler(ctx_handler);

                                remote.getUDP_Object().connect(UDP_Sender);

                                //System.out.println("UDP 바인딩 완료 bindPort : "+recvUDP.checkUDP_Connection);
                                //System.out.println("포트 바인딩 : "+remote.getUDP_Object().remoteAddress() + " / 로컬:"+remote.getUDP_Object().localAddress());
                                //remote.getUDP_Object().close();
                                //System.out.println("close 포트 바인딩 : "+remote.getUDP_Object().remoteAddress()+ " / 로컬:"+remote.getUDP_Object().localAddress());

                                System.out.println("channelOnConnected : "+remote.getTCP_Object().remoteAddress());

                                //UDP 연결 상태를 Enable로 바꿈
                                remote.isUDPConnectionAvailable = true;

                                //연결 수립시 처리 담당.
                                OnConnected.OnConnected(remote);
                            }
                        }
                        data = null;
                        return;

                    default:  {

                        System.out.println("디폴트...");

                        RMI_ID unInitializedUser = RMI_ID.findRMI_HOST_ID(rmi_host_id);

                        System.out.println(unInitializedUser.rmi_host_id);

                        if(unInitializedUser != null && unInitializedUser.isUDPConnectionAvailable == false)
                        {
                            try {
                                unInitializedUser.setUDP_ObjectHandler(ctx_handler);

                                unInitializedUser.getUDP_Object().connect(UDP_Sender);

                                System.out.println("2차 UDP 바인딩 완료 : "+unInitializedUser.getUDP_Object().localAddress());

                                System.out.println("channelOnConnected : "+unInitializedUser.getTCP_Object().remoteAddress());

                                //UDP 연결 상태를 Enable로 바꿈
                                unInitializedUser.isUDPConnectionAvailable = true;

                                //연결 수립시 처리 담당.
                                OnConnected.OnConnected(unInitializedUser);
                            }
                            catch (IllegalArgumentException e)
                            {
                                System.out.println("2차 UDP 바인딩중 Error 발생" + e);
                            }
                        }
                        else{

                            if(unInitializedUser == null){
                                System.out.println("unInitializedUser가 null");
                            }
                        }
                    }
                    break;
                }
            }
            else{

                System.out.println("UDP sender Null임..");
            }
        }

        //도착한 패킷 수신처리 부분.
        client_to_server.recvRMI_Method(RMI_ID_id, rmi_ctx, packetType, data);
    }

//================================================================================
//직접적으로 네트워크 송수신 함수 부분과 접하는 지점.





//호출되었을 때 실행되는 부분. 로그 기록에 사용되는 용도
//이 부분을 수동으로 작성할 필요가 있음.
//================================================================================

    //송신시 로그 기록 작업 작성 부분.
    public static void onRMI_Call(short rmi_ctx, short packetType, Object LogData)
    {
        //로그 기록 설정여부 판단.
        if(!isLogEnable)
            return;

        //로그 작업
        String log = "["+getCurrentTime()+" onRMI_Call:"+RMI_PacketType.name(packetType)+" "+ RMI_Context.name(rmi_ctx)+"]\n"+LogData;
        System.out.println(log);
    }

    //수신시 로그 기록 작업 작성 부분.
    public static void onRMI_Recv(short rmi_ctx, short packetType, Object LogData)
    {
        //로그 기록 설정여부 판단.
        if(!isLogEnable)
            return;

        //로그 작업
        String log = "["+getCurrentTime()+" onRMI_Recv:"+RMI_PacketType.name(packetType)+" "+RMI_Context.name(rmi_ctx)+"]\n"+LogData;
        System.out.println(log);
    }

//================================================================================
//호출되었을 때 실행되는 부분. 로그 기록에 사용되는 용도.




    //RMI 암호화 관리자 클래스.
    private static final RMI__EncryptManager RMI_ENCRYPT = new RMI__EncryptManager();

    //날짜 지정용 클래스.
    private static Date currentDate = new Date();  //2019.12.31 23:59:59.456 (ms단위까지)
    private static SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

    //버퍼 재사용을 위한 버퍼Pool 할당을 위한 객체. 이로서 버퍼 생성, 삭제에 많은 비용을 들일 필요없이 재사용을 하여
    //자원 절약을 통해 성능 향상에 도움이 된다!
    private static PooledByteBufAllocator alloc = PooledByteBufAllocator.DEFAULT;


    //날짜 문자열 출력용 메소드.
    public static String getCurrentTime() {
        currentDate.setTime(System.currentTimeMillis());
        return date.format(currentDate);
    }

    //접속제한이 초과되었을때, 해당 유저에게 초과되었음을 알리는 메시지 전송후 채널 닫음.
    static void send_OverConnectionAnnounce(ChannelHandlerContext ctx)
    {
        System.out.println("OverConnectionAnnounce ");

        RMI_OverConnectionAnnounce overConnectionAnnounce = new RMI.RMI_Classes.RMI_InitClasses.RMI_OverConnectionAnnounce();
        overConnectionAnnounce.maxConnection = RMI_NettyServer.getClient_Max_Connection();
        byte[] overConnection_data = overConnectionAnnounce.getBytes();

        //접속자수 제한을 초과하였음을, 해당 클라이언트에게 송신 후, 연결을 끊는다.
        ByteBuf RMI_OverConnectionAnnounce = alloc.directBuffer(32768);;

        RMI_OverConnectionAnnounce.writeIntLE(overConnection_data.length); //길이값 지정.
        RMI_OverConnectionAnnounce.writeIntLE(RMI_ID.SERVER.rmi_host_id); //보내는 측의 rmi_host_id지정
        RMI_OverConnectionAnnounce.writeShortLE(RMI_Context.Reliable);
        RMI_OverConnectionAnnounce.writeShortLE(RMI_ConnectionPacketType.RMI_OverConnectionAnnounce);
        RMI_OverConnectionAnnounce.writeBytes(overConnection_data);

        //송신하는 부분.
        ctx.writeAndFlush(RMI_OverConnectionAnnounce);

        //채널 종료하는 부분.
        ctx.executor()
                .schedule( new RMI_OverConnectionTask(ctx), 1000, TimeUnit.MILLISECONDS );

        overConnection_data = null;
        overConnectionAnnounce = null;
    }
}

