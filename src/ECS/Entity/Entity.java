package ECS.Entity;

import ECS.Classes.Type.AttributeType;
import ECS.Classes.Type.Team;
import ECS.Classes.Vector3;
import ECS.Components.PositionComponent;

/**
 * 업뎃날짜 : 2002 02 04 화 권령희
 * 업뎃내용 : 속성 변수 추가
 */
public class Entity implements Cloneable {

    public int entityID;
    public int team;
    public PositionComponent positionComponent;

    public int attribute;


    public Entity() {
        entityID = 0;
        positionComponent = new PositionComponent(new Vector3(0, 0, 0));

        team = 0;
        attribute = AttributeType.NONE;
    }

    public Entity(int entityID) {
        this.entityID = entityID;

        positionComponent = new PositionComponent(new Vector3(0, 0, 0));
        team = 0;
        attribute = AttributeType.NONE;
    }

    public Entity(PositionComponent positionComponent) {

        entityID = 0;
        this.positionComponent = positionComponent;

        team = 0;
        attribute = AttributeType.NONE;
    }

    public Entity(int entityID, int team, PositionComponent positionComponent, int attribute) {
        this.entityID = entityID;
        this.team = team;
        this.positionComponent = positionComponent;
        this.attribute = attribute;
    }

    public Entity(int entityID, int team, int attribute) {
        this.entityID = entityID;
        this.team = team;
        this.attribute = attribute;

        positionComponent = new PositionComponent(new Vector3(0, 0, 0));
    }

    public Entity(int entityID, int attribute) {
        this.entityID = entityID;
        this.attribute = attribute;

        team = 0;
        positionComponent = new PositionComponent(new Vector3(0, 0, 0));
    }


    /**
     * 오후 5:06 2020-04-07
     * 목적 : 팀 타임을 세팅하기 위한 매서드
     */
    public void setTeamElementalType(int teamType){

        this.team = teamType;
        System.out.println("앤티티 " + this.entityID + "의 팀 속성이 " + this.team + "으로 세팅되었습니다");
    }



    @Override
    public Entity clone() {
        Entity entity = null;
        try{
            entity = (Entity) super.clone();

            entity.positionComponent = (PositionComponent) positionComponent.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

}
