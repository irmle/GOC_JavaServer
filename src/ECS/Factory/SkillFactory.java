package ECS.Factory;

import ECS.ActionQueue.ActionStopUsingSkill;
import ECS.ActionQueue.Skill.ActionGetSkill;
import ECS.ActionQueue.Skill.ActionUpgradeSkill;
import ECS.ActionQueue.Skill.ActionUseAttack;
import ECS.ActionQueue.Skill.ActionUseSkill;
import RMI.AutoCreatedClass.CharacterData;
import RMI.AutoCreatedClass.SkillInfoData;
import ECS.Classes.Type.*;
import ECS.Components.*;
import ECS.Classes.*;
import ECS.Entity.*;
import ECS.Game.*;
import RMI.RMI_Classes.*;
import RMI.RMI_Common._RMI_ParsingClasses.EntityType;
import RMI.RMI_Common.server_to_client;
import com.sun.beans.finder.BeanInfoFinder;
import io.netty.channel.AddressedEnvelope;
import sun.text.resources.no.CollationData_no;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 생성 : 2019년 11월 14일
 *
 * 업뎃날짜 : 2020 03 20 금요일 새벽 권령희
 *
 * 업뎃 내용 :
 *
 *      스킬사용, 스킬 중단 - 귀환 케이스 추가
 *      ㄴ 귀한 취소를 위한 스킬 중단 매서드의 경우, hp시스템 처리 도중에 호출될 수 있음.
 *
 *      귀한(recall) 매서드 ; 귀환자에게 5초간 지속되는 '귀환' 타입의 상태버프를 넣어줌.
 *                              ㄴ 5초간 방해받지 않고 상태가 지속될 시, 귀환 지점으로 이동하게 된다.
 *
 * ================================================================================================
 *
 * 직업별 스킬이 각각 6개씩 추가됨에 따라, 스킬 습득 가능 검사 조건이 추가되었음.
 * 전사 완료(디버깅은 아직)
 * 마법사 - 썬더 빼고 완료 (디버깅 아직)
 * 궁수 작성중..
 *
 * 궁수 폭풍의 시 같은 경우, 별도 요청 혹은 조건을 만족할 때 까지 스킬 처리를 계속 지속해야 한다
 *  ㄴ 중단 될 때의 처리가 별도로 필요, stopUsingSkill 매서드 만들었음
 *
 *
 *
 * <p>
 * <p>
 * 스킬(정확히는 SkillInfo) 객체 생성을 위한 클래스다.
 * 월드맵매니저 초기화 작업 시 생성해줘야 할듯?
 * <p>
 * 스킬정보를 담고있는 CSV파일을 읽어들여, SkillID와 SKillInfo 쌍으로 구성된 리스트를 갖는다.
 * <p>
 * 유저로부터 스킬습득 요청을 받으면, SkillID를 건네받아
 * 해당하는 스킬을 생성해 돌려준다.
 *
 *
 *
 *  [ 2019년 12월 05일 목요일 메모 ]
 *
 *  - 스킬 시스템 재설계 필요. '시전 시간' 개념이 들어가야 한다. 스킬 방동 시 효과 적용이랑 장판 등 오브젝트 및 애니메이션 등 동기화를 맞추기 위해서.
 *
 */
public class SkillFactory {

    /**
     * 멤버 변수
     */
    public static HashMap<Integer, SkillInfo> skillInfoTable;
    public static HashMap<Integer, HashMap<Integer, SkillInfoPerLevel>> skillLevelTable;  // 스킬의 각 레벨별 스탯값을 가지고 있다.

    public static void initFactory() {

        System.out.println("SkillFactory 초기화중...");

        skillInfoTable = new HashMap<>();
        skillLevelTable = new HashMap<>();

        /* 파일을 읽어, 위 테이블을 채우는 처리를 작성할 것 */

        /* 위 처리 로직이 완성되기 전까지는, 필요한 스킬을 하드코딩해서 테이블에 넣어줄 것 */
        readSkillInfoFromFile();

        System.out.println("SkillFactory 초기화 완료");
    }

    /**  매서드 */

    /**
     * [2019 11 14 목요일 오후 3시 ~ ]
     * SkillInfo 클래스의 clone() 함수 재정의 필요. 틀만 만들어둔 상태임
     * static으로 선언한 매서드는 static으로 선언된 외부 변수만을 참조할 수 있다??
     *
     * @param requestedSkillID
     * @return
     */
    public static SkillInfo createSkill(int requestedSkillID) {

        SkillInfo newSkill;

        /* 생성하는 처리 */
        // 스킬 테이블에서 해당하는 스킬을 찾은 후, 개를 클론함

        SkillInfo skillInfo = skillInfoTable.get(requestedSkillID);

        newSkill = skillInfo.clone();

        /* 레벨 테이블을 참고하여 값 넣어줌 ==>> 굳이.. 여기서 해 줄 필요는 없을듯 ? 그냥 하드코딩 생성자에서 똑같이 해주면 될거같다. 원인도 알았으니까. */
        newSkill.skillCoolTime = skillLevelTable.get(newSkill.skillType).get(1).coolTime;
        newSkill.reqMP = skillInfoTable.get(newSkill.skillType).reqMP;
        newSkill.skillRange = skillInfoTable.get(newSkill.skillType).skillRange;

        return newSkill;
    }

    /**
     * 안쓰는거
     * 클라이언트에서 ActionUseSkill 관련 패킷이 도달하였을 경우, 스킬이 사용된 월드맵 정보, 해당 슬롯의 스킬정보,
     * 스킬이 사용된 Action정보(스킬이 사용된 방향, 스킬이 사용된 거리정보)를 받아와서 해당 WorldMap의
     * Entity추가 요쳥 큐에 삽입하여,  다음 Tickrate때 스킬오브젝트나 투사체가 생성이 되도록 한다.
     */
    public static void useSkill(WorldMap targetWorldMap, ActionUseSkill event) {
        CharacterEntity userEntity = targetWorldMap.characterEntity.get(event.userEntityID);
        SkillSlot slot = userEntity.skillSlotComponent.skillSlotList.get(event.skillSlotNum);
        SkillInfo skillInfo = slot.skillinfo;

        //아직 스킬쿨타임이 남아있다면 사용할 수 없다.
        if (slot.remainCoolTime > 0f)
            return;

        //스킬을 사용하였으므로, 쿨타임 적용.
        slot.remainCoolTime = skillInfo.skillCoolTime;


        int skillLevel = slot.skillLevel;


        Vector3 direction = event.skillDirection;
        float distanceRate = event.skillDistanceRate;

        //존재하지 않을수도 있음.
        int targetEntityID = event.targetEntityID;


        FlyingObjectEntity newFlyingObjectEntity = new FlyingObjectEntity();
        SkillObjectEntity newSkillObjectEntity = new SkillObjectEntity();

        targetWorldMap.requestCreateQueue.add(newFlyingObjectEntity);
        targetWorldMap.requestCreateQueue.add(newSkillObjectEntity);
    }


    /**
     * 클라이언트에서 ActionUseAttack 관련 패킷이 도달하였을 경우, 스킬이 사용된 월드맵 정보,
     * 공격자의 EntityID, 피격자의 EntityID를 받아온 뒤, 각각의 EntityType을 가져온다.
     * <p>
     * 해당Type의 Entity목록에서, 해당하는 Entity객체를 불러온 뒤,  근거리/원거리 공격에 따라
     * 투사체를 생성하거나, DamageHistory에 데미지를 추가하거나 한다.
     * <p>
     * 투사체 생성시에는 Entity추가 요쳥 큐에 삽입하여,  다음 Tickrate때 투사체가 생성이 되도록 한다.
     * 데미지를 입힐시에는 TargetEntity객체의 DamageHistory에 접근하여 HP데미지를 추가하여,
     * 다음 Tickrate때 적용이 되도록 한다.
     */
    public static void doAttack(WorldMap targetWorldMap, ActionUseAttack event) {

        RMI_ID[] TARGET = RMI_ID.getArray(targetWorldMap.worldMapRMI_IDList.values());

        short attackerEntityType = -1;
        short targetEntityType = -1;

        if (!targetWorldMap.entityMappingList.containsKey(event.attackerEntityID)) {
            return;
        }
        else
            attackerEntityType = targetWorldMap.entityMappingList.get(event.attackerEntityID);
        if (!targetWorldMap.entityMappingList.containsKey(event.targetEntityID)) {
            return;
        }
        else{
            targetEntityType = targetWorldMap.entityMappingList.get(event.targetEntityID);
        }

        /**
         * 2020 01 30 목 권령희 추가
         *
         * 공격자의 상태 컴포넌트를 점검, 상태에 따른 처리를 한다
         */

        CharacterEntity attacker = targetWorldMap.characterEntity.get(event.attackerEntityID);
        ConditionComponent attackerCondition = attacker.conditionComponent;
        AttackComponent attackerAttack = attacker.attackComponent;

        System.out.println("doAttack 캐릭터 타입 : " + attacker.characterComponent.characterType);

        /* 캐릭터 타입(직업)별 평타 혹은 특수 처리 */
        switch (attacker.characterComponent.characterType) {

            case CharacterType.KNIGHT :
                break;
            case CharacterType.MAGICIAN :
                break;

            case CharacterType.ARCHER :

                /* 난사 스킬 효과가 지속되고 있는 경우 */
                if(attackerCondition.isArcherFireActivated){

                    System.out.println("궁수 난사 스킬이 적용된 공격" );

                    /* 난사스킬 활성화 시 평타 공격 처리 */
                    doAttack_onArcherFire(targetWorldMap, attacker);
                    return;
                }
                else if(attackerCondition.isArcherHeadShotActivated){

                    System.out.println("궁수 헤드샷 스킬 적용된 공격");

                    /* 헤드샷 스킬 사용 직후 평타 공격 처리 */
                    doAttack_onArcherHeadShot(targetWorldMap, attacker, event);
                    return;

                }

                break;

        }

        /**************************************************************************************************/

        System.out.println("dddddddddd");

        //공격모션 중계
        server_to_client.motionCharacterDoAttack(TARGET, RMI_Context.Reliable_Public_AES256,
                event.attackerEntityID, event.targetEntityID);

        //근접공격시 데미지 객체 (임시)
        DamageHistory damage = null;

        switch (attackerEntityType) {
            case EntityType.CharacterEntity: {

                //클라이언트가 공격했을때 Entity이므로 캐릭터 엔티티로 고정.
                CharacterEntity attackerEntity = targetWorldMap.characterEntity.get(event.attackerEntityID);

                //대상Entity의 종류가 몬스터이므로 몬스터의 위치를 지정한다.
                System.out.println(event.targetEntityID);
                MonsterEntity targetEntity = targetWorldMap.monsterEntity.get(event.targetEntityID);
                System.out.println(event.targetEntityID);
                if(targetEntity == null){
                    System.out.println("널이라네..왜지");
                }

                //아직 스킬쿨타임이 남아있다면 사용할 수 없다.
                if (attackerEntity.attackComponent.remainCoolTime > 0f)
                    return;

                //현재 공격불능 상태라면 사용할 수 없다.

                if (attackerEntity.conditionComponent.isDisableAttack == true)
                    return;

                //타겟이 Untargetable 상태라면 공격할 수 없다.
                if (targetEntity.conditionComponent.isUnTargetable == true)
                    return;

                //공격판정이 일어났으므로 쿨타임 초기화! (1sec 단위)
                attackerEntity.attackComponent.remainCoolTime = (1f / attackerEntity.attackComponent.attackSpeed);


                //나중에 추가할 예정.
                /*BuffAction attackDelay = new BuffAction();
                attackDelay.

                attackerEntity.buffActionHistoryComponent.conditionHistory.add(attackDelay);*/


                //추후, 캐릭터 Type을 통해서 원거리인지, 근거리인지 체크 할것.
                //targetEntity.characterComponent.characterType


                //근거리 전사 캐릭터라면 투사체 생성 필요없이 타겟의 데미지 히스토리에 바로 연산처리 요청할것.
                if (attackerEntity.characterComponent.characterType == CharacterType.KNIGHT) {

                    /** 2020 01 31 금 수정 */
                    if(false){  // 기존 버전

                        //원래 공격력 + 보너스 공격력
                        float sumDamage = attackerEntity.attackComponent.attackDamage + attackerEntity.conditionComponent.attackDamageBonus;
                        //위의 수치에 공격력 증가비율 적용
                        float rateDamage = sumDamage * attackerEntity.conditionComponent.attackDamageRate;
                        //위의 수치에서 타겟의 방어력 수치만큼 데미지 경감하여 최종 데미지를 구함.
                        float finalDamage = rateDamage - targetEntity.defenseComponent.defense;

                        //혹시나 방어력이 너무 높아서 finalDamage가 마이너스가 되는경우, 데미지 수치는 0으로 함.
                        if (finalDamage < 0)
                            finalDamage = 0;

                        //DamageHistory 생성.
                        damage = new DamageHistory(attackerEntity.entityID, true, finalDamage);

                        //다음 Tickrate시에 처리가 되도록 타겟의 데미지 히스토리에 반영.
                        targetEntity.hpHistoryComponent.hpHistory.add(damage);

                        //그리고 doAttack() 종료.
                        return;
                    }
                    else if(false){  /* new version. 크리티컬 개념 추가 */

                        /* 평타 vs 치명타 결정 */
                        int max = 99;
                        int min = 0;
                        int randomValue = (int)(Math.random()*((max-min)+1))+min;
                        float criticalChance = attackerCondition.criticalChanceRate + attackerCondition.criticalChanceRate + randomValue;

                        /* 평타, 치명타 여부에 따른 처리 */
                        boolean isCriticalAttack = (criticalChance >= 100f) ? true : false;
                        if(isCriticalAttack){

                            //원래 공격력 + 보너스 공격력
                            float sumDamage = attackerEntity.attackComponent.attackDamage + attackerEntity.conditionComponent.attackDamageBonus;
                            //위의 수치에 공격력 증가비율 적용
                            float rateDamage = sumDamage * attackerEntity.conditionComponent.attackDamageRate;

                            float criticalDamage
                                    = (attackerEntity.attackComponent.criticalDamage * 0.01f) + attackerCondition.criticalDamageRate;
                            // 컨디션 컴포넌트의 criticalDamageRate는 디폴트값이 1, 여기에 추가 적용된 수치들이 * 0.01f 연산된 값이 붙는다.
                            // 어택 컴포넌트의 크리티컬데미지는 레벨1 기준 50으로 시작한다.

                            float dmgAmount = rateDamage * criticalDamage;

                            BuffAction criticalDmg = new BuffAction();
                            criticalDmg.unitID = attacker.entityID;
                            criticalDmg.skillUserID = attacker.entityID;
                            criticalDmg.remainTime = 0.15f;
                            criticalDmg.coolTime = -1f;
                            criticalDmg.remainCoolTime = -1f;
                            criticalDmg.floatParam.add(new ConditionFloatParam(ConditionType.criticalDamageAmount, dmgAmount));

                            targetEntity.buffActionHistoryComponent.conditionHistory.add(criticalDmg);

                        } else {

                            //원래 공격력 + 보너스 공격력
                            float sumDamage = attackerEntity.attackComponent.attackDamage + attackerEntity.conditionComponent.attackDamageBonus;
                            //위의 수치에 공격력 증가비율 적용
                            float rateDamage = sumDamage * attackerEntity.conditionComponent.attackDamageRate;

                            BuffAction flatDmg = new BuffAction();
                            flatDmg.unitID = attacker.entityID;
                            flatDmg.skillUserID = attacker.entityID;
                            flatDmg.remainTime = 0.15f;
                            flatDmg.coolTime = -1f;
                            flatDmg.remainCoolTime = -1f;
                            flatDmg.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, rateDamage));

                            targetEntity.buffActionHistoryComponent.conditionHistory.add(flatDmg);

                        }

                        return;

                    }
                    else {  /* 2020 02 06 new version. 데미지 시스템 적용 */

                        float attackerDefaultDamage = attackerAttack.attackDamage;

                        // createDamageParam 매서드 내에서, 평타&치명타 판정이 일어난다.
                        ConditionFloatParam damageParam = createDamageParam(attackerDefaultDamage, attackerAttack, attackerCondition);

                        // 위에서 얻은 데미지 타입을 가지고, 데미지 버프를 생성한다.
                        BuffAction damageBuff = createDamageBuff(damageParam, attacker.entityID, attacker.entityID);

                        targetEntity.buffActionHistoryComponent.conditionHistory.add(damageBuff);

                        return;
                    }

                }   // 근거리 공격처리 끝, 끝나면 리턴하게 되어 아래 처리는 실행하지 않는다.

                /*******************************************************************************************************/


                //투사체를 생성하는 원거리 캐릭터의 경우.

                System.out.println("투사체를 생성함. ");

                //임시 값 생성.
                FlyingObjectEntity flyingObject = new FlyingObjectEntity();
                flyingObject.entityID = targetWorldMap.worldMapEntityIDGenerater.getAndIncrement();
                flyingObject.team = Team.BLUE;
                flyingObject.positionComponent = new PositionComponent();
                flyingObject.positionComponent.position = (Vector3) attackerEntity.positionComponent.position.clone();

                flyingObject.positionComponent.position.set(
                        flyingObject.positionComponent.position.x()+ 0.188f,
                        attackerEntity.positionComponent.position.y() + 1.5f,
                        flyingObject.positionComponent.position.z()+ 0.992f);


                flyingObject.flyingObjectComponent = new FlyingObjectComponent();

                switch (attackerEntity.characterComponent.characterType) {
                    case CharacterType.MAGICIAN: {
                        //flyingObject.flyingObjectComponent.createdSkillType = 1020005;
                        flyingObject.flyingObjectComponent.createdSkillType = SkillType.MAGICIAN_NORMAL_ATTACK;
                        break;
                    }
                    case CharacterType.ARCHER: {
                        //flyingObject.flyingObjectComponent.createdSkillType = 1030009;
                        flyingObject.flyingObjectComponent.createdSkillType = SkillType.ARCHER_NORMAL_ATTACK;
                        break;
                    }
                }

                flyingObject.flyingObjectComponent.flyingSpeed = 20f;
                flyingObject.flyingObjectComponent.flyingObjectRadius = 10f;

                flyingObject.flyingObjectComponent.userEntityID = flyingObject.entityID;
                flyingObject.flyingObjectComponent.targetEntityID = event.targetEntityID;

                flyingObject.flyingObjectComponent.startPosition = flyingObject.positionComponent.position;


                Vector3 attackerEntityPos = attackerEntity.positionComponent.position;
                Vector3 targetEntityPos = targetEntity.positionComponent.position;

                flyingObject.flyingObjectComponent.direction = Vector3.getTargetDirection(attackerEntityPos, targetEntityPos);



                // 아래 버프를 적용하기 위해 주석처리함.
                // 투사체를 생성하는 시점에, 평타/치명타 여부를 결정하도록 함.
                //flyingObject.flyingObjectComponent.buffAction = new BuffAction();
                //flyingObject.flyingObjectComponent.buffAction.unitID = attackerEntity.entityID;
                //flyingObject.flyingObjectComponent.buffAction.skillUserID = attackerEntity.entityID;
                //flyingObject.flyingObjectComponent.buffAction.floatParam = new ArrayList<>();
                //flyingObject.flyingObjectComponent.buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, attackerEntity.attackComponent.attackDamage));
                //flyingObject.flyingObjectComponent.buffAction.remainTime = 0.15f; //50ms남음



                if(false){  /** 2020 01 31, 평타 치명타 판정 및 처리 */

                    /* 평타 vs 치명타 결정 */
                    int max = 99;
                    int min = 0;
                    int randomValue = (int)(Math.random()*((max-min)+1))+min;
                    float criticalChance = attackerCondition.criticalChanceRate + randomValue;

                    /* 평타, 치명타 여부에 따른 처리 */
                    boolean isCriticalAttack = (criticalChance >= 100f) ? true : false;
                    if(isCriticalAttack){

                        //원래 공격력 + 보너스 공격력
                        float sumDamage = attackerEntity.attackComponent.attackDamage + attackerEntity.conditionComponent.attackDamageBonus;
                        //위의 수치에 공격력 증가비율 적용
                        float rateDamage = sumDamage * attackerEntity.conditionComponent.attackDamageRate;

                        float criticalDamage
                                = (attackerEntity.attackComponent.criticalDamage * 0.01f) + attackerCondition.criticalDamageRate;
                        // 컨디션 컴포넌트의 criticalDamageRate는 디폴트값이 1, 여기에 추가 적용된 수치들이 * 0.01f 연산된 값이 붙는다.
                        // 어택 컴포넌트의 크리티컬데미지는 레벨1 기준 50으로 시작한다.

                        float dmgAmount = rateDamage * criticalDamage;

                        BuffAction criticalDmg = new BuffAction();
                        criticalDmg.unitID = attacker.entityID;
                        criticalDmg.skillUserID = attacker.entityID;
                        criticalDmg.remainTime = 0.15f;
                        criticalDmg.coolTime = -1f;
                        criticalDmg.remainCoolTime = -1f;
                        criticalDmg.floatParam.add(new ConditionFloatParam(ConditionType.criticalDamageAmount, dmgAmount));

                        flyingObject.flyingObjectComponent.buffAction = criticalDmg;

                    } else {

                        //원래 공격력 + 보너스 공격력
                        float sumDamage = attackerEntity.attackComponent.attackDamage + attackerEntity.conditionComponent.attackDamageBonus;
                        //위의 수치에 공격력 증가비율 적용
                        float rateDamage = sumDamage * attackerEntity.conditionComponent.attackDamageRate;

                        BuffAction flatDmg = new BuffAction();
                        flatDmg.unitID = attacker.entityID;
                        flatDmg.skillUserID = attacker.entityID;
                        flatDmg.remainTime = 0.15f;
                        flatDmg.coolTime = -1f;
                        flatDmg.remainCoolTime = -1f;
                        flatDmg.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, rateDamage));

                        flyingObject.flyingObjectComponent.buffAction = flatDmg;

                    }

                }
                else{
                    /** 2002 02 06 ver, 데미지 시스템 적용*/

                    float attackerDefaultDamage = attackerAttack.attackDamage;

                    // createDamageParam 매서드 내에서, 평타&치명타 판정이 일어난다.
                    ConditionFloatParam damageParam = createDamageParam(attackerDefaultDamage, attackerAttack, attackerCondition);

                    // 위에서 얻은 데미지 타입을 가지고, 데미지 버프를 생성한다.
                    BuffAction damageBuff = createDamageBuff(damageParam, attacker.entityID, attacker.entityID);

                    flyingObject.flyingObjectComponent.buffAction = damageBuff;

                }

                /************************************************************************  */

                //doAttack은 타게팅 투사체이므로 이 값은 0으로 한다.
                //논 타게팅 투사체라면, 이 거리만큼 진행후 삭제처리 된다.
                flyingObject.flyingObjectComponent.flyingObjectRemainDistance = 0f;


                //Entity생성 Queue에 삽입.
                targetWorldMap.requestCreateQueue.add(flyingObject);
                break;
            }
            default:
                throw new IllegalArgumentException("SkillFactory doAttack() 중 비정상적인 attackerEntityType 값 : " + attackerEntityType);
        }

    } //doAttack 종료 부분.


    /**
     * 일단 안쓰는 함수. 지워버리고 싶네..
     * 클라이언트(플레이어 캐릭터)의 스킬 사용 요청을 처리한다.
     *
     */
    public static void processToUseSkill(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 시전자를 찾는다 */
        CharacterEntity skillUser =  worldMap.characterEntity.get(event.userEntityID);
        ConditionComponent userCondition = skillUser.conditionComponent;
        SkillSlot skillToUse = skillUser.skillSlotComponent.skillSlotList.get(event.skillSlotNum);
        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);

        /** 시전자가 스킬을 사용할 수 있는 상태인지 판별한다 */

        boolean isAbleToUseSkill = false;
        boolean hasEnoughMp = false;
        boolean isCoolTimeZero = false;

        hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        isAbleToUseSkill = ((!userCondition.isDisableSkill) && hasEnoughMp && isCoolTimeZero) ? true : false;  // 일단은 이거밖에 안떠오르네..

        if(isAbleToUseSkill){

            /** 스킬 타입을 판별한다 */

            boolean isSingleTarget = false;
            boolean isMultiTarget = false;
            boolean isNotYetTargeted = false;

            // 단일 아군 버프 대상이 있을수도 있지만.. 일단은 몬스터로 한정하긴 함.
            // 투사체가 있다고 하면, 그 투사체가 버프를 가질 것이니까. 여기서 투사체가 있냐없냐를 판단하면 안될 듯.
            isSingleTarget
                    = ((event.targetEntityID > -1)
                    && (worldMap.monsterEntity.containsKey(event.targetEntityID))) ? true : false;

            // 단일타겟이 지정된 상태가 아니며, 사용할 스킬정보가 버프를 가지고 있다면.
            isMultiTarget = ( (!isSingleTarget) && (skillToUse.skillinfo.buffAction != null)) ? true : false;

            // 얘도 좀 불명확한데.. 일단은, 싱글타겟도 아닌데 멀티타겟도 아니라면. 투사체 또는 오브젝트가 범위를 지정하는 애 인걸로.
            isNotYetTargeted = ( (!isSingleTarget) && (!isMultiTarget) ) ? true : false;


            /** 스킬 타입에 따라, 스킬 적용 대상을 판별한다 */

            ArrayList<Entity> targetList = new ArrayList<>();

            Entity target = null;
            short targetType = worldMap.entityMappingList.get(event.targetEntityID);

            if(isSingleTarget){ /* 단일 타게팅 스킬인 경우 */
                // 현재까지 나온 스킬목록 중 여기에 해당되는 스킬은 전사평타, 마법사평타, 마법사 파볼, 궁수 평타임.

                /* 일단은 캐릭터랑 몬스터 두 개만 넣음. */
                switch (targetType){
                    case EntityType.CharacterEntity :
                        target = worldMap.characterEntity.get(event.targetEntityID);
                        break;
                    case EntityType.MonsterEntity :
                        target = worldMap.monsterEntity.get(event.targetEntityID);
                        break;
                }
                targetList.add(target);

            }
            else if(isMultiTarget){  /* 멀티 타게팅 스킬인 경우 */
                // 현재까지 나온 스킬목록 중 여기에 해당되는 스킬은 전사 베기, 마법사 힐링임.
                // 멀티 타겟이 미리 정해져서 오는 경우가 아니더라도, 최소한 요 함수가 실행되는 시점에 스킬적용할 타겟을 결정할 수 있다면
                // 요 타입에 해당된다고 판단. 아래 notYetTargeted와의 차이점이다. 쟤는 스킬적용할 멀티 대상이 요 매서드가 아니라, 여기서 생성, 결정된
                // 투사체 또는 스킬 오브젝트가 각각 시스템을 도는 과정에서 대상이 정해진다. 현재 수준에서의 분류는 이러함...
                // 근데.. 그저께 생각하다가 흘려보낸게 있는데. 이런 멀티 타겟? 애들도. 그냥 여기서는 스킬 오브젝트(사실은 없지만)를 생성 해주는 부분까지만 하고,
                // 실제 범위판정 처리는 요 매서드가 아니라, 스킬 오브젝트 시스템, 플라잉 오브젝트 시스템이 하도록 하는 편이 더 깔끔할 수 있지 않을까 싶어서
                // 스.오 없는 멀티타겟 애들도 스오를 만들어주자!라고 생각했었던거 같음.
                // 요 매서드는 가능함 깔끔하게 유지하고 싶고.....
                // 일단은 여기서도 범위판정 하도록 해두고, 이거에 대해서 좀 이따 사람들이랑 얘기해보고? 내 나름대로 로직 검증도 하고. 그 후에
                // 바꿀지 말지 결정하자.

                /* 스킬의 사거리 등을 고려하여, 타겟을 결정한다. */




            }
            else if(isNotYetTargeted){  /* 멀티 논타겟 스킬인 경우?? */

                /* 할 게 있던가... 따로 없었던 듯 ?? */

            }



            //  일단. 투사체 생성은 생성이고, 아래 스킬이 가지고 있는 버프.. 대상에 버프 적용해주는거는.
            // ㄴ 아 싱글타겟 투사체 있는 경우랑, 없는경우 두 개를 분리하는게 문제ㅔㄴ..
            // 생성ㅈ요청에 넘겨주는 시점을 뒤로 빼면 문제가 안될것도 같긴한데.. 애매
            // 투사체 있으면.. 걔한테 무조건 버프디버프 넣어주고, 각 타겟에 대해 적용하는 것도
            // 투사체 생성 항목에서 하는걸로 치셈 찝찝한데 어쩔수없다
            // 밑에 스킬.. 부분도, 바로 타겟어쩌구로 들어가지 말고, 스킬 있어없어부터 따지고 들어가고.


            FlyingObjectEntity newFlyingObj = null;
            SkillObjectEntity newSkillObj = null;


            /** 스킬이 투사체를 가지고 있다면, 투사체를 생성한다 */
            boolean skillHasFlyingObject = false;
            skillHasFlyingObject = (skillToUse.skillinfo.flyingObjectInfo == null) ? false : true;
            if(skillHasFlyingObject){

                /* 스킬정보 및 유저 레벨에 따라 투사체 생성 */
                // FlyingObjectEntity newFlyingObj = null;

                PositionComponent positionComponent = null;
                FlyingObjectComponent flyingObjectComponent = null;

                Vector3 startPos = null;
                Vector3 direction = null;
                BuffAction buffAction = null;   // 투사체가 갖게될 버프액션

                ArrayList<ConditionBoolParam> boolParams;
                ArrayList<ConditionFloatParam> floatParams;

                Vector3 tempVector= null;

                int createdSkillType = skillToUse.skillinfo.skillType;
                int userEntityID = skillUser.entityID;
                float flyingSpeed = currentLevelSkillInfo.flyingObjectSpeed;
                float flyingObjectRadius = currentLevelSkillInfo.attackRange;

                // 일단은.. 스위치로 하드코딩.
                switch (skillToUse.skillinfo.skillType) {

                    /* 전사 찌르기 */
                    case SkillType.KNIGHT_PIERCE :

                        break;

                    /* 마법사 평타 */
                    case SkillType.MAGICIAN_NORMAL_ATTACK :

                        /* 투사체의 시작 지점 설정하기 ; 일단은 스킬시전자의 위치 그대로. 차후에 좀 변경해야할수도? */
                        startPos = new Vector3(skillUser.positionComponent.position.x(),
                                skillUser.positionComponent.position.y(), skillUser.positionComponent.position.z());

                        positionComponent = new PositionComponent(new Vector3(startPos.x(), startPos.y(), startPos.z()));

                        /* 투사체의 방향 구하기 */
                        tempVector = new Vector3();
                        tempVector = Vector3.normalizeVector(startPos, targetList.get(0).positionComponent.position);

                        direction = new Vector3(tempVector.x(), tempVector.y(), tempVector.z());

                        /* 투사체의 효과 (버프액션) */
                        buffAction = (BuffAction) skillInfoTable.get(skillToUse.skillinfo.skillType).flyingObjectInfo.buffAction.clone();
                        buffAction.unitID = skillUser.entityID;
                        buffAction.skillUserID = skillUser.entityID;
                        buffAction.floatParam = new ArrayList<>();
                        buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));

                        createdSkillType = skillToUse.skillinfo.skillType;
                        userEntityID = skillUser.entityID;
                        flyingSpeed = currentLevelSkillInfo.flyingObjectSpeed;
                        flyingObjectRadius = currentLevelSkillInfo.attackRange;

                        flyingObjectComponent = new FlyingObjectComponent(createdSkillType, userEntityID, flyingSpeed, flyingObjectRadius,
                                startPos, direction, -1, buffAction, -1);
                        flyingObjectComponent.targetEntityID = targetList.get(0).entityID;

                        newFlyingObj = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
                        newFlyingObj.entityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

                        worldMap.requestCreateQueue.add(newFlyingObj);

                        break;

                    /* 마법사 파볼 */
                    case SkillType.MAGICIAN_FIREBALL :

                        /* 투사체의 시작 지점 설정하기 ; 일단은 스킬시전자의 위치 그대로. 차후에 좀 변경해야할수도? */
                        startPos = new Vector3(skillUser.positionComponent.position.x(),
                                skillUser.positionComponent.position.y(), skillUser.positionComponent.position.z());

                        positionComponent = new PositionComponent(new Vector3(startPos.x(), startPos.y(), startPos.z()));

                        /* 투사체의 방향 구하기 */
                        tempVector = new Vector3();
                        tempVector = Vector3.normalizeVector(startPos, targetList.get(0).positionComponent.position);

                        direction = new Vector3(tempVector.x(), tempVector.y(), tempVector.z());

                        /* 투사체의 효과 (버프액션) */
                        buffAction = (BuffAction) skillInfoTable.get(skillToUse.skillinfo.skillType).flyingObjectInfo.buffAction.clone();
                        buffAction.unitID = skillUser.entityID;
                        buffAction.skillUserID = skillUser.entityID;
                        buffAction.floatParam = new ArrayList<>();
                        buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));

                        createdSkillType = skillToUse.skillinfo.skillType;
                        userEntityID = skillUser.entityID;
                        flyingSpeed = currentLevelSkillInfo.flyingObjectSpeed;
                        flyingObjectRadius = currentLevelSkillInfo.attackRange;

                        flyingObjectComponent = new FlyingObjectComponent(createdSkillType, userEntityID, flyingSpeed, flyingObjectRadius,
                                startPos, direction, -1, buffAction, -1);
                        flyingObjectComponent.targetEntityID = targetList.get(0).entityID;

                        newFlyingObj = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
                        newFlyingObj.entityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

                        worldMap.requestCreateQueue.add(newFlyingObj);

                        break;

                    /* 마법사 메테오 */
                    case SkillType.MAGICIAN_METEOR :

                        /* 투사체의 시작 지점 설정하기 ; 일단은 스킬시전자의 위치 그대로. 차후에 좀 변경해야할수도? */
                        startPos = new Vector3(skillUser.positionComponent.position.x(),
                                skillUser.positionComponent.position.y(), skillUser.positionComponent.position.z() + 100f);

                        positionComponent = new PositionComponent(new Vector3(startPos.x(), startPos.y(), startPos.z()));

                        /* 투사체의 방향 구하기 */
                        tempVector = new Vector3();
                        //tempVector = Vector3.normalizeVector(startPos, event.);

                        direction = new Vector3(tempVector.x(), tempVector.y(), tempVector.z());

                        /* 투사체의 효과 (버프액션) */
                        buffAction = (BuffAction) skillInfoTable.get(skillToUse.skillinfo.skillType).flyingObjectInfo.buffAction.clone();
                        buffAction.unitID = skillUser.entityID;
                        buffAction.skillUserID = skillUser.entityID;
                        buffAction.floatParam = new ArrayList<>();
                        buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));

                        createdSkillType = skillToUse.skillinfo.skillType;
                        userEntityID = skillUser.entityID;
                        flyingSpeed = currentLevelSkillInfo.flyingObjectSpeed;
                        flyingObjectRadius = currentLevelSkillInfo.attackRange;

                        float remainDistance = event.skillDistanceRate * skillInfo.skillRange;

                        flyingObjectComponent = new FlyingObjectComponent(createdSkillType, userEntityID, flyingSpeed, flyingObjectRadius,
                                startPos, direction, remainDistance, buffAction, -1);
                        flyingObjectComponent.targetEntityID = targetList.get(0).entityID;

                        newFlyingObj = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
                        newFlyingObj.entityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

                        /* 투사체 자체에 넣어줄 버프 */
                        BuffAction flyingBuff;
                        // 이동 시작 제어 버프
                        flyingBuff = new BuffAction(newFlyingObj.entityID, userEntityID, 1f, -1, -1, new ArrayList<>(), new ArrayList<>());
                        flyingBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));


                        worldMap.requestCreateQueue.add(newFlyingObj);


                        break;

                    /* 궁수 평타 */
                    case SkillType.ARCHER_NORMAL_ATTACK :

                        break;

                    /* 궁수 파워샷 */
                    case SkillType.ARCHER_POWER_SHOT :

                        break;

                    /* 궁수 멀티샷 */
                    case  SkillType.ARCHER_MULTI_SHOT:

                        break;

                    /* 궁수 화살비 */
                    case SkillType.ARCHER_ARROW_RAIN :

                        break;

                    default:

                        break;


                }

                /* 투사체의 타겟이 지정돼 있는 경우 */
                if(isSingleTarget){
                    // 궁수 평타, 마법사 평타, 마법사 파볼..

                    /*flyingObjectComponent.targetEntityID = targetList.get(0).entityID;*/


                } else if(isMultiTarget){


                }
            }


            /** 스킬이 스킬 오브젝트를 가지고 있다면, 스킬 오브젝트를 생성한다 */
            boolean skillHasSkillObject = false;
            skillHasSkillObject = (skillToUse.skillinfo.skillObjectInfo == null) ? false : true;
            if(skillHasSkillObject){

                /* 스킬정보 및 유저 레벨에 따라 스킬 오브젝트 생성 */
                //

                PositionComponent positionComponent = null;
                SkillObjectComponent skillObjectComponent = null;

                SkillObjectInfo skillObjectInfo = skillInfoTable.get(skillInfo.skillType).skillObjectInfo;

                // 스킬 오브젝트에 들어가는 목록 // 당장 계산 안해도 될 것만.
                int createdSkillType = skillInfo.skillType;
                int skillAreaType = skillObjectInfo.skillAreaType;
                int userEntityID = skillUser.entityID;

                long tempDistance;  // 계산용.
                float skillObjectDurationTime;
                float skillObjectRadius = skillObjectInfo.skillObjectRadius;
                float skillObjectAngle;
                float skillObjectDistance = event.skillDistanceRate * skillInfo.skillRange;

                Vector3 startPos;
                Vector3 directionPos;

                float distanceRate;
                BuffAction buffAction;  //스킬 오브젝트 적중시 부여될 버프 효과 정보.

                // 일단은.. 스위치로 하드코딩.
                switch (skillToUse.skillinfo.skillType) {

                    /* 전사 회오리*/
                    case SkillType.KNIGHT_TORNADO:


                        break;

                    /* 마법사 메테오 */
                    case SkillType.MAGICIAN_METEOR :

                        /* 스킬 오브젝트 컴포넌트에 들어갈 값들 구하기 */

                        // 스킬 오브젝트 위치
                        Vector3 userPos = skillUser.positionComponent.position;
                        Vector3 skillDirection = event.skillDirection;
                        float skillDistanceRate = event.skillDistanceRate;

                        float moveDistance = skillInfo.skillRange * skillDistanceRate;

                        Vector3 moveVector = new Vector3(skillDirection.x(), skillDirection.y(), skillDirection.z());
                        moveVector.set(moveVector.x() * moveDistance,
                                moveVector.y() * moveDistance,
                                moveVector.z() * moveDistance );

                        Vector3 resultPos = new Vector3(userPos.x(), userPos.y(), userPos.z());
                        resultPos.movePosition(resultPos, moveVector );


                        // 지속시간도 계산해주고..
                        float flyingObjectestryoTime
                                = newFlyingObj.flyingObjectComponent.flyingObjectRemainDistance / newFlyingObj.flyingObjectComponent.flyingSpeed;


                        // 버프액션 ; 메테오가 대상자들에게 줄 버프 목록들

                        buffAction = (BuffAction) skillObjectInfo.buffAction.clone();
                        buffAction.unitID = newFlyingObj.entityID;
                        buffAction.skillUserID = skillUser.entityID;
                        buffAction.remainTime = currentLevelSkillInfo.durationTime + currentLevelSkillInfo.coolTime;
                        buffAction.coolTime = currentLevelSkillInfo.coolTime;
                        buffAction.remainCoolTime = currentLevelSkillInfo.coolTime;
                        buffAction.floatParam = new ArrayList<>();
                        buffAction.floatParam.add(new ConditionFloatParam(ConditionType.hpRecoveryAmount, currentLevelSkillInfo.attackDamage));


                        /* 스킬 오브젝트에 들어갈 컴포넌트들 생성하기 */

                        positionComponent = new PositionComponent();    // 좌표 계산 해줘야함
                        positionComponent.position.set(resultPos);

                        skillObjectComponent
                                = new SkillObjectComponent(createdSkillType, skillAreaType, userEntityID,
                                10f, currentLevelSkillInfo.attackRange, -1f,
                                0f, resultPos, event.skillDirection, event.skillDistanceRate, null);


                        /* 스킬 오브젝트 생성하기 */
                        newSkillObj = new SkillObjectEntity(positionComponent, skillObjectComponent);
                        newSkillObj.entityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

                        /* 월드에 스킬 오브젝트 객체 생성 요청 */
                        worldMap.requestCreateQueue.add(newSkillObj);

                        break;


                    /* 궁수 화살비 */
                    case SkillType.ARCHER_ARROW_RAIN :

                        break;

                    default:

                        break;


                }












            }


            /** 스킬이 버프를 가지고 있다면, 위에서 정해진 타겟들에 대해 그 효과를 적용한다  */
            boolean skillHasSkillBuff = false;
            skillHasSkillBuff = (skillToUse.skillinfo.buffAction == null) ? false : true;
            if(skillHasSkillBuff){

                if(isSingleTarget){
                    // 전사 평타

                } else if(isMultiTarget){
                    // 전사 베기, 마법사 힐

                }
            }

            /** 스킬 쿨타임 및 시전자쪽에 필요한 처리 */    // 뭔가 명확하게 정리가 잘 안되네


            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 */
            // 아.. 공통적인거라고 해도, 스킬 시전자들이 시전 후 받는 제한이라던가 그런것도 따로 들어있으면 좋을거같은데.
            // 단순히버프 이런게 아니라. 스킬처리 후 사용자들한테 처리해줄 전용 변수...
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.skillUserID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = ( worldMap.tickRate * 1.5f );
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();

            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);
            // 이렇게 직접적으로 넣어주는거 말고도, 막 버프주기 이런 함수 만들어서 처리하게 하면 비효율적이려나??



            // 중계가 필요하다면 중계처리를 하고.





        }
        else {  // 스킬을 사용할 수 있는 상태가 아니다... 뭔가 별도로 처리해줄 게 있나?? 아니면 걍 무시하면 되나.

        }





    }


    /**
     * 유저가 스킬 시전 이벤트를 발생시켰을 때, 이벤트 정보를 확인하여 해당 스킬 처리 매서드를 호출해 처리한다.
     * @param worldMap
     * @param event
     */
    public static void useSkill2(WorldMap worldMap, ActionUseSkill event){

        System.out.println("클라이언트가 보낸 슬롯 넘버 : " + event.skillSlotNum);

        /* 스킬 시전자 및 시전 스킬 정보 */
        CharacterEntity skillUser =  worldMap.characterEntity.get(event.userEntityID);
        ConditionComponent userCondition = skillUser.conditionComponent;

        SkillSlot skillToUse = null;
        SkillSlot slot;

        /** 2020 03 20 수정 */
        /**
         * 귀환 기능이 스킬로서 추가됨. 우선 슬롯번호, 스킬타입 == 100으로 처리하기로 함.
         * 이 경우, 귀환 스킬을 딱히 습득해서 배우거나 하는게 아니기 때문에
         *  (나중에 뭐.. 겜 시작과 동시에 캐릭터 생성할 때, 귀환 전용 슬롯을 넣어줘도 되기는 하지만)
         *  스킬사용 요청 이벤트에 포함된 슬롯 번호가 100인 경우, 별도로 슬롯검색 등을 하지 않는걸로 함.
         */

        int slotNum = event.skillSlotNum;
        int skillType = -1;

        if(slotNum == 100){

            skillType = SkillType.RECALL;
        }
        else{

            for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){
                slot = skillUser.skillSlotComponent.skillSlotList.get(i);

                System.out.println("슬롯 사이즈 : " + skillUser.skillSlotComponent.skillSlotList.size());

                System.out.println("슬롯 번호 : " + slot.slotNum);

                if(slot.slotNum == event.skillSlotNum){
                    skillType = slot.skillinfo.skillType;
                    skillToUse = slot;
                    break;
                }
            }

        }

        SkillInfo skillInfo;
        SkillInfoPerLevel currentLevelSkillInfo;
        if(skillToUse != null){ /* 스킬 슬롯이 검색된 경우, 해당 슬롯의 정보를 찾을 것이다.   */
            skillInfo = skillToUse.skillinfo;
            currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);
        }
        /* ㄴ 보니까.. 이 매서드 내에선 쓸 데 없는데... 왜 했지?? */

        System.out.println("스킬 타입 : " + skillType );

        switch (skillType){

            /** 궁수 */
            case SkillType.ARCHER_NORMAL_ATTACK :

                System.out.println("궁수 평타 사용");
                break;
            case SkillType.ARCHER_POWER_SHOT :

                useSkill_archerPowerShot(worldMap, event);
                System.out.println("파워 샷 사용");
                break;

            case SkillType.ARCHER_MULTI_SHOT :

                useSkill_archerMultiShot2(worldMap, event);
                System.out.println("멀티 샷 사용");
                break;

            case SkillType.ARCHER_ARROW_RAIN :

                useSkill_archerArrowRain(worldMap, event);
                System.out.println("화살비 사용");
                break;

            case SkillType.ARCHER_INC_ATTACK_SPEED :

                useSkill_archerIncAttackSpeed(worldMap, event);
                System.out.println("속공 사용");
                break;

            case SkillType.ARCHER_HEAD_SHOT :

                useSkill_archerHeadShot(worldMap, event);
                System.out.println("헤드 샷 사용");
                break;

            case SkillType.ARCHER_CRITICAL_HIT :

                useSkill_archerCriticalHit(worldMap, event);
                System.out.println("치명타 사용");
                break;

            case SkillType.ARCHER_STORM :

                useSkill_archerStorm(worldMap, event);
                System.out.println("폭풍의 시 사용");
                break;

            case SkillType.ARCHER_FIRE :

                useSkill_archerFire(worldMap, event);
                System.out.println("난사 사용");
                break;

            case SkillType.ARCHER_SNIPE :

                useSkill_archerSnipe(worldMap, event);
                System.out.println("저격 사용");
                break;


            /** 마법사 */
            case SkillType.MAGICIAN_NORMAL_ATTACK :

                System.out.println("마법 평타 사용");
                break;

            case SkillType.MAGICIAN_FIREBALL :

                useSkill_magicianFireball(worldMap, event);
                System.out.println("파이어볼 사용");
                break;

            case SkillType.MAGICIAN_HEAL :

                useSkill_magicianHeal(worldMap, event);
                System.out.println("힐링 사용");
                break;

            case SkillType.MAGICIAN_METEOR :

                useSkill_magicianMeteor(worldMap, event);
                System.out.println("메테오 사용");
                break;

            case SkillType.MAGICIAN_LIGHTNING_ROAD :

                useSkill_magicianLightningRoad(worldMap, event);
                System.out.println("라이트닝 로드 사용");
                break;

            case SkillType.MAGICIAN_ICEBALL :

                useSkill_magicianIceBall(worldMap, event);
                System.out.println("아이스볼 사용");
                break;

            case SkillType.MAGICIAN_SHIELD :

                useSkill_magicianShield(worldMap, event);
                System.out.println("쉴드 사용");
                break;

            case SkillType.MAGICIAN_ICE_FIELD :

                useSkill_magicianIceField(worldMap, event);
                System.out.println("아이스 필드 사용");
                break;

            case SkillType.MAGICIAN_THUNDER :

                useSkill_magicianThunder(worldMap, event);
                System.out.println("썬더 사용");
                break;

            case SkillType.MAGICIAN_FROZEN_BEAM :

                useSkill_magicianFrozenBeam(worldMap, event);
                System.out.println("프로즌빔 사용");
                break;


            /** 전사 */
            case SkillType.KNIGHT_NORMAL_ATTACK :

                System.out.println("전사 평타 사용");
                break;

            case SkillType.KNIGHT_CUT :

                useSKill_knightCut(worldMap, event);
                System.out.println("베기 사용");
                break;

            case SkillType.KNIGHT_PIERCE :

                useSKill_knightPierce(worldMap, event);
                System.out.println("찌르기 사용");
                break;

            case SkillType.KNIGHT_TORNADO :

                useSKill_knightTornado(worldMap, event);
                System.out.println("회오리 사용");
                break;

            case SkillType.KNIGHT_GARREN_Q :

                useSkill_knightGarrenQ(worldMap, event);
                System.out.println("가렌 Q");
                break;

            case SkillType.KNIGHT_GARREN_E :

                useSkill_knightGarrenE(worldMap, event);
                System.out.println("가렌 E");
                break;

            case SkillType.KNIGHT_GARREN_R :

                useSkill_knightGarrenR(worldMap, event);
                System.out.println("가렌 R");
                break;

            case SkillType.KNIGHT_BERSERKER :

                useSkill_knightBerserker(worldMap, event);
                System.out.println("버서커 사용");
                break;

            case SkillType.KNIGHT_INCR_HP :

                useSkill_knightIncrHp(worldMap, event);
                System.out.println("체력 증가 사용");
                break;

            case SkillType.KNIGHT_INVINCIBLE :

                useSkill_knightInvincible(worldMap, event);
                System.out.println("무적 사용");
                break;


            /** 2020 03 20 추가 */
            case SkillType.RECALL :

                recall(worldMap, event);
                System.out.println("귀환 시전");
                break;



        }

    }


    /**
     * [2019 11 25 월요일]
     * 작 성 자 : 권령희
     * 기    능 : 플레이어 캐릭터들이 사용하는 스킬 정보들을 하드코딩해둔 함수.
     *          이후 유저들이 스킬 관련 인터랙션을 수행할 때, 여기서 만들어진 정보를 참조한다.
     *          차후 실제 파일로부터 읽어오도록 변경할 것.
     *
     * [ 2020 01 23 목요일 업뎃 ]
     * 전사, 마법사, 궁수 각각 6개씩 새로운 스킬 추가됨.
     *
     * [ 2020 02 06 목요일 메모]
     *  신규 스킬들처럼, 스킬 종류별로 read 함수를 별도로 만들 것.
     *  그리고 그 매서드들을 현 매서드 내에서 호출하는 형태로.
     *  ㄴ 신규 스킬들의 경우, 매서드 제일 아래에서 각 매서드들의 정보를 세팅하는 매서드를 호출하고 있음
     */
    public static void readSkillInfoFromFile(){

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;
        SkillObjectInfo newSkillObjInfo;
        FlyingObjectInfo newFlyingObjInfo;
        BuffAction newBuff;
        BuffAction newSkillObjBuff;
        BuffAction newFlyingObjBuff;

        ArrayList<ConditionBoolParam> boolParams;
        ArrayList<ConditionFloatParam> floatParams;

        /* ============================================================================================================== */

        /** 전사 1단계 스킬 ; 베기 ; 스킬번호_2 */

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, 100f, 5f, 5f, -1, -1, 5));
        levelInfo.put(2, new SkillInfoPerLevel(2, 150f, 5f, 5f, -1, -1, 5));
        levelInfo.put(3, new SkillInfoPerLevel(3, 200f, 5f, 5f, -1, -1, 5));
        levelInfo.put(4, new SkillInfoPerLevel(4, 250f, 5f, 5f, -1, -1, 5));
        levelInfo.put(5, new SkillInfoPerLevel(5, 300f, 5f, 5f, -1, -1, 5));
        skillLevelTable.put(SkillType.KNIGHT_CUT, levelInfo);

        /* Skill Buff */
        boolParams = new ArrayList<>();
        /*boolParams.add(new ConditionBoolParam((ConditionType.isDisableAttack), true));
        boolParams.add(new ConditionBoolParam((ConditionType.isDisableMove), true));
        boolParams.add(new ConditionBoolParam((ConditionType.isUnTargetable), true));*/
        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));  // 데미지값.. 해당 스킬의 레벨1에 해당하는 데미지 값을 줄까 하다가, 그냥 -1로 넣음. 왜 버포 자체에 대한 정보에 저런 실제 값이 들어가야 하는지??
        newBuff = new BuffAction(-1, -1, 1f, 0.25f, 1f, boolParams, floatParams);

        /* Skill Object Info */
        newSkillObjBuff = null;
        newSkillObjInfo = new SkillObjectInfo(SkillAreaType.CONE,
                -1f, -1f, -1f, -1f,
                null);

        /* Flying Object Info */
        newFlyingObjBuff = null;
        newFlyingObjInfo = null;

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.KNIGHT_CUT, "베기",
                5, 0, 10, 5f,
                newBuff, null, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

        /* ============================================================================================================== */

        /** 전사 2단계 스킬 ; 찌르기 ; 스킬번호_3 */
        /**
         * 몹이 타격을 받은 뒤, 뒤로 나자빠져야 하는데. 이걸 어케 구현할지가 고민.
         * 먼가.. 스턴?이라는 상태가 여하튼 있어서, 걔가 해제되는 시점에 이런 나자빠짐 처리를 넣어주는 방식밖에는 생각이 안나네 지금은.
         *
         */

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, 100f, 10f, 1f, -1, -1, 5f));
        levelInfo.put(2, new SkillInfoPerLevel(2, 150f, 11f, 1.1f, -1, -1, 5f));
        levelInfo.put(3, new SkillInfoPerLevel(3, 200f, 12f, 1.2f, -1, -1, 5f));
        levelInfo.put(4, new SkillInfoPerLevel(4, 250f, 13f, 1.3f, -1, -1, 5f));
        levelInfo.put(5, new SkillInfoPerLevel(5, 300f, 14f, 1.4f, -1, -1, 5f));
        skillLevelTable.put(SkillType.KNIGHT_PIERCE, levelInfo);

        /* Skill Buff */

        /**
         * 스킬 시전자의 이동 속도를 5배 빠르게 함. ==>> 스킬의 버프!로 넣어줘도 괜찮은지.. 이것도좀 체계 정리가 필요할거같은데
         */
        /*boolParams = new ArrayList<>();
        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.moveSpeedRate, 500f));
        newBuff = new BuffAction(-1, 1.5f, -1f, -1, boolParams, floatParams);*/

        /* Flying Object Info */
        boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam((ConditionType.isDisableAttack), true));
        boolParams.add(new ConditionBoolParam((ConditionType.isDisableMove), true));
        //boolParams.add(new ConditionBoolParam((ConditionType.isUnTargetable), true));
        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));   /* 데미지 양 : 레벨테이블 참조. */

        /**
         * 투사체 자체에 쿨타임 개념이 들어있진 않으므로. -1.
         */
        newFlyingObjBuff = new BuffAction(-1, -1, 1.5f, 1f, 1f, boolParams, floatParams);

        /**
         * 남은거리 : 스킬 사거리(500)
         * 속도 : 유저 속도(100단위) * 5
         * 시간 : ( 남은 거리 / 속도 ) 만큼.
         */
        newFlyingObjInfo = new FlyingObjectInfo(-1f, 1f * 2, -1f, newFlyingObjBuff);


        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.KNIGHT_PIERCE, "찌르기",
                5, 0, 50, 10f,
                null, null, newFlyingObjInfo);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

        /* ============================================================================================================== */

        /** 전사 3단계 스킬 ; 회오리 ; 스킬번호_4 */
        /**
         * 캐릭터가 스킬을 사용하면
         *      스킬 사용 가능 여부를 판단한다
         *      if 사용 가능하다면
         *
         *          스킬 오브젝트를 생성한다.
         *          스킬 오브젝트 시스템에서 할 일이긴 한데,
         *              타겟 목록을 판별한다
         *              if( 에어본 면역자, 죽은자, 타겟 불가능자 등등 이라면)
         *                  제외한다!
         *
         *              타겟들에 대해, 스킬 오브젝트가 가지고 있는 버프 효과를 추가한다.
         *                  0. 스킬 오브젝트의 높이.. 위로?? 대상들의 높이를 끌어올린다
         *                  1. 에어본 상태 추가
         *                  2. 지속 데미지 버프 추가
         *
         *             스킬 오브젝트가 지속시간이 다 되어 삭제될 때,
         *                  각종 버프는 알아서 사라질테고..
         *                  타겟들의 높이를 스오 아래로(본래 위치, 바닥으로) 끌어내린다.
         *
         *
         *
         *
         *
         */

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, 100f * 0.2f, 7f, 7f, -1, 6f, 5f));
        levelInfo.put(2, new SkillInfoPerLevel(2, 150f, 8f, 8f, -1, 6f, 5f));
        levelInfo.put(3, new SkillInfoPerLevel(3, 200f, 9f, 9f, -1, 6f, 5f));
        levelInfo.put(4, new SkillInfoPerLevel(4, 250f, 10f, 10f, -1, 6f, 5f));
        levelInfo.put(5, new SkillInfoPerLevel(5, 300f, 11f, 11f, -1, 6f, 5f));
        skillLevelTable.put(SkillType.KNIGHT_TORNADO, levelInfo);


        /* Skill Object Info */
        boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam(ConditionType.isAirborne, true));
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
        //boolParams.add(new ConditionBoolParam((ConditionType.isUnTargetable), true));
        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));

        newSkillObjBuff = new BuffAction(-1, -1, 7f, 0.5f, 2f, boolParams, floatParams);
        newSkillObjInfo = new SkillObjectInfo(SkillAreaType.CIRCLE,
                7f, 7f, -1f, -1f,
                newSkillObjBuff);


        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.KNIGHT_TORNADO, "회오리",
                5, 0, 100, 7f,
                null, newSkillObjInfo, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

        /* ============================================================================================================== */


        /********************************************* 마법사 스킬 *****************************************************/

        /************ 마법사 스킬 0 : 평타 ; 스킬번호_5 ***********/
        /**
         * 단일, 투사체 있음 ; 구, 타게팅,
         *
         * 마법사가 평타를 시전한다
         * 시전 조건을 만족한다면, 시전함.
         * 단일 타게팅 ==>> 시전 시점에 이미 타겟이 정해져있다. ==>> 타겟(들)을 판별할 필요가 없다.
         * 단일 타겟이 정해져있는 경우, ...
         * 일단 투사체를 생성한다 (시전)
         *      투사체 시스템에서 처리하겠지.
         *          타겟을 정한다 // 이미 정해져 있다
         *          정해진 타겟에 대해, 이동한다
         *          이동 후, 공격범위 내에 들어왔는지 판단한다
         *          공격 범위 혹은 충돌이라면, 투사체가 가지고 있는 버프(...)를 입힌다.
         *          공격처리하고 사라진다.
         *
         */

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, -1f, 10f, 10f, 7f, -1, -1));
        skillLevelTable.put(SkillType.MAGICIAN_NORMAL_ATTACK, levelInfo);

        /* Flying Object Info */
        boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));  // 기획에 따라 지울수도. 일단은, 버프적용될 때 못움직이게? 혹은 잠시라도 멈추게.
        boolParams.add(new ConditionBoolParam((ConditionType.isDisableAttack), true));
        //boolParams.add(new ConditionBoolParam((ConditionType.isUnTargetable), true));

        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));   // 데미지 양 : 레벨테이블 참조.

        newFlyingObjBuff = new BuffAction(-1, -1, -1, -1, -1, boolParams, floatParams);

        // 거리.. 계산 해야할 듯. 어케할지는 모르겠지만. .. 이걸 지금 여기서 계산해줘야하나?? 싶기도하긴 하다. 아근데 이 스킬은 대상이 정해져있자나!
        newFlyingObjInfo = new FlyingObjectInfo(7f, -1f, -1f, newFlyingObjBuff);


        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_NORMAL_ATTACK, "마법사 평타",
                -1f, 0, 0, 10f,
                null, null, newFlyingObjInfo);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);


        /************ 마법사 스킬 1 : 파이어볼 ; 스킬번호_6 ***********/
        /**
         * 법사 일반 평타랑 다를게 없는거같은데... 사거리나 공격력, 쿨타임, 레벨 등등만 달라지고.
         * 후딱 하고 메테오랑 힐링 해야지.
         *
         */
        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, 100f, 14f, 2.5f, 7f, -1f, 5f));
        levelInfo.put(2, new SkillInfoPerLevel(2, 150f, 14f, 2.5f, 7f, -1f, 5f));
        levelInfo.put(3, new SkillInfoPerLevel(3, 200f, 14f, 2.5f, 7f, -1f, 5f));
        levelInfo.put(4, new SkillInfoPerLevel(4, 250f, 14f, 2.5f, 7f, -1f, 5f));
        levelInfo.put(5, new SkillInfoPerLevel(5, 300f, 14f, 2.5f, 7f, -1f, 5f));
        skillLevelTable.put(SkillType.MAGICIAN_FIREBALL, levelInfo);

        /* Flying Object Info */
        boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));  // 기획에 따라 지울수도. 일단은, 버프적용될 때 못움직이게? 혹은 잠시라도 멈추게.
        boolParams.add(new ConditionBoolParam((ConditionType.isDisableAttack), true));
        //boolParams.add(new ConditionBoolParam((ConditionType.isUnTargetable), true));

        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));   // 데미지 양 : 레벨테이블 참조.


        newFlyingObjBuff = new BuffAction(-1, -1, 0.11f, -1, -1, boolParams, floatParams);

        // 거리.. 계산 해야할 듯. 어케할지는 모르겠지만. .. 이걸 지금 여기서 계산해줘야하나?? 싶기도하긴 하다. 아근데 이 스킬은 대상이 정해져있자나!
        newFlyingObjInfo = new FlyingObjectInfo(7f, 2.5f, -1f, newFlyingObjBuff);


        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_FIREBALL, "파이어볼",
                5f, 0, 5, 14f,
                null, null, newFlyingObjInfo);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);



        /************ 마법사 스킬 2 : 힐링 ; 스킬번호_7 ***********/
        /**
         * 스킬 공격력만큼, 범위 내 아군들이 회복한다.
         * 범위(스킬모양 : 원)
         * 스오.
         *
         */
        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, 100f, 10f, 10f, -1f, -1, 5f));
        levelInfo.put(2, new SkillInfoPerLevel(2, 150f, 11f, 11f, -1f, -1, 5f));
        levelInfo.put(3, new SkillInfoPerLevel(3, 200f, 12f, 12f, -1f, -1, 5f));
        levelInfo.put(4, new SkillInfoPerLevel(4, 250f, 13f, 13f, -1f, -1, 5f));
        levelInfo.put(5, new SkillInfoPerLevel(5, 300f, 14f, 14f, -1f, -1, 5f));
        skillLevelTable.put(SkillType.MAGICIAN_HEAL, levelInfo);

        /* Skill Object Info */
        /*boolParams = new ArrayList<>();

        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.hpRecoveryAmount, -1));

        newSkillObjBuff = new BuffAction(-1, 2f, 1.5f, 1.5f, boolParams, floatParams);
        newSkillObjInfo = new SkillObjectInfo(SkillAreaType.CIRCLE,
                5f, 500f * 2, -1f, -1f,
                newSkillObjBuff);*/


        /* Skill Info */
        /*newSkillInfo = new SkillInfo(SkillType.MAGICIAN_HEAL, "힐링",
                5, 0, 50, 500f * 2,
                null, newSkillObjInfo, null);*/


        /*** 장판의 회복 버프를, 스킬의 버프로 바꿔줌. */
        /* Skill Object Info */
        boolParams = new ArrayList<>();

        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.hpRecoveryAmount, -1));

        newSkillObjBuff = new BuffAction(-1, -1, 2f, 1.5f, 1.5f, boolParams, floatParams);
        newSkillObjInfo = new SkillObjectInfo(SkillAreaType.CIRCLE,
                3f, 10f, -1f, -1f,
                new BuffAction());


        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_HEAL, "힐링",
                5, 0, 50, 10f,
                newSkillObjBuff, newSkillObjInfo, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);



        /************ 마법사 스킬 3 : 메테오 ; 스킬번호_8 ***********/
        /**
         * 스킬을 시전한다.
         *      메테오 투사체를 생성한다.
         *          장판은.. 메테오가 떨어지고 나서(사라지고) 생기는듯?? -->> 이걸 어케하지..
         *          ㄴ 메테오가 사라질 때, 즉 적들 충돌 및 데미지 처리를 한 후에, 메테오 장판 오브젝트를 생성한다
         *              (먼가 플오 시스템에서 전혀다른 앤티티를 생성하는게 잠깐 거부감 들었는데, 생각해보니까 다른 시스템들에서도 잘만 만들...고,
         *              앤티티 삭제 요청 생성요청 넣어주고 그러잖아 일단 이렇게 가고 나중에 바꾸자)
         *
         *      플라잉 오브젝트 시스템에서,,
         *          투사체가 땅에 충돌하면서, 반경 500에 있는 적들에게 메테오 데미지 100%씩 입힌다.
         *          메테오 장판을 생성한다. <- 버프 효과는, 메테오 공격력의 10퍼센트 데미지를 3초동안 매 초마다 지속적으로 입히는 것.
         *          메테오는 사라진다,,
         *
         *      스킬 오브젝트 시스템에서,, 이제, 스킬 오브젝트에서도 그.. 스킬타입 검사해야되나 -> 묘하게 비합리적인거같기도하고
         *          장판 범위 내(1000) 위치하는 적들에게 (버프 : 데미지, 데미지량 : 스킬렙메테오10퍼, 지속시간 : 장판의 남은 지속시간) 버프를 걸어준다.
         *          다음 틱에서는, 이미 자신의 효과를 받고 있는(이걸 어케따지지.. ㅋㅋ헐) 대상에 대해서는 패스,
         *              새로 범위 내에 들어온 적이라면, 매번 갱신되는 메테오장판의 지속시간을 넣어준다.
         *          시간이 다되면 장판은 사라진다,,,
         *
         */

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, 100f, 20f, 10f, 3f, 3f, 5f));
        levelInfo.put(2, new SkillInfoPerLevel(2, 150f, 20f, 11f, 3f, 4f, 5f));
        levelInfo.put(3, new SkillInfoPerLevel(3, 200f, 20f, 12f, 3f, 5f, 5f));
        levelInfo.put(4, new SkillInfoPerLevel(4, 250f, 20f, 13f, 3f, 6f, 5f));
        levelInfo.put(5, new SkillInfoPerLevel(5, 300f, 20f, 14f, 3f, 7f, 5f));
        skillLevelTable.put(SkillType.MAGICIAN_METEOR, levelInfo);

        /* Skill Object Info */
        boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));  // 기획에 따라 지울수도. 일단은, 버프적용될 때 못움직이게? 혹은 잠시라도 멈추게.
        boolParams.add(new ConditionBoolParam((ConditionType.isDisableAttack), true));
        //boolParams.add(new ConditionBoolParam((ConditionType.isUnTargetable), true));

        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));

        newSkillObjBuff = new BuffAction(-1, -1, 3f, 0f, 1f, boolParams, floatParams);
        newSkillObjInfo = new SkillObjectInfo(SkillAreaType.CIRCLE,
                5f, 10f, -1f, -1f,
                newSkillObjBuff);


        /* Flying Object Info */ // 메테오는 투사체가 없는걸로..
       /* boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));

        newFlyingObjBuff = new BuffAction(-1, -1, -1, -1, boolParams, floatParams);

        newFlyingObjInfo = new FlyingObjectInfo(7f, -1f, -1f, newFlyingObjBuff);*/


        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_METEOR, "메테오",
                5f, 0, 100, 10f,
                null, newSkillObjInfo, null);


        // 초반 딜 버프 넣어줌.
        boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
        boolParams.add(new ConditionBoolParam((ConditionType.isDisableAttack), true));
        //boolParams.add(new ConditionBoolParam((ConditionType.isUnTargetable), true));
        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));

        newSkillInfo.buffAction = new BuffAction(-1, -1, 3.0f, 2.0f, 2.0f, boolParams, floatParams);



        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

        /*************************************************************************************************************/


        /********************************************* 궁수 스킬 *****************************************************/

        /************ 궁수 스킬 0 : 평타 ; 스킬번호_9 ***********/
        /**
         * Skill ID : # 0 2 03 0009
         *              단일, 타게팅 + 투사체 있음, 직업 : 궁수, 스킬에 부여된 번호
         * 사거리 : 600
         * 쿨타임 : 공격속도
         *
         * 스킬을 사용한다
         *      화살 투사체를 생성한다 (투사체 속도 : 7)
         *      ㄴ 투사체는, 궁수(플레이어 캐릭터)의 공격력만큼의 데미지를 입히는 버프를 갖는다.
         *
         * 투사체 시스템 :
         *      화살 투사체를 이동시킨다. (투사체 속도 : 7)
         *      ㄴ if 타게팅 대상이 공격범위에 들 정도로 가까워지거나 부딪히면, 대상에게 버프를 부여한다.
         *      투사체는 파괴.
         *
         */
        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, -1f, 12f, 12f, 10f, -1f, -1f));
        skillLevelTable.put(SkillType.ARCHER_NORMAL_ATTACK, levelInfo);

        /* Flying Object Info */
        boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
        boolParams.add(new ConditionBoolParam((ConditionType.isDisableAttack), true));
        //boolParams.add(new ConditionBoolParam((ConditionType.isUnTargetable), true));
        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));

        newFlyingObjBuff = new BuffAction(-1, -1, -1, -1, -1, boolParams, floatParams);

        newFlyingObjInfo = new FlyingObjectInfo(7f, -1f, -1f, newFlyingObjBuff);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_NORMAL_ATTACK, "궁수 평타",
                -1f, 0, 0, 12f,
                null, newSkillObjInfo, newFlyingObjInfo);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);



        /************ 궁수 스킬 1 : 파워샷 ; 스킬번호_10 ***********/
        /**
         * Skill ID : # 1 4 03 0010
         *             범위, 논타게팅 + 투사체 있음, 직업 : 궁수, 스킬에 부여된 번호
         * 사거리 : 1000
         * 쿨타임 : 5
         * 소모 마력 : 10
         *
         *  스킬을 사용하면, 유저가 모음을 지속한 시간과 그 범위?가 전달된다.
         *  요 값을 가지고, 투사체를 생성한다.
         *      1초 미만 : 기본 데미지
         *      2초 미만 : 기본 * 1.5배
         *      2초 이상 : 기본 * 2배
         *
         *      사거리에 도달할 때 까지, 관통하는 적들 여럿에게 데미지를 준다.
         *      거리에 도달하면 파괴된다.
         *
         */
        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, 100f, 20f, 2.5f, 29f, -1f, 5));
        levelInfo.put(2, new SkillInfoPerLevel(2, 150f, 21f, 2.5f, 29f, -1f, 5));
        levelInfo.put(3, new SkillInfoPerLevel(3, 200f, 22f, 2.5f, 29f, -1f, 5));
        levelInfo.put(4, new SkillInfoPerLevel(4, 250f, 23f, 2.5f, 30f, -1f, 5));
        levelInfo.put(5, new SkillInfoPerLevel(5, 300f, 24f, 2.5f, 30f, -1f, 5));

        skillLevelTable.put(SkillType.ARCHER_POWER_SHOT, levelInfo);

        /* Flying Object Info */
        boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
        boolParams.add(new ConditionBoolParam(ConditionType.isUnTargetable, true));
        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));

        newFlyingObjBuff = new BuffAction(-1, -1, 0.11f, -1f, -1f, boolParams, floatParams);
        newFlyingObjInfo = new FlyingObjectInfo(9f, 2.5f, -1f, newFlyingObjBuff);


        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_POWER_SHOT, "파워 샷",
                5f, 0, 20, 20f,
                null, null, newFlyingObjInfo);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);


        /************ 궁수 스킬 2 : 멀티샷 ; 스킬번호_11 ***********/
        /**
         * Skill ID : # 1 4 03 0011
         *             범위, 논타게팅 + 투사체 있음, 직업 : 궁수, 스킬에 부여된 번호
         * 사거리 : 700
         * 쿨타임 : 10
         * 소모 마력 : 50
         *
         *  스킬을 사용한다.
         *      캐릭터의 스킬 레벨에 따라, 다른 갯수의 화살 투사체를 생성하게 된다.
         *      ==============
         *      1렙 =>> 3
         *      2렙 =>> 4
         *      3렙 =>> 5
         *      4렙 =>> 6
         *      5렙 =>> 7
         *      ==============
         *      120도 각 내에 맞추어, 각 갯수별 투사체가 날아갈 방향을 지정해준다.
         *
         *  투사체 시스템에서
         *      투사체와 충돌한 대상에게 데미지가 들어가고, 투사체(화살)은 파괴된다.
         *
         *
         */

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, 100f, 14f, 3f, 22f, -1f, 5f));
        levelInfo.put(2, new SkillInfoPerLevel(2, 150f, 14f, 3f, 22f, -1f, 5f));
        levelInfo.put(3, new SkillInfoPerLevel(3, 200f, 14f, 3f, 22f, -1f, 5f));
        levelInfo.put(4, new SkillInfoPerLevel(4, 250f, 14f, 3f, 22f, -1f, 5f));
        levelInfo.put(5, new SkillInfoPerLevel(5, 300f, 14f, 3f, 22f, -1f, 5f));
        skillLevelTable.put(SkillType.ARCHER_MULTI_SHOT, levelInfo);

        /* Flying Object Info */
        boolParams = new ArrayList<>();
        //boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
        //boolParams.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
        //boolParams.add(new ConditionBoolParam(ConditionType.isUnTargetable, true));
        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.damageAmount, -1));

        newFlyingObjBuff = new BuffAction(-1,-1, 0.15f, -1, -1, boolParams, floatParams);
        newFlyingObjInfo = new FlyingObjectInfo(22f, 3f, -1f, newFlyingObjBuff);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_MULTI_SHOT, "멀티 샷",
                5f, 0, 50, 14f,
                null, null, newFlyingObjInfo);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);





        /************ 궁수 스킬 3 : 에로우 레인 ; 스킬번호_12 ***********/
        /**
         * Skill ID : # 1 4 03 0012
         *             범위, 논타게팅 + 투사체 있음, 직업 : 궁수, 스킬에 부여된 번호
         * 사거리 : 700
         * 쿨타임 : 30
         * 소모 마력 : 100
         * 지속시간 : 3초
         * 공격 범위(?) : 500
         * 투사체 속도 : 4
         *
         *  각 화살을 맞는 단일 개체에 데미지가 들어가는게 아니라, 사실상 3초동안 지속되는 장판이 있고,
         *  그 내부에 있는 적들에게 지속적인 데미지가 들어가는데, 화살은 걍 애니메이션임. // 맞나?? 이걸 어케 구현하지.. 나는 데미지만 생각하면 되나??
         *  ㄴ 공격범위 있는거 보면 맞는듯. ??..
         *  음.. 근데 사거리가 있잖아. ???
         *  ㄴ 사거리는. 시전자를 기준으로, 스킬을 적용할 범위를 지정할 때, 어느정도 멀리까지 할 수 있냐? 를 말하는거고,
         *  그렇게 정해진 지점에 대해서. 그 지점을 중심으로 공격 범위에 해당하는 부분에 존재하는 적들에게 데미지를 입히는건가?
         *
         *  스킬을 사용한다.
         *      장판 깔아준다!
         *
         *      그 장판 위치에.. 적들이. 아니.. 뭔가가 날라가긴 날라가야 함. 걔가 단순 이미지 뿐이라고 해도. 그러면.. 투사체가 있긴 있어야겟지?? 맞나?
         *      투사체 날라가고, (화살 다발..인데 사실은 객체 하나)
         *
         *      투사체가 메테오마냥 그 지점에 떨어지면, 그 위치에 장판을 깔아주는거임.
         *      그리고 메테오장판처럼, 3초동안.. 지속 데미지...?를 주는거임.
         *      뭐 독 들어있고 그런게 아니니까, 장판이 지속되는 동안, 화살이.. 어! 계속 떨어져야 하나?? (애니메이션 상ㅇ으론 그런데.. 물론 3초동안 계속
         *      시전자가 활을 쉬지않고 날리는 그런 모양새는 아니고).

         *
         */
        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, new SkillInfoPerLevel(1, 100f, 14f, 10f, 4f, 3f, 5f));
        levelInfo.put(2, new SkillInfoPerLevel(2, 150f, 14f, 11f, 4f, 4f, 5f));
        levelInfo.put(3, new SkillInfoPerLevel(3, 200f, 14f, 12f, 4f, 5f, 5f));
        levelInfo.put(4, new SkillInfoPerLevel(4, 250f, 14f, 13f, 4f, 6f, 5f));
        levelInfo.put(5, new SkillInfoPerLevel(5, 300f, 14f, 14f, 4f, 7f, 5f));
        skillLevelTable.put(SkillType.ARCHER_ARROW_RAIN, levelInfo);

        /* Skill Object Info */
        boolParams = new ArrayList<>();
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableMove, true));  // 기획에 따라 지울수도. 일단은, 버프적용될 때 못움직이게? 혹은 잠시라도 멈추게.
        boolParams.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));

        floatParams = new ArrayList<>();
        floatParams.add(new ConditionFloatParam(ConditionType.hpRecoveryAmount, -1));

        newSkillObjBuff = new BuffAction(-1, -1, 3f, 0.5f, 1f, boolParams, floatParams);
        newSkillObjInfo = new SkillObjectInfo(SkillAreaType.CIRCLE,
                3f, 10f, -1f, -1f,
                newSkillObjBuff);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_ARROW_RAIN, "애로우 레인",
                5f, 0, 100, 14f,
                null, newSkillObjInfo, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);


        /***************************************************************************************************************/

        /**
         * 2020 01 23 목 권령희
         * 새로 추가될 스킬들의 정보를 불러오는 처리.. 얘네는 각각 매서드를 따로 만들어줬음
         */

        /* 전사 */
        readSkillInfo_Knight_GarrenQ();
        readSkillInfo_Knight_Berserker();
        readSkillInfo_Knight_GarrenE();
        readSkillInfo_Knight_IncrHp();
        readSkillInfo_Knight_GarrenR();
        readSkillInfo_Knight_Invincible();

        /* 마법사 */
        readSkillInfo_Magician_LightningRoad();
        readSkillInfo_Magician_IceBall();
        readSkillInfo_Magician_Shield();
        readSkillInfo_Magician_IceField();
        readSkillInfo_Magician_Thunder();
        readSkillInfo_Magician_FrozenBeam();

        /* 궁수*/
        readSkillInfo_Archer_IncAttackSpeed();
        readSkillInfo_Archer_HeadShot();
        readSkillInfo_Archer_CriticalHit();
        readSkillInfo_Archer_Storm();
        readSkillInfo_Archer_Fire();
        readSkillInfo_Archer_Snipe();

    }

    /**************************************** 각 스킬별 함수 ***********************************************************/

    /**
     * 궁수가 파워샷 스킬을 사용했을 때 처리를 담당한다.
     * @param worldMap
     * @param event
     */
    public static void useSkill_archerPowerShot(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 시전자 및 시전 스킬 정보 */
        CharacterEntity skillUser =  worldMap.characterEntity.get(event.userEntityID);
        ConditionComponent userCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        SkillSlot skillToUse = null;
        SkillSlot slot = null;

        int slotNum = event.skillSlotNum;
        int skillType = -1;
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){
            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == event.skillSlotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;
                break;
            }
        }

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);

        /** 시전자가 스킬을 사용할 수 있는 상태인지 판별한다 */

        boolean isAbleToUseSkill = false;
        boolean hasEnoughMp = false;
        boolean isCoolTimeZero = false;

        hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        isAbleToUseSkill = ((!userCondition.isDisableSkill) && hasEnoughMp && isCoolTimeZero) ? true : false;  // 일단은 이거밖에 안떠오르네..

        if(isAbleToUseSkill) {  /* 스킬을 사용할 수 있는 상태라면 */

            /** 투사체 및 오브젝트 등을 생성하기 위해 필요한 데이터 계산 */

            /** event에서 받아온 데이터들 꺼내기 */

            Vector3 skillDirection  = event.skillDirection;
            float skillDistanceRate = event.skillDistanceRate;


            /** 투사체 생성에 필요한 정보들 꺼내기 & 활용하기 */

            /* EntityID */
            int publishedEntityID;  // FlyingObject Entity생성 후 부여받을 것. 그 전까지는 비우거나 쓰레기값,,

            /* Position Component */
            PositionComponent positionComponent;
            Vector3 position;        // 일단. 유저..의 위치로 둬도 괜찮을듯??

            /* FlyingObject Component */
            int createdSkillType;   // 투사체를 생선한 스킬 타입. 궁수 파워샷
            int userEntityID;       // 스킬 시전자. 위에 published~랑 같다.
            float flyingSpeed;      // 스킬정보 및 레벨 테이블 등을 참고하면 된다.
            float flyingObjectRadius;   // 스킬정보 및 레벨 테이블 참고

            Vector3 startPosition;  // 위 position component와 같다고 봐도 될 듯?
            Vector3 direction;      // 클라 event로 받아오는 정보.

            float flyingObjectRemainDistance;   // 최대 사거리까지 남은 거리.. 계산해야 할듯?

            BuffAction buffAction;  // 충돌하는 Entity에게 부여할 버프/데미지/효과

            int targetEntityID;     // 여기서는 없음. 미리 타게팅된 적이 있는가??
            // 이것도. 나중에는... 서버에서 다 계산되도록 해야??

            boolean beDestroyedByCrash;

            /*==========================================================================================================*/

            /* position component */
            positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());

            /* flyingObject component */
            createdSkillType = skillInfo.skillType;
            flyingSpeed = skillInfo.flyingObjectInfo.flyingObjectSpeed;

            /**  2019 12 27 */
            flyingSpeed = currentLevelSkillInfo.flyingObjectSpeed;

            flyingObjectRadius = skillInfo.flyingObjectInfo.flyingObjectRadius;

            startPosition = (Vector3) positionComponent.position.clone();
            direction = skillDirection;

            flyingObjectRemainDistance = currentLevelSkillInfo.range * skillDistanceRate;

            /*==========================================================================================================*/

            int newEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

            /**
             * 2020 02 06 버프 적용
             *
             * [ 버프 목록 ]
             * -- 데미지 : 스킬 공격력 만큼 데미지를 입힘, 지속시간 ( 0.15 f), 쿨타임&남은쿨타임 없음 (-1f)
             * --   상태 : 이동 불가&공격 불가, 지속시간 3f, 쿨타임&남은쿨타임 없음 (-1f)
             *
             * 특이사항
             * ㄴ 데미지는 한 번 타격을 입히고 사라진다.
             *      이 스킬의 관통 투사체에 한 번 공격당한 몬스터가 또 공격받는 것을 막기 위해
             *      상태 버프를 두어, 그 지속시간을 조금 길게 두고 처리.
             *
             *      더 길어야 하면 어쩌지...
             *      ㄴ 그때는 공식 만들어야지 범위 벗어날 때 까지 지속되거나 혹은.. 새로추가된 다른 일부 스킬들 처럼,
             *          " 특정 스킬의 효과를 받고 있는 중이다 & 이미 받은 상태다 " 라는걸 나타내기 위해서
             *          별도 상태 변수를 하나 추가해서 걔를 버프로 쓰던가.
             *          -->> 사실 어케보면 이게 제일 하드코딩스럽지 않고 깔끔한걸수도 있음
             *      ㄴ 추가로, 투사체 시스템에서.. 요 스킬에 대해,
             *          대상이 투사체가 날아가는 방향의 전방에 있는지 확인하는 처리도 넣을 것. [ NOT YET ]
             *
             * ㄴ 이전에는, 위와 같은 처리를 위해, 단순히 데미지 버프 하나를 10초동안 유지되도록 했었음.
             *  쿨타임을 10초로 줘서, 유지기간 내 딱 한번만 적용되게끔 함.
             *
             *  데미지와 상태가 같은 버프액션에 들어가야 하므로,
             *  여기서 데미지 버프를 위해 createDamageBuff()매서드를 호출하지 않고 Param만 얻음.
             *  ㄴ 이 매서드는.. flyingObjectSystem에서 해당 Param을 꺼내 호출할 듯.
             *
             */

            /* 데미지 버프 */
            float skillDefaultDamage = currentLevelSkillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
            ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, userCondition);

            /* 상태 버프 */
            ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
            ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);


            BuffAction flyingObjBuff = new BuffAction();
            flyingObjBuff.unitID = newEntityID;
            flyingObjBuff.skillUserID = skillUser.entityID;

            flyingObjBuff.remainTime = 3f;  // 아 이걸 마냥 길게 하기도 그런데..만약 다른 방법을 쓰게 되면, 얘는 1f 이하로 두자.
            flyingObjBuff.coolTime = -1f;
            flyingObjBuff.remainCoolTime = -1f;

            flyingObjBuff.floatParam.add(damageParam);
            flyingObjBuff.boolParam.add(moveDebuff);
            flyingObjBuff.boolParam.add(attackDebuff);

            /***********************************************************************************************************/


            /* FlyingObject Component */
            FlyingObjectComponent flyingObjectComponent
                    = new FlyingObjectComponent(createdSkillType, skillUser.entityID, flyingSpeed, flyingObjectRadius,
                    startPosition, direction, flyingObjectRemainDistance, flyingObjBuff, -1);
            flyingObjectComponent.beDestroyedByCrash = false;

            /* FlyingObject Entity */
            FlyingObjectEntity flyingObjectEntity
                    = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
            flyingObjectEntity.entityID = newEntityID;



            /**  생성요청 큐에 넣기  */
            worldMap.requestCreateQueue.add(flyingObjectEntity);

            /** 스킬 쿨타임 및 시전자쪽에 필요한 처리 */    // 뭔가 명확하게 정리가 잘 안되네

            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.skillUserID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = (worldMap.tickRate * 0.01f);
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();


            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;



            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            // 중계가 필요하다면 중계처리를 하고.
            //공격모션 중계
            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.ARCHER_POWER_SHOT;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);
        }

    }

    /**
     * 궁수가 멀티샷 스킬을 사용했을 때 처리를 담당한다. (안씀)
     * @param worldMap
     * @param event
     */
    public static void useSkill_archerMultiShot(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 시전자 및 시전 스킬 정보 */
        CharacterEntity skillUser =  worldMap.characterEntity.get(event.userEntityID);
        ConditionComponent userCondition = skillUser.conditionComponent;

        SkillSlot skillToUse = null;
        SkillSlot slot = null;

        int slotNum = event.skillSlotNum;
        int skillType = -1;
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){
            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == event.skillSlotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;
                break;
            }
        }

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);

        System.out.println("스킬 정보 : " + skillInfo.skillName);


        /** 시전자가 스킬을 사용할 수 있는 상태인지 판별한다 */

        boolean isAbleToUseSkill = false;
        boolean hasEnoughMp = false;
        boolean isCoolTimeZero = false;

        hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        isAbleToUseSkill = ((!userCondition.isDisableSkill) && hasEnoughMp && isCoolTimeZero) ? true : false;  // 일단은 이거밖에 안떠오르네..

        if(isAbleToUseSkill) {  /* 스킬을 사용할 수 있는 상태라면 */

            /** 투사체 및 오브젝트 등을 생성하기 위해 필요한 데이터 계산 */

            /** event에서 받아온 데이터들 꺼내기 */

            Vector3 skillDirection  = event.skillDirection;     // 필요!
            float skillDistanceRate = event.skillDistanceRate;    // 필요!

            System.out.println("스킬이 시전된 방향 : " + skillDirection.x() + ", " + skillDirection.y() + ", " + skillDirection.z() + ", ");

            System.out.println("스킬 거리 비율(?) : " + skillDistanceRate);



            /** 투사체 생성에 필요한 정보들 꺼내기 & 활용하기 */

            /* EntityID */
            int publishedEntityID;  // FlyingObject Entity생성 후 부여받을 것. 그 전까지는 비우거나 쓰레기값,,

            /* Position Component */
            PositionComponent positionComponent;
            Vector3 position;        // 일단. 유저..의 위치로 둬도 괜찮을듯??

            /* FlyingObject Component */
            int createdSkillType;   // 투사체를 생선한 스킬 타입. 궁수 파워샷
            int userEntityID;       // 스킬 시전자. 위에 published~랑 같다.
            float flyingSpeed;      // 스킬정보 및 레벨 테이블 등을 참고하면 된다.
            float flyingObjectRadius;   // 스킬정보 및 레벨 테이블 참고

            Vector3 startPosition;  // 위 position component와 같다고 봐도 될 듯?
            Vector3 direction;      // 클라 event로 받아오는 정보.

            float flyingObjectRemainDistance;   // 최대 사거리까지 남은 거리.. 계산해야 할듯?

            BuffAction buffAction;  // 충돌하는 Entity에게 부여할 버프/데미지/효과

            int targetEntityID;     // 여기서는 없음. 미리 타게팅된 적이 있는가??
            // 이것도. 나중에는... 서버에서 다 계산되도록 해야??

            boolean beDestroyedByCrash;

            /*==========================================================================================================*/

            ArrayList<Double> directions = new ArrayList<>();
            // 아 미친 일단 하드코딩 해야겠다,,,

            //skillToUse.skillLevel = 3;
            int beCreatedFlyingObjectCount = (skillToUse.skillLevel + 2);

            switch (skillToUse.skillLevel){

                case 1:
                    System.out.println("멀티샷 레벨 1");
                    directions.add(60d);
                    directions.add(0d);
                    directions.add(-60d);
                    break;
                case 2:
                    System.out.println("멀티샷 레벨 2");
                    directions.add(60d);
                    directions.add(20d);
                    directions.add(-20d);
                    directions.add(-60d);
                    break;
                case 3:
                    System.out.println("멀티샷 레벨 3");
                    directions.add(60d);
                    directions.add(30d);
                    directions.add(0d);
                    directions.add(-30d);
                    directions.add(-60d);
                    break;
                case 4:
                    System.out.println("멀티샷 레벨 4");
                    directions.add(60d);
                    directions.add(36d);
                    directions.add(12d);
                    directions.add(-12d);
                    directions.add(-36d);
                    directions.add(-60d);
                    break;
                case 5:
                    System.out.println("멀티샷 레벨 5");
                    directions.add(60d);
                    directions.add(40d);
                    directions.add(20d);
                    directions.add(0d);
                    directions.add(-20d);
                    directions.add(-40d);
                    directions.add(-60d);
                    break;
                default:
                    break;
            }

            /* 생성될 투사체 갯수만큼 반복한다 */
            System.out.println("멀티 샷 생성할 투사체 갯수 : " + beCreatedFlyingObjectCount);
            for(int i=0; i<beCreatedFlyingObjectCount; i++){

                /* position component */
                positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());

                /* flyingObject component */
                createdSkillType = skillInfo.skillType;
                flyingSpeed = skillInfo.flyingObjectInfo.flyingObjectSpeed;
                flyingObjectRadius = skillInfo.flyingObjectInfo.flyingObjectRadius;

                //startPosition = (Vector3) positionComponent.position.clone();
                startPosition = (Vector3) event.skillDirection.clone();
                System.out.println((i+1) + "번째 투사체 원래 방향 : " + startPosition.x() + ", " + startPosition.y() + ", " + startPosition.z());
                direction = Vector3.rotateVector3ByAngleAxis(startPosition, new Vector3(0,1,0), directions.get(i)).normalize();
                System.out.println((i+1) + "번째 투사체 방향 : " + direction.x() + ", " + direction.y() + ", " + direction.z());

                //flyingObjectRemainDistance = skillInfo.skillRange * skillDistanceRate;
                flyingObjectRemainDistance = currentLevelSkillInfo.range * skillDistanceRate;


                /* buffAction **/
                buffAction = (BuffAction) skillInfo.flyingObjectInfo.buffAction.clone();
                buffAction.floatParam = new ArrayList<>();
                buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));


                /*==========================================================================================================*/

                int newEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

                buffAction.unitID = newEntityID;

                        /* FlyingObject Component */
                FlyingObjectComponent flyingObjectComponent
                        = new FlyingObjectComponent(createdSkillType, skillUser.entityID, flyingSpeed, flyingObjectRadius,
                        startPosition, direction, flyingObjectRemainDistance, buffAction, -1);
                flyingObjectComponent.beDestroyedByCrash = true;

                /* FlyingObject Entity */
                FlyingObjectEntity flyingObjectEntity
                        = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
                flyingObjectEntity.entityID = newEntityID;


                /**  생성요청 큐에 넣기  */
                worldMap.requestCreateQueue.add(flyingObjectEntity);


            }

            /** 스킬 쿨타임 및 시전자쪽에 필요한 처리 */    // 뭔가 명확하게 정리가 잘 안되네

            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = ( worldMap.tickRate * 0.01f );
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();


            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;



            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            // 중계가 필요하다면 중계처리를 하고.
            //공격모션 중계
            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.ARCHER_MULTI_SHOT;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);
        }


    }

    /**
     * 궁수가 화살비 스킬을 사용했을 때 처리를 담당한다.
     * @param worldMap
     * @param event
     */
    public static void useSkill_archerArrowRain(WorldMap worldMap, ActionUseSkill event){

        //System.out.println("스킬 시전자 및 시전하려는 스킬의 정보를 찾습니다.");

        /* 스킬 시전자 및 시전 스킬 정보 */
        CharacterEntity skillUser =  worldMap.characterEntity.get(event.userEntityID);
        ConditionComponent userCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        SkillSlot skillToUse = null;
        SkillSlot slot;

        int slotNum = event.skillSlotNum;
        int skillType = -1;
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){
            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == event.skillSlotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;
                break;
            }
        }

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);

        System.out.println("클라가 보낸 슬롯 넘버 : " + event.skillSlotNum);
        System.out.println("스킬 슬롯 : " + skillToUse.slotNum);
        System.out.println("스킬 타입 : " + skillType);


        /** 시전자가 스킬을 사용할 수 있는 상태인지 판별한다 */

        boolean isAbleToUseSkill = false;
        boolean hasEnoughMp = false;
        boolean isCoolTimeZero = false;

        hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        isAbleToUseSkill = ((!userCondition.isDisableSkill) && hasEnoughMp && isCoolTimeZero) ? true : false;  // 일단은 이거밖에 안떠오르네..

        if(isAbleToUseSkill) {  /* 스킬을 사용할 수 있는 상태라면 */

            System.out.println("스킬사용 가능합니다.");

            /** 투사체 및 오브젝트 등을 생성하기 위해 필요한 데이터 계산 */

            /** event에서 받아온 데이터들 꺼내기 */

            Vector3 skillDirection  = event.skillDirection;     // 필요!
            float skillDistanceRate = event.skillDistanceRate;    // 필요!


            /** 스.오 생성에 필요한 정보들 꺼내기 & 활용하기 */

            /* EntityID */
            int publishedEntityID;  // FlyingObject Entity생성 후 부여받을 것. 그 전까지는 비우거나 쓰레기값,,

            /* Position Component */
            PositionComponent positionComponent;
            Vector3 position;        // 일단. 유저..의 위치로 둬도 괜찮을듯??

            /* SkillObject Component */
            int createdSkillType;   // 투사체를 생선한 스킬 타입.
            int skillAreaType;
            int userEntityID;       // 스킬 시전자. 위에 published~랑 같다.
            float skillObjectDurationTime;      // 스킬정보 및 레벨 테이블 등을 참고하면 된다.
            float skillObjectRadius;   // 스킬정보 및 레벨 테이블 참고

            Vector3 startPosition;
            Vector3 direction;      // 클라 event로 받아오는 정보.

            float distanceRate;

            BuffAction buffAction;  // 충돌하는 Entity에게 부여할 버프/데미지/효과

            /*==========================================================================================================*/

            /* position component */
            //positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());
            positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());


            /* skillObject component */
            createdSkillType = skillInfo.skillType;
            skillAreaType = skillInfo.skillObjectInfo.skillAreaType;
            //skillObjectDurationTime = skillInfo.skillObjectInfo.skillObjectDurationTime;
            skillObjectDurationTime = currentLevelSkillInfo.durationTime;
            //skillObjectRadius = skillInfo.skillObjectInfo.skillObjectRadius;
            skillObjectRadius = currentLevelSkillInfo.attackRange;

            // 장판이 펼쳐질 지점 구해야 함.
            startPosition = (Vector3) positionComponent.position.clone();

            System.out.println("시작(시전자 위치) : "
                    + startPosition.x() + ", " + startPosition.y() + ", " + startPosition.z() + ", ");

            direction = skillDirection;

            // 유저 위치로부터. 유저가 바라보는? 선택한 지점이 있고. 유저로부터 그 거리까지의 비율?이 올거고. 그 비율이랑 최대사거리를 곱하면.
            // 거기까지의 거리가 나올거고. 방향은 없어도 될거같고. 그러면, 그 지점의 position을 구하면 될듯.
            // 그래서 그걸 어떻게 구하지.
            //
            distanceRate = event.skillDistanceRate;

            /* 장판 위치 계산하기 */
            direction.setSpeed(skillDistanceRate * currentLevelSkillInfo.range);

            startPosition.movePosition(startPosition, direction);




            /** 일단 혹시 몰라서 냅두는데, 만약 리팩토링한 버프가 잘 먹히면 지워버릴 것  */
            /* buffAction **/
            buffAction = (BuffAction) skillInfo.skillObjectInfo.buffAction.clone();
            buffAction.remainTime = 3f; /** 2020 02 06 아니 왜 3으로 한거야.. */

            /** 2019 12 27 */
            buffAction.remainTime = currentLevelSkillInfo.durationTime;

            buffAction.floatParam = new ArrayList<>();
            buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));


            /*==========================================================================================================*/

            int newEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();


            /**
             * 2020 02 06 목요일 추가
             * [ 버프 ]
             * -- 데미지 버프 ; 화살 장판 범위 내에 있다는 가정 하에, 1초마다 한 번 데미지가 적용되도록 함.
             *                  데미지'버프' 유지 시간 1초, 쿨타임 1초, 남은 쿨타임 초기값 0초??
             *                  ㄴ 이걸 뭘로 주는게 적절한지 모르겠다 일단 해보고 조금씩 수정하는 걸로??
             *
             * 기존꺼 기준으로, 스킬을 맞았을 때 이동 못하게 하는 효과는 없음..
             * ㄴ 이걸 주고 안주고의 기준이 애매하긴 한데,
             *      그런 효과 없이 장판에 있는동안 초당 데미지를 주도록 하려면
             *      데미지버프를 1초동안 유지되도록 하고, 쿨타임을 사용하는 수 밖에 없을 듯.
             *
             * 얘 같은 경우는, 스킬 오브젝트 시스템에서 바로 타겟에게 버프 넣어주면 되겠다. 굳이 데미지버프만들기~ 이러지 않고.
             */
            /* 데미지 버프 */
            float skillDefaultDamage = currentLevelSkillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
            ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, userCondition);

            /* 상태 버프 */
            ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
            ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);


            BuffAction skillObjBuff = new BuffAction();
            skillObjBuff.unitID = newEntityID;
            skillObjBuff.skillUserID = skillUser.entityID;

            skillObjBuff.remainTime = 1f;   // 기준 ;
            skillObjBuff.coolTime = 1f;
            skillObjBuff.remainCoolTime = 0f;

            skillObjBuff.floatParam.add(damageParam);
            skillObjBuff.boolParam.add(moveDebuff);
            skillObjBuff.boolParam.add(attackDebuff);

            /***********************************************************************************************************/


            /* SkillObject Component */
            SkillObjectComponent skillObjectComponent
                    = new SkillObjectComponent(createdSkillType, skillAreaType, skillUser.entityID, skillObjectDurationTime, skillObjectRadius,
                    -1f, -1f, startPosition, event.skillDirection, skillDistanceRate, skillObjBuff);

            positionComponent = new PositionComponent((Vector3) startPosition.clone());

            /* SkillObject Entity */
            SkillObjectEntity skillObjectEntity
                    = new SkillObjectEntity(positionComponent, skillObjectComponent);
            skillObjectEntity.entityID = newEntityID;




            /**  생성요청 큐에 넣기  */
            worldMap.requestCreateQueue.add(skillObjectEntity);

            System.out.println("스킬 오브젝트 생성 요청함 : " + worldMap.requestCreateQueue.size());

            /** 스킬 쿨타임 및 시전자쪽에 필요한 처리 */    // 뭔가 명확하게 정리가 잘 안되네

            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            System.out.println("스킬 쿨타임 초기화: " + skillToUse.remainCoolTime);

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = ( worldMap.tickRate * 0.01f);
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();


            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;

            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            System.out.println("스킬 시전자 상태 업데이트 함. ");

            // 중계가 필요하다면 중계처리를 하고.
            //공격모션 중계
            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.ARCHER_ARROW_RAIN;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);
        }


    }

    /** 마법사 스킬 처리 함수들 2019 12 04 새벽 */

    /**
     * 마법사가 파이어볼 스킬을 사용했을 때 처리를 담당한다.
     * @param worldMap
     * @param event
     */
    public static void useSkill_magicianFireball(WorldMap worldMap, ActionUseSkill event) {

        /** 스킬 시전자 및 시전 스킬 정보 */
        CharacterEntity skillUser = worldMap.characterEntity.get((event.userEntityID));
        ConditionComponent userCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        System.out.println("유저 찾음");


        /** 시전자 스킬슬롯에서, 시전하려는 스킬을 담고있는 슬롯을 찾는다 */
        SkillSlot skillToUse = null;    // 찾으려는 스킬을 담을 슬롯.
        int slotNum = event.skillSlotNum;
        int skillType = -1; // 초기값. 일치하는 스킬을 찾으면, 0 이상의 값을 가질 것.

        SkillSlot slot = null;  // 아래 올바른 스킬 슬롯을 찾기 위해 사용하는 임시 슬롯.
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){

            System.out.println("스킬 찾는중...");

            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == event.skillSlotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;
                break;
            }
        }

        System.out.println("스킬 검색함.");

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);

        System.out.println("스킬 정보 : " + skillInfo.skillName);



        /** 스킬시전 가능여부 판별 및 처리 */
        boolean isAlbeToUSeSkill = false;
        boolean hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;

        isAlbeToUSeSkill = ((!userCondition.isDisableSkill) && hasEnoughMp && isCoolTimeZero) ? true : false;


        if(isAlbeToUSeSkill){   /* 스킬 시전이 가능하다 */

            System.out.println("스킬 시전 가능.");

            /** 투사체 및 오브젝트 등을 생성하기 위해 필요한 데이터 계산 */

            /* event */
            Vector3 skillDirection = event.skillDirection;  // 여기서도 필요한지 모르겠네.. 일단은 받는걸로?
            //Vector3 skillDirection = new Vector3(event.skillDirection.x(), 0, event.skillDirection.y());  // 여기서도 필요한지 모르겠네.. 일단은 받는걸로?


            /* Entity ID */
            int publishedEntityID;  // 오브젝트 생성 후 부여받을 값을 담을 것임.
            publishedEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

            /* Position Component */
            PositionComponent positionComponent;    // 오브젝트의 위치값을 담을 변수. 오브젝트가 생성될 위치(= 스킬 시전자의 위치) 로 초기화.
            positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());
            positionComponent.position.y(2.2f);

            /* FlyingObject Component */
            int createdSkillType = skillInfo.skillType;
            int userEintityID = skillUser.entityID;
            float flyingSpeed = skillInfo.flyingObjectInfo.flyingObjectSpeed;

            /** 2019 12 27 */
            flyingSpeed = currentLevelSkillInfo.flyingObjectSpeed;

            float flyingObjectRadius = skillInfo.flyingObjectInfo.flyingObjectRadius;

            Vector3 startPosition = (Vector3) positionComponent.position.clone();
            Vector3 direction = skillDirection;

            // float flyingObjectRemainDistance = -1f; // 일단, 파이어볼은 타게팅 스킬이라. 얘를 계산해 줄 필요가 없음.

            /* BuffAction */
            BuffAction buffAction = (BuffAction) skillInfo.flyingObjectInfo.buffAction.clone();
            buffAction.floatParam = new ArrayList<>();
            buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));

            /**
             * 2019 12 23 월요일
             *      수정함
             * 2020 02 06 목요일
             *      아래에 추가한 버프 내용이 잘 동작한다면, 아래 buffAction은 지울 것.
             */
            buffAction.unitID = publishedEntityID;
            buffAction.skillUserID = skillUser.entityID;

            boolean beDestroyedByCrash = true;  // 타게팅 스킬 대상으로는 필요없는 값일지도? 아직 잘 모르겠음. 나중에 로직이 추가될 수도 있지. 투사체의 생명주기에 대해.


            /** 타겟을 지정한다 */
            /** 공격 범위 내에서 가장 가까운 타겟을 찾는다 */

            System.out.println("타겟을 검색함");

            float minDistance = currentLevelSkillInfo.attackRange;
            int targetID = -1;

            targetID = event.targetEntityID;


            /**
             * 2020 02 06
             * [ 버프 ]
             * -- 데미지 버프 ;
             *      얘는...
             *
             *      나중에 스킬 쪽을 바꾸더라도, 투사체 시스템에서는 투사체에 들어있는 버프액션을 가지고
             *      createDamageBuff 매서드를 호출하는 걸로??
             *
             */
            /* 데미지 버프 */
            float skillDefaultDamage = currentLevelSkillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
            ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, userCondition);

            /* 상태 버프 */
            ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
            ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);


            BuffAction flyingObjBuff = new BuffAction();
            flyingObjBuff.unitID =publishedEntityID;
            flyingObjBuff.skillUserID = skillUser.entityID;

            flyingObjBuff.remainTime = 1f;  // 기준 ; 상태버프.
            flyingObjBuff.coolTime = -1f;
            flyingObjBuff.remainCoolTime = -1f;

            flyingObjBuff.floatParam.add(damageParam);
            flyingObjBuff.boolParam.add(moveDebuff);
            flyingObjBuff.boolParam.add(attackDebuff);

            /***********************************************************************************************************/


            /** 오브젝트 생성 */
            /* Component */
            FlyingObjectComponent flyingObjectComponent
                    = new FlyingObjectComponent(createdSkillType, userEintityID, flyingSpeed, flyingObjectRadius,
                    startPosition, direction, -1f, flyingObjBuff, targetID);
            flyingObjectComponent.beDestroyedByCrash = beDestroyedByCrash;

            /* Entity */
            FlyingObjectEntity flyingObjectEntity
                    = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
            flyingObjectEntity.entityID = publishedEntityID;

            System.out.println("투사체 생성함");


            /** 생성요청 큐에 넣기 */
            worldMap.requestCreateQueue.add(flyingObjectEntity);

            System.out.println("생성 요청 큐에 넣었고");

            /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.skillUserID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = (worldMap.tickRate * 0.01f);
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();


            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;

            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            //공격모션 중계
            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.MAGICIAN_FIREBALL;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);

        }

    }

    /**
     * 마법사가 힐링 스킬을 사용했을 때 처리를 담당한다.
     * @param worldMap
     * @param event
     */
    public static void useSkill_magicianHeal(WorldMap worldMap, ActionUseSkill event) {

        /** 스킬 시전자 및 시전 스킬 정보 */
        CharacterEntity skillUser = worldMap.characterEntity.get((event.userEntityID));
        ConditionComponent userCondition = skillUser.conditionComponent;

        /** 스킬시전 가능여부 판별 및 처리 */
        /** 시전자 스킬슬롯에서, 시전하려는 스킬을 담고있는 슬롯을 찾는다 */
        SkillSlot skillToUse = null;    // 찾으려는 스킬을 담을 슬롯.
        int slotNum = event.skillSlotNum;
        int skillType = -1; // 초기값. 일치하는 스킬을 찾으면, 0 이상의 값을 가질 것.

        SkillSlot slot = null;  // 아래 올바른 스킬 슬롯을 찾기 위해 사용하는 임시 슬롯.
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){

            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == slotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;
                break;
            }
        }

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);


        ArrayList<Integer> targetList = new ArrayList<>();

        /** 스킬시전 가능여부 판별 및 처리 */
        boolean isAlbeToUSeSkill = false;
        boolean hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;

        isAlbeToUSeSkill = ((!userCondition.isDisableSkill) && hasEnoughMp && isCoolTimeZero) ? true : false;


        if(isAlbeToUSeSkill) {   /* 스킬 시전이 가능하다 */

            /** 투사체 및 오브젝트 등을 생성하기 위해 필요한 데이터 계산 */

            /* event */
            Vector3 skillDirection = event.skillDirection;  // 여기서도 필요한지 모르겠네.. 일단은 받는걸로?
            float skillDistanceRate = event.skillDistanceRate;

            /* Entity ID */
            int publishedEntityID;  // 오브젝트 생성 후 부여받을 값을 담을 것임.
            publishedEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

            /* Position Component */
            PositionComponent positionComponent;    // 오브젝트의 위치값을 담을 변수. 오브젝트가 생성될 위치(= 스킬 시전자의 위치) 로 초기화.
            positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());


            /* SKillObject Component */
            int createdSkillType = skillInfo.skillType;
            int skillAreaType = skillInfo.skillObjectInfo.skillAreaType;
            int userEintityID = skillUser.entityID;
            float skillObjectDurationTIme = skillInfo.skillObjectInfo.skillObjectDurationTime;

            skillObjectDurationTIme = 3f;

            float skillObjectRadius = skillInfo.skillObjectInfo.skillObjectRadius;

            /** 2019 12 27 */
            skillObjectRadius = currentLevelSkillInfo.range;

            Vector3 startPosition = (Vector3) positionComponent.position.clone();
            Vector3 direction = skillDirection;


            /* BuffAction */
            /*BuffAction buffAction = (BuffAction) skillInfo.skillObjectInfo.buffAction.clone();
            buffAction.floatParam = new ArrayList<>();
            buffAction.floatParam.add(new ConditionFloatParam(ConditionType.hpRecoveryAmount, currentLevelSkillInfo.attackDamage));
            buffAction.unitID = publishedEntityID;*/
            BuffAction buffAction = (BuffAction) skillInfo.buffAction.clone();
            buffAction.floatParam = new ArrayList<>();
            buffAction.floatParam.add(new ConditionFloatParam(ConditionType.hpRecoveryAmount, currentLevelSkillInfo.attackDamage));
            buffAction.unitID = publishedEntityID;
            buffAction.skillUserID = skillUser.entityID;

            /** 2020 03 12 */
            buffAction.skillType = SkillType.MAGICIAN_HEAL;
            buffAction.buffDurationTime = buffAction.remainTime;


            /** 힐 스킬 범위 판정 및 효과 부여? **/
            /* 버프대상(캐릭터) 갯수만큼 반복한다 */
            for(HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()){

                CharacterEntity character = characterEntity.getValue();

                /** 대상의 거리를 계산한다 */
                float currentDistance = 0f;

                Vector3 currentPos = skillUser.positionComponent.position;
                Vector3 charPos = character.positionComponent.position;

                currentDistance = Vector3.distance(currentPos, charPos);

                /** 대상이 버프 범위에 있는지 판별한다 */
                boolean isInTargetRange = false;

                float targetHP = character.hpComponent.currentHP;
                if( (currentDistance < skillObjectRadius) && targetHP > 0) {
                    isInTargetRange = true;

                }

                /** 대상이 버프 적용 범위에 위치한다면 */
                if(isInTargetRange) {

                    /**
                     * 버프의 타겟 목록 배열을 list로 대체함.
                     */
                    /* 버프적용 타겟으로 추가한다 */
                    // buff.targetIDList[buff.targetIDList.length] = character.entityID;
                    targetList.add(character.entityID);

                }

            }


            /* 버프 타겟 갯수만큼 반복한다 */
            for(int j=0; j<targetList.size(); j++){

                /* 버프 대상 */
                int targetID = targetList.get(j);
                CharacterEntity target = worldMap.characterEntity.get(targetID);

                /** 대상이 이미 자신의 버프를 받고 있는가?를 검사한다 */
                List<BuffAction> buffActionList = target.buffActionHistoryComponent.conditionHistory;
                boolean targetHasBuffAlready = false;

                BuffAction targetsBuffAction = new BuffAction();    // 일단 안씀.

                /* 대상의 버프액션 갯수만큼 반복한다 */
                /*for(int k=0; k<buffActionList.size(); k++){

                    if(buffTurret.entityID == buffActionList.get(k).unitID){

                        targetsBuffAction = buffActionList.get(k);

                        targetHasBuffAlready = true;
                        break;
                    }
                }*/

                if(targetHasBuffAlready) { /** 대상이 이미 효과를 받고 있다면 */

                    // buffInfo.remainTime = deltaTime;    // 버프 지속 남은 시간을 초기화해준다
                    targetsBuffAction.remainTime = worldMap.tickRate * 0.001f;

                }
                else{   /** 기존에 효과를 받고있지 않다면 */

                    /* 대상의 버프 목록에 추가해준다. */
                    BuffAction newBuff = (BuffAction) buffAction.clone();
                    buffActionList.add(newBuff);

                }
            }


            /**
             * 2020 02 06 목
             * 미치것네.. 즉발이라 위에서 버프처리해주고 끝날거면
             * 뭐하러 오브젝트 생성하지??
             * ㄴ 아.. 혹시. 클라에서 장판깔게 하려고 그러는건가.....................
             *  .......그래도.. 그러면.. 아니 클라 모양이나 효과를 위해 껍데기 객체를 만든다는 게 좀 많이 이상한데..
             *  이왕 만들어준거, 생성된 스킬 오브젝트가 효과 넣어주는 처리 하게 해야하는거 아녀?? ; ;
             *  '스킬 처리'의 영역이 참 모호하네.....
             *  일단 최대한 안건들고 싶으니까 ㅋㅋ 별 문제 일어나지 않는 이상
             *  이쪽은 걍 놔두자.
             *
             */
            /** 오브젝트 생성 */
            /* Component */
            SkillObjectComponent skillObjectComponent
                    = new SkillObjectComponent(createdSkillType, skillAreaType, skillUser.entityID, skillObjectDurationTIme, skillObjectRadius,
                    -1f, -1f, startPosition, event.skillDirection, skillDistanceRate, new BuffAction());

            positionComponent = new PositionComponent((Vector3) startPosition.clone());

            /* Entity */
            SkillObjectEntity skillObjectEntity
                    = new SkillObjectEntity(positionComponent, skillObjectComponent);
            skillObjectEntity.entityID = publishedEntityID;


            /** 생성요청 큐에 넣기 */
            worldMap.requestCreateQueue.add(skillObjectEntity);


            /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */
            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.skillUserID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = (worldMap.tickRate * 0.01f);
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();



            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;

            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            //공격모션 중계
            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.MAGICIAN_HEAL;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);

        }

    }

    /**
     * 마법사가 메테오 스킬을 사용했을 때 처리를 담당한다.
     * @param worldMap
     * @param event
     */
    public static void useSkill_magicianMeteor(WorldMap worldMap, ActionUseSkill event){

        //** 스킬 시전자 및 시전 스킬 정보 */
        CharacterEntity skillUser = worldMap.characterEntity.get((event.userEntityID));
        ConditionComponent userCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /** 시전자 스킬슬롯에서, 시전하려는 스킬을 담고있는 슬롯을 찾는다 */
        SkillSlot skillToUse = null;    // 찾으려는 스킬을 담을 슬롯.
        int slotNum = event.skillSlotNum;
        int skillType = -1; // 초기값. 일치하는 스킬을 찾으면, 0 이상의 값을 가질 것.

        SkillSlot slot = null;  // 아래 올바른 스킬 슬롯을 찾기 위해 사용하는 임시 슬롯.
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){

            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == slotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;
                break;
            }
        }

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);


        ArrayList<Integer> targetList = new ArrayList<>();

        /** 스킬시전 가능여부 판별 및 처리 */
        boolean isAlbeToUSeSkill = false;
        boolean hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;

        isAlbeToUSeSkill = ((!userCondition.isDisableSkill) && hasEnoughMp && isCoolTimeZero) ? true : false;


        if(isAlbeToUSeSkill) {   /* 스킬 시전이 가능하다 */

            /** 투사체 및 오브젝트 등을 생성하기 위해 필요한 데이터 계산 */

            /* event */
            Vector3 skillDirection = event.skillDirection;  // 여기서도 필요한지 모르겠네.. 일단은 받는걸로?
            float skillDistanceRate = event.skillDistanceRate;

            /* Entity ID */
            int publishedEntityID;  // 오브젝트 생성 후 부여받을 값을 담을 것임.
            publishedEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

            /* Position Component */
            PositionComponent positionComponent;    // 오브젝트의 위치값을 담을 변수. 오브젝트가 생성될 위치로 초기화.
            positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());


            /* SKillObject Component */
            int createdSkillType = skillInfo.skillType;
            int skillAreaType = skillInfo.skillObjectInfo.skillAreaType;
            int userEintityID = skillUser.entityID;
            float skillObjectDurationTIme = skillInfo.skillObjectInfo.skillObjectDurationTime;
            float skillObjectRadius = skillInfo.skillObjectInfo.skillObjectRadius;

            /** 2019 12 27 */
            skillObjectDurationTIme = currentLevelSkillInfo.durationTime + 2.2f;
            skillObjectRadius = currentLevelSkillInfo.attackRange;

            Vector3 startPosition;
            Vector3 direction;

            /* 장판이 펼쳐질 위치 구하기 */
            startPosition = (Vector3) positionComponent.position.clone();
            direction = skillDirection;

            /* 장판 위치 계산하기 */
            direction.setSpeed(skillDistanceRate * currentLevelSkillInfo.range);

            System.out.println("스킬 거리 비율 : " + skillDistanceRate);
            System.out.println("스킬 장판 시작 위치 : " + startPosition.x() + ", " + startPosition.y() + ", " + startPosition.z() );
            System.out.println("스킬 방향 스피드 : " + skillDistanceRate * currentLevelSkillInfo.range);
            System.out.println("스킬 정보 - range : " + currentLevelSkillInfo.range);

            startPosition.movePosition(startPosition, direction);

            System.out.println("스킬 장판 시작 위치(연산 후) : " + startPosition.x() + ", " + startPosition.y() + ", " + startPosition.z() );


            /* Skill Object BuffAction */
            BuffAction buffAction = (BuffAction) skillInfo.skillObjectInfo.buffAction.clone();
            //buffAction.coolTime = 1f;
            //buffAction.remainCoolTime = 1f;
            //buffAction.remainTime = 5f;
            buffAction.floatParam = new ArrayList<>();
            buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));
            buffAction.unitID = publishedEntityID;
            buffAction.skillUserID = skillUser.entityID;


            /**
             * 2020 02 06 목
             */
            /* 데미지 버프 */
            float skillDefaultDamage = currentLevelSkillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
            ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, userCondition);

            /* 상태 버프 */
            ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
            ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);


            BuffAction skillObjBuff = new BuffAction();
            skillObjBuff.unitID = publishedEntityID;
            skillObjBuff.skillUserID = skillUser.entityID;

            //skillObjBuff.remainTime = currentLevelSkillInfo.durationTime;   // readInfo에는 3이라 돼있는데.. 왜지?? 3초동안 못움직이게 하기인가?? ;
            skillObjBuff.remainTime = 1f;
            skillObjBuff.coolTime = 1f;
            skillObjBuff.remainCoolTime = 0f;

            skillObjBuff.floatParam.add(damageParam);
            skillObjBuff.boolParam.add(moveDebuff);
            skillObjBuff.boolParam.add(attackDebuff);



            /** 오브젝트 생성 */
            /* Component */
            SkillObjectComponent skillObjectComponent
                    = new SkillObjectComponent(createdSkillType, skillAreaType, skillUser.entityID, skillObjectDurationTIme, skillObjectRadius,
                    -1f, -1f, startPosition, event.skillDirection, skillDistanceRate, skillObjBuff);

            /* Entity */
            SkillObjectEntity skillObjectEntity
                    = new SkillObjectEntity(positionComponent, skillObjectComponent);
            skillObjectEntity.entityID = publishedEntityID;
            skillObjectEntity.positionComponent.position = startPosition;


            /** 생성요청 큐에 넣기 */
            worldMap.requestCreateQueue.add(skillObjectEntity);


            /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */
            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.skillUserID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = (worldMap.tickRate * 0.01f);
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();


            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;


            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            //공격모션 중계
            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.MAGICIAN_METEOR;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);

        }
    }


    /** 전사 스킬 처리 매서드 영역 */

    /**
     *  전사가 CUT(베기) 스킬을 사용
     */
    public static void useSKill_knightCut(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 시전자 및 시전 스킬 정보 */

        CharacterEntity skillUser = worldMap.characterEntity.get((event.userEntityID));
        ConditionComponent userCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /** 시전자 스킬슬롯에서, 시전하려는 스킬을 담고있는 슬롯을 찾는다 */
        SkillSlot skillToUse = null;    // 찾으려는 스킬을 담을 슬롯.
        int slotNum = event.skillSlotNum;
        int skillType = -1; // 초기값. 일치하는 스킬을 찾으면, 0 이상의 값을 가질 것.

        SkillSlot slot = null;  // 아래 올바른 스킬 슬롯을 찾기 위해 사용하는 임시 슬롯.
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){

            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == slotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;

                System.out.println("스킬 슬롯 찾음 : " + skillToUse.skillinfo.skillName + ", " + skillToUse.slotNum);
                break;
            }
        }

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);


        /** 스킬 시전 가능 여부를 판별한다 */

        boolean isAbleToUseSkill = false;
        boolean hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;

        isAbleToUseSkill = ((!userCondition.isDisableSkill) && isCoolTimeZero) ? true : false;

        //System.out.println("");



        /** 판별 결과에 따른 처리 */
        if(isAbleToUseSkill){   /* 스킬 시전이 가능하다면 */

            System.out.println("스킬을 시전합니다.");

            /* 타겟 목록 */
            ArrayList<Integer> targetListByDistance = new ArrayList<>();    // 1차로 공격 사거리 내 타겟들을 검색한 결과
            ArrayList<Integer> targetList = new ArrayList<>();  // 위 결과를 토대로, 각도범위 판정을 하여 최종적으로 스킬을 적용할 대상들을 거른다.

            /* event */
            Vector3 skillDirection = event.skillDirection;  // 여기서도 필요한지 모르겠네.. 일단은 받는걸로?
            float skillDistanceRate = event.skillDistanceRate;

            System.out.println("스킬 방향 받아온 값 : "
                    + skillDirection.x() + ", "+ skillDirection.y() + ", "+ skillDirection.z());
            System.out.println("스킬 거리비율 받아온 값 : "
                    + skillDistanceRate);

            /* Entity ID */
            int publishedEntityID;  // 오브젝트 생성 후 부여받을 값을 담을 것임.
            publishedEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();
            System.out.println("생성될 스킬 오브젝트의 ID : "
                    + publishedEntityID);

            /******************************  타겟 판별 및 스킬 적용 ***************************************************/
            /** 스킬 범위(거리 기준)에 들어가는 타겟을 찾는다 */   // 기존과 동일함

            System.out.println("거리 타겟을 판별합니다.");

            /* 몬스터 갯수만큼 반복한다 */
            for(HashMap.Entry<Integer, MonsterEntity> MonsterEntity : worldMap.monsterEntity.entrySet()){

                MonsterEntity monster = MonsterEntity.getValue();

                /** 대상과의 거리를 계산한다 */
                float currentDistance = 0f;

                Vector3 userPos = (Vector3) skillUser.positionComponent.position.clone();
                Vector3 targetPos = (Vector3) monster.positionComponent.position.clone();

                System.out.println("유저의 좌표 : "
                        + userPos.x() + ", "+ userPos.y() + ", "+ userPos.z());
                System.out.println("타겟의 좌표 : "
                        + targetPos.x() + ", "+ targetPos.y() + ", "+ targetPos.z());

                currentDistance = Vector3.distance(userPos, targetPos);

                System.out.println("유저와 타겟의 거리 : " + currentDistance);

                /** 대상이 스킬범위에 있는지 판별한다 */
                boolean isTargetInRange = false;

                float targetHP = monster.hpComponent.currentHP;
                float skillRange = currentLevelSkillInfo.attackRange;
                if( (currentDistance < skillRange) && (targetHP > 0)){
                    isTargetInRange = true;
                }

                /** 대상이 스킬 범위에 존재한다면, 타겟 목록에 추가한다*/
                if(isTargetInRange){

                    System.out.println("대상" + monster.entityID + "가 범위거리 내 존재합니다. ");
                    targetListByDistance.add(monster.entityID);
                }

            }


            /** 공격 범위(반원)에 들어가는 타겟을 찾는다. */

            System.out.println("공격 각도에 들어가는 타겟을 찾습니다.");

            for(int i=0; i<targetListByDistance.size(); i++){

                // 유저가 바라보는 방향과 대상 사이의 각을 구한다.
                // if 그 각도가 -90 ~ 90 사이의 각이라면,
                // 최종 타겟 목록에 추가한다.

                // 타겟
                MonsterEntity monster = worldMap.monsterEntity.get(targetListByDistance.get(i));

                // 유저가 바라보는 방향 백터 구하기
                Vector3 userDirection = (Vector3) skillDirection.clone();

                Vector3 monsterDirection = Vector3.getTargetDirection(skillUser.positionComponent.position , monster.positionComponent.position);

                float betweenAngle = Vector3.getAngle(userDirection, monsterDirection);
                System.out.println("유저와 대상의 사이각 : " + betweenAngle);

                boolean isTargetInRange = false;
                if((betweenAngle <= 90f)){

                    System.out.println("대상" + monster.entityID + "가 범위각도 내에 존재합니다.");
                    isTargetInRange = true;
                }

                if(isTargetInRange){
                    System.out.println("대상" + monster.entityID + "을 최종 타겟 목록에 추가합니다.");
                    targetList.add(monster.entityID);
                }
            }


            /** 최종 타겟들에 대해 공격 처리를 한다 */

            /**
             * 2002 02 06 목
             * 상태이상 버프는 그대로 두고, 상태 조절이 필요할 때 조작하는걸로,
             * 일단은 데미지 부분만. 데미지 버프 생성하는 매서드 호출해서 처리하기...
             * ㄴ 스킬정보에 들어있는 버퍼를 복사해서 사용하고 있는데, 일단 거기에는
             *      지속시간 1초, 쿨타임 1초, 남은 쿨타임 0.25초로 되어있다.
             *      0.25 ->> 0.15 가 되는게 크게 문제가 될지는 모르겠지만.
             *      일단은 0.15가 되도록 하는걸로??
             */

            /* 버프1 : 데미지 ; 즉발 */
            /*BuffAction damageBuff = (BuffAction) skillToUse.skillinfo.buffAction.clone();
            damageBuff.floatParam.get(0).value = currentLevelSkillInfo.attackDamage;
            damageBuff.unitID = publishedEntityID;
            damageBuff.skillUserID = skillUser.entityID;*/

            float skillDamage = currentLevelSkillInfo.attackDamage;
            ConditionFloatParam damageParam = createDamageParam(skillDamage, userAttack, userCondition);
            BuffAction damageBuff = createDamageBuff(damageParam, publishedEntityID, skillUser.entityID);


            /* 버프2 : 상태이상(못움직임 등) ; 한.. 1초간 유지되도록?? */
            BuffAction conditionDebuf = new BuffAction();
            conditionDebuf.remainCoolTime = 0f;
            conditionDebuf.remainTime = 0.5f;
            conditionDebuf.coolTime = 0f;
            conditionDebuf.unitID = publishedEntityID;
            conditionDebuf.skillUserID = skillUser.entityID;
            conditionDebuf.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            conditionDebuf.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            conditionDebuf.boolParam.add(new ConditionBoolParam(ConditionType.isUnTargetable, true));


            for(int i=0; i<targetList.size(); i++){

                MonsterEntity monster = worldMap.monsterEntity.get(targetList.get(i));

                monster.buffActionHistoryComponent.conditionHistory.add((BuffAction) damageBuff.clone());
                monster.buffActionHistoryComponent.conditionHistory.add((BuffAction) conditionDebuf.clone());

            }

            /**********************************************************************************************************/


            /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */
            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.skillUserID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = (worldMap.tickRate * 0.01f) ;
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();



            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;

            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            //공격모션 중계
            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.KNIGHT_CUT;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);
        }
        else{
            /* 스킬 시전이 불가능한 상태. */


        }

    }

    /**
     *  전사가 PIERCE(찌르기) 스킬을 사용
     */
    public static void useSKill_knightPierce(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 시전자 및 시전 스킬 정보 */

        CharacterEntity skillUser = worldMap.characterEntity.get((event.userEntityID));
        ConditionComponent userCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /** 시전자 스킬슬롯에서, 시전하려는 스킬을 담고있는 슬롯을 찾는다 */
        SkillSlot skillToUse = null;    // 찾으려는 스킬을 담을 슬롯.
        int slotNum = event.skillSlotNum;
        int skillType = -1; // 초기값. 일치하는 스킬을 찾으면, 0 이상의 값을 가질 것.

        SkillSlot slot = null;  // 아래 올바른 스킬 슬롯을 찾기 위해 사용하는 임시 슬롯.
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){

            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == slotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;
                break;
            }
        }

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);


        /** 스킬 시전 가능 여부를 판별한다 */

        boolean isAbleToUseSkill = false;
        boolean hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;

        isAbleToUseSkill = ((!userCondition.isDisableSkill) && isCoolTimeZero) ? true : false;

        /** 판별 결과에 따른 처리 */
        if(isAbleToUseSkill){   /* 스킬 시전이 가능하다면 */

            /* 타겟 목록 */
            ArrayList<Integer> targetList = new ArrayList<>();  // 위 결과를 토대로, 각도범위 판정을 하여 최종적으로 스킬을 적용할 대상들을 거른다.

            /* event */
            Vector3 skillDirection = event.skillDirection;
            float skillDistanceRate = event.skillDistanceRate;

            /* Entity ID */
            int publishedEntityID;  // 오브젝트 생성 후 부여받을 값을 담을 것임.
            publishedEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

            /* Position Component */
            PositionComponent positionComponent;
            Vector3 position;

            /* FlyingObject Component */
            int createdSkillType;   // 투사체를 생선한 스킬 타입. 궁수 파워샷
            int userEntityID;       // 스킬 시전자. 위에 published~랑 같다.
            float flyingSpeed;      // 스킬정보 및 레벨 테이블 등을 참고하면 된다.
            float flyingObjectRadius;   // 스킬정보 및 레벨 테이블 참고

            Vector3 startPosition;  // 위 position component와 같다고 봐도 될 듯?
            Vector3 direction;      // 클라 event로 받아오는 정보.

            float flyingObjectRemainDistance;   // 최대 사거리까지 남은 거리.. 계산해야 할듯?

            BuffAction buffAction;  // 충돌하는 Entity에게 부여할 버프/데미지/효과

            int targetEntityID;     // 여기서는 없음. 미리 타게팅된 적이 있는가??
            // 이것도. 나중에는... 서버에서 다 계산되도록 해야??

            boolean beDestroyedByCrash;

            /** 투사체 생성 */

            /* position component */
            positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());

            /* flyingObject component */
            createdSkillType = skillInfo.skillType;
            flyingObjectRadius = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel).attackRange;

            float userSpeed = skillUser.velocityComponent.moveSpeed;    // N단위값 들어있음.
            flyingSpeed = userSpeed * 5f;

            startPosition = (Vector3) positionComponent.position.clone();
            direction = skillDirection;

            //flyingObjectRemainDistance = skillInfo.skillRange * skillDistanceRate;
            flyingObjectRemainDistance = currentLevelSkillInfo.range;

            int newEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

            /* buffAction **/
            buffAction = (BuffAction) skillInfo.flyingObjectInfo.buffAction.clone();
            buffAction.remainTime = 0.5f;
            buffAction.remainCoolTime = 0f;
            buffAction.coolTime = 1f;
            buffAction.floatParam = new ArrayList<>();
            buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));

            buffAction.unitID = newEntityID;
            buffAction.skillUserID = skillUser.entityID;


            /**
             * 2020 02 06 목
             * 기존 ; readInfo에서 넣어준 (상태 + 데미지)를 복사해서 한꺼번에 넣어줌
             * >> 이거를 상태, 데미지 각각 분리해서 넣어주기.
             * 아 분리 안되는구나! 얘는 즉발이 아니라 투사체 적중 시 나눠서 처리해 줘야 할 거니까
             * 한 버프에 넣어주긴 해야겠네..
             * 분리하는거는 투사체 시스템에서.
             */

            /* 데미지 버프 */
            float skillDefaultDamage = currentLevelSkillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
            ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, userCondition);

            /* 상태 버프 */
            ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
            ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);


            BuffAction flyingObjBuff = new BuffAction();
            flyingObjBuff.unitID = newEntityID;
            flyingObjBuff.skillUserID = skillUser.entityID;

            flyingObjBuff.remainTime = 3f;  // 얘도 관통 투사체라... 한 번 맞은 애가 또 맞지 않게 해야되는데.
                                            // 일단 시간값은 이렇게 주고, 필요하면 나중에 더 줄이고,
                                            // 그.. 투사체에서 타겟 검색할 때, 각도 판정 처리 혹시 안 돼있으면 하기.
            flyingObjBuff.coolTime = -1f;
            flyingObjBuff.remainCoolTime = -1f;

            flyingObjBuff.floatParam.add(damageParam);
            flyingObjBuff.boolParam.add(moveDebuff);
            flyingObjBuff.boolParam.add(attackDebuff);


            /***********************************************************************************************************/

            /* FlyingObject Component */
            FlyingObjectComponent flyingObjectComponent
                    = new FlyingObjectComponent(createdSkillType, skillUser.entityID, flyingSpeed, flyingObjectRadius,
                    startPosition, direction, flyingObjectRemainDistance, flyingObjBuff, -1);
            flyingObjectComponent.beDestroyedByCrash = false;

            /* FlyingObject Entity */
            FlyingObjectEntity flyingObjectEntity
                    = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
            flyingObjectEntity.entityID = newEntityID;


            /**  생성요청 큐에 넣기  */
            worldMap.requestCreateQueue.add(flyingObjectEntity);
            System.out.println("생성자 큐에 찌르기 투사체 정보 집어넣음");


            /**********************************************************************************************************/


            /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */
            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.skillUserID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = (worldMap.tickRate * 0.02f);
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();


            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;


            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.KNIGHT_PIERCE;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);


        }
        else{
            /* 스킬 시전이 불가능한 상태. */
            System.out.println("스킬 사용이 불가능하다고 판단 ");

        }

    }

    /**
     *  전사가 TORNADO(토네이도) 스킬을 사용
     */
    public static void useSKill_knightTornado(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 시전자 및 시전 스킬 정보 */

        CharacterEntity skillUser = worldMap.characterEntity.get((event.userEntityID));
        ConditionComponent userCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /** 시전자 스킬슬롯에서, 시전하려는 스킬을 담고있는 슬롯을 찾는다 */
        SkillSlot skillToUse = null;    // 찾으려는 스킬을 담을 슬롯.
        int slotNum = event.skillSlotNum;
        int skillType = -1; // 초기값. 일치하는 스킬을 찾으면, 0 이상의 값을 가질 것.

        SkillSlot slot = null;  // 아래 올바른 스킬 슬롯을 찾기 위해 사용하는 임시 슬롯.
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){

            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == slotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;
                break;
            }
        }

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);


        /** 스킬 시전 가능 여부를 판별한다 */

        boolean isAbleToUseSkill = false;
        boolean hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;

        isAbleToUseSkill = ((!userCondition.isDisableSkill) && isCoolTimeZero) ? true : false;

        /** 판별 결과에 따른 처리 */
        if(isAbleToUseSkill){   /* 스킬 시전이 가능하다면 */

            /* event */
            Vector3 skillDirection = event.skillDirection;  // 여기서도 필요한지 모르겠네.. 일단은 받는걸로?
            float skillDistanceRate = event.skillDistanceRate;

            /* EntityID */
            int publishedEntityID;  // FlyingObject Entity생성 후 부여받을 것. 그 전까지는 비우거나 쓰레기값,,

            /* Position Component */
            PositionComponent positionComponent;
            Vector3 position;

            /* SkillObject Component */
            int createdSkillType;   // 투사체를 생선한 스킬 타입.
            int skillAreaType;
            int userEntityID;       // 스킬 시전자. 위에 published~랑 같다.
            float skillObjectDurationTime;      // 스킬정보 및 레벨 테이블 등을 참고하면 된다.
            float skillObjectRadius;   // 스킬정보 및 레벨 테이블 참고

            Vector3 startPosition;
            Vector3 direction;      // 클라 event로 받아오는 정보.

            float distanceRate;

            BuffAction buffAction;  // 충돌하는 Entity에게 부여할 버프/데미지/효과


            /**********************************************************************************************************/

            /* position component */
            //positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());
            positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());


            /* skillObject component */
            createdSkillType = skillInfo.skillType;
            skillAreaType = skillInfo.skillObjectInfo.skillAreaType;
            //skillObjectDurationTime = skillInfo.skillObjectInfo.skillObjectDurationTime;
            skillObjectDurationTime = currentLevelSkillInfo.durationTime;
            //skillObjectRadius = skillInfo.skillObjectInfo.skillObjectRadius;
            skillObjectRadius = currentLevelSkillInfo.attackRange;

            // 장판이 펼쳐질 지점 구해야 함.
            startPosition = (Vector3) positionComponent.position.clone();
            //startPosition.set(startPosition.x(), 2f, startPosition.z());

            System.out.println("시작(시전자 위치) : "
                    + startPosition.x() + ", " + startPosition.y() + ", " + startPosition.z() + ", ");

            direction = skillDirection;


            /* buffAction **/ // 2020 02 06 안씀
            buffAction = (BuffAction) skillInfo.skillObjectInfo.buffAction.clone();
            buffAction.floatParam = new ArrayList<>();
            buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));

            /*==========================================================================================================*/

            int newEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();
            buffAction.unitID = newEntityID;
            buffAction.skillUserID = skillUser.entityID;

            /**
             * 2020 02 06
             *
             */

            /* 데미지 버프 */
            float skillDefaultDamage = currentLevelSkillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
            ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, userCondition);

            /* 상태 버프 */
            //ConditionBoolParam airborneDeBuff = new ConditionBoolParam(ConditionType.isAirborne, true);
            ConditionBoolParam moveDeBuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
            ConditionBoolParam attackDeBuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);


            BuffAction skillObjBuff = new BuffAction();
            skillObjBuff.unitID = newEntityID;
            skillObjBuff.skillUserID = skillUser.entityID;

            // 일단 적당히 써놓고 디버깅하면서 맞추자. 이거는.. 스오 시스템 수정할 때 맞춰야 할듯.
            skillObjBuff.remainTime = skillObjectDurationTime;
            skillObjBuff.coolTime = -1f;
            skillObjBuff.remainCoolTime = -1f;

            skillObjBuff.floatParam.add(damageParam);
            //skillObjBuff.boolParam.add(airborneDeBuff);
            skillObjBuff.boolParam.add(moveDeBuff);
            skillObjBuff.boolParam.add(attackDeBuff);



            /********************************************************************************************************** */


            /* SkillObject Component */
            SkillObjectComponent skillObjectComponent
                    = new SkillObjectComponent(createdSkillType, skillAreaType, newEntityID,skillObjectDurationTime, skillObjectRadius,
                    -1f, -1f, startPosition, event.skillDirection, skillDistanceRate, skillObjBuff);

            positionComponent = new PositionComponent((Vector3) startPosition.clone());

            /* SkillObject Entity */
            SkillObjectEntity skillObjectEntity
                    = new SkillObjectEntity(positionComponent, skillObjectComponent);
            skillObjectEntity.entityID = newEntityID;




            /**  생성요청 큐에 넣기  */
            worldMap.requestCreateQueue.add(skillObjectEntity);


            /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */
            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.skillUserID = skillUser.entityID;
            //userBuffAfterSkillUse.remainTime = (worldMap.tickRate * 0.03f);
            userBuffAfterSkillUse.remainTime = (skillObjectDurationTime);
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();



            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;


            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.KNIGHT_TORNADO;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);


        }
        else{
            /* 스킬 시전이 불가능한 상태. */

        }
    }

    /** 궁수 멀티샷 두 번째 버전 */
    public static void useSkill_archerMultiShot2(WorldMap worldMap, ActionUseSkill event){
        /* 스킬 시전자 및 시전 스킬 정보 */
        CharacterEntity skillUser =  worldMap.characterEntity.get(event.userEntityID);
        ConditionComponent userCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        SkillSlot skillToUse = null;
        SkillSlot slot = null;

        int slotNum = event.skillSlotNum;
        int skillType = -1;
        for(int i=0; i<skillUser.skillSlotComponent.skillSlotList.size(); i++){
            slot = skillUser.skillSlotComponent.skillSlotList.get(i);
            if(slot.slotNum == event.skillSlotNum){
                skillType = slot.skillinfo.skillType;
                skillToUse = slot;
                break;
            }
        }

        SkillInfo skillInfo = skillToUse.skillinfo;
        SkillInfoPerLevel currentLevelSkillInfo = skillLevelTable.get(skillInfo.skillType).get(skillToUse.skillLevel);


        /** 시전자가 스킬을 사용할 수 있는 상태인지 판별한다 */

        boolean isAbleToUseSkill = false;
        boolean hasEnoughMp = false;
        boolean isCoolTimeZero = false;

        hasEnoughMp = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        isAbleToUseSkill = ((!userCondition.isDisableSkill) && hasEnoughMp && isCoolTimeZero) ? true : false;  // 일단은 이거밖에 안떠오르네..

        if(isAbleToUseSkill) {  /* 스킬을 사용할 수 있는 상태라면 */

            /** 투사체 및 오브젝트 등을 생성하기 위해 필요한 데이터 계산 */

            /** event에서 받아온 데이터들 꺼내기 */

            Vector3 skillDirection  = event.skillDirection;     // 필요!
            float skillDistanceRate = event.skillDistanceRate;    // 필요!


            /** 투사체 생성에 필요한 정보들 꺼내기 & 활용하기 */

            /* EntityID */
            int publishedEntityID;  // FlyingObject Entity생성 후 부여받을 것. 그 전까지는 비우거나 쓰레기값,,

            /* Position Component */
            PositionComponent positionComponent;
            Vector3 position;        // 일단. 유저..의 위치로 둬도 괜찮을듯??

            /* FlyingObject Component */
            int createdSkillType;   // 투사체를 생선한 스킬 타입. 궁수 파워샷
            int userEntityID;       // 스킬 시전자. 위에 published~랑 같다.
            float flyingSpeed;      // 스킬정보 및 레벨 테이블 등을 참고하면 된다.
            float flyingObjectRadius;   // 스킬정보 및 레벨 테이블 참고

            Vector3 startPosition;  // 위 position component와 같다고 봐도 될 듯?
            Vector3 direction;      // 클라 event로 받아오는 정보.

            float flyingObjectRemainDistance;   // 최대 사거리까지 남은 거리.. 계산해야 할듯?

            BuffAction buffAction;  // 충돌하는 Entity에게 부여할 버프/데미지/효과

            int targetEntityID;     // 여기서는 없음. 미리 타게팅된 적이 있는가??
            // 이것도. 나중에는... 서버에서 다 계산되도록 해야??

            boolean beDestroyedByCrash;

            /*==========================================================================================================*/

            ArrayList<Double> directions = new ArrayList<>();
            int beCreatedFlyingObjectCount = 7;

            directions.add(30d);
            directions.add(20d);
            directions.add(10d);
            directions.add(0d);
            directions.add(-10d);
            directions.add(-20d);
            directions.add(-30d);

            /* 생성될 투사체 갯수만큼 반복한다 */
            System.out.println("멀티 샷 생성할 투사체 갯수 : " + beCreatedFlyingObjectCount);
            for(int i=0; i<beCreatedFlyingObjectCount; i++){

                /* position component */
                positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());
                positionComponent.position.y(2.2f);

                /* flyingObject component */
                createdSkillType = skillInfo.skillType;
                flyingSpeed = skillInfo.flyingObjectInfo.flyingObjectSpeed;
                flyingObjectRadius = skillInfo.flyingObjectInfo.flyingObjectRadius;

                startPosition = (Vector3) event.skillDirection.clone();
                direction = Vector3.rotateVector3ByAngleAxis(startPosition, new Vector3(0,1,0), directions.get(i)).normalize();

                //flyingObjectRemainDistance = skillInfo.skillRange * skillDistanceRate;
                flyingObjectRemainDistance = currentLevelSkillInfo.range * skillDistanceRate;


                int newEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

                /* buffAction **/
                buffAction = (BuffAction) skillInfo.flyingObjectInfo.buffAction.clone();
                buffAction.floatParam = new ArrayList<>();
                buffAction.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, currentLevelSkillInfo.attackDamage));


                buffAction.unitID = newEntityID;
                buffAction.skillUserID = skillUser.entityID;
                /*==========================================================================================================*/


                /**
                 * 2020 02 06
                 * 위에꺼 버프 안 씀...
                 */
                /* 데미지 버프 */
                float skillDefaultDamage = currentLevelSkillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
                ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, userCondition);

                /* 상태 버프 */
                ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
                ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);


                BuffAction flyingObjBuff = new BuffAction();
                flyingObjBuff.unitID = newEntityID;
                flyingObjBuff.skillUserID = skillUser.entityID;

                flyingObjBuff.remainTime = 3f;  // 상태버프 얼마나 적용시켜 줄건지??
                flyingObjBuff.coolTime = -1f;
                flyingObjBuff.remainCoolTime = -1f;

                flyingObjBuff.floatParam.add(damageParam);
                flyingObjBuff.boolParam.add(moveDebuff);
                flyingObjBuff.boolParam.add(attackDebuff);


                /*******************************************************************************************************/



                /* FlyingObject Component */
                FlyingObjectComponent flyingObjectComponent
                        = new FlyingObjectComponent(createdSkillType, skillUser.entityID, flyingSpeed, flyingObjectRadius,
                        startPosition, direction, flyingObjectRemainDistance, flyingObjBuff, -1);
                flyingObjectComponent.beDestroyedByCrash = true;

                /* FlyingObject Entity */
                FlyingObjectEntity flyingObjectEntity
                        = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
                flyingObjectEntity.entityID = newEntityID;


                /**  생성요청 큐에 넣기  */
                worldMap.requestCreateQueue.add(flyingObjectEntity);


            }

            /** 스킬 쿨타임 및 시전자쪽에 필요한 처리 */    // 뭔가 명확하게 정리가 잘 안되네

            /* 스킬 쿨타임을 초기화한다 */
            skillToUse.remainCoolTime = skillInfoTable.get(skillToUse.skillinfo.skillType).skillCoolTime;

            /* 시전자 상태 업데이트 */
            BuffAction userBuffAfterSkillUse = new BuffAction();
            userBuffAfterSkillUse.unitID = skillUser.entityID;
            userBuffAfterSkillUse.skillUserID = skillUser.entityID;
            userBuffAfterSkillUse.remainTime = ( worldMap.tickRate * 0.02f );
            userBuffAfterSkillUse.remainCoolTime = -1;
            userBuffAfterSkillUse.coolTime = -1;
            userBuffAfterSkillUse.boolParam = new ArrayList<>();
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
            userBuffAfterSkillUse.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));
            userBuffAfterSkillUse.floatParam = new ArrayList<>();



            skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;


            skillUser.buffActionHistoryComponent.conditionHistory.add(userBuffAfterSkillUse);

            // 중계가 필요하다면 중계처리를 하고.
            //공격모션 중계
            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.ARCHER_MULTI_SHOT;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);
        }

    }


    /************************************ 스킬 습득 및 업그레이드 관련 매서드 *****************************************/


    /**
     * 2019 12 27 금요일 새벽 권령희
     * 기능 : 스킬 습득 가능 여부 체크하기
     *
     * 2020 01 23 목요일 새벽 권령희
     *  직업별 스킬이 각각 6개씩 추가됨에 따라, 스킬 습득 가능 검사 조건이 추가되었음.
     *  ableSkillType, ableCharType ; 각각, 슬롯(스킬단계)와 스킬의 일치, 직업과 스킬의 일치를 검사한다.
     */
    public static boolean checkIsPossibleToGetSkill(WorldMap worldMap, ActionGetSkill event){

        boolean isPossible = false;

        /* 이벤트 정보 */
        int userID = event.entityID;
        CharacterEntity user = worldMap.characterEntity.get(userID);
        List<SkillSlot> skillSlots = user.skillSlotComponent.skillSlotList;

        int skillType = event.skillID;
        int slotNum = event.skillSlotNum;


        /* 일치하는 번호의 슬롯이 이미 존재, 채워져있는지 검사 */

        boolean slotIsEmpty = true;

        SkillSlot slot = null;
        for(int i=0; i<skillSlots.size(); i++){

            slot = skillSlots.get(i);
            if(slot.slotNum == slotNum){

                System.out.println("스킬을 이미 배운 상태. 동일한 슬롯번호의 슬롯이존재함");

                slotIsEmpty = false;
                break;
            }
        }


        /* 슬롯번호 및 유저 레벨을 비교, 현 레벨 상 해당 스킬을 배우는 것이 가능한지 여부를 검사 */
        boolean ableLevel = false;

        if(slotIsEmpty){

            int userLevel = user.characterComponent.level;
            System.out.println("유저레벨 : " + userLevel);
            
            switch (slotNum) {

                case 1:
                    if(userLevel >= 1){
                        ableLevel = true;
                    }
                    break;
                case 2:
                    if(userLevel >= 2){
                        ableLevel = true;
                    }
                    break;
                case 3:
                    if(userLevel >= 3){
                        ableLevel = true;
                    }
                    break;
            }

        }
        else{
            System.out.println("스킬슬롯이 비어있지 않음");
        }

        /** 2020 01 23 목 새벽 추가 */

        /* 배우려는 스킬이 직업에 맞는 스킬타입인가? 검사 */

        boolean isAbleCharType = false;
        if(ableLevel){

            int characterType = user.characterComponent.characterType;  // 전사, 마법사, 궁수 등 캐릭터 타입
            switch (characterType) {

                case CharacterType.KNIGHT :
                    isAbleCharType = ((2 <= skillType) && (skillType <= 10)) ? true : false;
                    break;
                case CharacterType.MAGICIAN :
                    isAbleCharType = ((12 <= skillType) && (skillType <= 20)) ? true : false;
                    break;
                case CharacterType.ARCHER :
                    isAbleCharType = ((22 <= skillType) && (skillType <= 30)) ? true : false;
                    break;
                default:
                    System.out.println("직업과 스킬이 일치하지 않습니다");
                    break;
            }

        }
        else{
            System.out.println("스킬습득 가능한 레벨이 아님 ");
        }


        /* 배우려는 스킬이 단계에 맞는 스킬타입인가? 검사  */

        boolean isAbleSkillType = false;
        if(isAbleCharType) {

            int remainder = (skillType % 10);   // 스킬 번호에 대해 나머지 연산 후 결과 값을 담을 것.
                                                // 이 값의 범위를 통해, 스킬 슬롯과 스킬 단계의 일치 여부를 판단 할 것이다.
            switch (slotNum) {

                case 1:
                    isAbleSkillType = ((2 <= remainder) && (remainder < 5)) ? true : false;
                    break;
                case 2:
                    isAbleSkillType = ((5 <= remainder) && (remainder < 8)) ? true : false;
                    break;
                case 3:
                    isAbleSkillType = ( ((8 <= remainder) && (remainder < 11)) || (remainder == 0) ) ? true : false;
                    break;
                default:
                    break;

            }

        }
        else{
            System.out.println("스킬과 직업이 일치하지 않음 ");
        }

        /****************************/

        /* 스킬 포인트가 있는지 검사 */
        boolean enoughSkillPoint = false;
        if(isAbleSkillType){    // << 원래는 ableLevel 이였는데
                                // 2020 01 23 이후, 위에 두 개 조건 검사가 포함되면서  isAbleSkillType 으로 변경됨.

            int userSkillPoint = user.characterComponent.skillPoint;
            enoughSkillPoint = (userSkillPoint >= 1) ? true : false;
        }
        else{
            System.out.println("현재 단계 슬롯에서 습득가능한 스킬이 아님");
        }

        isPossible = (enoughSkillPoint && ableLevel && slotIsEmpty && isAbleCharType && isAbleSkillType) ? true : false;

        return isPossible;

    }

    /**
     * 2019 12 27 금요일 새벽 권령희
     * 스킬 업그레이드 가능 여부 체크하기
     */
    public static boolean checkIsPossibleToUpgradeSkill(WorldMap worldMap, ActionUpgradeSkill event){

        boolean isPossible = false;

        /* 이벤트 정보 */
        int userID = event.entityID;
        CharacterEntity user = worldMap.characterEntity.get(userID);
        List<SkillSlot> skillSlots = user.skillSlotComponent.skillSlotList;

        int slotNum = event.skillSlotNum;


        /* 일치하는 번호의 슬롯을 찾는다 */

        boolean slotIsNotEmpty = false;

        SkillSlot slot = null;
        for(int i=0; i<skillSlots.size(); i++){

            slot = skillSlots.get(i);
            if(slot.slotNum == slotNum){
                slotIsNotEmpty = true;
                break;
            }
        }


        /* 슬롯의 스킬 레벨 및 스킬 타입을 보고, 해당 스킬을 업그레이드하는 것이 가능한지 여부를 검사 */
        boolean ableSkillLevel = false;
        SkillInfo skillInfo = slot.skillinfo;

        if(slotIsNotEmpty){

            int skillLevel = slot.skillLevel;
            ableSkillLevel = ( (1<= skillLevel ) && ( skillLevel < 5) ) ? true : false;

        }


        /* 스킬 포인트가 있는지 검사 */
        boolean enoughSkillPoint = false;
        if(ableSkillLevel){

            int userSkillPoint = user.characterComponent.skillPoint;
            enoughSkillPoint = (userSkillPoint >= 1) ? true : false;

            System.out.println("남은 스킬 포인트 : " + userSkillPoint);
        }

        isPossible = (enoughSkillPoint && ableSkillLevel && slotIsNotEmpty) ? true : false;

        return isPossible;
    }

    /**
     * 스킬 업그레이드 처리
     */
    public static void upgradeSkill(WorldMap worldMap, ActionUpgradeSkill event){

        /* 이벤트 정보 */
        int userID = event.entityID;
        CharacterEntity user = worldMap.characterEntity.get(userID);
        List<SkillSlot> skillSlots = user.skillSlotComponent.skillSlotList;

        int slotNum = event.skillSlotNum;


        /** 슬롯을 찾는다*/   // 가능하다는 판정 하에 불리는거니까.. 절대 못찾을 수가 없겠지??
        SkillSlot slot = null;
        for(int i=0; i<skillSlots.size(); i++){

            slot = skillSlots.get(i);
            if(slot.slotNum == slotNum){
                break;
            }
        }


        /** 업그레이드 처리를 한다 */

        /* 스킬 포인트 감소 */
        user.characterComponent.skillPoint--;

        System.out.println("남은 스킬 포인트 : " + user.characterComponent.skillPoint);

        /* 스킬레벨 증가 */
        slot.skillLevel++;

        /* 스킬정보 테이블 참고해서, 새로운 스킬Info 만들어서 대체시켜버리기 ==>> 안그래도 될듯 */

    }


    /****************** 새로 추가될 스킬들의 정보를 읽어오는 매서드 _ 각 스킬별 매서드 ********************************/
    /**
     * 기존 스킬들이랑 다르게, 어차피 레벨에 따라 달라지는 값들 그리고 각 스킬 시전자 등등에게 적용되는 버프 등을
     * 굳이 별도 버프로 만들어서 넣어주고.. 하지 않는걸로 해보려고 함.
     *
     */

    /**
     * [ 2020 01 23 목요일 권령희 작성 ]
     * 전사 1단계 스킬 중 하나인 "롤 가렌 Q" 의 정보를 구성하는 함수.
     *
     * 레벨 정보 테이블에 들어가는 duration time(지속시간)은 스킬 사용 시 시전자에게 적용되는 버프의 지속 시간이다.
     * 시전자에게 지속되는 버프 : 이동 속도가 지속 시간동안 10(~30)% 증가함. << 아니 이거에 대한 정보가...
     *
     */
    public static void readSkillInfo_Knight_GarrenQ(){

        /** 전사 1단계 스킬 : 가렌Q 스킬번호 : 3 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.knight_GarrenQ(1, 100f, 3.5f, 3.5f, 3f, 5f, 10f));
        levelInfo.put(2, SkillInfoPerLevel.knight_GarrenQ(2, 150f, 3.5f, 3.5f, 3f, 5f, 15f));
        levelInfo.put(3, SkillInfoPerLevel.knight_GarrenQ(3, 200f, 3.5f, 3.5f, 3f, 5f, 20f));
        levelInfo.put(4, SkillInfoPerLevel.knight_GarrenQ(4, 250f, 3.5f, 3.5f, 3f, 5f, 25f));
        levelInfo.put(5, SkillInfoPerLevel.knight_GarrenQ(5, 300f, 3.5f, 3.5f, 3f, 5f, 30f));
        skillLevelTable.put(SkillType.KNIGHT_GARREN_Q, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.KNIGHT_GARREN_Q, "가렌 Q",
                5f, 0, 0, 0f,
                null, null, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }

    /**
     * [ 2020 01 23 목요일 권령희 작성 ]
     * 전사 2단계 스킬 중 하나인 "롤 가렌 E" 의 정보를 구성하는 함수.
     *
     */
    public static void readSkillInfo_Knight_GarrenE(){

        /** 전사 2단계 스킬 : 가렌E 스킬번호 : 6 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.knight_GarrenE(1, 100f, 5f, 5f, 4f, 10f));
        levelInfo.put(2, SkillInfoPerLevel.knight_GarrenE(2, 150f, 5f, 5f, 4f, 10f));
        levelInfo.put(3, SkillInfoPerLevel.knight_GarrenE(3, 200f, 5f, 5f, 4f, 10f));
        levelInfo.put(4, SkillInfoPerLevel.knight_GarrenE(4, 250f, 5f, 5f, 4f, 10f));
        levelInfo.put(5, SkillInfoPerLevel.knight_GarrenE(5, 300f, 5f, 5f, 4f, 10f));
        skillLevelTable.put(SkillType.KNIGHT_GARREN_E, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.KNIGHT_GARREN_E, "가렌 E",
                10f, 0, 0, 0f,
                null, null, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }

    /**
     * [ 2020 01 23 목요일 권령희 작성 ]
     * 전사 3단계 스킬 중 하나인 "롤 가렌 R" 의 정보를 구성하는 함수.
     *
     */
    public static void readSkillInfo_Knight_GarrenR(){

        /** 전사 3단계 스킬 : 가렌R 스킬번호 : 9 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.knight_GarrenR(1, 100f, 6f, 6f, 30f));
        levelInfo.put(2, SkillInfoPerLevel.knight_GarrenR(2, 150f, 6f, 6f, 30f));
        levelInfo.put(3, SkillInfoPerLevel.knight_GarrenR(3, 200f, 6f, 6f, 30f));
        levelInfo.put(4, SkillInfoPerLevel.knight_GarrenR(4, 250f, 6f, 6f, 30f));
        levelInfo.put(5, SkillInfoPerLevel.knight_GarrenR(5, 300f, 6f, 6f, 30f));
        skillLevelTable.put(SkillType.KNIGHT_GARREN_R, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.KNIGHT_GARREN_R, "가렌 R",
                30f, 0, 0, 0f,
                null, null, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }

    /**
     * [ 2020 01 23 목요일 권령희 작성 ]
     * 전사 1단계 스킬 중 하나인 "버서커" 의 정보를 구성하는 함수.
     *
     */
    public static void readSkillInfo_Knight_Berserker(){

        /** 전사 2단계 스킬 : 버서커 스킬번호 : 4 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.knight_Berserker(1, 10f, 20f, 10f, 5f));
        levelInfo.put(2, SkillInfoPerLevel.knight_Berserker(2, 10f, 20f, 15f, 10f));
        levelInfo.put(3, SkillInfoPerLevel.knight_Berserker(3, 10f, 20f, 20f, 15f));
        levelInfo.put(4, SkillInfoPerLevel.knight_Berserker(4, 10f, 20f, 25f, 20f));
        levelInfo.put(5, SkillInfoPerLevel.knight_Berserker(5, 10f, 20f, 30f, 25f));
        skillLevelTable.put(SkillType.KNIGHT_BERSERKER, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.KNIGHT_BERSERKER, "버서커",
                20f, 0, 0, 0f,
                null, null, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }


    /**
     * [ 2020 01 23 목요일 권령희 작성 ]
     * 전사 2단계 스킬 중 하나인 "피뻥" 의 정보를 구성하는 함수.
     *
     */
    public static void readSkillInfo_Knight_IncrHp(){

        /** 전사 2단계 스킬 : 피뻥 스킬번호 : 7 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.knight_maxHpIncr(1, 10f, 20f, 60f));
        levelInfo.put(2, SkillInfoPerLevel.knight_maxHpIncr(2, 10f, 20f, 60f));
        levelInfo.put(3, SkillInfoPerLevel.knight_maxHpIncr(3, 10f, 20f, 60f));
        levelInfo.put(4, SkillInfoPerLevel.knight_maxHpIncr(4, 10f, 20f, 60f));
        levelInfo.put(5, SkillInfoPerLevel.knight_maxHpIncr(5, 10f, 20f, 60f));
        skillLevelTable.put(SkillType.KNIGHT_INCR_HP, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.KNIGHT_INCR_HP, "피뻥",
                20f, 0, 0, 0f,
                null, null, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }


    /**
     * [ 2020 01 23 목요일 권령희 작성 ]
     * 전사 3단계 스킬 중 하나인 "무적" 의 정보를 구성하는 함수.
     *
     */
    public static void readSkillInfo_Knight_Invincible(){

        /** 전사 3단계 스킬 : 무적 스킬번호 : 10 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.knight_invincible(1, 5f, 30f));
        levelInfo.put(2, SkillInfoPerLevel.knight_invincible(2, 5f, 30f));
        levelInfo.put(3, SkillInfoPerLevel.knight_invincible(3, 7f, 30f));
        levelInfo.put(4, SkillInfoPerLevel.knight_invincible(4, 7f, 30f));
        levelInfo.put(5, SkillInfoPerLevel.knight_invincible(5, 10f, 30f));
        skillLevelTable.put(SkillType.KNIGHT_INVINCIBLE, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.KNIGHT_INVINCIBLE, "무적",
                30f, 0, 0, 0f,
                null, null, null);

        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }

    /*******************************************************************************************************************/

    /**
     * [ 2020 01 27 월요일 권령희 작성 ]
     * 마법사 1단계 스킬 중 하나인 "라이트닝 로드"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Magician_LightningRoad(){

        /** 마법사 1단계 스킬 : 라이트닝 로드 스킬번호 : 13 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.magician_LightningRoad(1, 100f, 14f, 5f));
        levelInfo.put(2, SkillInfoPerLevel.magician_LightningRoad(2, 150f, 14f, 5f));
        levelInfo.put(3, SkillInfoPerLevel.magician_LightningRoad(3, 200f, 14f, 5f));
        levelInfo.put(4, SkillInfoPerLevel.magician_LightningRoad(4, 250f, 14f, 5f));
        levelInfo.put(5, SkillInfoPerLevel.magician_LightningRoad(5, 300f, 14f, 5f));
        skillLevelTable.put(SkillType.MAGICIAN_LIGHTNING_ROAD, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_LIGHTNING_ROAD, "라이트닝 로드",
                5f, 0, 10, 0f,
                null, null, null);
        // 스킬 사용할 때는, 스킬Info 자체의 값을 활용한다기보다는, 위 레벨 테이블의 값을 참조하게 되어있음.
        // 그래서 위 스킬인포의 skillRange에 굳이 값을 넣지는 않았는데
        // 만약에 클라가 이 값을 참조한다/ 클라에 이 스킬Info 값을 주도록 되어있다고 한다면 채워야겠지
        // ㄴ 아마 그렇지 않을까?? 싶은게, 캐릭터가 스킬슬롯을 가지고 있고, 스킬슬롯은 스킬정보를 가지고 있잖음
        // 다 작성하고 나서, 통신용 클래스들 좀 살펴봐야겠다.


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);
    }

    /**
     * [ 2020 01 27 월요일 권령희 작성 ]
     * 마법사 1단계 스킬 중 하나인 "아이스볼"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Magician_IceBall(){

        /** 마법사 1단계 스킬 : 아이스볼 스킬번호 : 14 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.magician_IceBall(1, 100f, 14f, 7f * 3, 5f));
        levelInfo.put(2, SkillInfoPerLevel.magician_IceBall(2, 150f, 14f, 7f * 3, 5f));
        levelInfo.put(3, SkillInfoPerLevel.magician_IceBall(3, 200f, 14f, 7f * 3, 5f));
        levelInfo.put(4, SkillInfoPerLevel.magician_IceBall(4, 250f, 14f, 7f * 3, 5f));
        levelInfo.put(5, SkillInfoPerLevel.magician_IceBall(5, 300f, 14f, 7f * 3, 5f));
        skillLevelTable.put(SkillType.MAGICIAN_ICEBALL, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_ICEBALL, "아이스볼",
                5f, 0, 10, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);
    }

    /**
     * [ 2020 01 27 월요일 권령희 작성 ]
     * 마법사 2단계 스킬 중 하나인 "쉴드"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Magician_Shield(){

        /** 마법사 2단계 스킬 : 쉴드 스킬번호 : 16 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.magician_Shield(1, 100f, 10f, 10f, 10f, 20f));
        levelInfo.put(2, SkillInfoPerLevel.magician_Shield(2, 150f, 11f, 11f, 10f, 20f));
        levelInfo.put(3, SkillInfoPerLevel.magician_Shield(3, 200f, 12f, 12f, 10f, 20f));
        levelInfo.put(4, SkillInfoPerLevel.magician_Shield(4, 250f, 13f, 13f, 10f, 20f));
        levelInfo.put(5, SkillInfoPerLevel.magician_Shield(5, 300f, 14f, 14f, 10f, 20f));
        skillLevelTable.put(SkillType.MAGICIAN_SHIELD, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_SHIELD, "쉴드",
                20f, 0, 50, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);
    }

    /**
     * [ 2020 01 27 월요일 권령희 작성 ]
     * 마법사 2단계 스킬 중 하나인 "아이스 필드"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Magician_IceField(){

        /** 마법사 2단계 스킬 : 아이스필드 스킬번호 : 17 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.magician_IceField(1, 100f, 15f, 7f, 3f, 15f));
        levelInfo.put(2, SkillInfoPerLevel.magician_IceField(2, 150f, 15f, 8f, 4f, 16f));
        levelInfo.put(3, SkillInfoPerLevel.magician_IceField(3, 200f, 15f, 9f, 5f, 17f));
        levelInfo.put(4, SkillInfoPerLevel.magician_IceField(4, 250f, 15f, 10f, 6f, 18f));
        levelInfo.put(5, SkillInfoPerLevel.magician_IceField(5, 300f, 15f, 11f, 7f, 19f));
        skillLevelTable.put(SkillType.MAGICIAN_ICE_FIELD, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_ICE_FIELD, "아이스 필드",
                15f, 0, 50, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);
    }

    /**
     * [ 2020 01 27 월요일 권령희 작성 ]
     * 마법사 3단계 스킬 중 하나인 "썬더"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Magician_Thunder(){

        /** 마법사 3단계 스킬 : 썬더 스킬번호 : 19 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.magician_Thunder(1, 100f, 18f, 10f, 5, 3f, 30f));
        levelInfo.put(2, SkillInfoPerLevel.magician_Thunder(2, 150f, 18f, 11f, 5, 4f, 30f));
        levelInfo.put(3, SkillInfoPerLevel.magician_Thunder(3, 200f, 18f, 12f, 5, 5f, 30f));
        levelInfo.put(4, SkillInfoPerLevel.magician_Thunder(4, 250f, 18f, 13f, 5, 6f, 30f));
        levelInfo.put(5, SkillInfoPerLevel.magician_Thunder(5, 300f, 18f, 14f, 5, 7f, 30f));
        skillLevelTable.put(SkillType.MAGICIAN_THUNDER, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_THUNDER, "썬더",
                30f, 0, 100, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);
    }

    /**
     * [ 2020 01 27 월요일 권령희 작성 ]
     * 마법사 3단계 스킬 중 하나인 "프로즌 빔"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Magician_FrozenBeam(){

        /** 마법사 3단계 스킬 : 프로즌빔 스킬번호 : 20 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.magician_FrozenBeam(1, 100f, 14f, 1.0f, 5f, 30f));
        levelInfo.put(2, SkillInfoPerLevel.magician_FrozenBeam(2, 150f, 15f, 1.5f, 5f, 30f));
        levelInfo.put(3, SkillInfoPerLevel.magician_FrozenBeam(3, 200f, 16f, 2.0f, 5f, 30f));
        levelInfo.put(4, SkillInfoPerLevel.magician_FrozenBeam(4, 250f, 17f, 2.5f, 5f, 30f));
        levelInfo.put(5, SkillInfoPerLevel.magician_FrozenBeam(5, 300f, 18f, 3.0f, 5f, 30f));
        skillLevelTable.put(SkillType.MAGICIAN_FROZEN_BEAM, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.MAGICIAN_FROZEN_BEAM, "프로즌 빔",
                30f, 0, 100, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);
    }

    /*******************************************************************************************************************/

    /* 궁수 영역 */

    /**
     * [ 2020 01 28 화요일 권령희 작성 ]
     * 궁수 1단계 스킬 중 하나인 "공격 속도 증가"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Archer_IncAttackSpeed(){

        /** 궁수 1단계 스킬 : 속공 스킬번호 : 23 */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.archer_IncAttackSpeed(1, 10f, 10f, 20f));
        levelInfo.put(2, SkillInfoPerLevel.archer_IncAttackSpeed(2, 15f, 11f, 20f));
        levelInfo.put(3, SkillInfoPerLevel.archer_IncAttackSpeed(3, 20f, 12f, 20f));
        levelInfo.put(4, SkillInfoPerLevel.archer_IncAttackSpeed(4, 25f, 13f, 20f));
        levelInfo.put(5, SkillInfoPerLevel.archer_IncAttackSpeed(5, 30f, 14f, 20f));
        skillLevelTable.put(SkillType.ARCHER_INC_ATTACK_SPEED, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_INC_ATTACK_SPEED, "속공",
                20f, 0, 10f, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }

    /**
     * [ 2020 01 28 화요일 권령희 작성 ]
     * 궁수 1단계 스킬 중 하나인 "헤드 샷"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Archer_HeadShot(){

        /** 궁수 N단계 스킬 : 스킬명 스킬번호 : NN */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.archer_HeadShot(1, 100f, 50f, 5f));
        levelInfo.put(2, SkillInfoPerLevel.archer_HeadShot(2, 150f, 60f, 5f));
        levelInfo.put(3, SkillInfoPerLevel.archer_HeadShot(3, 200f, 70f, 5f));
        levelInfo.put(4, SkillInfoPerLevel.archer_HeadShot(4, 250f, 80f, 5f));
        levelInfo.put(5, SkillInfoPerLevel.archer_HeadShot(5, 300f, 90f, 5f));
        skillLevelTable.put(SkillType.ARCHER_HEAD_SHOT, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_HEAD_SHOT, "헤드샷",
                5f, 0, 10, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }

    /**
     * [ 2020 01 28 화요일 권령희 작성 ]
     * 궁수 2단계 스킬 중 하나인 "크리티컬 히트(치명타)"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Archer_CriticalHit(){

        /** 궁수 N단계 스킬 : 스킬명 스킬번호 : NN */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.archer_CriticalHit(1, 5f, 10f, 10f, 20f));
        levelInfo.put(2, SkillInfoPerLevel.archer_CriticalHit(2, 10f, 15f, 11f, 20f));
        levelInfo.put(3, SkillInfoPerLevel.archer_CriticalHit(3, 15f, 20f, 12f, 20f));
        levelInfo.put(4, SkillInfoPerLevel.archer_CriticalHit(4, 20f, 25f, 13f, 20f));
        levelInfo.put(5, SkillInfoPerLevel.archer_CriticalHit(5, 25f, 30f, 14f, 20f));
        skillLevelTable.put(SkillType.ARCHER_CRITICAL_HIT, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_CRITICAL_HIT, "치명타",
                20f, 0, 50, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }

    /**
     * [ 2020 01 28 화요일 권령희 작성 ]
     * 궁수 2단계 스킬 중 하나인 "폭풍의 시"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Archer_Storm(){

        /** 궁수 N단계 스킬 : 스킬명 스킬번호 : NN */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.archer_Storm(1, 100f, 15f, 27f, 3f, 5f));
        levelInfo.put(2, SkillInfoPerLevel.archer_Storm(2, 150f, 16.5f, 27f, 3f, 5f));
        levelInfo.put(3, SkillInfoPerLevel.archer_Storm(3, 200f, 18f, 27f, 3f, 5f));
        levelInfo.put(4, SkillInfoPerLevel.archer_Storm(4, 250f, 19.5f, 27f, 3f, 5f));
        levelInfo.put(5, SkillInfoPerLevel.archer_Storm(5, 300f, 21f, 27f, 3f, 5f));
        skillLevelTable.put(SkillType.ARCHER_STORM, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_STORM, "폭풍의 시",
                3f, 0, 5f, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }

    /**
     * [ 2020 01 28 화요일 권령희 작성 ]
     * 궁수 3단계 스킬 중 하나인 "난사"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Archer_Fire(){

        /** 궁수 N단계 스킬 : 스킬명 스킬번호 : NN */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.archer_Fire(1, 11f, 30f));
        levelInfo.put(2, SkillInfoPerLevel.archer_Fire(2, 12f, 30f));
        levelInfo.put(3, SkillInfoPerLevel.archer_Fire(3, 13f, 30f));
        levelInfo.put(4, SkillInfoPerLevel.archer_Fire(4, 14f, 30f));
        levelInfo.put(5, SkillInfoPerLevel.archer_Fire(5, 15f, 30f));
        skillLevelTable.put(SkillType.ARCHER_FIRE, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_FIRE, "난사",
                30f, 0, 100, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }

    /**
     * [ 2020 01 27 월요일 권령희 작성 ]
     * 궁수 3단계 스킬 중 하나인 "저격"의 정보를 구성하는 함수
     */
    public static void readSkillInfo_Archer_Snipe(){

        /** 궁수 N단계 스킬 : 스킬명 스킬번호 : NN */

        SkillInfo newSkillInfo;
        HashMap<Integer, SkillInfoPerLevel> levelInfo;

        /* 스킬 레벨 테이블을 채운다. */
        levelInfo = new HashMap<>();
        levelInfo.put(1, SkillInfoPerLevel.archer_Snipe(1, 100f, 30f, 50f, 30f, 30f));
        levelInfo.put(2, SkillInfoPerLevel.archer_Snipe(2, 150f, 30f, 100f, 30f, 30f));
        levelInfo.put(3, SkillInfoPerLevel.archer_Snipe(3, 200f, 30f, 150f, 30f, 30f));
        levelInfo.put(4, SkillInfoPerLevel.archer_Snipe(4, 250f, 30f, 200f, 30f, 30f));
        levelInfo.put(5, SkillInfoPerLevel.archer_Snipe(5, 300f, 30f, 250f, 30f, 30f));
        skillLevelTable.put(SkillType.ARCHER_SNIPE, levelInfo);

        /* Skill Info */
        newSkillInfo = new SkillInfo(SkillType.ARCHER_SNIPE, "저격",
                30f, 0, 100, 0f,
                null, null, null);


        /* 스킬Info 테이블에 집어넣는다 */
        skillInfoTable.put(newSkillInfo.skillType, newSkillInfo);

    }



    /*******************************************************************************************************************/

    /************************************ 새로 추가될 스킬들 함수 ******************************************************/

    /**
     * 전사 첫 번째 슬롯 스킬 중 하나인 가렌Q 스킬 사용 처리 ; 일단 롤 설명 그대로 갖고옴.
     * 1. 스킬 시전자의 이동 디버프를 제거하고
     * 2. 이동속도 증가 버프를 걸어준다 (스킬 레벨에 따라 그 비율이 달라짐)
     * 3. 공격범위 내에 타게팅된 적이 존재하는 경우( 클라에서 타게팅된 몹의 ID를 보내줌 )
     *      해당 몹에게 대미지를 가하고
     *    그렇지 않은 경우
     *      다음 공격 시 몹에게 스킬 대미지를 주기 위한 버프를 걸어줌.
     * @param worldMap
     * @param event
     */
    public static void useSkill_knightGarrenQ(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */

        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;


        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        // 전사는 mp가 없어, 충분한 mp를 가지고 있는지를 판단하는 처리는 이번에 제외함. 이후 기존에 구현된 전사 스킬들도 수정할 것.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        /** 스킬 시전자가 이동 디버프를 받고 있다면 제거한다 */
        cancelDeBuffEffect(skillUser, ConditionType.isDisableMove);     // 이동불가 효과를 제거
        cancelDeBuffEffect(skillUser, ConditionType.moveSpeedRate);     // 이동속도 비율 감소 효과를 제거
        cancelDeBuffEffect(skillUser, ConditionType.moveSpeedBonus);    // 이동속도 수치 감소 효과를 제거

        /** 스킬 시전자에게 이동속도 증가 버프를 걸어준다 */

        BuffAction moveSpeedRateBuff = new BuffAction();
        moveSpeedRateBuff.unitID = skillUser.entityID;  // 버프 발생 주체는 시전자( 투사체나 오브젝트가 아니라)
        moveSpeedRateBuff.skillUserID = skillUser.entityID; // 버프 발생 주체는 시전자
        moveSpeedRateBuff.remainTime = skillInfo.durationTime; // 버프가 지속되는 시간

        moveSpeedRateBuff.coolTime = -1f;   // 지속시간동안 효과가 쭉 유지되는 스킬이므로, 쿨타임이 존재하지 않음
        moveSpeedRateBuff.remainCoolTime = -1f;  //  쿨타임이 적용되는 스킬이 아니므로, 남은쿨타임 역시 존재하지 않음

        /** 2020 03 12 */
        moveSpeedRateBuff.skillType = SkillType.KNIGHT_GARREN_Q;
        moveSpeedRateBuff.buffDurationTime = moveSpeedRateBuff.remainTime;

        /*
         * 그동안 종종 remainCoolTime을 잘못 써왔던거 같은게, 버프의 시작 시점을
         * 늦추기 위한 용도로 써 왔다. 그런데 애초에 이게 작동하려면 쿨타임이 있어야 하고,
         * 쿨타임이 있다면 효과가 쭉 지속되는게 아니라 시간 단위로 효과가 발생하는 것..
         * 물론 쿨타임을 틱레이트보다 더 적게하면, 쿨타임을 사용하면서도 쭉 발생하는 효과를
         * 줄 순 있겠지.. 이렇게 할까?? 제대로 동작할진 모르겠음
         *
         * 이걸 왜 고민하냐면, 스킬을 '시전'하는 시점 동안에는 (클라로 치면 애니메이션 발동) 유저가 다른 스킬을 사용하거나
         * 움직이는 걸 잠깐이나마 방지하는 효과를 주려고 할 경우,
         * ㄴ 이 효과가 지난 후에 비로소 위에서 주려고 하는 이동속도 증가 효과가 적용되어야 하기 때문이다.
         *
         */
        moveSpeedRateBuff.remainCoolTime = 0.2f;            // 효과가 시작될 시점을 0.2초 뒤로 한다
        moveSpeedRateBuff.coolTime = worldMap.tickRate;     // 매 틱레이트마다 효과가 발생하게끔 하기 위해서,
                                                            // 월드의 tickRate와 같은 값을 가지게 함. 현재 기준 0.1f (100ms)

        moveSpeedRateBuff.floatParam.add(new ConditionFloatParam(ConditionType.moveSpeedRate, skillInfo.moveSpeedRate));
        // ㄴ 부여될 효과 지정. >> 나중에는 효과를 지정하는 방법을 바꾸거나, 인터페이스를 만들어야 할 듯.
        // 각 스킬별 효과를 각각 정의해놓는다던지.. 이렇게 일일이 하나하나 지정 해줄 게 아니라.


        /** 타게팅 된 적의 존재 유무에 따라 처리를 한다 */

        MonsterEntity targetMob = worldMap.monsterEntity.get(event.targetEntityID);
        if((targetMob != null) && (! skillUser.conditionComponent.isDisableAttack)){
            // 범위 내 타겟이 지정 된 경우 & 스킬 시전자가 공격 불가능 상태가 아닌 경우

            /** 대미지를 계산하여 타겟을 공격 */

            float skillDefaultDamage = userAttack.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
            ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, skillUserCondition);
            BuffAction damageBuff = createDamageBuff(damageParam, skillUser.entityID, skillUser.entityID);
            targetMob.buffActionHistoryComponent.conditionHistory.add(damageBuff);

            // doAttack()에 있는거 가져옴.
            skillUser.attackComponent.remainCoolTime = (1f / skillUser.attackComponent.attackSpeed);


            /** 침묵.. 필요한 경우 나중에 추가 (실제 가렌 스킬에는 이 효과가 있엇음 )*/
            // ...

            /* 스킬 모션 중계 */

            SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.KNIGHT_GARREN_Q;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);

        }
        else{   // 지정된 타겟이 없는 상태, 범위 내에 타게팅 대상이 존재하지 않을 때
                // ㄴ.. 구현 하려고 했는데. 못해먹겠어요.

            /** 스킬 시전자에게, 다음 공격 시 스킬의 대미지가 유지되도록 상태이상(? 버프)를 걸어준다 */

            /*
             * 별도 '버프'를 만들어 넣어줄 수도 있고..
             * 위에서 만든 버프에 추가로 효과를 넣어줄 수도 있고
             * ㄴ 어차피 특정 버프에서 특정 상태나 효과만 골라서 제거하게끔 하는 매서드를 아래에 만들어 놨으니까..
             *
             * 최대한 롤에 구현된 정도를 흉내내기 위한 처리인데, 필요없으면 지워도 되고.
             * 스킬시전 당시 스킬 사거리 내에 타겟이 존재하면, 이동속도 증가 + 바로 대상 타격(스킬댐 들어감)하는데,
             * 타겟이 존재하지 않는 상태에서 스킬 시전할 경우
             *  스킬시전 후 3촌가? 효과가 지속되는 동안 공격을 한 번 하면 (평타)
             *  스킬시전 바로 직후가 아니더라도 같은 때리기 모션 및 대미지가 들어가는 걸 확인해서
             *  최대한 그거에 가깝게 하려고 하다보니까 갖가지 편법을 쓰게 됨.
             *  여하튼 일단 이렇게 구현해보겠습니다..
             *  필요없다면 그냥 이동속도 증가 + 범위 내 있을 때에만 스킬대미지 적용하게끔 하는걸로.
             *
             * 지속시간 내에 몹을 공격하지 않으면 그대로 효과가 사라질 것이고,
             * 공격 시 doAttack() 매서드 내에서
             *      시전자가 ConditionType.isGarrenQApplied가 활성화 된 상태인지, 혹은 그런 버프를 가지고 있는지를 검사 후
             *      값이 true로 세팅 돼있으면, 가렌Q 스킬의 대미지 값을 가져와서 적에게 공격,
             *      그리고 해당 버프는 삭제처리, 모션은 스킬사용 모션(가렌Q)을 중계하도록 함.
             *
             *  ㄴ >> 이거땜에 포기. 뭔가좀 많이 답이 없어지는 구간... 못 할거까진 없는데, 굳이 이렇게까지 해야되나? 싶은거랑
             *        애초에 "타게팅" 스킬이니까. 타겟 없으면(없을거같진 않지만, 예외처리 차원이라고 치자) 없는대로 처리하는 걸로.
             *
             *          스킬슬롯 다시 가져오고, 스킬정보 가져오고, 그걸로 스킬대미지 계산하고,
             *          버프 검색하고, 모션도 스킬 모션으로 중계하는데,
             *          모션이 의도한대로 들어갈지 잘 모르겠고, 이거는 클라를 봐야 알 것같은 부분이고,
             *
             */

            // 안하기로 했으니까 무시.
            //moveSpeedRateBuff.boolParam.add(new ConditionBoolParam(ConditionType.isGarrenQApplied, true));

            /**
             * 스킬을 시전할 때랑, 시전 후 평타공격(?)을 할 때의 모션 두 가지가 있는데... 이거를.. 구분해야될거같음
             * 일단 적 내려치는 모션으로 생각하고, 위에 타겟이 있을 경우에 평타처리 후 스킬모션을 쓰도록 했음.
             * 아래는.. 스킬을 썻다! 라는 이펙트만 걍 처리해주면 되는데, 적당한 게 있을 경우 그걸로 처리할 것.
             * 일단은 주석처리..
             * 클라에서 이 두 개를 구분 가능하다면 그냥 이대로 써도 될거같고.
             */
            /* 스킬 모션 중계 */

            /*SkillInfoData skillInfoData = new SkillInfoData();
            skillInfoData.skillType = SkillType.KNIGHT_GARREN_Q;
            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);*/


        }
        // 스킬버프 넣어줌
        skillUser.buffActionHistoryComponent.conditionHistory.add(moveSpeedRateBuff);


        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        // 쿨타임 처리
        skillToUse.remainCoolTime = skillInfo.coolTime;

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);


    }

    /**
     * 전사 두 번째 슬롯 스킬 중 하나인 가렌E 스킬 사용 처리
     * 스킬오브젝트 생성하고 끝.
     * 스.오 시스템에서, 오브젝트의 위치를 시전자의 위치와 동기화해준다.
     *      그 이후 처리는, 스오가 범위 및 타겟인식을 통해 기존 시스템대로 굴러갈 것.
     *
     * @param worldMap
     * @param event
     */
    public static void useSkill_knightGarrenE(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */

        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /* 스킬 시전 가능 여부를 판단한다 */
        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        // 전사는 mp가 없어, 충분한 mp를 가지고 있는지를 판단하는 처리는 이번에 제외함. 이후 기존에 구현된 전사 스킬들도 수정할 것.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }

        /* 스킬 사용 처리를 한다 */

        /** 스킬 오브젝트를 생성한다 */

        int skillObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

        /* position component */
        PositionComponent positionComponent;    // 장판이 펼쳐질 위치 == 시전자가 서있는 위치. 요 스킬의 경우, 시전자의 움직임에 따라 계속 위치가 바뀔 거라서.
        positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());

        /* skillObject component */
        int createdSkillType;
        createdSkillType = skillToUse.skillinfo.skillType;

        int skillAreaType;
        skillAreaType = SkillAreaType.CIRCLE;

        float skillObjectDurationTime;
        skillObjectDurationTime = skillInfo.durationTime;

        float skillObjectRadius;
        skillObjectRadius = skillInfo.attackRange;

        Vector3 startPosition;
        startPosition = positionComponent.position;

        Vector3 direction;
        direction = event.skillDirection;   // 여기선 필요없을거같긴한데..

        float distanceRate;
        distanceRate = event.skillDistanceRate;

        // 대미지 버프
        BuffAction skillObjDamageBuff = new BuffAction();
        skillObjDamageBuff.unitID = skillObjectEntityID;
        skillObjDamageBuff.skillUserID = skillUser.entityID;
        skillObjDamageBuff.remainTime = 1f;      // 1초당 한 번씩 대미지가 들어가고, 1초 뒤에도 범위 내에 있을 경우 스.오가 타겟에게 다시 딜을 넣어줌
        skillObjDamageBuff.remainCoolTime = -1f;
        skillObjDamageBuff.coolTime = -1f;
        skillObjDamageBuff.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, skillInfo.attackDamage));


        // 상태이상 버프 / 필요없으면 지을 것 >> 아니.. 넣어줄 수가 없네
        BuffAction skillObjCondBuff = new BuffAction();
        skillObjCondBuff.unitID = skillObjectEntityID;
        skillObjCondBuff.skillUserID = skillUser.entityID;
        skillObjCondBuff.remainTime = 0.3f;
        skillObjCondBuff.remainCoolTime = -1f;
        skillObjCondBuff.coolTime = -1f;
        skillObjCondBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
        skillObjCondBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));    // 필요없으면 지울 것


        /**
         * 2020 02 06 ver, 위에꺼 사용안함. 혹시나 해서 일단 남겨두는 것.
         */

        /* 데미지 버프 */
        float skillDefaultDamage = skillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
        ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, skillUserCondition);

        /* 상태 버프 */
        ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
        ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);


        BuffAction skillObjBuff = new BuffAction();
        skillObjBuff.unitID = skillObjectEntityID;
        skillObjBuff.skillUserID = skillUser.entityID;

        skillObjBuff.remainTime = skillInfo.durationTime;   // 상태버프. 1초동안 못움직이고 공격 못한다,,
        skillObjBuff.coolTime = 0.1f;
        skillObjBuff.remainCoolTime = 0f;

        skillObjBuff.floatParam.add(damageParam);


        /***************************************************************************************************************/



        /* SkillObject Component */
        SkillObjectComponent skillObjectComponent
                = new SkillObjectComponent(createdSkillType, skillAreaType, skillUser.entityID, skillObjectDurationTime, skillObjectRadius,
                -1f, -1f, startPosition, event.skillDirection, distanceRate, skillObjBuff);

        /* SkillObject Entity */
        SkillObjectEntity skillObjectEntity
                = new SkillObjectEntity(positionComponent, skillObjectComponent);
        skillObjectEntity.entityID = skillObjectEntityID;


        //전사 E 장판
        worldMap.requestCreateQueue.add(skillObjectEntity);

        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.KNIGHT_GARREN_E;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);




        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */


        // 쿨타임 처리
        skillToUse.remainCoolTime = skillInfo.coolTime;

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);

    }

    /**
     * 전사 세 번째 슬롯 스킬 중 하나인 가렌R 스킬 사용 처리
     * @param worldMap
     * @param event
     */
    public static void useSkill_knightGarrenR(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        // 전사는 mp가 없어, 충분한 mp를 가지고 있는지를 판단하는 처리는 이번에 제외함. 이후 기존에 구현된 전사 스킬들도 수정할 것.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        MonsterEntity targetMob = worldMap.monsterEntity.get(event.targetEntityID);
        if(targetMob == null){  // 놀랍다 이게 복붙이 되다니

            System.out.println("엥 몹이 널인데?????????");
            return;

        }

        /** 스킬 공격력을 대미지로 하여, 대상에게 1차 딜을 넣음 */
        /*float damage = skillInfo.attackDamage * 100 / ( 100 + targetMob.defenseComponent.defense);
        DamageHistory damageHistory = new DamageHistory(skillUser.entityID, true, skillInfo.attackDamage);

        targetMob.hpHistoryComponent.hpHistory.add(damageHistory);*/

        float skillDefaultDamage = skillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
        ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, skillUserCondition);
        BuffAction firstDamage = createDamageBuff(damageParam, skillUser.entityID, skillUser.entityID);
        targetMob.buffActionHistoryComponent.conditionHistory.add(firstDamage);


        // doAttack()에 있는거 가져옴.
        skillUser.attackComponent.remainCoolTime = (1f / skillUser.attackComponent.attackSpeed);


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.KNIGHT_GARREN_R;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);


        /**
         * 2020 02 06
         * 음.. 1차 때 준 최종 딜의 20퍼를 고정으로 줘야할 것만 같은데, 그럴 수가 없다ㅠ
         * 시스템 설계 망햇어요
         * 일단은 그냥 기본 스킬댐의 20퍼를 가지고 처리하게 하는 수밖에..
         */

        /** 1차에서 준 대미지의 20% 만큼의 대미지로 2차 딜을 넣음 */    // >> 시간차 좀 주는 게 나으려나?? 버프로 해서.

        float secondDamage = skillInfo.attackDamage * 0.2f;
        ConditionFloatParam secondDamageParam = createDamageParam(secondDamage, userAttack, skillUserCondition);
        BuffAction secondDamageBuff = createDamageBuff(secondDamageParam, skillUser.entityID, skillUser.entityID);

        secondDamageBuff.remainCoolTime = 0.1f;   // 0.1초 뒤에 딜 처리가 되게끔.
        secondDamageBuff.remainTime = 0.25f;   // 0.2 초 뒤에 사라지게끔
        secondDamageBuff.coolTime = 0.3f;     // 대미지 처리를 한 번만 처리하게끔

        targetMob.buffActionHistoryComponent.conditionHistory.add(secondDamageBuff);


        /** 상태 버프 */
        ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
        ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);

        BuffAction condBuff = new BuffAction();
        condBuff.unitID = skillUser.entityID;
        condBuff.skillUserID = skillUser.entityID;

        condBuff.remainTime = 3f;
        condBuff.remainCoolTime = -1f;
        condBuff.coolTime = -1f;

        condBuff.boolParam.add(moveDebuff);
        condBuff.boolParam.add(attackDebuff);

        targetMob.buffActionHistoryComponent.conditionHistory.add(condBuff);




        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        // 쿨타임 처리
        skillToUse.remainCoolTime = skillInfo.coolTime;


        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);


    }

    /**
     * 전사 첫 번째 슬롯 스킬 중 하나인 버서커 스킬 사용 처리
     * @param worldMap
     * @param event
     */
    public static void useSkill_knightBerserker(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;


        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        // 전사는 mp가 없어, 충분한 mp를 가지고 있는지를 판단하는 처리는 이번에 제외함. 이후 기존에 구현된 전사 스킬들도 수정할 것.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        /** 스킬 시전자에게, 흡혈 및 공격속도 증가 버프를 적용한다 */

        BuffAction berserkerBuff = new BuffAction();
        berserkerBuff.unitID = skillUser.entityID;
        berserkerBuff.skillUserID = skillUser.entityID;
        berserkerBuff.remainTime = skillInfo.durationTime;
        berserkerBuff.remainCoolTime = -1f;
        berserkerBuff.coolTime = -1f;

        berserkerBuff.floatParam.add(new ConditionFloatParam(ConditionType.bloodSuckingRate, skillInfo.bloodSuckingRate ));
        berserkerBuff.floatParam.add(new ConditionFloatParam(ConditionType.attackSpeedRate, skillInfo.attackSpeedRate ));

        /** 2020 03 12 */
        berserkerBuff.skillType = SkillType.KNIGHT_BERSERKER;
        berserkerBuff.buffDurationTime = berserkerBuff.remainTime;

        skillUser.buffActionHistoryComponent.conditionHistory.add(berserkerBuff);



        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.KNIGHT_BERSERKER;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        // 쿨타임 처리
        skillToUse.remainCoolTime = skillInfo.coolTime;


        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);

    }

    /**
     * 전사 두 번째 슬롯 스킬 중 하나인 피뻥 스킬 사용 처리
     * @param worldMap
     * @param event
     */
    public static void useSkill_knightIncrHp(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;



        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        // 전사는 mp가 없어, 충분한 mp를 가지고 있는지를 판단하는 처리는 이번에 제외함. 이후 기존에 구현된 전사 스킬들도 수정할 것.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        /** 시전자에게.. 체력증가 버프를 넣어준다. */

        BuffAction hpIncBuff = new BuffAction();
        hpIncBuff.unitID = skillUser.entityID;
        hpIncBuff.skillUserID = skillUser.entityID;
        hpIncBuff.remainTime = skillInfo.durationTime;
        hpIncBuff.coolTime = -1f;
        hpIncBuff.remainCoolTime = -1f;
        hpIncBuff.floatParam.add(new ConditionFloatParam(ConditionType.maxHPRate, skillInfo.maxHpRate));

        skillUser.buffActionHistoryComponent.conditionHistory.add(hpIncBuff);


        /** 2020 03 12 */
        hpIncBuff.skillType = SkillType.KNIGHT_INCR_HP;
        hpIncBuff.buffDurationTime = hpIncBuff.remainTime;


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.KNIGHT_INCR_HP;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        // 쿨타임 처리
        skillToUse.remainCoolTime = skillInfo.coolTime;


        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);


    }

    /**
     * 전사 세 번째 슬롯 스킬 중 하나인 무적 스킬 사용 처리
     * @param worldMap
     * @param event
     */
    public static void useSkill_knightInvincible(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;


        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        // 전사는 mp가 없어, 충분한 mp를 가지고 있는지를 판단하는 처리는 이번에 제외함. 이후 기존에 구현된 전사 스킬들도 수정할 것.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        BuffAction invincibleBuff = new BuffAction();
        invincibleBuff.unitID = skillUser.entityID;
        invincibleBuff.skillUserID = skillUser.entityID;
        invincibleBuff.remainTime = skillInfo.durationTime;
        invincibleBuff.coolTime = -1f;
        invincibleBuff.remainCoolTime = -1f;
        invincibleBuff.boolParam.add(new ConditionBoolParam(ConditionType.isTargetingInvincible, true));

        /** 2020 03 12 */
        invincibleBuff.skillType = SkillType.KNIGHT_INVINCIBLE;
        invincibleBuff.buffDurationTime = invincibleBuff.remainTime;

        skillUser.buffActionHistoryComponent.conditionHistory.add(invincibleBuff);

        System.out.println("???? 무적 처리 안함??? ");


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.KNIGHT_INVINCIBLE;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        // 쿨타임 처리
        skillToUse.remainCoolTime = skillInfo.coolTime;

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        // 안해도..
        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);

    }


    /**
     * 마법사 첫 번째 슬롯 스킬 중 하나인 라이트닝 로드 스킬 사용 처리
     *
     * 장판 없음. 아래 매서드에서 즉발처리 함 (만약 구현해야 하면 알려주세ㅐ요)
     * 시전자 위치를 기준으로 스킬 사거리만큼, 시전자가 바라보는 방향을 향해 타겟을 검색
     * ㄴ
     *
     *
     *
     * @param worldMap
     * @param event
     */
    public static void useSkill_magicianLightningRoad(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;


        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */


        /** 1. 타겟을 검색한다 */
        /**
         * 모든 몬스터 앤티티에 대해
         * 0) 죽어있거나, 타게팅 불가능한 대상은 제외한다
         * 1) 대상이 스킬 사거리 내에 존재하는지 판단한다
         * 2) 대상이 범위각 내에 존재하는지 판단한다 ; 여기서는, 스킬을 사용하는 방향 기준으로 정면. 180도.
         * 3) 대상이 공격 범위 내에 존재하는지 판단한다
         *
         */

        ArrayList<MonsterEntity> targetList = new ArrayList<>();    // 최종 타겟 목록을 담을 걳.
        for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()) {

            MonsterEntity monster = monsterEntity.getValue();

            /** 죽어있거나, 타게팅 불가능한 대상은 제외한다 */
            HPComponent targetHP = monster.hpComponent;
            ConditionComponent targetCondition = monster.conditionComponent;

            boolean targettingDisable = ((targetHP.currentHP <= 0f) || (targetCondition.isUnTargetable == true)) ? true : false;
            if(targettingDisable){
                continue;
            }

            /** 대상이 스킬 사거리 내에 존재하는지 판단한다 */

            float skillRange = skillInfo.range * event.skillDistanceRate;
            Vector3 targetPos = monster.positionComponent.position;
            Vector3 skillUserPos = skillUser.positionComponent.position;

            /* 대상과 스킬 시전자(스킬이 시전되는 위치) 사이의 거리를 구함 */
            float targetDistance = Vector3.distance(skillUserPos, targetPos);
            if(targetDistance > skillRange){
                continue;
            }


            /** 대상이 범위각 내에 존재하는지 판단한다 */

            Vector3 skillDirection = event.skillDirection;
            Vector3 monsterDirection = Vector3.getTargetDirection(skillUserPos , targetPos);

            float betweenAngle = Vector3.getAngle(skillDirection, monsterDirection);
            if(betweenAngle > 90f){
                continue;
            }


            /** 대상이 공격범위 내에 존재하는지 판단한다 */
            // 점과 선 사이 거리를 구하는 방법(외적) 사용..

            float attackRange = skillInfo.attackRange;
            float distance = Vector3.getDistanceBetweenVectorAndDot(skillDirection, skillUserPos, targetPos);
            if(distance > attackRange){
                continue;
            }

            // 최종적으로 타겟 목록에 집어넣음
            targetList.add(monster);

        }


        /** 버프 구성 */

        /* 데미지 버프 */
        float skillDefaultDamage = skillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
        ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, skillUserCondition);
        BuffAction damageBuff = createDamageBuff(damageParam, skillUser.entityID, skillUser.entityID);

        /* 상태 버프 */
        ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
        ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);

        BuffAction condBuff = new BuffAction();
        condBuff.unitID = skillUser.entityID;
        condBuff.skillUserID = skillUser.entityID;

        condBuff.remainTime = 3f;
        condBuff.remainCoolTime = -1f;
        condBuff.coolTime = -1f;

        condBuff.boolParam.add(moveDebuff);
        condBuff.boolParam.add(attackDebuff);

        /* ********************************************************/

        /** 2. 타겟에 대해, 대미지 혹은 추가 스킬효과를 적용한다 */

        for(int i=0; i<targetList.size(); i++){

            MonsterEntity monster = targetList.get(i);

            monster.buffActionHistoryComponent.conditionHistory.add((BuffAction) damageBuff.clone());
            monster.buffActionHistoryComponent.conditionHistory.add((BuffAction) condBuff.clone());

        }


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.MAGICIAN_LIGHTNING_ROAD;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        // 쿨타임 처리
        skillToUse.remainCoolTime = skillInfo.coolTime;

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);

    }

    /**
     * 마법사 첫 번째 슬롯 스킬 중 하나인 아이스볼 스킬 사용 처리
     * @param worldMap
     * @param event
     */
    public static void useSkill_magicianIceBall(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        /** 투사체를 생성한다 */

        int flyingObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

        /* position component */
        PositionComponent positionComponent;    // 장판이 생성될 위치
        positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());
        positionComponent.position.y(2.2f);


        /* flyingObject component */
        int createdSkillType;
        createdSkillType = skillToUse.skillinfo.skillType;

        float flyingObjectRadius;
        flyingObjectRadius = skillInfo.attackRange;

        float flyingObjectSpeed = skillInfo.flyingObjectSpeed;

        Vector3 startPosition;
        startPosition = (Vector3) positionComponent.position.clone();

        Vector3 direction;  // 투사체가 날아갈 방향
        direction = event.skillDirection;


        int targetEntityID = event.targetEntityID;


        /* 데미지 버프 */
        float skillDefaultDamage = skillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
        ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, skillUserCondition);

        /* 상태 버프 */
        ConditionFloatParam moveSpeedDebuff = new ConditionFloatParam(ConditionType.moveSpeedRate, -50f);
        // 2020 03 12
        ConditionBoolParam slow = new ConditionBoolParam(ConditionType.isSlow, true);


        BuffAction flyingObjBuff = new BuffAction();
        flyingObjBuff.unitID = flyingObjectEntityID;
        flyingObjBuff.skillUserID = skillUser.entityID;

        flyingObjBuff.remainTime = 3f;
        flyingObjBuff.coolTime = -1f;
        flyingObjBuff.remainCoolTime = -1f;

        flyingObjBuff.floatParam.add(damageParam);
        flyingObjBuff.floatParam.add(moveSpeedDebuff);
        // 2020 03 12
        flyingObjBuff.boolParam.add(slow);


        /** 2020 03 12 */
        flyingObjBuff.skillType = SkillType.MAGICIAN_ICEBALL;
        flyingObjBuff.buffDurationTime = flyingObjBuff.remainTime;


        /* FlyingObject Component */
        FlyingObjectComponent flyingObjectComponent
                = new FlyingObjectComponent(skillUser.entityID, startPosition, direction, flyingObjectSpeed, flyingObjectRadius, 0f, flyingObjBuff, targetEntityID);
        flyingObjectComponent.beDestroyedByCrash = true;
        flyingObjectComponent.createdSkillType = createdSkillType;

        /* FlyingObject Entity */
        FlyingObjectEntity flyingObjectEntity
                = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
        flyingObjectEntity.entityID = flyingObjectEntityID;



        /** 생성요청 큐에 넣기 */
        worldMap.requestCreateQueue.add(flyingObjectEntity);

        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 쿨타임을 초기화한다 */
        skillToUse.remainCoolTime = skillInfo.coolTime;

        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.MAGICIAN_ICEBALL;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);


    }


    /**
     * 마법사 두 번째 슬롯 스킬 중 하나인 실드 스킬 사용 처리
     * @param worldMap
     * @param event
     */
    public static void useSkill_magicianShield(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        /** 버프 정보 구성 */

        BuffAction defenseBuff = new BuffAction();
        defenseBuff.unitID = skillUser.entityID;
        defenseBuff.skillUserID = skillUser.entityID;
        defenseBuff.remainTime = skillInfo.durationTime;
        defenseBuff.coolTime = -1f;
        defenseBuff.remainCoolTime = -1f;
        defenseBuff.floatParam.add(new ConditionFloatParam(ConditionType.defenseBonus, skillInfo.attackDamage));


        /** 2020 03 12 */
        defenseBuff.skillType = SkillType.MAGICIAN_SHIELD;
        defenseBuff.buffDurationTime = defenseBuff.remainTime;


        /** 타겟을 검색한다 */
        /**
         * 모든 몬스터 앤티티에 대해
         * 0) 죽은 대상은 제외한다
         * 1) 대상이 스킬 사거리 내에 존재하는지 판단한다
         *
         */

        ArrayList<CharacterEntity> targetList = new ArrayList<>();    // 최종 타겟 목록을 담을 걳.
        for(HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();

            /** 죽어있거나, 타게팅 불가능한 대상은 제외한다 */
            HPComponent targetHP = character.hpComponent;
            ConditionComponent targetCondition = character.conditionComponent;

            boolean targettingDisable = (targetHP.currentHP <= 0f) ? true : false;
            if(targettingDisable){
                continue;
            }

            /** 대상이 스킬 사거리 내에 존재하는지 판단한다 */

            float skillRange = skillInfo.range;
            Vector3 targetPos = character.positionComponent.position;
            Vector3 skillUserPos = skillUser.positionComponent.position;

            /* 대상과 스킬 시전자(스킬이 시전되는 위치) 사이의 거리를 구함 */
            float targetDistance = Vector3.distance(skillUserPos, targetPos);
            if(targetDistance > skillRange){
                continue;
            }


            /** 타겟에게 효과를 걸어준다 */

            character.buffActionHistoryComponent.conditionHistory.add(defenseBuff);

        }



        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.MAGICIAN_SHIELD;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        // 쿨타임 처리
        skillToUse.remainCoolTime = skillInfo.coolTime;

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);


    }

    /**
     * 마법사 두 번째 슬롯 스킬 중 하나인 아이스 필드 스킬 사용 처리
     * @param worldMap
     * @param event
     */
    public static void useSkill_magicianIceField(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        /** 스킬 오브젝트를 생성한다 (아이스 장판) */

        int skillObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

        /* position component */

        PositionComponent positionComponent;    // 장판이 생성될 위치.
        positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());

        Vector3 startPosition = (Vector3) positionComponent.position.clone();

        /* 장판이 펼쳐질 위치 구하기 */
        Vector3 skillDir = (Vector3) event.skillDirection.clone();

        float skillDistanceRate = event.skillDistanceRate;
        skillDir.setSpeed(skillDistanceRate * skillInfo.range);

        startPosition.movePosition(startPosition, skillDir);


        /* skillObject component */
        int createdSkillType;
        createdSkillType = skillToUse.skillinfo.skillType;

        int skillAreaType = SkillAreaType.CIRCLE;

        float skillObjectRadius;
        skillObjectRadius = skillInfo.attackRange;

        float skillObjDurationTime = skillInfo.durationTime;

        int userEntityID = skillUser.entityID;




        /* 데미지 버프 */
        float skillDefaultDamage = skillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
        ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, skillUserCondition);

        /* 상태 버프 */
        ConditionFloatParam moveSpeedDebuff = new ConditionFloatParam(ConditionType.moveSpeedRate, -50f);
        // 2020 03 12
        ConditionBoolParam slow = new ConditionBoolParam(ConditionType.isSlow, true);


        BuffAction skillObjBuff = new BuffAction();
        skillObjBuff.unitID = skillObjectEntityID;
        skillObjBuff.skillUserID = skillUser.entityID;

        skillObjBuff.remainTime = 3f;
        skillObjBuff.coolTime = -1f;
        skillObjBuff.remainCoolTime = -1f;

        skillObjBuff.floatParam.add(damageParam);
        skillObjBuff.floatParam.add(moveSpeedDebuff);
        // 2020 03 12
        skillObjBuff.boolParam.add(slow);


        /** 2020 03 12 */
        skillObjBuff.skillType = SkillType.MAGICIAN_ICE_FIELD;
        skillObjBuff.buffDurationTime = skillObjBuff.remainTime;


        /* SkillObject Component */
        SkillObjectComponent skillObjectComponent
                = new SkillObjectComponent(createdSkillType, skillAreaType, skillUser.entityID,
                skillObjDurationTime, skillObjectRadius, 0f,
                0f, startPosition, skillDir, skillDistanceRate, skillObjBuff);

        /* FlyingObject Entity */
        SkillObjectEntity skillObjectEntity
                = new SkillObjectEntity(positionComponent, skillObjectComponent);
        skillObjectEntity.entityID = skillObjectEntityID;
        skillObjectEntity.positionComponent.position = startPosition;



        /** 생성요청 큐에 넣기 */
        worldMap.requestCreateQueue.add(skillObjectEntity);

        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 쿨타임을 초기화한다 */
        skillToUse.remainCoolTime = skillInfo.coolTime;

        /* 스킬 모션 중계 */
        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.MAGICIAN_ICE_FIELD;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);


        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);

    }

    /**
     * 마법사 세 번째 슬롯 스킬 중 하나인 썬더 스킬 사용 처리
     * @param worldMap
     * @param event
     */
    public static void useSkill_magicianThunder(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        /** 스킬 오브젝트를 생성한다 (전기 장판) */
        /**
         * 초당 전기데미지 + 스턴..
         * 전기 뎀 ==>> 1초당
         * 스턴 ==>> 초당.. 인데, 장판 벗어날 틈도 줘야 하니까?? 0.7정도 줘봄.
         * 그러면, 장판에서 "기존 효과를 받고 있지 않은지 검사"를 할 텐데,
         *      스턴효과가 끝났더라도 전기뎀 버프가 남아있는동안은(0.3초) 데미지를 받지 않음. 그 사이에 이동 좀 하다가
         *      여전히(?) 장판이면 또 데미지랑 스턴 받겟지.
         *
         *      ㄴ 이런 효과가 아니라, 스킬 사용된 후 계속 스턴상태에 있는거를 의도한 거라면,
         *      전기뎀이랑 마찬가지로 스턴도 지속시간 1초, 쿨타임1초를 주면 되겠다.
         */

        int skillObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

        /* position component */

        PositionComponent positionComponent;    // 장판이 생성될 위치.
        positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());

        Vector3 startPosition = (Vector3) positionComponent.position.clone();

        /* 장판이 펼쳐질 위치 구하기 */
        Vector3 skillDir = (Vector3) event.skillDirection.clone();

        float skillDistanceRate = event.skillDistanceRate;
        skillDir.setSpeed(skillDistanceRate * skillInfo.range);

        startPosition.movePosition(startPosition, skillDir);


        /* skillObject component */
        int createdSkillType;
        createdSkillType = skillToUse.skillinfo.skillType;

        int skillAreaType = SkillAreaType.CIRCLE;

        float skillObjectRadius;
        skillObjectRadius = skillInfo.attackRange;

        float skillObjDurationTime = skillInfo.durationTime;

        int userEntityID = skillUser.entityID;



        /* 데미지 버프 */
        float skillDefaultDamage = skillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
        ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, skillUserCondition);

        /* 상태 버프 */
        ConditionBoolParam stunBuff = new ConditionBoolParam(ConditionType.isStunned, true);

        BuffAction skillObjBuff = new BuffAction();
        skillObjBuff.unitID = skillObjectEntityID;
        skillObjBuff.skillUserID = skillUser.entityID;

        skillObjBuff.remainTime = 0.7f;
        skillObjBuff.coolTime = -1f;
        skillObjBuff.remainCoolTime = -1f;

        skillObjBuff.floatParam.add(damageParam);
        skillObjBuff.boolParam.add(stunBuff);


        /** 2020 03 12 */
        skillObjBuff.skillType = SkillType.MAGICIAN_THUNDER;
        skillObjBuff.buffDurationTime = skillObjBuff.remainTime;


        /* SkillObject Component */
        SkillObjectComponent skillObjectComponent
                = new SkillObjectComponent(createdSkillType, skillAreaType, skillUser.entityID,
                skillObjDurationTime, skillObjectRadius, 0f,
                0f, startPosition, skillDir, skillDistanceRate, skillObjBuff);

        /* SkillObject Entity */
        SkillObjectEntity skillObjectEntity
                = new SkillObjectEntity(positionComponent, skillObjectComponent);
        skillObjectEntity.entityID = skillObjectEntityID;
        skillObjectEntity.positionComponent.position = startPosition;



        /** 생성요청 큐에 넣기 */
        worldMap.requestCreateQueue.add(skillObjectEntity);

        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 쿨타임을 초기화한다 */
        skillToUse.remainCoolTime = skillToUse.skillinfo.skillCoolTime;


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.MAGICIAN_THUNDER;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);

    }

    /**
     * 마법사 세 번째 슬롯 스킬 중 하나인 프로즌 빔 스킬 사용 처리
     * @param worldMap
     * @param event
     */
    public static void useSkill_magicianFrozenBeam(WorldMap worldMap, ActionUseSkill event){

        /**
         * 스킬 설명 : 일직선으로 냉기 빔 날려서 상대를 얼린다.
         *      범위, 논타겟, 즉발, 60도 콘, 빙결시간, 지속시간
         *
         * -- 타겟 하나에 대해, 스킬 효과는 한 번 적용된다고 가정
         *      ; 한 번 범위 내에 들어서 스킬 맞은 애는, 빙결시간이 지난 후 여전히 스킬 범위 내에 있다 하더라도 효과를 받지 않는다
         *
         * -- 구체적인 스킬 효과 (임시. 바뀔 수 있음 )
         *      1) 냉각. 빙결시간동안 이동 불가, 공격 불가
         *      2) 빙결이 풀리고 난 후,
         *          ① 스킬 공격 대미지 들어감
         *          ② 슬로우. 잠시 이동속도 느려짐 ; 슬로우의 지속 시간은.. 빙결 시간과 똑같이 하거나 혹은 지속시간 만큼으로.
         *     스킬 오브젝트의 버프 액션에는 위 순서대로 들어갈 것, 버프지속시간 등은 냉각 효과를 기준으로 들어감.
         *     위의 각 효과를 하나씩 꺼내서, 각각을 하나의 버프액션으로 만들어 타겟에게 적용한다.
         *
         * 1) 캐릭터가 스킬을 시전하면, 스킬을 사용한 방향이 고정되고, 시전 캐릭터도 스킬이 지속되는 시간동안 고정된다 (이동 불가, 공격 불가)
         * 2) 콘 형의 스킬 오브젝트(장판)을 생성한다. 장판이 가지는 효과는 위와 같다
         * 3) 장판 생성 후, 장판이 유지되는 지속시간 동안 매번 모든 몹에 대해 <<< 스킬 오브젝트 시스템에 작성할 로직.
         *      ① 몹의 상태 (무적, 타게팅불가능, 죽어있음)
         *      ② 범위 거리
         *      ③ 범위 각도
         *      ④ 기존에 효과를 받고 있는지 여부 ; 기존에 효과를 받고 있다면 패스한다
         *          를 차례대로 판단, 최종 타겟에게만 효과를 적용한다
         *
         */

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;


        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }

        /* 스킬 사용 처리를 한다 */

        /** 스킬 오브젝트를 생성한다  */

        int skillObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

        /* position component */

        PositionComponent positionComponent;    // 장판이 생성될 위치.
        positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());

        Vector3 startPosition = (Vector3) positionComponent.position.clone();

        /* 장판이 펼쳐질 위치 구하기 */
        Vector3 skillDir = (Vector3) event.skillDirection.clone();

        float skillDistanceRate = event.skillDistanceRate;

        // 콘 형이라.. 시전자 위치로 고정될 것이라?? 일단 주석처리 해둠.
        //skillDir.setSpeed(skillDistanceRate * skillInfo.range);

        //startPosition.movePosition(startPosition, skillDir);


        /* skillObject component */
        int createdSkillType;
        createdSkillType = skillToUse.skillinfo.skillType;

        int skillAreaType = SkillAreaType.CONE;

        float skillObjectRadius;
        skillObjectRadius = skillInfo.range;

        float skillObjDurationTime = skillInfo.durationTime;

        int userEntityID = skillUser.entityID;



        /** 데미지 버프 */
        float skillDefaultDamage = skillInfo.attackDamage;  // 스킬레벨당 공격 데미지를 따른다.
        ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, skillUserCondition);

        /** 상태 버프 */
        /* 슬로우 */
        ConditionFloatParam slowParam = new ConditionFloatParam(ConditionType.moveSpeedRate, -50f);

        /* 프리징 */
        ConditionBoolParam moveDebuff = new ConditionBoolParam(ConditionType.isDisableMove, true);
        ConditionBoolParam attackDebuff = new ConditionBoolParam(ConditionType.isDisableAttack, true);
        // 2020 03 12
        ConditionBoolParam freezing = new ConditionBoolParam(ConditionType.isFreezing, true);



        BuffAction skillObjBuff = new BuffAction();
        skillObjBuff.unitID = skillObjectEntityID;
        skillObjBuff.skillUserID = skillUser.entityID;

        skillObjBuff.remainTime = skillInfo.freezingTime;
        skillObjBuff.coolTime = -1f;
        skillObjBuff.remainCoolTime = -1f;

        skillObjBuff.floatParam.add(damageParam);
        skillObjBuff.floatParam.add(slowParam);
        skillObjBuff.boolParam.add(moveDebuff);
        skillObjBuff.boolParam.add(attackDebuff);
        skillObjBuff.boolParam.add(freezing);


        /** 2020 03 12 */
        skillObjBuff.skillType = SkillType.MAGICIAN_FROZEN_BEAM;
        skillObjBuff.buffDurationTime = skillObjBuff.remainTime;


        /* SkillObject Component */
        SkillObjectComponent skillObjectComponent
                = new SkillObjectComponent(createdSkillType, skillAreaType, userEntityID,
                skillObjDurationTime, skillObjectRadius, 30f,
                0f, startPosition, skillDir, skillDistanceRate, skillObjBuff);

        /* SkillObject Entity */
        SkillObjectEntity skillObjectEntity
                = new SkillObjectEntity(positionComponent, skillObjectComponent);
        skillObjectEntity.entityID = skillObjectEntityID;



        /** 생성요청 큐에 넣기 */
        worldMap.requestCreateQueue.add(skillObjectEntity);

        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 쿨타임을 초기화한다 */
        skillToUse.remainCoolTime = skillToUse.skillinfo.skillCoolTime;


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.MAGICIAN_FROZEN_BEAM;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);


        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = skillInfo.durationTime;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);

    }


    /**
     *
     * @param worldMap
     * @param event
     */
    public static void useSkill_archerIncAttackSpeed(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;


        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }

        /* 스킬 사용 처리를 한다 */

        BuffAction attackSpeedBuff = new BuffAction();
        attackSpeedBuff.unitID = skillUser.entityID;
        attackSpeedBuff.skillUserID = skillUser.entityID;
        attackSpeedBuff.remainTime = skillInfo.durationTime;
        attackSpeedBuff.remainCoolTime = -1f;
        attackSpeedBuff.coolTime = -1f;
        attackSpeedBuff.floatParam.add(new ConditionFloatParam(ConditionType.attackSpeedRate, skillInfo.attackSpeedRate));


        /** 2020 03 12 */
        attackSpeedBuff.skillType = SkillType.ARCHER_INC_ATTACK_SPEED;
        attackSpeedBuff.buffDurationTime = attackSpeedBuff.remainTime;

        skillUser.buffActionHistoryComponent.conditionHistory.add(attackSpeedBuff);


        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 쿨타임을 초기화한다 */
        skillToUse.remainCoolTime = skillToUse.skillinfo.skillCoolTime;


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.ARCHER_INC_ATTACK_SPEED;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);


        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.1f;
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);

    }

    /**
     * 궁수 헤드샷
     * @param worldMap
     * @param event
     */
    /**
     * 궁수 헤드샷
     * @param worldMap
     * @param event
     */
    public static void useSkill_archerHeadShot(WorldMap worldMap, ActionUseSkill event){

        System.out.println("해드샷 사용 ");

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }

        /* 스킬 사용 처리를 한다 */

        BuffAction headShotBuff = new BuffAction();
        headShotBuff.unitID = skillUser.entityID;
        headShotBuff.skillUserID = skillUser.entityID;
        headShotBuff.remainTime = skillInfo.coolTime;
        headShotBuff.coolTime = -1f;
        headShotBuff.remainCoolTime = -1f;

        /** 2020 03 12 */
        headShotBuff.skillType = SkillType.ARCHER_HEAD_SHOT;
        headShotBuff.buffDurationTime = headShotBuff.remainTime;

        ConditionBoolParam headShotParam = new ConditionBoolParam(ConditionType.isArcherHeadShotActivated, true);
        headShotBuff.boolParam.add(headShotParam);

        ConditionFloatParam criticalDam = new ConditionFloatParam(ConditionType.criticalDamageRate, skillInfo.criticalBonusDamageRate);
        headShotBuff.floatParam.add(criticalDam);


        skillUser.buffActionHistoryComponent.conditionHistory.add(headShotBuff);



        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 쿨타임을 초기화한다 */
        skillToUse.remainCoolTime = skillToUse.skillinfo.skillCoolTime;


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.ARCHER_HEAD_SHOT;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = skillInfo.durationTime;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);
    }

    /**
     * 범위 내 주변의 모든 캐릭터(아군)들에게 크리티컬 버프를 걸어주는 것임
     *
     * @param worldMap
     * @param event
     */
    public static void useSkill_archerCriticalHit(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;


        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }

        /* 스킬 사용 처리를 한다 */




        /** 버프 정보 구성 */

        BuffAction criticalBuff = new BuffAction();
        criticalBuff.unitID = skillUser.entityID;
        criticalBuff.skillUserID = skillUser.entityID;
        criticalBuff.remainTime = skillInfo.durationTime;
        criticalBuff.coolTime = -1f;
        criticalBuff.remainCoolTime = -1f;

        criticalBuff.floatParam.add(new ConditionFloatParam(ConditionType.criticalChanceRate, skillInfo.criticalChanceRate + 100f));
        criticalBuff.floatParam.add(new ConditionFloatParam(ConditionType.criticalDamageRate, skillInfo.criticalDamageRate));


        /** 2020 03 12 */
        criticalBuff.skillType = SkillType.ARCHER_CRITICAL_HIT;
        criticalBuff.buffDurationTime = criticalBuff.remainTime;


        /** 타겟을 검색한다 */
        /**
         * 모든 캐릭터 앤티티에 대해
         * 0) 죽은 대상은 제외한다
         * 1) 대상이 스킬 사거리 내에 존재하는지 판단한다
         *
         */

        for(HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();

            /** 죽어있거나, 타게팅 불가능한 대상은 제외한다 */
            HPComponent targetHP = character.hpComponent;
            ConditionComponent targetCondition = character.conditionComponent;

            boolean targettingDisable = (targetHP.currentHP <= 0f) ? true : false;
            if(targettingDisable){
                continue;
            }

            /** 대상이 스킬 사거리 내에 존재하는지 판단한다 */

            float skillRange = skillInfo.range;
            Vector3 targetPos = character.positionComponent.position;
            Vector3 skillUserPos = skillUser.positionComponent.position;

            /* 대상과 스킬 시전자(스킬이 시전되는 위치) 사이의 거리를 구함 */
            float targetDistance = Vector3.distance(skillUserPos, targetPos);
            if(targetDistance > skillRange){
                continue;
            }


            /** 타겟에게 효과를 걸어준다 */

            character.buffActionHistoryComponent.conditionHistory.add(criticalBuff);

        }




        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 쿨타임을 초기화한다 */
        skillToUse.remainCoolTime = skillToUse.skillinfo.skillCoolTime;


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.ARCHER_CRITICAL_HIT;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.1f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);
    }

    /**
     * 궁수 폭풍의 시
     * @param worldMap
     * @param event
     */
    public static void useSkill_archerStorm(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;

        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillInfo.requireMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){

            // 흠..
            if(!hasEnoughMP){   // 마력이 부족해서 스킬을 중단해야 하는 경우

                /* 스킬 쿨타임을 초기화한다 */
                skillToUse.remainCoolTime = skillToUse.skillinfo.skillCoolTime;
            }

            return;
        }


        /**
         * 아 이거 조금 애매하네..
         * 스킬 사용 가능 MP를, 매번 깎아줄 MP 양으로 해줘야 할지, 아니면 최소 요구사항이 사실 50mp인 거여서, 일단 그만큼 있을 경우에만
         *  스킬 사용 가능! 한 다음에, 그 이상일 경우에 스킬 사용 단위 mp를 깎아주는 식??
         * 아니면 아예 처음부터 reqMP 를 단위 MP로 넣어줄지...
         * ㄴ 이러면 조금 걱정되는게, 마력이 없어서 스킬 사용이 안되는 경우에도 유저가 손을 떼지 않고 있다가, (이러면 쿨타임은 영원히 0)
         *      마력이 차면 그 순간... >> 아니 근데 이런 경우에는, 애초에 스킬 사용 요청 자체가 안가잖아?
         *      그러면 마력이 있고 쿨타임이 0이고를 떠나서, 스킬 사용 처리가 안될 것임 그러면 유저는 손을 뗄 수 밖에 없고
         *      떼면 그 순간 스탑 요청이 가서, 쿨타임을 적용할 거고..
         *      아니그러면 또..! 머여 스킬 끝난지 쿨타임 한참 지났는데 왜 또 기다려돼 하는 상황이 발생할 수도 있겠네 흠..
         *
         *      마력때문에 스킬사용을 중단해야 하는 경우에도, 쿨타임을 세탕해 준 다음에
         *      중단 요청을 처리하는 시점에서, 이미 쿨타임이 0 이상이라면
         *          이전에 적용해 준 걸로 치고, 걍 PASS 이렇게 처리해줘야 하나
         *
         *      >> 그래 일단 이렇게 하는걸로
         */

        if(event.remainCoolTime > 0){

            event.remainCoolTime -= worldMap.tickRate * 0.001f;
            worldMap.enqueueClientAction(event);
            return;
        }


        // 스킬 사용이 가능하므로, 마력 차감
        skillUser.mpComponent.currentMP -= skillToUse.skillinfo.reqMP;



        /* 스킬 사용 처리를 한다 */

        /**
         * 스킬 처리가 가능한 경우 ( 마력 충분, 스킬 쿨타임 0) ,
         *      1. 투사체 생성
         *      2. 같은 이벤트를 action 목록에 넣어줌
         */

        /** 투사체 생성 */
        int flyingObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

        /* position component */
        PositionComponent positionComponent;
        positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());

        // 투사체의 시작지점 높이 설정해주기 >> doAttack()의 투사체 설정값을 따름. >> 이거 상수화하면 좋을거같은데
        Vector3 position = positionComponent.position;
        position.set(position.x() + 0.188f, position.y() + 1.5f, position.z() + 0.992f);


        /* flyingObject component >> 평타 투사체 값을 따름 */

        int createdSkillType = skillToUse.skillinfo.skillType;

        float flyingObjectRadius = 2.5f; // 괜찮은 값인지...!!
        float flyingObjectSpeed = skillInfo.flyingObjectSpeed;

        float remainDistance = skillInfo.range; // 아맞다 range들 범위 수정 해줘야 됨 * 0.01f

        Vector3 startPosition;
        startPosition = (Vector3) positionComponent.position.clone();

        // 투사체가 날아갈 방향 ; 논타겟임
        Vector3 direction = event.skillDirection;

        System.out.println("폭시 투사체 생성시 방향 : " + event.skillDirection.x() + ", " +
                event.skillDirection.y() + ", " + event.skillDirection.z() + ", ");



        /* 데미지 버프 */
        float skillDefaultDamage = skillInfo.attackDamage;
        ConditionFloatParam damageParam = createDamageParam(skillDefaultDamage, userAttack, skillUserCondition);
        BuffAction stormBuff = createDamageBuff(damageParam, flyingObjectEntityID, skillUser.entityID);


        /* FlyingObject Component */
        FlyingObjectComponent flyingObjectComponent
                = new FlyingObjectComponent(skillUser.entityID,startPosition, direction, flyingObjectSpeed, flyingObjectRadius, remainDistance, stormBuff, 0);
        flyingObjectComponent.beDestroyedByCrash = true;
        flyingObjectComponent.createdSkillType = createdSkillType;


        /* FlyingObject Entity */
        FlyingObjectEntity flyingObjectEntity
                = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
        flyingObjectEntity.entityID = flyingObjectEntityID;


        /* 생성요청 큐에 넣기 */
        worldMap.requestCreateQueue.add(flyingObjectEntity);


        /** 스킬 지속 처리 */

        // 아 추가할 거 생각났다.
        // 유저가 스킬을 계속 시전하는 동안 이동이 가능하므로, 도중에 방향을 바꾸지 않는다는 보장이 없잖아?
        // 그래서.. 아래 이벤트를 큐에 넣어주기 전에, 이벤트 발생 direction을 유저가 바라보는 방향으로 바꿔서 넣어줘야 할듯

        // event.skillDirection = skillUser.velocityComponent.velocity;    // 이렇게 해도 될려나??

        /** 2020 02 20 목 권령희 */
        event.remainCoolTime = 0.1f;
        worldMap.enqueueClientAction(event);




        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.ARCHER_STORM;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);


        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.1f;
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);
    }

    /**
     * 난사
     * @param worldMap
     * @param event
     */
    public static void useSkill_archerFire(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;
        AttackComponent userAttack = skillUser.attackComponent;


        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }


        /* 스킬 사용 처리를 한다 */

        /**
         * 스킬 시전 후 이 매서드에서 처리해 줄 내용은
         *      스킬 지속시간동안 시전자의 '난사스킬' 상태를 활성화해주는 버프를 넣어주는 것이다.
         *
         * 난사 스킬의 효과는
         *      스킬 지속 시간동안, 평타 공격 시 평타 범위 내 모든 적들에게 평타공격을 한다. (평타 화살을 날린다)
         *
         * '난사스킬' 상태가 활성화되면, 평타 공격 시 위 처리를 위한 별도 매서드를 대신 호출한다.
         *  매서드 처리 내용 :
         *
         *      모션을 중계한다. (모션 타입 ; attack_fire 혹은 archerFireAttack 등등.. )
         *      전체 몹 목록에 대해 반복한다.
         *          몹의 상태, 몹과의 거리 등을 바탕으로, 타게팅 가능 여부를 판단한다
         *          타게팅 가능한 경우
         *              해당 몹을 타겟으로 한 투사체를 생성한다
         *
         *
         * 일부 스킬 활성화 상태에서 평타 공격을 하는 경우의 처리 루틴 :
         *
         *      0. 클이언트가 doAttack RMI를 호출한다
         *      1. ActionUseAttack 이벤트가 생성된다
         *      2. 스킬 팩토리의 doAttack() 매서드가 호출된다
         *      =============== 일반공격 쿨타임 등등 공격 가능 여부를 판단 및 쿨타임 처리 후, ===================
         *      3. 시전자의 상태 컴포넌트를 확인한다
         *      4. 특정 스킬이 활성화되어있는 경우, 해당 처리를 위한 별도 매서드를 호출한다.
         *          그렇지 않은 경우는 기존의 평타 루틴을 수행하도록 한다.
         *
         * 아 만약에 여러 스킬이 동시에 활성화된 경우가 좀 걸리네..
         * 스킬 조합에 따라 여러가지 경우의 수가 나올텐데, 그럴때마다 매번 매서드 만들어주기도 그렇고..
         * ㄴ 이런 경우의 수도.. 설계 때 하나하나 다 따져봐야겠지??
         * 그냥 무작정 효과중첩 못하는걸로 싸잡아 결정하는 것보다
         * 어느 정도까지 중첩을 허용할 것인지, 허용하지 않을 스킬이나 효과가 있는지도 하나하나 다 결정해줘야 할듯
         *
         * 음.. 다중공격 vs 단일공격 같은 경우는 어케보면 쉽게 제어할 수 있을지도?? 그냥 공격가능 모드를 단일/복수 둬서,
         * 난타 효과 적용에 따라 저 값을 설정해주고, 저 설정값에 따라 타겟 설정, 효과부여 하도록 하면 하나의 루틴으로 통합할 수 있을거같고.
         * 오히려 문제?가 되는거는 헤드샷이라던가 광전사라던지.. 한번하고 끝나는 예외처리같은 경우.. 이런거는 또 어케 하나의 루틴으로
         * 잘 통합시키지???
         */


        /* 유저에게 스킬 버프 적용하기 */

        BuffAction fireBuff = new BuffAction();
        fireBuff.unitID = skillUser.entityID;
        fireBuff.skillUserID = skillUser.entityID;
        fireBuff.remainTime = skillInfo.durationTime;
        fireBuff.coolTime = -1f;
        fireBuff.remainCoolTime = -1f;

        fireBuff.boolParam.add(new ConditionBoolParam(ConditionType.isArcherFireActivated, true));

        /** 2020 03 12 */
        fireBuff.skillType = SkillType.ARCHER_FIRE;
        fireBuff.buffDurationTime = fireBuff.remainTime;

        skillUser.buffActionHistoryComponent.conditionHistory.add(fireBuff);

        /*
        * 스킬타입별 투사체 조건이라던가 뭐 그런거를 별도로 만들어두고, 여기서 필요한 값들 적용해둔 다음에
        * 그런거는 스킬Info에 넣어둬도 괜찮을듯.. 아니면 걍 팩토리같은거에 등록해두고, 필요할 때 마다 가져와서
        * 쓰는거지 이것도 지금 막 적용하기보다는 그냥 여러개 적용해보는 용도로.
        *
        */



        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 쿨타임을 초기화한다 */
        skillToUse.remainCoolTime = skillToUse.skillinfo.skillCoolTime;


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.ARCHER_FIRE;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.1f;      // 애니메이션 재생 속도가 얼마나 될지 모르겟지만.. 0.2초동안 지속되도록 함 문제있음 나중에 수정
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);
    }

    /**
     * 저격
     * @param worldMap
     * @param event
     */
    public static void useSkill_archerSnipe(WorldMap worldMap, ActionUseSkill event){

        /* 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillToUse = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);
        if(skillToUse == null){
            return;
        }

        int skillType = skillToUse.skillinfo.skillType;
        SkillInfoPerLevel skillInfo = skillLevelTable.get(skillType).get(skillToUse.skillLevel);

        ConditionComponent skillUserCondition = skillUser.conditionComponent;


        /* 스킬 시전 가능 여부를 판단한다 */

        //스킬 타입에 따라, 시전 가능 조건이 달라질 수 있어 함수화 하지 않음.
        boolean isCoolTimeZero = (skillToUse.remainCoolTime <= 0) ? true : false;
        boolean hasEnoughMP = (skillUser.mpComponent.currentMP >= skillToUse.skillinfo.reqMP) ? true : false;
        boolean isAbleToUseSkill = ((!skillUserCondition.isDisableSkill) && isCoolTimeZero && hasEnoughMP) ? true : false;
        if(!isAbleToUseSkill){
            return;
        }

        /* 스킬 사용 처리를 한다 */

        /** 투사체를 생성한다 */

        int flyingObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

        /* position component */
        PositionComponent positionComponent;
        positionComponent = new PositionComponent((Vector3) skillUser.positionComponent.position.clone());

        // 투사체의 시작지점 높이 설정해주기 >> doAttack()의 투사체 설정값을 따름. >> 이거 상수화하면 좋을거같은데
        Vector3 position = positionComponent.position;
        position.set(position.x() + 0.188f, position.y() + 1.5f, position.z() + 0.992f);


        /* flyingObject component >> 평타 투사체 값을 따름 */

        int createdSkillType = skillToUse.skillinfo.skillType;

        float flyingObjectRadius = 10f;
        float flyingObjectSpeed = skillInfo.flyingObjectSpeed;

        float remainDistance = skillInfo.range; // 아맞다 range들 범위 수정 해줘야 됨 * 0.01f

        Vector3 startPosition;
        startPosition = (Vector3) positionComponent.position.clone();

        // 투사체가 날아갈 방향 ; 논타겟임
        Vector3 direction = event.skillDirection;


        /* 버프 */

        BuffAction snipeBuff = new BuffAction();
        snipeBuff.unitID = flyingObjectEntityID;
        snipeBuff.skillUserID = skillUser.entityID;
        snipeBuff.remainTime = 0.5f;
        snipeBuff.remainCoolTime = -1f;
        snipeBuff.coolTime = -1f;
        // 위 지속시간 및 쿨타임 등은, 상태이상 버프를 기준으로 함

        /* 1차 댐 - 크리티컬 */
        // 스킬댐 * ( 1 + 크리댐 퍼센테이지(1~ ) )
        float skillDefaultDamage = skillInfo.attackDamage;
        ConditionFloatParam criticalDamageParam = new ConditionFloatParam(ConditionType.criticalDamageAmount, skillDefaultDamage);


        /* 2차 댐 - 크리 추가 */  // 투사체 적중 시, 우선 1차 댐이 바로 들어가도록 하고, 2차는 0.1초 뒤에 들어가게끔 버프 만들어서 줄 것.
        // 흠.. 어차피 당연하게 들어갈 추가 댐이라면, 그리고 그 대미지가 위 1차 값의 영향을 받는다면, 굳이 이렇게 안넣어줘도
        // 어차피 투사체 시스템에서 스킬타입 구분해서 처리해 줄 거니까
        // 거기서 2차 댐 값을 계산해줘도 딱히 문제는 없을거 같기도 하네..
        // 그리고 보스인지 여부는 투사체 시스템에서 충돌 시에 알 수 있는데, 만약 보스라면 1차댐에 두 배 해줘야 하....
        //
        float criticalBonusDam = skillDefaultDamage * skillInfo.criticalBonusDamageRate * 1.01f; // 이 공식이 아니라면 수정..
        ConditionFloatParam criticalBonusDamageParam = new ConditionFloatParam(ConditionType.criticalDamageAmount, criticalBonusDam);

        // 보스인 경우
        float bossCriticalDamage = skillInfo.attackDamage * 2;
        ConditionFloatParam bossDamageParam = new ConditionFloatParam(ConditionType.criticalDamageAmount, bossCriticalDamage);

        float bossCriticalBonusDam = bossCriticalDamage * skillInfo.criticalBonusDamageRate * 1.01f;
        ConditionFloatParam bossBonusDamageParam = new ConditionFloatParam(ConditionType.criticalDamageAmount, bossCriticalBonusDam);


        snipeBuff.floatParam.add(criticalDamageParam);
        snipeBuff.floatParam.add(criticalBonusDamageParam);
        snipeBuff.floatParam.add(bossDamageParam);
        snipeBuff.floatParam.add(bossBonusDamageParam);



        /* 상태이상 버프 */   // 상태 이상은, 일단 크리티컬 1차 댐이 들어가는 타이밍에 같이 적용되도록 함.
        snipeBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
        snipeBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));
        snipeBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));



        /* FlyingObject Component */
        FlyingObjectComponent flyingObjectComponent
                = new FlyingObjectComponent(skillUser.entityID,startPosition, direction, flyingObjectSpeed, flyingObjectRadius, remainDistance, snipeBuff, 0);
        flyingObjectComponent.beDestroyedByCrash = true;
        flyingObjectComponent.createdSkillType = createdSkillType;


        /* FlyingObject Entity */
        FlyingObjectEntity flyingObjectEntity
                = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
        flyingObjectEntity.entityID = flyingObjectEntityID;


        /** 생성요청 큐에 넣기 */
        worldMap.requestCreateQueue.add(flyingObjectEntity);




        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 스킬 쿨타임을 초기화한다 */
        skillToUse.remainCoolTime = skillToUse.skillinfo.skillCoolTime;


        /* 스킬 모션 중계 */

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.ARCHER_SNIPE;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);



        /* 스킬 사용 후 시전자에게 걸어줄 상태처리를 한다 (이동/스킬사용 등등) */

        BuffAction afterUsing = new BuffAction();
        afterUsing.unitID = skillUser.entityID;     // 스킬 시전자가 본인에게 거는 것.
        afterUsing.skillUserID = skillUser.entityID;
        afterUsing.remainTime = 0.2f;
        afterUsing.remainCoolTime = -1f;    // 남은 쿨타임 없음
        afterUsing.coolTime = -1f;  // 쿨타임 없음

        // 테스트 후 필요없는거 지울 것
        afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableSkill, true));   // 스킬사용 못하게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));    // 못움직에게 막음
        //afterUsing.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));  // 공격 못하게 막음

        skillUser.buffActionHistoryComponent.conditionHistory.add(afterUsing);
    }





    /*******************************************************************************************************************/

    /**
     * 넘겨받은 유저 정보와 스킬 슬롯 번호를 가지고, 해다 유저의 slotNum번째 슬롯을 찾음
     * @param slotNum
     * @param skillUser
     * @return
     */
    public static SkillSlot findSkillSlotBySlotNum(int slotNum, CharacterEntity skillUser){

        SkillSlot slot = null;

        List<SkillSlot> skillSlots = skillUser.skillSlotComponent.skillSlotList;

        for(int i=0; i<skillSlots.size(); i++){

            if(skillSlots.get(i).slotNum == slotNum){
                slot = skillSlots.get(i);
                break;
            }

        }

        return slot;

    }


    /**
     * 지정된 타입의 상태이상 효과를 취소시키는 함수 (디버프 효과인 경우에만)
     * @param character
     * @param conditionType
     */
    public static void cancelDeBuffEffect(CharacterEntity character, int conditionType){

        /* 건네받은 상태 컴포넌트에서, 지정된 상태와 관련된 버프를 찾아 제거한다 */
        // 이동 관련 상태가 포함된 "버프" 전체를 없애야 할지.. 아니면 해당 버프 중에서 이동 관련 효과만 지워야 하는지..
        // 개복잡하니까 일단은 그 효과'만' 지우는 걸로?? 이거는 나중에 얘기를 해 봐야 할듯..

        ArrayList<BuffAction> userBuffList = character.buffActionHistoryComponent.conditionHistory;

        BuffAction buff;
        for(int i=0; i< userBuffList.size(); i++ ){

            buff = userBuffList.get(i);

            ArrayList<ConditionBoolParam> boolParams = buff.boolParam;
            for(int j=0; j<boolParams.size(); j++){

                int condType = boolParams.get(j).type;
                if(condType == conditionType){

                    if(boolParams.get(j).value == true){
                        boolParams.remove(j--);

                        System.out.println("디버프 지움");
                    }
                }

            }

            ArrayList<ConditionFloatParam> floatParams = buff.floatParam;
            for(int j=0; j<floatParams.size(); j++){

                int condType = floatParams.get(j).type;
                if(condType == conditionType){

                    if(floatParams.get(j).value < 0){
                        boolParams.remove(j--);
                        System.out.println("디버프 지움");
                    }
                }
            }


        }

    }


    /**
     * 지정된 타입의 스킬의 효과를 취소시키는 함수
     */
    public static void cancelSkillBuffEffect(CharacterEntity character, int skillType){

        /* 건네받은 상태 컴포넌트에서, 지정된 스킬와 관련된 버프를 찾아 제거한다 */
        // 이동 관련 상태가 포함된 "버프" 전체를 없애야 할지.. 아니면 해당 버프 중에서 이동 관련 효과만 지워야 하는지..
        // 개복잡하니까 일단은 그 효과'만' 지우는 걸로?? 이거는 나중에 얘기를 해 봐야 할듯..

        ArrayList<BuffAction> userBuffList = character.buffActionHistoryComponent.conditionHistory;

        BuffAction buff;
        for(int i=0; i< userBuffList.size(); i++ ){

            buff = userBuffList.get(i);

            if(buff.skillType == skillType){

                userBuffList.remove(buff);
                i--;
                System.out.println("버프 삭제함 ");
            }
        }

    }

    /**
     * 스킬 사용 중단 함수 - 스킬을 계속 사용하는 데 있어 별도 쿨타임을 적용하지 않는 경우
     * event에 지정되어 있는 유저의 특정 스킬 사용을 중단 처리 한다
     * ㄴ 스킬 타입에 따라 중단 처리가 각각 달라질 수 있는데..
     *      일단 중단 처리가 필요한 스킬은 궁수 폭풍의 시 하나고.
     *      얘의 경우, 스킬 쿨타임을 초기화해주면 됨.
     * @param worldMap
     * @param event
     */
    public static void stopUsingSkill(WorldMap worldMap, ActionStopUsingSkill event){

        /** 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        /** 2020 03 20 권령희 수정 for 귀환 */

        int skillType = -1;
        SkillInfoPerLevel skillInfo = null;

        SkillSlot skillToStop = findSkillSlotBySlotNum(event.skillSlotNum, skillUser);

        if(skillToStop == null){    /* 해당 스킬 슬롯을 찾지 못했는데 */

            if(event.skillSlotNum == 100){  // 스킬 요청이 귀환인 경우

                skillType = SkillType.RECALL;
            }
            else{   // 귀환도 아니라면, 잘못된 요청으로 간주하고 리턴함

                return;
            }
        }
        else{   /* 스킬 슬롯이 존재하는 경우 */

            skillType = skillToStop.skillinfo.skillType;
            skillInfo = skillLevelTable.get(skillType).get(skillToStop.skillLevel);

        }


        RMI_ID[] TARGET;

        /**  스킬 타입에 따른 중단 처리 */

        switch ( skillType ){

            /* 궁수 폭풍의 시 */
            case SkillType.ARCHER_STORM :

                if(skillToStop.remainCoolTime <= 0){
                    skillToStop.remainCoolTime = skillInfo.coolTime;
                }

                SkillInfoData ARCHER_STORM = new SkillInfoData();
                ARCHER_STORM.skillType = skillType;

                TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
                server_to_client.motionCharacterCancelSkill(TARGET, RMI_Context.Reliable, skillUser.entityID, ARCHER_STORM);
                break;


            case SkillType.KNIGHT_GARREN_E :

                SkillInfoData KNIGHT_GARREN_E = new SkillInfoData();
                KNIGHT_GARREN_E.skillType = skillType;

                TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
                server_to_client.motionCharacterCancelSkill(TARGET, RMI_Context.Reliable, skillUser.entityID, KNIGHT_GARREN_E);
                break;

            case SkillType.MAGICIAN_FROZEN_BEAM :

                SkillInfoData MAGICIAN_FROZEN_BEAM = new SkillInfoData();
                MAGICIAN_FROZEN_BEAM.skillType = skillType;

                TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
                server_to_client.motionCharacterCancelSkill(TARGET, RMI_Context.Reliable, skillUser.entityID, MAGICIAN_FROZEN_BEAM);
                break;

            /** 2020 03 20 권령희 추가 for 귀환 */
            case SkillType.RECALL :

                if(skillUser.conditionComponent.isReturning){

                    /* 귀환 처리 ; 받고 있는 버프 목록 중에서, 귀환 처리를 삭제하는 처리를.. */
                    cancelSkillBuffEffect(skillUser, SkillType.RECALL);

                    SkillInfoData RECALL = new SkillInfoData();
                    RECALL.skillType = skillType;

                    TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
                    server_to_client.motionCharacterCancelSkill(TARGET, RMI_Context.Reliable, skillUser.entityID, RECALL);

                    skillUser.conditionComponent.isReturning = false;
                }


                break;
        }


    }


    /**
     * 스킬 사용 중단 함수 - 스킬이 지속되는 데 있어 쿨타임 개념이 적용되는 경우
     * 기획에 따라 사용 안할수도 있는데, 일단 만들어 둠
     * @param worldMap
     * @param event
     */
    public static void stopUsingSkill_(WorldMap worldMap, ActionStopUsingSkill event){


    }


    /**
     *
     * @param worldMap
     * @param attacker
     */
    public static void doAttack_onArcherFire(WorldMap worldMap, CharacterEntity attacker){

        /* 모션을 중계한다 */  // 일단 매서드 호출 직전 밖에서 해주긴 했는데 만약 필요하다면 이쪽으로 옮길 것.
        // ...

        /* 전체 몹에 대해 반복한다 */
        for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()){

            MonsterEntity monster = monsterEntity.getValue();

            /* 타게팅 가능 여부를 판단 */

            /** 죽어있거나, 타게팅 불가능한 대상은 제외한다 */
            HPComponent targetHP = monster.hpComponent;
            ConditionComponent targetCondition = monster.conditionComponent;

            boolean targettingDisable = ((targetHP.currentHP <= 0f) || (targetCondition.isUnTargetable == true)) ? true : false;
            if(targettingDisable){
                continue;
            }

            /** 대상이 스킬 사거리 내에 존재하는지 판단한다 */

            Vector3 targetPos = monster.positionComponent.position;
            Vector3 attackerPos = attacker.positionComponent.position;

            float targetDistance = Vector3.distance(attackerPos, targetPos); // 대상과 스킬 시전자(스킬이 시전되는 위치) 사이의 거리를 구함
            if(targetDistance > attacker.attackComponent.attackRange){
                continue;
            }

            /* 투사체 준비 */
            FlyingObjectComponent flyingObjComponent = initFlyingObjInfo_ArcherFire(attacker);

            System.out.println("궁수 난사 타겟 지정됨 : 몬스터 " + monster.entityID);

            /* 타게팅 가능한 경우, 해당 몹을 타겟으로 하는 투사체를 생성 */
            int flyingObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

            flyingObjComponent.targetEntityID = monster.entityID;

            flyingObjComponent.direction = Vector3.getTargetDirection(attackerPos, targetPos);



            /* 평타 vs 치명타 결정 */
            int max = 99;
            int min = 0;
            int randomValue = (int)(Math.random()*((max-min)+1))+min;
            float criticalChance = attacker.attackComponent.criticalChance + attacker.conditionComponent.criticalChanceRate + randomValue;

            /* 평타, 치명타 여부에 따른 처리 */
            boolean isCriticalAttack = (criticalChance >= 100f) ? true : false;
            /*if(isCriticalAttack){

                //원래 공격력 + 보너스 공격력
                float sumDamage = attacker.attackComponent.attackDamage + attacker.conditionComponent.attackDamageBonus;
                //위의 수치에 공격력 증가비율 적용
                float rateDamage = sumDamage * attacker.conditionComponent.attackDamageRate;

                float criticalDamage
                        = (attacker.attackComponent.criticalDamage * 0.01f) + attacker.conditionComponent.criticalDamageRate;
                // 컨디션 컴포넌트의 criticalDamageRate는 디폴트값이 1, 여기에 추가 적용된 수치들이 * 0.01f 연산된 값이 붙는다.
                // 어택 컴포넌트의 크리티컬데미지는 레벨1 기준 50으로 시작한다.

                float dmgAmount = rateDamage * criticalDamage;

                BuffAction criticalDmg = new BuffAction();

                criticalDmg.skillUserID = attacker.entityID;
                criticalDmg.remainTime = 0.15f;
                criticalDmg.coolTime = -1f;
                criticalDmg.remainCoolTime = -1f;
                criticalDmg.floatParam.add(new ConditionFloatParam(ConditionType.criticalDamageAmount, dmgAmount));

                flyingObjComponent.buffAction = criticalDmg;

            }
            else {

                //원래 공격력 + 보너스 공격력
                float sumDamage = attacker.attackComponent.attackDamage + attacker.conditionComponent.attackDamageBonus;
                //위의 수치에 공격력 증가비율 적용
                float rateDamage = sumDamage * attacker.conditionComponent.attackDamageRate;

                BuffAction flatDmg = new BuffAction();

                flatDmg.skillUserID = attacker.entityID;
                flatDmg.remainTime = 0.15f;
                flatDmg.coolTime = -1f;
                flatDmg.remainCoolTime = -1f;
                flatDmg.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, rateDamage));

                flyingObjComponent.buffAction = flatDmg;

            }*/

            /** 2020 02 20 */
            float attackerDefaultDamage = attacker.attackComponent.attackDamage;

            // createDamageParam 매서드 내에서, 평타&치명타 판정이 일어난다.
            ConditionFloatParam damageParam = createDamageParam(attackerDefaultDamage, attacker.attackComponent, attacker.conditionComponent);

            // 위에서 얻은 데미지 타입을 가지고, 데미지 버프를 생성한다.
            BuffAction damageBuff = createDamageBuff(damageParam, flyingObjectEntityID, attacker.entityID);

            flyingObjComponent.buffAction = damageBuff;
            flyingObjComponent.createdSkillType = SkillType.ARCHER_FIRE;

            //flyingObjComponent.buffAction.unitID = flyingObjectEntityID;
            PositionComponent foPos = new PositionComponent(attackerPos.x(), attackerPos.y(), attackerPos.z());

            FlyingObjectEntity flyingObject
                    = new FlyingObjectEntity(foPos, flyingObjComponent);
            flyingObject.entityID = flyingObjectEntityID;
            flyingObject.flyingObjectComponent.beDestroyedByCrash = true;
            flyingObject.flyingObjectComponent.userEntityID = attacker.entityID;

            flyingObject.positionComponent.position.set(
                    flyingObject.positionComponent.position.x()+ 0.188f,
                    attackerPos.y() + 1f,
                    flyingObject.positionComponent.position.z()+ 0.992f);



            worldMap.requestCreateQueue.add(flyingObject);

            System.out.println("ㅇㅇㅇㅇㅇ");

            RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
            server_to_client.motionCharacterDoAttack(TARGET, RMI_Context.Reliable_Public_AES256, attacker.entityID, monster.entityID);
        }


        /* 공격 쿨타임 초기화 */
        attacker.attackComponent.remainCoolTime = ( 1f / attacker.attackComponent.attackSpeed );


    }


    public static FlyingObjectComponent initFlyingObjInfo_ArcherFire(CharacterEntity attacker){

        /* position component */
        PositionComponent positionComponent;
        positionComponent = new PositionComponent((Vector3) attacker.positionComponent.position.clone());

        /* flyingObject component */
        //int createdSkillType = SkillType.ARCHER_FIRE;
        int createdSkillType = SkillType.ARCHER_NORMAL_ATTACK;

        float flyingObjectRadius = 2.5f;
        float flyingObjectSpeed = 20f;


        Vector3 startPosition;
        startPosition = (Vector3) positionComponent.position.clone();


        // 투사체 버프
        BuffAction flyingObjCondBuff = new BuffAction();

        /*flyingObjCondBuff.skillUserID = attacker.entityID;
        flyingObjCondBuff.remainTime = 0.15f;
        flyingObjCondBuff.remainCoolTime = -1f;
        flyingObjCondBuff.coolTime = -1f;
        flyingObjCondBuff.floatParam.add(new ConditionFloatParam(ConditionType.damageAmount, attacker.attackComponent.attackDamage));*/


        /* FlyingObject Component */
        FlyingObjectComponent flyingObjectComponent
                = new FlyingObjectComponent(createdSkillType, attacker.entityID, flyingObjectSpeed, flyingObjectRadius,
                startPosition, new Vector3(), 0f, flyingObjCondBuff, 0);
        flyingObjectComponent.beDestroyedByCrash = true;


        return flyingObjectComponent;

    }


    /* 일단 안씀 */
    public static void createFlyingObj_ArcherFire(FlyingObjectComponent component, MonsterEntity targetMob, WorldMap worldMap){

        int flyingObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

        /* 타겟 정보를 가지고, 방향 및 타겟 정보를 설정 */


        FlyingObjectEntity flyingObject = new FlyingObjectEntity();



    }

    /**
     * 헤드샷이 적용된 평타 처리
     */
    public static void doAttack_onArcherHeadShot(WorldMap worldMap, CharacterEntity attacker, ActionUseAttack event){

        System.out.println("헤드샷 적용된 두어택 ");


        /** 스킬 사용에 필요한 정보들을 찾는다 ; 슬롯, 유저, 스킬정보 등등 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.attackerEntityID);
        if(skillUser == null){
            return;
        }

        SkillSlot skillUsed = findSkillSlotBySkillType(worldMap, skillUser, SkillType.ARCHER_HEAD_SHOT);
        if(skillUsed == null){
            return;
        }


        SkillInfoPerLevel skillInfo = skillLevelTable.get(SkillType.ARCHER_HEAD_SHOT).get(skillUsed.skillLevel);


        /** 투사체를 생성한다 */

        int flyingObjectEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

        /* position component */
        PositionComponent positionComponent;
        positionComponent = new PositionComponent((Vector3) attacker.positionComponent.position.clone());

        // 투사체의 시작지점 높이 설정해주기 >> doAttack()의 투사체 설정값을 따름. >> 이거 상수화하면 좋을거같은데
        Vector3 position = positionComponent.position;
        position.set(position.x() + 0.188f, position.y() + 1.5f, position.z() + 0.992f);


        /* flyingObject component >> 평타 투사체 값을 따름 */

        int createdSkillType = SkillType.ARCHER_HEAD_SHOT;

        float flyingObjectRadius = 2.5f;
        float flyingObjectSpeed = 20f;

        Vector3 startPosition;
        startPosition = (Vector3) positionComponent.position.clone();

        // 투사체가 날아갈 방향
        int targetEntityID = event.targetEntityID;
        MonsterEntity targetMob = worldMap.monsterEntity.get(targetEntityID);

        Vector3 targetPos = (Vector3) targetMob.positionComponent.position.clone();
        targetPos.set(targetPos.x(), 1.5f, targetPos.y());


        /* 버프 */

        BuffAction headShotBuff = new BuffAction();
        headShotBuff.unitID = flyingObjectEntityID;
        headShotBuff.skillUserID = attacker.entityID;

        headShotBuff.remainTime = 0.5f;
        headShotBuff.coolTime = -1f; // 의미없는 값
        headShotBuff.remainTime = -1f;   // 의미없는 값

        ConditionFloatParam criticalParam = new ConditionFloatParam(ConditionType.criticalDamageAmount, skillInfo.attackDamage);
        headShotBuff.floatParam.add(criticalParam);


        // 어.. 헐. 상태(이동불가 뭐 그런) 처리도 줘야할텐데?? 시간값을 어케 줘야..
        // ==>> 잠깐이면 되지 않을까?? 어차피 오랫동안 줄 버프도 아니고 타격효과 위해서 잠시 못움직이게 하는 것뿐이니까
        //      어차피 위에서.. 쿨타임도 0.1로 줬고, 아래 버프가 지속될 시간을 remainTime으로 주면 되겠고.
        //      리멘쿨타임이 0.1 즉 투사체가 충돌한 시점에서 바로 적용되는게 아니라는게 좀 걸리긴 하는데
        //      엄청 크게 문제가 될것같진 않지만.. 정 안되면 저것도 쪼개야지 뭐
        //      2차 대미지도 별도 버프로 분리해서 걔를 0.1f로 주고, 아래 상태이상의 경우에는
        //          노 쿨타임, 노 리멘쿨타임, 위의 리멘타임을 상태이상의 리메인 타임으로 주면.. 쪼개기 넘하네..
        headShotBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableAttack, true));
        headShotBuff.boolParam.add(new ConditionBoolParam(ConditionType.isDisableMove, true));


        /* FlyingObject Component */
        FlyingObjectComponent flyingObjectComponent
                = new FlyingObjectComponent(skillUser.entityID,startPosition, targetPos, flyingObjectSpeed, flyingObjectRadius, 0f, headShotBuff, targetEntityID);
        flyingObjectComponent.beDestroyedByCrash = true;
        flyingObjectComponent.createdSkillType = createdSkillType;

        /* FlyingObject Entity */
        FlyingObjectEntity flyingObjectEntity
                = new FlyingObjectEntity(positionComponent, flyingObjectComponent);
        flyingObjectEntity.entityID = flyingObjectEntityID;



        /** 생성요청 큐에 넣기 */
        worldMap.requestCreateQueue.add(flyingObjectEntity);


        /** 스킬 쿨타임 등 시전자에 필요한 처리를 한다 */

        /* 공격 쿨타임 초기화 */
        attacker.attackComponent.remainCoolTime = ( 1f / attacker.attackComponent.attackSpeed );


        /* 스킬 모션 중계 */

        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterDoAttack(TARGET, RMI_Context.Reliable_Public_AES256, attacker.entityID, event.targetEntityID);
    }


    /*******************************************************************************************************************/

    /**
     * 평타인지, 치명타인지 결정
     * @param attackComponent
     * @param conditionComponent
     * @return
     */
    public static int decideDamageType(AttackComponent attackComponent, ConditionComponent conditionComponent){

        /* 평타 vs 치명타 결정 */
        int max = 99;
        int min = 0;
        int randomValue = (int)(Math.random()*((max-min)+1))+min;
        float criticalChance = attackComponent.criticalChance + conditionComponent.criticalChanceRate + randomValue;

        /* 평타, 치명타 여부에 따른 처리 */
        boolean isCriticalAttack = (criticalChance >= 100f) ? true : false;

        if(isCriticalAttack){
            return DamageType.CRITICAL_DAMAGE;
        }
        else{
            return DamageType.FLAT_DAMAGE;
        }

    }


    /**
     * 데미지 파라미터 만드는 매서드...
     * 스킬의 효과로 데미지 버프를 적용할 때 호출한다.
     * 스킬의 효과가 데미지 뿐 아니라 다른 버프효과도 같이 있는 경우 호출한다
     * 스킬의 효과가 단지 데미지 뿐이라면, 아래 createDamageBuff 를 호출하여, 아예 데미지 전용 버프액션을 생성하여 사용한다
     *
     * @param defaultDamage
     * @param attack
     * @param condition
     * @return
     */
    public static ConditionFloatParam createDamageParam(float defaultDamage, AttackComponent attack, ConditionComponent condition){

        ConditionFloatParam damageParam = null;

        // 공격자의 크리티컬 스탯을 가지고, 발생할 데미지의 타입을 결정한다.
        int damageType = decideDamageType(attack, condition);
        switch (damageType) {

            case DamageType.FLAT_DAMAGE :

                damageParam = new ConditionFloatParam(ConditionType.damageAmount, defaultDamage);
                break;

            case DamageType.CRITICAL_DAMAGE :

                damageParam = new ConditionFloatParam(ConditionType.criticalDamageAmount, defaultDamage);
                break;
        }

        return damageParam;

    }


    /**
     * 데미지 "버프"를 생성한다.
     * 일반 공격 또는 스킬 효과로 데미지만 들어가는 경우
     * ㄴ 물론.. 잠깐 이동 못하게하기 뭐 그런게 들어갈 수도 있겠지만
     *  ㅇ아 몰라 일단 만들어두고 생각하자
     * 그.. 스킬 시전자나 효과적용 앤티티같은 경우는 외부에 넣어주는 걸로??
     * @param defaultDamage
     * @param attack
     * @param condition
     * @return
     */
    public static BuffAction createDamageBuff(float defaultDamage, AttackComponent attack, ConditionComponent condition){

        BuffAction damageBuff = new BuffAction();

        damageBuff.remainTime = 0.15f;
        damageBuff.remainCoolTime = -1f;
        damageBuff.coolTime = -1f;

        damageBuff.floatParam.add( createDamageParam(defaultDamage, attack, condition));

        return damageBuff;

    }

    // 음 생각해보니까.. 아예 철저히 처음부터 데미지도 결정하고.. 뭐 그런 경우가 아니라면,
    // damageParam을 통해 만들어진 거를 받아 버프로 구성해주는 걸로 ??
    // 올,, 이걸 쓸 확률이 더 높을듯.
    public static BuffAction createDamageBuff(ConditionFloatParam damageParam, int unitID, int skillUserID){

        BuffAction damageBuff = new BuffAction();

        damageBuff.unitID = unitID;
        damageBuff.skillUserID = skillUserID;

        damageBuff.remainTime = 0.15f;
        damageBuff.remainCoolTime = -1f;
        damageBuff.coolTime = -1f;

        damageBuff.floatParam.add( damageParam);

        return damageBuff;

    }

    /** 2002 02 21 */
    public static SkillSlot findSkillSlotBySkillType(WorldMap worldMap, CharacterEntity character, int skillType){

        SkillSlot slot = null;
        List<SkillSlot> skillSlots = character.skillSlotComponent.skillSlotList;

        for(int i=0; i<skillSlots.size(); i++){

            slot = skillSlots.get(i);
            if(slot.skillinfo.skillType == skillType){
                break;
            }
        }
        return slot;
    }

    /** 2020 03 20 권령희 추가  */
    /**
     * 귀환 시전 시 호출, 유저가 받고 있는 버프 목록에, 유저의 상태를 '귀환중'으로 바꾸는 효과를 넣어준다.
     */
    public static void recall(WorldMap worldMap, ActionUseSkill event){

        /* 유저 캐릭터를 찾음 */
        CharacterEntity skillUser = worldMap.characterEntity.get(event.userEntityID);
        if(skillUser == null){
            return;
        }

        /* 귀환 상태임을 나타내줄 효과 구성 */
        BuffAction recallAction  = new BuffAction(skillUser.entityID, skillUser.entityID, 5f, -1f, -1f);
        recallAction.skillType = SkillType.RECALL;

        ConditionBoolParam recallParam = new ConditionBoolParam(ConditionType.isReturning, true);
        recallAction.boolParam.add(recallParam);

        /* 버프 넣어줌 */
        skillUser.buffActionHistoryComponent.conditionHistory.add(recallAction);

        SkillInfoData skillInfoData = new SkillInfoData();
        skillInfoData.skillType = SkillType.RECALL;
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.motionCharacterUseSkill(TARGET, RMI_Context.Reliable, event.userEntityID, skillInfoData);
    }




}