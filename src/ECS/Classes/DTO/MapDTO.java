package ECS.Classes.DTO;

import ECS.Classes.MapInfo;

import java.util.ArrayList;

public class MapDTO implements Cloneable {
    public int low;
    public ArrayList<MapInfo> mapInfos;

    /* 생성자 추가 */
    public MapDTO() {
    }

    public MapDTO(int low, ArrayList<MapInfo> mapInfos) {
        this.low = low;
        this.mapInfos = mapInfos;
    }

    @Override
    public MapDTO clone() {
        MapDTO clone;
        try {
            clone = (MapDTO) super.clone();
            clone.mapInfos = new ArrayList<>();
            for (int i = 0; i <mapInfos.size() ; i++) {
                clone.mapInfos.add((MapInfo)mapInfos.get(i).clone());
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }


}
