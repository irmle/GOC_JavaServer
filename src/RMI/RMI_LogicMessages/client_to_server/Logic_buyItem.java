package RMI.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.Build.ActionUpgradeBuilding;
import ECS.ActionQueue.ClientAction;
import ECS.ActionQueue.Item.ActionBuyItem;
import ECS.ActionQueue.Upgrade.ActionStoreUpgrade;
import ECS.Classes.Type.TurretType;
import ECS.Classes.Type.Upgrade.StoreUpgradeType;
import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import RMI.RMI_Classes.*;

/**
 * 생 성 자 : 이수헌
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 16 월요일 오후
 * 업뎃날짜 : 2020 01 06 화요일 오후
 * 업뎃내용 :
 *      - 상점 업그레이드 기능 디버깅용. 템 구매 매서드가 호출될 시, 업그레이드 처리를 하게끔 함.
 *      ㄴ 상점 업글용 RMI가 추가되면 다시 수정할 것.
 * 목    적 :
 *      유저가 아이템 구매 RMI를 호출했을 때, 서버에서 처리되는 매서드이다.
 *      넘겨받은 파라미터값을 사용하여
 *          1. 유저가 속한 월드를 찾고
 *          2. 아이템 구매 요청 정보를 담은 Action 객체를 생성하고
 *          3. 생성한 Action 객체를 1에서 찾은 월드 내 액션 큐에 집어넣는다
 */
public class Logic_buyItem {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, int itemType, short itemCount) {

        System.out.println("Logic_buyItem = "+worldMapID+" / "+userEntityID+" / "+itemSlotNum+" / "+itemType+" / "+itemCount);

        /*유저가 속한 월드를 찾는다 */
        WorldMap worldmap1 = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        /* 아이템 구매 Action 객체를 생성한다*/
        ActionBuyItem action1 = new ActionBuyItem(userEntityID, itemSlotNum, itemType, itemCount);
        action1.actionType = ClientAction.ActionType.ACTION_BUY_ITEM;

        /* 생성한 Action 객체를 월드의 액션 큐에 집어넣는다*/
        worldmap1.enqueueClientAction(action1);


        /**
         * 상점 업그레이드 테스트용 >> 상점 업그레이드 RMI 추가 후 아래 코드 복붙, 받아온 값을 활용하게끔 수정해야 함.
         * 필요한 것 : userEntityID, int upgradeType (업그레이드 타입 - StoreUpgradeType)
         * 타입 종류 :
         *      StoreUpgradeType.CRYSTAL_UPGRADE = 1
         *      StoreUpgradeType.EXP_UPGRADE = 2
         *      StoreUpgradeType.GOLD_UPGRADE = 3
         */

        if(false){
            /* 유저가 속한 월드를 찾는다 */
            WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

            /* 업그레이드 Action 객체를 생성한다 */
            ActionStoreUpgrade action = new ActionStoreUpgrade(userEntityID, StoreUpgradeType.GOLD_UPGRADE, 0);
            action.actionType = ClientAction.ActionType.ACTION_STORE_UPGRADE;

            /* 생성한 Action 객체를 월드의 액션 큐에 집어넣는다 */
            worldmap.enqueueClientAction(action);
        }

        /**
         * 건물 업그레이드 테스트용
         * >> 클라이언트에 건물업글 UI가 없어서, 템 구매 RMI 호출 시 임의 터렛으로 업그레이드하도록 함
         * >> 아 이미 upgradeTurret 매서드는 있네 .. 아래는 그냥 지우거나 if(false)로 줘도 될듯
         * 필요한 것 : userEntityID, turretEntityID, turretType(기존 터렛타입. 어택 1~ 10, 버프 11~20)
         * 74 ~92 라인은 테스트를 위해 넣은 것이므로 지워야.
         */
        if(false){
            System.out.println("터렛 업그레이드 요청을 받았습니다. ");

            /* 유저가 속한 월드를 찾는다 */
            WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

            /* ======= 야매 디버깅을 위해 추가한 코드 2020 01 08 ================================ */
            int turretEntityID = worldmap.buildSystem.findBuildSlotBySlotNum(1).getBuildingEntityID();
            int turretType = TurretType.ATTACK_TURRET_TYPE1_UPGRADE1;

            if(worldmap.attackTurretEntity.get(turretEntityID).turretComponent.turretType == TurretType.ATTACK_TURRET_DEFAULT){
                turretType = TurretType.ATTACK_TURRET_TYPE2_UPGRADE1;
            } else if(worldmap.attackTurretEntity.get(turretEntityID).turretComponent.turretType == TurretType.ATTACK_TURRET_TYPE1_UPGRADE1){
                turretType = TurretType.ATTACK_TURRET_TYPE1_UPGRADE2;
            } else if(worldmap.attackTurretEntity.get(turretEntityID).turretComponent.turretType == TurretType.ATTACK_TURRET_TYPE1_UPGRADE2){
                turretType = TurretType.ATTACK_TURRET_TYPE1_UPGRADE3;
            } else if(worldmap.attackTurretEntity.get(turretEntityID).turretComponent.turretType == TurretType.ATTACK_TURRET_TYPE2_UPGRADE1){
                turretType = TurretType.ATTACK_TURRET_TYPE2_UPGRADE2;
            } else if(worldmap.attackTurretEntity.get(turretEntityID).turretComponent.turretType == TurretType.ATTACK_TURRET_TYPE2_UPGRADE2){
                turretType = TurretType.ATTACK_TURRET_TYPE2_UPGRADE3;
            } else if(worldmap.attackTurretEntity.get(turretEntityID).turretComponent.turretType == TurretType.ATTACK_TURRET_TYPE3_UPGRADE1){
                turretType = TurretType.ATTACK_TURRET_TYPE3_UPGRADE2;
            } else if(worldmap.attackTurretEntity.get(turretEntityID).turretComponent.turretType == TurretType.ATTACK_TURRET_TYPE3_UPGRADE2){
                turretType = TurretType.ATTACK_TURRET_TYPE3_UPGRADE3;
            }
            /* ========================================================================================= */


            /* 건설 Action 객체를 생성한다 */
            ActionUpgradeBuilding action = new ActionUpgradeBuilding(userEntityID, turretEntityID, turretType);
            action.actionType = ClientAction.ActionType.ACTION_UPGRADE_BUILDING;

            /* 생성한 Action 객체를 월드의 액션 큐에 집어넣는다 */
            worldmap.enqueueClientAction(action);
        }
    }
}