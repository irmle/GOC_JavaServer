package ECS.Classes.Type.Jungle;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 27
 * 업뎃날짜 :
 * 목    적 :
 * 업뎃내용 :
 */
public class JungleMobState {

    public static final int EMPTY = 0;  // 슬롯이 비어, 몬스터가 할당되지 않은 상태 (맨 처음)

    public static final int IDLE = 1;   // 몬스터가 스폰지점에서 놀고있는 상태

    public static final int TARGET_INDICATE = 2;    // 몬스터가 최초로 피격당해 주변의 적을 감지하는 상태
    public static final int TARGET_TRACE = 3;       // 몬스터가 가까운 적을 타게팅하고 공격하기 위해 움직이는 상태
    public static final int TARGET_ATTACK = 4;      // 몬스터가 타겟 가까이에 접근하여, 공격 가능한 상태

    public static final int RETURN_TO_SP = 5;       // 스폰 지점으로 돌아가기 위해 움직이는 상태
    public static final int DIED = 6;               // 몬스터가 죽음

    public static final int REGEN_WAITING = 7;      // 몬스터가 리젠되는 중

}
