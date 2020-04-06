package RMI.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.Skill.ActionUpgradeSkill;
import ECS.ActionQueue.ClientAction;
import ECS.Game.*;
import RMI.RMI_Classes.*;

public class Logic_upgradeSkill {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum)
    {
        //do something.

        /* 월드를 검색한다 */
        WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        /* 스킬 업그레이드를 위한 요청액션 생성 */
        ActionUpgradeSkill actionUpgradeSkill = new ActionUpgradeSkill(userEntityID, skillSlotNum);

        actionUpgradeSkill.actionType = ClientAction.ActionType.ActionUpgradeSkill;

        /* 큐에 넣어준다 */
        worldmap.enqueueClientAction(actionUpgradeSkill);





        // ====================================================================== //
        // ㄴ 실제 액션 처리에서 넣어줄 것

        /*
​
        1. 캐릭을 찾는다
​
        2. 업글 가능한 조건인지 판단한다 : 별도 함수 호출.
            함수의 구체적인 로직은 아직,,
            뭘 보고 레벨업 가능 여부를 판단한지 모르겠어서.
            물론, 클라이언트 측에서 레벨업 가능할 때만 ui를 활성화할거기 때문에
                딱히 검증이 필요하지 않을 수도 있지만.
            그래도 일단 껍데기만이라도 만들어두려고 함

            boolean
​
        3. if 가능하다면
            유저 캐릭터의 스킬 슬롯에 접근해서. 레벨 ++1 해주고.
           else
            안함.
        */

    }
}