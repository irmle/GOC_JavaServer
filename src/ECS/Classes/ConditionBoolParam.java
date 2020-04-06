package ECS.Classes;

public class ConditionBoolParam implements Cloneable{

    public int type; //moveSpeedRate 등등
    public boolean value; //true or false

    public ConditionBoolParam(int type, boolean value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Object clone() {
        ConditionBoolParam conditionBoolParam = null;
        try{
            conditionBoolParam = (ConditionBoolParam) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return conditionBoolParam;
    }
}
