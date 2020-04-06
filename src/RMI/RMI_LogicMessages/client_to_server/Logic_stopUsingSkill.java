package RMI.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.ActionStopUsingSkill;
import ECS.ActionQueue.ClientAction;
import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Common.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import java.util.LinkedList;

/**
 * 업뎃날짜 : 2020 02 07 금요일
 * 목    적 :
 *      # 궁수 폭풍의 시 스킬 중단처리
 *      # 그 외.. 중단/취소 등 처리가 필요한 스킬이 있다면 적을 것.
 */
public class Logic_stopUsingSkill {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum)
    {
        //do something.

        /* 유저가 속한 월드를 찾는다 */
        WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        /* 스킬 사용을 위한 액션 요청을 생성*/
        ActionStopUsingSkill actionStopUsingSkill = new ActionStopUsingSkill(userEntityID, skillSlotNum);
        actionStopUsingSkill.actionType = ClientAction.ActionType.ActionStopUsingSkill;

        /* 큐에 넣어준다 */
        worldmap.enqueueClientAction(actionStopUsingSkill);

    }
}