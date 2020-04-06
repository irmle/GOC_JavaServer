package ECS.ActionQueue.Skill;

import ECS.ActionQueue.ClientAction;

//클라이언트로부터 스킬레벨업 관련 패킷을 받았을 때 ActionRequest큐에 담길 객체
public class ActionUpgradeSkill extends ClientAction {

    public int entityID; //스킬레벨을 업그레이드할 캐릭터의 EntityID.
    public short skillSlotNum; //강화될 스킬이 장착되어있는 스킬슬롯 번호.

    public ActionUpgradeSkill(int entityID, short skillSlotNum)
    {
        this.skillSlotNum = skillSlotNum;
        this.entityID = entityID;
    }
}
