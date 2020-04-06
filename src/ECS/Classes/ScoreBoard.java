package ECS.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 04 화요일
 * 업뎃날짜 :
 * 목    적 :
 *      GameStatus 클래스에 포함되어, 게임 플레이어들의 각종 게임 스코어 항목들을 관리하기 위함
 *
 *      플레이어 개개인과 별개로 스코어 보드에 들어가야 할 게 있다면 여기에 추가.
 *
 *      추가할 매서드 목록 :
 *      특정 유저의 스코어 검색   // 계속 쓰이지 않을까 싶다 매 단위 로직마다.. 까지는 아니더라도, 갱신이 필요한 이벤트가 발생할 때 마다
 *                                // 우선 해당 유저를 찾아야겠지.
 *
 *
 */
public class ScoreBoard implements Cloneable{

    List<PlayerGameScore> playerGameScoreList;

    public ScoreBoard() {
        playerGameScoreList = new ArrayList<>();
    }

    public ScoreBoard(List<PlayerGameScore> playerGameScoreList) {
        this.playerGameScoreList = playerGameScoreList;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        ScoreBoard scoreBoard;
        try {

            scoreBoard = (ScoreBoard) super.clone();

            scoreBoard.playerGameScoreList = new ArrayList<>();
            for(int i=0; i<playerGameScoreList.size(); i++){

                scoreBoard.playerGameScoreList.add( (PlayerGameScore) playerGameScoreList.get(i).clone() );
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return scoreBoard;
    }
}
