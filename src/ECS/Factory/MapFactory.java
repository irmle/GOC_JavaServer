package ECS.Factory;

import ECS.Classes.*;
import ECS.Classes.DTO.MapDTO;
import ECS.Classes.Type.PathType;
import ECS.Entity.CharacterEntity;
import ECS.Entity.MonsterEntity;
import ECS.Game.MapDataManager;
import ECS.Game.WorldMap;
import ECS.System.MonsterSystem3;
import Enum.MapType;
import Enum.MapComponents;
import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import sun.swing.StringUIClientPropertyKey;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

/**
 * 2019.12.05 홍성준
 * 월드맵(게임세션)에 들어갈 맵 만드는 공장
 *
 * 2019 12 26 목 권령희
 *      길(이동지점) 찾아주는 함수 추가
 *
 * 2020 01 09 목 권령희
 *      가까운 movePoint 찾는 함수 추가
 *
 * 2020 01 14 화 권령희
 *      A* 알고리즘 관련 함수 추가
 *
 */

public class MapFactory implements Cloneable {
    /**
     * 멤버 변수
     */
    public static MapDTO[] maps;

    // 2019 12 19 목 권령희 추가
    public static HashMap<MapComponents, TileSize> mapComponentSizeInfoTable;

    // 2019 12 20 금 권령희 추가
    public static int TILE_LENGTH = 2;

    // 2020 01 14 화 권령희 추가
    public static int WEIGHT_DEFAULT = 1;
    public static int WEIGHT_DIAGONAL = 2;

    public MapFactory() {
        initFactory();
    }

    /**
     * 매서드
     */


    public static void initFactory() {
        System.out.println("MapFactory 초기화중...");

        mapComponentSizeInfoTable = new HashMap<MapComponents, TileSize>();
        initMapSizeInfo();

        initMap2();

        /** 파일로부터 정보 읽기 */
        //readBarricadeInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        // convertBarricadeInfoToEntity();

        System.out.println("MapFactory 초기화 완료");
    }


    //맵데이터 메니져에서 맵 깊은볶사
    private static void initMap2() {

        MapDTO[] mapDTOS = MapDataManager.gameMaps.get(MapType.MAIN);   // 참조
        maps = new MapDTO[MapDataManager.gameMaps.get((MapType.MAIN)).length];  // 복사용

        for (int i = 0; i < mapDTOS.length; i++) {
            maps[i] = mapDTOS[i].clone();

        }
    }

    /**
     * 2019 12 19 권령희 추가
     * 맵의 각 컴포넌트별 차지하는 타일 사이즈를 초기화한다./ 파일로부터 읽어온다
     */
    public static void initMapSizeInfo(){

        mapComponentSizeInfoTable.put(MapComponents.LAND, new TileSize(1, 1));
        mapComponentSizeInfoTable.put(MapComponents.TURRET_AREA, new TileSize(2, 2));
        mapComponentSizeInfoTable.put(MapComponents.BARRIER_AREA, new TileSize(2, 2));
        mapComponentSizeInfoTable.put(MapComponents.RESPAWN_POINT, new TileSize(1, 1));
        mapComponentSizeInfoTable.put(MapComponents.MONSTER_SPAWN_POINT, new TileSize(1, 1));
        mapComponentSizeInfoTable.put(MapComponents.JUNGLE_SPAWN_POINT, new TileSize(1, 1));
        mapComponentSizeInfoTable.put(MapComponents.MOVE_POINT, new TileSize(1, 1));
        mapComponentSizeInfoTable.put(MapComponents.SPAWN_POINT, new TileSize(1, 1));

        mapComponentSizeInfoTable.put(MapComponents.WALL, new TileSize(1, 1));
        mapComponentSizeInfoTable.put(MapComponents.TURRET, new TileSize(2, 2));
        mapComponentSizeInfoTable.put(MapComponents.BARRIER, new TileSize(2, 2));
        mapComponentSizeInfoTable.put(MapComponents.CRYSTAL_AREA, new TileSize(4, 4));
        mapComponentSizeInfoTable.put(MapComponents.SHOP_AREA, new TileSize(3, 3));

    }

    /**
     * 2019 12 18 권령희 추가
     * <p>
     * 맵 타입을 인자로 받아, 해당하는 맵 정보를 맵매니저로부터 가져와 깊은 복사를 진행, 넘겨주는 함수.
     * 월드맵에서 호출할 것
     * <p>
     * 잘 동작할지 모르겠네,,
     *
     * @param mapType
     * @return
     */
    public static MapDTO[] createMap(MapType mapType) {

        MapDTO[] newMap = null;
        MapDTO[] mapInfo = MapDataManager.gameMaps.get(mapType);

        newMap = new MapDTO[mapInfo.length];
        for (int i = 0; i < mapInfo.length; i++) {
            newMap[i] = mapInfo[i].clone();
        }

        return newMap;
    }

    public static boolean moveCheck(MapDTO[] maps, float posX, float posY) {
        int tileSize = 2;
        boolean canmove = false;
        int y = (int) Math.floor(Math.abs((posX / tileSize)));
        int x = (int) Math.floor(Math.abs((posY / tileSize)));
        //몰라 시바 밥먹고 클라가 보낸 캐릭터xy 좌표 맵에서 이동가능 불가능 첵
        if (x < 100 || y < 100)
            canmove = maps[x].mapInfos.get(y).canMove;
        //System.out.println("what" + y + "/" + x);
        //System.out.println("what" + maps[x].mapInfos.get(y).what);
        return canmove;
    }


    /**
     * 2019 12 26 목 추가
     *
     */
    public static boolean checkTile(MapInfo targetTile, float posX, float posY){

        boolean arrived = false;

        int tileSize = 2;

        int tileY = (int) Math.floor(Math.abs((posX / tileSize)));
        int tileX = (int) Math.floor(Math.abs((posY / tileSize)));

        arrived = ((targetTile.arrayX == tileX) && (targetTile.arrayY == tileY)) ? true : false;

        return arrived;
    }

    public static int getTileX(MapInfo targetTile, float posX, float posY){

        int tileSize = 2;

        int tileX = (int) Math.floor(Math.abs((posY / tileSize)));

        return tileX;
    }

    public static int getTileY(MapInfo targetTile, float posX, float posY){

        int tileSize = 2;

        int tileY = (int) Math.floor(Math.abs((posX / tileSize)));

        return tileY;
    }

    public static MapInfo findTileByPosition(MapDTO[] maps, float posX, float posY){

        int tileSize = 2;

        int tileY = (int) Math.floor(Math.abs((posX / tileSize)));
        int tileX = (int) Math.floor(Math.abs((posY / tileSize)));


        return maps[tileX].mapInfos.get(tileY);

    }

    /**
     * 이 함수 먼가 응용할 수 있을듯.. 찾고자 하는 타일 타입을 주면, 다 찾아주는거.
     * 그러면,, OO타일 찾기 각 함수들은 걍 매핑함수가 되는거지..
     */
    public static ArrayList<MapInfo> findMonsterSpawnPointList(MapDTO[] maps){

        ArrayList<MapInfo> monsterSpawnPoints = new ArrayList<>();

        MapDTO currentDTO;
        MapInfo currentMapInfo;
        for(int i=0; i<maps.length; i++){

            /* DTO 하나를 꺼낸다 */
            currentDTO = maps[i];

            for(int j=0; j<currentDTO.mapInfos.size(); j++){

                /* 타일(MapInfo) 하나를 꺼낸다 */
                currentMapInfo = currentDTO.mapInfos.get(j);

                /* 타일의 타입을 확인한다 */
                if(currentMapInfo.what == MapComponents.MONSTER_SPAWN_POINT){
                    monsterSpawnPoints.add(currentMapInfo);
                }
            }
        }
        // System.out.println("몬스터 스폰 포인트 갯수 : " + monsterSpawnPoints.size());

        return monsterSpawnPoints;
    }

    /**
     * 2019 12 26
     * 탑, 미들, 바텀 이동경로(Path) 목록 검색해주는 함수
     *
     * .. 뻘짓했는데, 일단 다 집어넣고 나서
     *      탑은 Y를 기준으로 정렬해주면 되고
     *      바텀은 X를 기준으로 정렬해주면 되고
     *      미들은 그냥 뒤집어넣으면 됨
     */
    public static HashMap<Integer, ArrayList<MapInfo>> findMovePointsByPath(MapDTO[] maps){

        HashMap<Integer, ArrayList<MapInfo>> pathList = new HashMap<>();

        ArrayList<MapInfo> topPath = new ArrayList<>();
        ArrayList<MapInfo> middlePath = new ArrayList<>();
        ArrayList<MapInfo> bottomPath = new ArrayList<>();

        /** Move Point 목록을 먼저 찾는다 */
        ArrayList<MapInfo> movePointList;
        movePointList = MapFactory.findMapInfoListByType(maps, MapComponents.MOVE_POINT);

        /** movePoint들을 path별로 분류한다*/
        MapInfo currentMapInfo;
        for (int i=0; i<movePointList.size(); i++){

            /* movePoint를 하나 꺼낸다 */
            currentMapInfo = movePointList.get(i);

            /* 타일의 X, Y 좌표를 검사한다 */
            boolean isTop = (currentMapInfo.arrayX < currentMapInfo.arrayY) ? true : false;
            boolean isMiddle = (currentMapInfo.arrayX == currentMapInfo.arrayY) ? true : false;
            boolean isBottom = (currentMapInfo.arrayX > currentMapInfo.arrayY) ? true : false;

            /* 검사(?) 결과에 따라 Path를 분류한다 */
            if(isTop){
                topPath.add(currentMapInfo);
            } else if(isMiddle){
                middlePath.add(currentMapInfo);
            } else if(isBottom){
                bottomPath.add(currentMapInfo);
            }

        }

        /** 각 Path의 특징에 맞게?? 재정렬한다. 몬스터가 시작하는 방향으..로.. */
        // 일단 대강 작성만 해둠. 비교 이렇게 사용하는거 맞는지 모르겠음
        topPath.sort(new Comparator<MapInfo>() {
            @Override
            public int compare(MapInfo o1, MapInfo o2) {
                if(o1.arrayY > o2.arrayY){
                    return -1;
                }
                else if(o1.arrayY < o2.arrayY){
                    return 1;
                }
                return 0;
            }
        });

        bottomPath.sort(new Comparator<MapInfo>() {
            @Override
            public int compare(MapInfo o1, MapInfo o2) {
                if(o1.arrayX > o2.arrayX){
                    return -1;
                }
                else if(o1.arrayX < o2.arrayX){
                    return 1;
                }
                return 0;
            }
        });

        middlePath.sort(new Comparator<MapInfo>() {
            @Override
            public int compare(MapInfo o1, MapInfo o2) {
                if(o1.arrayX > o2.arrayX){
                    return -1;
                }
                else if(o1.arrayX < o2.arrayX){
                    return 1;
                }
                return 0;
            }
        });

        /** hashMap에 넣어준다 */
        pathList.put(PathType.TOP, topPath);
        pathList.put(PathType.MIDDLE, middlePath);
        pathList.put(PathType.BOTTOM, bottomPath);

        return pathList;
    }

    /**
     * 앤티티의 현재 픽셀 좌표를 기준으로, 가장 가까운 타일을 찾는다.
     * @param worldMap  ;
     * @param currentPosition ; 앤티티(아마도 몬스터)의 현재 위치(픽셀좌표)
     *                              ㄴ 나중에.. 플레이어 팀 측에 인공지능이 추가된다면야.. 같이 활용할 순 있겠지
     * @return
     */
    public static MapInfo findNearMovePoint(WorldMap worldMap, Vector3 currentPosition){

        MapInfo nearTile = new MapInfo(); // 리턴값

        /** 맵에 존재하는 전체 move point 목록을 찾는다 */
        ArrayList<MapInfo> movePointList;
        movePointList = MapFactory.findMapInfoListByType(worldMap.gameMap, MapComponents.MOVE_POINT);

        /** 몬스터의 픽셀좌표(넘어온 값 ; currentPosition)를 기준으로, 가장 가까운 move point 타일을 찾는다. */

        MapInfo currentMovePoint = null;
        MapInfo currentNearMP = null;
        Vector3 currentMovePosition = movePointList.get(0).getPixelPosition();
        float minDistance = Vector3.distance(currentPosition, currentMovePosition);
        float currentDistance;

        for (int i=1; i<movePointList.size(); i++){

            currentMovePoint = movePointList.get(i);
            currentMovePosition = currentMovePoint.getPixelPosition();
            currentDistance = Vector3.distance(currentPosition, currentMovePosition);

            if(currentDistance < minDistance){
                minDistance = currentDistance;
                currentNearMP = currentMovePoint;
            }

        }

        /** 해당 타일이 어느 경로(path)에 속해있는지 찾는다 ; 탑, 미들, 바텀 */

        ArrayList<MapInfo> path;
        int pathType = PathType.NONE;
        for(HashMap.Entry<Integer, ArrayList<MapInfo>> route : worldMap.pathList.entrySet()){

            path = route.getValue();
            if(path.contains(currentNearMP)){
                pathType = route.getKey();
                break;
            }
        }

        /** Path에서 해당 타일의 다음 타일을 가져온다 */
        int currentNearIndex = worldMap.pathList.get(pathType).indexOf(currentNearMP);
        MapInfo nextNearMP = worldMap.pathList.get(pathType).get(currentNearIndex + 1);


        /** 두 타일 중, 몬스터가 타겟으로 삼아야 할 타일을 결정한다 */

        /* 두 가지 길이 값을 구한다 */
        float nearToNext = Vector3.distance(currentMovePoint.getPixelPosition(), nextNearMP.getPixelPosition());
        float currentToNear;
        float currentToNext = Vector3.distance(currentPosition, nextNearMP.getPixelPosition());

        /* 판단 */
        if(nearToNext >= currentToNext){
            nearTile = nextNearMP;
        }
        else{
            nearTile = currentNearMP;
        }


        /** 결정된 쪽의 타일을 반환한다 */

        return nearTile;
    }

    public static MapInfo findNearMovePointVer20200213(WorldMap worldMap, Vector3 currentPosition){

        MapInfo nearTile = new MapInfo(); // 리턴값

        /** 맵에 존재하는 전체 move point 목록을 찾는다 */
        ArrayList<MapInfo> movePointList;
        movePointList = MapFactory.findMapInfoListByType(worldMap.gameMap, MapComponents.MOVE_POINT);

        /** 몬스터의 픽셀좌표(넘어온 값 ; currentPosition)를 기준으로, 가장 가까운 move point 타일을 찾는다. */

        MapInfo currentMovePoint = null;
        MapInfo currentNearMP = null;
        Vector3 currentMovePosition = movePointList.get(0).getPixelPosition();
        float minDistance = Vector3.distance(currentPosition, currentMovePosition);
        float currentDistance;

        for (int i=1; i<movePointList.size(); i++){

            currentMovePoint = movePointList.get(i);
            currentMovePosition = currentMovePoint.getPixelPosition();
            currentDistance = Vector3.distance(currentPosition, currentMovePosition);

            if(currentDistance < minDistance){
                minDistance = currentDistance;
                currentNearMP = currentMovePoint;
            }

        }

        /** 해당 타일이 어느 경로(path)에 속해있는지 찾는다 ; 탑, 미들, 바텀 */

        ArrayList<MapInfo> path;
        int pathType = PathType.NONE;
        for(HashMap.Entry<Integer, ArrayList<MapInfo>> route : worldMap.pathList.entrySet()){

            path = route.getValue();
            if(path.contains(currentNearMP)){
                pathType = route.getKey();
                break;
            }
        }

        /** Path에서 해당 타일의 다음 타일을 가져온다 */
        int currentNearIndex = worldMap.pathList.get(pathType).indexOf(currentNearMP);

        // 만약 타일의 끝이라면.. 해당 타일을 목적지로 삼아야지...
        if((currentNearIndex+1) == worldMap.pathList.get((pathType)).size()){
            nearTile = currentNearMP;

        }
        else {

            MapInfo nextNearMP = worldMap.pathList.get(pathType).get(currentNearIndex + 1);

            /** 두 타일 중, 몬스터가 타겟으로 삼아야 할 타일을 결정한다 */

            /* 두 가지 길이 값을 구한다 */
            float nearToNext = Vector3.distance(currentMovePoint.getPixelPosition(), nextNearMP.getPixelPosition());
            float currentToNear;
            float currentToNext = Vector3.distance(currentPosition, nextNearMP.getPixelPosition());

            /* 판단 */
            if(nearToNext >= currentToNext){
                nearTile = nextNearMP;
            }
            else{
                nearTile = currentNearMP;
            }

        }


        /** 결정된 쪽의 타일을 반환한다 */

        return nearTile;
    }


    /**
     * 건네받은 타일이 어느 경로(탑, 미들, 바텀)에 속해있는지 알려준다.
     */
    public static int findPathTypeByMapInfo(WorldMap worldMap, MapInfo mapInfo){

        ArrayList<MapInfo> path;
        int pathType = PathType.NONE;
        for(HashMap.Entry<Integer, ArrayList<MapInfo>> route : worldMap.pathList.entrySet()){

            path = route.getValue();
            if(path.contains(mapInfo)){
                pathType = route.getKey();
                break;
            }
        }

        return pathType;
    }



    /**
     * 지정된 타입의 타일을 모두 찾아주는 매서드
     */
    public static ArrayList<MapInfo> findMapInfoListByType(MapDTO[] maps, MapComponents mapType){

        ArrayList<MapInfo> targetTileList = new ArrayList<>();

        MapDTO currentDTO;
        MapInfo currentMapInfo;
        for(int i=0; i<maps.length; i++){

            /* DTO 하나를 꺼낸다 */
            currentDTO = maps[i];

            for(int j=0; j<currentDTO.mapInfos.size(); j++){

                /* 타일(MapInfo) 하나를 꺼낸다 */
                currentMapInfo = currentDTO.mapInfos.get(j);

                /* 타일의 타입을 확인한다 */
                if(currentMapInfo.what == mapType){
                    targetTileList.add(currentMapInfo);
                }
            }
        }

        return targetTileList;

    }



    /**
     * 2019 12 18 수 작성
     * 2019 12 19 목 업뎃 ; MapInfo -> MapComponentUnit 적용
     * 맵을 처음부터 끝까지 훑어, 포탑, 바리케이드 등 건설 가능 지점들만 찾아 리스트로 만들어 반환한다
     * ㄴ>> 맵 전체 컴포넌트유닛 리스트를 훑어, 포탑/바리케이드 등 건설 가능 지점들만 찾아 리스트로 만들어 반환한다
     *
     */
    public static ArrayList<MapComponentUnit> getBuildableArea(ArrayList<MapComponentUnit> entireMapComponentUnitList){

        ArrayList<MapComponentUnit> buildAreaArrayList = new ArrayList<>();

        MapComponents currentMapComponent;
        MapComponentUnit currentComponentUnit;

        boolean isBuildableArea;

        for (int i=0; i<entireMapComponentUnitList.size(); i++){

            /* 맵 컴포넌트 유닛을 하나 꺼낸다 */
            currentComponentUnit = entireMapComponentUnitList.get(i);
            currentMapComponent = currentComponentUnit.componentType;

            /* 해당 유닛 영역이 건설 가능한 지점인지 판단한다 */
            isBuildableArea = ( (currentMapComponent == MapComponents.BARRIER_AREA)
                    || (currentMapComponent == MapComponents.TURRET_AREA) ) ? true : false;

            /* 건설 가능하다면, [건설가능한 영역 목록]에 추가한다 */
            if(isBuildableArea){
                buildAreaArrayList.add(currentComponentUnit);
            }

        }

        return buildAreaArrayList;

    }


    /**
     * 2019 12 19 목 권령희
     * 맵 정보로부터, 맵의 타일을 각 컴포넌트 타입별로 인식해서 리스트로 반환한다
     * @param maps
     * @return
     */
    public static ArrayList<MapComponentUnit> getMapComponentUnitsFromEntireMap(MapDTO[] maps){

        ArrayList<MapComponentUnit> componentUnitList = new ArrayList<>();
        boolean[][] checkedArea = new boolean[maps.length][maps[0].mapInfos.size()]; // ...

        ArrayList<MapInfo> occupyingTiles;
        MapDTO currentDTO;
        MapInfo currentMapInfo;


        /* checkArea를 초기화한다 */
        initCheckedArea(checkedArea);

        /* 전체 타일을 한 칸 씩 읽으며 반복한다 */
        for(int i=0; i<maps.length; i++){

            /* DTO 하나를 꺼낸다 */
            currentDTO = maps[i];

            for(int j=0; j<currentDTO.mapInfos.size(); j++){

                /* 타일(MapInfo) 하나를 꺼낸다 */
                currentMapInfo = currentDTO.mapInfos.get(j);

                /* 이미 영역이 확인된 타일이라면, 다음 타일로 넘어간다 */
                boolean isAlreadyChecked = (checkedArea[i][j] == true) ? true : false;
                if(isAlreadyChecked){
                    continue;
                }

                /* 해당 타일이 속한 컴포넌트의 맵 크기정보를 가져온다 */
                TileSize currentMapSize = mapComponentSizeInfoTable.get(currentMapInfo.what);

                /* 현재 좌표로부터, 해당하는 맵 컴포넌트의 크기만큼 타일정보를 읽어들인다 */
                occupyingTiles = findComponentArea(i, j, currentMapSize, checkedArea, maps);

                /* 읽어들인 타일 영역을 가지고, 새 맵 컴포넌트 유닛을 생성한다 */
                MapComponentUnit newMapUnit = new MapComponentUnit(currentMapInfo.what, occupyingTiles);

                /* 생성한 유닛을 전체 맵 유닛 목록에 추가한다 */
                componentUnitList.add(newMapUnit);

            }
        }

        return componentUnitList;
    }

    /**
     * 2019 12 19 목 권령희
     * [확인된 영역] 배열을 초기화한다
     * 확인된 영역은, 전체 맵 타일을 맵 컴포넌트 단위로 인식하는 처리를 수행하기 바로 직전에 초기화된다.
     *
     * @param checkedArea
     */
    public static void initCheckedArea(boolean[][] checkedArea){

        for(int i=0; i<maps.length; i++){

            MapDTO mapDTO = maps[i];
            for (int j=0; j<mapDTO.mapInfos.size(); j++){

                checkedArea[i][j] = false;
            }
        }
    }

    /**
     * 2019 12 19 목 권령희
     * 주어진 타일 좌표를 시작으로, 주어진 크기만큼 타일을 읽어 한 영역으로 인식한다.
     */
    public static ArrayList<MapInfo> findComponentArea(int startX, int startY, TileSize tileSize, boolean[][] checkedArea, MapDTO[] maps){

        ArrayList<MapInfo> componentArea = new ArrayList<>();

        MapDTO mapDTO;
        MapInfo mapInfo;

        /* 시작지점(startX, startY)부터 타일 사이즈만큼 검색한다 */
        for (int i = startX; i < startX + tileSize.sizeX; i++){

            mapDTO = maps[startX];
            for(int j = startY; j < startY + tileSize.sizeY; j++){

                /* 범위 내 타일 하나를 꺼내고 */
                mapInfo = mapDTO.mapInfos.get(j);

                /* 해당 타일을 컴포넌트 영역에 추가한다 */
                componentArea.add(mapInfo);

                /* 확인한 영역으로 체크한다 */
                checkedArea[i][j] = true;
            }
        }

        return componentArea;
    }


    /**
     * 건설가능 지점들을..
     * @param buildSlotList
     * @return
     */
    public ArrayList<BuildAreaInfo> createBuildableAreaInfoList(ArrayList<BuildSlot> buildSlotList){

        ArrayList<BuildAreaInfo> buildAreaInfoList = new ArrayList<>();

        BuildSlot buildSlot;
        Vector3 centerPos;
        for(int i=0; i<buildSlotList.size(); i++){

            buildSlot = buildSlotList.get(i);
            centerPos = buildSlot.mapPosition.getCenterPositionFromMapArea();

            BuildAreaInfo newBuildableArea = new BuildAreaInfo(buildSlot.mapPosition.componentType,
                    centerPos, buildSlot.slotNum);

            buildAreaInfoList.add(newBuildableArea);
        }

        return buildAreaInfoList;

    }

    public static void testMap(ArrayList<MapComponentUnit> mapComponentUnits) {

        // System.out.println("=======================================================================");

        MapComponentUnit unit;
        for (int i = 0; i < mapComponentUnits.size(); i++) {

            unit = mapComponentUnits.get(i);
            if (unit.componentType == MapComponents.WALL) {

                MapInfo mapInfo;
                for (int j = 0; j < unit.occupyingTilesList.size(); j++) {
                    mapInfo = unit.occupyingTilesList.get(j);
                    // System.out.println("벽 좌표 : " + mapInfo.arrayX + ", " + mapInfo.arrayY);
                }
            }
        }

        // System.out.println("=======================================================================");
    }


    /** Path Find 매서드 추가 */

    /**
     * 길 찾기 매서드
     * @param worldMap
     * @param startPoint 길찾기 시작지점(현재 위치)
     * @param endPoint 목적지(도착 위치)
     * @return
     */
    public static ArrayList<MapInfo> pathFind(WorldMap worldMap, MapInfo startPoint, MapInfo endPoint){

        System.out.println("패스 파인딩 호출");
        // System.out.println("출발 지점 : " + startPoint.arrayX + ", " + startPoint.arrayY);
        // System.out.println("도착 지점 : " + endPoint.arrayX + ", " + endPoint.arrayY);


        ArrayList<MapInfo> finalPath = new ArrayList();
        ArrayList<Node> openedNodeList = new ArrayList<>();
        //ArrayList<Node> closedNodeList = new ArrayList<>();

        /* 노드맵을 초기화한다 */
        initNodeMap(worldMap.nodeMap);

        Node startNode = worldMap.nodeMap[(int)startPoint.arrayX][(int)startPoint.arrayY];
        Node endNode = worldMap.nodeMap[(int)endPoint.arrayX][(int)endPoint.arrayY];
        // System.out.println("출발 노드 지점 : " + (int)startPoint.arrayX + ", " + (int)startPoint.arrayY);
        // System.out.println("도착 노드 지점 : " + (int)endPoint.arrayX + ", " + (int)endPoint.arrayY);

        /* 열린 노드 목록에 시작 노드를 넣는다 */
        startNode.setNodeValue(0f, 0f, 0f);
        openedNodeList.add(startNode);

        /* 목적지까지의 경로롤 모두 찾을 때 까지 반복한다 */
        while( !openedNodeList.isEmpty() ){
            //while( true ){

            /* 열린 목록에서 노드를 하나 꺼낸다 */
            // Node currentNode = openedNodeList.remove(0);
            Node currentNode = openedNodeList.get(0);

            /* 꺼낸 노드가 목적 노드와 일치한다면 중단 */
            if(currentNode == endNode){ // 아.. 설마 endNode의 부모 설정을 안해줬나?? 아니 근데.. 애초에 열린 목록에 집어넣을 떄..

                System.out.println("경로 다 찾음");
                break;
            }

            /* 해당 노드를 닫힌 목록에 집어넣는다 ==>> 닫는다 */
            //closedNodeList.add(currentNode);
            currentNode.setClosed(true);
            openedNodeList.remove(0);

            //System.out.println("설마 지워진 건 아니겟지?? : " + currentNode.isClosed);

            /* 현 노드의 인접 노드 목록을 구한다 */
            ArrayList<Node> neighborNodes = getNeighborNode(worldMap, worldMap.nodeMap, currentNode);

            /* 인접 노드 목록에 대해 반복한다 */
            Node currentNeighbor;
            for(int i=0; i<neighborNodes.size(); i++){

                currentNeighbor = neighborNodes.get(i);

                /* 노드가 닫힌 목록에 들어있는 경우, 다음으로 넘어간다 */
                if(currentNeighbor.isClosed){
                    continue;
                }


                /* 노드의 g, h, f를 계산한다. */

                int gVal = (
                        (Math.abs( ( (int)currentNode.getTile().arrayX - (int)currentNeighbor.getTile().arrayX ) ) == 1)
                                && (Math.abs ( ( (int)currentNode.getTile().arrayY - (int)currentNeighbor.getTile().arrayY ) ) == 1)
                )
                        ? WEIGHT_DIAGONAL : WEIGHT_DEFAULT ;


                float g = currentNode.g + gVal;
                float h = (float) Math.sqrt(
                        Math.pow( Math.abs(currentNeighbor.getTile().arrayX - endNode.getTile().arrayX), 2)
                                + Math.pow( Math.abs(currentNeighbor.getTile().arrayY - endNode.getTile().arrayY), 2));
                float f = g + h;

                /*
                System.out.println("[ " + currentNeighbor.getTile().arrayX + ", " + currentNeighbor.getTile().arrayY + ", " +
                        currentNeighbor.isClosed
                        + ", f=" + f
                        + ", g=" + g
                        + ", h=" + h+ " ]");*/


                /* 노드가 열린 목록에 들어있지 않다면 */
                if( ! openedNodeList.contains(currentNeighbor)){

                    // System.out.println("노드가 열린 목록에 들어있지 않음");
                    currentNeighbor.setNodeValue(g, h, f);  // 위에서 계산한 값 세팅 후
                    currentNeighbor.setParentNode(currentNode);
                    openedNodeList.add(currentNeighbor);    // 열린 목록에 넣어준다
                    /*System.out.println("[ " + openedNodeList.get(i).getTile().arrayX + ", " + openedNodeList.get(i).getTile().arrayY + ", " +
                            openedNodeList.get(i).isClosed
                            + ", f=" + openedNodeList.get(i).f
                            + ", g=" + openedNodeList.get(i).g
                            + ", h=" + openedNodeList.get(i).h+ " ] 를 열린 목록에 넣음");*/
                }
                else {

                    // System.out.println("노드가 열린 목록에 들어있음");
                    /* 기존 노드의 f값과 현재 계산한 f 값을 비교해서, 더 낮은 값으로 업데이트 해준다 */
                    boolean lowerThanFormer = ( f < currentNeighbor.f ) ? true : false;
                    // System.out.println("현재 계산한 f값 : " + f + ", 기존 f값 : " + currentNeighbor.f);
                    if(lowerThanFormer){

                        currentNeighbor.setNodeValue(g, h, f);  // 위에서 계산한 값 세팅 후
                        currentNeighbor.setParentNode(currentNode);

                        // System.out.println(" 업데이트 함");

                    }
                }
            }

            // 오름차순
            openedNodeList.sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {

                    if(o1.f < o2.f){
                        return -1;
                    }
                    else if(o1.f > o2.f){
                        return 1;
                    }
                    return 0;
                }
            });


            if(false){
                System.out.println("========== 정렬 확인용 ============");
                for(int i=0; i<openedNodeList.size(); i++){
                    System.out.print("[ " + openedNodeList.get(i).getTile().arrayX + ", " + openedNodeList.get(i).getTile().arrayY + ", " +
                            openedNodeList.get(i).isClosed + ", " + openedNodeList.get(i).f + " ]");
                }
                System.out.println("");

                int closedNum = 0;
                //System.out.println("========== 정렬 확인용 ============");
                //System.out.println("노드맵 가로 길이 : " + worldMap.nodeMap.length);
                for(int i=0; i<worldMap.nodeMap.length; i++){

                    Node[] nodes = worldMap.nodeMap[i];
                    //System.out.println("노드맵 세로 길이 : " + nodes.length);
                    for(int j=0; j<nodes.length; j++){
                        if(nodes[j].isClosed == true){
                            closedNum++;
                        }
                    }

                }
                System.out.println("닫힌 갯수 : " + closedNum);
            }


        }

        // System.out.println("끝 노드의 부모 : " + endNode.getParentNode());

        /* 경로 나열....??  */
        Node pathNode = endNode;    // 부모 노드부터 시작해서
        ArrayList<MapInfo> tempPath = new ArrayList();
        while(true){

            // System.out.print("[ " + pathNode.getTile().arrayX + ", " + pathNode.getTile().arrayY + ", " + pathNode.getTile().canMove + " ]");
            tempPath.add(pathNode.getTile());  // 노드의 타일을 하나씩 꺼낸다
            pathNode = pathNode.getParentNode();
            if(pathNode == null){
                break;
            }

        }

        // 최종
        // System.out.println("패스파인딩 경로 수 : " + tempPath.size());
        for(int i=tempPath.size()-1; i>=0; i--){
            finalPath.add(tempPath.get(i));
            // System.out.print("[ " + tempPath.get(i).arrayX + ", " + tempPath.get(i).arrayY + ", " + tempPath.get(i).canMove + " ]");
        }
        // System.out.println("");


        return finalPath;
    }

    public static ArrayList<MapInfo> pathFindForJungle(WorldMap worldMap, MapInfo startPoint, MapInfo endPoint, int targetID){

        System.out.println("패스 파인딩 호출");
        // System.out.println("출발 지점 : " + startPoint.arrayX + ", " + startPoint.arrayY);
        // System.out.println("도착 지점 : " + endPoint.arrayX + ", " + endPoint.arrayY);


        ArrayList<MapInfo> finalPath = new ArrayList();
        ArrayList<Node> openedNodeList = new ArrayList<>();
        //ArrayList<Node> closedNodeList = new ArrayList<>();

        /* 노드맵을 초기화한다 */
        initNodeMap(worldMap.nodeMap);

        Node startNode = worldMap.nodeMap[(int)startPoint.arrayX][(int)startPoint.arrayY];
        Node endNode = worldMap.nodeMap[(int)endPoint.arrayX][(int)endPoint.arrayY];
        // System.out.println("출발 노드 지점 : " + (int)startPoint.arrayX + ", " + (int)startPoint.arrayY);
        // System.out.println("도착 노드 지점 : " + (int)endPoint.arrayX + ", " + (int)endPoint.arrayY);

        /* 열린 노드 목록에 시작 노드를 넣는다 */
        startNode.setNodeValue(0f, 0f, 0f);
        openedNodeList.add(startNode);

        /* 목적지까지의 경로롤 모두 찾을 때 까지 반복한다 */
        while( !openedNodeList.isEmpty() ){
            //while( true ){

            /* 열린 목록에서 노드를 하나 꺼낸다 */
            // Node currentNode = openedNodeList.remove(0);
            Node currentNode = openedNodeList.get(0);

            /* 꺼낸 노드가 목적 노드와 일치한다면 중단 */
            if(currentNode == endNode){ // 아.. 설마 endNode의 부모 설정을 안해줬나?? 아니 근데.. 애초에 열린 목록에 집어넣을 떄..

                System.out.println("경로 다 찾음");
                break;
            }

            /* 해당 노드를 닫힌 목록에 집어넣는다 ==>> 닫는다 */
            //closedNodeList.add(currentNode);
            currentNode.setClosed(true);
            openedNodeList.remove(0);

            //System.out.println("설마 지워진 건 아니겟지?? : " + currentNode.isClosed);

            /* 현 노드의 인접 노드 목록을 구한다 */
            ArrayList<Node> neighborNodes = getNeighborNodeForJungle(worldMap, worldMap.nodeMap, currentNode, targetID);

            /* 인접 노드 목록에 대해 반복한다 */
            Node currentNeighbor;
            for(int i=0; i<neighborNodes.size(); i++){

                currentNeighbor = neighborNodes.get(i);

                /* 노드가 닫힌 목록에 들어있는 경우, 다음으로 넘어간다 */
                if(currentNeighbor.isClosed){
                    continue;
                }


                /* 노드의 g, h, f를 계산한다. */

                int gVal = (
                        (Math.abs( ( (int)currentNode.getTile().arrayX - (int)currentNeighbor.getTile().arrayX ) ) == 1)
                                && (Math.abs ( ( (int)currentNode.getTile().arrayY - (int)currentNeighbor.getTile().arrayY ) ) == 1)
                )
                        ? WEIGHT_DIAGONAL : WEIGHT_DEFAULT ;


                float g = currentNode.g + gVal;
                float h = (float) Math.sqrt(
                        Math.pow( Math.abs(currentNeighbor.getTile().arrayX - endNode.getTile().arrayX), 2)
                                + Math.pow( Math.abs(currentNeighbor.getTile().arrayY - endNode.getTile().arrayY), 2));
                float f = g + h;

                /*
                System.out.println("[ " + currentNeighbor.getTile().arrayX + ", " + currentNeighbor.getTile().arrayY + ", " +
                        currentNeighbor.isClosed
                        + ", f=" + f
                        + ", g=" + g
                        + ", h=" + h+ " ]");*/


                /* 노드가 열린 목록에 들어있지 않다면 */
                if( ! openedNodeList.contains(currentNeighbor)){

                    // System.out.println("노드가 열린 목록에 들어있지 않음");
                    currentNeighbor.setNodeValue(g, h, f);  // 위에서 계산한 값 세팅 후
                    currentNeighbor.setParentNode(currentNode);
                    openedNodeList.add(currentNeighbor);    // 열린 목록에 넣어준다
                    /*System.out.println("[ " + openedNodeList.get(i).getTile().arrayX + ", " + openedNodeList.get(i).getTile().arrayY + ", " +
                            openedNodeList.get(i).isClosed
                            + ", f=" + openedNodeList.get(i).f
                            + ", g=" + openedNodeList.get(i).g
                            + ", h=" + openedNodeList.get(i).h+ " ] 를 열린 목록에 넣음");*/
                }
                else {

                    // System.out.println("노드가 열린 목록에 들어있음");
                    /* 기존 노드의 f값과 현재 계산한 f 값을 비교해서, 더 낮은 값으로 업데이트 해준다 */
                    boolean lowerThanFormer = ( f < currentNeighbor.f ) ? true : false;
                    // System.out.println("현재 계산한 f값 : " + f + ", 기존 f값 : " + currentNeighbor.f);
                    if(lowerThanFormer){

                        currentNeighbor.setNodeValue(g, h, f);  // 위에서 계산한 값 세팅 후
                        currentNeighbor.setParentNode(currentNode);

                        // System.out.println(" 업데이트 함");

                    }
                }
            }

            // 오름차순
            openedNodeList.sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {

                    if(o1.f < o2.f){
                        return -1;
                    }
                    else if(o1.f > o2.f){
                        return 1;
                    }
                    return 0;
                }
            });


            if(false){
                System.out.println("========== 정렬 확인용 ============");
                for(int i=0; i<openedNodeList.size(); i++){
                    System.out.print("[ " + openedNodeList.get(i).getTile().arrayX + ", " + openedNodeList.get(i).getTile().arrayY + ", " +
                            openedNodeList.get(i).isClosed + ", " + openedNodeList.get(i).f + " ]");
                }
                System.out.println("");

                int closedNum = 0;
                //System.out.println("========== 정렬 확인용 ============");
                //System.out.println("노드맵 가로 길이 : " + worldMap.nodeMap.length);
                for(int i=0; i<worldMap.nodeMap.length; i++){

                    Node[] nodes = worldMap.nodeMap[i];
                    //System.out.println("노드맵 세로 길이 : " + nodes.length);
                    for(int j=0; j<nodes.length; j++){
                        if(nodes[j].isClosed == true){
                            closedNum++;
                        }
                    }

                }
                System.out.println("닫힌 갯수 : " + closedNum);
            }


        }

        // System.out.println("끝 노드의 부모 : " + endNode.getParentNode());

        /* 경로 나열....??  */
        Node pathNode = endNode;    // 부모 노드부터 시작해서
        ArrayList<MapInfo> tempPath = new ArrayList();
        while(true){

            // System.out.print("[ " + pathNode.getTile().arrayX + ", " + pathNode.getTile().arrayY + ", " + pathNode.getTile().canMove + " ]");
            tempPath.add(pathNode.getTile());  // 노드의 타일을 하나씩 꺼낸다
            pathNode = pathNode.getParentNode();
            if(pathNode == null){
                break;
            }

        }

        // 최종
        // System.out.println("패스파인딩 경로 수 : " + tempPath.size());
        for(int i=tempPath.size()-1; i>=0; i--){
            finalPath.add(tempPath.get(i));
            // System.out.print("[ " + tempPath.get(i).arrayX + ", " + tempPath.get(i).arrayY + ", " + tempPath.get(i).canMove + " ]");
        }
        // System.out.println("");


        return finalPath;
    }

    /**
     * 게임 맵 배열 전체를 하나씩 읽으면서, 대응하는 노드를 하나씩 생성한다
     * 월드맵에서 호출..
     * @param maps
     */
    public static Node[][] createNodeMap(MapDTO[] maps){

        // System.out.println("노드 맵 생성");

        Node[][] nodeMap = new Node[maps.length][maps.length];

        ArrayList<MapInfo> mapInfos;
        for(int i=0; i<maps.length; i++){

            mapInfos = maps[i].mapInfos;
            for(int j=0; j<mapInfos.size(); j++){
                nodeMap[i][j] = new Node(mapInfos.get(j));

                /*System.out.println("노드맵 인덱스 : [ " + i + ", " + j + " ]");
                System.out.println("맵 인덱스 : [ " + mapInfos.get(j).arrayX + ", " + mapInfos.get(j).arrayY + " ]");
                System.out.println("맵 pos : [ " + mapInfos.get(j).positionX + ", " + mapInfos.get(j).positionZ + " ]");*/
            }
        }

        // System.out.println("사이즈 : " + nodeMap.length + ", " + nodeMap[0].length);

        return nodeMap;
    }

    /**
     * 노드맵 전체를 초기화한다.
     * 길찾기 알고리즘을 시작하기 전에 수행한다
     * @param nodeMap
     */
    public static void initNodeMap(Node[][] nodeMap){

        // System.out.println("노드맵 초기화");

        for(int i=0; i<nodeMap.length; i++){
            for(int j=0; j<nodeMap[i].length; j++){
                nodeMap[i][j].initNode();
            }
        }
    }

    /**
     * 주어진 노드에 인접해 있는 노드들 중, 이동 가능한 목록을 찾는다
     * @param nodeMap
     * @param node
     * @return
     */
    public static ArrayList<Node> getNeighborNode(WorldMap worldMap, Node[][] nodeMap, Node node){

        // System.out.print("이웃하는 노드들을 검색합니다 ;  ");

        ArrayList<Node> nodes = new ArrayList<>();

        /* 노드가 가리키는 타일을 꺼내, 좌표를 확인한다 */
        MapInfo tile = node.getTile();
        int x = (int) tile.arrayX;
        int y = (int) tile.arrayY;

        // System.out.println("[" + x + "][" + y + "]");

        MapInfo currentTile;
        /* 현재 노드에 인접해 있는 노드들을 검사한다 */
        for(int i = x-1; i <= x+1; i++){    // "<" ==>> "<="
            for(int j = y-1; j <= y+1; j++){

                /* 현재 노드인 경우 넘어감 */ // 아 이게 잘못됐네.. .
                if( (i == x) && (j == y) ) {
                    continue;
                }

                /* 이동 가능한 타일인 경우, 인접 노드 목록에 추가한다  */
                currentTile = nodeMap[i][j].getTile();
                boolean isWalkable = ( currentTile.canMove == true ) ? true : false;

                /** 2020 02 19 수 */
                boolean isEmptyTile = MonsterSystem3.isTileEmpty(worldMap, currentTile, 0);

                // System.out.println("[" + i + "][" + j + "] 타일 : " + isWalkable);
                if(isWalkable && isEmptyTile){
                    nodes.add(nodeMap[i][j]);

                    // System.out.println("[" + i + "][" + j + "] 타일 노드를 추가합니다");
                }
            }
        }

        return nodes;
    }

    public static ArrayList<Node> getNeighborNodeForJungle(WorldMap worldMap, Node[][] nodeMap, Node node, int targetID){

        // System.out.print("이웃하는 노드들을 검색합니다 ;  ");

        ArrayList<Node> nodes = new ArrayList<>();

        /* 노드가 가리키는 타일을 꺼내, 좌표를 확인한다 */
        MapInfo tile = node.getTile();
        int x = (int) tile.arrayX;
        int y = (int) tile.arrayY;

        //System.out.println("[" + x + "][" + y + "]");

        MapInfo currentTile;
        /* 현재 노드에 인접해 있는 노드들을 검사한다 */
        for(int i = x-1; i <= x+1; i++){    // "<" ==>> "<="
            for(int j = y-1; j <= y+1; j++){

                /* 현재 노드인 경우 넘어감 */ // 아 이게 잘못됐네.. .
                if( (i == x) && (j == y) ) {
                    continue;
                }

                /* 이동 가능한 타일인 경우, 인접 노드 목록에 추가한다  */
                currentTile = nodeMap[i][j].getTile();
                boolean isWalkable = ( currentTile.canMove == true ) ? true : false;

                /** 2020 02 28 금 */
                boolean isEmptyTile = isTileEmpty20200228ver(worldMap, currentTile, targetID);

                //System.out.println("[" + i + "][" + j + "] 타일 : " + isWalkable);
                if(isWalkable && isEmptyTile){
                    nodes.add(nodeMap[i][j]);

                    //System.out.println("[" + i + "][" + j + "] 타일 노드를 추가합니다");
                }
            }
        }

        return nodes;
    }



    public static MapDTO[] deepCopy(MapDTO[] arr) {
        if (arr == null) return null;
        MapDTO[] result = new MapDTO[arr.length];
        System.arraycopy(arr, 0, result, 0, arr.length);

        for (int i = 0; i < arr.length; i++) {
            result[i].mapInfos.add((MapInfo) arr[i].mapInfos.get(i).clone());
        }

        return result;
    }

    /**
     * 몬스터 뿐만 아니라, ...
     * @param worldMap
     * @param targetTile
     * @param targetEntityID
     * @return
     */
    public static boolean isTileEmpty20200228ver(WorldMap worldMap, MapInfo targetTile, int targetEntityID){

        boolean isEmpty = true;

        for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()) {

            /** 0. 몬스터 정보 */
            MonsterEntity monster = monsterEntity.getValue();
            if ((monster.hpComponent.currentHP <= 0) || monster.entityID == targetEntityID) {
                continue;
            }

            Vector3 mobPos = monster.positionComponent.position;
            MapInfo mobPosTile = MapFactory.findTileByPosition(worldMap.gameMap, mobPos.x(), mobPos.z());

            if (mobPosTile == targetTile) {
                isEmpty = false;
            }
        }

        for(HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            /** 캐릭터 정보 */
            CharacterEntity character = characterEntity.getValue();
            if ((character.hpComponent.currentHP <= 0) ||character.entityID == targetEntityID) {
                continue;
            }

            Vector3 charPos = character.positionComponent.position;
            MapInfo charPosTile = MapFactory.findTileByPosition(worldMap.gameMap, charPos.x(), charPos.z());

            if (charPosTile == targetTile) {
                isEmpty = false;
            }
        }

        return isEmpty;

    }

    @Override
    public MapFactory clone() {
        MapFactory clone;
        try {
            clone = (MapFactory) super.clone();
            if (maps != null) {
                // clone.maps = deepCopy(maps);
                clone.maps = maps.clone();
            } else {
                clone.maps = null;
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }


}