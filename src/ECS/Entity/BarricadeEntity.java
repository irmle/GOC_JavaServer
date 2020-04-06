package ECS.Entity;

import ECS.Components.*;

public class BarricadeEntity extends Entity {

    /** 멤버 변수 영역*/

    public BarricadeComponent barricadeComponent;

    public HPComponent hpComponent;
    public DefenseComponent defenseComponent;

    public BuffActionHistoryComponent buffActionHistoryComponent;
    public HpHistoryComponent hpHistoryComponent;
    public ConditionComponent conditionComponent;

    /** 생성자 영역 */

    public BarricadeEntity() {
    }

    public BarricadeEntity(int entityID) {
        super(entityID);
    }

    public BarricadeEntity(PositionComponent positionComponent) {
        super(positionComponent);
    }

    public BarricadeEntity(BarricadeComponent barricadeComponent, HPComponent hpComponent, DefenseComponent defenseComponent, BuffActionHistoryComponent buffActionHistoryComponent, HpHistoryComponent hpHistoryComponent, ConditionComponent conditionComponent) {
        this.barricadeComponent = barricadeComponent;
        this.hpComponent = hpComponent;
        this.defenseComponent = defenseComponent;
        this.buffActionHistoryComponent = buffActionHistoryComponent;
        this.hpHistoryComponent = hpHistoryComponent;
        this.conditionComponent = conditionComponent;
    }

    public BarricadeEntity(int entityID, BarricadeComponent barricadeComponent, HPComponent hpComponent, DefenseComponent defenseComponent, BuffActionHistoryComponent buffActionHistoryComponent, HpHistoryComponent hpHistoryComponent, ConditionComponent conditionComponent) {
        super(entityID);
        this.barricadeComponent = barricadeComponent;
        this.hpComponent = hpComponent;
        this.defenseComponent = defenseComponent;
        this.buffActionHistoryComponent = buffActionHistoryComponent;
        this.hpHistoryComponent = hpHistoryComponent;
        this.conditionComponent = conditionComponent;
    }

    public BarricadeEntity(PositionComponent positionComponent, BarricadeComponent barricadeComponent, HPComponent hpComponent, DefenseComponent defenseComponent, BuffActionHistoryComponent buffActionHistoryComponent, HpHistoryComponent hpHistoryComponent, ConditionComponent conditionComponent) {
        super(positionComponent);
        this.barricadeComponent = barricadeComponent;
        this.hpComponent = hpComponent;
        this.defenseComponent = defenseComponent;
        this.buffActionHistoryComponent = buffActionHistoryComponent;
        this.hpHistoryComponent = hpHistoryComponent;
        this.conditionComponent = conditionComponent;
    }

    /** 매서드 영역 */

    @Override
    public Entity clone() {
        BarricadeEntity barricadeEntity;

        try {
            barricadeEntity = (BarricadeEntity) super.clone();

            /* Barricade Component */
            barricadeEntity.barricadeComponent = (BarricadeComponent) barricadeComponent.clone();

            /* Position Component */
            barricadeEntity.positionComponent = (PositionComponent) positionComponent.clone();

            /* HP Component */
            barricadeEntity.hpComponent = (HPComponent) hpComponent.clone();

            /* Defense Component */
            barricadeEntity.defenseComponent = (DefenseComponent) defenseComponent.clone();

            /* BuffActionHistory Component */
            barricadeEntity.buffActionHistoryComponent = (BuffActionHistoryComponent) buffActionHistoryComponent.clone();

            /* HpHistory Component */
            barricadeEntity.hpHistoryComponent = (HpHistoryComponent) hpHistoryComponent.clone();

            /* Condition Component */
            barricadeEntity.conditionComponent = (ConditionComponent) conditionComponent.clone();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return barricadeEntity;
    }

}
