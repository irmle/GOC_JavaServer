package ECS.System;

import ECS.Classes.DamageHistory;
import ECS.Components.MPComponent;
import ECS.Components.MPHistoryComponent;
import ECS.Entity.*;
import ECS.Game.WorldMap;

import java.util.HashMap;
import java.util.List;

public class MpHistorySystem {

    public WorldMap worldMap;

    float coolTime;
    float remainCoolTime;

    public HashMap<Integer, Entity> worldEntityList;
    public HashMap<Integer, MPHistoryComponent> mpHistoryComponents;

    public MpHistorySystem(WorldMap worldMap, float coolTime) {
        this.worldMap = worldMap;
        this.coolTime = coolTime;
        this.remainCoolTime = this.coolTime;
    }

    public void onUpdate(float deltaTime)
    {

        // 시스템 도는 주기 조절용 코드
        /*remainCoolTime -= worldMap.tickRate;
        if(remainCoolTime <= 0){
            remainCoolTime = coolTime;
        }
        else{
            return;
        }*/


        /* mp히스토리 컴포넌트만 갖고오기 */
        for (HashMap.Entry<Integer, CharacterEntity> characterEntity : worldMap.characterEntity.entrySet()) {

            CharacterEntity character = characterEntity.getValue();
            if( worldMap.checkUserIsDead(character)){

                character.mpHistoryComponent.mpHistory.clear();
                continue;
            }

            MPHistoryComponent mpHistoryList = characterEntity.getValue().mpHistoryComponent;
            List<DamageHistory> damageHistories = mpHistoryList.mpHistory;

            // mp 총량 계산
            float sum = 0;
            for (int j=0; j<damageHistories.size(); j++){

                DamageHistory damageHistory = damageHistories.get(j);
                if(damageHistory.isDamage == true){
                    //System.out.println("마력을 소모합니다 : " + damageHistory.amount);
                    sum -= damageHistory.amount;
                }
                else{
                    //System.out.println("마력을 회복합니다 : " + damageHistory.amount);
                    sum += damageHistory.amount;
                }
            }

            // 해당 앤티티의 mp 컴포넌트에 접근, 반영
            MPComponent mp = characterEntity.getValue().mpComponent;

            float tempMpValue = mp.currentMP + sum;
            if( tempMpValue <= 0f){
                mp.currentMP = 0f;
            }
            else if( mp.maxMP > tempMpValue ){
                mp.currentMP = tempMpValue;
            }
            else if(mp.maxMP <= tempMpValue){
                mp.currentMP = mp.maxMP;
            }
            //System.out.println("현재 마력 : " + mp.currentMP);

            character.mpHistoryComponent.mpHistory.clear();

        }



    }


    //월드의 모든 Entity 를 가져와서, MP히스토리의 내역을 일괄적용후 최종 MP 상태를 확정함.

    /*

    월드의 엔티티 목록 = <mpHistory> 컴포넌트를 갖는 모든 엔티티.

    for(월드의 엔티티 목록 개수만큼 반복)
    {
        해당 엔티티1개의 MPHistory 리스트 객체 가져옴.

        //적용될 mp 총량.
        float sum;
        for ( MPHistory 리스트 개수만큼 반복 )
        {
            if(MPHistory 리스트 항목, isDamage == true)
                sum -= Amount  //소모량이라면 마이너스
            else
                sum += Amount  //회복량이라면 플러스
        }
        해당 엔티티의 MP컴포넌트에 접근,  현재 MP를 가져옴

        //현재 mp에 SUM을 반영함.
        현재MP += SUM;
    }


    */

    /* for문 버전
    int size = worldMap.characterEntity.size();
        for(int i=0; i<size; i++){

            MPHistoryComponent mpHistoryList = worldMap.characterEntity.get(i).mpHistory;
            List<DamageHistory> damageHistories = mpHistoryList.mpHistory;

            // mp 총량 계산
            float sum = 0;
            for (int j=0; j<damageHistories.size(); j++){

                DamageHistory damageHistory = damageHistories.get(j);
                if(damageHistory.isDamage == true){
                    sum -= damageHistory.amount;
                }
                else{
                    sum += damageHistory.amount;
                }
            }

            // 해당 앤티티의 mp 컴포넌트에 접근, 반영
            MPComponent mp = worldMap.characterEntity.get(i).mp;

            float tempMpValue = mp.currentMP + sum;
            if(tempMpValue >= mp.maxMP){
                mp.currentMP = mp.maxMP;
            }
            else if(tempMpValue <= 0){
                mp.currentMP = 0;
            }
            else{
                mp.currentMP = tempMpValue;
            }



        }
     */
}
