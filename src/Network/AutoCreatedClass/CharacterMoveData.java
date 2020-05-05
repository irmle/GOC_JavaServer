package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class CharacterMoveData {

    public int entityID;
    public float posX;
    public float posY;
    public float posZ;
    public float velX;
    public float velY;
    public float velZ;
    public float quarternionY;
    public float quarternionZ;

    public CharacterMoveData() { }

    public CharacterMoveData(flat_CharacterMoveData data) {
        this.entityID = data.entityID();
        this.posX = data.posX();
        this.posY = data.posY();
        this.posZ = data.posZ();
        this.velX = data.velX();
        this.velY = data.velY();
        this.velZ = data.velZ();
        this.quarternionY = data.quarternionY();
        this.quarternionZ = data.quarternionZ();
    }

    public static CharacterMoveData createCharacterMoveData(byte[] data)
    {
        return flat_CharacterMoveData.getRootAsflat_CharacterMoveData( data );
    }

    public static byte[] getBytes(CharacterMoveData data)
    {
        return flat_CharacterMoveData.createflat_CharacterMoveData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}