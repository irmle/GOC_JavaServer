package RMI.RMI_Common._RMI_ParsingClasses;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class createWorldMapAttackTurretEntityInfo {

    public LinkedList <AttackTurretData> attackTurretList = new LinkedList<>();

    public createWorldMapAttackTurretEntityInfo() { }

    public createWorldMapAttackTurretEntityInfo(flat_createWorldMapAttackTurretEntityInfo data) {
        for(int i = 0;i < data.attackTurretListLength();i++) {
            this.attackTurretList.addLast(new AttackTurretData(data.attackTurretList(i)));
        }
    }

    public static createWorldMapAttackTurretEntityInfo createcreateWorldMapAttackTurretEntityInfo(byte[] data)
    {
        return flat_createWorldMapAttackTurretEntityInfo.getRootAsflat_createWorldMapAttackTurretEntityInfo( data );
    }

    public static byte[] getBytes(createWorldMapAttackTurretEntityInfo data)
    {
        return flat_createWorldMapAttackTurretEntityInfo.createflat_createWorldMapAttackTurretEntityInfo( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}