package Network.RMI_LogicMessages.client_to_server;

import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 05 12 화요일
 * 업뎃날짜 :
 * 목    적 :
 *      -- 서버 ; 유저로부터 받은 이벤트 정보를, 알맞은 채널 유저들에게 중계한다.
 *      -- 클라 ; 유저 본인에게 발생한 플레이 이벤트 결과를, 해당 이벤트에 대한 정보를 담아 서버로 전송한다.
 *              전송된 이벤트정보는 같은 채널/길드의 다른 유저들에게 공지성 메시지로 전송된다.
 *
 *              이벤트 예))
 *                  -- 아이템을 만렙까지 강화함.
 *                      해당 템 정보(타입, 레벨 등)와 최종 강화된 템 스펙(특정 스탯이 몇 등)을
 *                      JSON 형식으로 담아 서버로 보낸다.
 *
 */
public class Logic_request_BroadcastPlayerEvent {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int messageType, String broadcastingDataJS)
    {
        //do something.
    }
}