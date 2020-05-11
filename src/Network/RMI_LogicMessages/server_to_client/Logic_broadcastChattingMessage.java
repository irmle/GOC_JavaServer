package Network.RMI_LogicMessages.server_to_client;

import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 05 12 화요일
 * 업뎃날짜 :
 * 목    적 :
 *      -- 서버 ; 유저로부터 받은 채팅 메시지를, 같은 채널에 속한 다른 유저들에게 전송한다
 *      -- 클라 ; 같은 채널 내 다른 유저들이 보낸 채팅 메시지를 처리한다
 *
 */
public class Logic_broadcastChattingMessage {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, MessageData message)
    {
        //do something.
    }
}