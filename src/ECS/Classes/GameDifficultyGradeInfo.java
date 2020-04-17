package ECS.Classes;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 오후 11:34 2020-04-07
 * 업뎃날짜 : 오후 11:34 2020-04-07
 * 목    적 :
 *      -- 게임 난이도 등급별 정보를 담기 위한 클래스이다.
 *      -- 게임 서버 실행 시
 *          GDM 에서 GameDifficultyGradeInfoList.csv 파일을 읽어 파싱하여,
 *          등급별로 해당 클래스 객체를 생성하여 리스트로 관리한다
 *
 * 작업이력 :
 *
 *      -- 오전 3:23 2020-04-08
 *          [ 멤버변수 추가 ]
 *          [ 생성자 추가 ]
 *
 *
 * 메    모 :
 *
 *
 *
 */
public class GameDifficultyGradeInfo implements Cloneable{

    /** 멤버 변수 */

    /* 게임 난이도 등급 */
    public int grade;

    /* 등급 전투력 구간 */
    public float minStrengthPower;
    public float maxStrengthPower;

    /* 등장 몬스터 레벨 구간 */
    public int minMonsterLevel;
    public int maxMonsterLevel;

    /* 몹 등급에 따른, 초기 몬스터 스탯 적용 비율 */
    public float monsterStatRate;



    /** 생성자 */

    public GameDifficultyGradeInfo(int grade, float minStrengthPower, float maxStrengthPower, int minMonsterLevel, int maxMonsterLevel, float monsterStatRate) {
        this.grade = grade;
        this.minStrengthPower = minStrengthPower;
        this.maxStrengthPower = maxStrengthPower;
        this.minMonsterLevel = minMonsterLevel;
        this.maxMonsterLevel = maxMonsterLevel;
        this.monsterStatRate = monsterStatRate;
    }

    @Override
    public GameDifficultyGradeInfo clone() {

        GameDifficultyGradeInfo gameDifficultyGradeInfo;

        try{
            gameDifficultyGradeInfo = (GameDifficultyGradeInfo) super.clone();
        } catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }

        return gameDifficultyGradeInfo;

    }

}
