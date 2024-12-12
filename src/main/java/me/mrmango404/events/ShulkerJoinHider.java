package me.mrmango404.events;

import me.mrmango404.Main;
import me.mrmango404.util.CheckActive;
import me.mrmango404.util.HideShulker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.BoundingBox;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class ShulkerJoinHider implements Listener {

	Main mainClass = Main.getMain();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		if (!CheckActive.isServerActive()) {
			return;
		}

		BoundingBox boundingBox;
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		World world = player.getWorld();
		int distance = mainClass.getServer().getViewDistance() * 16;

		/*
		Check if the joining player is near the range of an active viewing section,
		If so, hide the shulkers
		 */
		for (Map.Entry<UUID, Location> locationSet : Main.globalActiveViewSessionLocations.entrySet()) {
			Location viewSectionLocation = locationSet.getValue();
			int playerLocationTopX = viewSectionLocation.getBlockX() + distance;
			int playerLocationTopY = viewSectionLocation.getBlockY() + distance;
			int playerLocationTopZ = viewSectionLocation.getBlockZ() + distance;
			int playerLocationBottomX = viewSectionLocation.getBlockX() - distance;
			int playerLocationBottomY = viewSectionLocation.getBlockY() - distance;
			int playerLocationBottomZ = viewSectionLocation.getBlockZ() - distance;
			boundingBox = new BoundingBox(
					playerLocationTopX,
					playerLocationTopY,
					playerLocationTopZ,
					playerLocationBottomX,
					playerLocationBottomY,
					playerLocationBottomZ);
			Predicate<Entity> entityPredicate = entity -> entity instanceof Player;
			Collection<Entity> players = world.getNearbyEntities(boundingBox, entityPredicate);

			for (Entity entity : players) {
				if (entity.getUniqueId().equals(playerUUID)) {
					UUID viewSectionOwnerUUID = locationSet.getKey();

					// Prevent the player from hiding their own shulker.
					if (viewSectionOwnerUUID.equals(playerUUID)) {
						continue;
					}

					for (Entity entitySingle : Main.globalSingleShulkers.get(viewSectionOwnerUUID)) {
						new HideShulker(player, entitySingle);
					}

					for (Map.Entry<LivingEntity, LivingEntity> entityDoubleSet : Main.globalDoubleShulkers.get(viewSectionOwnerUUID).entrySet()) {
						new HideShulker(player, entityDoubleSet.getKey());
						new HideShulker(player, entityDoubleSet.getValue());
					}
				}
			}
		}
	}
}
