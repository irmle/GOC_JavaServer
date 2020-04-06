package ECS.ActionQueue.Skill;

import ECS.ActionQueue.ClientAction;
import ECS.Classes.Vector3;

//클라이언트로부터 스킬사용 관련 패킷을 받았을 때 ActionRequest큐에 담길 객체
public class ActionUseSkill extends ClientAction {

    public int userEntityID; //스킬을 사용한 캐릭터 객체의 EntityID
    public Vector3 skillDirection; //스킬이 사용된 방향
    public float skillDistanceRate; //스킬이 사용될 거리비율 ( ex > 스킬의 최대사거리 * skillDistanceRate = 스킬이 사용될 거리)
    public short skillSlotNum; //사용된 스킬이 위치한 스킬 슬롯
    public int targetEntityID = Integer.MIN_VALUE; //스킬의 대상이 되는 객체의 EntityID. (타게팅 스킬인 경우에만 지정됨!)

    /** 2020 02 20 목 권령희 */
    public float remainCoolTime;


    public ActionUseSkill(int userEntityID, Vector3 skillDirection, float skillDistanceRate, short skillSlotNum, int targetEntityID) {
        this.skillDirection = skillDirection;
        this.skillDistanceRate = skillDistanceRate;
        this.skillSlotNum = skillSlotNum;
        this.targetEntityID = targetEntityID;
        this.userEntityID = userEntityID;

        this.remainCoolTime = 0f;
    }
}
