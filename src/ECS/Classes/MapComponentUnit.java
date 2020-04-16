package ECS.Classes;

import ECS.Factory.MapFactory;
import Enum.MapComponents;

import java.util.ArrayList;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 19 목
 * 업뎃날짜 : 2019 12 20 금
 * 목    적 :
 */
public class MapComponentUnit implements Cloneable {

    /* 멤버 변수 */
    public MapComponents componentType;
    public ArrayList<MapInfo> occupyingTilesList;

    /* 생성자 */

    public MapComponentUnit() {
        componentType = null;
        occupyingTilesList = new ArrayList<>();
    }

    public MapComponentUnit(MapComponents componentType) {
        this.componentType = componentType;
        occupyingTilesList = new ArrayList<>();
    }

    public MapComponentUnit(MapComponents componentType, ArrayList<MapInfo> occupyingTilesList) {
        this.componentType = componentType;
        this.occupyingTilesList = occupyingTilesList;
    }

    /* 매서드 */

    @Override
    public MapComponentUnit clone() {

        MapComponentUnit newMapUnit = null;

        try {
            newMapUnit = (MapComponentUnit) super.clone();

            newMapUnit.occupyingTilesList = new ArrayList<>();
            for(int i=0; i<occupyingTilesList.size(); i++) {
                newMapUnit.occupyingTilesList.add(occupyingTilesList.get(i));
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return newMapUnit;
    }

    public void setComponentType(MapComponents componentType){

        MapInfo mapInfo;
        for (int i=0; i<occupyingTilesList.size(); i++){

            mapInfo = occupyingTilesList.get(i);
            mapInfo.what = componentType;
        }
    }

    public void setMapInfoMovable(boolean isMovable){

        MapInfo mapInfo;
        for (int i=0; i<occupyingTilesList.size(); i++){

            mapInfo = occupyingTilesList.get(i);
            mapInfo.canMove = isMovable;

        }
    }


    /**
     * 해당 맵 컴포넌트가 맵에서 차지하고 있는 영역에 대한 중심 좌표를 구하는 함수
     */
    public Vector3 getCenterPositionFromMapArea(){

        /** 타일 하나 당 길이 정보를 가져온다 */
        int tileLength = MapFactory.TILE_LENGTH;

        /** 현 영역의 시작타일과 마지막 타일을 구한다 */
        MapInfo startTile = occupyingTilesList.get(0);
        MapInfo endTile = occupyingTilesList.get( occupyingTilesList.size() -1 );

        /** 시작 타일의 좌상단 좌표를 구한다 */
        float upperLeftX = startTile.positionX - (tileLength/2.f);
        float upperLeftZ = startTile.positionZ - (tileLength/2.f);

        /** 마지막 타일의 우하단 좌표를 구한다 */
        float belowRightX = endTile.positionX + (tileLength/2.f);
        float belowRightZ = endTile.positionZ + (tileLength/2.f);

        /** 위에서 구한 두 좌표를 가지고, 영역의 중점 좌표를 구한다 */
        float centerX = ( upperLeftX + belowRightX ) / 2;
        float centerZ = ( upperLeftZ+ belowRightZ ) / 2;

        /** 중심좌표값을 가지는 3차원 Vector 생성 */
        Vector3 centerPosition = new Vector3(centerX, 0f, centerZ);

        return centerPosition;

    }





}
