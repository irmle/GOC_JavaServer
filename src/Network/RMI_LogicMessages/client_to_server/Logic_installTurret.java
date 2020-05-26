package Network.RMI_LogicMessages.client_to_server;

import ECS.ActionQueue.Build.ActionInstallBuilding;
import ECS.ActionQueue.ClientAction;
import ECS.Classes.Type.Build.BuildType;
import ECS.Classes.Type.TurretType;
import ECS.Entity.CharacterEntity;
import ECS.Game.MatchingManager;
import ECS.Game.WorldMap;
import Network.RMI_Classes.*;

/**
 * 생 성 자 : 이수헌
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 18 수요일 새벽
 * 업뎃날짜 :
 * 목    적 :
 *      유저가 포탑 설치 RMI를 호출했을 때, 서버에서 처리되는 매서드이다.
 *      넘겨받은 파라미터값을 사용하여
 *          1. 유저가 속한 월드를 찾고
 *          2. 포탑 설치 요청 정보를 담은 Action 객체를 생성하고
 *          3. 생성한 Action 객체를 1에서 찾은 월드 내 액션 큐에 집어넣는다
 */
public class Logic_installTurret {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretType, int areaNumber) {

        System.out.println("터렛 인스톨 요청을 받음.. : " + turretType);

        // 월드 내 건설 가능한 지점 목록을 담고 잇는 빌드슬롯 목록 중, 어느 슬롯에 대한 건설 요청인지??
        int buildSlotNum = areaNumber;

        if(turretType == TurretType.ATTACK_TURRET_DEFAULT){
            turretType = BuildType.TURRET_ATTACK;
        }
        else if(turretType == TurretType.BUFF_TURRET_DEFAULT){
            turretType = BuildType.TURRET_BUFF;
        }

        /* 유저가 속한 월드를 찾는다 */
        WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);
        CharacterEntity character = worldmap.characterEntity.get(userEntityID);
        if( worldmap.checkUserIsDead(character)){

            return;
        }

        /* 건설 Action 객체를 생성한다 */
        ActionInstallBuilding action = new ActionInstallBuilding(userEntityID, buildSlotNum, turretType);
        action.actionType = ClientAction.ActionType.ACTION_INSTALL_BUILDING;

        /* 생성한 Action 객체를 월드의 액션 큐에 집어넣는다 */
        worldmap.enqueueClientAction(action);

    }
}