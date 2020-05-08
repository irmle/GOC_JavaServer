package ECS.Chatting.Type;

public class MessageType {

    public static final int NONE = 0;

    /** 접속 관련 */
    public static final int SERVER_CONNECT = 1;
    public static final int SERVER_DISCONNECT = 2;

    /** 로비 관련 */
    public static final int LOBBY_CHANGE_CHANNEL = 11;
    public static final int LOBBY_JOIN_CHANNEL = 12;
    public static final int LOBBY_LEAVE_CHANNEL = 13;
    public static final int LOBBY_CHAT_MSG = 14;

    /** 세션 관련 */
    public static final int SESSION_JOIN_CHANNEL = 21;
    public static final int SESSION_LEAVE_CHANNEL = 22;
    public static final int SESSION_CHAT_MSG = 23;

    /** 길드 관련*/
    public static final int GUILD_JOIN_MEMBER = 31;
    public static final int GUILD_LEAVE_MEMBER = 32;
    public static final int GUILD_CHAT_MSG = 33;

    /** 통지 관련 */
    public static final int NOTIFICATION_ITEM_UPGRADE = 41;
    public static final int NOTIFICATION_CHARACTER_UPGRADE = 42;



}
