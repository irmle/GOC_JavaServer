package ECS.Classes;

public class SkillSlot {
    public int slotNum;
    public SkillInfo skillinfo;
    public int skillLevel;
    public float remainCoolTime;


    public SkillSlot() {
        this.skillLevel = 1;
        this.remainCoolTime = 0f;
    }

    public SkillSlot(int slotNum) {
        this.slotNum = slotNum;

        this.skillLevel = 1;
        this.remainCoolTime = 0f;
    }

    public SkillSlot(int slotNum, SkillInfo skillinfo) {
        this.slotNum = slotNum;
        this.skillinfo = skillinfo;
        this.skillLevel = 1;
        this.remainCoolTime = 0f;
    }
}
