package ECS.ActionQueue.Build;

import ECS.ActionQueue.ClientAction;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020년 01월 07일 화요일
 * 업뎃날짜 :
 * 목    적 :
 *      터렛(포탑) 업그레이드 요청을 처리하기 위한 클래스
 *
 */
public class ActionUpgradeBuilding extends ClientAction {

    /* 멤버 변수 */
    public int userEntityID;
    public int turretEntityID;
    public int turretType;

    /* 생성자 */
    public ActionUpgradeBuilding(int userEntityID, int turretEntityID, int turretType) {
        this.userEntityID = userEntityID;
        this.turretEntityID = turretEntityID;
        this.turretType = turretType;
    }
}
