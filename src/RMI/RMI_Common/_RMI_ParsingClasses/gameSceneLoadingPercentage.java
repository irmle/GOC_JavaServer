package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class gameSceneLoadingPercentage {

    public int worldMapID;
    public float percentage;

    public gameSceneLoadingPercentage() { }

    public gameSceneLoadingPercentage(flat_gameSceneLoadingPercentage data) {
        this.worldMapID = data.worldMapID();
        this.percentage = data.percentage();
    }

    public static gameSceneLoadingPercentage creategameSceneLoadingPercentage(byte[] data)
    {
        return flat_gameSceneLoadingPercentage.getRootAsflat_gameSceneLoadingPercentage( data );
    }

    public static byte[] getBytes(gameSceneLoadingPercentage data)
    {
        return flat_gameSceneLoadingPercentage.createflat_gameSceneLoadingPercentage( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}