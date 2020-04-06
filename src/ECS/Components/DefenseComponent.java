package ECS.Components;

/** Defense Power Component */
public class DefenseComponent implements Cloneable {

    public float defense;

    public DefenseComponent() {
    }

    public DefenseComponent(float defense) {
        this.defense = defense;
    }

    @Override
    public Object clone() {
        DefenseComponent defenseComponent = null;
        try{
            defenseComponent = (DefenseComponent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return defenseComponent;
    }
}
