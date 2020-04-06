package ECS.Entity;

import ECS.Classes.Vector3;
import ECS.Components.FlyingObjectComponent;
import ECS.Components.PositionComponent;

public class FlyingObjectEntity extends Entity implements Cloneable{

    public FlyingObjectComponent flyingObjectComponent;

    public FlyingObjectEntity(FlyingObjectComponent flyingObjectComponent) {
        this.flyingObjectComponent = flyingObjectComponent;

        //this.positionComponent.position.set((Vector3) flyingObjectComponent.startPosition.clone());
    }

    public FlyingObjectEntity(PositionComponent positionComponent, FlyingObjectComponent flyingObjectComponent) {
        super(positionComponent);
        this.flyingObjectComponent = flyingObjectComponent;
    }

    public FlyingObjectEntity() {
    }

    @Override
    public FlyingObjectEntity clone()  {

        FlyingObjectEntity flyingObjectEntity;

        try {
            flyingObjectEntity = (FlyingObjectEntity) super.clone();
            flyingObjectEntity.flyingObjectComponent = (FlyingObjectComponent) flyingObjectComponent.clone();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return flyingObjectEntity;
    }
}
