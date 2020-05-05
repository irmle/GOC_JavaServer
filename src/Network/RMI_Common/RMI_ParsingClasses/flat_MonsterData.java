// automatically generated by the FlatBuffers compiler, do not modify

package Network.RMI_Common.RMI_ParsingClasses;

import Network.RMI_Classes.*;
import Network.AutoCreatedClass.*;
import io.netty.buffer.*;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class flat_MonsterData extends Table {
  public static flat_MonsterData getRootAsflat_MonsterData(ByteBuffer _bb) { return getRootAsflat_MonsterData(_bb, new flat_MonsterData()); }
  public static flat_MonsterData getRootAsflat_MonsterData(ByteBuffer _bb, flat_MonsterData obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_MonsterData __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int entityID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int team() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int monsterType() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int monsterLevel() { int o = __offset(10); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int monsterElemental() { int o = __offset(12); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float moveSpeed() { int o = __offset(14); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float currentHP() { int o = __offset(16); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHP() { int o = __offset(18); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float recoveryRateHP() { int o = __offset(20); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float shieldAmount() { int o = __offset(22); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isShieldActivated() { int o = __offset(24); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public float currentMP() { int o = __offset(26); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMP() { int o = __offset(28); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float recoveryRateMP() { int o = __offset(30); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamage() { int o = __offset(32); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackSpeed() { int o = __offset(34); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackRange() { int o = __offset(36); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defense() { int o = __offset(38); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posX() { int o = __offset(40); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posY() { int o = __offset(42); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posZ() { int o = __offset(44); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float velX() { int o = __offset(46); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float velY() { int o = __offset(48); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float velZ() { int o = __offset(50); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float quarternionY() { int o = __offset(52); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float quarternionZ() { int o = __offset(54); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float lookRadius() { int o = __offset(56); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isDisableMove() { int o = __offset(58); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableAttack() { int o = __offset(60); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableSkill() { int o = __offset(62); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableItem() { int o = __offset(64); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDamageImmunity() { int o = __offset(66); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isUnTargetable() { int o = __offset(68); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public float moveSpeedRate() { int o = __offset(70); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackSpeedRate() { int o = __offset(72); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float hpRecoveryRate() { int o = __offset(74); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float mpRecoveryRate() { int o = __offset(76); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float goldGainRate() { int o = __offset(78); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float expGainRate() { int o = __offset(80); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float buffDurationRate() { int o = __offset(82); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamageRate() { int o = __offset(84); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defenseRate() { int o = __offset(86); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHPRate() { int o = __offset(88); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMPRate() { int o = __offset(90); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float coolTimeReduceRate() { int o = __offset(92); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float moveSpeedBonus() { int o = __offset(94); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamageBonus() { int o = __offset(96); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defenseBonus() { int o = __offset(98); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHPBonus() { int o = __offset(100); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMPBonus() { int o = __offset(102); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float bloodSuckingRate() { int o = __offset(104); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float criticalChanceRate() { int o = __offset(106); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float criticalDamageRate() { int o = __offset(108); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isAirborneImmunity() { int o = __offset(110); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isAirborne() { int o = __offset(112); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isTargetingInvincible() { int o = __offset(114); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isStunned() { int o = __offset(116); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isFreezing() { int o = __offset(118); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSlow() { int o = __offset(120); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSilence() { int o = __offset(122); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isBlind() { int o = __offset(124); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSightBlocked() { int o = __offset(126); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isGrounding() { int o = __offset(128); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isPolymorph() { int o = __offset(130); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisarmed() { int o = __offset(132); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSnare() { int o = __offset(134); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isKnockedAirborne() { int o = __offset(136); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isKnockback() { int o = __offset(138); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSuspension() { int o = __offset(140); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isTaunt() { int o = __offset(142); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isCharm() { int o = __offset(144); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isFlee() { int o = __offset(146); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSuppressed() { int o = __offset(148); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSleep() { int o = __offset(150); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }

  public static int createflat_MonsterData(FlatBufferBuilder builder,
      int entityID,
      int team,
      int monsterType,
      int monsterLevel,
      int monsterElemental,
      float moveSpeed,
      float currentHP,
      float maxHP,
      float recoveryRateHP,
      float shieldAmount,
      boolean isShieldActivated,
      float currentMP,
      float maxMP,
      float recoveryRateMP,
      float attackDamage,
      float attackSpeed,
      float attackRange,
      float defense,
      float posX,
      float posY,
      float posZ,
      float velX,
      float velY,
      float velZ,
      float quarternionY,
      float quarternionZ,
      float lookRadius,
      boolean isDisableMove,
      boolean isDisableAttack,
      boolean isDisableSkill,
      boolean isDisableItem,
      boolean isDamageImmunity,
      boolean isUnTargetable,
      float moveSpeedRate,
      float attackSpeedRate,
      float hpRecoveryRate,
      float mpRecoveryRate,
      float goldGainRate,
      float expGainRate,
      float buffDurationRate,
      float attackDamageRate,
      float defenseRate,
      float maxHPRate,
      float maxMPRate,
      float coolTimeReduceRate,
      float moveSpeedBonus,
      float attackDamageBonus,
      float defenseBonus,
      float maxHPBonus,
      float maxMPBonus,
      float bloodSuckingRate,
      float criticalChanceRate,
      float criticalDamageRate,
      boolean isAirborneImmunity,
      boolean isAirborne,
      boolean isTargetingInvincible,
      boolean isStunned,
      boolean isFreezing,
      boolean isSlow,
      boolean isSilence,
      boolean isBlind,
      boolean isSightBlocked,
      boolean isGrounding,
      boolean isPolymorph,
      boolean isDisarmed,
      boolean isSnare,
      boolean isKnockedAirborne,
      boolean isKnockback,
      boolean isSuspension,
      boolean isTaunt,
      boolean isCharm,
      boolean isFlee,
      boolean isSuppressed,
      boolean isSleep) {
    builder.startObject(74);
    flat_MonsterData.addCriticalDamageRate(builder, criticalDamageRate);
    flat_MonsterData.addCriticalChanceRate(builder, criticalChanceRate);
    flat_MonsterData.addBloodSuckingRate(builder, bloodSuckingRate);
    flat_MonsterData.addMaxMPBonus(builder, maxMPBonus);
    flat_MonsterData.addMaxHPBonus(builder, maxHPBonus);
    flat_MonsterData.addDefenseBonus(builder, defenseBonus);
    flat_MonsterData.addAttackDamageBonus(builder, attackDamageBonus);
    flat_MonsterData.addMoveSpeedBonus(builder, moveSpeedBonus);
    flat_MonsterData.addCoolTimeReduceRate(builder, coolTimeReduceRate);
    flat_MonsterData.addMaxMPRate(builder, maxMPRate);
    flat_MonsterData.addMaxHPRate(builder, maxHPRate);
    flat_MonsterData.addDefenseRate(builder, defenseRate);
    flat_MonsterData.addAttackDamageRate(builder, attackDamageRate);
    flat_MonsterData.addBuffDurationRate(builder, buffDurationRate);
    flat_MonsterData.addExpGainRate(builder, expGainRate);
    flat_MonsterData.addGoldGainRate(builder, goldGainRate);
    flat_MonsterData.addMpRecoveryRate(builder, mpRecoveryRate);
    flat_MonsterData.addHpRecoveryRate(builder, hpRecoveryRate);
    flat_MonsterData.addAttackSpeedRate(builder, attackSpeedRate);
    flat_MonsterData.addMoveSpeedRate(builder, moveSpeedRate);
    flat_MonsterData.addLookRadius(builder, lookRadius);
    flat_MonsterData.addQuarternionZ(builder, quarternionZ);
    flat_MonsterData.addQuarternionY(builder, quarternionY);
    flat_MonsterData.addVelZ(builder, velZ);
    flat_MonsterData.addVelY(builder, velY);
    flat_MonsterData.addVelX(builder, velX);
    flat_MonsterData.addPosZ(builder, posZ);
    flat_MonsterData.addPosY(builder, posY);
    flat_MonsterData.addPosX(builder, posX);
    flat_MonsterData.addDefense(builder, defense);
    flat_MonsterData.addAttackRange(builder, attackRange);
    flat_MonsterData.addAttackSpeed(builder, attackSpeed);
    flat_MonsterData.addAttackDamage(builder, attackDamage);
    flat_MonsterData.addRecoveryRateMP(builder, recoveryRateMP);
    flat_MonsterData.addMaxMP(builder, maxMP);
    flat_MonsterData.addCurrentMP(builder, currentMP);
    flat_MonsterData.addShieldAmount(builder, shieldAmount);
    flat_MonsterData.addRecoveryRateHP(builder, recoveryRateHP);
    flat_MonsterData.addMaxHP(builder, maxHP);
    flat_MonsterData.addCurrentHP(builder, currentHP);
    flat_MonsterData.addMoveSpeed(builder, moveSpeed);
    flat_MonsterData.addMonsterElemental(builder, monsterElemental);
    flat_MonsterData.addMonsterLevel(builder, monsterLevel);
    flat_MonsterData.addMonsterType(builder, monsterType);
    flat_MonsterData.addTeam(builder, team);
    flat_MonsterData.addEntityID(builder, entityID);
    flat_MonsterData.addIsSleep(builder, isSleep);
    flat_MonsterData.addIsSuppressed(builder, isSuppressed);
    flat_MonsterData.addIsFlee(builder, isFlee);
    flat_MonsterData.addIsCharm(builder, isCharm);
    flat_MonsterData.addIsTaunt(builder, isTaunt);
    flat_MonsterData.addIsSuspension(builder, isSuspension);
    flat_MonsterData.addIsKnockback(builder, isKnockback);
    flat_MonsterData.addIsKnockedAirborne(builder, isKnockedAirborne);
    flat_MonsterData.addIsSnare(builder, isSnare);
    flat_MonsterData.addIsDisarmed(builder, isDisarmed);
    flat_MonsterData.addIsPolymorph(builder, isPolymorph);
    flat_MonsterData.addIsGrounding(builder, isGrounding);
    flat_MonsterData.addIsSightBlocked(builder, isSightBlocked);
    flat_MonsterData.addIsBlind(builder, isBlind);
    flat_MonsterData.addIsSilence(builder, isSilence);
    flat_MonsterData.addIsSlow(builder, isSlow);
    flat_MonsterData.addIsFreezing(builder, isFreezing);
    flat_MonsterData.addIsStunned(builder, isStunned);
    flat_MonsterData.addIsTargetingInvincible(builder, isTargetingInvincible);
    flat_MonsterData.addIsAirborne(builder, isAirborne);
    flat_MonsterData.addIsAirborneImmunity(builder, isAirborneImmunity);
    flat_MonsterData.addIsUnTargetable(builder, isUnTargetable);
    flat_MonsterData.addIsDamageImmunity(builder, isDamageImmunity);
    flat_MonsterData.addIsDisableItem(builder, isDisableItem);
    flat_MonsterData.addIsDisableSkill(builder, isDisableSkill);
    flat_MonsterData.addIsDisableAttack(builder, isDisableAttack);
    flat_MonsterData.addIsDisableMove(builder, isDisableMove);
    flat_MonsterData.addIsShieldActivated(builder, isShieldActivated);
    return flat_MonsterData.endflat_MonsterData(builder);
  }

  public static void startflat_MonsterData(FlatBufferBuilder builder) { builder.startObject(74); }
  public static void addEntityID(FlatBufferBuilder builder, int entityID) { builder.addInt(0, entityID, 0); }
  public static void addTeam(FlatBufferBuilder builder, int team) { builder.addInt(1, team, 0); }
  public static void addMonsterType(FlatBufferBuilder builder, int monsterType) { builder.addInt(2, monsterType, 0); }
  public static void addMonsterLevel(FlatBufferBuilder builder, int monsterLevel) { builder.addInt(3, monsterLevel, 0); }
  public static void addMonsterElemental(FlatBufferBuilder builder, int monsterElemental) { builder.addInt(4, monsterElemental, 0); }
  public static void addMoveSpeed(FlatBufferBuilder builder, float moveSpeed) { builder.addFloat(5, moveSpeed, 0.0f); }
  public static void addCurrentHP(FlatBufferBuilder builder, float currentHP) { builder.addFloat(6, currentHP, 0.0f); }
  public static void addMaxHP(FlatBufferBuilder builder, float maxHP) { builder.addFloat(7, maxHP, 0.0f); }
  public static void addRecoveryRateHP(FlatBufferBuilder builder, float recoveryRateHP) { builder.addFloat(8, recoveryRateHP, 0.0f); }
  public static void addShieldAmount(FlatBufferBuilder builder, float shieldAmount) { builder.addFloat(9, shieldAmount, 0.0f); }
  public static void addIsShieldActivated(FlatBufferBuilder builder, boolean isShieldActivated) { builder.addBoolean(10, isShieldActivated, false); }
  public static void addCurrentMP(FlatBufferBuilder builder, float currentMP) { builder.addFloat(11, currentMP, 0.0f); }
  public static void addMaxMP(FlatBufferBuilder builder, float maxMP) { builder.addFloat(12, maxMP, 0.0f); }
  public static void addRecoveryRateMP(FlatBufferBuilder builder, float recoveryRateMP) { builder.addFloat(13, recoveryRateMP, 0.0f); }
  public static void addAttackDamage(FlatBufferBuilder builder, float attackDamage) { builder.addFloat(14, attackDamage, 0.0f); }
  public static void addAttackSpeed(FlatBufferBuilder builder, float attackSpeed) { builder.addFloat(15, attackSpeed, 0.0f); }
  public static void addAttackRange(FlatBufferBuilder builder, float attackRange) { builder.addFloat(16, attackRange, 0.0f); }
  public static void addDefense(FlatBufferBuilder builder, float defense) { builder.addFloat(17, defense, 0.0f); }
  public static void addPosX(FlatBufferBuilder builder, float posX) { builder.addFloat(18, posX, 0.0f); }
  public static void addPosY(FlatBufferBuilder builder, float posY) { builder.addFloat(19, posY, 0.0f); }
  public static void addPosZ(FlatBufferBuilder builder, float posZ) { builder.addFloat(20, posZ, 0.0f); }
  public static void addVelX(FlatBufferBuilder builder, float velX) { builder.addFloat(21, velX, 0.0f); }
  public static void addVelY(FlatBufferBuilder builder, float velY) { builder.addFloat(22, velY, 0.0f); }
  public static void addVelZ(FlatBufferBuilder builder, float velZ) { builder.addFloat(23, velZ, 0.0f); }
  public static void addQuarternionY(FlatBufferBuilder builder, float quarternionY) { builder.addFloat(24, quarternionY, 0.0f); }
  public static void addQuarternionZ(FlatBufferBuilder builder, float quarternionZ) { builder.addFloat(25, quarternionZ, 0.0f); }
  public static void addLookRadius(FlatBufferBuilder builder, float lookRadius) { builder.addFloat(26, lookRadius, 0.0f); }
  public static void addIsDisableMove(FlatBufferBuilder builder, boolean isDisableMove) { builder.addBoolean(27, isDisableMove, false); }
  public static void addIsDisableAttack(FlatBufferBuilder builder, boolean isDisableAttack) { builder.addBoolean(28, isDisableAttack, false); }
  public static void addIsDisableSkill(FlatBufferBuilder builder, boolean isDisableSkill) { builder.addBoolean(29, isDisableSkill, false); }
  public static void addIsDisableItem(FlatBufferBuilder builder, boolean isDisableItem) { builder.addBoolean(30, isDisableItem, false); }
  public static void addIsDamageImmunity(FlatBufferBuilder builder, boolean isDamageImmunity) { builder.addBoolean(31, isDamageImmunity, false); }
  public static void addIsUnTargetable(FlatBufferBuilder builder, boolean isUnTargetable) { builder.addBoolean(32, isUnTargetable, false); }
  public static void addMoveSpeedRate(FlatBufferBuilder builder, float moveSpeedRate) { builder.addFloat(33, moveSpeedRate, 0.0f); }
  public static void addAttackSpeedRate(FlatBufferBuilder builder, float attackSpeedRate) { builder.addFloat(34, attackSpeedRate, 0.0f); }
  public static void addHpRecoveryRate(FlatBufferBuilder builder, float hpRecoveryRate) { builder.addFloat(35, hpRecoveryRate, 0.0f); }
  public static void addMpRecoveryRate(FlatBufferBuilder builder, float mpRecoveryRate) { builder.addFloat(36, mpRecoveryRate, 0.0f); }
  public static void addGoldGainRate(FlatBufferBuilder builder, float goldGainRate) { builder.addFloat(37, goldGainRate, 0.0f); }
  public static void addExpGainRate(FlatBufferBuilder builder, float expGainRate) { builder.addFloat(38, expGainRate, 0.0f); }
  public static void addBuffDurationRate(FlatBufferBuilder builder, float buffDurationRate) { builder.addFloat(39, buffDurationRate, 0.0f); }
  public static void addAttackDamageRate(FlatBufferBuilder builder, float attackDamageRate) { builder.addFloat(40, attackDamageRate, 0.0f); }
  public static void addDefenseRate(FlatBufferBuilder builder, float defenseRate) { builder.addFloat(41, defenseRate, 0.0f); }
  public static void addMaxHPRate(FlatBufferBuilder builder, float maxHPRate) { builder.addFloat(42, maxHPRate, 0.0f); }
  public static void addMaxMPRate(FlatBufferBuilder builder, float maxMPRate) { builder.addFloat(43, maxMPRate, 0.0f); }
  public static void addCoolTimeReduceRate(FlatBufferBuilder builder, float coolTimeReduceRate) { builder.addFloat(44, coolTimeReduceRate, 0.0f); }
  public static void addMoveSpeedBonus(FlatBufferBuilder builder, float moveSpeedBonus) { builder.addFloat(45, moveSpeedBonus, 0.0f); }
  public static void addAttackDamageBonus(FlatBufferBuilder builder, float attackDamageBonus) { builder.addFloat(46, attackDamageBonus, 0.0f); }
  public static void addDefenseBonus(FlatBufferBuilder builder, float defenseBonus) { builder.addFloat(47, defenseBonus, 0.0f); }
  public static void addMaxHPBonus(FlatBufferBuilder builder, float maxHPBonus) { builder.addFloat(48, maxHPBonus, 0.0f); }
  public static void addMaxMPBonus(FlatBufferBuilder builder, float maxMPBonus) { builder.addFloat(49, maxMPBonus, 0.0f); }
  public static void addBloodSuckingRate(FlatBufferBuilder builder, float bloodSuckingRate) { builder.addFloat(50, bloodSuckingRate, 0.0f); }
  public static void addCriticalChanceRate(FlatBufferBuilder builder, float criticalChanceRate) { builder.addFloat(51, criticalChanceRate, 0.0f); }
  public static void addCriticalDamageRate(FlatBufferBuilder builder, float criticalDamageRate) { builder.addFloat(52, criticalDamageRate, 0.0f); }
  public static void addIsAirborneImmunity(FlatBufferBuilder builder, boolean isAirborneImmunity) { builder.addBoolean(53, isAirborneImmunity, false); }
  public static void addIsAirborne(FlatBufferBuilder builder, boolean isAirborne) { builder.addBoolean(54, isAirborne, false); }
  public static void addIsTargetingInvincible(FlatBufferBuilder builder, boolean isTargetingInvincible) { builder.addBoolean(55, isTargetingInvincible, false); }
  public static void addIsStunned(FlatBufferBuilder builder, boolean isStunned) { builder.addBoolean(56, isStunned, false); }
  public static void addIsFreezing(FlatBufferBuilder builder, boolean isFreezing) { builder.addBoolean(57, isFreezing, false); }
  public static void addIsSlow(FlatBufferBuilder builder, boolean isSlow) { builder.addBoolean(58, isSlow, false); }
  public static void addIsSilence(FlatBufferBuilder builder, boolean isSilence) { builder.addBoolean(59, isSilence, false); }
  public static void addIsBlind(FlatBufferBuilder builder, boolean isBlind) { builder.addBoolean(60, isBlind, false); }
  public static void addIsSightBlocked(FlatBufferBuilder builder, boolean isSightBlocked) { builder.addBoolean(61, isSightBlocked, false); }
  public static void addIsGrounding(FlatBufferBuilder builder, boolean isGrounding) { builder.addBoolean(62, isGrounding, false); }
  public static void addIsPolymorph(FlatBufferBuilder builder, boolean isPolymorph) { builder.addBoolean(63, isPolymorph, false); }
  public static void addIsDisarmed(FlatBufferBuilder builder, boolean isDisarmed) { builder.addBoolean(64, isDisarmed, false); }
  public static void addIsSnare(FlatBufferBuilder builder, boolean isSnare) { builder.addBoolean(65, isSnare, false); }
  public static void addIsKnockedAirborne(FlatBufferBuilder builder, boolean isKnockedAirborne) { builder.addBoolean(66, isKnockedAirborne, false); }
  public static void addIsKnockback(FlatBufferBuilder builder, boolean isKnockback) { builder.addBoolean(67, isKnockback, false); }
  public static void addIsSuspension(FlatBufferBuilder builder, boolean isSuspension) { builder.addBoolean(68, isSuspension, false); }
  public static void addIsTaunt(FlatBufferBuilder builder, boolean isTaunt) { builder.addBoolean(69, isTaunt, false); }
  public static void addIsCharm(FlatBufferBuilder builder, boolean isCharm) { builder.addBoolean(70, isCharm, false); }
  public static void addIsFlee(FlatBufferBuilder builder, boolean isFlee) { builder.addBoolean(71, isFlee, false); }
  public static void addIsSuppressed(FlatBufferBuilder builder, boolean isSuppressed) { builder.addBoolean(72, isSuppressed, false); }
  public static void addIsSleep(FlatBufferBuilder builder, boolean isSleep) { builder.addBoolean(73, isSleep, false); }
  public static int endflat_MonsterData(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_MonsterData(FlatBufferBuilder builder,
 MonsterData data) {
        return createflat_MonsterData(builder , data.entityID, data.team, data.monsterType, data.monsterLevel, data.monsterElemental, data.moveSpeed, data.currentHP, data.maxHP, data.recoveryRateHP, data.shieldAmount, data.isShieldActivated, data.currentMP, data.maxMP, data.recoveryRateMP, data.attackDamage, data.attackSpeed, data.attackRange, data.defense, data.posX, data.posY, data.posZ, data.velX, data.velY, data.velZ, data.quarternionY, data.quarternionZ, data.lookRadius, data.isDisableMove, data.isDisableAttack, data.isDisableSkill, data.isDisableItem, data.isDamageImmunity, data.isUnTargetable, data.moveSpeedRate, data.attackSpeedRate, data.hpRecoveryRate, data.mpRecoveryRate, data.goldGainRate, data.expGainRate, data.buffDurationRate, data.attackDamageRate, data.defenseRate, data.maxHPRate, data.maxMPRate, data.coolTimeReduceRate, data.moveSpeedBonus, data.attackDamageBonus, data.defenseBonus, data.maxHPBonus, data.maxMPBonus, data.bloodSuckingRate, data.criticalChanceRate, data.criticalDamageRate, data.isAirborneImmunity, data.isAirborne, data.isTargetingInvincible, data.isStunned, data.isFreezing, data.isSlow, data.isSilence, data.isBlind, data.isSightBlocked, data.isGrounding, data.isPolymorph, data.isDisarmed, data.isSnare, data.isKnockedAirborne, data.isKnockback, data.isSuspension, data.isTaunt, data.isCharm, data.isFlee, data.isSuppressed, data.isSleep);
    }

    public static byte[] createflat_MonsterData(MonsterData data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_MonsterData.createflat_MonsterData(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static MonsterData getRootAsflat_MonsterData(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        MonsterData result = new MonsterData(flat_MonsterData.getRootAsflat_MonsterData( buf ) );
        buf = null;
        return result;
    }

}