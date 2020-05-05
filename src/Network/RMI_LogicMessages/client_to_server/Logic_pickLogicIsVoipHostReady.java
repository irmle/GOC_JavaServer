package Network.RMI_LogicMessages.client_to_server;

import ECS.Game.GameSessionRoom;
import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import Network.RMI_Classes.*;

public class Logic_pickLogicIsVoipHostReady {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, boolean isVoipHostReady)
    {
        //보이스채팅 서버준비가 완료되었다면
        GameSessionRoom gameSessionRoom = MatchingManager.findGameSessionRoomFromTokenID(googleIDToken);

        //나머지 유저들에게 보이스채팅이 완료됨을 중계한다.
        if(gameSessionRoom != null)
            gameSessionRoom.voiceChatServerOnReady(isVoipHostReady);

        //gameSessionRoom이 null이라면 이미 게임이 시작되었거나, 잘못된 패킷이다.
        else
        {
            //이미 게임이 시작된 경우를 다시 판별하여 호출한다.
            WorldMap worldMap = MatchingManager.findWorldMapFromTokenID(googleIDToken);
            if(worldMap != null)
            {
                //나머지 유저들에게 보이스채팅이 완료됨을 중계한다.
                worldMap.voiceChatServerOnReady(isVoipHostReady);
            }
        }
    }
}