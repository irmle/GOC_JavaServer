package ECS.Game;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.StringTokenizer;

import ECS.Classes.*;
import ECS.Classes.Type.*;
import ECS.Classes.Type.BalanceData.BalanceDataType;
import ECS.Classes.Type.Jungle.JungleMobType;

/**
 * 업뎃날짜 : 오후 11:34 2020-04-07
 * 업뎃내용 :
 *  -- 기존의 하드코딩 해서 쓰던 데이터들을, 파일에서 읽어오도록 변경
 *  -- 일단 게임에서 쓰일 모든 데이터 관련 파일들을, 이 GDM 클래스에서 읽어오도록 함.
 *      이후 각 덷이터를 필요로 하는 클래스들에서 가져다 쓰거나 가공하여 처리.
 *  --
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
    public static HashMap<Integer, Float> levelUpTable;  // currentLevel, expAmountForLevelUP
    static HashMap<Integer, CharacterLevelUpInfo> statByCharacterLevelUpTable;  // CharacterType, characterLevelUpInfo

    // ??
    public static int MAX_ITEM_SLOT_SIZE = 2;
    public static int MAX_ITEM_COUNT = 5;
    public static float ITEM_USE_COOLTIME = 1f;


    public static void initGameData() {

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

        // 2020 04 03
        initGameDataManager();

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

        BalanceData levelUpBalanceData = balanceDataInfoList.get(BalanceDataType.EXP_FOR_CHARACTER_LEVEL_UP);

        float a = levelUpBalanceData.weightValue;  // 가중치.
        float c = levelUpBalanceData.adjustmentValue;  // 조정치.

        /* 각 레벨에 도달하기 위한 누적 경험치를 구한다 */
        HashMap<Integer, Float> accumulatedExpList = new HashMap<>();
        accumulatedExpList.put(1, 0f);  // 1렙에 도달하기 위한 누적 경험치. 없음.
        for (int i = 2; i <= levelUpBalanceData.maxLevel; i++) {
            float exp = (float) (a * Math.pow((double) i-1, 2d) + c);
            accumulatedExpList.put(i, exp);
        }

        /* 누적 경험치를 이용해, 레벨업에 필요한 경험치량을 구한다 */
        levelUpTable = new HashMap<>();
        levelUpTable.put(1, 0f);    // 1렙에 도달하기 위한 필요 경험치. 없음.
        for (int i = 2; i <= levelUpBalanceData.maxLevel; i++) {
            float needExp = accumulatedExpList.get(i) - accumulatedExpList.get(i - 1);
            levelUpTable.put(i, needExp);
        }

        /* 테스트용 출력 */
        if(false){

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

        int CHAR_MAX_LEVEL = balanceDataInfoList.get(BalanceDataType.EXP_FOR_CHARACTER_LEVEL_UP).maxLevel;

        float expAmount;
        if(currentLevel == CHAR_MAX_LEVEL){
            expAmount = levelUpTable.get(currentLevel);
        }
        expAmount = levelUpTable.get(currentLevel+1);

        return expAmount;
    }

    /**
     * 넘겨받은 캐릭터 타입을 가지고, 해당 타입의 캐릭터가 레벨업할 때 변동되는 스탯 정보를 담고있는 클래스를 리턴한다
     *
     * 2020 04 03 수정. csv 파일에서 가져온 데이터를 담고있는 데이터 목록을 활용하도록.
     */
    public static CharacterLevelUpInfo getLevelUpInfo(int characterType){

        //return statByCharacterLevelUpTable.get(characterType);

        return characterLevelUpInfoList.get(characterType);
    }

    /*******************************************************************************************************************/

    /*******************************************************************************************************************/
    /**
     * 2020 03 31 버전.
     * 파일에서 읽어오는 값들을 적용해 처리하도록 수정하는 작업이 완전히 끝나면,
     *  위의 기존 변수 내역들은 지울 것.
     */

    /************************ 멤버 변수 ***************************************/

    /* 파일 목록 */
    public static HashMap<Integer, String> filePathList;

    /* 몬스터 정보 목록 <MonsterType, MonsterInfo> */
    public static HashMap<Integer, MonsterInfo> monsterInfoList;
    public static HashMap<String, Integer> monsterActionList;

    /* 웨이브별 몬스터 등장 목록 < WaveCount, <MonsterType, mobCount>*/
    public static HashMap<Integer, HashMap<Integer, Integer>> waveArmyList;

    /* 정글몬스터 정보 목록 <JungleMonsterType, MonsterInfo> */
    public static HashMap<String, Integer> jungleMonsterList;
    public static HashMap<Integer, MonsterInfo> jungleMonsterInfoList;

    /* 캐릭터 타입별 레벨업 시 스탯 변화 목록 < CharacterType, CharacterLevelUpInfo >*/
    public static HashMap<Integer, CharacterLevelUpInfo> characterLevelUpInfoList;

    public static HashMap<String, Integer> turretList;

    /* 공격 포탑 타입별 정보 */
    public static HashMap<Integer, AttackTurretInfo> attackTurretInfoList;

    /* 버프 포탑 타입별 정보 */
    public static HashMap<Integer, BuffTurretInfo> buffTurretInfoList;

    /* 바리케이드 정보 */
    public static HashMap<Integer, BarricadeInfo> barricadeInfoList;

    /* 상점 판매 (아이템) 목록 */
    public static HashMap<String, Integer> itemList;
    public static HashMap<Integer, ItemInfo> itemInfoList;

    /* 상점 업그레이드 단계별 정보 */
    public static HashMap<String, Integer> upgradeList;
    public static HashMap<Integer, HashMap<Integer, StoreUpgradeInfoPerLevel>> storeUpgradeInfoPerLevelList;

    /* 속성 상성 효과 목록 < 상성타입, 값( -50% ~ 50%) > */
    public static HashMap<String, Integer> synastryEffectList;
    public static HashMap<Integer, Float> synastryEffectValueList;

    /* 속성별 상성 대응 목록 <속성 타입, < 대응 속성 타입, 상성 타입> >*/
    public static HashMap<String, Integer> elementalTypeList;
    public static HashMap<Integer, HashMap<Integer, Integer>> elementalSynastryInfoList;

    /* 스킬 목록 */
    public static HashMap<String, Integer> skillTypeList;
    public static HashMap<Integer, SkillInfo> skillInfoList;

    /* 스킬 레벨별 정보 목록 < 스킬 타입, < 스킬 레빌, 레벨별 스킬 정보 > > */
    public static HashMap<Integer, HashMap<Integer, SkillInfoPerLevel>> skillInfoPerLevelList;

    /* 효과 목록 */
    public static HashMap<Integer, HashMap<Integer, HashMap< String, BuffInfo>>> effectInfoList;

    /* 밸런싱 데이터 목록*/
    public static HashMap<String, Integer> balanceDataTypeList;
    public static HashMap<Integer, BalanceData> balanceDataInfoList;

    /**
     * 주석 작성
     * 오후 11:34 2020-04-07
     */
    /* 게임 난이도 등급 정보 목록 */
    public static HashMap<String, Integer> gameDifficultyGradeList;
    public static HashMap<Integer, GameDifficultyGradeInfo> gameDifficultyGradeInfoList;


    public static HashMap<String, Integer> systemEffectTypeList;


    /************************ 생성자 ******************************************/

    public static void initGameDataManager() {

        /* 파일 경로 목록 초기화*/
        initFilePath();

        /* 파일로부터 게임 데이터를 읽어 초기화 */
        initGameDataFromFiles();
    }

    /************************ 매서드 ******************************************/

    public static void initFilePath(){

        filePathList = new HashMap<>();

        String path = "C:\\Users\\irmle\\Desktop\\로컬 서버 작업\\20200331(25th_2)\\NovaGameTeam\\GameDataFiles";
        path = Paths.get("").toAbsolutePath().toString() + "/src/GameDataFiles/";

        filePathList.put(FilePathType.MONSTER_INFO_LIST, path + "MonsterInfoList" + ".csv");
        filePathList.put(FilePathType.MONSTER_LIST_PER_WAVE, path + "MonsterListPerWave" + ".csv");
        filePathList.put(FilePathType.JUNGLE_MONSTER_INFO_LIST, path + "JungleMonsterInfoList" + ".csv");
        filePathList.put(FilePathType.CHANGED_STAT_BY_LEVEL_PER_CHAR, path + "ChangedStatByLevelUpPerCharType" + ".csv");
        filePathList.put(FilePathType.BARRICADE_INFO_LIST, path + "BarricadeInfoList" + ".csv");
        filePathList.put(FilePathType.TURRET_INFO_LIST, path + "TurretInfoList" + ".csv");
        filePathList.put(FilePathType.SYNASTRY_EFFECT_LIST, path + "SynastryEffectList" + ".csv");
        filePathList.put(FilePathType.ELEMENTAL_SYNASTRY_INFO_LIST, path + "ElementalSynastryInfoList" + ".csv");
        filePathList.put(FilePathType.SKILL_INFO_LIST, path + "SkillInfoList" + ".csv");
        filePathList.put(FilePathType.SKILL_INFO_LIST_PER_SKILL_LEVEL, path + "SkillInfoListPerSkillLevel" + ".csv");
        filePathList.put(FilePathType.STORE_SALES_LIST, path + "StoreSalesList" + ".csv");
        filePathList.put(FilePathType.STORE_UPGRADE_LIST_PER_LEVEL, path + "StoreUpgradeListPerLevel" + ".csv");
        filePathList.put(FilePathType.WHOLE_EFFECT_VALUE_LIST, path + "WholeEffectValueList" + ".csv");
        filePathList.put(FilePathType.BALANCE_DATA_LIST, path + "BalanceDataList" + ".csv");
        filePathList.put(FilePathType.GAME_DIFFICULTY_GRADE_LIST, path + "GameDifficultyGradeInfoList" + ".csv");

    }


    /**
     * 게임 데이터 파일을 읽어들이는 처리를 한다.
     * 각 파일별 매서드 모두를 호출함.
     */
    public static void initGameDataFromFiles() {

        readMonsterInfo();    // 완료
        readWaveMonsterInfo();
        readJungleMonsterInfo();
        readCharacterStatChangedByLevelUpInfo();
        readTurretInfo();
        readBarricadeInfo();
        readStoreSaleInfo();
        readSynastryEffectInfo();
        readElementalSynastryInfo();
        readStoreUpgradeInfo();

        readSystemEffectTypeList();

        readSkillInfo();
        readSkillStatByLevelInfo();
        readEffectInfo();

        readBalanceInfo();
        readGameDifficultyGradeInfo();

    }

    /**
     * 게임 내 몬스터 종류별 스펙을 읽어들임
     */
    public static void readMonsterInfo(){

        /** 0. 초기화 */
        monsterInfoList = new HashMap<>();

        /*
         * MONSTER_ATTACK 하나밖에 없어서 그냥 여기다 하드코딩 했었는데.
         * 나중에 별도 목록 만들 것.
         */
        monsterActionList = new HashMap<>();
        monsterActionList.put("MONSTER_ATTACK", MonsterActionType_ForEffect.MONSTER_ATTACK);
        monsterActionList.put("JUNGLE_MOB_RETURN", MonsterActionType_ForEffect.JUNGLE_MOB_RETURN);

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.MONSTER_INFO_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;
            MonsterInfo newMobInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* id를 얻는다. */
                    String id = tokenizer.nextToken();
                    switch (id){

                        /*
                         * 원래 switch ~ case 문 내에서 헤더와 컨텐츠를 분리해주려 했는데,
                         * tokenizef에서 분리한 String과 constant String의  비교가 생각대로 동작하지 않아서..
                         * case문 빼도 상관없긴 한데, 일단 냅둠.
                         */
                        /** 헤더를 제외한 나머지의 경우, 읽은 라인을 가지고 MonsterInfo를 생성한다  */
                        default:

                            /* 몹 타입 ID를 얻음 */
                            int mobTypeID = Integer.parseInt(id);

                            /* 새 MonsterInfo를 생성한다 */
                            newMobInfo = createMonsterInfo(mobTypeID, tokenizer);
                            monsterInfoList.put(mobTypeID, newMobInfo);

                            System.out.println("몬스터 " + newMobInfo.monsterName + "의 정보를 파싱했습니다. ");
                            //newMobInfo.printMonsterInfo();

                            break;

                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * readMonsterInfo 매서드가 파일에서 읽어들인 몬스터 하나의 정보를 Tokenizer로 분리해 넘겨준다.
     * 넘겨받은 Tokenizer에 들어있는 정보를 하나씩 읽어, 타입에 맞게 형변환 하여
     * 최종적으로 MonsterInfo 객체 하나를 생성해 리턴한다.
     * @param mobInfo
     * @return
     */
    public static MonsterInfo createMonsterInfo(int mobTypeID, StringTokenizer mobInfo){

        MonsterInfo newMonsterInfo;

        /** 파싱 */

        /* 몬스터 이름 */
        String mobName = mobInfo.nextToken();

        /* 몬스터 등급 */
        String mobGradeStr = mobInfo.nextToken();
        int mobGrade = getGradeByParsingString(mobGradeStr);

        /* 몬스터 속성 */
        String monElemStr = mobInfo.nextToken();
        int mobElemental = getElementalByParsingString(monElemStr);

        /* 몬스터 체력 */
        float mobHP = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 체력증가량 (레벨업 시) */
        float mobHPIncrValue = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 체력회복량 */
        float mobHPRecv = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 체력회복 증가량 (레벨업 시) */
        float mobRecvIncrValue = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 공격력 */
        float mobAttackDamage = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 공격력 증가량 */
        float mobAttackDamageIncrValue = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 방어력 */
        float mobDefense = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 방어력 증가량 */
        float mobDefenseIncrValue = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 공격속도 */
        float mobAttackSpeed = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 이동속도 */
        float mobMoveSpeed = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 사정거리 */
        float mobAttackRange = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 시야 */
        float mobLookRadius = Float.parseFloat(mobInfo.nextToken());


        /** 생성*/
        newMonsterInfo = new MonsterInfo(
                mobTypeID, mobGrade, mobElemental, mobName,
                mobMoveSpeed, mobHP, mobHPRecv,
                mobAttackDamage, mobAttackSpeed,mobAttackRange,
                mobDefense, mobLookRadius,
                mobHPIncrValue, mobRecvIncrValue, mobAttackDamageIncrValue, mobDefenseIncrValue);

        return newMonsterInfo;
    }

    /**
     * 등급 파싱 매서드.
     * @param gradeStr
     * @return
     */
    public static int getGradeByParsingString(String gradeStr){

        int grade;
        switch (gradeStr){

            case "N":
                grade = GradeType.NORMAL;
                break;
            case "R":
                grade = GradeType.RARE;
                break;
            case "E":
                grade = GradeType.EPIC;
                break;
            case "U":
                grade = GradeType.UNIQUE;
                break;
            case "L":
                grade = GradeType.LEGENDERY;
                break;

            default:
                grade = GradeType.NONE;
                break;
        }

        return grade;

    }

    /**
     * 속성 파싱 매서드.
     * @param elementalStr
     * @return
     */
    public static int getElementalByParsingString(String elementalStr){

        int elemental;
        switch (elementalStr){

            case "R":
            case "RED":
                elemental = ElementalType.RED;
                break;
            case "B":
            case "BLUE":
                elemental = ElementalType.BLUE;
                break;
            case "G":
            case "GREEN":
                elemental = ElementalType.GREEN;
                break;

            case "Y":
            case "YELLOW":
                elemental = ElementalType.YELLOW;
                break;
            case "P":
            case "PINK":
                elemental = ElementalType.PINK;
                break;

            case "S":
            case "SKYBLUE":
                elemental = ElementalType.SKY_BLUE;
                break;


            case "W":
            case "WHITE":
                elemental = ElementalType.WHITE;
                break;

            case "BK":
            case "BLACK":
                elemental = ElementalType.BLACK;
                break;

            default:
                elemental = ElementalType.NONE;
                break;
        }

        return elemental;

    }


    /**
     * 시스템 효과 파싱 매서드.
     * @param str
     * @return
     */
    public static int getSystemEffectTypeByParsingString(String str){

        int type;
        switch (str){

            case "WELL":
                type = GradeType.NORMAL;
                break;
            case "SELF_RECOVERY":
                type = GradeType.RARE;
                break;

                default:
                type = GradeType.NONE;
                break;
        }

        return type;

    }


    /**
     * 웨이브당 등장하는 몬스터 종류 및 수 정보를 읽어들임
     */
    public static void readWaveMonsterInfo(){

        /** 0. 초기화 */
        waveArmyList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.MONSTER_LIST_PER_WAVE);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* 웨이브 수를 얻는다. */
                    int waveCount = Integer.parseInt(tokenizer.nextToken());

                    /* 아직 21웨이브 이상의 경우 몹 등장 스펙이 정해지지 않음. 죄다 랜덤.
                     * 일단, 수가 정해져 있는 20까지만 데이터를 집어넣고,
                     * 그 이상은 파일에서 읽지 말고, 나중에 랜덤으로 구하는 처리 만들어서 추가해주는 식으로 해야 할 듯.
                     */
                    if(waveCount > 20){
                        break;
                    }

                    System.out.println("웨이브" + waveCount + "에 등장하는 몬스터 정보를 읽어들입니다.");

                    /* 군단 정보를 집어넣을 맵 생성 */
                    HashMap<Integer, Integer> waveArmy = new HashMap<>();
                    for(int i=1; i<=monsterInfoList.size(); i++){

                        int mobCount = Integer.parseInt(tokenizer.nextToken());
                        if(mobCount > 0){
                            waveArmy.put(i, mobCount);
                            //System.out.println(mobCount);
                        }
                    }

                    waveArmyList.put(waveCount, waveArmy);

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 게임 내 정글몬스터 종류별 스펙을 읽어들임
     */
    public static void readJungleMonsterInfo(){

        /** 0. 초기화 */
        jungleMonsterInfoList = new HashMap<>();
        jungleMonsterList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.JUNGLE_MONSTER_INFO_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;
            MonsterInfo newMobInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* id를 얻는다. */
                    String id = tokenizer.nextToken();

                    switch (id){

                        /*
                         * 원래 switch ~ case 문 내에서 헤더와 컨텐츠를 분리해주려 했는데,
                         * tokenizef에서 분리한 String과 constant String의  비교가 생각대로 동작하지 않아서..
                         * case문 빼도 상관없긴 한데, 일단 냅둠.
                         */
                        /** 헤더를 제외한 나머지의 경우, 읽은 라인을 가지고 MonsterInfo를 생성한다  */
                        default:

                            /* 몹 타입 ID를 얻음 */
                            int mobTypeID = Integer.parseInt(id);


                            /* 새 MonsterInfo를 생성한다 */
                            newMobInfo = createJungleMonsterInfo(mobTypeID, tokenizer);
                            jungleMonsterInfoList.put(mobTypeID, newMobInfo);

                            jungleMonsterList.put(newMobInfo.monsterName, mobTypeID);

                            System.out.println("정글 몬스터 " + newMobInfo.monsterName + "의 정보를 파싱했습니다. ");
                            //newMobInfo.printMonsterInfo();

                            break;

                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MonsterInfo createJungleMonsterInfo(int mobTypeID, StringTokenizer mobInfo){

        MonsterInfo newMonsterInfo;

        /** 파싱 */

        /* 몬스터 이름 */
        String mobName = mobInfo.nextToken();

        /* 몬스터 등급 */
        String mobGradeStr = mobInfo.nextToken();
        int mobGrade = getGradeByParsingString(mobGradeStr);

        /* 몬스터 속성 */
        String monElemStr = mobInfo.nextToken();
        int mobElemental = getElementalByParsingString(monElemStr);

        /* 몬스터 체력 */
        float mobHP = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 체력증가량 (레벨업 시) */
        float mobHPIncrValue = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 체력회복량 */
        float mobHPRecv = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 체력회복 증가량 (레벨업 시) */
        float mobRecvIncrValue = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 공격력 */
        float mobAttackDamage = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 공격력 증가량 */
        float mobAttackDamageIncrValue = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 방어력 */
        float mobDefense = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 방어력 증가량 */
        float mobDefenseIncrValue = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 공격속도 */
        float mobAttackSpeed = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 이동속도 */
        float mobMoveSpeed = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 사정거리 */
        float mobAttackRange = Float.parseFloat(mobInfo.nextToken());

        /* 몬스터 시야 */
        float mobLookRadius = Float.parseFloat(mobInfo.nextToken());

        /* 정글몹 리젠 타임 */
        float mobRegenTime = Float.parseFloat(mobInfo.nextToken());

        /* 처치 골드 */
        int rewardGold = Integer.parseInt(mobInfo.nextToken());

        /* 처치 골드 */
        int rewardGoldIncrValue = Integer.parseInt(mobInfo.nextToken());

        /* 처치 경험치 */
        int rewardExp = Integer.parseInt(mobInfo.nextToken());

        /* 처치 골드 */
        int rewardExpIncrValue = Integer.parseInt(mobInfo.nextToken());

        /* 버프를 가지고 있는가? */
        boolean hasRewardBuff = Boolean.parseBoolean(mobInfo.nextToken());

        /* 등장 확률 */
        float appearProbRate = Float.parseFloat(mobInfo.nextToken());

        /** 생성*/
        newMonsterInfo = new MonsterInfo(
                mobTypeID, mobGrade, mobElemental, mobName,
                mobMoveSpeed, mobHP, mobHPRecv,
                mobAttackDamage, mobAttackSpeed,mobAttackRange,
                mobDefense, mobLookRadius,rewardExp, rewardGold, mobRegenTime,
                mobHPIncrValue, mobRecvIncrValue, mobAttackDamageIncrValue, mobDefenseIncrValue,
                rewardGoldIncrValue, rewardExpIncrValue, hasRewardBuff, appearProbRate);

        return newMonsterInfo;
    }

    /**
     * 각 캐릭터 타입별로, 레벨업 시 변화되는 스탯 정보를 읽어들임
     */
    public static void readCharacterStatChangedByLevelUpInfo(){

        /** 0. 초기화 */
        characterLevelUpInfoList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.CHANGED_STAT_BY_LEVEL_PER_CHAR);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;
            CharacterLevelUpInfo newCharLevelUpInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* id를 얻는다. */
                    String id = tokenizer.nextToken();
                    switch (id){

                        /*
                         * 원래 switch ~ case 문 내에서 헤더와 컨텐츠를 분리해주려 했는데,
                         * tokenizef에서 분리한 String과 constant String의  비교가 생각대로 동작하지 않아서..
                         * case문 빼도 상관없긴 한데, 일단 냅둠.
                         */
                        /** 헤더를 제외한 나머지의 경우, 읽은 라인을 가지고 MonsterInfo를 생성한다  */
                        default:

                            /* 캐릭터 타입 ID를 얻음 */
                            int charTypeID = Integer.parseInt(id);

                            String charType = tokenizer.nextToken();

                            /* 새 CharacterInfo 를 생성한다 */
                            newCharLevelUpInfo = createCharLevelUpInfo(charTypeID, tokenizer);
                            characterLevelUpInfoList.put(charTypeID, newCharLevelUpInfo);

                            System.out.println("캐릭터 " + charType + "의 정보를 파싱했습니다. ");

                            break;

                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CharacterLevelUpInfo createCharLevelUpInfo(int charTypeID, StringTokenizer charInfo){

        CharacterLevelUpInfo newCharLevelUpInfo;

        /** 파싱 */

        /* 체력 증가량  */
        float charHPIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 마나 증가량 */
        float charMPIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 공격력 증가량 */
        float attackDamageIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 공격속도 증가량 */
        String attackSpeedStr = charInfo.nextToken();
        if(attackSpeedStr.contains("%")){
            attackSpeedStr = attackSpeedStr.substring(0, attackSpeedStr.indexOf('%'));
            //System.out.println("%를 제거했습니다.");
        }
        float attackSpeedIncrValue = Float.parseFloat(attackSpeedStr);
        //float attackSpeedIncrValue = Float.parseFloat(charInfo.nextToken());  // %를 고려하지 않음.

        /* 체력회복량 증가량 */
        float hpRecoveryIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 마력회복량 증가량 */
        float mpRecoveryIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 방어력 증가량 */
        float defenseIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 치명타확률 증가량 */
        float criticalProbIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 치명타데미지 증가량 */
        float criticalDamageIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 이동속도 증가량 */
        float moveSpeedIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 사정거리 증가량 */
        float attackRangeIncrValue = Float.parseFloat(charInfo.nextToken());

        /* 시야 증가량 */
        float lookRadiusIncrValue = Float.parseFloat(charInfo.nextToken());


        /** 생성*/
        newCharLevelUpInfo = new CharacterLevelUpInfo(charHPIncrValue, charMPIncrValue,
                attackDamageIncrValue, attackSpeedIncrValue,
                hpRecoveryIncrValue, mpRecoveryIncrValue, defenseIncrValue,
                criticalProbIncrValue, criticalDamageIncrValue,
                moveSpeedIncrValue, attackRangeIncrValue, lookRadiusIncrValue);


        return newCharLevelUpInfo;
    }

    /**
     * 포탑 종류 및 스펙 정보를 읽어들임
     */
    public static void readTurretInfo(){

        /** 0. 초기화 */
        attackTurretInfoList = new HashMap<>();
        buffTurretInfoList = new HashMap<>();

        turretList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.TURRET_INFO_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;

            AttackTurretInfo newATurretInfo;
            BuffTurretInfo newBTurretInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* id를 얻는다. */
                    String id = tokenizer.nextToken();
                    int turretTypeID = Integer.parseInt(id);

                    String turretTypeStr = tokenizer.nextToken();
                    if(turretTypeStr.contains("ATTACK")){

                        newATurretInfo = createAttackTurretInfo(turretTypeID, turretTypeStr, tokenizer);
                        attackTurretInfoList.put(turretTypeID, newATurretInfo);

                        turretList.put(turretTypeStr, turretTypeID);
                        System.out.println("터렛 " + turretTypeStr + "의 데이터를 파싱합니다.");

                    } else if(turretTypeStr.contains("BUFF")){

                        newBTurretInfo = createBuffTurretInfo(turretTypeID, turretTypeStr, tokenizer);
                        buffTurretInfoList.put(turretTypeID, newBTurretInfo);

                        turretList.put(turretTypeStr, turretTypeID);
                        System.out.println("터렛 " + turretTypeStr + "의 데이터를 파싱합니다.");

                    } else{

                        System.out.println("터렛 " + turretTypeStr +  "의 타입을 알 수 없습니다. ");
                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 공격 포탑의 정보를 파싱, 생성하는 함수
     * @param turretID
     * @param turretName
     * @param tokenizer
     * @return
     */
    public static AttackTurretInfo createAttackTurretInfo(int turretID, String turretName, StringTokenizer tokenizer){

        AttackTurretInfo newATurret;

        /** 파싱 */

        /* 터렛 최대 체력 */
        float turretHP = Float.parseFloat(tokenizer.nextToken());

        /* 터렛 체력 회복량 */
        float hpReceveryValue = Float.parseFloat(tokenizer.nextToken());

        /* 공격력 */
        float attackDamage = 0f;
        float minAttackDamage = 0f;
        float maxAttackDamage = 0f;

        String attackDamageStr = tokenizer.nextToken();
        if(attackDamageStr.contains("~")){  /* 공격력이 범위값(000 ~ 999)을 가지는 경우, 두 값을 분리한다. */

            String[] attackDamageStrArray = attackDamageStr.split("~");
            minAttackDamage = Float.parseFloat(attackDamageStrArray[0]);
            maxAttackDamage = Float.parseFloat(attackDamageStrArray[1]);

        }
        else{
            attackDamage = Float.parseFloat(attackDamageStr);
        }

        /* 공격 속도 */
        float attackSpeed = Float.parseFloat(tokenizer.nextToken());

        /* 방어력 */
        float defense = Float.parseFloat(tokenizer.nextToken());

        /* 사정거리 */
        float attackRange = Float.parseFloat(tokenizer.nextToken());

        /* 건설 비용 */
        int buildCost = Integer.parseInt(tokenizer.nextToken());

        /* 건설 시간*/
        float buildTime = Float.parseFloat(tokenizer.nextToken());

        /* 투사체 속도 */
        float flyingObsSpeed = Float.parseFloat(tokenizer.nextToken());

        /* 투사체 공격 범위 */
        float flyingObsAttackRadius = Float.parseFloat(tokenizer.nextToken());


        /** 생성*/
        newATurret = new AttackTurretInfo(turretID, turretName,
                buildCost, buildTime, turretHP, hpReceveryValue,
                attackDamage, attackSpeed, attackRange,
                minAttackDamage, maxAttackDamage, defense, flyingObsSpeed, flyingObsAttackRadius);


        return newATurret;
    }


    /**
     * 버프 포탑의 정보를 파싱, 생성하는 함수
     * @param turretID
     * @param turretName
     * @param tokenizer
     * @return
     */
    public static BuffTurretInfo createBuffTurretInfo(int turretID, String turretName, StringTokenizer tokenizer){

        BuffTurretInfo newBTurret;


        /** 파싱 */
        /* 터렛 최대 체력 */
        float turretHP = Float.parseFloat(tokenizer.nextToken());

        /* 터렛 체력 회복량 */
        float hpReceveryValue = Float.parseFloat(tokenizer.nextToken());

        /* 공격력 */   // 공격이랑 싱크를 맞추느라 넣었긴 한데, 0임.
        float attackDamage = Float.parseFloat(tokenizer.nextToken());

        /* 공격 속도 */
        float attackSpeed = Float.parseFloat(tokenizer.nextToken());

        /* 방어력 */
        float defense = 0f;
        float minDefense = 0f;
        float maxDefense = 0f;

        String defenseStr = tokenizer.nextToken();
        if(defenseStr.contains("~")){  /* 방어력이 범위값(000 ~ 999)을 가지는 경우, 두 값을 분리한다. */

            String[] defenseStrArray = defenseStr.split("~");
            minDefense = Float.parseFloat(defenseStrArray[0]);
            maxDefense = Float.parseFloat(defenseStrArray[1]);

        }
        else{
            defense = Float.parseFloat(defenseStr);
        }


        /* 효과 적용거리 */
        float buffAreaRange = Float.parseFloat(tokenizer.nextToken());

        /* 건설 비용 */
        int buildCost = Integer.parseInt(tokenizer.nextToken());

        /* 건설 시간*/
        float buildTime = Float.parseFloat(tokenizer.nextToken());


        /** 생성*/
        newBTurret = new BuffTurretInfo(turretID, turretName, buildCost, buildTime,
                turretHP, hpReceveryValue, buffAreaRange,
                defense, minDefense, maxDefense,
                attackSpeed);

        return newBTurret;

    }




    /**
     * 스킬 목록 및 정보를 읽어들임
     */
    public static void readSkillInfo(){

        /** 0. 초기화 */
        skillTypeList = new HashMap<>();
        skillInfoList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.SKILL_INFO_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;

            SkillInfo newSkillInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* id를 얻는다. */
                    String id = tokenizer.nextToken();
                    int skillTypeID = Integer.parseInt(id);

                    /* 스킬 타입명을 얻는다 */
                    String skillType = tokenizer.nextToken();
                    skillTypeList.put(skillType, skillTypeID);

                    newSkillInfo = createSkillInfo(skillTypeID, skillType, tokenizer);
                    skillInfoList.put(skillTypeID, newSkillInfo);

                    System.out.println("스킬 " + skillType + "의 정보를 파싱합니다.");
                    //newSkillInfo.printSkillInfo();

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static SkillInfo createSkillInfo(int skillID, String skillType, StringTokenizer tokenizer){

        SkillInfo skillInfo;

        /** 파싱 */

        /* 스킬명 */
        String skillName = tokenizer.nextToken();

        /* 적용 범위 */  // 단일, 범위
        String skillTargetArea = tokenizer.nextToken();

        /* 적중 타입 */ // 타겟&논타겟/즉발&투사체
        String skillEffectHitType = tokenizer.nextToken();

        /* 스킬 모양 */
        String skillAreaTypeStr = tokenizer.nextToken();
        int skillAreaType = getSkillAreaTypeByParsingString(skillAreaTypeStr);

        /* 각도 */
        float skillAreaAngle = Float.parseFloat(tokenizer.nextToken());

        /* 스킬 사용 사거리 */
        float skillRange = Float.parseFloat(tokenizer.nextToken());

        /* 쿨타임 */
        float skillCoolTime = Float.parseFloat(tokenizer.nextToken());

        /* MP 소모값 */
        float requireMP = Float.parseFloat(tokenizer.nextToken());

        /* .. 나머지 더 있긴 한데, 이것들은 나중에 사용하게 된다면 추가하는 걸로. 스킬 단계, max레벨, 습득여부, sp소모값 등등. */
        /* .. 아냐 일단은 읽어두기라도 하자! */

        /* 스킬 단계 */
        int skillGrade = Integer.parseInt(tokenizer.nextToken());

        /* 스킬의 max레벨 */
        int skillMaxLevel = Integer.parseInt(tokenizer.nextToken());

        /* 습득 여부 */
        boolean hasAcquired = Boolean.parseBoolean(tokenizer.nextToken());

        /* 스킬 습득에 필요한 SP(스킬 포인트) 값 */
        int requiredSP = Integer.parseInt(tokenizer.nextToken());



        /** 생성 */

        /* 스킬 오브젝트가 존재하는 경우 */
        /**
         * 콘형의 각도같은 경우, 나중에 스킬레벨정보로 뺄 수도 있음. 여기서는 모양만 지정해주고.
         * 나중에.. 스킬 정보랑 스킬레벨 정보를 좀 더 정교하게?? 투사체에 해당하는 값이랑 장판에 해당하는 값, 스킬 고유 값
         * 등등으로 분리하게 되면 그 때 적용할 것. 일단은 '스킬 정보'로서 같이 묶여있으니까..
         */
        SkillObjectInfo skillObjectInfo;
        switch (skillAreaType){

            case SkillAreaType.CIRCLE:
            case SkillAreaType.CONE:
            case SkillAreaType.RECTANGLE:

                skillObjectInfo = new SkillObjectInfo(skillAreaType, skillAreaAngle);

                break;

            default:
                skillObjectInfo = null;
                break;

        }

        float requireHp = 0f; // 현 스킬스펙 상, hp를 소모하는 스킬은 존재하지 않아서 일단 0으로.
        skillInfo = new SkillInfo(skillID, skillName, skillCoolTime, requireHp, requireMP, skillRange,
                null, skillObjectInfo, null);


        return skillInfo;

    }

    public static int getSkillAreaTypeByParsingString(String skillAreaTypeStr){

        int skillAreaType;
        switch (skillAreaTypeStr){

            case "CIRCLE":
                skillAreaType = SkillAreaType.CIRCLE;
                break;
            case "CONE":
                skillAreaType = SkillAreaType.CONE;
                break;
            case "RECTANGLE":
                skillAreaType = SkillAreaType.RECTANGLE;
                break;

            default:
                skillAreaType = SkillAreaType.NONE;
                break;
        }


        return skillAreaType;
    }


    /**
     * 스킬 레벨에 따라 달라지는 스탯을 읽어들임
     */
    public static void readSkillStatByLevelInfo(){

        /** 0. 초기화 */
        skillInfoPerLevelList = new HashMap<>();

        for (HashMap.Entry<String, Integer> entry : skillTypeList.entrySet()) {

            //System.out.println(entry.getKey());

        }



        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.SKILL_INFO_LIST_PER_SKILL_LEVEL);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;

            HashMap<Integer, SkillInfoPerLevel> skillInfoPerLevel;  // 한 스킬타입에 대해, 레벨별(1 ~ ) 스킬정보를 담을 것이다.
            SkillInfoPerLevel newSkillInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* 스킬 타입 및 ID를 얻는다. */
                    String skillType = tokenizer.nextToken();
                    String skillIDStr = tokenizer.nextToken();  // 엑셀에서 해당 행을 지워주면, 없애줘도 됨.

                    int skillTypeID = skillTypeList.get(skillType);

                    /* 해당 타입의 스킬레벨정보가 목록에 들어있다면 가져오고, 그렇지 않으면 새로 생성한다 */
                    if(skillInfoPerLevelList.containsKey(skillTypeID)){

                        skillInfoPerLevel = skillInfoPerLevelList.get(skillTypeID);
                    }
                    else{

                        skillInfoPerLevel = new HashMap<>();
                        skillInfoPerLevelList.put(skillTypeID, skillInfoPerLevel);
                    }


                    /* 스킬 레벨을 읽음 */
                    int skillLevel = Integer.parseInt(tokenizer.nextToken());

                    /* 레벨별 스킬 정보를 생성 */
                    newSkillInfo = createSkillInfoPerLevel(skillTypeID, skillLevel, tokenizer);

                    skillInfoPerLevel.put(skillLevel, newSkillInfo);

                    System.out.println("스킬 " + skillType + "의 " + skillLevel + "레벨 데이터를 파싱했습니다.");

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     *
     */
    public static SkillInfoPerLevel createSkillInfoPerLevel(int skillTypeID, int skillLevel, StringTokenizer tokenizer){

        SkillInfoPerLevel newSkillInfo;

        /** 파싱 */

        /* 공격력 */
        float attackDamage = Float.parseFloat(tokenizer.nextToken());

        /* 스킬 사거리 */
        float skillRange = Float.parseFloat(tokenizer.nextToken());

        /* 장판 사거리 */    // 현재 쓰이지 않음.
        float skillObjRange = Float.parseFloat(tokenizer.nextToken());

        /* 투사체 이동거리 */
        float flyingObjMoveDistance = Float.parseFloat(tokenizer.nextToken());

        /* 공격 범위 */
        float attackRange = Float.parseFloat(tokenizer.nextToken());

        /* 지속 시간 */
        float durationTime = Float.parseFloat(tokenizer.nextToken());

        /**
         * 작성날짜 : 2020 05 31 일요일
         * 업뎃내용 : 에어본 효과의 최대 높이를 지정해주기 위한 값 추가
         */
        /* 최대 높이 (에어본) */
        float maxHeight = Float.parseFloat(tokenizer.nextToken());

        /* 투사체 속도 */
        float flyingObjSpeed = Float.parseFloat(tokenizer.nextToken());

        /* 효과 지속 시간 */  // 현재 쓰이지 않음.
        float effectDurationTime = Float.parseFloat(tokenizer.nextToken());

        /* 치명타 발생 확률 */
        float criticalAppearProbRate = Float.parseFloat(removePercentage(tokenizer.nextToken()));
        //System.out.println("치명타 발생확률 값 : " + criticalAppearProbRate);

        /* 치명뎀 */
        float criticalDamageRate = Float.parseFloat(removePercentage(tokenizer.nextToken()));
        //System.out.println("치명타 데미지 값 : " + criticalDamageRate);


        /* 이동속도 증가율 */
        float moveSpeedIncrRate = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /* 공격속도 증가율 */
        float attackSpeedIncrRate = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /* 흡혈률 */
        float bloodSuckingRate = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /* 치명추가뎀 */
        float criticalBonusDamRate = Float.parseFloat(removePercentage(tokenizer.nextToken()));
        //System.out.println("치명추가댐 값 : " + criticalBonusDamRate);

        /* 빙결시간 */
        float freezingTime = Float.parseFloat(tokenizer.nextToken());

        /* 최대체력 증가율 */
        float maxHPIncrRate = Float.parseFloat(removePercentage(tokenizer.nextToken()));


        /**
         * 작성날짜 : 2020 05 20 수
         * 작성내용 :
         *      -- 스킬별, 스킬 레벨별, 스탯별 적용 계수값
         */

        /* 공격력 계수 */
        float attackDamageCoefficient = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /* 방어력 계수 */
        float defenseCoefficient = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /* 공격속도 계수 */
        float attackSpeedCoefficient = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /* 최대체력 계수 */
        float maxHPCoefficient = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /* 최대마력 계수 */
        float maxMPCoefficient = Float.parseFloat(removePercentage(tokenizer.nextToken()));


        /** 생성 */

        float skillCooTime = skillInfoList.get(skillTypeID).skillCoolTime;
        newSkillInfo = new SkillInfoPerLevel(skillLevel, durationTime, skillCooTime,
                attackDamage, skillRange, attackRange,
                flyingObjSpeed,
                moveSpeedIncrRate, attackSpeedIncrRate, bloodSuckingRate, maxHPIncrRate, freezingTime,
                criticalAppearProbRate, criticalDamageRate,criticalBonusDamRate,
                attackDamageCoefficient, defenseCoefficient, attackSpeedCoefficient, maxHPCoefficient, maxMPCoefficient,
                maxHeight, effectDurationTime);

        return newSkillInfo;
    }

    /**
     * 퍼센테이지 값을 갖는 문자열들에서 '%'를 제거해줌.
     * 일부 float 값들을 파싱할 때에 사용.
     * @param str
     * @return
     */
    public static String removePercentage(String str){

        if(str.contains("%")){
            str = str.substring(0, str.indexOf("%"));
        }

        return str;
    }



    public static void readSystemEffectTypeList(){

        systemEffectTypeList = new HashMap<>();
        systemEffectTypeList.put("WELL", SystemEffectType.WELL);
        systemEffectTypeList.put("SELF_RECOVERY", SystemEffectType.SELF_RECOVERY);


    }

    /**
     * 스킬, 아이템, 포탑, 정글몬스터 사냥시 버프 등등
     * 각각이 발생시키는 효과 목록을 모두 읽어들임.
     */
    public static void readEffectInfo(){

        /** 0. 초기화 */
        effectInfoList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.WHOLE_EFFECT_VALUE_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;

            HashMap<Integer, HashMap<String, BuffInfo>> effectInfoPerCauseTypeList;
            HashMap<String, BuffInfo> effectInfoPerCauseList;
            BuffInfo buffInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* 효과 원인 타입을 얻는다. */
                    String effectCauseTypeStr = tokenizer.nextToken();
                    int effectCauseTypeID = getEffectCauseTypeByParsingString(effectCauseTypeStr);

                    /* 해당 효과원인의 목록이 있다면 가져오고, 그렇지 않으면 새로 생성한다 */
                    if(effectInfoList.containsKey(effectCauseTypeID)){

                        effectInfoPerCauseTypeList = effectInfoList.get(effectCauseTypeID);
                    }
                    else{

                        effectInfoPerCauseTypeList = new HashMap<>();
                        effectInfoList.put(effectCauseTypeID, effectInfoPerCauseTypeList);
                    }


                    /* 효과 발생자 타입 읽음 */
                    String effectCauseStr = tokenizer.nextToken();
                    int effectCause;
                    switch (effectCauseTypeID){

                        case EffectCauseType.SKILL :
                            effectCause = skillTypeList.get(effectCauseStr);
                            break;
                        case EffectCauseType.ITEM :
                            effectCause = itemList.get(effectCauseStr);
                            break;
                        case EffectCauseType.TURRET :
                            effectCause = turretList.get(effectCauseStr);
                            break;
                        case EffectCauseType.MONSTER :
                            effectCause = monsterActionList.get(effectCauseStr);
                            break;
                        case EffectCauseType.JUNGLE :
                            effectCause = jungleMonsterList.get(effectCauseStr);
                            break;
                        case EffectCauseType.SYSTEM :
                            effectCause = systemEffectTypeList.get(effectCauseStr);
                            break;


                        default:
                            /**
                             * 수정 필요, 다른 것들도.. 타입(?) 구하는 함수 추가해서.
                             */
                            effectCause = EffectCauseType.NONE;
                            break;

                    }

                    /* 해당 효과원인의 목록이 있다면 가져오고, 그렇지 않으면 새로 생성한다 */
                    if(effectInfoPerCauseTypeList.containsKey(effectCause)){

                        effectInfoPerCauseList = effectInfoPerCauseTypeList.get(effectCause);
                    }
                    else{

                        effectInfoPerCauseList = new HashMap<>();
                        effectInfoPerCauseTypeList.put(effectCause, effectInfoPerCauseList);
                    }

                    /* 효과 타입을 읽음 */
                    String effectType = tokenizer.nextToken();

                    /* 효과 정보를 생성 */
                    buffInfo = createEffectBuffInfo(effectCauseTypeID, effectCause, effectType, tokenizer);

                    effectInfoPerCauseList.put(effectType, buffInfo);
                    buffInfo.printEffectInfo();

                    System.out.println(effectCause + "의 " + effectType + "효과 데이터를 파싱했습니다.");

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static BuffInfo createEffectBuffInfo(int effectCauseType, int effectCause, String effectType, StringTokenizer tokenizer){

        BuffInfo newBuffInfo;

        /** 파싱 */

        /* 실제 효과 ID */
        int effectTypeID = getEffectTypeByParsingString(effectType);

        /* 적중 타입 */
        int effectOnHitType = getOnHitTypeByParsingString(tokenizer.nextToken());

        /* 효과 지속 시간 */
        float effectDurationTime;
        String effectDurationTimeStr = tokenizer.nextToken();
        if(effectDurationTimeStr.contains("스킬")){
            effectDurationTime = 0f;
        }
        else{
            //System.out.println("스킬이 아님..");
            effectDurationTime = Float.parseFloat(effectDurationTimeStr);
        }

        /* 쿨타임(도트효과인 경우) */
        float coolTime = Float.parseFloat(tokenizer.nextToken());

        /* 남은 쿨타임( 적중 대기 시간) */
        float remainCoolTime = Float.parseFloat(tokenizer.nextToken());

        /* 효과값 */
        float effectValue;
        String effectValueStr = tokenizer.nextToken();
        if(effectValueStr.contains("스킬") || effectValueStr.contains("공격력") || effectValueStr.contains("스탯")){
            effectValue = 0f;
        }
        else if(effectValueStr.contains("FALSE")){
            effectValue = -1f;
        }
        else if(effectValueStr.contains("TRUE")){
            effectValue = 1f;
        }
        else{
            effectValue = Float.parseFloat(removePercentage(effectValueStr));
        }


        /** 생성 */
        newBuffInfo = new BuffInfo(effectCauseType, effectCause,
                effectType, effectTypeID, effectOnHitType,
                effectDurationTime, coolTime, remainCoolTime, effectValueStr);

        return newBuffInfo;
    }

    public static int getEffectCauseTypeByParsingString(String str){

        int effectCauseType;
        switch (str){

            case "SKILL":
                effectCauseType = EffectCauseType.SKILL;
                break;
            case "ITEM":
                effectCauseType = EffectCauseType.ITEM;
                break;
            case "TURRET":
                effectCauseType = EffectCauseType.TURRET;
                break;
            case "MONSTER":
                effectCauseType = EffectCauseType.MONSTER;
                break;
            case "JUNGLE":
                effectCauseType = EffectCauseType.JUNGLE;
                break;
            case "SYSTEM":
                effectCauseType = EffectCauseType.SYSTEM;
                break;

            default:
                effectCauseType = EffectCauseType.NONE;
                break;

        }

        return effectCauseType;
    }

    /**
     * 적중 타입 구하기
     * @param str
     * @return
     */
    public static int getOnHitTypeByParsingString(String str){

        int onHitType;
        switch (str){

            case "즉발":
                onHitType = EffectApplicationType.즉발;
                break;
            case "지속":
                onHitType = EffectApplicationType.지속;
                break;
            case "도트":
                onHitType = EffectApplicationType.도트;
                break;

            default:
                onHitType = EffectApplicationType.NONE;
                break;

        }

        return onHitType;

    }

    /**
     * 효과 타입 구하기
     * @param str
     * @return
     */
    public static int getEffectTypeByParsingString(String str){

        int effectType;
        switch (str){

            case "이동불가":
                effectType = ConditionType.isDisableMove;
                break;
            case "데미지":
                effectType = ConditionType.damageAmount;
                break;
            case "공격력증가" :
                effectType = ConditionType.attackDamageRate;
                break;
            case "이동속도":
            case "이동속도증가":
                effectType = ConditionType.moveSpeedRate;
                break;
            case "흡혈":
                effectType = ConditionType.bloodSuckingRate;
                break;
            case "공격속도":
            case "공격속도증가":
                effectType = ConditionType.attackSpeedRate;
                break;
            case "최대체력증가":
                effectType = ConditionType.maxHPRate;
                break;
            case "에어본":
                effectType = ConditionType.isAirborne;
                break;
            case "무적":
                effectType = ConditionType.isTargetingInvincible;
                break;
            case "슬로우":
                effectType = ConditionType.moveSpeedRate;
                break;
            case "체력회복":
                effectType = ConditionType.hpRecoveryAmount;
                break;
            case "방어력증가":
                effectType = ConditionType.defenseBonus;
                break;

                /** 2020 05 05 추가*/
            case "방어막":
                effectType = ConditionType.shieldAmount;
                break;
            case "장판 데미지":
                effectType = ConditionType.damageAmount;
                break;
            case "스턴":
                effectType = ConditionType.isStunned;
                break;
            case "빙결":
                effectType = ConditionType.isFreezing;
                break;
            case "헤드샷활성화":
                effectType = ConditionType.isArcherHeadShotActivated;
                break;
            case "크리뎀":
                effectType = ConditionType.criticalDamageAmount;
                break;
            case "치명확률":
                effectType = ConditionType.criticalChanceRate;
                break;
            case "치명데미지증가":
                effectType = ConditionType.criticalDamageRate;
                break;
            case "난사활성화":
                effectType = ConditionType.isArcherFireActivated;
                break;
            case "1차 데미지":
            case "2차 데미지":
            case "보스 1차데미지":
            case "보스 2차데미지":
                effectType = ConditionType.criticalDamageAmount;
                break;
            case "귀환상태":
                effectType = ConditionType.isReturning;
                break;
            case "마력회복":
                effectType = ConditionType.mpRecoveryAmount;
                break;
            case "최대마력증가":
                effectType = ConditionType.maxMPRate;
                break;
            case "추가 데미지" :
                effectType = ConditionType.damageAmount;
                break;

            case "체력회복속도" :
            case "체력회복 속도증가" :
                effectType = ConditionType.hpRecoveryRate;
                break;

            case "마력회복속도" :
            case "마력회복 속도증가" :
                effectType = ConditionType.mpRecoveryRate;
                break;

            case "스킬사용불가" :
                effectType = ConditionType.isDisableSkill;
                break;

            default:
                effectType = -1;
                break;

        }

        return effectType;

    }

    /**
     * 게임 월드 및 규칙 관련된 설정값들을 읽어들임
     */
    public static void readGameWorldOptionData(){


    }

    /**
     * 바리케이드 정보를 읽어들임
     */
    public static void readBarricadeInfo(){

        /** 0. 초기화 */
        barricadeInfoList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.BARRICADE_INFO_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;
            BarricadeInfo barricadeInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* id를 얻는다. */
                    String id = tokenizer.nextToken();
                    int barricadeID = Integer.parseInt(id);

                    barricadeInfo = createBarricadeInfo(barricadeID, tokenizer) ;
                    barricadeInfoList.put(barricadeID, barricadeInfo);

                    System.out.println("바리케이드 " + barricadeInfo.barricadeName + "의 정보를 파싱합니다.");


                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static BarricadeInfo createBarricadeInfo(int barricadeID, StringTokenizer tokenizer){

        BarricadeInfo newBarricade;

        /** 파싱 */

        /* 이름(타입) */
        String barricadeName = tokenizer.nextToken();

        /* 바리케이드 최대 체력 */
        float barricadeHP = Float.parseFloat(tokenizer.nextToken());

        /* 터렛 체력 회복량 */
        float hpReceveryValue = Float.parseFloat(tokenizer.nextToken());

        /* 공격력 */   // 공격이랑 싱크를 맞추느라 넣었긴 한데, 0임.
        float attackDamage = Float.parseFloat(tokenizer.nextToken());

        /* 공격 속도 */
        float attackSpeed = Float.parseFloat(tokenizer.nextToken());

        /* 방어력 */
        float defense = Float.parseFloat(tokenizer.nextToken());

        /* 사정 거리 */
        float attackRange = Float.parseFloat(tokenizer.nextToken());

        /* 건설 비용 */
        int buildCost = Integer.parseInt(tokenizer.nextToken());

        /* 건설 시간*/
        float buildTime = Float.parseFloat(tokenizer.nextToken());


        /** 생성*/
        newBarricade = new BarricadeInfo(barricadeID, barricadeName,
                barricadeHP, hpReceveryValue, attackRange, defense, buildTime, buildCost);

        return newBarricade;

    }


    /**
     * 상점에서 판매하는 목록정보를 읽어들임
     * 아이템 및 업그레이드..
     * 나중에 봐서, 업그레이드는 빼던가 할 것.
     */
    public static void readStoreSaleInfo(){

        /** 0. 초기화 */
        itemInfoList = new HashMap<>();
        itemList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.STORE_SALES_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;
            ItemInfo newItem;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* 판매 타입을 얻는다 */
                    String salesType = tokenizer.nextToken();
                    if(salesType.contains("ITEM")){

                        /* id를 얻는다. */
                        String id = tokenizer.nextToken();
                        int itemID = Integer.parseInt(id);

                        newItem = createItemInfo(itemID, tokenizer) ;
                        itemInfoList.put(itemID, newItem);
                        itemList.put(newItem.itemName, itemID);

                        System.out.println("아이템 " + newItem.itemName + "의 정보를 파싱합니다.");

                    }

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ItemInfo createItemInfo(int itemType, StringTokenizer tokenizer){

        ItemInfo itemInfo;

        /** 파싱*/

        /* 아이템 이름 */
        String itemName = tokenizer.nextToken();

        /* 타입*/
        String itemTypeStr = tokenizer.nextToken();

        /* 가격 */
        int itemCost = Integer.parseInt(tokenizer.nextToken());


        /** 생성*/
        itemInfo = new ItemInfo(itemType, itemTypeStr, itemCost);


        return itemInfo;

    }



    /**
     * 상점에서 판매하는 업그레이드 목록을 읽어들임
     * (스킬 레벨별 정보를 읽어들이는 것과 비슷한 맥락)
     */
    public static void readStoreUpgradeInfo() {

        /** 0. 초기화 */
        upgradeList = new HashMap<>();
        storeUpgradeInfoPerLevelList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.STORE_UPGRADE_LIST_PER_LEVEL);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;

            HashMap<Integer, StoreUpgradeInfoPerLevel> upgradeInfoPerLevel;  // 한 스킬타입에 대해, 레벨별(1 ~ ) 스킬정보를 담을 것이다.
            StoreUpgradeInfoPerLevel upgradeInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* 업그레이드 타입 및 ID를 얻는다. */
                    int upgradeTypeID = Integer.parseInt(tokenizer.nextToken());

                    String upgradeType = tokenizer.nextToken();
                    String upgradeTypeName = tokenizer.nextToken();

                    /* 해당 타입의 스킬레벨정보가 목록에 들어있다면 가져오고, 그렇지 않으면 새로 생성한다 */
                    if(storeUpgradeInfoPerLevelList.containsKey(upgradeTypeID)){

                        upgradeInfoPerLevel = storeUpgradeInfoPerLevelList.get(upgradeTypeID);
                    }
                    else{

                        upgradeInfoPerLevel = new HashMap<>();
                        storeUpgradeInfoPerLevelList.put(upgradeTypeID, upgradeInfoPerLevel);
                    }

                    /* 업글 레벨을 읽음 */
                    int upgradeLevel = Integer.parseInt(tokenizer.nextToken());

                    /* 레벨별 스킬 정보를 생성 */
                    upgradeInfo = createUpgradeInfo(upgradeTypeID, upgradeTypeName, upgradeLevel, tokenizer);

                    upgradeInfoPerLevel.put(upgradeLevel, upgradeInfo);

                    System.out.println("업그레이드 타입 " + upgradeType + "의 " + upgradeLevel + "레벨 데이터를 파싱했습니다.");

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StoreUpgradeInfoPerLevel createUpgradeInfo(int upgradeTypeID, String upgradeName, int upgradeLevel, StringTokenizer tokenizer){

        StoreUpgradeInfoPerLevel upgradeInfo;

        /** 파싱 */

        /* 강화 가격 */
        int upgradeCost = Integer.parseInt(tokenizer.nextToken());

        /* 강화 효과값 */
        float effectValue = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /** 생성 */
        upgradeInfo = new StoreUpgradeInfoPerLevel(upgradeTypeID, upgradeName, upgradeLevel, upgradeCost, effectValue);

        return upgradeInfo;
    }


    /**
     * 색상속성에 따른 상성 목록 정보를 읽어들임
     */
    public static void readElementalSynastryInfo(){

        /** 0. 초기화 */
        elementalTypeList = new HashMap<>();
        elementalSynastryInfoList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.ELEMENTAL_SYNASTRY_INFO_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;
            ItemInfo newItem;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                HashMap<Integer, Integer> synastryLsit = new HashMap<>();

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* id를 얻는다. */
                    String id = tokenizer.nextToken();
                    int elementalID = Integer.parseInt(id);

                    /* 속성명을 얻는다 */
                    String elementalType = tokenizer.nextToken();
                    System.out.println(elementalType);

                    elementalTypeList.put(elementalType, elementalID);

                    for (int i=1; i<=8; i++){   // 총 속성 타입이 8개라서

                        /* 상성값을 읽어들인다 */
                        String synastryType = tokenizer.nextToken();
                        if (synastryType.contains("-")){
                            synastryLsit.put(i, AttributeSynastryType.EQUAL);
                            //System.out.println(synastryType + " : " + synastryLsit.get(i));
                        }
                        else {

                            synastryLsit.put(i, synastryEffectList.get(synastryType));
                            //System.out.println(synastryType + " : " + synastryLsit.get(i));

                        }

                    }

                    elementalSynastryInfoList.put(elementalID, synastryLsit);

                    System.out.println("속성타입 " + elementalType + "의 상성 데이터를 파싱했습니다.");

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 상성 효과 종류를 읽어들임
     */
    public static void readSynastryEffectInfo(){

        /** 0. 초기화 */
        synastryEffectList = new HashMap<>();
        synastryEffectValueList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.SYNASTRY_EFFECT_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;
            ItemInfo newItem;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* id를 얻는다. */
                    String id = tokenizer.nextToken();
                    int synastryTypeID = Integer.parseInt(id);

                    /* 상성타입을 얻는다 */
                    String typeName = tokenizer.nextToken();

                    /* 상성효과 값을 얻는다 */
                    String effectValueStr = tokenizer.nextToken();
                    //System.out.println(effectValueStr);

                    if(effectValueStr.contains("%")){

                        effectValueStr = effectValueStr.substring(0, effectValueStr.indexOf('%'));
                    }
                    float synastryEffectValue = Float.parseFloat(effectValueStr);

                    synastryEffectList.put(typeName, synastryTypeID);
                    synastryEffectValueList.put(synastryTypeID, synastryEffectValue);
                    System.out.println("상성타입 " + typeName + "의 정보를 파싱합니다.");


                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*******************************************************************************************************************/

    /**
     *
     */
    public static void readBalanceInfo(){

        /** 0. 초기화 */
        balanceDataTypeList = new HashMap<>();
        balanceDataInfoList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.BALANCE_DATA_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;
            BalanceData balanceData;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* 밸런스 데이터 타입명을 얻는다 */
                    String typeName = tokenizer.nextToken();

                    /* 타입의 ID값을 찾는다 */
                    int typeID = getBalanceDataTypeByParsingString(typeName);

                    /* 밸런스 데이터 객체를 생성한다 */
                    balanceData = createBalanceInfo(typeID, typeName, tokenizer);

                    balanceDataTypeList.put(balanceData.balanceDataName, balanceData.balanceDataType);
                    balanceDataInfoList.put(balanceData.balanceDataType, balanceData);
                    System.out.println("밸런스 데이터 " + typeName + "(" + typeID + ")" +"의 정보를 파싱합니다.");


                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    public static BalanceData createBalanceInfo(int balanceDataType, String typeName, StringTokenizer tokenizer){

        BalanceData balanceData;

        /** 파싱 */

        /* 최대 레벨 */
        int maxLevel;
        String maxLevelStr = tokenizer.nextToken();
        if(maxLevelStr.contains("-")){
            maxLevel = 0;
        }
        else{
            maxLevel = Integer.parseInt(maxLevelStr);
        }


        /* 조정치 */
        float adjustmentValue = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /* 가중치 */
        float weightValue = Float.parseFloat(removePercentage(tokenizer.nextToken()));

        /** 생성 */
        balanceData = new BalanceData(balanceDataType, typeName, maxLevel, adjustmentValue, weightValue);

        return balanceData;
    }


    /**
     *
     * @param str
     * @return
     */
    public static int getBalanceDataTypeByParsingString(String str){

        int balanceType;
        switch (str){

            case "REWARD_EXP_PER_MONSTER_LEVEL":
                balanceType = BalanceDataType.REWARD_EXP_PER_MONSTER_LEVEL;
                break;
            case "REWARD_GOLD_PER_MONSTER_LEVEL":
                balanceType = BalanceDataType.REWARD_GOLD_PER_MONSTER_LEVEL;
                break;
            case "EXP_FOR_CHARACTER_LEVEL_UP":
                balanceType = BalanceDataType.EXP_FOR_CHARACTER_LEVEL_UP;
                break;
            case "BARRICADE_UPGRADE_HP":
                balanceType = BalanceDataType.BARRICADE_UPGRADE_HP;
                break;
            case "BARRICADE_UPGRADE_DEFENSE":
                balanceType = BalanceDataType.BARRICADE_UPGRADE_DEFENSE;
                break;
            case "BARRICADE_UPGRADE_COST_COLD":
                balanceType = BalanceDataType.BARRICADE_UPGRADE_COST_COLD;
                break;
            case "CHAR_DEFEAT_TIME":
                balanceType = BalanceDataType.CHAR_DEFEAT_TIME;
                break;

            default:
                balanceType = BalanceDataType.NONE;
                break;

        }

        return balanceType;

    }


    /**
     *
     */
    public static void readGameDifficultyGradeInfo(){

        /** 0. 초기화 */
        gameDifficultyGradeList = new HashMap<>();
        gameDifficultyGradeInfoList = new HashMap<>();

        /** 1. 파일 참조를 얻는다. */
        String filePath = filePathList.get(FilePathType.GAME_DIFFICULTY_GRADE_LIST);
        File file = new File(filePath);
        boolean isExist = file.exists();
        if(!isExist){
            System.out.println(file.getName() + "파일이 존재하지 않습니다. 데이터를 읽어들이는 데 실패했습니다. ");
            return;
        }


        /** 2. 파일 내용을 한 줄 한 줄 읽어 처리한다 */
        FileReader fr;
        BufferedReader br;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String readStr;
            StringTokenizer tokenizer;
            GameDifficultyGradeInfo gradeInfo;

            readStr = br.readLine();    // 한 줄을 미리 읽어, 헤더를 제거한다.
            while (true){

                /* 파일의 끝에 도달하면 멈춘다 */
                readStr = br.readLine();
                if(readStr == null){
                    break;
                }

                /* 토크나이징 */
                tokenizer = new StringTokenizer(readStr, ",");
                if(tokenizer.countTokens() > 0){

                    /* 난이도 등급 타입명을 얻는다 */
                    String typeName = tokenizer.nextToken();

                    /* 타입ID를 얻음 */
                    int typeID = getGameGradeTypeByParsingString(typeName);

                    /* 난이도 등급 객체를 생성한다 */
                    gradeInfo = createGameGradeInfo(typeID, tokenizer);

                    gameDifficultyGradeList.put(typeName, typeID);
                    gameDifficultyGradeInfoList.put(gradeInfo.grade, gradeInfo);

                    System.out.println("난이도 등급 " + typeName + "(" + typeID + ")" +"의 정보를 파싱합니다.");


                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public static int getGameGradeTypeByParsingString(String str){

        int gradeType;
        switch (str){

            case "F":
                gradeType = GameDifficultyGrade.GRADE_F;
                break;
            case "E":
                gradeType = GameDifficultyGrade.GRADE_E;
                break;
            case "D":
                gradeType = GameDifficultyGrade.GRADE_D;
                break;
            case "C":
                gradeType = GameDifficultyGrade.GRADE_C;
                break;
            case "B":
                gradeType = GameDifficultyGrade.GRADE_B;
                break;
            case "A":
                gradeType = GameDifficultyGrade.GRADE_A;
                break;
            case "S":
                gradeType = GameDifficultyGrade.GRADE_S;
                break;
            case "SS":
                gradeType = GameDifficultyGrade.GRADE_SS;
                break;
            case "SSS":
                gradeType = GameDifficultyGrade.GRADE_SSS;
                break;
            case "U":
                gradeType = GameDifficultyGrade.GRADE_U;
                break;
            case "R":
                gradeType = GameDifficultyGrade.GRADE_R;
                break;

            default:
                gradeType = GameDifficultyGrade.NONE;
                break;

        }

        return gradeType;

    }



    /**
     *
     */
    public static GameDifficultyGradeInfo createGameGradeInfo(int grade, StringTokenizer tokenizer){

        GameDifficultyGradeInfo gradeInfo;

        /** 파싱 */

        /* 전투력 MIN */
        float minStrength = Float.parseFloat(tokenizer.nextToken());

        /* 전투력 MAX */
        float maxStrength = Float.parseFloat(tokenizer.nextToken());

        /* 등장 몬스터 레벨 MIN */
        int minLevel = Integer.parseInt(tokenizer.nextToken());

        /* 등장 몬스터 레벨 MAX */
        int maxLevel = Integer.parseInt(tokenizer.nextToken());

        /* 게임 난이도에 따른, 몬스터 스탯 적용 비율 */
        float monsterStatRate = Float.parseFloat(removePercentage(tokenizer.nextToken()));


        /** 생성 */
        gradeInfo = new GameDifficultyGradeInfo(grade, minStrength, maxStrength, minLevel, maxLevel, monsterStatRate);


        return gradeInfo;

    }


    public static int getJungleTypeByParsingString(String str){

        int jungleType;
        switch (str){

            case "LIZARD":
                jungleType = JungleMobType.LIZARD;
                break;
            case "FAIRY":
                jungleType = JungleMobType.FAIRY;
                break;
            case "DRAGON":
                jungleType = JungleMobType.DRAGON;
                break;
            case "DEVIL":
                jungleType = JungleMobType.DEVIL;
                break;

            default:
                jungleType = JungleMobType.NONE;
                break;

        }

        return jungleType;



    }
}
