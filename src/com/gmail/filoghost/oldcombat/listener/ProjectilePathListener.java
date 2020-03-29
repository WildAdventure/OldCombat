package com.gmail.filoghost.oldcombat.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectilePathListener implements Listener {
	
	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile projectile = event.getEntity();
		if (projectile.getShooter() instanceof Player) {
			Player shooter = (Player) projectile.getShooter();
			
			double oldSpeed = projectile.getVelocity().length();
			projectile.setVelocity(shooter.getLocation().getDirection().normalize().multiply(oldSpeed));
		}
	}

}
