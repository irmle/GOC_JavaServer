package ECS.ActionQueue.Item;

import ECS.ActionQueue.ClientAction;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 16 월
 * 업뎃날짜 :
 * 목    적 :
 *      클라이언트의 아이템 판매 요청 정보를 담고 있는 Action 클래스이다
 *
 *      클라이언트가 아이템 판매 RMI를 호출하면,
 *          RMI 매서드 내에서 해당 클래스를 생성해서
 *          유저가 속한 월드 내 Action 큐에 넣어주고
 *          월드의 유저 Action 처리 매서드 내에서 실제 아이템 판매 처리를 수행한다
 */
public class ActionSellItem extends ClientAction {

    /* 멤버 변수 */
    public int sellerEntityID;   // 아이템을 판매하려는 캐릭터의 ID
    public int itemSlotNum;
    public int itemID;          // 아이템에 할당된 ID.
    public int itemCount;       // 판매하려는 아이템 갯수


    /* 생성자 */
    public ActionSellItem(int sellerEntityID, int itemSlotNum, int itemID, int itemCount) {
        this.sellerEntityID = sellerEntityID;
        this.itemSlotNum = itemSlotNum;
        this.itemID = itemID;
        this.itemCount = itemCount;
    }
}
