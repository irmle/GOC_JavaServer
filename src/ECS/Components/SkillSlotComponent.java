package ECS.Components;

import ECS.Classes.SkillSlot;

import java.util.ArrayList;
import java.util.List;

public class SkillSlotComponent {

    public List<SkillSlot> skillSlotList;

    public SkillSlotComponent() {
        skillSlotList = new ArrayList<>();
    }
}
