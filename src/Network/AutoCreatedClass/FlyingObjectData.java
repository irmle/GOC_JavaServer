package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class FlyingObjectData {

    public int entityID;
    public int team;
    public float posX;
    public float posY;
    public float posZ;
    public int userEntityID;
    public int createdSkillType;
    public float directionX;
    public float directionY;
    public float directionZ;

    public FlyingObjectData() { }

    public FlyingObjectData(flat_FlyingObjectData data) {
        this.entityID = data.entityID();
        this.team = data.team();
        this.posX = data.posX();
        this.posY = data.posY();
        this.posZ = data.posZ();
        this.userEntityID = data.userEntityID();
        this.createdSkillType = data.createdSkillType();
        this.directionX = data.directionX();
        this.directionY = data.directionY();
        this.directionZ = data.directionZ();
    }

    public static FlyingObjectData createFlyingObjectData(byte[] data)
    {
        return flat_FlyingObjectData.getRootAsflat_FlyingObjectData( data );
    }

    public static byte[] getBytes(FlyingObjectData data)
    {
        return flat_FlyingObjectData.createflat_FlyingObjectData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}