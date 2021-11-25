package ECS.Classes.Type.Build;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 18 수
 * 업뎃날짜 :
 * 목    적 :
 *      건설 슬롯 내 건설 타입에, 어떤 건물을 지으려는지를 나타내기 위함.
 *      빈 슬롯의 경우 이 값이 비어있거나, NONE이고, 실제로 건설이 진행되거나 종료된 이후에 그 값이 부여된다.
 *      건설 타입은 포탑(공격, 회복 포함. 나중에 문제가 된다면 분리할 것), 바리케이드 두 종류이다.
 *
 */
public class BuildType {

    public static final int NONE = 0;
    public static final int BARRIER = 1;
    public static final int TURRET_ATTACK = 2;
    public static final int TURRET_BUFF = 3;

}
