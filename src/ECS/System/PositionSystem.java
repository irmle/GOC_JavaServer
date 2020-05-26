package ECS.System;

import ECS.Classes.MapInfo;
import ECS.Classes.Vector3;
import ECS.Components.PositionComponent;
import ECS.Components.VelocityComponent;
import ECS.Entity.CharacterEntity;
import ECS.Entity.Entity;
import ECS.Entity.MonsterEntity;
import ECS.Factory.MapFactory;
import ECS.Game.WorldMap;
import Network.RMI_Common.RMI_ParsingClasses.EntityType;

import java.util.HashMap;

/**
 * 작 성 자 : 권령희
 * 작성날짜 : 2020 02 19
 * 업뎃날짜 :
 * 목    적 :
 *      캐릭터의 이동 처리 및 위치 보정을 위한 시스템이다.
 *      캐릭터 시스템 이후에 돌게 됨.
 * 로   직 :
 *

 */
public class PositionSystem {

    WorldMap worldMap;

    public PositionSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }


    public void onUpdate(float deltaTime) {

        /** 캐릭터 */
        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();

            /** 죽은 애 패스 처리 */
            if( worldMap.checkUserIsDead(character)){

                continue;
            }

            /* 이동 가능한 상태의 캐릭터만 처리한다 */
            /** 2020 02 19 수 수정, 권령희 */
            /**
             * 원래는 아래 주석과 같이 캐릭터의 이동 가능/불가능 여부를 가지고 이동 및 위치보정 처리 실행 여부를 결정했는데,
             * 그렇게 하니까
             * 전사 찌르기 스킬의 경우, 캐릭터의 movable 상태를 비활성화 시키고 스킬 투사체의 움직임을 따라가는 방식이었는데
             * 아래 이동가능 여부에서 짤려버려서.. 이동을 안하게 됨 (스킬 효과(데미지)는 먹히겠지만
             * 그래서.. 이동/위치보정 처리가 성공적으로 끝나면, 월드맵의 nextPos를 지워주고,
             * nextPos가 있는 경우에만 돌리도록 함.
             * ㄴ 캐릭터가 이동 가능한 경우, characterSystem에서 nextPos를 채워줄 것이므로 아래 처리가 가능하고
             *      찌르기 스킬을 써서 이동이 불가능한 상태일 경우, 투사체 시스템에서 투사체가 이동한 위치를 nextPos로 넣어줄 것이므로
             *      characterSystem이 nextPos를 덮어쓰는 것도 막고, 아래 로직도 돌릴 수 있다.
             *
             */


            //boolean isMovable = character.conditionComponent.isDisableMove ? false : true;
            boolean isMovable = worldMap.charNextPosList.containsKey(character.entityID) ? true : false;
            if(isMovable){

                /* 이동할 위치를 가져온다 */
                Vector3 movedPos = worldMap.charNextPosList.get(character.entityID);
                MapInfo movedTile = MapFactory.findTileByPosition(worldMap.gameMap, movedPos.x(), movedPos.z());


                /* 이동 단위 벡터를 구한다 */
                //Vector3 unitVec3 = (Vector3) movedPos.clone();
                Vector3 unitVec3 = Vector3.getTargetDirection(character.positionComponent.position, movedPos);
                unitVec3 = unitVec3.normalize();
                unitVec3.setSpeed(deltaTime);

                /* 이동 가능한 지점 혹은 목표지점에 도달할 때 까지 반복 */
                while (true){

                    /* 캐릭터의 기존 위치에, 단위벡터를 더한다(단위백터만큼 이동시킨다) */
                    Vector3 currentPos = (Vector3)character.positionComponent.position.clone();
                    currentPos.movePosition(currentPos, unitVec3);

                    /* 위 좌표의 타일을 검사한다 */
                    MapInfo currentTile = MapFactory.findTileByPosition(worldMap.gameMap, currentPos.x(), currentPos.z());

                    /*System.out.println("현재이동된 좌표 : " + currentPos.x() + ", "
                            + currentPos.z() );
                    System.out.println("타일 좌표 : " + currentTile.arrayX + ", "
                            + currentTile.arrayY );*/

                    /** 벽 등 이동 불가능한 타일인 경우  */
                    if(currentTile.canMove == false){

                        /**
                         * 작성날짜 : 2020 05 19 화요일
                         * 목    적 : 벽 등 이동 불가능한 지점에 도달했을 때,
                         *              벽을 향해 수직/수평으로 달리고 있는 게 아니라면
                         *              마냥 제자리걸음 하지 말고
                         *              이동하고자 하는 방향에 가깝게 벽을 타고 내려가든 달려가든
                         *              매끄러운 처리가 되도록 수정.
                         *
                         *
                         */

                        /* 아래 조건 안쓰임. 일단 남겨둠.
                        boolean isHorizontal = ( Math.round(character.velocityComponent.velocity.z()) == 0) ? true : false;
                        boolean isVertical = ( Math.round(character.velocityComponent.velocity.x()) == 0) ? true : false;

                        if(!(isHorizontal && isVertical)){

                        }
                        */

                        //System.out.println("매끄럽게 이동 ");

                        /** ... */

                        Vector3 newPosXChanged = (Vector3) character.positionComponent.position.clone();
                        newPosXChanged.x(movedPos.x());

                        Vector3 newPosZChanged = (Vector3) character.positionComponent.position.clone();
                        newPosZChanged.z(movedPos.z());


                        if(MapFactory.moveCheck(worldMap.gameMap, newPosXChanged.x(), newPosXChanged.z()) == true){

                            character.positionComponent.position.set(newPosXChanged);
                            MapFactory.findTileByPosition(worldMap.gameMap, newPosXChanged.x(), newPosXChanged.z());
                        }
                        else if(MapFactory.moveCheck(worldMap.gameMap, newPosZChanged.x(), newPosZChanged.z()) == true){

                            character.positionComponent.position.set(newPosZChanged);
                            MapFactory.findTileByPosition(worldMap.gameMap, newPosZChanged.x(), newPosZChanged.z());
                        }


                        worldMap.charNextPosList.remove(character.entityID);


                        break;
                    }


                    /** 경계타일인 경우 */
                    /* 현재 타일이 경계인지 아닌지 확인한다 */
                    int mapSize = worldMap.gameMap.length;
                    boolean isBorderTile =
                            ((currentTile.arrayX >= (mapSize-1)) || (currentTile.arrayX <= 0)
                                    || (currentTile.arrayY >= (mapSize-1)) || (currentTile.arrayY <= 0))
                                    ? true : false;

                    if(isBorderTile){
                        /* 경계 타일(현재 타일)의 중점 좌표를 최종 좌표로 지정한다 */
                        // 아님 얘도 걍 여기서 멈출수도 잇고..
                        // 아래 처럼, 현재 위치와 중점좌표와의 거리가 얼마 이하일 때에 그렇게 해주도록 처리할 수도 있고..

                        //character.positionComponent.position.set(currentTile.getPixelPosition());

                        /*System.out.println("경계 타일에 다다라서 멈춤");
                        System.out.println("현재 좌표 : " + character.positionComponent.position.x() + ", "
                                + character.positionComponent.position.z() );*/

                        break;
                    }


                    /** 이동 가능한 타일의 경우 */
                    /* 목적지 타일에 도달했는지 확인한다 */
                    boolean arrived = (movedTile == currentTile) ? true : false;
                    if(arrived){
                        /* 도달했다면, 목적지 좌표를 현재 위치로 대입한다 */
                        // 나중에 좀 수정할수도.. 목적좌표와 현재 거리 차가 1 혹은 0.n 이하일 때, 대입하는걸로.. ??
                        // 당장은 이게 클라에서 어케 그려질지 잘 모르겟네.

                        character.positionComponent.position.set(movedPos);


                        /*System.out.println("목적지 타일에 다다라서 멈춤");
                        System.out.println("현재 좌표 : " + character.positionComponent.position.x() + ", "
                                + character.positionComponent.position.z() );*/

                        break;
                    }
                    else{
                        /* 아직 도달하지 않았다면, 위에서 계산한 현재 좌표를 캐릭터의 좌표로 업데이트 한다 */
                        // 벽을 만나거나, 경계에 도달하거나 목적지에 도달할 때 까지 반복한다

                        character.positionComponent.position.set(currentPos);

                        /*System.out.println("목적지 타일에 다다르지 않아 계속함");
                        System.out.println("현재 좌표 : " + character.positionComponent.position.x() + ", "
                                + character.positionComponent.position.z() );*/
                    }
                }

                worldMap.charNextPosList.remove(character.entityID);

            }


        }



        /** 2020 05 14 */
        /** 끼인거 처리; 일단은 캐릭터만. */
        for(HashMap.Entry<Integer, Vector3> stuckEntity : worldMap.charNextPosList_gotStuck.entrySet()){

            int type = worldMap.entityMappingList.get(stuckEntity.getKey());
            Entity entity;

            Vector3 entityPos;
            VelocityComponent entityVelocity;
            switch (type){

                case EntityType.CharacterEntity :

                    entity = worldMap.characterEntity.get(stuckEntity.getKey());
                    entityPos = entity.positionComponent.position;
                    entityVelocity = ((CharacterEntity) entity).velocityComponent;

                    break;

                case EntityType.MonsterEntity :

                    entity = worldMap.monsterEntity.get(stuckEntity.getKey());
                    entityPos = entity.positionComponent.position;
                    entityVelocity = ((MonsterEntity) entity).velocityComponent;

                    break;

                default:

                    continue;
            }


            /* 이동할 위치를 가져온다 */
            Vector3 movedPos = (Vector3) stuckEntity.getValue().clone();
            //MapInfo movedTile = MapFactory.findTileByPosition(worldMap.gameMap, movedPos.x(), movedPos.z());

            /* 이동 단위 벡터를 구한다 */
            Vector3 unitVec3 = Vector3.getTargetDirection(entityPos, movedPos);
            unitVec3 = unitVec3.normalize();
            unitVec3.setSpeed(deltaTime);

            /*System.out.println("이동량 : " + unitVec3.x() + ", "
                    + unitVec3.y() + ", " + unitVec3.z());*/


            /* 이동 가능한 지점 혹은 목표지점에 도달할 때 까지 반복 */
            while (true){

                /* 캐릭터의 기존 위치에, 단위벡터를 더한다(단위백터만큼 이동시킨다) */
                Vector3 currentPos = (Vector3)entityPos.clone();
                currentPos.movePosition(currentPos, unitVec3);

                entityPos.set(currentPos);

                /* 위 좌표의 타일을 검사한다 */
                MapInfo currentTile = MapFactory.findTileByPosition(worldMap.gameMap, currentPos.x(), currentPos.z());

                /** 이동 가능한 타일인 경우  */
                if(currentTile.canMove == true){
                    break;
                }

            }

        }

        worldMap.charNextPosList_gotStuck.clear();


    }

}
