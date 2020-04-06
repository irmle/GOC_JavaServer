package ECS.Game;

import ECS.Classes.DTO.MapDTO;
import Enum.MapType;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * 2019.12.06 홍성준
 * 맵 에디터에서 뽑아낸 json 파일 서버 JSONFile/Maps 경로에 박아두고,
 * 서버 시작할때 여기서 불러와서 gameMaps에 박고 클론따서 사용
 * gameMaps KEY = enum 으로 관리 차후 메인맵 이외에 맵 생기면 서버 /Enum/MapType 가서 추가
 */
public class MapDataManager implements Cloneable {

    //맵 데이터
    public static HashMap<MapType, MapDTO[]> gameMaps;

    //localPath
    static String localPath = Paths.get("").toAbsolutePath().toString();


    public static void initMapData() {
        System.out.println("MapDataManager 초기화중...");

        //초기화
        gameMaps = new HashMap<>();

        //데이터 로드
        lordMapFile();


        System.out.println("MapDataManager 초기화 환료");
    }


    private static void lordMapFile() {

        //메인맵
        lordMainMap();

        //추가맵

    }


    private static void lordMainMap() {
        try {
            Gson gson = new Gson();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(mapPath("GOC_MAP_JSON")));
            MapDTO[] mapInfo = gson.fromJson(bufferedReader, MapDTO[].class);

            // System.out.println("" + mapInfo.length);

            gameMaps.put(MapType.MAIN, mapInfo);

            //맵에 값  잘들어오는지 테스트
//            for (int i = 0; i < mapInfo.length; i++) {
//                for (int j = 0; j < mapInfo[i].mapInfos.size(); j++) {
//                    System.out.println("MapDataManager/lordMainMap : " + mapInfo[i].mapInfos.get(j).what.getcomponent());
//                }
//            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static String mapPath(String name) {
        String mapname = null;
        mapname = localPath + "/src/JSONFiles/Maps/"+name+ ".json";
        return mapname;
    }


}
