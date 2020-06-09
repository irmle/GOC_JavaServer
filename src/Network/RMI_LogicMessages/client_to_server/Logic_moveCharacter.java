package Network.RMI_LogicMessages.client_to_server;

import ECS.Entity.CharacterEntity;
import ECS.Game.*;
import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;

public class Logic_moveCharacter {

    //Called_RMI(Remote Method Invocation)_Method
    public static void RMI_Packet(RMI_ID rmi_id, short rmi_ctx, int worldMapID, CharacterMoveData characterMoveData)
    {
        //do something.
        //System.out.println("worldMapID = "+worldMapID+ " / entityID ="+ characterMoveData.entityID+" "+characterMoveData.posX+"/"+characterMoveData.posY+"/"+characterMoveData.posZ);
        //System.out.println("worldMapID = "+worldMapID+ " / entityID ="+ characterMoveData.entityID+" "+characterMoveData.velX+"/"+characterMoveData.velY+"/"+characterMoveData.velZ);


        /* 유저가 속한 월드를 찾는다 */
        WorldMap worldmap = MatchingManager.findWorldMapFromWorldMapID(worldMapID);

        if(worldmap == null)
            System.out.println("worldmap == null");

        CharacterEntity character = worldmap.characterEntity.get(characterMoveData.entityID);
        if( worldmap.checkUserIsDead(character)){

            return;
        }

        if(character == null)
            System.out.println("character == null");

        if(character.positionComponent == null)
            System.out.println("character.positionComponent == null");

        if(character.positionComponent.position == null)
            System.out.println("character.positionComponent.position == null");

        /*if(character.conditionComponent.isDisableMove == false){

            character.positionComponent.position.set(characterMoveData.posX, characterMoveData.posY, characterMoveData.posZ);
            *//*System.out.println("이동" + characterMoveData.posX + ", " +
                characterMoveData.posY + ", " + characterMoveData.posZ);*//*

            character.velocityComponent.velocity.set(characterMoveData.velX, characterMoveData.velY, characterMoveData.velZ);
        }*/

        /*System.out.println("이동 좌표 : " + characterMoveData.posX + ", "
                + characterMoveData.posY + ", " + characterMoveData.posZ );
        System.out.println("회전방향 : " + characterMoveData.quarternionY + ", "
                + characterMoveData.quarternionZ );
        System.out.println("속력 : " + characterMoveData.velX + ", "
                + characterMoveData.velY + ", " + characterMoveData.velZ );
*/

        character.rotationComponent.y = characterMoveData.quarternionY;
        character.rotationComponent.z = characterMoveData.quarternionZ;
        character.velocityComponent.velocity.set(characterMoveData.velX, characterMoveData.velY, characterMoveData.velZ);

        //System.out.println("이동방향 벡터 크기 : " + character.velocityComponent.velocity.length());
        //character.velocityComponent.velocity.set(characterMoveData.velX, characterMoveData.velY, characterMoveData.velZ);
//
//        if (MapFactory.moveCheck(worldmap.gameMap,characterMoveData.posX,characterMoveData.posZ)) {
//            System.out.println("캐릭터 좌표 x : "+characterMoveData.posX+"  /  캐릭터 좌표 y : "+ characterMoveData.posZ);
//            System.out.println("이동가능");
//            character.positionComponent.position.set(characterMoveData.posX, characterMoveData.posY, characterMoveData.posZ);
//            character.velocityComponent.velocity.set(characterMoveData.velX, characterMoveData.velY, characterMoveData.velZ);
//        }else {
//            character.velocityComponent.velocity.set(0,0,0);
//            System.out.println("캐릭터 좌표 x : "+characterMoveData.posX+"  /  캐릭터 좌표 y : "+ characterMoveData.posZ);
//            System.out.println("이동불가능");
//        }


            /*System.out.println("이동" + characterMoveData.posX + ", " +
                characterMoveData.posY + ", " + characterMoveData.posZ);*/



    }
}