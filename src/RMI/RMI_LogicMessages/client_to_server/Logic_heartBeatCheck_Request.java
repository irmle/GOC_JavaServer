package RMI.RMI_LogicMessages.client_to_server;

import RMI.RMI_Classes.*;
import RMI.RMI_Common.*;

public class Logic_heartBeatCheck_Request {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, float timeData)
    {
        //do something.
        server_to_client.heartBeatCheck_Response(rmi_id, rmi_ctx, timeData);
    }
}