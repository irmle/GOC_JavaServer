package Network.RMI_LogicMessages.client_to_server;

import Network.RMI_Classes.*;
import Network.RMI_Common.*;

public class Logic_pingCheck_Request {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, float timeData)
    {
        //do something.
        server_to_client.pingCheck_Response(rmi_id, rmi_ctx, timeData);
    }
}