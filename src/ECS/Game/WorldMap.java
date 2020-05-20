package ECS.Game;

import ECS.ActionQueue.Build.ActionUpgradeBarricade;
import ECS.ActionQueue.Build.ActionUpgradeBuilding;
import ECS.ActionQueue.Skill.ActionGetSkill;
import ECS.ActionQueue.Skill.ActionUpgradeSkill;
import ECS.ActionQueue.Skill.ActionUseAttack;
import ECS.ActionQueue.Skill.ActionUseSkill;
import ECS.ActionQueue.Upgrade.ActionStoreUpgrade;
import ECS.Classes.Type.Jungle.JungleMobType;
import Network.AutoCreatedClass.*;

import ECS.ActionQueue.Build.ActionInstallBuilding;
import ECS.ActionQueue.Item.ActionBuyItem;
import ECS.ActionQueue.Item.ActionSellItem;
import ECS.ActionQueue.Item.ActionUseItem;
import ECS.Classes.DTO.MapDTO;
import Network.AutoCreatedClass.ConditionData;
import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.RMI_Common.*;
import Network.RMI_Classes.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ECS.ActionQueue.ClientAction.*;
import ECS.ActionQueue.*;
import ECS.Classes.Type.*;
import ECS.Components.*;
import ECS.Classes.*;
import ECS.Factory.*;
import ECS.System.*;
import ECS.Entity.*;
import Enum.MapType;
import Enum.MapComponents;
import com.google.gson.*;
import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;

/**
 * 업뎃날짜 : 2020 05 05 화요일 권령희
 *
 * 2020 02 10 월요일 추가
 * -- http 요청 관련 처리 ;
 *      >> 인게임 진입 시 캐릭터 정보를 웹서버에 요청
 *      >> 인게임 종료 시 게임 결과를 웹서버에 저장
 *
 * -- initWorldMap( ) 수정
 *      # 상위 호출자로부터 전달받은 LoadPalyerData[] 정보를 가지고 JSON 정보를 구성해서 요청을 보냄.
 *      # 응답을 받으면, JSON 파싱을 통해 캐릭터 앤티티를 생성.
 *
 * 2020 02 18 화요일 추가
 * -- 캐릭터의 다음 이동 좌표(목적지?)를 담아두기 위한 HashMap 하나 추가.
 * -- 이동 및 위치 보정 처리를 위한 시스템 추가. ; PositionSystem
 *      >> 벽 뚫기랑 맵 이탈 방지를 위함.
 *
 * 2020 02 27 목요일 추가
 * -- 정글몹 슬롯 목록 검색 및 초기화
 * -- 정글몹 시스템 생성 및 돌리기
 *
 * 2020 05 05 화요일 추가
 * -- 캐릭터 정보 요청 시 정보에 guardianID 추가
 * -- 캐릭터 중계 정보에 캐릭터 킬, 데스 카운트 추가
 * -- 월드 중계 정보에 웨이브 총 몹 수, 살아있는 몹 카운트 추가
 * -- 방호벽 중계 정보에 업그레이드 레벨 추가
 *
 *
 */
public class WorldMap {

    WorldMap worldMap;

    /* 게임 난이도 관련 */
    public int gameGrade = 0;
    public int monsterExpLevel = 0;
    public float standardDeviation = 30f;

    //현재 월드맵의 고유 식별자ID
    int worldMapID;

    //맵 만드는 클래스
    public MapFactory mapFactory;

    //게임로직이 돌아가는 TickRate 설정.
    public static int tickRate = 100; //ms 단위.

    //몬스터 생성 쿨타임
    static long SPAWN_COOL_TIME = 500; //ms 단위.

    //월드맵 최대 접속 유저수
    public int userCount;

    //isGameMapStarted 가 true가 된 시점부터 카운트 시작.
    public long totalPlayTime = 0;

    //로딩중인 시간.
    long loadingTime = 0;

    //게임이 종료될 경우 True가 되며 게임맵이 종료된다.
    boolean isGameEnd = false;

    //유저가 다 모여서 게임 맵이 시작하였는지 여부.
    boolean isGameMapStarted = false;

    //웨이브가 진행중인지 여부.
    boolean isWaveStarted = false;

    //현재 웨이브 정보 Count 및 웨이브 대기시간 Count.
    long waveWaitTimeCount = 0;

    int waveInfoCount = 0;

    //동기화 제어용 모니터객체
    final Object locking = new Object();


    //월드맵에 접속중인 유저의 EntityID(int) & RMI_ID 목록
    public HashMap<Integer, RMI_ID> worldMapRMI_IDList;

    //월드맵에 접속중인 유저의 TokenID & EntityID(int) 목록.
    public HashMap<String, Integer> worldMapTokenIDList;

    /** 2020 02 10 추가 */
    // 접속중인 캐릭터들의 디비상 토큰(auto incr?) 목록.
    public HashMap<Integer, Integer> dbUserTokenListByEntity;

    // ㄴ 유저구글토큰 & 디비토큰 (혹시나 해서..)
    public HashMap<String, Integer> dbUserTokenListByGoogle;

    /* 2020 01 16 권령희 추가 */
    // 월드맵에 접속한 유저들의 각종 게임 스코어 목록
    public HashMap<Integer, PlayerGameScore> playerGameScoreList;

    // 게임 종료 후 서버로 저장할 정보들
    public long gameStartTime;
    public long gameFinishTime;
    public long gameElapsedTime;

    /* ***************************** */

    //01 29 이수헌 보이스 채팅 재접속기능 추가.
    //보이스채팅서버 호스트.
    RMI_ID voipHost = null;
    boolean isVoipHostReady = false;



    //Entity 목록
    public HashMap<Integer, CharacterEntity> characterEntity;
    public HashMap<Integer, MonsterEntity> monsterEntity;

    public HashMap<Integer, AttackTurretEntity> attackTurretEntity;
    public HashMap<Integer, BuffTurretEntity> buffTurretEntity;
    public HashMap<Integer, BarricadeEntity> barricadeEntity;
    public HashMap<Integer, CrystalEntity> crystalEntity;

    public HashMap<Integer, SkillObjectEntity> skillObjectEntity;
    public HashMap<Integer, FlyingObjectEntity> flyingObjectEntity;

    //월드의 Entity 데이터를 좀 더 손쉽게 사용하기 위한 Data클래스.
    public WorldMapDataListSet worldEntityData;

    //처리로직 System.
    BuffActionSystem buffActionSystem;

    CharacterSystem characterSystem;

    HpHistorySystem hpHistorySystem;
    MpHistorySystem mpHistorySystem;
    LevelSystem levelSystem;

    MonsterSystem monsterSystem;
    MonsterSystem2 monsterSystem2;

    AttackTurretSystem attackTurretSystem;
    BuffTurretSystem buffTurretSystem;

    SkillObjectSystem skillObjectSystem;
    FlyingObjectSystem flyingObjectSystem;

    /* 2019 12 15 이후 추가된 시스템 목록 */
    DeathSystem deathSystem;
    RewardSystem rewardSystem;
    LevelUpSystem levelUpSystem;
    ItemSlotSystem itemSlotSystem;
    public BuildSystem buildSystem;

    Store store;

    SelfRecoverySystem selfRecoverySystem;

    /** 2020 02 06 */
    DamageHistorySystem damageHistorySystem;

    /** 2020 03 20 */
    CharacterSpawnPointWellSystem wellSystem;



    //게임 로직 스레드
    Thread gameLogicThread;

    //클라이언트로부터 수신한 요청이 담겨있는 큐
    Queue<ClientAction> requestActionQueue;

    //Entity 생성 요청 큐
    public Queue<Entity> requestCreateQueue;

    //Entity 삭제 요청 큐
    public Queue<Entity> requestDeleteQueue;

    //고유한 EntityID를 할당하기 위한 AtomicInteger.
    public AtomicInteger worldMapEntityIDGenerater = new AtomicInteger(1);

    //EntityID(int), EntityType(short) 목록정보.  //EntityID가 어떤 종류의 Entity인지 저장하고 있는 목록.
    public HashMap<Integer, Short> entityMappingList;

    // 크리스탈 ID..
    public int crystalID;


    //DestroyEntityInfo 객체 안에는 파괴해야할 Entity정보가 존재한다.
    //Type에 맞게 타입과, EntityID를 입력후 목록에 추가한 다음, 모든 클라이언트에 송신한다.
    LinkedList<DestroyEntityData> destroyEntityDataList;

    //월드맵 별로 Random처리를 위한 RandomClass
    Random ran = new Random();

    //몬스터 스폰 시간.
    long remainedSpawnCoolTime;   // ms단위. 500 ~ 800 고려중
    //스폰할 몬스터 목록
    Queue<Integer> monsterSpawnList;

    /** 오후 7:18 2020-05-05 추가 */

    int currentWaveEntireMobCount;
    int currentWaveAliveMobCount;

    /*********************************/



    /* 사망 객체 리스트 */
    /**
     * hp 시스템에서 캐릭터, 몬스터의 체력이 0이 됨을 인지한 순간, death객체를 만들어 이 리스트에 넣어준다.
     * 이후 사망 시스템에서 이 큐의 항목을 하나씩 읽으면서, 각 앤티티별 사망 처리를 수행한다.
     *      ㄴ 캐릭터 사망의 경우 부활
     *      ㄴ 몬스터 사망의 경우 보상처리 및 몬스터 앤티티 삭제.
     */
    public Queue<Death> deathQueue;

    /* 맵 */
    public MapDTO[] gameMap;
    public Node[][] nodeMap;

    public ArrayList<MapComponentUnit> mapComponentUnitList;
    public ArrayList<MapComponentUnit> buildableAreaList;
    // 필요하다면 그 외 컴포넌트 타입별 유닛들도 리스트를 여기에 추가할 것
    // Path
    public ArrayList<MapInfo> topPathMovePointList;
    public ArrayList<MapInfo> middlePathMovePointList;
    public ArrayList<MapInfo> bottomPathMovePointList;
    public HashMap<Integer, ArrayList<MapInfo>> pathList;

    // 몬스터 스폰 지점 목록
    public ArrayList<MapInfo> monsterSpawnPointList;
    public HashMap<MapInfo, Integer> monsterSpawnMappingList;

    // 몬스터.. TO_MP 경로 해시맵 ; 몬스터 앤티티 ID, 경로 ; 몬스터가 길을 잃어서, 이동 포인트까지 잘 도착하기 위한 경로를 넣어둠.
    public HashMap<Integer, ArrayList<MapInfo>> mpPathList;

    public ArrayList<BuildSlot> buildSlotList;

    public ArrayList<StoreUpgradeSlot> upgradeSlotList;

    /** 2020 02 10 추가, HTTP 통신 관련 */
    AsyncHttpClient httpClient;

    boolean gameResultIsSaved = false;

    /** 2020 02 19 추가, 위치 시스템 관련 */

    public HashMap<Integer, Vector3> charNextPosList;
    PositionSystem positionSystem;

    /** 2020 05 14 추가, 건물에 끼는 경우 */
    public HashMap<Integer, Vector3> charNextPosList_gotStuck;  // 건물 등에 끼인 경우



    /** 2020 02 19 추가, 몬스터 충돌 관련 */
    MonsterSystem3 monsterSystem3;

    /** 2020 02 27 추가, 정글몬스터 관련 */
    public ArrayList<JungleMonsterSlot> jungleMonsterSlots;
    public HashMap<JungleMonsterSlot, MonsterEntity> jungleMonsterSlotMonsterEntityHashMap;
    public HashMap<Integer, JungleMonsterSlot> jungleMonsterSlotHashMap;
    public JungleMonsterSystem jungleMonsterSystem;


    //WorldMap 생성자
    public WorldMap(int worldMapID, RMI_ID[] matchingUserList, LoadingPlayerData[] matchingUserDataList, RMI_ID VoIP_HostRMI_ID) {

        System.out.println("WorldMap [" + worldMapID + "] 초기화중...");

        worldMap = this;

        //한 맵당 최대 유저수 Count;
        userCount = MatchingManager.userCount;

        //할당받은 MapID 세팅.
        this.worldMapID = worldMapID;


        //로직스레드 및 로직 초기화
        gameLogicThread = new Thread(new gameLogic());

        //엔티티 목록 초기화
        characterEntity = new HashMap<>();
        monsterEntity = new HashMap<>();
        attackTurretEntity = new HashMap<>();
        buffTurretEntity = new HashMap<>();
        barricadeEntity = new HashMap<>();
        crystalEntity = new HashMap<>();
        skillObjectEntity = new HashMap<>();
        flyingObjectEntity = new HashMap<>();

        //worldEntityData 초기화
        worldEntityData = new WorldMapDataListSet();

        //요청 큐 초기화
        requestActionQueue = new LinkedList<>();
        requestCreateQueue = new LinkedList<>();
        requestDeleteQueue = new LinkedList<>();

        //EntityID , EntityType을 서로 연결해주는 매핑 리스트 초기화
        entityMappingList = new HashMap<>();

        //시스템 초기화
        buffActionSystem = new BuffActionSystem(this);
        characterSystem = new CharacterSystem(this);
        hpHistorySystem = new HpHistorySystem(this);
        mpHistorySystem = new MpHistorySystem(this, 200f);
        //levelSystem = new LevelSystem(this);
        //monsterSystem = new MonsterSystem(this);
        monsterSystem2 = new MonsterSystem2(this);
        attackTurretSystem = new AttackTurretSystem(this);
        buffTurretSystem = new BuffTurretSystem(this, 200f);
        skillObjectSystem = new SkillObjectSystem(this);
        flyingObjectSystem = new FlyingObjectSystem(this);

        deathSystem = new DeathSystem(this, 200f);
        rewardSystem = new RewardSystem(this, 200f);
        levelUpSystem = new LevelUpSystem(this, 200f);
        itemSlotSystem = new ItemSlotSystem(this, 200f);
        buildSystem = new BuildSystem(this, 200f);

        selfRecoverySystem = new SelfRecoverySystem(this);
        damageHistorySystem = new DamageHistorySystem(this);

        /** 2020 03 20 */
        wellSystem = new CharacterSpawnPointWellSystem(this, 1000f);


        store = new Store(this);

        //TokenID , EntityID를 서로 연결해주는 매핑 리스트 초기화.
        //이 리스트는 추후 재접속, 또는 게임로직이 종료되어 월드맵을 소멸시킬 때,
        //TokenID에 해당하는 유저를 게임 참여중 상태에서, 새 게임 참여가능 상태로 바꿔주기 위해 쓰인다.
        worldMapTokenIDList = new HashMap<>();

        //EntityID , RMI_ID를 서로 연결해주는 매핑 리스트 초기화.
        worldMapRMI_IDList = new HashMap<>();

        //파괴될 목록이 정리된 목록 초기화.
        destroyEntityDataList = new LinkedList<>();


        /* 사망 리스트 초기화 */
        deathQueue = new LinkedList<>();

        //몬스터 스폰 관련 변수
        remainedSpawnCoolTime = SPAWN_COOL_TIME;
        monsterSpawnList = new LinkedList<>();


        /**
         * 2019 12 18 추가.
         */
        /* 맵 생성 */
        gameMap = MapFactory.createMap(MapType.MAIN);
        mapComponentUnitList = MapFactory.getMapComponentUnitsFromEntireMap(gameMap);

        //buildableAreaList = MapFactory.getBuildableArea(gameMap);   // 건설(build) 가능한 맵 영역을 찾는다.// 이 함수 나중에, 빌드 시스템으로 옮기기
        buildableAreaList = MapFactory.getBuildableArea(mapComponentUnitList);

        buildSlotList = buildSystem.initBuildSlotList(buildableAreaList);


        /**
         * 2019 12 26 추가.
         */

        if(true){
            /* 초기화 */ // 나중에, 함수로 뺄 것.
            /*
            pathList = new HashMap<>();

            topPathMovePointList = new ArrayList<>();
            pathList.put(PathType.TOP, topPathMovePointList);

            middlePathMovePointList = new ArrayList<>();
            pathList.put(PathType.MIDDLE, middlePathMovePointList);

            bottomPathMovePointList = new ArrayList<>();
            pathList.put(PathType.BOTTOM, bottomPathMovePointList);
            */

            /* 길(맵인포 목록) 찾아주고 */
            pathList = MapFactory.findMovePointsByPath(gameMap);



            /* 몬스터 스폰 지점 찾기 */
            monsterSpawnPointList = MapFactory.findMonsterSpawnPointList(gameMap);
            // 스폰 지점들과 길 연결... 일단 하드코딩! 가장 먼저 나오는게, 탑일 가능성이 높지...
            monsterSpawnMappingList = new HashMap<>();
            monsterSpawnMappingList.put(monsterSpawnPointList.get(0), PathType.TOP);
            monsterSpawnMappingList.put(monsterSpawnPointList.get(1), PathType.MIDDLE);
            monsterSpawnMappingList.put(monsterSpawnPointList.get(2), PathType.BOTTOM);

            System.out.println("몬스터 스폰포인트 : " + monsterSpawnPointList.size());
            for(int i=0; i<monsterSpawnPointList.size(); i++){

                System.out.println("몬스터 스폰포인트 : "
                        + monsterSpawnPointList.get(i).positionX
                        + ", " + monsterSpawnPointList.get(i).positionZ);

                System.out.println("몬스터 스폰포인트 타일 : "
                        + monsterSpawnPointList.get(i).arrayX
                        + ", " + monsterSpawnPointList.get(i).arrayY);
            }

        }

        /**
         * 2020 01 07 추가
         * 상점 업그레이드 슬롯 목록 초기화 작업
         */
        upgradeSlotList = new ArrayList<>();
        for(int i=0; i<3; i++){
            StoreUpgradeSlot slot = new StoreUpgradeSlot(i+1, i+1);
            upgradeSlotList.add(slot);
        }


        /********************************/

        // 2020 01 09
        /* 경로 구분 테스트 */
        if(false){

            MapInfo mapInfo;
            System.out.println("=========================================");

            ArrayList<MapInfo> path = pathList.get(PathType.TOP);
            for(int i=0; i<path.size(); i++){

                mapInfo = path.get(i);
                System.out.println("탑 경로 " + i + " : " + mapInfo.arrayX + ", " + mapInfo.arrayY);

            }
            System.out.println("=========================================");

            path = pathList.get(PathType.MIDDLE);
            for(int i=0; i<path.size(); i++){

                mapInfo = path.get(i);
                System.out.println("미들 경로 " + i + " : " + mapInfo.arrayX + ", " + mapInfo.arrayY);

            }
            System.out.println("=========================================");

            path = pathList.get(PathType.BOTTOM);
            for(int i=0; i<path.size(); i++){

                mapInfo = path.get(i);
                System.out.println("바텀 경로 " + i + " : " + mapInfo.arrayX + ", " + mapInfo.arrayY);

            }
            System.out.println("=========================================");


            // 2020 01 11

            ArrayList<MapInfo> mapInfos;
            for(int i=0; i<gameMap.length; i++){

                System.out.print(i + " ");
                mapInfos = gameMap[i].mapInfos;
                for(int j=0; j<mapInfos.size(); j++){

                    //System.out.print(j + " ");

                    if(mapInfos.get(j).canMove == true){
                        System.out.print(" ");
                    }
                    else{
                        System.out.print("x");
                    }

                }
                System.out.println("");
            }

            System.out.println("=========================================================");

        }


        // 2020 01 14
        nodeMap = MapFactory.createNodeMap(gameMap);
        mpPathList = new HashMap<>();


        // 길찾기 테스트
        if(false){

            MapInfo startPoint = MapFactory.findTileByPosition(gameMap, 25 * 2, -52 * 2);
            System.out.println("시작 지점 : " + startPoint.arrayX + ", " + startPoint.arrayY + ", " + startPoint.canMove);
            MapInfo endPoint = MapFactory.findTileByPosition(gameMap, 100, -100);
            System.out.println("도착 지점 : " + endPoint.arrayX + ", " + endPoint.arrayY+ ", " + endPoint.canMove);


            ArrayList<MapInfo> pathFound = MapFactory.pathFind(this, startPoint, endPoint);
            for(int i=0; i<pathFound.size(); i++){
                System.out.println("경로 " + i + " : [" + pathFound.get(i).arrayX + ", " + pathFound.get(i).arrayY + "] " );
            }

        }

        /* 2020 01 16 목 추가 ; 게임 스코어 정보 초기화 */
        playerGameScoreList = new HashMap<>();


        /** 2020 02 10 추가, http 관련 */
        initHttpClient();

        dbUserTokenListByEntity = new HashMap<>();
        dbUserTokenListByGoogle = new HashMap<>();

        /** 2020 02 19 추가, 위치 관련 */
        charNextPosList = new HashMap<>();
        positionSystem = new PositionSystem(this);
        charNextPosList_gotStuck = new HashMap<>();

        /** 2020 02 19 추가, 몬스터 충돌 관련 */
        monsterSystem3 = new MonsterSystem3(this);

        /** 2020 02 27 추가, 정글몹 시스템 관련 */
        jungleMonsterSystem = new JungleMonsterSystem(this);
        jungleMonsterSlots = new ArrayList<>();
        jungleMonsterSlotMonsterEntityHashMap = new HashMap<>();
        jungleMonsterSlotHashMap = new HashMap<>();

        /** 정글몹 목록 초기화 처리 해주기 << 나중에 함수로 따로 분리하던지..  */

        /* 맵상 정글몹 스폰 지점을 찾는다 */
        ArrayList<MapInfo> jungleSPList = MapFactory.findMapInfoListByType(gameMap, MapComponents.JUNGLE_SPAWN_POINT);

        System.out.println("정글 갯수 : " + jungleSPList.size());

        JungleMonsterSlot slot = null;
        for(int i=0; i< jungleSPList.size(); i++){

            slot = new JungleMonsterSlot(i+1, jungleSPList.get(i));
            // 슬롯에 몹정보 할당해주는 처리. 따로 빼도 되고.

            jungleMonsterSlots.add(slot);
        }



        //월드맵 시작전 초기화!
        initWorldMap(matchingUserList, matchingUserDataList);


        //보이스 채팅 Host설정
        this.voipHost = VoIP_HostRMI_ID;
        this.isVoipHostReady = true;


        /*
         * 슬롯에 지정된 몹을 실제로 생성하는 처리는, 정글몹 시스템이 슬롯 상태를 보고
         * EMPTY일 경우, 슬롯에 지정된 몬스터 정보에 따라 몬스터를 생성한다
         *
         * 오후 6:42 2020-04-08 매서드 수정.
         *  -- 전자는 기존의 하드코딩 세팅 매서드
         *  -- 후자는 각 정글몬스터의 발생 확률을 가지고,
         *      각 슬롯에 나타날 몬스터를 랜덤으로 지정해주는 식으로 초기화 작업을 수행함
         */
        //setJungleMonsterSetting();
        initializeJungleMobSpawnPoints();

        //게임 로직 스레드 실행!
        gameLogicThread.start();

        System.out.println("WorldMap [" + worldMapID + "] 초기화 완료");

    }


    //월드맵 시작전 초기화 해주는 부분.
    void initWorldMap(RMI_ID[] matchingUserList, LoadingPlayerData[] matchingUserDataList) {

        /** 캐릭터 정보 요청을 위한 요청데이터(JS) 구성 */

        HashMap<String, LoadingPlayerData> playerInfo = new HashMap<>();
        HashMap<String, String> nickName_googleTokenList = new HashMap<>();
        HashMap<String, RMI_ID> googleToken_rmiIDList = new HashMap<>();

        for(int i=0; i< matchingUserDataList.length; i++){

            LoadingPlayerData loadingPlayerData = matchingUserDataList[i];
            RMI_ID rmi_id = matchingUserList[i];
            String tokenID = loadingPlayerData.tokenID;

            if(true){  // 테스트 출력

                System.out.println("플레이어 " + i);
                System.out.println("rmi_id : " + rmi_id);
                System.out.println("tokenID : " + tokenID);
                System.out.println("====================");

            }
            playerInfo.put(tokenID, loadingPlayerData);

            String nickName = loadingPlayerData.characterName;

            nickName_googleTokenList.put(nickName, tokenID);

            googleToken_rmiIDList.put(tokenID, rmi_id);
        }

        String requestInfo = convertPlayerInfoToJSon(playerInfo);

        System.out.println("캐릭터 요청을 보냄");

        /** 캐릭터 정보 요청을 보낸다 */
        /*
         * 일단 최대 3회까지 시도하는걸로.
         */
        // RQ_getPlayerCharInfo(requestInfo);

        int requestCount = 0;
        final int MAX_RQ_COUNT = 3;

        Response response = null;
        boolean requestSucceed = false;
        while(true){

            if(requestCount == MAX_RQ_COUNT){
                System.out.println("요청 횟수가 3회에 도달했습니다.");
                break;
            }

            requestCount++;

            response = RQ_getPlayerCharInfo(requestInfo);   // 요청을 보내고, 응답이 올 때까지 기다림.
            if((response != null) && response.getStatusCode() == HttpConstants.ResponseStatusCodes.OK_200){

                /** 응답을 성공적으로 받았을 때의 처리를 작성 */
                System.out.println("캐릭터 정보를 성공적으로 받음");

                requestSucceed = true;

                break;
            }
            else{
                /** 실패?했을 때의 처리를 작성 */

                System.out.println("캐릭터 정보를 받아오는 데 실패..");
            }

        }


        /** 응답 내용을 가지고 캐릭터를 생성한다   */
        /*
         * 아래..에 있는 처리를 적절히 수행해주면 될듯. 응답내용 바꿔서.
         *
         * 월드맵 캐릭터 초기화! 그와동시에, 매칭이 완료되었음을 전달한다!
         * 월드맵을 생성할 때, 매칭이 완료되었던 RMI_ID목록을 가져와서 해당 리스트에 담는다.
         */
        if(requestSucceed){

            String playerInfoString = response.getResponseBody().toString();

            int index = playerInfoString.indexOf("{");
            System.out.println("엔터 인덱스..?? : " + index);

            String testStr = playerInfoString.substring(index);
            System.out.println("테스트 스트링 : " + testStr);
            // 위 처리를 왜 해주냐면.. 응답 내용에, 서버의 IP 주소(아마도) 가 같이 포함되어서 오는데, 왜 그런지는 모르겠지만
            // 이거때문에 JS 파싱이 제대로 되지 않아서.. 걔를 분리해주기 위함임.

            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(testStr);
            JsonObject playerInfoJSO = jsonElement.getAsJsonObject();

            if(false) {
                System.out.println("Element : " + jsonElement);
                System.out.println("obj : " + playerInfoJSO);
            }

            CharacterEntity newCharEntity = null;
            CharDataFromJS newCharData = null;
            for(int i=0; i<matchingUserList.length; i++){

                String userStr = (i+1) + "";

                System.out.println(userStr);

                JsonObject infoJSO = playerInfoJSO.getAsJsonObject(userStr);

                System.out.println(userStr + " : " + infoJSO + "\n");

                newCharData = parsePlayerInfoJSonToData(infoJSO);



                LoadingPlayerData loadingPlayerData;
                String tokenId = nickName_googleTokenList.get(newCharData.nickName);
                loadingPlayerData = playerInfo.get(tokenId);
                RMI_ID rmi_id = googleToken_rmiIDList.get(tokenId);

                newCharEntity = createCharacterEntityFromData(newCharData, loadingPlayerData);



                //불러온 tokenID를 List에 추가. tokenID를 기준으로 EntityID를 불러온다.
                worldMapTokenIDList.put(tokenId, newCharEntity.entityID);

                //EntityID, RMI_ID를 매핑하는 Map. 이 Map의 경우에는 도중에 플레이어가 접속이 끊길때마다, 비워지게된다.
                //같은 TokenID로 들어올때마다 바뀔 수 있도록 해야할 것.
                worldMapRMI_IDList.put(newCharEntity.entityID, rmi_id);

                // 2020 02 10 추가. db상의 토큰과 매칭
                dbUserTokenListByGoogle.put(tokenId, newCharData.userToken);
                dbUserTokenListByEntity.put(newCharEntity.entityID, newCharData.userToken);

                //이후, 각각의 rmi_id에 맞게 캐릭터 Entity를 생성하고 월드맵의 Entity목록에 추가한다.
                characterEntity.put(newCharEntity.entityID, newCharEntity);
                entityMappingList.put(newCharEntity.entityID, EntityType.CharacterEntity);

                //모든 클라이언트와 동시에 게임을 시작하기 위해서는, 모든 클라이언트들이 준비가 되었는지 확인이 필요한데,
                //월드맵Scene 로딩 준비가 모두 끝났는지 확인하는 프로세스를 위한 항목.
                loadingProgressList.put(tokenId, loadingPlayerData);

                /* 2020 01 16 목 */
                // 접속한 유저 캐릭터에 게임 스코어 객체 할당.
                playerGameScoreList.put(newCharEntity.entityID, new PlayerGameScore(newCharEntity));

            }


            /**
             * [작성] 오전 4:09 2020-04-08
             * 생성된 플레이어 목록을 가지고, 팀 속성 및 게임 난이도를 결정한다
             */

            /** 팀 속성 정하기 및 캐릭터들에 팀속성 할당 */
            decideTeamElemental();

            /** 게임 난이도 등급 정하기 */
            decideGameDifficultyGrade();


            /***************************************************************************************************/



            /** 크리스탈 **/
            CrystalEntity crystal = CrystalFactory.createCrystal();
            crystal.entityID = worldMapEntityIDGenerater.getAndIncrement();
            crystalEntity.put(crystal.entityID, crystal);
            crystalID = crystal.entityID;

            System.out.println("크리스탈 생성: " + crystal.entityID);
            //EntityID , EntityType
            entityMappingList.put(crystal.entityID, EntityType.CrystalEntity);


            //매칭이 완료되었음을 중계한다. 이 메소드가 호출되면, 클라이언트들은 로딩 장면인 LoadingGameScene을 로드하게 된다.
            server_to_client.completeMatching(matchingUserList, RMI_Context.Reliable_Public_AES256,
                    getWorldMapID(), "127.0.0.1_임시주소", 65005);

            System.out.println("매칭 처리 및 중계 완료");
        }
        else{

            System.out.println("매칭 처리를 완료할 수 없음");
        }
    }


    //플레이어가 월드맵과 재연결된 경우.
    public void userReconnectWorldMap(String tokenID, RMI_ID rmi_id) {

        synchronized (locking) {
            RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());

            //토큰ID로부터, 할당받은 캐릭터의 EntityID를 가져온다.
            int entityID = worldMapTokenIDList.get(tokenID);

            //EntityID와 RMI_ID를 다시 세팅한다.
            worldMapRMI_IDList.put(entityID, rmi_id);



            //게임이 시작한 이후에만 중계하도록 한다 (로딩중에 튕긴것은 중계할 필요가 없다)
            if (isGameMapStarted) {

                //500ms 후에 재접속 시퀀스를 송신한다.
                rmi_id.getTCP_Object().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        //할당된 자신의 캐릭터의 EntityID를 재접속한 클라이언트에 보내서 자기자신의 EntityID를 세팅하도록 한다.
                        server_to_client.initializeMyselfCharacterInfo(rmi_id, RMI_Context.Reliable, entityID);
                    }
                }, 500, TimeUnit.MILLISECONDS);

                //위의 500ms가 경과한 후부터 200ms후에 아래의 작업이 실행된다.

                //7000ms 후에 재접속 시퀀스를 송신한다.
                rmi_id.getTCP_Object().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        //그 다음, 월드맵의 모든 Entity들의 정보를 보내서 초기화 하도록 전송한다.
                        WorldMapDataListSet worldData = initData();

                        /** 2020 03 06 */
                        server_to_client.broadcastingStoreUpgradeBuffList(TARGET, RMI_Context.Reliable, worldData.storeUpgradeBuffSlotData);

                        //유저가 다시 접속하였으므로, 재연결 되었다는 메시지를 모든 클라이언트에게 중계한다
                        server_to_client.userReconnected(TARGET, RMI_Context.Reliable_Public_AES256, entityID);

                        server_to_client.initializeWorldMap(rmi_id, RMI_Context.Reliable,
                                worldData.characterData, worldData.monsterData, worldData.buffTurretData, worldData.attackTurretData,
                                worldData.barricadeData, worldData.crystalData, worldData.skillObjectData, worldData.flyingObjectData, worldData.buildSlotData);
                    }
                }, 600, TimeUnit.MILLISECONDS);

            }

            //음성채팅서버 호스트가 선정되어있고, 재접속한 유저가 Host유저가 아니라면 음성채팅 세션에 재접속 하게끔 하는 부분.
            if(voipHost != null && rmi_id.rmi_host_id != voipHost.rmi_host_id)
            {
                //연결이 끊겼던 유저가 재접속시, 음성채팅 클라이언트로 접속준비하게끔 알림.
                server_to_client.pickLogicIsVoipHost(rmi_id, RMI_Context.Reliable_AES256, false, getWorldMapID());

                //음성채팅 준비가 되어있는 상태라면 바로 재접속 하게끔 알림 전송
                if(isVoipHostReady)
                {
                    server_to_client.pickLogicConnectToVoipHost(rmi_id, RMI_Context.Reliable_AES256, true, getWorldMapID());
                }
                //아니라면 Host로부터 Ready가 되었다는 확인메시지가 올때 실행되는 부분에서 같이 알림 전송.
            }
        }
    }


    //플레이어와 월드맵간 연결이 해제됬을 경우.
    public void userLeftWorldMap(String tokenID, RMI_ID rmi_id) {

        synchronized (locking) {
            // 토큰값, EntityID 리스트는 그대로 둠.
            //토큰ID로부터, 할당받은 캐릭터의 EntityID를 가져온다.
            int entityID = worldMapTokenIDList.get(tokenID);

            //유저가 월드맵을 떠났으므로, 목록에서 제거한다.
            if (worldMapRMI_IDList.containsKey(entityID))
                worldMapRMI_IDList.remove(entityID);

            //모든 클라이언트와 동시에 게임을 시작하기 위해서는, 모든 클라이언트들이 준비가 되었는지 확인이 필요한데,
            //월드맵Scene 로딩 준비가 모두 끝났는지 확인하는 프로세스를 위한 항목.
            //유저의 접속이 끊겼으므로 이 항목에서 제거해야한다.
            if (loadingProgressList.containsKey(tokenID)) {
                LoadingPlayerData data = loadingProgressList.get(tokenID);

                //접속이 끊겼음을 전달하기 위함.
                data.currentProgressPercentage = -100f;
            }

            //유저의 접속이 끊겼으므로, 연결을 종료하였다는 메시지를 중계한다.
            //단, 게임이 시작한 이후에만 중계하도록 한다 (로딩중에 튕긴것은 중계할 필요가 없다)
            if (isGameMapStarted) {
                RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
                server_to_client.userDisconnected(TARGET, RMI_Context.Reliable_Public_AES256, entityID);
            }



            //방에 참가했던 유저들이 2명이상이고, 나간 유저가 VoIP Host유저라면 세션 폭파후 재생성한다.
            //그 후, Host유저를 선별한뒤 같은 월드맵ID로 음성채팅 세션 재생성
            if(loadingProgressList.size() > 1
                    && voipHost != null
                    && rmi_id.rmi_host_id == voipHost.rmi_host_id )
            {
                //음성채팅방이 폭파되었으므로 null로 세팅.
                voipHost = null;

                //음성채팅방이 폭파되었으므로 false로 세팅.
                isVoipHostReady = false;

                //나간 유저를 제외한 나머지 유저의 목록
                RMI_ID[] List = RMI_ID.getArray(worldMapRMI_IDList.values());

                for (int i = 0; i < List.length; i++) {

                    //남아있는 유저들 중 맨 앞에 있는 유저를 VoipHost로 한다.
                    if(i == 0)
                    {
                        voipHost = List[i];

                        //새로이 Host로 선정됨을 알림.
                        server_to_client.pickLogicIsVoipHost(voipHost, RMI_Context.Reliable_AES256, true, getWorldMapID());
                    }
                    else
                    {
                        //호스트가 아닌 클라이언트로 선정됨을 중계.
                        RMI_ID newVoipUser = List[i];
                        server_to_client.pickLogicIsVoipHost(newVoipUser, RMI_Context.Reliable_AES256, false, getWorldMapID());
                    }
                }
            }

        }
    }


    //재접속시, 월드맵에 활성화중인 음성채팅서버가 있는지 체크.
    //유효하다면 재접속. 아직 재생성중이라면 대기 후 voiceChatServerOnReady 시에 같이 재전송 받도록할 것


    //월드맵에서 새로이 Host로 선정된 유저의 음성채팅준비가 완료되었다는 신호가 오면
    //그 이외의 다른 유저들에게 해당 음성채팅서버로 접속하라는 것을 전송.
    public void voiceChatServerOnReady(boolean isVoipHostReady)
    {
        if(voipHost == null)
            return;

        this.isVoipHostReady = isVoipHostReady;

        //음성채팅 서버 생성 성공시
        if(this.isVoipHostReady)
        {
            RMI_ID[] List = RMI_ID.getArray(worldMapRMI_IDList.values());

            for (int i = 0; i < List.length; i++) {
                RMI_ID rmi_id = List[i];

                if(rmi_id.rmi_host_id != voipHost.rmi_host_id)
                    server_to_client.pickLogicConnectToVoipHost(rmi_id, RMI_Context.Reliable_AES256, this.isVoipHostReady, getWorldMapID());
            }
            //voipHost = null;
        }
        //음성채팅 서버 생성 실패시
        else
        {
            RMI_ID[] List = RMI_ID.getArray(worldMapRMI_IDList.values());

            //다른 유저를 서버로 지정한다.
            for (int i = 0; i < List.length; i++) {
                RMI_ID rmi_id = List[i];

                if(rmi_id.rmi_host_id != voipHost.rmi_host_id)
                {
                    voipHost = rmi_id;
                    break;
                }
            }


            for (int i = 0; i < List.length; i++) {
                RMI_ID rmi_id = List[i];

                //지정된 유저는 음성채팅 서버를 open
                if(rmi_id.rmi_host_id == voipHost.rmi_host_id)
                {
                    //새로이 Host로 선정됨을 알림.
                    server_to_client.pickLogicIsVoipHost(voipHost, RMI_Context.Reliable_AES256, true, getWorldMapID());
                }
                //나머지 유저는 음성채팅 서버가 열리기를 기다린다.
                else
                {
                    //호스트가 아닌 클라이언트로 선정됨을 중계.
                    RMI_ID newVoipUser = List[i];
                    server_to_client.pickLogicIsVoipHost(newVoipUser, RMI_Context.Reliable_AES256, false, getWorldMapID());
                }
            }

        }
    }



    public HashMap<String, LoadingPlayerData> loadingProgressList = new HashMap<>();

    //클라이언트의 월드맵 로딩 퍼센테이지 체크용. 모든 클라이언트의 준비가 끝나거나,
    //또는 로딩시작후 특정시간이 경과하면, 일단 로딩이 완료된 사용자만 먼저 진입하도록 한다.
    public void checkUserLoadingProgress() {
        //게임이 이미 시작된 경우라면 이 함수는 실행하지 않는다.
        if (isGameMapStarted)
            return;

        //로딩 경과시간 계산.
        loadingTime += tickRate;

        //이 변수가 true라면 게임을 시작할 준비가 된것이다!
        //단, 아래의 조건을 하나라도 만족하지 못한다면 false가 될것이다.
        boolean isReadyStartGame = true;

        for (HashMap.Entry<String, LoadingPlayerData> loadingProgress : loadingProgressList.entrySet()) {
            LoadingPlayerData loadingPlayerData = loadingProgress.getValue();

            //현재 로딩률이 100% 미만인 경우가 하나라도 있다면 아직 준비가 되지 않은 것이다.
            if (loadingPlayerData.currentProgressPercentage < 100f)
            {
                //접속이 끊겨있는 유저가 있다면 무시하도록 한다.
                if(loadingPlayerData.currentProgressPercentage == -100f)
                    continue;

                isReadyStartGame = false;
                //유니티의 AsyncLoadScene이 반환하는 비동기씬 로딩값은 0.9f일때 로딩이 완료되었음을 뜻한다.
                //완료되었다는 의미로 100f 로 치환함.
            }
            else
                loadingPlayerData.currentProgressPercentage = 100f;
        }



        /*//60초(6만ms) 가 경과하였는지 체크. 이 시간동안 로딩이 끝나지 않았다면, 일단 완료된 유저들부터 게임을 시작함.
        if(loadingTime > 60000)
            isReadyStartGame = true;*/


        //2명이상일 때, 그리고 300ms 마다 로딩상황을 중계한다.
        if (loadingProgressList.size() > 0 && loadingTime % 300 == 0) {
            RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
            server_to_client.broadcastingLoadingProgress(TARGET, RMI_Context.Reliable, new LinkedList<>(loadingProgressList.values()));
        }

        //게임을 시작할 준비가 되었다면.
        if (isReadyStartGame) {

            synchronized (locking) {
                RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());

                //모든 유저의 로딩이 완료된 것을 감지하면 호출되는 함수.
                //게임준비가 완료되었고, 게임이 시작되었다는 것을 모든 클라에게 전송한다.
                server_to_client.StartGame(TARGET, RMI_Context.Reliable_Public_AES256);

                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //게임시작 직후, 첫 웨이브 시작시 3초 대기.
                waveWaitTimeCount = 3000;

                //먼저 자신의 캐릭터 EntityID를 각각의 클라이언트에 중계한다.
                //이 값을 통해 클라이언트는 로컬객체와 원격객체를 구분하게끔 한다.
                for (HashMap.Entry<Integer, RMI_ID> entityID_RMI_ID : worldMapRMI_IDList.entrySet()) {
                    int ownCharacterEntityID = entityID_RMI_ID.getKey();
                    RMI_ID rmi_id = entityID_RMI_ID.getValue();

                    //할당된 자신의 캐릭터의 EntityID를 각각의 클라이언트들에게 1개씩 보내서 자기자신의 EntityID를 세팅하도록 한다.
                    server_to_client.initializeMyselfCharacterInfo(rmi_id, RMI_Context.Reliable_AES256, ownCharacterEntityID);
                }


                //그 다음, 월드맵의 모든 Entity들의 정보를 보내서 각 클라에서 초기화 하도록 모든 클라에게 중계한다.
                WorldMapDataListSet worldData = initData();
                server_to_client.initializeWorldMap(TARGET, RMI_Context.Reliable_Public_AES256,
                        worldData.characterData, worldData.monsterData, worldData.buffTurretData, worldData.attackTurretData,
                        worldData.barricadeData, worldData.crystalData, worldData.skillObjectData, worldData.flyingObjectData,
                        worldData.buildSlotData);

                server_to_client.broadcastingStoreUpgradeBuffList(TARGET, RMI_Context.Reliable, worldData.storeUpgradeBuffSlotData);

                //게임이 시작됨을 알리는 플래그변수.
                isGameMapStarted = true;
                waveInfoCount = 1;
                //모든 과정이 끝났다면 게임을 시작하고 매 TickRate마다 월드맵의 모든 Entity 들의 Snapshot을 각 클라이언트에 중계한다.

                gameStartTime = System.currentTimeMillis();

            }
        }
    }

    //클라이언트의 월드맵 로딩 퍼센테이지 체크용.
    public void updateUserLoadingProgress(RMI_ID rmi_id, float percentage) {
        String tokenID = SessionManager.findTokenIDfromRMI_HostID(rmi_id.rmi_host_id);

        if (loadingProgressList.containsKey(tokenID))
            loadingProgressList.get(tokenID).currentProgressPercentage = percentage;
    }


    //이 함수는 임시 함수이다. 추후 데이터베이스나 또는 캐릭터 Pick창에 의해 정해진 값을 토대로 캐릭터Data를 불러와서
    //초기화 해주도록 해야 할 것이다.
    CharacterData createArcherCharacterData(String characterName, int characterType) {
        CharacterData characterData = new CharacterData();
        characterData.entityID = worldMapEntityIDGenerater.getAndIncrement();
        characterData.characterName = characterName;
        characterData.characterType = characterType;

        characterData.moveSpeed = 6f;

        characterData.maxHP = 450f;
        characterData.currentHP = 150f;
        characterData.recoveryRateHP = 3f;

        characterData.maxMP = 350f;
        characterData.currentMP = 150f;
        characterData.recoveryRateMP = 8f;

        characterData.posX = 3;
        characterData.posY = 0f;
        characterData.posZ = -3;

        characterData.velX = 0f;
        characterData.velY = 0f;
        characterData.velZ = 0f;

        characterData.quarternionY = 0f;
        characterData.quarternionZ = 0f;


        characterData.attackSpeed = 0.66f;
        characterData.attackRange = 15f;
        characterData.attackDamage = 60f;

        characterData.defense = 20f;

        characterData.level = 1;
        characterData.gold = 5000;
        //characterData.gold = 0;
        characterData.exp = 0;

        characterData.isDisableMove = false;
        characterData.isDisableAttack = false;
        characterData.isDisableSkill = false;
        characterData.isDisableItem = false;
        characterData.isDamageImmunity = false;
        characterData.isUnTargetable = false;

        characterData.moveSpeedRate = 1f;
        characterData.attackSpeedRate = 1f;
        characterData.hpRecoveryRate = 1f;
        characterData.mpRecoveryRate = 1f;
        characterData.goldGainRate = 1f;
        characterData.expGainRate = 1f;
        characterData.buffDurationRate = 1f;
        characterData.attackDamageRate = 1f;
        characterData.defenseRate = 1f;
        characterData.maxHPRate = 1f;
        characterData.maxMPRate = 1f;
        characterData.coolTimeReduceRate = 1f;

        characterData.moveSpeedBonus = 0f;
        characterData.attackDamageBonus = 0f;
        characterData.defenseBonus = 0f;
        characterData.maxHPBonus = 0f;
        characterData.maxMPBonus = 0f;

        return characterData;
    }

    /* 마법사 캐릭터 생성용 임시 함수 2019-12-03 오후 11시 */
    CharacterData createMagicianCharacterData(String characterName, int characterType) {
        CharacterData characterData = new CharacterData();
        characterData.entityID = worldMapEntityIDGenerater.getAndIncrement();
        characterData.characterName = characterName;
        characterData.characterType = characterType;

        characterData.moveSpeed = 6f;

        characterData.maxHP = 430f;
        characterData.currentHP = 130f;
        characterData.recoveryRateHP = 5f;

        characterData.maxMP = 350f;
        characterData.currentMP = 150f;
        characterData.recoveryRateMP = 10f;

        characterData.posX = 3;
        characterData.posY = 0f;
        characterData.posZ = -3;

        characterData.velX = 0f;
        characterData.velY = 0f;
        characterData.velZ = 0f;

        characterData.quarternionY = 0f;
        characterData.quarternionZ = 0f;


        characterData.attackSpeed = 0.625f;
        characterData.attackRange = 14f;
        characterData.attackDamage = 50f;

        characterData.defense = 30f;

        characterData.level = 1;
        characterData.gold = 5000;
        characterData.exp = 0;

        characterData.isDisableMove = false;
        characterData.isDisableAttack = false;
        characterData.isDisableSkill = false;
        characterData.isDisableItem = false;
        characterData.isDamageImmunity = false;
        characterData.isUnTargetable = false;

        characterData.moveSpeedRate = 1f;
        characterData.attackSpeedRate = 1f;
        characterData.hpRecoveryRate = 1f;
        characterData.mpRecoveryRate = 1f;
        characterData.goldGainRate = 1f;
        characterData.expGainRate = 1f;
        characterData.buffDurationRate = 1f;
        characterData.attackDamageRate = 1f;
        characterData.defenseRate = 1f;
        characterData.maxHPRate = 1f;
        characterData.maxMPRate = 1f;
        characterData.coolTimeReduceRate = 1f;

        characterData.moveSpeedBonus = 0f;
        characterData.attackDamageBonus = 0f;
        characterData.defenseBonus = 0f;
        characterData.maxHPBonus = 0f;
        characterData.maxMPBonus = 0f;

        return characterData;
    }

    /* 전사 캐릭터 생성용 임시 함수 */
    CharacterData createKnightCharacterData(String characterName, int characterType) {
        CharacterData characterData = new CharacterData();
        characterData.entityID = worldMapEntityIDGenerater.getAndIncrement();
        characterData.characterName = characterName;
        characterData.characterType = characterType;

        characterData.moveSpeed = 6f * 2;   // (파일에는 300f)

        characterData.maxHP = 600f;
        characterData.currentHP = 600f;
        characterData.recoveryRateHP = 7f;

        characterData.maxMP = 100f;
        characterData.currentMP = 100f;
        characterData.recoveryRateMP = 5f;

        characterData.posX = 3;
        characterData.posY = 0f;
        characterData.posZ = -3;

        characterData.velX = 0f;
        characterData.velY = 0f;
        characterData.velZ = 0f;

        characterData.quarternionY = 0f;
        characterData.quarternionZ = 0f;


        characterData.attackSpeed = 0.625f;
        characterData.attackRange = 3.5f;
        characterData.attackDamage = 60f;

        characterData.defense = 35f;

        characterData.level = 1;
        characterData.gold = 5000;
        characterData.exp = 0;

        characterData.isDisableMove = false;
        characterData.isDisableAttack = false;
        characterData.isDisableSkill = false;
        characterData.isDisableItem = false;
        characterData.isDamageImmunity = false;
        characterData.isUnTargetable = false;

        characterData.moveSpeedRate = 1f;
        characterData.attackSpeedRate = 1f;
        characterData.hpRecoveryRate = 1f;
        characterData.mpRecoveryRate = 1f;
        characterData.goldGainRate = 1f;
        characterData.expGainRate = 1f;
        characterData.buffDurationRate = 1f;
        characterData.attackDamageRate = 1f;
        characterData.defenseRate = 1f;
        characterData.maxHPRate = 1f;
        characterData.maxMPRate = 1f;
        characterData.coolTimeReduceRate = 1f;

        characterData.moveSpeedBonus = 0f;
        characterData.attackDamageBonus = 0f;
        characterData.defenseBonus = 0f;
        characterData.maxHPBonus = 0f;
        characterData.maxMPBonus = 0f;

        return characterData;
    }

    //현재 사망중인 캐릭터 목록.
    public LinkedList<DefeatCharacterData> defeatCharacterList = new LinkedList<>();

    //캐릭터 부활 관리.
    void checkCharacterRespawn() {
        //아직 게임시작이 되지 않았다면 실행하지 않는다.
        if (isGameMapStarted == false)
            return;

        Iterator<DefeatCharacterData> iterator = defeatCharacterList.iterator();

        while (iterator.hasNext()) {
            DefeatCharacterData data = iterator.next();
            CharacterEntity entity = data.defeatCharacter;

            //남은 부활시간에서 tickRate시간만큼 깎는다.
            data.remainRespawnTimeMilliSeconds -= tickRate;

            //부활시간이 경과했다면.
            if ((data.remainRespawnTimeMilliSeconds <= 0f) && (entity.hpComponent.currentHP <= 0)) {
                //캐릭터가 다시 부활하였으므로 목록에서 제거한다.0
                iterator.remove();

                //부활하였으므로 HP/MP를 다 채우고, 걸렸던 상태이상을 원래대로 돌려놓는다.
                entity.hpComponent.currentHP = entity.hpComponent.maxHP;
                entity.mpComponent.currentMP = entity.mpComponent.maxMP;

                entity.conditionComponent.isDisableMove = false;
                entity.conditionComponent.isDisableAttack = false;
                entity.conditionComponent.isUnTargetable = false;
                entity.conditionComponent.isDisableSkill = false;
                entity.conditionComponent.isDisableItem = false;

                //부활하였으므로 부활지점으로 이동한다.
                entity.positionComponent.position =
                        new Vector3(3f, 0f, -3f);

                //캐릭터가 부활하였음을 중계한다.
                RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
                server_to_client.userCharacterRespawn(TARGET, RMI_Context.Reliable_Public_AES128, entity.entityID);
            }
        }
    }

    //캐릭터가 사망했을 경우, 월드맵에 중계한다
    public void userDefeated(CharacterEntity defeatCharacter, int remainRespawnTimeMilliSeconds) {
        RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
        server_to_client.userCharacterDefeat(TARGET, RMI_Context.Reliable_Public_AES128,
                defeatCharacter.entityID, remainRespawnTimeMilliSeconds);

        //entityID와, 남은시간(ms단위)을 같이 보낸다.
        //클라이언트에서는 remainRespawnTimeMilliSeconds값으로, 자체적으로 Timer를 돌린다.
    }

    //래핑 클래스.
    private class gameLogic implements Runnable {

        @Override
        public void run() {

            long startLogicTime;
            long elapsedLogicTime;

            /***********************************************************************************************************/

            try {
                System.out.println("게임 로직 스레드 실행.");

                //게임로직 시작!
                while (!isGameEnd) {

                    //System.out.println("==========================================================");

                    //로직이 시작되는 부분.
                    startLogicTime = System.nanoTime();

                    //각 클라이언트의 월드맵Scene 로딩상황을 체크하는 부분. 이부분에서 isGameMapStarted가 true가 된다.
                    //모든 유저가 모두 로딩이 끝났거나, 로딩하고나서 특정시간이 경과할때까지 모두 끝나지 않았다면
                    //게임을 시작한다. 그 후로 이 메소드는 더이상 호출되지 않는다.
                    checkUserLoadingProgress();

                    //캐릭터의 부활 타이머를 체크하는 부분이다. 캐릭터의 부활시간이 다 지나면 부활처리를 하게 된다.
                    checkCharacterRespawn();


                    if (isGameMapStarted){

                        //토탈 게임시간 카운팅. isGameMapStarted 가 true가 되면 플레이 타임 카운트를 시작한다.
                        totalPlayTime += tickRate;
                        gameElapsedTime = totalPlayTime;

                        if(totalPlayTime % 500 == 0)
                        {
                            //500ms단위로 게임 진행 시간 중계
                            RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
                            GameWorldStatus data = new GameWorldStatus();
                            data.currentGameworldTime = totalPlayTime;

                            /** 오후 7:11 2020-05-05 추가 **********************************************/
                            /**
                             * 매번 이렇게 세지 말고, DeathSystem에서 몹 죽음 하나 처리할 때 마다 해줄까 했는데,
                             * null 관련해서? 약간 오류가 있을 수 있을거 같아서
                             * 일단은 이렇게 하는게 제일 정확하다고 생각.
                             */

                            /** 몬스터 마릿수를 전체 웨이브 마릿수로 초기화한다 */
                            currentWaveAliveMobCount = currentWaveEntireMobCount;

                            /** 죽은 몹의 카운트를 센다 */
                            for (HashMap.Entry<Integer, MonsterEntity> monsterEntity : monsterEntity.entrySet()){
                                MonsterEntity monster = monsterEntity.getValue();

                                /** 정글몬스터는 살아있는 몬스터 카운트에서 제외한다 */
                                if(jungleMonsterSlotHashMap.containsKey(monster.entityID)){
                                    continue;
                                }

                                /** 죽은 몹을 발견할 때 마다 마릿수를 1씩 차감한다 */
                                if(monster.hpComponent.currentHP <= 0){
                                    currentWaveEntireMobCount--;
                                }
                            }

                            /** 월드 상태 정보에 몬스터 수를 반영한다 */

                            data.entireWaveMobCount = currentWaveEntireMobCount;
                            data.currentAliveMobCount = currentWaveAliveMobCount;



                            /***************************************************************************/



                            server_to_client.broadcastingGameWorldStatusSnapshot(TARGET, RMI_Context.UnReliable, data);
                        }


                        /** 웨이브 컨트롤 */
                        /* 첫 웨이브때 처리할 게 있다면. 없다면 이후에 이 절을 지울 것. */
                        if (waveInfoCount == 0) {

                            /* 첫 번째 웨이브 세팅 */
                            //waveInfoCount = 1;
                            // ㄴ 게임시작 매서드에서 처리하게끔 햇음. 나중에 정리할 때 이부분 지울 것.
                        }

                        /**
                         * 2020 04 14
                         * 무한모드 설정을 위해, 실행되지 않게 묶어둠.
                         * 나중에, 웨이브 시스템 별도로 파서 정리할 것.
                         */
                        if(false){
                            //마지막 웨이브에 도달하였을 경우, 게임이 클리어 됬음을 클라이언트에게 알림.
                            if (waveInfoCount > 7) {
                                /* 게임 종료 조건 설정 및 브로드캐스팅 */
                                boolean crystalIsDead = crystalEntity.isEmpty();

                                if (crystalIsDead == false) {

                                    isGameEnd = true;
                                    System.out.println("클리어 성공! 게임을 종료합니다.");

                                    // 2020 01 16
                                    gameFinishTime = System.currentTimeMillis();
                                    //gameElapsedTime = gameFinishTime - gameStartTime;

                                    //클리어에 성공하여 게임이 종료되었음을 모든 클라이언트에게 중계.  1번값을 보낸다.
                                    //클리어에 실패하였다면 -1 번값을 보낸다.
                                    // ㄴ>> 게임이 완전히 종료된 이후에 처리하도록 변경되었음. 일단 주석만 남겨둠.

                                    continue;
                                }
                            }
                        }


                        /* 게임시작 이후, 웨이브 로직을 실행한다 */
                        if (isGameMapStarted) {

                            //게임이 시작된 이후, 현재 맵에 접속중인 유저들 지속적으로 체크!
                            //연결이 끊긴 유저의 경우, worldMapRMI_IDList 에서 제거된다.
                            //만약 이 맵에 들어온 유저들 모두가 연결이 종료되었을 경우, 월드맵을 종료한다.
                            if (worldMapRMI_IDList.isEmpty()) {
                                isGameEnd = true;
                                System.out.println("모든 유저의 접속이 해제되었으므로, 게임을 종료합니다.");
                                continue;
                            }


                            if (!isWaveStarted) {   /* 웨이브 시작 전, 대기시간 */

                                System.out.println(waveInfoCount + " 번째 웨이브 시작을 대기중입니다. ");

                                /* 웨이팅 대기시간을 카운팅한다 */
                                waveWaitTimeCount -= tickRate;
                                if (waveWaitTimeCount < 0) {

                                    /** 기존 몹 시체들을 청소한다  */
                                    /**
                                     * 불완전한 방법이긴 한데.. 몹들이 모두 죽었다는 판정 후에도, 걔들이 만약에 지속적인 데미지를
                                     * 누군가에게 주고 있다거나, 반대로 걔네를 활용해야 하는 처리가 있다고 할 때
                                     * 그 처리들이 다 끝나기도 전에 몹 객체들이 삭제될 가능성이 있으니까
                                     * 현 웨이브가 끝나고, 일정시간 대기 후 다음 웨이브가 막 시작하려는 찰나에
                                     * 몹 목록 지워준다던지 그런 처리를 하는걸로.. 일단은.
                                     * 후에, 이 외에도 별도로 추가될만한?? 정리작업 등이 필요하면 처리할 것.
                                     */

                                    for (HashMap.Entry<Integer, MonsterEntity> monster : monsterEntity.entrySet()) {

                                        MonsterEntity mobb = monster.getValue();

                                        HPComponent mobHP = mobb.hpComponent;
                                        if(mobHP.currentHP <= 0){
                                            monsterEntity.remove(mobb);
                                        }

                                    }


                                    /** 2020 05 08 */
                                    // 투사체랑 장판 목록 클리어하는 처리 지움.


                                    /** 다음 웨이브에서 생성할 몹 목록을 갱신한다 */

                                    /* 이전 웨이브에서 사용한 몬스터 목록 클리어 */
                                    monsterSpawnList.clear();

                                    /* 웨이브 카운트에 따른 목록 갱신 처리를 한다 */

                                    HashMap<Integer, Integer> currentWaveArmy;
                                    if(waveInfoCount < 21){

                                        /* 20웨이브 이하인 경우에는, 파일로부터 미리 읽어들인 웨이브 별 등장 몬스터 목록을 따른다 */
                                        // currentWaveArmy = GameDataManager.waveArmy.get(waveInfoCount);
                                        currentWaveArmy = GameDataManager.waveArmyList.get(waveInfoCount);

                                    }
                                    else{

                                        /* 21웨이브 이상인 경우, 웨이브에 등장할 몬스터를 랜덤으로 정한다 */

                                        /** 이번 웨이브에 총 등장할 몬스터의 마릿수를 결정한다 (1인 플레이 기준) */
                                        int monsterCount = decideNextWaveMonsterCount(waveInfoCount);


                                        /** 마릿수만큼, 몬스터를 랜덤으로 뽑는다 */
                                        currentWaveArmy = decideWaveMonsterTypeByRandom(waveInfoCount, monsterCount);

                                    }

                                    System.out.println(waveInfoCount + " 번째 웨이브를 위한 몬스터 정보를 불러오는 중입니다. ");


                                    /* 위에서 결정된 몹 종류 및 마릿수를 가지고, 최종적으로 몹 리스트를 갱신한다 */
                                    for (HashMap.Entry<Integer, Integer> monsters : currentWaveArmy.entrySet()) {

                                        int mobCount = monsters.getValue();

                                        /**
                                         * 2020 04 25 수정,
                                         *      플레이어 수에 따라 몹 수가 비례해서 나오게끔..
                                         *      단, 이렇게 처리하면 중간에 유저 이탈 시 등장 마릿수에도 영향을 주게 되는데,
                                         *      이걸로 괜찮은지?? 는 회의를 통해 결정해야 할 듯.
                                         *
                                         *      또, 21웨이브 이하에서만 이게 적용되도록 할지
                                         *          아니면 전부 적용되도록 할지도..
                                         */
                                        /* 해당 종류의 마릿수만큼 반복한다 */
                                        int charCount = characterEntity.size();
                                        for (int i = 0; i < (mobCount*charCount); i++) {

                                            monsterSpawnList.add(monsters.getKey());    // 생성 큐에 한마리씩 집어넣는다.
                                        }

                                    }

                                    /** 오후 7:18 2020-05-05 추가 */

                                    currentWaveEntireMobCount = monsterSpawnList.size();
                                    currentWaveAliveMobCount = currentWaveEntireMobCount;


                                    /*******************************/




                                    /* 웨이브 상태를 "진행중"으로 변경한다 */
                                    isWaveStarted = true;
                                    System.out.println("곧" + waveInfoCount + " 번째 웨이브를 시작합니다.");

                                    RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
                                    server_to_client.StartWave(TARGET, RMI_Context.Reliable_Public_AES256, waveInfoCount);
                                }

                            }
                            else { /* 웨이브 시작 */

                                if (!monsterSpawnList.isEmpty()) {   /* 큐에 생성할 몬스터가 남아있다면 */
                                    //System.out.println("생성할 몬스터의 남은 마릿수 : " + monsterSpawnList.size());

                                    /* 몬스터를 spawn할 조건이 만족되었는지 판단한다 : 대략 0.5 ~ 0.8초 정도?? */
                                    boolean spawnable;
                                    spawnable = (remainedSpawnCoolTime <= 0) ? true : false;

                                    if (spawnable) {
                                        //System.out.println("새 몬스터를 생성합니다.");

                                        /* 큐에서 항목을 하나 꺼낸다 */
                                        int spawnMonsterID = monsterSpawnList.poll();

                                        /* 꺼낸 항목에 해당하는 몬스터 객체를 생성한다 ( 팩토리 활용 ) */
                                        MonsterEntity newMonster
                                                = MonsterFactory.createMonster(spawnMonsterID, worldMap);

                                        /* 새로 생성된 몬스터Entity에 고유 EntityID를 부여한다. */
                                        newMonster.entityID = worldMapEntityIDGenerater.getAndIncrement();

                                        /* 몬스터의 타겟을 크리스탈로 설정한다. */
                                        newMonster.monsterComponent.targetID = crystalID;

                                        if(monsterSpawnPointList.size() == 0){
                                            System.out.println("현 웨이브의 몬스터를 모두 생성함");
                                        }


                                        /**
                                         * 2019 12 26 추가 *************************
                                         * 2020 02 14 잠시 수정
                                         */
                                        if(true){

                                            int spawnPointIndex = monsterSpawnList.size()%3 + 1;
                                            //int spawnPointIndex = 2;

                                            MapInfo spawnPoint = null;
                                            switch (spawnPointIndex){
                                                case PathType.TOP :
                                                    spawnPoint = monsterSpawnPointList.get(0);
                                                    break;
                                                case PathType.BOTTOM :
                                                    spawnPoint = monsterSpawnPointList.get(1);
                                                    break;
                                                case PathType.MIDDLE :
                                                    spawnPoint = monsterSpawnPointList.get(2);
                                                    break;
                                            }

                                            newMonster.positionComponent.position.set(spawnPoint.getPixelPosition());
                                            newMonster.monsterComponent.movePathType = spawnPointIndex;
                                            newMonster.monsterComponent.movePointIndex = 0;

                                        }

                                        /* 객체 생성 요청 리스트에 추가한다 */
                                        requestCreateQueue.add(newMonster);

                                        /* spawn 조건을 초기화화한다 */
                                        remainedSpawnCoolTime = SPAWN_COOL_TIME;

                                    } else {

                                        /* spawn 조건을 업뎃한다 */
                                        remainedSpawnCoolTime -= tickRate;
                                    }

                                } else {    /* 이번 웨이브에서 생성되어야 할 몹이 모두 생성된 상태 */

                                    /* 월드 내 몹이 모두 죽었는지 판단한다 */

                                    /** 2002 02 13 변경................... */
                                    boolean allMonsterIsDead = true;
                                    for (HashMap.Entry<Integer, MonsterEntity> monsterEntity : monsterEntity.entrySet()){
                                        MonsterEntity monster = monsterEntity.getValue();

                                        /** 2020 02 28 추가. 정글몬스터의 생사 여부는 패스함 */
                                        if(jungleMonsterSlotHashMap.containsKey(monster.entityID)){
                                            continue;
                                        }

                                        if(monster.hpComponent.currentHP > 0){
                                            allMonsterIsDead = false;
                                        }
                                    }

                                    if (allMonsterIsDead) {

                                        RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
                                        server_to_client.EndWave(TARGET, RMI_Context.Reliable_Public_AES256, waveInfoCount);

                                        System.out.println(waveInfoCount + "번째 웨이브가 종료되었습니다. ");

                                        /* 웨이브 종료 조건 컨트롤 (테스트용) */
                                        if (false) {
                                            isGameEnd = true;
                                            System.out.println("게임을 종료합니다.");
                                        }

                                        /* 현 웨이브가 무사히 종료된 것 */
                                        waveInfoCount++;    // 웨이브 카운트 추가
                                        isWaveStarted = false;  // 웨이브 상태 변경
                                        waveWaitTimeCount = 5000;  // 웨이브 대기시간 초기화.

                                    }

                                }

                            }

                            /* 게임 종료 조건 설정 및 브로드캐스팅 */
                            boolean crystalIsDead = crystalEntity.isEmpty();

                            /* 크리스탈이 파괴되었다면 */
                            if (crystalIsDead) {
                                isGameEnd = true;
                                System.out.println("클리어 실패! 게임을 종료합니다.");

                                // 2020 01 16
                                gameFinishTime = System.currentTimeMillis();
                                //gameElapsedTime = gameFinishTime - gameStartTime;

                                //클리어에 실패하여 게임이 종료되었음을 모든 클라이언트에게 중계.  -1번값을 보낸다.
                                //클리어에 성공하였다면 1 번값을 보낸다.
                                //RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
                                //server_to_client.EndGame(TARGET, RMI_Context.Reliable_Public_AES256, -1, "ㅁㄴ움노ㅓ오ㅠ머ㅏㄴㅇ");
                                continue;
                            }


                        }
                        /* 웨이브 로직의 끝 */

                        System.out.println( "웨이브 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        /* ================================================================================================= */

                        //클라이언트로부터 보내진 ActionQueue를 꺼내서 모두 처리함.
                        dequeueClientAction();

                        System.out.println( "클라요청 처리 후 경과 시간: "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d) );

                        //이전 틱에서 추가된, [추가요청 Queue] 에 쌓여있는 오브젝트들을 리스트에 추가함.
                        createEntityFromQueue();

                        System.out.println( "앤티티 생성 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        //아이템 관련 처리
                        itemSlotSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "아이템 로직 처리 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        //건설 관련 처리
                        buildSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "건설 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        /** 2020 03 20 추가 */
                        wellSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "우물 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        // 2019 12 26 추가
                        selfRecoverySystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "자가회복 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        //버프 관련 처리
                        buffActionSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "버프 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        /** 2020 02 06 추가 */
                        damageHistorySystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "데미지 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));


                        //캐릭터 관련 처리
                        characterSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "캐릭터 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        /** 2020 02 19 추가 */
                        positionSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "위치 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));


                        //MP 상태 관련 처리
                        mpHistorySystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "mp 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        //HP 상태 관련 처리
                        hpHistorySystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "hp 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        // 레벨업 처리
                        levelUpSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "레벨업 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        //몬스터AI, 상태, 행동 관련 처리
                        monsterSystem2.onUpdate(tickRate * 0.001f);

                        System.out.println( "몬스터 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        /** 2020 04 08 주석 */
                        //monsterSystem3.onUpdate(tickRate * 0.001f);

                        /** 2020 02 28 추가 */
                        jungleMonsterSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "정글몹 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));


                        //공격 포탑 관련 처리
                        attackTurretSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "공격포탑 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        //버프 포탑 관련 처리
                        buffTurretSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "버프포탑 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        //투사체 관련 처리
                        flyingObjectSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "투사체 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        //스킬 오브젝트 관련 처리
                        skillObjectSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "장판 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        /** 2020 02 13 위치 옮겨봄..... 사망한 앤티티 널 문제 때문에 */
                        // 사망 처리
                        deathSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "사망 로직 후 경과 시간 : " +  String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));

                        // 보상 처리
                        rewardSystem.onUpdate(tickRate * 0.001f);

                        System.out.println( "보상 로직 후 경과 시간 : "  + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));



                        //삭제 요청 큐의 모든 Entity 삭제 및 중계.
                        deleteEntityFromQueue();

                        System.out.println( "앤티티 삭제 로직 후 경과 시간 : " + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));




                    }



                    //중계처리, 이 프레임 처리결과 전체를 모든 클라이언트에 중계.
                    //월드맵의 모든 Entity 객체를 전체 클라이언트에게 Broadcasting.
                    broadcastingWorldSnapshot();

                    System.out.println( "중계 로직 후 경과 시간 : " + String.format("%.6f", (System.nanoTime() - startLogicTime) * 0.000001d));


                    //로직처리시 걸린 시간.
                    elapsedLogicTime = System.nanoTime() - startLogicTime;  //nano sec 단위.

                    // [1sec 1초] = [1,000ms 천 밀리세컨드] = [1,000,000us 백만 마이크로세컨드] = [1,000,000,000ns 10억 나노세컨드];

                    //ns를 ms로 변환.
                    double msElapsedLogicTime = (double) elapsedLogicTime * 0.000001d; //100만으로 나눔.

                    System.out.println("한 로직 실행하는 데 총 걸린 시간 : " + String.format("%.3f", msElapsedLogicTime));

                    long sleepTime = tickRate - Math.round(msElapsedLogicTime); //반올림
                    if (sleepTime > 0) {
                        //System.out.println("elapsedLogicTime = " + String.format("%.3f", msElapsedLogicTime) + " ms  /  sleepTime = " + sleepTime + " ms");
                        Thread.currentThread().sleep(sleepTime);
                    } else {
                        System.out.println("tickRate OverTime = " + sleepTime * -1 + " ms");
                        //Thread.currentThread().sleep(0, 1);
                    }
                }
                System.out.println("게임 로직 스레드 종료.");

                /** 2020 03 09 월요일 권령희 작성 */
                boolean oldVersion = false;
                if(oldVersion){

                    /* 2020 01 17 금 */
                    /* 게임 결과 계산하는 처리 및 DB(웹서버)에 저장, 유저들에게 게임 결과를(등급 및 점수 등등) 중계 */

                    calculateGameResult();  // 게임 결과를 계산한다
                    String gameResult = convertGameResultToJSon();  // 서버에 보내줄 데이터를 json 형태로 변환

                    /* HTTP 요청을 만들어 전송 */

                    String ipAddr = "http://ngnl.xyz/result/endgame.php";
                    Future<Response> future = httpClient.preparePost(ipAddr)
                            .addFormParam("data", gameResult)
                            .execute(new AsyncCompletionHandler<Response>() {
                                @Override
                                public Response onCompleted(Response response) throws Exception {
                                    System.out.println("요청에 대한 응답 : " + response);
                                    //client.close();

                                    //gameResultIsSaved = true;
                                    return response;
                                }

                                @Override
                                public void onThrowable(Throwable t) {
                                    System.out.println("오류?");
                                    super.onThrowable(t);
                                }

                                @Override
                                public State onStatusReceived(HttpResponseStatus status) throws Exception {
                                    System.out.println("상태 코드 : " + status);
                                    return super.onStatusReceived(status);
                                }
                            });
                    try {
                        future.get();


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    /* **************************************************************************/



                }
                else{

                    broadcastingGameResult();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } /*catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("게임 로직중 NullPointerException \n" + e.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }*/  finally {
                if (isGameEnd == true && gameResultIsSaved == true) {
                    System.out.println("worldMapID [" + worldMapID + "] 의 게임 로직 스레드 정상 종료됨.");

                    //정상적으로 맵이 종료됨.
                    clearWorldMap();
                } else {
                    System.out.println("Error : worldMapID [" + worldMapID + "] 의 게임 로직 스레드 에러 발생!!");

                    //스레드 상태 체크. 스레드가 꺼졌다면, 다시 재실행.
                    //그 후 로그 출력 or 기록.
                    /** 디버깅시 오류 발생하는 정확한 지점 알아보려고 주석처리함 */
                    //restartGameLogicThread();
                }
            }
        }
    }


    //스레드 체크.
    void restartGameLogicThread() {
        this.gameLogicThread = null;
        System.out.println("Error : worldMapID [" + worldMapID + "] 의 게임 로직 스레드 다시 할당 후 재실행!!");
        this.gameLogicThread = new Thread( new gameLogic() );
        gameLogicThread.start();
    }

    //월드맵 종료시 호출되어, 월드맵의 모든 List등을 Clear처리해주는 부분.
    //MatchingManager에서 이 월드맵을 제거하도록 요청하는 부분도 존재함.
    void clearWorldMap() {
        this.characterEntity.clear();
        this.monsterEntity.clear();
        this.attackTurretEntity.clear();
        this.buffTurretEntity.clear();
        this.barricadeEntity.clear();
        this.crystalEntity.clear();
        this.skillObjectEntity.clear();
        this.flyingObjectEntity.clear();

        this.requestActionQueue.clear();
        this.requestCreateQueue.clear();
        this.requestDeleteQueue.clear();

        this.destroyEntityDataList.clear();
        this.entityMappingList.clear();

        //MatchingManager에서 현재 종료된 월드맵을 제거한다.
        MatchingManager.removeWorldMap(this);
    }


    //Tickrate동안 네트워크에서 받아온 이벤트들을 액션큐에 쌓는다.
    public void enqueueClientAction(ClientAction clientAction) {
        this.requestActionQueue.add(clientAction);
    }

    //Tickrate동안 액션큐에 쌓인 이벤트들을 꺼내와서 내부의 Action을 처리하는 부분.
    private void dequeueClientAction() {

        //클라이언트로부터 받아온 ActionQueue 개수.
        int requestCount = requestActionQueue.size();

        int resultCode;
        //받아온 개수만큼 요청Queue에 삽입.
        for (int i = 0; i < requestCount; i++) {

            ClientAction action = requestActionQueue.poll();

            /** 2019 12 27 금요일 새벽 권령희 */
            boolean newMode = true;

            switch (action.actionType) {

                /* 스킬 관련 액션 처리 */
                //스킬을 습득할 때.
                case ActionType.ActionGetSkill: {

                    ActionGetSkill getSkillEvent = (ActionGetSkill) action;

                    if(newMode){

                        boolean isPossible = SkillFactory.checkIsPossibleToGetSkill(this, getSkillEvent);
                        if(isPossible){

                            CharacterEntity entity = characterEntity.get(getSkillEvent.entityID);

                            /* 스킬을 생성한다 */
                            SkillInfo newSkill = SkillFactory.createSkillInfo(getSkillEvent.skillID);
                            SkillSlot slot = new SkillSlot(getSkillEvent.skillSlotNum, newSkill);
                            entity.skillSlotComponent.skillSlotList.add(slot);

                            entity.characterComponent.skillPoint--;


                        }
                        else{

                            //System.out.println("스킬 습득 불가능");
                        }
                    }
                    else{
                        //정보를 세팅하는 부분.
                        //이곳에서 스킬Factory를 활용하여 배울 스킬을 해당 슬롯에 세팅할 것.
                        ActionGetSkill event = (ActionGetSkill) action;

                        CharacterEntity entity = characterEntity.get(event.entityID);

                        /* 스킬을 생성한다 */
                        SkillInfo newSkill = SkillFactory.createSkillInfo(event.skillID);
                        SkillSlot slot = new SkillSlot(event.skillSlotNum, newSkill);

                        entity.skillSlotComponent.skillSlotList.add(slot);

                    }

                    break;
                }
                //스킬레벨을 올릴 때.
                case ActionType.ActionUpgradeSkill: {

                    ActionUpgradeSkill event = (ActionUpgradeSkill) action;

                    if(newMode){

                        boolean isPossible = SkillFactory.checkIsPossibleToUpgradeSkill(this, event);

                        if(isPossible){

                            SkillFactory.upgradeSkill(this, event);
                                                    }
                        else{
                            // 스킬 업그레이드 못함..
                        }
                    }
                    else {

                        //이미 캐릭터의 스킬 슬롯에 들어가 있는 스킬의 레벨을 업그레이드 한다.

                        CharacterEntity entity = characterEntity.get(event.entityID);
                        SkillSlot slot = entity.skillSlotComponent.skillSlotList.get(event.skillSlotNum);

                        if (slot.skillLevel < 5)
                            slot.skillLevel++;
                        else {
                            System.out.println("EntityID : " + entity.entityID + " [" + entity.characterComponent.characterName + "]");
                            System.out.println("ActionType.ActionUpgradeSkill: 스킬의 최대레벨을 넘을 수 없습니다");
                        }
                    }

                    break;
                }
                //일반공격이 행해졌을 때.
                case ActionType.ActionUseAttack: {

                    //Entity의 AttackComponent 정보의 일반공격의 공격력, 사거리등을 가져와서 공격행위를 한다.
                    //Entity의 종류에 따라서 근거리/원거리로 나뉘어지고, 근거리라면 타겟의 데미지 히스토리 추가.
                    //원거리라면 투사체 생성후 FlyingObject 생성 요청 큐에 삽입.
                    ActionUseAttack event = (ActionUseAttack) action;

                    SkillFactory.doAttack(this, event);
                    break;
                }
                //스킬이 사용되었을 때.
                case ActionType.ActionUseSkill: {
                    System.out.println("스킬을 사용합니다.");

                    //이미 캐릭터의 스킬 슬롯에 들어가 있는 스킬의 정보를 가지고와서
                    //SkillObject 생성이나, FlyingObject 생성을 하고, 이를 생성 요청 큐에 삽입.
                    ActionUseSkill event = (ActionUseSkill) action;

                    //입력된 정보로부터 스킬 생성.
                    SkillFactory.useSkill2(this, event);

                    break;

                }

                // 2020 01 29 수 추가
                /**
                 * 스킬 사용 중단 요청
                 *      ; 일단, 스킬이 계속 사용되는 데 있어 별도 쿨타임 개념이 적용되지 않는 경우로 작성
                 *      (쿨타임 개념 적용 할 경우, 별도 추가처리가 필요함)
                 *
                 */
                case ActionType.ActionStopUsingSkill : {

                    System.out.println("스킬 중단 요청을 처리합니다");

                    ActionStopUsingSkill event = (ActionStopUsingSkill) action;
                    SkillFactory.stopUsingSkill(this, event);

                    break;

                }


                /**
                 * 2019 12 17 화
                 *
                 * 12주차 아웃풋 목표상
                 *      아이템 판매 처리는 다음에. 우선은 구매랑 사용까지만 처리할 것임
                 * 현재 진행 정도 :
                 *      아이템 구매
                 *      이제 아이템 사용 작성하려고 함.
                 *
                 * 아이템 관련 처리 결과를 중계하기 위한 통신 설계 필요.
                 * 일단은 서버 처리 로직만 작성한 상태.
                 */
                /* 아이템 관련 액션 처리 */
                case ActionType.ACTION_BUY_ITEM:

                    ActionBuyItem buyItemEvent = (ActionBuyItem) action;

                    /* 아이템 구매 가능 여부를 판단한다 */
                    resultCode = store.isAbleToBuyItem(buyItemEvent);
                    boolean isAbleToBuyItem = (resultCode == NotificationType.SUCCESS) ? true : false;

                    /* 구매 가능한 경우 처리를 한다 */
                    if(isAbleToBuyItem){

                        /* (상점 내) 아이템 구매 매서드 호출한다 */
                        store.purchaseItem(buyItemEvent);

                        // 여기에 구매자에게 구매 결과를 중계하는 처리 작성
                        // 위의 결과가 실패든 성공이든,
                        //      그 값을 가지고 적절한 중계처리를 하게끔 통신 설계를 할 것
                    }
                    else {

                        // 해당 유저에게 구매 불가능하다는 중계처리를 해 줌.
                        RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
                        server_to_client.userFailedUseItem(TARGET, RMI_Context.Reliable_Public_AES256, resultCode);
                    }

                    break;

                case ActionType.ACTION_SELL_ITEM:

                    ActionSellItem sellItemEvent = (ActionSellItem) action;

                    /* 아이템 판매 가능 여부를 판단한다 */
                    resultCode = store.isAbleToSellItem(sellItemEvent);
                    boolean isAbleToSellItem = (resultCode == NotificationType.SUCCESS) ? true : false;

                    /* 판매 가능한 경우 처리를 한다 */
                    if(isAbleToSellItem){

                        // 상점 내 아이템 판매 매서드 호출.
                        store.sellItem(sellItemEvent);

                        // 판매자에게 판매결과 중계 매서드 호출
                    }

                    break;

                case ActionType.ACTION_USE_ITEM:

                    ActionUseItem useItemEvent = (ActionUseItem) action;

                    /* 아이템 사용 가능 여부를 판단한다 */
                    resultCode = itemSlotSystem.isAbleToUseItem(useItemEvent);
                    boolean isAbleToUseItem = (resultCode == NotificationType.SUCCESS) ? true : false;

                    /* 사용 가능한 경우 처리를 한다 */
                    if(isAbleToUseItem){

                        itemSlotSystem.useItem(useItemEvent);

                        // 해당하는 템 슬롯의 상태 변경, 갯수 변경 처리
                        // 위 처리를.. "아이템 사용"이라는 이름의 매서드로 묶자. >> 그래서 어디다 두지??
                        // 템 사용은 사실상.. 상점에서 하는게 아니니까. 슬롯 컴포쪽에 둘까??
                        // ECS 구조에서, 매서드를 컴포넌트쪽에 둬도 될까??
                    }
                    else{
                        /* 아이템을 사용하고자 했던 유저에게 아이템 사용이 불가능하다는 중계를 해줌 */

                        RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
                        server_to_client.userFailedUseItem(TARGET, RMI_Context.Reliable_Public_AES256, resultCode);
                    }

                    break;

                /* ======== 아이템 처리 케이스 끝 ================================= */

                /**
                * 2020 01 07 화
                */
                /* 상점 업그레이드 관련 케이스 */
                case ActionType.ACTION_STORE_UPGRADE :

                    ActionStoreUpgrade upgradeEvent = (ActionStoreUpgrade) action;

                    /* 업그레이드 가능 여부를 판단한다 */
                    boolean isAbleToUpgrade = store.isAbleToUpgrade(upgradeEvent, this);

                    /* 업그레이드 가능한 경우 처리를 한다 */
                    if(isAbleToUpgrade){

                        store.processUpgrade(upgradeEvent, this);
                        // 중계는 위 매서드 내부에서 수행함.
                    }
                    else{
                        // 업그레이드 불가능하다고 통지
                    }

                    break;


                /* ======== 상점 업그레이드 처리 케이스 끝 ================================= */


                /**
                 * 2020 01 07 화 저녁
                 * 업그레이드 작성
                 */
                /* 건설 관련 케이스 */
                case ActionType.ACTION_INSTALL_BUILDING:

                    ActionInstallBuilding installEvent = (ActionInstallBuilding) action;

                    /* 건설 가능 여부를 판단한다 */

                    resultCode = buildSystem.isAbleToInstall(installEvent);
                    boolean isAbleToInstall = (resultCode == NotificationType.SUCCESS) ? true : false;

                    /* 건설 가능한 경우 처리를 한다 */
                    if(isAbleToInstall){

                        System.out.println("건물을 건설합니다 ");
                        buildSystem.installBuilding(installEvent);
                    }
                    else{

                        /* 건설 불가능하다고 통지 */
                        RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
                        server_to_client.userFailedInstallBuilding(TARGET, RMI_Context.Reliable_Public_AES256, resultCode);
                    }

                    break;

                case ActionType.ACTION_UPGRADE_BUILDING :

                    ActionUpgradeBuilding event = (ActionUpgradeBuilding) action;

                    /* 업그레이드 가능 여부를 판단한다 */
                    boolean isAbleToUpgradeBuilding = buildSystem.isAbleToUpgradeBuilding(event);

                    /* 업그레이드 가능한 경우 처리를 한다 */
                    if(isAbleToUpgradeBuilding){

                        // 업글처리
                        buildSystem.upgradeBuilding(event);
                    }
                    else{

                        // 업글 불가능하다고 통지

                    }
                    break;

                case ActionType.ACTION_UPGRADE_BARRICADE :

                    ActionUpgradeBarricade upgradeBarricadeEvent = (ActionUpgradeBarricade) action;

                    /* 업그레이드 가능 여부를 판단한다 */
                    boolean isAbleToUpgradeBarri = buildSystem.isAbleToUpgradeBarricade(upgradeBarricadeEvent);

                    /* 업그레이드 가능한 경우 처리를 한다 */
                    if(isAbleToUpgradeBarri){

                        // 업글처리
                        buildSystem.upgradeBarricade(upgradeBarricadeEvent);
                    }
                    else{

                        // 업글 불가능하다고 통지
                    }

                    break;


                /* ======== 건설 처리 케이스 끝 ================================= */


            }
        }
    }


    //생성요청큐에 쌓여있던 Entity들을 다 꺼내와서, 월드맵의 Entity 목록에 추가한 뒤, 이를 모든 클라이언트에 중계함.
    void createEntityFromQueue() {

        int size = this.requestCreateQueue.size();
        if (size > 0) {

            System.out.println("생성하려는 Entity의 갯수 : " + size);

            for (int i = 0; i < size; i++) {
                Entity target = requestCreateQueue.poll();

                //Queue에서 꺼내온 뒤, Entity목록에 추가! 그리고 이를 전송하기 위한 객체에 추가!
                if (target instanceof CharacterEntity) {
                    CharacterEntity data = (CharacterEntity) target;
                    characterEntity.put(target.entityID, data);
                    entityMappingList.put(target.entityID, EntityType.CharacterEntity);
                    worldEntityData.characterData.add(getCharacterDataFromEntity(data));
                } else if (target instanceof MonsterEntity) {
                    MonsterEntity data = (MonsterEntity) target;
                    monsterEntity.put(target.entityID, data);
                    entityMappingList.put(target.entityID, EntityType.MonsterEntity);
                    worldEntityData.monsterData.add(getMonsterDataFromEntity(data));
                } else if (target instanceof AttackTurretEntity) {
                    AttackTurretEntity data = (AttackTurretEntity) target;
                    attackTurretEntity.put(target.entityID, data);
                    entityMappingList.put(target.entityID, EntityType.AttackTurretEntity);
                    worldEntityData.attackTurretData.add(getAttackTurretDataFromEntity(data));
                } else if (target instanceof BuffTurretEntity) {
                    BuffTurretEntity data = (BuffTurretEntity) target;
                    buffTurretEntity.put(target.entityID, data);
                    entityMappingList.put(target.entityID, EntityType.BuffTurretEntity);
                    worldEntityData.buffTurretData.add(getBuffTurretDataFromEntity(data));
                } else if (target instanceof BarricadeEntity) {
                    BarricadeEntity data = (BarricadeEntity) target;
                    barricadeEntity.put(target.entityID, data);
                    entityMappingList.put(target.entityID, EntityType.BarricadeEntity);
                    worldEntityData.barricadeData.add(getBarricadeDataFromEntity(data));
                } else if (target instanceof CrystalEntity) {
                    CrystalEntity data = (CrystalEntity) target;
                    crystalEntity.put(target.entityID, data);
                    entityMappingList.put(target.entityID, EntityType.CrystalEntity);
                    worldEntityData.crystalData.add(getCrystalDataFromEntity(data));
                } else if (target instanceof SkillObjectEntity) {
                    SkillObjectEntity data = (SkillObjectEntity) target;
                    skillObjectEntity.put(target.entityID, data);
                    entityMappingList.put(target.entityID, EntityType.SkillObjectEntity);
                    worldEntityData.skillObjectData.add(getSkillObjectDataFromEntity(data));
                } else if (target instanceof FlyingObjectEntity) {
                    FlyingObjectEntity data = (FlyingObjectEntity) target;
                    flyingObjectEntity.put(target.entityID, data);
                    entityMappingList.put(target.entityID, EntityType.FlyingObjectEntity);
                    worldEntityData.flyingObjectData.add(getFlyingObjectDataFromEntity(data));

                }
            }

            //다 담아졌으면, 이 월드맵에 접속중인 유저들의 RMI_ID목록을 가져옴.
            RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());

            //이후, 미리 담아둔 객체에 데이터가 있다면 꺼내와서 모든 클라이언트에게 송신한다.
            if (worldEntityData.characterData.size() > 0) {
                server_to_client.createWorldMapCharacterEntityInfo(TARGET, RMI_Context.Reliable_Public_AES256, worldEntityData.characterData);
            }
            if (worldEntityData.monsterData.size() > 0) {
                server_to_client.createWorldMapMonsterEntityInfo(TARGET, RMI_Context.Reliable_Public_AES256, worldEntityData.monsterData);
            }
            if (worldEntityData.attackTurretData.size() > 0) {
                server_to_client.createWorldMapAttackTurretEntityInfo(TARGET, RMI_Context.Reliable_Public_AES256, worldEntityData.attackTurretData);
            }
            if (worldEntityData.buffTurretData.size() > 0) {
                server_to_client.createWorldMapBuffTurretEntityInfo(TARGET, RMI_Context.Reliable_Public_AES256, worldEntityData.buffTurretData);
            }
            if (worldEntityData.barricadeData.size() > 0) {
                server_to_client.createWorldMapBarricadeEntityInfo(TARGET, RMI_Context.Reliable_Public_AES256, worldEntityData.barricadeData);
            }
            if (worldEntityData.crystalData.size() > 0) {
                server_to_client.createWorldMapCrystalDataEntityInfo(TARGET, RMI_Context.Reliable_Public_AES256, worldEntityData.crystalData);
            }
            if (worldEntityData.skillObjectData.size() > 0) {
                server_to_client.createWorldMapSkillObjectEntityInfo(TARGET, RMI_Context.Reliable_Public_AES256, worldEntityData.skillObjectData);
            }
            if (worldEntityData.flyingObjectData.size() > 0) {
                server_to_client.createWorldMapFlyingObjectEntityInfo(TARGET, RMI_Context.Reliable_Public_AES256, worldEntityData.flyingObjectData);

            }

            //다 송신하였으면 재사용을 위해 clear처리!
            worldEntityData.clear();

            System.out.println("CreateRequestQueue : " + size);
        }
    }

    void deleteEntityFromQueue() {

        int size = this.requestDeleteQueue.size();
        if (size > 0) {

            for (int i = 0; i < size; i++) {
                Entity target = requestDeleteQueue.poll();

                if (target instanceof CharacterEntity) {
                    DestroyEntityData data = new DestroyEntityData();
                    data.entityType = EntityType.CharacterEntity;
                    data.destroyedEntityID = target.entityID;
                    destroyEntityDataList.add(data);
                    //characterEntity.remove(target.entityID);
                    //entityMappingList.remove(target.entityID);
                } else if (target instanceof MonsterEntity) {
                    DestroyEntityData data = new DestroyEntityData();
                    data.entityType = EntityType.MonsterEntity;
                    data.destroyedEntityID = target.entityID;
                    destroyEntityDataList.add(data);
                    //monsterEntity.remove(target.entityID);
                    //entityMappingList.remove(target.entityID);
                } else if (target instanceof AttackTurretEntity) {
                    DestroyEntityData data = new DestroyEntityData();
                    data.entityType = EntityType.AttackTurretEntity;
                    data.destroyedEntityID = target.entityID;
                    destroyEntityDataList.add(data);
                    //attackTurretEntity.remove(target.entityID);
                    //entityMappingList.remove(target.entityID);
                } else if (target instanceof BuffTurretEntity) {
                    DestroyEntityData data = new DestroyEntityData();
                    data.entityType = EntityType.BuffTurretEntity;
                    data.destroyedEntityID = target.entityID;
                    destroyEntityDataList.add(data);
                    //buffTurretEntity.remove(target.entityID);
                    //entityMappingList.remove(target.entityID);
                } else if (target instanceof BarricadeEntity) {
                    DestroyEntityData data = new DestroyEntityData();
                    data.entityType = EntityType.BarricadeEntity;
                    data.destroyedEntityID = target.entityID;
                    destroyEntityDataList.add(data);
                    //barricadeEntity.remove(target.entityID);
                    //entityMappingList.remove(target.entityID);
                } else if (target instanceof CrystalEntity) {
                    DestroyEntityData data = new DestroyEntityData();
                    data.entityType = EntityType.CrystalEntity;
                    data.destroyedEntityID = target.entityID;
                    destroyEntityDataList.add(data);
                    crystalEntity.remove(target.entityID);
                    entityMappingList.remove(target.entityID);
                } else if (target instanceof SkillObjectEntity) {
                    DestroyEntityData data = new DestroyEntityData();
                    data.entityType = EntityType.SkillObjectEntity;
                    data.destroyedEntityID = target.entityID;
                    destroyEntityDataList.add(data);
                    skillObjectEntity.remove(target.entityID);
                    entityMappingList.remove(target.entityID);
                } else if (target instanceof FlyingObjectEntity) {
                    DestroyEntityData data = new DestroyEntityData();
                    data.entityType = EntityType.FlyingObjectEntity;
                    data.destroyedEntityID = target.entityID;
                    destroyEntityDataList.add(data);
                    flyingObjectEntity.remove(target.entityID);
                    entityMappingList.remove(target.entityID);
                }
            }

            //다 담아졌으면, 이 월드맵에 접속중인 유저들의 RMI_ID목록을 가져옴.
            RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());

            System.out.println("DestroyRequestQueue : " + size);

            //파괴해야할 목록을 모든 클라이언트에 중계한다.
            server_to_client.destroyWorldMapEntityInfo(TARGET, RMI_Context.Reliable_Public_AES256, destroyEntityDataList);

            /** 2020 05 08 */
            // 원래 여기에 destroyEntityDataList.clear()를 해줬는데. 이게, 클라에서 일부 객체가 사라지지 않는 원인이 될 수도 잇어서.
            // 지우기로 함.

        }
    }


    //이 월드맵에 존재하는 모든 Entity목록들을 손쉽게 가져오도록 하는 부분.
    public WorldMapDataListSet initData() {
        WorldMapDataListSet worldEntityData = new WorldMapDataListSet();

        int size = characterEntity.size();
        if (size > 0) {
            LinkedList<CharacterEntity> data = new LinkedList<>(characterEntity.values());

            for (int i = 0; i < size; i++) {
                CharacterEntity entity = data.poll();
                worldEntityData.characterData.add(getCharacterDataFromEntity(entity));
            }
        }

        size = monsterEntity.size();
        if (size > 0) {
            LinkedList<MonsterEntity> data = new LinkedList<>(monsterEntity.values());

            for (int i = 0; i < size; i++) {
                MonsterEntity entity = data.poll();
                worldEntityData.monsterData.add(getMonsterDataFromEntity(entity));
            }
        }

        size = attackTurretEntity.size();
        if (size > 0) {
            LinkedList<AttackTurretEntity> data = new LinkedList<>(attackTurretEntity.values());

            for (int i = 0; i < size; i++) {
                AttackTurretEntity entity = data.poll();
                worldEntityData.attackTurretData.add(getAttackTurretDataFromEntity(entity));
            }
        }

        size = buffTurretEntity.size();
        if (size > 0) {
            LinkedList<BuffTurretEntity> data = new LinkedList<>(buffTurretEntity.values());

            for (int i = 0; i < size; i++) {
                BuffTurretEntity entity = data.poll();
                worldEntityData.buffTurretData.add(getBuffTurretDataFromEntity(entity));
            }
        }

        size = barricadeEntity.size();
        if (size > 0) {
            LinkedList<BarricadeEntity> data = new LinkedList<>(barricadeEntity.values());

            for (int i = 0; i < size; i++) {
                BarricadeEntity entity = data.poll();
                worldEntityData.barricadeData.add(getBarricadeDataFromEntity(entity));
            }
        }

        size = crystalEntity.size();
        if (size > 0) {
            LinkedList<CrystalEntity> data = new LinkedList<>(crystalEntity.values());

            for (int i = 0; i < size; i++) {
                CrystalEntity entity = data.poll();
                worldEntityData.crystalData.add(getCrystalDataFromEntity(entity));
            }
        }

        size = skillObjectEntity.size();
        if (size > 0) {
            LinkedList<SkillObjectEntity> data = new LinkedList<>(skillObjectEntity.values());

            for (int i = 0; i < size; i++) {
                SkillObjectEntity entity = data.poll();
                worldEntityData.skillObjectData.add(getSkillObjectDataFromEntity(entity));
            }
        }

        size = flyingObjectEntity.size();
        if (size > 0) {
            LinkedList<FlyingObjectEntity> data = new LinkedList<>(flyingObjectEntity.values());

            for (int i = 0; i < size; i++) {
                FlyingObjectEntity entity = data.poll();
                worldEntityData.flyingObjectData.add(getFlyingObjectDataFromEntity(entity));
            }
        }

        /**
         * 2020 02 17 권령희
         * BuildSlot 목록도 위 앤티티 리스트들과 같이, 클라이언트에 추가해주기 위한 변환 처리 작성하기.
         * 각 앤티티로부터 Data를 얻는것과 같은 변환 매서드도 필요함.
         *  BuildSlotData getBuildSlotData(BuildSlot slot)
         *
         */

        size = buildSlotList.size();
        if( size > 0){

            for(int i=0; i< size; i++){
                BuildSlot buildSlot = buildSlotList.get(i);
                worldEntityData.buildSlotData.add(getBuildSlotData(buildSlot));
            }
        }

        size = upgradeSlotList.size();
        if( size > 0){

            for(int i=0; i< size; i++){
                StoreUpgradeSlot slot = upgradeSlotList.get(i);
                worldEntityData.storeUpgradeBuffSlotData.add(getStoreUpgradeSlotData(slot));
            }
        }


        return worldEntityData;
    }


    /**
     * 2020 02 17 권령희
     *
     * 빌드 슬롯 목록 및 상태를 중계하기 위한 처리 추가 작성 필요
     * broadcastingBuildSlotDataSnapshot(TARGET);
     */
    //월드 스냅샷 중계
    void broadcastingWorldSnapshot() {

        //현재 월드맵에 접속중인 유저들의 RMI_ID목록을 가져옴.
        RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());

        //CharacterEntity 중계
        broadcastingCharacterEntitySnapshot(TARGET);
        //MonsterEntity 중계
        broadcastingMonsterEntitySnapshot(TARGET);

        //AttackTurretEntity 중계
        broadcastingAttackTurretEntitySnapshot(TARGET);
        //BuffTurretEntity 중계
        broadcastingBuffTurretEntitySnapshot(TARGET);
        //BarricadeEntity 중계
        broadcastingBarricadeEntitySnapshot(TARGET);
        //CrystalEntity 중계
        broadcastingCrystalEntitySnapshot(TARGET);

        //SkillObjectEntity 중계
        broadcastingSkillObjectEntitySnapshot(TARGET);
        //FlyingObjectEntity 중계
        broadcastingFlyingObjectEntitySnapshot(TARGET);

        /**
         * 2020 02 17 여기
         */
        broadcastingBuildSlotDataSnapshot(TARGET);

        //재활용을 위해 Clear() 호출.
        worldEntityData.clear();
    }

    void broadcastingCharacterEntitySnapshot(RMI_ID[] TARGET) {

        int size = characterEntity.size();
        if (size > 0) {
            LinkedList<CharacterEntity> data = new LinkedList<>(characterEntity.values());

            for (int i = 0; i < size; i++) {
                CharacterEntity entity = data.poll();

                CharacterData characterData = getCharacterDataFromEntity(entity);

                worldEntityData.characterData.add(getCharacterDataFromEntity(entity));

                //System.out.println("현 캐릭터의 스킬 정보 : " + entity.skillSlotComponent.skillSlot.size());
                //System.out.println("캐릭터 좌표 : " + characterData.posX + ", "+ characterData.posY + ", "+ characterData.posZ);

            }
            server_to_client.broadcastingCharacterSnapshot(TARGET, RMI_Context.UnReliable, worldEntityData.characterData);
        }
    }

    void broadcastingMonsterEntitySnapshot(RMI_ID[] TARGET) {

        int size = monsterEntity.size();
        if (size > 0) {
            LinkedList<MonsterEntity> data = new LinkedList<>(monsterEntity.values());

            for (int i = 0; i < size; i++) {
                MonsterEntity entity = data.poll();
                worldEntityData.monsterData.add(getMonsterDataFromEntity(entity));
            }
            server_to_client.broadcastingMonsterSnapshot(TARGET, RMI_Context.UnReliable, worldEntityData.monsterData);
        }
    }

    void broadcastingAttackTurretEntitySnapshot(RMI_ID[] TARGET) {

        int size = attackTurretEntity.size();
        if (size > 0) {
            LinkedList<AttackTurretEntity> data = new LinkedList<>(attackTurretEntity.values());

            for (int i = 0; i < size; i++) {
                AttackTurretEntity entity = data.poll();
                worldEntityData.attackTurretData.add(getAttackTurretDataFromEntity(entity));
            }
            server_to_client.broadcastingAttackTurretSnapshot(TARGET, RMI_Context.UnReliable, worldEntityData.attackTurretData);
        }
    }

    void broadcastingBuffTurretEntitySnapshot(RMI_ID[] TARGET) {

        int size = buffTurretEntity.size();
        if (size > 0) {
            LinkedList<BuffTurretEntity> data = new LinkedList<>(buffTurretEntity.values());

            for (int i = 0; i < size; i++) {
                BuffTurretEntity entity = data.poll();
                worldEntityData.buffTurretData.add(getBuffTurretDataFromEntity(entity));
            }
            server_to_client.broadcastingBuffTurretSnapshot(TARGET, RMI_Context.UnReliable, worldEntityData.buffTurretData);
        }
    }

    void broadcastingBarricadeEntitySnapshot(RMI_ID[] TARGET) {

        int size = barricadeEntity.size();
        if (size > 0) {
            LinkedList<BarricadeEntity> data = new LinkedList<>(barricadeEntity.values());

            for (int i = 0; i < size; i++) {
                BarricadeEntity entity = data.poll();
                worldEntityData.barricadeData.add(getBarricadeDataFromEntity(entity));
            }
            server_to_client.broadcastingBarricadeSnapshot(TARGET, RMI_Context.UnReliable, worldEntityData.barricadeData);
        }
    }

    void broadcastingCrystalEntitySnapshot(RMI_ID[] TARGET) {

        int size = crystalEntity.size();
        if (size > 0) {
            LinkedList<CrystalEntity> data = new LinkedList<>(crystalEntity.values());

            for (int i = 0; i < size; i++) {
                CrystalEntity entity = data.poll();
                worldEntityData.crystalData.add(getCrystalDataFromEntity(entity));
            }
            server_to_client.broadcastingCrystalSnapshot(TARGET, RMI_Context.UnReliable, worldEntityData.crystalData);
        }
    }

    void broadcastingSkillObjectEntitySnapshot(RMI_ID[] TARGET) {

        int size = skillObjectEntity.size();
        if (size > 0) {
            LinkedList<SkillObjectEntity> data = new LinkedList<>(skillObjectEntity.values());

            for (int i = 0; i < size; i++) {
                SkillObjectEntity entity = data.poll();
                worldEntityData.skillObjectData.add(getSkillObjectDataFromEntity(entity));
            }
            server_to_client.broadcastingSkillObjectSnapshot(TARGET, RMI_Context.UnReliable, worldEntityData.skillObjectData);
        }
    }

    void broadcastingFlyingObjectEntitySnapshot(RMI_ID[] TARGET) {

        int size = flyingObjectEntity.size();
        if (size > 0) {
            LinkedList<FlyingObjectEntity> data = new LinkedList<>(flyingObjectEntity.values());

            for (int i = 0; i < size; i++) {
                FlyingObjectEntity entity = data.poll();
                worldEntityData.flyingObjectData.add(getFlyingObjectDataFromEntity(entity));
            }
            server_to_client.broadcastingFlyingObjectSnapshot(TARGET, RMI_Context.UnReliable, worldEntityData.flyingObjectData);
        }
    }

    /**
     * 2020 02 17 권령희
     * 음.. 빌드슬롯 하나의 목록 상태를 보내주기 위한 broadcasting 매서드를 server_to_client에 추가해줘야 할듯.
     *
     */
    void broadcastingBuildSlotDataSnapshot(RMI_ID[] TARGET){

        int size = buildSlotList.size();
        if(size > 0){

            for(int i=0; i< size; i++){
                BuildSlot buildSlot = buildSlotList.get(i);
                worldEntityData.buildSlotData.add(getBuildSlotData(buildSlot));
            }
            server_to_client.broadcastingBuildSlotSnapshot(TARGET, RMI_Context.UnReliable, worldEntityData.buildSlotData);

        }

    }

    /**
     * 2020 03 06.. 매번 스냅샷을 전송할 필요가 없어 안씀.
     *
     * @param
     */
    void broadcastingStoreUpgradeSlotDataSnapshot(RMI_ID[] TARGET){

        int size = upgradeSlotList.size();
        if(size > 0){

            for(int i=0; i< size; i++){
                StoreUpgradeSlot slot = upgradeSlotList.get(i);
                worldEntityData.storeUpgradeBuffSlotData.add(getStoreUpgradeSlotData(slot));
            }
            server_to_client.broadcastingBuildSlotSnapshot(TARGET, RMI_Context.Reliable_Public_AES256, worldEntityData.buildSlotData);

        }


    }



    //Data 클래스로부터 Entity 클래스 생성하는 부분.
    CharacterEntity getCharacterEntityFromData(CharacterData characterData) {

        CharacterEntity entity = new CharacterEntity();

        entity.entityID = characterData.entityID;
        entity.characterComponent = new CharacterComponent();
        entity.characterComponent.characterName = characterData.characterName;
        entity.characterComponent.characterType = characterData.characterType;
        entity.characterComponent.level = characterData.level;
        entity.characterComponent.exp = characterData.exp;
        entity.characterComponent.gold = characterData.gold;

        entity.characterComponent.skillPoint = 1;

        entity.skillSlotComponent = new SkillSlotComponent();

        entity.itemSlotComponent = new ItemSlotComponent();

        entity.hpComponent = new HPComponent();
        entity.hpComponent.currentHP = characterData.currentHP;

        entity.hpComponent.maxHP = characterData.maxHP;
        entity.hpComponent.recoveryRateHP = characterData.recoveryRateHP;

        entity.mpComponent = new MPComponent();
        entity.mpComponent.currentMP = characterData.currentMP;
        entity.mpComponent.maxMP = characterData.maxMP;
        entity.mpComponent.recoveryRateMP = characterData.recoveryRateMP;

        entity.attackComponent = new AttackComponent();
        entity.attackComponent.attackRange = characterData.attackRange;
        entity.attackComponent.attackSpeed = characterData.attackSpeed;
        entity.attackComponent.attackDamage = characterData.attackDamage;

        entity.defenseComponent = new DefenseComponent();
        entity.defenseComponent.defense = characterData.defense;

        entity.positionComponent = new PositionComponent();
        entity.positionComponent.position = new Vector3(characterData.posX, characterData.posY, characterData.posZ);
        //entity.positionComponent.position = new Vector3(-62, 0, -33);

        entity.rotationComponent = new RotationComponent();
        entity.rotationComponent.y = characterData.quarternionY;
        entity.rotationComponent.z = characterData.quarternionZ;

        entity.velocityComponent = new VelocityComponent();
        entity.velocityComponent.velocity = new Vector3(characterData.velX, characterData.velY, characterData.velZ);
        entity.velocityComponent.moveSpeed = characterData.moveSpeed;

        entity.buffActionHistoryComponent = new BuffActionHistoryComponent();
        entity.buffActionHistoryComponent.conditionHistory = new ArrayList<>();

        entity.hpHistoryComponent = new HpHistoryComponent();
        entity.hpHistoryComponent.hpHistory = new LinkedList<>();

        entity.mpHistoryComponent = new MPHistoryComponent();
        entity.mpHistoryComponent.mpHistory = new LinkedList<>();

        entity.conditionComponent = new ConditionComponent();
        entity.conditionComponent.isDisableAttack = characterData.isDisableAttack;
        entity.conditionComponent.isDisableMove = characterData.isDisableMove;
        entity.conditionComponent.isUnTargetable = characterData.isUnTargetable;
        entity.conditionComponent.isDamageImmunity = characterData.isDamageImmunity;
        entity.conditionComponent.isDisableSkill = characterData.isDisableSkill;
        entity.conditionComponent.isDisableItem = characterData.isDisableItem;

        entity.conditionComponent.hpRecoveryRate = characterData.hpRecoveryRate;
        entity.conditionComponent.mpRecoveryRate = characterData.mpRecoveryRate;
        entity.conditionComponent.maxHPRate = characterData.maxHPRate;
        entity.conditionComponent.maxMPRate = characterData.maxMPRate;
        entity.conditionComponent.moveSpeedRate = characterData.moveSpeedRate;
        entity.conditionComponent.expGainRate = characterData.expGainRate;
        entity.conditionComponent.goldGainRate = characterData.goldGainRate;
        entity.conditionComponent.attackDamageRate = characterData.attackDamageRate;
        entity.conditionComponent.attackSpeedRate = characterData.attackSpeedRate;
        entity.conditionComponent.defenseRate = characterData.defenseRate;
        entity.conditionComponent.buffDurationRate = characterData.buffDurationRate;
        entity.conditionComponent.coolTimeReduceRate = characterData.coolTimeReduceRate;

        entity.conditionComponent.attackDamageBonus = characterData.attackDamageBonus;
        entity.conditionComponent.moveSpeedBonus = characterData.moveSpeedBonus;
        entity.conditionComponent.defenseBonus = characterData.defenseBonus;
        entity.conditionComponent.maxHPBonus = characterData.maxHPBonus;
        entity.conditionComponent.maxMPBonus = characterData.maxMPBonus;

        entity.rewardHistoryComponent = new RewardHistoryComponent();


        switch (entity.characterComponent.characterType){

            case CharacterType.KNIGHT :
                entity.attribute = AttributeType.RED;
                break;
            case CharacterType.MAGICIAN :
                entity.attribute = AttributeType.BLUE;
                break;
            case CharacterType.ARCHER :
                entity.attribute = AttributeType.GREEN;
                break;

        }


        return entity;
    }

    /**
     * 업뎃날짜 : 오후 6:48 2020-05-05
     * 업뎃내용 :
     *      -- 통신용 CharacterData에, 캐릭터의 게임 스코어 정보(몹킬, 데스 카운트) 추가
     *      -- ㄴ shieldAmount 추가
     *
     */
    public CharacterData getCharacterDataFromEntity(CharacterEntity entity) {

        CharacterData characterData = new CharacterData();

        characterData.entityID = entity.entityID;
        characterData.team = entity.team;

        characterData.characterType = entity.characterComponent.characterType;
        characterData.characterName = entity.characterComponent.characterName;

        characterData.level = entity.characterComponent.level;
        characterData.exp = (int) entity.characterComponent.exp;
        characterData.gold = entity.characterComponent.gold;

        characterData.currentHP = entity.hpComponent.currentHP;
        characterData.maxHP = entity.hpComponent.maxHP;
        characterData.recoveryRateHP = entity.hpComponent.recoveryRateHP;

        characterData.currentMP = entity.mpComponent.currentMP;
        characterData.maxMP = entity.mpComponent.maxMP;
        characterData.recoveryRateMP = entity.mpComponent.recoveryRateMP;

        characterData.attackRange = entity.attackComponent.attackRange;
        characterData.attackSpeed = entity.attackComponent.attackSpeed;
        characterData.attackDamage = entity.attackComponent.attackDamage;

        characterData.criticalChance = entity.attackComponent.criticalChance;
        characterData.criticalDamage = entity.attackComponent.criticalDamage;

        characterData.defense = entity.defenseComponent.defense;

        characterData.posX = entity.positionComponent.position.x();
        characterData.posY = entity.positionComponent.position.y();
        characterData.posZ = entity.positionComponent.position.z();

        characterData.quarternionY = entity.rotationComponent.y;
        characterData.quarternionZ = entity.rotationComponent.z;

        characterData.velX = entity.velocityComponent.velocity.x();
        characterData.velY = entity.velocityComponent.velocity.y();
        characterData.velZ = entity.velocityComponent.velocity.z();

        characterData.moveSpeed = entity.velocityComponent.moveSpeed;


        characterData.isDisableAttack = entity.conditionComponent.isDisableAttack;
        characterData.isDisableMove = entity.conditionComponent.isDisableMove;
        characterData.isUnTargetable = entity.conditionComponent.isUnTargetable;
        characterData.isDamageImmunity = entity.conditionComponent.isDamageImmunity;
        characterData.isDisableSkill = entity.conditionComponent.isDisableSkill;
        characterData.isDisableItem = entity.conditionComponent.isDisableItem;

        characterData.hpRecoveryRate = entity.conditionComponent.hpRecoveryRate;
        characterData.mpRecoveryRate = entity.conditionComponent.mpRecoveryRate;
        characterData.maxHPRate = entity.conditionComponent.maxHPRate;
        characterData.maxMPRate = entity.conditionComponent.maxMPRate;
        characterData.moveSpeedRate = entity.conditionComponent.moveSpeedRate;
        characterData.expGainRate = entity.conditionComponent.expGainRate;
        characterData.goldGainRate = entity.conditionComponent.goldGainRate;
        characterData.attackDamageRate = entity.conditionComponent.attackDamageRate;
        characterData.attackSpeedRate = entity.conditionComponent.attackSpeedRate;
        characterData.defenseRate = entity.conditionComponent.defenseRate;
        characterData.buffDurationRate = entity.conditionComponent.buffDurationRate;
        characterData.coolTimeReduceRate = entity.conditionComponent.coolTimeReduceRate;

        characterData.attackDamageBonus = entity.conditionComponent.attackDamageBonus;
        characterData.moveSpeedBonus = entity.conditionComponent.moveSpeedBonus;
        characterData.defenseBonus = entity.conditionComponent.defenseBonus;
        characterData.maxHPBonus = entity.conditionComponent.maxHPBonus;
        characterData.maxMPBonus = entity.conditionComponent.maxMPBonus;

        characterData.criticalChanceRate = entity.conditionComponent.criticalChanceRate;
        characterData.criticalDamageRate = entity.conditionComponent.criticalDamageRate;

        /* 2020 03 12 */
        characterData.isAirborne = entity.conditionComponent.isAriborne;
        characterData.isAirborneImmunity = entity.conditionComponent.isAirborneImmunity;
        characterData.isKnockedAirborne = entity.conditionComponent.isKnockedAirborne;
        characterData.isKnockback = entity.conditionComponent.isKnockback;
        characterData.isBlind = entity.conditionComponent.isBlind;
        characterData.isCharm = entity.conditionComponent.isCharm;
        characterData.isDisarmed = entity.conditionComponent.isDisarmed;
        characterData.isFlee = entity.conditionComponent.isFlee;
        characterData.isFreezing = entity.conditionComponent.isFreezing;
        characterData.isGrounding = entity.conditionComponent.isGrounding;
        characterData.isPolymorph = entity.conditionComponent.isPolymorph;
        characterData.isSightBlocked = entity.conditionComponent.isSightBlocked;
        characterData.isSilence = entity.conditionComponent.isSilence;
        characterData.isSleep = entity.conditionComponent.isSleep;
        characterData.isSlow = entity.conditionComponent.isSlow;
        characterData.isSnare = entity.conditionComponent.isSnare;
        characterData.isStunned = entity.conditionComponent.isStunned;
        characterData.isSuppressed = entity.conditionComponent.isSuppressed;
        characterData.isSuspension = entity.conditionComponent.isSuspension;
        characterData.isTargetingInvincible = entity.conditionComponent.isTargetingInvincible;
        characterData.isTaunt = entity.conditionComponent.isTaunt;


        for(int i=0; i<entity.skillSlotComponent.skillSlotList.size(); i++){

            /* 현재 인덱스의 스킬 하나를 꺼내서, 통신용 데이터인 SkillSlotData 객체를 하나 생성해서 넣어준다  */
            SkillSlot skillSlot = entity.skillSlotComponent.skillSlotList.get(i);
            SkillSlotData skillSlotData = new SkillSlotData();

            skillSlotData.slotNum = skillSlot.slotNum;
            skillSlotData.skillLevel = skillSlot.skillLevel;
            skillSlotData.remainCoolTime = skillSlot.remainCoolTime;

            skillSlotData.skillInfo = new SkillInfoData();
            skillSlotData.skillInfo.skillType = skillSlot.skillinfo.skillType;

            characterData.skillSlot.add(skillSlotData);
        }


        for(int i=0; i<entity.itemSlotComponent.itemSlotList.size(); i++){

            /* 현재 인덱스의 스킬 하나를 꺼내서, 통신용 데이터인 SkillSlotData 객체를 하나 생성해서 넣어준다  */
            ItemSlot itemSlot = entity.itemSlotComponent.itemSlotList.get(i);
            ItemSlotData itemSlotData = new ItemSlotData();

            itemSlotData.slotNum = itemSlot.slotNum;
            itemSlotData.itemCount = itemSlot.itemCount;
            itemSlotData.remainCoolTime = itemSlot.remainCoolTime;

            itemSlotData.itemInfo = new ItemInfoData();
            itemSlotData.itemInfo.itemType = itemSlot.itemInfo.itemType;

            characterData.itemSlot.add(itemSlotData);


            /** 오후 6:40 2020-05-05 추가 ****************************/

            /* KILL & DEATH 스코어 정보 추가 */
            characterData.kill = playerGameScoreList.get(entity.entityID).monsterKillCount;
            characterData.death = playerGameScoreList.get(entity.entityID).characterDeathCount;

            /* SHIELD 값 추가 */
            characterData.shieldAmount = entity.hpComponent.shieldAmount;
            characterData.isShieldActivated = entity.conditionComponent.isShieldActivated;

            /*********************************************************/

        }



        /** 2020 03 12 */
        /**
         * 버프 받고있는 목록 넘겨주는거 (임시..)
         */

        int size = entity.buffActionHistoryComponent.conditionHistory.size();
        for(int i=0; i<size; i++){

            BuffAction currentBuff = entity.buffActionHistoryComponent.conditionHistory.get(i);

            System.out.println("스킬타입 =" + currentBuff.skillType + ", 템타입 = " + currentBuff.itemType);

            if((currentBuff.skillType == 0) && (currentBuff.itemType == 0)){

                continue;
            }

            ConditionData conditionData = new ConditionData();
            conditionData.itemType = currentBuff.itemType;
            conditionData.skillType = currentBuff.skillType;
            conditionData.buffDurationTime = currentBuff.buffDurationTime;
            conditionData.buffRemainTime = currentBuff.remainTime;

            characterData.conditionList.add(conditionData);

        }

        /*================================*/

        return characterData;
    }


    /**
     * 업뎃날짜 : 오후 8:20 2020-05-05 추가
     * 업뎃내용 :
     *      -- shieldAmount 추가
     *
     */
    MonsterData getMonsterDataFromEntity(MonsterEntity entity) {
        MonsterData data = new MonsterData();

        data.entityID = entity.entityID;
        data.monsterType = entity.monsterComponent.monsterType;

        data.currentHP = entity.hpComponent.currentHP;
        data.maxHP = entity.hpComponent.maxHP;
        data.recoveryRateHP = entity.hpComponent.recoveryRateHP;

        data.attackRange = entity.attackComponent.attackRange;
        data.attackSpeed = entity.attackComponent.attackSpeed;
        data.attackDamage = entity.attackComponent.attackDamage;

        data.defense = entity.defenseComponent.defense;

        data.posX = entity.positionComponent.position.x();
        data.posY = entity.positionComponent.position.y();
        data.posZ = entity.positionComponent.position.z();

        data.quarternionY = entity.rotationComponent.y;
        data.quarternionZ = entity.rotationComponent.z;

        data.velX = entity.velocityComponent.velocity.x();
        data.velY = entity.velocityComponent.velocity.y();
        data.velZ = entity.velocityComponent.velocity.z();

        data.moveSpeed = entity.velocityComponent.moveSpeed;

        data.lookRadius = entity.sightComponent.lookRadius;

        data.isDisableAttack = entity.conditionComponent.isDisableAttack;
        data.isDisableMove = entity.conditionComponent.isDisableMove;
        data.isUnTargetable = entity.conditionComponent.isUnTargetable;
        data.isDamageImmunity = entity.conditionComponent.isDamageImmunity;
        data.isDisableSkill = entity.conditionComponent.isDisableSkill;
        data.isDisableItem = entity.conditionComponent.isDisableItem;

        data.hpRecoveryRate = entity.conditionComponent.hpRecoveryRate;
        data.mpRecoveryRate = entity.conditionComponent.mpRecoveryRate;
        data.maxHPRate = entity.conditionComponent.maxHPRate;
        data.maxMPRate = entity.conditionComponent.maxMPRate;
        data.moveSpeedRate = entity.conditionComponent.moveSpeedRate;
        data.expGainRate = entity.conditionComponent.expGainRate;
        data.goldGainRate = entity.conditionComponent.goldGainRate;
        data.attackDamageRate = entity.conditionComponent.attackDamageRate;
        data.attackSpeedRate = entity.conditionComponent.attackSpeedRate;
        data.defenseRate = entity.conditionComponent.defenseRate;
        data.buffDurationRate = entity.conditionComponent.buffDurationRate;
        data.coolTimeReduceRate = entity.conditionComponent.coolTimeReduceRate;

        data.attackDamageBonus = entity.conditionComponent.attackDamageBonus;
        data.moveSpeedBonus = entity.conditionComponent.moveSpeedBonus;
        data.defenseBonus = entity.conditionComponent.defenseBonus;
        data.maxHPBonus = entity.conditionComponent.maxHPBonus;
        data.maxMPBonus = entity.conditionComponent.maxMPBonus;

        data.criticalChanceRate = entity.conditionComponent.criticalChanceRate;
        data.criticalDamageRate = entity.conditionComponent.criticalDamageRate;

        /* 2020 03 12 */
        data.isAirborne = entity.conditionComponent.isAriborne;
        data.isAirborneImmunity = entity.conditionComponent.isAirborneImmunity;
        data.isKnockedAirborne = entity.conditionComponent.isKnockedAirborne;
        data.isKnockback = entity.conditionComponent.isKnockback;
        data.isBlind = entity.conditionComponent.isBlind;
        data.isCharm = entity.conditionComponent.isCharm;
        data.isDisarmed = entity.conditionComponent.isDisarmed;
        data.isFlee = entity.conditionComponent.isFlee;
        data.isFreezing = entity.conditionComponent.isFreezing;
        data.isGrounding = entity.conditionComponent.isGrounding;
        data.isPolymorph = entity.conditionComponent.isPolymorph;
        data.isSightBlocked = entity.conditionComponent.isSightBlocked;
        data.isSilence = entity.conditionComponent.isSilence;
        data.isSleep = entity.conditionComponent.isSleep;
        data.isSlow = entity.conditionComponent.isSlow;
        data.isSnare = entity.conditionComponent.isSnare;
        data.isStunned = entity.conditionComponent.isStunned;
        data.isSuppressed = entity.conditionComponent.isSuppressed;
        data.isSuspension = entity.conditionComponent.isSuspension;
        data.isTargetingInvincible = entity.conditionComponent.isTargetingInvincible;
        data.isTaunt = entity.conditionComponent.isTaunt;


        /** 오후 8:20 2020-05-05 추가 */
        data.shieldAmount = entity.hpComponent.shieldAmount;
        data.isShieldActivated = entity.conditionComponent.isShieldActivated;

        data.monsterLevel = entity.monsterComponent.monsterLevel;
        data.monsterElemental = entity.attribute;


        return data;
    }

    /**
     * 업뎃날짜 : 오후 8:20 2020-05-05 추가
     * 업뎃내용 :
     *      -- shieldAmount 추가
     *
     */
    AttackTurretData getAttackTurretDataFromEntity(AttackTurretEntity entity) {
        AttackTurretData data = new AttackTurretData();

        data.entityID = entity.entityID;
        data.TurretType = entity.turretComponent.turretType;
        data.costGold = entity.turretComponent.costGold;
        data.costTime = entity.turretComponent.costTime;

        data.currentHP = entity.hpComponent.currentHP;
        data.maxHP = entity.hpComponent.maxHP;
        data.recoveryRateHP = entity.hpComponent.recoveryRateHP;

        data.attackRange = entity.attackComponent.attackRange;
        data.attackSpeed = entity.attackComponent.attackSpeed;
        data.attackDamage = entity.attackComponent.attackDamage;

        data.defense = entity.defenseComponent.defense;

        data.posX = entity.positionComponent.position.x();
        data.posY = entity.positionComponent.position.y();
        data.posZ = entity.positionComponent.position.z();

        data.isDisableAttack = entity.conditionComponent.isDisableAttack;
        data.isDisableMove = entity.conditionComponent.isDisableMove;
        data.isUnTargetable = entity.conditionComponent.isUnTargetable;
        data.isDamageImmunity = entity.conditionComponent.isDamageImmunity;
        data.isDisableSkill = entity.conditionComponent.isDisableSkill;
        data.isDisableItem = entity.conditionComponent.isDisableItem;

        data.hpRecoveryRate = entity.conditionComponent.hpRecoveryRate;
        data.mpRecoveryRate = entity.conditionComponent.mpRecoveryRate;
        data.maxHPRate = entity.conditionComponent.maxHPRate;
        data.maxMPRate = entity.conditionComponent.maxMPRate;
        data.moveSpeedRate = entity.conditionComponent.moveSpeedRate;
        data.expGainRate = entity.conditionComponent.expGainRate;
        data.goldGainRate = entity.conditionComponent.goldGainRate;
        data.attackDamageRate = entity.conditionComponent.attackDamageRate;
        data.attackSpeedRate = entity.conditionComponent.attackSpeedRate;
        data.defenseRate = entity.conditionComponent.defenseRate;
        data.buffDurationRate = entity.conditionComponent.buffDurationRate;
        data.coolTimeReduceRate = entity.conditionComponent.coolTimeReduceRate;

        data.attackDamageBonus = entity.conditionComponent.attackDamageBonus;
        data.moveSpeedBonus = entity.conditionComponent.moveSpeedBonus;
        data.defenseBonus = entity.conditionComponent.defenseBonus;
        data.maxHPBonus = entity.conditionComponent.maxHPBonus;
        data.maxMPBonus = entity.conditionComponent.maxMPBonus;


        /** 오후 8:20 2020-05-05 추가 */
        data.shieldAmount = entity.hpComponent.shieldAmount;
        data.isShieldActivated = entity.conditionComponent.isShieldActivated;


        return data;
    }

    /**
     * 업뎃날짜 : 오후 8:20 2020-05-05 추가
     * 업뎃내용 :
     *      -- shieldAmount 추가
     *
     */
    BuffTurretData getBuffTurretDataFromEntity(BuffTurretEntity entity) {
        BuffTurretData data = new BuffTurretData();

        data.entityID = entity.entityID;
        data.TurretType = entity.turretComponent.turretType;
        data.costGold = entity.turretComponent.costGold;
        data.costTime = entity.turretComponent.costTime;

        data.currentHP = entity.hpComponent.currentHP;
        data.maxHP = entity.hpComponent.maxHP;
        data.recoveryRateHP = entity.hpComponent.recoveryRateHP;

        data.buffAreaRange = entity.buffComponent.buffAreaRange;

        data.defense = entity.defenseComponent.defense;

        data.posX = entity.positionComponent.position.x();
        data.posY = entity.positionComponent.position.y();
        data.posZ = entity.positionComponent.position.z();

        data.isDisableAttack = entity.conditionComponent.isDisableAttack;
        data.isDisableMove = entity.conditionComponent.isDisableMove;
        data.isUnTargetable = entity.conditionComponent.isUnTargetable;
        data.isDamageImmunity = entity.conditionComponent.isDamageImmunity;
        data.isDisableSkill = entity.conditionComponent.isDisableSkill;
        data.isDisableItem = entity.conditionComponent.isDisableItem;

        data.hpRecoveryRate = entity.conditionComponent.hpRecoveryRate;
        data.mpRecoveryRate = entity.conditionComponent.mpRecoveryRate;
        data.maxHPRate = entity.conditionComponent.maxHPRate;
        data.maxMPRate = entity.conditionComponent.maxMPRate;
        data.moveSpeedRate = entity.conditionComponent.moveSpeedRate;
        data.expGainRate = entity.conditionComponent.expGainRate;
        data.goldGainRate = entity.conditionComponent.goldGainRate;
        data.attackDamageRate = entity.conditionComponent.attackDamageRate;
        data.attackSpeedRate = entity.conditionComponent.attackSpeedRate;
        data.defenseRate = entity.conditionComponent.defenseRate;
        data.buffDurationRate = entity.conditionComponent.buffDurationRate;
        data.coolTimeReduceRate = entity.conditionComponent.coolTimeReduceRate;

        data.attackDamageBonus = entity.conditionComponent.attackDamageBonus;
        data.moveSpeedBonus = entity.conditionComponent.moveSpeedBonus;
        data.defenseBonus = entity.conditionComponent.defenseBonus;
        data.maxHPBonus = entity.conditionComponent.maxHPBonus;
        data.maxMPBonus = entity.conditionComponent.maxMPBonus;


        /** 오후 8:20 2020-05-05 추가 */
        data.shieldAmount = entity.hpComponent.shieldAmount;
        data.isShieldActivated = entity.conditionComponent.isShieldActivated;


        return data;
    }

    /**
     * 업뎃날짜 : 오후 7:32 2020-05-05
     * 업뎃내용 : 방호벽 정보에 방호벽 강화 레벨 추가
     *              shieldAmount 추가
     *
     */
    BarricadeData getBarricadeDataFromEntity(BarricadeEntity entity) {
        BarricadeData data = new BarricadeData();

        data.entityID = entity.entityID;

        data.costTime = entity.barricadeComponent.costTime;
        data.costGold = entity.barricadeComponent.costGold;

        data.currentHP = entity.hpComponent.currentHP;
        data.maxHP = entity.hpComponent.maxHP;
        data.recoveryRateHP = entity.hpComponent.recoveryRateHP;

        data.defense = entity.defenseComponent.defense;

        data.posX = entity.positionComponent.position.x();
        data.posY = entity.positionComponent.position.y();
        data.posZ = entity.positionComponent.position.z();

        data.isDisableAttack = entity.conditionComponent.isDisableAttack;
        data.isDisableMove = entity.conditionComponent.isDisableMove;
        data.isUnTargetable = entity.conditionComponent.isUnTargetable;
        data.isDamageImmunity = entity.conditionComponent.isDamageImmunity;
        data.isDisableSkill = entity.conditionComponent.isDisableSkill;
        data.isDisableItem = entity.conditionComponent.isDisableItem;

        data.hpRecoveryRate = entity.conditionComponent.hpRecoveryRate;
        data.mpRecoveryRate = entity.conditionComponent.mpRecoveryRate;
        data.maxHPRate = entity.conditionComponent.maxHPRate;
        data.maxMPRate = entity.conditionComponent.maxMPRate;
        data.moveSpeedRate = entity.conditionComponent.moveSpeedRate;
        data.expGainRate = entity.conditionComponent.expGainRate;
        data.goldGainRate = entity.conditionComponent.goldGainRate;
        data.attackDamageRate = entity.conditionComponent.attackDamageRate;
        data.attackSpeedRate = entity.conditionComponent.attackSpeedRate;
        data.defenseRate = entity.conditionComponent.defenseRate;
        data.buffDurationRate = entity.conditionComponent.buffDurationRate;
        data.coolTimeReduceRate = entity.conditionComponent.coolTimeReduceRate;

        data.attackDamageBonus = entity.conditionComponent.attackDamageBonus;
        data.moveSpeedBonus = entity.conditionComponent.moveSpeedBonus;
        data.defenseBonus = entity.conditionComponent.defenseBonus;
        data.maxHPBonus = entity.conditionComponent.maxHPBonus;
        data.maxMPBonus = entity.conditionComponent.maxMPBonus;


        /** 오후 7:32 2020-05-05 추가 ********************************/

        data.upgradeLevel = entity.barricadeComponent.upgradeLevel;


        data.shieldAmount = entity.hpComponent.shieldAmount;
        data.isShieldActivated = entity.conditionComponent.isShieldActivated;


        /*************************************************************/


        return data;
    }

    /**
     * 업뎃날짜 : 오후 8:20 2020-05-05 추가
     * 업뎃내용 :
     *      -- shieldAmount 추가
     *
     */
    CrystalData getCrystalDataFromEntity(CrystalEntity entity) {
        CrystalData data = new CrystalData();

        data.entityID = entity.entityID;

        data.crystalLevel = entity.crystalComponent.crystalLevel;

        data.currentHP = entity.hpComponent.currentHP;
        data.maxHP = entity.hpComponent.maxHP;
        data.recoveryRateHP = entity.hpComponent.recoveryRateHP;

        data.defense = entity.defenseComponent.defense;

        data.posX = entity.positionComponent.position.x();
        data.posY = entity.positionComponent.position.y();
        data.posZ = entity.positionComponent.position.z();

        data.isDisableAttack = entity.conditionComponent.isDisableAttack;
        data.isDisableMove = entity.conditionComponent.isDisableMove;
        data.isUnTargetable = entity.conditionComponent.isUnTargetable;
        data.isDamageImmunity = entity.conditionComponent.isDamageImmunity;
        data.isDisableSkill = entity.conditionComponent.isDisableSkill;
        data.isDisableItem = entity.conditionComponent.isDisableItem;

        data.hpRecoveryRate = entity.conditionComponent.hpRecoveryRate;
        data.mpRecoveryRate = entity.conditionComponent.mpRecoveryRate;
        data.maxHPRate = entity.conditionComponent.maxHPRate;
        data.maxMPRate = entity.conditionComponent.maxMPRate;
        data.moveSpeedRate = entity.conditionComponent.moveSpeedRate;
        data.expGainRate = entity.conditionComponent.expGainRate;
        data.goldGainRate = entity.conditionComponent.goldGainRate;
        data.attackDamageRate = entity.conditionComponent.attackDamageRate;
        data.attackSpeedRate = entity.conditionComponent.attackSpeedRate;
        data.defenseRate = entity.conditionComponent.defenseRate;
        data.buffDurationRate = entity.conditionComponent.buffDurationRate;
        data.coolTimeReduceRate = entity.conditionComponent.coolTimeReduceRate;

        data.attackDamageBonus = entity.conditionComponent.attackDamageBonus;
        data.moveSpeedBonus = entity.conditionComponent.moveSpeedBonus;
        data.defenseBonus = entity.conditionComponent.defenseBonus;
        data.maxHPBonus = entity.conditionComponent.maxHPBonus;
        data.maxMPBonus = entity.conditionComponent.maxMPBonus;


        /** 오후 8:20 2020-05-05 추가 */
        data.shieldAmount = entity.hpComponent.shieldAmount;
        data.isShieldActivated = entity.conditionComponent.isShieldActivated;


        return data;
    }

    SkillObjectData getSkillObjectDataFromEntity(SkillObjectEntity entity) {
        SkillObjectData data = new SkillObjectData();

        data.entityID = entity.entityID;

        data.posX = entity.positionComponent.position.x();
        data.posY = entity.positionComponent.position.y();
        data.posZ = entity.positionComponent.position.z();

        data.userEntityID = entity.skillObjectComponent.userEntityID;

        data.createdSkillType = entity.skillObjectComponent.createdSkillType;
        data.directionX = entity.skillObjectComponent.direction.x();
        data.directionY = entity.skillObjectComponent.direction.y();
        data.directionZ = entity.skillObjectComponent.direction.z();
        return data;
    }

    FlyingObjectData getFlyingObjectDataFromEntity(FlyingObjectEntity entity) {
        FlyingObjectData data = new FlyingObjectData();

        data.entityID = entity.entityID;

        data.posX = entity.positionComponent.position.x();
        data.posY = entity.positionComponent.position.y();
        data.posZ = entity.positionComponent.position.z();

        data.userEntityID = entity.flyingObjectComponent.userEntityID;

        data.createdSkillType = entity.flyingObjectComponent.createdSkillType;
        data.directionX = entity.flyingObjectComponent.direction.x();
        data.directionY = entity.flyingObjectComponent.direction.y();
        data.directionZ = entity.flyingObjectComponent.direction.z();


        return data;
    }

    /**
     * 2020 02 17 권령희
     * 월드맵의 BuildSlot 목록으로부터, 클라이언트로 슬롯 정보를 보내주기 위한 형태로 변경해주는 매서드
     * @return
     */
    BuildSlotData getBuildSlotData(BuildSlot slot){

        BuildSlotData slotData = new BuildSlotData();

        /**
         * BuildSlotData
         *      ★ public int slotNum;
         *      ★ public int slotState;
         *
         *      public buildingType;    // 필요없을수도.. 서버에서 다 검사하니까?
         *      public int buildingEntityID;   // 필요없을수도..
         *      public int builderEntityID; // 필요없을수도.. 굳이 해당 건물의 건설자가 누구다! 라고 겜 화면에 명시해줄 게 아니라면?
         *
         *      // 아래는.. 건물이 위치한 영역의 중심 좌표. 맵팩토리의 매서드를 호출해서, 맵컴포넌트 영역의 중심 좌표를 알아낼 것.
         *      ★ public float centerX;
         *      ★ public float centerY;
         *      ★ public float centerZ;
         *
         *      ★ public float remainBuildTime;   // 건물 건설중 혹은 업그레이드 중일 경우, 처리가 끝날 때 까지 남은 시간
         *
         */

        slotData.slotNum = slot.slotNum;
        slotData.slotState = slot.getSlotState();

        Vector3 centerPos = slot.mapPosition.getCenterPositionFromMapArea();
        slotData.centerX = centerPos.x();
        slotData.centerY = centerPos.y();
        slotData.centerZ = centerPos.z();

        slotData.remainBuildTime = slot.getRemainBuildTime();


        return slotData;

    }

    /**
     * 2020 03 06
     *
     * @return
     */
    StoreUpgradeBuffSlotData getStoreUpgradeSlotData(StoreUpgradeSlot slot){

        StoreUpgradeBuffSlotData slotData = new StoreUpgradeBuffSlotData();

        slotData.upgradeType = slot.upgradeType;
        slotData.upgradeLevel = slot.upgradeLevel;
        slotData.slotNum = slot.slotNum;

        return slotData;
    }




    //월드맵ID 가져오기
    public int getWorldMapID() {
        return this.worldMapID;
    }


    public void calculateGameResult(){

        ArrayList<PlayerGameScore> gameScores = new ArrayList<>();

        /** 유저들의 게임 플레이 점수를 계산한다. */
        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : characterEntity.entrySet()) {

            /* 캐릭터 정보를 가져온다 */
            CharacterEntity character = characterEntity.getValue();
            int entityID = character.entityID;
            PlayerGameScore gameScore = playerGameScoreList.get(entityID);

            if(false){

                System.out.println("플레이어 캐릭터 ID : " + character.entityID);
                System.out.println("몬스터에게 입힌 총 데미지 : " + gameScore.givenDamageAmount);
                System.out.println("총 몬스터를 죽인 수 : " + gameScore.monsterKillCount);
                System.out.println("총 벌어들인 골드 : " + gameScore.earnedGold);
                System.out.println("캐릭터의 최종 레벨 : " + character.characterComponent.level);
                System.out.println("몬스터에게 받은 총 데미지 : " + gameScore.getDamagedAmount);
                System.out.println("캐릭터가 총 죽은 횟수 : " + gameScore.characterDeathCount);
                System.out.println("도달한 웨이브 카운트 : " + waveInfoCount);

            }


            /* 최종 점수를 계산 */
            // (적에게 가한 피해 + 킬수 + 골드 + 레벨 - 받은 데미지) / 사망횟수 * 스테이지
            gameScore.finalScore =
                    (gameScore.givenDamageAmount + gameScore.monsterKillCount
                            + gameScore.earnedGold + character.characterComponent.level
                            - gameScore.getDamagedAmount) / (gameScore.characterDeathCount+1) * waveInfoCount;
            // 왜 죽은 횟수에 +1 해줬냐면, 캐릭터가 한 번도 죽지 않았을 경우 나누기 0이 되어 값이.. 무한대가 나온다고 해야하나..
            // 일단 이걸 피하기 위해서.

            if(gameScore.finalScore < 0){
                gameScore.finalScore= 0f;
            }

            gameScores.add(gameScore);

        }

        /** 최종 점수에 따라 유저들의 게임 플레이에 등급을 부여 */
        // 아니.. 정렬을 어떻게 한거지??
        gameScores.sort(new Comparator<PlayerGameScore>() {
            @Override
            public int compare(PlayerGameScore o1, PlayerGameScore o2) {
                if(o1.finalScore < o2.finalScore){
                    return -1;
                }
                else if(o1.finalScore > o2.finalScore){
                    return 1;
                }
                return 0;
            }
        });

        /* 0 == 브론즈, 1 == 실버, 2 == 골드 */
        for(int i=0; i<gameScores.size(); i++){

            gameScores.get(i).resultGrade = gameScores.size() - i;
        }

    }

    /**
     * 업뎃 : 2020 03 09 월요일 권령희
     * @return DB에 최종적으로 전송할 JSON 데이터
     */
    public String convertGameResultToJSon(){

        String resultJsonStr = null;
        Gson gson = new Gson();

        /* 플레이어 */
        JsonObject players = new JsonObject();
        JsonObject player;
        players.addProperty("count", worldMapTokenIDList.size());

        int userNum = 0;
        for (HashMap.Entry<String, Integer> tokenEntry  : worldMapTokenIDList.entrySet()) {

            userNum++;
            String tokenID = tokenEntry.getKey();
            int entityID = tokenEntry.getValue();

            int dbUserToken = dbUserTokenListByGoogle.get(tokenID);

            PlayerGameScore gameScore = playerGameScoreList.get(entityID);

            int level = characterEntity.get(entityID).characterComponent.level;

            int charType = characterEntity.get(entityID).characterComponent.characterType;

            player = new JsonObject();
            player.addProperty("googleToken", tokenID);
            player.addProperty("userToken", dbUserToken);
            player.addProperty("earnedGold", gameScore.earnedGold);
            player.addProperty("level", level);
            player.addProperty("givenDamageAmount", gameScore.givenDamageAmount);
            player.addProperty("getDamagedAmount", gameScore.getDamagedAmount);
            player.addProperty("monsterKillCount", gameScore.monsterKillCount);
            player.addProperty("deathCount", gameScore.characterDeathCount);
            player.addProperty("grade", gameScore.resultGrade);
            player.addProperty("finalScore", gameScore.finalScore);
            player.addProperty("guardianType", charType);

            /** 2020 03 09 월 추가 */
            /**
             * 닉네임.. 웹서버에는 보낼 필요 없는 정보인데, 이후에 클라이언트에 넘겨줄 때에 필요하다
             * 여기서 캐릭터 닉네임 참조하는 편이 더 편해서.. 일단 여기서 추가 처리함.
             */
            CharacterComponent characterComponent = characterEntity.get(entityID).characterComponent;
            player.addProperty("nickName", characterComponent.characterName);

            /** 2020 03 09 월 추가 */
            /**
             * 플레이어 정보에 유저가 습득했던 스킬들의 정보를 추가한다.
             */
            CharacterEntity character = characterEntity.get(entityID);
            SkillSlotComponent skills = character.skillSlotComponent;

            JsonObject skillSet = new JsonObject();
            for(int i=0; i<skills.skillSlotList.size(); i++){

                System.out.println(i);

                SkillSlot currentSKill = skills.skillSlotList.get(i);
                //currentSKill = SkillFactory.findSkillSlotBySlotNum(i+1, character);
                if(currentSKill == null){
                    currentSKill = new SkillSlot(i+1, new SkillInfo(SkillType.NONE));

                }
                else{
/*
                    System.out.println("슬롯 번호 : " + currentSKill.slotNum);
                    System.out.println("스킬 레벨 : " + currentSKill.skillLevel);
*/
                }

                JsonObject skill = new JsonObject();
                skill.addProperty("skillType", currentSKill.skillinfo.skillType);
                skill.addProperty("skillLevel", currentSKill.skillLevel);

                //skillSet.add(currentSKill.slotNum + "", skill);   // 슬롯 번호랑.. JSON Name?? 간 충돌... 찍은 스킬 목록만 보여주느냐, 아니면 빈 슬롯이라도 비어있다고 넣어주느냐,,
                skillSet.add(i+1 + "", skill);
            }

            player.add("skillSet", skillSet);

            /***************************************************************************************/

            players.add(userNum + "", player);

            System.out.println(player);

        }

        //System.out.println(players);


        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat elapsedformat = new SimpleDateFormat("HHmmss");


        Date startTime = new Date();
        startTime.setTime(gameStartTime);
        System.out.println(format.format(startTime));

        Date finishTime = new Date();
        startTime.setTime(gameFinishTime);
        System.out.println(format.format(finishTime));



        Date elapsedTime = new Date();
        elapsedTime.setTime(gameElapsedTime);
        System.out.println(format.format(elapsedTime));

        int elapsedTime_H = (int) ((gameElapsedTime * 0.001) / 60) / 60;
        int elapsedTime_M = (int) ((gameElapsedTime * 0.001) /60) % 60 ;
        int elapsedTime_S = (int) (gameElapsedTime * 0.001) % 60;

        System.out.println(elapsedTime_H + " " + elapsedTime_M + " " + elapsedTime_S);

        String elapsedTimeStr = "";
        if(elapsedTime_H < 10){
            elapsedTimeStr += 0;
        }
        elapsedTimeStr += elapsedTime_H;

        if(elapsedTime_M < 10){
            elapsedTimeStr += 0;
        }
        elapsedTimeStr += elapsedTime_M;

        if(elapsedTime_S < 10){
            elapsedTimeStr += 0;
        }
        elapsedTimeStr += elapsedTime_S;


        JsonObject gameResult = new JsonObject();
        gameResult.addProperty("playTime", elapsedTimeStr);
        gameResult.addProperty("startTime", format.format(startTime));
        gameResult.addProperty("finishTime", format.format(finishTime));
        gameResult.addProperty("stage", waveInfoCount-1);
        gameResult.add("players", players);

        resultJsonStr = gson.toJson(gameResult);

        //System.out.println(resultJsonStr);

        return resultJsonStr;
    }


    /** 2020 02 10 추가한 매서드 시작 */

    public void initHttpClient(){

        httpClient = Dsl.asyncHttpClient();
        initRequestInfo();

    }

    public void initRequestInfo(){

        //initRQ_getPlayerCharInfo();
        //initRQ_saveGameResult();

    }

    public Response RQ_getPlayerCharInfo(String playerRequestInfo){

        Response response = null;
        //String ipAddr = "http://112.221.220.205/result/insertgame.php";  /** 주소 바꿀 것*/
        String ipAddr = "http://ngnl.xyz/result/insertgame.php";

        Future<Response> future =
                httpClient.preparePost(ipAddr)
                        .addFormParam("data", playerRequestInfo)
                        .execute(new AsyncCompletionHandler<Response>() {
                            @Override
                            public Response onCompleted(Response response) throws Exception {
                                System.out.println("요청에 대한 응답 : " + response);
                                // httpClient.close();
                                return response;
                            }

                            @Override
                            public void onThrowable(Throwable t) {
                                //System.out.println("오류?");
                                super.onThrowable(t);
                            }

                            @Override
                            public State onStatusReceived(HttpResponseStatus status) throws Exception {
                                //System.out.println("상태 코드 : " + status);
                                return super.onStatusReceived(status);
                            }
                        });

        try {
            response = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            return response;
        }

    }

    /**
     *
     *
     */
    public Response RQ_saveGameResult(String gameResult){

        Response response = null;
        //String ipAddr = "http://112.221.220.205/result/endgame.php";
        String ipAddr = "http://ngnl.xyz/result/endgame.php";

        Future<Response> future =
                httpClient.preparePost(ipAddr)
                        .addFormParam("data", gameResult)
                        .execute(new AsyncCompletionHandler<Response>() {
                            @Override
                            public Response onCompleted(Response response) throws Exception {
                                System.out.println("요청에 대한 응답 : " + response);
                                // httpClient.close();
                                return response;
                            }

                            @Override
                            public void onThrowable(Throwable t) {
                                //System.out.println("오류?");
                                super.onThrowable(t);
                            }

                            @Override
                            public State onStatusReceived(HttpResponseStatus status) throws Exception {
                                //System.out.println("상태 코드 : " + status);
                                return super.onStatusReceived(status);
                            }
                        });

        try {
            response = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            return response;
        }

    }

    // 파라미터랑 리턴값 바뀔 수 있음

    /**
     * 업뎃날짜 : 오후 6:40 2020-05-05
     * 업뎃내용 :
     *      -- 캐릭터 정보 요청할 때, 클라로부터 받아온 guardianID를 요청JS에 추가해서 보냄
     *      -- ㄴ 올바른 캐릭터 타입 & 장착 정보를 받아오기 위함.
     *
     */
    public String convertPlayerInfoToJSon(HashMap<String, LoadingPlayerData> playerInfo){

        String requestInfoStr = "";
        Gson gson = new Gson();

        JsonObject gameResult = new JsonObject();
        gameResult.addProperty("count", playerInfo.size());

        /* 플레이어 */
        int userNum = 0;
        JsonObject player;
        for (HashMap.Entry<String, LoadingPlayerData> tokenEntry  : playerInfo.entrySet()) {

            userNum ++;

            String tokenID = tokenEntry.getKey();
            int characterType = tokenEntry.getValue().characterType;
            int guardianID = tokenEntry.getValue().guardianID;

            int dbCharType = 0;
            switch (characterType){
                case CharacterType.KNIGHT :
                    dbCharType = CharacterType.KNIGHT;
                    break;
                case CharacterType.MAGICIAN :
                    dbCharType = CharacterType.MAGICIAN;
                    break;
                case CharacterType.ARCHER :
                    dbCharType = CharacterType.ARCHER;
                    break;
            }

            player = new JsonObject();
            player.addProperty("googleToken", tokenID);
            player.addProperty("guardianType", dbCharType);

            /** 오후 6:40 2020-05-05 추가된 부분 ********************/

            player.addProperty("guardianID", guardianID);

            /********************************************************/



            String numStr = userNum + "";
            gameResult.add(numStr, player);

            //System.out.println("플레이어 " + userNum +"의 JS : " + player);

        }

        requestInfoStr = gson.toJson(gameResult);
        System.out.println("플레이어 정보 요청을 위한 최종 JS : " + requestInfoStr);

        return requestInfoStr;

    }

    /**
     * DB로부터 가져온 JSON 데이터 중 플레이어 한명의 JSO를 가지고 캐릭터 데이터를 만든다
     * @param playerInfo
     * @return
     */
    public CharDataFromJS parsePlayerInfoJSonToData(JsonObject playerInfo){

        System.out.println("혹시 가디언인포라는 이름이 없어서 널이 뜬다던가??");

        CharDataFromJS charData = new CharDataFromJS(playerInfo.getAsJsonObject("guardianINFO"));

        System.out.println(playerInfo);

        return charData;
    }

    /**
     * 업뎃날짜 : 오후 8:05 2020-05-05
     * 업뎃내용 :
     *  hpComponent에 shieldAmount 처리 추가
     *
     */
    public CharacterEntity createCharacterEntityFromData(CharDataFromJS characterData, LoadingPlayerData playerData){

        CharacterEntity entity = new CharacterEntity();

        int newID = worldMapEntityIDGenerater.getAndIncrement();

        entity.entityID = newID;
        entity.characterComponent = new CharacterComponent();
        entity.characterComponent.characterName = playerData.characterName;
        entity.characterComponent.characterType = characterData.guardianType;
        entity.attribute = characterData.elemental;
        entity.attribute++;

        entity.characterComponent.level = 1;
        entity.characterComponent.exp = 0;
        entity.characterComponent.gold = 1000;

        entity.characterComponent.skillPoint = 1;

        entity.skillSlotComponent = new SkillSlotComponent();
        entity.itemSlotComponent = new ItemSlotComponent();

        entity.hpComponent = new HPComponent();
        //characterData.hp *= 10;
        entity.hpComponent.originalMaxHp = characterData.hp;
        entity.hpComponent.currentHP = characterData.hp;
        entity.hpComponent.maxHP = characterData.hp;
        entity.hpComponent.recoveryRateHP = characterData.hpRecoveryRate;

        /** 오후 8:05 2020-05-05 추가 */
        entity.hpComponent.shieldAmount = 0f;

        /*******************************************************************/


        entity.mpComponent = new MPComponent();
        entity.mpComponent.originalMaxMP = characterData.mp;
        entity.mpComponent.currentMP = characterData.mp;
        entity.mpComponent.maxMP = characterData.mp;
        entity.mpComponent.recoveryRateMP = characterData.mpRecoveryRate;

        entity.attackComponent = new AttackComponent();
        entity.attackComponent.attackRange = characterData.attackRange;
        entity.attackComponent.attackSpeed = characterData.attackSpeed;
        entity.attackComponent.attackDamage = characterData.attackDamage;


        /**
         * 추가 & 수정
         * 오전 4:56 2020-04-07
         * 크리티컬 적용 안해주고 있었음. 이거랑 밸런스 추가.
         */
        entity.attackComponent.balance = characterData.balance;
        entity.attackComponent.criticalChance = characterData.criticalRate;
        entity.attackComponent.criticalDamage = characterData.criticalBonus;

        entity.defenseComponent = new DefenseComponent();
        entity.defenseComponent.defense = characterData.defense;

        entity.positionComponent = new PositionComponent();
        //entity.positionComponent.position = new Vector3(150f, 0, -150f);
        //entity.positionComponent.position = new Vector3(150f, 0, -150f);
        entity.positionComponent.position = new Vector3(3f + ran.nextFloat()*0.5f, 0, -3f + ran.nextFloat()*0.5f);

        entity.rotationComponent = new RotationComponent();
        entity.rotationComponent.y = 0f;
        entity.rotationComponent.z = 0f;

        entity.velocityComponent = new VelocityComponent();
        entity.velocityComponent.velocity = new Vector3(0f, 0f, 0f);
        entity.velocityComponent.moveSpeed = characterData.moveSpeed;

        entity.buffActionHistoryComponent = new BuffActionHistoryComponent();
        entity.buffActionHistoryComponent.conditionHistory = new ArrayList<>();

        entity.hpHistoryComponent = new HpHistoryComponent();
        entity.hpHistoryComponent.hpHistory = new LinkedList<>();

        entity.mpHistoryComponent = new MPHistoryComponent();
        entity.mpHistoryComponent.mpHistory = new LinkedList<>();

        entity.conditionComponent = new ConditionComponent();

        entity.rewardHistoryComponent = new RewardHistoryComponent();





        return entity;

    }



    /** 2020 02 10 추가한 매서드 끝*/



    /** 2020 03 09 월요일 권령희 작성 */
    /**
     * 요 매서드를, 게임 메인 로직 스레드 종료 후 호출하게 하기.
     * 이전 처리들은 지우고.
     */
    public void broadcastingGameResult(){

        /** 게임 결과를 계산(?)한다  */
        calculateGameResult();

        /** 웹서버에 저장하기 위한 요청데이터(JS) 구성 */
        String gameResultJS = convertGameResultToJSon();

        /** 게임결과 저장 요청을 보낸다 */

        int requestCount = 0;
        final int MAX_RQ_COUNT = 3;

        Response response = null;
        boolean requestSucceed = false;
        while (true){

            if(requestCount == MAX_RQ_COUNT){
                System.out.println("요청 횟수가 3회에 도달하였습니다. 저장 실패..");
                break;
            }

            requestCount++;

            /** 요청을 보냄 */
            response = RQ_saveGameResult(gameResultJS);
            if((response != null) && response.getStatusCode() == HttpConstants.ResponseStatusCodes.OK_200){

                /** 응답을 성공적으로 받았을 때의 처리를 작성 */

                System.out.println("게임 결과 저장 성공 및 보상정보 응답 받음");

                requestSucceed = true;
                gameResultIsSaved = true;
                break;
            }
            else{

                /** 실패했을 때의 처리를 작성 */


                System.out.println("게임 결과 저장 실패.. 재도전!");

            }


        }

        /** 응답 내용을 가지고, 클라이언트에 게임 결과를 보낸다 */

        int resultCode = -1;

        if(requestSucceed){

            /** 게임 결과 저장에 성공했다는 응답을 보냄 */
            /**
             * resultCode = 0 또는 1; // 게임 클리어 시 1, 패배시 0
             * resultMessage = 서버에서 받아온 보상정보를 덧붙여 JS 구성;
             */
            resultCode = (crystalEntity.isEmpty()) ? 0 : 1;


            /** 웹서버에서 받아온 응답 내용 파싱하는 처리 */
            JsonObject rewardInfo = parseGameRewardInfo(response);

            /** 파싱한 내용을, 위에서 구성한 게임결과 JS의 각 플레이어 정보에 덧붙임 */
            String updatedGameResultJS = addRewardInfoToGameResultJS(gameResultJS, rewardInfo);

            //System.out.println("클라이언트에 보내줄 게임결과 : " + updatedGameResultJS);
            System.out.println("클라이언트에 게임 결과를 전송합니다.");

            /** 클라이언트에 결과 전송 */
            RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
            server_to_client.EndGame(TARGET, RMI_Context.Reliable_Public_AES256, resultCode, updatedGameResultJS);
            //server_to_client.EndGame(TARGET, RMI_Context.Reliable_Public_AES256, 1);

        }
        else{

            /** 게임 결과 저장에 실패했다는 응답을 보냄 */
            /**
             * resultCode = -1;
             * resultMessage = "게임 결과 저장에 실패했습니다";
             */

            // 아직 RMI가 변경되지 않음..
            String resultMessage = "게임 결과 저장에 실패했습니다";

            RMI_ID[] TARGET = RMI_ID.getArray(worldMapRMI_IDList.values());
            server_to_client.EndGame(TARGET, RMI_Context.Reliable_Public_AES256, resultCode, resultMessage);
            //server_to_client.EndGame(TARGET, RMI_Context.Reliable_Public_AES256, -1);


        }

    }

    /**
     * 기존 게임결과 JS에, 웹서버에서 받아온 보상 정보를 덧붙여서
     * 클라이언트에 최종적으로 보내줄 결과JS를 만든다.
     * @param gameResultJS
     * @return
     */
    public String addRewardInfoToGameResultJS(String gameResultJS, JsonObject rewardInfoJS){

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(gameResultJS);
        JsonObject updatedGameResult = jsonElement.getAsJsonObject();

        JsonObject players = updatedGameResult.getAsJsonObject("players");

        int count = rewardInfoJS.get("count").getAsInt();
        for(int i=0; i<count; i++){

            int playerNum = i+1;
            JsonObject player = players.getAsJsonObject(playerNum+"");

            String tokenID = player.get("googleToken").getAsString();
            CharacterEntity character = characterEntity.get(worldMapTokenIDList.get(tokenID));
            int guardianType = character.characterComponent.characterType;

            for(int j=0; j<count; j++){

                JsonObject reward = rewardInfoJS.getAsJsonObject(playerNum+"");
                if(reward.get("userToken").getAsInt() == player.get("userToken").getAsInt()){

                    player.addProperty("guardianType", guardianType);
                    player.addProperty("rewardExp", reward.get("rewardExp").getAsFloat());
                    player.addProperty("rewardGold", reward.get("rewardGold").getAsInt());

                    break;
                }
            }

        }


       return updatedGameResult.toString();

    }

    /**
     *
     * @param response
     * @return
     */
    public JsonObject parseGameRewardInfo(Response response){

        /** 파싱 */
        String rewardInfoString = response.getResponseBody();

        int index = rewardInfoString.indexOf("{");

        String testStr = rewardInfoString.substring(index);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(testStr);
        JsonObject rewardInfoObj = jsonElement.getAsJsonObject();

        return rewardInfoObj;
    }

    /*******************************************************************************************************************/

    /************* 정글몹 관련 처리 ************************************************************************************/

    /**
     * 업뎃시점 :
     *      -- 오후 6:33 2020-04-08
     *
     * 기    능 :
     *      -- 게임 시작 시 정글 몹 슬롯을 초기화하는 작업을 한다.
     *
     * 내    용 :
     *
     *      -- 초기화 작업 :
     *
     *          슬롯에 정글몹 배치하기
     *
     *      -- 방법 :
     *
     *              정글몹 슬롯 목록을 돌면서, 해당 슬롯에 배치할 정글몹을 랜덤으로 결정한다.
     *              ㄴ 정글몹을 랜덤으로 결정하기 위한 매서드를 호출.
     *              몹 지정 후, 몹이 바로 생성될 수 있게끔 상태를 설정한다 ; 아마 EMPTY
     *
     * 인풋 :
     * 아웃풋 :
     */
    public void initializeJungleMobSpawnPoints(){

        /** GDM 데이터 참조 */
        HashMap<Integer, MonsterInfo> jungleMonsterInfoList = GameDataManager.jungleMonsterInfoList;
        MonsterInfo jungleInfo;

        /** 정글몹 슬롯 목록을 돌면서 반복한다 */

        int monsterType = JungleMobType.NONE;
        JungleMonsterSlot jungleSlot;

        for(int i=0; i<jungleMonsterSlots.size(); i++){

            jungleSlot = jungleMonsterSlots.get(i);

            /** 슬롯에 들어갈 몬스터를 결정한다 */
            monsterType = decideJungleMonsterToBeSpawned();

            /** 결정된 정글 몬스터의 정보를 참조 */
            jungleInfo = jungleMonsterInfoList.get(monsterType);

            /** 슬롯에, 정글몹의 정보를 세팅한다 */
            jungleSlot.setJungleMonsterInfo(monsterType, jungleInfo.regenTime * 60f);

            //System.out.println("슬롯" + i + "에 셋팅된 몬스터 타입 : " + jungleInfo.monsterName);

        }

    }

    /**
     * 업뎃시점 :
     *
     *      -- 오후 6:13 2020-04-08
     *
     * 기    능 :
     *
     *      -- 임의 정글몹 슬롯에 생성할 정글몬스터를 확률에 따라 랜덤으로 결정한다
     *
     * 사    용 :
     *
     *      # 게임이 막 시작된 후 정글몹 슬롯을 초기화 할 때.
     *      # 각 슬롯의 정글몹이 죽은 후, 리젠 대기상태를 거쳐 몬스터를 부활시킬 때.
     *
     * 처리방법 :
     *
     *      1. 0~99 사이의 랜덤값을 하나 생성함
     *          # 예외처리 ( 범위 내 값이 발생한다던지 하는.. )
     *              정글 몹 목록의 Info를 돌면서, 확률값을 모두 더한다.
     *              0 ~ 최종적으로 더해진 수 사이의 값이 나오도록 한다.
     *
     *      2. GDM의 정글몬스터 Info 목록을 참조하여 반복한다
     *          구간 MAX 값 = 0으로 초기화;
     *          for(정글몹 목록){
     *
     *              현 정글몹의 등장 확률값을 가지고, 구간 MAX 값을 갱신한다 ( += 누적 )
     *              랜덤값이 MAX값보다 작은지 판단한다
     *              if 랜덤값이 MAX값 이하라면
     *                  해당 구간의 몬스터로 결정
     *                  break;
     *          }
     *
     *
     */
    public int decideJungleMonsterToBeSpawned(){

        int jungleType = JungleMobType.NONE;

        /** GDM 데이터 참조 */
        HashMap<Integer, MonsterInfo> jungleMonsterList = GameDataManager.jungleMonsterInfoList;


        /** 랜덤 수 설정 */

        /* 랜덤뽑기 할 범위의 max 값 구하기 */
        float maxValue = 0;
        for(MonsterInfo monsterInfo : jungleMonsterList.values()){

            maxValue +=  monsterInfo.appearProbRate;
        }

        /* 랜덤 뽑기를 수행 */
        float randomNum = (float) ( (Math.random() * (maxValue) ) );



        /** 정글몹 등장 확률구간을 활용하여, 랜덤값이 속한 구간을 정함 */
        float rangeMax = 0;
        for(MonsterInfo monsterInfo : jungleMonsterList.values()){

            rangeMax +=  monsterInfo.appearProbRate;
            if(randomNum < rangeMax){

                jungleType = monsterInfo.monsterType;
                break;
            }
        }


        return jungleType;
    }


    /**
     * 기    능 :
     *
     *   INPUT :
     *  OUTPUT :
     * PROCESS :
     */
    public void decideGameDifficultyGrade(){

        /** 팀 전투력을 계산한다 */
        float teamStrengthPower = GameDifficultyGrade.calculateTeamStrengthPower(characterEntity);

        System.out.println("전투력 : " + teamStrengthPower);

        /** 전투력이 속한 등급을 찾는다 */
        int grade = GameDifficultyGrade.decideGameGrade(teamStrengthPower);

        System.out.println("등급 : " + grade);


        /** 등장하는 몬스터의 레벨을 구한다 */
        int level = GameDifficultyGrade.decideMonsterExpLevel(grade, teamStrengthPower);

        System.out.println("몹 레벨 : " + level);

        /** 월드에 세팅 */
        this.monsterExpLevel = level;
        this.gameGrade = grade;


    }


    /**
     *
     */
    public void decideTeamElemental(){

        /** 인자로 건네줄 속성값 목록 구성 */
        ArrayList<Integer> elementalList = new ArrayList<>();
        for(CharacterEntity character : characterEntity.values()){
            elementalList.add( character.attribute);
        }

        /** 속성을 혼합한다 */
        int teamElemental = ElementalType.getMixedElemental(elementalList);

        //System.out.println("팀 속성 : " + teamElemental);

        /** 캐릭터들에 할당 */
        for(CharacterEntity character : characterEntity.values()){

            //character.setTeamElementalType(teamElemental);
            //character.setTeamElementalType(ElementalType.BLACK);
            character.attribute = teamElemental;
        }

    }




    /*******************************************************************************************************************/


    /**
     * 2020 04 14
     * 기    능 :
     *      -- 다음 웨이브에서 등장할 몬스터 마릿수를 랜덤으로 결정한다
     *
     * 공   식 :
     * 5개 웨이브를 단위로 함.
     *
     * 현 웨이브가 현 웨이브 % 5 == 1 일 때,
     * >> (현 웨이브 - ( 현 웨이브 % 10 - 1)) * 2;
     *
     * 현 웨이브 % 5 == 2 일 때,
     * >> (현 웨이브 - ( 현 웨이브 % 10 - 1)) * 2 ) *  0.8
     *
     * 현 웨이브 % 5 == 3 일 때,
     * >> (현 웨이브 - ( 현 웨이브 % 10 - 1)) * 2 ) *  1.2
     *
     * 현 웨이브 % 5 == 4 일 때,
     * >> (현 웨이브 - ( 현 웨이브 % 10 - 1)) * 2 ) *  0.6
     *
     * 현 웨이브 % 5 == 5 일 때,
     * >> (현 웨이브 - ( 현 웨이브 % 10 - 1)) * 2 ) *  1.4
     *
     */
    public int decideNextWaveMonsterCount(int waveCount){

        int entireMonsterCount = 0;

        int defaultMonsterCount = ( waveCount - ( waveCount % 10 -1) ) * 2;

        int waveCountNum = waveCount % 5;
        switch (waveCountNum){

            case 1:

                entireMonsterCount = defaultMonsterCount;
                break;

            case 2:

                entireMonsterCount = (int) Math.round(defaultMonsterCount * 0.8);
                break;

            case 3:

                entireMonsterCount = (int) Math.round(defaultMonsterCount * 1.2);
                break;

            case 4:

                entireMonsterCount = (int) Math.round(defaultMonsterCount * 0.6);
                break;

            case 0:

                entireMonsterCount = (int) Math.round(defaultMonsterCount * 1.4);
                break;

        }


        return entireMonsterCount;

    }


    /**
     *
     */
    public HashMap<Integer, Integer> decideWaveMonsterTypeByRandom(int waveCount, int totalMonsterCount){

        HashMap<Integer, Integer> waveArmyList = new HashMap<>();

        /* 웨이브 카운트에 따라, 등장하는 몬스터의 타입 범위가 달라진다 */
        // 아 이것도 나중에 파일로 만들어야겟지..
        int minTypeNum = 0;
        int maxTypeNum = 0;

        int waveCountNum  = waveCount % 5;
        switch (waveCountNum){

            case 1 :

                minTypeNum = 1;
                maxTypeNum = 6;
                break;
            case 2 :

                minTypeNum = 5;
                maxTypeNum = 11;
                break;
            case 3 :

                minTypeNum = 1;
                maxTypeNum = 15;
                break;
            case 4 :

                minTypeNum = 10;
                maxTypeNum = 15;
                break;
            case 0 :

                minTypeNum = 1;
                maxTypeNum = 15;
                break;

        }


        /* 총 마릿수만큼 반복한다 */
        int randomMobType = 0;
        for(int i=1; i<=totalMonsterCount; i++){

            //System.out.println(waveCount + "번째 웨이브의" + i + "번째 몬스터를 뽑습니다. ");

            /* 랜덤으로 몹 종류 뽑기를 수행 */
            randomMobType = (int)( (Math.random() * (maxTypeNum - minTypeNum) + 1) + minTypeNum );

            /* 몹리스트에 집어넣는다 */
            if(waveArmyList.containsKey(randomMobType)){

                /* 이미 뽑힌 적이 있는 몬스터라면 */
                int mobCount = waveArmyList.get(randomMobType);
                waveArmyList.put(randomMobType, ++mobCount);
            }
            else{

                /* 새로 등장한 몬스터라면 */
                waveArmyList.put(randomMobType, 1);
            }

        /*    System.out.println("몹 타입 " + randomMobType + "을 뽑았습니다., " +
                    "해당 타입 마릿 수 : " + waveArmyList.get(randomMobType) + "마리");
*/
        }


        return waveArmyList;
    }


    /*******************************************************************************************************************/
    /**
     * 2020 04 18 새벽 작성
     * 필요 데이터를 GDM으로부터 클론하여 사용하게끔, 초반에 미리 복사해두는 처리
     */

    /**
     * 기    능 : 월드맵에서 필요로 하는 데이터를, GDM에서 복사해온다.
     * 처    리 :
     *      WorldMap에서 필요로 하는 GDM 데이터는 다음과 같다
     *      -- 정글몹정보 목록
     *      -- 웨이브별 등장 몹 목록
     *
     */
    public void getNeedDataFromGDM(){

        /* 초기화 처리 */


        /* 정글몹 정보 목록을 복사한다 */
        bringJungleMobInfoListFromGDM();

        /* 웨이브별 등장 몹 목록을 복사한다 */
        bringWaveMonsterInfoListFromGDM();

    }

    public void bringJungleMobInfoListFromGDM(){

        HashMap<Integer, MonsterInfo> jungleMonsterInfoList = new HashMap<>();
        for( HashMap.Entry<Integer, MonsterInfo> jungleMonsterInfo : GameDataManager.jungleMonsterInfoList.entrySet()){

            int jungleKey = jungleMonsterInfo.getKey();
            MonsterInfo jungleValue = jungleMonsterInfo.getValue();
            jungleMonsterInfoList.put(jungleKey, jungleValue.clone());

        }

    }

    public void bringWaveMonsterInfoListFromGDM(){

        HashMap<Integer, HashMap<Integer, Integer>> waveArmyList = new HashMap<>();
        for( HashMap.Entry<Integer, HashMap<Integer, Integer>> waveArmy : GameDataManager.waveArmyList.entrySet()){

            int waveKey = waveArmy.getKey();

            HashMap<Integer, Integer> waveValue = new HashMap<>();
            for( HashMap.Entry<Integer, Integer> waveMob : waveArmy.getValue().entrySet()){

                int mobKey = waveMob.getKey();
                int mobValue = waveMob.getValue();

                waveValue.put(mobKey, mobValue);

            }

            waveArmyList.put(waveKey, waveValue);

        }

    }



}
