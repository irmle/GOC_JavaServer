package Network.AutoCreatedClass;

import Network.RMI_Common.RMI_ParsingClasses.*;
import Network.AutoCreatedClass.*;
import java.util.LinkedList;

public class BuffTurretData {

    public int entityID;
    public int team;
    public int TurretType;
    public float costTime;
    public int costGold;
    public float currentHP;
    public float maxHP;
    public float recoveryRateHP;
    public float shieldAmount;
    public boolean isShieldActivated;
    public float defense;
    public float posX;
    public float posY;
    public float posZ;
    public float buffAreaRange;
    public boolean isDisableMove;
    public boolean isDisableAttack;
    public boolean isDisableSkill;
    public boolean isDisableItem;
    public boolean isDamageImmunity;
    public boolean isUnTargetable;
    public float moveSpeedRate;
    public float attackSpeedRate;
    public float hpRecoveryRate;
    public float mpRecoveryRate;
    public float goldGainRate;
    public float expGainRate;
    public float buffDurationRate;
    public float attackDamageRate;
    public float defenseRate;
    public float maxHPRate;
    public float maxMPRate;
    public float coolTimeReduceRate;
    public float moveSpeedBonus;
    public float attackDamageBonus;
    public float defenseBonus;
    public float maxHPBonus;
    public float maxMPBonus;
    public float bloodSuckingRate;
    public float criticalChanceRate;
    public float criticalDamageRate;
    public boolean isAirborneImmunity;
    public boolean isAirborne;
    public boolean isTargetingInvincible;
    public boolean isStunned;
    public boolean isFreezing;
    public boolean isSlow;
    public boolean isSilence;
    public boolean isBlind;
    public boolean isSightBlocked;
    public boolean isGrounding;
    public boolean isPolymorph;
    public boolean isDisarmed;
    public boolean isSnare;
    public boolean isKnockedAirborne;
    public boolean isKnockback;
    public boolean isSuspension;
    public boolean isTaunt;
    public boolean isCharm;
    public boolean isFlee;
    public boolean isSuppressed;
    public boolean isSleep;

    public BuffTurretData() { }

    public BuffTurretData(flat_BuffTurretData data) {
        this.entityID = data.entityID();
        this.team = data.team();
        this.TurretType = data.TurretType();
        this.costTime = data.costTime();
        this.costGold = data.costGold();
        this.currentHP = data.currentHP();
        this.maxHP = data.maxHP();
        this.recoveryRateHP = data.recoveryRateHP();
        this.shieldAmount = data.shieldAmount();
        this.isShieldActivated = data.isShieldActivated();
        this.defense = data.defense();
        this.posX = data.posX();
        this.posY = data.posY();
        this.posZ = data.posZ();
        this.buffAreaRange = data.buffAreaRange();
        this.isDisableMove = data.isDisableMove();
        this.isDisableAttack = data.isDisableAttack();
        this.isDisableSkill = data.isDisableSkill();
        this.isDisableItem = data.isDisableItem();
        this.isDamageImmunity = data.isDamageImmunity();
        this.isUnTargetable = data.isUnTargetable();
        this.moveSpeedRate = data.moveSpeedRate();
        this.attackSpeedRate = data.attackSpeedRate();
        this.hpRecoveryRate = data.hpRecoveryRate();
        this.mpRecoveryRate = data.mpRecoveryRate();
        this.goldGainRate = data.goldGainRate();
        this.expGainRate = data.expGainRate();
        this.buffDurationRate = data.buffDurationRate();
        this.attackDamageRate = data.attackDamageRate();
        this.defenseRate = data.defenseRate();
        this.maxHPRate = data.maxHPRate();
        this.maxMPRate = data.maxMPRate();
        this.coolTimeReduceRate = data.coolTimeReduceRate();
        this.moveSpeedBonus = data.moveSpeedBonus();
        this.attackDamageBonus = data.attackDamageBonus();
        this.defenseBonus = data.defenseBonus();
        this.maxHPBonus = data.maxHPBonus();
        this.maxMPBonus = data.maxMPBonus();
        this.bloodSuckingRate = data.bloodSuckingRate();
        this.criticalChanceRate = data.criticalChanceRate();
        this.criticalDamageRate = data.criticalDamageRate();
        this.isAirborneImmunity = data.isAirborneImmunity();
        this.isAirborne = data.isAirborne();
        this.isTargetingInvincible = data.isTargetingInvincible();
        this.isStunned = data.isStunned();
        this.isFreezing = data.isFreezing();
        this.isSlow = data.isSlow();
        this.isSilence = data.isSilence();
        this.isBlind = data.isBlind();
        this.isSightBlocked = data.isSightBlocked();
        this.isGrounding = data.isGrounding();
        this.isPolymorph = data.isPolymorph();
        this.isDisarmed = data.isDisarmed();
        this.isSnare = data.isSnare();
        this.isKnockedAirborne = data.isKnockedAirborne();
        this.isKnockback = data.isKnockback();
        this.isSuspension = data.isSuspension();
        this.isTaunt = data.isTaunt();
        this.isCharm = data.isCharm();
        this.isFlee = data.isFlee();
        this.isSuppressed = data.isSuppressed();
        this.isSleep = data.isSleep();
    }

    public static BuffTurretData createBuffTurretData(byte[] data)
    {
        return flat_BuffTurretData.getRootAsflat_BuffTurretData( data );
    }

    public static byte[] getBytes(BuffTurretData data)
    {
        return flat_BuffTurretData.createflat_BuffTurretData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}