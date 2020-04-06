package ECS.Entity;

import ECS.Components.BuffComponent;
import ECS.Components.PositionComponent;
import ECS.Components.SkillObjectComponent;

public class SkillObjectEntity extends Entity {

    public SkillObjectComponent skillObjectComponent;

    public SkillObjectEntity(SkillObjectComponent skillObjectComponent) {
        this.skillObjectComponent = skillObjectComponent;
    }

    public SkillObjectEntity(PositionComponent positionComponent, SkillObjectComponent skillObjectComponent) {
        super(positionComponent);
        this.skillObjectComponent = skillObjectComponent;
    }

    public SkillObjectEntity() {
    }

    @Override
    public Entity clone() {

        SkillObjectEntity skillObjectEntity;

        try {
            skillObjectEntity = (SkillObjectEntity) super.clone();
            skillObjectEntity.skillObjectComponent = (SkillObjectComponent) skillObjectComponent.clone();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return skillObjectEntity;
    }
}
