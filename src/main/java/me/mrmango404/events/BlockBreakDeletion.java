package me.mrmango404.events;

import me.mrmango404.Main;
import me.mrmango404.util.CheckActive;
import me.mrmango404.util.ClearShulker;
import me.mrmango404.util.DebugManager;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockBreakDeletion implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {

		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		Location blockLocation = event.getBlock().getLocation();

		if (!CheckActive.isPlayerActive(player)) {
			return;
		}

		ArrayList<LivingEntity> singleShulkersToRemove = new ArrayList<>();
		HashMap<LivingEntity, LivingEntity> doubleShulkersToRemove = new HashMap<>();
		ArrayList<LivingEntity> globalSingleShulkers = Main.globalSingleShulkers.get(playerUUID);
		HashMap<LivingEntity, LivingEntity> globalDoubleShulkers = Main.globalDoubleShulkers.get(playerUUID);

		for (LivingEntity singleShulker : globalSingleShulkers) {
			Location entityLocation = singleShulker.getLocation();
			entityLocation.setX(Math.floor(entityLocation.getX()));
			entityLocation.setZ(Math.floor(entityLocation.getZ()));

			if (blockLocation.equals(entityLocation)) {
				singleShulker.remove();
				singleShulkersToRemove.add(singleShulker);
			}

			DebugManager.log("Block location" + blockLocation);
			DebugManager.log("Entity location" + singleShulker.getLocation());
			DebugManager.log("Entity location (modified)\n" + entityLocation);
		}

		for (Map.Entry<LivingEntity, LivingEntity> doubleShulkerSet : globalDoubleShulkers.entrySet()) {
			LivingEntity shulkerLeft = doubleShulkerSet.getKey();
			LivingEntity shulkerRight = doubleShulkerSet.getValue();
			Location shulkerLeftLocation = shulkerLeft.getLocation();
			Location shulkerRightLocation = shulkerRight.getLocation();
			shulkerLeftLocation.setX(Math.floor(shulkerLeftLocation.getX()));
			shulkerLeftLocation.setZ(Math.floor(shulkerLeftLocation.getZ()));
			shulkerRightLocation.setX(Math.floor(shulkerRightLocation.getX()));
			shulkerRightLocation.setZ(Math.floor(shulkerRightLocation.getZ()));

			if (blockLocation.equals(shulkerLeftLocation) || blockLocation.equals(shulkerRightLocation)) {
				shulkerLeft.remove();
				shulkerRight.remove();
				doubleShulkersToRemove.put(shulkerLeft, shulkerRight);
			}
		}

		if (!singleShulkersToRemove.isEmpty()) {
			globalSingleShulkers.removeAll(singleShulkersToRemove);
		}

		if (!doubleShulkersToRemove.isEmpty()) {
			globalDoubleShulkers.keySet().removeAll(doubleShulkersToRemove.keySet());
		}

		if (globalSingleShulkers.isEmpty() && globalDoubleShulkers.isEmpty()) {
			new ClearShulker(player);
		}
	}
}


