package ECS.Factory;

import ECS.Components.CrystalComponent;
import ECS.Components.DefenseComponent;
import ECS.Components.HPComponent;
import ECS.Components.PositionComponent;
import ECS.Entity.CrystalEntity;

public class CrystalFactory {

    /** 멤버 변수 */


    /** 매서드 */

    public static void initFactory() {
        System.out.println("CrystalFactory 초기화중...");

        /** 파일로부터 정보 읽기 */
        //readBarricadeInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        // convertBarricadeInfoToEntity();

        System.out.println("CrystalFactory 초기화 완료");
    }


    public static CrystalEntity createCrystal(){

        CrystalEntity newCrystal;

        /* 생성하는 처리 : */

        PositionComponent positionComponent = new PositionComponent(15, 0, -15);
        CrystalComponent crystalComponent = new CrystalComponent();
        HPComponent hpComponent = new HPComponent(10000f, 1f);
        DefenseComponent defenseComponent = new DefenseComponent(0);

        newCrystal = new CrystalEntity(positionComponent, crystalComponent, hpComponent, defenseComponent);

        return newCrystal;
    }
}
