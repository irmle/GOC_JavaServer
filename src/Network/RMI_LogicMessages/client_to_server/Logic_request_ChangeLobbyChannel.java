package Network.RMI_LogicMessages.client_to_server;

import ECS.Chatting.ChattingManager;
import ECS.Chatting.Classes.ChattingUser;
import ECS.Chatting.Classes.LogMessage;
import ECS.Chatting.LogManager;
import ECS.Chatting.Type.MessageType;
import Network.RMI;
import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;
import Network.RMI_Common.server_to_client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 05 12 화요일
 * 업뎃날짜 :
 * 목    적 : 유저가 보낸 로비 채팅채널 변경 요청을 처리한다
 * 기    능 :
 *
 *
 */
public class Logic_request_ChangeLobbyChannel {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int channelNum)
    {
        //do something.

        /**
         * 1. 채널 변경 처리를 함
         * 2. 관련 통지 메시지 만들어서
         * 3. 통지 메서드 호출
         */

        // .. 채팅매니저 내부에 묶어둬야 겠다. change라는 이름으로.
        ChattingUser user = ChattingManager.searchUser(rmi_id);

        int formerChannelNum = user.getLobbyChannelNum();

        ChattingManager.leaveLobbyChannel(user);
        ChattingManager.joinLobbyChannel(channelNum, user);


        // 통지 메시지 만들기 호출
        MessageData messageData = new MessageData();
        messageData.channelNum = user.lobbyChannelNum;
        messageData.nickName = user.nickName;
        messageData.messageType = MessageType.LOBBY_CHANGE_CHANNEL;
        messageData.messageContent = user.getLobbyChannelNum() + "번 채널에 입장했습니다.";
        // ㄴ 이거.. 하드코딩이 되지 않도록. 먼가 매서드를 만든다거나 조치를 취해야겠다.

        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        Date currentTime = new Date();
        messageData.messageContent = format.format(currentTime);
        // ㄴ 이것도 매서드 만들기..

        // 중계 & 통지
        server_to_client.response_lobbyChannelChanged(rmi_id, rmi_ctx, channelNum, messageData);


        int changedChannelNum = user.getLobbyChannelNum();

        // 순서가 조금 엉망이지만.. 로그메시지 만들기?

        LogMessage logMessage
                = LogMessage.createMessage_LobbyChannelChanged(
                        currentTime.getTime(), user.userToken, user.nickName,
                formerChannelNum, changedChannelNum);

        // 큐에 집어넣음.
        LogManager.enqueueLogMessage(logMessage);







    }
}