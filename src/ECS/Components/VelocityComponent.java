package ECS.Components;

import ECS.Classes.Vector3;

public class VelocityComponent implements Cloneable {

    public Vector3 velocity;
    public float moveSpeed;


    public VelocityComponent(Vector3 velocity) {
        this.velocity = velocity;
    }

    public VelocityComponent(float x, float y, float z) {

        this.velocity = new Vector3(x, y, z);
    }

    public VelocityComponent(float moveSpeed) {
        this.moveSpeed = moveSpeed;
        velocity = new Vector3(0, 0, 0);
    }

    public VelocityComponent() {
        this.velocity = new Vector3(0f, 0f, 0f);
        this.moveSpeed = 250;
    }

    @Override
    public Object clone()  {
        VelocityComponent velocityComponent = null;
        try{
            velocityComponent = (VelocityComponent) super.clone();

            velocityComponent.velocity = (Vector3) velocity.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return velocityComponent;
    }
}
