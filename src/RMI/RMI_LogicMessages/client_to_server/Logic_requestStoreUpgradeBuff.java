package RMI.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.ClientAction;
import ECS.ActionQueue.Upgrade.ActionStoreUpgrade;
import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Common.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import java.util.LinkedList;
public class Logic_requestStoreUpgradeBuff {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int storeUpgradeType, int buffLevel)
    {
        //do something.

        /* 월드를 검색한다 */
        WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        /* 상점 버프 업그레이드를 위한 요청액션 생성 */
        ActionStoreUpgrade actionStoreUpgrade = new ActionStoreUpgrade(userEntityID, storeUpgradeType, buffLevel);

        actionStoreUpgrade.actionType = ClientAction.ActionType.ACTION_STORE_UPGRADE;

        /* 큐에 넣어준다 */
        worldmap.enqueueClientAction(actionStoreUpgrade);



    }
}