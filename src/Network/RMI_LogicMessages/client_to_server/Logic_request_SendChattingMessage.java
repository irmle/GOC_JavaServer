package Network.RMI_LogicMessages.client_to_server;

import ECS.Chatting.ChattingManager;
import ECS.Chatting.Classes.ChattingUser;
import ECS.Chatting.Type.MessageType;
import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 05 12 화요일
 * 업뎃날짜 : 2020 06 09 화요일
 * 목    적 : 유저가 보낸 채팅 메시지를, 같은 채널에 참여해있는 다른 유저들에게 중계한다
 * 기    능 :
 *
 *      -- 여기서는, 유저가 입력한 "채팅 메시지"만을 취급함.
 *      그 외 다른 이벤트 같은 경우, Logic_request_BroadcastPlayerEvent 등에서 메시지 타입을 보고 나누어 처리함.
 *      ㄴ 당장은 저 매서드가 호출될 일이 없어 보이고...
 *
 *
 */
public class Logic_request_SendChattingMessage {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int messageType, String messageData)
    {
        //do something.

        /** 유저를 찾음 */
        ChattingUser messageSender = ChattingManager.searchUser(rmi_id);
        if(messageSender == null){
            return;
        }

        /** 메시지 타입에 따라 적절히 중계*/
        switch (messageType){

            case MessageType.LOBBY_CHAT_MSG :

                ChattingManager.broadcastChatMsg_Lobby(messageSender, messageData);
                break;

            case MessageType.SESSION_CHAT_MSG :

                ChattingManager.broadcastChatMsg_Session(messageSender, messageData);
                break;

            case MessageType.GUILD_CHAT_MSG :

                ChattingManager.broadcastChatMsg_Session(messageSender, messageData);
                break;

            default:

                break;
        }


    }
}