package ECS.Classes;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 04 화요일
 * 업뎃날짜 :
 * 목    적 :
 *      클라이언트에 게임 진행 상황 중계, 게임 종료 후 최종 결과 내는 데 필요한 값들 관리하기 위한 클래스.
 *
 *      scoreBoard는 게임이 참여중인 각 플레이어들의 스코어를 가지고 있다.
 *
 *      작성할 매서드 목록 :
 *      long 시간값을 특정 시간 형태로 구하기 // 최종 결과를 저장할 때 쓰일 듯
 *      playTime 갱신 // 매 단위로직마다 해줘야 할 듯
 *      stage 증가    // 웨이브 수 올라갈 때 마다?? 스테이지를 어케 정의하느냐에 따라 다르겟지만
 *
 *      각각의 getter
 *
 *
 *
 *
 */
public class GameStatus implements Cloneable{

    public int stage;

    public long playTime;
    public long startTime;
    public long finishTime;

    public ScoreBoard scoreBoard;

    public GameStatus() {

        stage = 0;

        playTime = 0;
        startTime = 0;
        finishTime = 0;

        scoreBoard = new ScoreBoard();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        GameStatus gameStatus;

        try{

            gameStatus = (GameStatus) super.clone();
            gameStatus.scoreBoard = (ScoreBoard) scoreBoard.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return gameStatus;
    }
}
