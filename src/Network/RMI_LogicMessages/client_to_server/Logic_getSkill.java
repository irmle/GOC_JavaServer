package Network.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.Skill.ActionGetSkill;
import ECS.ActionQueue.ClientAction;
import ECS.Game.*;
import Network.RMI_Classes.*;

public class Logic_getSkill {


    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum, int skillID)
    {
        //do something.
        /* 유저가 속한 월드를 찾는다 */
        WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        /* 스킬 습득을 위한 액션 요청을 생성*/
        ActionGetSkill actionGetSkill = new ActionGetSkill(userEntityID, skillSlotNum, skillID);

        actionGetSkill.actionType = ClientAction.ActionType.ActionGetSkill;

        /* 액션 큐에 집어넣는다 */
        worldmap.enqueueClientAction(actionGetSkill);

        System.out.println("유저" + userEntityID + "가 " + skillSlotNum + "번째 슬롯에 " + skillID + "번 스킬 습득을 요청합니다.");






        // =======================================================================================
        // ==>> 잘못 한 것,, 이거를... 실제 액션 처리 시에, 액션겟스킬 타입일 경우에 처리해줘야 하는 부분일듯?
        // 거기서는.. 월드맵 딱히 검색 안해줘도 될거같고.
        // 팩토리 이용해서 스킬 사용하는 부분만.


        /* 유저가 속한 월드를 찾는다 */
        //WorldMap worldmap = WorldMapManager.findWorldMapFromWorldMapID(worldMapID);

        /* 스킬을 생성한다 */
        //SkillInfo newSkill = SkillFactory.createSkill(skillID);


        /* 유저의 스킬슬롯에, 스킬을 추가해준다 */

        //CharacterEntity character = worldmap.characterEntity.get(userEntityID);

        //SkillSlot newSlot = new SkillSlot(skillSlotNum, newSkill);
        //character.skillSlot.skillSlot.add(newSlot);
    }
}