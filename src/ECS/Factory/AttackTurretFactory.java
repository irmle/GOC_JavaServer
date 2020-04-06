package ECS.Factory;

import ECS.Classes.AttackTurretInfo;
import ECS.Classes.Type.*;
import ECS.Components.*;
import ECS.Entity.AttackTurretEntity;

import java.util.HashMap;

/**
 * 클래스명 : AttackTurretFactory.class
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 11월 19일 오후
 *
 * 목    적 :
 *      - 공격 포탑의 정보가 담긴 파일로부터 정보를 읽어와 초기화 작업을 한다.
 *      - 초기화된 정보를 바탕으로, 게임 내에서 특정 터렛 생성 요청이 있을 경우
 *      해당 터렛을 생성하여 반환해주는 역할을 한다
 *      - 업그레이드도 처리할 예정.
 *
 * 사용예시 :
 *
 * 이력사항 :
 *
 */
public class AttackTurretFactory {

    /** 멤버 변수 */
    public static HashMap<Integer, AttackTurretInfo> attackTurretInfoTable;
    public static HashMap<Integer, AttackTurretEntity> attackTurretEntityTable;


    /** 생성자 */


    /** 매서드 */

    public static void initFactory(){

        System.out.println("AttackTurret Factory 초기화중...");

        attackTurretInfoTable = new HashMap<>();
        attackTurretEntityTable = new HashMap<>();

        /* 파일을 읽어, 위 테이블을 채우는 처리를 작성할 것 */
        /* 위 처리 로직이 완성되기 전까지는, 필요한 스킬을 하드코딩해서 테이블에 넣어줄 것 */

        /** 파일로부터 정보 읽기 */
        readAttackTurretInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        convertAttackTurretInfoToEntity();

        System.out.println("AttackTurret Factory 초기화 완료");
    }

    public static void readAttackTurretInfoFromFile(){

        AttackTurretInfo attackTurretInfo;

        /* 공격포탑 기본 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_DEFAULT, 250, 5f, 500, 0f,
                         52f * 0.1f, 0.83f, 15f, 1);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_DEFAULT, attackTurretInfo);

        /* 공격포탑 타입1 *//*
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE1_UPGRADE1, 500, 5f, 5000, 0f,
                         300f, 0.83f, 0.5f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE1_UPGRADE1, attackTurretInfo);*/
        /* 공격포탑 타입1 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE1_UPGRADE1, 500, 5f, 5000, 0f,
                        30f, 0.83f, 15f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE1_UPGRADE1, attackTurretInfo);

        /* 공격포탑 타입1-2 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE1_UPGRADE2, 1000, 5f, 10000, 0f,
                         60f, 0.83f, 15f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE1_UPGRADE2, attackTurretInfo);

        /* 공격포탑 타입1-3 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE1_UPGRADE3, 2000, 5f, 5000, 0f,
                         90f, 0.83f, 15f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE1_UPGRADE3, attackTurretInfo);

        /* 공격포탑 타입2 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE2_UPGRADE1, 500, 5f, 5000, 0f,
                         20f, 0.83f, 21f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE2_UPGRADE1, attackTurretInfo);

        /* 공격포탑 타입2-2 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE2_UPGRADE2, 1000, 5f, 10000, 0f,
                         40f, 0.83f, 21f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE2_UPGRADE2, attackTurretInfo);

        /* 공격포탑 타입2-3 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE2_UPGRADE3, 2000, 5f, 5000, 0f,
                         60f, 0.83f, 21f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE2_UPGRADE3, attackTurretInfo);

        /* 공격포탑 타입3 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE3_UPGRADE1, 500, 5f, 5000, 0f,
                         15f, 1.13f, 18f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE3_UPGRADE1, attackTurretInfo);

        /* 공격포탑 타입3-2 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE3_UPGRADE2, 1000, 5f, 10000, 0f,
                         30f, 1.13f, 18f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE3_UPGRADE2, attackTurretInfo);

        /* 공격포탑 타입3-3 */
        attackTurretInfo
                = new AttackTurretInfo
                (TurretType.ATTACK_TURRET_TYPE3_UPGRADE3, 2000, 5f, 15000, 0f,
                         45f, 1.13f, 18f, 40);

        attackTurretInfoTable.put(TurretType.ATTACK_TURRET_TYPE3_UPGRADE3, attackTurretInfo);


    }

    public static void convertAttackTurretInfoToEntity(){

        for( HashMap.Entry<Integer, AttackTurretInfo> attackTurretInfoEntry : attackTurretInfoTable.entrySet() ){

            AttackTurretInfo attackTurretInfo = attackTurretInfoEntry.getValue();

            /** 공격 타워 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Turret Component */
            TurretComponent turretComponent
                    = new TurretComponent(attackTurretInfo.turretType, attackTurretInfo.costTime, attackTurretInfo.costGold);

            /* Position Component */
            PositionComponent positionComponent
                    = new PositionComponent(0f, 0f, 0f);

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(attackTurretInfo.maxHp, attackTurretInfo.recoveryRateHP);

            /* Attack Component */
            AttackComponent attackComponent
                    = new AttackComponent(attackTurretInfo.attackDamage, attackTurretInfo.attackSpeed, attackTurretInfo.attackRange);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(attackTurretInfo.defense);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();


            /** 생성된 컴포넌트들을 가지고, 공격포탑 Entity 객체를 만든다 */

            /* AttackTurret Entity*/
            AttackTurretEntity attackTurretEntity
                    = new AttackTurretEntity(positionComponent, turretComponent, hpComponent,
                    attackComponent, defenseComponent,
                    buffActionHistoryComponent, hpHistoryComponent, conditionComponent);

            if(attackTurretEntity.conditionComponent == null){
                System.out.println("어택터렛 팩토린데 널임 헐;; ");
            }
            else{
                System.out.println("어택터렛 팩토리인데 널 아님");
            }


            /** 목록에 추가 */

            attackTurretEntityTable.put(attackTurretInfo.turretType, attackTurretEntity);

        }

    }

    public static AttackTurretEntity createAttackTurret(int requestedTurretType){

        AttackTurretEntity newTurret;

        newTurret = (AttackTurretEntity) ( attackTurretEntityTable.get(requestedTurretType) ).clone();

        return newTurret;
    }


}
