package Network.RMI_LogicMessages.client_to_server;

import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import Network.RMI_Classes.*;

public class Logic_gameSceneLoadingPercentage {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, float percentage)
    {
        WorldMap targetWorldMap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        //로딩시, 퍼센테이지 갱신!
        if(targetWorldMap != null)
            targetWorldMap.updateUserLoadingProgress(rmi_id, percentage);
    }
}