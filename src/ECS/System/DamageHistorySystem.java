package ECS.System;

import ECS.Classes.DamageHistory;
import ECS.Classes.Type.AttributeSynastryType;
import ECS.Classes.Type.AttributeType;
import ECS.Classes.Type.DamageType;
import ECS.Components.*;
import ECS.Entity.*;
import ECS.Game.GameDataManager;
import ECS.Game.WorldMap;
import RMI.RMI_Common._RMI_ParsingClasses.EntityType;
import org.omg.CORBA.INTERNAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 04 화
 * 업뎃날짜 :
 * 업뎃내용 :
 * 목    적 :
 *      발생한 데미지를 적용하기 전에, 각종 보정 처리를 담당하는 시스템.
 *      버프 시스템과 HP 시스템 사이 혹은 단위 로직 맨 마지막에 수행될 것임.
 *      ㄴ 우선은 모든 데미지가 버프를 통하도록 되어 있으므로, 버프랑 HP
 *      ㄴ 이후에 다이렉트로 데미지를 생성하도록 한 후, 로직 맨 마지막에 처리.
 *
 *      큰 로직 :
 *
 *          각 앤티티 타입별 리스트 전체에 대해 반복한다
 *          for(특정 앤티티 타입 리스트 전체에 대해 ){
 *
 *                  for( 앤티티 하나의 DamageHistory 리스트 전체 대해 ){
 *
 *                      데미지 히스토리 하나에 대해, 다음 처리를 반복한다
 *
 *                      if 데미지가 아니라 회복이라면 PASS
 *
 *                      1) 평타 계산
 *                          - 기존 데미지에 들어있는 값을 가지고 처리함
 *                          - 데미지의 밸런스 맞추기
 *                          - 앤티티의 방어력 적용
 *
 *                      2) 크리티컬 처리
 *                      if(데미지 타입이 크리티컬이라면)
 *                          공격자 상태값을 참조해서, 크리티컬 데미지를 적용함
 *
 *                      3) 속성 처리
 *                          공격자랑 앤티티랑 속성값 참조해서 속성 보정 함
 *
 *                      4) 그 외 보정 처리 할 것 있으면 더 해주고...
 *
 *                      최종값을 데미지값으로 업데이트한다.
 *
 *                      여기서 혹은 hp에서 RMI 호출해줌 ; 데미지 타입에 따라....
 *
 *
 *                  }
 *          }
 *
 *      처리를 수행하는 데 필요한 다른 처리 :
 *          앤티티 ID로 앤티티 객체 찾기
 *
 *          밸런스맞춰진 평타댐
 *          치명타 적용하기
 *          속성보정하기
 *
 *
 * 업뎃할 내용 :
 *
 *
 */
public class DamageHistorySystem {

    WorldMap worldMap;

    static final float BALNENCE_VALUE = 30f;

    /* 상성 처리를 위한 */
    HashMap<Integer, Float> damageRatePerSynastry;
    HashMap<Integer, HashMap<Integer, Integer>> attrTablePerAttacker;

    public DamageHistorySystem(WorldMap worldMap) {
        this.worldMap = worldMap;

        damageRatePerSynastry = GameDataManager.synastryEffectValueList;
        attrTablePerAttacker = GameDataManager.elementalSynastryInfoList;

        // readAttributeInfo();
    }

    public void onUpdate(float deltaTime){

        updateCharacterDamage();
        updateMonsterDamage();
        updateAttackTurretDamage();
        updateBuffTurretDamage();
        updateBarricadeDamage();
        updateCrystalDamage();

    }

    /*******************************************************************************************************************/

    /**'업데이트'말고.. 다른 이름 붙이는게 좋을 듯 나중에 */

    /**
     * 캐릭터 엔티티들의 데미지 보정처리를 함.
     */
    public void updateCharacterDamage() {

        //System.out.println("캐릭터 뎀 처리");

        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();
            ConditionComponent characterCondition = character.conditionComponent;

            List<DamageHistory> damageHistory = character.hpHistoryComponent.hpHistory;

            DamageHistory currentDamage;
            for(int i=0; i<damageHistory.size(); i++){

                currentDamage = damageHistory.get(i);

                // 데미지가 아니라 회복이라면 패스
                boolean isRecovery = (currentDamage.isDamage == false) ? true : false;
                if(isRecovery){
                    continue;
                }

                if(characterCondition.isTargetingInvincible){
                    currentDamage.amount = 0;
                    continue;
                }


                float finalDamage = 0f;

                /* 공격자를 찾는다 */
                Entity attacker = null;
                AttackComponent attackerAttack = null;
                ConditionComponent attackerCondition = null;


                if(!worldMap.entityMappingList.containsKey(currentDamage.unitID)){

                    continue;
                }

                int entityType = worldMap.entityMappingList.get(currentDamage.unitID);

                switch (entityType){
                    case EntityType.CharacterEntity :
                        attacker = worldMap.characterEntity.get(currentDamage.unitID);
                        attackerAttack = ((CharacterEntity)attacker).attackComponent;
                        attackerCondition = ((CharacterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.MonsterEntity :
                        attacker = worldMap.monsterEntity.get(currentDamage.unitID);
                        attackerAttack = ((MonsterEntity)attacker).attackComponent;
                        attackerCondition = ((MonsterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.AttackTurretEntity :
                        attacker = worldMap.attackTurretEntity.get(currentDamage.unitID);
                        attackerAttack = ((AttackTurretEntity)attacker).attackComponent;
                        attackerCondition = ((AttackTurretEntity)attacker).conditionComponent;
                        break;
                }


                /* 평타 구함 */
                float flatDamage =
                        calculateFlatDamage(currentDamage.amount, attackerAttack, attackerCondition, character.defenseComponent, characterCondition);

                /* 치명타 적용함.. 혹은 데미지 타입별 특수처리가 필요한 경우 */
                switch(currentDamage.damageType){

                    case DamageType.FLAT_DAMAGE :

                        finalDamage = flatDamage;
                        break;

                    case DamageType.CRITICAL_DAMAGE :

                        float criticalDamage = calculateCriticalDamage(flatDamage, attackerAttack, attackerCondition);
                        finalDamage = criticalDamage;
                        break;
                }

                /* 속성보정 처리함 */
                float attrDamage;
                if(attacker.attribute == AttributeType.NONE){
                    attrDamage = finalDamage;
                }
                else{
                    attrDamage = applyAttribute(finalDamage, attacker.attribute, character.attribute);
                }


                /** 기타 최종딜 추가 처리가 필요하다면 여기에 */

                /* damageHistory에 최종 데미지를 업데이트함 */
                currentDamage.amount = attrDamage;

                /* 중계용 RMI 호출 */

            }


        }
    }

    public void updateMonsterDamage(){

        //System.out.println("몹뎀 처리");


        for (HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()) {

            MonsterEntity monster = monsterEntity.getValue();
            ConditionComponent mobCondition = monster.conditionComponent;

            HPComponent monsterHp = monster.hpComponent;
            if(monsterHp.currentHP <= 0){
                continue;

                /**
                 * 사실 이 처리는.. 몹의 정보를 참조하는 데 있어서, 얘가 죽은애든 말든
                 * 일단 서버에서 아직 사라지진 않은 거라서 별 문제는 없을건데 말임...
                 */
            }

            List<DamageHistory> damageHistory = monster.hpHistoryComponent.hpHistory;

            DamageHistory currentDamage;
            for(int i=0; i<damageHistory.size(); i++){

                currentDamage = damageHistory.get(i);

                // 데미지가 아니라 회복이라면 패스
                boolean isRecovery = (currentDamage.isDamage == false) ? true : false;
                if(isRecovery){
                    continue;
                }

                float finalDamage = 0f;

                /* 공격자를 찾는다 */
                Entity attacker = null;
                AttackComponent attackerAttack = null;
                ConditionComponent attackerCondition = null;

                if(!(worldMap.entityMappingList.containsKey(currentDamage.unitID))){

                    continue;
                }
                int entityType = worldMap.entityMappingList.get(currentDamage.unitID);

                switch (entityType){
                    case EntityType.CharacterEntity :
                        attacker = worldMap.characterEntity.get(currentDamage.unitID);
                        attackerAttack = ((CharacterEntity)attacker).attackComponent;
                        attackerCondition = ((CharacterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.MonsterEntity :
                        attacker = worldMap.monsterEntity.get(currentDamage.unitID);
                        attackerAttack = ((MonsterEntity)attacker).attackComponent;
                        attackerCondition = ((MonsterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.AttackTurretEntity :
                        attacker = worldMap.attackTurretEntity.get(currentDamage.unitID);
                        attackerAttack = ((AttackTurretEntity)attacker).attackComponent;
                        attackerCondition = ((AttackTurretEntity)attacker).conditionComponent;
                        break;
                }


                /* 평타 구함 */
                float flatDamage =
                        calculateFlatDamage(currentDamage.amount, attackerAttack, attackerCondition, monster.defenseComponent, monster.conditionComponent);

                /* 치명타 적용함.. 혹은 데미지 타입별 특수처리가 필요한 경우 */
                switch(currentDamage.damageType){

                    case DamageType.FLAT_DAMAGE :

                        finalDamage = flatDamage;
                        break;

                    case DamageType.CRITICAL_DAMAGE :

                        float criticalDamage = calculateCriticalDamage(flatDamage, attackerAttack, attackerCondition);
                        finalDamage = criticalDamage;
                        break;
                }

                /* 속성보정 처리함 */
                float attrDamage;
                if(attacker.attribute == AttributeType.NONE){
                    attrDamage = finalDamage;
                }
                else{
                    attrDamage = applyAttribute(finalDamage, attacker.attribute, monster.attribute);
                }



                /** 기타 최종딜 추가 처리가 필요하다면 여기에 */

                /* damageHistory에 최종 데미지를 업데이트함 */
                currentDamage.amount = attrDamage;

                /* 중계용 RMI 호출 */

            }


        }

    }

    public void updateAttackTurretDamage(){

        for (HashMap.Entry<Integer, AttackTurretEntity> attackTurretEntity : worldMap.attackTurretEntity.entrySet()) {

            AttackTurretEntity attackTurret = attackTurretEntity.getValue();
            ConditionComponent attackTurretCondition = attackTurret.conditionComponent;

            List<DamageHistory> damageHistory = attackTurret.hpHistoryComponent.hpHistory;

            DamageHistory currentDamage;
            for(int i=0; i<damageHistory.size(); i++){

                currentDamage = damageHistory.get(i);

                // 데미지가 아니라 회복이라면 패스
                boolean isRecovery = (currentDamage.isDamage == false) ? true : false;
                if(isRecovery){
                    continue;
                }

                float finalDamage = 0f;

                /* 공격자를 찾는다 */
                Entity attacker = null;
                AttackComponent attackerAttack = null;
                ConditionComponent attackerCondition = null;

                // .... 2020 02 13...
                if(!worldMap.entityMappingList.containsKey(currentDamage.unitID)){

                    continue;
                }

                int entityType = worldMap.entityMappingList.get(currentDamage.unitID);
                switch (entityType){
                    case EntityType.CharacterEntity :
                        attacker = worldMap.characterEntity.get(currentDamage.unitID);
                        attackerAttack = ((CharacterEntity)attacker).attackComponent;
                        attackerCondition = ((CharacterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.MonsterEntity :
                        attacker = worldMap.monsterEntity.get(currentDamage.unitID);
                        attackerAttack = ((MonsterEntity)attacker).attackComponent;
                        attackerCondition = ((MonsterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.AttackTurretEntity :
                        attacker = worldMap.attackTurretEntity.get(currentDamage.unitID);
                        attackerAttack = ((AttackTurretEntity)attacker).attackComponent;
                        attackerCondition = ((AttackTurretEntity)attacker).conditionComponent;
                        break;
                }


                /* 평타 구함 */
                float flatDamage =
                        calculateFlatDamage(currentDamage.amount, attackerAttack, attackerCondition, attackTurret.defenseComponent, attackTurretCondition);

                /* 치명타 적용함.. 혹은 데미지 타입별 특수처리가 필요한 경우 */
                switch(currentDamage.damageType){

                    case DamageType.FLAT_DAMAGE :

                        finalDamage = flatDamage;
                        break;

                    case DamageType.CRITICAL_DAMAGE :

                        float criticalDamage = calculateCriticalDamage(flatDamage, attackerAttack, attackerCondition);
                        finalDamage = criticalDamage;
                        break;
                }

                /**
                 * 2020 02 07 캐릭터랑 몬스터를 제외한 녀석들은 속성 없으므로
                 *  속성 처리 제외, 위에 finalDamage를 최종 데미지로 적용
                 */

                /* 속성보정 처리함 */
                //

                /** 기타 최종딜 추가 처리가 필요하다면 여기에 */

                /* damageHistory에 최종 데미지를 업데이트함 */
                currentDamage.amount = finalDamage;

            }


        }

    }

    public void updateBuffTurretDamage(){

        for (HashMap.Entry<Integer, BuffTurretEntity> buffTurretEntity : worldMap.buffTurretEntity.entrySet()) {

            BuffTurretEntity buffTurret = buffTurretEntity.getValue();
            ConditionComponent buffTurretCondition = buffTurret.conditionComponent;

            List<DamageHistory> damageHistory = buffTurret.hpHistoryComponent.hpHistory;

            DamageHistory currentDamage;
            for(int i=0; i<damageHistory.size(); i++){

                currentDamage = damageHistory.get(i);

                // 데미지가 아니라 회복이라면 패스
                boolean isRecovery = (currentDamage.isDamage == false) ? true : false;
                if(isRecovery){
                    continue;
                }

                float finalDamage = 0f;

                /* 공격자를 찾는다 */
                Entity attacker = null;
                AttackComponent attackerAttack = null;
                ConditionComponent attackerCondition = null;

                if(!worldMap.entityMappingList.containsKey(currentDamage.unitID)){

                    continue;
                }
                int entityType = worldMap.entityMappingList.get(currentDamage.unitID);

                switch (entityType){
                    case EntityType.CharacterEntity :
                        attacker = worldMap.characterEntity.get(currentDamage.unitID);
                        attackerAttack = ((CharacterEntity)attacker).attackComponent;
                        attackerCondition = ((CharacterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.MonsterEntity :
                        attacker = worldMap.monsterEntity.get(currentDamage.unitID);
                        attackerAttack = ((MonsterEntity)attacker).attackComponent;
                        attackerCondition = ((MonsterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.AttackTurretEntity :
                        attacker = worldMap.attackTurretEntity.get(currentDamage.unitID);
                        attackerAttack = ((AttackTurretEntity)attacker).attackComponent;
                        attackerCondition = ((AttackTurretEntity)attacker).conditionComponent;
                        break;
                }


                /* 평타 구함 */
                float flatDamage =
                        calculateFlatDamage(currentDamage.amount, attackerAttack, attackerCondition, buffTurret.defenseComponent, buffTurretCondition);

                /* 치명타 적용함.. 혹은 데미지 타입별 특수처리가 필요한 경우 */
                switch(currentDamage.damageType){

                    case DamageType.FLAT_DAMAGE :

                        finalDamage = flatDamage;
                        break;

                    case DamageType.CRITICAL_DAMAGE :

                        float criticalDamage = calculateCriticalDamage(flatDamage, attackerAttack, attackerCondition);
                        finalDamage = criticalDamage;
                        break;
                }

                /**
                 * 2020 02 07 캐릭터랑 몬스터를 제외한 녀석들은 속성 없으므로
                 *  속성 처리 제외, 위에 finalDamage를 최종 데미지로 적용
                 */

                /* 속성보정 처리함 */
                //

                /** 기타 최종딜 추가 처리가 필요하다면 여기에 */

                /* damageHistory에 최종 데미지를 업데이트함 */
                currentDamage.amount = finalDamage;


            }


        }

    }

    public void updateBarricadeDamage(){

        for (HashMap.Entry<Integer, BarricadeEntity> barricadeEntity : worldMap.barricadeEntity.entrySet()) {

            BarricadeEntity barricade = barricadeEntity.getValue();
            ConditionComponent barricadeCondition = barricade.conditionComponent;

            List<DamageHistory> damageHistory = barricade.hpHistoryComponent.hpHistory;

            DamageHistory currentDamage;
            for(int i=0; i<damageHistory.size(); i++){

                currentDamage = damageHistory.get(i);

                // 데미지가 아니라 회복이라면 패스
                boolean isRecovery = (currentDamage.isDamage == false) ? true : false;
                if(isRecovery){
                    continue;
                }

                float finalDamage = 0f;

                /* 공격자를 찾는다 */
                Entity attacker = null;
                AttackComponent attackerAttack = null;
                ConditionComponent attackerCondition = null;

                if(!worldMap.entityMappingList.containsKey(currentDamage.unitID)){

                    continue;
                }
                int entityType = worldMap.entityMappingList.get(currentDamage.unitID);
                switch (entityType){
                    case EntityType.CharacterEntity :
                        attacker = worldMap.characterEntity.get(currentDamage.unitID);
                        attackerAttack = ((CharacterEntity)attacker).attackComponent;
                        attackerCondition = ((CharacterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.MonsterEntity :
                        attacker = worldMap.monsterEntity.get(currentDamage.unitID);
                        attackerAttack = ((MonsterEntity)attacker).attackComponent;
                        attackerCondition = ((MonsterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.AttackTurretEntity :
                        attacker = worldMap.attackTurretEntity.get(currentDamage.unitID);
                        attackerAttack = ((AttackTurretEntity)attacker).attackComponent;
                        attackerCondition = ((AttackTurretEntity)attacker).conditionComponent;
                        break;
                }


                /* 평타 구함 */
                float flatDamage =
                        calculateFlatDamage(currentDamage.amount, attackerAttack, attackerCondition, barricade.defenseComponent, barricadeCondition);

                /* 치명타 적용함.. 혹은 데미지 타입별 특수처리가 필요한 경우 */
                switch(currentDamage.damageType){

                    case DamageType.FLAT_DAMAGE :

                        finalDamage = flatDamage;
                        break;

                    case DamageType.CRITICAL_DAMAGE :

                        float criticalDamage = calculateCriticalDamage(flatDamage, attackerAttack, attackerCondition);
                        finalDamage = criticalDamage;
                        break;
                }

                /**
                 * 2020 02 07 캐릭터랑 몬스터를 제외한 녀석들은 속성 없으므로
                 *  속성 처리 제외, 위에 finalDamage를 최종 데미지로 적용
                 */

                /* 속성보정 처리함 */
                //

                /** 기타 최종딜 추가 처리가 필요하다면 여기에 */

                /* damageHistory에 최종 데미지를 업데이트함 */
                currentDamage.amount = finalDamage;


            }


        }

    }


    public void updateCrystalDamage(){

        for (HashMap.Entry<Integer, CrystalEntity> crystalEntity : worldMap.crystalEntity.entrySet()) {

            CrystalEntity crystal = crystalEntity.getValue();
            ConditionComponent crystalCondition = crystal.conditionComponent;

            List<DamageHistory> damageHistory = crystal.hpHistoryComponent.hpHistory;

            DamageHistory currentDamage;
            for(int i=0; i<damageHistory.size(); i++){

                currentDamage = damageHistory.get(i);

                // 데미지가 아니라 회복이라면 패스
                boolean isRecovery = (currentDamage.isDamage == false) ? true : false;
                if(isRecovery){
                    continue;
                }

                float finalDamage = 0f;

                /* 공격자를 찾는다 */
                Entity attacker = null;
                AttackComponent attackerAttack = null;
                ConditionComponent attackerCondition = null;

                if(!worldMap.entityMappingList.containsKey(currentDamage.unitID)){
                    continue;
                }
                int entityType = worldMap.entityMappingList.get(currentDamage.unitID);
                switch (entityType){
                    case EntityType.CharacterEntity :
                        attacker = worldMap.characterEntity.get(currentDamage.unitID);
                        attackerAttack = ((CharacterEntity)attacker).attackComponent;
                        attackerCondition = ((CharacterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.MonsterEntity :
                        attacker = worldMap.monsterEntity.get(currentDamage.unitID);
                        attackerAttack = ((MonsterEntity)attacker).attackComponent;
                        attackerCondition = ((MonsterEntity)attacker).conditionComponent;
                        break;
                    case EntityType.AttackTurretEntity :
                        attacker = worldMap.attackTurretEntity.get(currentDamage.unitID);
                        attackerAttack = ((AttackTurretEntity)attacker).attackComponent;
                        attackerCondition = ((AttackTurretEntity)attacker).conditionComponent;
                        break;
                }


                /* 평타 구함 */
                float flatDamage =
                        calculateFlatDamage(currentDamage.amount, attackerAttack, attackerCondition, crystal.defenseComponent, crystalCondition);

                /* 치명타 적용함.. 혹은 데미지 타입별 특수처리가 필요한 경우 */
                switch(currentDamage.damageType){

                    case DamageType.FLAT_DAMAGE :

                        finalDamage = flatDamage;
                        break;

                    case DamageType.CRITICAL_DAMAGE :

                        float criticalDamage = calculateCriticalDamage(flatDamage, attackerAttack, attackerCondition);
                        finalDamage = criticalDamage;
                        break;
                }

                /**
                 * 2020 02 07 캐릭터랑 몬스터를 제외한 녀석들은 속성 없으므로
                 *  속성 처리 제외, 위에 finalDamage를 최종 데미지로 적용
                 */

                /* 속성보정 처리함 */
                //

                /** 기타 최종딜 추가 처리가 필요하다면 여기에 */

                /* damageHistory에 최종 데미지를 업데이트함 */
                currentDamage.amount = finalDamage;


            }


        }

    }


    /*******************************************************************************************************************/

    /**
     * 추가
     * 오전 5:09 2020-04-07
     *
     * 밸런스가 적용된 평타 데미지를 계산한다
     * 처리 :
     *      -- 맥댐 : 기존 공격력
     *      -- 민댐 : 맥댐 / 2
     *      -- 대미지 평균(?) : 민댐 + (최대댐-최소댐) * 밸런스
     *      -- 최종 평타 대미지 : N( 대미지 평균, 28.9*28.9 ) 표준편차 : 28.9
     *
     *      위에서 구해진 최종(..) 평타 대미지에, 캐릭터 상태값에 따른 보너스 & 데미지 비율을 적용한다.
     *      ㄴ 이거 순서가 바뀔지도.. 보너스 및 비율을 적용한 맥댐을 대상으로 정규분포를 구하는 걸지도??
     *
     *
     * input : 넘어온 데미지값 - 일반 공격이라면 공격컴포넌트 사용하면 상관없는데, 스킬 등 특수 값의 경우를 위해 필요..
     *          공격자의 공격 컴포넌트 - 공격대미지, 상태 컴포넌트 - 보너스데미지, 데미지 비율 등등
     *          방어자의 방어 컴포넌트 - 방어값
     *
     * 공식 :
     *      공격력 * 100 / (100 + 방어력)
     *
     *      여기서 공격력은, 공격자의 기본 공격력에 밸런스 개념을 도입하여 도출한다.
     *      일단은 데미지시스템에 정의된 고정 수치만큼 ± 하여 만든 최소, 최대 값 중 하나를 선택하는 식으로 함.
     *
     * @return
     */
    public float calculateFlatDamage(
            float damage, AttackComponent attackerAttack, ConditionComponent attackerCondition,
            DefenseComponent targetDefense, ConditionComponent targetCondition){

        float STANDARD_DEVIATION = 28.9f;    // 표준편차... 확정되면 파일로 처리할 것.
        Random random = new Random();

        float finalFlatDamage = 0f;

        /** 밸런스 처리 작성 */
        // 공격자의 공격대미지 & 밸런스 수치에 맞추어 평타 대미지를 생성한다
        float maxDam = damage;
        float minDam = ( maxDam / 2f );

        float flatDamageExp = minDam + (maxDam - minDam) * (attackerAttack.balance * 0.01f) ;
        float flatDamage = flatDamageExp + (float)(random.nextGaussian() * STANDARD_DEVIATION);
        if(flatDamage < minDam){
            flatDamage = minDam;
        } else if(flatDamage > maxDam){
            flatDamage = maxDam;
        }

        //System.out.println("최대 데미지 : " + maxDam);
        //System.out.println("최소 데미지 : " + minDam);
        //System.out.println("기대 데미지 : " + flatDamageExp);
        //System.out.println("평뎀뽑 & 보정 : " + flatDamage);

        flatDamage = ( flatDamage + attackerCondition.attackDamageBonus ) * attackerCondition.attackDamageRate;

        //System.out.println("공격자 공격보너스 : " + attackerCondition.attackDamageBonus);
        //System.out.println("공격자 데미지비율 : " + attackerCondition.attackDamageRate);
        //System.out.println("상태적용 평뎀 : " + flatDamage);


        /** 공식에 따라 평타 데미지값을 도출 */
        float TARGET_DEFENSE = (targetDefense.defense + targetCondition.defenseBonus) * targetCondition.defenseRate;
        finalFlatDamage = (int)( flatDamage * 100 / (100 + TARGET_DEFENSE) );


        //System.out.println("타겟 방어력 : " + TARGET_DEFENSE);
        //System.out.println("최종 평댐 : " + finalFlatDamage);

        //System.out.println("");

        return finalFlatDamage;
    }

    /**
     * 평타 데미지를 계산한다
     * input : 넘어온 데미지값 - 일반 공격이라면 공격컴포넌트 사용하면 상관없는데, 스킬 등 특수 값의 경우를 위해 필요..
     *          공격자의 공격 컴포넌트 - 공격대미지, 상태 컴포넌트 - 보너스데미지, 데미지 비율 등등
     *          방어자의 방어 컴포넌트 - 방어값
     *
     * 공식 :
     *      공격력 * 100 / (100 + 방어력)
     *
     *      여기서 공격력은, 공격자의 기본 공격력에 밸런스 개념을 도입하여 도출한다.
     *      일단은 데미지시스템에 정의된 고정 수치만큼 ± 하여 만든 최소, 최대 값 중 하나를 선택하는 식으로 함.
     *
     * @return
     */
    public float calculateFlatDamageNotBalanced(
            float damage, AttackComponent attackerAttack, ConditionComponent attackerCondition, DefenseComponent targetDefense, ConditionComponent targetCondition){

        float finalFlatDamage = 0f;

        /** 밸런스 처리 작성 */
        // 공격자의 공격대미지 & 밸런스 수치에 맞추어 평타 대미지를 생성한다
        float complementValue = ( damage * BALNENCE_VALUE * 0.01f );
        float minDam = damage - complementValue;
        float maxDam = damage + complementValue;

        float flatDamage = (int)( (Math.random() * (maxDam - minDam) + 1) + minDam );


        flatDamage = ( flatDamage + attackerCondition.attackDamageBonus ) * attackerCondition.attackDamageRate;

        /** 공식에 따라 평타 데미지값을 도출 */
        float TARGET_DEFENSE = targetDefense.defense * targetCondition.defenseRate;
        finalFlatDamage = (int)( flatDamage * 100 / (100 + TARGET_DEFENSE) );

        return finalFlatDamage;
    }

    /**
     * 크리티컬 데미지를 계산한다
     * input : 데미지, 공격자의 공격 컴포넌트, 상태 컴포넌트
     *
     * 공식 : 대미지 * 치댐
     *          =N16*100/(100+AA13)*(100+N22)%*T25
     *          = 위에서 구한 flatDamage * (100 + 공격자 치명타 ; criticalDamage ) * 0.01f
     */
    public float calculateCriticalDamage(float flatDamage, AttackComponent attackerAttack, ConditionComponent attackerCondiiton){

        float criticalDamage = 0f;
        float finalCriticalDamage = 0f;

        /**
         * 1. flatDam에 공격자의 치명댐을 적용한 다음에
         * 2. 공격자의 치명댐 비율을 적용하는걸로?? 처리함 우선.
         * 뭐가 적절할지는...
         * 1. 치명댐에 치명댐 비율 적용한 값을 flatDam에 적용...
         * 아 그게 그건가??
         */

        criticalDamage = flatDamage * (100 + attackerAttack.criticalDamage) * 0.01f;
        finalCriticalDamage = (int) ( criticalDamage * attackerCondiiton.criticalDamageRate );

        //System.out.println("크리티컬 뎀 데미지 적용 : " + criticalDamage);
        //System.out.println("최종 크리뎀 : " + finalCriticalDamage);
        //System.out.println("");

        return finalCriticalDamage;
    }

    /**
     * 속성 보정 처리를 한다
     * input : 데미지, 공격자의 속성타입, 방어자의 속성타입
     * 공식 : 최종딜 * 속성보정
     */
    public float applyAttribute(float damage, int attackerAttrType, int targetAttrType){

        float attributedDamage = 0f;

        //System.out.println("공격자 속성 : " + attackerAttrType);
        //System.out.println("타겟 속성 : " + targetAttrType);

        int synastry = attrTablePerAttacker.get(attackerAttrType).get(targetAttrType);
        float synastryRate = ( 100f + damageRatePerSynastry.get(synastry) ) ;

        attributedDamage = damage * synastryRate * 0.01f;

        //System.out.println("속성간 상성 적용 비율 : " + synastryRate);
        //System.out.println("최종 속성뎀 : " + attributedDamage);

        //System.out.println("=============================");

        return attributedDamage;
    }



    /*******************************************************************************************************************/

    /**
     *
     */
    public void readAttributeInfo(){

        /** 상성별 데미지 비율*/
        damageRatePerSynastry.put(AttributeSynastryType.EQUAL, 0f);
        damageRatePerSynastry.put(AttributeSynastryType.WEAK_LITTLE, -10f);
        damageRatePerSynastry.put(AttributeSynastryType.STRONG_LITTLE, 10f);
        damageRatePerSynastry.put(AttributeSynastryType.WEAK, -30f);
        damageRatePerSynastry.put(AttributeSynastryType.STRONG, 30f);
        damageRatePerSynastry.put(AttributeSynastryType.WEAK_MUCH, -50f);
        damageRatePerSynastry.put(AttributeSynastryType.STRONG_MUCH, 50f);

        /***************************************************************************************************************/

        /** 속성별 상성 */

        HashMap<Integer, Integer> synastryTablePerTarget;

        /* 공격자 타입 RED */
        synastryTablePerTarget = new HashMap<>();
        synastryTablePerTarget.put(AttributeType.RED, AttributeSynastryType.EQUAL);
        synastryTablePerTarget.put(AttributeType.BLUE, AttributeSynastryType.WEAK_LITTLE);
        synastryTablePerTarget.put(AttributeType.GREEN, AttributeSynastryType.STRONG_LITTLE);
        synastryTablePerTarget.put(AttributeType.YELLOW, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.PINK, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.SKY_BLUE, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.WHITE, AttributeSynastryType.WEAK_MUCH);
        synastryTablePerTarget.put(AttributeType.BLACK, AttributeSynastryType.WEAK_MUCH);
        attrTablePerAttacker.put(AttributeType.RED, synastryTablePerTarget);

        /* 공격자 타입 BLUE */
        synastryTablePerTarget = new HashMap<>();
        synastryTablePerTarget.put(AttributeType.RED, AttributeSynastryType.STRONG_LITTLE);
        synastryTablePerTarget.put(AttributeType.BLUE, AttributeSynastryType.EQUAL);
        synastryTablePerTarget.put(AttributeType.GREEN, AttributeSynastryType.WEAK_LITTLE);
        synastryTablePerTarget.put(AttributeType.YELLOW, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.PINK, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.SKY_BLUE, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.WHITE, AttributeSynastryType.WEAK_MUCH);
        synastryTablePerTarget.put(AttributeType.BLACK, AttributeSynastryType.WEAK_MUCH);
        attrTablePerAttacker.put(AttributeType.BLUE, synastryTablePerTarget);

        /* 공격자 타입 GREEN */
        synastryTablePerTarget = new HashMap<>();
        synastryTablePerTarget.put(AttributeType.RED, AttributeSynastryType.WEAK_LITTLE);
        synastryTablePerTarget.put(AttributeType.BLUE, AttributeSynastryType.STRONG_LITTLE);
        synastryTablePerTarget.put(AttributeType.GREEN, AttributeSynastryType.EQUAL);
        synastryTablePerTarget.put(AttributeType.YELLOW, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.PINK, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.SKY_BLUE, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.WHITE, AttributeSynastryType.WEAK_MUCH);
        synastryTablePerTarget.put(AttributeType.BLACK, AttributeSynastryType.WEAK_MUCH);
        attrTablePerAttacker.put(AttributeType.GREEN, synastryTablePerTarget);

        /* 공격자 타입 YELLOW */
        synastryTablePerTarget = new HashMap<>();
        synastryTablePerTarget.put(AttributeType.RED, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.BLUE, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.GREEN, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.YELLOW, AttributeSynastryType.EQUAL);
        synastryTablePerTarget.put(AttributeType.PINK, AttributeSynastryType.WEAK_LITTLE);
        synastryTablePerTarget.put(AttributeType.SKY_BLUE, AttributeSynastryType.STRONG_LITTLE);
        synastryTablePerTarget.put(AttributeType.WHITE, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.BLACK, AttributeSynastryType.WEAK);
        attrTablePerAttacker.put(AttributeType.YELLOW, synastryTablePerTarget);

        /* 공격자 타입 PINK */
        synastryTablePerTarget = new HashMap<>();
        synastryTablePerTarget.put(AttributeType.RED, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.BLUE, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.GREEN, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.YELLOW, AttributeSynastryType.STRONG_LITTLE);
        synastryTablePerTarget.put(AttributeType.PINK, AttributeSynastryType.EQUAL);
        synastryTablePerTarget.put(AttributeType.SKY_BLUE, AttributeSynastryType.WEAK_LITTLE);
        synastryTablePerTarget.put(AttributeType.WHITE, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.BLACK, AttributeSynastryType.WEAK);
        attrTablePerAttacker.put(AttributeType.PINK, synastryTablePerTarget);


        /* 공격자 타입 SKU_BLUE */
        synastryTablePerTarget = new HashMap<>();
        synastryTablePerTarget.put(AttributeType.RED, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.BLUE, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.GREEN, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.YELLOW, AttributeSynastryType.WEAK_LITTLE);
        synastryTablePerTarget.put(AttributeType.PINK, AttributeSynastryType.STRONG_LITTLE);
        synastryTablePerTarget.put(AttributeType.SKY_BLUE, AttributeSynastryType.EQUAL);
        synastryTablePerTarget.put(AttributeType.WHITE, AttributeSynastryType.WEAK);
        synastryTablePerTarget.put(AttributeType.BLACK, AttributeSynastryType.WEAK);
        attrTablePerAttacker.put(AttributeType.SKY_BLUE, synastryTablePerTarget);


        /* 공격자 타입 WHITE */
        synastryTablePerTarget = new HashMap<>();
        synastryTablePerTarget.put(AttributeType.RED, AttributeSynastryType.STRONG_MUCH);
        synastryTablePerTarget.put(AttributeType.BLUE, AttributeSynastryType.STRONG_MUCH);
        synastryTablePerTarget.put(AttributeType.GREEN, AttributeSynastryType.STRONG_MUCH);
        synastryTablePerTarget.put(AttributeType.YELLOW, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.PINK, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.SKY_BLUE, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.WHITE, AttributeSynastryType.EQUAL);
        synastryTablePerTarget.put(AttributeType.BLACK, AttributeSynastryType.STRONG);
        attrTablePerAttacker.put(AttributeType.WHITE, synastryTablePerTarget);


        /* 공격자 타입 BLACK */
        synastryTablePerTarget = new HashMap<>();
        synastryTablePerTarget.put(AttributeType.RED, AttributeSynastryType.STRONG_MUCH);
        synastryTablePerTarget.put(AttributeType.BLUE, AttributeSynastryType.STRONG_MUCH);
        synastryTablePerTarget.put(AttributeType.GREEN, AttributeSynastryType.STRONG_MUCH);
        synastryTablePerTarget.put(AttributeType.YELLOW, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.PINK, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.SKY_BLUE, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.WHITE, AttributeSynastryType.STRONG);
        synastryTablePerTarget.put(AttributeType.BLACK, AttributeSynastryType.EQUAL);
        attrTablePerAttacker.put(AttributeType.BLACK, synastryTablePerTarget);

    }


    public float getAttributeRate(int attackerType, int targetType){

        switch (attackerType){

            case AttributeType.RED :
                switch (targetType){
                    case AttributeType.RED :
                        return 0f;
                    case  AttributeType.BLUE :
                        return -10f;
                    case  AttributeType.GREEN :
                        return 10f;
                    case  AttributeType.YELLOW :
                    case  AttributeType.PINK :
                    case  AttributeType.SKY_BLUE :
                        return -30f;
                    case  AttributeType.WHITE :
                    case  AttributeType.BLACK :
                        return -50f;
                }
                break;
            case AttributeType.BLUE :
                switch (targetType){
                    case AttributeType.RED :
                        return 10f;
                    case  AttributeType.BLUE :
                        return 0f;
                    case  AttributeType.GREEN :
                        return -10f;
                    case  AttributeType.YELLOW :
                    case  AttributeType.PINK :
                    case  AttributeType.SKY_BLUE :
                        return -30f;
                    case  AttributeType.WHITE :
                    case  AttributeType.BLACK :
                        return -50f;
                }
            case AttributeType.GREEN :
                switch (targetType){
                    case AttributeType.RED :
                        return -10f;
                    case  AttributeType.BLUE :
                        return 10f;
                    case  AttributeType.GREEN :
                        return 0f;
                    case  AttributeType.YELLOW :
                    case  AttributeType.PINK :
                    case  AttributeType.SKY_BLUE :
                        return -30f;
                    case  AttributeType.WHITE :
                    case  AttributeType.BLACK :
                        return -50f;
                }
            case AttributeType.YELLOW :
                switch (targetType){
                    case AttributeType.RED :
                    case  AttributeType.BLUE :
                    case  AttributeType.GREEN :
                        return 30f;
                    case  AttributeType.YELLOW :
                        return 0f;
                    case  AttributeType.PINK :
                        return -10f;
                    case  AttributeType.SKY_BLUE :
                        return 10f;
                    case  AttributeType.WHITE :
                    case  AttributeType.BLACK :
                        return -30f;
                }
            case AttributeType.PINK :
                switch (targetType){
                    case AttributeType.RED :
                    case  AttributeType.BLUE :
                    case  AttributeType.GREEN :
                        return 30f;
                    case  AttributeType.YELLOW :
                        return 10f;
                    case  AttributeType.PINK :
                        return 0f;
                    case  AttributeType.SKY_BLUE :
                        return -10f;
                    case  AttributeType.WHITE :
                    case  AttributeType.BLACK :
                        return -30f;
                }
            case AttributeType.SKY_BLUE :
                switch (targetType){
                    case AttributeType.RED :
                    case  AttributeType.BLUE :
                    case  AttributeType.GREEN :
                        return 30f;
                    case  AttributeType.YELLOW :
                        return -10f;
                    case  AttributeType.PINK :
                        return 10f;
                    case  AttributeType.SKY_BLUE :
                        return 0f;
                    case  AttributeType.WHITE :
                    case  AttributeType.BLACK :
                        return -30f;
                }
            case AttributeType.WHITE :
                switch (targetType){
                    case AttributeType.RED :
                    case  AttributeType.BLUE :
                    case  AttributeType.GREEN :
                        return 50f;
                    case  AttributeType.YELLOW :
                    case  AttributeType.PINK :
                    case  AttributeType.SKY_BLUE :
                        return 30f;
                    case  AttributeType.WHITE :
                        return 0f;
                    case  AttributeType.BLACK :
                        return 30f;
                }
            case AttributeType.BLACK :
                switch (targetType){
                    case AttributeType.RED :
                    case  AttributeType.BLUE :
                    case  AttributeType.GREEN :
                        return 50f;
                    case  AttributeType.YELLOW :
                    case  AttributeType.PINK :
                    case  AttributeType.SKY_BLUE :
                        return 30f;
                    case  AttributeType.WHITE :
                        return 30f;
                    case  AttributeType.BLACK :
                        return 0f;
                }

        }

        return 0f;
    }


    public int getUserAttrType(int userID){

        int attrType = worldMap.crystalEntity.get(userID).attribute;

        return attrType;
    }


    /*******************************************************************************************************************/
    /**
     * 2020 04 18 새벽 작성
     * 필요 데이터를 GDM으로부터 클론하여 사용하게끔, 초반에 미리 복사해두는 처리
     */

    /**
     * 기    능 : 데미지 시스템에서 필요로 하는 데이터를, GDM에서 복사해온다.
     * 처    리 :
     *      DamageSystem 에서 필요로 하는 GDM 데이터는 다음과 같다
     *      -- 상성효과값 목록
     *      -- 앨리멘탈 상성 정보 목록
     *
     */
    public void getNeedDataFromGDM(){

        /* 초기화 처리 */


        /* 상성효과값 목록을 복사한다 */
        bringSynastryEffectValueListFromGDM();

        /* 앨리멘탈 상성 정보 목록을 복사한다 */
        bringElementalSynastryInfoListFromGDM();

    }

    public void bringSynastryEffectValueListFromGDM(){

        HashMap<Integer, Float> synatsryEffectValueList = new HashMap<>();
        for( HashMap.Entry<Integer, Float> synastryEffect : GameDataManager.synastryEffectValueList.entrySet()){

            int effectKey = synastryEffect.getKey();
            float effectValue = synastryEffect.getValue();
            synatsryEffectValueList.put(effectKey, effectValue);

        }

    }

    public void bringElementalSynastryInfoListFromGDM(){

        HashMap<Integer, HashMap<Integer, Integer>> elementalSynastryInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, HashMap<Integer, Integer>> elementalSynastryInfo
                : GameDataManager.elementalSynastryInfoList.entrySet()) {

            int elementalKey = elementalSynastryInfo.getKey();

            HashMap<Integer, Integer> elementalValue = new HashMap<>();
            for (HashMap.Entry<Integer, Integer> synastryInfo : elementalSynastryInfo.getValue().entrySet()) {

                int synastryKey = synastryInfo.getKey();
                int synastryValue = synastryInfo.getValue();

                elementalValue.put(synastryKey, synastryValue);

            }

            elementalSynastryInfoList.put(elementalKey, elementalValue);

        }

    }



}




