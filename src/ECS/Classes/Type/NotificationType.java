package ECS.Classes.Type;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 20 금 새벽
 * 업뎃날짜 :
 * 목    적 :
 *  유저요청 처리 시 응답 코드를 담기 위함.
 */
public class NotificationType {

    public static final int SUCCESS = 0;

    /* 아이템 */
    public static final int ERR_ITEM_LACK_MONEY = 1;
    public static final int ERR_ITEM_SLOT_COUNT_OVER = 2;
    public static final int ERR_ITEM_FULL_SLOT = 3;

    public static final int ERR_ITEM_USER_DISABLE_USING = 4;
    public static final int ERR_ITEM_COOL_TIME_NOT_YET = 5;
    public static final int ERR_ITEM_INVALID_ITEM = 6;  // 아이템 사용, 아이템 판매 시, 그 갯수가 0일 때??

    public static final int ERR_ITEM_EMPTY_SLOT = 7;
    public static final int ERR_ITEM_IS_USING = 8;


    /* 건설 */
    public static final int ERR_BUILD_INSTALL_LACK_MONEY = 11;
    public static final int ERR_BUILD_INSTALL_INVALID_SLOT = 12;
    public static final int ERR_BUILD_INSTALL_INVALID_TERRAIN = 13;
    public static final int ERR_BUILD_INSTALL_NOT_EMPTY_SLOT = 14;



}
