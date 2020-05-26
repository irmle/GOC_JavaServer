package Network.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.Build.ActionUpgradeBarricade;
import ECS.ActionQueue.ClientAction;
import ECS.Entity.CharacterEntity;
import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import Network.RMI_Classes.*;

public class Logic_upgradeBarricade {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int barricadeEntityID, int barricadeType)
    {
        System.out.println("바리케이드 업그레이드 요청을 받았습니다. ");

        /* 유저가 속한 월드를 찾는다 */
        WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);
        CharacterEntity character = worldmap.characterEntity.get(userEntityID);
        if( worldmap.checkUserIsDead(character)){

            return;
        }

        /* 건설 Action 객체를 생성한다 */
        ActionUpgradeBarricade action = new ActionUpgradeBarricade(userEntityID, barricadeEntityID);
        action.actionType = ClientAction.ActionType.ACTION_UPGRADE_BARRICADE;

        /* 생성한 Action 객체를 월드의 액션 큐에 집어넣는다 */
        worldmap.enqueueClientAction(action);


    }
}