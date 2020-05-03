package ECS.Game;

import ECS.ActionQueue.Item.ActionBuyItem;
import ECS.ActionQueue.Item.ActionSellItem;
import ECS.ActionQueue.Upgrade.ActionStoreUpgrade;
import ECS.Classes.*;
import ECS.Classes.Type.ItemSlotState;
import ECS.Classes.Type.ItemType;
import ECS.Classes.Type.NotificationType;
import ECS.Classes.Type.Upgrade.StoreUpgradeType;
import ECS.Entity.CharacterEntity;
import ECS.Entity.CrystalEntity;
import Network.AutoCreatedClass.StoreUpgradeBuffSlotData;
import Network.RMI_Classes.RMI_Context;
import Network.RMI_Classes.RMI_ID;
import Network.RMI_Common.server_to_client;

import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 17 화
 * 업뎃날짜 : 2020 01 07 화
 * 업뎃내용 :
 *      상점 업그레이드 관련 테이블 및 메서드 추가
 * 목    적 :
 *      유저가 아이템을 구매하거나 업그레이드를 진행할 때 사용되는 인터페이스 역할을 한다.
 *
 *      판매하는 아이템 목록과 가격정보를 관리하고
 *      업그레이드 슬롯을 두어, 업그레이드 현황을 관리하고
 *      아이템 구매 및 판매 처리 시, 구매/판매 가능여부 판단 및 실제 구매/판매 처리를 진행하는 매서드를 가지고 있음.
 */
public class Store {

    /* 멤버 변수 */

    static int MAX_SLOT_SIZE = 2;
    static int MAX_ITEM_COUNT = 5;

    public static HashMap<Integer, Integer> itemList;  // 아이템 타입, 아이템 가격 맵

    // 업그레이드 관련 데이터 테이블
    public static HashMap<Integer, HashMap<Integer, Integer>> upgradeInfoTable;


    public WorldMap worldMap;

    /* 생성자 */
    public Store(WorldMap worldMap) {

        itemList = new HashMap<>();
        upgradeInfoTable = new HashMap<>();

        this.worldMap = worldMap;
        initStore();

    }

    /**
     * 상점 초기화에 필요한 작업들을 수행한다
     */
    public void initStore(){

        readItemsInfoFromGDM();
        readUpgradeInfoFromGDM();

    }

    /**
     * 판매 아이템 목록 정보를 읽어오는 처리를 수행
     */
    public void readItemsInfo(){

        itemList.put(ItemType.HP_POTION, 100);
        itemList.put(ItemType.MP_POTION, 100);
        itemList.put(ItemType.SPEED_POTION, 100);
        itemList.put(ItemType.DEFENSE_POTION, 100);
        itemList.put(ItemType.ATTACK_POTION, 100);

    }

    /**
     * 2020 04 01 작성
     * 위의 하드코딩 readInfo 함수를 대체하여
     * GameDataManager 의 값을 참조하여 아이템 가격 테이블을 구성.
     *
     */
    public void readItemsInfoFromGDM(){

        ItemInfo itemInfo;

        for( HashMap.Entry<Integer, ItemInfo> entry : GameDataManager.itemInfoList.entrySet()){

            itemInfo = entry.getValue();
            itemList.put(itemInfo.itemType, itemInfo.itemCost);

            //System.out.println(itemInfo.itemName + "가격 : " + itemInfo.itemCost);

        }
    }

    /**
     * 업그레이드 관련 정보를 읽어오는 처리를 수행
     */
    public void readUpgradeInfo(){

        HashMap<Integer, Integer> upgradePriceTable;

        /* 크리스탈 */
        upgradePriceTable = new HashMap<>();
        upgradePriceTable.put(1, 100);
        upgradePriceTable.put(2, 200);
        upgradePriceTable.put(3, 400);
        upgradePriceTable.put(4, 800);
        upgradePriceTable.put(5, 1600);
        upgradeInfoTable.put(StoreUpgradeType.CRYSTAL_UPGRADE, upgradePriceTable);

        /* 경험치 */
        upgradePriceTable = new HashMap<>();
        upgradePriceTable.put(1, 100);
        upgradePriceTable.put(2, 200);
        upgradePriceTable.put(3, 400);
        upgradePriceTable.put(4, 800);
        upgradePriceTable.put(5, 1600);
        upgradeInfoTable.put(StoreUpgradeType.EXP_UPGRADE, upgradePriceTable);

        /* 골드 */
        upgradePriceTable = new HashMap<>();
        upgradePriceTable.put(1, 100);
        upgradePriceTable.put(2, 200);
        upgradePriceTable.put(3, 400);
        upgradePriceTable.put(4, 800);
        upgradePriceTable.put(5, 1600);
        upgradeInfoTable.put(StoreUpgradeType.GOLD_UPGRADE, upgradePriceTable);

    }


    /**
     * 2020 04 01 작성
     * 위의 하드코딩 readInfo 함수를 대체하여
     * GameDataManager 의 값을 참조하여 업그레이드 가격 테이블을 구성.
     */
    public void readUpgradeInfoFromGDM(){

        HashMap<Integer, Integer> upgradePriceTable;

        for (HashMap.Entry<Integer, HashMap<Integer, StoreUpgradeInfoPerLevel>> entry
                : GameDataManager.storeUpgradeInfoPerLevelList.entrySet()){

            HashMap<Integer, StoreUpgradeInfoPerLevel> upgradeInfoList = entry.getValue();

            upgradePriceTable = new HashMap<>();
            for (HashMap.Entry<Integer, StoreUpgradeInfoPerLevel> e : upgradeInfoList.entrySet()){

                StoreUpgradeInfoPerLevel upgradeInfo = e.getValue();
                upgradePriceTable.put(upgradeInfo.upgradeLevel, upgradeInfo.upgradeCost);

                //System.out.println(upgradeInfo.upgradeName + ", 레벨 " + upgradeInfo.upgradeLevel + " : " + upgradeInfo.upgradeCost);
            }

            upgradeInfoTable.put(entry.getKey(), upgradePriceTable);

        }

    }






    /**
     * 특정 업그레이드의 비용 정보를 리턴한다
     */
    public int getUpgradePrice(int upgradeType, int level){

        return upgradeInfoTable.get(upgradeType).get(level);
    }


    /**
     * 특정 아이템의 가격 정보를 리턴한다
     */
    public int getItemPriceInfo(int itemType){

        return itemList.get(itemType);

    }

    /**
     * 특정 유저가 해당 아이템을 구매하는 것이 가능한지 여부를 판단한다
     */
    public int isAbleToBuyItem(ActionBuyItem event){

        int resultCode = -1;

        boolean ableToBuyItem = false;

        CharacterEntity user = worldMap.characterEntity.get(event.buyerEntityID);
        int itemType = event.itemID;
        int buyCount = event.itemCount;

        /* 유저가 아이템 금액을 지불할 수 있는지 여부를 판단한다 */
        int usersGold = user.characterComponent.getCurrentGold();
        int itemPrice = getItemPriceInfo(itemType);
        boolean ableToPay = (usersGold >= itemPrice * buyCount) ? true : false;

        /* 지불 가능한 경우 */
        if(ableToPay){

            /** 유저가 해당 아이템을 가지고 있는지 판별한다 */
            boolean hasTheItem = user.itemSlotComponent.checkHasTheItemAlreadyOrNot(itemType);

            if(hasTheItem){ /* 구입하려는 아이템을 이미 가지고 있는 경우 */

                /** 해당 아이템을 이미 최대 소지갯수만큼 가지고있지 않은지 판별한다 */

                /* 해당 아이템 슬롯을 찾는다 */
                ItemSlot targetSlot = user.itemSlotComponent.findItemSlotByItemType(itemType);

                /* 슬롯이 가지고 있는 아이템 갯수를 판별한다 */
                boolean isFullSlot = targetSlot.isFullSlot();

                // 풀슬롯이면 바로 구매 불가능행
                if(!isFullSlot){    // 풀슬롯이 아닐 경우, 구매하려는 갯수만큼 구매가 가능한지?
                    int targetSlotItemCount = targetSlot.getItemCount();
                    ableToBuyItem = (targetSlotItemCount + buyCount <= MAX_ITEM_COUNT) ? true : false;

                    if(ableToBuyItem){
                        resultCode = NotificationType.SUCCESS;
                    }
                    else{
                        resultCode = NotificationType.ERR_ITEM_SLOT_COUNT_OVER;

                    }

                }

            }
            else{   /* 구입하려는 아이템을 가지고 있지 않은 경우 */

                /** 유저의 아이템 슬롯이 이미 꽉 차 있지 않은지 판별한다 */
                ableToBuyItem = (user.itemSlotComponent.isFullAllItemSlot() == false ) ? true : false;

                if(ableToBuyItem == false){
                    resultCode = NotificationType.ERR_ITEM_FULL_SLOT;
                }
                else{
                    resultCode = NotificationType.SUCCESS;
                }
            }

        }
        else{   /* 지불이 불가능한 경우 */

            resultCode = NotificationType.ERR_ITEM_LACK_MONEY;
        }


        return resultCode;
    }


    /**
     * 특정 유저의 아이템 구매 처리를 진행한다
     *      선 지불, 후 획득
     */
    public boolean purchaseItem(ActionBuyItem event){

        boolean result = false;

        /* 구매 정보 */
        CharacterEntity buyer = worldMap.characterEntity.get(event.buyerEntityID);
        int itemType = event.itemID;
        int itemCount = event.itemCount;

        /* 아이템 금액 지불 처리 하기 */
        int payment = getPayment(itemType, itemCount);
        boolean paymentSueccss = (buyer.characterComponent.payGold(payment) == true ) ?  true : false;

        if(paymentSueccss){  // 지불 처리에 성공했다면

            ItemSlot slot;

            /* 적절한 아이템슬롯을 찾아, 구매한 아이템을 넣어주기 */
            boolean hasTheItem = buyer.itemSlotComponent.checkHasTheItemAlreadyOrNot(itemType);
            if(hasTheItem){

                /* 기존 아이템이 들어있는 슬롯을 찾아, 갯수를 늘려주기 */
                slot = buyer.itemSlotComponent.findItemSlotByItemType(itemType);
                slot.increaseItemCount(itemCount);   // 아이템 갯수를 늘려준다
            }
            else{   /* 해당 아이템을 가지고 있지 않은 경우 */

                /* 비어있는 슬롯을 찾아 아이템 정보를 채워주기 */

                slot = buyer.itemSlotComponent.findEmptySlot();

                /** 2020 04 01 추가 */
                slot.itemInfo = GameDataManager.itemInfoList.get(itemType).clone();


                // 아 아이템팩토리 아직 안만들었는데. 일단은 수동으로 넣어주는 걸로?? 아이템 효과도 케이스문으로 처리.
                // 팩토리 자체는 비워두더라도. 나중을 위해서 만들어두긴 해야할듯? 틀이라도.
                // 팩토리 채워지고 나면. 그리고 버프액션쪽.. 실제 효과를 부여하는 처리에 대한 시스템이 잘 정립되고 나면
                // 아래 각 내용들을.. 그것들을 활용하는 방향으로 대체할 것.

                /*switch (itemType){

                    case ItemType.HP_POTION :
                        slot.itemInfo = new ItemInfo(itemType, "체력 회복 포션", new BuffAction());
                        break;

                    case ItemType.MP_POTION :
                        slot.itemInfo = new ItemInfo(itemType, "마력 회복 포션", new BuffAction());
                        break;

                    case ItemType.SPEED_POTION :
                        slot.itemInfo = new ItemInfo(itemType, "이동속도 증가 포션", new BuffAction());
                        break;

                    case ItemType.DEFENSE_POTION :
                        slot.itemInfo = new ItemInfo(itemType, "방어력 증가 포션", new BuffAction());
                        break;

                    case ItemType.ATTACK_POTION :
                        slot.itemInfo = new ItemInfo(itemType, "공격력 증가 포션", new BuffAction());
                        break;
                }*/


                slot.itemCount = itemCount;
                slot.setSlotState(ItemSlotState.IDLE);

            }

            result = true;

        }

        return result;
    }

    /**
     * 지정 아이템 및 구매하려는 갯수에 대해, 지불해야 할 금액을 반환한다
     * @return
     */
    public int getPayment(int itemType, int count){

        int pricePerCount = itemList.get(itemType);
        int payment = pricePerCount * count;

        return payment;
    }


    /**
     * 미완성
     * @param event
     * @return
     */
    public int isAbleToSellItem(ActionSellItem event){

        int resultCode = -1;
        boolean isAbleToSell = false;

        CharacterEntity seller = worldMap.characterEntity.get(event.sellerEntityID);
        int itemType = event.itemID;
        int sellCount = event.itemCount;
        int slotNum = event.itemSlotNum;

        /* 슬롯을 찾는다 */
        ItemSlot itemSlot = seller.itemSlotComponent.findItemSlotByItemType(itemType);

        if(itemSlot == null){   /* 슬롯이*/
            resultCode = NotificationType.ERR_ITEM_EMPTY_SLOT;
        }
        else{

            int slotState = itemSlot.getSlotState();
            isAbleToSell = ((slotState != ItemSlotState.EMPTY) && (slotState != ItemSlotState.READY)
                    && (itemSlot.getItemCount() > 0)) ? true : false;
            if(isAbleToSell){
                resultCode = NotificationType.SUCCESS;
            }

        }

        return resultCode;
    }

    /**
     *
     */
    public void sellItem(ActionSellItem event){

        /* 이벤트 정보 꺼내고 */
        /*event.itemCount;
        event.itemID;
        event.sellerEntityID;
        event.itemSlotNum;
*/
        // 유저 찾고
        CharacterEntity seller = worldMap.characterEntity.get(event.sellerEntityID);
        // 슬롯 찾고
        ItemSlot itemSlot = seller.itemSlotComponent.findItemSlotBySlotNum(event.itemSlotNum);
        // 돌려받을 금액 찾고
        int paybackPrice = getSellingPrice(event.itemID);

        /* 갯수 줄여주고 */
        itemSlot.decreaseItemCount();
        boolean isEmpty = (itemSlot.getItemCount() == 0 ) ? true : false;
        if(isEmpty){
            itemSlot.emptySlot();
            itemSlot.setSlotState(ItemSlotState.EMPTY);
        }

        /* 판매금액 받고 */
        seller.characterComponent.paybackGold(paybackPrice);

    }

    /**
     * 판매처리 시 되돌려받을 금액 구하기
     * @param itemType
     * @return
     */
    public int getSellingPrice(int itemType){

        int paybackPrice;

        int itemPrice = getItemPriceInfo(itemType);
        paybackPrice = itemPrice / 2;

        return paybackPrice;

    }


    /**
     *
     * 업그레이드 요청에 대해, 업그레이드 가능한지 여부를 판단하는 함수
     *
     */
    public boolean isAbleToUpgrade(ActionStoreUpgrade event, WorldMap worldMap){

        boolean isAble = false;

        /* 이벤트 정보 */
        int upgradeType = event.upgradeType;
        CharacterEntity user = worldMap.characterEntity.get(event.userEntityID);

        /* 업그레이드 슬롯 찾기 */
        StoreUpgradeSlot slot = findUpgradeSlotByType(upgradeType, worldMap);

        /* 슬롯의 업그레이드 레벨 검사 */
        boolean isAbleToUpgradeLevel = ((0 <=slot.upgradeLevel) && (slot.upgradeLevel < 5)) ? true : false;

        /* 업그레이드 하려는 레벨과 현재 슬롯의 레벨 검사 */
        boolean ableLevel = false;
        if(isAbleToUpgradeLevel){

            ableLevel = (event.upgradeLevel == (slot.upgradeLevel+1)) ? true : false;
        }

        /* 유저가 지불할 수 있는지 검사 */
        boolean ableToPay = false;
        if(ableLevel){

            int usersGold = user.characterComponent.getCurrentGold();
            int upgradePrice = getUpgradePrice(upgradeType, slot.upgradeLevel+1);
            ableToPay = (usersGold >= upgradePrice) ? true : false;
        }

        /* 판단 */
        if(ableToPay){
            isAble = true;
        }

        return isAble;
    }

    /**
     *
     * 업그레이드 가능 여부 판단 후, 실제 업그레이드 처리를 수행하기 위해 호출하는 함수
     */
    public void processUpgrade(ActionStoreUpgrade event, WorldMap worldMap){

        /* 이벤트 정보 */
        int upgradeType = event.upgradeType;
        CharacterEntity user = worldMap.characterEntity.get(event.userEntityID);

        /* 업그레이드 슬롯 찾기 */
        StoreUpgradeSlot slot = findUpgradeSlotByType(upgradeType, worldMap);

        /* 업글 레벨 업*/
        slot.upgradeLevel++;

        /* 지불 */
        int price = getUpgradePrice(upgradeType, slot.upgradeLevel);
        user.characterComponent.payGold(price);

        /* 업그레이드 처리 */
        switch (upgradeType){

            case StoreUpgradeType.CRYSTAL_UPGRADE :

                System.out.println("크리스탈을 업그레이드합니다. 업그레이드 레벨 : " + slot.upgradeLevel);

                StoreUpgradeInfoPerLevel crystalUpgradeInfo
                        = GameDataManager.storeUpgradeInfoPerLevelList.get(StoreUpgradeType.CRYSTAL_UPGRADE).get(slot.upgradeLevel);

                /**
                 * 오전 1:34 2020-04-04 권령희
                 * '최초 체력'을 기준으로 값이 올라가는 거겟지?
                 *  설마.. 이전에 강화된 값을 대상으로 퍼센트가 올라가진 않겠지?;
                 *
                 */
                CrystalEntity crystal = worldMap.crystalEntity.get(worldMap.crystalID);

                float maxHpBeforeUpgrade = crystal.hpComponent.maxHP;

                crystal.hpComponent.maxHP = crystal.hpComponent.originalMaxHp * (crystalUpgradeInfo.effectValue * 0.01f);
                crystal.hpComponent.currentHP += (crystal.hpComponent.maxHP - maxHpBeforeUpgrade);

                /**
                 * 오전 1:34 2020-04-04 권령희
                 * 아 문제 안되겠지...??
                 *  현재로서는, 크리스탈의 최대 체력에 영향을 미치는 요소가
                 *  요 상점 업그레이드밖에 없으니까 ? ;;
                 *  -- 기획상 업그레이드 수치의 의미를.. '기존 레벨 대비 값'으로 변경하자고 하는게 좋을 듯.
                 */
                //crystal.hpComponent.originalMaxHp = crystal.hpComponent.maxHP;

                break;
            case StoreUpgradeType.EXP_UPGRADE :

                System.out.println("경험치획득 비율을 업그레이드합니다. 업그레이드 레벨 : " + slot.upgradeLevel);

                break;
            case StoreUpgradeType.GOLD_UPGRADE :

                System.out.println("골드획득 비율을 업그레이드합니다. 업그레이드 레벨 : " + slot.upgradeLevel);

                break;
        }

        /* 업그레이드 성공 중계 */
        StoreUpgradeBuffSlotData slotData = new StoreUpgradeBuffSlotData();
        slotData.slotNum = slot.slotNum;
        slotData.upgradeLevel = slot.upgradeLevel;
        slotData.upgradeType = slot.upgradeType;

        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.userSucceedStoreUpgradeBuff(TARGET, RMI_Context.Reliable_Public_AES256, slotData);

    }

    /**
     * 업그레이드 슬롯을 찾는다
     *
     */
    public static StoreUpgradeSlot findUpgradeSlotByType(int upgradeType, WorldMap worldMap){

        StoreUpgradeSlot slot = null;
        for(int i=0; i<worldMap.upgradeSlotList.size(); i++){

            if(worldMap.upgradeSlotList.get(i).upgradeType == upgradeType){
                slot = worldMap.upgradeSlotList.get(i);
                break;
            }

        }

        return slot;
    }



    /*******************************************************************************************************************/
    /**
     * 2020 04 18 새벽 작성
     * 필요 데이터를 GDM으로부터 클론하여 사용하게끔, 초반에 미리 복사해두는 처리
     */

    /**
     * 기    능 : 스토어에서 필요로 하는 데이터를, GDM에서 복사해온다.
     * 처    리 :
     *      Store에서 필요로 하는 GDM 데이터는 다음과 같다
     *      -- 아이템정보 목록
     *      -- 레벨별 상점 강화 목록
     *
     */
    public void getNeedDataFromGDM(){

        /* 초기화 처리 */


        /* 아이템 정보 목록을 복사한다 */
        bringItemInfoListFromGDM();

        /* 레벨별 상점 업그레이드 정보 목록을 복사한다 */
        bringStoreUpgradeInfoListFromGDM();

    }

    public void bringItemInfoListFromGDM(){

        HashMap<Integer, ItemInfo> itemInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, ItemInfo> itemInfo : GameDataManager.itemInfoList.entrySet()){

            int itemKey = itemInfo.getKey();
            ItemInfo itemValue = itemInfo.getValue();
            itemInfoList.put(itemKey, itemValue.clone());

        }

    }

    public void bringStoreUpgradeInfoListFromGDM(){

        HashMap<Integer, HashMap<Integer, StoreUpgradeInfoPerLevel>> storeUpgradeInfoPerLevelList = new HashMap<>();
        for( HashMap.Entry<Integer, HashMap<Integer, StoreUpgradeInfoPerLevel>> storeUpgradeInfo
                : GameDataManager.storeUpgradeInfoPerLevelList.entrySet()){

            int storeUpgradeKey = storeUpgradeInfo.getKey();

            HashMap<Integer, StoreUpgradeInfoPerLevel> storeUpgradeValue = new HashMap<>();
            for( HashMap.Entry<Integer, StoreUpgradeInfoPerLevel> upgradeInfoPerLevel : storeUpgradeInfo.getValue().entrySet()){

                int upgradeKey = upgradeInfoPerLevel.getKey();
                StoreUpgradeInfoPerLevel upgradeValue = upgradeInfoPerLevel.getValue();

                storeUpgradeValue.put(upgradeKey, upgradeValue.clone());

            }

            storeUpgradeInfoPerLevelList.put(storeUpgradeKey, storeUpgradeValue);

        }

    }








}
