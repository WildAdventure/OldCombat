package com.gmail.filoghost.oldcombat.nms.v1_9_R2;

import com.gmail.filoghost.oldcombat.ValueModifier;

import net.minecraft.server.v1_9_R2.DamageSource;
import net.minecraft.server.v1_9_R2.EnchantmentProtection;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.MathHelper;

public class OldEnchantmentProtection extends EnchantmentProtection {

	public OldEnchantmentProtection(Rarity arg0, DamageType arg1, EnumItemSlot[] arg2) {
		super(arg0, arg1, arg2);
	}

	@Override
	public int a(final int level, final DamageSource damageSource) {
		if (damageSource.ignoresInvulnerability()) {
			return 0;
		}
		
		float protectionFactor = (6 + level * level) / 3.0f;

		if (a == DamageType.ALL) {
			 return MathHelper.d(protectionFactor * 0.75f * ValueModifier.PROTECTION_ALL);
		}
		if ((a == DamageType.FIRE) && (damageSource.o())) {
			 return MathHelper.d(protectionFactor * 1.25f * ValueModifier.PROTECTION_FIRE);
		}
		if ((a == DamageType.FALL) && (damageSource == DamageSource.FALL)) {
			 return MathHelper.d(protectionFactor * 2.5f * ValueModifier.PROTECTION_FALL);
		}
		if ((a == DamageType.EXPLOSION) && (damageSource.isExplosion())) {
			 return MathHelper.d(protectionFactor * 1.5f * ValueModifier.PROTECTION_EXPLOSION);
		}
		if ((a == DamageType.PROJECTILE) && (damageSource.a())) {
			 return MathHelper.d(protectionFactor * 1.5f * ValueModifier.PROTECTION_PROJECTILE);
		}
		
		return 0;
	}

}
