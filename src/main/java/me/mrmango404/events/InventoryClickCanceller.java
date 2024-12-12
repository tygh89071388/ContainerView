package me.mrmango404.events;

import me.mrmango404.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickCanceller implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {

		Player player = (Player) event.getWhoClicked();
//		UUID playerUUID = player.getUniqueId();

		String eventTitle = event.getView().getTitle();
		if (eventTitle.contains(Main.GUIIdentifier)) {
			event.setCancelled(true);
		}

//		ArrayList<LivingEntity> globalSingleShulkers = Main.globalSingleShulkers.get(playerUUID);
//		HashMap<LivingEntity, LivingEntity> globalDoubleShulkers = Main.globalDoubleShulkers.get(playerUUID);
//
//		if (globalSingleShulkers != null) {
//			for (LivingEntity singleShulker : globalSingleShulkers) {
//				Location location = singleShulker.getLocation();
//				eventTitle = getString(event, eventTitle, location);
//			}
//		}
//
//		if (globalDoubleShulkers != null) {
//			for (Map.Entry<LivingEntity, LivingEntity> doubleShulkerSet : globalDoubleShulkers.entrySet()) {
//				Location locationLeft = doubleShulkerSet.getKey().getLocation();
//				Location locationRight = doubleShulkerSet.getValue().getLocation();
//				eventTitle = getString(event, eventTitle, locationLeft);
//				eventTitle = getString(event, eventTitle, locationRight);
//			}
//		}
	}
//
//	private String getString(InventoryClickEvent event, String eventTitle, Location location) {
//
//		String containerTitle;
//		int x = location.getBlockX();
//		int y = location.getBlockY();
//		int z = location.getBlockZ();
//		containerTitle = Translate.color(ConfigHandler.Msg.GUI_TITLE
//				.replace("{icon}", ConfigHandler.Msg.CHAT_ICON)
//				.replace("{x}", String.valueOf(x))
//				.replace("{y}", String.valueOf(y))
//				.replace("{z}", String.valueOf(z)));
//
//		eventTitle = ChatColor.stripColor(eventTitle);
//		containerTitle = ChatColor.stripColor(containerTitle);
//
//		System.out.println(eventTitle);
//		System.out.println(containerTitle);
//		System.out.println(" ");
//
//		if (eventTitle.equals(containerTitle)) {
//			event.setCancelled(true);
//		}
//		return eventTitle;
//	}
}
