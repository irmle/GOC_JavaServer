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
public final class flat_CrystalData extends Table {
  public static flat_CrystalData getRootAsflat_CrystalData(ByteBuffer _bb) { return getRootAsflat_CrystalData(_bb, new flat_CrystalData()); }
  public static flat_CrystalData getRootAsflat_CrystalData(ByteBuffer _bb, flat_CrystalData obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_CrystalData __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int entityID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int team() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int crystalLevel() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float currentHP() { int o = __offset(10); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHP() { int o = __offset(12); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float recoveryRateHP() { int o = __offset(14); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float shieldAmount() { int o = __offset(16); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isShieldActivated() { int o = __offset(18); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public float defense() { int o = __offset(20); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posX() { int o = __offset(22); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posY() { int o = __offset(24); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posZ() { int o = __offset(26); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isDisableMove() { int o = __offset(28); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableAttack() { int o = __offset(30); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableSkill() { int o = __offset(32); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableItem() { int o = __offset(34); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDamageImmunity() { int o = __offset(36); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isUnTargetable() { int o = __offset(38); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public float moveSpeedRate() { int o = __offset(40); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackSpeedRate() { int o = __offset(42); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float hpRecoveryRate() { int o = __offset(44); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float mpRecoveryRate() { int o = __offset(46); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float goldGainRate() { int o = __offset(48); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float expGainRate() { int o = __offset(50); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float buffDurationRate() { int o = __offset(52); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamageRate() { int o = __offset(54); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defenseRate() { int o = __offset(56); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHPRate() { int o = __offset(58); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMPRate() { int o = __offset(60); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float coolTimeReduceRate() { int o = __offset(62); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float moveSpeedBonus() { int o = __offset(64); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamageBonus() { int o = __offset(66); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defenseBonus() { int o = __offset(68); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHPBonus() { int o = __offset(70); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMPBonus() { int o = __offset(72); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float bloodSuckingRate() { int o = __offset(74); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float criticalChanceRate() { int o = __offset(76); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float criticalDamageRate() { int o = __offset(78); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isAirborneImmunity() { int o = __offset(80); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isAirborne() { int o = __offset(82); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isTargetingInvincible() { int o = __offset(84); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isStunned() { int o = __offset(86); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isFreezing() { int o = __offset(88); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSlow() { int o = __offset(90); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSilence() { int o = __offset(92); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isBlind() { int o = __offset(94); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSightBlocked() { int o = __offset(96); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isGrounding() { int o = __offset(98); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isPolymorph() { int o = __offset(100); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisarmed() { int o = __offset(102); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSnare() { int o = __offset(104); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isKnockedAirborne() { int o = __offset(106); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isKnockback() { int o = __offset(108); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSuspension() { int o = __offset(110); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isTaunt() { int o = __offset(112); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isCharm() { int o = __offset(114); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isFlee() { int o = __offset(116); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSuppressed() { int o = __offset(118); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSleep() { int o = __offset(120); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }

  public static int createflat_CrystalData(FlatBufferBuilder builder,
      int entityID,
      int team,
      int crystalLevel,
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
    builder.startObject(59);
    flat_CrystalData.addCriticalDamageRate(builder, criticalDamageRate);
    flat_CrystalData.addCriticalChanceRate(builder, criticalChanceRate);
    flat_CrystalData.addBloodSuckingRate(builder, bloodSuckingRate);
    flat_CrystalData.addMaxMPBonus(builder, maxMPBonus);
    flat_CrystalData.addMaxHPBonus(builder, maxHPBonus);
    flat_CrystalData.addDefenseBonus(builder, defenseBonus);
    flat_CrystalData.addAttackDamageBonus(builder, attackDamageBonus);
    flat_CrystalData.addMoveSpeedBonus(builder, moveSpeedBonus);
    flat_CrystalData.addCoolTimeReduceRate(builder, coolTimeReduceRate);
    flat_CrystalData.addMaxMPRate(builder, maxMPRate);
    flat_CrystalData.addMaxHPRate(builder, maxHPRate);
    flat_CrystalData.addDefenseRate(builder, defenseRate);
    flat_CrystalData.addAttackDamageRate(builder, attackDamageRate);
    flat_CrystalData.addBuffDurationRate(builder, buffDurationRate);
    flat_CrystalData.addExpGainRate(builder, expGainRate);
    flat_CrystalData.addGoldGainRate(builder, goldGainRate);
    flat_CrystalData.addMpRecoveryRate(builder, mpRecoveryRate);
    flat_CrystalData.addHpRecoveryRate(builder, hpRecoveryRate);
    flat_CrystalData.addAttackSpeedRate(builder, attackSpeedRate);
    flat_CrystalData.addMoveSpeedRate(builder, moveSpeedRate);
    flat_CrystalData.addPosZ(builder, posZ);
    flat_CrystalData.addPosY(builder, posY);
    flat_CrystalData.addPosX(builder, posX);
    flat_CrystalData.addDefense(builder, defense);
    flat_CrystalData.addShieldAmount(builder, shieldAmount);
    flat_CrystalData.addRecoveryRateHP(builder, recoveryRateHP);
    flat_CrystalData.addMaxHP(builder, maxHP);
    flat_CrystalData.addCurrentHP(builder, currentHP);
    flat_CrystalData.addCrystalLevel(builder, crystalLevel);
    flat_CrystalData.addTeam(builder, team);
    flat_CrystalData.addEntityID(builder, entityID);
    flat_CrystalData.addIsSleep(builder, isSleep);
    flat_CrystalData.addIsSuppressed(builder, isSuppressed);
    flat_CrystalData.addIsFlee(builder, isFlee);
    flat_CrystalData.addIsCharm(builder, isCharm);
    flat_CrystalData.addIsTaunt(builder, isTaunt);
    flat_CrystalData.addIsSuspension(builder, isSuspension);
    flat_CrystalData.addIsKnockback(builder, isKnockback);
    flat_CrystalData.addIsKnockedAirborne(builder, isKnockedAirborne);
    flat_CrystalData.addIsSnare(builder, isSnare);
    flat_CrystalData.addIsDisarmed(builder, isDisarmed);
    flat_CrystalData.addIsPolymorph(builder, isPolymorph);
    flat_CrystalData.addIsGrounding(builder, isGrounding);
    flat_CrystalData.addIsSightBlocked(builder, isSightBlocked);
    flat_CrystalData.addIsBlind(builder, isBlind);
    flat_CrystalData.addIsSilence(builder, isSilence);
    flat_CrystalData.addIsSlow(builder, isSlow);
    flat_CrystalData.addIsFreezing(builder, isFreezing);
    flat_CrystalData.addIsStunned(builder, isStunned);
    flat_CrystalData.addIsTargetingInvincible(builder, isTargetingInvincible);
    flat_CrystalData.addIsAirborne(builder, isAirborne);
    flat_CrystalData.addIsAirborneImmunity(builder, isAirborneImmunity);
    flat_CrystalData.addIsUnTargetable(builder, isUnTargetable);
    flat_CrystalData.addIsDamageImmunity(builder, isDamageImmunity);
    flat_CrystalData.addIsDisableItem(builder, isDisableItem);
    flat_CrystalData.addIsDisableSkill(builder, isDisableSkill);
    flat_CrystalData.addIsDisableAttack(builder, isDisableAttack);
    flat_CrystalData.addIsDisableMove(builder, isDisableMove);
    flat_CrystalData.addIsShieldActivated(builder, isShieldActivated);
    return flat_CrystalData.endflat_CrystalData(builder);
  }

  public static void startflat_CrystalData(FlatBufferBuilder builder) { builder.startObject(59); }
  public static void addEntityID(FlatBufferBuilder builder, int entityID) { builder.addInt(0, entityID, 0); }
  public static void addTeam(FlatBufferBuilder builder, int team) { builder.addInt(1, team, 0); }
  public static void addCrystalLevel(FlatBufferBuilder builder, int crystalLevel) { builder.addInt(2, crystalLevel, 0); }
  public static void addCurrentHP(FlatBufferBuilder builder, float currentHP) { builder.addFloat(3, currentHP, 0.0f); }
  public static void addMaxHP(FlatBufferBuilder builder, float maxHP) { builder.addFloat(4, maxHP, 0.0f); }
  public static void addRecoveryRateHP(FlatBufferBuilder builder, float recoveryRateHP) { builder.addFloat(5, recoveryRateHP, 0.0f); }
  public static void addShieldAmount(FlatBufferBuilder builder, float shieldAmount) { builder.addFloat(6, shieldAmount, 0.0f); }
  public static void addIsShieldActivated(FlatBufferBuilder builder, boolean isShieldActivated) { builder.addBoolean(7, isShieldActivated, false); }
  public static void addDefense(FlatBufferBuilder builder, float defense) { builder.addFloat(8, defense, 0.0f); }
  public static void addPosX(FlatBufferBuilder builder, float posX) { builder.addFloat(9, posX, 0.0f); }
  public static void addPosY(FlatBufferBuilder builder, float posY) { builder.addFloat(10, posY, 0.0f); }
  public static void addPosZ(FlatBufferBuilder builder, float posZ) { builder.addFloat(11, posZ, 0.0f); }
  public static void addIsDisableMove(FlatBufferBuilder builder, boolean isDisableMove) { builder.addBoolean(12, isDisableMove, false); }
  public static void addIsDisableAttack(FlatBufferBuilder builder, boolean isDisableAttack) { builder.addBoolean(13, isDisableAttack, false); }
  public static void addIsDisableSkill(FlatBufferBuilder builder, boolean isDisableSkill) { builder.addBoolean(14, isDisableSkill, false); }
  public static void addIsDisableItem(FlatBufferBuilder builder, boolean isDisableItem) { builder.addBoolean(15, isDisableItem, false); }
  public static void addIsDamageImmunity(FlatBufferBuilder builder, boolean isDamageImmunity) { builder.addBoolean(16, isDamageImmunity, false); }
  public static void addIsUnTargetable(FlatBufferBuilder builder, boolean isUnTargetable) { builder.addBoolean(17, isUnTargetable, false); }
  public static void addMoveSpeedRate(FlatBufferBuilder builder, float moveSpeedRate) { builder.addFloat(18, moveSpeedRate, 0.0f); }
  public static void addAttackSpeedRate(FlatBufferBuilder builder, float attackSpeedRate) { builder.addFloat(19, attackSpeedRate, 0.0f); }
  public static void addHpRecoveryRate(FlatBufferBuilder builder, float hpRecoveryRate) { builder.addFloat(20, hpRecoveryRate, 0.0f); }
  public static void addMpRecoveryRate(FlatBufferBuilder builder, float mpRecoveryRate) { builder.addFloat(21, mpRecoveryRate, 0.0f); }
  public static void addGoldGainRate(FlatBufferBuilder builder, float goldGainRate) { builder.addFloat(22, goldGainRate, 0.0f); }
  public static void addExpGainRate(FlatBufferBuilder builder, float expGainRate) { builder.addFloat(23, expGainRate, 0.0f); }
  public static void addBuffDurationRate(FlatBufferBuilder builder, float buffDurationRate) { builder.addFloat(24, buffDurationRate, 0.0f); }
  public static void addAttackDamageRate(FlatBufferBuilder builder, float attackDamageRate) { builder.addFloat(25, attackDamageRate, 0.0f); }
  public static void addDefenseRate(FlatBufferBuilder builder, float defenseRate) { builder.addFloat(26, defenseRate, 0.0f); }
  public static void addMaxHPRate(FlatBufferBuilder builder, float maxHPRate) { builder.addFloat(27, maxHPRate, 0.0f); }
  public static void addMaxMPRate(FlatBufferBuilder builder, float maxMPRate) { builder.addFloat(28, maxMPRate, 0.0f); }
  public static void addCoolTimeReduceRate(FlatBufferBuilder builder, float coolTimeReduceRate) { builder.addFloat(29, coolTimeReduceRate, 0.0f); }
  public static void addMoveSpeedBonus(FlatBufferBuilder builder, float moveSpeedBonus) { builder.addFloat(30, moveSpeedBonus, 0.0f); }
  public static void addAttackDamageBonus(FlatBufferBuilder builder, float attackDamageBonus) { builder.addFloat(31, attackDamageBonus, 0.0f); }
  public static void addDefenseBonus(FlatBufferBuilder builder, float defenseBonus) { builder.addFloat(32, defenseBonus, 0.0f); }
  public static void addMaxHPBonus(FlatBufferBuilder builder, float maxHPBonus) { builder.addFloat(33, maxHPBonus, 0.0f); }
  public static void addMaxMPBonus(FlatBufferBuilder builder, float maxMPBonus) { builder.addFloat(34, maxMPBonus, 0.0f); }
  public static void addBloodSuckingRate(FlatBufferBuilder builder, float bloodSuckingRate) { builder.addFloat(35, bloodSuckingRate, 0.0f); }
  public static void addCriticalChanceRate(FlatBufferBuilder builder, float criticalChanceRate) { builder.addFloat(36, criticalChanceRate, 0.0f); }
  public static void addCriticalDamageRate(FlatBufferBuilder builder, float criticalDamageRate) { builder.addFloat(37, criticalDamageRate, 0.0f); }
  public static void addIsAirborneImmunity(FlatBufferBuilder builder, boolean isAirborneImmunity) { builder.addBoolean(38, isAirborneImmunity, false); }
  public static void addIsAirborne(FlatBufferBuilder builder, boolean isAirborne) { builder.addBoolean(39, isAirborne, false); }
  public static void addIsTargetingInvincible(FlatBufferBuilder builder, boolean isTargetingInvincible) { builder.addBoolean(40, isTargetingInvincible, false); }
  public static void addIsStunned(FlatBufferBuilder builder, boolean isStunned) { builder.addBoolean(41, isStunned, false); }
  public static void addIsFreezing(FlatBufferBuilder builder, boolean isFreezing) { builder.addBoolean(42, isFreezing, false); }
  public static void addIsSlow(FlatBufferBuilder builder, boolean isSlow) { builder.addBoolean(43, isSlow, false); }
  public static void addIsSilence(FlatBufferBuilder builder, boolean isSilence) { builder.addBoolean(44, isSilence, false); }
  public static void addIsBlind(FlatBufferBuilder builder, boolean isBlind) { builder.addBoolean(45, isBlind, false); }
  public static void addIsSightBlocked(FlatBufferBuilder builder, boolean isSightBlocked) { builder.addBoolean(46, isSightBlocked, false); }
  public static void addIsGrounding(FlatBufferBuilder builder, boolean isGrounding) { builder.addBoolean(47, isGrounding, false); }
  public static void addIsPolymorph(FlatBufferBuilder builder, boolean isPolymorph) { builder.addBoolean(48, isPolymorph, false); }
  public static void addIsDisarmed(FlatBufferBuilder builder, boolean isDisarmed) { builder.addBoolean(49, isDisarmed, false); }
  public static void addIsSnare(FlatBufferBuilder builder, boolean isSnare) { builder.addBoolean(50, isSnare, false); }
  public static void addIsKnockedAirborne(FlatBufferBuilder builder, boolean isKnockedAirborne) { builder.addBoolean(51, isKnockedAirborne, false); }
  public static void addIsKnockback(FlatBufferBuilder builder, boolean isKnockback) { builder.addBoolean(52, isKnockback, false); }
  public static void addIsSuspension(FlatBufferBuilder builder, boolean isSuspension) { builder.addBoolean(53, isSuspension, false); }
  public static void addIsTaunt(FlatBufferBuilder builder, boolean isTaunt) { builder.addBoolean(54, isTaunt, false); }
  public static void addIsCharm(FlatBufferBuilder builder, boolean isCharm) { builder.addBoolean(55, isCharm, false); }
  public static void addIsFlee(FlatBufferBuilder builder, boolean isFlee) { builder.addBoolean(56, isFlee, false); }
  public static void addIsSuppressed(FlatBufferBuilder builder, boolean isSuppressed) { builder.addBoolean(57, isSuppressed, false); }
  public static void addIsSleep(FlatBufferBuilder builder, boolean isSleep) { builder.addBoolean(58, isSleep, false); }
  public static int endflat_CrystalData(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_CrystalData(FlatBufferBuilder builder,
 CrystalData data) {
        return createflat_CrystalData(builder , data.entityID, data.team, data.crystalLevel, data.currentHP, data.maxHP, data.recoveryRateHP, data.shieldAmount, data.isShieldActivated, data.defense, data.posX, data.posY, data.posZ, data.isDisableMove, data.isDisableAttack, data.isDisableSkill, data.isDisableItem, data.isDamageImmunity, data.isUnTargetable, data.moveSpeedRate, data.attackSpeedRate, data.hpRecoveryRate, data.mpRecoveryRate, data.goldGainRate, data.expGainRate, data.buffDurationRate, data.attackDamageRate, data.defenseRate, data.maxHPRate, data.maxMPRate, data.coolTimeReduceRate, data.moveSpeedBonus, data.attackDamageBonus, data.defenseBonus, data.maxHPBonus, data.maxMPBonus, data.bloodSuckingRate, data.criticalChanceRate, data.criticalDamageRate, data.isAirborneImmunity, data.isAirborne, data.isTargetingInvincible, data.isStunned, data.isFreezing, data.isSlow, data.isSilence, data.isBlind, data.isSightBlocked, data.isGrounding, data.isPolymorph, data.isDisarmed, data.isSnare, data.isKnockedAirborne, data.isKnockback, data.isSuspension, data.isTaunt, data.isCharm, data.isFlee, data.isSuppressed, data.isSleep);
    }

    public static byte[] createflat_CrystalData(CrystalData data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_CrystalData.createflat_CrystalData(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static CrystalData getRootAsflat_CrystalData(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        CrystalData result = new CrystalData(flat_CrystalData.getRootAsflat_CrystalData( buf ) );
        buf = null;
        return result;
    }

}