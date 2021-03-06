package ECS.Classes;

import Enum.MapComponents;

/**
 * 2019 12 26 목 권령희 - 매서드 추가 ; getPixelPosition()
 */
public class MapInfo implements Cloneable{
    public MapComponents what; //enum 해당지역이 무슨 타입인지
    public boolean canMove; //해당지역 갈수있는지
    public float arrayX; //타일 좌표 x
    public float arrayY; //타일 좌표 y
    public float positionX;//타일 내부 좌표 중점x
    public float positionZ;//타일 내부 좌표 중점y

    public MapInfo() {
    }

    public MapInfo(MapComponents what) {
        this.what = what;
    }

    public MapInfo(MapComponents what, boolean canMove, float arrayX, float arrayY, float positionX, float positionZ) {
        this.what = what;
        this.canMove = canMove;
        this.arrayX = arrayX;
        this.arrayY = arrayY;
        this.positionX = positionX;
        this.positionZ = positionZ;
    }

    /**
     * 2019 12 26 추가
     * 멤버 변수인 x, z값을 가지고, 3차원 vector 좌표를 생성해 리턴
     * @return
     */
    public Vector3 getPixelPosition(){

        Vector3 position = new Vector3(positionX, 0f, positionZ);

        return position;
    }



    @Override
    public Object clone() {
        MapInfo mapInfo = null;
        try{
            mapInfo = (MapInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return mapInfo;
    }
}
