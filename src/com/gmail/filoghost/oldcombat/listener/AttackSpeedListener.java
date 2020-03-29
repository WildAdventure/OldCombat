package com.gmail.filoghost.oldcombat.listener;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AttackSpeedListener implements Listener {
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
	}

}
