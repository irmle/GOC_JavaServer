package ECS.ActionQueue.Skill;

import ECS.ActionQueue.ClientAction;

//클라이언트로부터 일반공격 관련 패킷을 받았을 때 ActionRequest큐에 담길 객체
public class ActionUseAttack extends ClientAction {

    public int attackerEntityID; //일반공격을 한 객체의 EntityID
    public int targetEntityID; //일반공격의 타겟 EntityID


    public ActionUseAttack() {
    }

    public ActionUseAttack(int attackerEntityID, int targetEntityID) {
        this.targetEntityID = targetEntityID;
        this.attackerEntityID = attackerEntityID;
    }

}
