package RMI.RMI_LogicMessages.client_to_server;

import ECS.Game.GameSessionRoom;
import ECS.Game.MatchingManager;
import RMI.RMI_Classes.*;

public class Logic_pickCancel {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, String googleIDToken)
    {
        //유저가 선택취소를 눌렀다면
        GameSessionRoom gameSessionRoom = MatchingManager.findGameSessionRoomFromTokenID(googleIDToken);

        if(gameSessionRoom != null)
        {
            gameSessionRoom.userOnCancel( googleIDToken );
        }
    }
}