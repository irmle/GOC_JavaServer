package Network.RMI_Common;

import Network.RMI_LogicMessages.client_to_server.*;
import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import Network.RMI_Classes.*;
import Network.*;


import java.util.LinkedList;

public class client_to_server {


    //네트워크로 송신하는 로직을 담음.
    private static void callRMI_Method(RMI_ID rmi_id, short rmi_ctx, short packetType, byte[] RMIdata) {

        //암호화를 담당하는 부분.
        byte[] data = RMI_EncryptManager.RMI_EncryptMethod(rmi_id, rmi_ctx, true, RMIdata);

        //RMI_Context.Reliable = 1; ~ RMI_Context.UnReliable_Public_AES256 = 10;
        if(1<=rmi_ctx && rmi_ctx<=5)
            RMI.sendByte_TCP(rmi_id, rmi_ctx, packetType, data); //신뢰성 전송
        else
            RMI.sendByte_UDP(rmi_id, rmi_ctx, packetType, data); //비신뢰성 전송
    }

    //네트워크로 송신하는 로직을 담음.
    private static void callRMI_Method(RMI_ID[] rmi_id, short rmi_ctx, short packetType, byte[] RMIdata) {

        //암호화를 담당하는 부분.
        byte[] data = RMI_EncryptManager.RMI_EncryptMethod_Arr(rmi_id, rmi_ctx, true, RMIdata);

        //RMI_Context.Reliable = 1; ~ RMI_Context.UnReliable_Public_AES256 = 10;
        if(1<=rmi_ctx && rmi_ctx<=5)
            RMI.sendByte_TCP(rmi_id, rmi_ctx, packetType, data); //신뢰성 전송
        else
            RMI.sendByte_UDP(rmi_id, rmi_ctx, packetType, data); //비신뢰성 전송
    }

    //네트워크에서 수신하는 로직을 담음.
    public static void recvRMI_Method(RMI_ID rmi_id, short rmi_ctx, short packetType, byte[] RMIdata) {

        //복호화를 담당하는 부분.
        byte[] data = RMI_EncryptManager.RMI_EncryptMethod(rmi_id, rmi_ctx, false, RMIdata);

        parseRMI(rmi_id, rmi_ctx, packetType, data);
    }





//수신 처리.
////////////////////////////////////////////////////////////////////////

    private static void parseRMI(RMI_ID rmi_id, short rmi_ctx, short packetType, byte[] RMIdata)
    {
        try {
        switch (packetType) {

            case RMI_PacketType.pingCheck_Request:
                pingCheck_Request r0 = pingCheck_Request.createpingCheck_Request(RMIdata);
                Logic_pingCheck_Request.RMI_Packet(rmi_id, rmi_ctx, r0.timeData);
                RMI.onRMI_Recv(rmi_ctx, packetType, r0);
                r0 = null;
                break;
            case RMI_PacketType.heartBeatCheck_Request:
                heartBeatCheck_Request r1 = heartBeatCheck_Request.createheartBeatCheck_Request(RMIdata);
                Logic_heartBeatCheck_Request.RMI_Packet(rmi_id, rmi_ctx, r1.timeData);
                RMI.onRMI_Recv(rmi_ctx, packetType, r1);
                r1 = null;
                break;
            case RMI_PacketType.requestLogin:
                requestLogin r2 = requestLogin.createrequestLogin(RMIdata);
                Logic_requestLogin.RMI_Packet(rmi_id, rmi_ctx, r2.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, r2);
                r2 = null;
                break;
            case RMI_PacketType.requestMatching:
                requestMatching r3 = requestMatching.createrequestMatching(RMIdata);
                Logic_requestMatching.RMI_Packet(rmi_id, rmi_ctx, r3.googleIDToken, r3.selectedMapType);
                RMI.onRMI_Recv(rmi_ctx, packetType, r3);
                r3 = null;
                break;
            case RMI_PacketType.cancelMatching:
                cancelMatching r4 = cancelMatching.createcancelMatching(RMIdata);
                Logic_cancelMatching.RMI_Packet(rmi_id, rmi_ctx, r4.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, r4);
                r4 = null;
                break;
            case RMI_PacketType.pickReady:
                pickReady r5 = pickReady.createpickReady(RMIdata);
                Logic_pickReady.RMI_Packet(rmi_id, rmi_ctx, r5.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, r5);
                r5 = null;
                break;
            case RMI_PacketType.pickCancel:
                pickCancel r6 = pickCancel.createpickCancel(RMIdata);
                Logic_pickCancel.RMI_Packet(rmi_id, rmi_ctx, r6.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, r6);
                r6 = null;
                break;
            case RMI_PacketType.pickSelectedCharacter:
                pickSelectedCharacter r7 = pickSelectedCharacter.createpickSelectedCharacter(RMIdata);
                Logic_pickSelectedCharacter.RMI_Packet(rmi_id, rmi_ctx, r7.googleIDToken, r7.characterType, r7.guardianID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r7);
                r7 = null;
                break;
            case RMI_PacketType.pickSendChat:
                pickSendChat r8 = pickSendChat.createpickSendChat(RMIdata);
                Logic_pickSendChat.RMI_Packet(rmi_id, rmi_ctx, r8.googleIDToken, r8.chatMessage);
                RMI.onRMI_Recv(rmi_ctx, packetType, r8);
                r8 = null;
                break;
            case RMI_PacketType.pickLogicIsVoipHostReady:
                pickLogicIsVoipHostReady r9 = pickLogicIsVoipHostReady.createpickLogicIsVoipHostReady(RMIdata);
                Logic_pickLogicIsVoipHostReady.RMI_Packet(rmi_id, rmi_ctx, r9.googleIDToken, r9.isVoipHostReady);
                RMI.onRMI_Recv(rmi_ctx, packetType, r9);
                r9 = null;
                break;
            case RMI_PacketType.gameSceneLoadingPercentage:
                gameSceneLoadingPercentage r10 = gameSceneLoadingPercentage.creategameSceneLoadingPercentage(RMIdata);
                Logic_gameSceneLoadingPercentage.RMI_Packet(rmi_id, rmi_ctx, r10.worldMapID, r10.percentage);
                RMI.onRMI_Recv(rmi_ctx, packetType, r10);
                r10 = null;
                break;
            case RMI_PacketType.requestReconnectWorldMap:
                requestReconnectWorldMap r11 = requestReconnectWorldMap.createrequestReconnectWorldMap(RMIdata);
                Logic_requestReconnectWorldMap.RMI_Packet(rmi_id, rmi_ctx, r11.worldMapID, r11.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, r11);
                r11 = null;
                break;
            case RMI_PacketType.moveCharacter:
                moveCharacter r12 = moveCharacter.createmoveCharacter(RMIdata);
                Logic_moveCharacter.RMI_Packet(rmi_id, rmi_ctx, r12.worldMapID, r12.characterMoveData);
                RMI.onRMI_Recv(rmi_ctx, packetType, r12);
                r12 = null;
                break;
            case RMI_PacketType.doAttack:
                doAttack r13 = doAttack.createdoAttack(RMIdata);
                Logic_doAttack.RMI_Packet(rmi_id, rmi_ctx, r13.worldMapID, r13.attackerEntityID, r13.targetEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r13);
                r13 = null;
                break;
            case RMI_PacketType.useSkill:
                useSkill r14 = useSkill.createuseSkill(RMIdata);
                Logic_useSkill.RMI_Packet(rmi_id, rmi_ctx, r14.worldMapID, r14.userEntityID, r14.directionX, r14.directionY, r14.directionZ, r14.distanceRate, r14.skillSlotNum, r14.targetEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r14);
                r14 = null;
                break;
            case RMI_PacketType.stopUsingSkill:
                stopUsingSkill r15 = stopUsingSkill.createstopUsingSkill(RMIdata);
                Logic_stopUsingSkill.RMI_Packet(rmi_id, rmi_ctx, r15.worldMapID, r15.userEntityID, r15.skillSlotNum);
                RMI.onRMI_Recv(rmi_ctx, packetType, r15);
                r15 = null;
                break;
            case RMI_PacketType.upgradeSkill:
                upgradeSkill r16 = upgradeSkill.createupgradeSkill(RMIdata);
                Logic_upgradeSkill.RMI_Packet(rmi_id, rmi_ctx, r16.worldMapID, r16.userEntityID, r16.skillSlotNum);
                RMI.onRMI_Recv(rmi_ctx, packetType, r16);
                r16 = null;
                break;
            case RMI_PacketType.getSkill:
                getSkill r17 = getSkill.creategetSkill(RMIdata);
                Logic_getSkill.RMI_Packet(rmi_id, rmi_ctx, r17.worldMapID, r17.userEntityID, r17.skillSlotNum, r17.skillType);
                RMI.onRMI_Recv(rmi_ctx, packetType, r17);
                r17 = null;
                break;
            case RMI_PacketType.requestStoreUpgradeBuff:
                requestStoreUpgradeBuff r18 = requestStoreUpgradeBuff.createrequestStoreUpgradeBuff(RMIdata);
                Logic_requestStoreUpgradeBuff.RMI_Packet(rmi_id, rmi_ctx, r18.worldMapID, r18.userEntityID, r18.storeUpgradeType, r18.buffLevel);
                RMI.onRMI_Recv(rmi_ctx, packetType, r18);
                r18 = null;
                break;
            case RMI_PacketType.installTurret:
                installTurret r19 = installTurret.createinstallTurret(RMIdata);
                Logic_installTurret.RMI_Packet(rmi_id, rmi_ctx, r19.worldMapID, r19.userEntityID, r19.turretType, r19.areaNumber);
                RMI.onRMI_Recv(rmi_ctx, packetType, r19);
                r19 = null;
                break;
            case RMI_PacketType.upgradeTurret:
                upgradeTurret r20 = upgradeTurret.createupgradeTurret(RMIdata);
                Logic_upgradeTurret.RMI_Packet(rmi_id, rmi_ctx, r20.worldMapID, r20.userEntityID, r20.turretEntityID, r20.turretType);
                RMI.onRMI_Recv(rmi_ctx, packetType, r20);
                r20 = null;
                break;
            case RMI_PacketType.installBarricade:
                installBarricade r21 = installBarricade.createinstallBarricade(RMIdata);
                Logic_installBarricade.RMI_Packet(rmi_id, rmi_ctx, r21.worldMapID, r21.userEntityID, r21.barricadeType, r21.areaNumber);
                RMI.onRMI_Recv(rmi_ctx, packetType, r21);
                r21 = null;
                break;
            case RMI_PacketType.upgradeBarricade:
                upgradeBarricade r22 = upgradeBarricade.createupgradeBarricade(RMIdata);
                Logic_upgradeBarricade.RMI_Packet(rmi_id, rmi_ctx, r22.worldMapID, r22.userEntityID, r22.barricadeEntityID, r22.barricadeType);
                RMI.onRMI_Recv(rmi_ctx, packetType, r22);
                r22 = null;
                break;
            case RMI_PacketType.buyItem:
                buyItem r23 = buyItem.createbuyItem(RMIdata);
                Logic_buyItem.RMI_Packet(rmi_id, rmi_ctx, r23.worldMapID, r23.userEntityID, r23.itemSlotNum, r23.itemType, r23.itemCount);
                RMI.onRMI_Recv(rmi_ctx, packetType, r23);
                r23 = null;
                break;
            case RMI_PacketType.sellItem:
                sellItem r24 = sellItem.createsellItem(RMIdata);
                Logic_sellItem.RMI_Packet(rmi_id, rmi_ctx, r24.worldMapID, r24.userEntityID, r24.itemSlotNum, r24.itemType, r24.itemCount);
                RMI.onRMI_Recv(rmi_ctx, packetType, r24);
                r24 = null;
                break;
            case RMI_PacketType.useItem:
                useItem r25 = useItem.createuseItem(RMIdata);
                Logic_useItem.RMI_Packet(rmi_id, rmi_ctx, r25.worldMapID, r25.userEntityID, r25.itemSlotNum, r25.itemCount);
                RMI.onRMI_Recv(rmi_ctx, packetType, r25);
                r25 = null;
                break;
            case RMI_PacketType.request_ChangeLobbyChannel:
                request_ChangeLobbyChannel r26 = request_ChangeLobbyChannel.createrequest_ChangeLobbyChannel(RMIdata);
                Logic_request_ChangeLobbyChannel.RMI_Packet(rmi_id, rmi_ctx, r26.channelNum);
                RMI.onRMI_Recv(rmi_ctx, packetType, r26);
                r26 = null;
                break;
            case RMI_PacketType.request_SendChattingMessage:
                request_SendChattingMessage r27 = request_SendChattingMessage.createrequest_SendChattingMessage(RMIdata);
                Logic_request_SendChattingMessage.RMI_Packet(rmi_id, rmi_ctx, r27.messageType, r27.messageData);
                RMI.onRMI_Recv(rmi_ctx, packetType, r27);
                r27 = null;
                break;
            case RMI_PacketType.request_BroadcastPlayerEvent:
                request_BroadcastPlayerEvent r28 = request_BroadcastPlayerEvent.createrequest_BroadcastPlayerEvent(RMIdata);
                Logic_request_BroadcastPlayerEvent.RMI_Packet(rmi_id, rmi_ctx, r28.messageType, r28.broadcastingDataJS);
                RMI.onRMI_Recv(rmi_ctx, packetType, r28);
                r28 = null;
                break;
                
            default:
                System.out.println("[client_to_server] 그룹에 존재하지 않는 RMI콜. 패킷 무시.");
                //if(rmi_id.getTCP_Object()!=null)
                    //rmi_id.getTCP_Object().close();
                //if(rmi_id.getUDP_Object()!=null)
                    //rmi_id.getUDP_Object().close();
                break;
        }
        } catch (Exception e) {
        System.out.println("Packet파싱중 에러발생! 데이터 손상! rmi_ctx="+rmi_ctx+" packetType="+packetType+" RMIdata="+RMIdata.length + "\n" + e.toString());
        }
    }

//송신 처리!
////////////////////////////////////////////////////////////////////////
    public static void pingCheck_Request(RMI_ID rmi_id, short rmi_ctx, float timeData) {
        pingCheck_Request _data__1 = new pingCheck_Request();
        _data__1.timeData = timeData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pingCheck_Request, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pingCheck_Request, _data__1);
        _data__1 = null;
    }

    public static void pingCheck_Request(RMI_ID[] rmi_id, short rmi_ctx, float timeData) {
        pingCheck_Request _data__1 = new pingCheck_Request();
        _data__1.timeData = timeData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pingCheck_Request, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pingCheck_Request, _data__1);
        _data__1 = null;
    }

    public static void heartBeatCheck_Request(RMI_ID rmi_id, short rmi_ctx, float timeData) {
        heartBeatCheck_Request _data__1 = new heartBeatCheck_Request();
        _data__1.timeData = timeData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.heartBeatCheck_Request, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.heartBeatCheck_Request, _data__1);
        _data__1 = null;
    }

    public static void heartBeatCheck_Request(RMI_ID[] rmi_id, short rmi_ctx, float timeData) {
        heartBeatCheck_Request _data__1 = new heartBeatCheck_Request();
        _data__1.timeData = timeData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.heartBeatCheck_Request, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.heartBeatCheck_Request, _data__1);
        _data__1 = null;
    }

    public static void requestLogin(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        requestLogin _data__1 = new requestLogin();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestLogin, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.requestLogin, _data__1);
        _data__1 = null;
    }

    public static void requestLogin(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        requestLogin _data__1 = new requestLogin();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestLogin, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.requestLogin, _data__1);
        _data__1 = null;
    }

    public static void requestMatching(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, int selectedMapType) {
        requestMatching _data__1 = new requestMatching();
        _data__1.googleIDToken = googleIDToken;
        _data__1.selectedMapType = selectedMapType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.requestMatching, _data__1);
        _data__1 = null;
    }

    public static void requestMatching(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, int selectedMapType) {
        requestMatching _data__1 = new requestMatching();
        _data__1.googleIDToken = googleIDToken;
        _data__1.selectedMapType = selectedMapType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.requestMatching, _data__1);
        _data__1 = null;
    }

    public static void cancelMatching(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        cancelMatching _data__1 = new cancelMatching();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.cancelMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.cancelMatching, _data__1);
        _data__1 = null;
    }

    public static void cancelMatching(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        cancelMatching _data__1 = new cancelMatching();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.cancelMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.cancelMatching, _data__1);
        _data__1 = null;
    }

    public static void pickReady(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickReady _data__1 = new pickReady();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickReady, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickReady, _data__1);
        _data__1 = null;
    }

    public static void pickReady(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickReady _data__1 = new pickReady();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickReady, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickReady, _data__1);
        _data__1 = null;
    }

    public static void pickCancel(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickCancel _data__1 = new pickCancel();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickCancel, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickCancel, _data__1);
        _data__1 = null;
    }

    public static void pickCancel(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickCancel _data__1 = new pickCancel();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickCancel, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickCancel, _data__1);
        _data__1 = null;
    }

    public static void pickSelectedCharacter(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, int characterType, int guardianID) {
        pickSelectedCharacter _data__1 = new pickSelectedCharacter();
        _data__1.googleIDToken = googleIDToken;
        _data__1.characterType = characterType;
        _data__1.guardianID = guardianID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickSelectedCharacter, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickSelectedCharacter, _data__1);
        _data__1 = null;
    }

    public static void pickSelectedCharacter(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, int characterType, int guardianID) {
        pickSelectedCharacter _data__1 = new pickSelectedCharacter();
        _data__1.googleIDToken = googleIDToken;
        _data__1.characterType = characterType;
        _data__1.guardianID = guardianID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickSelectedCharacter, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickSelectedCharacter, _data__1);
        _data__1 = null;
    }

    public static void pickSendChat(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, String chatMessage) {
        pickSendChat _data__1 = new pickSendChat();
        _data__1.googleIDToken = googleIDToken;
        _data__1.chatMessage = chatMessage;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickSendChat, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickSendChat, _data__1);
        _data__1 = null;
    }

    public static void pickSendChat(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, String chatMessage) {
        pickSendChat _data__1 = new pickSendChat();
        _data__1.googleIDToken = googleIDToken;
        _data__1.chatMessage = chatMessage;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickSendChat, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickSendChat, _data__1);
        _data__1 = null;
    }

    public static void pickLogicIsVoipHostReady(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, boolean isVoipHostReady) {
        pickLogicIsVoipHostReady _data__1 = new pickLogicIsVoipHostReady();
        _data__1.googleIDToken = googleIDToken;
        _data__1.isVoipHostReady = isVoipHostReady;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicIsVoipHostReady, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicIsVoipHostReady, _data__1);
        _data__1 = null;
    }

    public static void pickLogicIsVoipHostReady(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, boolean isVoipHostReady) {
        pickLogicIsVoipHostReady _data__1 = new pickLogicIsVoipHostReady();
        _data__1.googleIDToken = googleIDToken;
        _data__1.isVoipHostReady = isVoipHostReady;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicIsVoipHostReady, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicIsVoipHostReady, _data__1);
        _data__1 = null;
    }

    public static void gameSceneLoadingPercentage(RMI_ID rmi_id, short rmi_ctx, int worldMapID, float percentage) {
        gameSceneLoadingPercentage _data__1 = new gameSceneLoadingPercentage();
        _data__1.worldMapID = worldMapID;
        _data__1.percentage = percentage;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.gameSceneLoadingPercentage, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.gameSceneLoadingPercentage, _data__1);
        _data__1 = null;
    }

    public static void gameSceneLoadingPercentage(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, float percentage) {
        gameSceneLoadingPercentage _data__1 = new gameSceneLoadingPercentage();
        _data__1.worldMapID = worldMapID;
        _data__1.percentage = percentage;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.gameSceneLoadingPercentage, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.gameSceneLoadingPercentage, _data__1);
        _data__1 = null;
    }

    public static void requestReconnectWorldMap(RMI_ID rmi_id, short rmi_ctx, int worldMapID, String googleIDToken) {
        requestReconnectWorldMap _data__1 = new requestReconnectWorldMap();
        _data__1.worldMapID = worldMapID;
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestReconnectWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.requestReconnectWorldMap, _data__1);
        _data__1 = null;
    }

    public static void requestReconnectWorldMap(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, String googleIDToken) {
        requestReconnectWorldMap _data__1 = new requestReconnectWorldMap();
        _data__1.worldMapID = worldMapID;
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestReconnectWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.requestReconnectWorldMap, _data__1);
        _data__1 = null;
    }

    public static void moveCharacter(RMI_ID rmi_id, short rmi_ctx, int worldMapID, CharacterMoveData characterMoveData) {
        moveCharacter _data__1 = new moveCharacter();
        _data__1.worldMapID = worldMapID;
        _data__1.characterMoveData = characterMoveData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.moveCharacter, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.moveCharacter, _data__1);
        _data__1 = null;
    }

    public static void moveCharacter(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, CharacterMoveData characterMoveData) {
        moveCharacter _data__1 = new moveCharacter();
        _data__1.worldMapID = worldMapID;
        _data__1.characterMoveData = characterMoveData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.moveCharacter, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.moveCharacter, _data__1);
        _data__1 = null;
    }

    public static void doAttack(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int attackerEntityID, int targetEntityID) {
        doAttack _data__1 = new doAttack();
        _data__1.worldMapID = worldMapID;
        _data__1.attackerEntityID = attackerEntityID;
        _data__1.targetEntityID = targetEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.doAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.doAttack, _data__1);
        _data__1 = null;
    }

    public static void doAttack(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int attackerEntityID, int targetEntityID) {
        doAttack _data__1 = new doAttack();
        _data__1.worldMapID = worldMapID;
        _data__1.attackerEntityID = attackerEntityID;
        _data__1.targetEntityID = targetEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.doAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.doAttack, _data__1);
        _data__1 = null;
    }

    public static void useSkill(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, float directionX, float directionY, float directionZ, float distanceRate, short skillSlotNum, int targetEntityID) {
        useSkill _data__1 = new useSkill();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.directionX = directionX;
        _data__1.directionY = directionY;
        _data__1.directionZ = directionZ;
        _data__1.distanceRate = distanceRate;
        _data__1.skillSlotNum = skillSlotNum;
        _data__1.targetEntityID = targetEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.useSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.useSkill, _data__1);
        _data__1 = null;
    }

    public static void useSkill(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, float directionX, float directionY, float directionZ, float distanceRate, short skillSlotNum, int targetEntityID) {
        useSkill _data__1 = new useSkill();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.directionX = directionX;
        _data__1.directionY = directionY;
        _data__1.directionZ = directionZ;
        _data__1.distanceRate = distanceRate;
        _data__1.skillSlotNum = skillSlotNum;
        _data__1.targetEntityID = targetEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.useSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.useSkill, _data__1);
        _data__1 = null;
    }

    public static void stopUsingSkill(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum) {
        stopUsingSkill _data__1 = new stopUsingSkill();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.skillSlotNum = skillSlotNum;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.stopUsingSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.stopUsingSkill, _data__1);
        _data__1 = null;
    }

    public static void stopUsingSkill(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum) {
        stopUsingSkill _data__1 = new stopUsingSkill();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.skillSlotNum = skillSlotNum;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.stopUsingSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.stopUsingSkill, _data__1);
        _data__1 = null;
    }

    public static void upgradeSkill(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum) {
        upgradeSkill _data__1 = new upgradeSkill();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.skillSlotNum = skillSlotNum;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeSkill, _data__1);
        _data__1 = null;
    }

    public static void upgradeSkill(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum) {
        upgradeSkill _data__1 = new upgradeSkill();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.skillSlotNum = skillSlotNum;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeSkill, _data__1);
        _data__1 = null;
    }

    public static void getSkill(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum, int skillType) {
        getSkill _data__1 = new getSkill();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.skillSlotNum = skillSlotNum;
        _data__1.skillType = skillType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.getSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.getSkill, _data__1);
        _data__1 = null;
    }

    public static void getSkill(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum, int skillType) {
        getSkill _data__1 = new getSkill();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.skillSlotNum = skillSlotNum;
        _data__1.skillType = skillType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.getSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.getSkill, _data__1);
        _data__1 = null;
    }

    public static void requestStoreUpgradeBuff(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int storeUpgradeType, int buffLevel) {
        requestStoreUpgradeBuff _data__1 = new requestStoreUpgradeBuff();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.storeUpgradeType = storeUpgradeType;
        _data__1.buffLevel = buffLevel;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestStoreUpgradeBuff, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.requestStoreUpgradeBuff, _data__1);
        _data__1 = null;
    }

    public static void requestStoreUpgradeBuff(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int storeUpgradeType, int buffLevel) {
        requestStoreUpgradeBuff _data__1 = new requestStoreUpgradeBuff();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.storeUpgradeType = storeUpgradeType;
        _data__1.buffLevel = buffLevel;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestStoreUpgradeBuff, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.requestStoreUpgradeBuff, _data__1);
        _data__1 = null;
    }

    public static void installTurret(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretType, int areaNumber) {
        installTurret _data__1 = new installTurret();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.turretType = turretType;
        _data__1.areaNumber = areaNumber;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.installTurret, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.installTurret, _data__1);
        _data__1 = null;
    }

    public static void installTurret(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretType, int areaNumber) {
        installTurret _data__1 = new installTurret();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.turretType = turretType;
        _data__1.areaNumber = areaNumber;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.installTurret, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.installTurret, _data__1);
        _data__1 = null;
    }

    public static void upgradeTurret(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretEntityID, int turretType) {
        upgradeTurret _data__1 = new upgradeTurret();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.turretEntityID = turretEntityID;
        _data__1.turretType = turretType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeTurret, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeTurret, _data__1);
        _data__1 = null;
    }

    public static void upgradeTurret(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretEntityID, int turretType) {
        upgradeTurret _data__1 = new upgradeTurret();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.turretEntityID = turretEntityID;
        _data__1.turretType = turretType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeTurret, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeTurret, _data__1);
        _data__1 = null;
    }

    public static void installBarricade(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int barricadeType, int areaNumber) {
        installBarricade _data__1 = new installBarricade();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.barricadeType = barricadeType;
        _data__1.areaNumber = areaNumber;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.installBarricade, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.installBarricade, _data__1);
        _data__1 = null;
    }

    public static void installBarricade(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int barricadeType, int areaNumber) {
        installBarricade _data__1 = new installBarricade();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.barricadeType = barricadeType;
        _data__1.areaNumber = areaNumber;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.installBarricade, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.installBarricade, _data__1);
        _data__1 = null;
    }

    public static void upgradeBarricade(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int barricadeEntityID, int barricadeType) {
        upgradeBarricade _data__1 = new upgradeBarricade();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.barricadeEntityID = barricadeEntityID;
        _data__1.barricadeType = barricadeType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeBarricade, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeBarricade, _data__1);
        _data__1 = null;
    }

    public static void upgradeBarricade(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int barricadeEntityID, int barricadeType) {
        upgradeBarricade _data__1 = new upgradeBarricade();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.barricadeEntityID = barricadeEntityID;
        _data__1.barricadeType = barricadeType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeBarricade, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeBarricade, _data__1);
        _data__1 = null;
    }

    public static void buyItem(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, int itemType, short itemCount) {
        buyItem _data__1 = new buyItem();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.itemSlotNum = itemSlotNum;
        _data__1.itemType = itemType;
        _data__1.itemCount = itemCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.buyItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.buyItem, _data__1);
        _data__1 = null;
    }

    public static void buyItem(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, int itemType, short itemCount) {
        buyItem _data__1 = new buyItem();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.itemSlotNum = itemSlotNum;
        _data__1.itemType = itemType;
        _data__1.itemCount = itemCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.buyItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.buyItem, _data__1);
        _data__1 = null;
    }

    public static void sellItem(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, int itemType, short itemCount) {
        sellItem _data__1 = new sellItem();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.itemSlotNum = itemSlotNum;
        _data__1.itemType = itemType;
        _data__1.itemCount = itemCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.sellItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.sellItem, _data__1);
        _data__1 = null;
    }

    public static void sellItem(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, int itemType, short itemCount) {
        sellItem _data__1 = new sellItem();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.itemSlotNum = itemSlotNum;
        _data__1.itemType = itemType;
        _data__1.itemCount = itemCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.sellItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.sellItem, _data__1);
        _data__1 = null;
    }

    public static void useItem(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, short itemCount) {
        useItem _data__1 = new useItem();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.itemSlotNum = itemSlotNum;
        _data__1.itemCount = itemCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.useItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.useItem, _data__1);
        _data__1 = null;
    }

    public static void useItem(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, short itemCount) {
        useItem _data__1 = new useItem();
        _data__1.worldMapID = worldMapID;
        _data__1.userEntityID = userEntityID;
        _data__1.itemSlotNum = itemSlotNum;
        _data__1.itemCount = itemCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.useItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.useItem, _data__1);
        _data__1 = null;
    }

    public static void request_ChangeLobbyChannel(RMI_ID rmi_id, short rmi_ctx, int channelNum) {
        request_ChangeLobbyChannel _data__1 = new request_ChangeLobbyChannel();
        _data__1.channelNum = channelNum;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.request_ChangeLobbyChannel, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.request_ChangeLobbyChannel, _data__1);
        _data__1 = null;
    }

    public static void request_ChangeLobbyChannel(RMI_ID[] rmi_id, short rmi_ctx, int channelNum) {
        request_ChangeLobbyChannel _data__1 = new request_ChangeLobbyChannel();
        _data__1.channelNum = channelNum;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.request_ChangeLobbyChannel, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.request_ChangeLobbyChannel, _data__1);
        _data__1 = null;
    }

    public static void request_SendChattingMessage(RMI_ID rmi_id, short rmi_ctx, int messageType, String messageData) {
        request_SendChattingMessage _data__1 = new request_SendChattingMessage();
        _data__1.messageType = messageType;
        _data__1.messageData = messageData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.request_SendChattingMessage, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.request_SendChattingMessage, _data__1);
        _data__1 = null;
    }

    public static void request_SendChattingMessage(RMI_ID[] rmi_id, short rmi_ctx, int messageType, String messageData) {
        request_SendChattingMessage _data__1 = new request_SendChattingMessage();
        _data__1.messageType = messageType;
        _data__1.messageData = messageData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.request_SendChattingMessage, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.request_SendChattingMessage, _data__1);
        _data__1 = null;
    }

    public static void request_BroadcastPlayerEvent(RMI_ID rmi_id, short rmi_ctx, int messageType, String broadcastingDataJS) {
        request_BroadcastPlayerEvent _data__1 = new request_BroadcastPlayerEvent();
        _data__1.messageType = messageType;
        _data__1.broadcastingDataJS = broadcastingDataJS;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.request_BroadcastPlayerEvent, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.request_BroadcastPlayerEvent, _data__1);
        _data__1 = null;
    }

    public static void request_BroadcastPlayerEvent(RMI_ID[] rmi_id, short rmi_ctx, int messageType, String broadcastingDataJS) {
        request_BroadcastPlayerEvent _data__1 = new request_BroadcastPlayerEvent();
        _data__1.messageType = messageType;
        _data__1.broadcastingDataJS = broadcastingDataJS;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.request_BroadcastPlayerEvent, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.request_BroadcastPlayerEvent, _data__1);
        _data__1 = null;
    }

}
