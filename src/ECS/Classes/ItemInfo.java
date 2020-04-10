package ECS.Classes;


import ECS.Classes.Type.ItemType;

public class ItemInfo implements Cloneable{

    //아이템 타입
    public int itemType = ItemType.HP_POTION;

    //아이템 이름
    public String itemName = "마나 포션";

    //아이템 사용시 부여되는 BuffAction 정보 (없다면 null 값) 회복량, 이동속도증가, 방어력증가등이 있음
    public BuffAction buffAction;

    /** 2020 03 31 */
    public int itemCost = 0;



    public ItemInfo(int itemType, String itemName, BuffAction buffAction) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.buffAction = buffAction;
    }

    public ItemInfo(int itemType, String itemName) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.buffAction = new BuffAction();
    }
    public ItemInfo() {
        this.itemType = ItemType.NONE;
        this.itemName = "";
        this.buffAction = new BuffAction();
    }

    /**
     * 2020 03 31
     * @param itemType
     * @param itemName
     * @param itemCost
     */
    public ItemInfo(int itemType, String itemName, int itemCost) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemCost = itemCost;

        this.buffAction = new BuffAction();
    }

    /**
     * 2020 03 31
     * @return
     */



    @Override
    public ItemInfo clone() {

        ItemInfo itemInfo = null;

        try{
            itemInfo = (ItemInfo) super.clone();
            itemInfo.buffAction = (BuffAction) this.buffAction.clone();

        } catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }

        return itemInfo;
    }
}
