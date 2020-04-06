package RMI.RMI_LogicMessages.client_to_server;

import ECS.Game.GameSessionRoom;
import ECS.Game.MatchingManager;
import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Common.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import java.util.LinkedList;
public class Logic_pickLogicIsVoipHostReady {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, boolean isVoipHostReady)
    {
        //보이스채팅 서버준비가 완료되었다면
        GameSessionRoom gameSessionRoom = MatchingManager.findGameSessionRoomFromTokenID(googleIDToken);

        //나머지 유저들에게 보이스채팅이 완료됨을 중계한다.
        gameSessionRoom.voiceChatServerOnReady();
    }
}