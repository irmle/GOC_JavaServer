package ECS.Game;

import java.util.ArrayList;
import java.util.HashMap;
import ECS.Classes.*;
import ECS.Classes.Type.CharacterType;
import ECS.Entity.CrystalEntity;
import ECS.Factory.RewardFactory;
import ECS.System.LevelUpSystem;
import ECS.System.RewardSystem;

/**
 * 2019 12 16 월 권령희 추가
 *      레벨업 관련 데이터테이블.. 팩토리에 넣기에는 애매해서 일단 여기다가 추가.
 *      levelUpTable() 매서드 추가
 * 2019 12 26 목 권령희 추가
 *      레벨업 관련 데이터테이블 << 엑셀에 있는 값 및 공식 적용
 *
 */
public class GameDataManager {

    // 웨이브 카운트별 등장 몬스터 리스트
    static HashMap<Integer, HashMap<Integer, Integer>> waveArmy;

    static HashMap<Integer, CharacterInfo> characterTableDB;
    static HashMap<Integer, MonsterInfo> monsterTableDB;
    static HashMap<Integer, AttackTurretInfo> attackTurretInfoTableDB;
    static HashMap<Integer, BuffTurretInfo> buffTurretInfoTableDB;
    static HashMap<Integer, BarricadeInfo> barricadeTableDB;

    // 캐릭터 레벨업 관련
    static HashMap<Integer, Float> levelUpTable;  // currentLevel, expAmountForLevelUP
    static HashMap<Integer, CharacterLevelUpInfo> statByCharacterLevelUpTable;  // CharacterType, characterLevelUpInfo

    // ??
    public static int MAX_ITEM_SLOT_SIZE = 2;
    public static int MAX_ITEM_COUNT = 5;
    public static float ITEM_USE_COOLTIME = 1f;

    public static void initGameData()
    {
        System.out.println("GameDataManager 초기화중...");

        //테이블 초기화
        waveArmy = new HashMap<>();

        characterTableDB = new HashMap<>();
        monsterTableDB = new HashMap<>();
        attackTurretInfoTableDB = new HashMap<>();
        buffTurretInfoTableDB = new HashMap<>();
        barricadeTableDB = new HashMap<>();

        levelUpTable = new HashMap<>();
        statByCharacterLevelUpTable = new HashMap<>();

        //데이터 로드
        loadWaveArmy();

        loadCharacterTableDB();
        loadMonsterTableDB();
        loadAttackTurretTableDB();
        loadBuffTurretTableDB();
        loadBarricadeTableDB();

        // 레벨업 관련 데이터 로드
        loadLevelUpTable();
        loadCharacterLevelUpTable();

        System.out.println("GameDataManager 초기화 완료");
    }


    public static void loadLevelUpTable(){

        initLevelUpTable();
    }

    /**
     * 2019 12 26 목
     * 각 레벨별로 레벨업에 필요한 경험치를 계산한다
     */
    public static void initLevelUpTable() {

        float a = 100;  // 가중치.
        float c = 50;  // 조정치.

        /* 각 레벨별 누적 경험치를 구한다 */
        HashMap<Integer, Float> accumulatedExpList = new HashMap<>();
        accumulatedExpList.put(1, 0f);
        for (int i = 2; i <= 15; i++) {
            float exp = (float) (a * Math.pow((double) i-1, 2d) + c);
            accumulatedExpList.put(i, exp);
        }

        /* 누적 경험치를 이용해, 레벨업에 필요한 경험치량을 구한다 */
        levelUpTable = new HashMap<>();
        levelUpTable.put(1, 0f);
        for (int i = 2; i <= 15; i++) {
            float needExp = accumulatedExpList.get(i) - accumulatedExpList.get(i - 1);
            levelUpTable.put(i, needExp);
        }

        /* 테스트용 출력 */
        if(true){

            System.out.println("레벨업 정보 출력 테스트");
            for(int i=1; i<=15; i++){

                System.out.print("레벨" + i + "누적 경험치 : " + accumulatedExpList.get(i));
                System.out.println(", 필요 경험치 : " + levelUpTable.get(i));
            }
        }


    }


    public static void loadCharacterLevelUpTable(){

        CharacterLevelUpInfo knightInfo
                = new CharacterLevelUpInfo(84f, 0f, 4.5f, 4.65f * 0.01f,
                0.5f, 0f, 3.7f,
                1f, 5f, 0f, 0f, 0f);
        statByCharacterLevelUpTable.put(CharacterType.KNIGHT, knightInfo);

        CharacterLevelUpInfo archerInfo
                = new CharacterLevelUpInfo(91f, 50f, 4.3f, 4.65f * 0.01f,
                0.5f, 1f, 2.5f,
                1f, 5f, 0f, 0f, 0f);
        statByCharacterLevelUpTable.put(CharacterType.ARCHER, archerInfo);

        CharacterLevelUpInfo magicianInfo
                = new CharacterLevelUpInfo(92f, 35f, 4.5f, 3.65f * 0.01f,
                0.5f, 0.6f, 2f,
                1f, 5f, 0f, 0f, 0f);
        statByCharacterLevelUpTable.put(CharacterType.MAGICIAN, magicianInfo);

    }



    /**
     * 웨이브 단계별 등장하는 몬스터의 종류와 각각 등장하는 마릿수를
     * 파일로부터 읽어오는 처리를 하는 함수.
     * 일단은, 하드코딩을 통해 테이블을 채워준다.
     * 모든 월드가 공통.
     * ㄴ 읽기 뿐이긴 한데.. 모두가 이걸 공유하도록 하는게 성능(?) 면에서 어떤 문제가.. 있을까??
     */
    static void loadWaveArmy() {

        HashMap<Integer, Integer> monsters;
        /** 일단 하드코딩 한다*/


        /* 1웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 3);
        waveArmy.put(1, monsters);

        /* 2웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 7);
        //monsters.put(2, 3);
        //monsters.put(1, 3);
        waveArmy.put(2, monsters);

        /* 3웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 9);
        //monsters.put(2, 5);
        //monsters.put(1, 5);
        waveArmy.put(3, monsters);


        //


        /* 4웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 18);
        /*monsters.put(2, 5);
        monsters.put(3, 3);
        monsters.put(6, 2);*/
        waveArmy.put(4, monsters);

        /* 5웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 25);
        /*monsters.put(3, 5);
        monsters.put(4, 10);*/
        waveArmy.put(5, monsters);

        /* 6웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 6);
        /*monsters.put(6, 5);
        monsters.put(7, 3);
        monsters.put(8, 2);*/
        waveArmy.put(6, monsters);

        /* 7웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 24);
        /*monsters.put(6, 5);
        monsters.put(7, 8);
        monsters.put(9, 5);*/
        waveArmy.put(7, monsters);

        /* 8웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 25);
        /*monsters.put(7, 5);
        monsters.put(8, 5);
        monsters.put(9, 5);
        monsters.put(10, 1);*/
        waveArmy.put(8, monsters);

        /* 9웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 27);
        /*monsters.put(7, 5);
        monsters.put(9, 8);
        monsters.put(10, 3);*/
        waveArmy.put(9, monsters);

        /* 10웨이브 */
        monsters = new HashMap<>();
        monsters.put(1, 29);
        /*monsters.put(9, 5);
        monsters.put(10, 8);*/
        waveArmy.put(10, monsters);

        /* 11웨이브 */
        monsters = new HashMap<>();
        /*monsters.put(11, 10);
        monsters.put(12, 2);*/
        waveArmy.put(11, monsters);

        /* 12웨이브 */
        monsters = new HashMap<>();
        monsters.put(11, 5);
        monsters.put(12, 2);
        monsters.put(13, 5);
        waveArmy.put(12, monsters);

        /* 13웨이브 */
        monsters = new HashMap<>();
        monsters.put(12, 2);
        monsters.put(13, 5);
        monsters.put(14, 5);
        waveArmy.put(13, monsters);

        /* 14웨이브 */
        monsters = new HashMap<>();
        monsters.put(13, 5);
        monsters.put(14, 5);
        monsters.put(15, 3);
        waveArmy.put(14, monsters);

        /* 15웨이브 */
        monsters = new HashMap<>();
        monsters.put(12, 2);
        monsters.put(14, 5);
        monsters.put(15, 5);
        waveArmy.put(15, monsters);

        /* 16웨이브 */
        monsters = new HashMap<>();
        monsters.put(12, 2);
        monsters.put(15, 10);
        waveArmy.put(16, monsters);

        /* 17웨이브 */
        monsters = new HashMap<>();
        monsters.put(12, 2);
        monsters.put(13, 10);
        waveArmy.put(17, monsters);

        /* 18웨이브 */
        monsters = new HashMap<>();
        monsters.put(12, 2);
        monsters.put(14, 10);
        waveArmy.put(18, monsters);

        /* 19웨이브 */
        monsters = new HashMap<>();
        monsters.put(12, 1);
        monsters.put(13, 5);
        monsters.put(14, 5);
        monsters.put(15, 5);
        waveArmy.put(19, monsters);

        /* 20웨이브 */
        monsters = new HashMap<>();
        monsters.put(12, 8);
        monsters.put(13, 2);
        monsters.put(14, 2);
        monsters.put(15, 2);
        waveArmy.put(20, monsters);
    }
    static void loadCharacterTableDB()
    {

    }

    static void loadMonsterTableDB()
    {

    }

    static void loadAttackTurretTableDB()
    {

    }

    static void loadBuffTurretTableDB()
    {

    }

    static void loadBarricadeTableDB()
    {

    }


    //characterType을 통해 데이터를  불러옴.
    public static CharacterInfo loadCharacterInfo(int characterType)
    {
        CharacterInfo result = characterTableDB.get(characterType);
        return (CharacterInfo)result.clone();
    }

    //characterType을 통해 데이터를  불러옴.
    public static MonsterInfo loadMonsterInfo(int monsterType)
    {
        MonsterInfo result = monsterTableDB.get(monsterType);
        return (MonsterInfo)result.clone();
    }

    //characterType을 통해 데이터를  불러옴.
    public static AttackTurretInfo loadAtackTurretInfo(int turretType)
    {
        AttackTurretInfo result = attackTurretInfoTableDB.get(turretType);
        return (AttackTurretInfo)result.clone();
    }

    //characterType을 통해 데이터를  불러옴.
    public static BuffTurretInfo loadBuffTurretInfo(int turretType)
    {
        BuffTurretInfo result = buffTurretInfoTableDB.get(turretType);
        return (BuffTurretInfo)result.clone();
    }

    //characterType을 통해 데이터를  불러옴.
    public static BarricadeInfo loadBarricadeInfo(int barricadeType)
    {
        BarricadeInfo result = barricadeTableDB.get(barricadeType);
        return (BarricadeInfo)result.clone();
    }

    /**
     * 넘겨받은 레벨 값을 가지고, 현 레벨에서 다음 레벨로 업그레이드 하기 위해 필요한 경험치 량을 찾아 리턴한다
     * @param currentLevel
     * @return
     */
    public static float getMaxExpByLevel(int currentLevel){

        float expAmount;
        if(currentLevel == LevelUpSystem.MAX_LEVEL){
            expAmount = levelUpTable.get(currentLevel);
        }
        expAmount = levelUpTable.get(currentLevel+1);

        return expAmount;
    }

    /**
     * 넘겨받은 캐릭터 타입을 가지고, 해당 타입의 캐릭터가 레벨업할 때 변동되는 스탯 정보를 담고있는 클래스를 리턴한다
     */
    public static CharacterLevelUpInfo getLevelUpInfo(int characterType){

        return statByCharacterLevelUpTable.get(characterType);
    }

}
