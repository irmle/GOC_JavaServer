package ECS.Classes.Type;

/**
 * 수정
 * 오전 1:19 2020-04-07
 * 내용 :
 *  - 타입 항목으로, JUNGLE_MOB_RETURN ( 정글 몹 리턴 시 회복 처리를 위함) 추가
 *  - 타입 클래스 이름을 MonsterActionType_ForEffect 라고 변경
 *      ㄴ 기존의 몬스터 AI를 위한 MonsterActionType 라는 타입 클래스가 이미 존재해서..
 *          현 클래스는 몬스터의 행동에 따라 부여되는 효과 목록을 관리하기 위한 클래스이므로,
 *          뒤에 ForEffect 를 덧붙임.
 */
public class MonsterActionType_ForEffect {

    public static final int NONE = 0;
    public static final int MONSTER_ATTACK = 1;
    public static final int JUNGLE_MOB_RETURN = 2;


}
