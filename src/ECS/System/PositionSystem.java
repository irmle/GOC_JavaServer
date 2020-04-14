package ECS.System;

import ECS.Classes.MapInfo;
import ECS.Classes.Vector3;
import ECS.Entity.CharacterEntity;
import ECS.Factory.MapFactory;
import ECS.Game.WorldMap;

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

                System.out.println("이동 가능하므,, 포지션 시스템 ");

                /* 이동할 위치를 가져온다 */
                Vector3 movedPos = worldMap.charNextPosList.get(character.entityID);
                MapInfo movedTile = MapFactory.findTileByPosition(worldMap.gameMap, movedPos.x(), movedPos.z());

                System.out.println("최종목적 좌표 : " + movedPos.x() + ", "
                        + movedPos.z() );

                System.out.println("최종목적 타일 : " + movedTile.arrayX + ", "
                        + movedTile.arrayY );

                /* 이동 단위 벡터를 구한다 */
                //Vector3 unitVec3 = (Vector3) movedPos.clone();
                Vector3 unitVec3 = Vector3.getTargetDirection(character.positionComponent.position, movedPos);
                unitVec3 = unitVec3.normalize();
                unitVec3.setSpeed(deltaTime);   // 흠..

                System.out.println("단위벡터  : " + unitVec3.x() + ", "
                        + unitVec3.z() );

                /* 이동 가능한 지점 혹은 목표지점에 도달할 때 까지 반복 */
                while (true){

                    /* 캐릭터의 기존 위치에, 단위벡터를 더한다(단위백터만큼 이동시킨다) */
                    Vector3 currentPos = (Vector3)character.positionComponent.position.clone();
                    currentPos.movePosition(currentPos, unitVec3);

                    /* 위 좌표의 타일을 검사한다 */
                    MapInfo currentTile = MapFactory.findTileByPosition(worldMap.gameMap, currentPos.x(), currentPos.z());
                    System.out.println("현재이동된 좌표 : " + currentPos.x() + ", "
                            + currentPos.z() );
                    System.out.println("타일 좌표 : " + currentTile.arrayX + ", "
                            + currentTile.arrayY );

                    /** 벽 등 이동 불가능한 타일인 경우  */
                    if(currentTile.canMove == false){
                        System.out.println("이동 불가능한 지점에 다다라서 멈춤");
                        System.out.println("현재 좌표 : " + character.positionComponent.position.x() + ", "
                                + character.positionComponent.position.z() );
                       break;
                    }


                    /** 경계타일인 경우 */
                    /* 현재 타일이 경계인지 아닌지 확인한다 */
                    int mapSize = worldMap.gameMap.length;
                    boolean isBorderTile =
                            ((currentTile.arrayX == (mapSize-1))
                                    || (currentTile.arrayY == (mapSize-1)))
                            ? true : false;

                    if(isBorderTile){
                        /* 경계 타일(현재 타일)의 중점 좌표를 최종 좌표로 지정한다 */
                        // 아님 얘도 걍 여기서 멈출수도 잇고..
                        // 아래 처럼, 현재 위치와 중점좌표와의 거리가 얼마 이하일 때에 그렇게 해주도록 처리할 수도 있고..

                        character.positionComponent.position.set(currentTile.getPixelPosition());

                        System.out.println("경계 타일에 다다라서 멈춤");
                        System.out.println("현재 좌표 : " + character.positionComponent.position.x() + ", "
                                + character.positionComponent.position.z() );

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

                        System.out.println("목적지 타일에 다다라서 멈춤");
                        System.out.println("현재 좌표 : " + character.positionComponent.position.x() + ", "
                                + character.positionComponent.position.z() );

                        break;
                    }
                    else{
                        /* 아직 도달하지 않았다면, 위에서 계산한 현재 좌표를 캐릭터의 좌표로 업데이트 한다 */

                        character.positionComponent.position.set(currentPos);


                        System.out.println("목적지 타일에 다다르지 않아 계속함");
                        System.out.println("현재 좌표 : " + character.positionComponent.position.x() + ", "
                                + character.positionComponent.position.z() );
                        // 벽을 만나거나, 경계에 도달하거나 목적지에 도달할 때 까지 반복한다

                    }
                }

                worldMap.charNextPosList.remove(character.entityID);


            }
        }
    }

}