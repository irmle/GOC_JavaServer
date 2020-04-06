package ECS.Classes;

import ECS.Components.PositionComponent;

public class Death implements Cloneable{

    /** 멤버 변수 */
    public int deadEntityID;
    public int deathType;   // 사망 타입에 따라. 죽인이가 의미가 있을수도 있고.
    public int killerEntityID;

    /*
    public long deadTime;
    public PositionComponent deadPosition;
    */

    /** 생성자 */
    public Death(int deadEntityID, int killerEntityID) {
        this.deadEntityID = deadEntityID;
        this.killerEntityID = killerEntityID;
    }

    public Death(int deadEntityID, int deathType, int killerEntityID) {
        this.deadEntityID = deadEntityID;
        this.deathType = deathType;
        this.killerEntityID = killerEntityID;
    }


    /** 메서드 */

    @Override
    public Death clone() {

        Death death;

        try {
            death = (Death) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return death;
    }

}
