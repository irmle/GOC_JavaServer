package ECS.System;

import ECS.Classes.BuffAction;
import ECS.Classes.ConditionFloatParam;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Type.SkillType;
import ECS.Classes.Vector3;
import ECS.Components.CharacterComponent;
import ECS.Entity.CharacterEntity;
import ECS.Game.WorldMap;

import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2002 03 20 금 새벽
 * 업뎃날짜 :
 * 목    적 : 우물 회복 시스템
 */
public class CharacterSpawnPointWellSystem {

    WorldMap worldMap;

    public CharacterSpawnPointWellSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime) {

        /* 우물 위치 */
        Vector3 wellPoint = new Vector3(3f, 0f, -3f);
        float buffAreaRange = 15f;


        /** 캐릭터 */
        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();

            /* 캐릭터가 우물 범위 내에 있는지 검사한다 */

            Vector3 charPos = character.positionComponent.position;

            float currentDistance = 0f;
            currentDistance = Vector3.distance(wellPoint, charPos);

            System.out.println("우물과 캐릭터 간 거리 : " + currentDistance);

            /** 대상이 범위에 있는지 판별한다 */
            boolean isInTargetRange = false;

            float targetHP = character.hpComponent.currentHP;
            if( (currentDistance < buffAreaRange) && targetHP < character.hpComponent.maxHP) {
                isInTargetRange = true;

                System.out.println("우물에 의한 회복 버프를 받을 수 있음. ");

            }

            /** 대상이 버프 적용 범위에 위치한다면 */
            if(isInTargetRange) {

                character.buffActionHistoryComponent.conditionHistory.add( createRecoveryBuff(character.entityID) );

                System.out.println("우물 버프를 넣음.");
            }

        }

    }

    /**
     * 우물 버프 생성 매서드.
     * 우물 근처에 있을 경우, 평소 회복률의 5배 속도로 회복하게끔 버프를 넣어주는데
     * 그냥.. 계산하기 쉽게? 한 번 들어간 버프는 한 번만 적용되도록. 지속시간 1초, 쿨타임1초로 주었다.
     * 범위 내에 있는 동안 계속해서 버프를 받을거고? >> 근데 이러면.. 이걸 표시해줄 때가 문제긴 한데.
     * @param characterID
     * @return
     */
    public BuffAction createRecoveryBuff(int characterID){

        BuffAction buffAction = new BuffAction(characterID, characterID, 1f, 0f, 1f);
        buffAction.skillType = SkillType.WELL_RECOVERY;

        ConditionFloatParam recoveryParamHP = new ConditionFloatParam(ConditionType.hpRecoveryRate, 400f);
        buffAction.floatParam.add(recoveryParamHP);

        ConditionFloatParam recoveryParamMP = new ConditionFloatParam(ConditionType.mpRecoveryRate, 400f);
        buffAction.floatParam.add(recoveryParamMP);
        
        return buffAction;

    }


}
