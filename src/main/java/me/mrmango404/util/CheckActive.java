package me.mrmango404.util;

import me.mrmango404.Main;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CheckActive {


	/*
	This function checks if the player is in an active viewing section
	 */
	public static boolean isPlayerActive(Player player) {

		UUID playerUUID = player.getUniqueId();
		return Main.globalActiveViewSessionLocations.containsKey(playerUUID);
	}

	/*
	This function checks if there are any active viewing sections on the server
	 */
	public static boolean isServerActive() {

		return !Main.globalActiveViewSessionLocations.isEmpty();
	}
}
