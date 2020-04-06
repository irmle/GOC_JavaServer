package ECS.Components;

import ECS.Classes.Type.PathType;

/**
 * 2019 12 26 권령희 추가 ; 이동 포인트 관련
 */
public class MonsterComponent implements Cloneable {

    public int targetID;
    public int monsterType;
    public String monsterName;

    public int movePathType;
    public int movePointIndex;


    public MonsterComponent(int monsterType, String monsterName) {
        this.monsterType = monsterType;
        this.monsterName = monsterName;

        this.targetID = 0;

        movePathType = PathType.NONE;
        movePointIndex = 0;
    }

    public MonsterComponent(int targetID, int monsterType, String monsterName) {
        this.targetID = targetID;
        this.monsterType = monsterType;
        this.monsterName = monsterName;

        movePathType = 0;
        movePointIndex = 0;
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
