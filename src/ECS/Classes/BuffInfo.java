package ECS.Classes;


import ECS.Classes.Type.EffectCauseType;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 03 31 화
 * 업뎃날짜 :
 * 업뎃내용 :
 * --
 * --
 * --
 *
 */
public class BuffInfo implements Cloneable{

    public int effectCauseType = EffectCauseType.NONE;

    public int effectCause = EffectCauseType.NONE;

    public String effectTypeName = "효과타입";

    public int effectType = 0;

    public int effectAppicationType = EffectCauseType.NONE;

    public float effectDurationTime = 0f;

    public float effectCoolTime = 0f;

    public float remainCoolTime = 0f;   // 적중 대기 시간

    public String effectValue;

    public BuffInfo(int effectCauseType, int effectCause, String effectTypeName, int effectType, int effectAppicationType, float effectDurationTime, float effectCoolTime, float remainCoolTime, String effectValue) {
        this.effectCauseType = effectCauseType;
        this.effectCause = effectCause;
        this.effectTypeName = effectTypeName;
        this.effectType = effectType;
        this.effectAppicationType = effectAppicationType;
        this.effectDurationTime = effectDurationTime;
        this.effectCoolTime = effectCoolTime;
        this.remainCoolTime = remainCoolTime;
        this.effectValue = effectValue;
    }

    public void printEffectInfo(){

        System.out.println(effectCauseType);
        System.out.println(effectCause);
        System.out.println(effectTypeName);
        System.out.println(effectType);
        System.out.println(effectAppicationType);
        System.out.println(effectDurationTime);
        System.out.println(effectCoolTime);
        System.out.println(remainCoolTime);
        System.out.println(effectValue);



    }

    @Override
    protected BuffInfo clone() throws CloneNotSupportedException {

        BuffInfo buffInfo = null;

        try{
            buffInfo = (BuffInfo) super.clone();

        } catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }

        return buffInfo;
    }
}
