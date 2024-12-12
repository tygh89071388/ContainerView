package me.mrmango404.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class LootCanceller implements Listener {

	@EventHandler
	public void onShulkerDeath(EntityDeathEvent event) {

		Entity eventEntity = event.getEntity();
		if (eventEntity.isGlowing() && eventEntity.getType() == EntityType.SHULKER) {
			event.getDrops().clear();
			event.setDroppedExp(0);
		}
	}
}
