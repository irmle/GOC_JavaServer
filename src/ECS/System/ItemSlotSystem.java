package ECS.System;

import ECS.ActionQueue.Item.ActionUseItem;
import ECS.Classes.*;
import ECS.Classes.Type.*;
import ECS.Entity.AttackTurretEntity;
import ECS.Entity.CharacterEntity;
import ECS.Factory.SkillFactory;
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

    float coolTime;
    float remainCoolTime;

    /* 아이템 타입별 효과 목록 */
    public static HashMap<Integer, HashMap<String, BuffInfo>> itemEffectInfoLIST;

    /* 생성자 */
    public ItemSlotSystem(WorldMap worldMap, float coolTime) {

        this.worldMap = worldMap;
        this.coolTime = coolTime;
        this.remainCoolTime = this.coolTime;


        itemEffectInfoLIST = GameDataManager.effectInfoList.get(EffectCauseType.ITEM);
    }

    /* 매서드 */

    /**
     * 모든 캐릭터들의 아이템 슬롯목록을 검사하면서, 각 슬롯의 상태값에 따라 필요한 처리를 한다
     *
     * @param deltaTime
     */
    public void onUpdate(float deltaTime){

        remainCoolTime -= worldMap.tickRate;
        if(remainCoolTime <= 0){
            remainCoolTime = coolTime;
        }
        else{
            return;
        }

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
                            //System.out.println("아이템 갯수가 0이되었습니다. 쿨타임을 적용하지 않습니다.");
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
                            //System.out.println("아이템 사용 쿨타임이 종료되었습니다.");
                            currentSlot.setSlotState(ItemSlotState.END);
                        }
                        else{
                            currentSlot.remainCoolTime -= deltaTime;
                        }
                        break;

                    case ItemSlotState.END :
                        //ystem.out.println("END");
                        if(currentSlot.getItemCount() == 0){

                            /* 슬롯을 비워주는 처리를 한다 */
                            //System.out.println("아이템 갯수가 0이되어, 슬롯 비우는 처리를 진행합니다.");
                            currentSlot.emptySlot();

                            /* 상태 전이 */
                            currentSlot.setSlotState(ItemSlotState.EMPTY);
                        }
                        else{
                            //System.out.println("슬롯 상태가 IDLE로 돌아갑니다, 남은 아이템 갯수 : " + currentSlot.itemCount);
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
            //System.out.println("아이템을 사용할 수 있습니다.");
            resultCode =  NotificationType.SUCCESS;
        }
        else{

            if(userStateAbleUseItem == false){
                //System.out.println("아이템을 사용할 수 있는 상태가 아닙니다");
                resultCode =  NotificationType.ERR_ITEM_USER_DISABLE_USING;
            }
            else if(isCoolTimeZero == false){
                //System.out.println("해당 아이템의 쿨타임이 아직 지나지 않았습니다.");
                resultCode =  NotificationType.ERR_ITEM_COOL_TIME_NOT_YET;
            }
            else if(isValidItem){
                //System.out.println("유효하지 않은 아이템입니다. 슬롯이 비어있거나, 갯수가 하나 이상이 아닙니다.");
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

        //System.out.println("아이템 " + itemInfo.itemName + "의 효과를 적용합니다. ");

        BuffAction itemBuff = null;

        int itemType = itemInfo.itemType;
        switch (itemType){

            case ItemType.HP_POTION :

                System.out.println("체력회복 포션의 효과를 적용합니다.");

                /*itemBuff = new BuffAction(user.entityID, user.entityID, 5f, 0f, 1f);
                itemBuff.itemType = ItemType.HP_POTION;

                ConditionFloatParam hpBuff = new ConditionFloatParam(ConditionType.hpRecoveryAmount, 20);
                itemBuff.floatParam.add(hpBuff);
                */

                itemBuff = createItemEffect(itemType, "체력회복", user.entityID);


                break;

            case ItemType.MP_POTION :

                System.out.println("마력회복 포션의 효과를 적용합니다.");

                /*itemBuff = new BuffAction(user.entityID, user.entityID, 5f, 0f, 1f);
                itemBuff.itemType = ItemType.MP_POTION;

                ConditionFloatParam mpBuff = new ConditionFloatParam(ConditionType.mpRecoveryAmount, 20);
                itemBuff.floatParam.add(mpBuff);*/

                itemBuff = createItemEffect(itemType, "마력회복", user.entityID);

                break;

            case ItemType.SPEED_POTION :

                System.out.println("이동속도 증가 포션의 효과를 적용합니다.");

                /*itemBuff = new BuffAction(user.entityID, user.entityID, 5f, 0f, 0f);
                itemBuff.itemType = ItemType.SPEED_POTION;

                ConditionFloatParam speedBuff = new ConditionFloatParam(ConditionType.moveSpeedRate, 250f);
                itemBuff.floatParam.add(speedBuff);*/

                itemBuff = createItemEffect(itemType, "이동속도증가", user.entityID);

                break;

            case ItemType.DEFENSE_POTION :

                System.out.println("방어력 증가 포션의 효과를 적용합니다.");

                /*itemBuff = new BuffAction(user.entityID, user.entityID, 10f, 0f, 0f);
                itemBuff.itemType = ItemType.DEFENSE_POTION;

                ConditionFloatParam defenseBuff = new ConditionFloatParam(ConditionType.defenseRate, 10f);
                itemBuff.floatParam.add(defenseBuff);*/

                itemBuff = createItemEffect(itemType, "방어력증가", user.entityID);

                break;

            case ItemType.ATTACK_POTION :

                System.out.println("공격력 증가 포션의 효과를 적용합니다.");

                /*itemBuff = new BuffAction(user.entityID, user.entityID, 10f, 0f, 0f);
                itemBuff.itemType = ItemType.ATTACK_POTION;

                ConditionFloatParam attackBuff = new ConditionFloatParam(ConditionType.attackDamageRate, 20f);
                itemBuff.floatParam.add(attackBuff);*/

                itemBuff = createItemEffect(itemType, "공격속도증가", user.entityID);

                break;
        }

        // 이렇게 해보고.. 만약에 안되면 각 케이스 안에다 넣어줄 것.
        user.buffActionHistoryComponent.conditionHistory.add(itemBuff);

    }





    /**
     *      Aim : 스킬에서 적용하고자 하는 효과를 생성할 때 호출하면 된다!
     *    Input :
     *   Output :
     *  Process :
     *
     */
    public static BuffAction createItemEffect(int itemType, String effectName, int effectEntityID){

        /** 템 효과 목록에서, 생성하고자 하는 effect 를 검색한다 */
        BuffInfo effectInfo = itemEffectInfoLIST.get(itemType).get(effectName);

        /** 효과의 지속시간을 구한다 (필요하다면) */
        /*
         * 조건 : 효과의 적중 타입이 '지속'이면서 효과정보 객체에 들어있는 지속시간 값이 0 이하인 경우
         *
         */
        float effectDurationTime;
        boolean needToGetDurationTime =
                (( effectInfo.effectAppicationType == EffectApplicationType.지속)
                        && ( effectInfo.effectDurationTime <= 0f)) ? true : false;
        if(needToGetDurationTime){

            effectDurationTime = effectInfo.effectDurationTime;
        }
        else{

            /* 지속시간 값을 별도로 구해 줄 필요가 없다면, 기존에 들어있는 값을 가져와 그대로 적용하면 된다. */
            effectDurationTime = effectInfo.effectDurationTime;
        }


        /* 별도 예외처리가 필요하다면?? */


        /** 효과 객체를 생성한다 (틀) */
        // 효과정보 객체에 들어있는 정보를 바탕으로, BuffAction 객체를 생성한다.
        BuffAction newEffect = new BuffAction(itemType, effectDurationTime, effectInfo.remainCoolTime, effectInfo.effectCoolTime);
        newEffect.itemType = itemType;

        /** 효과 내용을 채운다 */
        // BuffAction 객체에, 실제 효과를 부여하기 위한 처리를 한다. 경우에 따라, 스킬 시전자 정보 혹은 스킬레벨 정보를 참조해야 한다.

        int effectType = GameDataManager.getEffectTypeByParsingString(effectName);
        boolean isConditionEffect = checkIsConditionEffect(effectType);
        if(isConditionEffect){

            /* 상태이상을 결정하는 효과 타입인 경우, boolParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionBoolParam conditionEffect = new ConditionBoolParam(effectType, true);
            newEffect.addEffect(conditionEffect);
        }
        else{

            /* 기존 스탯 등에 영향을 미치는 버프 OR 디버프 효과 타입인 경우, floatParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionFloatParam valueEffect = createEffectParam(itemType, effectInfo);
            newEffect.addEffect(valueEffect);

        }

        // 나중에.. 근거리 공격용? 매서드도 하나 만들자..
        newEffect.unitID = effectEntityID;
        newEffect.skillUserID = newEffect.unitID;

        /* Output */
        return newEffect;

    }


    /**
     * 넘겨받은 효과가 상태 이상 타입의 효과인지 여부를 판단하는 매서드
     * @return
     */
    public static boolean checkIsConditionEffect(int effectType){

        boolean isConditionEffect = false;

        switch (effectType){

            case ConditionType.isDisableMove :
            case ConditionType.isDisableAttack :
            case ConditionType.isDisableSkill :
            case ConditionType.isDisableItem :
            case ConditionType.isDamageImmunity :
            case ConditionType.isUnTargetable :

            case ConditionType.isAirborneImmunity :
            case ConditionType.isAirborne :
            case ConditionType.isGarrenQApplied :
            case ConditionType.isTargetingInvincible :
            case ConditionType.isArcherFireActivated :
            case ConditionType.isStunned :
            case ConditionType.isArcherHeadShotActivated :
            case ConditionType.isFreezing :
            case ConditionType.isSlow :
            case ConditionType.isSilence :
            case ConditionType.isBlind :
            case ConditionType.isSightBlocked :
            case ConditionType.isGrounding :
            case ConditionType.isPolymorph :
            case ConditionType.isDisarmed :
            case ConditionType.isSnare :
            case ConditionType.isKnockedAirborne :
            case ConditionType.isKnockback :
            case ConditionType.isSuspension :
            case ConditionType.isTaunt :
            case ConditionType.isCharm :
            case ConditionType.isFlee :
            case ConditionType.isSuppressed :
            case ConditionType.isSleep :
            case ConditionType.isReturning :

                isConditionEffect = true;
                break;

            default:
                isConditionEffect = false;
        }

        return isConditionEffect;
    }

    /**
     * 상태이상이 아닌 타입의 스킬 효과 이펙트를 생성하는 매서드
     *
     * 아 이름짓는거때문에 먼가 통일하고싶은데.. bool 이랑 param 이랑.. 그럴 여유는 없겟지..
     */
    public static ConditionFloatParam createEffectParam(int itemType, BuffInfo effectInfo){

        /* Input */
        int effectType = GameDataManager.getEffectTypeByParsingString(effectInfo.effectTypeName);
        String effectValueStr = effectInfo.effectValue;

        /* Output */
        float effectValue = 0f;
        ConditionFloatParam valueEffect;

        /* 효과값을 결정한다 */
        switch (effectValueStr){

            default :

                effectValue = Float.parseFloat( GameDataManager.removePercentage(effectValueStr) );

                //System.out.println("그 외 ; 이미 값이 정해져 있음. %나 파싱해");
                break;

        }

        /* 예외처리
            ; 일반 '데미지' 타입인 경우, 해당 공격이 평탄지, 크리티컬인지 판정도 거처야 한다. */

        switch (effectType){    // 효과타입Name == "데미지"로 하는게 의미상 더 정확하긴 할텐데..

            default:

                valueEffect = new ConditionFloatParam(effectType, effectValue);
                break;
        }


        return valueEffect;

    }






    /*******************************************************************************************************************/












}
