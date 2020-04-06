package ECS.ActionQueue.Build;

import ECS.ActionQueue.ClientAction;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 03 03 화
 * 목    적 :
 *      바리케이드를 업그레이드하는 이벤트 정보를 담는 용도
 */
public class ActionUpgradeBarricade extends ClientAction {

    public int userEntityID;
    public int barricadeEntityID;

    public ActionUpgradeBarricade(int userEntityID, int barricadeEntityID) {
        this.userEntityID = userEntityID;
        this.barricadeEntityID = barricadeEntityID;
    }
}
