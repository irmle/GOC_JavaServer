package Network.RMI_LogicMessages.server_to_client;

import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 05 12 화요일
 * 업뎃날짜 :
 * 목    적 :
 *      -- 서버 ; 게임에 막 접속한 유저에게 로비 채널을 랜덤으로 할당하고,
 *                  그 처리 결과 (할당된 채널 번호 및 그 내용을 담은 메시지 )를 해당 유저에게 전송한다
 *      -- 클라 ; 할당된 채널 번호를 세팅 (알맞은 채팅목록?에 보여주기 위해 필요 할 것이다.. 혹은 필요할 수도 있다..
 *                  메시지를 메시지 큐에 넣음
 *
 */
public class Logic_response_lobbyChannelAllocated {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int channelNum, MessageData message)
    {
        //do something.
    }
}