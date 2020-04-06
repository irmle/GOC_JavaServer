package ECS.System;

import ECS.Classes.Reward;
import ECS.Classes.Type.RewardType;
import ECS.Classes.Type.Upgrade.StoreUpgradeType;
import ECS.Components.RewardHistoryComponent;
import ECS.Entity.CharacterEntity;
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

    public RewardSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime){

        /* 모든 캐릭터에 대해 반복 */
        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();
            List<Reward> rewardList = character.rewardHistoryComponent.rewardHistory;

            // 보상 비율
            int expUpgradeLevel = Store.findUpgradeSlotByType(StoreUpgradeType.EXP_UPGRADE, worldMap).upgradeLevel;
            float expRate = 1 + (expUpgradeLevel * 0.1f);
            System.out.println("경험치 보상 비율 : " + expRate + ", 경험치 업글 레벨 : " + expUpgradeLevel);

            int goldUpgradeLevel = Store.findUpgradeSlotByType(StoreUpgradeType.GOLD_UPGRADE, worldMap).upgradeLevel;
            float goldRate = 1 + (goldUpgradeLevel * 0.1f);
            System.out.println("골드 보상 비율 : " + goldRate + ", 골드 업글 레벨 : " + goldUpgradeLevel);


            /* 현 캐릭터의 보상 목록을 처리 */
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

                        System.out.println("받은 경험치 : " + currentReward.rewardExp + ", 현재 경험치 : " + character.characterComponent.exp);
                        System.out.println("받은 골드 : " + currentReward.rewardGold + ", 현재 골드 : " + character.characterComponent.gold);

                        // 누적
                        expSum += (float)finalRewardExp;
                        goldSum += (int)finalRewardGold;

                        break;

                    case RewardType.KILL_MONSTER_BY_TURRET :

                        // 몬스터를 죽이고 얻는 보상의 경우, 경험치와 골드를 둘 다 얻는다.
                        /*character.characterComponent.getExpReward(currentReward.rewardExp * 0.5f); // 경험치
                        character.characterComponent.getGoldReward(currentReward.rewardGold / 2);   // 골드*/

                        // 경험치
                        finalRewardExp = currentReward.rewardExp * expRate * 0.5f;
                        character.characterComponent.getExpReward((float)finalRewardExp); // 경험치
                        // 골드
                        finalRewardGold = currentReward.rewardGold * goldRate * 0.5f;
                        character.characterComponent.getGoldReward((int)finalRewardGold);   // 골드

                        System.out.println("받은 경험치 : " + currentReward.rewardExp + ", 현재 경험치 : " + character.characterComponent.exp);
                        System.out.println("받은 골드 : " + currentReward.rewardGold + ", 현재 골드 : " + character.characterComponent.gold);

                        // 누적
                        expSum += (float)finalRewardExp;
                        goldSum += (int)finalRewardGold;

                        break;

                    case RewardType.KILL_MONSTER_JUNGLE_MONSTER :

                        System.out.println("정글몬스터 보상 처리 ");

                        // 경험치
                        finalRewardExp = currentReward.rewardExp * expRate * 0.5f;
                        character.characterComponent.getExpReward((float)finalRewardExp); // 경험치
                        // 골드
                        finalRewardGold = currentReward.rewardGold * goldRate * 0.5f;
                        character.characterComponent.getGoldReward((int)finalRewardGold);   // 골드

                        System.out.println("받은 경험치 : " + currentReward.rewardExp + ", 현재 경험치 : " + character.characterComponent.exp);
                        System.out.println("받은 골드 : " + currentReward.rewardGold + ", 현재 골드 : " + character.characterComponent.gold);

                        // 누적
                        expSum += (float)finalRewardExp;
                        goldSum += (int)finalRewardGold;

                        /** 2020 02 28 */
                        character.buffActionHistoryComponent.conditionHistory.add(currentReward.rewardBuff);

                        System.out.println("정글몹 버프 보상 받았음 :" );

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



}
