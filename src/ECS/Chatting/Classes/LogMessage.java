package ECS.Chatting.Classes;

import ECS.Chatting.Type.ChannelType;
import ECS.Chatting.Type.MessageType;

/**
 * (일단은) 로그 메시지를 남기기 위해 필요한 정보들을 담는 구조.
 */
public class LogMessage {

    /** 멤버 변수 */

    /* 메시지 정보 */
    int messageType;
    String message;
    long currentTime;

    /* 채널 정보 */
    int channelType;
    int channelNum;

    /* 송신자 정보 */
    int userToken;
    String userNickname;


    /** 생성자 & 초기화 매서드 */
    public LogMessage(int messageType, String message, long currentTime, int channelType, int channelNum, int userToken, String userNickname) {
        this.messageType = messageType;
        this.message = message;
        this.currentTime = currentTime;
        this.channelType = channelType;
        this.channelNum = channelNum;
        this.userToken = userToken;
        this.userNickname = userNickname;
    }

    public LogMessage(long currentTime, int userToken, String userNickname) {
        this.currentTime = currentTime;
        this.userToken = userToken;
        this.userNickname = userNickname;
    }

    /**
     * 메시지 타입?별로 생성자 매핑매서드 하나씩 만들 것. 그리고 생성자 호출
     * 뭐 아래 예시처럼..
     */

    public LogMessage createMessage_ServerConnect(long currentTime, int userToken, String userNickname){

        LogMessage newLogMessage = new LogMessage(currentTime, userToken, userNickname);

        newLogMessage.messageType = MessageType.SERVER_CONNECT;
        newLogMessage.channelType = ChannelType.NONE;
        newLogMessage.channelNum = 0;

        newLogMessage.message = "님이 서버에 접속했습니다.";   // 갖고올 것..

        return newLogMessage;
    }













}
