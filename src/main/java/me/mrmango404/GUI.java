package me.mrmango404;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUI implements InventoryHolder {

	private final Inventory inventory;

	public GUI(Main plugin, InventoryType type, String title, int... size) {
		if (type == InventoryType.CHEST) {
			inventory = Bukkit.createInventory(this, size[0], title);
			return;
		}
		inventory = plugin.getServer().createInventory(this, type, title);
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
