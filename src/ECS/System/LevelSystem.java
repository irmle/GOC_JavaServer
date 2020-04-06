package ECS.System;

import ECS.Components.CharacterComponent;
import ECS.Entity.CharacterEntity;
import ECS.Game.WorldMap;

import java.util.HashMap;

public class LevelSystem {

    public WorldMap worldMap;



    public LevelSystem(WorldMap worldMap) {

        this.worldMap = worldMap;
    }

    public void onUpdate(float deltaTime)
    {
        // 레벨 컴포넌트를 갖는 앤티티 : 캐릭터앤티티

        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterComponent characterComponent = characterEntity.getValue().characterComponent;

            int maxExp;
            while(true){

                // 월드맵의 expTable은 HashMap<Integer, Integer>이다.
                // 별도 파일로부터 각 레벨과 레벨별 최대경험치량을 읽어들였다고 가정.


               /* maxExp = worldMap.expTable.get(characterComponent.level);

                if(maxExp <= characterComponent.exp){
                    characterComponent.level++;

                    characterComponent.exp -= maxExp;
                }
                else{
                    break;
                }*/
            }

        }


    }


    //경험치, 레벨가 존재하는 CharacterComponent 를 가진, Entity를 가져옴.
    //경험치량과 레벨을 체크하여, 현재 경험치량이 레벨업 조건을 만족하는 경험치를 넘어섰다면,
    //레벨업 처리를 하는것이 목적.

    /*


    월드의 엔티티 목록 = <CharacterComponent> 컴포넌트를 갖는 모든 엔티티.

    for( 월드의 엔티티 목록 개수만큼 반복 )
    {
        해당하는 CharacterComponent 한개, 가져옴.

        미리 로드가 되어있는 경험치테이블 정보와, 현재 가져온 CharacterComponent의 경험치값, 레벨값을 비교하여

        현재 경험치량이 레벨업 조건을 만족하는 경험치를 넘어섰는지 여부를 판별.

        EXPTable<level(int), exp(int)>

        EXPTable에 level을 입력하면 나오는 exp [EXPTABLE_EXP] 와,  현재 가져온 CharacterComponent의 경험치값 [CurrentEXP]을 비교.

        int delta = CurrentEXP - EXPTABLE_EXP;

        만약 가져온 CharacterComponent의 경험치값이 더 크다면,
        if(EXPTABLE_EXP < CurrentEXP)
        {
            level ++;



            //레벨업을 한 후의, 다음 레벨에 도달하기 위한 요구 경험치량과, 남은 요구 경험치량과 비교하여,
            //남은 요구 경험치량이, 다음 레벨에 도달할 수 있는 경험치량을 넘었다면.
            while ( [EXPTable<level(int), exp(int)>의 EXP]  < delta)
            {
                level ++;
                delta = CurrentEXP - EXPTABLE_EXP;
            }

            CharacterComponent.level = level;
        }

        CharacterComponent.CurrentEXP = delta;
    }

     */

    /* 포문버전... 혹시나 해서 남겨둠
    int count = worldMap.characterEntity.size();
        for(int i=0; i<count; i++){

            CharacterComponent characterComponent = worldMap.characterEntity.get(i).character;

            int maxExp;
            while(true){

                // 월드맵의 expTable은 HashMap<Integer, Integer>이다.
                // 별도 파일로부터 각 레벨과 레벨별 최대경험치량을 읽어들였다고 가정.
                maxExp = worldMap.expTable.get(characterComponent.level);

                if(maxExp <= characterComponent.exp){
                    characterComponent.level++;

                    characterComponent.exp -= maxExp;
                }
                else{
                    break;
                }
            }

        }
     */
}
