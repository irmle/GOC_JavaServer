package ECS.ActionQueue.Skill;

import ECS.ActionQueue.ClientAction;

//클라이언트로부터 스킬습득 관련 패킷을 받았을 때 ActionRequest큐에 담길 객체
public class ActionGetSkill extends ClientAction {

    public int entityID; //스킬을 습득할 캐릭터의 EntityID.
    public short skillSlotNum; //스킬이 추가될 스킬슬롯 번호.
    public int skillID; //추가될 스킬의 skillID. 이 값으로 skill에 대한 정보를 모두 불러올 수 있다.
    //(SkillType, SkillName, 스킬효과나 소모HP, MP등 모든 값)


    public ActionGetSkill(int entityID, short skillSlotNum, int skillID) {
        this.entityID = entityID;
        this.skillID = skillID;
        this.skillSlotNum = skillSlotNum;
    }

}
