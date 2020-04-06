package ECS.Classes;

import ECS.Classes.Type.ItemSlotState;
import ECS.Game.GameDataManager;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * 2019 12 16 월요일 권령희 추가
 * 2019 12 17 화요일 권령희 추가
 * 추가한 내용 :
 *      슬롯상태 변수 추가
 *      생성자 및 clone 추가
 *      각종 매서드 추가
 */
public class ItemSlot implements Cloneable{
    public int slotNum;
    public ItemInfo itemInfo;
    public int itemCount;
    public float remainCoolTime;

    public int slotState;

    public ItemSlot(int slotNum, ItemInfo itemInfo, int itemCount, float remainCoolTime) {
        this.slotNum = slotNum;
        this.itemInfo = itemInfo;
        this.itemCount = itemCount;
        this.remainCoolTime = remainCoolTime;

        slotState = ItemSlotState.EMPTY; // 초기값

    }

    /**
     * 초기화용
     * @param slotNum
     */
    public ItemSlot(int slotNum) {
        this.slotNum = slotNum;

        this.itemInfo = new ItemInfo();
        this.itemCount = 0;
        this.remainCoolTime = 0;

        this.slotState = ItemSlotState.EMPTY;
    }

    @Override
    public ItemSlot clone() {

        ItemSlot itemSlot = null;

        try {
            itemSlot = (ItemSlot) super.clone();
            itemSlot.itemInfo = this.itemInfo.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return itemSlot;
    }

    public int getItemCount(){

        return itemCount;
    }

    public boolean isFullSlot(){

        boolean result = false;
        if(this.itemCount >= GameDataManager.MAX_ITEM_COUNT){
            result = true;
        }

        return result;
    }

    /**
     * 아이템 구매로 인해 아이템 갯수를 늘려준다
     * @param itemCount
     */
    public void increaseItemCount(int itemCount){

        this.itemCount += itemCount;
    }

    /**
     *  아이템 사용으로 인해 아이템 갯수를 하나 줄인다
     */
    public void decreaseItemCount(){
        this.itemCount--;
    }


    /**
     * 슬롯의 상태를 변경하는 함수
     */
    public void setSlotState(int slotState){

        this.slotState = slotState;
    }

    public int getSlotState(){

        return this.slotState;
    }

    /**
     * 아이템 갯수가 0이 된 슬롯을 비워주는 처리를 한다
     * 상태를 EMPTY로 바꿔주는거는.. 아이템슬로 시스템에서 처리한다
     * 나중에 좀더 고민해보고 여기다가 넣을 것.
     */
    public void emptySlot(){

        this.itemInfo = new ItemInfo();
        this.itemCount = 0;
        this.remainCoolTime = 0;

    }


}
