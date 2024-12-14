package me.mrmango404.events;

import me.mrmango404.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickCanceller implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {

		String eventTitle = event.getView().getTitle();
		if (eventTitle.contains(Main.GUIIdentifier)) {
			event.setCancelled(true);
		}
	}
}
