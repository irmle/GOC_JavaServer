package Network.RMI_Common;

import Network.RMI_LogicMessages.server_to_client.*;
import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import Network.RMI_Classes.*;
import Network.*;


import java.util.LinkedList;

public class server_to_client {


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

            case RMI_PacketType.pingCheck_Response:
                pingCheck_Response r0 = pingCheck_Response.createpingCheck_Response(RMIdata);
                Logic_pingCheck_Response.RMI_Packet(rmi_id, rmi_ctx, r0.timeData);
                RMI.onRMI_Recv(rmi_ctx, packetType, r0);
                r0 = null;
                break;
            case RMI_PacketType.heartBeatCheck_Response:
                heartBeatCheck_Response r1 = heartBeatCheck_Response.createheartBeatCheck_Response(RMIdata);
                Logic_heartBeatCheck_Response.RMI_Packet(rmi_id, rmi_ctx, r1.timeData);
                RMI.onRMI_Recv(rmi_ctx, packetType, r1);
                r1 = null;
                break;
            case RMI_PacketType.loginOK:
                Logic_loginOK.RMI_Packet(rmi_id, rmi_ctx);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.completeMatching:
                completeMatching r3 = completeMatching.createcompleteMatching(RMIdata);
                Logic_completeMatching.RMI_Packet(rmi_id, rmi_ctx, r3.worldMapID, r3.ipAddress, r3.port);
                RMI.onRMI_Recv(rmi_ctx, packetType, r3);
                r3 = null;
                break;
            case RMI_PacketType.errorMatching:
                errorMatching r4 = errorMatching.createerrorMatching(RMIdata);
                Logic_errorMatching.RMI_Packet(rmi_id, rmi_ctx, r4.errorCode, r4.errorReason);
                RMI.onRMI_Recv(rmi_ctx, packetType, r4);
                r4 = null;
                break;
            case RMI_PacketType.broadcastingCurrentMatchingPlayerCount:
                broadcastingCurrentMatchingPlayerCount r5 = broadcastingCurrentMatchingPlayerCount.createbroadcastingCurrentMatchingPlayerCount(RMIdata);
                Logic_broadcastingCurrentMatchingPlayerCount.RMI_Packet(rmi_id, rmi_ctx, r5.matchingPlayerCount);
                RMI.onRMI_Recv(rmi_ctx, packetType, r5);
                r5 = null;
                break;
            case RMI_PacketType.pickLogicStart:
                pickLogicStart r6 = pickLogicStart.createpickLogicStart(RMIdata);
                Logic_pickLogicStart.RMI_Packet(rmi_id, rmi_ctx, r6.loadingPlayerList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r6);
                r6 = null;
                break;
            case RMI_PacketType.pickLogicTime:
                pickLogicTime r7 = pickLogicTime.createpickLogicTime(RMIdata);
                Logic_pickLogicTime.RMI_Packet(rmi_id, rmi_ctx, r7.remainCharacterPickTime);
                RMI.onRMI_Recv(rmi_ctx, packetType, r7);
                r7 = null;
                break;
            case RMI_PacketType.pickLogicUserOnSelectedCharacter:
                pickLogicUserOnSelectedCharacter r8 = pickLogicUserOnSelectedCharacter.createpickLogicUserOnSelectedCharacter(RMIdata);
                Logic_pickLogicUserOnSelectedCharacter.RMI_Packet(rmi_id, rmi_ctx, r8.googleIDToken, r8.characterType);
                RMI.onRMI_Recv(rmi_ctx, packetType, r8);
                r8 = null;
                break;
            case RMI_PacketType.pickLogicUserOnReady:
                pickLogicUserOnReady r9 = pickLogicUserOnReady.createpickLogicUserOnReady(RMIdata);
                Logic_pickLogicUserOnReady.RMI_Packet(rmi_id, rmi_ctx, r9.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, r9);
                r9 = null;
                break;
            case RMI_PacketType.pickLogicUserOnCancel:
                pickLogicUserOnCancel r10 = pickLogicUserOnCancel.createpickLogicUserOnCancel(RMIdata);
                Logic_pickLogicUserOnCancel.RMI_Packet(rmi_id, rmi_ctx, r10.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, r10);
                r10 = null;
                break;
            case RMI_PacketType.pickLogicUserOnDisconnected:
                pickLogicUserOnDisconnected r11 = pickLogicUserOnDisconnected.createpickLogicUserOnDisconnected(RMIdata);
                Logic_pickLogicUserOnDisconnected.RMI_Packet(rmi_id, rmi_ctx, r11.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, r11);
                r11 = null;
                break;
            case RMI_PacketType.pickLogicUserOnChatMessage:
                pickLogicUserOnChatMessage r12 = pickLogicUserOnChatMessage.createpickLogicUserOnChatMessage(RMIdata);
                Logic_pickLogicUserOnChatMessage.RMI_Packet(rmi_id, rmi_ctx, r12.googleIDToken, r12.chatMessage);
                RMI.onRMI_Recv(rmi_ctx, packetType, r12);
                r12 = null;
                break;
            case RMI_PacketType.pickLogicIsVoipHost:
                pickLogicIsVoipHost r13 = pickLogicIsVoipHost.createpickLogicIsVoipHost(RMIdata);
                Logic_pickLogicIsVoipHost.RMI_Packet(rmi_id, rmi_ctx, r13.isVoipHost, r13.worldMapID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r13);
                r13 = null;
                break;
            case RMI_PacketType.pickLogicConnectToVoipHost:
                pickLogicConnectToVoipHost r14 = pickLogicConnectToVoipHost.createpickLogicConnectToVoipHost(RMIdata);
                Logic_pickLogicConnectToVoipHost.RMI_Packet(rmi_id, rmi_ctx, r14.isVoipOK, r14.worldMapID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r14);
                r14 = null;
                break;
            case RMI_PacketType.reconnectingWorldMap:
                reconnectingWorldMap r15 = reconnectingWorldMap.createreconnectingWorldMap(RMIdata);
                Logic_reconnectingWorldMap.RMI_Packet(rmi_id, rmi_ctx, r15.worldMapID, r15.loadingPlayerList, r15.ipAddress, r15.port);
                RMI.onRMI_Recv(rmi_ctx, packetType, r15);
                r15 = null;
                break;
            case RMI_PacketType.broadcastingLoadingProgress:
                broadcastingLoadingProgress r16 = broadcastingLoadingProgress.createbroadcastingLoadingProgress(RMIdata);
                Logic_broadcastingLoadingProgress.RMI_Packet(rmi_id, rmi_ctx, r16.loadingPlayerList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r16);
                r16 = null;
                break;
            case RMI_PacketType.initializeMyselfCharacterInfo:
                initializeMyselfCharacterInfo r17 = initializeMyselfCharacterInfo.createinitializeMyselfCharacterInfo(RMIdata);
                Logic_initializeMyselfCharacterInfo.RMI_Packet(rmi_id, rmi_ctx, r17.ownUserEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r17);
                r17 = null;
                break;
            case RMI_PacketType.initializeWorldMap:
                initializeWorldMap r18 = initializeWorldMap.createinitializeWorldMap(RMIdata);
                Logic_initializeWorldMap.RMI_Packet(rmi_id, rmi_ctx, r18.characterList, r18.monsterList, r18.buffTurretList, r18.attackTurretList, r18.barricadeList, r18.crystalList, r18.skillObjectList, r18.flyingObjectList, r18.buildSlotList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r18);
                r18 = null;
                break;
            case RMI_PacketType.userDisconnected:
                userDisconnected r19 = userDisconnected.createuserDisconnected(RMIdata);
                Logic_userDisconnected.RMI_Packet(rmi_id, rmi_ctx, r19.userEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r19);
                r19 = null;
                break;
            case RMI_PacketType.userReconnected:
                userReconnected r20 = userReconnected.createuserReconnected(RMIdata);
                Logic_userReconnected.RMI_Packet(rmi_id, rmi_ctx, r20.userEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r20);
                r20 = null;
                break;
            case RMI_PacketType.userCharacterDefeat:
                userCharacterDefeat r21 = userCharacterDefeat.createuserCharacterDefeat(RMIdata);
                Logic_userCharacterDefeat.RMI_Packet(rmi_id, rmi_ctx, r21.userEntityID, r21.remainTimeMilliSeconds);
                RMI.onRMI_Recv(rmi_ctx, packetType, r21);
                r21 = null;
                break;
            case RMI_PacketType.userCharacterRespawn:
                userCharacterRespawn r22 = userCharacterRespawn.createuserCharacterRespawn(RMIdata);
                Logic_userCharacterRespawn.RMI_Packet(rmi_id, rmi_ctx, r22.userEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r22);
                r22 = null;
                break;
            case RMI_PacketType.userFailedBuyItem:
                userFailedBuyItem r23 = userFailedBuyItem.createuserFailedBuyItem(RMIdata);
                Logic_userFailedBuyItem.RMI_Packet(rmi_id, rmi_ctx, r23.errorCode);
                RMI.onRMI_Recv(rmi_ctx, packetType, r23);
                r23 = null;
                break;
            case RMI_PacketType.userFailedUseItem:
                userFailedUseItem r24 = userFailedUseItem.createuserFailedUseItem(RMIdata);
                Logic_userFailedUseItem.RMI_Packet(rmi_id, rmi_ctx, r24.errorCode);
                RMI.onRMI_Recv(rmi_ctx, packetType, r24);
                r24 = null;
                break;
            case RMI_PacketType.userFailedInstallBuilding:
                userFailedInstallBuilding r25 = userFailedInstallBuilding.createuserFailedInstallBuilding(RMIdata);
                Logic_userFailedInstallBuilding.RMI_Packet(rmi_id, rmi_ctx, r25.errorCode);
                RMI.onRMI_Recv(rmi_ctx, packetType, r25);
                r25 = null;
                break;
            case RMI_PacketType.userFailedUpgradeBuilding:
                userFailedUpgradeBuilding r26 = userFailedUpgradeBuilding.createuserFailedUpgradeBuilding(RMIdata);
                Logic_userFailedUpgradeBuilding.RMI_Packet(rmi_id, rmi_ctx, r26.errorCode);
                RMI.onRMI_Recv(rmi_ctx, packetType, r26);
                r26 = null;
                break;
            case RMI_PacketType.userSucceedInstallBuilding:
                userSucceedInstallBuilding r27 = userSucceedInstallBuilding.createuserSucceedInstallBuilding(RMIdata);
                Logic_userSucceedInstallBuilding.RMI_Packet(rmi_id, rmi_ctx, r27.builderEntityID, r27.buildingType);
                RMI.onRMI_Recv(rmi_ctx, packetType, r27);
                r27 = null;
                break;
            case RMI_PacketType.userSucceedStoreUpgradeBuff:
                userSucceedStoreUpgradeBuff r28 = userSucceedStoreUpgradeBuff.createuserSucceedStoreUpgradeBuff(RMIdata);
                Logic_userSucceedStoreUpgradeBuff.RMI_Packet(rmi_id, rmi_ctx, r28.storeUpgradeBuffSlotData);
                RMI.onRMI_Recv(rmi_ctx, packetType, r28);
                r28 = null;
                break;
            case RMI_PacketType.broadcastingStoreUpgradeBuffList:
                broadcastingStoreUpgradeBuffList r29 = broadcastingStoreUpgradeBuffList.createbroadcastingStoreUpgradeBuffList(RMIdata);
                Logic_broadcastingStoreUpgradeBuffList.RMI_Packet(rmi_id, rmi_ctx, r29.storeUpgradeBuffSlotDataList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r29);
                r29 = null;
                break;
            case RMI_PacketType.StartGame:
                Logic_StartGame.RMI_Packet(rmi_id, rmi_ctx);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.StartWave:
                StartWave r31 = StartWave.createStartWave(RMIdata);
                Logic_StartWave.RMI_Packet(rmi_id, rmi_ctx, r31.waveCount);
                RMI.onRMI_Recv(rmi_ctx, packetType, r31);
                r31 = null;
                break;
            case RMI_PacketType.EndWave:
                EndWave r32 = EndWave.createEndWave(RMIdata);
                Logic_EndWave.RMI_Packet(rmi_id, rmi_ctx, r32.waveCount);
                RMI.onRMI_Recv(rmi_ctx, packetType, r32);
                r32 = null;
                break;
            case RMI_PacketType.EndGame:
                EndGame r33 = EndGame.createEndGame(RMIdata);
                Logic_EndGame.RMI_Packet(rmi_id, rmi_ctx, r33.resultCode, r33.resultJS);
                RMI.onRMI_Recv(rmi_ctx, packetType, r33);
                r33 = null;
                break;
            case RMI_PacketType.motionCharacterDoAttack:
                motionCharacterDoAttack r34 = motionCharacterDoAttack.createmotionCharacterDoAttack(RMIdata);
                Logic_motionCharacterDoAttack.RMI_Packet(rmi_id, rmi_ctx, r34.characterEntityID, r34.targetEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r34);
                r34 = null;
                break;
            case RMI_PacketType.motionCharacterUseSkill:
                motionCharacterUseSkill r35 = motionCharacterUseSkill.createmotionCharacterUseSkill(RMIdata);
                Logic_motionCharacterUseSkill.RMI_Packet(rmi_id, rmi_ctx, r35.characterEntityID, r35.usedSkill);
                RMI.onRMI_Recv(rmi_ctx, packetType, r35);
                r35 = null;
                break;
            case RMI_PacketType.motionCharacterCancelSkill:
                motionCharacterCancelSkill r36 = motionCharacterCancelSkill.createmotionCharacterCancelSkill(RMIdata);
                Logic_motionCharacterCancelSkill.RMI_Packet(rmi_id, rmi_ctx, r36.characterEntityID, r36.usedSkill);
                RMI.onRMI_Recv(rmi_ctx, packetType, r36);
                r36 = null;
                break;
            case RMI_PacketType.motionCharacterUseItem:
                motionCharacterUseItem r37 = motionCharacterUseItem.createmotionCharacterUseItem(RMIdata);
                Logic_motionCharacterUseItem.RMI_Packet(rmi_id, rmi_ctx, r37.characterEntityID, r37.usedItem);
                RMI.onRMI_Recv(rmi_ctx, packetType, r37);
                r37 = null;
                break;
            case RMI_PacketType.motionMonsterDoAttack:
                motionMonsterDoAttack r38 = motionMonsterDoAttack.createmotionMonsterDoAttack(RMIdata);
                Logic_motionMonsterDoAttack.RMI_Packet(rmi_id, rmi_ctx, r38.monsterEntityID, r38.targetEntityType, r38.targetEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, r38);
                r38 = null;
                break;
            case RMI_PacketType.motionMonsterUseSkill:
                motionMonsterUseSkill r39 = motionMonsterUseSkill.createmotionMonsterUseSkill(RMIdata);
                Logic_motionMonsterUseSkill.RMI_Packet(rmi_id, rmi_ctx, r39.monsterEntityID, r39.usedSkill);
                RMI.onRMI_Recv(rmi_ctx, packetType, r39);
                r39 = null;
                break;
            case RMI_PacketType.broadcastingCharacterSnapshot:
                broadcastingCharacterSnapshot r40 = broadcastingCharacterSnapshot.createbroadcastingCharacterSnapshot(RMIdata);
                Logic_broadcastingCharacterSnapshot.RMI_Packet(rmi_id, rmi_ctx, r40.characterSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, r40);
                r40 = null;
                break;
            case RMI_PacketType.broadcastingMonsterSnapshot:
                broadcastingMonsterSnapshot r41 = broadcastingMonsterSnapshot.createbroadcastingMonsterSnapshot(RMIdata);
                Logic_broadcastingMonsterSnapshot.RMI_Packet(rmi_id, rmi_ctx, r41.monsterSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, r41);
                r41 = null;
                break;
            case RMI_PacketType.broadcastingBuffTurretSnapshot:
                broadcastingBuffTurretSnapshot r42 = broadcastingBuffTurretSnapshot.createbroadcastingBuffTurretSnapshot(RMIdata);
                Logic_broadcastingBuffTurretSnapshot.RMI_Packet(rmi_id, rmi_ctx, r42.buffTurretSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, r42);
                r42 = null;
                break;
            case RMI_PacketType.broadcastingAttackTurretSnapshot:
                broadcastingAttackTurretSnapshot r43 = broadcastingAttackTurretSnapshot.createbroadcastingAttackTurretSnapshot(RMIdata);
                Logic_broadcastingAttackTurretSnapshot.RMI_Packet(rmi_id, rmi_ctx, r43.attackTurretSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, r43);
                r43 = null;
                break;
            case RMI_PacketType.broadcastingBarricadeSnapshot:
                broadcastingBarricadeSnapshot r44 = broadcastingBarricadeSnapshot.createbroadcastingBarricadeSnapshot(RMIdata);
                Logic_broadcastingBarricadeSnapshot.RMI_Packet(rmi_id, rmi_ctx, r44.barricadeSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, r44);
                r44 = null;
                break;
            case RMI_PacketType.broadcastingCrystalSnapshot:
                broadcastingCrystalSnapshot r45 = broadcastingCrystalSnapshot.createbroadcastingCrystalSnapshot(RMIdata);
                Logic_broadcastingCrystalSnapshot.RMI_Packet(rmi_id, rmi_ctx, r45.crystalSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, r45);
                r45 = null;
                break;
            case RMI_PacketType.broadcastingSkillObjectSnapshot:
                broadcastingSkillObjectSnapshot r46 = broadcastingSkillObjectSnapshot.createbroadcastingSkillObjectSnapshot(RMIdata);
                Logic_broadcastingSkillObjectSnapshot.RMI_Packet(rmi_id, rmi_ctx, r46.skillObjectSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, r46);
                r46 = null;
                break;
            case RMI_PacketType.broadcastingFlyingObjectSnapshot:
                broadcastingFlyingObjectSnapshot r47 = broadcastingFlyingObjectSnapshot.createbroadcastingFlyingObjectSnapshot(RMIdata);
                Logic_broadcastingFlyingObjectSnapshot.RMI_Packet(rmi_id, rmi_ctx, r47.flyingObjectSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, r47);
                r47 = null;
                break;
            case RMI_PacketType.broadcastingBuildSlotSnapshot:
                broadcastingBuildSlotSnapshot r48 = broadcastingBuildSlotSnapshot.createbroadcastingBuildSlotSnapshot(RMIdata);
                Logic_broadcastingBuildSlotSnapshot.RMI_Packet(rmi_id, rmi_ctx, r48.buildSlotList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r48);
                r48 = null;
                break;
            case RMI_PacketType.broadcastingGameWorldStatusSnapshot:
                broadcastingGameWorldStatusSnapshot r49 = broadcastingGameWorldStatusSnapshot.createbroadcastingGameWorldStatusSnapshot(RMIdata);
                Logic_broadcastingGameWorldStatusSnapshot.RMI_Packet(rmi_id, rmi_ctx, r49.gameworldStatusSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, r49);
                r49 = null;
                break;
            case RMI_PacketType.broadcastingDamageAmount:
                broadcastingDamageAmount r50 = broadcastingDamageAmount.createbroadcastingDamageAmount(RMIdata);
                Logic_broadcastingDamageAmount.RMI_Packet(rmi_id, rmi_ctx, r50.damageType, r50.entityID, r50.damageAmount);
                RMI.onRMI_Recv(rmi_ctx, packetType, r50);
                r50 = null;
                break;
            case RMI_PacketType.createWorldMapCharacterEntityInfo:
                createWorldMapCharacterEntityInfo r51 = createWorldMapCharacterEntityInfo.createcreateWorldMapCharacterEntityInfo(RMIdata);
                Logic_createWorldMapCharacterEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r51.characterList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r51);
                r51 = null;
                break;
            case RMI_PacketType.createWorldMapMonsterEntityInfo:
                createWorldMapMonsterEntityInfo r52 = createWorldMapMonsterEntityInfo.createcreateWorldMapMonsterEntityInfo(RMIdata);
                Logic_createWorldMapMonsterEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r52.monsterList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r52);
                r52 = null;
                break;
            case RMI_PacketType.createWorldMapBuffTurretEntityInfo:
                createWorldMapBuffTurretEntityInfo r53 = createWorldMapBuffTurretEntityInfo.createcreateWorldMapBuffTurretEntityInfo(RMIdata);
                Logic_createWorldMapBuffTurretEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r53.buffTurretList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r53);
                r53 = null;
                break;
            case RMI_PacketType.createWorldMapAttackTurretEntityInfo:
                createWorldMapAttackTurretEntityInfo r54 = createWorldMapAttackTurretEntityInfo.createcreateWorldMapAttackTurretEntityInfo(RMIdata);
                Logic_createWorldMapAttackTurretEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r54.attackTurretList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r54);
                r54 = null;
                break;
            case RMI_PacketType.createWorldMapBarricadeEntityInfo:
                createWorldMapBarricadeEntityInfo r55 = createWorldMapBarricadeEntityInfo.createcreateWorldMapBarricadeEntityInfo(RMIdata);
                Logic_createWorldMapBarricadeEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r55.barricadeList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r55);
                r55 = null;
                break;
            case RMI_PacketType.createWorldMapCrystalDataEntityInfo:
                createWorldMapCrystalDataEntityInfo r56 = createWorldMapCrystalDataEntityInfo.createcreateWorldMapCrystalDataEntityInfo(RMIdata);
                Logic_createWorldMapCrystalDataEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r56.crystalList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r56);
                r56 = null;
                break;
            case RMI_PacketType.createWorldMapSkillObjectEntityInfo:
                createWorldMapSkillObjectEntityInfo r57 = createWorldMapSkillObjectEntityInfo.createcreateWorldMapSkillObjectEntityInfo(RMIdata);
                Logic_createWorldMapSkillObjectEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r57.skillObjectList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r57);
                r57 = null;
                break;
            case RMI_PacketType.createWorldMapFlyingObjectEntityInfo:
                createWorldMapFlyingObjectEntityInfo r58 = createWorldMapFlyingObjectEntityInfo.createcreateWorldMapFlyingObjectEntityInfo(RMIdata);
                Logic_createWorldMapFlyingObjectEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r58.flyingObjectList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r58);
                r58 = null;
                break;
            case RMI_PacketType.destroyWorldMapEntityInfo:
                destroyWorldMapEntityInfo r59 = destroyWorldMapEntityInfo.createdestroyWorldMapEntityInfo(RMIdata);
                Logic_destroyWorldMapEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r59.destroyedEntityList);
                RMI.onRMI_Recv(rmi_ctx, packetType, r59);
                r59 = null;
                break;
                
            default:
                System.out.println("[server_to_client] 그룹에 존재하지 않는 RMI콜. 패킷 무시.");
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
    public static void pingCheck_Response(RMI_ID rmi_id, short rmi_ctx, float timeData) {
        pingCheck_Response _data__1 = new pingCheck_Response();
        _data__1.timeData = timeData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pingCheck_Response, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pingCheck_Response, _data__1);
        _data__1 = null;
    }

    public static void pingCheck_Response(RMI_ID[] rmi_id, short rmi_ctx, float timeData) {
        pingCheck_Response _data__1 = new pingCheck_Response();
        _data__1.timeData = timeData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pingCheck_Response, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pingCheck_Response, _data__1);
        _data__1 = null;
    }

    public static void heartBeatCheck_Response(RMI_ID rmi_id, short rmi_ctx, float timeData) {
        heartBeatCheck_Response _data__1 = new heartBeatCheck_Response();
        _data__1.timeData = timeData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.heartBeatCheck_Response, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.heartBeatCheck_Response, _data__1);
        _data__1 = null;
    }

    public static void heartBeatCheck_Response(RMI_ID[] rmi_id, short rmi_ctx, float timeData) {
        heartBeatCheck_Response _data__1 = new heartBeatCheck_Response();
        _data__1.timeData = timeData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.heartBeatCheck_Response, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.heartBeatCheck_Response, _data__1);
        _data__1 = null;
    }

    public static void loginOK(RMI_ID rmi_id, short rmi_ctx) {
        loginOK _data__1 = new loginOK();
        byte[] RMIdata = new byte[1];
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.loginOK, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.loginOK, _data__1);
        _data__1 = null;
    }

    public static void loginOK(RMI_ID[] rmi_id, short rmi_ctx) {
        loginOK _data__1 = new loginOK();
        byte[] RMIdata = new byte[1];
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.loginOK, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.loginOK, _data__1);
        _data__1 = null;
    }

    public static void completeMatching(RMI_ID rmi_id, short rmi_ctx, int worldMapID, String ipAddress, int port) {
        completeMatching _data__1 = new completeMatching();
        _data__1.worldMapID = worldMapID;
        _data__1.ipAddress = ipAddress;
        _data__1.port = port;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.completeMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.completeMatching, _data__1);
        _data__1 = null;
    }

    public static void completeMatching(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, String ipAddress, int port) {
        completeMatching _data__1 = new completeMatching();
        _data__1.worldMapID = worldMapID;
        _data__1.ipAddress = ipAddress;
        _data__1.port = port;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.completeMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.completeMatching, _data__1);
        _data__1 = null;
    }

    public static void errorMatching(RMI_ID rmi_id, short rmi_ctx, int errorCode, String errorReason) {
        errorMatching _data__1 = new errorMatching();
        _data__1.errorCode = errorCode;
        _data__1.errorReason = errorReason;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.errorMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.errorMatching, _data__1);
        _data__1 = null;
    }

    public static void errorMatching(RMI_ID[] rmi_id, short rmi_ctx, int errorCode, String errorReason) {
        errorMatching _data__1 = new errorMatching();
        _data__1.errorCode = errorCode;
        _data__1.errorReason = errorReason;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.errorMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.errorMatching, _data__1);
        _data__1 = null;
    }

    public static void broadcastingCurrentMatchingPlayerCount(RMI_ID rmi_id, short rmi_ctx, int matchingPlayerCount) {
        broadcastingCurrentMatchingPlayerCount _data__1 = new broadcastingCurrentMatchingPlayerCount();
        _data__1.matchingPlayerCount = matchingPlayerCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCurrentMatchingPlayerCount, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCurrentMatchingPlayerCount, _data__1);
        _data__1 = null;
    }

    public static void broadcastingCurrentMatchingPlayerCount(RMI_ID[] rmi_id, short rmi_ctx, int matchingPlayerCount) {
        broadcastingCurrentMatchingPlayerCount _data__1 = new broadcastingCurrentMatchingPlayerCount();
        _data__1.matchingPlayerCount = matchingPlayerCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCurrentMatchingPlayerCount, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCurrentMatchingPlayerCount, _data__1);
        _data__1 = null;
    }

    public static void pickLogicStart(RMI_ID rmi_id, short rmi_ctx, LinkedList<LoadingPlayerData> loadingPlayerList) {
        pickLogicStart _data__1 = new pickLogicStart();
        _data__1.loadingPlayerList = loadingPlayerList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicStart, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicStart, _data__1);
        _data__1 = null;
    }

    public static void pickLogicStart(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<LoadingPlayerData> loadingPlayerList) {
        pickLogicStart _data__1 = new pickLogicStart();
        _data__1.loadingPlayerList = loadingPlayerList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicStart, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicStart, _data__1);
        _data__1 = null;
    }

    public static void pickLogicTime(RMI_ID rmi_id, short rmi_ctx, float remainCharacterPickTime) {
        pickLogicTime _data__1 = new pickLogicTime();
        _data__1.remainCharacterPickTime = remainCharacterPickTime;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicTime, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicTime, _data__1);
        _data__1 = null;
    }

    public static void pickLogicTime(RMI_ID[] rmi_id, short rmi_ctx, float remainCharacterPickTime) {
        pickLogicTime _data__1 = new pickLogicTime();
        _data__1.remainCharacterPickTime = remainCharacterPickTime;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicTime, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicTime, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnSelectedCharacter(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, int characterType) {
        pickLogicUserOnSelectedCharacter _data__1 = new pickLogicUserOnSelectedCharacter();
        _data__1.googleIDToken = googleIDToken;
        _data__1.characterType = characterType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnSelectedCharacter, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnSelectedCharacter, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnSelectedCharacter(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, int characterType) {
        pickLogicUserOnSelectedCharacter _data__1 = new pickLogicUserOnSelectedCharacter();
        _data__1.googleIDToken = googleIDToken;
        _data__1.characterType = characterType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnSelectedCharacter, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnSelectedCharacter, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnReady(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnReady _data__1 = new pickLogicUserOnReady();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnReady, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnReady, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnReady(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnReady _data__1 = new pickLogicUserOnReady();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnReady, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnReady, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnCancel(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnCancel _data__1 = new pickLogicUserOnCancel();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnCancel, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnCancel, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnCancel(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnCancel _data__1 = new pickLogicUserOnCancel();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnCancel, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnCancel, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnDisconnected(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnDisconnected _data__1 = new pickLogicUserOnDisconnected();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnDisconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnDisconnected, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnDisconnected(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnDisconnected _data__1 = new pickLogicUserOnDisconnected();
        _data__1.googleIDToken = googleIDToken;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnDisconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnDisconnected, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnChatMessage(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, String chatMessage) {
        pickLogicUserOnChatMessage _data__1 = new pickLogicUserOnChatMessage();
        _data__1.googleIDToken = googleIDToken;
        _data__1.chatMessage = chatMessage;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnChatMessage, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnChatMessage, _data__1);
        _data__1 = null;
    }

    public static void pickLogicUserOnChatMessage(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, String chatMessage) {
        pickLogicUserOnChatMessage _data__1 = new pickLogicUserOnChatMessage();
        _data__1.googleIDToken = googleIDToken;
        _data__1.chatMessage = chatMessage;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnChatMessage, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnChatMessage, _data__1);
        _data__1 = null;
    }

    public static void pickLogicIsVoipHost(RMI_ID rmi_id, short rmi_ctx, boolean isVoipHost, int worldMapID) {
        pickLogicIsVoipHost _data__1 = new pickLogicIsVoipHost();
        _data__1.isVoipHost = isVoipHost;
        _data__1.worldMapID = worldMapID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicIsVoipHost, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicIsVoipHost, _data__1);
        _data__1 = null;
    }

    public static void pickLogicIsVoipHost(RMI_ID[] rmi_id, short rmi_ctx, boolean isVoipHost, int worldMapID) {
        pickLogicIsVoipHost _data__1 = new pickLogicIsVoipHost();
        _data__1.isVoipHost = isVoipHost;
        _data__1.worldMapID = worldMapID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicIsVoipHost, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicIsVoipHost, _data__1);
        _data__1 = null;
    }

    public static void pickLogicConnectToVoipHost(RMI_ID rmi_id, short rmi_ctx, boolean isVoipOK, int worldMapID) {
        pickLogicConnectToVoipHost _data__1 = new pickLogicConnectToVoipHost();
        _data__1.isVoipOK = isVoipOK;
        _data__1.worldMapID = worldMapID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicConnectToVoipHost, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicConnectToVoipHost, _data__1);
        _data__1 = null;
    }

    public static void pickLogicConnectToVoipHost(RMI_ID[] rmi_id, short rmi_ctx, boolean isVoipOK, int worldMapID) {
        pickLogicConnectToVoipHost _data__1 = new pickLogicConnectToVoipHost();
        _data__1.isVoipOK = isVoipOK;
        _data__1.worldMapID = worldMapID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicConnectToVoipHost, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicConnectToVoipHost, _data__1);
        _data__1 = null;
    }

    public static void reconnectingWorldMap(RMI_ID rmi_id, short rmi_ctx, int worldMapID, LinkedList<LoadingPlayerData> loadingPlayerList, String ipAddress, int port) {
        reconnectingWorldMap _data__1 = new reconnectingWorldMap();
        _data__1.worldMapID = worldMapID;
        _data__1.loadingPlayerList = loadingPlayerList;
        _data__1.ipAddress = ipAddress;
        _data__1.port = port;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.reconnectingWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.reconnectingWorldMap, _data__1);
        _data__1 = null;
    }

    public static void reconnectingWorldMap(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, LinkedList<LoadingPlayerData> loadingPlayerList, String ipAddress, int port) {
        reconnectingWorldMap _data__1 = new reconnectingWorldMap();
        _data__1.worldMapID = worldMapID;
        _data__1.loadingPlayerList = loadingPlayerList;
        _data__1.ipAddress = ipAddress;
        _data__1.port = port;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.reconnectingWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.reconnectingWorldMap, _data__1);
        _data__1 = null;
    }

    public static void broadcastingLoadingProgress(RMI_ID rmi_id, short rmi_ctx, LinkedList<LoadingPlayerData> loadingPlayerList) {
        broadcastingLoadingProgress _data__1 = new broadcastingLoadingProgress();
        _data__1.loadingPlayerList = loadingPlayerList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingLoadingProgress, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingLoadingProgress, _data__1);
        _data__1 = null;
    }

    public static void broadcastingLoadingProgress(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<LoadingPlayerData> loadingPlayerList) {
        broadcastingLoadingProgress _data__1 = new broadcastingLoadingProgress();
        _data__1.loadingPlayerList = loadingPlayerList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingLoadingProgress, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingLoadingProgress, _data__1);
        _data__1 = null;
    }

    public static void initializeMyselfCharacterInfo(RMI_ID rmi_id, short rmi_ctx, int ownUserEntityID) {
        initializeMyselfCharacterInfo _data__1 = new initializeMyselfCharacterInfo();
        _data__1.ownUserEntityID = ownUserEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.initializeMyselfCharacterInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.initializeMyselfCharacterInfo, _data__1);
        _data__1 = null;
    }

    public static void initializeMyselfCharacterInfo(RMI_ID[] rmi_id, short rmi_ctx, int ownUserEntityID) {
        initializeMyselfCharacterInfo _data__1 = new initializeMyselfCharacterInfo();
        _data__1.ownUserEntityID = ownUserEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.initializeMyselfCharacterInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.initializeMyselfCharacterInfo, _data__1);
        _data__1 = null;
    }

    public static void initializeWorldMap(RMI_ID rmi_id, short rmi_ctx, LinkedList<CharacterData> characterList, LinkedList<MonsterData> monsterList, LinkedList<BuffTurretData> buffTurretList, LinkedList<AttackTurretData> attackTurretList, LinkedList<BarricadeData> barricadeList, LinkedList<CrystalData> crystalList, LinkedList<SkillObjectData> skillObjectList, LinkedList<FlyingObjectData> flyingObjectList, LinkedList<BuildSlotData> buildSlotList) {
        initializeWorldMap _data__1 = new initializeWorldMap();
        _data__1.characterList = characterList;
        _data__1.monsterList = monsterList;
        _data__1.buffTurretList = buffTurretList;
        _data__1.attackTurretList = attackTurretList;
        _data__1.barricadeList = barricadeList;
        _data__1.crystalList = crystalList;
        _data__1.skillObjectList = skillObjectList;
        _data__1.flyingObjectList = flyingObjectList;
        _data__1.buildSlotList = buildSlotList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.initializeWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.initializeWorldMap, _data__1);
        _data__1 = null;
    }

    public static void initializeWorldMap(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CharacterData> characterList, LinkedList<MonsterData> monsterList, LinkedList<BuffTurretData> buffTurretList, LinkedList<AttackTurretData> attackTurretList, LinkedList<BarricadeData> barricadeList, LinkedList<CrystalData> crystalList, LinkedList<SkillObjectData> skillObjectList, LinkedList<FlyingObjectData> flyingObjectList, LinkedList<BuildSlotData> buildSlotList) {
        initializeWorldMap _data__1 = new initializeWorldMap();
        _data__1.characterList = characterList;
        _data__1.monsterList = monsterList;
        _data__1.buffTurretList = buffTurretList;
        _data__1.attackTurretList = attackTurretList;
        _data__1.barricadeList = barricadeList;
        _data__1.crystalList = crystalList;
        _data__1.skillObjectList = skillObjectList;
        _data__1.flyingObjectList = flyingObjectList;
        _data__1.buildSlotList = buildSlotList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.initializeWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.initializeWorldMap, _data__1);
        _data__1 = null;
    }

    public static void userDisconnected(RMI_ID rmi_id, short rmi_ctx, int userEntityID) {
        userDisconnected _data__1 = new userDisconnected();
        _data__1.userEntityID = userEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userDisconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userDisconnected, _data__1);
        _data__1 = null;
    }

    public static void userDisconnected(RMI_ID[] rmi_id, short rmi_ctx, int userEntityID) {
        userDisconnected _data__1 = new userDisconnected();
        _data__1.userEntityID = userEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userDisconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userDisconnected, _data__1);
        _data__1 = null;
    }

    public static void userReconnected(RMI_ID rmi_id, short rmi_ctx, int userEntityID) {
        userReconnected _data__1 = new userReconnected();
        _data__1.userEntityID = userEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userReconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userReconnected, _data__1);
        _data__1 = null;
    }

    public static void userReconnected(RMI_ID[] rmi_id, short rmi_ctx, int userEntityID) {
        userReconnected _data__1 = new userReconnected();
        _data__1.userEntityID = userEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userReconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userReconnected, _data__1);
        _data__1 = null;
    }

    public static void userCharacterDefeat(RMI_ID rmi_id, short rmi_ctx, int userEntityID, int remainTimeMilliSeconds) {
        userCharacterDefeat _data__1 = new userCharacterDefeat();
        _data__1.userEntityID = userEntityID;
        _data__1.remainTimeMilliSeconds = remainTimeMilliSeconds;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userCharacterDefeat, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userCharacterDefeat, _data__1);
        _data__1 = null;
    }

    public static void userCharacterDefeat(RMI_ID[] rmi_id, short rmi_ctx, int userEntityID, int remainTimeMilliSeconds) {
        userCharacterDefeat _data__1 = new userCharacterDefeat();
        _data__1.userEntityID = userEntityID;
        _data__1.remainTimeMilliSeconds = remainTimeMilliSeconds;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userCharacterDefeat, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userCharacterDefeat, _data__1);
        _data__1 = null;
    }

    public static void userCharacterRespawn(RMI_ID rmi_id, short rmi_ctx, int userEntityID) {
        userCharacterRespawn _data__1 = new userCharacterRespawn();
        _data__1.userEntityID = userEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userCharacterRespawn, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userCharacterRespawn, _data__1);
        _data__1 = null;
    }

    public static void userCharacterRespawn(RMI_ID[] rmi_id, short rmi_ctx, int userEntityID) {
        userCharacterRespawn _data__1 = new userCharacterRespawn();
        _data__1.userEntityID = userEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userCharacterRespawn, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userCharacterRespawn, _data__1);
        _data__1 = null;
    }

    public static void userFailedBuyItem(RMI_ID rmi_id, short rmi_ctx, int errorCode) {
        userFailedBuyItem _data__1 = new userFailedBuyItem();
        _data__1.errorCode = errorCode;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedBuyItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedBuyItem, _data__1);
        _data__1 = null;
    }

    public static void userFailedBuyItem(RMI_ID[] rmi_id, short rmi_ctx, int errorCode) {
        userFailedBuyItem _data__1 = new userFailedBuyItem();
        _data__1.errorCode = errorCode;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedBuyItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedBuyItem, _data__1);
        _data__1 = null;
    }

    public static void userFailedUseItem(RMI_ID rmi_id, short rmi_ctx, int errorCode) {
        userFailedUseItem _data__1 = new userFailedUseItem();
        _data__1.errorCode = errorCode;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedUseItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedUseItem, _data__1);
        _data__1 = null;
    }

    public static void userFailedUseItem(RMI_ID[] rmi_id, short rmi_ctx, int errorCode) {
        userFailedUseItem _data__1 = new userFailedUseItem();
        _data__1.errorCode = errorCode;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedUseItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedUseItem, _data__1);
        _data__1 = null;
    }

    public static void userFailedInstallBuilding(RMI_ID rmi_id, short rmi_ctx, int errorCode) {
        userFailedInstallBuilding _data__1 = new userFailedInstallBuilding();
        _data__1.errorCode = errorCode;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedInstallBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedInstallBuilding, _data__1);
        _data__1 = null;
    }

    public static void userFailedInstallBuilding(RMI_ID[] rmi_id, short rmi_ctx, int errorCode) {
        userFailedInstallBuilding _data__1 = new userFailedInstallBuilding();
        _data__1.errorCode = errorCode;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedInstallBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedInstallBuilding, _data__1);
        _data__1 = null;
    }

    public static void userFailedUpgradeBuilding(RMI_ID rmi_id, short rmi_ctx, int errorCode) {
        userFailedUpgradeBuilding _data__1 = new userFailedUpgradeBuilding();
        _data__1.errorCode = errorCode;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedUpgradeBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedUpgradeBuilding, _data__1);
        _data__1 = null;
    }

    public static void userFailedUpgradeBuilding(RMI_ID[] rmi_id, short rmi_ctx, int errorCode) {
        userFailedUpgradeBuilding _data__1 = new userFailedUpgradeBuilding();
        _data__1.errorCode = errorCode;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedUpgradeBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedUpgradeBuilding, _data__1);
        _data__1 = null;
    }

    public static void userSucceedInstallBuilding(RMI_ID rmi_id, short rmi_ctx, int builderEntityID, int buildingType) {
        userSucceedInstallBuilding _data__1 = new userSucceedInstallBuilding();
        _data__1.builderEntityID = builderEntityID;
        _data__1.buildingType = buildingType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userSucceedInstallBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userSucceedInstallBuilding, _data__1);
        _data__1 = null;
    }

    public static void userSucceedInstallBuilding(RMI_ID[] rmi_id, short rmi_ctx, int builderEntityID, int buildingType) {
        userSucceedInstallBuilding _data__1 = new userSucceedInstallBuilding();
        _data__1.builderEntityID = builderEntityID;
        _data__1.buildingType = buildingType;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userSucceedInstallBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userSucceedInstallBuilding, _data__1);
        _data__1 = null;
    }

    public static void userSucceedStoreUpgradeBuff(RMI_ID rmi_id, short rmi_ctx, StoreUpgradeBuffSlotData storeUpgradeBuffSlotData) {
        userSucceedStoreUpgradeBuff _data__1 = new userSucceedStoreUpgradeBuff();
        _data__1.storeUpgradeBuffSlotData = storeUpgradeBuffSlotData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userSucceedStoreUpgradeBuff, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userSucceedStoreUpgradeBuff, _data__1);
        _data__1 = null;
    }

    public static void userSucceedStoreUpgradeBuff(RMI_ID[] rmi_id, short rmi_ctx, StoreUpgradeBuffSlotData storeUpgradeBuffSlotData) {
        userSucceedStoreUpgradeBuff _data__1 = new userSucceedStoreUpgradeBuff();
        _data__1.storeUpgradeBuffSlotData = storeUpgradeBuffSlotData;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userSucceedStoreUpgradeBuff, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userSucceedStoreUpgradeBuff, _data__1);
        _data__1 = null;
    }

    public static void broadcastingStoreUpgradeBuffList(RMI_ID rmi_id, short rmi_ctx, LinkedList<StoreUpgradeBuffSlotData> storeUpgradeBuffSlotDataList) {
        broadcastingStoreUpgradeBuffList _data__1 = new broadcastingStoreUpgradeBuffList();
        _data__1.storeUpgradeBuffSlotDataList = storeUpgradeBuffSlotDataList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingStoreUpgradeBuffList, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingStoreUpgradeBuffList, _data__1);
        _data__1 = null;
    }

    public static void broadcastingStoreUpgradeBuffList(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<StoreUpgradeBuffSlotData> storeUpgradeBuffSlotDataList) {
        broadcastingStoreUpgradeBuffList _data__1 = new broadcastingStoreUpgradeBuffList();
        _data__1.storeUpgradeBuffSlotDataList = storeUpgradeBuffSlotDataList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingStoreUpgradeBuffList, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingStoreUpgradeBuffList, _data__1);
        _data__1 = null;
    }

    public static void StartGame(RMI_ID rmi_id, short rmi_ctx) {
        StartGame _data__1 = new StartGame();
        byte[] RMIdata = new byte[1];
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.StartGame, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.StartGame, _data__1);
        _data__1 = null;
    }

    public static void StartGame(RMI_ID[] rmi_id, short rmi_ctx) {
        StartGame _data__1 = new StartGame();
        byte[] RMIdata = new byte[1];
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.StartGame, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.StartGame, _data__1);
        _data__1 = null;
    }

    public static void StartWave(RMI_ID rmi_id, short rmi_ctx, int waveCount) {
        StartWave _data__1 = new StartWave();
        _data__1.waveCount = waveCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.StartWave, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.StartWave, _data__1);
        _data__1 = null;
    }

    public static void StartWave(RMI_ID[] rmi_id, short rmi_ctx, int waveCount) {
        StartWave _data__1 = new StartWave();
        _data__1.waveCount = waveCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.StartWave, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.StartWave, _data__1);
        _data__1 = null;
    }

    public static void EndWave(RMI_ID rmi_id, short rmi_ctx, int waveCount) {
        EndWave _data__1 = new EndWave();
        _data__1.waveCount = waveCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.EndWave, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.EndWave, _data__1);
        _data__1 = null;
    }

    public static void EndWave(RMI_ID[] rmi_id, short rmi_ctx, int waveCount) {
        EndWave _data__1 = new EndWave();
        _data__1.waveCount = waveCount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.EndWave, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.EndWave, _data__1);
        _data__1 = null;
    }

    public static void EndGame(RMI_ID rmi_id, short rmi_ctx, int resultCode, String resultJS) {
        EndGame _data__1 = new EndGame();
        _data__1.resultCode = resultCode;
        _data__1.resultJS = resultJS;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.EndGame, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.EndGame, _data__1);
        _data__1 = null;
    }

    public static void EndGame(RMI_ID[] rmi_id, short rmi_ctx, int resultCode, String resultJS) {
        EndGame _data__1 = new EndGame();
        _data__1.resultCode = resultCode;
        _data__1.resultJS = resultJS;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.EndGame, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.EndGame, _data__1);
        _data__1 = null;
    }

    public static void motionCharacterDoAttack(RMI_ID rmi_id, short rmi_ctx, int characterEntityID, int targetEntityID) {
        motionCharacterDoAttack _data__1 = new motionCharacterDoAttack();
        _data__1.characterEntityID = characterEntityID;
        _data__1.targetEntityID = targetEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterDoAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterDoAttack, _data__1);
        _data__1 = null;
    }

    public static void motionCharacterDoAttack(RMI_ID[] rmi_id, short rmi_ctx, int characterEntityID, int targetEntityID) {
        motionCharacterDoAttack _data__1 = new motionCharacterDoAttack();
        _data__1.characterEntityID = characterEntityID;
        _data__1.targetEntityID = targetEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterDoAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterDoAttack, _data__1);
        _data__1 = null;
    }

    public static void motionCharacterUseSkill(RMI_ID rmi_id, short rmi_ctx, int characterEntityID, SkillInfoData usedSkill) {
        motionCharacterUseSkill _data__1 = new motionCharacterUseSkill();
        _data__1.characterEntityID = characterEntityID;
        _data__1.usedSkill = usedSkill;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterUseSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterUseSkill, _data__1);
        _data__1 = null;
    }

    public static void motionCharacterUseSkill(RMI_ID[] rmi_id, short rmi_ctx, int characterEntityID, SkillInfoData usedSkill) {
        motionCharacterUseSkill _data__1 = new motionCharacterUseSkill();
        _data__1.characterEntityID = characterEntityID;
        _data__1.usedSkill = usedSkill;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterUseSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterUseSkill, _data__1);
        _data__1 = null;
    }

    public static void motionCharacterCancelSkill(RMI_ID rmi_id, short rmi_ctx, int characterEntityID, SkillInfoData usedSkill) {
        motionCharacterCancelSkill _data__1 = new motionCharacterCancelSkill();
        _data__1.characterEntityID = characterEntityID;
        _data__1.usedSkill = usedSkill;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterCancelSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterCancelSkill, _data__1);
        _data__1 = null;
    }

    public static void motionCharacterCancelSkill(RMI_ID[] rmi_id, short rmi_ctx, int characterEntityID, SkillInfoData usedSkill) {
        motionCharacterCancelSkill _data__1 = new motionCharacterCancelSkill();
        _data__1.characterEntityID = characterEntityID;
        _data__1.usedSkill = usedSkill;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterCancelSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterCancelSkill, _data__1);
        _data__1 = null;
    }

    public static void motionCharacterUseItem(RMI_ID rmi_id, short rmi_ctx, int characterEntityID, ItemInfoData usedItem) {
        motionCharacterUseItem _data__1 = new motionCharacterUseItem();
        _data__1.characterEntityID = characterEntityID;
        _data__1.usedItem = usedItem;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterUseItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterUseItem, _data__1);
        _data__1 = null;
    }

    public static void motionCharacterUseItem(RMI_ID[] rmi_id, short rmi_ctx, int characterEntityID, ItemInfoData usedItem) {
        motionCharacterUseItem _data__1 = new motionCharacterUseItem();
        _data__1.characterEntityID = characterEntityID;
        _data__1.usedItem = usedItem;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterUseItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterUseItem, _data__1);
        _data__1 = null;
    }

    public static void motionMonsterDoAttack(RMI_ID rmi_id, short rmi_ctx, int monsterEntityID, short targetEntityType, int targetEntityID) {
        motionMonsterDoAttack _data__1 = new motionMonsterDoAttack();
        _data__1.monsterEntityID = monsterEntityID;
        _data__1.targetEntityType = targetEntityType;
        _data__1.targetEntityID = targetEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionMonsterDoAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionMonsterDoAttack, _data__1);
        _data__1 = null;
    }

    public static void motionMonsterDoAttack(RMI_ID[] rmi_id, short rmi_ctx, int monsterEntityID, short targetEntityType, int targetEntityID) {
        motionMonsterDoAttack _data__1 = new motionMonsterDoAttack();
        _data__1.monsterEntityID = monsterEntityID;
        _data__1.targetEntityType = targetEntityType;
        _data__1.targetEntityID = targetEntityID;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionMonsterDoAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionMonsterDoAttack, _data__1);
        _data__1 = null;
    }

    public static void motionMonsterUseSkill(RMI_ID rmi_id, short rmi_ctx, int monsterEntityID, SkillInfoData usedSkill) {
        motionMonsterUseSkill _data__1 = new motionMonsterUseSkill();
        _data__1.monsterEntityID = monsterEntityID;
        _data__1.usedSkill = usedSkill;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionMonsterUseSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionMonsterUseSkill, _data__1);
        _data__1 = null;
    }

    public static void motionMonsterUseSkill(RMI_ID[] rmi_id, short rmi_ctx, int monsterEntityID, SkillInfoData usedSkill) {
        motionMonsterUseSkill _data__1 = new motionMonsterUseSkill();
        _data__1.monsterEntityID = monsterEntityID;
        _data__1.usedSkill = usedSkill;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionMonsterUseSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionMonsterUseSkill, _data__1);
        _data__1 = null;
    }

    public static void broadcastingCharacterSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<CharacterData> characterSnapshot) {
        broadcastingCharacterSnapshot _data__1 = new broadcastingCharacterSnapshot();
        _data__1.characterSnapshot = characterSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCharacterSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCharacterSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingCharacterSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CharacterData> characterSnapshot) {
        broadcastingCharacterSnapshot _data__1 = new broadcastingCharacterSnapshot();
        _data__1.characterSnapshot = characterSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCharacterSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCharacterSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingMonsterSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<MonsterData> monsterSnapshot) {
        broadcastingMonsterSnapshot _data__1 = new broadcastingMonsterSnapshot();
        _data__1.monsterSnapshot = monsterSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingMonsterSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingMonsterSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingMonsterSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<MonsterData> monsterSnapshot) {
        broadcastingMonsterSnapshot _data__1 = new broadcastingMonsterSnapshot();
        _data__1.monsterSnapshot = monsterSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingMonsterSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingMonsterSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingBuffTurretSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<BuffTurretData> buffTurretSnapshot) {
        broadcastingBuffTurretSnapshot _data__1 = new broadcastingBuffTurretSnapshot();
        _data__1.buffTurretSnapshot = buffTurretSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBuffTurretSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBuffTurretSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingBuffTurretSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BuffTurretData> buffTurretSnapshot) {
        broadcastingBuffTurretSnapshot _data__1 = new broadcastingBuffTurretSnapshot();
        _data__1.buffTurretSnapshot = buffTurretSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBuffTurretSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBuffTurretSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingAttackTurretSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<AttackTurretData> attackTurretSnapshot) {
        broadcastingAttackTurretSnapshot _data__1 = new broadcastingAttackTurretSnapshot();
        _data__1.attackTurretSnapshot = attackTurretSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingAttackTurretSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingAttackTurretSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingAttackTurretSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<AttackTurretData> attackTurretSnapshot) {
        broadcastingAttackTurretSnapshot _data__1 = new broadcastingAttackTurretSnapshot();
        _data__1.attackTurretSnapshot = attackTurretSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingAttackTurretSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingAttackTurretSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingBarricadeSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<BarricadeData> barricadeSnapshot) {
        broadcastingBarricadeSnapshot _data__1 = new broadcastingBarricadeSnapshot();
        _data__1.barricadeSnapshot = barricadeSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBarricadeSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBarricadeSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingBarricadeSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BarricadeData> barricadeSnapshot) {
        broadcastingBarricadeSnapshot _data__1 = new broadcastingBarricadeSnapshot();
        _data__1.barricadeSnapshot = barricadeSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBarricadeSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBarricadeSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingCrystalSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<CrystalData> crystalSnapshot) {
        broadcastingCrystalSnapshot _data__1 = new broadcastingCrystalSnapshot();
        _data__1.crystalSnapshot = crystalSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCrystalSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCrystalSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingCrystalSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CrystalData> crystalSnapshot) {
        broadcastingCrystalSnapshot _data__1 = new broadcastingCrystalSnapshot();
        _data__1.crystalSnapshot = crystalSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCrystalSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCrystalSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingSkillObjectSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<SkillObjectData> skillObjectSnapshot) {
        broadcastingSkillObjectSnapshot _data__1 = new broadcastingSkillObjectSnapshot();
        _data__1.skillObjectSnapshot = skillObjectSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingSkillObjectSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingSkillObjectSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingSkillObjectSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<SkillObjectData> skillObjectSnapshot) {
        broadcastingSkillObjectSnapshot _data__1 = new broadcastingSkillObjectSnapshot();
        _data__1.skillObjectSnapshot = skillObjectSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingSkillObjectSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingSkillObjectSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingFlyingObjectSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<FlyingObjectData> flyingObjectSnapshot) {
        broadcastingFlyingObjectSnapshot _data__1 = new broadcastingFlyingObjectSnapshot();
        _data__1.flyingObjectSnapshot = flyingObjectSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingFlyingObjectSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingFlyingObjectSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingFlyingObjectSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<FlyingObjectData> flyingObjectSnapshot) {
        broadcastingFlyingObjectSnapshot _data__1 = new broadcastingFlyingObjectSnapshot();
        _data__1.flyingObjectSnapshot = flyingObjectSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingFlyingObjectSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingFlyingObjectSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingBuildSlotSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<BuildSlotData> buildSlotList) {
        broadcastingBuildSlotSnapshot _data__1 = new broadcastingBuildSlotSnapshot();
        _data__1.buildSlotList = buildSlotList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBuildSlotSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBuildSlotSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingBuildSlotSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BuildSlotData> buildSlotList) {
        broadcastingBuildSlotSnapshot _data__1 = new broadcastingBuildSlotSnapshot();
        _data__1.buildSlotList = buildSlotList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBuildSlotSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBuildSlotSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingGameWorldStatusSnapshot(RMI_ID rmi_id, short rmi_ctx, GameWorldStatus gameworldStatusSnapshot) {
        broadcastingGameWorldStatusSnapshot _data__1 = new broadcastingGameWorldStatusSnapshot();
        _data__1.gameworldStatusSnapshot = gameworldStatusSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingGameWorldStatusSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingGameWorldStatusSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingGameWorldStatusSnapshot(RMI_ID[] rmi_id, short rmi_ctx, GameWorldStatus gameworldStatusSnapshot) {
        broadcastingGameWorldStatusSnapshot _data__1 = new broadcastingGameWorldStatusSnapshot();
        _data__1.gameworldStatusSnapshot = gameworldStatusSnapshot;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingGameWorldStatusSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingGameWorldStatusSnapshot, _data__1);
        _data__1 = null;
    }

    public static void broadcastingDamageAmount(RMI_ID rmi_id, short rmi_ctx, int damageType, int entityID, float damageAmount) {
        broadcastingDamageAmount _data__1 = new broadcastingDamageAmount();
        _data__1.damageType = damageType;
        _data__1.entityID = entityID;
        _data__1.damageAmount = damageAmount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingDamageAmount, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingDamageAmount, _data__1);
        _data__1 = null;
    }

    public static void broadcastingDamageAmount(RMI_ID[] rmi_id, short rmi_ctx, int damageType, int entityID, float damageAmount) {
        broadcastingDamageAmount _data__1 = new broadcastingDamageAmount();
        _data__1.damageType = damageType;
        _data__1.entityID = entityID;
        _data__1.damageAmount = damageAmount;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingDamageAmount, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingDamageAmount, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapCharacterEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<CharacterData> characterList) {
        createWorldMapCharacterEntityInfo _data__1 = new createWorldMapCharacterEntityInfo();
        _data__1.characterList = characterList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapCharacterEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapCharacterEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapCharacterEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CharacterData> characterList) {
        createWorldMapCharacterEntityInfo _data__1 = new createWorldMapCharacterEntityInfo();
        _data__1.characterList = characterList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapCharacterEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapCharacterEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapMonsterEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<MonsterData> monsterList) {
        createWorldMapMonsterEntityInfo _data__1 = new createWorldMapMonsterEntityInfo();
        _data__1.monsterList = monsterList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapMonsterEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapMonsterEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapMonsterEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<MonsterData> monsterList) {
        createWorldMapMonsterEntityInfo _data__1 = new createWorldMapMonsterEntityInfo();
        _data__1.monsterList = monsterList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapMonsterEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapMonsterEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapBuffTurretEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<BuffTurretData> buffTurretList) {
        createWorldMapBuffTurretEntityInfo _data__1 = new createWorldMapBuffTurretEntityInfo();
        _data__1.buffTurretList = buffTurretList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapBuffTurretEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapBuffTurretEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapBuffTurretEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BuffTurretData> buffTurretList) {
        createWorldMapBuffTurretEntityInfo _data__1 = new createWorldMapBuffTurretEntityInfo();
        _data__1.buffTurretList = buffTurretList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapBuffTurretEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapBuffTurretEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapAttackTurretEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<AttackTurretData> attackTurretList) {
        createWorldMapAttackTurretEntityInfo _data__1 = new createWorldMapAttackTurretEntityInfo();
        _data__1.attackTurretList = attackTurretList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapAttackTurretEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapAttackTurretEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapAttackTurretEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<AttackTurretData> attackTurretList) {
        createWorldMapAttackTurretEntityInfo _data__1 = new createWorldMapAttackTurretEntityInfo();
        _data__1.attackTurretList = attackTurretList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapAttackTurretEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapAttackTurretEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapBarricadeEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<BarricadeData> barricadeList) {
        createWorldMapBarricadeEntityInfo _data__1 = new createWorldMapBarricadeEntityInfo();
        _data__1.barricadeList = barricadeList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapBarricadeEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapBarricadeEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapBarricadeEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BarricadeData> barricadeList) {
        createWorldMapBarricadeEntityInfo _data__1 = new createWorldMapBarricadeEntityInfo();
        _data__1.barricadeList = barricadeList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapBarricadeEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapBarricadeEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapCrystalDataEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<CrystalData> crystalList) {
        createWorldMapCrystalDataEntityInfo _data__1 = new createWorldMapCrystalDataEntityInfo();
        _data__1.crystalList = crystalList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapCrystalDataEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapCrystalDataEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapCrystalDataEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CrystalData> crystalList) {
        createWorldMapCrystalDataEntityInfo _data__1 = new createWorldMapCrystalDataEntityInfo();
        _data__1.crystalList = crystalList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapCrystalDataEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapCrystalDataEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapSkillObjectEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<SkillObjectData> skillObjectList) {
        createWorldMapSkillObjectEntityInfo _data__1 = new createWorldMapSkillObjectEntityInfo();
        _data__1.skillObjectList = skillObjectList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapSkillObjectEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapSkillObjectEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapSkillObjectEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<SkillObjectData> skillObjectList) {
        createWorldMapSkillObjectEntityInfo _data__1 = new createWorldMapSkillObjectEntityInfo();
        _data__1.skillObjectList = skillObjectList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapSkillObjectEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapSkillObjectEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapFlyingObjectEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<FlyingObjectData> flyingObjectList) {
        createWorldMapFlyingObjectEntityInfo _data__1 = new createWorldMapFlyingObjectEntityInfo();
        _data__1.flyingObjectList = flyingObjectList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapFlyingObjectEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapFlyingObjectEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void createWorldMapFlyingObjectEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<FlyingObjectData> flyingObjectList) {
        createWorldMapFlyingObjectEntityInfo _data__1 = new createWorldMapFlyingObjectEntityInfo();
        _data__1.flyingObjectList = flyingObjectList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapFlyingObjectEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapFlyingObjectEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void destroyWorldMapEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<DestroyEntityData> destroyedEntityList) {
        destroyWorldMapEntityInfo _data__1 = new destroyWorldMapEntityInfo();
        _data__1.destroyedEntityList = destroyedEntityList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.destroyWorldMapEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.destroyWorldMapEntityInfo, _data__1);
        _data__1 = null;
    }

    public static void destroyWorldMapEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<DestroyEntityData> destroyedEntityList) {
        destroyWorldMapEntityInfo _data__1 = new destroyWorldMapEntityInfo();
        _data__1.destroyedEntityList = destroyedEntityList;
        byte[] RMIdata = _data__1.getBytes(); //_data__1.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.destroyWorldMapEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.destroyWorldMapEntityInfo, _data__1);
        _data__1 = null;
    }

}
