package ECS.Components;

import ECS.Classes.Reward;

import java.util.LinkedList;
import java.util.List;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 14 토요일
 * 업뎃날짜 :
 *
 * 캐릭터가 몹을 처치할 때 마다 받을 보상 목록을 쌓아두기 위한 클래스
 * 각 캐릭터는 보상History 목록을 가지고 있고, 몹을 죽일 때 마다 해당 목록에 보상 객체가 하나씩 쌓인다.
 * 매 틱레이트마다 쌓인 보상 목록을, 보상 시스템에서 처리한다.
 * 처리가 끝나면 비워진다.
 *
 */
public class RewardHistoryComponent implements Cloneable {

    /** 멤버 변수 */

    public List<Reward> rewardHistory;

    /** 생성자 */
    public RewardHistoryComponent() {

        rewardHistory = new LinkedList<>();
    }

    /** 매서드 */
    @Override
    protected RewardHistoryComponent clone()  {
        RewardHistoryComponent rewardHistoryComponent;

        try {
            rewardHistoryComponent = (RewardHistoryComponent) super.clone();

            rewardHistoryComponent.rewardHistory = new LinkedList<>();
            for(int i=0; i<rewardHistory.size(); i++){
                rewardHistoryComponent.rewardHistory.add( rewardHistory.get(i).clone() );
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return rewardHistoryComponent;
    }
}
