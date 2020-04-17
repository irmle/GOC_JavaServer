package ECS.System;

import ECS.Classes.CharacterLevelUpInfo;
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

        remainCoolTime -= worldMap.tickRate;
        if(remainCoolTime <= 0){
            remainCoolTime = coolTime;
        }
        else{
            return;
        }

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
                    character.hpComponent.maxHP = character.hpComponent.originalMaxHp;
                    //character.hpComponent.currentHP = character.hpComponent.maxHP;
                    character.hpComponent.recoveryRateHP += levelUpInfo.recoveryRateHP;

                    character.hpComponent.currentHP += levelUpInfo.maxHP;
                    if(character.hpComponent.currentHP > character.hpComponent.maxHP){
                        character.hpComponent.currentHP = character.hpComponent.maxHP;
                    }

                    character.mpComponent.originalMaxMP += levelUpInfo.maxMP;
                    character.mpComponent.maxMP = character.mpComponent.originalMaxMP;
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



}
