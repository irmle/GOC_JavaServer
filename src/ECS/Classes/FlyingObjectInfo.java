package ECS.Classes;


public class FlyingObjectInfo implements Cloneable {

    //스킬, 일반공격마다 지정되는 투사체Info 이다.

    //투사체 속도
    public float flyingObjectSpeed = 0f;

    //투사체 효과 적용 반경 (논타겟, 타겟형식의 투사체 모두에게 적용. 만약 이 값이 0이라면 단일 대상)
    //(투사체가, 처음 생성된 위치에서 해당방향으로 flyingObjectSpeed * 이동시간 씩 나아간 지점에서
    // 해당 반경내에 가장 가까이 위치한 Target의 범위 판정에 필요한 값)
    public float flyingObjectRadius = 0f;


    //투사체 최대 거리 (논타겟 투사체 오브젝트에서만 유효. 타게팅 투사체 오브젝트라면 flyingObjectDistance과 상관없으므로 flyingObjectDistance값은 0이다.)
    //추후, 이동한 거리만큼 flyingObjectRemainDistance값을 빼서 최종적으로 이 값이 0이 되었을 경우, 투사체를 사라지게 한다.
    //flyingObjectSpeed 값으로 투사체의 효과 적용위치가 조금씩 바뀌면서 범위판정(flyingObjectRadius 반경 내)이 일어날 텐데,
    //이때 충돌하는 개체가 있다면 효과를 적용하고, 사라지게 한다.
    public float flyingObjectRemainDistance = 0f;

    //투사체 적중시 부여될 버프 효과 정보. (없다면 null 값) (단순 데미지, 회복도 포함)
    public BuffAction buffAction;


    public FlyingObjectInfo(float flyingObjectSpeed, float flyingObjectRadius, float flyingObjectRemainDistance, BuffAction buffAction) {
        this.flyingObjectSpeed = flyingObjectSpeed;
        this.flyingObjectRadius = flyingObjectRadius;
        this.flyingObjectRemainDistance = flyingObjectRemainDistance;
        this.buffAction = buffAction;
    }

    @Override
    public Object clone() {
        FlyingObjectInfo clone;
        try {
            clone = (FlyingObjectInfo) super.clone();
            clone.buffAction = (BuffAction) buffAction.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
