import ECS.Classes.PlayerGameScore;
import ECS.Classes.Vector3;
import ECS.Entity.CharacterEntity;
import RMI.RMI_NettyServer;
import com.google.gson.*;

import java.text.SimpleDateFormat;
import java.util.Date;


//서버 시작 부분.
public class Start_GameServer { 

    public static void main(String[] args) {

        boolean testMode = false;
        if(!testMode){

            //서버 객체 생성.
            RMI_NettyServer gameserver = new RMI_NettyServer(); //생성자로 기본 bind port값 지정 가능. 

            //Java 옵션 인자값 지정.
            //서버 시작.
            gameserver.startServer(args);

        }
        else {

            /* 백터 테스트 */
            if (false) {

                Vector3 dir = new Vector3(10, 0, -10);
                Vector3 user = new Vector3(3, 0, -5);
                Vector3 mob = new Vector3(5, 0, -4);

                float distance = Vector3.getDistanceBetweenVectorAndDot(dir, user, mob);

                System.out.println("값 : " + distance);

            }


            if (false) {
                String resultJsonStr = null;
                Gson gson = new Gson();

                /* 플레이어 */
                JsonArray players = new JsonArray();
                JsonObject player;
                JsonObject unit;
                String playersStringTemp = "";
                for (int i = 0; i < 3; i++) {


                    String tokenID = "kjkjk39fdfjdfe00";
                    int entityID = 32;

                    PlayerGameScore gameScore = new PlayerGameScore(new CharacterEntity());

                    int level = 3;
                    System.out.println("레벨 : " + level);

                    unit = new JsonObject();
                    unit.addProperty("fieldNum", i);
                    player = new JsonObject();
                    player.addProperty("googleToken", tokenID);
                    player.addProperty("earnedGold", gameScore.earnedGold);
                    player.addProperty("level", level);
                    player.addProperty("givenDamageAmount", gameScore.givenDamageAmount);
                    player.addProperty("getDamagedAmount", gameScore.getDamagedAmount);
                    player.addProperty("monsterKillCount", gameScore.monsterKillCount);
                    player.addProperty("deathCount", gameScore.characterDeathCount);
                    player.addProperty("grade", gameScore.resultGrade);
                    player.addProperty("finalScore", gameScore.finalScore);
                    unit.add("player_info", player);

                    //players.add(player);
                    players.add(unit);
                    playersStringTemp += player;

                    System.out.println("플레이어" + i + "제쓴 : " + unit);

                }

                System.out.println("플레이어들" + players.toString());
                //System.out.println("플레이어들 스트링" + playersStringTemp);


                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat elapsedformat = new SimpleDateFormat("HHmmss");


                Date startTime = new Date();
                startTime.setTime(000);
                System.out.println(format.format(startTime));

                Date finishTime = new Date();
                startTime.setTime(111);
                System.out.println(format.format(finishTime));


                Date elapsedTime = new Date();
                elapsedTime.setTime(222);
                System.out.println(format.format(elapsedTime));

                int elapsedTime_H = (int) ((333 * 0.001) / 60) / 60;
                int elapsedTime_M = (int) ((222 * 0.001) / 60) % 60;
                int elapsedTime_S = (int) (111 * 0.001) % 60;

                System.out.println(elapsedTime_H + " " + elapsedTime_M + " " + elapsedTime_S);

                String elapsedTimeStr = "";
                if (elapsedTime_H < 10) {
                    elapsedTimeStr += 0;
                }
                elapsedTimeStr += elapsedTime_H;

                if (elapsedTime_M < 10) {
                    elapsedTimeStr += 0;
                }
                elapsedTimeStr += elapsedTime_M;

                if (elapsedTime_S < 10) {
                    elapsedTimeStr += 0;
                }
                elapsedTimeStr += elapsedTime_S;


                JsonObject gameResult = new JsonObject();
                gameResult.addProperty("playTime", elapsedTimeStr);
                gameResult.addProperty("startTime", format.format(startTime));
                gameResult.addProperty("finishTime", format.format(finishTime));
                gameResult.addProperty("stage", 45);
                gameResult.add("players", players);

                resultJsonStr = gson.toJson(gameResult);

                System.out.println(resultJsonStr);

                // return resultJsonStr;


                System.out.println("최종 JSON 값" + resultJsonStr);

            }

            if (false) {
                String resultJsonStr = null;
                Gson gson = new Gson();

                /* 플레이어 */
                //JsonArray players = new JsonArray();
                JsonObject players = new JsonObject();
                JsonObject player;
                JsonObject unit;
                String playersStringTemp = "";
                for (int i = 0; i < 3; i++) {


                    String tokenID = "kjkjk39fdfjdfe00";
                    int entityID = 32;

                    PlayerGameScore gameScore = new PlayerGameScore(new CharacterEntity());

                    int level = 3;
                    System.out.println("레벨 : " + level);


                    player = new JsonObject();
                    player.addProperty("googleToken", tokenID);
                    player.addProperty("earnedGold", gameScore.earnedGold);
                    player.addProperty("level", level);
                    player.addProperty("givenDamageAmount", gameScore.givenDamageAmount);
                    player.addProperty("getDamagedAmount", gameScore.getDamagedAmount);
                    player.addProperty("monsterKillCount", gameScore.monsterKillCount);
                    player.addProperty("deathCount", gameScore.characterDeathCount);
                    player.addProperty("grade", gameScore.resultGrade);
                    player.addProperty("finalScore", gameScore.finalScore);


                    players.add((i+1) + "", player);
                    //players.add(unit);
                    playersStringTemp += player;

                    System.out.println("플레이어" + i + "제쓴 : " + player);

                }

                System.out.println("플레이어들" + players.toString());
                //System.out.println("플레이어들 스트링" + playersStringTemp);


                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat elapsedformat = new SimpleDateFormat("HHmmss");


                Date startTime = new Date();
                startTime.setTime(000);
                System.out.println(format.format(startTime));

                Date finishTime = new Date();
                startTime.setTime(111);
                System.out.println(format.format(finishTime));


                Date elapsedTime = new Date();
                elapsedTime.setTime(222);
                System.out.println(format.format(elapsedTime));

                int elapsedTime_H = (int) ((333 * 0.001) / 60) / 60;
                int elapsedTime_M = (int) ((222 * 0.001) / 60) % 60;
                int elapsedTime_S = (int) (111 * 0.001) % 60;

                System.out.println(elapsedTime_H + " " + elapsedTime_M + " " + elapsedTime_S);

                String elapsedTimeStr = "";
                if (elapsedTime_H < 10) {
                    elapsedTimeStr += 0;
                }
                elapsedTimeStr += elapsedTime_H;

                if (elapsedTime_M < 10) {
                    elapsedTimeStr += 0;
                }
                elapsedTimeStr += elapsedTime_M;

                if (elapsedTime_S < 10) {
                    elapsedTimeStr += 0;
                }
                elapsedTimeStr += elapsedTime_S;


                JsonObject gameResult = new JsonObject();
                gameResult.addProperty("playTime", elapsedTimeStr);
                gameResult.addProperty("startTime", format.format(startTime));
                gameResult.addProperty("finishTime", format.format(finishTime));
                gameResult.addProperty("stage", 45);
                gameResult.add("players", players);

                resultJsonStr = gson.toJson(gameResult);

                System.out.println(resultJsonStr);

                // return resultJsonStr;


                System.out.println("최종 JSON 값" + resultJsonStr);

            }


            /* OK. 최소값에서 최대값 까지 잘 나옴  */
            if(false){

                int max = 99;
                int min = 0;

                int randomValue;
                for (int i=0; i<1000; i++){
                    randomValue = (int)(Math.random()*((max-min)+1))+min;
                    System.out.println(randomValue);
                }

            }

            if(false){

                float damage = 100;
                float BALNENCE_VALUE = 30;

                float complementValue = ( damage * BALNENCE_VALUE * 0.01f );
                float minDam = damage - complementValue;
                float maxDam = damage + complementValue;

                float TARGET_DEFENSE = 22;

                float flatDamage;
                float finalFlatDamage;
                for (int i=0; i<100; i++){
                    flatDamage = (int) ((Math.random()*(maxDam - minDam) + 1) + minDam );
                    finalFlatDamage = (int) (flatDamage * 100 / (100 + TARGET_DEFENSE) );
                    System.out.print(flatDamage + ", ");
                    System.out.println(finalFlatDamage);
                }
            }

            /** 2020 02 10 */
            if(true){

                class test{

                    float earnGold;
                    float damage;

                    public test(float earnGold, float damage) {
                        this.earnGold = earnGold;
                        this.damage = damage;
                    }
                }



                String resultJsonStr = null;
                Gson gson = new Gson();

                /* 플레이어 */
                JsonArray players = new JsonArray();
                JsonObject player = null;
                String playersStringTemp = "";
                for (int i=0; i<1; i++) {

                    String tokenID = "toekn" + i;
                    int entityID = i;

                    int level = 1;
                    System.out.println("레벨 : " + level);

                    player = new JsonObject();
                    //player.addProperty("googleToken", "tokennen");
                    player.addProperty("earnGold", 333);
                    //player.addProperty("level", level);
                    player.addProperty("damage", 444);
                    //player.addProperty("getDamagedAmount", 222);
                    //player.addProperty("monsterKillCount", 111);
                    //player.addProperty("deathCount", 5);
                    //player.addProperty("grade", 1);
                    //player.addProperty("finalScore", 4823423);

                    //players.add(player);

                    playersStringTemp += player;

                }

                //System.out.println(players);
                System.out.println("플레이어들스트링" + playersStringTemp);


                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat elapsedformat = new SimpleDateFormat("HHmmss");


                Date startTime = new Date();
                //startTime.setTime(gameStartTime);
                System.out.println(format.format(startTime));

                Date finishTime = new Date();
                //startTime.setTime(gameFinishTime);
                System.out.println(format.format(finishTime));


                long gameElapsedTime = System.currentTimeMillis();

                Date elapsedTime = new Date();
                //elapsedTime.setTime(gameElapsedTime);
                System.out.println(format.format(elapsedTime));

                int elapsedTime_H = (int) ((gameElapsedTime * 0.001) / 60) / 60;
                int elapsedTime_M = (int) ((gameElapsedTime * 0.001) /60) % 60 ;
                int elapsedTime_S = (int) (gameElapsedTime * 0.001) % 60;

                System.out.println(elapsedTime_H + " " + elapsedTime_M + " " + elapsedTime_S);

                String elapsedTimeStr = "";
                if(elapsedTime_H < 10){
                    elapsedTimeStr += 0;
                }
                elapsedTimeStr += elapsedTime_H;

                if(elapsedTime_M < 10){
                    elapsedTimeStr += 0;
                }
                elapsedTimeStr += elapsedTime_M;

                if(elapsedTime_S < 10){
                    elapsedTimeStr += 0;
                }
                elapsedTimeStr += elapsedTime_S;


                JsonObject gameResult = new JsonObject();
                gameResult.addProperty("playTime", elapsedTimeStr);
                gameResult.addProperty("startTime", format.format(startTime));
                gameResult.addProperty("finishTime", format.format(finishTime));
                gameResult.addProperty("stage", 3);
                gameResult.add("player", player);

                resultJsonStr = gson.toJson(player);

                System.out.println(resultJsonStr);


                /*==================================================================================================== */

                System.out.println("\n\n");

                test score = gson.fromJson(resultJsonStr, test.class);


                JsonParser parser = new JsonParser();

                JsonElement element = parser.parse(resultJsonStr);
                JsonObject object = element.getAsJsonObject();

                float damagee = object.get("damage").getAsFloat();

                System.out.println("파싱결과 : " + damagee);


















            }


        }


    }
}
