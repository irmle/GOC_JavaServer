package ECS.Game;


import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import Network.AutoCreatedClass.LoadingPlayerData;
import ECS.Classes.Type.CharacterType;
import Network.RMI_Classes.RMI_Context;
import Network.RMI_Classes.RMI_ID;
import Network.RMI_Common.server_to_client;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;

/**
 * 업뎃날짜 : 2020 03 16 월요일 권령희
 * 업뎃내용 :
 *      매칭이 완료된 후 캐릭터 픽이 시작되기 전에, 웹서버에 매칭유저들의 구글 닉네임을 요청한다.
 *      ㄴ 받아온 닉네임이 클라이언트 픽창에 반영되어, 각 매칭 유저들의 닉네임을 알 수 있게 함.
 *      ㄴ 게임(월드맵) 내 캐릭터들의 닉네임으로도 반영되게끔 함.
 *
 *      변경 지점 :
 *          GameSessionRoom 클래스 생성자 내부에서, LoadingPlayerInfo 라는.. 각 플레이어 정보를 구성하기 전에(for문 바로 위)
 *              -- 웹서버에 http 요청을 보내기 위한 클라이언트 생성 및 초기화 (매서드)
 *              -- 매칭 유저들의 닉네임 정보를 요청하는 리퀘스트 구성 및 요청 처리 (매서드)
 *              -- 받아온 응답을 파싱하여 <google tokenID, google nickName> 쌍의 값을 생성 ( 매서드 )
 *         이후 아래의 for문에서.. 위 토큰-닉네임 쌍 값을 읽으면서,
 *              플레이어 정보를 생성해 클라이언트들에 중계, 캐릭터픽 로직 수행을 시작
 *
 */



//캐릭터 픽이 진행되는 공간. 유저가 1명이라도 나간다면 방은 폭파됨 (닷지?)
public class GameSessionRoom {

    //이 시간동안 픽이 이루어 진다.
    public float remainCharacterPickTime = 60400f; //60초  ms단위.

    static Random ran = new Random();

    //게임세션룸 스레드
    Thread gameSessionThread;

    //GameSessionRoom이 유효한지 판별
    boolean isGameSessionEnd;

    //락킹용 모니터객체
    Object locking = new Object();

    //보이스채팅서버 호스트.
    RMI_ID voipHost = null;
    boolean isVoipHostReady = false;

    int worldMapID;

    //월드맵에 접속중인 유저의 TokenID & RMI_ID 목록.
    public HashMap<String, Boolean> isPlayerOnReady;
    public HashMap<String, RMI_ID> gameSessionRMI_IDList;
    public HashMap<String, LoadingPlayerData> gameSessionPlayerDataList;

    /* 2020 03 16 */
    // tokenID & (google) nickName 목록 ;
    public HashMap<String, String> gameSessionNickNameList;
    public AsyncHttpClient httpClient;

    //GameSessionRoom 생성자 부분. 이곳에서 픽창 로직이 시작된다.
    public GameSessionRoom(RMI_ID[] matchingList) {

        isGameSessionEnd = false;

        gameSessionThread = new Thread(gameSessionLogic);

        isPlayerOnReady = new HashMap<>();
        gameSessionRMI_IDList = new HashMap<>();
        gameSessionPlayerDataList = new HashMap<>();


        /**
         * 2020 03 16
         * 매칭된 유저들의 닉네임 정보를 웹서버에 요청
         */
        /*************************************************/

        /** HttpClient 생성 및 초기화 */
        initHttpClient();

        /** 유저들의 닉네임 정보를 요청 */

        /* 요청할 플레이어 정보(JS) 구성 */
        String requestPlayerInfoJS = getPlayerInfoForNicknameRequest(matchingList);
        System.out.println("플레이어 닉네임 정보 요청 JS : " + requestPlayerInfoJS);

        /* 닉네임 요청 생성 및 전송 */
        Response response = requestPlayerNickName(requestPlayerInfoJS);
        if(response == null){

            System.out.println("매칭된 유저의 닉네임 정보를 받아오는 데 실패했습니다.. 세션을 종료합니다.");
            // 나중에.. 여기서 뭐 다른 처리를 해주도록 해야할 듯. 단순 리턴 말고??
            return;

        }

        System.out.println("플레이어 닉네임 정보 받아오는 데 성공");

        System.out.println("닉네임 응답 내용 : ");
        System.out.println(response);


        /** 응답 정보를 파싱하여 닉네임 정보를 얻음 */
        gameSessionNickNameList = getPlayerNickNameList(response, matchingList);

        /***************************************************************************************************************/


        for (int i = 0; i < matchingList.length; i++) {
            RMI_ID rmi_id = matchingList[i];

            //2명 이상이고 맨 앞에 있는 유저를 VoipHost로 한다.
            if(i == 0 && matchingList.length > 1)
                voipHost = rmi_id;

            String tokenID = SessionManager.findTokenIDfromRMI_HostID(rmi_id.rmi_host_id);

            System.out.println("매칭리스트에서 넘어온 매칭유저 정보 RMI " + (i+1 )+ " : " + rmi_id);
            System.out.println("매칭리스트에서 넘어온 매칭유저 정보 토큰 " + (i+1 )+ " : " + tokenID);
            System.out.println("매칭리스트에서 넘어온 매칭유저 정보 닉네임 " + (i+1 )+ " : " + gameSessionNickNameList.get(tokenID));

            LoadingPlayerData loadingPlayerData = new LoadingPlayerData();
            loadingPlayerData.tokenID = tokenID;

            /* 2020 03 16 */
            //loadingPlayerData.characterName = "테스트 사용자 " + MatchingManager.playerCountNum.getAndIncrement(); // 테스트 사용자 10
            loadingPlayerData.characterName = gameSessionNickNameList.get(tokenID);

            loadingPlayerData.characterType = CharacterType.NONE;
            loadingPlayerData.currentProgressPercentage = 0f;

            //TokenID, boolean 리스트에 추가.
            isPlayerOnReady.put(tokenID, false);
            //TokenID, RMI_ID 리스트에 추가.
            gameSessionRMI_IDList.put(tokenID, rmi_id);
            //TokenID, LoadingPlayerData 리스트에 추가.
            gameSessionPlayerDataList.put(tokenID, loadingPlayerData);
        }

        //월드맵ID 발급.
         this.worldMapID = MatchingManager.worldMapIDGenerater.getAndIncrement();

        /**
         * 채팅관련 처리 추가
         * 1. 위에서 발급된 worldMapID를 채널번호 삼아, 매칭 인원들을 세션 채널에 등록하는 처리
         * 2. 게임 세션채널에 참가하게 되었음을 알리는 메시지를 생성, 유저들에게 통지
         * ㄴ .. 위 처리를 모두 진행하게끔 묶어놓은 매서드를, 채팅매니저에 만들어두는게 좋을라나..
         *      인자로 3명 유저 정보 주고말임.
         */






        //픽창에 진입하였음을 해당 유저들에게 중계함.
        server_to_client.pickLogicStart(getRMI_IDArray(), RMI_Context.Reliable_Public_AES256, new LinkedList<>(gameSessionPlayerDataList.values()));

        //로직 Thread 시작됨.
        gameSessionThread.start();
    }

    int getWorldMapID()
    {
        return this.worldMapID;
    }

    Runnable gameSessionLogic = new Runnable() {
        @Override
        public void run() {

            try {

                Thread.currentThread().sleep(600);

                //보이스채팅 호스트여부를 중계하는 부분.
                if(voipHost != null && isVoipHostReady == false)
                {
                    RMI_ID[] List = getRMI_IDArray();

                    for (int i = 0; i < List.length; i++) {
                        RMI_ID rmi_id = List[i];

                        if(rmi_id.rmi_host_id == voipHost.rmi_host_id)
                            server_to_client.pickLogicIsVoipHost(rmi_id, RMI_Context.Reliable_AES256, true, getWorldMapID());
                        else
                            server_to_client.pickLogicIsVoipHost(rmi_id, RMI_Context.Reliable_AES256, false, getWorldMapID());
                    }
                }


                //100ms 마다 반복
                while (!isGameSessionEnd) {

                    //System.out.println("remainCharacterPickTime="+remainCharacterPickTime);
                    //모든 플레이어가 Ready 중인지 체크하기 위한 변수.
                    boolean isAllUserReady = true;

                    //모든 플레이어의 Ready상태를 검사.
                    for (HashMap.Entry<String, Boolean> data : isPlayerOnReady.entrySet()) {
                        //1개라도 false라면, isAllUserReady는 false가 된다.

                        //isAllUserReady가 true인 경우에만 불린값을 그대로 대입. 1번이라도 false가 되면 모든 유저가 준비를 하지 않은것이다!
                        if(isAllUserReady)
                            isAllUserReady = data.getValue();
                    }

                    //모든 플레이어가 Ready 중이고, 10초이상 시간이 남았다면, 대기시간을 10초로 단축한다!
                    if (isAllUserReady == true && remainCharacterPickTime > 10000f)
                        remainCharacterPickTime = 2000f;

                    //500ms 간격으로 남은시간 중계
                    if (remainCharacterPickTime % 500 == 0) {
                        server_to_client.pickLogicTime(getRMI_IDArray(), RMI_Context.Reliable_Public_AES256, remainCharacterPickTime);
                    }

                    remainCharacterPickTime -= 100f;

                    Thread.currentThread().sleep(100);

                    if (remainCharacterPickTime <= 0f) //시간이 다되었다면 중단.
                    {
                        //픽창 프로세스가 종료되어 월드맵 할당 과정으로 넘어가는 부분.
                        if(!isGameSessionEnd) //매칭 프로세스가 종료된 상태라면 맵 생성을 하지 않는다.
                            endPickProcess();

                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            finally {
                //gameSession 클리어 하는부분
                clearGameSessionRoom();
            }
        }
    };


    //유저가 캐릭터를 터치하여 선택하였을 때. 같은 픽창 세션의 유저들에게 중계
    public void userOnSelectedCharacter(String tokenID, int characterType, int guardianID) {

        LoadingPlayerData loadingPlayerData = gameSessionPlayerDataList.get(tokenID);

        loadingPlayerData.guardianID = guardianID;

        System.out.println("캐릭터 타입 고른거 : " + characterType);
        switch (characterType) {
            case CharacterType.KNIGHT:
                loadingPlayerData.characterType = CharacterType.KNIGHT;
                break;
            case CharacterType.MAGICIAN:
                loadingPlayerData.characterType = CharacterType.MAGICIAN;
                break;
            case CharacterType.ARCHER:
                loadingPlayerData.characterType = CharacterType.ARCHER;
                break;
            default:
                throw new IllegalArgumentException("userOnSelectedCharacter() 중 Error 발생! 잘못된 characterType값!");
        }

        //중계
        server_to_client.pickLogicUserOnSelectedCharacter(getRMI_IDArray(), RMI_Context.Reliable_Public_AES256,
                tokenID, characterType);
    }

    //서버의 준비상태를 보고받는다.
    //만약 isVoipHostReady 값이 false로, 생성에 실패했다면 다른 클라이언트에게 서버 역할을 위임하게 한다.(예정)
    public void voiceChatServerOnReady(boolean isVoipHostReady)
    {
        if(voipHost == null)
            return;

        this.isVoipHostReady = isVoipHostReady;

        //음성채팅 서버 생성 성공시
        if(this.isVoipHostReady)
        {
            RMI_ID[] List = getRMI_IDArray();

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
            RMI_ID[] List = getRMI_IDArray();

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

    //유저가 준비완료 하였을 때. 같은 픽창 세션의 유저들에게 중계
    public void userOnReady(String tokenID) {
        if (isPlayerOnReady.containsKey(tokenID))
            isPlayerOnReady.put(tokenID, true);

        //중계
        server_to_client.pickLogicUserOnReady(getRMI_IDArray(), RMI_Context.Reliable_Public_AES256,
                tokenID);
    }

    //유저가 준비취소 하였을 때. 같은 픽창 세션의 유저들에게 중계
    public void userOnCancel(String tokenID) {
        if (isPlayerOnReady.containsKey(tokenID))
            isPlayerOnReady.put(tokenID, false);

        //중계
        server_to_client.pickLogicUserOnCancel(getRMI_IDArray(), RMI_Context.Reliable_Public_AES256,
                tokenID);
    }

    //유저가 채팅메시지를 보냈을 때.
    public void userOnChatMessageSend(String tokenID, String chatMessage) {
        LoadingPlayerData data = gameSessionPlayerDataList.get(tokenID);

        System.out.println("[" + data.characterName + "] " + chatMessage);
    }


    //유저의 연결이 끊겼을 때. 게임세션을 폭파하고 남은 유저들은 재매칭을 시작한다.
    //(픽 도중에 닷지, 연결해제, 게임종료등이 일어났을 경우.)
    public void userOnDisconnected(String tokenID, RMI_ID rmi_id) {
        isGameSessionEnd = true;
        remainCharacterPickTime = 999999999f;

        if (gameSessionRMI_IDList.containsKey(tokenID))
            gameSessionRMI_IDList.remove(tokenID);

        //모든 플레이어의 Ready상태를 검사.
        for (HashMap.Entry<String, LoadingPlayerData> data : gameSessionPlayerDataList.entrySet()) {
            //1개라도 false라면, isAllUserReady는 false가 된다.
            String alltokenID = data.getKey();
            //tokenID와 GameSessionRoom목록간의 연결을 해제!
            MatchingManager.removeGameSessionRoom(alltokenID);
        }

        //남은유저들에게 픽창 화면에서 다시 로비의 매칭화면으로 돌아가게끔 중계한다. (자동 매칭 시작)
        server_to_client.pickLogicUserOnDisconnected(getRMI_IDArray(), RMI_Context.Reliable_Public_AES256,"");

        //남은 유저들은 다시 매칭을 진행하게끔 자동 재매칭 진행.
        MatchingManager.reStartMatching(gameSessionRMI_IDList.entrySet());
    }

    //gameSession 클리어 하는부분
    void clearGameSessionRoom() {

        isPlayerOnReady.clear();
        gameSessionRMI_IDList.clear();
        gameSessionPlayerDataList.clear();

        voipHost = null;
    }




    //타임이 경과되어 정상적으로 픽창이 끝났을 때. 월드맵을 생성하고, 선택된 캐릭터정보를 가지고 캐릭터를 생성하도록 한다.
    //캐릭터를 선택하지 않은 상태라면 Random으로 type을 정하고, 시간내에 Ready를 하지 않은 유저들도 강제Ready를 하게 한다.
    void endPickProcess() {

        int count = 0;
        RMI_ID[] rmi_ids = new RMI_ID[gameSessionPlayerDataList.size()];
        LoadingPlayerData[] loadingPlayerDatas = new LoadingPlayerData[gameSessionPlayerDataList.size()];

        //모든 플레이어의 Ready상태를 검사.
        for (HashMap.Entry<String, LoadingPlayerData> data : gameSessionPlayerDataList.entrySet()) {
            //1개라도 false라면, isAllUserReady는 false가 된다.
            String tokenID = data.getKey();
            LoadingPlayerData loadingPlayerData = data.getValue();

            //캐릭터 타입이 정해지지 않았다면 Random으로 Type을 정한다.
            if (loadingPlayerData.characterType == CharacterType.NONE) {
                loadingPlayerData.characterType = ran.nextInt(3) + 1;
            }

            rmi_ids[count] = gameSessionRMI_IDList.get(tokenID);
            loadingPlayerDatas[count] = loadingPlayerData;

            //tokenID와 GameSessionRoom목록간의 연결을 해제!
            MatchingManager.removeGameSessionRoom(tokenID);

            count++;
        }

        //픽이 완료되었으므로 월드맵 생성!
        generateNewWorldMap(rmi_ids, loadingPlayerDatas);
    }

    //픽이 정상적으로 종료되면 실행되는 부분.
    void generateNewWorldMap(RMI_ID[] matchingList, LoadingPlayerData[] matchingUserDataList) {
        //반복횟수
        int count = 0;

        //매칭 HashMap 반복용 Iterator.
        Iterator<String> keys = gameSessionRMI_IDList.keySet().iterator();


        //System.out.println("매칭 월드 : " + getWorldMapID);

        //매칭된 유저수만큼 반복
        while (keys.hasNext() && count < MatchingManager.userCount) {
            //Key를 가져옴
            String key = keys.next();

            //유저의 토큰값과 유저가 할당받은 WorldMapID값을 매핑한다.
            MatchingManager.playerWorldMapMappingList.put(key, this.worldMapID);

            //이후 Iterator.remove()를 이용하여 매칭 HashMap에서 제거한다.
            keys.remove();

            //1회 행하였으므로 다음 숫자로 카운트.
            count++;
        }

        //할당된 유저들과 함께, 새로운 월드맵을 생성한다. 동시에 유저들에게 픽이 종료되어 로딩 Scene을 불러오라고 중계함.
        WorldMap newMap = new WorldMap(this.worldMapID, matchingList, matchingUserDataList, voipHost);

        //생성된 월드맵을 월드맵 리스트에 삽입한다.
        MatchingManager.worldMapList.put(this.worldMapID, newMap);
        //System.out.println("새 월드맵 ID : " + newMap.worldMapID);
    }

    public RMI_ID[] getRMI_IDArray() {
        return RMI_ID.getArray(gameSessionRMI_IDList.values());
    }

    /**
     * 2020 03 16
     */
    /**********************************************************/


    public void initHttpClient(){

        httpClient = Dsl.asyncHttpClient();

    }

    public String getPlayerInfoForNicknameRequest(RMI_ID[] matchingList){

        String playerInfoJS = null;

        Gson gson = new Gson();
        JsonObject playerInfo = new JsonObject();
        playerInfo.addProperty("count", matchingList.length);

        /* 플레이어 */
        int userNum = 0;
        JsonObject player;

        for(int i=0; i<matchingList.length; i++){

            userNum ++;

            RMI_ID rmi_id = matchingList[i];
            String tokenID = SessionManager.findTokenIDfromRMI_HostID(rmi_id.rmi_host_id);

            player = new JsonObject();
            player.addProperty("googleToken", tokenID);

            String numStr = userNum + "";
            playerInfo.add(numStr, player);

        }

        playerInfoJS = gson.toJson(playerInfo);
        System.out.println("플레이어 정보 요청을 위한 최종 JS : " + playerInfoJS);

        return playerInfoJS;
    }

    public Response RQ_getPlayerNickInfo(String playerRequestInfo){

        Response response = null;
        //String ipAddr = "http://112.221.220.205/result/getnick.php";
        String ipAddr = "http://ngnl.xyz/result/getnick.php";

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
            response = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            return response;
        }

    }

    public Response requestPlayerNickName(String playerRequestInfo){

        Response response = null;

        int requestCount = 0;
        final int MAX_RQ_COUNT = 5;

        boolean requestSucceed = false;
        while(true){

            if(requestCount == MAX_RQ_COUNT){

                response = null;

                System.out.println("요청 횟수가 5회에 도달했습니다.");
                break;
            }

            requestCount++;

            response = RQ_getPlayerNickInfo(playerRequestInfo);   // 요청을 보내고, 응답이 올 때까지 기다림.
            if((response != null) && response.getStatusCode() == HttpConstants.ResponseStatusCodes.OK_200){

                /** 응답을 성공적으로 받았을 때의 처리를 작성 */
                System.out.println("플레이어 닉네임 정보를 성공적으로 받음");

                requestSucceed = true;

                break;
            }
            else{
                /** 실패?했을 때의 처리를 작성 */

                System.out.println("캐릭터 정보를 받아오는 데 실패..");
            }

        }


        return response;

    }

    public HashMap<String, String> getPlayerNickNameList(Response response, RMI_ID[] matchingList ) {

        HashMap<String, String> nickNameList = new HashMap<>();

        String playerInfoString = response.getResponseBody().toString();

        int index = playerInfoString.indexOf("{");
        String testStr = playerInfoString.substring(index);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(testStr);
        JsonObject playerInfoJSO = jsonElement.getAsJsonObject();

        if(false) {
            System.out.println("Element : " + jsonElement);
            System.out.println("obj : " + playerInfoJSO);
        }

        for(int i=0; i<matchingList.length; i++){

            // 토큰
            //RMI_ID rmi_id = matchingList[i];
            //String tokenID = SessionManager.findTokenIDfromRMI_HostID(rmi_id.rmi_host_id);

            // 닉넴 파싱
            /*String userStr = (i+1) + "";
            JsonObject infoJSO = playerInfoJSO.getAsJsonObject(userStr);
            String nickname = infoJSO.getAsJsonObject("userInfo").get("nickName").getAsString();*/


            /* 2020 03 17 화요일 구글토큰 관련 수정 권령희 */
            String userStr = (i+1) + "";
            JsonObject infoJSO = playerInfoJSO.getAsJsonObject(userStr);

            String tokenID = infoJSO.getAsJsonObject("userInfo").get("googleToken").getAsString();
            String nickname = infoJSO.getAsJsonObject("userInfo").get("nickName").getAsString();

            nickNameList.put(tokenID, nickname);

        }

        return nickNameList;
    }



    /**********************************************************/
}
