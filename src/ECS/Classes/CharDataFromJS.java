package ECS.Classes;

import ECS.Classes.Type.CharacterType;
import com.google.gson.JsonObject;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 10
 * 업뎃날짜 : 오전 4:57 2020-04-07
 * 목    적 :
 *      웹서버로부터 얻은 JSON 형태의 플레이어 캐릭터 정보를 옮겨담는 용도
 *
 * 작업이력 :
 * 메    모 :
 *
 *
 *
 *
 */
public class CharDataFromJS {

    /* 멤버 변수 */
    public int userToken;

    public int guardianType;
    public int guardianLV;
    public float exp;
    public int arousal;

    public float attackDamage;
    public float defense;

    public float hp;
    public float hpRecoveryRate;
    public float mp;
    public float mpRecoveryRate;

    public float attackRange;
    public float attackSpeed;
    public float moveSpeed;

    public float criticalRate;
    public float criticalBonus;

    public int elemental;

    public float balance;


    /* 생성자 */
    public CharDataFromJS(JsonObject playerInfo) {

        this.userToken = playerInfo.get("userToken").getAsInt();

        int dbCharType = playerInfo.get("guardianType").getAsInt();
        switch (dbCharType){
            case CharacterType.ARCHER :
                this.guardianType = CharacterType.ARCHER;
                break;
            case CharacterType.KNIGHT :
                this.guardianType = CharacterType.KNIGHT;
                break;
            case CharacterType.MAGICIAN :
                this.guardianType = CharacterType.MAGICIAN;
                break;
        }

        this.guardianLV = playerInfo.get("guardianLV").getAsInt();
        this.exp = playerInfo.get("exp").getAsFloat();
        this.arousal = playerInfo.get("arousal").getAsInt();
        this.attackDamage = playerInfo.get("attackDamage").getAsFloat();
        this.defense = playerInfo.get("defence").getAsFloat();
        this.hp = playerInfo.get("hp").getAsFloat();
        this.hpRecoveryRate = playerInfo.get("hpRecoveryRate").getAsFloat();
        this.mp = playerInfo.get("mp").getAsFloat();
        this.mpRecoveryRate = playerInfo.get("mpRecoveryRate").getAsFloat();
        this.attackSpeed = playerInfo.get("attackSpeed").getAsFloat();
        this.moveSpeed = playerInfo.get("moveSpeed").getAsFloat();
        this.criticalRate = playerInfo.get("criticalRate").getAsFloat();
        this.criticalBonus = playerInfo.get("criticalBonus").getAsFloat();

        this.attackRange = playerInfo.get("attackRange").getAsFloat();

        this.elemental = playerInfo.get("guardianCrystal").getAsInt();

        /**
         * 오전 4:56 2020-04-07
         * 밸런스값 추가
         * 일단은 80 고정하기로 했으니까 이렇게 하고..
         * 성준씨랑 이야기 해 보고? 웹서버에서 보내준다고 하면
         * 그때 풀지 머.
         * 근데.. 아마 DB도 새로 짜야할텐데, 밸런스 필드 추가하고 그래야할듯
         */
        this.balance = 80f;
        //this.balance = playerInfo.get("balance").getAsFloat();


        System.out.println("공격데미지 : " + this.attackDamage);
        System.out.println("방어력 : " + this.defense);
        System.out.println("체력 : " + this.hp);
        System.out.println("체력 회복률 : " + this.hpRecoveryRate);
        System.out.println("공격 속도 : " + this.attackSpeed);
        System.out.println("공격 범위 : " + this.attackRange);
        System.out.println("이동 속도 : " + this.moveSpeed);
        System.out.println("크리티컬 비율 : " + this.criticalRate);
        System.out.println("크리티컬 보너스 : " + this.criticalBonus);

        System.out.println("웹서버로부터 받아온 속성 타입 : " + this.elemental);


    }


    public CharDataFromJS(int userToken, int guardianType, int guardianLV, float exp, int arousal, float attackDamage, float defense, float hp, float hpRecoveryRate, float mp, float mpRecoveryRate, float attackSpeed, float moveSpeed, float criticalRate, float criticalBonus) {
        this.userToken = userToken;
        this.guardianType = guardianType;
        this.guardianLV = guardianLV;
        this.exp = exp;
        this.arousal = arousal;
        this.attackDamage = attackDamage;
        this.defense = defense;
        this.hp = hp;
        this.hpRecoveryRate = hpRecoveryRate;
        this.mp = mp;
        this.mpRecoveryRate = mpRecoveryRate;
        this.attackSpeed = attackSpeed;
        this.moveSpeed = moveSpeed;
        this.criticalRate = criticalRate;
        this.criticalBonus = criticalBonus;
    }
}
