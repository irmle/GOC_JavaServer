package RMI.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.Build.ActionInstallBuilding;
import ECS.ActionQueue.Build.ActionUpgradeBuilding;
import ECS.ActionQueue.ClientAction;
import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import RMI.RMI_Classes.*;

import javax.swing.plaf.synth.SynthScrollBarUI;

public class Logic_upgradeTurret {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretEntityID, int turretType)
    {
        System.out.println("터렛 업그레이드 요청을 받았습니다. ");

        /* 유저가 속한 월드를 찾는다 */
        WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        /* 건설 Action 객체를 생성한다 */
        ActionUpgradeBuilding action = new ActionUpgradeBuilding(userEntityID, turretEntityID, turretType);
        action.actionType = ClientAction.ActionType.ACTION_UPGRADE_BUILDING;

        /* 생성한 Action 객체를 월드의 액션 큐에 집어넣는다 */
        worldmap.enqueueClientAction(action);

    }
}