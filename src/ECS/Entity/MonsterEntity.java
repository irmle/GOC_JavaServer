package ECS.Entity;

import ECS.Classes.ConditionBoolParam;
import ECS.Components.*;

import java.lang.management.ClassLoadingMXBean;
import java.util.ArrayList;

public class MonsterEntity extends Entity implements Cloneable {

    public MonsterComponent monsterComponent;

    public HPComponent hpComponent;

    public AttackComponent attackComponent;
    public DefenseComponent defenseComponent;

    public SightComponent sightComponent;

    public RotationComponent rotationComponent;
    public VelocityComponent velocityComponent;

    public BuffActionHistoryComponent buffActionHistoryComponent;
    public HpHistoryComponent hpHistoryComponent;
    public ConditionComponent conditionComponent;


    public MonsterEntity(MonsterComponent monsterComponent, HPComponent hpComponent, AttackComponent attackComponent, DefenseComponent defenseComponent, SightComponent sightComponent, RotationComponent rotationComponent, VelocityComponent velocityComponent) {
        this.monsterComponent = monsterComponent;
        this.hpComponent = hpComponent;
        this.attackComponent = attackComponent;
        this.defenseComponent = defenseComponent;
        this.sightComponent = sightComponent;
        this.velocityComponent = velocityComponent;
        this.rotationComponent = rotationComponent;
        this.buffActionHistoryComponent = new BuffActionHistoryComponent();
        this.hpHistoryComponent = new HpHistoryComponent();
        this.conditionComponent = new ConditionComponent();
    }

    public MonsterEntity(PositionComponent positionComponent, MonsterComponent monsterComponent, HPComponent hpComponent, AttackComponent attackComponent, DefenseComponent defenseComponent, SightComponent sightComponent, RotationComponent rotationComponent, VelocityComponent velocityComponent, BuffActionHistoryComponent buffActionHistoryComponent, HpHistoryComponent hpHistoryComponent, ConditionComponent conditionComponent) {
        super(positionComponent);
        this.monsterComponent = monsterComponent;
        this.hpComponent = hpComponent;
        this.attackComponent = attackComponent;
        this.defenseComponent = defenseComponent;
        this.sightComponent = sightComponent;
        this.rotationComponent = rotationComponent;
        this.velocityComponent = velocityComponent;
        this.buffActionHistoryComponent = buffActionHistoryComponent;
        this.hpHistoryComponent = hpHistoryComponent;
        this.conditionComponent = conditionComponent;
    }

    /**
     *
     * 내부 컴포넌트들 각각 CLONABLE 구현하고, 별도 처리 해줘야.
     */
    @Override
    public MonsterEntity clone() {
        MonsterEntity monsterEntity;

        try {
            monsterEntity = (MonsterEntity) super.clone();

            /* Monster Component */
            monsterEntity.monsterComponent = (MonsterComponent) monsterComponent.clone();

            /* Position Component */
            monsterEntity.positionComponent = (PositionComponent) positionComponent.clone();

            /* HP Component */
            monsterEntity.hpComponent = (HPComponent) hpComponent.clone();

            /* Attack Component */
            monsterEntity.attackComponent = (AttackComponent) attackComponent.clone();

            /* Defense Component */
            monsterEntity.defenseComponent = (DefenseComponent) defenseComponent.clone();

            /* Sight Component */
            monsterEntity.sightComponent = (SightComponent) sightComponent.clone();

            /* Rotation Component */
            monsterEntity.rotationComponent = (RotationComponent) rotationComponent.clone();

            /* Velocity Component */
            monsterEntity.velocityComponent = (VelocityComponent) velocityComponent.clone();

            /* BuffActionHistory Component */
            monsterEntity.buffActionHistoryComponent = (BuffActionHistoryComponent) buffActionHistoryComponent.clone();

            /* HpHistory Component */
            monsterEntity.hpHistoryComponent = (HpHistoryComponent) hpHistoryComponent.clone();

            /* Condition Component */
            monsterEntity.conditionComponent = (ConditionComponent) conditionComponent.clone();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return monsterEntity;
    }

}
