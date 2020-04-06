package ECS.Components;

public class SightComponent implements Cloneable {

    public float lookRadius;

    public SightComponent(float lookRadius) {
        this.lookRadius = lookRadius;
    }

    @Override
    public Object clone()  {
        SightComponent sightComponent = null;
        try{
            sightComponent = (SightComponent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return sightComponent;
    }
}
