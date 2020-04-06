package ECS.Components;

public class TurretComponent implements Cloneable {

    public int turretType; //포탑 타입
    public float costTime; //건설시간
    public int costGold; //건설비용

    public TurretComponent(int turretType, float costTime, int costGold) {
        this.turretType = turretType;
        this.costTime = costTime;
        this.costGold = costGold;
    }

    @Override
    public Object clone() {
        TurretComponent turretComponent = null;
        try{
            turretComponent = (TurretComponent) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return turretComponent;
    }
}
