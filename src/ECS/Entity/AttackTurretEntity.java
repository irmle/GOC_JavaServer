package ECS.Entity;

import ECS.Components.*;

public class AttackTurretEntity extends Entity {

    public TurretComponent turretComponent;

    public HPComponent hpComponent;
    public AttackComponent attackComponent;
    public DefenseComponent defenseComponent;

    public BuffActionHistoryComponent buffActionHistoryComponent;
    public HpHistoryComponent hpHistoryComponent;
    public ConditionComponent conditionComponent;

    public AttackTurretEntity(PositionComponent positionComponent, TurretComponent turretComponent, HPComponent hpComponent, AttackComponent attackComponent, DefenseComponent defenseComponent, BuffActionHistoryComponent buffActionHistoryComponent, HpHistoryComponent hpHistoryComponent, ConditionComponent conditionComponent) {
        super(positionComponent);
        this.turretComponent = turretComponent;
        this.hpComponent = hpComponent;
        this.attackComponent = attackComponent;
        this.defenseComponent = defenseComponent;
        this.buffActionHistoryComponent = buffActionHistoryComponent;
        this.hpHistoryComponent = hpHistoryComponent;
        this.conditionComponent = conditionComponent;
    }

    @Override
    public Entity clone() {
        AttackTurretEntity attackTurretEntity;

        try {
            attackTurretEntity = (AttackTurretEntity) super.clone();

            /* Turret Component */
            attackTurretEntity.turretComponent
                    = (TurretComponent) turretComponent.clone();

            /* Position Component */
            attackTurretEntity.positionComponent = (PositionComponent) positionComponent.clone();

            /* HP Component */
            attackTurretEntity.hpComponent = (HPComponent) hpComponent.clone();

            /* Attack Component */
            attackTurretEntity.attackComponent = (AttackComponent) attackComponent.clone();

            /* Defense Component */
            attackTurretEntity.defenseComponent = (DefenseComponent) defenseComponent.clone();

            /* BuffActionHistory Component */
            attackTurretEntity.buffActionHistoryComponent = (BuffActionHistoryComponent) buffActionHistoryComponent.clone();

            /* HpHistory Component */
            attackTurretEntity.hpHistoryComponent = (HpHistoryComponent) hpHistoryComponent.clone();

            /* Condition Component */
            attackTurretEntity.conditionComponent = (ConditionComponent) conditionComponent.clone();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return attackTurretEntity;
    }

}
