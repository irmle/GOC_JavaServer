package RMI.RMI_Common;

import java.util.LinkedList;
import RMI.RMI_LogicMessages.client_to_server.*;
import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.AutoCreatedClass.*;
import RMI.RMI_Classes.*;
import RMI.*;

public class client_to_server {


    //네트워크로 송신하는 로직을 담음.
    private static void callRMI_Method(RMI_ID rmi_id, short rmi_ctx, short packetType, byte[] RMIdata) {

        //암호화를 담당하는 부분.
        RMIdata = RMI__EncryptManager.RMI_EncryptMethod(rmi_id, rmi_ctx, true, RMIdata);

        //RMI_Context.Reliable = 1; ~ RMI_Context.UnReliable_Public_AES256 = 10;
        if(1<=rmi_ctx && rmi_ctx<=5)
            RMI_.sendByte_TCP(rmi_id, rmi_ctx, packetType, RMIdata); //신뢰성 전송
        else
            RMI_.sendByte_UDP(rmi_id, rmi_ctx, packetType, RMIdata); //비신뢰성 전송
    }

    //네트워크로 송신하는 로직을 담음.
    private static void callRMI_Method(RMI_ID[] rmi_id, short rmi_ctx, short packetType, byte[] RMIdata) {

        //암호화를 담당하는 부분.
        RMIdata = RMI__EncryptManager.RMI_EncryptMethod_Arr(rmi_id, rmi_ctx, true, RMIdata);

        //RMI_Context.Reliable = 1; ~ RMI_Context.UnReliable_Public_AES256 = 10;
        if(1<=rmi_ctx && rmi_ctx<=5)
            RMI_.sendByte_TCP(rmi_id, rmi_ctx, packetType, RMIdata); //신뢰성 전송
        else
            RMI_.sendByte_UDP(rmi_id, rmi_ctx, packetType, RMIdata); //비신뢰성 전송
    }

    //네트워크에서 수신하는 로직을 담음.
    public static void recvRMI_Method(RMI_ID rmi_id, short rmi_ctx, short packetType, byte[] RMIdata) {

        //복호화를 담당하는 부분.
        RMIdata = RMI__EncryptManager.RMI_EncryptMethod(rmi_id, rmi_ctx, false, RMIdata);

        parseRMI(rmi_id, rmi_ctx, packetType, RMIdata);
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
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.heartBeatCheck_Request:
                heartBeatCheck_Request r1 = heartBeatCheck_Request.createheartBeatCheck_Request(RMIdata);
                Logic_heartBeatCheck_Request.RMI_Packet(rmi_id, rmi_ctx, r1.timeData);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.requestLogin:
                requestLogin r2 = requestLogin.createrequestLogin(RMIdata);
                Logic_requestLogin.RMI_Packet(rmi_id, rmi_ctx, r2.googleIDToken);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.requestMatching:
                requestMatching r3 = requestMatching.createrequestMatching(RMIdata);
                Logic_requestMatching.RMI_Packet(rmi_id, rmi_ctx, r3.googleIDToken, r3.selectedMapType);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.cancelMatching:
                cancelMatching r4 = cancelMatching.createcancelMatching(RMIdata);
                Logic_cancelMatching.RMI_Packet(rmi_id, rmi_ctx, r4.googleIDToken);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickReady:
                pickReady r5 = pickReady.createpickReady(RMIdata);
                Logic_pickReady.RMI_Packet(rmi_id, rmi_ctx, r5.googleIDToken);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickCancel:
                pickCancel r6 = pickCancel.createpickCancel(RMIdata);
                Logic_pickCancel.RMI_Packet(rmi_id, rmi_ctx, r6.googleIDToken);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickSelectedCharacter:
                pickSelectedCharacter r7 = pickSelectedCharacter.createpickSelectedCharacter(RMIdata);
                Logic_pickSelectedCharacter.RMI_Packet(rmi_id, rmi_ctx, r7.googleIDToken, r7.characterType);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickSendChat:
                pickSendChat r8 = pickSendChat.createpickSendChat(RMIdata);
                Logic_pickSendChat.RMI_Packet(rmi_id, rmi_ctx, r8.googleIDToken, r8.chatMessage);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicIsVoipHostReady:
                pickLogicIsVoipHostReady r9 = pickLogicIsVoipHostReady.createpickLogicIsVoipHostReady(RMIdata);
                Logic_pickLogicIsVoipHostReady.RMI_Packet(rmi_id, rmi_ctx, r9.googleIDToken, r9.isVoipHostReady);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.gameSceneLoadingPercentage:
                gameSceneLoadingPercentage r10 = gameSceneLoadingPercentage.creategameSceneLoadingPercentage(RMIdata);
                Logic_gameSceneLoadingPercentage.RMI_Packet(rmi_id, rmi_ctx, r10.worldMapID, r10.percentage);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.requestReconnectWorldMap:
                requestReconnectWorldMap r11 = requestReconnectWorldMap.createrequestReconnectWorldMap(RMIdata);
                Logic_requestReconnectWorldMap.RMI_Packet(rmi_id, rmi_ctx, r11.worldMapID, r11.googleIDToken);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.moveCharacter:
                moveCharacter r12 = moveCharacter.createmoveCharacter(RMIdata);
                Logic_moveCharacter.RMI_Packet(rmi_id, rmi_ctx, r12.worldMapID, r12.characterMoveData);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.doAttack:
                doAttack r13 = doAttack.createdoAttack(RMIdata);
                Logic_doAttack.RMI_Packet(rmi_id, rmi_ctx, r13.worldMapID, r13.attackerEntityID, r13.targetEntityID);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.useSkill:
                useSkill r14 = useSkill.createuseSkill(RMIdata);
                Logic_useSkill.RMI_Packet(rmi_id, rmi_ctx, r14.worldMapID, r14.userEntityID, r14.directionX, r14.directionY, r14.directionZ, r14.distanceRate, r14.skillSlotNum, r14.targetEntityID);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.stopUsingSkill:
                stopUsingSkill r15 = stopUsingSkill.createstopUsingSkill(RMIdata);
                Logic_stopUsingSkill.RMI_Packet(rmi_id, rmi_ctx, r15.worldMapID, r15.userEntityID, r15.skillSlotNum);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.upgradeSkill:
                upgradeSkill r16 = upgradeSkill.createupgradeSkill(RMIdata);
                Logic_upgradeSkill.RMI_Packet(rmi_id, rmi_ctx, r16.worldMapID, r16.userEntityID, r16.skillSlotNum);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.getSkill:
                getSkill r17 = getSkill.creategetSkill(RMIdata);
                Logic_getSkill.RMI_Packet(rmi_id, rmi_ctx, r17.worldMapID, r17.userEntityID, r17.skillSlotNum, r17.skillType);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.requestStoreUpgradeBuff:
                requestStoreUpgradeBuff r18 = requestStoreUpgradeBuff.createrequestStoreUpgradeBuff(RMIdata);
                Logic_requestStoreUpgradeBuff.RMI_Packet(rmi_id, rmi_ctx, r18.worldMapID, r18.userEntityID, r18.storeUpgradeType, r18.buffLevel);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.installTurret:
                installTurret r19 = installTurret.createinstallTurret(RMIdata);
                Logic_installTurret.RMI_Packet(rmi_id, rmi_ctx, r19.worldMapID, r19.userEntityID, r19.turretType, r19.areaNumber);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.upgradeTurret:
                upgradeTurret r20 = upgradeTurret.createupgradeTurret(RMIdata);
                Logic_upgradeTurret.RMI_Packet(rmi_id, rmi_ctx, r20.worldMapID, r20.userEntityID, r20.turretEntityID, r20.turretType);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.installBarricade:
                installBarricade r21 = installBarricade.createinstallBarricade(RMIdata);
                Logic_installBarricade.RMI_Packet(rmi_id, rmi_ctx, r21.worldMapID, r21.userEntityID, r21.barricadeType, r21.areaNumber);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.upgradeBarricade:
                upgradeBarricade r22 = upgradeBarricade.createupgradeBarricade(RMIdata);
                Logic_upgradeBarricade.RMI_Packet(rmi_id, rmi_ctx, r22.worldMapID, r22.userEntityID, r22.barricadeEntityID, r22.barricadeType);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.buyItem:
                buyItem r23 = buyItem.createbuyItem(RMIdata);
                Logic_buyItem.RMI_Packet(rmi_id, rmi_ctx, r23.worldMapID, r23.userEntityID, r23.itemSlotNum, r23.itemType, r23.itemCount);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.sellItem:
                sellItem r24 = sellItem.createsellItem(RMIdata);
                Logic_sellItem.RMI_Packet(rmi_id, rmi_ctx, r24.worldMapID, r24.userEntityID, r24.itemSlotNum, r24.itemType, r24.itemCount);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.useItem:
                useItem r25 = useItem.createuseItem(RMIdata);
                Logic_useItem.RMI_Packet(rmi_id, rmi_ctx, r25.worldMapID, r25.userEntityID, r25.itemSlotNum, r25.itemCount);
                RMI_.onRMI_Recv(rmi_ctx, packetType, "");
                break;
                
            default:
                System.out.println("이 그룹에 존재하지 않는 RMI_ 콜. 패킷 무시 및 접속 차단");
                //if(rmi_id.getTCP_Object()!=null)
                    //((Channel)rmi_id.getTCP_Object()).close();
                break;
        }
        } catch (Exception e) {
        System.out.println("Packet파싱중 에러발생! 데이터 손상! rmi_ctx="+rmi_ctx+" packetType="+packetType+" RMIdata="+RMIdata.length + "\n" + e.toString());
        //rmi_id.getTCP_Object().close();
        }
    }

//송신 처리!
////////////////////////////////////////////////////////////////////////
    public static void pingCheck_Request(RMI_ID rmi_id, short rmi_ctx, float timeData) {
        pingCheck_Request _data = new pingCheck_Request();
        _data.timeData = timeData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pingCheck_Request, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pingCheck_Request, "");
    }

    public static void pingCheck_Request(RMI_ID[] rmi_id, short rmi_ctx, float timeData) {
        pingCheck_Request _data = new pingCheck_Request();
        _data.timeData = timeData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pingCheck_Request, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pingCheck_Request, "");
    }

    public static void heartBeatCheck_Request(RMI_ID rmi_id, short rmi_ctx, float timeData) {
        heartBeatCheck_Request _data = new heartBeatCheck_Request();
        _data.timeData = timeData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.heartBeatCheck_Request, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.heartBeatCheck_Request, "");
    }

    public static void heartBeatCheck_Request(RMI_ID[] rmi_id, short rmi_ctx, float timeData) {
        heartBeatCheck_Request _data = new heartBeatCheck_Request();
        _data.timeData = timeData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.heartBeatCheck_Request, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.heartBeatCheck_Request, "");
    }

    public static void requestLogin(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        requestLogin _data = new requestLogin();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestLogin, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.requestLogin, "");
    }

    public static void requestLogin(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        requestLogin _data = new requestLogin();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestLogin, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.requestLogin, "");
    }

    public static void requestMatching(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, int selectedMapType) {
        requestMatching _data = new requestMatching();
        _data.googleIDToken = googleIDToken;
        _data.selectedMapType = selectedMapType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestMatching, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.requestMatching, "");
    }

    public static void requestMatching(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, int selectedMapType) {
        requestMatching _data = new requestMatching();
        _data.googleIDToken = googleIDToken;
        _data.selectedMapType = selectedMapType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestMatching, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.requestMatching, "");
    }

    public static void cancelMatching(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        cancelMatching _data = new cancelMatching();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.cancelMatching, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.cancelMatching, "");
    }

    public static void cancelMatching(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        cancelMatching _data = new cancelMatching();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.cancelMatching, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.cancelMatching, "");
    }

    public static void pickReady(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickReady _data = new pickReady();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickReady, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickReady, "");
    }

    public static void pickReady(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickReady _data = new pickReady();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickReady, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickReady, "");
    }

    public static void pickCancel(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickCancel _data = new pickCancel();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickCancel, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickCancel, "");
    }

    public static void pickCancel(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickCancel _data = new pickCancel();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickCancel, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickCancel, "");
    }

    public static void pickSelectedCharacter(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, int characterType) {
        pickSelectedCharacter _data = new pickSelectedCharacter();
        _data.googleIDToken = googleIDToken;
        _data.characterType = characterType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickSelectedCharacter, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickSelectedCharacter, "");
    }

    public static void pickSelectedCharacter(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, int characterType) {
        pickSelectedCharacter _data = new pickSelectedCharacter();
        _data.googleIDToken = googleIDToken;
        _data.characterType = characterType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickSelectedCharacter, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickSelectedCharacter, "");
    }

    public static void pickSendChat(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, String chatMessage) {
        pickSendChat _data = new pickSendChat();
        _data.googleIDToken = googleIDToken;
        _data.chatMessage = chatMessage;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickSendChat, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickSendChat, "");
    }

    public static void pickSendChat(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, String chatMessage) {
        pickSendChat _data = new pickSendChat();
        _data.googleIDToken = googleIDToken;
        _data.chatMessage = chatMessage;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickSendChat, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickSendChat, "");
    }

    public static void pickLogicIsVoipHostReady(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, boolean isVoipHostReady) {
        pickLogicIsVoipHostReady _data = new pickLogicIsVoipHostReady();
        _data.googleIDToken = googleIDToken;
        _data.isVoipHostReady = isVoipHostReady;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicIsVoipHostReady, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicIsVoipHostReady, "");
    }

    public static void pickLogicIsVoipHostReady(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, boolean isVoipHostReady) {
        pickLogicIsVoipHostReady _data = new pickLogicIsVoipHostReady();
        _data.googleIDToken = googleIDToken;
        _data.isVoipHostReady = isVoipHostReady;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicIsVoipHostReady, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicIsVoipHostReady, "");
    }

    public static void gameSceneLoadingPercentage(RMI_ID rmi_id, short rmi_ctx, int worldMapID, float percentage) {
        gameSceneLoadingPercentage _data = new gameSceneLoadingPercentage();
        _data.worldMapID = worldMapID;
        _data.percentage = percentage;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.gameSceneLoadingPercentage, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.gameSceneLoadingPercentage, "");
    }

    public static void gameSceneLoadingPercentage(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, float percentage) {
        gameSceneLoadingPercentage _data = new gameSceneLoadingPercentage();
        _data.worldMapID = worldMapID;
        _data.percentage = percentage;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.gameSceneLoadingPercentage, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.gameSceneLoadingPercentage, "");
    }

    public static void requestReconnectWorldMap(RMI_ID rmi_id, short rmi_ctx, int worldMapID, String googleIDToken) {
        requestReconnectWorldMap _data = new requestReconnectWorldMap();
        _data.worldMapID = worldMapID;
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestReconnectWorldMap, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.requestReconnectWorldMap, "");
    }

    public static void requestReconnectWorldMap(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, String googleIDToken) {
        requestReconnectWorldMap _data = new requestReconnectWorldMap();
        _data.worldMapID = worldMapID;
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestReconnectWorldMap, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.requestReconnectWorldMap, "");
    }

    public static void moveCharacter(RMI_ID rmi_id, short rmi_ctx, int worldMapID, CharacterMoveData characterMoveData) {
        moveCharacter _data = new moveCharacter();
        _data.worldMapID = worldMapID;
        _data.characterMoveData = characterMoveData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.moveCharacter, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.moveCharacter, "");
    }

    public static void moveCharacter(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, CharacterMoveData characterMoveData) {
        moveCharacter _data = new moveCharacter();
        _data.worldMapID = worldMapID;
        _data.characterMoveData = characterMoveData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.moveCharacter, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.moveCharacter, "");
    }

    public static void doAttack(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int attackerEntityID, int targetEntityID) {
        doAttack _data = new doAttack();
        _data.worldMapID = worldMapID;
        _data.attackerEntityID = attackerEntityID;
        _data.targetEntityID = targetEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.doAttack, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.doAttack, "");
    }

    public static void doAttack(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int attackerEntityID, int targetEntityID) {
        doAttack _data = new doAttack();
        _data.worldMapID = worldMapID;
        _data.attackerEntityID = attackerEntityID;
        _data.targetEntityID = targetEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.doAttack, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.doAttack, "");
    }

    public static void useSkill(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, float directionX, float directionY, float directionZ, float distanceRate, short skillSlotNum, int targetEntityID) {
        useSkill _data = new useSkill();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.directionX = directionX;
        _data.directionY = directionY;
        _data.directionZ = directionZ;
        _data.distanceRate = distanceRate;
        _data.skillSlotNum = skillSlotNum;
        _data.targetEntityID = targetEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.useSkill, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.useSkill, "");
    }

    public static void useSkill(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, float directionX, float directionY, float directionZ, float distanceRate, short skillSlotNum, int targetEntityID) {
        useSkill _data = new useSkill();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.directionX = directionX;
        _data.directionY = directionY;
        _data.directionZ = directionZ;
        _data.distanceRate = distanceRate;
        _data.skillSlotNum = skillSlotNum;
        _data.targetEntityID = targetEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.useSkill, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.useSkill, "");
    }

    public static void stopUsingSkill(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum) {
        stopUsingSkill _data = new stopUsingSkill();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.skillSlotNum = skillSlotNum;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.stopUsingSkill, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.stopUsingSkill, "");
    }

    public static void stopUsingSkill(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum) {
        stopUsingSkill _data = new stopUsingSkill();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.skillSlotNum = skillSlotNum;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.stopUsingSkill, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.stopUsingSkill, "");
    }

    public static void upgradeSkill(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum) {
        upgradeSkill _data = new upgradeSkill();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.skillSlotNum = skillSlotNum;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeSkill, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeSkill, "");
    }

    public static void upgradeSkill(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum) {
        upgradeSkill _data = new upgradeSkill();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.skillSlotNum = skillSlotNum;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeSkill, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeSkill, "");
    }

    public static void getSkill(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum, int skillType) {
        getSkill _data = new getSkill();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.skillSlotNum = skillSlotNum;
        _data.skillType = skillType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.getSkill, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.getSkill, "");
    }

    public static void getSkill(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short skillSlotNum, int skillType) {
        getSkill _data = new getSkill();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.skillSlotNum = skillSlotNum;
        _data.skillType = skillType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.getSkill, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.getSkill, "");
    }

    public static void requestStoreUpgradeBuff(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int storeUpgradeType, int buffLevel) {
        requestStoreUpgradeBuff _data = new requestStoreUpgradeBuff();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.storeUpgradeType = storeUpgradeType;
        _data.buffLevel = buffLevel;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestStoreUpgradeBuff, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.requestStoreUpgradeBuff, "");
    }

    public static void requestStoreUpgradeBuff(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int storeUpgradeType, int buffLevel) {
        requestStoreUpgradeBuff _data = new requestStoreUpgradeBuff();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.storeUpgradeType = storeUpgradeType;
        _data.buffLevel = buffLevel;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.requestStoreUpgradeBuff, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.requestStoreUpgradeBuff, "");
    }

    public static void installTurret(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretType, int areaNumber) {
        installTurret _data = new installTurret();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.turretType = turretType;
        _data.areaNumber = areaNumber;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.installTurret, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.installTurret, "");
    }

    public static void installTurret(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretType, int areaNumber) {
        installTurret _data = new installTurret();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.turretType = turretType;
        _data.areaNumber = areaNumber;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.installTurret, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.installTurret, "");
    }

    public static void upgradeTurret(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretEntityID, int turretType) {
        upgradeTurret _data = new upgradeTurret();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.turretEntityID = turretEntityID;
        _data.turretType = turretType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeTurret, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeTurret, "");
    }

    public static void upgradeTurret(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int turretEntityID, int turretType) {
        upgradeTurret _data = new upgradeTurret();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.turretEntityID = turretEntityID;
        _data.turretType = turretType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeTurret, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeTurret, "");
    }

    public static void installBarricade(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int barricadeType, int areaNumber) {
        installBarricade _data = new installBarricade();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.barricadeType = barricadeType;
        _data.areaNumber = areaNumber;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.installBarricade, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.installBarricade, "");
    }

    public static void installBarricade(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int barricadeType, int areaNumber) {
        installBarricade _data = new installBarricade();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.barricadeType = barricadeType;
        _data.areaNumber = areaNumber;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.installBarricade, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.installBarricade, "");
    }

    public static void upgradeBarricade(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int barricadeEntityID, int barricadeType) {
        upgradeBarricade _data = new upgradeBarricade();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.barricadeEntityID = barricadeEntityID;
        _data.barricadeType = barricadeType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeBarricade, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeBarricade, "");
    }

    public static void upgradeBarricade(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, int barricadeEntityID, int barricadeType) {
        upgradeBarricade _data = new upgradeBarricade();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.barricadeEntityID = barricadeEntityID;
        _data.barricadeType = barricadeType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.upgradeBarricade, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.upgradeBarricade, "");
    }

    public static void buyItem(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, int itemType, short itemCount) {
        buyItem _data = new buyItem();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.itemSlotNum = itemSlotNum;
        _data.itemType = itemType;
        _data.itemCount = itemCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.buyItem, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.buyItem, "");
    }

    public static void buyItem(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, int itemType, short itemCount) {
        buyItem _data = new buyItem();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.itemSlotNum = itemSlotNum;
        _data.itemType = itemType;
        _data.itemCount = itemCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.buyItem, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.buyItem, "");
    }

    public static void sellItem(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, int itemType, short itemCount) {
        sellItem _data = new sellItem();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.itemSlotNum = itemSlotNum;
        _data.itemType = itemType;
        _data.itemCount = itemCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.sellItem, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.sellItem, "");
    }

    public static void sellItem(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, int itemType, short itemCount) {
        sellItem _data = new sellItem();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.itemSlotNum = itemSlotNum;
        _data.itemType = itemType;
        _data.itemCount = itemCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.sellItem, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.sellItem, "");
    }

    public static void useItem(RMI_ID rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, short itemCount) {
        useItem _data = new useItem();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.itemSlotNum = itemSlotNum;
        _data.itemCount = itemCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.useItem, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.useItem, "");
    }

    public static void useItem(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, int userEntityID, short itemSlotNum, short itemCount) {
        useItem _data = new useItem();
        _data.worldMapID = worldMapID;
        _data.userEntityID = userEntityID;
        _data.itemSlotNum = itemSlotNum;
        _data.itemCount = itemCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.useItem, RMIdata);
        RMI_.onRMI_Call(rmi_ctx, RMI_PacketType.useItem, "");
    }

}
