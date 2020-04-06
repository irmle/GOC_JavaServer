package ECS.ActionQueue.Item;

import ECS.ActionQueue.ClientAction;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 16 월
 * 업뎃날짜 :
 * 목    적 :
 *      클라이언트의 아이템 사용 요청 정보를 담고 있는 Action 클래스이다
 *
 *      클라이언트가 아이템 사용 RMI를 호출하면,
 *          RMI 매서드 내에서 해당 클래스를 생성해서
 *          유저가 속한 월드 내 Action 큐에 넣어주고
 *          월드의 유저 Action 처리 매서드 내에서 실제 아이템 사용 처리를 수행한다
 */
public class ActionUseItem extends ClientAction {

    /* 멤버 변수 */
    public int userEntityID;   // 아이템 사용자 캐릭터 ID
    public int itemSlotNum;

    /* 생성자 */
    public ActionUseItem(int userEntityID, int itemSlotNum) {
        this.userEntityID = userEntityID;
        this.itemSlotNum = itemSlotNum;
    }
}
