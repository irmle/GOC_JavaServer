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
public final class flat_CharacterData extends Table {
  public static flat_CharacterData getRootAsflat_CharacterData(ByteBuffer _bb) { return getRootAsflat_CharacterData(_bb, new flat_CharacterData()); }
  public static flat_CharacterData getRootAsflat_CharacterData(ByteBuffer _bb, flat_CharacterData obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public flat_CharacterData __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public int entityID() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int team() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int characterType() { int o = __offset(8); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public String characterName() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer characterNameAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public ByteBuffer characterNameInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 10, 1); }
  public int characterElemental() { int o = __offset(12); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int kill() { int o = __offset(14); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int death() { int o = __offset(16); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float moveSpeed() { int o = __offset(18); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public flat_SkillSlotData skillSlot(int j) { return skillSlot(new flat_SkillSlotData(), j); }
  public flat_SkillSlotData skillSlot(flat_SkillSlotData obj, int j) { int o = __offset(20); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int skillSlotLength() { int o = __offset(20); return o != 0 ? __vector_len(o) : 0; }
  public flat_ItemSlotData itemSlot(int j) { return itemSlot(new flat_ItemSlotData(), j); }
  public flat_ItemSlotData itemSlot(flat_ItemSlotData obj, int j) { int o = __offset(22); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int itemSlotLength() { int o = __offset(22); return o != 0 ? __vector_len(o) : 0; }
  public flat_ConditionData conditionList(int j) { return conditionList(new flat_ConditionData(), j); }
  public flat_ConditionData conditionList(flat_ConditionData obj, int j) { int o = __offset(24); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int conditionListLength() { int o = __offset(24); return o != 0 ? __vector_len(o) : 0; }
  public int level() { int o = __offset(26); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int exp() { int o = __offset(28); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public int gold() { int o = __offset(30); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public float currentHP() { int o = __offset(32); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHP() { int o = __offset(34); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float recoveryRateHP() { int o = __offset(36); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float shieldAmount() { int o = __offset(38); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float currentMP() { int o = __offset(40); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMP() { int o = __offset(42); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float recoveryRateMP() { int o = __offset(44); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamage() { int o = __offset(46); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackSpeed() { int o = __offset(48); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackRange() { int o = __offset(50); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float criticalChance() { int o = __offset(52); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float criticalDamage() { int o = __offset(54); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defense() { int o = __offset(56); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posX() { int o = __offset(58); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posY() { int o = __offset(60); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float posZ() { int o = __offset(62); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float velX() { int o = __offset(64); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float velY() { int o = __offset(66); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float velZ() { int o = __offset(68); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float quarternionY() { int o = __offset(70); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float quarternionZ() { int o = __offset(72); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isDisableMove() { int o = __offset(74); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableAttack() { int o = __offset(76); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableSkill() { int o = __offset(78); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisableItem() { int o = __offset(80); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDamageImmunity() { int o = __offset(82); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isUnTargetable() { int o = __offset(84); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public float moveSpeedRate() { int o = __offset(86); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackSpeedRate() { int o = __offset(88); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float hpRecoveryRate() { int o = __offset(90); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float mpRecoveryRate() { int o = __offset(92); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float goldGainRate() { int o = __offset(94); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float expGainRate() { int o = __offset(96); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float buffDurationRate() { int o = __offset(98); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamageRate() { int o = __offset(100); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defenseRate() { int o = __offset(102); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHPRate() { int o = __offset(104); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMPRate() { int o = __offset(106); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float coolTimeReduceRate() { int o = __offset(108); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float moveSpeedBonus() { int o = __offset(110); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float attackDamageBonus() { int o = __offset(112); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float defenseBonus() { int o = __offset(114); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxHPBonus() { int o = __offset(116); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float maxMPBonus() { int o = __offset(118); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float bloodSuckingRate() { int o = __offset(120); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float criticalChanceRate() { int o = __offset(122); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float criticalDamageRate() { int o = __offset(124); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public boolean isAirborneImmunity() { int o = __offset(126); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isAirborne() { int o = __offset(128); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isTargetingInvincible() { int o = __offset(130); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isStunned() { int o = __offset(132); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isFreezing() { int o = __offset(134); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSlow() { int o = __offset(136); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSilence() { int o = __offset(138); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isBlind() { int o = __offset(140); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSightBlocked() { int o = __offset(142); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isGrounding() { int o = __offset(144); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isPolymorph() { int o = __offset(146); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isDisarmed() { int o = __offset(148); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSnare() { int o = __offset(150); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isKnockedAirborne() { int o = __offset(152); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isKnockback() { int o = __offset(154); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSuspension() { int o = __offset(156); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isTaunt() { int o = __offset(158); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isCharm() { int o = __offset(160); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isFlee() { int o = __offset(162); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSuppressed() { int o = __offset(164); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean isSleep() { int o = __offset(166); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }

  public static int createflat_CharacterData(FlatBufferBuilder builder,
      int entityID,
      int team,
      int characterType,
      int characterNameOffset,
      int characterElemental,
      int kill,
      int death,
      float moveSpeed,
      int skillSlotOffset,
      int itemSlotOffset,
      int conditionListOffset,
      int level,
      int exp,
      int gold,
      float currentHP,
      float maxHP,
      float recoveryRateHP,
      float shieldAmount,
      float currentMP,
      float maxMP,
      float recoveryRateMP,
      float attackDamage,
      float attackSpeed,
      float attackRange,
      float criticalChance,
      float criticalDamage,
      float defense,
      float posX,
      float posY,
      float posZ,
      float velX,
      float velY,
      float velZ,
      float quarternionY,
      float quarternionZ,
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
    builder.startObject(82);
    flat_CharacterData.addCriticalDamageRate(builder, criticalDamageRate);
    flat_CharacterData.addCriticalChanceRate(builder, criticalChanceRate);
    flat_CharacterData.addBloodSuckingRate(builder, bloodSuckingRate);
    flat_CharacterData.addMaxMPBonus(builder, maxMPBonus);
    flat_CharacterData.addMaxHPBonus(builder, maxHPBonus);
    flat_CharacterData.addDefenseBonus(builder, defenseBonus);
    flat_CharacterData.addAttackDamageBonus(builder, attackDamageBonus);
    flat_CharacterData.addMoveSpeedBonus(builder, moveSpeedBonus);
    flat_CharacterData.addCoolTimeReduceRate(builder, coolTimeReduceRate);
    flat_CharacterData.addMaxMPRate(builder, maxMPRate);
    flat_CharacterData.addMaxHPRate(builder, maxHPRate);
    flat_CharacterData.addDefenseRate(builder, defenseRate);
    flat_CharacterData.addAttackDamageRate(builder, attackDamageRate);
    flat_CharacterData.addBuffDurationRate(builder, buffDurationRate);
    flat_CharacterData.addExpGainRate(builder, expGainRate);
    flat_CharacterData.addGoldGainRate(builder, goldGainRate);
    flat_CharacterData.addMpRecoveryRate(builder, mpRecoveryRate);
    flat_CharacterData.addHpRecoveryRate(builder, hpRecoveryRate);
    flat_CharacterData.addAttackSpeedRate(builder, attackSpeedRate);
    flat_CharacterData.addMoveSpeedRate(builder, moveSpeedRate);
    flat_CharacterData.addQuarternionZ(builder, quarternionZ);
    flat_CharacterData.addQuarternionY(builder, quarternionY);
    flat_CharacterData.addVelZ(builder, velZ);
    flat_CharacterData.addVelY(builder, velY);
    flat_CharacterData.addVelX(builder, velX);
    flat_CharacterData.addPosZ(builder, posZ);
    flat_CharacterData.addPosY(builder, posY);
    flat_CharacterData.addPosX(builder, posX);
    flat_CharacterData.addDefense(builder, defense);
    flat_CharacterData.addCriticalDamage(builder, criticalDamage);
    flat_CharacterData.addCriticalChance(builder, criticalChance);
    flat_CharacterData.addAttackRange(builder, attackRange);
    flat_CharacterData.addAttackSpeed(builder, attackSpeed);
    flat_CharacterData.addAttackDamage(builder, attackDamage);
    flat_CharacterData.addRecoveryRateMP(builder, recoveryRateMP);
    flat_CharacterData.addMaxMP(builder, maxMP);
    flat_CharacterData.addCurrentMP(builder, currentMP);
    flat_CharacterData.addShieldAmount(builder, shieldAmount);
    flat_CharacterData.addRecoveryRateHP(builder, recoveryRateHP);
    flat_CharacterData.addMaxHP(builder, maxHP);
    flat_CharacterData.addCurrentHP(builder, currentHP);
    flat_CharacterData.addGold(builder, gold);
    flat_CharacterData.addExp(builder, exp);
    flat_CharacterData.addLevel(builder, level);
    flat_CharacterData.addConditionList(builder, conditionListOffset);
    flat_CharacterData.addItemSlot(builder, itemSlotOffset);
    flat_CharacterData.addSkillSlot(builder, skillSlotOffset);
    flat_CharacterData.addMoveSpeed(builder, moveSpeed);
    flat_CharacterData.addDeath(builder, death);
    flat_CharacterData.addKill(builder, kill);
    flat_CharacterData.addCharacterElemental(builder, characterElemental);
    flat_CharacterData.addCharacterName(builder, characterNameOffset);
    flat_CharacterData.addCharacterType(builder, characterType);
    flat_CharacterData.addTeam(builder, team);
    flat_CharacterData.addEntityID(builder, entityID);
    flat_CharacterData.addIsSleep(builder, isSleep);
    flat_CharacterData.addIsSuppressed(builder, isSuppressed);
    flat_CharacterData.addIsFlee(builder, isFlee);
    flat_CharacterData.addIsCharm(builder, isCharm);
    flat_CharacterData.addIsTaunt(builder, isTaunt);
    flat_CharacterData.addIsSuspension(builder, isSuspension);
    flat_CharacterData.addIsKnockback(builder, isKnockback);
    flat_CharacterData.addIsKnockedAirborne(builder, isKnockedAirborne);
    flat_CharacterData.addIsSnare(builder, isSnare);
    flat_CharacterData.addIsDisarmed(builder, isDisarmed);
    flat_CharacterData.addIsPolymorph(builder, isPolymorph);
    flat_CharacterData.addIsGrounding(builder, isGrounding);
    flat_CharacterData.addIsSightBlocked(builder, isSightBlocked);
    flat_CharacterData.addIsBlind(builder, isBlind);
    flat_CharacterData.addIsSilence(builder, isSilence);
    flat_CharacterData.addIsSlow(builder, isSlow);
    flat_CharacterData.addIsFreezing(builder, isFreezing);
    flat_CharacterData.addIsStunned(builder, isStunned);
    flat_CharacterData.addIsTargetingInvincible(builder, isTargetingInvincible);
    flat_CharacterData.addIsAirborne(builder, isAirborne);
    flat_CharacterData.addIsAirborneImmunity(builder, isAirborneImmunity);
    flat_CharacterData.addIsUnTargetable(builder, isUnTargetable);
    flat_CharacterData.addIsDamageImmunity(builder, isDamageImmunity);
    flat_CharacterData.addIsDisableItem(builder, isDisableItem);
    flat_CharacterData.addIsDisableSkill(builder, isDisableSkill);
    flat_CharacterData.addIsDisableAttack(builder, isDisableAttack);
    flat_CharacterData.addIsDisableMove(builder, isDisableMove);
    return flat_CharacterData.endflat_CharacterData(builder);
  }

  public static void startflat_CharacterData(FlatBufferBuilder builder) { builder.startObject(82); }
  public static void addEntityID(FlatBufferBuilder builder, int entityID) { builder.addInt(0, entityID, 0); }
  public static void addTeam(FlatBufferBuilder builder, int team) { builder.addInt(1, team, 0); }
  public static void addCharacterType(FlatBufferBuilder builder, int characterType) { builder.addInt(2, characterType, 0); }
  public static void addCharacterName(FlatBufferBuilder builder, int characterNameOffset) { builder.addOffset(3, characterNameOffset, 0); }
  public static void addCharacterElemental(FlatBufferBuilder builder, int characterElemental) { builder.addInt(4, characterElemental, 0); }
  public static void addKill(FlatBufferBuilder builder, int kill) { builder.addInt(5, kill, 0); }
  public static void addDeath(FlatBufferBuilder builder, int death) { builder.addInt(6, death, 0); }
  public static void addMoveSpeed(FlatBufferBuilder builder, float moveSpeed) { builder.addFloat(7, moveSpeed, 0.0f); }
  public static void addSkillSlot(FlatBufferBuilder builder, int skillSlotOffset) { builder.addOffset(8, skillSlotOffset, 0); }
  public static int createSkillSlotVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startSkillSlotVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addItemSlot(FlatBufferBuilder builder, int itemSlotOffset) { builder.addOffset(9, itemSlotOffset, 0); }
  public static int createItemSlotVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startItemSlotVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addConditionList(FlatBufferBuilder builder, int conditionListOffset) { builder.addOffset(10, conditionListOffset, 0); }
  public static int createConditionListVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startConditionListVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addLevel(FlatBufferBuilder builder, int level) { builder.addInt(11, level, 0); }
  public static void addExp(FlatBufferBuilder builder, int exp) { builder.addInt(12, exp, 0); }
  public static void addGold(FlatBufferBuilder builder, int gold) { builder.addInt(13, gold, 0); }
  public static void addCurrentHP(FlatBufferBuilder builder, float currentHP) { builder.addFloat(14, currentHP, 0.0f); }
  public static void addMaxHP(FlatBufferBuilder builder, float maxHP) { builder.addFloat(15, maxHP, 0.0f); }
  public static void addRecoveryRateHP(FlatBufferBuilder builder, float recoveryRateHP) { builder.addFloat(16, recoveryRateHP, 0.0f); }
  public static void addShieldAmount(FlatBufferBuilder builder, float shieldAmount) { builder.addFloat(17, shieldAmount, 0.0f); }
  public static void addCurrentMP(FlatBufferBuilder builder, float currentMP) { builder.addFloat(18, currentMP, 0.0f); }
  public static void addMaxMP(FlatBufferBuilder builder, float maxMP) { builder.addFloat(19, maxMP, 0.0f); }
  public static void addRecoveryRateMP(FlatBufferBuilder builder, float recoveryRateMP) { builder.addFloat(20, recoveryRateMP, 0.0f); }
  public static void addAttackDamage(FlatBufferBuilder builder, float attackDamage) { builder.addFloat(21, attackDamage, 0.0f); }
  public static void addAttackSpeed(FlatBufferBuilder builder, float attackSpeed) { builder.addFloat(22, attackSpeed, 0.0f); }
  public static void addAttackRange(FlatBufferBuilder builder, float attackRange) { builder.addFloat(23, attackRange, 0.0f); }
  public static void addCriticalChance(FlatBufferBuilder builder, float criticalChance) { builder.addFloat(24, criticalChance, 0.0f); }
  public static void addCriticalDamage(FlatBufferBuilder builder, float criticalDamage) { builder.addFloat(25, criticalDamage, 0.0f); }
  public static void addDefense(FlatBufferBuilder builder, float defense) { builder.addFloat(26, defense, 0.0f); }
  public static void addPosX(FlatBufferBuilder builder, float posX) { builder.addFloat(27, posX, 0.0f); }
  public static void addPosY(FlatBufferBuilder builder, float posY) { builder.addFloat(28, posY, 0.0f); }
  public static void addPosZ(FlatBufferBuilder builder, float posZ) { builder.addFloat(29, posZ, 0.0f); }
  public static void addVelX(FlatBufferBuilder builder, float velX) { builder.addFloat(30, velX, 0.0f); }
  public static void addVelY(FlatBufferBuilder builder, float velY) { builder.addFloat(31, velY, 0.0f); }
  public static void addVelZ(FlatBufferBuilder builder, float velZ) { builder.addFloat(32, velZ, 0.0f); }
  public static void addQuarternionY(FlatBufferBuilder builder, float quarternionY) { builder.addFloat(33, quarternionY, 0.0f); }
  public static void addQuarternionZ(FlatBufferBuilder builder, float quarternionZ) { builder.addFloat(34, quarternionZ, 0.0f); }
  public static void addIsDisableMove(FlatBufferBuilder builder, boolean isDisableMove) { builder.addBoolean(35, isDisableMove, false); }
  public static void addIsDisableAttack(FlatBufferBuilder builder, boolean isDisableAttack) { builder.addBoolean(36, isDisableAttack, false); }
  public static void addIsDisableSkill(FlatBufferBuilder builder, boolean isDisableSkill) { builder.addBoolean(37, isDisableSkill, false); }
  public static void addIsDisableItem(FlatBufferBuilder builder, boolean isDisableItem) { builder.addBoolean(38, isDisableItem, false); }
  public static void addIsDamageImmunity(FlatBufferBuilder builder, boolean isDamageImmunity) { builder.addBoolean(39, isDamageImmunity, false); }
  public static void addIsUnTargetable(FlatBufferBuilder builder, boolean isUnTargetable) { builder.addBoolean(40, isUnTargetable, false); }
  public static void addMoveSpeedRate(FlatBufferBuilder builder, float moveSpeedRate) { builder.addFloat(41, moveSpeedRate, 0.0f); }
  public static void addAttackSpeedRate(FlatBufferBuilder builder, float attackSpeedRate) { builder.addFloat(42, attackSpeedRate, 0.0f); }
  public static void addHpRecoveryRate(FlatBufferBuilder builder, float hpRecoveryRate) { builder.addFloat(43, hpRecoveryRate, 0.0f); }
  public static void addMpRecoveryRate(FlatBufferBuilder builder, float mpRecoveryRate) { builder.addFloat(44, mpRecoveryRate, 0.0f); }
  public static void addGoldGainRate(FlatBufferBuilder builder, float goldGainRate) { builder.addFloat(45, goldGainRate, 0.0f); }
  public static void addExpGainRate(FlatBufferBuilder builder, float expGainRate) { builder.addFloat(46, expGainRate, 0.0f); }
  public static void addBuffDurationRate(FlatBufferBuilder builder, float buffDurationRate) { builder.addFloat(47, buffDurationRate, 0.0f); }
  public static void addAttackDamageRate(FlatBufferBuilder builder, float attackDamageRate) { builder.addFloat(48, attackDamageRate, 0.0f); }
  public static void addDefenseRate(FlatBufferBuilder builder, float defenseRate) { builder.addFloat(49, defenseRate, 0.0f); }
  public static void addMaxHPRate(FlatBufferBuilder builder, float maxHPRate) { builder.addFloat(50, maxHPRate, 0.0f); }
  public static void addMaxMPRate(FlatBufferBuilder builder, float maxMPRate) { builder.addFloat(51, maxMPRate, 0.0f); }
  public static void addCoolTimeReduceRate(FlatBufferBuilder builder, float coolTimeReduceRate) { builder.addFloat(52, coolTimeReduceRate, 0.0f); }
  public static void addMoveSpeedBonus(FlatBufferBuilder builder, float moveSpeedBonus) { builder.addFloat(53, moveSpeedBonus, 0.0f); }
  public static void addAttackDamageBonus(FlatBufferBuilder builder, float attackDamageBonus) { builder.addFloat(54, attackDamageBonus, 0.0f); }
  public static void addDefenseBonus(FlatBufferBuilder builder, float defenseBonus) { builder.addFloat(55, defenseBonus, 0.0f); }
  public static void addMaxHPBonus(FlatBufferBuilder builder, float maxHPBonus) { builder.addFloat(56, maxHPBonus, 0.0f); }
  public static void addMaxMPBonus(FlatBufferBuilder builder, float maxMPBonus) { builder.addFloat(57, maxMPBonus, 0.0f); }
  public static void addBloodSuckingRate(FlatBufferBuilder builder, float bloodSuckingRate) { builder.addFloat(58, bloodSuckingRate, 0.0f); }
  public static void addCriticalChanceRate(FlatBufferBuilder builder, float criticalChanceRate) { builder.addFloat(59, criticalChanceRate, 0.0f); }
  public static void addCriticalDamageRate(FlatBufferBuilder builder, float criticalDamageRate) { builder.addFloat(60, criticalDamageRate, 0.0f); }
  public static void addIsAirborneImmunity(FlatBufferBuilder builder, boolean isAirborneImmunity) { builder.addBoolean(61, isAirborneImmunity, false); }
  public static void addIsAirborne(FlatBufferBuilder builder, boolean isAirborne) { builder.addBoolean(62, isAirborne, false); }
  public static void addIsTargetingInvincible(FlatBufferBuilder builder, boolean isTargetingInvincible) { builder.addBoolean(63, isTargetingInvincible, false); }
  public static void addIsStunned(FlatBufferBuilder builder, boolean isStunned) { builder.addBoolean(64, isStunned, false); }
  public static void addIsFreezing(FlatBufferBuilder builder, boolean isFreezing) { builder.addBoolean(65, isFreezing, false); }
  public static void addIsSlow(FlatBufferBuilder builder, boolean isSlow) { builder.addBoolean(66, isSlow, false); }
  public static void addIsSilence(FlatBufferBuilder builder, boolean isSilence) { builder.addBoolean(67, isSilence, false); }
  public static void addIsBlind(FlatBufferBuilder builder, boolean isBlind) { builder.addBoolean(68, isBlind, false); }
  public static void addIsSightBlocked(FlatBufferBuilder builder, boolean isSightBlocked) { builder.addBoolean(69, isSightBlocked, false); }
  public static void addIsGrounding(FlatBufferBuilder builder, boolean isGrounding) { builder.addBoolean(70, isGrounding, false); }
  public static void addIsPolymorph(FlatBufferBuilder builder, boolean isPolymorph) { builder.addBoolean(71, isPolymorph, false); }
  public static void addIsDisarmed(FlatBufferBuilder builder, boolean isDisarmed) { builder.addBoolean(72, isDisarmed, false); }
  public static void addIsSnare(FlatBufferBuilder builder, boolean isSnare) { builder.addBoolean(73, isSnare, false); }
  public static void addIsKnockedAirborne(FlatBufferBuilder builder, boolean isKnockedAirborne) { builder.addBoolean(74, isKnockedAirborne, false); }
  public static void addIsKnockback(FlatBufferBuilder builder, boolean isKnockback) { builder.addBoolean(75, isKnockback, false); }
  public static void addIsSuspension(FlatBufferBuilder builder, boolean isSuspension) { builder.addBoolean(76, isSuspension, false); }
  public static void addIsTaunt(FlatBufferBuilder builder, boolean isTaunt) { builder.addBoolean(77, isTaunt, false); }
  public static void addIsCharm(FlatBufferBuilder builder, boolean isCharm) { builder.addBoolean(78, isCharm, false); }
  public static void addIsFlee(FlatBufferBuilder builder, boolean isFlee) { builder.addBoolean(79, isFlee, false); }
  public static void addIsSuppressed(FlatBufferBuilder builder, boolean isSuppressed) { builder.addBoolean(80, isSuppressed, false); }
  public static void addIsSleep(FlatBufferBuilder builder, boolean isSleep) { builder.addBoolean(81, isSleep, false); }
  public static int endflat_CharacterData(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
    public static int createflat_CharacterData(FlatBufferBuilder builder,
 CharacterData data) {
        int characterNameOffset = builder.createString(data.characterName);
        int size8 = data.skillSlot.size();
        int[] skillSlot_ = new int[size8];
        for (int x = 0; x < size8; x++) {
        SkillSlotData aa = data.skillSlot.poll();
        skillSlot_[x] = flat_SkillSlotData.createflat_SkillSlotData(builder, aa);
        }
        int skillSlotOffset = flat_CharacterData.createSkillSlotVector(builder, skillSlot_);
        int size9 = data.itemSlot.size();
        int[] itemSlot_ = new int[size9];
        for (int x = 0; x < size9; x++) {
        ItemSlotData aa = data.itemSlot.poll();
        itemSlot_[x] = flat_ItemSlotData.createflat_ItemSlotData(builder, aa);
        }
        int itemSlotOffset = flat_CharacterData.createItemSlotVector(builder, itemSlot_);
        int size10 = data.conditionList.size();
        int[] conditionList_ = new int[size10];
        for (int x = 0; x < size10; x++) {
        ConditionData aa = data.conditionList.poll();
        conditionList_[x] = flat_ConditionData.createflat_ConditionData(builder, aa);
        }
        int conditionListOffset = flat_CharacterData.createConditionListVector(builder, conditionList_);
        return createflat_CharacterData(builder , data.entityID, data.team, data.characterType, characterNameOffset, data.characterElemental, data.kill, data.death, data.moveSpeed, skillSlotOffset, itemSlotOffset, conditionListOffset, data.level, data.exp, data.gold, data.currentHP, data.maxHP, data.recoveryRateHP, data.shieldAmount, data.currentMP, data.maxMP, data.recoveryRateMP, data.attackDamage, data.attackSpeed, data.attackRange, data.criticalChance, data.criticalDamage, data.defense, data.posX, data.posY, data.posZ, data.velX, data.velY, data.velZ, data.quarternionY, data.quarternionZ, data.isDisableMove, data.isDisableAttack, data.isDisableSkill, data.isDisableItem, data.isDamageImmunity, data.isUnTargetable, data.moveSpeedRate, data.attackSpeedRate, data.hpRecoveryRate, data.mpRecoveryRate, data.goldGainRate, data.expGainRate, data.buffDurationRate, data.attackDamageRate, data.defenseRate, data.maxHPRate, data.maxMPRate, data.coolTimeReduceRate, data.moveSpeedBonus, data.attackDamageBonus, data.defenseBonus, data.maxHPBonus, data.maxMPBonus, data.bloodSuckingRate, data.criticalChanceRate, data.criticalDamageRate, data.isAirborneImmunity, data.isAirborne, data.isTargetingInvincible, data.isStunned, data.isFreezing, data.isSlow, data.isSilence, data.isBlind, data.isSightBlocked, data.isGrounding, data.isPolymorph, data.isDisarmed, data.isSnare, data.isKnockedAirborne, data.isKnockback, data.isSuspension, data.isTaunt, data.isCharm, data.isFlee, data.isSuppressed, data.isSleep);
    }

    public static byte[] createflat_CharacterData(CharacterData data) {
        FlatBufferBuilder fbb = new FlatBufferBuilder();
        fbb.finish(flat_CharacterData.createflat_CharacterData(fbb, data));
        byte[] result = fbb.sizedByteArray();
        fbb = null;
        return result;
    }

    public static CharacterData getRootAsflat_CharacterData(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        CharacterData result = new CharacterData(flat_CharacterData.getRootAsflat_CharacterData( buf ) );
        buf = null;
        return result;
    }

}