package RMI.RMI_LogicMessages;

import ECS.Game.GameSessionRoom;
import ECS.Game.MatchingManager;
import ECS.Game.SessionManager;
import ECS.Game.WorldMap;
import RMI.RMI_Classes.RMI_ID;
import RMI._NettyHandlerClass.TCP_InBoundHandler;
import io.netty.channel.ChannelHandlerContext;


public class OnDisconnected {

    public static void OnDisconnected(ChannelHandlerContext ctx)
    {
        //기존에 접속한 유저들에게 새로 접속한 유저의 데이터를 송신.
        //자기자신은 제외한 범위.
        RMI_ID disconnectID = ctx.channel().attr(TCP_InBoundHandler.getAttrKey()).get();

        if(disconnectID != null)
        {
            //System.out.println("OnDisconnected disconnectID != null");

            //유저가 로그아웃 하였을 경우, 매칭중이라면 매칭 취소.
            String tokenID = SessionManager.findTokenIDfromRMI_HostID(disconnectID.rmi_host_id);
            if(tokenID != null)
            {
                //매칭 대기열에서 제거!
                MatchingManager.cancelMatching( tokenID );

                //게임세션에서 해당 유저의 접속이 해제되었다는 것을 중계함
                GameSessionRoom room = MatchingManager.findGameSessionRoomFromTokenID( tokenID );
                if(room != null)
                    room.userOnDisconnected( tokenID, disconnectID);

                //유저가 게임플레이 중, 접속이 끊겼을 경우,
                WorldMap targetWorldMap = MatchingManager.findWorldMapFromTokenID(tokenID);

                //유저의 연결이 끊겼을때의 처리를 행한다.
                if(targetWorldMap != null)
                    targetWorldMap.userLeftWorldMap(tokenID, disconnectID);
            }

            //유저가 로그아웃 하였을 경우, RMI_HostID로부터 리스트에서 제거 후 초기화 할것. 이 부분은 맨 마지막에 호출될 것.
            SessionManager.logout(disconnectID.rmi_host_id);
        }
    }
}
