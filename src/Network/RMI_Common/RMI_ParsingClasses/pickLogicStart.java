package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class pickLogicStart {

    public LinkedList <LoadingPlayerData> loadingPlayerList = new LinkedList<>();

    public pickLogicStart() { }

    public pickLogicStart(flat_pickLogicStart data) {
        for(int i = 0;i < data.loadingPlayerListLength();i++) {
            this.loadingPlayerList.addLast(new LoadingPlayerData(data.loadingPlayerList(i)));
        }
    }

    public static pickLogicStart createpickLogicStart(byte[] data)
    {
        return flat_pickLogicStart.getRootAsflat_pickLogicStart( data );
    }

    public static byte[] getBytes(pickLogicStart data)
    {
        return flat_pickLogicStart.createflat_pickLogicStart( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}