package ECS.ActionQueue;

//Client로부터 액션 이벤트가 왔을때,  Queue에 담는 용도.

/**
 * 2020 01 29 권령희 추가
 * 추가한 내용 :
 *      상점 업그레이드 타입 추가
 *      스킬 사용 중단 타입 추가
 *
 */
public class ClientAction {

    public int actionType;

    public class ActionType {

        /* 스킬 및 공격 */
        public static final int ActionGetSkill = 0;
        public static final int ActionUpgradeSkill = 1;
        public static final int ActionUseSkill = 2;
        public static final int ActionUseAttack = 3;

        /* 아이템 */
        public static final int ACTION_BUY_ITEM = 4;
        public static final int ACTION_SELL_ITEM = 5;
        public static final int ACTION_USE_ITEM = 6;

        /* 건설 */
        public static final int ACTION_INSTALL_BUILDING = 7;
        public static final int ACTION_UPGRADE_BUILDING = 8;
        public static final int ACTION_UPGRADE_BARRICADE= 9;

        /* 상점 업그레이드 */
        public static final int ACTION_STORE_UPGRADE = 10;

        // 스킬 사용 중단
        public static final int ActionStopUsingSkill = 11;


    }
}