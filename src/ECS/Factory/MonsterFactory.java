package ECS.Factory;

import ECS.Classes.*;
import ECS.Classes.Type.*;
import ECS.Classes.Type.Jungle.JungleMobType;
import ECS.Components.*;
import ECS.Entity.*;
import ECS.Game.GameDataManager;
import ECS.Game.WorldMap;
import com.sun.org.apache.xerces.internal.dom.DeferredCDATASectionImpl;

import java.util.HashMap;
import java.util.Random;

/**
 * 생성 : 2019년 11월 15일
 * 작성중,,
 * 업뎃 : 2020 02 27 권령희
 *
 *
 * 몬스터(MonsterEntity) 객체 생성을 위한 클래스다.
 * 월드맵매니저 초기화 작업 시 생성해줘야 할듯?
 *
 * 몬스터정보를 담고있는 CSV파일을 읽어들인다.
 * [ 몬스터ID : 몬스터앤티티 객체] 쌍을 갖도록 할지, 아니면 파일에서 통짜로 읽어온 구조체를 갖도록 할지를 아직 완전히 결정되지 않았다
 * 일단은 별도 구조체(클래스)를 사용하는 식으로..
 *
 * 시스템으로부터 몬스터 생성 요청을 받으면, MonsterID를 건네받아
 * 해당하는 몬스터 객체를 생성해 돌려준다.
 *
 * ==============================================================
 *  1. CSV파일로부터 정보를 읽어, < MonsterID, MonsterInfo > 맵을 만든다.
 *      ㄴ 일단은 요 함수를 호출할 경우, 파일 내 하드코딩된 값들을 리스트에 추가해주는 처리를 하도록 작성할 것이다.
 *
 *  2. 위 MonsterInfo 맵을 읽어서, < MonserID, MonsterEntity > 맵을 만든다.
 *      ㄴ 이 처리를 작성하는게 현재 핵심.. 좀 많이 길어질 듯.
 *      ㄴ 앤티티 생성에 관여하는 각종 컴포넌트 및 클래스들의 생성자, 초기값 등을 잘 정해줘야 할듯..
 *
 *  3. 실제 몬스터Entity의 생성 요청이 들어왔을 때, 위에서 생성된 Entity 목록에서 해당하는 몹을 찾아 clone()해준다.
 *
 */
public class MonsterFactory {

    /** 멤버 변수 */
    public static HashMap<Integer, MonsterInfo> monsterInfoTable;
    public static HashMap<Integer, MonsterEntity> monsterEntityTable;

    /* 2020 02 28 권령희 추가, 정글 몬스터 */
    public static HashMap<Integer, MonsterInfo> jungleMonsterInfoTable;
    public static HashMap<Integer, MonsterEntity> jungleMonsterEntityTable;

    // 2020 04 03 금
    /* 몬스터 공격 종류에 따른 효과 목록 */
    public static HashMap<Integer, HashMap<String, BuffInfo>> monsterAttackEffectInfoLIST;



    public static void initFactory() {
        System.out.println("MonsterFactory 초기화중...");

        monsterInfoTable = new HashMap<>();
        monsterEntityTable = new HashMap<>();

        /* 파일을 읽어, 위 테이블을 채우는 처리를 작성할 것 */
        /* 위 처리 로직이 완성되기 전까지는, 필요한 스킬을 하드코딩해서 테이블에 넣어줄 것 */

        /** 파일로부터 정보 읽기 */
        readMonsterInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        createMonsterEntityFromInfo();


        /* 2020 02 28 권령희 추가, 정글 몬스터 */
        jungleMonsterInfoTable = new HashMap<>();
        jungleMonsterEntityTable = new HashMap<>();

        readJungleMonsterInfoFromFile();
        createJungleMonsterEntityFromInfo();

        monsterAttackEffectInfoLIST = GameDataManager.effectInfoList.get(EffectCauseType.MONSTER);

        System.out.println("MonsterFactory 초기화 완료");
    }


    /** 매서드 목록 */


    /**
     * 일단 하드코딩으로 값 채움.
     * 파일 구조 및 파일 읽어들이는 방법 결정되면 수정할 것.
     */
    public static void readMonsterInfo(){

        MonsterInfo monsterInfo;
        // int monIDCount = 1;

        /* 고블린 */
        monsterInfo
                = new MonsterInfo(MonsterType.GOBLIN, GradeType.NORMAL, AttributeType.BLUE,
                "Goblin", 1000f, 0f,
                35f, 0.625f, 2.5f,
                0f, 10f, 2.5f * 2f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);



/*


        */
/* 고블린 파이터 *//*

        monsterInfo
                = new MonsterInfo(2, "Goblin Fighter",
                250f, 25f,
                300f, 40f, 0.625f, 150f,
                3f, 150 * 4f, 250f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* 고블린 axe(?) *//*

        monsterInfo
                = new MonsterInfo(3, "Goblin Axe",
                300f, 30f,
                300f, 45f, 0.625f, 180f,
                5f, 180 * 4f, 200f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* 고블린 bomb *//*

        monsterInfo
                = new MonsterInfo(4, "Goblin Bomb",
                200f, 20f,
                300f, 100f, 0.625f, 150f,
                0f, 150 * 4f, 300f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* goblinshman (?) *//*

        monsterInfo
                = new MonsterInfo(5, "Goblinshman",
                200f, 20f,
                300f, 30f, 0.625f, 600f,
                0f, 600 * 4f, 200f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* 오크(Orc) *//*

        monsterInfo
                = new MonsterInfo(6, "Orc",
                300f, 30f,
                300f, 30f, 0.625f, 150f,
                3f, 150 * 4f, 250f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* Orc Sword *//*

        monsterInfo
                = new MonsterInfo(7, "Orc Sword",
                400f, 40f,
                300f, 45f, 0.625f, 180f,
                3f, 180 * 4f, 250f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* Orc Axe *//*

        monsterInfo
                = new MonsterInfo(8, "Orc Axe",
                450f, 45f,
                300f, 50f, 0.625f, 180f,
                5f, 180 * 4f, 200f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* Orc Bow *//*

        monsterInfo
                = new MonsterInfo(9, "Orc Bow",
                300f, 30f,
                300f, 30f, 0.625f, 600f,
                1f, 600 * 4f, 250f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* OgreGeneral *//*

        monsterInfo
                = new MonsterInfo(10, "OgreGeneral",
                1000f, 100f,
                300f, 80f, 0.625f, 180f,
                10f, 180 * 4f, 200f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* Mummy *//*

        monsterInfo
                = new MonsterInfo(11, "Mummy",
                500f, 50f,
                300f, 30f, 0.625f, 150f,
                2f, 150 * 4f, 200f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* Mummy Pharaoh *//*

        monsterInfo
                = new MonsterInfo(12, "Mummy Pharaoh",
                800f, 80f,
                300f, 70f, 0.625f, 150f,
                3f, 150 * 4f, 200f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* Mummy Pirate *//*

        monsterInfo
                = new MonsterInfo(13, "Mummy Pirate",
                600f, 60f,
                300f, 55f, 0.625f, 180f,
                3f, 180 * 4f, 230f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* Mummy Pumpkin *//*

        monsterInfo
                = new MonsterInfo(14, "Mummy Pumpkin",
                700f, 70f,
                300f, 50f, 0.625f, 150f,
                5f, 150 * 4f, 200f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        */
/* Mummy Kettle *//*

        monsterInfo
                = new MonsterInfo(15, "Mummy Kettle",
                900f, 90f,
                300f, 60f, 0.625f, 150f,
                8f, 150 * 4f, 250f);

        monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);
*/

    }

    /**
     * 2020 04 01 작성
     * GDM가 파일로부터 읽어들인 정보를 가져와 목록을 구성한다
     */
    public static void readMonsterInfoFromFile(){

        MonsterInfo monsterInfo;

        for ( HashMap.Entry<Integer, MonsterInfo> monsterInfoEntry
                : GameDataManager.monsterInfoList.entrySet()){

            monsterInfo = monsterInfoEntry.getValue();

            monsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        }

        monsterInfoTable = GameDataManager.monsterInfoList;

    }


    /**
     * <MonsterID, MonsterInfo> 맵의 info를 하나씩 읽어, MonsterEntity 객체로 만들어준다.
     * 여기서 쓰이는 MonsterEntity는, 각 월드의 실제 몹 생성을 위한 틀로써 존재한다.
     * 모든 게임월드가 공유함.
     *
     */
    public static void convertMonsterInfoToEntity(){

        for( HashMap.Entry<Integer, MonsterInfo> monsterInfoEntry : monsterInfoTable.entrySet() ){

            MonsterInfo monsterInfo = monsterInfoEntry.getValue();

            /** 몬스터 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Monster Component */
            MonsterComponent monsterComponent
                    = new MonsterComponent(monsterInfo.monsterType, monsterInfo.monsterName);

            /* Position Component */
            // 여기에 몬스터 (생성) 위치를 지정해줄 Vector3 클래스를 만들어서 넣어줄 수도 있을듯
            PositionComponent positionComponent
                    = new PositionComponent(11, 0f, -19);

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(monsterInfo.maxHP, monsterInfo.recoveryRateHP);

            /* Attack Component */
            AttackComponent attackComponent
                    = new AttackComponent( monsterInfo.attackDamage, monsterInfo.attackSpeed, monsterInfo.attackRange);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(monsterInfo.defense);

            /* Sight Component */
            SightComponent sightComponent
                    = new SightComponent(monsterInfo.lookRadius);

            /* Rotation Component */
            // 하드코딩. Info에는 들어있지 않음
            RotationComponent rotationComponent
                    = new RotationComponent(0f, 0f);

            /* Velocity Component */
            VelocityComponent velocityComponent
                    = new VelocityComponent(monsterInfo.moveSpeed);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();

            /** 생성된 컴포넌트들을 가지고, 몬스터 Entity 객체를 만든다 */

            /* Monster Entity */
            int entityID = 0;   // ?? 어케 지정해주지.. 어디서 지정해주지
            MonsterEntity monsterEntity
                    = new MonsterEntity(positionComponent, monsterComponent, hpComponent,
                            attackComponent, defenseComponent, sightComponent,
                            rotationComponent, velocityComponent,
                            buffActionHistoryComponent, hpHistoryComponent, conditionComponent);

            monsterEntity.attribute = monsterInfo.monsterElemental;

            /** 목록에 추가 */

            monsterEntityTable.put(monsterInfo.monsterType, monsterEntity);

        }

    }

    public static void createMonsterEntityFromInfo(){

        for( HashMap.Entry<Integer, MonsterInfo> monsterInfoEntry : monsterInfoTable.entrySet() ){

            MonsterInfo monsterInfo = monsterInfoEntry.getValue();

            /** 몬스터 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Monster Component */
            MonsterComponent monsterComponent
                    = new MonsterComponent(monsterInfo.monsterType, monsterInfo.monsterName);

            /* Position Component */
            // 여기에 몬스터 (생성) 위치를 지정해줄 Vector3 클래스를 만들어서 넣어줄 수도 있을듯
            PositionComponent positionComponent
                    = new PositionComponent(new Vector3(0f, 0f, 0f));

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(monsterInfo.maxHP, monsterInfo.recoveryRateHP);

            /* Attack Component */
            AttackComponent attackComponent
                    = new AttackComponent( monsterInfo.attackDamage, monsterInfo.attackSpeed, monsterInfo.attackRange);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(monsterInfo.defense);

            /* Sight Component */
            SightComponent sightComponent
                    = new SightComponent(monsterInfo.lookRadius);

            /* Rotation Component */
            // 하드코딩. Info에는 들어있지 않음
            RotationComponent rotationComponent
                    = new RotationComponent(0f, 0f);

            /* Velocity Component */
            VelocityComponent velocityComponent
                    = new VelocityComponent(monsterInfo.moveSpeed);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();

            /** 생성된 컴포넌트들을 가지고, 몬스터 Entity 객체를 만든다 */

            /* Monster Entity */
            int entityID = 0;   // ?? 어케 지정해주지.. 어디서 지정해주지
            MonsterEntity monsterEntity
                    = new MonsterEntity(positionComponent, monsterComponent, hpComponent,
                    attackComponent, defenseComponent, sightComponent,
                    rotationComponent, velocityComponent,
                    buffActionHistoryComponent, hpHistoryComponent, conditionComponent);

            monsterEntity.attribute = monsterInfo.monsterElemental;

            //System.out.println("몬스터 속성 : " + monsterInfo.monsterElemental);

            /** 목록에 추가 */

            monsterEntityTable.put(monsterInfo.monsterType, monsterEntity);

        }

    }

    /**
     * 업뎃
     * 오전 1:49 2020-04-08
     *
     */
    public static MonsterEntity createMonster(int requestedMonsterID, WorldMap worldMap){

        MonsterEntity newMonster;

        /* 생성 */
        newMonster = (MonsterEntity) ( monsterEntityTable.get(requestedMonsterID) ).clone();
        newMonster.monsterComponent.monsterType %= 3;
        newMonster.monsterComponent.monsterType++;

        /*int monsterType = requestedMonsterID % 3;
        monsterType++;
        newMonster = (MonsterEntity) ( monsterEntityTable.get(monsterType) ).clone();*/

        /* 몬스터의 레벨을 결정한다 */
        int level = decideMonsterLevel(worldMap) + (worldMap.getWaveInfoCount() - 1);
        //level = decideMonsterLevel(worldMap);

        /* 게임 등급에 맞춰, 몬스터의 초기 스탯 비율을 적용한다 */
        applyMonsterStatByGameGrade(worldMap, newMonster);

        /* 플레이 인원 수에 맞춰서 스탯 추가 세팅 */
        if(worldMap.userCount == 2){

            newMonster.hpComponent.originalMaxHp *= 1.5f;
            newMonster.hpComponent.maxHP = newMonster.hpComponent.originalMaxHp;
            newMonster.hpComponent.currentHP = newMonster.hpComponent.originalMaxHp;

            newMonster.attackComponent.attackDamage *= 1.5f;
            newMonster.defenseComponent.defense *= 1.5f;
        }
        else if(worldMap.userCount == 3){

            newMonster.hpComponent.originalMaxHp *= 2f;
            newMonster.hpComponent.maxHP = newMonster.hpComponent.originalMaxHp;
            newMonster.hpComponent.currentHP = newMonster.hpComponent.originalMaxHp;

            newMonster.attackComponent.attackDamage *= 2f;
            newMonster.defenseComponent.defense *= 2f;
        }

        /* 레벨에 맞게, 몬스터의 스탯을 세팅한다 */
        resetMonsterStatByLevel(newMonster, level);

        return newMonster;
    }


    /** 2020 02 28 권령희 추가, 정글몬스터 관련 매서드 */

    public static void readJungleMonsterInfo(){

        MonsterInfo jungleMobInfo;

        /* 인간형1 */
        /*jungleMobInfo
                = new MonsterInfo(JungleMobType.HUMAN1, GradeType.NORMAL, AttributeType.RED,
                "인간형1", 5f, 500f,
                0f, 50f, 0.625f,
                3f, 5f, 10f, 1.5f);
        jungleMonsterInfoTable.put(jungleMobInfo.monsterType, jungleMobInfo);*/

        /* 인간형2 */
        /*jungleMobInfo
                = new MonsterInfo(JungleMobType.HUMAN2, GradeType.NORMAL, AttributeType.RED,
                "인간형2", 5f, 500f,
                0f, 50f, 0.625f,
                3f, 5f, 10f, 1.5f);
        jungleMonsterInfoTable.put(jungleMobInfo.monsterType, jungleMobInfo);*/

        /* 인간형3 */
        /*jungleMobInfo
                = new MonsterInfo(JungleMobType.HUMAN3, GradeType.NORMAL, AttributeType.GREEN,
                "인간형3", 5f, 500f, 0f,
                50f, 0.625f, 3.0f,
                5f, 10f, 1.5f);
        jungleMonsterInfoTable.put(jungleMobInfo.monsterType, jungleMobInfo);*/

        /* 인간형4 */
        /*jungleMobInfo
                = new MonsterInfo(JungleMobType.HUMAN4, GradeType.NORMAL, AttributeType.BLUE,
                "인간형4", 5f,  500f, 0f,
                50f, 0.625f, 3.0f,
                5f, 10f, 1.5f);
        jungleMonsterInfoTable.put(jungleMobInfo.monsterType, jungleMobInfo);*/

        /* 도마뱀 */
        jungleMobInfo
                = new MonsterInfo(JungleMobType.LIZARD, GradeType.RARE, AttributeType.YELLOW,
                "도마뱀", 5f, 1000f, 0f,
                100f, 0.625f, 3.0f,
                10f, 10f, 3f);
        jungleMonsterInfoTable.put(jungleMobInfo.monsterType, jungleMobInfo);

        /* 요정 */
        jungleMobInfo
                = new MonsterInfo(JungleMobType.FAIRY, GradeType.RARE, AttributeType.PINK,
                "요정", 5f, 1000f, 0f,
                100f, 0.625f, 3.0f,
                10f, 10f, 3f);
        jungleMonsterInfoTable.put(jungleMobInfo.monsterType, jungleMobInfo);

        /* 용 */
        jungleMobInfo
                = new MonsterInfo(JungleMobType.DRAGON, GradeType.UNIQUE, AttributeType.SKY_BLUE,
                "용", 5f, 3000f, 0f,
                300f, 0.625f, 3.0f,
                30f, 10f, 5f);
        jungleMonsterInfoTable.put(jungleMobInfo.monsterType, jungleMobInfo);

        /* 악마 */
        jungleMobInfo
                = new MonsterInfo(JungleMobType.DEVIL, GradeType.UNIQUE, AttributeType.SKY_BLUE,
                "악마", 5f, 3000f, 0f,
                300f, 0.625f, 3.0f,
                30f, 10f, 5f);
        jungleMonsterInfoTable.put(jungleMobInfo.monsterType, jungleMobInfo);

    }

    /**
     * 2020 04 01 작성
     *
     */
    public static void readJungleMonsterInfoFromFile(){

        MonsterInfo monsterInfo;

        for( HashMap.Entry<Integer, MonsterInfo> jungleMonsterInfoEntry
                : GameDataManager.jungleMonsterInfoList.entrySet()){

            monsterInfo = jungleMonsterInfoEntry.getValue();
            jungleMonsterInfoTable.put(monsterInfo.monsterType, monsterInfo);

        }

        jungleMonsterInfoTable = GameDataManager.jungleMonsterInfoList;

    }

    public static void convertJungleMonsterInfoToEntity(){

        for( HashMap.Entry<Integer, MonsterInfo> monsterInfoEntry : jungleMonsterInfoTable.entrySet() ){

            MonsterInfo monsterInfo = monsterInfoEntry.getValue();

            /** 몬스터 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Monster Component */
            MonsterComponent monsterComponent
                    = new MonsterComponent(monsterInfo.monsterType, monsterInfo.monsterName);

            /* Position Component */
            PositionComponent positionComponent
                    = new PositionComponent(new Vector3());

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(monsterInfo.maxHP, monsterInfo.recoveryRateHP);

            /* Attack Component */
            AttackComponent attackComponent
                    = new AttackComponent( monsterInfo.attackDamage, monsterInfo.attackSpeed, monsterInfo.attackRange);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(monsterInfo.defense);

            /* Sight Component */
            SightComponent sightComponent
                    = new SightComponent(monsterInfo.lookRadius);

            /* Rotation Component */
            // 하드코딩. Info에는 들어있지 않음
            RotationComponent rotationComponent
                    = new RotationComponent(0f, 0f);

            /* Velocity Component */
            VelocityComponent velocityComponent
                    = new VelocityComponent(monsterInfo.moveSpeed);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();

            /** 생성된 컴포넌트들을 가지고, 몬스터 Entity 객체를 만든다 */

            /* Monster Entity */
            int entityID = 0;   // ?? 어케 지정해주지.. 어디서 지정해주지
            MonsterEntity monsterEntity
                    = new MonsterEntity(positionComponent, monsterComponent, hpComponent,
                    attackComponent, defenseComponent, sightComponent,
                    rotationComponent, velocityComponent,
                    buffActionHistoryComponent, hpHistoryComponent, conditionComponent);

            monsterEntity.attribute = monsterInfo.monsterElemental;

            monsterEntity.team = Team.JUNGLE;

            /** 목록에 추가 */

            jungleMonsterEntityTable.put(monsterInfo.monsterType, monsterEntity);

        }

    }

    /**
     * 2020 04 01 작성
     */
    public static void createJungleMonsterEntityFromInfo(){

        for( HashMap.Entry<Integer, MonsterInfo> monsterInfoEntry : jungleMonsterInfoTable.entrySet() ){

            MonsterInfo monsterInfo = monsterInfoEntry.getValue();

            /** 몬스터 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Monster Component */
            MonsterComponent monsterComponent
                    = new MonsterComponent(monsterInfo.monsterType, monsterInfo.monsterName);

            /* Position Component */
            PositionComponent positionComponent
                    = new PositionComponent(new Vector3(0f, 0f, 0f));

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(monsterInfo.maxHP, monsterInfo.recoveryRateHP);

            /* Attack Component */
            AttackComponent attackComponent
                    = new AttackComponent( monsterInfo.attackDamage, monsterInfo.attackSpeed, monsterInfo.attackRange);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(monsterInfo.defense);

            /* Sight Component */
            SightComponent sightComponent
                    = new SightComponent(monsterInfo.lookRadius);

            /* Rotation Component */
            // 하드코딩. Info에는 들어있지 않음
            RotationComponent rotationComponent
                    = new RotationComponent(0f, 0f);

            /* Velocity Component */
            VelocityComponent velocityComponent
                    = new VelocityComponent(monsterInfo.moveSpeed);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();

            /** 생성된 컴포넌트들을 가지고, 몬스터 Entity 객체를 만든다 */

            /* Monster Entity */
            int entityID = 0;   // ?? 어케 지정해주지.. 어디서 지정해주지
            MonsterEntity monsterEntity
                    = new MonsterEntity(positionComponent, monsterComponent, hpComponent,
                    attackComponent, defenseComponent, sightComponent,
                    rotationComponent, velocityComponent,
                    buffActionHistoryComponent, hpHistoryComponent, conditionComponent);

            monsterEntity.attribute = monsterInfo.monsterElemental;

            monsterEntity.team = Team.JUNGLE;

            /** 목록에 추가 */

            jungleMonsterEntityTable.put(monsterInfo.monsterType, monsterEntity);

        }

    }

    public static MonsterEntity createJungleMonster(int requestedJungleMonsterType, WorldMap worldMap, int regenCount){

        MonsterEntity newJungleMob;

        /* 생성 */
        newJungleMob = (MonsterEntity) (jungleMonsterEntityTable.get(requestedJungleMonsterType).clone() );

        /* 몬스터의 레벨을 결정한다 */
        int level = decideMonsterLevel(worldMap);

        /* 게임 등급에 맞춰, 몬스터의 초기 스탯 비율을 적용한다 */
        applyMonsterStatByGameGrade(worldMap, newJungleMob);

        /* 레벨에 맞게, 몬스터의 스탯을 세팅한다 */
        resetJungleMonsterStatByLevel(newJungleMob, level);

        /* 리젠 카운트에 맞게, 몬스터의 스탯 비율을 추가 적용한다 */
        applyMonsterStatByJungleSlotRegenCount(worldMap, newJungleMob, regenCount);

        return newJungleMob;

    }

    /**
     *      Aim : 적용하고자 하는 효과를 생성할 때 호출하면 된다!
     *    Input :
     *   Output :
     *  Process :
     *
     */
    public static BuffAction createMonsterActionEffect(int attackType, String effectName, MonsterEntity monster, int effectEntityID){


        /** 몬스터 앵ㄱ션 효과 목록에서, 생성하고자 하는 effect 를 검색한다 */
        BuffInfo effectInfo = monsterAttackEffectInfoLIST.get(attackType).get(effectName);

        /** 효과의 지속시간을 구한다 (필요하다면) */
        /*
         * 조건 : 효과의 적중 타입이 '지속'이면서 효과정보 객체에 들어있는 지속시간 값이 0 이하인 경우
         *
         * 참고)) 현재, 몬스터 공격에 의한 효과(사실상 그냥 데미지)의 경우, 데미지 한 종류밖에 존재하지 않음..
         *          나중에, 몬스터 공격에 의한 다른 효과 타입 등이 추가될 수 있으므로,
         *          캐릭터 스킬 효과 생성 처리에서 사용했던 틀은 남겨두도록 함.
         */
        float effectDurationTime;
        boolean needToGetDurationTime =
                (( effectInfo.effectAppicationType == EffectApplicationType.지속)
                        && ( effectInfo.effectDurationTime <= 0f)) ? true : false;
        if(needToGetDurationTime){

            effectDurationTime = effectInfo.effectDurationTime;
        }
        else{

            effectDurationTime = effectInfo.effectDurationTime;
        }


        /* 몬스터 공격 종류?? 등등.. 에 따른 예외처리가 필요하다면 이 단락에 작성 */
        /*
         *...
         *
         *
         */


        /** 효과 객체를 생성한다 (틀) */
        // 효과정보 객체에 들어있는 정보를 바탕으로, BuffAction 객체를 생성한다.
        BuffAction newEffect = new BuffAction(attackType, effectDurationTime, effectInfo.remainCoolTime, effectInfo.effectCoolTime);


        /** 효과 내용을 채운다 */
        // BuffAction 객체에, 실제 효과를 부여하기 위한 처리를 한다. 경우에 따라, 공격자 정보를 참조해야 한다.

        int effectType = GameDataManager.getEffectTypeByParsingString(effectName);
        boolean isConditionEffect = checkIsConditionEffect(effectType);
        if(isConditionEffect){

            /* 상태이상을 결정하는 효과 타입인 경우, boolParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionBoolParam conditionEffect = new ConditionBoolParam(effectType, true);
            newEffect.addEffect(conditionEffect);
        }
        else{

            /* 기존 스탯 등에 영향을 미치는 버프 OR 디버프 효과 타입인 경우, floatParam 클래스를 활용해 효과 내용을 채운다 */
            ConditionFloatParam valueEffect = createEffectParam(attackType, effectInfo, monster);
            newEffect.addEffect(valueEffect);

        }

        // 나중에.. 근거리 공격용? 매서드도 하나 만들자..
        newEffect.unitID = effectEntityID;
        newEffect.skillUserID = newEffect.unitID;

        /* Output */
        return newEffect;

    }


    /**
     * 넘겨받은 효과가 상태 이상 타입의 효과인지 여부를 판단하는 매서드
     * @return
     */
    public static boolean checkIsConditionEffect(int effectType){

        boolean isConditionEffect = false;

        switch (effectType){

            case ConditionType.isDisableMove :
            case ConditionType.isDisableAttack :
            case ConditionType.isDisableSkill :
            case ConditionType.isDisableItem :
            case ConditionType.isDamageImmunity :
            case ConditionType.isUnTargetable :

            case ConditionType.isAirborneImmunity :
            case ConditionType.isAirborne :
            case ConditionType.isGarrenQApplied :
            case ConditionType.isTargetingInvincible :
            case ConditionType.isArcherFireActivated :
            case ConditionType.isStunned :
            case ConditionType.isArcherHeadShotActivated :
            case ConditionType.isFreezing :
            case ConditionType.isSlow :
            case ConditionType.isSilence :
            case ConditionType.isBlind :
            case ConditionType.isSightBlocked :
            case ConditionType.isGrounding :
            case ConditionType.isPolymorph :
            case ConditionType.isDisarmed :
            case ConditionType.isSnare :
            case ConditionType.isKnockedAirborne :
            case ConditionType.isKnockback :
            case ConditionType.isSuspension :
            case ConditionType.isTaunt :
            case ConditionType.isCharm :
            case ConditionType.isFlee :
            case ConditionType.isSuppressed :
            case ConditionType.isSleep :
            case ConditionType.isReturning :

                isConditionEffect = true;
                break;

            default:
                isConditionEffect = false;
        }

        return isConditionEffect;
    }

    /**
     * 상태이상이 아닌 타입의 스킬 효과 이펙트를 생성하는 매서드
     *
     * 아 이름짓는거때문에 먼가 통일하고싶은데.. bool 이랑 param 이랑.. 그럴 여유는 없겟지..
     */
    public static ConditionFloatParam createEffectParam(int attackType,  BuffInfo effectInfo, MonsterEntity monster){

        /* Input */
        int effectType = GameDataManager.getEffectTypeByParsingString(effectInfo.effectTypeName);
        String effectValueStr = effectInfo.effectValue;

        /* Output */
        float effectValue = 0f;
        ConditionFloatParam valueEffect;

        /* 효과값을 결정한다 */
        switch (effectValueStr){

            case "공격력" :

                /* 공격자 몬스터의 공격력 값을 가져와 적용한다 */
                effectValue = monster.attackComponent.attackDamage;

                //System.out.println("효과파람 생성 매서드 ; 공격력 타입 ");
                break;

            default :

                effectValue = Float.parseFloat( GameDataManager.removePercentage(effectValueStr) );

                //System.out.println("그 외 ; 이미 값이 정해져 있음. %나 파싱해");
                break;

        }

        /* 예외처리
            ; 일반 '데미지' 타입인 경우, 해당 공격이 평탄지, 크리티컬인지 판정도 거처야 한다. */

        switch (effectType){    // 효과타입Name == "데미지"로 하는게 의미상 더 정확하긴 할텐데..

            case ConditionType.damageAmount :
                valueEffect = SkillFactory.createDamageParam(effectValue, monster.attackComponent, monster.conditionComponent);
                break;

            default:

                valueEffect = new ConditionFloatParam(effectType, effectValue);
                break;
        }


        return valueEffect;

    }


    /**
     * 일단 껍데기만 남겨둠.
     * 몬스터의 경우, 아직까지는 데미지 이외에 별도 버프/디버프/상태이상을 주는 효과가 존재하지 않기 때문에.
     */
    public static float getProperEffectValue(int attackType, String effectName, int effectType){

        return  0f;

    }




    /*******************************************************************************************************************/

    /**
     * 주석 작성
     * 오전 1:35 2020-04-08
     * 기    능 :
     *      -- INPUT 으로 넘겨받은 MonsterEntity 에, 함께 넘겨받은 레벨에 맞도록 스탯값을 적용함
     *      -- entity 에 넘겨받은 레벨값도 적용함.
     *
     * 메    모 :
     *      -- 현재 기획? 기준으로, 레벨에 따라 달라지는 스탯은 총 4개이다.
     *
     *          * 체력, 체력회복, 공격력, 방어력
     *
     */
    public static void resetMonsterStatByLevel(MonsterEntity monster, int level){

        /** 몬스터 정보 참조 */
        MonsterInfo monsterInfo = monsterInfoTable.get(monster.monsterComponent.monsterType);

        HPComponent monsterHP = monster.hpComponent;
        AttackComponent monsterAttack = monster.attackComponent;
        DefenseComponent monsterDefense = monster.defenseComponent;

        monster.monsterComponent.monsterLevel = level;

        /** 레벨에 따른 스탯 값 적용하기 */

        /*System.out.println("레벨 : " + level);
        System.out.println("오리진체력 : " + monsterHP.originalMaxHp);
        System.out.println("레벨별 체력증가 비율 : " + monsterInfo.hpIncrValue);
        */
        if(level == 1)
            return;

        /* 체력 */
        monsterHP.originalMaxHp += monsterInfo.hpIncrValue * (level-1);
        monsterHP.maxHP = monsterHP.originalMaxHp;
        monsterHP.currentHP = monsterHP.originalMaxHp;

        /* 체력 회복 */
        monsterHP.recoveryRateHP += monsterInfo.hpRecoveryIncrValue * (level-1);

        /* 공격력 */
        monsterAttack.attackDamage += monsterInfo.attackDamageIncrValue * (level-1);

        /* 방어력 */
        monsterDefense.defense += monsterInfo.defenseIncrValue * (level-1);
/*

        System.out.println("몬스터 레벨 : " + level);
        System.out.println("몬스터 체력 : " + monsterHP.originalMaxHp);
        System.out.println("몬스터 공격력 : " + monsterAttack.attackDamage);
        System.out.println("몬스터 방어력 : " + monsterDefense.defense);
*/



        return;
    }

    /**
     * 주석 작성
     * 오후 4:55 2020-04-08
     * 기능 :
     *  -- INPUT 으로 넘겨받은 MonsterEntity 에, 함께 넘겨받은 레벨에 맞도록 스탯값을 적용함
     *  -- entity 에 넘겨받은 레벨값도 적용함.
     *
     */
    public static void resetJungleMonsterStatByLevel(MonsterEntity monster, int level){

        /** 몬스터 정보 참조 */
        MonsterInfo monsterInfo = jungleMonsterInfoTable.get(monster.monsterComponent.monsterType);

        HPComponent monsterHP = monster.hpComponent;
        AttackComponent monsterAttack = monster.attackComponent;
        DefenseComponent monsterDefense = monster.defenseComponent;

        /** 레벨에 따른 스탯 값 적용하기 */

        /* 체력 */
        monsterHP.originalMaxHp += monsterInfo.hpIncrValue * (level-1);
        monsterHP.maxHP = monsterHP.originalMaxHp;
        monsterHP.currentHP = monsterHP.originalMaxHp;

        /* 체력 회복 */
        monsterHP.recoveryRateHP += monsterInfo.hpRecoveryIncrValue * (level-1);

        /* 공격력 */
        monsterAttack.attackDamage += monsterInfo.attackDamageIncrValue * (level-1);

        /* 방어력 */
        monsterDefense.defense += monsterInfo.defenseIncrValue * (level-1);


        return;



    }

    /**
     * 주석 작성
     * 오전 1:39 2020-04-08
     * 기능 :
     *  -- 몬스터의 레벨을 랜덤으로 결정하여 넘겨준다.
     *  -- 매번 몬스터를 생성할 때 마다 해당 함수를 호출하여, 생성될 몬스터의 레벨을 정한다
     *  INPUT
     *      -- WorldMap worldMap ; 몹을 생성하려는 월드맵, 해당 월드맵의 게임 난이도 및 기대레벨 값을 참조한다
     *  OUTPUT
     *      -- int level ; 랜덤으로 결정된 레벨 값.
     *
     *  PROCESS
     *      -- 넘겨받은 월드맵을 참조하여, 기대레벨과 표준편차 값을 얻는다.
     *      -- 월드맵에 지정된 게임 난이도값을 얻는다.
     *      -- GDM 의 난이도 정보를 참조하여, 해당 난이도의 최소, 최대 레벨을 얻는다.
     *      -- 정규분포 N(기대레벨, 표준편차)를 활용하여 랜덤값을 하나 생성한다.
     *      -- 생성된 값이 최소,최대 레벨 값의 범위를 벗어날 경우, 값 보정 처리를 한다.
     *          ㄴ 최소값 이하라면 최소값으로, 최대값 이상이라면 최대값으로 설정한다
     *      -- 최종 레벨 값을 리턴한다
     *
     */
    public static int decideMonsterLevel(WorldMap worldMap){

        int level = 0;

        /** 월드로부터, 게임 등급을 참조 */
        int grade = worldMap.gameGrade;
        int expLevel = worldMap.monsterExpLevel;
        float standardDeviation = worldMap.standardDeviation;

        /** GDM 으로부터 난이도 등급 정보를 참조 */
        GameDifficultyGradeInfo gradeInfo = GameDataManager.gameDifficultyGradeInfoList.get(grade);
        int minLevel = gradeInfo.minMonsterLevel;
        int maxLevel = gradeInfo.maxMonsterLevel;


        /** 랜덤 레벨 계산 */
        Random random = new Random();
        level = Math.round( expLevel + (float)(random.nextGaussian() * standardDeviation));


        /** 범위를 벗어낫다면 보정 처리를 한다 */
        if(level < minLevel){
            level = minLevel;
        }
        else if(level > maxLevel){
            level = maxLevel;
        }


        /* 기대 몹 레벨로 고정 생성되게끔 */
        return expLevel;

        /* 기대 몹을 기반으로 한 랜덤 레벨로 생성되게끔 */
        //return level;

    }


    /**
     * 기    능 :게임 등급에 따라, 몬스터 생성 후 초기 스탯 비율을 조절함. (100% ~ )
     *
     *
     */
    public static void applyMonsterStatByGameGrade(WorldMap worldMap, MonsterEntity monster){

        /** 등급 정보를 참조함 */
        GameDifficultyGradeInfo currentDifficultyInfo
                = GameDataManager.gameDifficultyGradeInfoList.get(worldMap.gameGrade);

        float statRate = currentDifficultyInfo.monsterStatRate * 0.01f;

        /** 몬스터 정보 참조 */
        HPComponent monsterHP = monster.hpComponent;
        AttackComponent monsterAttack = monster.attackComponent;
        DefenseComponent monsterDefense = monster.defenseComponent;
        SightComponent monsterSight = monster.sightComponent;
        VelocityComponent monsterMove = monster.velocityComponent;

        /** 비율에 맞게, 스탯을 적용함 */

        /* 체력 */
        monsterHP.originalMaxHp *= statRate;
        monsterHP.maxHP = monsterHP.originalMaxHp;
        monsterHP.currentHP = monsterHP.originalMaxHp;

        /* 체력 회복 */
        monsterHP.recoveryRateHP *= statRate;

        /* 공격력 */
        monsterAttack.attackDamage *= statRate;

        /* 방어력 */
        monsterDefense.defense *= statRate;

        /* 공격속도 */
        monsterAttack.attackSpeed *= statRate;

        /* 이동속도 */
        monsterMove.moveSpeed += statRate;

        /* 사거리 */
        /**
         * 2020 04 28 화요일
         * 몹 생성 시, 게임 난이도 등급에 따라
         * 몬스터의 초기 스탯을 NN% 추가 적용하끔 하는데,
         * 여기서 공격 사거리만 주석처리 해 봄.
         * 시야는.. 멀리서도 타겟을 보고 쫒아가는 거니까 별 문제가 안될 거 같은데
         * 공격 사거리의 경우,,
         * ㄴ 아니근데 이게 문제가 맞는지 모르겠지만
         *      지금까지 플레에한 유저들의 게임 등급이 F를 벗어나지 않았다면 이런 문제는 애초에 발생하지 않았을 건데 말임??
         *       1. 스탯이 문제냐
         *       2. 통신이나 뭐 다른게 문제냐
         *       같은 방에서 플레이할 때에도 두 기기의 중계 상황이 다른데, 이거는 또 뭐 때문이냐
         *
         */
        //monsterAttack.attackRange *= statRate;

        /* 시야 */
        monsterSight.lookRadius += statRate;

   }

    /**
     * 기    능 : 특정 정글 슬롯의 몹 리젠 카운트에 따라, 몬스터 스탯 비율을 조절함. (100% ~ )
     *
     *
     */
    public static void applyMonsterStatByJungleSlotRegenCount(WorldMap worldMap, MonsterEntity monster,int regenCount){

        /** 리젠 횟수를 참조하여, 적용할 스탯 비율을 결정 */
        float appliedStatRate = 0.5f;
        float statRate = (1 + (regenCount * appliedStatRate));

        /** 몬스터 정보 참조 */
        HPComponent monsterHP = monster.hpComponent;
        AttackComponent monsterAttack = monster.attackComponent;
        DefenseComponent monsterDefense = monster.defenseComponent;
        SightComponent monsterSight = monster.sightComponent;
        VelocityComponent monstserMove = monster.velocityComponent;

        /** 비율에 맞게, 스탯을 적용함 */

        /* 체력 */
        monsterHP.originalMaxHp *= statRate;
        monsterHP.maxHP = monsterHP.originalMaxHp;
        monsterHP.currentHP = monsterHP.originalMaxHp;

        /* 체력 회복 */
        monsterHP.recoveryRateHP *= statRate;

        /* 공격력 */
        monsterAttack.attackDamage *= statRate;

        /* 방어력 */
        monsterDefense.defense *= statRate;

        /* 공격속도 */
        monsterAttack.attackSpeed *= statRate;

        /* 이동속도 */
        //monstserMove.moveSpeed *= statRate;

        /* 사거리 */
        //monsterAttack.attackRange *= statRate;

        /* 시야 */
        //monsterSight.lookRadius *= statRate;

    }


}
