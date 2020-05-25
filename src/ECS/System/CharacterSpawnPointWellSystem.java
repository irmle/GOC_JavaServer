package ECS.System;

import ECS.Classes.BuffAction;
import ECS.Classes.ConditionFloatParam;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Type.SkillType;
import ECS.Classes.Type.SystemEffectType;
import ECS.Classes.Vector3;
import ECS.Components.CharacterComponent;
import ECS.Components.HPComponent;
import ECS.Entity.CharacterEntity;
import ECS.Factory.SkillFactory;
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
    float remainCoolTime;
    public static final float COOL_TIME = 1f;    // 자가회복 쿨타임 ; 1초

    float systemCoolTime;
    float systemRemainCoolTime;

    public CharacterSpawnPointWellSystem(WorldMap worldMap, float systemCoolTime) {
        this.worldMap = worldMap;
        this.systemCoolTime = systemCoolTime;
        this.systemRemainCoolTime = this.systemCoolTime;
    }

    public void onUpdate(float deltaTime) {

        // 시스템 도는 주기 조절용 코드
        /*systemRemainCoolTime -= worldMap.tickRate;
        if(systemRemainCoolTime <= 0){
            systemRemainCoolTime = systemCoolTime;
        }
        else{
            return;
        }*/

        /* 우물 위치 */
        Vector3 wellPoint = new Vector3(3f, 0f, -3f);
        float buffAreaRange = 15f;

        /** 회복효과 발동 가능 여부를 체크한다 */
        boolean isCoolTimeRemained = (remainCoolTime > 0) ? true: false;

        if(isCoolTimeRemained){ /* 쿨타임이 아직 남아있다면 */
            remainCoolTime -= deltaTime;
        }
        else {   /* 쿨타임이 끝났다면 */

            /** 캐릭터 */
            for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

                CharacterEntity character = characterEntity.getValue();

                /* 죽은 애는 패스 */
                HPComponent hpComponent = character.hpComponent;
                if(hpComponent.currentHP <= 0){
                    continue;
                }

                /* 캐릭터가 우물 범위 내에 있는지 검사한다 */

                Vector3 charPos = character.positionComponent.position;

                float currentDistance = 0f;
                currentDistance = Vector3.distance(wellPoint, charPos);

                /** 대상이 범위에 있는지 판별한다 */
                boolean isInTargetRange = false;

                float targetHP = character.hpComponent.currentHP;
                float targetMP = character.mpComponent.currentMP;

                if( (currentDistance < buffAreaRange)
                        && ((targetHP < character.hpComponent.maxHP) || (targetMP < character.mpComponent.maxMP))) {
                    isInTargetRange = true;

                }

                /** 대상이 버프 적용 범위에 위치한다면 */
                if(isInTargetRange) {

                    //character.buffActionHistoryComponent.conditionHistory.add( createRecoveryBuff(character.entityID) );

                    character.buffActionHistoryComponent.conditionHistory.add(
                            SelfRecoverySystem.createSystemActionEffect(
                                    SystemEffectType.WELL, "체력회복 속도증가", character, character.entityID));

                    character.buffActionHistoryComponent.conditionHistory.add(
                            SelfRecoverySystem.createSystemActionEffect(
                                    SystemEffectType.WELL, "마력회복 속도증가", character, character.entityID));

                }

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

        ConditionFloatParam hpRecoveryParam = new ConditionFloatParam(ConditionType.hpRecoveryRate, 400f);
        buffAction.floatParam.add(hpRecoveryParam);

        ConditionFloatParam mpRecoveryParam = new ConditionFloatParam(ConditionType.mpRecoveryRate, 400f);
        buffAction.floatParam.add(mpRecoveryParam);

        return buffAction;

    }


}
