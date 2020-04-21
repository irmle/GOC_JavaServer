package ECS.Classes;

import ECS.Classes.Type.Jungle.JungleMobState;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 27
 * 업뎃날짜 : 오후 8:46 2020-04-08
 * 목    적 :
 * 업뎃내용 :
 *
 *
 */
public class JungleMonsterSlot {

    public static final int PATIENCE_VALUE = 5;

    public int slotNum;
    public MapInfo slotPoint; // 맵상 지점

    public int jungleMobType;  // 정글몹 종류를 나타냄
    public int monsterID;      // 슬롯에 속해있는 몹 객체의 ID. (다른 몹과 마찬가지로, 월드맵의 몬스터Entity 목록에 포함됨)

    public int monsterState;      // 정글몹 슬롯의 상태 ..정확히는 슬롯에 속한 몬스터의 상태가 어떠한가?를 나타냄

    public float regenTime;    // 현 슬롯의 몹이 재생성하는 데 걸리는 시간 (고정값)
    public float remainedRegenTime;    // 몹 리젠까지 남은 시간

    /** 오후 3:56 2020-04-21 추가 */
    public int regenCount;

    /** 오후 8:46 2020-04-08 추가 */
    public int patience;


    /**
     * 맵에 있는 정글몹 스폰 포인트 목록을 찾아 정글몹슬롯을 생성하고 할당할 때에 쓰일 생성자.
     * @param slotNum
     * @param slotPoint
     */
    public JungleMonsterSlot(int slotNum, MapInfo slotPoint) {
        this.slotNum = slotNum;
        this.slotPoint = slotPoint;

        monsterState = JungleMobState.EMPTY;
        patience = PATIENCE_VALUE;

        regenCount = 0;
    }


    /**
     * 추가 할 매서드 : 슬롯의 몹이 할당될 때 호출.
     *                  몹의 종류(타입), 생성된 몹의 ID, 몹의 리젠시간을 세팅해줌.
     */
    public void setJungleMonster(int jungleMobType, int monsterID, float regenTime){

        this.jungleMobType = jungleMobType;
        this.monsterID = monsterID;
        this.regenTime = regenTime;

        remainedRegenTime = 0f;
        patience = PATIENCE_VALUE;
    }

    /**
     * 아니면.. 위에거 대신, 아래 두 함수를 연달아 호출할 수도.
     *  아래 setInfo 함수를 먼저 호출해, 몬스터 정보를 지정하고, 슬롯에 지정된 몬스터 타입 등 정보에 따라 몬스터가 생성이 된 후에
     *      setMob 함수를 호출해서 생성된 몬스터객체와 연결해주는거임.
     * @param monsterID
     */
    public void setJungleMonster(int monsterID){

        this.monsterID = monsterID;
        patience = PATIENCE_VALUE;
    }

    public void setJungleMonsterInfo(int jungleMobType, float regenTime){

        this.jungleMobType = jungleMobType;
        this.regenTime = regenTime;

        remainedRegenTime = 0f;
        patience = PATIENCE_VALUE;
    }

    public void setMonsterState(int state){

        this.monsterState = state;
    }

    public void resetRemainedRegenTime(){

        this.remainedRegenTime = this.regenTime;
    }

    public void reduceRemainedRegenTime(float deltaTime){
        this.remainedRegenTime -= deltaTime;
    }

    public void reducePatience(){

        this.patience--;
    }

    public void resetPatience(){

        this.patience = PATIENCE_VALUE;
    }

    public boolean checkMonsterIsNotPatient(){

        boolean isNotPatinet = (this.patience <= 0) ? true : false;

        return isNotPatinet;
    }

    public void increaseRegenCount(){

        this.regenCount++;
    }



}
