package ECS.System;

import ECS.ActionQueue.Build.ActionInstallBuilding;
import ECS.ActionQueue.Build.ActionUpgradeBarricade;
import ECS.ActionQueue.Build.ActionUpgradeBuilding;
import ECS.Classes.*;
import ECS.Classes.Type.BalanceData.BalanceDataType;
import ECS.Classes.Type.Build.BuildSlotState;
import ECS.Classes.Type.Build.BuildType;
import ECS.Classes.Type.NotificationType;
import ECS.Classes.Type.TurretType;
import ECS.Components.*;
import ECS.Entity.*;
import ECS.Factory.AttackTurretFactory;
import ECS.Factory.BarricadeFactory;
import ECS.Factory.BuffTurretFactory;
import ECS.Factory.MapFactory;
import ECS.Game.GameDataManager;
import ECS.Game.WorldMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Enum.MapComponents;
import Network.RMI_Classes.RMI_Context;
import Network.RMI_Classes.RMI_ID;
import Network.RMI_Common.RMI_ParsingClasses.EntityType;
import Network.RMI_Common.server_to_client;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 18 수 새벽
 * 업뎃날짜 :
 * 목    적 :
 *      포탑, 바리케이드 등 앤티티를 건설가능 지점에 건설 혹은 업그레이드 처리를 수행하기 위해 도는 시스템
 */
public class BuildSystem {

    /* 멤버 변수 */
    WorldMap worldMap;

    float coolTime;
    float remainCoolTime;

    public static HashMap<Integer, Integer> installPriceTable;  // 설치 타입, 건설 가격 맵
    public static HashMap<Integer, Integer> upgradePriceTable;  //  업그레이드 타입, 건설 가격 맵 ; 바리는 업글 없음
                                                                // 터렛 타입이랑 각 비용을 넣을 것

    /* 생성자 */
    public BuildSystem(WorldMap worldMap, float coolTime) {

        this.worldMap = worldMap;

        this.coolTime = coolTime;
        this.remainCoolTime = this.coolTime;

        installPriceTable = new HashMap<>();
        upgradePriceTable = new HashMap<>();

        readBuildingInfo();

    }

    /* 매서드 */
    public void onUpdate(float deltaTime){

        // 시스템 도는 주기 조절용 코드
        /*remainCoolTime -= worldMap.tickRate;
        if(remainCoolTime <= 0){
            remainCoolTime = coolTime;
        }
        else{
            return;
        }*/

        /* 모든 건설 슬롯들에 대해 반복한다 */
        for(int i=0; i<worldMap.buildSlotList.size(); i++){

            BuildSlot currentBuildSlot = worldMap.buildSlotList.get(i);

            int slotState = currentBuildSlot.getSlotState();
            switch (slotState){

                case BuildSlotState.IDLE :

                    break;

                case BuildSlotState.READY :

                    int buildingEntityID = currentBuildSlot.getBuildingEntityID();
                    if(buildingEntityID == 0){        /* 설치 */

                        /* 설치하고자 하는 건물 Entity를 생성해준다 */
                        int newBuildingEntityID = createBuilding(currentBuildSlot);

                        currentBuildSlot.setBuildingEntityID(newBuildingEntityID);

                    }
                    else {       /* 업그레이드 */

                    }

                    /* 상태 전이 */
                    currentBuildSlot.setSlotState(BuildSlotState.START);

                    break;

                case BuildSlotState.START :

                    /** 2020 02 29 수정 */
                    currentBuildSlot.setRemainBuildTime(1f);    // 죄다 5초니까 일단...
                    currentBuildSlot.setSlotState(BuildSlotState.BUILDING);
                    break;

                case BuildSlotState.INSTALLING :

                    break;

                case BuildSlotState.UPGRADING :

                    break;

                case BuildSlotState.BUILDING:

                    boolean isFinished = ( currentBuildSlot.getRemainBuildTime() <= 0f ) ? true : false;
                    if(isFinished){
                        currentBuildSlot.setSlotState(BuildSlotState.END);
                    }
                    else{
                        currentBuildSlot.decreaseRemainBuildTime(deltaTime);
                    }

                    break;

                case BuildSlotState.END :

                    /* 업그레이드된 건물에 대한 처리 추가  */
                    // 해당 건물의 앤티티타입을 검사함
                    boolean isTurret =
                            (currentBuildSlot.getBuildingType() == BuildType.TURRET_ATTACK
                                    || currentBuildSlot.getBuildingType() == BuildType.TURRET_BUFF) ? true : false;

                    boolean upgraded = false;
                    if(isTurret){

                        int turretType;
                        switch (currentBuildSlot.getBuildingType()){
                            case BuildType.TURRET_ATTACK :

                                AttackTurretEntity attackTurret = worldMap.attackTurretEntity.get(currentBuildSlot.getBuildingEntityID());
                                turretType = attackTurret.turretComponent.turretType;
                                upgraded = (turretType > TurretType.ATTACK_TURRET_DEFAULT
                                        && turretType <= TurretType.ATTACK_TURRET_TYPE3_UPGRADE3) ? true : false;
                                break;

                            case BuildType.TURRET_BUFF :

                                BuffTurretEntity buffTurret = worldMap.buffTurretEntity.get(currentBuildSlot.getBuildingEntityID());
                                turretType = buffTurret.turretComponent.turretType;
                                upgraded = (turretType > TurretType.BUFF_TURRET_DEFAULT
                                        && turretType <= TurretType.BUFF_TURRET_TYPE3_UPGRADE3) ? true : false;
                                break;

                        }
                    }

                    if(upgraded){

                        AttackComponent attackComponent;
                        DefenseComponent defenseComponent;
                        HPComponent hpComponent;
                        BuffComponent buffComponent;

                        switch (currentBuildSlot.getBuildingType()){

                            case BuildType.TURRET_ATTACK :

                                AttackTurretEntity attackTurret = worldMap.attackTurretEntity.get(currentBuildSlot.getBuildingEntityID());

                                /* 갱신할 데이터 */
                                AttackTurretInfo attackTurretInfo = AttackTurretFactory.attackTurretInfoTable.get(attackTurret.turretComponent.turretType);

                                /* attack */
                                attackComponent = attackTurret.attackComponent;

                                attackComponent.attackDamage = attackTurretInfo.attackDamage;
                                attackComponent.attackRange = attackTurretInfo.attackRange;
                                attackComponent.attackSpeed = attackTurretInfo.attackSpeed;

                                /* defense */
                                defenseComponent = attackTurret.defenseComponent;
                                defenseComponent.defense = attackTurretInfo.defense;

                                /* hp */
                                hpComponent = attackTurret.hpComponent;

                                hpComponent.maxHP = attackTurretInfo.maxHp;
                                hpComponent.currentHP = hpComponent.maxHP;
                                hpComponent.recoveryRateHP = attackTurretInfo.recoveryRateHP;

                                break;

                            case BuildType.TURRET_BUFF :

                                BuffTurretEntity buffTurret = worldMap.buffTurretEntity.get(currentBuildSlot.getBuildingEntityID());

                                /* 갱신할 데이터 */
                                BuffTurretInfo buffTurretInfo = BuffTurretFactory.buffTurretInfoTable.get(buffTurret.turretComponent.turretType);

                                /* defense */
                                defenseComponent = buffTurret.defenseComponent;
                                defenseComponent.defense = buffTurretInfo.defense;

                                /* hp */
                                hpComponent = buffTurret.hpComponent;
                                hpComponent.maxHP = buffTurretInfo.maxHp;
                                hpComponent.currentHP = hpComponent.maxHP;
                                hpComponent.recoveryRateHP = buffTurretInfo.recoveryRateHP;

                                /* buff */
                                buffComponent = buffTurret.buffComponent;

                                ConditionFloatParam floatParam = new ConditionFloatParam(buffTurretInfo.buffType, buffTurretInfo.buffValue);
                                ArrayList<ConditionFloatParam> floatParams = new ArrayList<>();
                                floatParams.add(floatParam);

                                buffComponent.buffActionInfo.remainTime = buffTurretInfo.remainTime;
                                buffComponent.buffActionInfo.remainCoolTime = buffTurretInfo.remainCoolTime;
                                buffComponent.buffActionInfo.coolTime = buffTurretInfo.coolTime;
                                buffComponent.buffActionInfo.floatParam = floatParams;

                                buffComponent.buffAreaRange = buffTurretInfo.buffAreaRange;

                                break;
                        }

                    }

                    /* 상태 전이 */
                    currentBuildSlot.setSlotState(BuildSlotState.IDLE);
                    break;

                case BuildSlotState.DESTROYED :

                    currentBuildSlot.emptySlot();
                    break;

            }

        }

    }

    public void readBuildingInfo(){

        readInstallPriceInfo();
        readUpgradePriceInfo();

    }

    public void readInstallPriceInfo(){

        installPriceTable.put(BuildType.BARRIER, GameDataManager.barricadeInfoList.get(1).costGold);    // 바리케이드는.. 타입이 하나밖에 없어서.
        installPriceTable.put(BuildType.TURRET_ATTACK, GameDataManager.turretList.get(TurretType.ATTACK_TURRET_DEFAULT));
        installPriceTable.put(BuildType.TURRET_BUFF, GameDataManager.turretList.get(TurretType.ATTACK_TURRET_DEFAULT));

    }

    public void readUpgradePriceInfo(){

        /* 공격 터렛 */
        for(AttackTurretInfo attackTurretInfo : GameDataManager.attackTurretInfoList.values()){

            switch (attackTurretInfo.turretType){

                case TurretType.ATTACK_TURRET_DEFAULT :

                    installPriceTable.put(BuildType.TURRET_ATTACK, attackTurretInfo.costGold);
                    break;

                default:

                    upgradePriceTable.put(attackTurretInfo.turretType, attackTurretInfo.costGold);
                    break;
            }

        }

        /* 버프 터렛 */
        for(BuffTurretInfo buffTurretInfo : GameDataManager.buffTurretInfoList.values()){

            switch (buffTurretInfo.turretType){

                case TurretType.BUFF_TURRET_DEFAULT :

                    installPriceTable.put(BuildType.TURRET_BUFF, buffTurretInfo.costGold);
                    break;

                default:

                    upgradePriceTable.put(buffTurretInfo.turretType, buffTurretInfo.costGold);
                    break;
            }

        }
    }




    /**
     * 건설 가능한 지점 목록을 받아와서, 그 갯수만큼 빌드 슬롯을 만들고 각각에 지점을 집어넣은 리스트를 반환한다
     * @param buildableAreaList
     */
    public ArrayList<BuildSlot> initBuildSlotList(ArrayList<MapComponentUnit> buildableAreaList){

        ArrayList<BuildSlot> buildSlots = new ArrayList<>();

        MapComponentUnit mapUnit;
        BuildSlot newBuildSlot;
        for(int i=0; i<buildableAreaList.size(); i++){

            mapUnit = buildableAreaList.get(i);

            int newSlotNum = i+1;   // 슬롯 번호는 1부터 시작한다
            newBuildSlot = new BuildSlot(newSlotNum, mapUnit);
            buildSlots.add(newBuildSlot);
        }

        return buildSlots;
    }


    /**
     * 건설 가능 여부를 판단한다
     * 돈 가지고 있는지. 올바른 지점인지. 등등
     * @return
     */
    public int isAbleToInstall(ActionInstallBuilding event){

        int resultCode = -1;
        boolean result;

        CharacterEntity user = worldMap.characterEntity.get(event.builderEntityID);

        /* 유효한 슬롯 번호인가 ? */
        BuildSlot slot = findBuildSlotBySlotNum(event.buildSlotNum);
        boolean isValidSlot = (!(slot == null)) ? true : false; // 널이 아니라면 유효한 슬롯이다.

        /* 슬롯 내 맵 지형이, 짓고자 하는 건물을 지을 수 있는 지형이 맞는가? */
        boolean isValidBuildType = false;
        if(isValidSlot){    // 존재하는 슬롯이 아니라면 의미가 없으므로
            isValidBuildType = isValidBuildType(slot.mapPosition, event.buildType) ? true : false;
        }

        /* 슬롯이 비어있는 상태가 맞는가? */
        boolean isEmptySlot = false;
        if(isValidSlot){    // 존재하는 슬롯이 아니라면 의미가 없으므로
            isEmptySlot = slot.isEmpySlot() ? true : false;
        }

        /* 건설 비용을 지불할 수 있는가? */
        boolean ableToPay = false;

        int userGold = user.characterComponent.getCurrentGold();
        int buildPrice = getBuildPrice(event.buildType);
        ableToPay = (userGold >= buildPrice) ? true : false;


        /* 건설(최초 설치) 가능 여부 최종 판단 */
        result = ( isValidSlot && isValidBuildType && isEmptySlot && ableToPay ) ? true : false;
        if(result == true){
            resultCode = NotificationType.SUCCESS;
        }
        else{

            if(!isValidSlot){
                resultCode = NotificationType.ERR_BUILD_INSTALL_INVALID_SLOT;
            }
            else if(!isValidBuildType){
                resultCode = NotificationType.ERR_BUILD_INSTALL_INVALID_TERRAIN;
            }
            else if(!isEmptySlot){
                resultCode = NotificationType.ERR_BUILD_INSTALL_NOT_EMPTY_SLOT;
            }
            else {
                resultCode = NotificationType.ERR_BUILD_INSTALL_LACK_MONEY;
            }


        }

        return resultCode;
    }

    /**
     * 건설 가능하다는 가정 하에, 실제 건설 처리를 한다
     *  ㄴ 사실. 실제 진행은 시스템이 하는거고. 여기서 해주는거는, 돈 지불하고 상태 바꿔주는 거 정도??
     * @param event
     */
    public boolean installBuilding(ActionInstallBuilding event){

        boolean result = false;

        /* 설치 정보 */
        CharacterEntity builder = worldMap.characterEntity.get(event.builderEntityID);
        int buildType = event.buildType;
        int buildSlotNum= event.buildSlotNum;

        /* 건설(설치) 비용 지불 처리하기 */
        int payment = getBuildPrice(buildType);
        boolean paymentSuccess = ( builder.characterComponent.payGold(payment) == true ) ? true : false;


        if(paymentSuccess) { // 지불 처리에 성공했다면

            BuildSlot slot;

            /* 슬롯에, 건설하려는 건물 타입을 지정해준다 */
            slot = findBuildSlotBySlotNum(buildSlotNum);
            slot.setBuildingType(buildType);

            /* 슬롯의 상태를 변경한다 */
            slot.setSlotState(BuildSlotState.READY);
            slot.setBuilder(builder.entityID);

            /**/
            setMapInfoByInstalling(slot);

            result = true;
        }

        return result;
    }


    /**
     * 주어진 슬롯 번호를 사용해, 해당 슬롯을 찾아 반환한다
     * @param targetSlotNum
     * @return
     */
    public BuildSlot findBuildSlotBySlotNum(int targetSlotNum){

        BuildSlot buildSlot = null;

        BuildSlot slot;
        for(int i=0; i<worldMap.buildSlotList.size(); i++){

            slot = worldMap.buildSlotList.get(i);
            if(slot.slotNum == targetSlotNum){
                buildSlot = slot;
                break;
            }
        }

        return buildSlot;

    }

    public BuildSlot findBuildSlotByEntityID(int targetID){

        BuildSlot buildSlot = null;

        BuildSlot slot;
        for(int i=0; i<worldMap.buildSlotList.size(); i++){

            slot = worldMap.buildSlotList.get(i);

            if(slot.getBuildingEntityID() == targetID){
                buildSlot = slot;

                break;
            }
        }

        return buildSlot;

    }

    /**
     *
     * @param mapComponentUnit
     * @param buildType
     * @return
     */
    public boolean isValidBuildType(MapComponentUnit mapComponentUnit,int buildType){

        boolean result = false;

        MapComponents terrain = mapComponentUnit.componentType;
        switch (buildType){

            case BuildType.BARRIER :
                result = (terrain == MapComponents.BARRIER_AREA) ? true : false;
                break;

            case BuildType.TURRET_ATTACK :
            case BuildType.TURRET_BUFF :
                result = (terrain == MapComponents.TURRET_AREA) ? true : false;
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * 우선! 인스톨 전용,,
     * @param buildType
     * @return
     */
    public int getBuildPrice(int buildType){

        int price = installPriceTable.get(buildType);

        return price;
    }

    /**
     * 주어진 타입의 건물을 생성한다.
     * 생성한 건물의 EntityID을 반환한다
     * @param
     * @return
     */
    public int createBuilding(BuildSlot buildSlot){

        int newBuildingEntityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();
        int buildType = buildSlot.getBuildingType();

        /* 넣어줄 상태버프. 건설이 진행되는 동안, 공격 하지도, 공격 받지도, 버프처리도 못하게끔 */
        BuffAction stateBuff = new BuffAction(newBuildingEntityID, newBuildingEntityID, 1f, 0f, 0f);

        Entity newBuilding;
        switch (buildType) {

            case BuildType.BARRIER :

                BarricadeEntity newBarri = BarricadeFactory.createBarricade(buildType);
                // 바리케이드는 하나뿐이라 별도 타입이 정해져있지는 않고..
                // 팩토리에 1번으로 하나 들어가 있음. 건설타입 1도 바리케이드라 건설타입으로 넘겨줌

                newBarri.entityID = newBuildingEntityID;
                newBarri.positionComponent.position.set( buildSlot.mapPosition.getCenterPositionFromMapArea());
                newBarri.buffActionHistoryComponent.conditionHistory.add(stateBuff);

                /** 2020 03 03 추가 */
                /* 바리케이드 건설 이후 업그레이드 할 시에 필요한 정보들 세팅 */
                newBarri.barricadeComponent.upgradeLevel = 0;
                newBarri.barricadeComponent.costGold = getBarricadeUpgradeCost(0);
                newBarri.barricadeComponent.costTime = 0f;  // 일단 건설 시 바로 지어지는걸로?

                worldMap.requestCreateQueue.add(newBarri);
                worldMap.entityMappingList.put(newBuildingEntityID, EntityType.BarricadeEntity);

                break;

            case BuildType.TURRET_ATTACK :

                AttackTurretEntity newATurret = AttackTurretFactory.createAttackTurret(TurretType.ATTACK_TURRET_DEFAULT);

                newATurret.entityID = newBuildingEntityID;
                newATurret.positionComponent.position.set( buildSlot.mapPosition.getCenterPositionFromMapArea());
                newATurret.buffActionHistoryComponent.conditionHistory.add(stateBuff);

                buildSlot.setBuildingEntityID(newBuildingEntityID);

                worldMap.requestCreateQueue.add(newATurret);
                worldMap.entityMappingList.put(newBuildingEntityID, EntityType.AttackTurretEntity);

                break;

            case BuildType.TURRET_BUFF :

                BuffTurretEntity newBTurret = BuffTurretFactory.createBuffTurret(TurretType.BUFF_TURRET_DEFAULT);

                newBTurret.entityID = newBuildingEntityID;
                newBTurret.positionComponent.position.set( buildSlot.mapPosition.getCenterPositionFromMapArea());
                newBTurret.buffActionHistoryComponent.conditionHistory.add(stateBuff);

                buildSlot.setBuildingEntityID(newBuildingEntityID);

                worldMap.requestCreateQueue.add(newBTurret);
                worldMap.entityMappingList.put(newBuildingEntityID, EntityType.BuffTurretEntity);

                break;

        }

        return newBuildingEntityID;

    }

    public void setMapInfoByInstalling(BuildSlot slot){

        int buildType = slot.getBuildingType();
        switch (buildType){

            case BuildType.BARRIER :

                slot.mapPosition.setComponentType(MapComponents.BARRIER);
                break;

            case BuildType.TURRET_ATTACK:
            case BuildType.TURRET_BUFF :

                slot.mapPosition.setComponentType(MapComponents.TURRET);
                slot.mapPosition.setMapInfoMovable(false);

                processWhenMapBeingNotMovable(slot.mapPosition, EntityType.CharacterEntity);
                processWhenMapBeingNotMovable(slot.mapPosition, EntityType.MonsterEntity);

                break;

        }
    }

    /**
     * 업그레이드 가능 여부를 판단하는 함수
     */
    public boolean isAbleToUpgradeBuilding(ActionUpgradeBuilding event){

        boolean isAble = false;

        /* 이벤트 정보 */
        int turretType = event.turretType;
        CharacterEntity user = worldMap.characterEntity.get(event.userEntityID);
        int turretEntityID = event.turretEntityID;

        Entity turret = null;
        TurretComponent currentTurretComponent = null;

        /* 유효한 건물인가 ?? */
        boolean isValidTurret = worldMap.entityMappingList.containsKey(turretEntityID);
        if(isValidTurret){
            int entityType = worldMap.entityMappingList.get(turretEntityID);
            switch (entityType){

                case EntityType.AttackTurretEntity :
                    turret = worldMap.attackTurretEntity.get(turretEntityID);
                    currentTurretComponent = ((AttackTurretEntity)turret).turretComponent;
                    break;

                case EntityType.BuffTurretEntity :
                    turret = worldMap.buffTurretEntity.get(turretEntityID);
                    currentTurretComponent = ((BuffTurretEntity)turret).turretComponent;
                    break;
            }
        }


        /* 이미 업글이 진행중이진 않은가?? */
        boolean isNotBeingBuilt = false;
        if(isValidTurret){

            BuildSlot buildSlot = findBuildSlotByEntityID(turretEntityID);  // 터렛이 놓인 건설슬롯을 찾는다.
            isNotBeingBuilt = (buildSlot.getSlotState() == BuildSlotState.IDLE ) ? true : false;
        }

        /* 지정된 타입의 터렛으로 업그레이드가 가능한가?? */
        boolean isAbleTurretType = false;
        if(isNotBeingBuilt){

            int currentTurretType = currentTurretComponent.turretType;
            isAbleTurretType = isValidUpgradeType(currentTurretType, turretType);

        }

        /* 지불 가능한가??  */
        boolean isAbleToPay = false;
        if(isAbleTurretType){

            int upgradePrice = getUpgradePrice(turretType);
            int userGold = user.characterComponent.getCurrentGold();
            isAbleToPay = (userGold >= upgradePrice) ? true : false;
        }

        /* 최종 판정 */
        if(isAbleToPay){

            isAble = true;
        }

        return isAble;

    }


    /**
     * 현재 터렛에서 업그레이드 하려는 터렛 타입으로 업글이 가능한가?? 를 판단하는 함수.
     * 위 isAbleToUpgradeBuilding() 매서드에서 호출.
     */
    public boolean isValidUpgradeType(int currentTurretType, int upgradeTurretType){

        boolean isValid = false;

        switch (currentTurretType){

            /* 공격 포탑 */
            case TurretType.ATTACK_TURRET_DEFAULT :
                isValid = (upgradeTurretType == TurretType.ATTACK_TURRET_TYPE1_UPGRADE1
                        || upgradeTurretType == TurretType.ATTACK_TURRET_TYPE2_UPGRADE1
                        || upgradeTurretType == TurretType.ATTACK_TURRET_TYPE3_UPGRADE1)
                        ? true : false;
                break;

            case TurretType.ATTACK_TURRET_TYPE1_UPGRADE1 :
                isValid = (upgradeTurretType == TurretType.ATTACK_TURRET_TYPE1_UPGRADE2) ? true : false;
                break;

            case TurretType.ATTACK_TURRET_TYPE1_UPGRADE2 :
                isValid = (upgradeTurretType == TurretType.ATTACK_TURRET_TYPE1_UPGRADE3) ? true : false;
                break;

            case TurretType.ATTACK_TURRET_TYPE2_UPGRADE1 :
                isValid = (upgradeTurretType == TurretType.ATTACK_TURRET_TYPE2_UPGRADE2) ? true : false;
                break;

            case TurretType.ATTACK_TURRET_TYPE2_UPGRADE2 :
                isValid = (upgradeTurretType == TurretType.ATTACK_TURRET_TYPE2_UPGRADE3) ? true : false;
                break;

            case TurretType.ATTACK_TURRET_TYPE3_UPGRADE1 :
                isValid = (upgradeTurretType == TurretType.ATTACK_TURRET_TYPE3_UPGRADE2) ? true : false;
                break;

            case TurretType.ATTACK_TURRET_TYPE3_UPGRADE2 :
                isValid = (upgradeTurretType == TurretType.ATTACK_TURRET_TYPE3_UPGRADE3) ? true : false;
                break;


            /* 버프 포탑 */
            case TurretType.BUFF_TURRET_DEFAULT :
                isValid = (upgradeTurretType == TurretType.BUFF_TURRET_TYPE1_UPGRADE1
                        || upgradeTurretType == TurretType.BUFF_TURRET_TYPE2_UPGRADE1
                        || upgradeTurretType == TurretType.BUFF_TURRET_TYPE3_UPGRADE1)
                        ? true : false;
                break;

            case TurretType.BUFF_TURRET_TYPE1_UPGRADE1 :
                isValid = (upgradeTurretType == TurretType.BUFF_TURRET_TYPE1_UPGRADE2) ? true : false;
                break;

            case TurretType.BUFF_TURRET_TYPE1_UPGRADE2 :
                isValid = (upgradeTurretType == TurretType.BUFF_TURRET_TYPE1_UPGRADE3) ? true : false;
                break;

            case TurretType.BUFF_TURRET_TYPE2_UPGRADE1 :
                isValid = (upgradeTurretType == TurretType.BUFF_TURRET_TYPE2_UPGRADE2) ? true : false;
                break;

            case TurretType.BUFF_TURRET_TYPE2_UPGRADE2 :
                isValid = (upgradeTurretType == TurretType.BUFF_TURRET_TYPE2_UPGRADE3) ? true : false;
                break;

            case TurretType.BUFF_TURRET_TYPE3_UPGRADE1 :
                isValid = (upgradeTurretType == TurretType.BUFF_TURRET_TYPE3_UPGRADE2) ? true : false;
                break;

            case TurretType.BUFF_TURRET_TYPE3_UPGRADE2 :
                isValid = (upgradeTurretType == TurretType.BUFF_TURRET_TYPE3_UPGRADE3) ? true : false;
                break;

            /* 그 외 */
            default:
                isValid = false;
        }

        return isValid;
    }


    /**
     * 업그레이드 가능하다는 전제 하에, 업그레이드 처리를 하는 함수
     */
    public void upgradeBuilding(ActionUpgradeBuilding event){

        /* 이벤트 정보 */
        int turretType = event.turretType;
        CharacterEntity user = worldMap.characterEntity.get(event.userEntityID);
        int turretEntityID = event.turretEntityID;

        Entity turret = null;
        TurretComponent currentTurretComponent = null;

        /* 업그레이드 비용을 지불한다 */
        int upgradeCost = upgradePriceTable.get(turretType);
        user.characterComponent.payGold(upgradeCost);

        /* 건설 슬롯을 찾는다 */
        BuildSlot buildSlot = findBuildSlotByEntityID(turretEntityID);

        /* 슬롯의 상태를 변경한다 */
        buildSlot.setSlotState(BuildSlotState.START);

        /* 업그레이드하려는 타입으로 Turret Component를 바꿔준다 */
        int entityType = worldMap.entityMappingList.get(turretEntityID);
        switch (entityType){

            case EntityType.AttackTurretEntity :
                AttackTurretEntity attackTurret = worldMap.attackTurretEntity.get(turretEntityID);
                currentTurretComponent = attackTurret.turretComponent;

                AttackTurretInfo newATurretInfo = AttackTurretFactory.attackTurretInfoTable.get(turretType);
                currentTurretComponent.turretType = newATurretInfo.turretType;
                currentTurretComponent.costGold = newATurretInfo.costGold;
                currentTurretComponent.costTime = newATurretInfo.costTime;

                break;

            case EntityType.BuffTurretEntity :
                BuffTurretEntity buffTurret = worldMap.buffTurretEntity.get(turretEntityID);
                currentTurretComponent = buffTurret.turretComponent;

                BuffTurretInfo newBTurretInfo = BuffTurretFactory.buffTurretInfoTable.get(turretType);
                currentTurretComponent.turretType = newBTurretInfo.turretType;
                currentTurretComponent.costGold = newBTurretInfo.costGold;
                currentTurretComponent.costTime = newBTurretInfo.costTime;

                break;
        }

        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());

        //터렛이 업그레이드 되었음을, 모든 유저에게 중계한다.
        server_to_client.userSucceedInstallBuilding(TARGET, RMI_Context.Reliable_Public_AES256, turretEntityID, turretType);
    }

    /**
     *
     * 업그레이드 비용 겟
     */
    public int getUpgradePrice(int turretType){

        return upgradePriceTable.get(turretType);
    }

    /**
     * 바리케이드 업그레이드 가능 여부를 판단하는 함수
     */
    public boolean isAbleToUpgradeBarricade(ActionUpgradeBarricade event){

        boolean isAble = false;

        /* 이벤트 정보 */
        CharacterEntity user = worldMap.characterEntity.get(event.userEntityID);
        int barricadeEntityID = event.barricadeEntityID;

        BarricadeEntity barricade = null;
        BarricadeComponent barricadeComponent = null;

        /* 유효한 건물인가 ?? */
        boolean isValidBarri = worldMap.entityMappingList.containsKey(barricadeEntityID);
        if(isValidBarri){

            barricade = worldMap.barricadeEntity.get(barricadeEntityID);
            barricadeComponent = barricade.barricadeComponent;
        }


        /* 이미 업글&건설이 진행중이진 않은가?? */
        boolean isNotBeingBuilt = false;
        if(isValidBarri){

            BuildSlot buildSlot = findBuildSlotByEntityID(barricadeEntityID);  // 터렛이 놓인 건설슬롯을 찾는다.
            isNotBeingBuilt = (buildSlot.getSlotState() == BuildSlotState.IDLE ) ? true : false;
        }

        /* 최종 업글된 상태인지 아닌지 */
        boolean isNotFullyUpgraded = false;
        if(isNotBeingBuilt){

            int currentBarriLevel = barricadeComponent.upgradeLevel;
            isNotFullyUpgraded = (currentBarriLevel < 15) ? true : false;
        }

        /* 지불 가능한가??  */
        boolean isAbleToPay = false;
        if(isNotFullyUpgraded){

            int upgradePrice = barricadeComponent.costGold;
            int userGold = user.characterComponent.getCurrentGold();
            isAbleToPay = (userGold >= upgradePrice) ? true : false;

        }

        /* 최종 판정 */
        if(isAbleToPay){

            isAble = true;
        }

        return isAble;

    }

    /**
     * 업그레이드 가능하다는 전제 하에, 바리케이드 업그레이드 처리를 하는 함수
     */
    public void upgradeBarricade(ActionUpgradeBarricade event){

        /* 이벤트 정보 */
        CharacterEntity user = worldMap.characterEntity.get(event.userEntityID);
        int barricadeEntityID = event.barricadeEntityID;

        BarricadeEntity barricade = worldMap.barricadeEntity.get(barricadeEntityID);
        BarricadeComponent barricadeComponent = barricade.barricadeComponent;

        /* 업그레이드 비용을 지불한다 */
        int upgradeCost = barricadeComponent.costGold;
        user.characterComponent.payGold(upgradeCost);

        /* 건설 슬롯을 찾는다 */
        BuildSlot buildSlot = findBuildSlotByEntityID(barricadeEntityID);

        /* 슬롯의 상태를 변경한다 */
        buildSlot.setSlotState(BuildSlotState.START);

        /* 업그레이드 */
        barricadeComponent.upgradeLevel++;
        barricadeComponent.costGold = getBarricadeUpgradeCost(barricadeComponent.upgradeLevel); // 다음 업그레이드 시 필요한 금액을 구한다

        /**
         * 2020 03 03 화 권령희
         * 아래 두 개는.. 각 컴포넌트 값들에 DEFAULT(고정값)을 채워주기 위한 변수를 따로 추가한 후에 수정할 것
         * -- 상점 업그레이드도 추가되면, 걔네들 수치를 반영하기 위한 처리도 추가해줄 것
         */
        barricade.defenseComponent.defense += getBarricadeUpgradeDefenseValue(barricadeComponent.upgradeLevel);
        barricade.hpComponent.originalMaxHp += getBarricadeUpgradeHpValue(barricadeComponent.upgradeLevel);
        barricade.hpComponent.maxHP = barricade.hpComponent.originalMaxHp;
        barricade.hpComponent.currentHP += getBarricadeUpgradeHpValue(barricadeComponent.upgradeLevel);
        if(barricade.hpComponent.currentHP > barricade.hpComponent.maxHP){
            barricade.hpComponent.currentHP = barricade.hpComponent.maxHP;
        }

        // 바리케이드가 업그레이드 되었음을, 모든 유저에게 중계한다.
        RMI_ID[] TARGET = RMI_ID.getArray(worldMap.worldMapRMI_IDList.values());
        server_to_client.userSucceedInstallBuilding(TARGET, RMI_Context.Reliable_Public_AES256, barricadeEntityID, -1);
    }



    /**
     * 2020 03 03
     * 바리케이드 강화에 필요한 골드 구하기
     */
    public int getBarricadeUpgradeCost(int barricadeLevel){

        BalanceData upgradeCostData = GameDataManager.balanceDataInfoList.get(BalanceDataType.BARRICADE_UPGRADE_COST_COLD);

        int WEIGHT_VALUE = (int) upgradeCostData.weightValue;
        int COMPLEMENT_VALUE = (int) upgradeCostData.adjustmentValue;

        int cost = WEIGHT_VALUE * (int)Math.pow(barricadeLevel, 2) + COMPLEMENT_VALUE;

        return cost;
    }

    public float getBarricadeUpgradeHpValue(int barricadeLevel){

        BalanceData upgradeHpData = GameDataManager.balanceDataInfoList.get(BalanceDataType.BARRICADE_UPGRADE_HP);

        int WEIGHT_VALUE = (int) upgradeHpData.weightValue;
        int COMPLEMENT_VALUE = (int) upgradeHpData.adjustmentValue;

        float hp = WEIGHT_VALUE * (int)Math.pow(barricadeLevel, 2) + COMPLEMENT_VALUE;

        return hp;

    }

    public float getBarricadeUpgradeDefenseValue(int barricadeLevel){

        BalanceData upgradeDefenseData = GameDataManager.balanceDataInfoList.get(BalanceDataType.BARRICADE_UPGRADE_DEFENSE);

        int WEIGHT_VALUE = (int) upgradeDefenseData.weightValue;
        int COMPLEMENT_VALUE = (int) upgradeDefenseData.adjustmentValue;

        int defense = WEIGHT_VALUE * (int)Math.pow(barricadeLevel, 2) + COMPLEMENT_VALUE;

        return defense;
    }



    /*******************************************************************************************************************/
    /**
     * 2020 04 18 새벽 작성
     * 필요 데이터를 GDM으로부터 클론하여 사용하게끔, 초반에 미리 복사해두는 처리
     */

    /**
     * 기    능 : 건설 시스템에서 필요로 하는 데이터를, GDM에서 복사해온다.
     * 처    리 :
     *      BuildSystem에서 필요로 하는 GDM 데이터는 다음과 같다
     *      -- 바리케이드 목록
     *      -- 터렛 목록
     *      -- 공격터렛정보 목록
     *      -- 버프터렛정보 목록
     *      -- 밸런스데이터 목록
     *
     */
    public void getNeedDataFromGDM(){

        /* 초기화 처리 */


        /* 바리케이드 정보 목록을 복사한다 */
        bringBarricadeInfoListFromGDM();

        /* 터렛 목록을 복사한다 */
        bringTurretListFromGDM();

        /* 공격터렛정보 목록을 복사한다 */
        bringAttackTurretInfoListFromGDM();

        /* 버프터렛정보 목록을 복사한다 */
        bringBuffTurretInfoListFromGDM();

        /* 밸런스데이터 목록을 복사한다 */
        bringBalanceDataInfoListFromGDM();


    }

    public void bringBarricadeInfoListFromGDM(){

        HashMap<Integer, BarricadeInfo> barricadeInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, BarricadeInfo> barricadeInfo : GameDataManager.barricadeInfoList.entrySet()){

            int barricadeKey = barricadeInfo.getKey();
            BarricadeInfo barricadeValue = barricadeInfo.getValue();
            barricadeInfoList.put(barricadeKey, barricadeValue.clone());

        }

    }

    public void bringTurretListFromGDM(){

        HashMap<String, Integer> turretList = new HashMap<>();
        for( HashMap.Entry<String, Integer> turret : GameDataManager.turretList.entrySet()){

            String turretKey = turret.getKey();
            int turretValue = turret.getValue();
            turretList.put(turretKey, turretValue);

        }

    }



    public void bringAttackTurretInfoListFromGDM(){

        HashMap<Integer, AttackTurretInfo> attackTurretInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, AttackTurretInfo> attackTurretInfo : GameDataManager.attackTurretInfoList.entrySet()){

            int attackTurretKey = attackTurretInfo.getKey();
            AttackTurretInfo attackTurretValue = attackTurretInfo.getValue();
            attackTurretInfoList.put(attackTurretKey, attackTurretValue.clone());

        }

    }

    public void bringBuffTurretInfoListFromGDM(){

        HashMap<Integer, BuffTurretInfo> buffTurretInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, BuffTurretInfo> buffTurretInfo : GameDataManager.buffTurretInfoList.entrySet()){

            int buffTurretKey = buffTurretInfo.getKey();
            BuffTurretInfo buffTurretValue = buffTurretInfo.getValue();
            buffTurretInfoList.put(buffTurretKey, buffTurretValue.clone());

        }

    }


    public void bringBalanceDataInfoListFromGDM(){

        HashMap<Integer, BalanceData> balanceDataInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, BalanceData> balanceDataInfo : GameDataManager.balanceDataInfoList.entrySet()){

            int balanceKey = balanceDataInfo.getKey();
            BalanceData balanceValue = balanceDataInfo.getValue();
            balanceDataInfoList.put(balanceKey, balanceValue.clone());

        }

    }




    public void processWhenMapBeingNotMovable(MapComponentUnit mapArea, int entityType){

        switch (entityType){

            case EntityType.CharacterEntity :

                for ( CharacterEntity characterEntity : worldMap.characterEntity.values()){

                    Vector3 charPos = characterEntity.positionComponent.position;

                    boolean haveToMove = false;
                    for(int i=0; i<mapArea.occupyingTilesList.size(); i++){

                        MapInfo currentTile = mapArea.occupyingTilesList.get(i);
                        boolean isStand = MapFactory.checkTile(currentTile, charPos.x(), charPos.z());
                        if(isStand){
                            haveToMove = true;
                            break;
                        }
                    }

                    if(haveToMove){

                        Vector3 escapeDir = (worldMap.crystalEntity.get(worldMap.crystalID)).positionComponent.position;

                        worldMap.charNextPosList_gotStuck.put(characterEntity.entityID, escapeDir);
                    }
                }

                break;

            case EntityType.MonsterEntity :

                for ( MonsterEntity monsterEntity : worldMap.monsterEntity.values()){

                    Vector3 mobPos = monsterEntity.positionComponent.position;

                    boolean haveToMove = false;
                    for(int i=0; i<mapArea.occupyingTilesList.size(); i++){

                        MapInfo currentTile = mapArea.occupyingTilesList.get(i);
                        boolean isStand = MapFactory.checkTile(currentTile, mobPos.x(), mobPos.z());
                        if(isStand){
                            haveToMove = true;
                            break;
                        }
                    }

                    if(haveToMove){

                        Vector3 escapeDir = (worldMap.crystalEntity.get(worldMap.crystalID)).positionComponent.position;
                        worldMap.charNextPosList_gotStuck.put(monsterEntity.entityID, escapeDir);
                    }
                }

                break;
        }
    }
}
