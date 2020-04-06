package ECS.Components;

public class CrystalComponent implements Cloneable{

    public int crystalLevel;

    public CrystalComponent() {
        this.crystalLevel = 1;
    }

    public CrystalComponent(int crystalLevel) {
        this.crystalLevel = crystalLevel;
    }

    @Override
    public Object clone() {
        CrystalComponent crystalComponent = null;

        try{
            crystalComponent = (CrystalComponent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return crystalComponent;
    }
}
