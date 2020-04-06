package ECS.Classes;

import ECS.Entity.CharacterEntity;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 01 16 목
 * 업뎃날짜 : 2020 02 04 화
 * 업뎃내용 :
 *      멤버변수 bossMobKillCount 추가. 당분간 쓸일 없을지도 모르지만.
 * 목    적 :
 *      한 게임 내 플레이어들의 각 게임진행 현황/점수를 담아 관리하기 위한 클래스.
 *      게임이 끝난 후, 이 값들을 가지고 최종 점수를 계산하여, 플레이어들의 등급이 정해짐.
 *
 *      스코어 보드 클래스에 소속되어, 게임 시작 시 생성되어 게임 진행중에 수시로 업데이트된다.
 *      GameStatus -> ScoreBoard -> PlayerGameScore
 *
 * 업뎃할 매서드 :
 *      획득골드 증가 // 보상 시스템 처리 할 때
 *      몹킬 카운트 증가   // 사망 시스템 처리할 때
 *      보스몹킬 카운트 증가 // 사망 시스템 처리할 때?
 *      캐릭터 사망카운트 증가     // 사망 시스템 처리할 때?
 *
 *      받은데미지량 증가   // 데미지 처리(hp시스템 )할 때.. 먼가 찝찝 꼭 '데미지시스템'이라는 이름으로 무언가 있어야만 할 것 같은..
 *      입힌데미지량 증가   // 데미지 처리할 때
 *
 *      최종점수 계산     // 게임 종료 조건 만족 시, 혹은 겜 스레드 종료 시... ==>> 아니 게임이 안전 종료된 것도 아닌데 최종결과 내면 안되지
 *                          잠깐 튕긴 경우 제외, 모든 유저가 나갔다던가 하는 경우에만.
 *      게임결과 등급 세팅  // 위 처리와 같이.
 *
 *      각종 겟터들
 *
 *
 *
 */
public class PlayerGameScore implements Cloneable{

    /* 멤버 변수 */
    public CharacterEntity character;

    public int earnedGold;  // 이번 게임에서 총 벌어들인 골드
    public int monsterKillCount;
    public int bossMobKillCount;
    public int characterDeathCount;

    public double getDamagedAmount;     // 본인이 입은 총 데미지 량
    public double givenDamageAmount;    // 몬스터에게 입힌 총 데미지 량

    public double finalScore;   // 위 값들을 가지고 도출한 최종 점수.
    public int resultGrade;     // 게임 결과 등급


    /* 생성자 */
    public PlayerGameScore(CharacterEntity character) {

        this.character = character;

        earnedGold = 0;
        monsterKillCount = 0;
        bossMobKillCount = 0;
        characterDeathCount = 0;
        getDamagedAmount = 0d;
        givenDamageAmount = 0d;

        finalScore = 0d;
        resultGrade = 0;
    }

    public PlayerGameScore(int earnedGold, int monsterKillCount, int bossMobKillCount, int characterDeathCount, double getDamagedAmount, double givenDamageAmount) {
        this.earnedGold = earnedGold;
        this.monsterKillCount = monsterKillCount;
        this.bossMobKillCount =  bossMobKillCount;
        this.characterDeathCount = characterDeathCount;
        this.getDamagedAmount = getDamagedAmount;
        this.givenDamageAmount = givenDamageAmount;

        finalScore = 0d;
        resultGrade = 0;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        PlayerGameScore playerGameScore;

        try{

            playerGameScore = (PlayerGameScore) super.clone();
            playerGameScore.character = character;  // 복사가 필요한 게 아니라 참조가 필요한 것 뿐이라서..
                                                    // 이 줄을 아예 안써줘도 될수도 있지만 일단 적어둠
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return playerGameScore;
    }
}
