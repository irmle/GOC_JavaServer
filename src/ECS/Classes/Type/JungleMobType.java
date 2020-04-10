package ECS.Classes.Type;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 27
 * 업뎃날짜 : 2020 03 06 금
 * 목    적 :
 * 업뎃내용 :
 *      클라이언트에서 현재 있는 에셋으로 정글몹을 표현해주기 위해서, 기존 몹과 정글몹의 타입을 일치?시켜야 하는데..
 *      우선은 2~4번에 해당하는 몹을 정글몹으로 나타내는 걸로 함.
 */
public class JungleMobType {

    public static final int NONE = 0;

    /** 2020 03 06 기준, 기존 버전 */
    /* 일반. 인간형. 겸치랑 돈만 줌 */
    /*public static final int HUMAN1 = 1;
    public static final int HUMAN2 = 2;
    public static final int HUMAN3 = 3;
    public static final int HUMAN4 = 4;*/

    /* 특수. 버프주는 애들 */
    /*public static final int LIZARD = 5;
    public static final int FAIRY = 6;
    public static final int DRAGON = 7;
    public static final int DEVIL = 8;*/

    public static final int LIZARD = 1;
    public static final int FAIRY = 2;
    public static final int DRAGON = 3;
    public static final int DEVIL = 4;


}
