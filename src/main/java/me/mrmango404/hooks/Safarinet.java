package me.mrmango404.hooks;

import de.Linus122.SafariNet.API.Listener;
import de.Linus122.SafariNet.API.Status;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Safarinet implements Listener {

	@Override
	public void playerCatchEntity(Player player, Entity entity, Status status) {
		if (entity.isGlowing() && entity.getType() == EntityType.SHULKER) {
			status.setCancelled(true);
		}
	}

	@Override
	public void playerReleaseEntity(Player p, Entity e, Status s) {
		// do something
	}
}
