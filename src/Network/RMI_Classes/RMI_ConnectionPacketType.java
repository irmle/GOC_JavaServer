package Network.RMI_Classes;

public class RMI_ConnectionPacketType {

    //고정값.
    public static final short RMI_ProtocolVersionCheck = -32768; //버전 확인용
    public static final short RMI_RSA_PublicKey = -32767; //RSA 공개키 송신
    public static final short RMI_Send_EncryptedAES_Key = -32766; //클라가 생성한 AES 128,256키 송신
    public static final short RMI_Send_EncryptedAccept_Data = -32765; //서버가 서버의 공용 AES 128, 256키 및 RMI HOSTID, UDP initPort 송신
    public static final short RMI_UDP_ConnectionConfirm = -32764; //UDP 통신 체크
    public static final short RMI_OverConnectionAnnounce = -32763; //접속자수 제한에 걸렸을 경우.

}
