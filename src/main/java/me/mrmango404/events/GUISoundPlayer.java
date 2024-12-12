package me.mrmango404.events;

import me.mrmango404.Main;
import me.mrmango404.util.SoundPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GUISoundPlayer implements Listener {

	@EventHandler
	public void onGUIOpen(InventoryOpenEvent event) {
		if (event.getView().getTitle().contains(Main.GUIIdentifier)) {
			SoundPlayer.playSound((Player) event.getPlayer(), SoundPlayer.ACTION.OPEN);
		}
	}

	@EventHandler
	public void onGUIOpen(InventoryCloseEvent event) {
		if (event.getView().getTitle().contains(Main.GUIIdentifier)) {
			SoundPlayer.playSound((Player) event.getPlayer(), SoundPlayer.ACTION.CLOSE);
		}
	}
}
