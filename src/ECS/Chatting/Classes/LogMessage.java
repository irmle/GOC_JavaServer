package ECS.Chatting.Classes;

import ECS.Chatting.Type.ChannelType;
import ECS.Chatting.Type.MessageType;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    /** 메시지 객체를 로그내에 쓸 내용으로.. */
    public String getLogContent(){

        String logStr;

        // 시간 구함. 포맷 맞춰서.
        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        Date currentTime = new Date(this.currentTime);

        // 메시지 타입 구하기...... 타입뿐 아니라, 공통되는 부분은 위에서 처리하고,
        // 그 외에 타입별로 갈리는 부분은 switch ~ case 문으로 처리.
        String messageType = "메시지 타입";


        logStr = "[" + format.format(currentTime) + "]"
                + this.userNickname + "님이 " + this.message;


        return logStr;

    }












    /** 타입별 메시지 객체 생성      */


    public LogMessage createMessage_ServerConnect(long currentTime, int userToken, String userNickname){

        LogMessage newLogMessage = new LogMessage(currentTime, userToken, userNickname);

        newLogMessage.messageType = MessageType.SERVER_CONNECT;
        newLogMessage.channelType = ChannelType.NONE;
        newLogMessage.channelNum = 0;

        newLogMessage.message = "님이 서버에 접속했습니다.";   // 갖고올 것..

        return newLogMessage;
    }


    public static LogMessage createMessage_LobbyChannelChanged(long currentTime, int userToken, String userNickname, int formerChannelNum, int changedChannelNum){

        LogMessage newLogMessage = new LogMessage(currentTime, userToken, userNickname);

        newLogMessage.messageType = MessageType.LOBBY_CHANGE_CHANNEL;
        newLogMessage.channelType = ChannelType.LOBBY;
        newLogMessage.channelNum = changedChannelNum;

        newLogMessage.message = userNickname + "님이 " + formerChannelNum + "번 채널에서 " + changedChannelNum + "번 채널로 변경했습니다.";   // 갖고올 것..

        return newLogMessage;


    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }
}
