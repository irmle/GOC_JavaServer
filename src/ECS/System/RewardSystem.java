package ECS.System;

import ECS.Classes.Reward;
import ECS.Classes.StoreUpgradeInfoPerLevel;
import ECS.Classes.Type.RewardType;
import ECS.Classes.Type.Upgrade.StoreUpgradeType;
import ECS.Components.ConditionComponent;
import ECS.Components.RewardHistoryComponent;
import ECS.Entity.CharacterEntity;
import ECS.Game.GameDataManager;
import ECS.Game.Store;
import ECS.Game.WorldMap;

import java.util.HashMap;
import java.util.List;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 16 월
 * 업뎃날짜 : 2020 02 28 금
 * 목    적 :
 *      캐릭터에게 주어지는 각종 보상 처리를 한다. (추후 확장될지 모르겠지만, 우선은 보상받는 객체는 캐릭터 앤티티만으로. )
 *          보상 처리는 여러가지가 있을 수 있다. 예)) 몹 사냥, 접속유지, 특정상태 유지 등등.. .
 *          현재는 몹 사냥에 대한 보상만 존재.
 *      각 캐릭터가 가지고 있는 보상 컴포넌트의 보상 리스트를 하나씩 읽으면서, 보상 타입에 맞는 처리를 수행한다.
 *      현재 몹 사냥 보상의 경우, 골드 획득과 경험치 획득이 있음.
 *          골드와 경험치는 캐릭터 앤티티의 캐릭터 컴포넌트가 가지고 있음. <<- 여기에 골드와 경험치를 조작하는 매서드를 만들 것.
 *      보상 시스템이 종료된 후, 캐릭터의 경험치를 가지고 레벨 시스템이 돌게된다.
 *
 */
public class RewardSystem {

    WorldMap worldMap;

    float coolTime;
    float remainCoolTime;

    HashMap<Integer, HashMap<Integer, StoreUpgradeInfoPerLevel>> storeUpgradeInfoList;

    public RewardSystem(WorldMap worldMap, float coolTime) {

        this.worldMap = worldMap;
        storeUpgradeInfoList = GameDataManager.storeUpgradeInfoPerLevelList;
        this.coolTime = coolTime;
        this.remainCoolTime = this.coolTime;
    }

    public void onUpdate(float deltaTime){

        // 시스템 도는 주기 조절용 코드
        /*remainCoolTime -= worldMap.tickRate;
        if(remainCoolTime <= 0){
            remainCoolTime = coolTime;
        }
        else{
            return;
        }*/

        /** 상점 업그레이드에 따른 보상 비율을 결정한다 */

        /* 경험치 */
        int expUpgradeLevel = Store.findUpgradeSlotByType(StoreUpgradeType.EXP_UPGRADE, worldMap).upgradeLevel;
        StoreUpgradeInfoPerLevel expUpgradeInfo
                = storeUpgradeInfoList.get(StoreUpgradeType.EXP_UPGRADE).get(expUpgradeLevel);

        /* 골드 */
        int goldUpgradeLevel = Store.findUpgradeSlotByType(StoreUpgradeType.GOLD_UPGRADE, worldMap).upgradeLevel;
        StoreUpgradeInfoPerLevel goldUpgradeInfo
                = storeUpgradeInfoList.get(StoreUpgradeType.GOLD_UPGRADE).get(goldUpgradeLevel);


        /** 모든 캐릭터에 대해 반복 */
        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();
            List<Reward> rewardList = character.rewardHistoryComponent.rewardHistory;

            ConditionComponent charCondition = character.conditionComponent;

            /* 경험치 보상 비율 결정 */
            /**
             * 아 고민되는게
             * 이.. 상점 업그레이드로 인한 겸치량 증가 효과를
             *  여기서 계산해서 적용해주는게 적절한지,
             *  아니면 이번 턴의 캐릭터의 최종 상태를 결정하는 'BuffActionSystem'에서 적용해주는 게 좋은지..
             * 최종상태 결정 시점에 들어가야 할 것도 같긴 한데, 문제는 버프시스템은 최종상태 결정 처리가 메인이라기보다는
             *  현재 받고 있는 버프들을 캐릭의 상태에 반영해주는 게 목적인시스템이라..
             *  한번에 두 가지 일을 맡기는 꼴이 될수도 있고.
             * 또, 현재는 상점 업그레이드를 제외하고는, 캐릭터의 겸치/골드 획득 보상 비율에 영향을 미치는 요인이 전혀 없는 상태이지만,
             *  만약 추가된다고 할 경우, 버프를 받아 추가된 '최종 직전 상태'에 대해, 상점업글 효과를 어떤 식으로 적용 해 줄 것이냐?? 가
             *  최소한 나한테는 명확하지 않기 때문임. 나중에 바뀔 수도 있고.
             *      최종 상태에 대해 곱연산 퍼센테이지로 적용할 수도 있고,
             *      '추가'되는 개념으로 적용될 수도 있고 그래서 말임
             * 일단은, '최종 상태' 비율에 대해 상점 업그레이드 효과 퍼센테이지를 적용하는 걸로 생각하겠음.
             * ㄴ 이 결과 구해진 '진짜 최종 비율'을, 현재 처리하려는 보상에 적용하는 식.
             *
             */
            //float expRate = 1 + (expUpgradeLevel * 0.1f);
            float expRate = charCondition.expGainRate;
            if(expUpgradeLevel >= 1){
                expRate *= ( 1 +  expUpgradeInfo.effectValue * 0.01f );
            }

            //System.out.println("경험치 보상 비율 : " + expRate + ", 경험치 업글 레벨 : " + expUpgradeLevel);

            float goldRate = charCondition.goldGainRate;
            if(goldUpgradeLevel >= 1){
                goldRate *= ( 1 +  goldUpgradeInfo.effectValue * 0.01f );
            }
            //System.out.println("골드 보상 비율 : " + goldRate + ", 골드 업글 레벨 : " + goldUpgradeLevel);


            /** 현 캐릭터의 보상 목록을 처리한다  */
            Reward currentReward = null;

            // 중계를 위한.. 이번 틱 누적 보상
            float expSum = 0f;
            int goldSum = 0;

            for (int i = 0; i < rewardList.size(); i++) {

                currentReward = rewardList.get(i);

                double finalRewardExp;
                double finalRewardGold;

                /* 보상 타입에 따라 처리한다 */
                switch (currentReward.rewardType){

                    case RewardType.KILL_MONSTER :

                        // 몬스터를 죽이고 얻는 보상의 경우, 경험치와 골드를 둘 다 얻는다.

                        // 경험치
                        finalRewardExp = currentReward.rewardExp * expRate;
                        character.characterComponent.getExpReward((float)finalRewardExp); // 경험치
                        // 골드
                        finalRewardGold = currentReward.rewardGold * goldRate;
                        character.characterComponent.getGoldReward((int)finalRewardGold);   // 골드

                        // 누적
                        expSum += (float)finalRewardExp;
                        goldSum += (int)finalRewardGold;

                        break;

                    case RewardType.KILL_MONSTER_BY_TURRET :

                        /**
                         * [ 오전 1:13 2020-04-04 권령희 ]
                         * 터렛에 의한 몹 KIll 보상을 처리하는 방식에 대해서는,
                         *  나중에 확실해지면 수정하거나 할 것.
                         *  보상 적용 비율을 파일로부터 읽어 처리하도록 하거나, 아니면 아예 주지 않거나.
                         * 현재는, 터렛을 건축한 캐릭터에게 그 보상을 주도록 하고 있음.
                         *  ㄴ 아 또 이런경우에는.. 상점 보상 비율을 적용해야 하는가??
                         *  이게. 게임 월드 전체에 걸쳐 적용되는 개념으로 봐야할지,아니면 캐릭터에만 한정해야 할지..
                         */

                        // 경험치
                        finalRewardExp = currentReward.rewardExp * expRate * 0.5f;
                        character.characterComponent.getExpReward((float)finalRewardExp); // 경험치
                        // 골드
                        finalRewardGold = currentReward.rewardGold * goldRate * 0.5f;
                        character.characterComponent.getGoldReward((int)finalRewardGold);   // 골드


                        // 누적
                        expSum += (float)finalRewardExp;
                        goldSum += (int)finalRewardGold;

                        break;

                    case RewardType.KILL_MONSTER_JUNGLE_MONSTER :

                        // 경험치
                        finalRewardExp = currentReward.rewardExp * expRate;
                        character.characterComponent.getExpReward((float)finalRewardExp); // 경험치
                        // 골드
                        finalRewardGold = currentReward.rewardGold * goldRate;
                        character.characterComponent.getGoldReward((int)finalRewardGold);   // 골드

                        // 누적
                        expSum += (float)finalRewardExp;
                        goldSum += (int)finalRewardGold;

                        /** 2020 02 28 */
                        //character.buffActionHistoryComponent.conditionHistory.add(currentReward.rewardBuff);

                        /** 2020 04 03 */
                        for (int j=0; j<currentReward.rewardBuff.size(); j++){

                            character.buffActionHistoryComponent.conditionHistory.add(
                                    currentReward.rewardBuff.get(j));
                        }

                        break;

                    default:
                        break;

                }

            }

            /** 2020 02 13 목 */

            /* 스코어에 반영 */
            worldMap.playerGameScoreList.get(character.entityID).earnedGold += goldSum;


            /* 중계처리 (필요하다면. 혹은, 월드맵 메인 게임 루프에서 보상에 대한 중계도 하게끔 하던지 */
            // 개인별로 보상내역을 중계할 경우 여기에 중계 코드를 작성..

            /* 보상 목록을 초기화한다 */
            rewardList.clear();

        }


    }


/*******************************************************************************************************************/
    /**
     * 2020 04 18 새벽 작성
     * 필요 데이터를 GDM으로부터 클론하여 사용하게끔, 초반에 미리 복사해두는 처리
     */

    /**
     * 기    능 : 월드맵에서 필요로 하는 데이터를, GDM에서 복사해온다.
     * 처    리 :
     *      WorldMap에서 필요로 하는 GDM 데이터는 다음과 같다
     *      -- 정글몹정보 목록
     *      -- 웨이브별 등장 몹 목록
     *
     */
    public void getNeedDataFromGDM(){

        /* 초기화 처리 */


        /* 정글몹 정보 목록을 복사한다 */

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
