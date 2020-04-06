package ECS.ActionQueue.Upgrade;

import ECS.ActionQueue.ClientAction;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 01 07 화요일
 * 업뎃날짜 :
 * 목    적 :
 *      클라이언트(유저)로부터 받은 상점 업그레이드 요청 정보를 담기 위함.
 */
public class ActionStoreUpgrade extends ClientAction {

    public int userEntityID;
    public int upgradeType;
    public int upgradeLevel;

    public ActionStoreUpgrade(int userEntityID, int upgradeType, int upgradeLevel) {
        this.userEntityID = userEntityID;
        this.upgradeType = upgradeType;
        this.upgradeLevel = upgradeLevel;
    }
}
