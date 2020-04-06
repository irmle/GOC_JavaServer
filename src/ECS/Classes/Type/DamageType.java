package ECS.Classes.Type;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 04 목
 * 업뎃날짜 :
 * 업뎃내용 :
 * 목    적 : 데미지 타입 분류를 위한 클래스.
 *      회복도 데미지라니... 여기다가 추가해줘야 하나?? or 걍 냅두거나
 *      일단 타입에 추가는 해 줘 보자. 그리고 나중에.. DamageHistory의 isDamage 이거를 대체하던지, 아니면 걍 그대로 쓰던지
 *      ㄴ 만약 회복도 들어가게 되면.. 회복 데미지에 대한 처리도 같이 하게 되겠네 먼가 짬뽕....흠
 *      일단은 isDamage가 false면 걍 패스 하는걸로??? 딱 두줄이면 되니까.
 */
public class DamageType {

    public static final int NONE = 0;   // 쓸 일이 있을라나 모르겠음. 가끔...?? 뎀지가 0뜨는 경우..?? 회피?? 무적?? 회복??
    public static final int FLAT_DAMAGE = 1;
    public static final int CRITICAL_DAMAGE = 2;

    // 회복
    // 독 뭐 어쩌구...


}
