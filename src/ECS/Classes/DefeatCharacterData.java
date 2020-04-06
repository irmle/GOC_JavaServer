package ECS.Classes;

import ECS.Entity.CharacterEntity;

//사망한 캐릭터Entity와 남은 부활시간을 카운트하기위한 객체
public class DefeatCharacterData {

    public CharacterEntity defeatCharacter;
    public float remainRespawnTimeMilliSeconds;

    public DefeatCharacterData() {
    }

    public DefeatCharacterData(CharacterEntity defeatCharacter, float remainRespawnTimeMilliSeconds) {
        this.defeatCharacter = defeatCharacter;
        this.remainRespawnTimeMilliSeconds = remainRespawnTimeMilliSeconds;
    }
}
