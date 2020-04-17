package ECS.System;

import ECS.Classes.*;
import ECS.Classes.Type.ConditionType;
import ECS.Classes.Type.SkillType;
import ECS.Classes.Type.Team;
import ECS.Classes.Type.TurretType;
import ECS.Components.*;
import ECS.Entity.*;
import ECS.Factory.AttackTurretFactory;
import ECS.Factory.SkillFactory;
import ECS.Game.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;

public class AttackTurretSystem {

    WorldMap worldMap;

    public AttackTurretSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime) {

        /* 공격 터렛 갯수만큼 반복한다 */
        for(HashMap.Entry<Integer, AttackTurretEntity> attackTurretEntity : worldMap.attackTurretEntity.entrySet()){

            /* 터렛 및 공격 정보 */
            AttackTurretEntity attackTurret = attackTurretEntity.getValue();
            AttackComponent attackAbility = attackTurret.attackComponent;    // 데미지, 범위, 스피드, 쿨타임
            AttackTurretInfo turretInfo = AttackTurretFactory.attackTurretInfoTable.get(attackTurret.turretComponent.turretType);

            if( (attackTurret.hpComponent.currentHP <= 0)){
                continue;
            }

            /* 쿨타임 및 본인 상태 */
            boolean turretIsAttackablee =
                    ((!attackTurret.conditionComponent.isDisableAttack) && (attackAbility.remainCoolTime < 0))
                            ? true : false;
            if(!turretIsAttackablee){

                attackAbility.remainCoolTime -= deltaTime;
                continue;

            }



            /** 공격 범위 내에서 가장 가까운 타겟을 찾는다 */

            float minDistance = attackAbility.attackRange;
            int targetID = -1;
            MonsterEntity target;   // 굳이..

            /* 공격 대상(몬스터)의 갯수만큼 반복한다 */
            for(HashMap.Entry<Integer, MonsterEntity> monsterEntity : worldMap.monsterEntity.entrySet()){

                MonsterEntity monster = monsterEntity.getValue();

                /* 포탑과 대상의 거리를 계산한다 */
                float currentDistance = 0f;

                Vector3 turretPos = attackTurret.positionComponent.position;
                Vector3 mobPos = monster.positionComponent.position;

                currentDistance = Vector3.distance(turretPos, mobPos);

                /* 대상이 공격 범위에 들어있는지 판별한다 */
                boolean isInTargetRange = false;

                // 살아있지 않은 녀석은 애초에 범위 내에 존재하지 않는걸로.
                float targetHP = monster.hpComponent.currentHP;
                if( (currentDistance < attackAbility.attackRange) && targetHP > 0)
                {
                    isInTargetRange = true;
                }

                /* 범위에 들어있다면, 범위 대상 중 가장 가까운 녀석인지 판별한다 */
                if(isInTargetRange){

                    if(!(monster.conditionComponent.isUnTargetable)
                            && ( currentDistance <minDistance )){

                        minDistance = currentDistance;
                        targetID = monster.entityID;
                    }

                }


            } /* 공격 대상 갯수만큼 반복 종료 */


            /** 지저된 타겟에 대해 공격 처리를 한다 */

            /* 타겟이 존재한다면 */
            if(targetID > 0){  // 임시, 타겟ID의 초기값을 -1로 해두었다. 타겟을 찾아다면, 0보다 큰 값을 갖게 될 것이다.

                /* 포탑(자신)이 공격 가능한 상태인지 확인한다 */

                ConditionComponent condition = attackTurret.conditionComponent;

                /* 쿨타임 및 본인 상태 */
                boolean turretIsAttackable =
                        ((!condition.isDisableAttack) && (attackAbility.remainCoolTime < 0))
                                ? true : false;


                /* 공격 가능 여부에 따른 처리를 한다. */
                if(turretIsAttackable){ /* 공격 가능하다면 */

                    //System.out.println("turretIsAttackable = "+turretIsAttackable);

                    target = worldMap.monsterEntity.get(targetID);  // ..어! 필요 없엇네

                    /* 투사체를 생성한다 */

                    FlyingObjectComponent flyingObject = new FlyingObjectComponent();
                    flyingObject.userEntityID = attackTurret.entityID;
                    flyingObject.targetEntityID = targetID;

                    //System.out.println("create = "+targetID);

                    flyingObject.createdSkillType = SkillType.MAGICIAN_FIREBALL;    // "포탑 공격" 타입
                    //flyingObject.createdSkillType = attackTurret.turretComponent.turretType;    // "포탑 공격" 타입

                    flyingObject.flyingObjectRemainDistance = 0f; // 타겟이 정해져 있으므로, 목적지를 따로 정하지 않음
                    /*flyingObject.flyingSpeed = 15.0f;    // 대강 지어 적음.
                    flyingObject.flyingObjectRadius = 2.5f;*/

                    flyingObject.flyingSpeed = turretInfo.flyingObjSpeed;
                    flyingObject.flyingObjectRadius = turretInfo.flyingObjAttackRadius;

                    flyingObject.direction = new Vector3(0f, 0f, 0f);

                    flyingObject.buffAction = new BuffAction();
                    flyingObject.buffAction.unitID = attackTurret.entityID;

                    BuildSlot buildSlot = worldMap.buildSystem.findBuildSlotByEntityID(attackTurret.entityID);
                    flyingObject.buffAction.skillUserID = buildSlot.getBuilderEntityID();
                    //flyingObject.buffAction.skillUserID = attackTurret.entityID; //2020 01 22 주석처리함.. 왜 이걸 써놨지??
                                                                                    // 확실히 좀 애매하긴 하다. 이 투사체를 발사한 장본인은 터렛인데..
                                                                                    // 근데 또, 터렛이 죽음과 동시에 투사체가 적중해서 몹이 죽는 상황이 벌어진다면
                                                                                    // 널 뜰거아냐...
                                                                                    // 근데 또. 위에서.. 건물 사라졌다고 빌드슬롯에서 빌더정보도 초기화되면, 그것도 마찬가지임.
                                                                                    // 역시.. 사망/보상 처리에서, 죽인 앤티티 정보가 없으면 패스 처리하는 수 밖에 없나??

                    /**
                     * 2020 02 07
                     */
                    ConditionFloatParam damageParam
                            = SkillFactory.createDamageParam(attackAbility.attackDamage, attackTurret.attackComponent, attackTurret.conditionComponent);

                    flyingObject.buffAction.floatParam = new ArrayList<>();
                    flyingObject.buffAction.floatParam.add(damageParam);
                    flyingObject.buffAction.remainTime = 0.1f;


                    FlyingObjectEntity flyingObjectEntity = new FlyingObjectEntity(flyingObject);
                    flyingObjectEntity.team = Team.BLUE;
                    flyingObjectEntity.entityID = worldMap.worldMapEntityIDGenerater.getAndIncrement();

                    flyingObjectEntity.positionComponent.position = (Vector3)attackTurret.positionComponent.position.clone();
                    flyingObjectEntity.positionComponent.position.set(
                            flyingObjectEntity.positionComponent.position.x(),
                            flyingObjectEntity.positionComponent.position.y() + 6.3f,
                            flyingObjectEntity.positionComponent.position.z()
                    );

                    flyingObjectEntity.flyingObjectComponent = flyingObject;



                    flyingObject.startPosition = flyingObjectEntity.positionComponent.position;



                    /* 월드 내 Entity 생성 요청 큐에 추가한다 */
                    worldMap.requestCreateQueue.add(flyingObjectEntity);
                    // ㄴ 굳이 clone으로 해줘야 하나??


                    /* 공격 쿨타임을 초기화한다 */

                    attackAbility.remainCoolTime = 1f/attackAbility.attackSpeed;

                }


            } /* 타겟이 존재하는 경우의 처리 끝 */
            else{   /* 타겟이 존재하지 않음 */

                /* 공격 쿨타임을 갱신한다 */

                attackAbility.remainCoolTime -= deltaTime;
                // 공격 쿨타임 갱신을 위에서 해주는게 맞는지, 끝에서 해주는게 맞는지...

            }
        } /* 공격 터렛 갯수만큼 반복 끝 */

    }







    //공격타워의 사거리 내부에 들어온 몬스터 또는 적(캐릭터)가 들어왔을때,
    //들어온 대상중에서 가장 거리가 가까운 타겟을 판별하여 공격하는 로직.

    //공격 쿨타임계산

    /*


    월드에서 공격터렛 엔티티목록을 가져옴.

    몬스터 엔티티목록들을 가져옴 (거리 비교 용도,  공격가능대상은 몬스터만 고려됨);

    for ( 공격터렛 엔티티목록의 수만큼 반복함 ) {

        float minDistance = Float.maxValue;
        int targetID = 0; // 가장 가까운 애가  누구
        int targetType = None없음 (int); // 누구의 타입을 지정. (structureID or UnitID);


        //몬스터 앤티티의 포지션 컴포넌트 가져옴
        for( 몬스터 엔티티목록 개수만큼 반복 ){

            공격타워 자신의 엔티티.
            가져온 몬스터 엔티티.

            //거리계산
            float currentDistance = (공격 포탑 자신의 위치 - 가져온 몹의 위치).길이;

            float targetHP = target의 HP컴포넌트에서 가져온 현재 체력;
            float isUntargetable = target상태이상컴포넌트에서 가져온 Untargetable변수;

                //가장 가까운 타겟을 선택함.
                if( currentDistance < minDistance && targetHP > 0 && isUntargetable == false)
                {
                    minDistance = currentDistance;
                    targetID = 가져온 공격 포탑의 ID;
                    targetType = 몬스터(int); //지울 가능성 있음.
                }
        }

        //타겟이 존재한다면,
        if(targetID != 0 && targetType != None)
        {
            if( 남아있는 공격쿨타임 <= 0 && 타워 자신의 상태이상변수 isAttackable == true)
            {
                공격처리 실행.  (투사체 생성)

                투사체에 포탑 자기자신의 ID, 투사체ID를 발급함, 포탑 투사체라는 종류의 createdSkillID, 투사체속도, 타겟ID
                를 지정한다.

                그리고 공격이 적중하면, 해당 투사체에서 데미지 처리를 행한다.

                남아있는 공격 쿨타임 = (float)1/공격속도;  공격속도가 2라면,  1/2 = 0.5초
            }
         }

         //공격 터렛의 공격쿨타임이 0보다 크다면, tick만큼 감소.
         if(남아있는 공격쿨타임 >= 0)
              남아있는 공격 쿨타임 -= deltaTime //틱레이트만큼 남아있는 쿨타임 깎기
    }

    */


}
