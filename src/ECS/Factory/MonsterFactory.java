package ECS.Factory;

import ECS.Classes.*;
import ECS.Classes.Type.*;
import ECS.Classes.Type.Jungle.JungleMobType;
import ECS.Components.*;
import ECS.Entity.*;

import java.util.HashMap;

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


    public static void initFactory() {
        System.out.println("MonsterFactory 초기화중...");

        monsterInfoTable = new HashMap<>();
        monsterEntityTable = new HashMap<>();

        /* 파일을 읽어, 위 테이블을 채우는 처리를 작성할 것 */
        /* 위 처리 로직이 완성되기 전까지는, 필요한 스킬을 하드코딩해서 테이블에 넣어줄 것 */

        /** 파일로부터 정보 읽기 */
        readMonsterInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        convertMonsterInfoToEntity();


        /* 2020 02 28 권령희 추가, 정글 몬스터 */
        jungleMonsterInfoTable = new HashMap<>();
        jungleMonsterEntityTable = new HashMap<>();

        readJungleMonsterInfoFrom();
        convertJungleMonsterInfoToEntity();


        System.out.println("MonsterFactory 초기화 완료");
    }


    /** 매서드 목록 */


    /**
     * 일단 하드코딩으로 값 채움.
     * 파일 구조 및 파일 읽어들이는 방법 결정되면 수정할 것.
     */
    public static void readMonsterInfoFromFile(){

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

    /**
     * [2019 11 15 금요일 오후 12시 ~ ]
     *
     * @param requestedMonsterID
     * @return
     */
    public static MonsterEntity createMonster(int requestedMonsterID){

        MonsterEntity newMonster;

        /* 생성하는 처리 : */
        newMonster = (MonsterEntity) ( monsterEntityTable.get(requestedMonsterID) ).clone();

        return newMonster;
    }


    /** 2020 02 28 권령희 추가, 정글몬스터 관련 매서드 */

    public static void readJungleMonsterInfoFrom(){

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

    public static MonsterEntity createJungleMonster(int requestedJungleMonsterType){

        MonsterEntity newJungleMob;
        newJungleMob = (MonsterEntity) (jungleMonsterEntityTable.get(requestedJungleMonsterType).clone() );

        return newJungleMob;

    }



}
