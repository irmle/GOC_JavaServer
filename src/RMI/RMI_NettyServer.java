package RMI;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

import ECS.Game.*;
import ECS.Factory.*;

import RMI.RMI_Classes.PooledFlatBufferBuilder;
import RMI._NettyHandlerClass.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class RMI_NettyServer {



//서버 기본값 설정 부분.
//====================================================================

    //게임서버 리스닝 포트.
    private static int PORT_NUM = 65005; //default 값.
    //private static int PORT_NUM = 65006; //default 값.


    //게임서버 최대 동접자 수 제한.
    private static int client_Max_Connection = 500; //default 값.

//====================================================================


    public RMI_NettyServer(){}

    //설정된 최대 동접자수 만큼의 UDP채널 할당. //1개 클라당 1개 UDP소켓 연결. <추후 변경 가능>
    private static ConcurrentLinkedQueue<Integer> udpChannelPort_Queue = new ConcurrentLinkedQueue<>();

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
                            RMI_.rmi_protocol_version = Integer.parseInt(option[1]);
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

        //FlatBufferBuilder 재사용 용도의 풀링객체 초기화
        PooledFlatBufferBuilder.initPooledFlatBufferBuilder( cpu_Processor_count*2 );

        //이벤트 루프 부분의 이벤트 전용 쓰레드는 1개로 지정되어있으므로 싱글스레드로 동작한다!
        //리눅스 전용 Epoll 방식. 가장 성능이 좋으나 리눅스에서만 사용가능. Epoll이 사용가능한지 여부를 판별!
        if(Epoll.isAvailable())
        {
            System.out.println("\nEpoll 모드로 시동...\n");
            bossGroup = new EpollEventLoopGroup(1);
            workerGroup = new EpollEventLoopGroup(cpu_Processor_count*2); //기본값은 CPU 코어수 * 2
        }
        else //리눅스가 아닐경우 Epoll사용이 불가능하다! 그러므로 비동기 Nio방식으로 동작할것!
        {
            System.out.println("\nNio 모드로 시동...\n");
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup(cpu_Processor_count*2); //기본값은 CPU 코어수 * 2
        }
        //현재 머신의 CPU 코어 개수, 워커 스레드 개수 표시.
        System.out.println("[ CPU => '"+cpu_Processor_count+"' Core, workerThread = '"+cpu_Processor_count*2+"' started ]\n");

        System.out.println("rmi_protocol_version = "+RMI_.rmi_protocol_version+"\n");


        try {
            //TCP 서버용 부트스트랩 객체 선언!
            serverBootstrap = new ServerBootstrap();

            //UDP 서버용 부투트스트랩 객체!
            udpBootstrap = new Bootstrap();

            //Epoll 모드가 사용가능한지 판별함. Epoll 모드를 지원하는 리눅스의 경우 true, 그 이외의 OS 라면 false를 반환함.
            if(Epoll.isAvailable())
                serverBootstrap.channel(EpollServerSocketChannel.class); //Epoll 모드!
            else
                serverBootstrap.channel(NioServerSocketChannel.class);   //Nio 모드!

            //Epoll 모드가 사용가능한지 판별함. Epoll 모드를 지원하는 리눅스의 경우 true, 그 이외의 OS 라면 false를 반환함.
            if(Epoll.isAvailable())
                udpBootstrap.channel(EpollDatagramChannel.class); //Epoll UDP채널
            else
                udpBootstrap.channel(NioDatagramChannel.class);   //Nio UDP채널


            serverBootstrap
                    .group(bossGroup, workerGroup)
                    //부트스트랩에 그룹을 할당한다. 서버의 경우 이벤트수신/작업으로 2개의 그룹을 지정

                    .childOption(ChannelOption.TCP_NODELAY , true)
                    //TCP_NODELAY 옵션은 네이글 알고리즘(한꺼번에 모아서 전송)의 사용여부를 결정함.
                    //그리고 서버 소켓을 위한 옵션이 아님! 오직 클라이언트(.childOption)용으로 사용할 것!
                    //나의 경우는 실시간 네트워크 게임이 목적이므로 이를 true로 활성화를 해줌!

                    .childOption(ChannelOption.SO_LINGER, 0)
                    //연결이 끊겼을 때, 소켓버퍼에 남아있는 데이터 처리방식 여부를 지정함. 0=연결 해제시 즉시 소거.

                    //.handler(new LoggingHandler(LogLevel.INFO))
                    //서버 부트스트랩에 핸들러를 설정한다. 로그 핸들러가 지정되었으므로 로그 출력!

                    //.childOption(ChannelOption.SO_RCVBUF , 196608) //192KB
                    //수신버퍼 크기 지정. byte

                    .childOption(ChannelOption.ALLOCATOR , PooledByteBufAllocator.DEFAULT)
                    //채널에서 가져오는 ByteBufAllocator 지정

                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //서버부트스트랩에 할당될 핸들러를 지정한다! ChannelInitializer가 선언되었으므로
                        //채널을 초기화 한다!  채널은 어떻게 보면 네트워크 프로그래밍의 Socket이라고 보면 된다!

                        protected void initChannel(SocketChannel ch) {

                            //채널의 파이프라인을 가져와서 처리할 이벤트 핸들러를 할당한다!
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            p.addLast("HeartBeatDetecter", new ReadTimeoutHandler(60, TimeUnit.SECONDS));
                            p.addLast("ByteSizeFilterHandler", new ByteSizeFilterHandler());
                            p.addLast("PacketProcess_Recv", new TCP_InBoundHandler());
                            p.addLast("PacketProcess_Send", new TCP_OutBoundHandler());
                        }
                    });


            udpBootstrap
                    .group(workerGroup)
                    //부트스트랩에 그룹을 할당한다.

                    .option(ChannelOption.RCVBUF_ALLOCATOR , new FixedRecvByteBufAllocator(65536))
                    //1회에 받아오는 데이터그램 최대 크기.

                    //.option(ChannelOption.SO_KEEPALIVE, true)

                    //.handler(new LoggingHandler(LogLevel.INFO))
                    //서버 부트스트랩에 핸들러를 설정한다. 로그 핸들러가 지정되었으므로 로그 출력!

                    .option(ChannelOption.SO_RCVBUF, 524288) //512KB
                    //수신버퍼 크기설정.

                    .option(ChannelOption.ALLOCATOR , PooledByteBufAllocator.DEFAULT)
                    //채널에서 가져오는 ByteBufAllocator 지정

                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        public void initChannel(DatagramChannel ch)
                        {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            p.addLast(new UDP_InBoundHandler());
                            p.addLast(new UDP_OutBoundHandler());
                        }
                    });


            //설정된 최대 접속자수만큼 UDP채널을 미리 할당함.
            initUDPChannel(client_Max_Connection);

            System.out.println("TCP 채널 초기화중...");

            //TCP채널 개설. 이제 서버를 가동시킴! 지정한 포트에서의 접속을 감시함!
            //ChannelFuture channelFuture = serverBootstrap.bind( new InetSocketAddress("127.0.0.1", PORT_NUM) ).sync();
            ChannelFuture channelFuture = serverBootstrap.bind( PORT_NUM ).sync();

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

            //팩토리 초기화
            AttackTurretFactory.initFactory();
            BuffTurretFactory.initFactory();
            BarricadeFactory.initFactory();
            RewardFactory.initFactory();

            //로직처리시 걸린 시간.
            elapsedLogicTime = System.nanoTime() - startLogicTime;  //nano sec 단위.
            double msElapsedLogicTime = (double) elapsedLogicTime * 0.000001; //100만으로 나눔.

            System.out.println("\n[ 서버 가동 소요시간 : "+String.format("%.3f", msElapsedLogicTime)+" ms ]");
            System.out.println("\n["+RMI_.getCurrentTime()+"] 서버 가동 완료 ["+channelFuture.channel().localAddress().toString().replace('/',' ')+" ]");

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
    private void initUDPChannel(int connectionCount)
    {
        System.out.println("UDP 채널 ["+connectionCount+"] Connection 초기화중...");

        //최대 5만 제한.
        if(connectionCount>60000)
            connectionCount = 50000;

        int startBindPort = 10000;

        //Queue에 connectionCount개수 만큼의 값이 들어가게끔 지정.
        while( udpChannelPort_Queue.size() < connectionCount )
        {
            //UDP채널 개설 가능여부 확인..
            ChannelFuture udp_channelFuture = null;

            try {
                //udp_channelFuture = udpBootstrap.bind( new InetSocketAddress("127.0.0.1", startBindPort) ).sync();
                udp_channelFuture = udpBootstrap.bind( startBindPort ).sync();

                //연결가능한 port임을 확인했으면 다시 닫아둔다.
                udp_channelFuture.channel().close().sync();

                //바인딩 된 UDP 포트정보를 넣음.
                udpChannelPort_Queue.offer( startBindPort );

            } catch (Exception e) {

                continue;

            }
            finally {
                startBindPort++;
            }
        }

        System.out.println("UDP 채널 ["+udpChannelPort_Queue.size()+"] Connection 준비완료");
    }

    //연결시 UDP채널을 할당함. 단, 더이상 연결할 수 없을 경우 null 리턴.
    public static Channel popUDPChannel()
    {
        ChannelFuture udp_channelFuture = null;

        if(udpChannelPort_Queue.size()>0)
        {
            try {
                udp_channelFuture = udpBootstrap.bind( udpChannelPort_Queue.poll() ).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
            return udp_channelFuture.channel();
        }
        else
            return null;
    }

    //연결해제시 UDP채널을 반환함.
    public static void pushUDPChannel(int return_data)
    {
        udpChannelPort_Queue.offer(return_data);
    }

    //접속자수 1증가.
    public static int incrementUserConnection()
    {
        return countUserConnection.incrementAndGet();
    }

    //접속자수 1감소.
    public static int decrementUserConnection()
    {
        return countUserConnection.decrementAndGet();
    }

    //접속 제한수를 넘지않았는지 체크.
    public static boolean checkClient_Max_Connection()
    {
        //최대 접속자수에 도달했거나, 할당할 UDP채널이 없는 경우.
        if(countUserConnection.get() >= client_Max_Connection || udpChannelPort_Queue.size() == 0)
        {
            return false;
        }
        //접속 가능상태.
        else
            return true;
    }

    public static int getClient_Max_Connection()
    {
        return client_Max_Connection;
    }
}
