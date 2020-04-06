package ECS.ActionQueue.Build;

import ECS.ActionQueue.ClientAction;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 18 수
 * 업뎃날짜 :
 * 목    적 :
 *      클라이언트의 건설 요청 정보를 담고 있는 Action 클래스이다
 *
 *      클라이언트가 건설 RMI를 호출하면,
 *          RMI 매서드 내에서 해당 클래스를 생성해서
 *          유저가 속한 월드 내 Action 큐에 넣어주고
 *          월드의 유저 Action 처리 매서드 내에서 실제 건설 처리를 수행한다
 */
public class ActionInstallBuilding extends ClientAction {

    /* 멤버 변수 */
    public int builderEntityID;    // 건물을 생성하고자 하는 유저의 ID (돈은 얘가 지불해야되니까)
    public int buildSlotNum;
    public int buildType;  // 설치하고자 하는 건물의 타입. 바리케이드, 공격포탑, 버프포탑?


    /* 생성자 */
    public ActionInstallBuilding(int builderEntityID, int buildSlotNum, int buildType) {
        this.builderEntityID = builderEntityID;
        this.buildSlotNum = buildSlotNum;
        this.buildType = buildType;
    }


}
