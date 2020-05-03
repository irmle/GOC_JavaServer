package Network.RMI_Common.RMI_ParsingClasses;

public class getSkill {

    public int worldMapID;
    public int userEntityID;
    public short skillSlotNum;
    public int skillType;

    public getSkill() { }

    public getSkill(flat_getSkill data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.skillSlotNum = data.skillSlotNum();
        this.skillType = data.skillType();
    }

    public static getSkill creategetSkill(byte[] data)
    {
        return flat_getSkill.getRootAsflat_getSkill( data );
    }

    public static byte[] getBytes(getSkill data)
    {
        return flat_getSkill.createflat_getSkill( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}