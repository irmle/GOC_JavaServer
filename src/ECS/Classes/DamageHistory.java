package ECS.Classes;

import ECS.Classes.Type.DamageType;

/**
 * 업뎃날짜 : 2020 02 04 화 권령희
 * 업뎃내용 :
 *      데미지가 평타인지, 크리티컬인지 나타내는 변수를 추가.
 *      ㄴ 나중에.. 데미지 타입이 더 늘어날 수도 있을듯?? 그거에 따라 더 정교한 데미지 처리를 하게될수도..
 */
public class DamageHistory implements Cloneable {

    public int unitID;
    public boolean isDamage;
    public float amount;

    public int damageType;

    public DamageHistory(int unitID, boolean isDamage, float amount) {
        this.unitID = unitID;
        this.isDamage = isDamage;
        this.amount = amount;

        damageType = DamageType.NONE;
    }

    public DamageHistory(int unitID, boolean isDamage, float amount, int damageType) {
        this.unitID = unitID;
        this.isDamage = isDamage;
        this.amount = amount;
        this.damageType = damageType;
    }

    @Override
    public Object clone() {
        DamageHistory damageHistory = null;
        try{
            damageHistory = (DamageHistory) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return damageHistory;
    }
}
