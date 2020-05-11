package ECS.Chatting;

import ECS.Chatting.Classes.ChattingUser;
import ECS.Chatting.Type.ChannelType;
import Network.RMI_Classes.RMI_ID;

import java.io.CharArrayReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ChattingManager {

    /** 멤버 변수 */

    /* 접속자 관리 */
    public static LinkedList<ChattingUser> chattingUserList;
    public static HashMap<Integer, ChattingUser> chattingUserMap;  // < rmi_host_id, chattingUser >


    /* 채널 목록 */
    public static HashMap<Integer, LinkedList<ChattingUser>> lobbyChannelMap;      // 로비 채널 목록 ( 1 ~ 9999 )
    public static HashMap<Integer, LinkedList<ChattingUser>> sessionChannelMap;    // 세션 채널 목록 (생성된 게임월드 갯수만큼 )
    public static HashMap<Integer, LinkedList<ChattingUser>> guildChannelMap;      // 길드 채널 목록 ( 존재하는 길드 갯수만큼 )

    //동기화 제어용 모니터객체
    final Object locking = new Object();


    /** 생성자 & 초기화 매서드 */
    public static void initChattingManager(){

        System.out.println("ChattingManager 초기화중...");

        /* 접속자 목록 초기화 */
        chattingUserList = new LinkedList<>();
        chattingUserMap = new HashMap<>();

        /* 채널 목록 초기화 */
        lobbyChannelMap = new HashMap<>();
        sessionChannelMap = new HashMap<>();
        guildChannelMap = new HashMap<>();


        // 그 외 추가 초기화 필요한 경우 작업 작성


        System.out.println("ChattingManager 초기화 완료");

    }




    /** 매서드 */


    public static void broadcastChatMsg_Lobby(ChattingUser user, String Message){

        // 유저가 속한 채널을 찾음
        LinkedList<ChattingUser> channel = lobbyChannelMap.get(user.lobbyChannelNum);

        for (ChattingUser channelMember : channel){

            // 머.. RMI_ID 하나를 대상으로? 혹은 여럿을 대상으로 하는 RMI broadcasting 매서드를 호출하면 되겟지.

        }

    }

    public static void broadcastChatMsg_Session(ChattingUser user, String Message){

        // 유저가 속한 채널을 찾음
        LinkedList<ChattingUser> channel = sessionChannelMap.get(user.sessionChannelNum);

        for (ChattingUser channelMember : channel){

            // 머.. RMI_ID 하나를 대상으로? 혹은 여럿을 대상으로 하는 RMI broadcasting 매서드를 호출하면 되겟지.

        }

    }

    public static void broadcastChatMsg_Guild(ChattingUser user, String Message){

        // 유저가 속한 채널을 찾음
        LinkedList<ChattingUser> channel = guildChannelMap.get(user.guildChannelNum);

        for (ChattingUser channelMember : channel){

            // 머.. RMI_ID 하나를 대상으로? 혹은 여럿을 대상으로 하는 RMI broadcasting 매서드를 호출하면 되겟지.

        }

    }


    /**
     * 메시지(패킷) 타입에 맞춰서.. 위 브로드캐스트를 호출하거나.. 하는 매서드 필요할수도
     * 아님 걍 각 메시지 타입별 RMI 매서드 내부에서 위 함수들을 호출하던지.
     *
     */



    /**
     * 접속유저 생성.. 굳이 이렇게 안해도 될지도??
     */
    public static ChattingUser joinChattingServer(RMI_ID rmi_id){

        ChattingUser newUser = new ChattingUser(rmi_id);

        chattingUserList.add(newUser);

        return  newUser;

    }


    /**
     * 유저가 접속해있는 채널 전부 해제.
     */
    public void leaveChattingServer(RMI_ID rmi_id){

        ChattingUser leaveUser = chattingUserMap.get(rmi_id);

        leaveLobbyChannel(leaveUser);
        leaveSessionChannel(leaveUser);
        leaveGuildChannel(leaveUser);

        chattingUserMap.remove(rmi_id);
        chattingUserList.remove(leaveUser);

    }

    public static void leaveLobbyChannel(ChattingUser user){

        LinkedList<ChattingUser> channel = lobbyChannelMap.get(user.lobbyChannelNum);

        if(channel.contains(user)){
            channel.remove(user);
        }

        user.setLobbyChannelNum(-1);

    }

    public static void leaveSessionChannel(ChattingUser user){

        LinkedList<ChattingUser> channel = sessionChannelMap.get(user.lobbyChannelNum);

        if(channel.contains(user)){
            channel.remove(user);
        }

        user.setSessionChannelNum(-1);

    }

    public static void leaveGuildChannel(ChattingUser user){

        LinkedList<ChattingUser> channel = guildChannelMap.get(user.lobbyChannelNum);

        if(channel.contains(user)){
            channel.remove(user);
        }

        user.setGuildChannelNum(-1);
    }



    public static void joinLobbyChannel(int channelNum, ChattingUser user){

        LinkedList<ChattingUser> channelMemberList = getChannel(ChannelType.LOBBY, channelNum);

        channelMemberList.add(user);

        user.setLobbyChannelNum(channelNum);

    }

    public static void joinSessionChannel(int channelNum, ChattingUser user){

        LinkedList<ChattingUser> channelMemberList = getChannel(ChannelType.SESSION, channelNum);

        channelMemberList.add(user);

        user.setLobbyChannelNum(channelNum);

    }

    public static void joinGuildChannel(int channelNum, ChattingUser user){

        LinkedList<ChattingUser> channelMemberList = getChannel(ChannelType.GUILD, channelNum);

        channelMemberList.add(user);

        user.setLobbyChannelNum(channelNum);

    }


    /**
     *
     */
    public static ChattingUser searchUser(RMI_ID rmi_id){

        ChattingUser wantUser = null;
        for (ChattingUser chattingUser : chattingUserList){

            if (chattingUser.rmi_id == rmi_id){
                wantUser = chattingUser;
                break;
            }
        }

        return wantUser;

    }




    /**
     * 채널(로비) 번호 할당 매서드 ( 막 접속한 유저에게 채널 할당 해주는거임 )
     */
    public static int allocChannelNum(){

        long seedValue = System.currentTimeMillis();

        int channelValue = (int)(seedValue % 10000) + 1;

        return channelValue;

    }


    /**
     *
     * 채널에 가입시키기. 채널이 존재하지 않으면, 만들어서라도 가입시킨다
     */
    public void joinChannel(int channelType, int channelNum, ChattingUser chattingUser){

        LinkedList<ChattingUser> channelMemberList = getChannel(channelType, channelNum);

        switch (channelType){

            case ChannelType.LOBBY :

                chattingUser.setLobbyChannelNum(channelNum);
                break;

            case ChannelType.SESSION :

                chattingUser.setSessionChannelNum(channelNum);
                break;

            case ChannelType.GUILD :

                chattingUser.setGuildChannelNum(channelNum);
                break;

        }


        channelMemberList.add(chattingUser);

    }

    /**
     * 채널 (정확히는 채널 가입자 목록) 획득
     * @param channelType
     * @param channelNum
     * @return
     */
    public static LinkedList<ChattingUser> getChannel(int channelType, int channelNum){

        LinkedList<ChattingUser> channelMemberList = null;

        boolean isExistChannel;
        switch (channelType){

            case ChannelType.LOBBY :

                isExistChannel = lobbyChannelMap.containsKey(channelNum);
                if( !isExistChannel){

                    createNewChannel(channelType, channelNum);
                }
                channelMemberList = lobbyChannelMap.get(channelNum);

                break;

            case ChannelType.SESSION :

                isExistChannel = sessionChannelMap.containsKey(channelNum);
                if( !isExistChannel){

                    createNewChannel(channelType, channelNum);
                }
                channelMemberList = sessionChannelMap.get(channelNum);

                break;

            case ChannelType.GUILD :

                isExistChannel = guildChannelMap.containsKey(channelNum);
                if( !isExistChannel){

                    createNewChannel(channelType, channelNum);
                }
                channelMemberList = guildChannelMap.get(channelNum);

                break;

        }


        return channelMemberList;


    }



    /**
     * 채널 변경 매서드 // 굳이.. 변경처리하는 '매서드'가 필요하진 않을수도?? 걍 RMI에서 처리 로직을 전개하는 게 나을수도 있을듯.
     */
    public void changeChannel(ChattingUser chattingUser){






    }


    /**
     * 채널이 비어있는지 확인. ( 접속자가 아무도 없으면, 목록에서 해제?.. 그대로 냅둘수도.)
     */
    public boolean isEmptyChannel(int channelType, int channelNum){

        boolean isEmpty = false;

        switch (channelType){

            case ChannelType.LOBBY :

                isEmpty = lobbyChannelMap.get(channelNum).isEmpty();
                break;

            case ChannelType.SESSION :

                isEmpty = sessionChannelMap.get(channelNum).isEmpty();
                break;

            case ChannelType.GUILD :

                isEmpty = guildChannelMap.get(channelNum).isEmpty();
                break;

        }

        return isEmpty;

    }



    /**
     * 채널 생성 매서드
     */
    public static void createNewChannel(int channelType, int channelNum){

        LinkedList<ChattingUser> chattingUserList = new LinkedList<>();

        switch (channelType){

            case ChannelType.LOBBY :

                lobbyChannelMap.put(channelNum, chattingUserList);
                break;

            case ChannelType.SESSION :

                sessionChannelMap.put(channelNum, chattingUserList);
                break;

            case ChannelType.GUILD :

                guildChannelMap.put(channelNum, chattingUserList);
                break;

            default:

                break;


        }

    }


    /**
     * 채널 해제 매서드
     */
    public void releaseEmptyChannel(int channelType, int channelNum){

        switch (channelType){

            case ChannelType.LOBBY :

                lobbyChannelMap.remove(channelNum);
                break;

            case ChannelType.SESSION :

                sessionChannelMap.remove(channelNum);
                break;

            case ChannelType.GUILD :

                guildChannelMap.remove(channelNum);
                break;

            default:

                break;

        }

    }




    /**
     * 유저정보(JS) 파싱 매서드 ( 패킷에 들어있는, 보낸이 관련.. 뭐 userToken이나 닉네임 등등, 대표캐릭 등 )
     */

    /**
     * 각종 메시지 내용(JS) 파싱 매서드 :
     *  -- 일반 텍스트 메시지는 뭐 필요없을 수 있는데,
     *  -- 나중에 캐릭터 소환, 아이템 강화, 캐릭터 승급/강화 등등등 처리하려면.
     *      각 구조에 맞는 JS 파싱이 필요할 것.
     */








}
