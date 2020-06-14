package Network.RMI_LogicMessages.client_to_server;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import ECS.Chatting.ChattingManager;
import ECS.Chatting.Classes.ChattingUser;
import ECS.Chatting.Type.MessageType;
import ECS.Game.*;
import Network.AutoCreatedClass.MessageData;
import Network.RMI_Classes.*;
import Network.RMI_Common.server_to_client;

/**
 *
 */
public class Logic_requestLogin {

    //게임을 실행시키고 처음 유저가 보낸 패킷.
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {

        System.out.println("[requestLogin] ID:" + rmi_id.rmi_host_id + " 유저가 접속함.");

        //유저가 로그인 하였을 경우, RMI_HostID와, TokenID를 매핑한다.
        boolean loginResult = SessionManager.login(rmi_id.rmi_host_id, googleIDToken);

        /**
         * 채팅 관련 추가 처리
         *
         * 1. 채팅 접속작 객체를 생성한다
         * 2. 랜덤으로 로비 채널 하나를 선택해서
         * 3. 유저를 해당 채널에 할당
         *      ㄴ 이 과정에서, 매칭완료 후 유저들 닉네임을 가져오는 매서드를 여기서 호출해야 함..!!
         *      ㄴ 마침, 바로 위에서 닉네임 요청에 필요한 구글토큰을 세션매니저에 등록하고 있군..
         *      ㄴ 이러면, 나중에 매칭 후 매칭 인원들의 닉네임을 얻어오는 요청을 별도로 호출하지 않아도 되겠군
         *      ㄴ 세션 매니저 및 접속자 정보에 등록된 닉네임을 통해 겟;
         *      ㄴ 이거는.. 좀 수정이 필요하겟네 웹서버측이랑도 이야기 해야하고.
         * 4. 로비 채널이 할당되었음을 알리는 메시지를 만들어(통신용, 로그용) >> 로그용은 위 내용을 처리하는 과정에서 별도로 진행할 수도..
         * 5. 클라이언트에 해당 메시지를 보낸다. server_to_client.response_lobbyChannelAllocated()
         *      ㄴ 이걸.. 아래 로그인OK 이전에 처리해야 할지, 이후에 처리해야 할지..
         */

        /*ChattingUser newUser = ChattingManager.joinChattingServer(rmi_id);

        int lobbyChannelNum = newUser.getLobbyChannelNum();
        MessageData channelAllocMessage
                = ChattingManager.createMessageData(MessageType.LOBBY_JOIN_CHANNEL, newUser);

        server_to_client.response_lobbyChannelAllocated(rmi_id, rmi_ctx, lobbyChannelNum, channelAllocMessage);*/


        /***************************************************************************************************************/

        if(loginResult == true){

            //기존에 참가중이던 월드맵이 존재하는지 판단한다.
            //기존에 플레이중이던 월드맵이 존재한다면, 로비화면위에 재접속 안내창을 추가적으로 띄워서 해당 맵으로 접속하게끔 한다.
            WorldMap result = MatchingManager.isUserPlayingGame(googleIDToken);
            if(result == null) {

                //로그인 처리가 끝났다면 클라이언트에게 통지한다. 클라이언트에서 loginOK메소드가 호출되면 로비Scene를 로드한다.
                //클라이언트에서 로그인 후 일정시간 이상 loginOK가 호출되지 않으면 에러가 발생한 것으로 간주하고 로비Scene을 로드하지 않고 에러표시를 한다.
                server_to_client.loginOK(rmi_id, rmi_ctx);

            }
            else {
                int getWorldMapID = result.getWorldMapID();

                /**
                 * 세션 재접속 시, 기존 세션의 채널에 접속하게끔 하는 처리를 여기서 수행해도 될까??
                 * 1. 세션채널 관련 처리 수행(접속자 객체 내 채널값 변경 및 채팅 매니저의 채널목록에 등록
                 * 2. 세션이 할당되었음(사실 재할당이지만)을 알리는 메시지 생성
                 * 3. 당사자에 중계 ; server_to_client.response_SessionChannelAllocated() 호출
                 *
                 */

                /**************************************************************************************/

                server_to_client.reconnectingWorldMap(rmi_id, RMI_Context.Reliable_AES256, getWorldMapID,
                        new LinkedList<>(result.loadingProgressList.values()),"127.0.0.1_test", 65005);


                //500ms 후에 로딩되는 정도를 중계한다.
                rmi_id.getTCP_Object().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        server_to_client.broadcastingLoadingProgress(rmi_id, RMI_Context.Reliable_AES256, new LinkedList<>(result.loadingProgressList.values()));
                    }
                }, 500, TimeUnit.MILLISECONDS);
            }

        }

    }
}