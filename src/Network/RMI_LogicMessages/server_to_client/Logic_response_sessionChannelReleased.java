package Network.RMI_LogicMessages.server_to_client;

import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 05 12 화요일
 * 업뎃날짜 :
 * 목    적 :
 *      -- 서버 ; 게임 세션 하나가 종료되면, 해당 채널이 종료되었음을
 *                  세션에 참가했던 유저들에게 알린다
 *      -- 클라 ; 메시지를 메시지 큐에 넣음.
 *                  현재 세팅된 세션채널 번호를 초기화(-1)
 *
 */
public class Logic_response_sessionChannelReleased {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, MessageData message)
    {
        //do something.
    }
}