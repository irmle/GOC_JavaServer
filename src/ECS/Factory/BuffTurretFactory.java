package ECS.Factory;

import ECS.Classes.BuffAction;
import ECS.Classes.BuffTurretInfo;
import ECS.Classes.ConditionFloatParam;
import ECS.Classes.Type.*;
import ECS.Classes.Type.ConditionType;

import ECS.Components.*;
import ECS.Entity.BuffTurretEntity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 클래스명 : BuffTurretFactory.class
 * 작 성 자 : 권령희
 * 작성날짜 : 2019년 11월 19일 오후
 *
 * 목    적 :
 *      - 버프 포탑의 정보가 담긴 파일로부터 정보를 읽어와 초기화 작업을 한다.
 *      - 초기화된 정보를 바탕으로, 게임 내에서 특정 터렛 생성 요청이 있을 경우
 *      해당 터렛을 생성하여 반환해주는 역할을 한다
 *      - 업그레이드도 처리할 예정.
 *
 * 사용예시 :
 *
 * 이력사항 :
 *
 */
public class BuffTurretFactory {

    /** 멤버 변수 */
    public static HashMap<Integer, BuffTurretInfo> buffTurretInfoTable;
    public static HashMap<Integer, BuffTurretEntity> buffTurretEntityTable;


    /** 생성자 */


    /** 매서드 */

    public static void initFactory(){

        System.out.println("BuffTurret Factory 초기화중...");

        buffTurretInfoTable = new HashMap<>();
        buffTurretEntityTable = new HashMap<>();

        /* 파일을 읽어, 위 테이블을 채우는 처리를 작성할 것 */
        /* 위 처리 로직이 완성되기 전까지는, 필요한 스킬을 하드코딩해서 테이블에 넣어줄 것 */

        /** 파일로부터 정보 읽기 */
        readBuffTurretInfoFromFile();

        /** 읽어온 정보를 Entity로 변환하기 */
        convertBuffTurretInfoToEntity();

        System.out.println("Buff TurretFactory 초기화 완료");
    }

    public static void readBuffTurretInfoFromFile(){

        BuffTurretInfo buffTurretInfo;

        /* 버프포탑 기본 (테스트용) */
        //원본
        /*buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_DEFAULT, 300, 5f, 3600f, 3f,
                        10, 15f, 0f, 5f,
                        ConditionType.hpRecoveryAmount, 15f, 5f);*/

        //임시용
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_DEFAULT, 300, 1f, 3600f, 3f,
                        10f, 15f, 0f, 5f,
                        ConditionType.hpRecoveryAmount, 30f, 5f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_DEFAULT, buffTurretInfo);


        /* 버프포탑 기본 *//*
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_DEFAULT, 300, 5000f, 3600f, 3f,
                        5, 15f * 1000, 5f * 1000, 5f * 1000,
                        ConditionType.hpRecoveryAmount, 5f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_DEFAULT, buffTurretInfo);*/

        /* 버프포탑 타입1 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE1_UPGRADE1, 600, 5f, 5400f, 3f,
                        5, 15f, 5f, 5f,
                        ConditionType.mpRecoveryAmount, 10f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE1_UPGRADE1, buffTurretInfo);

        /* 버프포탑 타입1-2 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE1_UPGRADE2, 1200, 5f, 7200f, 3f,
                        5, 15f, 5f, 5f,
                        ConditionType.mpRecoveryAmount, 20f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE1_UPGRADE2, buffTurretInfo);

        /* 버프포탑 타입1-3 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE1_UPGRADE3, 2400, 5f, 9000f, 3f,
                        5, 15f, 5f, 5f,
                        ConditionType.mpRecoveryAmount, 40f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE1_UPGRADE3, buffTurretInfo);

        /* 버프포탑 타입2 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE2_UPGRADE1, 600, 5f, 5400f, 3f,
                        10, 15f, -1, -1,
                        ConditionType.moveSpeedRate, 3f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE2_UPGRADE1, buffTurretInfo);

        /* 버프포탑 타입2-2 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE2_UPGRADE2, 1200, 5f, 7200f, 3f,
                        10, 15f, -1, -1,
                        ConditionType.moveSpeedRate, 6f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE2_UPGRADE2, buffTurretInfo);

        /* 버프포탑 타입2-3 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE2_UPGRADE3, 2400, 5f, 9000f, 3f,
                        10, 15f, -1, -1,
                        ConditionType.moveSpeedRate, 40f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE2_UPGRADE3, buffTurretInfo);

        /* 버프포탑 타입3 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE3_UPGRADE1, 600, 5f, 5400f, 3f,
                        7, 15f, 5f, 5f,
                        ConditionType.hpRecoveryAmount, 10f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE3_UPGRADE1, buffTurretInfo);

        /* 버프포탑 타입3-2 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE3_UPGRADE2, 1200, 5f, 7200f, 3f,
                        7, 15f, 5f, 5f,
                        ConditionType.hpRecoveryAmount, 20f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE3_UPGRADE2, buffTurretInfo);

        /* 버프포탑 타입3-3 */
        buffTurretInfo
                = new BuffTurretInfo
                (TurretType.BUFF_TURRET_TYPE3_UPGRADE3, 2400, 5f, 9000f, 3f,
                        7, 15f, 5f, 5f,
                        ConditionType.hpRecoveryAmount, 40f, 55f);

        buffTurretInfoTable.put(TurretType.BUFF_TURRET_TYPE3_UPGRADE3, buffTurretInfo);

    }

    public static void convertBuffTurretInfoToEntity(){

        for( HashMap.Entry<Integer, BuffTurretInfo> buffTurretInfoEntry : buffTurretInfoTable.entrySet() ){

            BuffTurretInfo buffTurretInfo = buffTurretInfoEntry.getValue();

            /** 버프 타워 생성에 필요한 각 컴포넌트들 생성한다 */

            /* Turret Component */
            TurretComponent turretComponent
                    = new TurretComponent(buffTurretInfo.turretType, buffTurretInfo.costTime, buffTurretInfo.costGold);

            /* Position Component */
            PositionComponent positionComponent
                    = new PositionComponent(0f, 0f, 0f);

            /* HP Component */
            HPComponent hpComponent
                    = new HPComponent(buffTurretInfo.maxHp, buffTurretInfo.recoveryRateHP);

            /* Defense Component */
            DefenseComponent defenseComponent
                    = new DefenseComponent(buffTurretInfo.defense);

            /* Buff Component */
            /*      BuffAction      */
            /*          ConditionParam       */
            ConditionFloatParam floatParam = new ConditionFloatParam(buffTurretInfo.buffType, buffTurretInfo.buffValue);
            ArrayList<ConditionFloatParam> floatParams = new ArrayList<>();
            floatParams.add(floatParam);


            //시전자(unitID)는 나중에 채워줄 것, 0은 없다는 의미로 쓸 것. (내 개인적인 의견이고, 수헌씨랑 합의 봐야)
            BuffAction buffAction
                    = new BuffAction
                    (0, 0, buffTurretInfo.remainTime, buffTurretInfo.remainCoolTime,
                            buffTurretInfo.coolTime, new ArrayList<>(), floatParams);

            // 널.. 나중에 자료형을 바꾸거나, 얘를 넘겨주지말고 내부에서 초기화 및 생성하던가 하도록 바꿀 것.
            BuffComponent buffComponent
                    = new BuffComponent
                    (buffAction, buffTurretInfo.buffAreaRange);

            /* BuffActionHistory Component */
            BuffActionHistoryComponent buffActionHistoryComponent = new BuffActionHistoryComponent();

            /* HpHistory Component */
            HpHistoryComponent hpHistoryComponent = new HpHistoryComponent();

            /* Condition Component */
            ConditionComponent conditionComponent = new ConditionComponent();


            /** 생성된 컴포넌트들을 가지고, 공격포탑 Entity 객체를 만든다 */

            BuffTurretEntity buffTurretEntity
                    = new BuffTurretEntity
                    (positionComponent, turretComponent, hpComponent, defenseComponent,
                            buffComponent, buffActionHistoryComponent, hpHistoryComponent, conditionComponent);

            /** 목록에 추가 */

            buffTurretEntityTable.put(buffTurretInfo.turretType, buffTurretEntity);


        }

    }

    public static BuffTurretEntity createBuffTurret(int requestedTurretID){

        BuffTurretEntity newTurret;

        newTurret = (BuffTurretEntity) ( buffTurretEntityTable.get(requestedTurretID) ).clone();

        return newTurret;
    }

}
