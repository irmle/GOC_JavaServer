package ECS.Chatting;

import ECS.Chatting.Classes.ChattingUser;
import ECS.Chatting.Type.ChannelType;
import ECS.Chatting.Type.MessageType;
import ECS.Game.GameSessionRoom;
import ECS.Game.SessionManager;
import Network.AutoCreatedClass.MessageData;
import Network.RMI_Classes.RMI_ID;
import Network.RMI_Common.server_to_client;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;

import java.io.CharArrayReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ChattingManager {

    /** 멤버 변수 */

    /* 접속자 관리 */
    public static LinkedList<ChattingUser> chattingUserList;
    public static HashMap<Integer, ChattingUser> chattingUserMap;  // < rmi_host_id, chattingUser >


    /* 채널 목록 */
    public static HashMap<Integer, LinkedList<ChattingUser>> lobbyChannelMap;      // 로비 채널 목록 ( 1 ~ 9999 )
    public static HashMap<Integer, LinkedList<ChattingUser>> sessionChannelMap;    // 세션 채널 목록 (생성된 게임월드 갯수만큼 )
    public static HashMap<Integer, LinkedList<ChattingUser>> guildChannelMap;      // 길드 채널 목록 ( 존재하는 길드 갯수만큼 )

    //동기화 제어용 모니터객체
    final Object locking = new Object();

    public static AsyncHttpClient httpClient;


    /** 생성자 & 초기화 매서드 */
    public static void initChattingManager(){

        System.out.println("ChattingManager 초기화중...");

        /* 접속자 목록 초기화 */
        chattingUserList = new LinkedList<>();
        chattingUserMap = new HashMap<>();

        /* 채널 목록 초기화 */
        lobbyChannelMap = new HashMap<>();
        sessionChannelMap = new HashMap<>();
        guildChannelMap = new HashMap<>();


        // 그 외 추가 초기화 필요한 경우 작업 작성
        //initChattingManager();

        initHttpClient();

        System.out.println("ChattingManager 초기화 완료");

    }




    /** 매서드 */

    public static void initHttpClient(){

        httpClient = Dsl.asyncHttpClient();

    }

    public static String getPlayerInfoForNicknameRequest(RMI_ID accessPlayer){

        String playerInfoJS = null;

        Gson gson = new Gson();
        JsonObject playerInfo = new JsonObject();
        playerInfo.addProperty("count", 1); // 2020 05 14.. 플레이어 하나의 정보를 요청함.

        /** 플레이어 Json 정보를 구성 */
        JsonObject player;

        String tokenID = SessionManager.findTokenIDfromRMI_HostID(accessPlayer.rmi_host_id);

        player = new JsonObject();
        player.addProperty("googleToken", tokenID);

        int userNum = 1;
        String numStr = userNum + "";
        playerInfo.add(numStr, player);

        playerInfoJS = gson.toJson(playerInfo);
        System.out.println("접속자 정보 요청을 위한 최종 JS : " + playerInfoJS);


        return playerInfoJS;
    }

    public static Response RQ_getPlayerNickInfo(String playerRequestInfo){

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

    public static Response requestPlayerNickName(String playerRequestInfo){

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

                System.out.println("닉넴 정보를 받아오는 데 실패..");
            }

        }


        return response;

    }

    public static void parseAndSetUserInfo(Response response, ChattingUser user) {

        String playerInfoString = response.getResponseBody().toString();

        int index = playerInfoString.indexOf("{");
        String testStr = playerInfoString.substring(index);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(testStr);
        JsonObject playerInfoJSO = jsonElement.getAsJsonObject();


        /* 2020 03 17 화요일 구글토큰 관련 수정 권령희 */
        String userStr = 1 + "";
        JsonObject infoJSO = playerInfoJSO.getAsJsonObject(userStr);

        String tokenID = infoJSO.getAsJsonObject("userInfo").get("googleToken").getAsString();
        String nickname = infoJSO.getAsJsonObject("userInfo").get("nickName").getAsString();
        int userToken = infoJSO.getAsJsonObject("userInfo").get("userToken").getAsInt();

        user.setNickName(nickname);
        user.setUserToken(userToken);

        return;
    }


    public static void broadcastChatMsg_Lobby(ChattingUser user, String Message){

        // 유저가 속한 채널을 찾음
        LinkedList<ChattingUser> channel = lobbyChannelMap.get(user.lobbyChannelNum);


        RMI_ID[] targetToBroadcast = new RMI_ID[channel.size()];
        for(int i=0; i<targetToBroadcast.length; i++){
        //for (ChattingUser channelMember : channel){

            // 머.. RMI_ID 하나를 대상으로? 혹은 여럿을 대상으로 하는 RMI broadcasting 매서드를 호출하면 되겟지.
            targetToBroadcast[i] = channel.get(i).rmi_id;
        }



    }

    public static void broadcastChatMsg_Session(ChattingUser user, String Message){

        // 유저가 속한 채널을 찾음
        LinkedList<ChattingUser> channel = sessionChannelMap.get(user.sessionChannelNum);

        for (ChattingUser channelMember : channel){

            // 머.. RMI_ID 하나를 대상으로? 혹은 여럿을 대상으로 하는 RMI broadcasting 매서드를 호출하면 되겟지.

        }

    }

    public static void broadcastChatMsg_Guild(ChattingUser user, String Message){

        // 유저가 속한 채널을 찾음
        LinkedList<ChattingUser> channel = guildChannelMap.get(user.guildChannelNum);

        for (ChattingUser channelMember : channel){

            // 머.. RMI_ID 하나를 대상으로? 혹은 여럿을 대상으로 하는 RMI broadcasting 매서드를 호출하면 되겟지.

        }

    }


    /**
     * 메시지(패킷) 타입에 맞춰서.. 위 브로드캐스트를 호출하거나.. 하는 매서드 필요할수도
     * 아님 걍 각 메시지 타입별 RMI 매서드 내부에서 위 함수들을 호출하던지.
     *
     */



    /**
     * 접속유저 생성..
     */
    public static ChattingUser joinChattingServer(RMI_ID rmi_id){


        /** 1. 접속자 관리를 위한 객체를 하나 생성한다 */
        ChattingUser newUser = new ChattingUser(rmi_id);


        /** 2. 유저의 닉네임 정보를 요청 */

        /* 요청할 플레이어 정보(JS) 구성 */
        String requestPlayerInfoJS = getPlayerInfoForNicknameRequest(rmi_id);

        /* 닉네임 요청 생성 및 전송 */
        Response response = requestPlayerNickName(requestPlayerInfoJS);
        if(response == null){

            System.out.println("유저의 닉네임 정보를 받아오는 데 실패했습니다.. 세션을 종료합니다.");
            return null;
        }

        System.out.println("플레이어 닉네임 정보 받아오는 데 성공");

        /* 응답 정보를 파싱하여 닉네임 정보를 얻음, 세팅 */
        parseAndSetUserInfo(response, newUser);

        /* 접속자 목록에 집어넣음 */
        chattingUserList.add(newUser);

        /***************************************************************************************************************/


        /** 3. 로비 채널 번호를 할당한다 */

        int lobbyChannelNum = allocChannelNum();

        /** 4. 해당 로비 채널에 가입 */
        joinLobbyChannel(lobbyChannelNum, newUser);



        return  newUser;

    }


    /**
     * 유저가 접속해있는 채널 전부 해제.
     */
    public void leaveChattingServer(RMI_ID rmi_id){

        ChattingUser leaveUser = chattingUserMap.get(rmi_id);

        leaveLobbyChannel(leaveUser);
        leaveSessionChannel(leaveUser);
        leaveGuildChannel(leaveUser);

        chattingUserMap.remove(rmi_id);
        chattingUserList.remove(leaveUser);

    }

    public static void leaveLobbyChannel(ChattingUser user){

        LinkedList<ChattingUser> channel = lobbyChannelMap.get(user.lobbyChannelNum);

        if(channel.contains(user)){
            channel.remove(user);
        }

        user.setLobbyChannelNum(-1);

    }

    public static void leaveSessionChannel(ChattingUser user){

        LinkedList<ChattingUser> channel = sessionChannelMap.get(user.lobbyChannelNum);

        if(channel.contains(user)){
            channel.remove(user);
        }

        user.setSessionChannelNum(-1);

    }

    public static void leaveGuildChannel(ChattingUser user){

        LinkedList<ChattingUser> channel = guildChannelMap.get(user.lobbyChannelNum);

        if(channel.contains(user)){
            channel.remove(user);
        }

        user.setGuildChannelNum(-1);
    }



    public static void joinLobbyChannel(int channelNum, ChattingUser user){

        LinkedList<ChattingUser> channelMemberList = getChannel(ChannelType.LOBBY, channelNum);

        channelMemberList.add(user);

        user.setLobbyChannelNum(channelNum);

    }

    public static void joinSessionChannel(int channelNum, ChattingUser user){

        LinkedList<ChattingUser> channelMemberList = getChannel(ChannelType.SESSION, channelNum);

        channelMemberList.add(user);

        user.setLobbyChannelNum(channelNum);

    }

    public static void joinGuildChannel(int channelNum, ChattingUser user){

        LinkedList<ChattingUser> channelMemberList = getChannel(ChannelType.GUILD, channelNum);

        channelMemberList.add(user);

        user.setLobbyChannelNum(channelNum);

    }


    /**
     *
     */
    public static ChattingUser searchUser(RMI_ID rmi_id){

        ChattingUser wantUser = null;
        for (ChattingUser chattingUser : chattingUserList){

            if (chattingUser.rmi_id == rmi_id){
                wantUser = chattingUser;
                break;
            }
        }

        return wantUser;

    }




    /**
     * 채널(로비) 번호 할당 매서드 ( 막 접속한 유저에게 채널 할당 해주는거임 )
     */
    public static int allocChannelNum(){

        long seedValue = System.currentTimeMillis();

        int channelValue = (int)(seedValue % 10000) + 1;

        return channelValue;

    }


    /**
     *
     * 채널에 가입시키기. 채널이 존재하지 않으면, 만들어서라도 가입시킨다
     */
    public void joinChannel(int channelType, int channelNum, ChattingUser chattingUser){

        LinkedList<ChattingUser> channelMemberList = getChannel(channelType, channelNum);

        switch (channelType){

            case ChannelType.LOBBY :

                chattingUser.setLobbyChannelNum(channelNum);
                break;

            case ChannelType.SESSION :

                chattingUser.setSessionChannelNum(channelNum);
                break;

            case ChannelType.GUILD :

                chattingUser.setGuildChannelNum(channelNum);
                break;

        }


        channelMemberList.add(chattingUser);

    }

    /**
     * 채널 (정확히는 채널 가입자 목록) 획득
     * @param channelType
     * @param channelNum
     * @return
     */
    public static LinkedList<ChattingUser> getChannel(int channelType, int channelNum){

        LinkedList<ChattingUser> channelMemberList = null;

        boolean isExistChannel;
        switch (channelType){

            case ChannelType.LOBBY :

                isExistChannel = lobbyChannelMap.containsKey(channelNum);
                if( !isExistChannel){

                    createNewChannel(channelType, channelNum);
                }
                channelMemberList = lobbyChannelMap.get(channelNum);

                break;

            case ChannelType.SESSION :

                isExistChannel = sessionChannelMap.containsKey(channelNum);
                if( !isExistChannel){

                    createNewChannel(channelType, channelNum);
                }
                channelMemberList = sessionChannelMap.get(channelNum);

                break;

            case ChannelType.GUILD :

                isExistChannel = guildChannelMap.containsKey(channelNum);
                if( !isExistChannel){

                    createNewChannel(channelType, channelNum);
                }
                channelMemberList = guildChannelMap.get(channelNum);

                break;

        }


        return channelMemberList;


    }



    /**
     * 채널 변경 매서드
     * 업뎃날짜 : 2020 06 09 화요일 오후
     * 처리내용 :
     *      1. 채널 변경 처리를 수행
     *          1) 변경 전 채널 번호를 유지한다
     *          2) 기존 채널을 떠난다
     *          3) 새 채널에 가입한다
     *          4) 새 채널 번호를 얻는다
     *
     *      2. 채널 변경에 대한 로그 메시지 생성 및 로그 쌓기
     *          // 로그메시지 생성 및 로그 쓰기 처리는 다음에 작성할 것.
     *
     *      3. 채널 변경 성공(?) 중계 메시지를 생성해 리턴함
     *          // 이게 실패하는 케이스는 없겠지..
     *          // '채널변경' 타입의 메시지를 생성하는 처리를, 메시지생성 매서드에 추가해야 함.
     */
    public static MessageData changeChannel(ChattingUser chattingUser, int newChannelNum){

        /** 채널 변경 처리 */

        int oldChannelNum = chattingUser.getLobbyChannelNum();

        leaveLobbyChannel(chattingUser);
        joinLobbyChannel(newChannelNum, chattingUser);

        int currentChannelNum = chattingUser.getLobbyChannelNum();



        /** "채널 변경" 로그 메시지 생성 */




        /** 중계 메시지 생성 */

        MessageData channelChangedMsg = createMessageData(MessageType.LOBBY_CHANGE_CHANNEL, chattingUser);



        return channelChangedMsg;

    }


    /**
     * 채널이 비어있는지 확인. ( 접속자가 아무도 없으면, 목록에서 해제?.. 그대로 냅둘수도.)
     */
    public boolean isEmptyChannel(int channelType, int channelNum){

        boolean isEmpty = false;

        switch (channelType){

            case ChannelType.LOBBY :

                isEmpty = lobbyChannelMap.get(channelNum).isEmpty();
                break;

            case ChannelType.SESSION :

                isEmpty = sessionChannelMap.get(channelNum).isEmpty();
                break;

            case ChannelType.GUILD :

                isEmpty = guildChannelMap.get(channelNum).isEmpty();
                break;

        }

        return isEmpty;

    }



    /**
     * 채널 생성 매서드
     */
    public static void createNewChannel(int channelType, int channelNum){

        LinkedList<ChattingUser> chattingUserList = new LinkedList<>();

        switch (channelType){

            case ChannelType.LOBBY :

                lobbyChannelMap.put(channelNum, chattingUserList);
                break;

            case ChannelType.SESSION :

                sessionChannelMap.put(channelNum, chattingUserList);
                break;

            case ChannelType.GUILD :

                guildChannelMap.put(channelNum, chattingUserList);
                break;

            default:

                break;


        }

    }


    /**
     * 채널 해제 매서드
     */
    public void releaseEmptyChannel(int channelType, int channelNum){

        switch (channelType){

            case ChannelType.LOBBY :

                lobbyChannelMap.remove(channelNum);
                break;

            case ChannelType.SESSION :

                sessionChannelMap.remove(channelNum);
                break;

            case ChannelType.GUILD :

                guildChannelMap.remove(channelNum);
                break;

            default:

                break;

        }

    }




    /**
     * 유저정보(JS) 파싱 매서드 ( 패킷에 들어있는, 보낸이 관련.. 뭐 userToken이나 닉네임 등등, 대표캐릭 등 )
     */

    /**
     * 각종 메시지 내용(JS) 파싱 매서드 :
     *  -- 일반 텍스트 메시지는 뭐 필요없을 수 있는데,
     *  -- 나중에 캐릭터 소환, 아이템 강화, 캐릭터 승급/강화 등등등 처리하려면.
     *      각 구조에 맞는 JS 파싱이 필요할 것.
     */


    /**
     * 각종 메시지Data(통신용으로 클라에 전달해줄) 생성 매서드 :
     */
    public static MessageData createMessageData(int messageType, ChattingUser user){

        MessageData newMessage = new MessageData();

        /** 메시지 공통 영역 채우기 */
        newMessage.messageType = messageType;
        newMessage.nickName = user.getNickName();
        newMessage.sendTime = getCurrentTimeStr();

        /** 메시지 타입별 */
        switch (messageType){

            case MessageType.LOBBY_JOIN_CHANNEL :

                newMessage.channelNum = user.getLobbyChannelNum();
                newMessage.messageContent = newMessage.channelNum + "번 채널에 입장했습니다.";

                break;


            default:

                newMessage.channelNum = 0;
                newMessage.messageContent = "메시지 내용";

                break;

        }








        return newMessage;
    }

    /**
     *
     * @return
     */
    public static String getCurrentTimeStr(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        Date currentTime = new Date(System.currentTimeMillis());

        return format.format(currentTime);
    }








}
