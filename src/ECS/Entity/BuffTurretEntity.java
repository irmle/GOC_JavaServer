package ECS.Entity;

import ECS.Components.*;

public class BuffTurretEntity extends Entity {

    public TurretComponent turretComponent;

    public HPComponent hpComponent;
    public DefenseComponent defenseComponent;

    public BuffComponent buffComponent;

    public BuffActionHistoryComponent buffActionHistoryComponent;
    public HpHistoryComponent hpHistoryComponent;
    public ConditionComponent conditionComponent;

    public BuffTurretEntity(PositionComponent positionComponent, TurretComponent turretComponent, HPComponent hpComponent, DefenseComponent defenseComponent, BuffComponent buffComponent, BuffActionHistoryComponent buffActionHistory, HpHistoryComponent hpHistoryComponent, ConditionComponent conditionComponent) {
        super(positionComponent);
        this.turretComponent = turretComponent;
        this.hpComponent = hpComponent;
        this.defenseComponent = defenseComponent;
        this.buffComponent = buffComponent;
        this.buffActionHistoryComponent = buffActionHistory;
        this.hpHistoryComponent = hpHistoryComponent;
        this.conditionComponent = conditionComponent;
    }

    @Override
    public Entity clone() {
        BuffTurretEntity buffTurretEntity;

        try {
            buffTurretEntity = (BuffTurretEntity) super.clone();

            /* Turret Component */
            buffTurretEntity.turretComponent = (TurretComponent) turretComponent.clone();

            /* Position Component */
            buffTurretEntity.positionComponent = (PositionComponent) positionComponent.clone();

            /* HP Component */
            buffTurretEntity.hpComponent = (HPComponent) hpComponent.clone();

            /* Defense Component */
            buffTurretEntity.defenseComponent = (DefenseComponent) defenseComponent.clone();

            /* BuffActionHistory Component */
            buffTurretEntity.buffActionHistoryComponent = (BuffActionHistoryComponent) buffActionHistoryComponent.clone();

            /* HpHistory Component */
            buffTurretEntity.hpHistoryComponent = (HpHistoryComponent) hpHistoryComponent.clone();

            /* Condition Component */
            buffTurretEntity.conditionComponent = (ConditionComponent) conditionComponent.clone();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return buffTurretEntity;

    }

}
