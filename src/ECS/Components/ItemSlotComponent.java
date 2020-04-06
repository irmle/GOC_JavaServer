package ECS.Components;

import ECS.Classes.ItemInfo;
import ECS.Classes.ItemSlot;
import ECS.Classes.Type.ItemSlotState;
import ECS.Game.GameDataManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 2019 12 17 화 새벽 권령희 추가
 * 각종 매서드들 추가함
 *
 */
public class ItemSlotComponent {

    /* 멤벼 변수 */
    public List<ItemSlot> itemSlotList;

    /* 생성자 */

    /**
     * 게임데이터 매니저에 정의되어 있는,
     *      유저가 최대로 가질 수 있는 슬롯 갯수만큼 아이템 슬롯을 생성하여 목록에 추가한다
     * 시작 번호는 1번으로 한다
     */
    public ItemSlotComponent() {

        itemSlotList = new ArrayList<>();
//        for(int i=0; i< GameDataManager.MAX_ITEM_SLOT_SIZE; i++){
        for(int i=0; i < GameDataManager.MAX_ITEM_SLOT_SIZE; i++){
            int newSlotNum = i+1;
            itemSlotList.add( new ItemSlot(newSlotNum));
        }
    }

    /* 매서드 */

    /**
     * 념겨받은 타입에 해당하는 아이템을 유저가 이미 가지고 있는가?를 판별하는 매서드
     * @param itemType
     * @return
     */
    public boolean checkHasTheItemAlreadyOrNot(int itemType){

        boolean hasItem = false;

        ItemSlot slot;
        ItemInfo itemInfo;
        for(int i=0; i<itemSlotList.size(); i++){

            slot = itemSlotList.get(i);
            if(slot.slotState == ItemSlotState.EMPTY){  // 빈 슬롯은 넘어간다
                continue;
            }

            itemInfo = slot.itemInfo;
            if(itemInfo.itemType == itemType){
                hasItem = true;
                break;
            }
        }

        return hasItem;
    }

    /**
     * 이미 특정 아이템을 가지고 있다는 가정 하에, 해당 아이템이 몇 번째 슬롯에 들어있는지 판별한다
     * 슬롯번호는 1부터 시작하므로, 만약 해당 함수를 호출한 호출자가 받은 슬롯번호가 0일 경우, 해당 아이템이 없다는 뜻.
     * 아이템이 없는 경우엔 애초에 해당 함수를 호출하지도 않을거긴 하지만.
     * >> 쓸 일 없을듯...
     */
    public int getItemSlotNum(int itemType){

        int slotNum = 0;

        ItemSlot slot;
        for(int i=0; i<itemSlotList.size(); i++){

            slot = itemSlotList.get(i);
            if(slot.itemInfo.itemType == itemType){
                slotNum = slot.slotNum;
                break;
            }
        }

        return slotNum;
    }

    public ItemSlot findItemSlotByItemType(int itemType){

        ItemSlot slot = null;

        for(int i=0; i<itemSlotList.size(); i++){

            slot = itemSlotList.get(i);
            if(slot.itemInfo.itemType == itemType){
                break;
            }
        }

        return slot;

    }

    /**
     * 넘겨받은 슬롯 번호를 가지고, 실제 아이템 슬롯을 넘겨준다
     * @param itemSlotNum
     * @return
     */
    public ItemSlot findItemSlotBySlotNum(int itemSlotNum){

        ItemSlot slot = null;

        for(int i=0; i<itemSlotList.size(); i++){

            slot = itemSlotList.get(i);
            if(slot.slotNum == itemSlotNum){
                break;
            }
        }

        return slot;

    }

    /**
     * 비어있는 템슬롯을 찾아 반환한다
     *  ; 빈슬롯이 있다는 가정 하에, 점검 하에 호출되는 함수긴 한데.. 이거 리턴 결과가 널이면 망하는데 어떡하지
     */
    public ItemSlot findEmptySlot(){

        ItemSlot slot = null;

        for(int i=0; i<itemSlotList.size(); i++){

            slot = itemSlotList.get(i);
            if(slot.slotState == ItemSlotState.EMPTY){
                break;
            }
        }

        return slot;

    }

    /**
     * 모든 템슬롯이 비어있는지 확인하는 매서드
     */
    public boolean isEmptyAllItemSlot(){

        boolean result = true;

        ItemSlot slot;
        for(int i=0; i<itemSlotList.size(); i++){

            slot = itemSlotList.get(i);
            if(slot.slotState == ItemSlotState.EMPTY){
                continue;
            }
            else{
                result = false;
            }
        }

        return result;
    }

    /**
     * 모든 템슬롯이 꽉차있는지 확인하는 매서드
     */
    public boolean isFullAllItemSlot(){

        boolean result = true;

        ItemSlot slot;
        for(int i=0; i<itemSlotList.size(); i++){

            slot = itemSlotList.get(i);
            if(slot.slotState == ItemSlotState.EMPTY){
                result = false;
                break;
            }
        }

        return result;
    }


}
