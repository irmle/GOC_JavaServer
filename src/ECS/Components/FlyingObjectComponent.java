package ECS.Components;

import ECS.Classes.BuffAction;
import ECS.Classes.Vector3;

public class FlyingObjectComponent implements Cloneable {

    //트사체를 생성한 SkillType;
    public int createdSkillType;

    //시전자 유닛ID
    public int userEntityID = 0;

    //날아가는 속도.
    public float flyingSpeed = 0f;

    //투사체 효과 적용 반경.
    public float flyingObjectRadius = 0f;

    //투사체가 처음 생성된 위치
    public Vector3 startPosition;

    //투사체가 직선으로 날아갈 방향
    public Vector3 direction;

    //투사체가 논타게팅 투사체일 경우, 처음 시작된 위치에서 direction방향 * flyingSpeed * 시간만큼 이동하여
    //그때 이동한 거리만큼 flyingObjectRemainDistance 값을 깎는다. 이 값이 0이되면 논타게팅 투사체를 삭제한다..
    //0이 되기전까지 특정 타겟과 충돌하였다면 해당 타겟에 효과를 적용하고 투사체를 삭제한다.
    public float flyingObjectRemainDistance = 0;

    //투사체 적중시 부여될 버프 효과 정보. (없다면 null 값) (단순 데미지, 회복도 포함)
    public BuffAction buffAction;


    //투사체가 타게팅 투사체일 경우, flyingObjectRemainDistance값은 0일 것이다. 해당 타겟의 타겟ID로부터 타겟의 위치를 찾아서
    //현재 투사체의 위치와 대조 후, 이동처리. 적중거리에 도달하면 적중시 효과를 부여하고 사라질 것.
    public int targetEntityID = 0;


    /**
     * 추가 날짜 : 2019년 11월 28일 목요일 오후
     * 의도 :
     *  투사체가 파괴되는 시점을 결정하는 데 사용할 변수이다.
     *  - 해당 값이 true이면,
     *      적과 충돌하는 시점에 적에게 데미지를 주고 파괴된다.
     *  - 값이 false인 경우, 투사체가 가야하는 거리에 도달하기 전 까지는
     *      여러 번 충돌하더라도 파괴되지 않는다.
     *
     * UseCase :
     *      궁수 및 마법사의 대부분 투사체의 경우 1회 충돌 후 파괴되는데,
     *      궁수 파워 샷 스킬의 경우, 최대 사거리에 도달하기 까지 여러 적을 관통한다.
     *
     *      투사체 시스템에서 각 투사체에 대해 충돌 감지 후, 해당 투사체를 파괴하느냐 마느냐를 결정하기 위해 사용할 것임.
     *      투사체 시스템은 투사체의 남은 이동 거리를 투사체의 수명으로 판단하는데,
     *          beDestroyedByCrash 값이 true일 경우, 남은 거리를 0으로 바꿔주거나 그자리에서 바로 파괴 요청 목록에 넣어준다. // 뭐가 더 나을라나?? 중요한 건 아닌거같긴한데.
     *          값이 false라면, 단순히 남은 거리를 업데이트 해주면 된다.
     *
     *
     */
    public boolean beDestroyedByCrash;


    public FlyingObjectComponent(int userEntityID, Vector3 startPosition, Vector3 direction, float flyingSpeed, float flyingObjectRadius, float flyingObjectRemainDistance, BuffAction buffAction, int targetEntityID) {
        this.userEntityID = userEntityID;
        this.startPosition = startPosition;
        this.direction = direction;
        this.flyingSpeed = flyingSpeed;
        this.flyingObjectRadius = flyingObjectRadius;
        this.targetEntityID = targetEntityID;
        this.flyingObjectRemainDistance = flyingObjectRemainDistance;
        this.buffAction = buffAction;
    }


    public FlyingObjectComponent(int createdSkillType, int userEntityID, float flyingSpeed, float flyingObjectRadius, Vector3 startPosition, Vector3 direction, float flyingObjectRemainDistance, BuffAction buffAction, int targetEntityID) {
        this.createdSkillType = createdSkillType;
        this.userEntityID = userEntityID;
        this.flyingSpeed = flyingSpeed;
        this.flyingObjectRadius = flyingObjectRadius;
        this.startPosition = startPosition;
        this.direction = direction;
        this.flyingObjectRemainDistance = flyingObjectRemainDistance;
        this.buffAction = buffAction;
        this.targetEntityID = targetEntityID;

        // 일단은 이렇게 하드코딩하고, 후에 이 값이 true가 되는 애들이 있을 경우에 별도로 세팅해주는걸론
        this.beDestroyedByCrash = true;
    }

    public FlyingObjectComponent() {
    }

    /**
     * Vector3도 Clonable 해두긴 했는데...
     * 별도로 처리해줘야 하나?? >> 일단은 해야되는걸로
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone()  {
        FlyingObjectComponent flyingObject;
        try {

            flyingObject = (FlyingObjectComponent) super.clone();
            flyingObject.startPosition = (Vector3) startPosition.clone();
            flyingObject.direction = (Vector3) direction.clone();
            flyingObject.buffAction = (BuffAction) buffAction.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return flyingObject;
    }
}
