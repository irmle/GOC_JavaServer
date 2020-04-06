package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class createWorldMapBuffTurretEntityInfo {

    public LinkedList <BuffTurretData> buffTurretList = new LinkedList<>();

    public createWorldMapBuffTurretEntityInfo() { }

    public createWorldMapBuffTurretEntityInfo(flat_createWorldMapBuffTurretEntityInfo data) {
        for(int i = 0;i < data.buffTurretListLength();i++) {
            this.buffTurretList.addLast(new BuffTurretData(data.buffTurretList(i)));
        }
    }

    public static createWorldMapBuffTurretEntityInfo createcreateWorldMapBuffTurretEntityInfo(byte[] data)
    {
        return flat_createWorldMapBuffTurretEntityInfo.getRootAsflat_createWorldMapBuffTurretEntityInfo( data );
    }

    public static byte[] getBytes(createWorldMapBuffTurretEntityInfo data)
    {
        return flat_createWorldMapBuffTurretEntityInfo.createflat_createWorldMapBuffTurretEntityInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}