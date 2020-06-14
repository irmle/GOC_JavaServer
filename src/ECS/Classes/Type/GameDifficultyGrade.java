package ECS.Classes.Type;
import ECS.Classes.GameDifficultyGradeInfo;
import ECS.Entity.CharacterEntity;
import ECS.Game.GameDataManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 오후 4:58 2020-04-07
 * 업뎃날짜 : 오후 4:58 2020-04-07
 * 목적 : 게임의 난이도 등급을 나타내기 위한 클래스
 * 작업이력 :
 *
 *      -- 오전 3:33 2020-04-08
 *          [작성]
 *
 *
 *
 */
public class GameDifficultyGrade {

    /** 등급 목록 */

    public static final int NONE = 0;
    public static final int GRADE_F = 1;
    public static final int GRADE_E = 2;
    public static final int GRADE_D = 3;
    public static final int GRADE_C = 4;
    public static final int GRADE_B = 5;
    public static final int GRADE_A = 6;
    public static final int GRADE_S = 7;
    public static final int GRADE_SS = 8;
    public static final int GRADE_SSS = 9;
    public static final int GRADE_U = 10;
    public static final int GRADE_R = 11;




    /** 등급 관련 매서드 목록 */

    /**
     * 기능 :
     *      -- 넘겨받은 전투력을 가지고, 게임의 난이도 등급을 결정한다
     *
     */
    public static int decideGameGrade(float strength) {

        int grade = GameDifficultyGrade.NONE;

        /** GDM의 게임등급 리스트의 참조를 얻음 */

        HashMap<Integer, GameDifficultyGradeInfo> gameGradeInfo = GameDataManager.gameDifficultyGradeInfoList;

        float maxStrength;

        /** 게임등급 정보를 하나씩 읽으면서, 넘겨받은 전투력이 속한 구간을 검색한다 */

        GameDifficultyGradeInfo gradeInfo;
        for(int i=1; i<=gameGradeInfo.size(); i++){

            gradeInfo = gameGradeInfo.get(i);

            maxStrength = gradeInfo.maxStrengthPower;

            if(strength <= maxStrength){
                grade = gradeInfo.grade;
                break;
            }

        }


        return grade;

    }


    /**
     * 기능 :
     *      -- 공식에 따라 특정 캐릭터의 전투력 계산하여 리턴
     *
     * 공식 :
     *
     *      (생명력 * 1) + (방어력 * 9.33) + { 공격력 * 속도 * (1 + 치댐확률(%) * 치댐(100 + %) / 30.11 }
     *
     *          -- 생명력 계수 : 1
     *          -- 방어력 계수 : 9.33
     *          -- 공격력 계수 : 30.11
     *
     *      * 에픽세븐 공식 가져옴. 효과저항 이런거는 제외.
     *      * 위 계수들은, 우선 하드코딩해서 쓰다가 나중에 파일서 읽어오도록 바꿀 것임. 이번 주 내로.
     *
     */
    public static float calculateGuardianStrengthPower(CharacterEntity characterEntity){

        float guardianStrength = 0f;

        /** 전투력 계산에 필요한 값들 */
        float life = characterEntity.hpComponent.originalMaxHp;
        float defense = characterEntity.defenseComponent.defense;
        float attackDamage = characterEntity.attackComponent.attackDamage;
        float attackSpeed = characterEntity.attackComponent.attackSpeed;
        float criticalChance = characterEntity.attackComponent.criticalChance * 0.01f;
        float criticalDamage = characterEntity.attackComponent.criticalDamage;

        float LIFE_VALUE = 1f;
        float DEFENSE_VALUE = 3.33f;    // 9.33
        float ATTACK_VALUE = 10.11f;    // 30.11
        //ATTACK_VALUE = 10.11f;


        /** 계산 */
        // (생명력 * 1) + (방어력 * 9.33) + { 공격력 * 속도 * (1 + 치댐확률(%) * 치댐(100 + %) / 30.11 }
        guardianStrength
                = (life * LIFE_VALUE) + (defense * DEFENSE_VALUE)
                        + ( attackDamage * attackSpeed * ( 1 + criticalChance * criticalDamage / ATTACK_VALUE) );

        System.out.println("캐릭터 전투력 : " + guardianStrength);
        return guardianStrength;

    }

    /**
     * 팀의 전투력을 계산하는 매서드.
     * 일단은, 팀원들 전체의 전투력을 계산해 합산한 후, 평균을 구하는 걸로??
     * @param guardians
     */
    public static float calculateTeamStrengthPower(HashMap<Integer, CharacterEntity> guardians){

        float teamStrengthPower = 0f;

        for(CharacterEntity guardian : guardians.values()){

            teamStrengthPower += calculateGuardianStrengthPower(guardian);

        }

        teamStrengthPower /= guardians.size();

        return teamStrengthPower;

    }

    /**
     *기능 :
     *      *  -- 몬스터의 기대 레벨을 계산한다
     *
     *      INPUT
     *          -- int grade ; 게임의 난이도 등급
     *          -- strengthPower ; 팀의 전투력
     *      OUTPUT
     *          -- int level ; 현 게임 월드에서 많이 등장할 것이라고 기대되는 몬스터 레벨 값.
     *
     *      PROCESS
     *          --
     *          --
     *
     * @param grade
     * @param strengthPower
     * @return
     */
    public static int decideMonsterExpLevel(int grade, float strengthPower){

        int level = 0;


        /** GDM 으로부터 난이도 등급 정보를 참조 */
        GameDifficultyGradeInfo gradeInfo = GameDataManager.gameDifficultyGradeInfoList.get(grade);

        float strengthSize = gradeInfo.maxStrengthPower - gradeInfo.minStrengthPower;
        int minLevel = gradeInfo.minMonsterLevel;
        int maxLevel = gradeInfo.maxMonsterLevel;

        /** 현 등급 내 전투력을 구간별로 나눈다 ( 정확히는, 구간을 구분하는 크기값) */
        float strengthRangeSize = ( strengthSize ) / (maxLevel - minLevel + 1);


        /** 레벨 계산 */
        float currentRangeMaxStrength = gradeInfo.minStrengthPower;
        for(int i=minLevel; i<=maxLevel; i++){

            /* 현재 구간의 최대 전투력을 구함 */
            currentRangeMaxStrength += strengthRangeSize;

            /* 전투력이 현재 구간에 속한다면, 현재 몬스터 레벨값으로 결정한다 */
            if(strengthPower <= currentRangeMaxStrength){

                level = i;
                break;
            }

        }


        System.out.println("난이도 등급 : " + grade);
        System.out.println("전투력 : " + strengthPower);
        System.out.println("몬스터 기대 레벨 :" + level);

        level = minLevel;

        return level;

    }


}
