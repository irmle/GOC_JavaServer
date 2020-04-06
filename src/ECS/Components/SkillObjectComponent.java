package ECS.Components;

import ECS.Classes.BuffAction;
import ECS.Classes.Vector3;

public class SkillObjectComponent implements Cloneable{

    public int createdSkillType; //스킬 오브젝트를 생성한 스킬 타입

    public int skillAreaType; //스킬 오브젝트 모양 (부채꼴, 원형, 직선형 등)

    public int userEntityID; //시전자 유닛ID

    public float skillObjectDurationTime; //남은지속시간.

    //스킬 오브젝트 효과 적용 반경
    public float skillObjectRadius = 0f;

    //스킬 오브젝트 부채꼴 각도
    public float skillObjectAngle = 0f;

    //스킬 오브젝트 최대 거리 (직선형 스킬오브젝트에서만 유효)
    public float skillObjectDistance = 0f;

    //스킬이 시전된 위치 (스킬 시전시 시전자Entity위치)
    public Vector3 startPosition;

    //스킬이 시전된 방향 (스킬 조이스틱에서 받아온 값)
    public Vector3 direction;

    //스킬이 시전된 거리비율 (스킬 조이스틱에서 받아온 값)
    //(추후, 최대 사거리 * 이 값(distanceRate)을 하여 정확한 스킬지정 위치를 구할 수 있다)
    public float distanceRate;

    //스킬 오브젝트 적중시 부여될 버프 효과 정보. (없다면 null 값) (단순 데미지, 회복도 포함)
    public BuffAction buffAction;


    public SkillObjectComponent(int createdSkillType, int skillAreaType, int userEntityID, float skillObjectDurationTime, float skillObjectRadius, float skillObjectAngle, float skillObjectDistance, Vector3 startPosition, Vector3 direction, float distanceRate, BuffAction buffAction) {
        this.createdSkillType = createdSkillType;
        this.skillAreaType = skillAreaType;
        this.userEntityID = userEntityID;
        this.skillObjectDurationTime = skillObjectDurationTime;
        this.skillObjectRadius = skillObjectRadius;
        this.skillObjectAngle = skillObjectAngle;
        this.skillObjectDistance = skillObjectDistance;
        this.startPosition = startPosition;
        this.direction = direction;
        this.distanceRate = distanceRate;
        this.buffAction = buffAction;
    }

    @Override
    public Object clone()  {
        SkillObjectComponent skillObject;
        try {
            skillObject = (SkillObjectComponent) super.clone();
            skillObject.startPosition = (Vector3) startPosition.clone();
            skillObject.direction = (Vector3) direction.clone();
            skillObject.buffAction = (BuffAction) buffAction.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return skillObject;
    }
}
