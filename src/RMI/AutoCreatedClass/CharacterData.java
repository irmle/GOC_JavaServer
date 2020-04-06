package RMI.AutoCreatedClass;

import RMI.RMI_Common._RMI_ParsingClasses.*;
import RMI.RMI_Classes.*;
import RMI.AutoCreatedClass.*;
import io.netty.buffer.*;
import com.google.flatbuffers.*;
import java.util.LinkedList;

public class CharacterData {

    public int entityID;
    public int team;
    public int characterType;
    public String characterName;
    public float moveSpeed;
    public LinkedList <SkillSlotData> skillSlot = new LinkedList<>();
    public LinkedList <ItemSlotData> itemSlot = new LinkedList<>();
    public LinkedList <ConditionData> conditionList = new LinkedList<>();
    public int level;
    public int exp;
    public int gold;
    public float currentHP;
    public float maxHP;
    public float recoveryRateHP;
    public float currentMP;
    public float maxMP;
    public float recoveryRateMP;
    public float attackDamage;
    public float attackSpeed;
    public float attackRange;
    public float criticalChance;
    public float criticalDamage;
    public float defense;
    public float posX;
    public float posY;
    public float posZ;
    public float velX;
    public float velY;
    public float velZ;
    public float quarternionY;
    public float quarternionZ;
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

    public CharacterData() { }

    public CharacterData(flat_CharacterData data) {
        this.entityID = data.entityID();
        this.team = data.team();
        this.characterType = data.characterType();
        this.characterName = data.characterName();
        this.moveSpeed = data.moveSpeed();
        for(int i = 0;i < data.skillSlotLength();i++) {
            this.skillSlot.addLast(new SkillSlotData(data.skillSlot(i)));
        }
        for(int i = 0;i < data.itemSlotLength();i++) {
            this.itemSlot.addLast(new ItemSlotData(data.itemSlot(i)));
        }
        for(int i = 0;i < data.conditionListLength();i++) {
            this.conditionList.addLast(new ConditionData(data.conditionList(i)));
        }
        this.level = data.level();
        this.exp = data.exp();
        this.gold = data.gold();
        this.currentHP = data.currentHP();
        this.maxHP = data.maxHP();
        this.recoveryRateHP = data.recoveryRateHP();
        this.currentMP = data.currentMP();
        this.maxMP = data.maxMP();
        this.recoveryRateMP = data.recoveryRateMP();
        this.attackDamage = data.attackDamage();
        this.attackSpeed = data.attackSpeed();
        this.attackRange = data.attackRange();
        this.criticalChance = data.criticalChance();
        this.criticalDamage = data.criticalDamage();
        this.defense = data.defense();
        this.posX = data.posX();
        this.posY = data.posY();
        this.posZ = data.posZ();
        this.velX = data.velX();
        this.velY = data.velY();
        this.velZ = data.velZ();
        this.quarternionY = data.quarternionY();
        this.quarternionZ = data.quarternionZ();
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

    public static CharacterData createCharacterData(byte[] data)
    {
        return flat_CharacterData.getRootAsflat_CharacterData( data );
    }

    public static byte[] getBytes(CharacterData data)
    {
        return flat_CharacterData.createflat_CharacterData( data );
    }

    public byte[] getBytes()
    {
        return getBytes(this);
    }

}