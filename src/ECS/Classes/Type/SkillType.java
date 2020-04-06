package ECS.Classes.Type;

/**
 * 업뎃날짜 : 2020 03 20 금 새벽 00:45:00 권령희
 * 업뎃내용 : 귀환 공통스킬 타입(100) 추가
 */
public class SkillType {

    public static final int NONE = 0;

    /*********************** 전사 ***************************************/

        /* 평타 */
        public static final int KNIGHT_NORMAL_ATTACK = 1;
        /* 1단계 */
        public static final int KNIGHT_CUT = 2;
        public static final int KNIGHT_GARREN_Q = 3;
        public static final int KNIGHT_BERSERKER = 4;
        /* 2단계 */
        public static final int KNIGHT_PIERCE = 5;
        public static final int KNIGHT_GARREN_E = 6;
        public static final int KNIGHT_INCR_HP = 7;
        /* 3단계 */
        public static final int KNIGHT_TORNADO = 8;
        public static final int KNIGHT_GARREN_R = 9;
        public static final int KNIGHT_INVINCIBLE = 10;


    /*********************** 마법사 *************************************/

        /* 평타 */
        public static final int MAGICIAN_NORMAL_ATTACK = 11;
        /* 1단계 */
        public static final int MAGICIAN_FIREBALL = 12;
        public static final int MAGICIAN_LIGHTNING_ROAD = 13;
        public static final int MAGICIAN_ICEBALL = 14;
        /* 2단계 */
        public static final int MAGICIAN_HEAL = 15;
        public static final int MAGICIAN_SHIELD = 16;
        public static final int MAGICIAN_ICE_FIELD = 17;
        /* 3단계 */
        public static final int MAGICIAN_METEOR = 18;
        public static final int MAGICIAN_THUNDER = 19;
        public static final int MAGICIAN_FROZEN_BEAM = 20;


    /*********************** 궁수 ***************************************/

        /* 평타 */
        public static final int ARCHER_NORMAL_ATTACK = 21;
        /* 1단계 */
        public static final int ARCHER_POWER_SHOT = 22;
        public static final int ARCHER_INC_ATTACK_SPEED = 23;
        public static final int ARCHER_HEAD_SHOT = 24;
        /* 2단계 */
        public static final int ARCHER_MULTI_SHOT = 25;
        public static final int ARCHER_CRITICAL_HIT = 26;
        public static final int ARCHER_STORM = 27;
        /* 3단계 */
        public static final int ARCHER_ARROW_RAIN = 28;
        public static final int ARCHER_FIRE = 29;
        public static final int ARCHER_SNIPE = 30;


    /************************* 공통 영역 *************************************/

    /* 귀환 */
    public static final int RECALL = 100;

    /* 우물 */
    public static final int WELL_RECOVERY = 101;








}
