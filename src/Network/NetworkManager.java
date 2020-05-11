package Network;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

import ECS.Chatting.ChattingManager;
import ECS.Chatting.LogManager;
import ECS.Game.*;
import ECS.Factory.*;

import Network._NettyHandlerClass.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.timeout.ReadTimeoutHandler;

public class NetworkManager {



//서버 기본값 설정 부분.
//====================================================================

    //게임서버 리스닝 포트.
    private static int PORT_NUM = 65005; //default 값.

    //게임서버 최대 동접자 수 제한.
    private static int client_Max_Connection = 500; //default 값.

//====================================================================


    public NetworkManager(){}

    //설정된 최대 동접자수 만큼의 UDP채널 할당. //1개 클라당 1개 UDP소켓 연결. <추후 변경 가능>
    private static ConcurrentLinkedQueue<Channel> udpChannel_Queue = new ConcurrentLinkedQueue<>();

    //연결중인 클라이언트 수.
    private static AtomicInteger countUserConnection = new AtomicInteger(0);

    //Netty Framework 설정 시작!
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;

    //TCP 서버용 부트스트랩 객체 선언!
    private static ServerBootstrap serverBootstrap;

    //UDP 서버용 부투트스트랩 객체!
    private static Bootstrap udpBootstrap;


    //옵션 인자값이 잘못되었을 때, 콘솔에 관련 메시지를 표시하는 용도로 호출되는 메소드.
    void showOptionValueError()
    {
        System.out.println("<옵션 정보 : 아무것도 입력하지 않을 시, 기본값이 적용됨>");
        System.out.println("-port => 게임서버 리스닝 포트 / -maxconnection => 최대 접속자수 지정값 / -version => rmi 프로토콜 버전");
        System.out.println("옵션값 예시 ex) -port=123 -maxconnection=1000 -version=2019111101 -tickrate=100 -usercount=3");
    }

    //서버 초기화 부분.
    public void startServer(String[] options){

        //서버 초기화 소요시간 측정용
        long startLogicTime;
        long elapsedLogicTime;

        startLogicTime = System.nanoTime();

        //옵션값 지정.
        //추후, 인자값을 직접 입력하는 것이 아닌 ServerInfo.ini 등의 설정파일을 따로 두고,
        //이 파일을 읽어와서 옵션값을 파싱후, 적용하는 방식으로 변경할 예정.
        if(options.length>0)
        {
            try
            {
                //옵션값
                String [] option;
                for(int i=0;i<options.length;i++)
                {
                    if(options[i].contains("="))
                    {
                        option = options[i].split("=");
                        //잘못된 값일경우.
                        if(option.length!=2)
                        {
                            System.out.println("잘못된 옵션값 지정!");
                            showOptionValueError();
                            return;
                        }


                        if(option[0].equals("-port"))
                        {
                            PORT_NUM = Integer.parseInt(option[1]);
                        }
                        else if(option[0].equals("-maxconnection"))
                        {
                            client_Max_Connection = Integer.parseInt(option[1]);
                        }
                        else if(option[0].equals("-version"))
                        {
                            RMI.rmi_protocol_version = Integer.parseInt(option[1]);
                        }
                        else if(option[0].equals("-tickrate"))
                        {
                            WorldMap.tickRate = Integer.parseInt(option[1]);
                        }
                        else if(option[0].equals("-usercount"))
                        {
                            MatchingManager.userCount = Integer.parseInt(option[1]);
                        }
                        /*else if(option[0].equals("-db"))
                        {
                            PORT_NUM = Integer.parseInt(option[1]);
                        }
                        else if(option[0].equals("-dbname"))
                        {
                            PORT_NUM = Integer.parseInt(option[1]);
                        }
                        else if(option[0].equals("-dbid"))
                        {
                            PORT_NUM = Integer.parseInt(option[1]);
                        }
                        else if(option[0].equals("-dbpw"))
                        {
                            PORT_NUM = Integer.parseInt(option[1]);
                        }*/


                        else
                        {
                            System.out.println("잘못된 옵션값 지정!!");
                            showOptionValueError();
                            return;
                        }
                    }
                    else
                    {
                        System.out.println("잘못된 옵션값 지정!!!");
                        showOptionValueError();
                        return;
                    }
                    option = null;
                }
            }
            catch (Exception e)
            {
                System.out.println("옵션값 적용중 에러! 올바른 옵션값을 입력하십시오. \n" + e.toString());
                showOptionValueError();
                return;
            }
        }

        //사용가능 cpu코어수.
        int cpu_Processor_count = Runtime.getRuntime().availableProcessors();

        //Epoll 방식은 가장 성능이 좋으나 리눅스에서만 사용가능. Epoll이 사용가능한지 여부를 판별!
        System.out.println(Epoll.isAvailable() ?
                "\nEpoll 모드로 시동...\n" : "\nNio 모드로 시동...\n");

        //이벤트 루프 부분은 싱글스레드로 동작한다!
        //epoll 방식이 사용가능한지를 체크하여 가능한 경우는 epoll, 아닌 경우는 nio로 지정.
        bossGroup = Epoll.isAvailable() ?
                new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);

        //IO 작업 부분의 워커 스레드는 기본값으로 CPU 코어수 * 2 로 지정되어있다
        //epoll 방식이 사용가능한지를 체크하여 가능한 경우는 epoll, 아닌 경우는 nio로 지정.
        workerGroup = Epoll.isAvailable() ?
                new EpollEventLoopGroup(cpu_Processor_count * 2) : new NioEventLoopGroup(cpu_Processor_count * 2);

        //현재 머신의 CPU 코어 개수, 워커 스레드 개수 표시.
        System.out.println("[ CPU => '" + cpu_Processor_count + "' Core, workerThread = '" + cpu_Processor_count * 2 + "' started ]\n");

        System.out.println("rmi_protocol_version = " + RMI.rmi_protocol_version + "\n");


        try {
            //TCP 서버용 부트스트랩 객체 선언!
            serverBootstrap = new ServerBootstrap()

                    //Epoll 모드가 사용가능한지 판별함.
                    .channel(Epoll.isAvailable() ?
                            EpollServerSocketChannel.class : NioServerSocketChannel.class)

                    .group(bossGroup, workerGroup)
                    //부트스트랩에 그룹을 할당한다. 서버의 경우 이벤트수신/작업으로 2개의 그룹을 지정

                    .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator()
                            .respectMaybeMoreData(false).maxMessagesPerRead(cpu_Processor_count*4) )
                    /* serverBootstrap 리스닝 소켓채널 옵션 지정.
                    serverBootstrap 부모 채널의 기본 Default RCVBUF_ALLOCATOR 옵션값은
                    AdaptiveRecvByteBufAllocator 이고 maxMessagesPerRead() 값은 16이다
                    게임서버의 특성상 데이터 길이는 짧고 요청은 매우 많기 때문에 maxMessagesPerRead() 값에 좀 더 여유를 둬서,
                    더 많은 메시지를 처리할 수 있게함. 8 Thread CPU 기준, maxMessagesPerRead() = 8 * 4 = 32 개.
                    관련 링크 하단의 respectMaybeMoreData, maxMessagesPerRead 부분 참조.
                    https://netty.io/4.1/api/io/netty/channel/DefaultMaxMessagesRecvByteBufAllocator.html
                    */

                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //TCP_NODELAY 옵션은 네이글 알고리즘(한꺼번에 모아서 전송)의 사용여부를 결정함.
                    //그리고 서버 소켓을 위한 옵션이 아님! 오직 클라이언트(.childOption)용으로 사용할 것!
                    //나의 경우는 실시간 네트워크 게임이 목적이므로 이를 true로 활성화를 해줌!

                    .childOption(ChannelOption.SO_LINGER, 0)
                    //연결이 끊겼을 때, 소켓버퍼에 남아있는 데이터 처리방식 여부를 지정함. 0=연결 해제시 즉시 소거.


                    .childOption(ChannelOption.SO_RCVBUF, 131072) //128KB 131072
                    //수신버퍼 크기설정.

                    .childOption(ChannelOption.SO_SNDBUF, 131072) //128KB 131072
                    //송신버퍼 크기설정.

                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(48 * 1024, 64 * 1024)) //KB단위 지정.
                    /* 쓰기 버퍼 워터마크 지정.
                    channel 쓰기버퍼에 High WriteBufferWaterMark 이상의 byte가 쌓인다면 channel.isWritable()가 false가 된다.
                    channel.isWritable()가 false가 된 상태에서 channel 쓰기버퍼가 Low WriteBufferWaterMark 이하의 byte가 되었다면
                    다시 channel.isWritable()가 true가 된다.
                    DirectMemory Allocate시 서버측의  쓰기속도가 너무 빠르거나 클라측의 수신속도가 느릴경우,
                    channel 쓰기버퍼가 점점 커지는데, 이때 OutOfMemory Exception이 일어날 수 있기에 그것을 방지하기 위한 옵션.
                    channel.isWritable()과 적절히 활용해야 한다.
                    ex)channel.isWritable() == false시, PendingWriteQueue에 데이터를 보관하고
                    channel.isWritable() == true시, PendingWriteQueue에 쌓인 이벤트를 처리하거나 할 필요가 있다
                    channel.isWritable() 의 true, false 값이 바뀔때마다 InboundHandler 클래스의
                    channelWritabilityChanged(ChannelHandlerContext ctx) 이벤트 콜백 함수가 호출되므로,
                    이곳에서 PendingWriteQueue 관련 데이터 처리를 하면 될 것이다.
                    단, Queue에 쌓을 수 없을정도로 메시지가 많이 와서 OOM이 발생하는 경우에는 실패처리를 해서
                    해당 클라이언트와 연결을 끊거나, 서버의 수용인원을 조절하여 타협을 보거나, 서버의 RAM을 늘리거나 해야한다.
                    관련 링크
                    http://normanmaurer.me/presentations/2014-twitter-meetup-netty/slides.html#8
                    http://normanmaurer.me/presentations/2014-twitter-meetup-netty/slides.html#9
                    */

                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(32768))
                    //1회에 처리하는 TCP패킷 최대 크기 및 처리량 관련 옵션.

                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //채널에서 가져오는 ByteBufAllocator 지정.

                    //.handler(new LoggingHandler(LogLevel.INFO))
                    //서버 부트스트랩에 핸들러를 설정한다. 로그 핸들러가 지정되었으므로 로그 출력!

                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //서버부트스트랩에 할당될 핸들러를 지정한다! ChannelInitializer가 선언되었으므로
                        //채널을 초기화 한다!  채널은 어떻게 보면 네트워크 프로그래밍의 Socket이라고 보면 된다!
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            //채널의 파이프라인을 가져와서 처리할 이벤트 핸들러를 할당한다!
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IPFilterHandler());
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast("HeartBeatDetecter", new ReadTimeoutHandler(2, TimeUnit.MINUTES));
                            p.addLast(new TCP_InBoundHandler());
                            //p.addLast(new TCP_OutBoundHandler());
                        }
                    });


            //UDP 서버용 부투트스트랩 객체!
            udpBootstrap = new Bootstrap()

                    //Epoll 모드가 사용가능한지 판별함.
                    .channel(Epoll.isAvailable() ?
                            EpollDatagramChannel.class : NioDatagramChannel.class)

                    .group(workerGroup)
                    //부트스트랩에 그룹을 할당한다.

                    //.option(EpollChannelOption.MAX_DATAGRAM_PAYLOAD_SIZE, 131072) //128KB 131072

                    //.option(ChannelOption.SO_REUSEADDR, true) //같은 포트 재사용


                    .option(ChannelOption.SO_RCVBUF, 131072) //128KB 131072
                    //수신버퍼 크기설정.

                    .option(ChannelOption.SO_SNDBUF, 131072) //128KB 131072
                    //송신버퍼 크기설정.

                    .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(48 * 1024, 64 * 1024))
                    /* 쓰기 버퍼 워터마크 지정.
                    channel 쓰기버퍼에 High WriteBufferWaterMark 이상의 byte가 쌓인다면 channel.isWritable()가 false가 된다.
                    channel.isWritable()가 false가 된 상태에서 channel 쓰기버퍼가 Low WriteBufferWaterMark 이하의 byte가 되었다면
                    다시 channel.isWritable()가 true가 된다.
                    DirectMemory Allocate시 서버측의  쓰기속도가 너무 빠르거나 클라측의 수신속도가 느릴경우,
                    channel 쓰기버퍼가 점점 커지는데, 이때 OutOfMemory Exception이 일어날 수 있기에 그것을 방지하기 위한 옵션.
                    channel.isWritable()과 적절히 활용해야 한다.
                    ex)channel.isWritable() == false시, PendingWriteQueue에 데이터를 보관하고
                    channel.isWritable() == true시, PendingWriteQueue에 쌓인 이벤트를 처리하거나 할 필요가 있다
                    channel.isWritable() 의 true, false 값이 바뀔때마다 InboundHandler 클래스의
                    channelWritabilityChanged(ChannelHandlerContext ctx) 이벤트 콜백 함수가 호출되므로,
                    이곳에서 PendingWriteQueue 관련 데이터 처리를 하면 될 것이다.
                    단, Queue에 쌓을 수 없을정도로 메시지가 많이 와서 OOM이 발생하는 경우에는 실패처리를 해서
                    해당 클라이언트와 연결을 끊거나, 서버의 수용인원을 조절하여 타협을 보거나, 서버의 RAM을 늘리거나 해야한다.
                    관련 링크
                    http://normanmaurer.me/presentations/2014-twitter-meetup-netty/slides.html#8
                    http://normanmaurer.me/presentations/2014-twitter-meetup-netty/slides.html#9
                    */

                    //.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(32768))
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(32768)
                            .respectMaybeMoreData(false).maxMessagesPerRead(5))
                    //1회에 처리하는 데이터그램 최대 크기 및 처리량 관련 옵션.

                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //채널에서 가져오는 ByteBufAllocator 지정

                    //.handler(new LoggingHandler(LogLevel.INFO))
                    //서버 부트스트랩에 핸들러를 설정한다. 로그 핸들러가 지정되었으므로 로그 출력!

                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        public void initChannel(DatagramChannel ch) {
                            //채널의 파이프라인을 가져와서 처리할 이벤트 핸들러를 할당한다!
                            ChannelPipeline p = ch.pipeline();
                            //p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            p.addLast(new UDP_InBoundHandler());
                            //p.addLast(new UDP_OutBoundHandler());
                        }
                    });


            //설정된 최대 접속자수만큼 UDP채널을 미리 할당함.
            initUDPChannel(client_Max_Connection);

            System.out.println("TCP 채널 초기화중...");

            //TCP채널 개설. 이제 서버를 가동시킴! 지정한 포트에서의 접속을 감시함!
            //ChannelFuture channelFuture = serverBootstrap.bind( new InetSocketAddress("127.0.0.1", PORT_NUM) ).sync();
            ChannelFuture channelFuture = serverBootstrap.bind(PORT_NUM).sync();

            System.out.println("TCP 채널 준비완료");



            //게임 데이터 초기화.
            GameDataManager.initGameData();
            //GameDataManager.initGameDataManager();

            //SkillFactory 초기화
            SkillFactory.initFactory();

            //MonsterFactory 초기화
            MonsterFactory.initFactory();

            //세션 매니저 초기화.
            SessionManager.initSessionManager();

            //매칭 매니저 초기화.
            MatchingManager.initMatchingManager();

            // 맵 매니저 초기화
            MapDataManager.initMapData();

            MapFactory.initFactory();


            /** 2020 05 08 */
            // 채팅매니저 초기화
            ChattingManager.initChattingManager();
            // 로그매니저 초기화
            LogManager.initLogManager();




            //팩토리 초기화
            AttackTurretFactory.initFactory();
            BuffTurretFactory.initFactory();
            BarricadeFactory.initFactory();
            RewardFactory.initFactory();

            //로직처리시 걸린 시간.
            elapsedLogicTime = System.nanoTime() - startLogicTime;  //nano sec 단위.
            double msElapsedLogicTime = (double) elapsedLogicTime * 0.000001; //100만으로 나눔.

            System.out.println("\n[ 서버 가동 소요시간 : "+String.format("%.3f", msElapsedLogicTime)+" ms ]");
            System.out.println("\n["+ RMI.getCurrentTime()+"] 서버 가동 완료 ["+channelFuture.channel().localAddress().toString().replace('/',' ')+" ]");

            //서버 리스닝 소켓이 닫힐때 까지 블로킹!
            channelFuture.channel().closeFuture().sync();


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Server 종료 및 사용자원 해제...");
            //사용하던 자원을 해제해 준다!
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("Server 종료 완료됨.");
        }
    }//initServer 종료 부분.


    //설정된 최대 접속자수만큼 UDP채널을 미리 할당후, udpChannels_Queue에 넣어둔다.
    //유저가 로그인/로그아웃시마다 꺼내고 반환하는것을 반복함.
    private void initUDPChannel(int connectionCount) {
        System.out.println("UDP 채널 [" + connectionCount + "] Connection 초기화중...");

        //최대 5만 제한.
        if (connectionCount > 50000)
            connectionCount = 50000;

        int startBindPort = 10000;

        //Queue에 connectionCount개수 만큼의 값이 들어가게끔 지정.
        while (udpChannel_Queue.size() < connectionCount && startBindPort < 65535) {

            //UDP채널 개설 가능여부 확인..
            ChannelFuture udp_channelFuture = null;

            try {
                //udp_channelFuture = udpBootstrap.bind( new InetSocketAddress("127.0.0.1", startBindPort) ).sync();
                udp_channelFuture = udpBootstrap.bind(startBindPort).sync();

                //바인딩 된 UDP 채널을 넣음.
                udpChannel_Queue.offer(udp_channelFuture.channel());
            } catch (Exception e) {
                //만약 바인드 할수 없는, 이미 사용중인 Port라면, 다음 번호로 pass한다.
                continue;
            } finally {
                startBindPort++;
            }
        }

        System.out.println("UDP 채널 [" + udpChannel_Queue.size() + "] Connection 준비완료");
    }

    //연결시 UDP채널을 할당함. 단, 더이상 연결할 수 없을 경우 null 리턴.
    public static Channel popUDPChannel() {
        if (udpChannel_Queue.size() > 0)
            return udpChannel_Queue.poll();
        else
            return null;
    }

    //연결해제시 UDP채널을 반환함.
    public static void pushUDPChannel(Channel return_data) {

        return_data.disconnect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                //리스너 제거
                future.removeListener(this);

                //disconnect()가 성공했다면
                if(future.isSuccess())
                {
                    if(udpChannel_Queue.offer( future.channel() ))
                    {
                        if(udpChannel_Queue.size() == client_Max_Connection)
                            System.out.println("(udpChannel_Queue.size() == client_Max_Connection)\n모든 유저의 정상 접속 해제 완료!");

                        else if(udpChannel_Queue.size() > client_Max_Connection)
                            throw new IllegalStateException("UDP Queue에 처음에 할당된 채널수보다 많은 채널이 들어감! 에러!");
                    }
                    else
                    {
                        throw new IllegalStateException("UDP Queue.offer() 채널 반환 실패!");
                    }
                }
                //disconnect()에 실패했다면
                else
                {
                    System.out.println("future.channel().disconnect() 실패 : "+future.channel().localAddress());
                    throw new IllegalStateException("future.channel().disconnect() 실패 : "+future.channel().localAddress());
                }
            }
        });
    }

    //접속자수 1증가.
    public static int incrementUserConnection() {
        return countUserConnection.incrementAndGet();
    }

    //접속자수 1감소.
    public static int decrementUserConnection() {
        return countUserConnection.decrementAndGet();
    }

    //접속 제한수를 넘지않았는지 체크.
    public static boolean checkClient_Max_Connection() {
        //최대 접속자수에 도달했거나, 할당할 UDP채널이 없는 경우.
        if (countUserConnection.get() >= client_Max_Connection || udpChannel_Queue.size() == 0) {
            return false;
        }
        //접속 가능상태.
        else
            return true;
    }

    public static int getClient_Max_Connection() {
        return client_Max_Connection;
    }
}
