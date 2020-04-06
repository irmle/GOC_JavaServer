package ECS.Components;

public class HPComponent implements Cloneable {

    public float originalMaxHp;

    public float currentHP;
    public float maxHP;
    public float recoveryRateHP;

    public HPComponent( ) {
    }

    public HPComponent(float maxHP, float recoveryRateHP) {

        this.originalMaxHp = maxHP;

        this.currentHP = maxHP;
        this.maxHP = maxHP;
        this.recoveryRateHP = recoveryRateHP;
    }


    @Override
    public Object clone() {
        HPComponent hpComponent = null;
        try{
            hpComponent = (HPComponent) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return hpComponent;
    }
}
