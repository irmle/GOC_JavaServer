package ECS.System;

import ECS.Classes.BuffAction;
import ECS.Classes.BuildSlot;
import ECS.Classes.Type.TurretType;
import ECS.Classes.Vector3;
import ECS.Components.BuffComponent;
import ECS.Entity.BuffTurretEntity;
import ECS.Entity.CharacterEntity;
import ECS.Factory.BuffTurretFactory;
import ECS.Game.WorldMap;
import sun.plugin2.gluegen.runtime.BufferFactory;

import java.util.HashMap;
import java.util.List;

public class BuffTurretSystem {

    WorldMap worldMap;

    public BuffTurretSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime) {

        /* 버프타워 갯수만큼 반복한다 */
        for(HashMap.Entry<Integer, BuffTurretEntity> buffTurretEntity : worldMap.buffTurretEntity.entrySet()){

            /* 타워의 버프 정보 */
            BuffTurretEntity buffTurret = buffTurretEntity.getValue();
            BuffComponent buff = buffTurret.buffComponent;
            //BuffAction buffInfo = buff.buffActionInfo;

            /** 2020 04 03 */
            String effectName = "";
            switch (buffTurret.turretComponent.turretType){

                case TurretType.BUFF_TURRET_DEFAULT :

                    effectName = "체력회복";
                    break;

                case TurretType.BUFF_TURRET_TYPE1_UPGRADE1 :
                case TurretType.BUFF_TURRET_TYPE1_UPGRADE2 :
                case TurretType.BUFF_TURRET_TYPE1_UPGRADE3 :


                    effectName = "마력회복";
                    break;

                case TurretType.BUFF_TURRET_TYPE2_UPGRADE1 :
                case TurretType.BUFF_TURRET_TYPE2_UPGRADE2 :
                case TurretType.BUFF_TURRET_TYPE2_UPGRADE3 :

                    effectName = "이동속도증가";
                    break;

                case TurretType.BUFF_TURRET_TYPE3_UPGRADE1 :
                case TurretType.BUFF_TURRET_TYPE3_UPGRADE2 :
                case TurretType.BUFF_TURRET_TYPE3_UPGRADE3 :

                    effectName = "체력회복";
                    break;

            }



            if( (buffTurret.hpComponent.currentHP <= 0)){
                continue;
            }

            /* 버프대상(캐릭터) 갯수만큼 반복한다 */
            for(HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()){

                CharacterEntity character = characterEntity.getValue();

                /** 버프포탑과 대상의 거리를 계산한다 */
                float currentDistance = 0f;

                Vector3 turretPos = buffTurret.positionComponent.position;
                Vector3 charPos = character.positionComponent.position;

                currentDistance = Vector3.distance(turretPos, charPos);

                /** 대상이 버프 범위에 있는지 판별한다 */
                boolean isInTargetRange = false;

                float targetHP = character.hpComponent.currentHP;
                if( (currentDistance < buff.buffAreaRange) && targetHP > 0) {
                    isInTargetRange = true;

                    System.out.println("타겟을 찾았습니다");

                }

                /** 대상이 버프 적용 범위에 위치한다면 */
                if(isInTargetRange) {

                    /**
                     * 버프의 타겟 목록 배열을 list로 대체함.
                     */
                    /* 버프적용 타겟으로 추가한다 */
                    // buff.targetIDList[buff.targetIDList.length] = character.entityID;
                    buff.targetIDList.add(character.entityID);

                }

            }


            /* 버프 타겟 갯수만큼 반복한다 */
            for(int j=0; j<buff.targetIDList.size(); j++){

                /* 버프 대상 */
                int targetID = buff.targetIDList.get(j);
                CharacterEntity target = worldMap.characterEntity.get(targetID);

                /** 대상이 이미 자신의 버프를 받고 있는가?를 검사한다 */
                List<BuffAction> buffActionList = target.buffActionHistoryComponent.conditionHistory;
                boolean targetHasBuffAlready = false;

                BuffAction targetsBuffAction = new BuffAction();

                /* 대상의 버프액션 갯수만큼 반복한다 */
                for(int k=0; k<buffActionList.size(); k++){

                    if(buffTurret.entityID == buffActionList.get(k).unitID){

                        targetsBuffAction = buffActionList.get(k);

                        targetHasBuffAlready = true;
                        break;
                    }
                }

                if(targetHasBuffAlready) { /** 대상이 이미 효과를 받고 있다면 */

                    //System.out.println("이미 버프포탑의 버프를 받고 있는 사용자");

                    // buffInfo.remainTime = deltaTime;    // 버프 지속 남은 시간을 초기화해준다
                    //targetsBuffAction.remainTime = deltaTime;
                    //targetsBuffAction.remainTime = targetsBuffAction.buffDurationTime;

                }
                else{   /** 기존에 효과를 받고있지 않다면 */

                    //System.out.println("버프포탑의 버프를 새로 추가해줍니다 ");

                    /* 대상의 버프 목록에 추가해준다. */
                    //BuffAction newBuff = (BuffAction) buffInfo.clone();

                    BuffAction newBuff;
                    newBuff = BuffTurretFactory.createBuffTurretEffect(
                            buffTurret.turretComponent.turretType, effectName, buffTurret, buffTurret.entityID);

                    newBuff.unitID = buffTurret.entityID;


                    /**
                     * 2019 12 23 월요일 추가
                     */
                    BuildSlot buildSlot = worldMap.buildSystem.findBuildSlotByEntityID(buffTurret.entityID);
                    newBuff.skillUserID =  buildSlot.getBuilderEntityID();


                    buffActionList.add(newBuff);
                    // ㄴ깊은 복사가 이루어졌는지,, 테스트 필요할 듯.
                }
            }   /* 버프타겟 수만큼 반복 끝 */
            buffTurret.buffComponent.targetIDList.clear();

        }   /* 버프타워 수만큼 반복 끝 */

    }












    //버프타워의 효과범위 내부에 들어온 대상(캐릭터)을 판별하여
    //들어온 대상들에게 효과를 부여한다.

    //효과범위내에 존재하는 대상 판별하는 계산

    /*


    월드에서 버프터렛 엔티티목록을 가져옴.

    캐릭터 엔티티목록들을 가져옴 (거리 비교 용도,  효과부여대상은 캐릭터만 고려됨);

    for ( 버프터렛 엔티티목록의 수만큼 반복함 ) {

        버프 터렛 자신의 버프 컴포넌트에서, 버프액션을 가져옴.


         public int [ ] targetIDList;

        //캐릭터 앤티티의 포지션 컴포넌트 가져옴
        for( 캐릭터 엔티티목록 개수만큼 반복 ){

            버프타워 자신의 엔티티.
            가져온 캐릭터 엔티티.

            //효과범위내에 존재하는 대상 판별하는 계산
            float currentDistance = (버프 포탑 자신의 위치 - 가져온 캐릭터의 위치).길이;

            float targetHP = target의 HP컴포넌트에서 가져온 현재 체력;

                //버프 포탑 자신의 buffAreaRange 버프효과 범위내에 있다면, 해당하는 캐릭터의 ID를 targetIDList에 추가함.
                if( currentDistance < buffAreaRange && targetHP > 0)
                {
                    targetIDList . Add ( 범위안에 들어온 캐릭터ID)
                }
        }

        //타겟들이 존재한다면, 범위안에 들어가있는 타겟목록이 형성이 되었다면 그 타겟들의 버프액션 리스트에 접근한다.
        for ( targetIDList 수만큼 반복함 )
        {
            가져온 캐릭터 타겟 객체의 버프액션 리스트.

            BuffAction ownBuff = null;

            //캐릭터의 BuffActionList에 접근하여, 자신의 버프액션이 있는지 검색하는 부분.
            for(가져온 타겟 객체의 버프액션 리스트의 수 만큼 반복함.)
            {
                //캐릭터의 BuffActionList에 접근하여, 자신의 버프액션이 있는지 확인. (버프액션의 시전자ID를 자신의 ID와 비교함)
                if( 스킬오브젝트 자신의 타겟ID == 대상의 버프액션리스트의 시전자ID )
                {
                    해당하는 버프액션을 가져와서 삽입.
                    ownBuff = 가져온다.
                }
            }

            //이미 자신의 버프(또는 디버프)가 존재한다면
            if(ownBuff != null)
            {
                ownBuff.remainTime을 초기화 함.
                버프범위에서 벗어난뒤 몇초간 버프를 유지시킬것인지를 기준.
                일반적으로 tick타임인 deltaTime;

                ownBuff.remainTime = deltaTime;
            }
            else //없는 상태라면.
            {
                //캐릭터의 BuffActionList에 접근하여 자신의 버프로 버프액션을 생성후,
                //캐릭터의 BuffActionList에 포탑의 버프 액션을 추가해준다.

                BuffActionList.add ( new BuffAction (시전자id, 지속시간, 효과발동 쿨타임, ~~~~)   );
            }
        }
    }

    */

}
