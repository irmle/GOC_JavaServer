package ECS.Entity;

import ECS.Components.*;

public class CrystalEntity extends Entity {

    public CrystalComponent crystalComponent;

    public HPComponent hpComponent;
    public DefenseComponent defenseComponent;

    public BuffActionHistoryComponent buffActionHistoryComponent;
    public HpHistoryComponent hpHistoryComponent;
    public ConditionComponent conditionComponent;

    public CrystalEntity(PositionComponent positionComponent, CrystalComponent crystalComponent, HPComponent hpComponent, DefenseComponent defenseComponent) {
        super(positionComponent);
        this.crystalComponent = crystalComponent;
        this.hpComponent = hpComponent;
        this.defenseComponent = defenseComponent;

        this.buffActionHistoryComponent = new BuffActionHistoryComponent();
        this.hpHistoryComponent = new HpHistoryComponent();
        this.conditionComponent = new ConditionComponent();
    }

    public CrystalEntity(PositionComponent positionComponent, CrystalComponent crystalComponent, HPComponent hpComponent, DefenseComponent defenseComponent, BuffActionHistoryComponent buffActionHistoryComponent, HpHistoryComponent hpHistoryComponent, ConditionComponent conditionComponent) {
        super(positionComponent);
        this.crystalComponent = crystalComponent;
        this.hpComponent = hpComponent;
        this.defenseComponent = defenseComponent;
        this.buffActionHistoryComponent = buffActionHistoryComponent;
        this.hpHistoryComponent = hpHistoryComponent;
        this.conditionComponent = conditionComponent;
    }

    @Override
    public Entity clone() {
        CrystalEntity crystalEntity;

        try {
            crystalEntity = (CrystalEntity) super.clone();

            /* Crystal Component */
            crystalEntity.crystalComponent = (CrystalComponent) crystalComponent.clone();

            /* Position Component */
            crystalEntity.positionComponent = (PositionComponent) positionComponent.clone();

            /* HP Component */
            crystalEntity.hpComponent = (HPComponent) hpComponent.clone();

            /* Defense Component */
            crystalEntity.defenseComponent = (DefenseComponent) defenseComponent.clone();

            /* BuffActionHistory Component */
            crystalEntity.buffActionHistoryComponent = (BuffActionHistoryComponent) buffActionHistoryComponent.clone();

            /* HpHistory Component */
            crystalEntity.hpHistoryComponent = (HpHistoryComponent) hpHistoryComponent.clone();

            /* Condition Component */
            crystalEntity.conditionComponent = (ConditionComponent) conditionComponent.clone();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return crystalEntity;
    }
}
