package RMI.RMI_LogicMessages.client_to_server;

import ECS.Game.MatchingManager;
import RMI.RMI_Classes.*;

public class Logic_cancelMatching {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, String googleIDToken)
    {
        //매칭 취소.
        MatchingManager.cancelMatching(googleIDToken);
    }
}