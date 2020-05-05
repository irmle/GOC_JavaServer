package ECS.Game;

import java.util.LinkedList;
import Network.AutoCreatedClass.*;

/**
 * WorldMap 에서, requestCreateEntity큐, requestDestroyEntity큐에 쌓인, 생성 삭제 요청을 처리할 때
 * 월드맵의 Entity HashMap에 추가/삭제 함과 동시에 추가된, 삭제된 목록들을 각 클라이언트들에게 중계를 해야하는데,
 * 이때 중계 할때마다 일일이 new LinkedList 8가지를 만들어서 쓰는것은 비효율적이라고 생각하였으므로
 *
 * new LinkedList 8가지를 묶어놓은 이 클래스 [ WorldMapDataListSet ] 를 만들어서 월드맵에 하나 할당해두고,
 * 매 Tick마다 처리할때, clear()를 호출하면서
 *
 *
 * 재사용 하는 식으로 활용하기 위해 만들어진 클래스.
 *
 *
 * 2020 02 17 권령희
 * 수정할 것 :
 *  LinkedList<BuildSlotDta> buildSlotData 추가하기.
 *      BuildSlotData는 RMI.AutoCreatedClass 여기에 들어가야 하는데, 아마.. RMI 추가 처리가 필요한 듯.
 *
 *
 */

public class WorldMapDataListSet {
    LinkedList<CharacterData> characterData;
    LinkedList<MonsterData> monsterData;
    LinkedList<AttackTurretData> attackTurretData;
    LinkedList<BuffTurretData> buffTurretData;
    LinkedList<BarricadeData> barricadeData;
    LinkedList<CrystalData> crystalData;
    LinkedList<SkillObjectData> skillObjectData;
    LinkedList<FlyingObjectData> flyingObjectData;

    /** 2020 02 17 권령희 */
    LinkedList<BuildSlotData> buildSlotData;

    /** 2020 03 06 권령희 */
    LinkedList<StoreUpgradeBuffSlotData> storeUpgradeBuffSlotData;

    //생성자.
    public WorldMapDataListSet() {
        characterData = new LinkedList<>();
        monsterData = new LinkedList<>();
        attackTurretData = new LinkedList<>();
        buffTurretData = new LinkedList<>();
        barricadeData = new LinkedList<>();
        crystalData = new LinkedList<>();
        skillObjectData = new LinkedList<>();
        flyingObjectData = new LinkedList<>();

        buildSlotData = new LinkedList<>();
        storeUpgradeBuffSlotData = new LinkedList<>();
    }


    //다음 틱에 도달했을 때, 재사용을 위해서 일괄적으로 clear() 해주는 부분.
    public void clear() {
        characterData.clear();
        monsterData.clear();
        attackTurretData.clear();
        buffTurretData.clear();
        barricadeData.clear();
        crystalData.clear();
        skillObjectData.clear();
        flyingObjectData.clear();

        buildSlotData.clear();
        storeUpgradeBuffSlotData.clear();
    }
}
