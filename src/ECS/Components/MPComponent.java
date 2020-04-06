package ECS.Components;

public class MPComponent implements Cloneable {

    public float originalMaxMP;

    public float currentMP;
    public float maxMP;
    public float recoveryRateMP;

    public MPComponent() {
    }

    public MPComponent(float currentMP, float maxMP, float recoveryRateMP) {
        this.originalMaxMP = maxMP;

        this.currentMP = currentMP;
        this.maxMP = maxMP;
        this.recoveryRateMP = recoveryRateMP;
    }

    @Override
    public Object clone() {
        MPComponent mpComponent = null;
        try {
            mpComponent = (MPComponent) super.clone();
        } catch (CloneNotSupportedException e){
            throw  new RuntimeException(e);
        }

        return mpComponent;
    }
}