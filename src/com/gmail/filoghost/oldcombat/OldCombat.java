package com.gmail.filoghost.oldcombat;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.oldcombat.listener.AttackSpeedListener;
import com.gmail.filoghost.oldcombat.listener.MendingListener;
import com.gmail.filoghost.oldcombat.listener.WeaponDurabilityListener;
import com.gmail.filoghost.oldcombat.listener.ProjectilePathListener;
import com.gmail.filoghost.oldcombat.listener.ShieldReductionListener;
import com.gmail.filoghost.oldcombat.nms.NmsManager;

import wild.api.WildCommons;

public class OldCombat extends JavaPlugin {
	
	public static OldCombat instance;

	@Override
	public void onEnable() {
		instance = this;
		
		restoreEnchantments();
		restoreToolDamage();
		restoreFishingRodDamage();
		restoreArmorToughness();		
		
		Bukkit.getPluginManager().registerEvents(new AttackSpeedListener(), this);
		Bukkit.getPluginManager().registerEvents(new WeaponDurabilityListener(), this);
		Bukkit.getPluginManager().registerEvents(new ProjectilePathListener(), this);
		Bukkit.getPluginManager().registerEvents(new ShieldReductionListener(), this);
		
		if (!WildCommons.getBukkitVersion().equals("v1_9_R2")) {
			Bukkit.getPluginManager().registerEvents(new MendingListener(), this);
		}
	}


	private void restoreEnchantments() {
		String version = WildCommons.getBukkitVersion();
		NmsManager nmsManager;
		
		if ("v1_9_R2".equals(version)) {
			nmsManager = new com.gmail.filoghost.oldcombat.nms.v1_9_R2.NmsManagerImpl();
		} else if ("v1_12_R1".equals(version)) {
			nmsManager = new com.gmail.filoghost.oldcombat.nms.v1_12_R1.NmsManagerImpl();
		} else {
			System.out.println(
				" \n "
			   + "\n  ######################## ATTENZIONE ########################"
			   + "\n  #                                                          #"
			   + "\n  #  OldCombat non ha potuto ripristinare gli incantesimi    #"
			   + "\n  #  per questa versione.                                    #"
			   + "\n  #                                                          #"
			   + "\n  ############################################################"
			   + "\n "
			   + "\n Versione: " + version
			   + "\n "
			);
			
			WildCommons.pauseThread(3000);
			return;
		}
		
		try {
			nmsManager.modifyEnchantments();
		} catch (Throwable t) {
			getLogger().log(Level.SEVERE, "Cannot modify enchantments", t);
		}
	}


	private void restoreToolDamage() {
		try {
			WildCommons.Unsafe.setToolBaseDamage(Material.WOOD_AXE, 		2);
			WildCommons.Unsafe.setToolBaseDamage(Material.GOLD_AXE, 		2);
			WildCommons.Unsafe.setToolBaseDamage(Material.STONE_AXE, 		3);
			WildCommons.Unsafe.setToolBaseDamage(Material.IRON_AXE, 		4);
			WildCommons.Unsafe.setToolBaseDamage(Material.DIAMOND_AXE, 		5);
			
			WildCommons.Unsafe.setToolBaseDamage(Material.WOOD_PICKAXE, 	1);
			WildCommons.Unsafe.setToolBaseDamage(Material.GOLD_PICKAXE, 	1);
			WildCommons.Unsafe.setToolBaseDamage(Material.STONE_PICKAXE, 	2);
			WildCommons.Unsafe.setToolBaseDamage(Material.IRON_PICKAXE, 	3);
			WildCommons.Unsafe.setToolBaseDamage(Material.DIAMOND_PICKAXE, 	4);
			
			WildCommons.Unsafe.setToolBaseDamage(Material.WOOD_SPADE, 		0);
			WildCommons.Unsafe.setToolBaseDamage(Material.GOLD_SPADE, 		0);
			WildCommons.Unsafe.setToolBaseDamage(Material.STONE_SPADE, 		1);
			WildCommons.Unsafe.setToolBaseDamage(Material.IRON_SPADE, 		2);
			WildCommons.Unsafe.setToolBaseDamage(Material.DIAMOND_SPADE,	3);
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Cannot change base tool damage", e);
		}
	}
	
	
	private void restoreFishingRodDamage() {
		try {
			WildCommons.Unsafe.restoreFishingRodDamage();
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Cannot restore fishing rod damage", e);
		}
	}
	
	
	private void restoreArmorToughness() {
		try {
			WildCommons.Unsafe.setDefaultArmorToughness("LEATHER", 	99999999999999.);
			WildCommons.Unsafe.setDefaultArmorToughness("CHAIN", 	99999999999999.);
			WildCommons.Unsafe.setDefaultArmorToughness("IRON", 	99999999999999.);
			WildCommons.Unsafe.setDefaultArmorToughness("GOLD", 	99999999999999.);
			WildCommons.Unsafe.setDefaultArmorToughness("DIAMOND", 	99999999999999.);
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Cannot restore armor toughness", e);
		}
	}

}
