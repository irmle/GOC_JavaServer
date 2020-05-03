package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;

public class SkillObjectData {

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

    public SkillObjectData() { }

    public SkillObjectData(flat_SkillObjectData data) {
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

    public static SkillObjectData createSkillObjectData(byte[] data)
    {
        return flat_SkillObjectData.getRootAsflat_SkillObjectData( data );
    }

    public static byte[] getBytes(SkillObjectData data)
    {
        return flat_SkillObjectData.createflat_SkillObjectData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}