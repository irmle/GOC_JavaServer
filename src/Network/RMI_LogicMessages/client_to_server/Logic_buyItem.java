package Network.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.Build.ActionUpgradeBuilding;
import ECS.ActionQueue.ClientAction;
import ECS.ActionQueue.Item.ActionBuyItem;
import ECS.ActionQueue.Upgrade.ActionStoreUpgrade;
import ECS.Classes.Type.TurretType;
import ECS.Classes.Type.Upgrade.StoreUpgradeType;
import ECS.Entity.CharacterEntity;
import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import Network.RMI_Classes.*;

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

        CharacterEntity character = worldmap1.characterEntity.get(userEntityID);
        if( worldmap1.checkUserIsDead(character)){

            return;
        }

        /* 아이템 구매 Action 객체를 생성한다*/
        ActionBuyItem action1 = new ActionBuyItem(userEntityID, itemSlotNum, itemType, itemCount);
        action1.actionType = ClientAction.ActionType.ACTION_BUY_ITEM;

        /* 생성한 Action 객체를 월드의 액션 큐에 집어넣는다*/
        worldmap1.enqueueClientAction(action1);

    }
}