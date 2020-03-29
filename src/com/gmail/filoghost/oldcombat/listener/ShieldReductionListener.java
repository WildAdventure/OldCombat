package com.gmail.filoghost.oldcombat.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

@SuppressWarnings("deprecation")
public class ShieldReductionListener implements Listener {

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onCombat(EntityDamageByEntityEvent event) {
		double baseDamage = event.getOriginalDamage(DamageModifier.BASE);
		double blockingDamageReduction = event.getOriginalDamage(DamageModifier.BLOCKING); // Attenzione, è negativo
		
		if (blockingDamageReduction < 0) {
			blockingDamageReduction = - ((baseDamage - 1.0) / 2.0);
			event.setDamage(DamageModifier.BLOCKING, blockingDamageReduction);
		}
	}

}
