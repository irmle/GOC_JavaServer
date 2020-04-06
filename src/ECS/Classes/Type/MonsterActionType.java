package ECS.Classes.Type;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 26 목 새벽
 * 목    적 : 몬스터 AI 로직 수행 시, 몬스터가 어떤 행동을 할것인지 정의하기 위함
 */
public class MonsterActionType {

    public static final int DO_NOTHING = 0;
    public static final int ATTACK_TARGET = 1;
    public static final int CHASE_TARGET = 2;
    public static final int MOVE = 3;
}
