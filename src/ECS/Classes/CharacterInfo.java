package ECS.Classes;


import ECS.Classes.Type.CharacterType;
import ECS.Classes.Type.ElementalType;

import java.util.ArrayList;

//저장되어있는 캐릭터정보
public class CharacterInfo implements Cloneable {   //team정보등은 없어도 된다.

    //캐릭터 타입
    public int characterType = CharacterType.NONE;

    //캐릭터 속성
    public int characterElemental = ElementalType.BLUE;

    //기본 이동속도
    public float moveSpeed = 0f;

    //기본 최대체력
    public float maxHP = 0f;

    //기본 1초당 체력 회복량
    public float recoveryRateHP = 0f;

    //기본 최대마나
    public float maxMP = 0f;

    //기본 1초당 마나 회복량
    public float recoveryRateMP = 0f;

    //기본 공격력
    public float attackDamage = 0f;

    //기본 공격속도 (공격시 쿨타임. 1초당 공격가능 횟수)
    public float attackSpeed = 0f;

    //기본 공격 사거리
    public float attackRange = 0f;

    //기본 방어력
    public float defense = 0f;

    //기본 시야 거리
    public float lookRadius = 0f;

    //사용 가능한 스킬 종류 정보. 캐릭터종류당 3개.
    public ArrayList<SkillInfo> useableSkillList = new ArrayList<>();


    @Override
    public Object clone()  {
        CharacterInfo clone;
        try {
            clone = (CharacterInfo) super.clone();


        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return clone;
    }
}

