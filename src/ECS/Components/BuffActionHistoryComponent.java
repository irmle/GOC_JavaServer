package ECS.Components;

import ECS.Classes.BuffAction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BuffActionHistoryComponent implements Cloneable {

    public ArrayList<BuffAction> conditionHistory;

    public BuffActionHistoryComponent() {

        conditionHistory = new ArrayList<>();
    }

    @Override
    public Object clone()  {

        BuffActionHistoryComponent buffActionHistoryComponent;

        try {
            buffActionHistoryComponent = (BuffActionHistoryComponent) super.clone();

            buffActionHistoryComponent.conditionHistory = new ArrayList<>();
            for(int i=0; i<conditionHistory.size(); i++){
                buffActionHistoryComponent.conditionHistory.add((BuffAction) conditionHistory.get(i).clone());
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return buffActionHistoryComponent;
    }
}
