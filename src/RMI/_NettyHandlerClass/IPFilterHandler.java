package RMI._NettyHandlerClass;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import io.netty.handler.ipfilter.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ConcurrentSet;

//TCP연결 Accept시, Ban된 IP인지 검사하는 부분.
//Ban되어있는 IP라면 바로 연결을 끊도록 한다.
@ChannelHandler.Sharable
public class IPFilterHandler extends AbstractRemoteAddressFilter<InetSocketAddress> {

    //IP밴 리스트
    private static final ConcurrentSet<InetAddress> ipBanList = new ConcurrentSet<>();


    @Override //accept()에서 true를 반환하면 연결 수락, false를 반환하면 연결 차단.
    protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception {

        //접속을 시도한 IP주소/포트번호.
        InetAddress acceptedIPAddress = remoteAddress.getAddress();
        int acceptedPort = remoteAddress.getPort();

        //접속을 시도한 IP가 밴되어있는 IP인지 체크
        //ip가 밴리스트에 존재하는 IP라면 accept()에서 false를 return 하고 차단.
        if (ipBanList.contains(acceptedIPAddress)) {
            System.out.println("[IPFilterHandler] IP밴 처리된 유저가 접속시도... IP [" + acceptedIPAddress.getHostAddress() + ":" + acceptedPort + "]");
            return false;
        }
        //정상적인 IP라면 accept()에서 true를 return 하고 연결 수락.
        else
        {
            //ipBanList.remove(acceptedIPAddress);
            //System.out.println("접속 성공! IP [" + acceptedIPAddress.getHostAddress() + ":" + acceptedPort + "]");
            return true;
        }
    }


    //IP밴 목록에 추가함.
    public static boolean addIPBanList(InetSocketAddress bannedIP) {
        if(ipBanList.add(bannedIP.getAddress()))
        {
            System.out.println("[IPFilterHandler] addIPBanList : "+bannedIP.getAddress().getHostAddress());
            return true;
        }
        else
            return false;
    }

    //IP밴 목록에 추가함.
    public static boolean addIPBanList(InetAddress bannedIP) {
        if(ipBanList.add(bannedIP))
        {
            System.out.println("[IPFilterHandler] addIPBanList : "+bannedIP.getHostAddress());
            return true;
        }
        else
            return false;
    }

    //IP밴 목록에 추가함.
    public static boolean addIPBanList(String bannedIP) //192.168.1.1
    {
        try {
            if(ipBanList.add(InetAddress.getByName(bannedIP)))
            {
                System.out.println("[IPFilterHandler] addIPBanList : "+bannedIP);
                return true;
            }
            else
                return false;
        } catch (UnknownHostException e) {
            System.out.println("잘못된 형식의 IP 문자열 입력 : "+bannedIP);
            return false;
        }
    }

    //IP밴 목록에서 제거함.
    public static boolean removeIPBanList(InetSocketAddress bannedIP) {
        if(ipBanList.remove(bannedIP.getAddress()))
        {
            System.out.println("[IPFilterHandler] removeIPBanList : "+bannedIP.getAddress().getHostAddress());
            return true;
        }
        else
            return false;
    }

    //IP밴 목록에서 제거함.
    public static boolean removeIPBanList(InetAddress bannedIP) {
        if(ipBanList.remove(bannedIP))
        {
            System.out.println("[IPFilterHandler] removeIPBanList : "+bannedIP.getHostAddress());
            return true;
        }
        else
            return false;
    }

    //IP밴 목록에 추가함.
    public static boolean removeIPBanList(String bannedIP) //192.168.1.1
    {
        try {
            if(ipBanList.remove(InetAddress.getByName(bannedIP)))
            {
                System.out.println("[IPFilterHandler] removeIPBanList : "+bannedIP);
                return true;
            }
            else
                return false;
        } catch (UnknownHostException e) {
            System.out.println("잘못된 형식의 IP 문자열 입력 : "+bannedIP);
            return false;
        }
    }

    //IP밴 목록에서 모든 IP를 제거하여 IP밴 목록을 초기화 한다.
    public static void clearIPBanList() {
        ipBanList.clear();
    }

    //IP밴 목록의 항목 수를 반환
    public static int getIPBanCount() {
        return ipBanList.size();
    }

    //IP밴 목록 반환
    public static InetAddress[] getIPBanList() {
        return ipBanList.toArray(new InetAddress[0]);
    }
}
