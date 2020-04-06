package ECS.System;

import ECS.ActionQueue.Item.ActionUseItem;
import ECS.Classes.BuffAction;
import ECS.Classes.ConditionFloatParam;
import ECS.Classes.ItemInfo;
import ECS.Classes.ItemSlot;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Type.ItemSlotState;
import ECS.Classes.Type.ItemType;
import ECS.Classes.Type.NotificationType;
import ECS.Entity.CharacterEntity;
import ECS.Game.GameDataManager;
import ECS.Game.WorldMap;
import org.ietf.jgss.GSSManager;

import java.util.HashMap;
import java.util.List;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 17 화
 * 업뎃날짜 :
 * 목    적 :
 *      캐릭터(유저)들의 아이템 슬롯 상태를 기반으로,
 *      유저가 아이템 사용 시 처리 흐름을 돌리기 위해(?????) >> 말 더 정리해서 제대로 적을 것,,
 *
 */
public class ItemSlotSystem {

    /* 멤버 변수 */
    WorldMap worldMap;

    /* 생성자 */
    public ItemSlotSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    /* 매서드 */

    /**
     * 모든 캐릭터들의 아이템 슬롯목록을 검사하면서, 각 슬롯의 상태값에 따라 필요한 처리를 한다
     *
     * @param deltaTime
     */
    public void onUpdate(float deltaTime){

        /* 모든 캐릭터에 대해 반복한다 */
        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();
            List<ItemSlot> itemSlotList = character.itemSlotComponent.itemSlotList;

            ItemSlot currentSlot = null;
            for(int i=0; i<itemSlotList.size(); i++){

                currentSlot = itemSlotList.get(i);
                int slotState = currentSlot.getSlotState();

                //System.out.print("유저" + character.entityID + " 의 " + currentSlot.slotNum + "번째 슬롯 상태 : ");

                /* 슬롯의 각 상태에 따라 처리한다 */
                switch (slotState){

                    /* 빈 슬롯과 IDLE 상태의 경우, 별도 처리를 하지 않고 넘어간다. */
                    case ItemSlotState.EMPTY :
                        //System.out.println("EMPTY");
                        break;
                    case ItemSlotState.IDLE :
                        //System.out.println("IDLE");
                        break;

                    /* 실제 아이템 사용 요청이 들어온 이후의 처리 */
                    case ItemSlotState.READY :
                        //System.out.println("READY");
                        currentSlot.decreaseItemCount();    // 아이템 갯수를 줄인다
                        currentSlot.setSlotState(ItemSlotState.START);  // 상태 전이
                        break;

                    case ItemSlotState.START:
                        //System.out.println("START");

                        /* 실제 템 효과를 부여 */
                        applyItemBuff(character, currentSlot.itemInfo);

                        /* 다음 상태로 전이 */
                        if(currentSlot.getItemCount() == 0){
                            currentSlot.setSlotState(ItemSlotState.END);
                            System.out.println("아이템 갯수가 0이되었습니다. 쿨타임을 적용하지 않습니다.");
                        }
                        else{
                            currentSlot.remainCoolTime = GameDataManager.ITEM_USE_COOLTIME;
                            currentSlot.setSlotState(ItemSlotState.RUNNING);  // 상태 전이
                        }
                        break;

                    case ItemSlotState.RUNNING :
                        //System.out.println("RUNNING");

                        //System.out.println("남은 쿨타임 : " + currentSlot.remainCoolTime);
                        boolean isCoolTimeEnd = (currentSlot.remainCoolTime <= 0f) ? true : false;
                        if(isCoolTimeEnd){
                            System.out.println("아이템 사용 쿨타임이 종료되었습니다.");
                            currentSlot.setSlotState(ItemSlotState.END);
                        }
                        else{
                            currentSlot.remainCoolTime -= deltaTime;
                        }
                        break;

                    case ItemSlotState.END :
                        System.out.println("END");
                        if(currentSlot.getItemCount() == 0){

                            /* 슬롯을 비워주는 처리를 한다 */
                            System.out.println("아이템 갯수가 0이되어, 슬롯 비우는 처리를 진행합니다.");
                            currentSlot.emptySlot();

                            /* 상태 전이 */
                            currentSlot.setSlotState(ItemSlotState.EMPTY);
                        }
                        else{
                            System.out.println("슬롯 상태가 IDLE로 돌아갑니다, 남은 아이템 갯수 : " + currentSlot.itemCount);
                            // 쿨타임이 종료되었을 때 추가로 해줄 처리가 있다면 해주고
                            // 상태 전이
                            currentSlot.setSlotState(ItemSlotState.IDLE);
                        }
                        break;

                }

            }

        }

    }

    /**
     * 전달받은 아이템 사용 이벤트 정보를 가지고
     * 유저가 아이템을 사용할 수 있는지 여부를 판별하고, 그 결과를 반환한다
     * @param event
     * @return
     */
    public int isAbleToUseItem(ActionUseItem event){

        boolean isAbleToUseItem;

        int resultCode = -1;

        /*System.out.println("유저" + event.userEntityID + " 의 "
                + event.itemSlotNum + "번 슬롯의 아이템 사용 가능에 대한 판정을 시작합니다.");*/

        /* 이벤트 정보 */
        CharacterEntity user = worldMap.characterEntity.get(event.userEntityID);    // ID로 유저 찾음
        ItemSlot itemSlot = user.itemSlotComponent.findItemSlotBySlotNum(event.itemSlotNum);    // 슬롯번호로 슬롯 찾음

        boolean userStateAbleUseItem = (user.conditionComponent.isDisableItem == false) ? true : false;
        boolean isCoolTimeZero = (itemSlot.slotState == ItemSlotState.IDLE) ? true : false;
        boolean isValidItem = (itemSlot.itemCount > 0) ? true : false;


        isAbleToUseItem = ( userStateAbleUseItem && isCoolTimeZero && isValidItem ) ? true : false;


        if(isAbleToUseItem){
            System.out.println("아이템을 사용할 수 있습니다.");
            resultCode =  NotificationType.SUCCESS;
        }
        else{

            if(userStateAbleUseItem == false){
                System.out.println("아이템을 사용할 수 있는 상태가 아닙니다");
                resultCode =  NotificationType.ERR_ITEM_USER_DISABLE_USING;
            }
            else if(isCoolTimeZero == false){
                System.out.println("해당 아이템의 쿨타임이 아직 지나지 않았습니다.");
                resultCode =  NotificationType.ERR_ITEM_COOL_TIME_NOT_YET;
            }
            else if(isValidItem){
                System.out.println("유효하지 않은 아이템입니다. 슬롯이 비어있거나, 갯수가 하나 이상이 아닙니다.");
                resultCode =  NotificationType.ERR_ITEM_INVALID_ITEM;
            }

        }
        return resultCode;
    }


    /**
     * 월드맵의 아이템사용 이벤트 처리에서,
     *  위의 사용가능 여부 함수 호출 결과 유저가 아이템 사용하는 것이 가능하다면,
     *  그 후 해당 함수를 통해 실제 아이템 사용 처리를 하는 식.
     *
     * 살짝 고민되는거는
     *  월드맵에서 이 매서드를 호출하고,
     *  이 매서드 내에서 위 검사함수를 호출하는게 더 자연스러운지 아닌지..
     *  똑같이 event객체를 넘겨받고, 유저를 찾고.. 갖가지 처리를 다 해야되잖아
     *
     *  아 먼가 의도했던 게 이게 아닌거같기도 한데.. 이렇게 간단한다고?
     * @param event
     * @return
     */
    public void useItem(ActionUseItem event){

        System.out.println("아이템을 사용합니다.");

        /* 이벤트 정보 */
        CharacterEntity user = worldMap.characterEntity.get(event.userEntityID);    // ID로 유저 찾음
        ItemSlot itemSlot = user.itemSlotComponent.findItemSlotBySlotNum(event.itemSlotNum);    // 슬롯번호로 슬롯 찾음

        /* 아이템 사용에 따른 처리 예)) 못움직인다! 이런거 처리해줄 거 있으면 작성하고  */

        /* 슬롯의 상태를 변경한다 */
        itemSlot.setSlotState(ItemSlotState.READY);

    }


    /**
     * 아이템 타입별로, 유저에게 실제 아이템 효과를 적용한다
     *
     * 지금은 하드코딩, 나중에는 아이템 팩토리 사용해서
     *  아이템인포 자체에 버프를 만들어주던가 하기
     * @param user
     * @param itemInfo
     */
    public void applyItemBuff(CharacterEntity user, ItemInfo itemInfo){

        System.out.println("아이템 " + itemInfo.itemName + "의 효과를 적용합니다. ");

        BuffAction itemBuff = null;

        int itemType = itemInfo.itemType;
        switch (itemType){

            case ItemType.HP_POTION :

                System.out.println("체력회복 포션의 효과를 적용합니다.");

                // 이렇게 하는게 맞을라나 모르겠네..
                // 5초동안 체력 100 회복이니까. 총 5초에 걸쳐서, 1초마다 20씩 회복되도록 하고싶은데
                // 쿨타임을 1로 주는게 맞나? 아니면, 틱레이트 고려해서 0.9f 이렇게 줘야하는건지..
                // .. 는 테스트 해봐야 알 듯?
                itemBuff = new BuffAction(user.entityID, user.entityID, 5f, 0f, 1f);
                itemBuff.itemType = ItemType.HP_POTION;

                ConditionFloatParam hpBuff = new ConditionFloatParam(ConditionType.hpRecoveryAmount, 20);
                itemBuff.floatParam.add(hpBuff);

                break;

            case ItemType.MP_POTION :

                System.out.println("마력회복 포션의 효과를 적용합니다.");

                itemBuff = new BuffAction(user.entityID, user.entityID, 5f, 0f, 1f);
                itemBuff.itemType = ItemType.MP_POTION;

                ConditionFloatParam mpBuff = new ConditionFloatParam(ConditionType.mpRecoveryAmount, 20);
                itemBuff.floatParam.add(mpBuff);

                break;

            case ItemType.SPEED_POTION :

                System.out.println("이동속도 증가 포션의 효과를 적용합니다.");

                itemBuff = new BuffAction(user.entityID, user.entityID, 5f, 0f, 0f);
                itemBuff.itemType = ItemType.SPEED_POTION;

                ConditionFloatParam speedBuff = new ConditionFloatParam(ConditionType.moveSpeedRate, 250f);
                itemBuff.floatParam.add(speedBuff);

                break;

            case ItemType.DEFENSE_POTION :

                System.out.println("방어력 증가 포션의 효과를 적용합니다.");

                itemBuff = new BuffAction(user.entityID, user.entityID, 10f, 0f, 0f);
                itemBuff.itemType = ItemType.DEFENSE_POTION;

                ConditionFloatParam defenseBuff = new ConditionFloatParam(ConditionType.defenseRate, 10f);
                itemBuff.floatParam.add(defenseBuff);

                break;

            case ItemType.ATTACK_POTION :

                System.out.println("공격력 증가 포션의 효과를 적용합니다.");

                itemBuff = new BuffAction(user.entityID, user.entityID, 10f, 0f, 0f);
                itemBuff.itemType = ItemType.ATTACK_POTION;

                ConditionFloatParam attackBuff = new ConditionFloatParam(ConditionType.attackDamageRate, 20f);
                itemBuff.floatParam.add(attackBuff);

                break;
        }

        // 이렇게 해보고.. 만약에 안되면 각 케이스 안에다 넣어줄 것.
        user.buffActionHistoryComponent.conditionHistory.add(itemBuff);

    }
}
