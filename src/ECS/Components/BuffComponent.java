package ECS.Components;

import ECS.Classes.BuffAction;

import java.util.ArrayList;
import java.util.List;

public class BuffComponent implements Cloneable {

    public BuffAction buffActionInfo; //버프포탑에 적용되는 버프정보

    // public int [ ] targetIDList; // 타겟ID 목록
    public List<Integer> targetIDList;

    public float buffAreaRange;

    public BuffComponent(){}

    public BuffComponent(BuffAction buffActionInfo, float buffAreaRange) {
        this.buffActionInfo = buffActionInfo;
        this.buffAreaRange = buffAreaRange;

        targetIDList = new ArrayList<>();

    }

    public BuffComponent(BuffAction buffActionInfo, List<Integer> targetIDList, float buffAreaRange) {
        this.buffActionInfo = buffActionInfo;
        this.targetIDList = targetIDList;
        this.buffAreaRange = buffAreaRange;
    }

    @Override
    public Object clone()  {
        BuffComponent buffComponent;

        try {
            buffComponent = (BuffComponent) super.clone();

            buffComponent.buffActionInfo = (BuffAction) buffActionInfo.clone();

            buffComponent.targetIDList = new ArrayList<>();
            for(int i=0; i<targetIDList.size(); i++){
                buffComponent.targetIDList.add(targetIDList.get(i));
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return buffComponent;
    }

}
