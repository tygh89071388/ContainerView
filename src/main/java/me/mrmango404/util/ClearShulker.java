package me.mrmango404.util;

import me.mrmango404.Main;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClearShulker {

	public ClearShulker(Player player) {
		clear(player);
	}

	/**
	 * Clear all shulkers for a player.
	 */
	public void clear(Player player) {

		UUID playerUUID = player.getUniqueId();

		if (!CheckActive.isPlayerActive(player)) {
			return;
		}

		cancelScheduler(player);

		for (Team team : Main.globalTeamList.get(playerUUID)) {
			try {
				team.unregister();
			} catch (Exception e) {
				System.out.println("error...");
			}
		}

		for (LivingEntity shulker : Main.globalSingleShulkers.get(playerUUID)) {
			shulker.remove();
		}

		for (Map.Entry<LivingEntity, LivingEntity> set : Main.globalDoubleShulkers.get(playerUUID).entrySet()) {
			set.getKey().remove();
			set.getValue().remove();
		}

		Main.globalTeamList.remove(playerUUID);
		Main.globalSingleShulkers.remove(playerUUID);
		Main.globalDoubleShulkers.remove(playerUUID);
		Main.globalActiveViewSessionLocations.remove(playerUUID);

		MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.DISABLED);
	}


	/**
	 * Clear all shulkers on the server.
	 */
	public static void clear() {

		if (!CheckActive.isServerActive()) {
			return;
		}

		for (ArrayList<LivingEntity> entityArrayList : Main.globalSingleShulkers.values()) {
			for (LivingEntity entity : entityArrayList) {
				entity.remove();
			}
		}

		for (HashMap<LivingEntity, LivingEntity> entityHashMap : Main.globalDoubleShulkers.values()) {
			for (Map.Entry<LivingEntity, LivingEntity> entitySet : entityHashMap.entrySet()) {
				entitySet.getValue().remove();
				entitySet.getKey().remove();
			}
		}
	}

	/**
	 * Clear all the bukkit schedulers.
	 */
	private void cancelScheduler(Player player) {

		UUID playerUUID = player.getUniqueId();
		HashMap<UUID, BukkitTask> taskList = Main.globalSchedulers;

		if (!taskList.containsKey(playerUUID)) {
			return;
		}

		taskList.get(playerUUID).cancel();
		taskList.remove(playerUUID);
	}
}
