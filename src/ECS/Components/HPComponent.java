package ECS.Components;

/**
 * -- 업뎃날짜 :
 *      오후 8:56 2020-04-08
 *
 * -- 업뎃내용 :
 *      # checkCurrentHpPercentage() 추가
 *          ㄴ 기능 : 현재 체력이 얼마나 남았는지 (getter 아님, percentage 비율 측정) 계산하여 반환
 *      #
 *
 */
public class HPComponent implements Cloneable {

    public float originalMaxHp;

    public float currentHP;
    public float maxHP;
    public float recoveryRateHP;

    public HPComponent( ) {
    }

    public HPComponent(float maxHP, float recoveryRateHP) {

        this.originalMaxHp = maxHP;

        this.currentHP = maxHP;
        this.maxHP = maxHP;
        this.recoveryRateHP = recoveryRateHP;
    }


    /**
     * 기    능 :
     *      -- 최대 체력 대비 현재 체력 비율이 어느정도인지 계산한다
     *
     * @return
     */
    public float checkCurrentHpPercentage(){

        float percentage = (this.currentHP / this.originalMaxHp) * 100f;

        return percentage;
    }

    public boolean checkIfNeedHpRecovery(){

        boolean needRecovery = (this.currentHP < this.maxHP) ? true : false;

        return needRecovery;
    }


    @Override
    public Object clone() {
        HPComponent hpComponent = null;
        try{
            hpComponent = (HPComponent) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return hpComponent;
    }
}
