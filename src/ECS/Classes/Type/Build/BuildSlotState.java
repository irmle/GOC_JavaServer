package ECS.Classes.Type.Build;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 18 수 새벽
 * 업뎃날짜 :
 * 목    적 :
 *      빌드 슬롯에 상태를 나타내기 위함.
 *
 *      막 생성된 월드맵의 빌드 슬롯은 모두 비어있다. (EMPTY)
 *      유저가 포탑 또는 바리케이드를 건설하여 슬롯이 차게 되면, IDLE 상태로 들어간다.
 *      // 유저가 포탑/바리케이드를 파괴하거나 몹의 공격을 받아 파괴되면, EMPTY 상태로 돌아간다.
 *
 *      슬롯이 비어있지 않은 경우에 대해, 각 상태가 IDLE ~ END 상태를 순환한다
 *
 * 빌드 슬롯 상태 전이 :
 *      유저로부터 건설 요청이 들어오면, 슬롯 상태가 EMPTY >> READY 로 전이된다
 *      유저로부터 업그레이드 요청이 들어오면, 슬롯 상태가 IDLE >> READY 로 전이된다
 *      빌드 슬롯 시스템은
 *          ㅇㅇ
 *          ㅇㅇ
 *          ㅇㅇ
 *          ㅇㅇ
 *          ㅇㅇ
 *          .. 빌드 시스템 작성하면서 수정할 것. 일단은 틀만.
 */
public class BuildSlotState {

    public static final int IDLE = 0;
    public static final int READY = 1;
    public static final int START = 2;
    public static final int INSTALLING = 3; // 최초 설치 시  // 안쓸지도..
    public static final int UPGRADING = 4;  // 업그레이드 진행 시   // 안쓸지도..
    public static final int END = 5;

    public static final int EMPTY = 6;
    public static final int DESTROYED = 7;  // 안쓸지도..
    public static final int REPAIRING = 8;  // 안쓸지도..

    public static final int BUILDING = 9;

}
