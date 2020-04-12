package ECS.Components;

import ECS.Classes.Vector3;

public class PositionComponent implements Cloneable  {

    public Vector3 position;
    public Vector3 flyingObjectTargetPosition;
    public Vector3 crystalTargetPosition;

    public PositionComponent() {
        this.position = new Vector3(0f, 0f, 0f);
        this.flyingObjectTargetPosition = new Vector3(0f, 0f, 0f);
        this.crystalTargetPosition = new Vector3(0f, 0f, 0f);
    }

    public PositionComponent(Vector3 position) {
        this.position = position;
        this.crystalTargetPosition = position;
        this.flyingObjectTargetPosition = position;
    }


    public PositionComponent(float x, float y, float z) {
        this.position = new Vector3(x, y, z);
        this.crystalTargetPosition = new Vector3(x,0,z);
        this.flyingObjectTargetPosition = new Vector3(x,y/2,z);
    }

    @Override
    public Object clone() {
        PositionComponent positionComponent;
        try{
            positionComponent = (PositionComponent) super.clone();

            positionComponent.position = (Vector3) position.clone();
            positionComponent.crystalTargetPosition = (Vector3) crystalTargetPosition.clone();
            positionComponent.flyingObjectTargetPosition = (Vector3) flyingObjectTargetPosition.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return positionComponent;
    }

}
