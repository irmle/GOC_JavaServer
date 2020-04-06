package ECS.Factory;

import ECS.Classes.BuffAction;
import ECS.Classes.ConditionFloatParam;
import ECS.Classes.Reward;
import ECS.Classes.RewardInfo;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Type.MonsterType;
import ECS.Classes.Type.RewardType;
import ECS.Classes.Type.Jungle.JungleMobType;

import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 12월 16일 월요일
 * 업뎃날짜 : 2020 02 28 금
 * 목    적 :
 *      - 각종 보상 처리와 관련된 데이터를 파일로부터 읽어온다.
 *      - 유저 인터랙션에 따라 특정 보상이 주어질 필요가 있을 경우,
 *          읽어온 데이터를 기반으로 적절한 보상 객체를 만들어 제공하는 역할을 한다.
 *      - 보상 타입별 보상 생성 매서드 작성 예정. 현재는 몹 사냥에 대한 보상만 존재한다.
 *
 * 업뎃내용 :
 *      하드코딩 보상 테이블 >> 엑셀에 있는 보상 데이터 테이블 계산하는 매서드 만들고 적용
 */
public class RewardFactory {

    /** 멤버 변수 */

    // 몬스터 타입별로, 해당 몬스터 사냥 시 얻을 수 있는 보상이 담긴 테이블
    public static HashMap<Integer, RewardInfo> rewardInfoByKillingMonsterTable;

    // 캐릭터 레벨에 따라.. 몬스터(종류 상관NO) 사냥 시 얻을 수 있는 보상이 담긴 테이블
    public static HashMap<Integer, RewardInfo> rewardInfoByCharLevelTable;

    // 각 캐릭터 타입별로, 해당 캐릭터가 뭔가 수행 시(사냥, 접속, ..)
    //      얻을 수 있는 고유 특성이나 보상 등 정보가 담긴 테이블 >> 나중에 추가


    /** 생성자 */


    /** 매서드 */

    public static void initFactory(){

        System.out.println("Reward Factory 초기화중... ");

        // 각 멤버변수 생성 및 초기화
        rewardInfoByKillingMonsterTable = new HashMap<>();
        rewardInfoByCharLevelTable = new HashMap<>();

        // 파일로부터 정보 읽기
        readRewardInfoFromFile();

        //printInfo();


        System.out.println("Reward Factory 초기화 완료 ");
    }

    /**
     * 각종 보상 정보를 읽어오는 매서드들을 모두 호출하는 함수
     */
    public static void readRewardInfoFromFile(){

        readMonsterRewardInfoFromFile();
        readRewardInfoByCharacterLevel();

    }

    /**
     * 특정 몬스터를 죽였을 때 받을 수 있는 보상 정보가 몬스터 타입별로 정리되어 있는 파일을 읽어온다! 라고 가정
     * 현재는 구현된 몹 종류가 없어서, 일단은 전부 다 하드코딩으로 동일하게 둠.
     * 현재 동일하게 나타나는 리치 몬스터의 경우, MonsterType = NONE으로 두고 진행할 것.
     */
    public static void readMonsterRewardInfoFromFile(){

        RewardInfo rewardInfo;

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.NONE, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.GOBLIN, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.GOBLIN_FIGHTER, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.GOBLIN_AXE, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.GOBLIN_BOMB, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.GOBLIN_SHAMAN, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.ORC, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.ORC_SWORD, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.ORC_AXE, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.ORC_BOW, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.OGRE_GENERAL, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.MUMMY, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.MUMMY_PHARAOH, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.MUMMY_PIRATE, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.MUMMY_PUMPKIN, rewardInfo);

        rewardInfo = new RewardInfo(100, 100);
        rewardInfoByKillingMonsterTable.put(MonsterType.MUMMY_KETTLE, rewardInfo);

    }


    /**
     * 캐릭터 레벨에 따라 달라지는 보상정보 테이블
     */
    public static void readRewardInfoByCharacterLevel(){

        RewardInfo rewardInfo;
        for(int i=1; i<=15; i++ ){  // 해시맵이니까 1부터
            float exp = calculateRewardExpByLevel(i);
            int gold = calculateRewardGoldByLevel(i);

            rewardInfo = new RewardInfo(exp, gold);
            rewardInfoByCharLevelTable.put(i, rewardInfo);
        }

    }

    /**
     * 캐릭터 타입별로, 특정 액션을 했을 때 받을 수 있는 보상 및 추가처리에 관한 정보가 담겨있는 파일을 읽어오는 함수
     */
    // public void readRewardInfoByCharacterFromFile(){ }

    /**
     * 특정 몬스터를 죽인 것에 대한 보상을 생성할 때 호출되는 함수
     * - 나중에, createReward()라는 상위 매서드를 만들어서,
     *      인자로 받은 타입에 따라 하위 create함수를 호출하게끔 바꿀 것. (가능할지?)
     *
     *  2019 12 26 목 변경
     *      몹 살해에 대한 보상을.. 살해한 몹의 정보에 따라 주는것이 아닌, 캐릭터 레벨이 따라 지급
     *      나중에 함수를 분리하던가 다시 정리하던가 해야될거같다.
     */
    public static Reward createRewardByKillMonster(int deadEntityType, int killerLevel){

        Reward reward;

        /* 읽어온 리워드 파일 및 인자를 참고해서, 넘겨줄 리워드를 생성하는 처리 */
        // 나중에.. 더 복잡하고 상세한 처리를 할 것.

        /*int rewardType = RewardType.KILL_MONSTER;
        float rewardExp = rewardInfoByKillingMonsterTable.get(deadEntityType).exp;
        int rewardGold = rewardInfoByKillingMonsterTable.get(deadEntityType).gold;*/

        int rewardType = RewardType.KILL_MONSTER;
        float rewardExp = rewardInfoByCharLevelTable.get(killerLevel).exp;
        int rewardGold = rewardInfoByCharLevelTable.get(killerLevel).gold;

        reward = new Reward(rewardType, rewardExp, rewardGold);

        return reward;
    }

    public static Reward createRewardOfKillMonsterByTurret(int deadEntityType, int killerEntityID){

        Reward reward;

        /* 읽어온 리워드 파일 및 인자를 참고해서, 넘겨줄 리워드를 생성하는 처리 */
        // 나중에.. 더 복잡하고 상세한 처리를 할 것.

        int rewardType = RewardType.KILL_MONSTER_BY_TURRET;
        float rewardExp = rewardInfoByKillingMonsterTable.get(deadEntityType).exp;
        int rewardGold = rewardInfoByKillingMonsterTable.get(deadEntityType).gold;

        reward = new Reward(rewardType, rewardExp, rewardGold);

        return reward;
    }


    /**
     * 주어진 캐릭터 레벨에 받을 수 있는 경험치를 계산해주는 함수
     */
    public static float calculateRewardExpByLevel(int currentLevel){

        final float EXP_WEIGHT_VALUE = 0.1f;
        final float EXP_ADJUSTMENT_VALUE = 30f;

        float exp = (float) (EXP_WEIGHT_VALUE * Math.pow(currentLevel, 2) + EXP_ADJUSTMENT_VALUE );

        return exp;
    }

    /**
     * 주어진 캐릭터 레벨에 받을 수 있는 골드를 계산해주는 함수
     * 최종결과에 올림 해줘야 함
     */
    public static int calculateRewardGoldByLevel(int currentLevel){

        final float GOLD_WEIGHT_VALUE = 0.5f;
        final float GOLD_ADJUSTMENT_VALUE = 10f;

        double calculGold = (GOLD_WEIGHT_VALUE * Math.pow(currentLevel, 2) + GOLD_ADJUSTMENT_VALUE );
        int gold = (int) Math.ceil(calculGold);

        return gold;
    }

    /**
     * 테스트용 출력 함수
     */
    public static void printInfo(){

        for(int i=1; i<=rewardInfoByCharLevelTable.size(); i++){

            RewardInfo info = rewardInfoByCharLevelTable.get(i);
            System.out.println("레벨" + i + "의 보상 경험치 : " + info.exp + ", 보상 골드 : " + info.gold);
        }

    }

    /**
     * 2020 02 28
     */
    public static Reward createRewardByKillJungleMonster(int deadEntityType, int deadEntityID){

        Reward reward;

        int rewardType = RewardType.KILL_MONSTER_JUNGLE_MONSTER;
        float rewardExp = 0f;
        int rewardGold = 0;

        BuffAction rewardBuff = new BuffAction();
        rewardBuff.unitID = deadEntityID;
        rewardBuff.skillUserID = deadEntityID;
        rewardBuff.coolTime = -1;
        rewardBuff.remainCoolTime = -1;
        rewardBuff.remainTime = 0f;

        /* 일단 하드코딩으로 */
        switch (deadEntityType){

            /*case JungleMobType.HUMAN1 :
            case JungleMobType.HUMAN2 :
            case JungleMobType.HUMAN3 :
            case JungleMobType.HUMAN4 :

                rewardExp = 100;
                rewardGold = 100;
                break;*/

            case  JungleMobType.LIZARD :
                rewardExp = 300;
                rewardGold = 500;

                rewardBuff.remainTime = 1.5f * 60;
                rewardBuff.floatParam.add(new ConditionFloatParam(ConditionType.maxHPRate, 30f));

                break;

            case JungleMobType.FAIRY :
                rewardExp = 300;
                rewardGold = 500;

                rewardBuff.remainTime = 1.5f * 60;
                rewardBuff.floatParam.add(new ConditionFloatParam(ConditionType.maxMPRate, 30f));

                break;

            case JungleMobType.DRAGON :
                rewardExp = 500;
                rewardGold = 1000;

                rewardBuff.remainTime = 2.5f * 60;
                rewardBuff.floatParam.add(new ConditionFloatParam(ConditionType.attackDamageRate, 50f));
                rewardBuff.floatParam.add(new ConditionFloatParam(ConditionType.attackSpeedRate, 30f));
                rewardBuff.floatParam.add(new ConditionFloatParam(ConditionType.moveSpeedRate, 5f));

                break;

            case JungleMobType.DEVIL :
                rewardExp = 500;
                rewardGold = 1000;

                rewardBuff.remainTime = 2.5f * 60;
                rewardBuff.floatParam.add(new ConditionFloatParam(ConditionType.criticalDamageRate, 100f));
                rewardBuff.floatParam.add(new ConditionFloatParam(ConditionType.criticalChanceRate, 50f));
                rewardBuff.floatParam.add(new ConditionFloatParam(ConditionType.defenseRate, 30f));

                break;

        }


        reward = new Reward(rewardType, rewardExp, rewardGold, rewardBuff);

        return reward;
    }



}
