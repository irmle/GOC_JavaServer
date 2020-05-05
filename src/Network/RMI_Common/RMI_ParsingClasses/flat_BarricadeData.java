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
public final class flat_BarricadeData extends Table {
  public static flat_BarricadeData getRootAsflat_BarricadeData(ByteBuffer _bb) { return getRootAsflat_BarricadeData(_bb, new flat_BarricadeData()); }
  public static flat_BarricadeData getRootAsflat_BarricadeData(ByteBuffer _bb, flat_BarricadeData obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_BarricadeData __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int entityID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int team() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float costTime() { int o = __offset(8); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public int costGold() { int o = __offset(10); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int upgradeLevel() { int o = __offset(12); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float currentHP() { int o = __offset(14); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHP() { int o = __offset(16); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float recoveryRateHP() { int o = __offset(18); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float shieldAmount() { int o = __offset(20); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isShieldActivated() { int o = __offset(22); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public float defense() { int o = __offset(24); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posX() { int o = __offset(26); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posY() { int o = __offset(28); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posZ() { int o = __offset(30); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isDisableMove() { int o = __offset(32); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableAttack() { int o = __offset(34); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableSkill() { int o = __offset(36); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableItem() { int o = __offset(38); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDamageImmunity() { int o = __offset(40); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isUnTargetable() { int o = __offset(42); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public float moveSpeedRate() { int o = __offset(44); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackSpeedRate() { int o = __offset(46); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float hpRecoveryRate() { int o = __offset(48); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float mpRecoveryRate() { int o = __offset(50); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float goldGainRate() { int o = __offset(52); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float expGainRate() { int o = __offset(54); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float buffDurationRate() { int o = __offset(56); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamageRate() { int o = __offset(58); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defenseRate() { int o = __offset(60); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHPRate() { int o = __offset(62); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMPRate() { int o = __offset(64); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float coolTimeReduceRate() { int o = __offset(66); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float moveSpeedBonus() { int o = __offset(68); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamageBonus() { int o = __offset(70); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defenseBonus() { int o = __offset(72); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHPBonus() { int o = __offset(74); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMPBonus() { int o = __offset(76); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isAirborneImmunity() { int o = __offset(78); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isAirborne() { int o = __offset(80); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isTargetingInvincible() { int o = __offset(82); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isStunned() { int o = __offset(84); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isFreezing() { int o = __offset(86); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSlow() { int o = __offset(88); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSilence() { int o = __offset(90); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isBlind() { int o = __offset(92); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSightBlocked() { int o = __offset(94); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isGrounding() { int o = __offset(96); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isPolymorph() { int o = __offset(98); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisarmed() { int o = __offset(100); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSnare() { int o = __offset(102); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isKnockedAirborne() { int o = __offset(104); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isKnockback() { int o = __offset(106); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSuspension() { int o = __offset(108); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isTaunt() { int o = __offset(110); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isCharm() { int o = __offset(112); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isFlee() { int o = __offset(114); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSuppressed() { int o = __offset(116); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSleep() { int o = __offset(118); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }

  public static int createflat_BarricadeData(FlatBufferBuilder builder,
      int entityID,
      int team,
      float costTime,
      int costGold,
      int upgradeLevel,
      float currentHP,
      float maxHP,
      float recoveryRateHP,
      float shieldAmount,
      boolean isShieldActivated,
      float defense,
      float posX,
      float posY,
      float posZ,
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
    builder.startObject(58);
    flat_BarricadeData.addMaxMPBonus(builder, maxMPBonus);
    flat_BarricadeData.addMaxHPBonus(builder, maxHPBonus);
    flat_BarricadeData.addDefenseBonus(builder, defenseBonus);
    flat_BarricadeData.addAttackDamageBonus(builder, attackDamageBonus);
    flat_BarricadeData.addMoveSpeedBonus(builder, moveSpeedBonus);
    flat_BarricadeData.addCoolTimeReduceRate(builder, coolTimeReduceRate);
    flat_BarricadeData.addMaxMPRate(builder, maxMPRate);
    flat_BarricadeData.addMaxHPRate(builder, maxHPRate);
    flat_BarricadeData.addDefenseRate(builder, defenseRate);
    flat_BarricadeData.addAttackDamageRate(builder, attackDamageRate);
    flat_BarricadeData.addBuffDurationRate(builder, buffDurationRate);
    flat_BarricadeData.addExpGainRate(builder, expGainRate);
    flat_BarricadeData.addGoldGainRate(builder, goldGainRate);
    flat_BarricadeData.addMpRecoveryRate(builder, mpRecoveryRate);
    flat_BarricadeData.addHpRecoveryRate(builder, hpRecoveryRate);
    flat_BarricadeData.addAttackSpeedRate(builder, attackSpeedRate);
    flat_BarricadeData.addMoveSpeedRate(builder, moveSpeedRate);
    flat_BarricadeData.addPosZ(builder, posZ);
    flat_BarricadeData.addPosY(builder, posY);
    flat_BarricadeData.addPosX(builder, posX);
    flat_BarricadeData.addDefense(builder, defense);
    flat_BarricadeData.addShieldAmount(builder, shieldAmount);
    flat_BarricadeData.addRecoveryRateHP(builder, recoveryRateHP);
    flat_BarricadeData.addMaxHP(builder, maxHP);
    flat_BarricadeData.addCurrentHP(builder, currentHP);
    flat_BarricadeData.addUpgradeLevel(builder, upgradeLevel);
    flat_BarricadeData.addCostGold(builder, costGold);
    flat_BarricadeData.addCostTime(builder, costTime);
    flat_BarricadeData.addTeam(builder, team);
    flat_BarricadeData.addEntityID(builder, entityID);
    flat_BarricadeData.addIsSleep(builder, isSleep);
    flat_BarricadeData.addIsSuppressed(builder, isSuppressed);
    flat_BarricadeData.addIsFlee(builder, isFlee);
    flat_BarricadeData.addIsCharm(builder, isCharm);
    flat_BarricadeData.addIsTaunt(builder, isTaunt);
    flat_BarricadeData.addIsSuspension(builder, isSuspension);
    flat_BarricadeData.addIsKnockback(builder, isKnockback);
    flat_BarricadeData.addIsKnockedAirborne(builder, isKnockedAirborne);
    flat_BarricadeData.addIsSnare(builder, isSnare);
    flat_BarricadeData.addIsDisarmed(builder, isDisarmed);
    flat_BarricadeData.addIsPolymorph(builder, isPolymorph);
    flat_BarricadeData.addIsGrounding(builder, isGrounding);
    flat_BarricadeData.addIsSightBlocked(builder, isSightBlocked);
    flat_BarricadeData.addIsBlind(builder, isBlind);
    flat_BarricadeData.addIsSilence(builder, isSilence);
    flat_BarricadeData.addIsSlow(builder, isSlow);
    flat_BarricadeData.addIsFreezing(builder, isFreezing);
    flat_BarricadeData.addIsStunned(builder, isStunned);
    flat_BarricadeData.addIsTargetingInvincible(builder, isTargetingInvincible);
    flat_BarricadeData.addIsAirborne(builder, isAirborne);
    flat_BarricadeData.addIsAirborneImmunity(builder, isAirborneImmunity);
    flat_BarricadeData.addIsUnTargetable(builder, isUnTargetable);
    flat_BarricadeData.addIsDamageImmunity(builder, isDamageImmunity);
    flat_BarricadeData.addIsDisableItem(builder, isDisableItem);
    flat_BarricadeData.addIsDisableSkill(builder, isDisableSkill);
    flat_BarricadeData.addIsDisableAttack(builder, isDisableAttack);
    flat_BarricadeData.addIsDisableMove(builder, isDisableMove);
    flat_BarricadeData.addIsShieldActivated(builder, isShieldActivated);
    return flat_BarricadeData.endflat_BarricadeData(builder);
  }

  public static void startflat_BarricadeData(FlatBufferBuilder builder) { builder.startObject(58); }
  public static void addEntityID(FlatBufferBuilder builder, int entityID) { builder.addInt(0, entityID, 0); }
  public static void addTeam(FlatBufferBuilder builder, int team) { builder.addInt(1, team, 0); }
  public static void addCostTime(FlatBufferBuilder builder, float costTime) { builder.addFloat(2, costTime, 0.0f); }
  public static void addCostGold(FlatBufferBuilder builder, int costGold) { builder.addInt(3, costGold, 0); }
  public static void addUpgradeLevel(FlatBufferBuilder builder, int upgradeLevel) { builder.addInt(4, upgradeLevel, 0); }
  public static void addCurrentHP(FlatBufferBuilder builder, float currentHP) { builder.addFloat(5, currentHP, 0.0f); }
  public static void addMaxHP(FlatBufferBuilder builder, float maxHP) { builder.addFloat(6, maxHP, 0.0f); }
  public static void addRecoveryRateHP(FlatBufferBuilder builder, float recoveryRateHP) { builder.addFloat(7, recoveryRateHP, 0.0f); }
  public static void addShieldAmount(FlatBufferBuilder builder, float shieldAmount) { builder.addFloat(8, shieldAmount, 0.0f); }
  public static void addIsShieldActivated(FlatBufferBuilder builder, boolean isShieldActivated) { builder.addBoolean(9, isShieldActivated, false); }
  public static void addDefense(FlatBufferBuilder builder, float defense) { builder.addFloat(10, defense, 0.0f); }
  public static void addPosX(FlatBufferBuilder builder, float posX) { builder.addFloat(11, posX, 0.0f); }
  public static void addPosY(FlatBufferBuilder builder, float posY) { builder.addFloat(12, posY, 0.0f); }
  public static void addPosZ(FlatBufferBuilder builder, float posZ) { builder.addFloat(13, posZ, 0.0f); }
  public static void addIsDisableMove(FlatBufferBuilder builder, boolean isDisableMove) { builder.addBoolean(14, isDisableMove, false); }
  public static void addIsDisableAttack(FlatBufferBuilder builder, boolean isDisableAttack) { builder.addBoolean(15, isDisableAttack, false); }
  public static void addIsDisableSkill(FlatBufferBuilder builder, boolean isDisableSkill) { builder.addBoolean(16, isDisableSkill, false); }
  public static void addIsDisableItem(FlatBufferBuilder builder, boolean isDisableItem) { builder.addBoolean(17, isDisableItem, false); }
  public static void addIsDamageImmunity(FlatBufferBuilder builder, boolean isDamageImmunity) { builder.addBoolean(18, isDamageImmunity, false); }
  public static void addIsUnTargetable(FlatBufferBuilder builder, boolean isUnTargetable) { builder.addBoolean(19, isUnTargetable, false); }
  public static void addMoveSpeedRate(FlatBufferBuilder builder, float moveSpeedRate) { builder.addFloat(20, moveSpeedRate, 0.0f); }
  public static void addAttackSpeedRate(FlatBufferBuilder builder, float attackSpeedRate) { builder.addFloat(21, attackSpeedRate, 0.0f); }
  public static void addHpRecoveryRate(FlatBufferBuilder builder, float hpRecoveryRate) { builder.addFloat(22, hpRecoveryRate, 0.0f); }
  public static void addMpRecoveryRate(FlatBufferBuilder builder, float mpRecoveryRate) { builder.addFloat(23, mpRecoveryRate, 0.0f); }
  public static void addGoldGainRate(FlatBufferBuilder builder, float goldGainRate) { builder.addFloat(24, goldGainRate, 0.0f); }
  public static void addExpGainRate(FlatBufferBuilder builder, float expGainRate) { builder.addFloat(25, expGainRate, 0.0f); }
  public static void addBuffDurationRate(FlatBufferBuilder builder, float buffDurationRate) { builder.addFloat(26, buffDurationRate, 0.0f); }
  public static void addAttackDamageRate(FlatBufferBuilder builder, float attackDamageRate) { builder.addFloat(27, attackDamageRate, 0.0f); }
  public static void addDefenseRate(FlatBufferBuilder builder, float defenseRate) { builder.addFloat(28, defenseRate, 0.0f); }
  public static void addMaxHPRate(FlatBufferBuilder builder, float maxHPRate) { builder.addFloat(29, maxHPRate, 0.0f); }
  public static void addMaxMPRate(FlatBufferBuilder builder, float maxMPRate) { builder.addFloat(30, maxMPRate, 0.0f); }
  public static void addCoolTimeReduceRate(FlatBufferBuilder builder, float coolTimeReduceRate) { builder.addFloat(31, coolTimeReduceRate, 0.0f); }
  public static void addMoveSpeedBonus(FlatBufferBuilder builder, float moveSpeedBonus) { builder.addFloat(32, moveSpeedBonus, 0.0f); }
  public static void addAttackDamageBonus(FlatBufferBuilder builder, float attackDamageBonus) { builder.addFloat(33, attackDamageBonus, 0.0f); }
  public static void addDefenseBonus(FlatBufferBuilder builder, float defenseBonus) { builder.addFloat(34, defenseBonus, 0.0f); }
  public static void addMaxHPBonus(FlatBufferBuilder builder, float maxHPBonus) { builder.addFloat(35, maxHPBonus, 0.0f); }
  public static void addMaxMPBonus(FlatBufferBuilder builder, float maxMPBonus) { builder.addFloat(36, maxMPBonus, 0.0f); }
  public static void addIsAirborneImmunity(FlatBufferBuilder builder, boolean isAirborneImmunity) { builder.addBoolean(37, isAirborneImmunity, false); }
  public static void addIsAirborne(FlatBufferBuilder builder, boolean isAirborne) { builder.addBoolean(38, isAirborne, false); }
  public static void addIsTargetingInvincible(FlatBufferBuilder builder, boolean isTargetingInvincible) { builder.addBoolean(39, isTargetingInvincible, false); }
  public static void addIsStunned(FlatBufferBuilder builder, boolean isStunned) { builder.addBoolean(40, isStunned, false); }
  public static void addIsFreezing(FlatBufferBuilder builder, boolean isFreezing) { builder.addBoolean(41, isFreezing, false); }
  public static void addIsSlow(FlatBufferBuilder builder, boolean isSlow) { builder.addBoolean(42, isSlow, false); }
  public static void addIsSilence(FlatBufferBuilder builder, boolean isSilence) { builder.addBoolean(43, isSilence, false); }
  public static void addIsBlind(FlatBufferBuilder builder, boolean isBlind) { builder.addBoolean(44, isBlind, false); }
  public static void addIsSightBlocked(FlatBufferBuilder builder, boolean isSightBlocked) { builder.addBoolean(45, isSightBlocked, false); }
  public static void addIsGrounding(FlatBufferBuilder builder, boolean isGrounding) { builder.addBoolean(46, isGrounding, false); }
  public static void addIsPolymorph(FlatBufferBuilder builder, boolean isPolymorph) { builder.addBoolean(47, isPolymorph, false); }
  public static void addIsDisarmed(FlatBufferBuilder builder, boolean isDisarmed) { builder.addBoolean(48, isDisarmed, false); }
  public static void addIsSnare(FlatBufferBuilder builder, boolean isSnare) { builder.addBoolean(49, isSnare, false); }
  public static void addIsKnockedAirborne(FlatBufferBuilder builder, boolean isKnockedAirborne) { builder.addBoolean(50, isKnockedAirborne, false); }
  public static void addIsKnockback(FlatBufferBuilder builder, boolean isKnockback) { builder.addBoolean(51, isKnockback, false); }
  public static void addIsSuspension(FlatBufferBuilder builder, boolean isSuspension) { builder.addBoolean(52, isSuspension, false); }
  public static void addIsTaunt(FlatBufferBuilder builder, boolean isTaunt) { builder.addBoolean(53, isTaunt, false); }
  public static void addIsCharm(FlatBufferBuilder builder, boolean isCharm) { builder.addBoolean(54, isCharm, false); }
  public static void addIsFlee(FlatBufferBuilder builder, boolean isFlee) { builder.addBoolean(55, isFlee, false); }
  public static void addIsSuppressed(FlatBufferBuilder builder, boolean isSuppressed) { builder.addBoolean(56, isSuppressed, false); }
  public static void addIsSleep(FlatBufferBuilder builder, boolean isSleep) { builder.addBoolean(57, isSleep, false); }
  public static int endflat_BarricadeData(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_BarricadeData(FlatBufferBuilder builder,
 BarricadeData data) {
        return createflat_BarricadeData(builder , data.entityID, data.team, data.costTime, data.costGold, data.upgradeLevel, data.currentHP, data.maxHP, data.recoveryRateHP, data.shieldAmount, data.isShieldActivated, data.defense, data.posX, data.posY, data.posZ, data.isDisableMove, data.isDisableAttack, data.isDisableSkill, data.isDisableItem, data.isDamageImmunity, data.isUnTargetable, data.moveSpeedRate, data.attackSpeedRate, data.hpRecoveryRate, data.mpRecoveryRate, data.goldGainRate, data.expGainRate, data.buffDurationRate, data.attackDamageRate, data.defenseRate, data.maxHPRate, data.maxMPRate, data.coolTimeReduceRate, data.moveSpeedBonus, data.attackDamageBonus, data.defenseBonus, data.maxHPBonus, data.maxMPBonus, data.isAirborneImmunity, data.isAirborne, data.isTargetingInvincible, data.isStunned, data.isFreezing, data.isSlow, data.isSilence, data.isBlind, data.isSightBlocked, data.isGrounding, data.isPolymorph, data.isDisarmed, data.isSnare, data.isKnockedAirborne, data.isKnockback, data.isSuspension, data.isTaunt, data.isCharm, data.isFlee, data.isSuppressed, data.isSleep);
    }

    public static byte[] createflat_BarricadeData(BarricadeData data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_BarricadeData.createflat_BarricadeData(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static BarricadeData getRootAsflat_BarricadeData(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        BarricadeData result = new BarricadeData(flat_BarricadeData.getRootAsflat_BarricadeData( buf ) );
        buf = null;
        return result;
    }

}