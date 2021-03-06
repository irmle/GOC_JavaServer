package Network.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.ClientAction;
import ECS.ActionQueue.Item.ActionUseItem;
import ECS.Entity.CharacterEntity;
import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import Network.RMI_Classes.*;

/**
 * 생 성 자 : 이수헌
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 16 월요일 오후
 * 업뎃날짜 :
 * 목    적 :
 *      유저가 아이템 사용 RMI를 호출했을 때, 서버에서 처리되는 매서드이다.
 *      넘겨받은 파라미터값을 사용하여
 *          1. 유저가 속한 월드를 찾고
 *          2. 아이템 사용 요청 정보를 담은 Action 객체를 생성하고
 *          3. 생성한 Action 객체를 1에서 찾은 월드 내 액션 큐에 집어넣는다
 *
 * 아래.. usuer 오타 고치고싶은데!
 */
public class Logic_useItem {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int usuerEntityID, short itemSlotNum, short itemCount) {

        /* 유저가 속한 월드를 찾는다 */
        WorldMap worldMap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);
        CharacterEntity character = worldMap.characterEntity.get(usuerEntityID);
        if( worldMap.checkUserIsDead(character)){

            return;
        }

        /* 아이테 구매 Action 객체를 생성한다 */
        ActionUseItem action = new ActionUseItem(usuerEntityID, itemSlotNum);
        action.actionType = ClientAction.ActionType.ACTION_USE_ITEM;

        /* 생성한 Action 객체를 월드의 액션 큐에 집어넣는다 */
        worldMap.enqueueClientAction(action);

    }
}