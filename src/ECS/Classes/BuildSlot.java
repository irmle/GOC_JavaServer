package ECS.Classes;

import ECS.Classes.Type.Build.BuildSlotState;
import ECS.Classes.Type.Build.BuildType;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 18 수 새벽
 * 업뎃날짜 : 2019 12 22 일 새벽
 * 목    적 :
 *      포탑, 바리케이드 등 건설 가능한 맵 지형을 관리하기 위한 단위.
 * 수정사항 :
 *      기존 MapInfo mapPosition -->> MapComponentUnit 으로 변경. 이름은 안바꿔도 되겠지??
 *      getBuilderEntityID() 매서드 추가
 */
public class BuildSlot implements Cloneable{

    /* 멤벼 변수 */
    public int slotNum;
    int slotState;

    int buildingType;   // 건물이 지어진( 또는 지어지기로 결정된) 상태일 경우, 어느 타입이 건물인가?
    int buildingEntityID;   // 건물이 지어진 상태일 경우, 해당 Entity의 ID값
    int builderEntityID;    // 건설자

    //public MapInfo mapPosition;    // 맵상 위치
    public MapComponentUnit mapPosition;

    float remainBuildTime;    // 건설이 진행중인 경우, 건설 완료까지 남은 시간


    /* 생성자 */

    /**
     * 월드맵에서, 건설 가능한 지역들을 찾아서 걔네들로 슬롯목록을 만들어 초기화할 때 생성될 것임.
     * 슬롯 번호랑 맵상 위치만 받아서 넣어주고, 나머지는 직접 초기값 처리
     * @param slotNum
     * @param mapPosition
     */
    public BuildSlot(int slotNum, MapComponentUnit mapPosition) {
        this.slotNum = slotNum;
        this.mapPosition = mapPosition;

        //
        this.slotState = BuildSlotState.EMPTY;
        this.buildingType = BuildType.NONE;
        this.buildingEntityID = 0;  // 없다는 의미.
        this.builderEntityID = 0;
        this.remainBuildTime = 0f;

    }

    /* 매서드 */

    /**
     * 추가할 매서드 목록 생각나는대로 적기 // 아이템슬롯 때를 떠올리면서..
     * 1. 슬롯번호로 빌드슬롯 찾기 NO
     * 2. EntityID로 빌드슬롯 찾기 NO
     * 3. 맵위치로 빌드슬롯 찾기 NO
     * ㄴ 헐.. 적고보니까 얘네는 슬롯 하나에 들어갈 매서드가 아님. 슬롯들을 관리하는 측에서 검색용으로 필요한거지.
     * ㄴ 빌드 시스템에 넣어줄 애들.
     *
     * 4. 슬롯상태 세팅
     * 5. 슬롯이 비어있는지 확인하기
     * 6. 슬롯이 차있는지 확인하기
     * 7. 건설가능한 지역이 맞는지?? ..이거는 굳이..
     * 8. 슬롯 비우기 처리
     *
     * 9. 클론  OK
     */

    @Override
    public BuildSlot clone() {

        BuildSlot buildSlot = null;

        try {

            buildSlot = (BuildSlot) super.clone();
            buildSlot.mapPosition = (MapComponentUnit) mapPosition.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return buildSlot;
    }

    public int getSlotState(){

        return this.slotState;
    }

    public void setSlotState(int slotState){

        this.slotState = slotState;
    }


    /**
     * 슬롯이 비어있는 상태가 맞는지 검사한다. 슬롯상태 뿐 아니라, 다른 값들도 같이 검사..
     * .. 쓸 데 없는 짓인가! 걍 상태만 검사해도 되는 부분인데
     * @return
     */
    public boolean isEmpySlot(){

        boolean result = false;

        if((slotState == BuildSlotState.EMPTY)
                && (buildingType == BuildType.NONE) && (buildingEntityID == 0)) {

            result = true;
        }

        return result;
    }

    public void setBuildingType(int buildType){
        this.buildingType = buildType;

    }

    public int getBuildingType(){
        return this.buildingType;
    }

    public int getBuildingEntityID(){

        return this.buildingEntityID;
    }

    public void setBuildingEntityID(int newBuildingEntityID){
        this.buildingEntityID = newBuildingEntityID;
    }


    public void setBuilder(int builderEntityID){

        this.builderEntityID = builderEntityID;
    }

    public float getRemainBuildTime(){
        return this.remainBuildTime;
    }

    public void setRemainBuildTime(float remainBuildTime){

        this.remainBuildTime = remainBuildTime;
    }

    public void decreaseRemainBuildTime(float deltaTime){
        this.remainBuildTime -= deltaTime;
    }

    public void emptySlot(){

        this.buildingType = BuildType.NONE;
        this.remainBuildTime = 0f;
        this.slotState = BuildSlotState.EMPTY;
        this.builderEntityID = 0;
        this.buildingEntityID = 0;

        this.mapPosition.setMapInfoMovable(true);
    }

    public int getBuilderEntityID(){
        return this.builderEntityID;
    }

}
