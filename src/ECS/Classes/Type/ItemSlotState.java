package ECS.Classes.Type;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 17 화 새벽
 * 업뎃날짜 :
 * 목    적 :
 *      아이템 슬롯에 상태를 나타내기 위함.
 *
 *      막 생성된 유저의 아이템 슬롯은 모두 비어있다. (EMPTY)
 *      아이템을 구매하여 슬롯이 차게 되면, IDLE 상태로 들어간다.
 *      아이템을 판매하거나 사용하여 그 갯수가 0이 되면, EMPTY 상태로 돌아간다.
 *
 *      슬롯이 비어있지 않은 경우에 대해, 각 상태가 IDLE ~ END 상태를 순환한다
 *
 * 아이템 슬롯 상태 전이 :
 *      유저로부터 아이템 사용 요청이 들어오면, 슬롯 상태가 IDLE >> READY 로 전이된다
 *      아이템슬롯 시스템은
 *          READY 상태인 아이템에 대해 아이템 사용 처리(갯수 --) 를 진행하고, 상태를 READY >> START 으로 바꾼다
 *          START 상태인 슬롯에 대해 소지자에게 슬롯의 아이템 사용 효과를 부여하고, 상태를 START >> RUNNING 으로 바꾼다
 *          RUNNING 상태인 슬롯에 대해 재사용 대기 시간이 0이 될 때 까지 기다리며 시간을 줄여간다.
 *              재사용 대기시간이 0이 되는 순간, 상태를 RUNNING >> END 로 바꾼다.
 *          END 상태인 슬롯에 대해 .. 처리할 것 있으면 추가처리 + 상태를 END >> IDLE로 바꾼다
 *
 */
public class ItemSlotState {

    public static final int IDLE = 0;
    public static final int READY = 1;
    public static final int START = 2;
    public static final int RUNNING = 3;
    public static final int END = 4;

    public static final int EMPTY = 5;

}
