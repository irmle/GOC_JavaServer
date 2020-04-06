package RMI.RMI_LogicMessages.client_to_server;

import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import RMI.RMI_Classes.*;

public class Logic_requestReconnectWorldMap {

    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, String googleIDToken)
    {
        //클라이언트로부터 재접속 요청이 온 경우.
        WorldMap targetWorldMap = MatchingManager.findWorldMapFromTokenID(googleIDToken);

        if(targetWorldMap != null)
        {
            targetWorldMap.userReconnectWorldMap(googleIDToken, rmi_id);
        }
    }
}