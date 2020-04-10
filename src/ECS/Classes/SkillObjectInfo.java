package ECS.Classes;


import ECS.Classes.Type.SkillAreaType;

public class SkillObjectInfo implements Cloneable {

    //스킬마다 지정되는 스킬오브젝트Info 이다.

    //스킬 범위모양 타입
    public int skillAreaType = SkillAreaType.CONE;

    //스킬 오브젝트 지속시간 (즉시 발동형인경우 1회 시전후 사라짐. 지속형인경우 해당 시간동안 지속)
    public float skillObjectDurationTime = 0f;

    //스킬 오브젝트 효과 적용 반경 (스킬 오브젝트가 생성된 지점(point)을 기준으로 하는 범위 반경 값)
    //(원형, 직사각형, 부채꼴등의 형태에서 범위 판정에 필요한 값)
    public float skillObjectRadius = 0f;

    //스킬 오브젝트 부채꼴 각도 (부채꼴 형태의 스킬오브젝트에서만 값을 가짐. 나머지는 0으로 고정)
    //(0도 ~ 90도의 범위를 가져야 하며,  이 각도 값이 90도일 경우 부채꼴은 정확히 반원이 된다)
    public float skillObjectAngle = 0f;

    //스킬 오브젝트 최대 거리 (직선형 스킬오브젝트에서만 유효)
    public float skillObjectDistance = 0f;

    //스킬 적중시 부여될 버프 효과 정보. (없다면 null 값) (단순 데미지, 회복도 포함)
    public BuffAction buffAction;


    public SkillObjectInfo(int skillAreaType, float skillObjectDurationTime, float skillObjectRadius, float skillObjectAngle, float skillObjectDistance, BuffAction buffAction) {
        this.skillAreaType = skillAreaType;
        this.skillObjectDurationTime = skillObjectDurationTime;
        this.skillObjectRadius = skillObjectRadius;
        this.skillObjectAngle = skillObjectAngle;
        this.skillObjectDistance = skillObjectDistance;
        this.buffAction = buffAction;
    }

    /**
     * 2020 03 31
     * @param skillAreaType
     * @param skillObjectAngle
     */
    public SkillObjectInfo(int skillAreaType, float skillObjectAngle) {
        this.skillAreaType = skillAreaType;
        this.skillObjectAngle = skillObjectAngle;

        this.buffAction = new BuffAction();
    }

    @Override
    public Object clone() {
        SkillObjectInfo clone;
        try {
            clone = (SkillObjectInfo) super.clone();
            clone.buffAction = (BuffAction) buffAction.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
