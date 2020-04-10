package ECS.Classes.Type;

/**
 * 변경사항 :
 *  -- 등급 NONE(없음) 추가
 *      ㄴ 기존에는 NORMAL이 0번이였는데, NONE을 추가함에 따라 값이 한칸씩 밀리게끔 함. (기존 값 + 1)
 *  --
 *  --
 *
 */
public class GradeType {

    public static final int NONE = 0;
    public static final int NORMAL = 1;
    public static final int RARE = 2;
    public static final int EPIC = 3;
    public static final int UNIQUE = 4;
    public static final int LEGENDERY = 5;


}
