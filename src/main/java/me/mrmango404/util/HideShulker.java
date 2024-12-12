package me.mrmango404.util;

import me.mrmango404.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class HideShulker {

	private final Main mainClass = Main.getMain();

	/**
	 * Hide the shulker from online players.
	 *
	 * @param playerUUID Target player's UUID.
	 * @param entity     Entity to hide.
	 */
	public HideShulker(UUID playerUUID, Entity entity) {
		hide(playerUUID, entity);
	}

	/**
	 * Hide the shulker from joining players.
	 *
	 * @param player Target player.
	 * @param entity Entity to hide.
	 */
	public HideShulker(Player player, Entity entity) {
		hide(player, entity);
	}

	private void hide(UUID playerUUID, Entity entity) {

		ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

		for (Player playerToHide : players) {
			if (!playerToHide.getUniqueId().equals(playerUUID)) {
				playerToHide.hideEntity(mainClass, entity);
			}
		}
	}

	private void hide(Player player, Entity entity) {
		player.hideEntity(mainClass, entity);
	}
}
