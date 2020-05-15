package ECS.Chatting.Classes;

import ECS.Game.MatchingManager;
import ECS.Game.SessionManager;
import Network.RMI_Classes.RMI_ID;

/**
 * 채팅서버 접속자 하나를 관리하는 객체
 */
public class ChattingUser {

    /** 멤버 변수 */

    /* 접속 구분 정보 */
    public RMI_ID rmi_id;   // rmi_host_id(int)를 비롯해 소켓 채널 등등이 들어있음.

    /* 접속자 구분 정보 */
    public int userToken;   // DB상 등록되어있는, 유저 고유의 ID
    public String nickName;

    // 그 외.. 닉네임 등등 유저 정보가 필요하다면 여기에 추가될 수도.
    // 로그 쓸 때, 매번 패킷에 들어있는 유저 정보를 파싱하기 그렇잖아?? 아니.. 그래야하나?? 헐;
    // 모르겟다 이거는 로그 설계 및 저장 해보고, 필요하다 싶으면 그 때 추가하지 머.
    // 매번 파싱을 피할 수 없다면, 굳이 두지 않아도 될 듯.


    /* 속해있는 채팅 채널 정보 */
    public int lobbyChannelNum;   // 접속해있는 로비 채널.
    public int sessionChannelNum; // 접속해있는 세션 채널.
                                    // 인게임 플레이 시 접속되며, 인게임 서버의 worldID와 그 값이 동일함.
    public int guildChannelNum;    // 가입되어있는 길드의 채널.
                                     // 지금은 길드 시스템이 없어 쓰이지 않음.


    /** 생성자 & 초기화 매서드 */
    public ChattingUser() {

        rmi_id = RMI_ID.NONE;
        userToken = -1;
        nickName = "";

        lobbyChannelNum = -1;
        sessionChannelNum = -1;
        guildChannelNum = -1;

    }

    public ChattingUser(RMI_ID rmi_id) {
        this.rmi_id = rmi_id;

        userToken = -1;
        nickName = "";

        lobbyChannelNum = -1;   // 차후, 로비채널 할당 매서드를 여기서 호출해야 할 수도.
        sessionChannelNum = -1;
        guildChannelNum = -1;

    }

    /** getter & setter */

    public int getUserToken() {
        return userToken;
    }

    public void setUserToken(int userToken) {
        this.userToken = userToken;
    }

    public int getLobbyChannelNum() {
        return lobbyChannelNum;
    }

    public void setLobbyChannelNum(int lobbyChannelNum) {
        this.lobbyChannelNum = lobbyChannelNum;
    }

    public int getSessionChannelNum() {
        return sessionChannelNum;
    }

    public void setSessionChannelNum(int sessionChannelNum) {
        this.sessionChannelNum = sessionChannelNum;
    }

    public int getGuildChannelNum() {
        return guildChannelNum;
    }

    public void setGuildChannelNum(int guildChannelNum) {
        this.guildChannelNum = guildChannelNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /** 그 외 매서드 */

    /**
     * 인게임 종료 후, 게임세션 채팅채널을 떠나는 처리
     */
    public void leaveSessionChannel(){

        setSessionChannelNum(-1);
    }

    public void setUserInfo(int userToken, String nickName){

        this.setUserToken(userToken);
        this.setNickName(nickName);

    }




}
