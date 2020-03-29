package com.gmail.filoghost.oldcombat.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;

public class MendingListener implements Listener {
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onMending(PlayerItemMendEvent event) {
		int newRepairAmount = event.getRepairAmount() / 2;
		if (event.getRepairAmount() % 2 != 0) {
			newRepairAmount++;
		}
		event.setRepairAmount(newRepairAmount);
	}

}