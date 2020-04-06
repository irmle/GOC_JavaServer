package RMI.RMI_LogicMessages.client_to_server;

import ECS.Game.MatchingManager;
import RMI.RMI_Classes.*;

public class Logic_requestMatching {

    //추후, 맵의 종류를 골라서 매칭할 수 있도록 selectedMapType 변수를 추가함.
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, int selectedMapType)
    {
        //매칭 시작.
        MatchingManager.startMatching(googleIDToken, rmi_id);
    }
}