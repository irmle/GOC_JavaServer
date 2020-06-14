package ECS.Game;

import java.util.HashMap;

import Network.RMI_Classes.RMI_ID;


//로그인 토큰ID값과, RMI통신을 위해 할당된 RMI_ID를 매핑하여 관리하는 세션 클래스.
//rmi_hostID로 TokenID를 구하려고 할 때 사용됨.
public class SessionManager {

    //RMI_HostID(int), tokenID(String) 목록정보.
    static HashMap<Integer, String> sessionMappingList;


    //SessionManager 초기화 메소드. 생성자 느낌.
    public static void initSessionManager()
    {
        System.out.println("SessionManager 초기화중...");

        sessionMappingList = new HashMap<>();

        System.out.println("SessionManager 초기화 완료");
    }



    //RMI_HostID로부터 String TokenID를 가져온다.
    public static String findTokenIDfromRMI_HostID(int rmi_hostID)
    {
        String result = null;
        if(sessionMappingList.containsKey(rmi_hostID))
            result = sessionMappingList.get(rmi_hostID);

        return result;
    }


    //클라이언트로부터 Logic_requestLogin 메소드가 호출되었을 때. 해당하는 RMI_ID와 Token값을 세팅함.
    public static boolean login(int rmi_hostID, String tokenID)
    {
        boolean success = false;
        if(!sessionMappingList.containsValue(tokenID)){
            sessionMappingList.put(rmi_hostID, tokenID);
            success = true;
        }
        else {
            success = false;
            System.out.println("이미 접속중인 계정입니다.");
            RMI_ID target = RMI_ID.findRMI_HOST_ID(rmi_hostID);
            target.getTCP_Object().close();
        }

        return success;
    }

    //클라이언트와의 연결이 끊겼을 때 호출됨. 접속이 끊겼을 경우, 매핑 리스트에서 제거한다.
    public static void logout(int rmi_hostID)
    {
        if(sessionMappingList.containsKey(rmi_hostID))
            sessionMappingList.remove(rmi_hostID);
    }

}
