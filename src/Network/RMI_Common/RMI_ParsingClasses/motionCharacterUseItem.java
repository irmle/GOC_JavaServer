package Network.RMI_Common.RMI_ParsingClasses;

import Network.AutoCreatedClass.*;

public class motionCharacterUseItem {

    public int characterEntityID;
    public ItemInfoData usedItem;

    public motionCharacterUseItem() { }

    public motionCharacterUseItem(flat_motionCharacterUseItem data) {
        this.characterEntityID = data.characterEntityID();
        this.usedItem = new ItemInfoData(data.usedItem());
    }

    public static motionCharacterUseItem createmotionCharacterUseItem(byte[] data)
    {
        return flat_motionCharacterUseItem.getRootAsflat_motionCharacterUseItem( data );
    }

    public static byte[] getBytes(motionCharacterUseItem data)
    {
        return flat_motionCharacterUseItem.createflat_motionCharacterUseItem( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}