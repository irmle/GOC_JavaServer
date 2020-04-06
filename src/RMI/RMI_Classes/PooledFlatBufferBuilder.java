package RMI.RMI_Classes;

import com.google.flatbuffers.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PooledFlatBufferBuilder {

    //FlatBuffer 직렬화 라이브러리용 FlatBufferBuilder 객체 재사용 큐.
    public static final ConcurrentLinkedQueue<FlatBufferBuilder> DEFAULT = new ConcurrentLinkedQueue<>();

    public static void initPooledFlatBufferBuilder(int concurrentThreadCount)
    {
        //오브젝트풀 Queue에 동시 접근하는 스레드 개수의 4배만큼 미리 할당한다.
        int threadCount = concurrentThreadCount * 4;

        //FlatBufferBuilder 객체 재사용 큐 초기화.
        for (int i = 0; i < threadCount ; i++) {
            DEFAULT.offer(new FlatBufferBuilder());
        }
    }
}
