package Network.RMI_LogicMessages.server_to_client;

import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 05 12 화요일
 * 업뎃날짜 :
 * 목    적 :
 *      -- 서버 ; 유저에게 공지성 메시지를 전송한다
 *                  ex) 같은 게임세션에 속한 다른 유저에 대한 알림사항
 *                      같은 채널에 속한 다른 유저들에게 발생한 이벤트 ( 템 만렙강화 등 )
 *      -- 클라 ; 전송받은 공지성 메시지를 처리한다
 *                  // 메시지 큐에 집어넣음
 *
 */
public class Logic_broadcastNoticeMessage {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, MessageData message)
    {
        //do something.
    }
}