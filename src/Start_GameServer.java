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
    }
}
