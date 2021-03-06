package ECS.System;

import java.util.HashMap;

import ECS.Classes.ItemSlot;
import ECS.Classes.SkillSlot;
import ECS.Classes.Vector3;
import ECS.Entity.CharacterEntity;
import ECS.Factory.MapFactory;
import ECS.Game.WorldMap;

/**
 * 업뎃날짜 : 2020 02 18 화 권령희
 * 업뎃내용 :
 *      여기서 구한 '이동할 좌표'를, 바로 캐릭터의 좌표로 반영시키지 않음.
 *      대신, 유저가 속한 월드맵의 좌표 해시맵에 집어넣는걸로.
 *      ㄴ 이 값을 가지고, PositionSystem이 돌면서
 *          각종 이동 및 위치 보정 처리를 수행할 것임.
 *
 */
public class CharacterSystem {

    WorldMap worldMap;

    public CharacterSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime) {

        /** 캐릭터 */
        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();

            //공격 쿨타임 감소처리
            if (character.attackComponent.remainCoolTime > 0)
                character.attackComponent.remainCoolTime -= deltaTime;

            //스킬 쿨타임 감소처리
            for (int i = 0; i < character.skillSlotComponent.skillSlotList.size(); i++) {
                SkillSlot slot = character.skillSlotComponent.skillSlotList.get(i);

                if(slot.remainCoolTime > 0){

                    slot.remainCoolTime -= deltaTime;
                }
                else{
                    slot.remainCoolTime = 0;
                }
            }

            //아이템 사용 쿨타임 감소처리
            for (int i = 0; i < character.itemSlotComponent.itemSlotList.size(); i++) {
                ItemSlot slot = character.itemSlotComponent.itemSlotList.get(i);

                slot.remainCoolTime -= deltaTime;
            }

            /** 죽은 애 패스 처리 */
            if( worldMap.checkUserIsDead(character)){

                continue;
            }


            /** 2020 02 19 변경, 이동될 위치를 구하는 부분은 기존과 똑같음. */
            boolean isMovable = character.conditionComponent.isDisableMove ? false : true;
            if(isMovable){

                if(worldMap.charNextPosList_gotStuck.containsKey(character.entityID)){
                    continue;

                }

                if(character.velocityComponent.velocity.length() < 0.9){
                    continue;
                }

                float MoveSpeed = character.velocityComponent.moveSpeed;
                float moveSpeedBonus = character.conditionComponent.moveSpeedBonus;
                float moveSpeedRate = character.conditionComponent.moveSpeedRate;

                //위치계산용 임시 변수.
                Vector3 temp = (Vector3) character.positionComponent.position.clone();

                //이동할 방향 (normalize된 크기1의 벡터)
                Vector3 direction = (Vector3) character.velocityComponent.velocity.clone();
                direction = direction.normalize();

                //이동속도 버프등을 적용한 최종 이동수치.
                float movementSpeed = deltaTime * ( MoveSpeed + moveSpeedBonus ) * moveSpeedRate;

                //이동할 방향 * 최종 적용되는 이동수치 = 이동할 지점.
                Vector3 moveVector = direction.setSpeed(movementSpeed);

                temp.movePosition(temp, moveVector);

                //이동될 위치.
                Vector3 changedPosition = temp;

                // 아래 값을 가지고, positionSystem에서 이동 가능 여부 등을 판단할 것이다.
                worldMap.charNextPosList.put(character.entityID, changedPosition);

            }

        }
    }
}



