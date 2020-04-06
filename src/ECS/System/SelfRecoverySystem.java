package ECS.System;

import ECS.Classes.BuffAction;
import ECS.Classes.ConditionFloatParam;
import ECS.Classes.Type.ConditionType;
import ECS.Entity.CharacterEntity;
import ECS.Game.WorldMap;

import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2019 12 26 목 21:30
 * 업뎃날짜 :
 * 목    적 :
 *      캐릭터 및 기타 자가회복 속성을 갖는 앤티티들의 회복처리를 위한 시스템.
 *      현재 자가회복 속성은 체력 과 마력 두 가지가 있다.
 *          체력 회복의 경우, 캐릭터와 버프 포탑이 초당 N의 체력회복 속성을 가지고 있고,
 *          마력 회복의 경우, 캐릭터만이 초당 N의 회복 속성을 갖는다.
 *              그마저도 캐릭터 속성에 따라 마력회복을 하지 않는 타입도 있다 ; 전사 및 랜서류
 *
 */
public class SelfRecoverySystem {

    /* 멤버 변수 */
    WorldMap worldMap;
    float remainCoolTime;
    public static final float COOL_TIME = 1f;    // 자가회복 쿨타임 ; 1초

    /* 생성자 */
    public SelfRecoverySystem(WorldMap worldMap) {
        this.worldMap = worldMap;

        remainCoolTime = 0f;
    }

    /* 매서드 */
    public void onUpdate(float deltaTime){

        /** 회복효과 발동 가능 여부를 체크한다 */
        boolean isCoolTimeRemained = (remainCoolTime > 0) ? true: false;

        if(isCoolTimeRemained){ /* 쿨타임이 아직 남아있다면 */
            remainCoolTime -= deltaTime;
        }
        else{   /* 쿨타임이 끝났다면 */

            System.out.println("자가회복 버프를 추가합니다");

            /* 모든 캐릭터에 대해 처리한다 */
            for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

                /* 캐릭터 정보 */
                CharacterEntity character = characterEntity.getValue();
                int characterID = character.entityID;

                System.out.println("캐릭터 타입 : " + character.characterComponent.characterType);

                /** 죽은 사람은 제외한다 */
                float currentHP = character.hpComponent.currentHP;
                if(currentHP <= 0){
                    continue;
                }

                /** 1초동안 지속되는 회복 버프를 넣어준다 */

                BuffAction recoveryBuff = new BuffAction();
                recoveryBuff.unitID = characterID;
                recoveryBuff.skillUserID = characterID;

                recoveryBuff.remainTime = 1f;
                recoveryBuff.remainCoolTime = 0f;
                recoveryBuff.coolTime = 1f;

                float hpRecoveryAmount = character.hpComponent.recoveryRateHP * character.conditionComponent.hpRecoveryRate;
                System.out.println("초당 체력회복량 : " + hpRecoveryAmount);
                recoveryBuff.floatParam.add(new ConditionFloatParam(ConditionType.hpRecoveryAmount, hpRecoveryAmount));

                float mpRecoveryAmount = character.mpComponent.recoveryRateMP * character.conditionComponent.mpRecoveryRate;
                System.out.println("초당 마력회복량 : " + mpRecoveryAmount);
                recoveryBuff.floatParam.add(new ConditionFloatParam(ConditionType.mpRecoveryAmount, mpRecoveryAmount));

                character.buffActionHistoryComponent.conditionHistory.add(recoveryBuff);

            }

            remainCoolTime = COOL_TIME;
        }
    }

}
