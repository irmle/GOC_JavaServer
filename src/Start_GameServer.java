import ECS.Classes.PlayerGameScore;
import ECS.Classes.Vector3;
import ECS.Entity.CharacterEntity;
import Network.NetworkManager;
import com.google.gson.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/*import ECS.Game.MapDataManager;*/

//서버 시작 부분.
public class Start_GameServer { 

    public static void main(String[] args) {

        boolean testMode = false;
        if(!testMode){

            //서버 객체 생성.
            NetworkManager gameserver = new NetworkManager(); //생성자로 기본 bind port값 지정 가능.

            //Java 옵션 인자값 지정.
            //서버 시작.
            gameserver.startServer(args);

        }
        else {

            float durationTime = 1f;
            float remainTime = durationTime;

            float 중력_가중치 = 25f;
            float 중력_역가중치 = 10f;

            float 현재_높이 = 0f;

            while (true){

                remainTime -= 0.1f;
                if(remainTime <= 0){
                    break;
                }

                if(((remainTime ) > (durationTime/2-0.1f)) && ((remainTime) < (durationTime/2))){
                    System.out.println("정상");
                    System.out.println("남은 시간 :" + String.format("%.3f", remainTime) );

                }
                else if(remainTime > (durationTime/2) ){

                    System.out.println("올라감");
                    System.out.println("남은 시간 :" + String.format("%.3f", remainTime) );

                    /** 올라가는 처리 수행 */

                    /* 1. 이동량을 구한다 */
                    float 이동량;

                    float 지속시간의_절반 = (durationTime/2);
                    System.out.println("지속시간의 절반 :" + 지속시간의_절반 );

                    이동량 = (float)
                            ( Math.pow( Math.abs( remainTime - 지속시간의_절반 ), 2 )
                                    / Math.pow( (지속시간의_절반 + 1 ), 2));

                    float 분자절댓값 = Math.abs( remainTime - 지속시간의_절반);
                    System.out.println("분자절댓값 :" + String.format("%.3f", 분자절댓값) );

                    float 분자 = (float)Math.pow(분자절댓값, 2);
                    System.out.println("분자 :" + String.format("%.3f", 분자) );

                    float 분모 = (float)Math.pow( (지속시간의_절반 + 1 ), 2);
                    System.out.println("분모 :" + String.format("%.3f", 분모) );

                    float 분자_분모 = 분자 / 분모;
                    System.out.println("분자_분모 :" + String.format("%.3f", 분자_분모) );

                    float 중력가중치_적용 = 분자_분모 * 중력_가중치;
                    System.out.println("중력가중치_적용 :" + String.format("%.3f", 중력가중치_적용) );


                    이동량 = (1 / 이동량);

                    float 역수값 = ( 1 / 중력가중치_적용);
                    System.out.println("역수값 :" + String.format("%.3f", 역수값) );

                    현재_높이 += 역수값;
                    System.out.println("현재 높이 :" + String.format("%.3f", 현재_높이) );

                }
                else{

                    /** 내려가는 처리 수행 */

                    System.out.println("내려감");
                    System.out.println("남은 시간 :"  + String.format("%.3f", remainTime) );


                    /** 내려가는 처리 수행 */

                    /* 1. 이동량을 구한다 */
                    float 이동량;

                    float 지속시간의_절반 = (durationTime/2);
                    System.out.println("지속시간의 절반 :" + 지속시간의_절반 );

                    이동량 = (float)
                            ( Math.pow( Math.abs( remainTime - 지속시간의_절반 ), 2 )
                                    / Math.pow( (지속시간의_절반 + 1 ), 2));

                    float 분자절댓값 = Math.abs( remainTime - 지속시간의_절반);
                    System.out.println("분자절댓값 :" + String.format("%.3f", 분자절댓값) );

                    float 분자 = (float)Math.pow(분자절댓값, 2);
                    System.out.println("분자 :" + String.format("%.3f", 분자) );

                    float 분모 = (float)Math.pow( (지속시간의_절반 + 1 ), 2);
                    System.out.println("분모 :" + String.format("%.3f", 분모) );

                    float 분자_분모 = 분자 / 분모;
                    System.out.println("분자_분모 :" + String.format("%.3f", 분자_분모) );

                    float 중력가중치_적용 = 분자_분모 * 중력_가중치;
                    System.out.println("중력가중치_적용 :" + String.format("%.3f", 중력가중치_적용) );


                    이동량 = (1 / 이동량);

                    float 역수값 = ( 1 / 중력가중치_적용);
                    System.out.println("역수값 :" + String.format("%.3f", 역수값) );

                    현재_높이 -= 역수값;
                    System.out.println("현재 높이 :" + String.format("%.3f", 현재_높이) );


                }




                System.out.println("================");





            }















        }


    }
}
