package me.mrmango404.util;

import me.mrmango404.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class CancelScheduler {

	public CancelScheduler(Player player) {
		cancel(player);
	}

	private void cancel(Player player) {

		UUID playerUUID = player.getUniqueId();
		HashMap<UUID, BukkitTask> taskList = Main.globalSchedulers;

		if (!taskList.containsKey(playerUUID)) {
			return;
		}

		taskList.get(playerUUID).cancel();
		taskList.remove(playerUUID);
	}
}
