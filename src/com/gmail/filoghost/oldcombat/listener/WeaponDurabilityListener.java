package com.gmail.filoghost.oldcombat.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.oldcombat.DamageChanceCalculator;
import com.gmail.filoghost.oldcombat.OldCombat;
import com.gmail.filoghost.oldcombat.ValueModifier;
import com.google.common.collect.Lists;

public class WeaponDurabilityListener implements Listener {
	
	
	private int currentTick;
	private List<Player> thisTickMeleeAttackers;
	private List<Player> thisTickDefenders;
	

	public WeaponDurabilityListener() {
		thisTickMeleeAttackers = Lists.newArrayList();
		thisTickDefenders = Lists.newArrayList();
		
		Bukkit.getScheduler().runTaskTimer(OldCombat.instance, () -> {
			thisTickMeleeAttackers.clear();
			thisTickDefenders.clear();
			currentTick++;
		}, 1, 1);
	}
	
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCombat(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			// Player difensore
			thisTickDefenders.add((Player) event.getEntity());
		} else if (event.getDamager() instanceof Player) {
			// Player attaccante corpo a corpo
			thisTickMeleeAttackers.add((Player) event.getDamager());
		}
	}
	
	
	/*
	 * Per il tracking di attaccanti a distanza non si può tracciare ProjectileLaunchEvent
	 * perché avviene dopo ItemDamageEvent.
	 */	
	
	
	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onItemDamage(PlayerItemDamageEvent event) {
		if (isArmor(event.getItem())) {
			if (thisTickDefenders.contains(event.getPlayer())) {
				// In combattimento
				if (event.getDamage() > 1) {
					event.setDamage(1);
				}
			} else {
				// Fuori dal combattimento
				if (event.getDamage() > 1) {
					event.setDamage(1);
				}
			}
//			//modifyItemDamage(event, this::getArmorDamageChance);
			
		} else if (isMeleeWeapon(event.getItem()) && thisTickMeleeAttackers.contains(event.getPlayer())) {
			modifyItemDamage(event, this::getMeleeWeaponDamageChance);
			
		} else if (isRangedWeapon(event.getItem())) {
			modifyItemDamage(event, this::getRangedWeaponDamageChance);
		}
	}
	
	
	private void modifyItemDamage(PlayerItemDamageEvent event, DamageChanceCalculator damageChanceCalculator) {
		int durabilityLevel = event.getItem().getEnchantmentLevel(Enchantment.DURABILITY);
		
		if (durabilityLevel <= 0) {
			event.setDamage(getDurabilityToLoseForCombat(event.getItem()));
			return;
		}
			
		double damageChance = damageChanceCalculator.getDamageChance(durabilityLevel);
		
		if (Math.random() < damageChance) {
			event.setDamage(getDurabilityToLoseForCombat(event.getItem()));
		} else {
			event.setDamage(0);
		}
	}


//	private double getArmorDamageChance(int durabilityLevel) {
//		switch (durabilityLevel) {
//			case 1:
//				return (60 + (40.0 / (2))) / 100.0;
//			case 2:
//				return (60 + (40.0 / (3))) / 100.0;
//			case 3:
//				return (60 + (40.0 / (4))) / 100.0;
//			default:
//				return (60 + (40.0 / (durabilityLevel + 1))) / 100.0;
//		}
//	}
	
	
	/**
	 * Valori normali, con ogni livello l'arma dura {LIVELLO} volte in più, cioè {1 + LIVELLO} volte complessivamente.
	 * Durabilità 1 = Dura 2 volte
	 * Durabilità 2 = Dura 3 volte
	 * Durabilità 3 = Dura 4 volte
	 * 
	 * Valori modificati, con ogni livello l'arma dura {LIVELLO * MOD} volte in più, cioè {1 + (LIVELLO * MOD)} volte complessivamente.
	 * Con MOD = 0.75:
	 * Durabilità 1 = Dura 1.75 volte
	 * Durabilità 2 = Dura 2.50 volte
	 * Durabilità 3 = Dura 3.25 volte
	 * (L'incremento è di 0.75 invece che di 1)
	 */
	private double getMeleeWeaponDamageChance(int durabilityLevel) {
		double timesLastingComparedToNormal = 1 + (durabilityLevel * ValueModifier.DURABILITY_MELEE_WEAPONS);
		return 1 / timesLastingComparedToNormal;
	}
	
	private double getRangedWeaponDamageChance(int durabilityLevel) {
		double timesLastingComparedToNormal = 1 + (durabilityLevel * ValueModifier.DURABILITY_RANGED_WEAPONS);
		return 1 / timesLastingComparedToNormal;
	}
	
	
	private int getDurabilityToLoseForCombat(ItemStack item) {
		switch (item.getType()) {
			case DIAMOND_AXE:
			case IRON_AXE:
			case GOLD_AXE:
			case STONE_AXE:
			case WOOD_AXE:
				
			case DIAMOND_PICKAXE:
			case IRON_PICKAXE:
			case GOLD_PICKAXE:
			case STONE_PICKAXE:
			case WOOD_PICKAXE:
				
			case DIAMOND_SPADE:
			case IRON_SPADE:
			case GOLD_SPADE:
			case STONE_SPADE:
			case WOOD_SPADE:
				return 2;
			default:
				return 1;
		}
	}


	private boolean isArmor(ItemStack item) {
		switch (item.getType()) {
			case LEATHER_HELMET:
			case CHAINMAIL_HELMET:
			case IRON_HELMET:
			case GOLD_HELMET:
			case DIAMOND_HELMET:
			case LEATHER_CHESTPLATE:
			case CHAINMAIL_CHESTPLATE:
			case IRON_CHESTPLATE:
			case GOLD_CHESTPLATE:
			case DIAMOND_CHESTPLATE:
			case LEATHER_LEGGINGS:
			case CHAINMAIL_LEGGINGS:
			case IRON_LEGGINGS:
			case GOLD_LEGGINGS:
			case DIAMOND_LEGGINGS:
			case LEATHER_BOOTS:
			case CHAINMAIL_BOOTS:
			case IRON_BOOTS:
			case GOLD_BOOTS:
			case DIAMOND_BOOTS:
			case SHIELD:
				return true;
			default:
				return false;
		}
	}
	
	
	private boolean isMeleeWeapon(ItemStack item) {
		switch (item.getType()) {
			case DIAMOND_SWORD:
			case IRON_SWORD:
			case GOLD_SWORD:
			case STONE_SWORD:
			case WOOD_SWORD:
				
			case DIAMOND_AXE:
			case IRON_AXE:
			case GOLD_AXE:
			case STONE_AXE:
			case WOOD_AXE:
				
			case DIAMOND_PICKAXE:
			case IRON_PICKAXE:
			case GOLD_PICKAXE:
			case STONE_PICKAXE:
			case WOOD_PICKAXE:
				
			case DIAMOND_SPADE:
			case IRON_SPADE:
			case GOLD_SPADE:
			case STONE_SPADE:
			case WOOD_SPADE:
				
			case DIAMOND_HOE:
			case IRON_HOE:
			case GOLD_HOE:
			case STONE_HOE:
			case WOOD_HOE:
				return true;
			default:
				return false;
		}
	}
	
	private boolean isRangedWeapon(ItemStack item) {
		switch (item.getType()) {
			case BOW:
				return true;
			default:
				return false;
		}
	}


}
