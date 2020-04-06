package RMI.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.Skill.ActionUseAttack;
import ECS.ActionQueue.ClientAction;
import ECS.Game.*;
import RMI.RMI_Classes.*;

public class Logic_doAttack {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int attackerEntityID, int targetEntityID) {

        WorldMap result = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        //유저가 접속중인 월드맵을 찾아서 ClientActionQueue에 넣는다.
        if (result != null) {
            ActionUseAttack actionUseAttack = new ActionUseAttack();
            actionUseAttack.attackerEntityID = attackerEntityID;
            actionUseAttack.targetEntityID = targetEntityID;
            actionUseAttack.actionType = ClientAction.ActionType.ActionUseAttack;

            result.enqueueClientAction(actionUseAttack);
        }
    }

}