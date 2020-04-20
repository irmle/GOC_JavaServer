package RMI.RMI_LogicMessages.client_to_server;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import ECS.Game.*;
import RMI.RMI_Classes.*;
import RMI.RMI_Common.server_to_client;

public class Logic_requestLogin {

    //게임을 실행시키고 처음 유저가 보낸 패킷.
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {

        System.out.println("[requestLogin] ID:" + rmi_id.rmi_host_id + " 유저가 접속함.");

        //유저가 로그인 하였을 경우, RMI_HostID와, TokenID를 매핑한다.
        SessionManager.login(rmi_id.rmi_host_id, googleIDToken);

        //로그인 처리가 끝났다면 클라이언트에게 통지한다. 클라이언트에서 loginOK메소드가 호출되면 로비Scene를 로드한다.
        //클라이언트에서 로그인 후 일정시간 이상 loginOK가 호출되지 않으면 에러가 발생한 것으로 간주하고 로비Scene을 로드하지 않고 에러표시를 한다.
        server_to_client.loginOK(rmi_id, rmi_ctx);

        //기존에 참가중이던 월드맵이 존재하는지 판단한다.
        //기존에 플레이중이던 월드맵이 존재한다면, 로비화면위에 재접속 안내창을 추가적으로 띄워서 해당 맵으로 접속하게끔 한다.
        WorldMap result = MatchingManager.isUserPlayingGame(googleIDToken);
        if (result != null) {
            int getWorldMapID = result.getWorldMapID();

            //300ms 후에 재접속 시퀀스를 송신한다.
            rmi_id.getTCP_Object().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    server_to_client.reconnectingWorldMap(rmi_id, RMI_Context.Reliable_AES256, getWorldMapID,
                            new LinkedList<>(result.loadingProgressList.values()),"127.0.0.1_test", 65005);

                }
            }, 300, TimeUnit.MILLISECONDS);

            //1000ms 후에 로딩되는 정도를 중계한다.
            rmi_id.getTCP_Object().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    server_to_client.broadcastingLoadingProgress(rmi_id, RMI_Context.Reliable_AES256, new LinkedList<>(result.loadingProgressList.values()));
                }
            }, 1000, TimeUnit.MILLISECONDS);
        }

    }
}