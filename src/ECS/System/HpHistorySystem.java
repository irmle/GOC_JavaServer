package ECS.System;

import ECS.ActionQueue.ActionStopUsingSkill;
import ECS.Classes.*;
import ECS.Classes.Type.Build.BuildSlotState;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Type.DeathType;
import ECS.Classes.Type.Jungle.JungleMobState;
import ECS.Classes.Type.SkillType;
import ECS.Components.ConditionComponent;
import ECS.Components.HPComponent;
import ECS.Entity.*;
import ECS.Factory.SkillFactory;
import ECS.Game.WorldMap;
import Network.RMI_Classes.RMI_Context;
import Network.RMI_Classes.RMI_ID;
import Network.RMI_Common.RMI_ParsingClasses.EntityType;
import Network.RMI_Common.server_to_client;

import java.util.HashMap;
import java.util.List;

/**
 * 업뎃날짜 : 2020 03 20 금 권령희
 * 업뎃내용 :
 *          2020 03 20
 *          귀환 도중 데미지를 받을 시, 바로 귀환 취소 처리를 위한 이벤트를 생성해서,
 *              이벤트 목록을 거치지 않고 곧바로 스킬 취소 매서드를 호출해 처리하도록 함.
 *
 *          ======================================================================================================
 *
 *              흡혈 관련 처리 로직 및 매서드 작성 (버서커 스킬 추가로 인한)
 *              ㄴ 일단은.. 몬스터 앤티티에만 적용하는 걸로?? (캐릭터가 공격하는 대상이 일단 몬스터 뿐이니)
 *
 *              흡혈 매서드 추가
 *
 *              흡혈 매서드 수정 ; 데미지히스토리에 바로 회복 넣어주는 게 아니라, 버프로 처리.
 *
 *              데미지 관련 RMI 추가. - 일단은 데미지 양과 타입만 전해주게끔 되어있음.
 *                                      나중에 데미지 리뉴얼 되면, 데미지를 발생시킨 스킬과 버프/효과 타입 등등도 추가해야.
 *
 *
 */
public class HpHistorySystem {

    WorldMap worldMap;
    RMI_ID[] TARGET;

    public HpHistorySystem(WorldMap worldMap) {

        this.worldMap = worldMap;

    }

    public void onUpdate(float deltaTime)
    {
        /*
         각 앤티티 종류에 대해

         앤티티 리스트를 얻는다.
         리스트 크기만큼 반복() {
            앤티티(i)의 체력 히스토리 목록의 모든 값을 더한다.
         반영한다
         사망 판정을 하고, 그에 따른 처리를 한다.
         */

        boolean testMode = false;

        /** 캐릭터 */
        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();
            if( worldMap.checkUserIsDead(character)){

                character.hpHistoryComponent.hpHistory.clear();
                continue;
            }

            if(testMode){
                continue;
            }

            ConditionComponent characterCond = character.conditionComponent;

            // 계산
            float sum = 0;
            int lastDamagedEntity = 0;  // 초기화.

            List<DamageHistory> hpHistory = character.hpHistoryComponent.hpHistory;
            for (int j=0; j<hpHistory.size(); j++){

                DamageHistory damageHistory = hpHistory.get(j);
                if(damageHistory.isDamage == true){

                    if(characterCond.isTargetingInvincible){
                        //continue;
                    }

                    /** 2020 03 20 금 권령희 추가 */
                    /**
                     * 귀환 상태일 경우, 귀환 취소 처리를 한다.
                     */
                    if(characterCond.isReturning){

                        ActionStopUsingSkill cancelRecallAction = new ActionStopUsingSkill(character.entityID, (short)100);
                        cancelRecallAction.actionType = SkillType.RECALL;
                        SkillFactory.stopUsingSkill(worldMap, cancelRecallAction);

                    }

                    /****************************************************/

                    sum -= damageHistory.amount;
                    lastDamagedEntity = damageHistory.unitID;
                    System.out.println("데미지를 받습니다 : " + damageHistory.amount);

                    /** 2020 02 12 수 추가 */
                    worldMap.playerGameScoreList.get(character.entityID).getDamagedAmount += damageHistory.amount;

                }
                else{
                    //System.out.println("체력을 회복합니다 : " + damageHistory.amount);
                    sum += damageHistory.amount;
                }

                broadCastDamage(character.entityID, damageHistory.damageType, damageHistory.amount);


            }

            // 반영
            HPComponent hpComponent = character.hpComponent;

            /** 오후 9:15 2020-05-05 추가 **************************/


            /** 데미지인 경우만 처리한다..  */
            if (sum < 0){

                /** 데미지량이 쉴드값보다 작은 경우 */
                if(( Math.abs(sum) < (hpComponent.shieldAmount))){

                    hpComponent.shieldAmount -= (Math.abs(sum));
                    sum = 0;
                }
                else{   /** 데미지량이 쉴드값을 넘어서는 경우 */

                    sum += hpComponent.shieldAmount;
                    hpComponent.shieldAmount = 0;

                    /** 쉴드값이 소진되었으므로, 스킬 효과도 없애준다 */
                    SkillFactory.cancelSkillBuffEffect(character, SkillType.MAGICIAN_SHIELD);
                    // ㄴ 효과를 받고있지 않은 경우라면.. 뭐 아무일도 일어나지 않을 것이고.
                }

            }


            /*********************************************************/

            float tempVal = hpComponent.currentHP + sum;
            if( tempVal <= 0f ){
                hpComponent.currentHP = 0f;
            }
            else if( hpComponent.maxHP > tempVal ){
                hpComponent.currentHP = tempVal;
            }
            else if( hpComponent.maxHP <= tempVal){
                hpComponent.currentHP = hpComponent.maxHP;
            }
            //System.out.println("현재 체력 : " + hpComponent.currentHP);



            /* 사망여부 판정 2019 12 15 ver. */

            boolean characterIsDead = (hpComponent.currentHP <= 0) ? true : false;  // 사망 여부를 판단한다

            //characterIsDead = false;    // 2019 12 17
                                        // 일단 아래 코드가 동작하지 않도록 제어.. 사망 시스템 나중에 돌리자
            if(characterIsDead){

                //System.out.println("캐릭터가 죽었습니다..");

                // 사망 정보를 담은 객체를 하나 만들어
                int deadCharacter = character.entityID;
                int deathType = DeathType.KIILED;
                int killedMonster = lastDamagedEntity;
                Death characterDeath = new Death(deadCharacter, deathType, killedMonster);

                // 월드의 사망 리스트에 담는다.
                worldMap.deathQueue.add(characterDeath);

            }

            /* ================================================================================================= */

            // 사망여부 판정 - 이전 버전. 돌지 않도록 막아둠.
            //if( hpComponent.currentHP <= 0){
            if(false){

                /*
                사망처리를 한다.
                LastDamageUnitID 값으로 부터, Unit객체를 찾아서,
                상태이상 컴포넌트를 가져와서 경험치 / 골드 비율에 접근후에 해당 비율에 맞춰서 경험치 & 골드를 준다.
                유닛ID로 캐릭터 컴포넌트를 가져올 수 있음. 캐릭터 컴포넌트의  경험치 골드 항목에 바로 반영한다.
                 */

                /** 부활 처리 작성해줘야 함 */
                // 캐릭터인 경우. 죽으면 부활한다.
                hpComponent.currentHP = 0;

                // 캐릭터 죽었을 때의 처리 구체적으로 뭐뭐 해줘야되지,,
                character.conditionComponent.isDisableMove = true;
                character.conditionComponent.isDisableAttack = true;
                character.conditionComponent.isUnTargetable = true;

                // 플레이어 캐릭터는, 죽었다고 앤티티 삭제요청 큐에 밀어넣지 않는다.
                /** 죽었다고 알림은 보내줘야 할듯. */

                //부활시간 계산.
                //5초 + (진행시간 1분당 +1.5초).  => 12분짜리 게임일 경우, 부활시간은 12*1.5 + 5 = 23초.
                float remainRespawnTimeMilliSeconds = ((float)worldMap.totalPlayTime / 60000f) * 1.5f + 5000f;

                //월드맵의 사망리스트에 추가해준다.
                worldMap.defeatCharacterList.add( new DefeatCharacterData(character, remainRespawnTimeMilliSeconds) );

                //이후, 캐릭터가 사망함을 월드맵에 중계한다.
                worldMap.userDefeated(character, (int)remainRespawnTimeMilliSeconds);

                /** entityID를 가지고 Entity를 찾기 */
                //MonsterEntity Enemy = worldMap.monsterEntity.get(lastDamagedEntity);
            }

            character.hpHistoryComponent.hpHistory.clear();
        }


        /** 몬스터 */
        for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()) {

            MonsterEntity monster = monsterEntity.getValue();
            if( (monster.hpComponent.currentHP <= 0)){
                continue;
            }

            // 계산
            float sum = 0f;
            int lastDamagedEntity = 0;
            List<DamageHistory> hpHistory = monster.hpHistoryComponent.hpHistory;
            for (int j=0; j<hpHistory.size(); j++){

                DamageHistory damageHistory = hpHistory.get(j);

                if(damageHistory.isDamage == true){
                    sum -= damageHistory.amount;
                    lastDamagedEntity = damageHistory.unitID;

                    //System.out.println(monster.monsterComponent.monsterName + monster.entityID + "가 데미지" + damageHistory.amount + "를 받습니다.");

                    /* 2020 01 24 추가 ; 흡혈 처리 */
                    //blooldSuck(lastDamagedEntity, damageHistory.amount);

                    /** 2020 02 12 수 추가 */
                    // 몹에게 데미지를 준 앤티티가 캐릭터일 경우, 해당 캐릭터의 스코어에 반영한다
                    // ㄴ 아마.. 투사체나 스킬 오브젝트 등등의 경우, 데미지를 준 주체가 그 효과를 발동시킬 캐릭터로 되어있을거긴 한데.
                    // 건물은 아직.

                    /**
                     * 2020 04 29 타워관련 예외처리,,
                     *
                     */

                    boolean attackerExist = worldMap.entityMappingList.containsKey(damageHistory.unitID);
                    if(attackerExist){

                        int attackerType = worldMap.entityMappingList.get(damageHistory.unitID);
                        if(attackerType == EntityType.CharacterEntity){
                            worldMap.playerGameScoreList.get(damageHistory.unitID).givenDamageAmount += damageHistory.amount;

                            /* 2020 01 24 추가 ; 흡혈 처리 */
                            blooldSuck(lastDamagedEntity, damageHistory.amount);

                        }

                    }


                    /** 2020 02 28 추가 */
                    /**
                     * 정글몬스터에 대해..
                     */
                    if(worldMap.jungleMonsterSlotHashMap.containsKey(monster.entityID)){

                        //System.out.println("정글몹이다 ");

                        JungleMonsterSlot slot = JungleMonsterSystem.findJungleSlotByMonsterID(worldMap, monster.entityID);
                        if (slot.monsterState == JungleMobState.IDLE){
                            slot.setMonsterState(JungleMobState.TARGET_INDICATE);
                        }

                    }

                }
                else{
                    sum += damageHistory.amount;
                }

                broadCastDamage(monster.entityID, damageHistory.damageType, damageHistory.amount);

            }

            // 반영
            HPComponent hpComponent = monster.hpComponent;

            float tempVal = hpComponent.currentHP + sum;
            if( tempVal <= 0f ){
                hpComponent.currentHP = 0f;
            }
            else if( hpComponent.maxHP > tempVal ){
                hpComponent.currentHP = tempVal;
            }
            else if( hpComponent.maxHP <= tempVal){
                hpComponent.currentHP = hpComponent.maxHP;
            }


            /* 사망여부 판정 2019 12 22 ver. */

            boolean monsterIsDead = (hpComponent.currentHP <= 0) ? true : false;  // 사망 여부를 판단한다

            //characterIsDead = false;    // 2019 12 17
            // 일단 아래 코드가 동작하지 않도록 제어.. 사망 시스템 나중에 돌리자
            if(monsterIsDead){

                //System.out.println("몬스터가 죽었습니다..");

                // 사망 정보를 담은 객체를 하나 만들어
                int deadMonster = monster.entityID;
                int deathType = DeathType.KIILED;
                int killedEntity = lastDamagedEntity;

                Death monsterDeath = new Death(deadMonster, deathType, killedEntity);

                // 월드의 사망 리스트에 담는다.
                /*boolean result = worldMap.deathQueue.offer(monsterDeath);
                if(result == false){    // queue.offer() 매서드가 실패 시 false를 리턴한다고 해서..
                    System.out.println("사망자를 추가하는 과정에서 오류가 발생");
                }*/
                worldMap.deathQueue.add(monsterDeath);

                if(worldMap.jungleMonsterSlotHashMap.containsKey(monster.entityID)){

                    //System.out.println("정글몹이다 죽음 hp시스템 ");

                    JungleMonsterSlot slot = JungleMonsterSystem.findJungleSlotByMonsterID(worldMap, monster.entityID);
                    slot.setMonsterState(JungleMobState.DIED);

                }

            }


            // 사망여부 판정
            //if( hpComponent.currentHP <= 0){
            if(false){

                /*
                사망처리를 한다.
                LastDamageUnitID 값으로 부터, Unit객체를 찾아서,
                상태이상 컴포넌트를 가져와서 경험치 / 골드 비율에 접근후에 해당 비율에 맞춰서 경험치 & 골드를 준다.
                유닛ID로 캐릭터 컴포넌트를 가져올 수 있음. 캐릭터 컴포넌트의  경험치 골드 항목에 바로 반영한다.
                 */

                /** entityID를 가지고 Entity를 찾기 */
                CharacterEntity enemy;
                for(HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()){

                    CharacterEntity character = characterEntity.getValue();
                    if(character.entityID == lastDamagedEntity){
                        enemy = character;

                        /** 캐릭에게 보상 처리를 */

                        int hardCodingMoney = 100;
                        int hardCodingExp = 100;

                        ConditionComponent condition = enemy.conditionComponent;
                        enemy.characterComponent.gold += (int) (hardCodingMoney * condition.goldGainRate);
                        enemy.characterComponent.exp += (int) (hardCodingExp * condition.expGainRate);
                        break;
                    }
                }


                /** 죽은 몬스터 앤티티를, 앤티티 삭제 요청 큐에 집어넣는다. */
                worldMap.requestDeleteQueue.add(monster);

                //System.out.println( monster.monsterComponent.monsterName + monster.entityID  +"가 죽었습니다.");

            }

            monster.hpHistoryComponent.hpHistory.clear();
        }




        /** 바리케이드 */
        for(HashMap.Entry<Integer, BarricadeEntity> barricadeEntity : worldMap.barricadeEntity.entrySet()){

            BarricadeEntity barricade = barricadeEntity.getValue();
            if( (barricade.hpComponent.currentHP <= 0)){
                continue;
            }

            // 계산
            float sum = 0;
            int lastDamagedEntity = 0;
            List<DamageHistory> hpHistory = barricade.hpHistoryComponent.hpHistory;
            for (int j=0; j<hpHistory.size(); j++){

                DamageHistory damageHistory = hpHistory.get(j);
                if(damageHistory.isDamage == true){
                    sum -= damageHistory.amount;
                    lastDamagedEntity = damageHistory.unitID;
                }
                else{
                    sum += damageHistory.amount;
                }

                broadCastDamage(barricade.entityID, damageHistory.damageType, damageHistory.amount);
            }

            // 반영
            HPComponent hpComponent = barricade.hpComponent;

            float tempVal = hpComponent.currentHP + sum;
            if( hpComponent.maxHP > tempVal ){
                hpComponent.currentHP = tempVal;
            }
            else if( hpComponent.maxHP <= tempVal){
                hpComponent.currentHP = hpComponent.maxHP;
            }

            boolean barriIsDestroyed = (hpComponent.currentHP <= 0) ? true : false;
            if(barriIsDestroyed) {

                //System.out.println("바리케이드가 파괴되었습니다..");

                // 사망 정보를 담은 객체를 하나 만들어
                int destroyedBarri = barricade.entityID;
                int deathType = DeathType.KIILED;
                int killedEntity = lastDamagedEntity;
                Death barriDeath = new Death(destroyedBarri, deathType, killedEntity);

                // 월드의 사망 리스트에 담는다.
                boolean result = worldMap.deathQueue.offer(barriDeath);

                if (result == false) {    // queue.offer() 매서드가 실패 시 false를 리턴한다고 해서..
                    //System.out.println("사망자를 추가하는 과정에서 오류가 발생");
                }
            }



            // 파괴 여부 판정 << 옛날버전
            if(false){
            //if( hpComponent.currentHP <= 0){

                hpComponent.currentHP = 0;

                barricade.conditionComponent.isUnTargetable = true;

                //System.out.println( "바리케이드 " + barricade.entityID  + "가 파괴되었습니다.");

                /** 앤티티 파괴 요청 큐에 넣기 */
                worldMap.requestDeleteQueue.add(barricade);

            }

            barricade.hpHistoryComponent.hpHistory.clear();
        }



        /** 공격 포탑 */
        for(HashMap.Entry<Integer, AttackTurretEntity> attackTurretEntity : worldMap.attackTurretEntity.entrySet()){

            AttackTurretEntity attackTurret = attackTurretEntity.getValue();
            if( (attackTurret.hpComponent.currentHP <= 0)){
                continue;
            }

            // 계산
            float sum = 0;
            int lastDamagedEntity = 0;
            List<DamageHistory> hpHistory = attackTurret.hpHistoryComponent.hpHistory;
            for (int j=0; j<hpHistory.size(); j++){

                DamageHistory damageHistory = hpHistory.get(j);
                if(damageHistory.isDamage == true){
                    sum -= damageHistory.amount;
                    lastDamagedEntity = damageHistory.unitID;
                }
                else{
                    sum += damageHistory.amount;
                }

                broadCastDamage(attackTurret.entityID, damageHistory.damageType, damageHistory.amount);

            }

            // 반영
            HPComponent hpComponent = attackTurret.hpComponent;

            float tempVal = hpComponent.currentHP + sum;
            if( tempVal <= 0f ){
                hpComponent.currentHP = 0f;
            }
            else if( hpComponent.maxHP > tempVal ){
                hpComponent.currentHP = tempVal;
            }
            else if( hpComponent.maxHP <= tempVal){
                hpComponent.currentHP = hpComponent.maxHP;
            }



            /* 사망여부 판정 2019 12 22 ver. */

            boolean turretIsDestroyed = (hpComponent.currentHP <= 0) ? true : false;  // 사망 여부를 판단한다

            //characterIsDead = false;    // 2019 12 17
            // 일단 아래 코드가 동작하지 않도록 제어.. 사망 시스템 나중에 돌리자
            if(turretIsDestroyed){

                //System.out.println("공격포탑이 파괴되었습니다..");

                // 사망 정보를 담은 객체를 하나 만들어
                int destroyedTurret = attackTurret.entityID;
                int deathType = DeathType.KIILED;
                int killedEntity = lastDamagedEntity;
                Death turretDeath = new Death(destroyedTurret, deathType, killedEntity);

                // 월드의 사망 리스트에 담는다.
                boolean result = worldMap.deathQueue.offer(turretDeath);

                if(result == false){    // queue.offer() 매서드가 실패 시 false를 리턴한다고 해서..
                    //System.out.println("사망자를 추가하는 과정에서 오류가 발생");
                }
            }


            // 파괴 여부 판정
            //if( hpComponent.currentHP <= 0){
            if(false){

                hpComponent.currentHP = 0;

                attackTurret.conditionComponent.isUnTargetable = true;



                worldMap.requestDeleteQueue.add(attackTurret);

                /**/
                BuildSlot slot = worldMap.buildSystem.findBuildSlotByEntityID(attackTurret.entityID);
                if(slot == null){

                    //System.out.println("널입니다");

                }
                else{
                    slot.setSlotState(BuildSlotState.DESTROYED);

                }


            }

            attackTurret.hpHistoryComponent.hpHistory.clear();
        }


        /** 버프 포탑*/
        for(HashMap.Entry<Integer, BuffTurretEntity> buffTurretEntity : worldMap.buffTurretEntity.entrySet()){

            BuffTurretEntity buffTurret = buffTurretEntity.getValue();
            if( (buffTurret.hpComponent.currentHP <= 0)){
                continue;
            }

            // 계산
            float sum = 0;
            int lastDamagedEntity = 0;
            List<DamageHistory> hpHistory = buffTurret.hpHistoryComponent.hpHistory;
            for (int j=0; j<hpHistory.size(); j++){

                DamageHistory damageHistory = hpHistory.get(j);
                if(damageHistory.isDamage == true){
                    sum -= damageHistory.amount;
                    lastDamagedEntity = damageHistory.unitID;
                    //System.out.println("데미지를 받습니다 : " + damageHistory.amount);
                }
                else{
                    sum += damageHistory.amount;
                }

                broadCastDamage(buffTurret.entityID, damageHistory.damageType, damageHistory.amount);

            }

            // 반영
            HPComponent hpComponent = buffTurret.hpComponent;


            float tempVal = hpComponent.currentHP + sum;
            if( tempVal <= 0f ){
                hpComponent.currentHP = 0f;
            }
            else if( hpComponent.maxHP > tempVal ){
                hpComponent.currentHP = tempVal;
            }
            else if( hpComponent.maxHP <= tempVal){
                hpComponent.currentHP = hpComponent.maxHP;
            }

            //System.out.println("현재 체력 : " + hpComponent.currentHP);

            /* 사망여부 판정 2019 12 22 ver. */

            boolean turretIsDestroyed = (hpComponent.currentHP <= 0) ? true : false;  // 사망 여부를 판단한다

            //characterIsDead = false;    // 2019 12 17
            // 일단 아래 코드가 동작하지 않도록 제어.. 사망 시스템 나중에 돌리자
            if(turretIsDestroyed){

                //System.out.println("버프포탑이 파괴되었습니다..");

                // 사망 정보를 담은 객체를 하나 만들어
                int destroyedTurret = buffTurret.entityID;
                int deathType = DeathType.KIILED;
                int killedEntity = lastDamagedEntity;
                Death turretDeath = new Death(destroyedTurret, deathType, killedEntity);

                // 월드의 사망 리스트에 담는다.
                boolean result = worldMap.deathQueue.offer(turretDeath);

                if(result == false){    // queue.offer() 매서드가 실패 시 false를 리턴한다고 해서..
                    //System.out.println("사망자를 추가하는 과정에서 오류가 발생");
                }
            }

            // 파괴 여부 판정
            //if( hpComponent.currentHP <= 0){
            if(false){

                hpComponent.currentHP = 0;

                buffTurret.conditionComponent.isUnTargetable = true;

                /* 앤티티 파괴 요청 큐에 넣기 */
                worldMap.requestDeleteQueue.add(buffTurret);

            }

            buffTurret.hpHistoryComponent.hpHistory.clear();
        }


        /** 크리스탈 */
        for(HashMap.Entry<Integer, CrystalEntity>  crystalEntity : worldMap.crystalEntity.entrySet()){

            CrystalEntity crystal = crystalEntity.getValue();
            if( (crystal.hpComponent.currentHP <= 0)){
                continue;
            }

            // 계산
            float sum = 0;
            int lastDamagedEntity = 0;
            List<DamageHistory> hpHistory = crystal.hpHistoryComponent.hpHistory;
            for (int j=0; j<hpHistory.size(); j++){

                DamageHistory damageHistory = hpHistory.get(j);
                if(damageHistory.isDamage == true){
                    sum -= damageHistory.amount;
                    lastDamagedEntity = damageHistory.unitID;
                }
                else{
                    sum += damageHistory.amount;
                }

                broadCastDamage(crystal.entityID, damageHistory.damageType, damageHistory.amount);

            }

            // 반영
            // 반영
            HPComponent hpComponent = crystal.hpComponent;

            float tempVal = hpComponent.currentHP + sum;
            if( tempVal <= 0f ){
                hpComponent.currentHP = 0f;
            }
            else if( hpComponent.maxHP > tempVal ){
                hpComponent.currentHP = tempVal;
            }
            else if( hpComponent.maxHP <= tempVal){
                hpComponent.currentHP = hpComponent.maxHP;
            }


            /* 사망여부 판정 2019 12 22 ver. */

            boolean crystalIsDestroyed = (hpComponent.currentHP <= 0) ? true : false;  // 사망 여부를 판단한다

            //characterIsDead = false;    // 2019 12 17
            // 일단 아래 코드가 동작하지 않도록 제어.. 사망 시스템 나중에 돌리자
            if(crystalIsDestroyed){

                //System.out.println("크리스탈이 파괴되었습니다..");

                // 사망 정보를 담은 객체를 하나 만들어
                int destroyedCrystal = crystal.entityID;
                int deathType = DeathType.KIILED;
                int killedEntity = lastDamagedEntity;
                Death crystalDeath = new Death(destroyedCrystal, deathType, killedEntity);

                // 월드의 사망 리스트에 담는다.
                boolean result = worldMap.deathQueue.offer(crystalDeath);

                if(result == false){    // queue.offer() 매서드가 실패 시 false를 리턴한다고 해서..
                    //System.out.println("사망자를 추가하는 과정에서 오류가 발생");
                }
            }

            // 파괴 여부 판정
            //if( hpComponent.currentHP <= 0){
            if(false){

                hpComponent.currentHP = 0;

                crystal.conditionComponent.isUnTargetable = true;

                /* 앤티티 파괴 요청 큐에 넣기 */
                worldMap.requestDeleteQueue.add(crystal);

            }

            crystal.hpHistoryComponent.hpHistory.clear();
        }

    }


    /**
     * 흡혈처리 구현용. 매 데미지 하나를 처리할 때 마다, 해당 매서드를 호출해서 흡혈 처리 적용가능 시 적요하게끔.
     * if 흡혈 비율값이 0보다 크지 않다면 그냥 리턴할 것.
     * bloodSucker
     * @param bloodSuckerID
     * @param damageAmount
     */
    public void blooldSuck(int bloodSuckerID, float damageAmount){

        /* 공격자를 찾는다  */
        CharacterEntity bloodSucker = worldMap.characterEntity.get(bloodSuckerID);
        HPComponent bloodSuckerHP = bloodSucker.hpComponent;
        ConditionComponent bloodSuckerCond = bloodSucker.conditionComponent;

        /* 공격자의 생사 여부에 따라 처리  */
        boolean bloodSuckerIsDead = (bloodSuckerHP.currentHP <= 0f) ? true : false;
        if(bloodSuckerIsDead){  // 공격자가 죽어있는 상태라면, 흡혈 처리를 하지 않는다.
            return;
        }

        /* 흡혈할 양을 계산한다 */
        float bloodAmount = damageAmount * bloodSuckerCond.bloodSuckingRate;
        if(bloodAmount <= 0f){
            return; // bloodSuckingRate가 0f라서 흡혈을 할 수 있는 상태가 아닌 경우, 흡혈처리 하지않음. > >나중에 할 수도? 걍 별 변화없이 0 띠링 띄워주고.
        }

        /* 공격자에게 회복 처리를 해준다 */

        // 헐.. 흡혈은.. 자가회복처럼 취급해야 하는건가?? 설마 흡혈대상을 넣어야?? ㅋㅋ >> 지금 시점에서 중요한 건 아닌듯
        /*
        DamageHistory bloodSucking = new DamageHistory(bloodSuckerID, false, bloodAmount);
        bloodSucker.hpHistoryComponent.hpHistory.add(bloodSucking);
        */

        /** 2020 01 30 수정 << 회복 처리를 직접 데미지에 넣어주는 게 아니라, 버프로 넣어줌 */

        BuffAction bloodSucking = new BuffAction();
        bloodSucking.unitID = bloodSuckerID;
        bloodSucking.skillUserID = bloodSuckerID;

        bloodSucking.remainTime = 0.15f;
        bloodSucking.coolTime = -1f;
        bloodSucking.remainCoolTime = -1f;

        // 적에게 입힌 데미지 한 건에 대한 '회복'으로 처리
        bloodSucking.floatParam.add(new ConditionFloatParam(ConditionType.hpRecoveryAmount, bloodAmount));

        bloodSucker.buffActionHistoryComponent.conditionHistory.add(bloodSucking);

    }

    public void broadCastDamage(int entityID, int damageType, float damageAmount){

        TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());

        server_to_client.broadcastingDamageAmount(TARGET, RMI_Context.Reliable_Public_AES256, damageType, entityID, damageAmount);

    }


}
