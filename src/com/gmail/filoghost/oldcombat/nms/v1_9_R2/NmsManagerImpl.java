package com.gmail.filoghost.oldcombat.nms.v1_9_R2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.bukkit.craftbukkit.v1_9_R2.enchantments.CraftEnchantment;

import com.gmail.filoghost.oldcombat.nms.NmsManager;

import net.minecraft.server.v1_9_R2.Enchantment;
import net.minecraft.server.v1_9_R2.Enchantment.Rarity;
import net.minecraft.server.v1_9_R2.EnchantmentProtection;
import net.minecraft.server.v1_9_R2.Enchantments;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.MinecraftKey;
import net.minecraft.server.v1_9_R2.RegistryID;
import net.minecraft.server.v1_9_R2.RegistryMaterials;
import net.minecraft.server.v1_9_R2.RegistrySimple;
import wild.core.utils.ReflectionUtils;

public class NmsManagerImpl implements NmsManager {
	
	public void modifyEnchantments() throws Throwable {
		EnumItemSlot[] allItemSlots = { EnumItemSlot.HEAD, EnumItemSlot.CHEST, EnumItemSlot.LEGS, EnumItemSlot.FEET };

		replaceEnchantment("PROTECTION_ENVIRONMENTAL", 0, new MinecraftKey("protection"), new OldEnchantmentProtection(Rarity.COMMON, EnchantmentProtection.DamageType.ALL, allItemSlots));
		replaceEnchantment("PROTECTION_FIRE", 1, new MinecraftKey("fire_protection"), new OldEnchantmentProtection(Rarity.UNCOMMON, EnchantmentProtection.DamageType.FIRE, allItemSlots));
		replaceEnchantment("PROTECTION_FALL", 2, new MinecraftKey("feather_falling"), new OldEnchantmentProtection(Rarity.UNCOMMON, EnchantmentProtection.DamageType.FALL, allItemSlots));
		replaceEnchantment("PROTECTION_EXPLOSIONS", 3, new MinecraftKey("blast_protection"), new OldEnchantmentProtection(Rarity.RARE, EnchantmentProtection.DamageType.EXPLOSION, allItemSlots));
		replaceEnchantment("PROTECTION_PROJECTILE", 4, new MinecraftKey("projectile_protection"), new OldEnchantmentProtection(Rarity.UNCOMMON, EnchantmentProtection.DamageType.PROJECTILE, allItemSlots));
		
		replaceEnchantment("DAMAGE_ALL", 16, new MinecraftKey("sharpness"), new OldEnchantmentWeaponDamage(Rarity.COMMON, 0, new EnumItemSlot[] { EnumItemSlot.MAINHAND }));
		replaceEnchantment("DAMAGE_UNDEAD", 17, new MinecraftKey("smite"), new OldEnchantmentWeaponDamage(Rarity.UNCOMMON, 1, new EnumItemSlot[] { EnumItemSlot.MAINHAND }));
		replaceEnchantment("DAMAGE_ARTHROPODS", 18, new MinecraftKey("bane_of_arthropods"), new OldEnchantmentWeaponDamage(Rarity.UNCOMMON, 2, new EnumItemSlot[] { EnumItemSlot.MAINHAND }));
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private void replaceEnchantment(String constantName, int id, MinecraftKey key, Enchantment enchantment) throws Exception {
		RegistryMaterials<MinecraftKey, Enchantment> enchantsRegistry = Enchantment.enchantments;

		// Lato registri
		Map<MinecraftKey, Enchantment> map = (Map<MinecraftKey, Enchantment>) ReflectionUtils.getPrivateField(RegistrySimple.class, enchantsRegistry, "c");
		map.put(key, enchantment);

		RegistryID<Enchantment> registry = (RegistryID<Enchantment>) ReflectionUtils.getPrivateField(RegistryMaterials.class, enchantsRegistry, "a");
		registry.a(enchantment, id);
		
		// Lato costanti
		Field constantEnchantmentField = Enchantments.class.getDeclaredField(constantName);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(constantEnchantmentField, constantEnchantmentField.getModifiers() & ~Modifier.FINAL);
		constantEnchantmentField.set(null, enchantment);

		// Lato Bukkit
		CraftEnchantment craftEnchantment = new CraftEnchantment(enchantment);

		Map<Integer, org.bukkit.enchantments.Enchantment> byId = (Map<Integer, org.bukkit.enchantments.Enchantment>) ReflectionUtils.getPrivateField(org.bukkit.enchantments.Enchantment.class, null, "byId");
		byId.put(craftEnchantment.getId(), craftEnchantment);

		Map<String, org.bukkit.enchantments.Enchantment> byName = (Map<String, org.bukkit.enchantments.Enchantment>) ReflectionUtils.getPrivateField(org.bukkit.enchantments.Enchantment.class, null, "byName");
		byName.put(craftEnchantment.getName(), craftEnchantment);
	}
}
