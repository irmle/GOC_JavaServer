package ECS.Classes;

/**
 * 업뎃날짜 : 2020 01 28 화요일 권령희
 * 업뎃 내용 :
 *      새로운 스킬을 추가됨에 따라, 레벨 별로 달라지는 스킬 정보에 들어갈 별도 데이터가 등장하여 새로운 변수와 생성자를 작성
 *      >> 스킬별로 생성자 별도 정의 해둬야 겠음. 일단 이전에 구현된 스킬들 제외
 *
 *      >> 전사
 *      >> 마법사
 *      >> 궁수
 *
 */
public class SkillInfoPerLevel implements Cloneable {

    public int level;                  // 스킬의 레벨
    public float durationTime;         // 스킬 효과 지속 시간
    public float coolTime;             // 스킬 쿨타임

    public float attackDamage;         // 공격력
    public float range;                // 사거리
    public float attackRange;          // 공격 범위

    public float flyingObjectSpeed;    // 투사체(인 경우) 속도

    // 2020 01 23 추가
    public float moveSpeedRate;     // 이동속도 추가 비율, 전사 가렌 Q 스킬의 경우
    public float attackSpeedRate;   // 공격속도 추가 비율, 전사 버서커 스킬의 경우 & 궁수 속공 스킬의 경우
    public float bloodSuckingRate;  // 흡혈 비율, 전사 버서커 스킬의 경우
    public float maxHpRate;         // 최대체력 증가 비율, 전사 피뻥 스킬의 경우
    public float freezingTime;      // 빙결 시간. 마법사 프로즌빔 스킬의 경우
    public float criticalChanceRate;      // 치명타 발생 확률, 궁수 치명타 스킬의 경우
    public float criticalDamageRate;      // 치명타 대미지 비율, 궁수 치명타 스킬의 경우
    public float criticalBonusDamageRate;   // 치명타 추가 대미지 비율, 궁수 헤드샷 스킬의 경우 & 궁수 저격 스킬의 경우

    public float requireMP;


    /* 2020 05 20 추가 ; 스킬 데미지 계산 시 적용할 각종 스탯 계수 */
    public float attackDamageCoefficient;
    public float defenseCoefficient;
    public float attackSpeedCoefficient;
    public float maxHPCoefficient;
    public float maxMPCoefficient;

    /* 2020 05 31 추가 ; 전사 토네이도 에이본 효과 처리를 위한 값 */
    public float maxHeight;

    public SkillInfoPerLevel(int level, float durationTime, float coolTime, float attackDamage, float range, float attackRange, float flyingObjectSpeed, float moveSpeedRate, float attackSpeedRate, float bloodSuckingRate, float maxHpRate, float freezingTime, float criticalChanceRate, float criticalDamageRate, float criticalBonusDamageRate, float attackDamageCoefficient, float defenseCoefficient, float attackSpeedCoefficient, float maxHPCoefficient, float maxMPCoefficient, float maxHeight) {
        this.level = level;
        this.durationTime = durationTime;
        this.coolTime = coolTime;
        this.attackDamage = attackDamage;
        this.range = range;
        this.attackRange = attackRange;
        this.flyingObjectSpeed = flyingObjectSpeed;
        this.moveSpeedRate = moveSpeedRate;
        this.attackSpeedRate = attackSpeedRate;
        this.bloodSuckingRate = bloodSuckingRate;
        this.maxHpRate = maxHpRate;
        this.freezingTime = freezingTime;
        this.criticalChanceRate = criticalChanceRate;
        this.criticalDamageRate = criticalDamageRate;
        this.criticalBonusDamageRate = criticalBonusDamageRate;

        this.attackDamageCoefficient = attackDamageCoefficient;
        this.defenseCoefficient = defenseCoefficient;
        this.attackSpeedCoefficient = attackSpeedCoefficient;
        this.maxHPCoefficient = maxHPCoefficient;
        this.maxMPCoefficient = maxMPCoefficient;

        this.maxHeight = maxHeight;
    }

    // 기존 스킬 &  새로 추가된 변수들을 사용하지 않는 경우에 사용하는 생성자. >> 이것도.. 나중에 좀 더 정교하게 바뀔 것.
    // 이 생성자 내에서 대입받는 변수들 중에서도 안쓰는 것이 있는 스킬들도 분명 있을 것이라..
    public SkillInfoPerLevel(int level, float attackDamage, float range, float attackRange, float flyingObjectSpeed, float durationTime, float coolTime) {
        this.level = level;
        this.attackDamage = attackDamage;
        this.range = range;
        this.attackRange = attackRange;
        this.flyingObjectSpeed = flyingObjectSpeed;
        this.durationTime = durationTime;
        this.coolTime = coolTime;

        moveSpeedRate = 0f;
        attackSpeedRate = 0f;
        bloodSuckingRate = 0f;
        maxHpRate = 0f;
        freezingTime = 0f;
        criticalChanceRate = 0f;
        criticalDamageRate = 0f;
        criticalBonusDamageRate = 0f;
        requireMP = 0f;

        this.attackDamageCoefficient = 1;
        this.defenseCoefficient = 1;
        this.attackSpeedCoefficient = 1;
        this.maxHPCoefficient = 1;
        this.maxMPCoefficient = 1;

        maxHeight = 0;
    }

    /**
     *
     * @param level
     * @param attackDamage
     * @param range
     * @param attackRange
     * @param durationTime
     * @param coolTime
     */
    public SkillInfoPerLevel(int level, float attackDamage, float range, float attackRange, float durationTime, float coolTime) {
        this.level = level;
        this.attackDamage = attackDamage;
        this.range = range;
        this.attackRange = attackRange;
        this.durationTime = durationTime;
        this.coolTime = coolTime;

        flyingObjectSpeed = 0f;
        moveSpeedRate = 0f;
        attackSpeedRate = 0f;
        bloodSuckingRate = 0f;
        maxHpRate = 0f;
        freezingTime = 0f;
        criticalChanceRate = 0f;
        criticalDamageRate = 0f;
        criticalBonusDamageRate = 0f;
        requireMP = 0f;
    }

    /**
     * 지속 시간조차 없는.. 진짜 기본 생성자?
     * @param level
     * @param coolTime
     */
    public SkillInfoPerLevel(int level, float coolTime) {
        this.level = level;
        this.coolTime = coolTime;

        durationTime = 0f;
        attackDamage = 0f;
        range = 0f;
        attackRange = 0f;
        flyingObjectSpeed = 0f;
        moveSpeedRate = 0f;
        attackSpeedRate = 0f;
        bloodSuckingRate = 0f;
        maxHpRate = 0f;
        freezingTime = 0f;
        criticalChanceRate = 0f;
        criticalDamageRate = 0f;
        criticalBonusDamageRate = 0f;
        requireMP = 0f;
    }

    /**
     *
     * @param level
     * @param durationTime
     * @param coolTime
     */
    public SkillInfoPerLevel(int level, float durationTime, float coolTime) {
        this.level = level;
        this.durationTime = durationTime;
        this.coolTime = coolTime;

        attackDamage = 0f;
        range = 0f;
        attackRange = 0f;
        flyingObjectSpeed = 0f;
        moveSpeedRate = 0f;
        attackSpeedRate = 0f;
        bloodSuckingRate = 0f;
        maxHpRate = 0f;
        freezingTime = 0f;
        criticalChanceRate = 0f;
        criticalDamageRate = 0f;
        criticalBonusDamageRate = 0f;
        requireMP = 0f;
    }

    /* 아씨.. 생성자.. '생성자'를 만들지 말고, 스킬별로 매서드를 만들어서, 그 내부에서 각각 필요한 생성자를 부르게 할까 */

    /********************************************************************************************************************/
    /** 전사 */

    /**
     * 전사 가렌Q 스킬의 레벨별 스킬정보 생성자 래핑함수.
     * 스킬 레벨에 따라 이동속도증가 비율이 달라진다.
     * @param level
     * @param attackDamage
     * @param range
     * @param attackRange
     * @param durationTime
     * @param coolTime
     * @param moveSpeedRate
     * @return
     */
    public static SkillInfoPerLevel knight_GarrenQ(int level, float attackDamage, float range, float attackRange, float durationTime, float coolTime, float moveSpeedRate){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, attackRange, durationTime, coolTime);
        newSkillInfo.moveSpeedRate = moveSpeedRate;

        return newSkillInfo;
    }

    /**
     * 전사 가렌E 스킬의 레벨별 스킬정보 생성자 래핑함수.
     * 파라미터 attackDamage 를 durationTime 으로 나눈 값 만큼의 대미지를
     *      지속시간동안 적에게 매 초 마다 주도록 한다.
     *      >> 내부 생성자에서 attackDamage 로 실제로 넣어주는 값이 다름에 주의
     * @param level
     * @param attackDamage
     * @param range
     * @param attackRange
     * @param durationTime
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel knight_GarrenE(int level, float attackDamage, float range, float attackRange, float durationTime, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage / 4, range, attackRange, durationTime, coolTime);

        return newSkillInfo;
    }

    /**
     * 전사 가렌R 스킬의 레벨별 스킬정보 생성자 래핑함수.
     * 원래는 ...
     * @param level
     * @param attackDamage
     * @param range
     * @param attackRange
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel knight_GarrenR(int level, float attackDamage, float range, float attackRange, float coolTime){

        /*SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, coolTime);
        newSkillInfo.attackDamage = attackDamage;
        newSkillInfo.range = range;
        newSkillInfo.attackRange = attackRange;*/

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, attackRange, 0, coolTime);

        return newSkillInfo;
    }

    /**
     * 전사 버서커 스킬의 레벨별 스킬정보 생성자 래핑함수
     * @param level
     * @param durationTime
     * @param coolTime
     * @param attackSpeedRate
     * @param bloodSuckingRate
     * @return
     */
    public static SkillInfoPerLevel knight_Berserker(int level, float durationTime, float coolTime, float attackSpeedRate, float bloodSuckingRate){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, durationTime, coolTime);
        newSkillInfo.attackSpeedRate = attackSpeedRate;
        newSkillInfo.bloodSuckingRate = bloodSuckingRate;

        return newSkillInfo;
    }

    /**
     * 전사 피뻥 스킬의 레벨별 스킬정보 생성자 래핑함수
     * @param level
     * @param durationTime
     * @param coolTime
     * @param maxHpRate
     * @return
     */
    public static SkillInfoPerLevel knight_maxHpIncr(int level, float durationTime, float coolTime, float maxHpRate){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, durationTime, coolTime);
        newSkillInfo.maxHpRate = maxHpRate;

        return newSkillInfo;
    }

    /**
     * 전사 무적 스킬의 레벨별 스킬정보 생성자 래핑함수
     * @param level
     * @param durationTime
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel knight_invincible(int level, float durationTime, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, durationTime, coolTime);

        return newSkillInfo;
    }

    /** 마법사 */

    /**
     * 라이트닝 로드
     * @param level
     * @param attackDamage
     * @param range
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel magician_LightningRoad(int level, float attackDamage, float range, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, 2.5f, 0, coolTime);

        return newSkillInfo;
    }

    /**
     * 아이스볼
     * attackRange ==>> 파이어볼 기준으로 2.5f로 되어있어서.. 일단 똑같이 함.
     * @param level
     * @param attackDamage
     * @param range
     * @param flyingObjectSpeed
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel magician_IceBall(int level, float attackDamage, float range, float flyingObjectSpeed, float coolTime){
        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, 2.5f, flyingObjectSpeed, 0, coolTime);

        return newSkillInfo;
    }

    /**
     * 쉴드
     * @param level
     * @param attackDamage
     * @param range
     * @param attackRange
     * @param durationTime
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel magician_Shield(int level, float attackDamage, float range, float attackRange, float durationTime, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, attackRange, durationTime, coolTime);

        return newSkillInfo;
    }

    /**
     * 아이스필드
     * @param level
     * @param attackDamage
     * @param range
     * @param attackRange
     * @param durationTime
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel magician_IceField(int level, float attackDamage, float range, float attackRange, float durationTime, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, attackRange, durationTime, coolTime);

        return newSkillInfo;
    }

    /**
     * 썬더
     * @param level
     * @param attackDamage
     * @param range
     * @param attackRange
     * @param flyingObjectSpeed
     * @param durationTime
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel magician_Thunder(int level, float attackDamage, float range, float attackRange, float flyingObjectSpeed, float durationTime, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, attackRange, flyingObjectSpeed, durationTime, coolTime);

        return newSkillInfo;
    }

    /**
     * 프로즌빔
     * @param level
     * @param attackDamage
     * @param range
     * @param freezingTime
     * @param durationTime
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel magician_FrozenBeam(int level, float attackDamage, float range, float freezingTime, float durationTime, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, 0f, durationTime, coolTime);
        newSkillInfo.freezingTime = freezingTime;

        return newSkillInfo;
    }


    /** 궁수 */

    /**
     * 속공
     * @param level
     * @param attackSpeedRate
     * @param durationTime
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel archer_IncAttackSpeed(int level, float attackSpeedRate, float durationTime, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, durationTime, coolTime);
        newSkillInfo.attackSpeedRate = attackSpeedRate;

        return newSkillInfo;
    }

    /**
     * 헤드샷
     * @param level
     * @param attackDamage
     * @param criticalBonusDamageRate
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel archer_HeadShot(int level, float attackDamage, float criticalBonusDamageRate, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, coolTime);
        newSkillInfo.attackDamage = attackDamage;
        newSkillInfo.criticalBonusDamageRate = criticalBonusDamageRate;

        return newSkillInfo;
    }

    /**
     * 치명타
     * @param level
     * @param criticalChanceRate
     * @param criticalDamageRate
     * @param durationTime
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel archer_CriticalHit(int level, float criticalChanceRate, float criticalDamageRate, float durationTime, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, durationTime, coolTime);
        newSkillInfo.criticalChanceRate = criticalChanceRate;
        newSkillInfo.criticalDamageRate = criticalDamageRate;

        return newSkillInfo;
    }

    /**
     * 폭풍의 시
     * @param level
     * @param attackDamage
     * @param range
     * @param flyingObjectSpeed
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel archer_Storm(int level, float attackDamage, float range, float flyingObjectSpeed, float coolTime, float requireMP){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, 0, flyingObjectSpeed, 0, coolTime);
        newSkillInfo.requireMP = requireMP;

        return newSkillInfo;
    }

    /**
     * 난사
     * @param level
     * @param durationTime
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel archer_Fire(int level, float durationTime, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, durationTime, coolTime);

        return newSkillInfo;
    }

    /**
     * 저격
     * @param level
     * @param attackDamage
     * @param range
     * @param criticalBonusDamageRate
     * @param flyingObjectSpeed
     * @param coolTime
     * @return
     */
    public static SkillInfoPerLevel archer_Snipe(int level, float attackDamage, float range, float criticalBonusDamageRate, float flyingObjectSpeed, float coolTime){

        SkillInfoPerLevel newSkillInfo = new SkillInfoPerLevel(level, attackDamage, range, 0, flyingObjectSpeed, 0, coolTime);
        newSkillInfo.criticalBonusDamageRate = criticalBonusDamageRate;

        return newSkillInfo;
    }


    /*******************************************************************************************************************/

    @Override
    public SkillInfoPerLevel clone() {

        SkillInfoPerLevel skillInfoPerLevel;

        try{
            skillInfoPerLevel = (SkillInfoPerLevel) super.clone();
        } catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }

        return skillInfoPerLevel;

    }









}
