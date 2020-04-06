package ECS.Entity;

import ECS.Components.*;

/**
 * 업뎃날짜 : 2019 12 14 토
 * 업뎃 내용 : RewardHistoryComponent 추가
 */
public class CharacterEntity extends Entity {

    public CharacterComponent characterComponent;

    public SkillSlotComponent skillSlotComponent;
    public ItemSlotComponent itemSlotComponent;

    public HPComponent hpComponent;
    public MPComponent mpComponent;

    public AttackComponent attackComponent;
    public DefenseComponent defenseComponent;

    public RotationComponent rotationComponent;
    public VelocityComponent velocityComponent;

    public BuffActionHistoryComponent buffActionHistoryComponent;
    public HpHistoryComponent hpHistoryComponent;
    public MPHistoryComponent mpHistoryComponent;
    public ConditionComponent conditionComponent;

    public RewardHistoryComponent rewardHistoryComponent;

}
