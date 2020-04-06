package ECS.Components;

public class RotationComponent implements Cloneable {

    public float y;
    public float z;

    public RotationComponent() {
    }

    public RotationComponent(float y, float z) {
        this.y = y;
        this.z = z;
    }

    @Override
    public Object clone()  {
        RotationComponent rotationComponent = null;
        try{
            rotationComponent = (RotationComponent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return rotationComponent;
    }
}
