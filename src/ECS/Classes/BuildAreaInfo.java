package ECS.Classes;


import Enum.MapComponents;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 20
 * 목    적 :
 *      건설슬롯 지점 클라이언트한테 아렬주는 용도로 쓰는 클래스
 */
public class BuildAreaInfo implements Cloneable {

    public MapComponents componentType;
    public Vector3 centerPos;
    public int buildSlotNum;

    public BuildAreaInfo(MapComponents componentType, Vector3 centerPos, int buildSlotNum) {
        this.componentType = componentType;
        this.centerPos = centerPos;
        this.buildSlotNum = buildSlotNum;
    }

    @Override
    public BuildAreaInfo clone() {

        BuildAreaInfo buildAreaInfo;

        try {
            buildAreaInfo = (BuildAreaInfo) super.clone();
            buildAreaInfo.centerPos = (Vector3) centerPos.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return buildAreaInfo;

    }
}
