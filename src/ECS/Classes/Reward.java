package ECS.Classes;

import java.util.ArrayList;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 14 토요일
 * 업뎃날짜 : 2020 02 28 금
 *
 * 캐릭터가 몹을 처치할 때 마다 받을 보상을 처리하기 위한 클래스.
 * 각 캐릭터는 보상History 목록을 가지고 있고, 몹을 죽일 때 마다 해당 목록에 보상 객체가 하나씩 쌓인다.
 * 몹 죽임 판정은 현재 HP 시스템에서 처리한다. 추후 사망/부활 시스템으로 옮겨갈 가능성 있음.
 * 매 틱레이트마다 쌓인 보상 목록을, 보상 시스템에서 처리한다.
 *
 */
public class Reward implements Cloneable{

    /** 멤버 변수 */
    //public int receiverID;  // 보상 받을 Entity에게 할당된 ID. 우선은 캐릭터 Entity만 해당될듯
    //public int killedEnemyType; // 보상이 무엇으로부터 나오는가?
                                // 지금은 몬스터 Entity만 해당되는데, 나중엔 뭐 수정 깨부셨다거나
                                // 꼭 적을 처치한 것이 아니더라도 보상 받을 루트가 생길수도 있지.
                                // 그때 변수명 바꿀 것.
                                // 타입도 일단 int로 하다가, 나중에 Enum으로 통일할 것.

    /* ============================= */
    public int rewardType;  // 보상 원인. 무엇에 의한 보상인지?? 예)) 몹 처치, 접속유지, 특정상태, ... 등등을 타입으로 정의해서 사용할 것.
                            // 타입에 따라 아래 보상 목록 중에서 무엇을 받는지가 달라진다.
                            // 경험치만 받는 보상종류도 있을거고, 둘 다 받는 종류도 있을거고..
                            // 지금은 몹처치밖에 없는데, 이 타입의 경우 경험치와 골드를 둘 다 받음. 나중에 바뀔수도.
    /* 보상 목록 */
    public float rewardExp;
    public int rewardGold;

    /** 2020 02 28 */
    //public BuffAction rewardBuff;

    public ArrayList<BuffAction> rewardBuff;



    /** 생성자 */
    public Reward(int rewardType, float rewardExp, int rewardGold) {
        this.rewardType = rewardType;
        this.rewardExp = rewardExp;
        this.rewardGold = rewardGold;

        //rewardBuff = new BuffAction();
        rewardBuff = new ArrayList<>();
    }

    public Reward(int rewardType, float rewardExp, int rewardGold, ArrayList<BuffAction> rewardBuff) {
        this.rewardType = rewardType;
        this.rewardExp = rewardExp;
        this.rewardGold = rewardGold;
        this.rewardBuff = rewardBuff;
    }

    /** 매서드 */

    @Override
    public Reward clone() {

        Reward reward;

        try {
            reward = (Reward) super.clone();

            reward.rewardBuff = new ArrayList<>();
            for(int i=0; i<rewardBuff.size(); i++){
                reward.rewardBuff.add( (BuffAction) rewardBuff.get(i).clone());
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return reward;

    }
}
