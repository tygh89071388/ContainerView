package me.mrmango404.events;

import me.mrmango404.util.ClearShulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveCanceller implements Listener {

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		new ClearShulker(event.getPlayer());
	}
}
