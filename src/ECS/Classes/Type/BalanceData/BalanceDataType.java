package ECS.Classes.Type.BalanceData;

/**
 * 작성날짜 : 오후 10:44 2020-04-03
 * 업뎃날짜 : 오후 10:44 2020-04-03
 *
 * -- 목적 : 게임 내 종류별 밸런스 관련 값을 담는 BalanceData 클래스에서,
 *          해당 객체가 어떤 종류의 밸런스값을 담당하는지?를 구분하기 위함.
 * --
 * --
 * --
 * --
 *
 */
public class BalanceDataType {

    public static final int NONE = 0;

    /* 몬스터 레벨에 따른 보상(골드, 경험치) 증가량 */
    public static final int REWARD_EXP_PER_MONSTER_LEVEL = 1;
    public static final int REWARD_GOLD_PER_MONSTER_LEVEL = 2;

    /* 캐릭터 레벨업에 필요한 경험치 */
    public static final int EXP_FOR_CHARACTER_LEVEL_UP = 3;

    /* 방호벽 강화(상점 아님) 시 드는 골드 및 강화효과(최대체력 증가, 방어력 증가) */
    public static final int BARRICADE_UPGRADE_HP = 4;
    public static final int BARRICADE_UPGRADE_DEFENSE = 5;
    public static final int BARRICADE_UPGRADE_COST_COLD = 6;

    /* 캐릭터 사망 시, 총 게임 진행 시간에 따른 부활 시간 계산에 드는 계수*/
    public static final int CHAR_DEFEAT_TIME = 7;

    /* 추가 */








}
