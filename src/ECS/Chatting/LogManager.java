package ECS.Chatting;

import ECS.Chatting.Classes.LogMessage;

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
    public String getLogPath(long currentTime, int channelType){

        String path = "";








        return path;
    }



    /**
     * 로그 쓰기
     *
     * 경로 구하고.
     * 메시지 타입이.. 아니 채널 타입이 0이라면 그냥 경로에다 이름 붙여서 쓰고
     * 이외의 경우(0보다 큰, 즉 채널 로그)
     *  ㄴ 경로에다 이름 붙이고, 이름 뒤에 채널번호 붙여서 씀.
     *
     */
    public void writeLog(LogMessage logMessage){






    }


    /**
     * 메시지 쌓기
     */
    public void enqueueLogMessage(LogMessage logMessage){

        this.logLogMessageQueue.add(logMessage);
    }

    /**
     * 메시지 꺼내서 처리
     */
    static private void dequeueLogMessage(){

        int messageCount = logLogMessageQueue.size();

        for(int i=0; i<messageCount; i++){

            LogMessage logMessage = logLogMessageQueue.poll();






        }



    }


    /**
     * 뭐..
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
