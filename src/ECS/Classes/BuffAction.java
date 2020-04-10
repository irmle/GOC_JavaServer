package ECS.Classes;
import java.util.ArrayList;

/**
 * 업뎃날짜 : 2020년 03월 12일, 권령희
 * 업뎃내용 :
 *      아이템에 의한 버프일 경우를 나타내기 위해 아이템 타입 추가. >> 임시 처리임.
 *
 * 업뎃예정 : 이번 주 아웃풋이 확정되고 나면, 다시 뜯어고칠 것.. 상태 개념이랑 목록 재정립해서..
 */
public class BuffAction implements Cloneable {

    public int unitID; // 버프 발생시킨 유닛ID
    public float remainTime; // 버프의 남은 지속시간.
    public float remainCoolTime; // 버프 효과발동 쿨타임.
    public float coolTime; //원래 버프 효과발동 쿨타임.

    public int skillUserID;   // 스킬 시전자 유닛 ID

    /** 2020 02 21 */
    public int skillType;   // 스킬에 의한 버프가 아니라면, 값이 0일 것.

    /** 2020 03 12 */
    public int itemType;    // 템에 의한 버프가 아니라면, 값이 0일 것. (아마 itemType class에 정의돼있을 건데, NONE이라고 .)
    public float buffDurationTime;

    //float영향을 주는값, bool영향을 주는값.
    public ArrayList<ConditionBoolParam> boolParam;
    public ArrayList<ConditionFloatParam> floatParam;

    /** 생성자 */

    public BuffAction() {

        unitID = 0;
        skillUserID = 0;

        remainTime = 0;
        remainCoolTime = 0;
        coolTime = 0;

        boolParam = new ArrayList<>();
        floatParam = new ArrayList<>();

        skillType = 0;
        itemType = 0;

        buffDurationTime = 0;

    }

    public BuffAction(int unitID, int skillUserID, float remainTime, float remainCoolTime, float coolTime) {
        this.unitID = unitID;
        this.skillUserID = skillUserID;

        this.remainTime = remainTime;
        this.remainCoolTime = remainCoolTime;
        this.coolTime = coolTime;

        boolParam = new ArrayList<>();
        floatParam = new ArrayList<>();

        skillType = 0;
        itemType = 0;

        buffDurationTime = remainTime;
    }

    public BuffAction(int unitID, int skillUserID, float remainTime, float remainCoolTime, float coolTime, ArrayList<ConditionBoolParam> boolParam, ArrayList<ConditionFloatParam> floatParam) {
        this.unitID = unitID;
        this.skillUserID = skillUserID;

        this.remainTime = remainTime;
        this.remainCoolTime = remainCoolTime;
        this.coolTime = coolTime;
        this.boolParam = boolParam;
        this.floatParam = floatParam;

        skillType = 0;
        itemType = 0;

        buffDurationTime = remainTime;
    }

    /**
     * 2020 04 01 작성
     * 스킬의 효과를 생성할 때 쓸 생성자.
     * @param skillType
     * @param remainTime
     * @param remainCoolTime
     * @param coolTime
     */
    public BuffAction( int skillType, float remainTime, float remainCoolTime, float coolTime) {

        this.skillType = skillType;

        this.remainTime = remainTime;
        this.buffDurationTime = remainTime;

        this.remainCoolTime = remainCoolTime;
        this.coolTime = coolTime;

        boolParam = new ArrayList<>();
        floatParam = new ArrayList<>();

        skillUserID = 0;
        unitID = 0;
        itemType = 0;

    }

    public BuffAction(float remainTime, float remainCoolTime, float coolTime) {

        this.remainTime = remainTime;
        this.buffDurationTime = remainTime;

        this.remainCoolTime = remainCoolTime;
        this.coolTime = coolTime;

        boolParam = new ArrayList<>();
        floatParam = new ArrayList<>();

        skillType = 0;
        skillUserID = 0;
        unitID = 0;
        itemType = 0;

    }

    /**
     * 2020 04 01 작성
     * @param effectParam
     */
    public void addEffect(ConditionBoolParam effectParam){

        // 혹여나 널인 경우를 위한 예외처리
        if(this.boolParam == null){
            this.boolParam = new ArrayList<>();
        }

        this.boolParam.add(effectParam);

    }

    public void addEffect(ConditionFloatParam effectParam){

        // 혹여나 널인 경우를 위한 예외처리
        if(this.floatParam == null){
            this.floatParam = new ArrayList<>();
        }

        this.floatParam.add(effectParam);

    }


    @Override
    public Object clone() {

        BuffAction buffAction;

        try {
            buffAction = (BuffAction) super.clone();

            buffAction.boolParam = new ArrayList<>();
            for(int i=0; i<boolParam.size(); i++){
                buffAction.boolParam.add( (ConditionBoolParam) boolParam.get(i).clone());
            }

            buffAction.floatParam = new ArrayList<>();
            for(int i=0; i<floatParam.size(); i++){
                buffAction.floatParam.add( (ConditionFloatParam) floatParam.get(i).clone());
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return buffAction;
    }


}
