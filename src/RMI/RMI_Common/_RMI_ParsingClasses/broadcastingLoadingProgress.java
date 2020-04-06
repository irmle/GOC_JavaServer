package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class broadcastingLoadingProgress {

    public LinkedList <LoadingPlayerData> loadingPlayerList = new LinkedList<>();

    public broadcastingLoadingProgress() { }

    public broadcastingLoadingProgress(flat_broadcastingLoadingProgress data) {
        for(int i = 0;i < data.loadingPlayerListLength();i++) {
            this.loadingPlayerList.addLast(new LoadingPlayerData(data.loadingPlayerList(i)));
        }
    }

    public static broadcastingLoadingProgress createbroadcastingLoadingProgress(byte[] data)
    {
        return flat_broadcastingLoadingProgress.getRootAsflat_broadcastingLoadingProgress( data );
    }

    public static byte[] getBytes(broadcastingLoadingProgress data)
    {
        return flat_broadcastingLoadingProgress.createflat_broadcastingLoadingProgress( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}