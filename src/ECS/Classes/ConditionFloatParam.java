package ECS.Classes;

public class ConditionFloatParam implements Cloneable{

    public int type; //isMoveable 등등
    public float value; //1.5

    public ConditionFloatParam(int type, float value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Object clone()  {
        ConditionFloatParam conditionFloatParam = null;
        try{
            conditionFloatParam = (ConditionFloatParam) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return conditionFloatParam;
    }
}
