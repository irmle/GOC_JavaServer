package Network.RMI_Classes;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

//기본적으로 RMI_ID는 단일 지정!
public class RMI_ID {

    //모든 RMI_Connection 값이 저장된 HashMap. 소켓 주소값을 기준으로 찾음.
    private static final ConcurrentHashMap<Channel, RMI_ID> RMI_CONNECTION_List = new ConcurrentHashMap<>();

    //모든 RMI_HOST_ID값이 저장된 HashMap. id값을 기준으로 찾음.
    private static final ConcurrentHashMap<Integer, RMI_ID> RMI_HOST_ID_List = new ConcurrentHashMap<>();

    //모든 RMI_UNIQUE_ID값이 저장된 HashMap. id값을 기준으로 찾음.
    private static final ConcurrentHashMap<Integer, RMI_ID> RMI_UNIQUE_ID_List = new ConcurrentHashMap<>();



    //접속 계정마다 부여되는 고유id.
    private static AtomicInteger rmi_id_count = new AtomicInteger(Integer.MIN_VALUE);

    //NONE일 경우, 송신하지 않음! 모든 인자가 null 값.
    public static RMI_ID NONE = new RMI_ID(0, -1, null, null);

    //ALL일 경우, 서버 전체 유저에게 보낼 수 있는 ChannelGroup정보 보유.
    public static RMI_ID ALL = new RMI_ID(0, -2, null, null);

    public static RMI_ID SERVER = new RMI_ID(Integer.MAX_VALUE, 0, null, null);

    public static RMI_ID MYSELF = new RMI_ID(0, -10, null, null);



    //고유 RMI_HOST_ID. 커넥션 별로 1개씩 고유값.
    public int rmi_host_id;

    //고유 Unique_ID; //캐릭터 고유 id등을 지정할 것.
    public int unique_id;


    //TCP의 경우 Channel에 직접쓰는것 보다, ChannelHandlerContext에 쓰는것이 퍼포먼스적으로 더 낫기때문에 추가된 변수.
    //UDP의 경우 ChannelHandlerContext를 사용할 수 없으므로 기존의 방식을 유지.

    //대상 소켓 접근자
    private ChannelHandlerContext socketTCPHandler; //클라이언트or서버 channelHandler
    private ChannelHandlerContext socketUDPHandler; //클라이언트or서버 channelHandler

    private Channel socketTCP; //클라이언트or서버 channel
    private Channel socketUDP; //클라이언트or서버 channel

    //RMI ID별로 가지고있는 Key정보.
    public RMI_EncryptManager.EncryptKeyInfo AESKey;

    //UDP연결작업이 완료되었는지 여부.
    public boolean isUDPConnectionAvailable;


    public RMI_ID(int rmi_host_id, int unique_id, Channel socketTCP, Channel socketUDP) {
        this.rmi_host_id = rmi_host_id;
        this.unique_id = unique_id;
        this.socketTCP = socketTCP;
        this.socketUDP = socketUDP;
        this.AESKey = new RMI_EncryptManager.EncryptKeyInfo();
        this.isUDPConnectionAvailable = false;
    }

    public static void resetRMI_ID()
    {
        RMI_CONNECTION_List.clear();
        RMI_HOST_ID_List.clear();
        RMI_UNIQUE_ID_List.clear();

        rmi_id_count = new AtomicInteger(Integer.MIN_VALUE);

        NONE = null;
        ALL = null;
        SERVER = null;
        MYSELF = null;

        NONE = new RMI_ID(0, -1, null, null);
        ALL = new RMI_ID(0, -2, null, null);
        SERVER = new RMI_ID(Integer.MAX_VALUE, 0, null, null);
        MYSELF = new RMI_ID(0, -10, null, null);
        System.out.println("resetRMI_ID()");
    }

    public static int getNewRMI_HOST_ID()
    {
        int value = rmi_id_count.getAndIncrement();

        //값 초기화.
        if (value == Integer.MAX_VALUE-1)
            rmi_id_count = new AtomicInteger(Integer.MIN_VALUE);

        //0일시, 1 반환.
        if (value == 0)
            value = rmi_id_count.getAndIncrement();
        return value;
    }

    public static RMI_ID[] getArray(Collection<RMI_ID> values)
    {
        RMI_ID[] arr = new RMI_ID[values.size()];
        return values.toArray(arr);
    }

    public ChannelHandlerContext getTCP_ObjectHandler()
    {
        return this.socketTCPHandler;
    }

    public ChannelHandlerContext getUDP_ObjectHandler()
    {
        return this.socketUDPHandler;
    }


    public Channel getTCP_Object()
    {
        return this.socketTCP;
    }

    public Channel getUDP_Object()
    {
        return this.socketUDP;
    }

    public void setTCP_ObjectHandler(ChannelHandlerContext obj)
    {
        this.socketTCPHandler = obj;
    }
    public void setUDP_ObjectHandler(ChannelHandlerContext obj)
    {
        this.socketUDPHandler = obj;
    }

    public void setTCP_Object(Channel obj)
    {
        this.socketTCP = obj;
    }
    public void setUDP_Object(Channel obj)
    {
        this.socketUDP = obj;
    }

    public static RMI_ID createRMI_ID(Channel newConnection)
    {
        if (RMI_CONNECTION_List.containsKey(newConnection))
            throw new IllegalArgumentException("createRMI_ID 잘못된 처리. 이미 RMI ID가 발급된 Key입니다.");

        int hostid = getNewRMI_HOST_ID();
        RMI_ID newRMI_ID = new RMI_ID(hostid, RMI_ID.NONE.unique_id, newConnection, null);
        RMI_CONNECTION_List.put(newConnection, newRMI_ID);

        //새로 발급되었으므로 Host List에도 등록한다!
        setHostID(hostid, newRMI_ID);

        //System.out.println("새로운 유저 등록 : " + hostid);
        return newRMI_ID;
    }

    public static void setUniqueID(int unique_id, RMI_ID rmi_ID)
    {
        rmi_ID.unique_id = unique_id;
        if (RMI_UNIQUE_ID_List.containsKey(unique_id))
            System.out.println("RMI_Unique_ID : 이미 같은 Key가 존재함. 덮어씌움");
        RMI_UNIQUE_ID_List.put(unique_id, rmi_ID);
    }

    public static void setHostID(int rmi_host_id, RMI_ID rmi_ID)
    {
        rmi_ID.rmi_host_id = rmi_host_id;
        if (RMI_HOST_ID_List.containsKey(rmi_host_id))
            System.out.println("RMI_HOST_ID : 이미 같은 Key가 존재함. 덮어씌움");

        RMI_HOST_ID_List.put(rmi_host_id, rmi_ID);
    }

    public static RMI_ID findRMI_Connection(Channel connection) {
        if (!RMI_CONNECTION_List.containsKey(connection))
            System.out.println("findRMI_Connection : Key가 존재하지 않음!");
        return RMI_CONNECTION_List.get(connection);
    }

    public static RMI_ID findRMI_UNIQUE_ID(int unique_id) {
        if (!RMI_UNIQUE_ID_List.containsKey(unique_id))
            System.out.println("RMI_Unique_ID : Key가 존재하지 않음!");
        return RMI_UNIQUE_ID_List.get(unique_id);
    }

    public static RMI_ID findRMI_HOST_ID(int rmi_host_id) {
        if (!RMI_HOST_ID_List.containsKey(rmi_host_id))
            System.out.println("findRMI_HOST_ID : Key가 존재하지 않음!");
        return RMI_HOST_ID_List.get(rmi_host_id);
    }


    public static void removeRMI_ID(Channel connection)
    {
        if (RMI_CONNECTION_List.containsKey(connection))
        {
            RMI_ID rmi_id = RMI_CONNECTION_List.get(connection);

            if (RMI_UNIQUE_ID_List.containsKey(rmi_id.unique_id))
                RMI_UNIQUE_ID_List.remove(rmi_id.unique_id);

            if (RMI_HOST_ID_List.containsKey(rmi_id.rmi_host_id))
                RMI_HOST_ID_List.remove(rmi_id.rmi_host_id);

            RMI_CONNECTION_List.remove(connection);

            rmi_id = null;
        }
    }
}
