package com.gmail.filoghost.oldcombat.nms.v1_12_R1;

import com.gmail.filoghost.oldcombat.ValueModifier;

import net.minecraft.server.v1_12_R1.EnchantmentWeaponDamage;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.EnumMonsterType;

public class OldEnchantmentWeaponDamage extends EnchantmentWeaponDamage {


	public OldEnchantmentWeaponDamage(Rarity arg0, int arg1, EnumItemSlot[] arg2) {
		super(arg0, arg1, arg2);
	}

	@Override
    public float a(final int level, final EnumMonsterType monsterType) {
        if (this.a == 0) {
        	return level * 1.25f * ValueModifier.DAMAGE_ALL;
        }
        if (this.a == 1 && monsterType == EnumMonsterType.UNDEAD) {
            return level * 2.5f * ValueModifier.DAMAGE_UNDEAD;
        }
        if (this.a == 2 && monsterType == EnumMonsterType.ARTHROPOD) {
            return level * 2.5f * ValueModifier.DAMAGE_ARTHROPODS;
        }
        return 0.0f;
    }

}
