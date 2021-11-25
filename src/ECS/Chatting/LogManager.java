package ECS.Chatting;

import ECS.Chatting.Classes.LogMessage;
import ECS.Chatting.Type.ChannelType;

import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 */
public class LogManager {

    /** 멤버 변수 */

    //동기화 제어용 모니터객체
    static Object locking;

    //(로그)메시지 큐
    static Queue<LogMessage> logLogMessageQueue;

    // 로그쓰기 스레드
    static Thread logThread;



    /** 생성자 & 초기화 매서드 */
    public static void initLogManager(){

        System.out.println("LogManager 초기화중...");

        //
        locking = new Object();
        logLogMessageQueue = new LinkedList<>();   // ????


        /* 로그 파일 작성 스레드 할당 */
        logThread = new Thread(new logWriting());
        logThread.start();


        System.out.println("LogManager 초기화 완료");

    }




    /** 그 외 매서드 */

    /**
     * 경로 구하는 매서드
     */
    public static String getLogPath(long currentTime, int channelType){

        String finalPath = "";

        String commonPath = Paths.get("").toAbsolutePath().toString();
        commonPath += "/GameServerLog/Chatting";

        Date currentDate = new Date(currentTime);
        commonPath += "/" + currentDate.getYear();
        commonPath += "/" + currentDate.getMonth();
        commonPath += "/" + currentDate.getDate();

        switch (channelType){

            case ChannelType.LOBBY :
                finalPath = commonPath + "/Lobby";
                break;

            case ChannelType.SESSION :
                finalPath = commonPath + "/Session";
                break;

            case ChannelType.GUILD :
                finalPath = commonPath + "/Guild";
                break;

            default:
                finalPath = commonPath + "/";
                break;

        }


        return finalPath;
    }



    /**
     * 로그 쓰기
     *
     * 경로 구하고.
     * 메시지 타입이.. 아니 채널 타입이 0이라면 그냥 경로에다 이름 붙여서 쓰고
     * 이외의 경우(0보다 큰, 즉 채널 로그)
     *  ㄴ 경로에다 이름 붙이고, 이름 뒤에 채널번호 붙여서 씀.
     *
     *
     * 이거일단 인쓰는걸로. 아래걸로
     */
    public void writeLog(LogMessage logMessage){




    }

    /**
     * 흠..
     * 여기서 경로의 파일 열고 쓰는 처리 수행.
     * @param logMessage
     */
    public static void writeLog(String path, String logMessage){






    }


    /**
     * 메시지 쌓기
     */
    public static void enqueueLogMessage(LogMessage logMessage){

        logLogMessageQueue.add(logMessage);
    }

    /**
     * 메시지 꺼내서 처리
     */
    private static void dequeueLogMessage(){

        int messageCount = logLogMessageQueue.size();

        for(int i=0; i<messageCount; i++){

            LogMessage logMessage = logLogMessageQueue.poll();


            // 로그 메시지 객체를 가지고, 실제로 쓸 포맷을 만든다.
            String logStr = logMessage.toString();



            // 경로 구하는 처리
            String pathStr = getLogPath(logMessage.getCurrentTime(), logMessage.getChannelType());


            // 파일명 붙임처리
            String filePath = "";
            switch (logMessage.getChannelType()){

                case ChannelType.LOBBY :

                    filePath = pathStr + "/chatLog_channel_" + logMessage.getChannelNum();
                    break;

                case ChannelType.SESSION :

                    filePath = pathStr + "/chatLog_world_" + logMessage.getChannelNum();
                    break;

                case ChannelType.GUILD :

                    filePath = pathStr + "/chatLog_guild_" + logMessage.getChannelNum();
                    break;

                default:

                    filePath = pathStr + "/serverLog";
                    break;

            }


            // 경로의 파일을 열거나 생성 후, 쓰기 처리
            writeLog(filePath, logStr);

        }

    }


    /**
     *
     */
    static private class logWriting implements Runnable{

        @Override
        public void run() {

            try{

                if (checkMessageCount() > 0){

                    dequeueLogMessage();
                }
                else{

                    Thread.currentThread().sleep(1000);
                }

            }

            catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }



    public static int checkMessageCount(){

        return logLogMessageQueue.size();

    }











}
