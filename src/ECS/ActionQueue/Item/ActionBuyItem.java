package ECS.ActionQueue.Item;

import ECS.ActionQueue.ClientAction;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 16 월
 * 업뎃날짜 :
 * 목    적 :
 *      클라이언트의 아이템 구매 요청 정보를 담고 있는 Action 클래스이다
 *
 *      클라이언트가 아이템 구매 RMI를 호출하면,
 *          RMI 매서드 내에서 해당 클래스를 생성해서
 *          유저가 속한 월드 내 Action 큐에 넣어주고
 *          월드의 유저 Action 처리 매서드 내에서 실제 아이템 구매 처리를 수행한다
 *
 */
public class ActionBuyItem extends ClientAction {

    /* 멤버 변수 */
    public int buyerEntityID;   // 아이템 구매자 캐릭터 ID
    public int itemSlotNum;     // 이걸.. 구매 시점에 결정할 수 는 없을텐데 이미 해당 템을 가지고 있는 상황이라면 모를까
    public int itemID;          // 아이템에 할당된 ID.
                                //      현재는 ItemType 클래스에 정의되어있는 값으로 쓰는데,
                                //      나중에 '타입'이랑 'ID(고유번호)'랑 분리해야 할듯?
    public int itemCount;       // 구매하려는 아이템 갯수


    /* 생성자 */
    public ActionBuyItem(int buyerEntityID, int itemSlotNum, int itemID, int itemCount) {
        this.buyerEntityID = buyerEntityID;
        this.itemSlotNum = itemSlotNum;
        this.itemID = itemID;
        this.itemCount = itemCount;
    }


}
