package ECS.Classes;

import ECS.Classes.Type.*;


//스킬에 대한 데이터를 저장하는 부분.
public class SkillInfo implements Cloneable {

    //스킬 타입
    public int skillType = SkillType.ARCHER_POWER_SHOT;

    //스킬 이름
    public String skillName = "파이어볼";

    //스킬 기본 쿨타임
    public float skillCoolTime = 0f; //AttackSpeed를 통하여 진행 가능

    //스킬 사용시 기본 소모 HP
    public float reqHP = 0f;

    //스킬 사용시 기본 소모 MP
    public float reqMP = 0f;

    //스킬 사용가능 기본 사거리
    public float skillRange = 0f;


    //스킬 사용시 부여되는 BuffAction 정보 (없다면 null 값)
    public BuffAction buffAction;

    //스킬 사용시 생성할 SkillObjectInfo 정보 (없다면 null 값)
    public SkillObjectInfo skillObjectInfo;

    //스킬 사용시 생성할 FlyingObjectInfo 정보 (없다면 null 값)
    public FlyingObjectInfo flyingObjectInfo;

    //범위도 있어야 함.
    //레벨당 추가 효과 고려


    public SkillInfo(int skillType, String skillName, float skillCoolTime, float reqHP, float reqMP, float skillRange, BuffAction buffAction, SkillObjectInfo skillObjectInfo, FlyingObjectInfo flyingObjectInfo) {
        this.skillType = skillType;
        this.skillName = skillName;
        this.skillCoolTime = skillCoolTime;
        this.reqHP = reqHP;
        this.reqMP = reqMP;
        this.skillRange = skillRange;
        this.buffAction = buffAction;
        this.skillObjectInfo = skillObjectInfo;
        this.flyingObjectInfo = flyingObjectInfo;
    }

    public SkillInfo(int skillType) {
        this.skillType = skillType;
        this.skillName = "빈 슬롯";
    }

    @Override
    public SkillInfo clone() {
        SkillInfo clone;
        try {
            clone = (SkillInfo) super.clone();
            if(buffAction != null){
                clone.buffAction = (BuffAction) buffAction.clone();
            }
            else{
                clone.buffAction = null;
            }

            if(skillObjectInfo != null){
                clone.skillObjectInfo = (SkillObjectInfo) skillObjectInfo.clone();
            }
            else{
                clone.skillObjectInfo = null;
            }

            if(flyingObjectInfo != null){
                clone.flyingObjectInfo = (FlyingObjectInfo) flyingObjectInfo.clone();
            }
            else{
                clone.flyingObjectInfo = null;
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
