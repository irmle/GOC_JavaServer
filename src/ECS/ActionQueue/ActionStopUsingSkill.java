package ECS.ActionQueue;

import ECS.Classes.Vector3;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 01 29 수
 * 업뎃날짜 :
 * 목    적 :
 *      일부 스킬의 사용 취소 요청을 처리하기 위함.
 *      >> 궁수 폭풍의 시의 경우
 *          스킬 버튼을 누를 때 스킬 사용 요청이, 버튼에서 손을 뗄 때에 스킬 중단 요청을 보낼 것임
 *
 */
public class ActionStopUsingSkill extends ClientAction {

    public int userEntityID; //스킬을 사용한 캐릭터 객체의 EntityID
    public short skillSlotNum; //사용된 스킬이 위치한 스킬 슬롯

    public ActionStopUsingSkill(int userEntityID, short skillSlotNum) {
        this.userEntityID = userEntityID;
        this.skillSlotNum = skillSlotNum;
    }
}
