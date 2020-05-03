package Network.RMI_Common.RMI_ParsingClasses;

public class installTurret {

    public int worldMapID;
    public int userEntityID;
    public int turretType;
    public int areaNumber;

    public installTurret() { }

    public installTurret(flat_installTurret data) {
        this.worldMapID = data.worldMapID();
        this.userEntityID = data.userEntityID();
        this.turretType = data.turretType();
        this.areaNumber = data.areaNumber();
    }

    public static installTurret createinstallTurret(byte[] data)
    {
        return flat_installTurret.getRootAsflat_installTurret( data );
    }

    public static byte[] getBytes(installTurret data)
    {
        return flat_installTurret.createflat_installTurret( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}