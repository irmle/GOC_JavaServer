package ECS.System;

import ECS.Classes.BalanceData;
import ECS.Classes.CharacterLevelUpInfo;
import ECS.Classes.Type.BalanceData.BalanceDataType;
import ECS.Components.CharacterComponent;
import ECS.Entity.CharacterEntity;
import ECS.Game.GameDataManager;
import ECS.Game.WorldMap;

import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 ... ?
 * 업뎃날짜 : 2020 02 13 목
 * 목    적 :
 */
public class LevelUpSystem {

    public WorldMap worldMap;

    float coolTime;
    float remainCoolTime;

    public static final int MAX_LEVEL = 15;

    public LevelUpSystem(WorldMap worldMap, float coolTime) {
        this.worldMap = worldMap;
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

        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();
            CharacterComponent characterComponent = character.characterComponent;

            /** 2019 12 22 일 새벽 변경*/
            // 레벨업 할 수 있는 조건이 여러번 충족될 경우를 대비해서..

            //System.out.println("캐릭터" + character.entityID + " 의 레벨업 판정을 시작합니다 ");

            while(true){

                /** 2019 12 26 목 오후 추가.. */
                // 막렙(현 15)인 경우 처리하지않고 PASS
                //System.out.println("현재 레벨 : " + characterComponent.level);
                if(characterComponent.level == MAX_LEVEL){
                    break;
                }

                /* 레벨업 조건을 달성했는지 검사한다 */
                float needExpToLevelUp = GameDataManager.getMaxExpByLevel(characterComponent.level);
                boolean isAbleToLevelUp = (characterComponent.exp >= needExpToLevelUp) ? true : false;

                /* 레벨업이 가능하다면, 레벨업 처리를 한다. */
                if(isAbleToLevelUp) {

                    //System.out.println("캐릭터" + character.entityID + " 가 레벨업합니다.");

                    /** 1. 경험치 량 및 레벨 증감 처리를 한다 */
                    characterComponent.resetExpByLevelUp(needExpToLevelUp);
                    characterComponent.increaseLevelByLevelUp();

                    /** 2. 스킬포인트를 추가한다 */
                    characterComponent.increaseSkillPointByLevelUp();

                    /** 3. 스탯 증감 처리를 한다 */  // 아 이거 별도 매서드 두기 좀 애매하네..
                    CharacterLevelUpInfo levelUpInfo = GameDataManager.getLevelUpInfo(characterComponent.characterType);

                    character.hpComponent.originalMaxHp += levelUpInfo.maxHP;
                    character.hpComponent.maxHP += levelUpInfo.maxHP;
                    //character.hpComponent.currentHP = character.hpComponent.maxHP;
                    character.hpComponent.recoveryRateHP += levelUpInfo.recoveryRateHP;

                    character.hpComponent.currentHP += levelUpInfo.maxHP;
                    if(character.hpComponent.currentHP > character.hpComponent.maxHP){
                        character.hpComponent.currentHP = character.hpComponent.maxHP;
                    }

                    character.mpComponent.originalMaxMP += levelUpInfo.maxMP;
                    character.mpComponent.maxMP += levelUpInfo.maxMP;
                    //character.mpComponent.currentMP = character.mpComponent.maxMP;
                    character.mpComponent.recoveryRateMP += levelUpInfo.recoveryRateMP;

                    character.mpComponent.currentMP += levelUpInfo.maxMP;
                    if(character.mpComponent.currentMP > character.mpComponent.maxMP){
                        character.mpComponent.currentMP = character.mpComponent.maxMP;
                    }

                    character.attackComponent.attackDamage += levelUpInfo.attackDamage;
                    character.attackComponent.attackSpeed
                            += ( character.attackComponent.attackSpeed * levelUpInfo.attackSpeed * 0.01f );

                    character.defenseComponent.defense += levelUpInfo.defense;

                    // 아 치명타 관련 컴포넌트가 없는걸로 알고있는데..
                    // 이건머 나중에 추가하자 어차피 공식도 다시 정리해야되고 그때 한꺼번에 처리할 것

                    /** 2020 02 13 추가, 치명타 등등 */
                    character.attackComponent.criticalChance += levelUpInfo.criticalProbRate;
                    character.attackComponent.criticalDamage += levelUpInfo.criticalDamage;



                    /** 4. 그 외 레벨업에 따른 추가 버프처리가 있다면 수행...  */



                    /* (여기서 필요하다면) 중계 처리를 한다 */

                }
                else {

                    break;
                }

            }

        }

    }


    public void setCharacterStatByLevelUp(int characterType){





    }



    /*******************************************************************************************************************/
    /**
     * 2020 04 18 새벽 작성
     * 필요 데이터를 GDM으로부터 클론하여 사용하게끔, 초반에 미리 복사해두는 처리
     */

    /**
     * 기    능 : 레벨업 시스템에서 필요로 하는 데이터를, GDM에서 복사해온다.
     * 처    리 :
     *      LevelUp System 에서 필요로 하는 GDM 데이터는 다음과 같다
     *      -- 밸런스 데이터 목록
     *      -- 레벨업 테이블
     *      -- 캐릭터 레벨업 스탯변화 테이블
     *
     *      -- 매서드..
     *
     */
    public void getNeedDataFromGDM(){

        /* 초기화 처리 */


        /* 정글몹 정보 목록을 복사한다 */


    }

    public void bringLevelUpInfoListFromGDM(){

        HashMap<Integer, CharacterLevelUpInfo> characterLevelUpInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, CharacterLevelUpInfo> characterLevelUpInfo : GameDataManager.characterLevelUpInfoList.entrySet()){

            int levelUpKey = characterLevelUpInfo.getKey();
            CharacterLevelUpInfo levelUpValue = characterLevelUpInfo.getValue();
            characterLevelUpInfoList.put(levelUpKey, levelUpValue.clone());

        }

    }

    public void bringLevelUpTableFromGDM(){

        HashMap<Integer, Float> levelUpTable = new HashMap<>();
        for( HashMap.Entry<Integer, Float> levelUpInfo : GameDataManager.levelUpTable.entrySet()){

            int levelUpKey = levelUpInfo.getKey();
            float levelUpValue = levelUpInfo.getValue();
            levelUpTable.put(levelUpKey, levelUpValue);

        }

    }


    /**
     * 넘겨받은 레벨 값을 가지고, 현 레벨에서 다음 레벨로 업그레이드 하기 위해 필요한 경험치 량을 찾아 리턴한다
     * @param
     * @return
     */
    /*public static float getMaxExpByLevel(int currentLevel){

        int CHAR_MAX_LEVEL = balanceDataInfoList.get(BalanceDataType.EXP_FOR_CHARACTER_LEVEL_UP).maxLevel;

        float expAmount;
        if(currentLevel == CHAR_MAX_LEVEL){
            expAmount = levelUpTable.get(currentLevel);
        }
        expAmount = levelUpTable.get(currentLevel+1);

        return expAmount;
    }*/

    public void bringBalanceDataInfoListFromGDM(){

        HashMap<Integer, BalanceData> balanceDataInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, BalanceData> balanceDataInfo : GameDataManager.balanceDataInfoList.entrySet()){

            int balanceKey = balanceDataInfo.getKey();
            BalanceData balanceValue = balanceDataInfo.getValue();
            balanceDataInfoList.put(balanceKey, balanceValue.clone());

        }

    }




}
