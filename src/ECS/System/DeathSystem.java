package ECS.System;

import ECS.Classes.*;
import ECS.Classes.Type.Build.BuildSlotState;
import ECS.Classes.Type.Jungle.JungleMobState;
import ECS.Components.ConditionComponent;
import ECS.Entity.*;
import ECS.Factory.RewardFactory;
import ECS.Game.WorldMap;
import RMI.RMI_Common._RMI_ParsingClasses.EntityType;

import java.util.Queue;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 16 월 새벽
 * 업뎃날짜 : 2020 02 28 금
 * 목    적 :
 *      월드맵의 사망 리스트를 읽어, 사망 객체 타입별로 필요한 처리를 한다.
 *          예)) 사망한 객체가 몬스터인 경우, 해당 몹을 죽인 캐릭터에게 보상 처리 후 몬스터 객체 삭제
 *               사망한 객체가 캐릭터인 경우, 해당 캐릭터의 사망처리 및 부활 처리를 함
 *               ...
 *               여기에다 오브젝트 파괴도 넣을지는 아직 고민중 (넣는편이 깔끔할 거 같긴 하다)
 *
 */
public class DeathSystem {

    WorldMap worldMap;

    float coolTime;
    float remainCoolTime;

    public DeathSystem(WorldMap worldMap, float coolTime) {
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

        Queue<Death> deaths = worldMap.deathQueue;

        int size = deaths.size();

        for(int i=0; i<size; i++){

            //System.out.println("죽음 갯수 : " + size);
            Death death = deaths.poll();

            /* 사망자 ID로, 사망자 객체 타입을 판단한다 */
            int entityType = worldMap.entityMappingList.get(death.deadEntityID);
            //System.out.println("킬러 : " + death.killerEntityID);

            switch (entityType){

                case EntityType.CharacterEntity :
                    processCharacterDeath(death);
                    break;

                case EntityType.MonsterEntity :

                    MonsterEntity monster = worldMap.monsterEntity.get(death.deadEntityID);
                    if(worldMap.jungleMonsterSlotHashMap.containsKey(monster.entityID)){
                        processJungleMonsterDeath(death);
                    }
                    else{
                        processMonsterDeath(death);
                    }
                    break;

                case EntityType.BarricadeEntity :
                    processBarricadeDeath(death);
                    break;

                case EntityType.AttackTurretEntity :
                    processAttackTurretDeath(death);
                    break;

                case EntityType.BuffTurretEntity :
                    processBuffTurretDeath(death);
                    break;

                case EntityType.CrystalEntity :
                    processCrystalDeath(death);
                    break;

                default:
                    break;
            }

            //deaths.poll();  // 삭제

        }

        /* 모든 사망 처리 후 */
        //deaths.clear();

    }

    /** 각 Entity 사망 처리 매서드 */

    public void processCharacterDeath(Death death){

        //System.out.println("캐릭터의 사망 처리를 시작합니다.");

        /* 캐릭터 정보 */
        CharacterEntity character = worldMap.characterEntity.get(death.deadEntityID);
        ConditionComponent charCondition = character.conditionComponent;

        /** 캐릭터의 상태를 변경해준다 */   // 이것도 나중에 별도 매서드 하나 만들어두는게.. setCharacterStateDeath 머 이런 이름으로
        charCondition.isUnTargetable = true;
        charCondition.isDisableAttack = true;
        charCondition.isDisableMove = true;
        charCondition.isDisableSkill = true;
        charCondition.isDisableItem = true;

        /** 캐릭터가 부활하는 데 필요한 처리를 한다 __ 기존 hp 시스템에서 수헌씨가 작성한 것 */

        //부활시간 계산.
        //5초 + (진행시간 1분당 +3초).  => 12분짜리 게임일 경우, 부활시간은 12*3 + 5 = 41초.
        float remainRespawnTimeMilliSeconds = ((float)worldMap.totalPlayTime / 60000f) * 3f + 5000f;

        //월드맵의 사망리스트에 추가해준다.
        worldMap.defeatCharacterList.add( new DefeatCharacterData(character, remainRespawnTimeMilliSeconds) );

        /** 2020 02 12 수 */
        /* 캐릭터의 스코어에 반영한다 */
        worldMap.playerGameScoreList.get(character.entityID).characterDeathCount++;


        //이후, 캐릭터가 사망함을 월드맵에 중계한다.
        worldMap.userDefeated(character, (int)remainRespawnTimeMilliSeconds);
    }

    public void processMonsterDeath(Death death){

        /*System.out.println("몬스터의 사망 처리를 시작합니다.");

        System.out.println("사망한 몬스터 ID : "  + death.deadEntityID);
        System.out.println("몬스터를 사망시킨 EntityID : "  + death.killerEntityID);
*/

        /* 몬스터를 살해한 Entity의 정보를 찾는다 */
        Entity killer = null;
        int killerEntityType;

        /** 2020 02 12 수 추가 */
        // 현재.. 몹이 뭐 투사체 그런애들에 의해 죽은 경우, 몬스터의 사망을 처리하는 시점에 이미 그런 투사체 등등이
        // 삭제되어있을 가능성이 있다. 그런 경우 null 오류를 일으키는데, 이를 막고자 일단은 아래와 같이 예외 처리를 함
        // 아마 투사체나 장판같은 경우는, 지금쯤(?) 데미지를 입힌 주체가(killer) 그 스킬 시전자로 되어있을거긴 한데.
        // 건물같은 경우는 아직 모르겠네...
        // 나중에 이런거 고려해서 반영할 것
        // 아 그리고. 현재는, 가장 마지막에 타격을 준 애가 killer로 판정되는데. 이대로 쭉 갈수도 있지만??
        // 뭐 그 몹한테 타격을 가장 많이 입힌 애한테 kill 수를 주는걸로 나중에 바뀔 수도 있잖음? 이런 변경사항들 다 고려해도
        // 크게 무리가 없을 구조 생각해두기

        if(worldMap.entityMappingList.containsKey(death.killerEntityID)){

            killerEntityType = worldMap.entityMappingList.get(death.killerEntityID);
        }
        else{
            return;
        }


        //killerEntityType = EntityType.CharacterEntity;
        //System.out.println("킬러 앤티티타입 : " + killerEntityType);

        /* 죽은 몬스터 정보를 찾는다 */
        MonsterEntity deadMonster = worldMap.monsterEntity.get(death.deadEntityID);
        int typeOfDeadMonster = deadMonster.monsterComponent.monsterType;
        int monsterLevel = deadMonster.monsterComponent.monsterLevel;

          //      System.out.println("죽은 몬스터의 타입 : " + typeOfDeadMonster);

        Reward reward;

        /* 살해한 Entity의 타입에 따라 처리한다 */

        switch (killerEntityType) {

            case EntityType.CharacterEntity :

                /* 몹을 죽인 캐릭터를 찾는다 */
                killer = worldMap.characterEntity.get(death.killerEntityID);


                /* 몹을 죽인 캐릭터에게 보상을 넣어준다 */
                //reward = RewardFactory.createRewardByKillMonster(typeOfDeadMonster, monsterLevel);

                // 테스트용 빨리 만렙 만들기
                boolean isTestMode = false;
                if(isTestMode){   // 테스트
                    for(int i=0; i<100; i++){
                        //((CharacterEntity) killer).rewardHistoryComponent.rewardHistory.add(reward);
                    }
                }
                else{   // 테스트가 아님
                    //((CharacterEntity) killer).rewardHistoryComponent.rewardHistory.add(reward);
                }

                /** 2020 02 12 수 추가 */
                // 킬러 캐릭터의 스코어에 반영한다
                worldMap.playerGameScoreList.get(killer.entityID).monsterKillCount++;
                break;

            /**
             * 2019 12 22
             * 일단, 해당 포탑을 건설한 유저에게, 본인이 직접 처리했을 때 받는 보상의 10%을 받도록 함.
             */
            case EntityType.AttackTurretEntity :

                /* 포탑 정보 */
                AttackTurretEntity attackTurret = worldMap.attackTurretEntity.get(death.killerEntityID);

                /* 포탑을 건설한 유저 정보를 찾는다 */
                BuildSlot buildSlot = worldMap.buildSystem.findBuildSlotByEntityID(attackTurret.entityID);
                int buildUserEntityID = buildSlot.getBuilderEntityID();
                CharacterEntity turretBuildUser = worldMap.characterEntity.get(buildUserEntityID);
                int builderLevel = turretBuildUser.characterComponent.level;

                /** 2020 04 15 */
                killer = turretBuildUser;

                /** 2020 02 12 수 추가 */
                // 킬러 캐릭터의 스코어에 반영한다
                worldMap.playerGameScoreList.get(killer.entityID).monsterKillCount++;



                /* 해당 유저에게 보상을 넣어준다 */
                //reward = RewardFactory.createRewardOfKillMonsterByTurret(typeOfDeadMonster, monsterLevel);
                //turretBuildUser.rewardHistoryComponent.rewardHistory.add(reward);

                break;

            /**
             * 2019 12 23 업뎃
             * 타게팅 투사체를 처리하는 부분에서, .. 걔네가 타겟들한테 넣어주는 버프에서 버프의 EntityID가 얘네로 되어있는데,
             * 얘네들에 대한 케이스가 없었다고 해야되나.. 아니근데 없으면 없는대로 처리해주면 되는 부분 아닌가
             * 뭐 이거 추가해보고 결과 보면 알겠지
             * 안되면 걍 아까해줬던 것처럼 스킬쪽을 조지던가
             *
             */
            case EntityType.FlyingObjectEntity :

                /* 몹을 죽인 투사체 정보를 찾는다 */
                killer = worldMap.characterEntity.get(death.killerEntityID);

                /* 해당 투사체를 생성한 스킬 시전자를 찾는다 => 어차피 캐릭터일 것. */
                int skillUserID = ((FlyingObjectEntity ) killer).flyingObjectComponent.userEntityID;
                CharacterEntity skillUser = worldMap.characterEntity.get(skillUserID);

                /** 2020 04 15 */
                killer = skillUser;

                /* 몹을 죽인 캐릭터에게 보상을 넣어준다 */
                //reward = RewardFactory.createRewardByKillMonster(typeOfDeadMonster, monsterLevel);
                //skillUser.rewardHistoryComponent.rewardHistory.add(reward);

                /** 2020 02 12 수 추가 */
                // 킬러 캐릭터의 스코어에 반영한다
                worldMap.playerGameScoreList.get(killer.entityID).monsterKillCount++;


                break;

            case EntityType.SkillObjectEntity :

                break;


            default:
                break;

        }


        /** 2020 04 15 뉴 보상 처리 */

        /* 보상 get */
        reward = RewardFactory.createRewardByKillMonster(typeOfDeadMonster, monsterLevel);

        /* 경험치 N빵 */
        reward.rewardExp /= (worldMap.characterEntity.size());
        reward.rewardGold *= 100;

        Reward rewardN;
        for(CharacterEntity character : worldMap.characterEntity.values()){

            rewardN = (Reward) reward.clone();

            boolean isKiller = (character.entityID == killer.entityID) ? true : false;
            if(!isKiller){

                rewardN.rewardGold = 0;
            }

            character.rewardHistoryComponent.rewardHistory.add(reward);
        }


        System.out.println("킬수 : " + worldMap.playerGameScoreList.get(killer.entityID).monsterKillCount);


        /* 죽은 몹을 삭제 요청 목록에 넣어준다 */
        worldMap.requestDeleteQueue.add(deadMonster);

        //사망처리된 몬스터를 목록에서 삭제한다.
        worldMap.monsterEntity.remove(death.deadEntityID);
        worldMap.entityMappingList.remove(death.deadEntityID);

        //System.out.println("몬스터 죽음 처리 완료");
    }

    public void processBarricadeDeath(Death death){

        //System.out.println("바리케이드의 파괴 처리를 시작합니다.");
        /* 바리케이드 정보 */
        BarricadeEntity barricade = worldMap.barricadeEntity.get(death.deadEntityID);
        /* 바리케이드의 상태를 변경한다 */
        barricade.conditionComponent.isDisableAttack = true;
        barricade.conditionComponent.isUnTargetable = true;
        /* 바리케이드이 속해있는 건설 슬롯을 찾아, 슬롯의 상태를 DESTROYED로 변경한다 */
        BuildSlot buildSlot = worldMap.buildSystem.findBuildSlotByEntityID(barricade.entityID);
        buildSlot.setSlotState(BuildSlotState.DESTROYED);
        /* Entity 삭제요청 큐에 넣는다 */
        worldMap.requestDeleteQueue.add(barricade);

        //파괴처리된 바리케이드를 목록에서 삭제한다.
        worldMap.barricadeEntity.remove(death.deadEntityID);
        worldMap.entityMappingList.remove(death.deadEntityID);
    }

    public void processAttackTurretDeath(Death death){

        //System.out.println("공격포탑의 파괴 처리를 시작합니다.");

        /* 포탑 정보 */
        AttackTurretEntity attackTurret = worldMap.attackTurretEntity.get(death.deadEntityID);

        /* 포탑의 상태를 변경한다 */
        attackTurret.conditionComponent.isDisableAttack = true;
        attackTurret.conditionComponent.isUnTargetable = true;

        /* 포탑이 속해있는 건설 슬롯을 찾아, 슬롯의 상태를 DESTROYED로 변경한다 */
        BuildSlot buildSlot = worldMap.buildSystem.findBuildSlotByEntityID(attackTurret.entityID);
        buildSlot.setSlotState(BuildSlotState.DESTROYED);

        /* Entity 삭제요청 큐에 넣는다 */
        worldMap.requestDeleteQueue.add(attackTurret);

        //파괴처리된 포탑을 목록에서 삭제한다.
        worldMap.attackTurretEntity.remove(death.deadEntityID);
        worldMap.entityMappingList.remove(death.deadEntityID);

    }

    public void processBuffTurretDeath(Death death){

        //System.out.println("버프포탑의 파괴 처리를 시작합니다.");
        /* 포탑 정보 */
        BuffTurretEntity buffTurret = worldMap.buffTurretEntity.get(death.deadEntityID);
        /* 포탑의 상태를 변경한다 */
        buffTurret.conditionComponent.isDisableAttack = true;
        buffTurret.conditionComponent.isUnTargetable = true;
        /* 포탑이 속해있는 건설 슬롯을 찾아, 슬롯의 상태를 DESTROYED로 변경한다 */
        BuildSlot buildSlot = worldMap.buildSystem.findBuildSlotByEntityID(buffTurret.entityID);
        buildSlot.setSlotState(BuildSlotState.DESTROYED);
        /* Entity 삭제요청 큐에 넣는다 */
        worldMap.requestDeleteQueue.add(buffTurret);

        //파괴처리된 포탑을 목록에서 삭제한다.
        worldMap.buffTurretEntity.remove(death.deadEntityID);
        worldMap.entityMappingList.remove(death.deadEntityID);

    }


    public void processCrystalDeath(Death death){

        /* 크리스탈의 상태를 변경한다 */
        CrystalEntity crystal = worldMap.crystalEntity.get(death.deadEntityID);

        /* 크리스탈을 삭제요청 큐에 넣는다 */
        worldMap.requestDeleteQueue.add(crystal);

    }

    /**
     * 정글은... 포탑이 죽이고 어쩌고 이런걸 따지는 게 의미가 있나 싶긴한데..
     * @param death
     */
    public void processJungleMonsterDeath(Death death){
/*

        System.out.println("정글 몬스터의 사망 처리를 시작합니다.");

        System.out.println("사망한 몬스터 ID : "  + death.deadEntityID);
        System.out.println("몬스터를 사망시킨 EntityID : "  + death.killerEntityID);
*/


        /* 몬스터를 살해한 Entity의 정보를 찾는다 */
        Entity killer = null;
        int killerEntityType;

        /** 2020 02 12 수 추가 */
        // 현재.. 몹이 뭐 투사체 그런애들에 의해 죽은 경우, 몬스터의 사망을 처리하는 시점에 이미 그런 투사체 등등이
        // 삭제되어있을 가능성이 있다. 그런 경우 null 오류를 일으키는데, 이를 막고자 일단은 아래와 같이 예외 처리를 함
        // 아마 투사체나 장판같은 경우는, 지금쯤(?) 데미지를 입힌 주체가(killer) 그 스킬 시전자로 되어있을거긴 한데.
        // 건물같은 경우는 아직 모르겠네...
        // 나중에 이런거 고려해서 반영할 것
        // 아 그리고. 현재는, 가장 마지막에 타격을 준 애가 killer로 판정되는데. 이대로 쭉 갈수도 있지만??
        // 뭐 그 몹한테 타격을 가장 많이 입힌 애한테 kill 수를 주는걸로 나중에 바뀔 수도 있잖음? 이런 변경사항들 다 고려해도
        // 크게 무리가 없을 구조 생각해두기

        if(worldMap.entityMappingList.containsKey(death.killerEntityID)){

            killerEntityType = worldMap.entityMappingList.get(death.killerEntityID);
        }
        else{
            return;
        }


        //killerEntityType = EntityType.CharacterEntity;
//        System.out.println("킬러 앤티티타입 : " + killerEntityType);

        /* 죽은 몬스터 정보를 찾는다 */
        MonsterEntity deadMonster = worldMap.monsterEntity.get(death.deadEntityID);
        int typeOfDeadMonster = deadMonster.monsterComponent.monsterType;
        int monsterLevel = deadMonster.monsterComponent.monsterLevel;

  //      System.out.println("죽은 몬스터의 타입 : " + typeOfDeadMonster);

        /**
         * 2020 02 28
         *
         */
        JungleMonsterSlot slot = JungleMonsterSystem.findJungleSlotByMonsterID(worldMap, deadMonster.entityID);
        slot = worldMap.jungleMonsterSlotHashMap.get(deadMonster.entityID);
        //slot.setMonsterState(JungleMobState.DIED);




        Reward reward;

        /* 살해한 Entity의 타입에 따라 처리한다 */
        switch (killerEntityType) {

            case EntityType.CharacterEntity :

                /* 몹을 죽인 캐릭터를 찾는다 */
                killer = worldMap.characterEntity.get(death.killerEntityID);
                int killerLevel = ((CharacterEntity) killer).characterComponent.level;

                /* 몹을 죽인 캐릭터에게 보상을 넣어준다 */
                //reward = RewardFactory.createRewardByKillMonster(typeOfDeadMonster, killerLevel);
                reward = RewardFactory.createRewardByKillJungleMonster(typeOfDeadMonster, deadMonster.entityID,monsterLevel);

                // 테스트용 빨리 만렙 만들기
                boolean isTestMode = false;
                if(isTestMode){   // 테스트
                    for(int i=0; i<100; i++){
                        ((CharacterEntity) killer).rewardHistoryComponent.rewardHistory.add(reward);
                    }
                }
                else{   // 테스트가 아님
                    ((CharacterEntity) killer).rewardHistoryComponent.rewardHistory.add(reward);
                }

                /** 2020 02 12 수 추가 */
                // 킬러 캐릭터의 스코어에 반영한다
                worldMap.playerGameScoreList.get(killer.entityID).monsterKillCount++;

                break;

            /**
             * 2019 12 22
             * 일단, 해당 포탑을 건설한 유저에게, 본인이 직접 처리했을 때 받는 보상의 10%을 받도록 함.
             */
            case EntityType.AttackTurretEntity :

                /* 포탑 정보 */
                AttackTurretEntity attackTurret = worldMap.attackTurretEntity.get(death.killerEntityID);

                /* 포탑을 건설한 유저 정보를 찾는다 */
                BuildSlot buildSlot = worldMap.buildSystem.findBuildSlotByEntityID(attackTurret.entityID);
                int buildUserEntityID = buildSlot.getBuilderEntityID();
                CharacterEntity turretBuildUser = worldMap.characterEntity.get(buildUserEntityID);
                int builderLevel = turretBuildUser.characterComponent.level;

                killer = turretBuildUser;

                /* 해당 유저에게 보상을 넣어준다 */
                //reward = RewardFactory.createRewardOfKillMonsterByTurret(typeOfDeadMonster, builderLevel);
                reward = RewardFactory.createRewardByKillJungleMonster(typeOfDeadMonster, deadMonster.entityID, monsterLevel);
                turretBuildUser.rewardHistoryComponent.rewardHistory.add(reward);

                break;

            /**
             * 2019 12 23 업뎃
             * 타게팅 투사체를 처리하는 부분에서, .. 걔네가 타겟들한테 넣어주는 버프에서 버프의 EntityID가 얘네로 되어있는데,
             * 얘네들에 대한 케이스가 없었다고 해야되나.. 아니근데 없으면 없는대로 처리해주면 되는 부분 아닌가
             * 뭐 이거 추가해보고 결과 보면 알겠지
             * 안되면 걍 아까해줬던 것처럼 스킬쪽을 조지던가
             *
             */
            case EntityType.FlyingObjectEntity :

                /* 몹을 죽인 투사체 정보를 찾는다 */
                killer = worldMap.characterEntity.get(death.killerEntityID);

                /* 해당 투사체를 생성한 스킬 시전자를 찾는다 => 어차피 캐릭터일 것. */
                int skillUserID = ((FlyingObjectEntity ) killer).flyingObjectComponent.userEntityID;
                CharacterEntity skillUser = worldMap.characterEntity.get(skillUserID);

                /* 몹을 죽인 캐릭터에게 보상을 넣어준다 */
                //reward = RewardFactory.createRewardByKillMonster(typeOfDeadMonster, killer.entityID);
                reward = RewardFactory.createRewardByKillJungleMonster(typeOfDeadMonster, deadMonster.entityID, monsterLevel);
                skillUser.rewardHistoryComponent.rewardHistory.add(reward);


                break;

            case EntityType.SkillObjectEntity :

                /* 몹을 죽인 장판 정보를 찾는다 */
                killer = worldMap.characterEntity.get(death.killerEntityID);


                CharacterEntity user = worldMap.characterEntity.get(killer.entityID);

                /* 몹을 죽인 캐릭터에게 보상을 넣어준다 */
                //reward = RewardFactory.createRewardByKillMonster(typeOfDeadMonster, killer.entityID);
                reward = RewardFactory.createRewardByKillJungleMonster(typeOfDeadMonster, deadMonster.entityID, monsterLevel);
                user.rewardHistoryComponent.rewardHistory.add(reward);


                break;


            default:
                break;

        }

        System.out.println("킬수 : " + worldMap.playerGameScoreList.get(killer.entityID).monsterKillCount);

        /* 죽은 몹을 삭제 요청 목록에 넣어준다 */
        worldMap.requestDeleteQueue.add(deadMonster);

        //사망처리된 몬스터를 목록에서 삭제한다.
        worldMap.monsterEntity.remove(death.deadEntityID);
        worldMap.entityMappingList.remove(death.deadEntityID);
    //    System.out.println("몬스터 죽음 처리 완료");

    }


}
