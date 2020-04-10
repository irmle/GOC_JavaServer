package ECS.Components;

import ECS.Classes.Type.PathType;

/**
 * 업뎃날짜 : 오후 11:31 2020-04-03
 *
 * -- 오후 11:31 2020-04-03
 *
 *      1. "몬스터 레벨" 멤버 변수 추가.
 *
 *          # 추후 캐릭터의 전투력을 기반으로 몬스터의 레벨을 결정하고,
 *            잡은 몬스터의 레벨에 따라 보상 경험치 및 골드가 달라지게 할 것임.
 * --
 * --
 * --
 * --
 *
 */
public class MonsterComponent implements Cloneable {

    public int targetID;
    public int monsterType;
    public String monsterName;

    public int movePathType;
    public int movePointIndex;

    public int monsterLevel;


    public MonsterComponent(int monsterType, String monsterName) {
        this.monsterType = monsterType;
        this.monsterName = monsterName;

        this.targetID = 0;

        movePathType = PathType.NONE;
        movePointIndex = 0;

        monsterLevel = 1;
    }

    public MonsterComponent(int monsterType, String monsterName, int monsterLevel) {
        this.monsterType = monsterType;
        this.monsterName = monsterName;
        this.monsterLevel = monsterLevel;
    }

    public MonsterComponent(int targetID, int monsterType, String monsterName) {
        this.targetID = targetID;
        this.monsterType = monsterType;
        this.monsterName = monsterName;

        movePathType = 0;
        movePointIndex = 0;

        monsterLevel = 1;
    }

    @Override
    public Object clone() {
        MonsterComponent monsterComponent = null;
        try{
            monsterComponent = (MonsterComponent) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return monsterComponent;
    }
}
