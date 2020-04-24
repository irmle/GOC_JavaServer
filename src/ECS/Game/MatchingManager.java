package ECS.Game;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import RMI.RMI_Common.server_to_client;
import RMI.RMI_Classes.*;
import sun.awt.image.ImageWatched;

import javax.security.auth.callback.LanguageCallback;


//매치메이킹을 담당하는 클래스
public class MatchingManager {

    //한 방에 들어갈 수 있는 유저수.
    public static int userCount = 3;

    //
    public static float waitTime = 5f;


    //매칭용 tokenID(String), RMI_ID 목록정보.
    static LinkedHashMap<String, RMI_ID> matchingList;

    static LinkedHashMap<String, Float> matchingWaitTime;

    //매칭용 tokenID(String), GameSessionRoom 목록정보.
    static HashMap<String, GameSessionRoom> gameSessionList;

    //tokenID(String), worldMapID(int) 목록정보.  //tokenID를 활용하여 해당 유저가 속해있는 WorldMapID를 얻을 수 있음.
    static HashMap<String, Integer> playerWorldMapMappingList;

    //worldMapID(int), worldMap(WorldMap) 목록정보.
    static HashMap<Integer, WorldMap> worldMapList;


    //매칭 전용 스레드 및, 스레드 잠금용 모니터객체Object
    static Thread matchingThread;
    static Object locking;

    //고유한 월드맵ID를 할당하기 위한 AtomicInteger.
    static AtomicInteger worldMapIDGenerater;


    //캐릭터 카운트를 위한 번호
    static AtomicInteger playerCountNum = new AtomicInteger(1);

    //MatchingManager 초기화 메소드. 생성자 느낌.
    public static void initMatchingManager() {
        System.out.println("MatchingManager 초기화중...");

        //각종 변수 초기화
        locking = new Object();
        matchingList = new LinkedHashMap<>();
        matchingWaitTime = new LinkedHashMap<>();
        gameSessionList = new HashMap<>();
        playerWorldMapMappingList = new HashMap<>();
        worldMapList = new HashMap<>();

        //월드맵ID 생성기.
        worldMapIDGenerater = new AtomicInteger(1);

        //매칭 전용 스레드 할당
        matchingThread = new Thread(matchingLogic);
        matchingThread.start();

        System.out.println("MatchingManager 초기화 완료");
    }


    //주기적으로 매칭이 되었는지 확인할 용도.
    static Runnable matchingLogic = new Runnable() {
        @Override
        public void run() {

            while (true) {
                try {
                    //매칭이된 유저의 배열을 세팅한다.
                    if (checkMatchingUserCount()) {
                        System.out.println("매칭 성공. 캐릭터 픽창 진입.");
                    }

                    /**
                     * 2020 04 23 여기다가 추가하면 될 것 같은데.. 매칭 대기 인원 넣어주는거.
                     */




                    /* 시간 */
                    for(HashMap.Entry<String, Float> waitTimeEntry: matchingWaitTime.entrySet()){

                        float remainedWaitTime = waitTimeEntry.getValue();
                        remainedWaitTime -= 1f;

                        matchingWaitTime.put(waitTimeEntry.getKey(), remainedWaitTime);

                    }

                    //1초간 대기
                    Thread.currentThread().sleep(1000);


                } catch (Exception e) {

                }
            }
        }
    };

    //픽창 전용 로직. tokenID로부터 GameSessionRoom를 가져온다.
    public static GameSessionRoom findGameSessionRoomFromTokenID(String tokenID) {

        if (gameSessionList.containsKey(tokenID)) {

            GameSessionRoom findedGameSessionRoom = gameSessionList.get(tokenID);

            return findedGameSessionRoom;
        } else
            return null;
    }

    //세션ID의 계정이 플레이 중이었던 월드맵을 검색.
    //null이 반환되었다면, 새로운 대기열에 삽입.
    public static WorldMap findWorldMapFromWorldMapID(int worldMapID) {

        if (worldMapList.containsKey(worldMapID)) {

            WorldMap findedWorldMap = worldMapList.get(worldMapID);

            return findedWorldMap;
        } else
            return null;
    }

    //토큰ID로 현재 플레이중인 월드맵을 검색한다.
    public static WorldMap findWorldMapFromTokenID(String tokenID) {
        if (playerWorldMapMappingList.containsKey(tokenID)) {
            int worldMapID = playerWorldMapMappingList.get(tokenID);

            return findWorldMapFromWorldMapID(worldMapID);
        } else
            return null;
    }

    //현재 유저가 플레이중인 맵이 있는지 판단.
    public static WorldMap isUserPlayingGame(String tokenID) {
        if (playerWorldMapMappingList.containsKey(tokenID)) {
            int worldMapID = playerWorldMapMappingList.get(tokenID);
            WorldMap result = worldMapList.get(worldMapID);
            return result;
        } else
            return null;
    }

    //GameSessionRoom (닷지나 연결해제로 인한 방폭) 으로 다시 재매칭을 할때 사용됨.
    public static void reStartMatching(Set<HashMap.Entry<String, RMI_ID>> reStartData) {
        synchronized (locking) {
            for (HashMap.Entry<String, RMI_ID> data : reStartData) {
                String tokenID = data.getKey();
                RMI_ID rmi_id = data.getValue();

                if (!matchingList.containsKey(tokenID)){
                    matchingList.put(tokenID, rmi_id);
                    matchingWaitTime.put(tokenID, waitTime);
                }

            }
        }
    }

    //매칭 시작시
    public static void startMatching(String tokenID, RMI_ID rmi_id) {
        synchronized (locking) {
            //매칭중인 리스트에 없어야 하고, 현재 매칭이 되어서 worldMap에 들어간 상태도 아니여야 한다.
            if (!matchingList.containsKey(tokenID)) {
                //현재 픽중이거나, 게임중인 tokenID가 아니여야 한다.
                if (!playerWorldMapMappingList.containsKey(tokenID) && !gameSessionList.containsKey(tokenID)){
                    matchingList.put(tokenID, rmi_id);
                    matchingWaitTime.put(tokenID, waitTime);
                }
                else {
                    System.out.println("startMatching 요청이 있었으나 불가능한 상태!");
                    System.out.println(playerWorldMapMappingList.containsKey(tokenID) + " / " + gameSessionList.containsKey(tokenID));
                }

            }
        }
    }

    //매칭 취소시 (또는 매칭 도중 접속 끊김시)
    public static void cancelMatching(String tokenID) {
        synchronized (locking) {
            if (matchingList.containsKey(tokenID)){
                matchingList.remove(tokenID);
                matchingWaitTime.remove(tokenID);
            }
        }
    }

    /**
     * 2020 04 13 수정
     * 매칭 가능 여부를 판별하는 매서드를 별도로 만들었음.
     * @return
     */
    //매칭여부 체크. 인원수가 조건을 만족하면 해당 유저들을 매칭시켜 새로운 WorldMap으로 밀어넣는다.
    static boolean checkMatchingUserCount() {
        synchronized (locking) {
            //매칭이 될 조건을 만족하는 경우.
            /*if (matchingList.size() >= userCount) {*/
            if(checkMatchingCondition()){

                System.out.println("매칭 조건이 만족되어 세션을 생성합니다.");

                //매칭이된 유저의 배열을 세팅한다.
                //RMI_ID[] matchedUserList = new RMI_ID[userCount];

                RMI_ID[] matchedUserList;
                if(matchingList.size() >= userCount){
                    matchedUserList = new RMI_ID[userCount];
                }
                else{
                    matchedUserList = new RMI_ID[matchingList.size()];
                }

                try {

                    //반복횟수
                    int count = 0;
                    //매칭 HashMap 반복용 Iterator.
                    Iterator<String> keys = matchingList.keySet().iterator();

                    //매칭된 유저수만큼 반복
                    while (keys.hasNext() && count < userCount) {
                        //Key를 가져옴
                        String key = keys.next();

                        //매칭리스트로부터 유저의 RMI_ID를 가져옴
                        matchedUserList[count] = matchingList.get(key);

                        //이후 Iterator.remove()를 이용하여 매칭 HashMap에서 제거한다.
                        keys.remove();
                        matchingWaitTime.remove(key);

                        //1회 행하였으므로 다음 숫자로 카운트.
                        count++;
                    }

                    //픽창으로 이동하도록 한다.
                    GameSessionRoom gameSessionRoom = new GameSessionRoom(matchedUserList);

                    for (int i = 0; i < matchedUserList.length; i++) {
                        String tokenID = SessionManager.findTokenIDfromRMI_HostID(matchedUserList[i].rmi_host_id);
                        gameSessionList.put(tokenID, gameSessionRoom);
                    }

                    //매칭이 완료되었음을 반환한다.
                    return true;
                } catch (Exception e) {
                    System.out.println("checkMatchingUserCount() 매칭중 에러발생! \n" + e);

                    //errorCode 임시값.
                    //0 = otherException, 100 = NullPointerException
                    if (e instanceof NullPointerException) {
                        //매칭중 에러가 발생하였음을 전송한다.
                        server_to_client.errorMatching(matchedUserList, RMI_Context.Reliable_Public_AES256, 100, e.toString());
                    } else {
                        //매칭중 에러가 발생하였음을 전송한다.
                        server_to_client.errorMatching(matchedUserList, RMI_Context.Reliable_Public_AES256, 0, e.toString());
                    }
                    return false;
                }
            } else
                return false;
        }
    }


    /**
     * 2020 04 13 월요일
     * 매칭 가능 조건을 체크한다
     * 조건 :
     *  1) 인원이 3인이상 모였음
     *  2) 3인 이하일 때, 매칭 대기자 중 대기 시간이 NN초 이상 지났다면
     *      남은 인원들끼리 매칭되게끔 함.
     *
     * @return
     */
    public static boolean checkMatchingCondition(){

        boolean ableToMatch = false;
        if(matchingList.size() >= userCount){

           ableToMatch = true;

            System.out.println("매칭 인원이 모였습니다.");
        }
        else{

            if(matchingList.size() > 0) {

                Iterator<String> keys = matchingList.keySet().iterator();

                float remainedWaitingTime = matchingWaitTime.get(keys.next());
                if(remainedWaitingTime <= 0f){

                    ableToMatch = true;

                    System.out.println("매칭 대기 시간이 종료되었습니다.");
                }
                else{

                    /*System.out.println("남은 대기시간 : " + remainedWaitingTime);*/
                }

            }


        }

        return ableToMatch;
    }



    public static void removeGameSessionRoom(String tokenID) {
        if (gameSessionList.containsKey(tokenID))
            gameSessionList.remove(tokenID);
    }


    //종료된 월드맵을 목록에서 제거하는 메소드.
    public static void removeWorldMap(WorldMap removeWorldmap) {
        //매칭 매니저에 존재하는 해시맵들에서 종료된 월드맵 관련 항목들을 제거하는 부분.


        //tokenID(String), worldMapID(int) 목록정보.
        //playerWorldMapMappingList;

        //worldMapID(int), worldMap(WorldMap) 목록정보.
        //worldMapList;

        //월드맵의 게임이 종료되어 월드맵의 게임로직스레드가 종료된다면,
        //종료된 월드맵을 활성화된 월드맵 리스트에서 제거하고
        //종료된 월드맵과 연결된 토큰/월드맵id 매핑리스트에서 종료된 월드맵에 참여중이었던 유저들을 제거한다.
        //이로서 해당 유저들은 다시 새로운 맵에 참가 가능한 상태가 된다.


        //월드맵에 접속중인 User들의 tokenID를 전부 가져온다. 그 후, 월드맵이 제거됨에 따라
        //해당 유저들의 매핑정보도 삭제한다.
        Iterator<String> tokenIDs = removeWorldmap.worldMapTokenIDList.keySet().iterator();

        while (tokenIDs.hasNext()) {
            String tokenID = tokenIDs.next();

            playerWorldMapMappingList.remove(tokenID);
        }

        //활성화 중인 월드맵 리스트에서 해당 월드맵을 제거한다.
        worldMapList.remove(removeWorldmap.worldMapID);

        /*//월드맵이 종료되었으므로 월드맵에 접속중인 유저의 접속 종료!
        RMI_ID[] END_LIST = RMI_ID.getArray(removeWorldmap.worldMapRMI_IDList.values());
        for(int i=0;i<END_LIST.length;i++)
        {
            END_LIST[i].getTCP_Object().close();
        }*/

        removeWorldmap.worldMapRMI_IDList.clear();
    }

}
