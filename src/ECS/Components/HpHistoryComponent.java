package ECS.Components;

import ECS.Classes.BuffAction;
import ECS.Classes.DamageHistory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HpHistoryComponent implements Cloneable {

    public List<DamageHistory> hpHistory;

    public HpHistoryComponent() {

        hpHistory = new ArrayList<>();
    }

    @Override
    public Object clone()  {
        HpHistoryComponent hpHistoryComponent;

        try {
            hpHistoryComponent = (HpHistoryComponent) super.clone();

            hpHistoryComponent.hpHistory = new ArrayList<>();
            for(int i=0; i<hpHistory.size(); i++){
                hpHistoryComponent.hpHistory.add((DamageHistory) hpHistory.get(i).clone());
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return hpHistoryComponent;
    }
}
