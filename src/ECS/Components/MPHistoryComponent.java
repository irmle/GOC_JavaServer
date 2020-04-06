package ECS.Components;

import ECS.Classes.DamageHistory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MPHistoryComponent implements Cloneable{

    public List<DamageHistory> mpHistory;

    public MPHistoryComponent() {

        mpHistory = new ArrayList<>();
    }

    @Override
    public Object clone() {
        MPHistoryComponent mpHistoryComponent;

        try {
            mpHistoryComponent = (MPHistoryComponent) super.clone();

            mpHistoryComponent.mpHistory = new ArrayList<>();
            for(int i=0; i<mpHistory.size(); i++){
                mpHistoryComponent.mpHistory.add((DamageHistory) mpHistory.get(i).clone());
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return mpHistoryComponent;
    }
}
