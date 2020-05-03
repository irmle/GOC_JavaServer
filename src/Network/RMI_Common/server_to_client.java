package Network.RMI_Common;

import java.util.LinkedList;
import Network.RMI_LogicMessages.server_to_client.*;
import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import Network.RMI_Classes.*;
import Network.*;

public class server_to_client {


    //네트워크로 송신하는 로직을 담음.
    private static void callRMI_Method(RMI_ID rmi_id, short rmi_ctx, short packetType, byte[] RMIdata) {

        //암호화를 담당하는 부분.
        RMIdata = RMI_EncryptManager.RMI_EncryptMethod(rmi_id, rmi_ctx, true, RMIdata);

        //RMI_Context.Reliable = 1; ~ RMI_Context.UnReliable_Public_AES256 = 10;
        if(1<=rmi_ctx && rmi_ctx<=5)
            RMI.sendByte_TCP(rmi_id, rmi_ctx, packetType, RMIdata); //신뢰성 전송
        else
            RMI.sendByte_UDP(rmi_id, rmi_ctx, packetType, RMIdata); //비신뢰성 전송
    }

    //네트워크로 송신하는 로직을 담음.
    private static void callRMI_Method(RMI_ID[] rmi_id, short rmi_ctx, short packetType, byte[] RMIdata) {

        //암호화를 담당하는 부분.
        RMIdata = RMI_EncryptManager.RMI_EncryptMethod_Arr(rmi_id, rmi_ctx, true, RMIdata);

        //RMI_Context.Reliable = 1; ~ RMI_Context.UnReliable_Public_AES256 = 10;
        if(1<=rmi_ctx && rmi_ctx<=5)
            RMI.sendByte_TCP(rmi_id, rmi_ctx, packetType, RMIdata); //신뢰성 전송
        else
            RMI.sendByte_UDP(rmi_id, rmi_ctx, packetType, RMIdata); //비신뢰성 전송
    }

    //네트워크에서 수신하는 로직을 담음.
    public static void recvRMI_Method(RMI_ID rmi_id, short rmi_ctx, short packetType, byte[] RMIdata) {

        //복호화를 담당하는 부분.
        RMIdata = RMI_EncryptManager.RMI_EncryptMethod(rmi_id, rmi_ctx, false, RMIdata);

        parseRMI(rmi_id, rmi_ctx, packetType, RMIdata);
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
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.heartBeatCheck_Response:
                heartBeatCheck_Response r1 = heartBeatCheck_Response.createheartBeatCheck_Response(RMIdata);
                Logic_heartBeatCheck_Response.RMI_Packet(rmi_id, rmi_ctx, r1.timeData);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.loginOK:
                Logic_loginOK.RMI_Packet(rmi_id, rmi_ctx);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.completeMatching:
                completeMatching r3 = completeMatching.createcompleteMatching(RMIdata);
                Logic_completeMatching.RMI_Packet(rmi_id, rmi_ctx, r3.worldMapID, r3.ipAddress, r3.port);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.errorMatching:
                errorMatching r4 = errorMatching.createerrorMatching(RMIdata);
                Logic_errorMatching.RMI_Packet(rmi_id, rmi_ctx, r4.errorCode, r4.errorReason);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicStart:
                pickLogicStart r5 = pickLogicStart.createpickLogicStart(RMIdata);
                Logic_pickLogicStart.RMI_Packet(rmi_id, rmi_ctx, r5.loadingPlayerList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicTime:
                pickLogicTime r6 = pickLogicTime.createpickLogicTime(RMIdata);
                Logic_pickLogicTime.RMI_Packet(rmi_id, rmi_ctx, r6.remainCharacterPickTime);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicUserOnSelectedCharacter:
                pickLogicUserOnSelectedCharacter r7 = pickLogicUserOnSelectedCharacter.createpickLogicUserOnSelectedCharacter(RMIdata);
                Logic_pickLogicUserOnSelectedCharacter.RMI_Packet(rmi_id, rmi_ctx, r7.googleIDToken, r7.characterType);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicUserOnReady:
                pickLogicUserOnReady r8 = pickLogicUserOnReady.createpickLogicUserOnReady(RMIdata);
                Logic_pickLogicUserOnReady.RMI_Packet(rmi_id, rmi_ctx, r8.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicUserOnCancel:
                pickLogicUserOnCancel r9 = pickLogicUserOnCancel.createpickLogicUserOnCancel(RMIdata);
                Logic_pickLogicUserOnCancel.RMI_Packet(rmi_id, rmi_ctx, r9.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicUserOnDisconnected:
                pickLogicUserOnDisconnected r10 = pickLogicUserOnDisconnected.createpickLogicUserOnDisconnected(RMIdata);
                Logic_pickLogicUserOnDisconnected.RMI_Packet(rmi_id, rmi_ctx, r10.googleIDToken);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicUserOnChatMessage:
                pickLogicUserOnChatMessage r11 = pickLogicUserOnChatMessage.createpickLogicUserOnChatMessage(RMIdata);
                Logic_pickLogicUserOnChatMessage.RMI_Packet(rmi_id, rmi_ctx, r11.googleIDToken, r11.chatMessage);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicIsVoipHost:
                pickLogicIsVoipHost r12 = pickLogicIsVoipHost.createpickLogicIsVoipHost(RMIdata);
                Logic_pickLogicIsVoipHost.RMI_Packet(rmi_id, rmi_ctx, r12.isVoipHost, r12.worldMapID);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.pickLogicConnectToVoipHost:
                pickLogicConnectToVoipHost r13 = pickLogicConnectToVoipHost.createpickLogicConnectToVoipHost(RMIdata);
                Logic_pickLogicConnectToVoipHost.RMI_Packet(rmi_id, rmi_ctx, r13.isVoipOK, r13.worldMapID);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.reconnectingWorldMap:
                reconnectingWorldMap r14 = reconnectingWorldMap.createreconnectingWorldMap(RMIdata);
                Logic_reconnectingWorldMap.RMI_Packet(rmi_id, rmi_ctx, r14.worldMapID, r14.loadingPlayerList, r14.ipAddress, r14.port);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingLoadingProgress:
                broadcastingLoadingProgress r15 = broadcastingLoadingProgress.createbroadcastingLoadingProgress(RMIdata);
                Logic_broadcastingLoadingProgress.RMI_Packet(rmi_id, rmi_ctx, r15.loadingPlayerList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.initializeMyselfCharacterInfo:
                initializeMyselfCharacterInfo r16 = initializeMyselfCharacterInfo.createinitializeMyselfCharacterInfo(RMIdata);
                Logic_initializeMyselfCharacterInfo.RMI_Packet(rmi_id, rmi_ctx, r16.ownUserEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.initializeWorldMap:
                initializeWorldMap r17 = initializeWorldMap.createinitializeWorldMap(RMIdata);
                Logic_initializeWorldMap.RMI_Packet(rmi_id, rmi_ctx, r17.characterList, r17.monsterList, r17.buffTurretList, r17.attackTurretList, r17.barricadeList, r17.crystalList, r17.skillObjectList, r17.flyingObjectList, r17.buildSlotList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userDisconnected:
                userDisconnected r18 = userDisconnected.createuserDisconnected(RMIdata);
                Logic_userDisconnected.RMI_Packet(rmi_id, rmi_ctx, r18.userEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userReconnected:
                userReconnected r19 = userReconnected.createuserReconnected(RMIdata);
                Logic_userReconnected.RMI_Packet(rmi_id, rmi_ctx, r19.userEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userCharacterDefeat:
                userCharacterDefeat r20 = userCharacterDefeat.createuserCharacterDefeat(RMIdata);
                Logic_userCharacterDefeat.RMI_Packet(rmi_id, rmi_ctx, r20.userEntityID, r20.remainTimeMilliSeconds);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userCharacterRespawn:
                userCharacterRespawn r21 = userCharacterRespawn.createuserCharacterRespawn(RMIdata);
                Logic_userCharacterRespawn.RMI_Packet(rmi_id, rmi_ctx, r21.userEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userFailedBuyItem:
                userFailedBuyItem r22 = userFailedBuyItem.createuserFailedBuyItem(RMIdata);
                Logic_userFailedBuyItem.RMI_Packet(rmi_id, rmi_ctx, r22.errorCode);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userFailedUseItem:
                userFailedUseItem r23 = userFailedUseItem.createuserFailedUseItem(RMIdata);
                Logic_userFailedUseItem.RMI_Packet(rmi_id, rmi_ctx, r23.errorCode);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userFailedInstallBuilding:
                userFailedInstallBuilding r24 = userFailedInstallBuilding.createuserFailedInstallBuilding(RMIdata);
                Logic_userFailedInstallBuilding.RMI_Packet(rmi_id, rmi_ctx, r24.errorCode);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userFailedUpgradeBuilding:
                userFailedUpgradeBuilding r25 = userFailedUpgradeBuilding.createuserFailedUpgradeBuilding(RMIdata);
                Logic_userFailedUpgradeBuilding.RMI_Packet(rmi_id, rmi_ctx, r25.errorCode);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userSucceedInstallBuilding:
                userSucceedInstallBuilding r26 = userSucceedInstallBuilding.createuserSucceedInstallBuilding(RMIdata);
                Logic_userSucceedInstallBuilding.RMI_Packet(rmi_id, rmi_ctx, r26.builderEntityID, r26.buildingType);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.userSucceedStoreUpgradeBuff:
                userSucceedStoreUpgradeBuff r27 = userSucceedStoreUpgradeBuff.createuserSucceedStoreUpgradeBuff(RMIdata);
                Logic_userSucceedStoreUpgradeBuff.RMI_Packet(rmi_id, rmi_ctx, r27.storeUpgradeBuffSlotData);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingStoreUpgradeBuffList:
                broadcastingStoreUpgradeBuffList r28 = broadcastingStoreUpgradeBuffList.createbroadcastingStoreUpgradeBuffList(RMIdata);
                Logic_broadcastingStoreUpgradeBuffList.RMI_Packet(rmi_id, rmi_ctx, r28.storeUpgradeBuffSlotDataList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.StartGame:
                Logic_StartGame.RMI_Packet(rmi_id, rmi_ctx);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.StartWave:
                StartWave r30 = StartWave.createStartWave(RMIdata);
                Logic_StartWave.RMI_Packet(rmi_id, rmi_ctx, r30.waveCount);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.EndWave:
                EndWave r31 = EndWave.createEndWave(RMIdata);
                Logic_EndWave.RMI_Packet(rmi_id, rmi_ctx, r31.waveCount);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.EndGame:
                EndGame r32 = EndGame.createEndGame(RMIdata);
                Logic_EndGame.RMI_Packet(rmi_id, rmi_ctx, r32.resultCode, r32.resultJS);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.motionCharacterDoAttack:
                motionCharacterDoAttack r33 = motionCharacterDoAttack.createmotionCharacterDoAttack(RMIdata);
                Logic_motionCharacterDoAttack.RMI_Packet(rmi_id, rmi_ctx, r33.characterEntityID, r33.targetEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.motionCharacterUseSkill:
                motionCharacterUseSkill r34 = motionCharacterUseSkill.createmotionCharacterUseSkill(RMIdata);
                Logic_motionCharacterUseSkill.RMI_Packet(rmi_id, rmi_ctx, r34.characterEntityID, r34.usedSkill);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.motionCharacterCancelSkill:
                motionCharacterCancelSkill r35 = motionCharacterCancelSkill.createmotionCharacterCancelSkill(RMIdata);
                Logic_motionCharacterCancelSkill.RMI_Packet(rmi_id, rmi_ctx, r35.characterEntityID, r35.usedSkill);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.motionCharacterUseItem:
                motionCharacterUseItem r36 = motionCharacterUseItem.createmotionCharacterUseItem(RMIdata);
                Logic_motionCharacterUseItem.RMI_Packet(rmi_id, rmi_ctx, r36.characterEntityID, r36.usedItem);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.motionMonsterDoAttack:
                motionMonsterDoAttack r37 = motionMonsterDoAttack.createmotionMonsterDoAttack(RMIdata);
                Logic_motionMonsterDoAttack.RMI_Packet(rmi_id, rmi_ctx, r37.monsterEntityID, r37.targetEntityType, r37.targetEntityID);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.motionMonsterUseSkill:
                motionMonsterUseSkill r38 = motionMonsterUseSkill.createmotionMonsterUseSkill(RMIdata);
                Logic_motionMonsterUseSkill.RMI_Packet(rmi_id, rmi_ctx, r38.monsterEntityID, r38.usedSkill);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingCharacterSnapshot:
                broadcastingCharacterSnapshot r39 = broadcastingCharacterSnapshot.createbroadcastingCharacterSnapshot(RMIdata);
                Logic_broadcastingCharacterSnapshot.RMI_Packet(rmi_id, rmi_ctx, r39.characterSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingMonsterSnapshot:
                broadcastingMonsterSnapshot r40 = broadcastingMonsterSnapshot.createbroadcastingMonsterSnapshot(RMIdata);
                Logic_broadcastingMonsterSnapshot.RMI_Packet(rmi_id, rmi_ctx, r40.monsterSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingBuffTurretSnapshot:
                broadcastingBuffTurretSnapshot r41 = broadcastingBuffTurretSnapshot.createbroadcastingBuffTurretSnapshot(RMIdata);
                Logic_broadcastingBuffTurretSnapshot.RMI_Packet(rmi_id, rmi_ctx, r41.buffTurretSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingAttackTurretSnapshot:
                broadcastingAttackTurretSnapshot r42 = broadcastingAttackTurretSnapshot.createbroadcastingAttackTurretSnapshot(RMIdata);
                Logic_broadcastingAttackTurretSnapshot.RMI_Packet(rmi_id, rmi_ctx, r42.attackTurretSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingBarricadeSnapshot:
                broadcastingBarricadeSnapshot r43 = broadcastingBarricadeSnapshot.createbroadcastingBarricadeSnapshot(RMIdata);
                Logic_broadcastingBarricadeSnapshot.RMI_Packet(rmi_id, rmi_ctx, r43.barricadeSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingCrystalSnapshot:
                broadcastingCrystalSnapshot r44 = broadcastingCrystalSnapshot.createbroadcastingCrystalSnapshot(RMIdata);
                Logic_broadcastingCrystalSnapshot.RMI_Packet(rmi_id, rmi_ctx, r44.crystalSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingSkillObjectSnapshot:
                broadcastingSkillObjectSnapshot r45 = broadcastingSkillObjectSnapshot.createbroadcastingSkillObjectSnapshot(RMIdata);
                Logic_broadcastingSkillObjectSnapshot.RMI_Packet(rmi_id, rmi_ctx, r45.skillObjectSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingFlyingObjectSnapshot:
                broadcastingFlyingObjectSnapshot r46 = broadcastingFlyingObjectSnapshot.createbroadcastingFlyingObjectSnapshot(RMIdata);
                Logic_broadcastingFlyingObjectSnapshot.RMI_Packet(rmi_id, rmi_ctx, r46.flyingObjectSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingBuildSlotSnapshot:
                broadcastingBuildSlotSnapshot r47 = broadcastingBuildSlotSnapshot.createbroadcastingBuildSlotSnapshot(RMIdata);
                Logic_broadcastingBuildSlotSnapshot.RMI_Packet(rmi_id, rmi_ctx, r47.buildSlotList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingGameWorldStatusSnapshot:
                broadcastingGameWorldStatusSnapshot r48 = broadcastingGameWorldStatusSnapshot.createbroadcastingGameWorldStatusSnapshot(RMIdata);
                Logic_broadcastingGameWorldStatusSnapshot.RMI_Packet(rmi_id, rmi_ctx, r48.gameworldStatusSnapshot);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.broadcastingDamageAmount:
                broadcastingDamageAmount r49 = broadcastingDamageAmount.createbroadcastingDamageAmount(RMIdata);
                Logic_broadcastingDamageAmount.RMI_Packet(rmi_id, rmi_ctx, r49.damageType, r49.entityID, r49.damageAmount);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.createWorldMapCharacterEntityInfo:
                createWorldMapCharacterEntityInfo r50 = createWorldMapCharacterEntityInfo.createcreateWorldMapCharacterEntityInfo(RMIdata);
                Logic_createWorldMapCharacterEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r50.characterList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.createWorldMapMonsterEntityInfo:
                createWorldMapMonsterEntityInfo r51 = createWorldMapMonsterEntityInfo.createcreateWorldMapMonsterEntityInfo(RMIdata);
                Logic_createWorldMapMonsterEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r51.monsterList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.createWorldMapBuffTurretEntityInfo:
                createWorldMapBuffTurretEntityInfo r52 = createWorldMapBuffTurretEntityInfo.createcreateWorldMapBuffTurretEntityInfo(RMIdata);
                Logic_createWorldMapBuffTurretEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r52.buffTurretList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.createWorldMapAttackTurretEntityInfo:
                createWorldMapAttackTurretEntityInfo r53 = createWorldMapAttackTurretEntityInfo.createcreateWorldMapAttackTurretEntityInfo(RMIdata);
                Logic_createWorldMapAttackTurretEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r53.attackTurretList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.createWorldMapBarricadeEntityInfo:
                createWorldMapBarricadeEntityInfo r54 = createWorldMapBarricadeEntityInfo.createcreateWorldMapBarricadeEntityInfo(RMIdata);
                Logic_createWorldMapBarricadeEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r54.barricadeList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.createWorldMapCrystalDataEntityInfo:
                createWorldMapCrystalDataEntityInfo r55 = createWorldMapCrystalDataEntityInfo.createcreateWorldMapCrystalDataEntityInfo(RMIdata);
                Logic_createWorldMapCrystalDataEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r55.crystalList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.createWorldMapSkillObjectEntityInfo:
                createWorldMapSkillObjectEntityInfo r56 = createWorldMapSkillObjectEntityInfo.createcreateWorldMapSkillObjectEntityInfo(RMIdata);
                Logic_createWorldMapSkillObjectEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r56.skillObjectList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.createWorldMapFlyingObjectEntityInfo:
                createWorldMapFlyingObjectEntityInfo r57 = createWorldMapFlyingObjectEntityInfo.createcreateWorldMapFlyingObjectEntityInfo(RMIdata);
                Logic_createWorldMapFlyingObjectEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r57.flyingObjectList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
                break;
            case RMI_PacketType.destroyWorldMapEntityInfo:
                destroyWorldMapEntityInfo r58 = destroyWorldMapEntityInfo.createdestroyWorldMapEntityInfo(RMIdata);
                Logic_destroyWorldMapEntityInfo.RMI_Packet(rmi_id, rmi_ctx, r58.destroyedEntityList);
                RMI.onRMI_Recv(rmi_ctx, packetType, "");
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
    public static void pingCheck_Response(RMI_ID rmi_id, short rmi_ctx, float timeData) {
        pingCheck_Response _data = new pingCheck_Response();
        _data.timeData = timeData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pingCheck_Response, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pingCheck_Response, "");
    }

    public static void pingCheck_Response(RMI_ID[] rmi_id, short rmi_ctx, float timeData) {
        pingCheck_Response _data = new pingCheck_Response();
        _data.timeData = timeData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pingCheck_Response, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pingCheck_Response, "");
    }

    public static void heartBeatCheck_Response(RMI_ID rmi_id, short rmi_ctx, float timeData) {
        heartBeatCheck_Response _data = new heartBeatCheck_Response();
        _data.timeData = timeData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.heartBeatCheck_Response, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.heartBeatCheck_Response, "");
    }

    public static void heartBeatCheck_Response(RMI_ID[] rmi_id, short rmi_ctx, float timeData) {
        heartBeatCheck_Response _data = new heartBeatCheck_Response();
        _data.timeData = timeData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.heartBeatCheck_Response, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.heartBeatCheck_Response, "");
    }

    public static void loginOK(RMI_ID rmi_id, short rmi_ctx) {
        loginOK _data = new loginOK();
        byte[] RMIdata = new byte[1];
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.loginOK, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.loginOK, "");
    }

    public static void loginOK(RMI_ID[] rmi_id, short rmi_ctx) {
        loginOK _data = new loginOK();
        byte[] RMIdata = new byte[1];
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.loginOK, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.loginOK, "");
    }

    public static void completeMatching(RMI_ID rmi_id, short rmi_ctx, int worldMapID, String ipAddress, int port) {
        completeMatching _data = new completeMatching();
        _data.worldMapID = worldMapID;
        _data.ipAddress = ipAddress;
        _data.port = port;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.completeMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.completeMatching, "");
    }

    public static void completeMatching(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, String ipAddress, int port) {
        completeMatching _data = new completeMatching();
        _data.worldMapID = worldMapID;
        _data.ipAddress = ipAddress;
        _data.port = port;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.completeMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.completeMatching, "");
    }

    public static void errorMatching(RMI_ID rmi_id, short rmi_ctx, int errorCode, String errorReason) {
        errorMatching _data = new errorMatching();
        _data.errorCode = errorCode;
        _data.errorReason = errorReason;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.errorMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.errorMatching, "");
    }

    public static void errorMatching(RMI_ID[] rmi_id, short rmi_ctx, int errorCode, String errorReason) {
        errorMatching _data = new errorMatching();
        _data.errorCode = errorCode;
        _data.errorReason = errorReason;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.errorMatching, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.errorMatching, "");
    }

    public static void pickLogicStart(RMI_ID rmi_id, short rmi_ctx, LinkedList<LoadingPlayerData> loadingPlayerList) {
        pickLogicStart _data = new pickLogicStart();
        _data.loadingPlayerList = loadingPlayerList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicStart, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicStart, "");
    }

    public static void pickLogicStart(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<LoadingPlayerData> loadingPlayerList) {
        pickLogicStart _data = new pickLogicStart();
        _data.loadingPlayerList = loadingPlayerList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicStart, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicStart, "");
    }

    public static void pickLogicTime(RMI_ID rmi_id, short rmi_ctx, float remainCharacterPickTime) {
        pickLogicTime _data = new pickLogicTime();
        _data.remainCharacterPickTime = remainCharacterPickTime;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicTime, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicTime, "");
    }

    public static void pickLogicTime(RMI_ID[] rmi_id, short rmi_ctx, float remainCharacterPickTime) {
        pickLogicTime _data = new pickLogicTime();
        _data.remainCharacterPickTime = remainCharacterPickTime;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicTime, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicTime, "");
    }

    public static void pickLogicUserOnSelectedCharacter(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, int characterType) {
        pickLogicUserOnSelectedCharacter _data = new pickLogicUserOnSelectedCharacter();
        _data.googleIDToken = googleIDToken;
        _data.characterType = characterType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnSelectedCharacter, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnSelectedCharacter, "");
    }

    public static void pickLogicUserOnSelectedCharacter(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, int characterType) {
        pickLogicUserOnSelectedCharacter _data = new pickLogicUserOnSelectedCharacter();
        _data.googleIDToken = googleIDToken;
        _data.characterType = characterType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnSelectedCharacter, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnSelectedCharacter, "");
    }

    public static void pickLogicUserOnReady(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnReady _data = new pickLogicUserOnReady();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnReady, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnReady, "");
    }

    public static void pickLogicUserOnReady(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnReady _data = new pickLogicUserOnReady();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnReady, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnReady, "");
    }

    public static void pickLogicUserOnCancel(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnCancel _data = new pickLogicUserOnCancel();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnCancel, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnCancel, "");
    }

    public static void pickLogicUserOnCancel(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnCancel _data = new pickLogicUserOnCancel();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnCancel, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnCancel, "");
    }

    public static void pickLogicUserOnDisconnected(RMI_ID rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnDisconnected _data = new pickLogicUserOnDisconnected();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnDisconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnDisconnected, "");
    }

    public static void pickLogicUserOnDisconnected(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken) {
        pickLogicUserOnDisconnected _data = new pickLogicUserOnDisconnected();
        _data.googleIDToken = googleIDToken;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnDisconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnDisconnected, "");
    }

    public static void pickLogicUserOnChatMessage(RMI_ID rmi_id, short rmi_ctx, String googleIDToken, String chatMessage) {
        pickLogicUserOnChatMessage _data = new pickLogicUserOnChatMessage();
        _data.googleIDToken = googleIDToken;
        _data.chatMessage = chatMessage;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnChatMessage, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnChatMessage, "");
    }

    public static void pickLogicUserOnChatMessage(RMI_ID[] rmi_id, short rmi_ctx, String googleIDToken, String chatMessage) {
        pickLogicUserOnChatMessage _data = new pickLogicUserOnChatMessage();
        _data.googleIDToken = googleIDToken;
        _data.chatMessage = chatMessage;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicUserOnChatMessage, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicUserOnChatMessage, "");
    }

    public static void pickLogicIsVoipHost(RMI_ID rmi_id, short rmi_ctx, boolean isVoipHost, int worldMapID) {
        pickLogicIsVoipHost _data = new pickLogicIsVoipHost();
        _data.isVoipHost = isVoipHost;
        _data.worldMapID = worldMapID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicIsVoipHost, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicIsVoipHost, "");
    }

    public static void pickLogicIsVoipHost(RMI_ID[] rmi_id, short rmi_ctx, boolean isVoipHost, int worldMapID) {
        pickLogicIsVoipHost _data = new pickLogicIsVoipHost();
        _data.isVoipHost = isVoipHost;
        _data.worldMapID = worldMapID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicIsVoipHost, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicIsVoipHost, "");
    }

    public static void pickLogicConnectToVoipHost(RMI_ID rmi_id, short rmi_ctx, boolean isVoipOK, int worldMapID) {
        pickLogicConnectToVoipHost _data = new pickLogicConnectToVoipHost();
        _data.isVoipOK = isVoipOK;
        _data.worldMapID = worldMapID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicConnectToVoipHost, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicConnectToVoipHost, "");
    }

    public static void pickLogicConnectToVoipHost(RMI_ID[] rmi_id, short rmi_ctx, boolean isVoipOK, int worldMapID) {
        pickLogicConnectToVoipHost _data = new pickLogicConnectToVoipHost();
        _data.isVoipOK = isVoipOK;
        _data.worldMapID = worldMapID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.pickLogicConnectToVoipHost, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.pickLogicConnectToVoipHost, "");
    }

    public static void reconnectingWorldMap(RMI_ID rmi_id, short rmi_ctx, int worldMapID, LinkedList<LoadingPlayerData> loadingPlayerList, String ipAddress, int port) {
        reconnectingWorldMap _data = new reconnectingWorldMap();
        _data.worldMapID = worldMapID;
        _data.loadingPlayerList = loadingPlayerList;
        _data.ipAddress = ipAddress;
        _data.port = port;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.reconnectingWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.reconnectingWorldMap, "");
    }

    public static void reconnectingWorldMap(RMI_ID[] rmi_id, short rmi_ctx, int worldMapID, LinkedList<LoadingPlayerData> loadingPlayerList, String ipAddress, int port) {
        reconnectingWorldMap _data = new reconnectingWorldMap();
        _data.worldMapID = worldMapID;
        _data.loadingPlayerList = loadingPlayerList;
        _data.ipAddress = ipAddress;
        _data.port = port;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.reconnectingWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.reconnectingWorldMap, "");
    }

    public static void broadcastingLoadingProgress(RMI_ID rmi_id, short rmi_ctx, LinkedList<LoadingPlayerData> loadingPlayerList) {
        broadcastingLoadingProgress _data = new broadcastingLoadingProgress();
        _data.loadingPlayerList = loadingPlayerList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingLoadingProgress, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingLoadingProgress, "");
    }

    public static void broadcastingLoadingProgress(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<LoadingPlayerData> loadingPlayerList) {
        broadcastingLoadingProgress _data = new broadcastingLoadingProgress();
        _data.loadingPlayerList = loadingPlayerList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingLoadingProgress, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingLoadingProgress, "");
    }

    public static void initializeMyselfCharacterInfo(RMI_ID rmi_id, short rmi_ctx, int ownUserEntityID) {
        initializeMyselfCharacterInfo _data = new initializeMyselfCharacterInfo();
        _data.ownUserEntityID = ownUserEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.initializeMyselfCharacterInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.initializeMyselfCharacterInfo, "");
    }

    public static void initializeMyselfCharacterInfo(RMI_ID[] rmi_id, short rmi_ctx, int ownUserEntityID) {
        initializeMyselfCharacterInfo _data = new initializeMyselfCharacterInfo();
        _data.ownUserEntityID = ownUserEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.initializeMyselfCharacterInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.initializeMyselfCharacterInfo, "");
    }

    public static void initializeWorldMap(RMI_ID rmi_id, short rmi_ctx, LinkedList<CharacterData> characterList, LinkedList<MonsterData> monsterList, LinkedList<BuffTurretData> buffTurretList, LinkedList<AttackTurretData> attackTurretList, LinkedList<BarricadeData> barricadeList, LinkedList<CrystalData> crystalList, LinkedList<SkillObjectData> skillObjectList, LinkedList<FlyingObjectData> flyingObjectList, LinkedList<BuildSlotData> buildSlotList) {
        initializeWorldMap _data = new initializeWorldMap();
        _data.characterList = characterList;
        _data.monsterList = monsterList;
        _data.buffTurretList = buffTurretList;
        _data.attackTurretList = attackTurretList;
        _data.barricadeList = barricadeList;
        _data.crystalList = crystalList;
        _data.skillObjectList = skillObjectList;
        _data.flyingObjectList = flyingObjectList;
        _data.buildSlotList = buildSlotList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.initializeWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.initializeWorldMap, "");
    }

    public static void initializeWorldMap(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CharacterData> characterList, LinkedList<MonsterData> monsterList, LinkedList<BuffTurretData> buffTurretList, LinkedList<AttackTurretData> attackTurretList, LinkedList<BarricadeData> barricadeList, LinkedList<CrystalData> crystalList, LinkedList<SkillObjectData> skillObjectList, LinkedList<FlyingObjectData> flyingObjectList, LinkedList<BuildSlotData> buildSlotList) {
        initializeWorldMap _data = new initializeWorldMap();
        _data.characterList = characterList;
        _data.monsterList = monsterList;
        _data.buffTurretList = buffTurretList;
        _data.attackTurretList = attackTurretList;
        _data.barricadeList = barricadeList;
        _data.crystalList = crystalList;
        _data.skillObjectList = skillObjectList;
        _data.flyingObjectList = flyingObjectList;
        _data.buildSlotList = buildSlotList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.initializeWorldMap, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.initializeWorldMap, "");
    }

    public static void userDisconnected(RMI_ID rmi_id, short rmi_ctx, int userEntityID) {
        userDisconnected _data = new userDisconnected();
        _data.userEntityID = userEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userDisconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userDisconnected, "");
    }

    public static void userDisconnected(RMI_ID[] rmi_id, short rmi_ctx, int userEntityID) {
        userDisconnected _data = new userDisconnected();
        _data.userEntityID = userEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userDisconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userDisconnected, "");
    }

    public static void userReconnected(RMI_ID rmi_id, short rmi_ctx, int userEntityID) {
        userReconnected _data = new userReconnected();
        _data.userEntityID = userEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userReconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userReconnected, "");
    }

    public static void userReconnected(RMI_ID[] rmi_id, short rmi_ctx, int userEntityID) {
        userReconnected _data = new userReconnected();
        _data.userEntityID = userEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userReconnected, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userReconnected, "");
    }

    public static void userCharacterDefeat(RMI_ID rmi_id, short rmi_ctx, int userEntityID, int remainTimeMilliSeconds) {
        userCharacterDefeat _data = new userCharacterDefeat();
        _data.userEntityID = userEntityID;
        _data.remainTimeMilliSeconds = remainTimeMilliSeconds;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userCharacterDefeat, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userCharacterDefeat, "");
    }

    public static void userCharacterDefeat(RMI_ID[] rmi_id, short rmi_ctx, int userEntityID, int remainTimeMilliSeconds) {
        userCharacterDefeat _data = new userCharacterDefeat();
        _data.userEntityID = userEntityID;
        _data.remainTimeMilliSeconds = remainTimeMilliSeconds;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userCharacterDefeat, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userCharacterDefeat, "");
    }

    public static void userCharacterRespawn(RMI_ID rmi_id, short rmi_ctx, int userEntityID) {
        userCharacterRespawn _data = new userCharacterRespawn();
        _data.userEntityID = userEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userCharacterRespawn, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userCharacterRespawn, "");
    }

    public static void userCharacterRespawn(RMI_ID[] rmi_id, short rmi_ctx, int userEntityID) {
        userCharacterRespawn _data = new userCharacterRespawn();
        _data.userEntityID = userEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userCharacterRespawn, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userCharacterRespawn, "");
    }

    public static void userFailedBuyItem(RMI_ID rmi_id, short rmi_ctx, int errorCode) {
        userFailedBuyItem _data = new userFailedBuyItem();
        _data.errorCode = errorCode;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedBuyItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedBuyItem, "");
    }

    public static void userFailedBuyItem(RMI_ID[] rmi_id, short rmi_ctx, int errorCode) {
        userFailedBuyItem _data = new userFailedBuyItem();
        _data.errorCode = errorCode;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedBuyItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedBuyItem, "");
    }

    public static void userFailedUseItem(RMI_ID rmi_id, short rmi_ctx, int errorCode) {
        userFailedUseItem _data = new userFailedUseItem();
        _data.errorCode = errorCode;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedUseItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedUseItem, "");
    }

    public static void userFailedUseItem(RMI_ID[] rmi_id, short rmi_ctx, int errorCode) {
        userFailedUseItem _data = new userFailedUseItem();
        _data.errorCode = errorCode;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedUseItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedUseItem, "");
    }

    public static void userFailedInstallBuilding(RMI_ID rmi_id, short rmi_ctx, int errorCode) {
        userFailedInstallBuilding _data = new userFailedInstallBuilding();
        _data.errorCode = errorCode;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedInstallBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedInstallBuilding, "");
    }

    public static void userFailedInstallBuilding(RMI_ID[] rmi_id, short rmi_ctx, int errorCode) {
        userFailedInstallBuilding _data = new userFailedInstallBuilding();
        _data.errorCode = errorCode;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedInstallBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedInstallBuilding, "");
    }

    public static void userFailedUpgradeBuilding(RMI_ID rmi_id, short rmi_ctx, int errorCode) {
        userFailedUpgradeBuilding _data = new userFailedUpgradeBuilding();
        _data.errorCode = errorCode;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedUpgradeBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedUpgradeBuilding, "");
    }

    public static void userFailedUpgradeBuilding(RMI_ID[] rmi_id, short rmi_ctx, int errorCode) {
        userFailedUpgradeBuilding _data = new userFailedUpgradeBuilding();
        _data.errorCode = errorCode;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userFailedUpgradeBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userFailedUpgradeBuilding, "");
    }

    public static void userSucceedInstallBuilding(RMI_ID rmi_id, short rmi_ctx, int builderEntityID, int buildingType) {
        userSucceedInstallBuilding _data = new userSucceedInstallBuilding();
        _data.builderEntityID = builderEntityID;
        _data.buildingType = buildingType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userSucceedInstallBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userSucceedInstallBuilding, "");
    }

    public static void userSucceedInstallBuilding(RMI_ID[] rmi_id, short rmi_ctx, int builderEntityID, int buildingType) {
        userSucceedInstallBuilding _data = new userSucceedInstallBuilding();
        _data.builderEntityID = builderEntityID;
        _data.buildingType = buildingType;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userSucceedInstallBuilding, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userSucceedInstallBuilding, "");
    }

    public static void userSucceedStoreUpgradeBuff(RMI_ID rmi_id, short rmi_ctx, StoreUpgradeBuffSlotData storeUpgradeBuffSlotData) {
        userSucceedStoreUpgradeBuff _data = new userSucceedStoreUpgradeBuff();
        _data.storeUpgradeBuffSlotData = storeUpgradeBuffSlotData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userSucceedStoreUpgradeBuff, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userSucceedStoreUpgradeBuff, "");
    }

    public static void userSucceedStoreUpgradeBuff(RMI_ID[] rmi_id, short rmi_ctx, StoreUpgradeBuffSlotData storeUpgradeBuffSlotData) {
        userSucceedStoreUpgradeBuff _data = new userSucceedStoreUpgradeBuff();
        _data.storeUpgradeBuffSlotData = storeUpgradeBuffSlotData;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.userSucceedStoreUpgradeBuff, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.userSucceedStoreUpgradeBuff, "");
    }

    public static void broadcastingStoreUpgradeBuffList(RMI_ID rmi_id, short rmi_ctx, LinkedList<StoreUpgradeBuffSlotData> storeUpgradeBuffSlotDataList) {
        broadcastingStoreUpgradeBuffList _data = new broadcastingStoreUpgradeBuffList();
        _data.storeUpgradeBuffSlotDataList = storeUpgradeBuffSlotDataList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingStoreUpgradeBuffList, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingStoreUpgradeBuffList, "");
    }

    public static void broadcastingStoreUpgradeBuffList(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<StoreUpgradeBuffSlotData> storeUpgradeBuffSlotDataList) {
        broadcastingStoreUpgradeBuffList _data = new broadcastingStoreUpgradeBuffList();
        _data.storeUpgradeBuffSlotDataList = storeUpgradeBuffSlotDataList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingStoreUpgradeBuffList, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingStoreUpgradeBuffList, "");
    }

    public static void StartGame(RMI_ID rmi_id, short rmi_ctx) {
        StartGame _data = new StartGame();
        byte[] RMIdata = new byte[1];
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.StartGame, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.StartGame, "");
    }

    public static void StartGame(RMI_ID[] rmi_id, short rmi_ctx) {
        StartGame _data = new StartGame();
        byte[] RMIdata = new byte[1];
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.StartGame, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.StartGame, "");
    }

    public static void StartWave(RMI_ID rmi_id, short rmi_ctx, int waveCount) {
        StartWave _data = new StartWave();
        _data.waveCount = waveCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.StartWave, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.StartWave, "");
    }

    public static void StartWave(RMI_ID[] rmi_id, short rmi_ctx, int waveCount) {
        StartWave _data = new StartWave();
        _data.waveCount = waveCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.StartWave, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.StartWave, "");
    }

    public static void EndWave(RMI_ID rmi_id, short rmi_ctx, int waveCount) {
        EndWave _data = new EndWave();
        _data.waveCount = waveCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.EndWave, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.EndWave, "");
    }

    public static void EndWave(RMI_ID[] rmi_id, short rmi_ctx, int waveCount) {
        EndWave _data = new EndWave();
        _data.waveCount = waveCount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.EndWave, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.EndWave, "");
    }

    public static void EndGame(RMI_ID rmi_id, short rmi_ctx, int resultCode, String resultJS) {
        EndGame _data = new EndGame();
        _data.resultCode = resultCode;
        _data.resultJS = resultJS;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.EndGame, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.EndGame, "");
    }

    public static void EndGame(RMI_ID[] rmi_id, short rmi_ctx, int resultCode, String resultJS) {
        EndGame _data = new EndGame();
        _data.resultCode = resultCode;
        _data.resultJS = resultJS;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.EndGame, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.EndGame, "");
    }

    public static void motionCharacterDoAttack(RMI_ID rmi_id, short rmi_ctx, int characterEntityID, int targetEntityID) {
        motionCharacterDoAttack _data = new motionCharacterDoAttack();
        _data.characterEntityID = characterEntityID;
        _data.targetEntityID = targetEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterDoAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterDoAttack, "");
    }

    public static void motionCharacterDoAttack(RMI_ID[] rmi_id, short rmi_ctx, int characterEntityID, int targetEntityID) {
        motionCharacterDoAttack _data = new motionCharacterDoAttack();
        _data.characterEntityID = characterEntityID;
        _data.targetEntityID = targetEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterDoAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterDoAttack, "");
    }

    public static void motionCharacterUseSkill(RMI_ID rmi_id, short rmi_ctx, int characterEntityID, SkillInfoData usedSkill) {
        motionCharacterUseSkill _data = new motionCharacterUseSkill();
        _data.characterEntityID = characterEntityID;
        _data.usedSkill = usedSkill;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterUseSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterUseSkill, "");
    }

    public static void motionCharacterUseSkill(RMI_ID[] rmi_id, short rmi_ctx, int characterEntityID, SkillInfoData usedSkill) {
        motionCharacterUseSkill _data = new motionCharacterUseSkill();
        _data.characterEntityID = characterEntityID;
        _data.usedSkill = usedSkill;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterUseSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterUseSkill, "");
    }

    public static void motionCharacterCancelSkill(RMI_ID rmi_id, short rmi_ctx, int characterEntityID, SkillInfoData usedSkill) {
        motionCharacterCancelSkill _data = new motionCharacterCancelSkill();
        _data.characterEntityID = characterEntityID;
        _data.usedSkill = usedSkill;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterCancelSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterCancelSkill, "");
    }

    public static void motionCharacterCancelSkill(RMI_ID[] rmi_id, short rmi_ctx, int characterEntityID, SkillInfoData usedSkill) {
        motionCharacterCancelSkill _data = new motionCharacterCancelSkill();
        _data.characterEntityID = characterEntityID;
        _data.usedSkill = usedSkill;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterCancelSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterCancelSkill, "");
    }

    public static void motionCharacterUseItem(RMI_ID rmi_id, short rmi_ctx, int characterEntityID, ItemInfoData usedItem) {
        motionCharacterUseItem _data = new motionCharacterUseItem();
        _data.characterEntityID = characterEntityID;
        _data.usedItem = usedItem;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterUseItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterUseItem, "");
    }

    public static void motionCharacterUseItem(RMI_ID[] rmi_id, short rmi_ctx, int characterEntityID, ItemInfoData usedItem) {
        motionCharacterUseItem _data = new motionCharacterUseItem();
        _data.characterEntityID = characterEntityID;
        _data.usedItem = usedItem;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionCharacterUseItem, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionCharacterUseItem, "");
    }

    public static void motionMonsterDoAttack(RMI_ID rmi_id, short rmi_ctx, int monsterEntityID, short targetEntityType, int targetEntityID) {
        motionMonsterDoAttack _data = new motionMonsterDoAttack();
        _data.monsterEntityID = monsterEntityID;
        _data.targetEntityType = targetEntityType;
        _data.targetEntityID = targetEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionMonsterDoAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionMonsterDoAttack, "");
    }

    public static void motionMonsterDoAttack(RMI_ID[] rmi_id, short rmi_ctx, int monsterEntityID, short targetEntityType, int targetEntityID) {
        motionMonsterDoAttack _data = new motionMonsterDoAttack();
        _data.monsterEntityID = monsterEntityID;
        _data.targetEntityType = targetEntityType;
        _data.targetEntityID = targetEntityID;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionMonsterDoAttack, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionMonsterDoAttack, "");
    }

    public static void motionMonsterUseSkill(RMI_ID rmi_id, short rmi_ctx, int monsterEntityID, SkillInfoData usedSkill) {
        motionMonsterUseSkill _data = new motionMonsterUseSkill();
        _data.monsterEntityID = monsterEntityID;
        _data.usedSkill = usedSkill;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionMonsterUseSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionMonsterUseSkill, "");
    }

    public static void motionMonsterUseSkill(RMI_ID[] rmi_id, short rmi_ctx, int monsterEntityID, SkillInfoData usedSkill) {
        motionMonsterUseSkill _data = new motionMonsterUseSkill();
        _data.monsterEntityID = monsterEntityID;
        _data.usedSkill = usedSkill;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.motionMonsterUseSkill, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.motionMonsterUseSkill, "");
    }

    public static void broadcastingCharacterSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<CharacterData> characterSnapshot) {
        broadcastingCharacterSnapshot _data = new broadcastingCharacterSnapshot();
        _data.characterSnapshot = characterSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCharacterSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCharacterSnapshot, "");
    }

    public static void broadcastingCharacterSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CharacterData> characterSnapshot) {
        broadcastingCharacterSnapshot _data = new broadcastingCharacterSnapshot();
        _data.characterSnapshot = characterSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCharacterSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCharacterSnapshot, "");
    }

    public static void broadcastingMonsterSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<MonsterData> monsterSnapshot) {
        broadcastingMonsterSnapshot _data = new broadcastingMonsterSnapshot();
        _data.monsterSnapshot = monsterSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingMonsterSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingMonsterSnapshot, "");
    }

    public static void broadcastingMonsterSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<MonsterData> monsterSnapshot) {
        broadcastingMonsterSnapshot _data = new broadcastingMonsterSnapshot();
        _data.monsterSnapshot = monsterSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingMonsterSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingMonsterSnapshot, "");
    }

    public static void broadcastingBuffTurretSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<BuffTurretData> buffTurretSnapshot) {
        broadcastingBuffTurretSnapshot _data = new broadcastingBuffTurretSnapshot();
        _data.buffTurretSnapshot = buffTurretSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBuffTurretSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBuffTurretSnapshot, "");
    }

    public static void broadcastingBuffTurretSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BuffTurretData> buffTurretSnapshot) {
        broadcastingBuffTurretSnapshot _data = new broadcastingBuffTurretSnapshot();
        _data.buffTurretSnapshot = buffTurretSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBuffTurretSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBuffTurretSnapshot, "");
    }

    public static void broadcastingAttackTurretSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<AttackTurretData> attackTurretSnapshot) {
        broadcastingAttackTurretSnapshot _data = new broadcastingAttackTurretSnapshot();
        _data.attackTurretSnapshot = attackTurretSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingAttackTurretSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingAttackTurretSnapshot, "");
    }

    public static void broadcastingAttackTurretSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<AttackTurretData> attackTurretSnapshot) {
        broadcastingAttackTurretSnapshot _data = new broadcastingAttackTurretSnapshot();
        _data.attackTurretSnapshot = attackTurretSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingAttackTurretSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingAttackTurretSnapshot, "");
    }

    public static void broadcastingBarricadeSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<BarricadeData> barricadeSnapshot) {
        broadcastingBarricadeSnapshot _data = new broadcastingBarricadeSnapshot();
        _data.barricadeSnapshot = barricadeSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBarricadeSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBarricadeSnapshot, "");
    }

    public static void broadcastingBarricadeSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BarricadeData> barricadeSnapshot) {
        broadcastingBarricadeSnapshot _data = new broadcastingBarricadeSnapshot();
        _data.barricadeSnapshot = barricadeSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBarricadeSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBarricadeSnapshot, "");
    }

    public static void broadcastingCrystalSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<CrystalData> crystalSnapshot) {
        broadcastingCrystalSnapshot _data = new broadcastingCrystalSnapshot();
        _data.crystalSnapshot = crystalSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCrystalSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCrystalSnapshot, "");
    }

    public static void broadcastingCrystalSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CrystalData> crystalSnapshot) {
        broadcastingCrystalSnapshot _data = new broadcastingCrystalSnapshot();
        _data.crystalSnapshot = crystalSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingCrystalSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingCrystalSnapshot, "");
    }

    public static void broadcastingSkillObjectSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<SkillObjectData> skillObjectSnapshot) {
        broadcastingSkillObjectSnapshot _data = new broadcastingSkillObjectSnapshot();
        _data.skillObjectSnapshot = skillObjectSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingSkillObjectSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingSkillObjectSnapshot, "");
    }

    public static void broadcastingSkillObjectSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<SkillObjectData> skillObjectSnapshot) {
        broadcastingSkillObjectSnapshot _data = new broadcastingSkillObjectSnapshot();
        _data.skillObjectSnapshot = skillObjectSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingSkillObjectSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingSkillObjectSnapshot, "");
    }

    public static void broadcastingFlyingObjectSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<FlyingObjectData> flyingObjectSnapshot) {
        broadcastingFlyingObjectSnapshot _data = new broadcastingFlyingObjectSnapshot();
        _data.flyingObjectSnapshot = flyingObjectSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingFlyingObjectSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingFlyingObjectSnapshot, "");
    }

    public static void broadcastingFlyingObjectSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<FlyingObjectData> flyingObjectSnapshot) {
        broadcastingFlyingObjectSnapshot _data = new broadcastingFlyingObjectSnapshot();
        _data.flyingObjectSnapshot = flyingObjectSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingFlyingObjectSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingFlyingObjectSnapshot, "");
    }

    public static void broadcastingBuildSlotSnapshot(RMI_ID rmi_id, short rmi_ctx, LinkedList<BuildSlotData> buildSlotList) {
        broadcastingBuildSlotSnapshot _data = new broadcastingBuildSlotSnapshot();
        _data.buildSlotList = buildSlotList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBuildSlotSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBuildSlotSnapshot, "");
    }

    public static void broadcastingBuildSlotSnapshot(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BuildSlotData> buildSlotList) {
        broadcastingBuildSlotSnapshot _data = new broadcastingBuildSlotSnapshot();
        _data.buildSlotList = buildSlotList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingBuildSlotSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingBuildSlotSnapshot, "");
    }

    public static void broadcastingGameWorldStatusSnapshot(RMI_ID rmi_id, short rmi_ctx, GameWorldStatus gameworldStatusSnapshot) {
        broadcastingGameWorldStatusSnapshot _data = new broadcastingGameWorldStatusSnapshot();
        _data.gameworldStatusSnapshot = gameworldStatusSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingGameWorldStatusSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingGameWorldStatusSnapshot, "");
    }

    public static void broadcastingGameWorldStatusSnapshot(RMI_ID[] rmi_id, short rmi_ctx, GameWorldStatus gameworldStatusSnapshot) {
        broadcastingGameWorldStatusSnapshot _data = new broadcastingGameWorldStatusSnapshot();
        _data.gameworldStatusSnapshot = gameworldStatusSnapshot;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingGameWorldStatusSnapshot, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingGameWorldStatusSnapshot, "");
    }

    public static void broadcastingDamageAmount(RMI_ID rmi_id, short rmi_ctx, int damageType, int entityID, float damageAmount) {
        broadcastingDamageAmount _data = new broadcastingDamageAmount();
        _data.damageType = damageType;
        _data.entityID = entityID;
        _data.damageAmount = damageAmount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingDamageAmount, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingDamageAmount, "");
    }

    public static void broadcastingDamageAmount(RMI_ID[] rmi_id, short rmi_ctx, int damageType, int entityID, float damageAmount) {
        broadcastingDamageAmount _data = new broadcastingDamageAmount();
        _data.damageType = damageType;
        _data.entityID = entityID;
        _data.damageAmount = damageAmount;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.broadcastingDamageAmount, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.broadcastingDamageAmount, "");
    }

    public static void createWorldMapCharacterEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<CharacterData> characterList) {
        createWorldMapCharacterEntityInfo _data = new createWorldMapCharacterEntityInfo();
        _data.characterList = characterList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapCharacterEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapCharacterEntityInfo, "");
    }

    public static void createWorldMapCharacterEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CharacterData> characterList) {
        createWorldMapCharacterEntityInfo _data = new createWorldMapCharacterEntityInfo();
        _data.characterList = characterList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapCharacterEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapCharacterEntityInfo, "");
    }

    public static void createWorldMapMonsterEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<MonsterData> monsterList) {
        createWorldMapMonsterEntityInfo _data = new createWorldMapMonsterEntityInfo();
        _data.monsterList = monsterList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapMonsterEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapMonsterEntityInfo, "");
    }

    public static void createWorldMapMonsterEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<MonsterData> monsterList) {
        createWorldMapMonsterEntityInfo _data = new createWorldMapMonsterEntityInfo();
        _data.monsterList = monsterList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapMonsterEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapMonsterEntityInfo, "");
    }

    public static void createWorldMapBuffTurretEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<BuffTurretData> buffTurretList) {
        createWorldMapBuffTurretEntityInfo _data = new createWorldMapBuffTurretEntityInfo();
        _data.buffTurretList = buffTurretList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapBuffTurretEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapBuffTurretEntityInfo, "");
    }

    public static void createWorldMapBuffTurretEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BuffTurretData> buffTurretList) {
        createWorldMapBuffTurretEntityInfo _data = new createWorldMapBuffTurretEntityInfo();
        _data.buffTurretList = buffTurretList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapBuffTurretEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapBuffTurretEntityInfo, "");
    }

    public static void createWorldMapAttackTurretEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<AttackTurretData> attackTurretList) {
        createWorldMapAttackTurretEntityInfo _data = new createWorldMapAttackTurretEntityInfo();
        _data.attackTurretList = attackTurretList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapAttackTurretEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapAttackTurretEntityInfo, "");
    }

    public static void createWorldMapAttackTurretEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<AttackTurretData> attackTurretList) {
        createWorldMapAttackTurretEntityInfo _data = new createWorldMapAttackTurretEntityInfo();
        _data.attackTurretList = attackTurretList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapAttackTurretEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapAttackTurretEntityInfo, "");
    }

    public static void createWorldMapBarricadeEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<BarricadeData> barricadeList) {
        createWorldMapBarricadeEntityInfo _data = new createWorldMapBarricadeEntityInfo();
        _data.barricadeList = barricadeList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapBarricadeEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapBarricadeEntityInfo, "");
    }

    public static void createWorldMapBarricadeEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<BarricadeData> barricadeList) {
        createWorldMapBarricadeEntityInfo _data = new createWorldMapBarricadeEntityInfo();
        _data.barricadeList = barricadeList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapBarricadeEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapBarricadeEntityInfo, "");
    }

    public static void createWorldMapCrystalDataEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<CrystalData> crystalList) {
        createWorldMapCrystalDataEntityInfo _data = new createWorldMapCrystalDataEntityInfo();
        _data.crystalList = crystalList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapCrystalDataEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapCrystalDataEntityInfo, "");
    }

    public static void createWorldMapCrystalDataEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<CrystalData> crystalList) {
        createWorldMapCrystalDataEntityInfo _data = new createWorldMapCrystalDataEntityInfo();
        _data.crystalList = crystalList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapCrystalDataEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapCrystalDataEntityInfo, "");
    }

    public static void createWorldMapSkillObjectEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<SkillObjectData> skillObjectList) {
        createWorldMapSkillObjectEntityInfo _data = new createWorldMapSkillObjectEntityInfo();
        _data.skillObjectList = skillObjectList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapSkillObjectEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapSkillObjectEntityInfo, "");
    }

    public static void createWorldMapSkillObjectEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<SkillObjectData> skillObjectList) {
        createWorldMapSkillObjectEntityInfo _data = new createWorldMapSkillObjectEntityInfo();
        _data.skillObjectList = skillObjectList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapSkillObjectEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapSkillObjectEntityInfo, "");
    }

    public static void createWorldMapFlyingObjectEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<FlyingObjectData> flyingObjectList) {
        createWorldMapFlyingObjectEntityInfo _data = new createWorldMapFlyingObjectEntityInfo();
        _data.flyingObjectList = flyingObjectList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapFlyingObjectEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapFlyingObjectEntityInfo, "");
    }

    public static void createWorldMapFlyingObjectEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<FlyingObjectData> flyingObjectList) {
        createWorldMapFlyingObjectEntityInfo _data = new createWorldMapFlyingObjectEntityInfo();
        _data.flyingObjectList = flyingObjectList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.createWorldMapFlyingObjectEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.createWorldMapFlyingObjectEntityInfo, "");
    }

    public static void destroyWorldMapEntityInfo(RMI_ID rmi_id, short rmi_ctx, LinkedList<DestroyEntityData> destroyedEntityList) {
        destroyWorldMapEntityInfo _data = new destroyWorldMapEntityInfo();
        _data.destroyedEntityList = destroyedEntityList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.destroyWorldMapEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.destroyWorldMapEntityInfo, "");
    }

    public static void destroyWorldMapEntityInfo(RMI_ID[] rmi_id, short rmi_ctx, LinkedList<DestroyEntityData> destroyedEntityList) {
        destroyWorldMapEntityInfo _data = new destroyWorldMapEntityInfo();
        _data.destroyedEntityList = destroyedEntityList;
        byte[] RMIdata = _data.getBytes(); //_data.getBytes()
        callRMI_Method(rmi_id, rmi_ctx, RMI_PacketType.destroyWorldMapEntityInfo, RMIdata);
        RMI.onRMI_Call(rmi_ctx, RMI_PacketType.destroyWorldMapEntityInfo, "");
    }

}
