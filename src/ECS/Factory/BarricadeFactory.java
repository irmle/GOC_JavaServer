package ECS.Factory;

import ECS.Classes.BarricadeInfo;
import ECS.Classes.MonsterInfo;
import ECS.Components.*;
import ECS.Entity.BarricadeEntity;
import ECS.Game.GameDataManager;

import java.util.HashMap;

public class BarricadeFactory {

    /** 멤버 변수 */

    public static HashMap<Integer, BarricadeInfo> barricadeInfoTable;
    public static HashMap<Integer, BarricadeEntity> barricadeEntityTable;

    /** 매서드 */

    public static void initFactory() {
        System.out.println("BarricadeFactory 초기화중...");

        barricadeInfoTable = new HashMap<>();
        barricadeEntityTable = new HashMap<>();

        /** 파일로부터 정보 읽기 */
        readBarricadeInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        createBarricadeEntityFromInfo();

        System.out.println("BarricadeFactory 초기화 완료");
    }

    public static void readBarricadeInfoFromFile(){

        BarricadeInfo barricadeInfo;

        /* GDM의 바리케이드 정보 목록을 가져와 하나씩 처리한다 */
        for ( HashMap.Entry<Integer, BarricadeInfo> barricadeInfoEntry
                : GameDataManager.barricadeInfoList.entrySet() ){

            barricadeInfo = barricadeInfoEntry.getValue();
            barricadeInfoTable.put(barricadeInfo.barricadeType, barricadeInfo);

        }

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

    public static void createBarricadeEntityFromInfo(){

        for( BarricadeInfo barricadeInfo : barricadeInfoTable.values() ){

            /** 바리케이드 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Barricade Component */
            BarricadeComponent barricadeComponent
                    = new BarricadeComponent(barricadeInfo.costTime, barricadeInfo.costGold);

            /* Position Component */
            PositionComponent positionComponent
                    = new PositionComponent();

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(barricadeInfo.maxHP, barricadeInfo.recoveryRateHP);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(barricadeInfo.defense);

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

            /** Entity 목록에 추가 */
            barricadeEntityTable.put(barricadeInfo.barricadeType, barricadeEntity);

        }

    }


    public static BarricadeEntity createBarricade(int requestedBarricadeID){

        BarricadeEntity newBarricade;

        /* 생성하는 처리 : */
        newBarricade = (BarricadeEntity) ( barricadeEntityTable.get(requestedBarricadeID) ).clone();

        return newBarricade;
    }





}
