package Network.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.Skill.ActionUseSkill;
import ECS.ActionQueue.ClientAction;
import ECS.Classes.Vector3;
import ECS.Game.*;
import Network.RMI_Classes.*;

public class Logic_useSkill {


    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, float directionX, float directionY, float directionZ, float distanceRate, short skillSlotNum, int targetEntityID)
    {
        /* 유저가 속한 월드를 찾는다 */
        WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        /* 스킬 사용을 위한 액션 요청을 생성*/
        Vector3 direction = new Vector3(directionX, directionY, directionZ);

        ActionUseSkill actionUseSkill = new ActionUseSkill(userEntityID, direction, distanceRate, skillSlotNum, targetEntityID);

        actionUseSkill.actionType = ClientAction.ActionType.ActionUseSkill;

        /* 큐에 넣어준다 */
        worldmap.enqueueClientAction(actionUseSkill);

        if(true){
            System.out.println(" === 스킬 사용 요청 정보 출력 ======================== ");
            System.out.println("유저" + userEntityID + "가 " + skillSlotNum + "번째 슬롯 스킬 사용을 요청합니다.");
            System.out.println("스킬 사용자 ID : " + actionUseSkill.userEntityID);
            System.out.println("스킬 사용 거리 비율 : " + actionUseSkill.skillDistanceRate);
            System.out.println("스킬 방향" + actionUseSkill.skillDirection.x() + ", " +
                    actionUseSkill.skillDirection.y() + ", " + actionUseSkill.skillDirection.z());
            System.out.println("지정된 타겟 : " + targetEntityID);
            System.out.println("======================================================= ");
        }

    }
}