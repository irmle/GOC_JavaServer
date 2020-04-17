package RMI.RMI_Classes;

public class RMI_Context {

    //TCP
    public static final short Reliable = 1; //신뢰성 있는 평문 전송

    //각 클라이언트마다 가지고 있는 암호화 키 사용.
    public static final short Reliable_AES128 = 2; //신뢰성 있는 AES-128-CBC 전송
    public static final short Reliable_AES256 = 3; //신뢰성 있는 AES-256-CBC 전송

    //서버가 생성하고, 모든 클라이언트가 공유하고있는 암호화키 사용.
    public static final short Reliable_Public_AES128 = 4; //신뢰성 있는 공용키 AES-128-CBC 전송
    public static final short Reliable_Public_AES256 = 5; //신뢰성 있는 공용키 AES-256-CBC 전송

    //UDP
    public static final short UnReliable = 6; //비 신뢰성 평문 전송

    //각 클라이언트마다 가지고 있는 암호화 키 사용.
    public static final short UnReliable_AES128 = 7; //비 신뢰성 AES-128-CBC 전송
    public static final short UnReliable_AES256 = 8; //비 신뢰성 AES-256-CBC 전송

    //서버가 생성하고, 모든 클라이언트가 공유하고있는 암호화키 사용.
    public static final short UnReliable_Public_AES128 = 9; //비 신뢰성 공용키 AES-128-CBC 전송
    public static final short UnReliable_Public_AES256 = 10; //비 신뢰성 공용키 AES-256-CBC 전송

    private static final String[] names =
    {
        "Reliable", "Reliable_AES128", "Reliable_AES256", "Reliable_Public_AES128", "Reliable_Public_AES256",
        "UnReliable", "UnReliable_AES128", "UnReliable_AES256", "UnReliable_Public_AES128", "UnReliable_Public_AES256"
    };

    public static String name(int e)
    {
        if(e<1)
            e=1;
        else if(e>names.length)
            e=names.length;
        return names[e - 1];
    }
}

