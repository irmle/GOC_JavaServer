package ECS.Components;

import ECS.Classes.Type.CharacterType;

/**
 * 2019 12 16 월요일 권령희 추가
 *      스킬포인트 항목 추가함.
 *          이걸 스킬슬롯 컴포넌트에 넣을까도 고민했는데,
 *          일단 스킬포인트를 일종의 캐릭터 재화?같은 개념으로 보고 여기다가 추가함.
 */
public class CharacterComponent {

    public int characterType;
    public String characterName;

    public int level = 0;
    public float exp;
    public int gold;

    public int skillPoint = 0;


    /** 매서드 */

    /**
     * 보상 시스템에서, 경험치 보상을 받을 때 호출되는 함수
     */
    public void getExpReward(float rewardExp){

        this.exp += rewardExp;
    }

    /**
     * 보상 시스템에서, 골드 보상을 받을 때 호출되는 함수
     */
    public void getGoldReward(int rewardGold){

        this.gold += rewardGold;
    }

    /**
     * 상점시스템(?)에서, 아이템을 구입하거나 업그레이드를 위해 골드를 지불할 때 호출되는 함수
     */
    public boolean payGold(int price){

        /* 지불 가능 여부를 확인한다 */
        boolean hasEnoughGold = (this.gold >= price ) ? true : false;

        boolean paymentResult = false;
        if(hasEnoughGold){

            /* 지불할 금액만큼 골드를 차감한다 */
            this.gold -= price;

            paymentResult = true;
        }

        return paymentResult;
    }

    /**
     * 상점에 아이템을 판매한 경우, 판매 금액만큼 되돌려받는 처리를 하기 위한 호출되는 함수
     */
    public void paybackGold(int price){

        this.gold += price;
    }

    /**
     * 현재 경험치값을 리턴한다
     * @return
     */
    public float getCurrentExp(){

        return exp;
    }

    /**
     * 현재 골드값을 리턴한다
     */
    public int getCurrentGold(){

        return gold;
    }


    /**
     * 레벨업 처리로 인해 경험치 값을 줄인다
     */
    public void resetExpByLevelUp(float expAmount){

        exp -= expAmount;
    }

    /**
     * 레벨업 처리로 인해 현재 레벨 값을 1 올린다
     */
    public void increaseLevelByLevelUp(){

        level++;
    }

    /**
     * 레벨업 처리로 인해 스킬포인트 값을 1 올린다
     */
    public void increaseSkillPointByLevelUp(){

        skillPoint++;

    }








}
