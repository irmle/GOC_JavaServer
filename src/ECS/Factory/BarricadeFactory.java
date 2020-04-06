package ECS.Factory;

import ECS.Classes.MonsterInfo;
import ECS.Components.*;
import ECS.Entity.BarricadeEntity;

import java.util.HashMap;

public class BarricadeFactory {

    /** 멤버 변수 */

    public static HashMap<Integer, BarricadeEntity> barricadeEntityTable;

    /** 매서드 */

    public static void initFactory() {
        System.out.println("BarricadeFactory 초기화중...");

        barricadeEntityTable = new HashMap<>();

        /** 파일로부터 정보 읽기 */
        //readBarricadeInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        convertBarricadeInfoToEntity();

        System.out.println("BarricadeFactory 초기화 완료");
    }

    public static void readBarricadeInfoFromFile(){

        //MonsterInfo monsterInfo;
        // int monIDCount = 1;

        /* 고블린 */
        /*monsterInfo
                = new MonsterInfo(1, "Goblin",
                200f, 20f,
                300f, 30f, 0.625f, 150f,
                0f, 150 * 4f, 250f);

        monsterInfoTable.put(monsterInfo.monsterID, monsterInfo);*/

    }

    /**
     * 일단 바리케이드 종류가 하나라고 가정, 바리케이드 스펙이 정해지지 않아서..
     * 모든 걸 하드코딩 함.
     */
    public static void convertBarricadeInfoToEntity(){

        for( int i=0; i<1; i++ ){

            /** 바리케이드 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Barricade Component */
            BarricadeComponent barricadeComponent
                    = new BarricadeComponent(5000f, 250);

            /* Position Component */
            PositionComponent positionComponent
                    = new PositionComponent(-400f, 0f, -40f);

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(100, 1);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(5f);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();

            /** 생성된 컴포넌트들을 가지고, 바리케이드 Entity 객체를 만든다 */

            /* Barricade Entity */
            BarricadeEntity barricadeEntity
                    = new BarricadeEntity(positionComponent, barricadeComponent,hpComponent, defenseComponent,
                    buffActionHistoryComponent, hpHistoryComponent, conditionComponent );

            /** 목록에 추가 */

            barricadeEntityTable.put(1, barricadeEntity);

        }

    }


    public static BarricadeEntity createBarricade(int requestedBarricadeID){

        BarricadeEntity newBarricade;

        /* 생성하는 처리 : */
        newBarricade = (BarricadeEntity) ( barricadeEntityTable.get(requestedBarricadeID) ).clone();

        return newBarricade;
    }





}
