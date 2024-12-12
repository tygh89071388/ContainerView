package me.mrmango404.commands;

import me.mrmango404.ConfigHandler;
import me.mrmango404.Main;
import me.mrmango404.util.CheckActive;
import me.mrmango404.util.MsgPlayer;
import me.mrmango404.util.SoundPlayer;
import me.mrmango404.util.Translate;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class CommandViewGUI {

	public void run(Player player, CommandView.CONTAINER_TYPE type, int container) {

		if (!player.hasPermission(ConfigHandler.Permission.USE)) {
			MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NO_PERMISSION);
			return;
		}

		if (!CheckActive.isPlayerActive(player)) {
			MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NOT_VIEWABLE);
			return;
		}

		LivingEntity shulker;
		Block targetBlock;
		ChatColor teamColor;
		World world = player.getWorld();
		UUID playerUUID = player.getUniqueId();
		ArrayList<LivingEntity> globalSingleShulkers = Main.globalSingleShulkers.get(playerUUID);
		HashMap<LivingEntity, LivingEntity> globalDoubleShulkers = Main.globalDoubleShulkers.get(playerUUID);


		if (type == CommandView.CONTAINER_TYPE.SINGLE) {
			if (globalSingleShulkers == null) {
				MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NOT_VIEWABLE);
				return;
			}

			if (globalSingleShulkers.size() < container) {
				MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NOT_VIEWABLE);
				return;
			}

			Location containerLocation;
			List<String> tag;

			shulker = globalSingleShulkers.get(container);
			containerLocation = shulker.getLocation();
			targetBlock = world.getBlockAt(shulker.getLocation());
			tag = new ArrayList<>(shulker.getScoreboardTags());
			teamColor = ChatColor.valueOf(tag.getFirst());
			viewContainer(player, containerLocation, teamColor, targetBlock, CommandView.CONTAINER_TYPE.SINGLE);
		}

		if (type == CommandView.CONTAINER_TYPE.DOUBLE) {
			if (globalDoubleShulkers == null) {
				MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NOT_VIEWABLE);
				return;
			}

			if (globalDoubleShulkers.size() < container) {
				MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NOT_VIEWABLE);
				return;
			}

			List<String> tag;
			LivingEntity shulkerLeft = null;
			Location containerLocation;

			int i = 0;
			for (Map.Entry<LivingEntity, LivingEntity> entry : globalDoubleShulkers.entrySet()) {
				if (i == container) {
					shulkerLeft = entry.getValue();
					break;
				}
				i++;
			}

			containerLocation = shulkerLeft.getLocation();
			targetBlock = world.getBlockAt(shulkerLeft.getLocation());
			tag = new ArrayList<>(shulkerLeft.getScoreboardTags());
			teamColor = ChatColor.valueOf(tag.getFirst());
			viewContainer(player, containerLocation, teamColor, targetBlock, CommandView.CONTAINER_TYPE.DOUBLE);
		}

	}

	private void openContainer(Player player, Block targetBlock, String containerTitle) {

		if (targetBlock.getType().equals(Material.CHEST) || targetBlock.getType().equals(Material.TRAPPED_CHEST)) {
			Chest chest = (Chest) targetBlock.getState();
			Inventory inv = Bukkit.createInventory(player, chest.getInventory().getSize(), containerTitle);
			inv.setContents(chest.getInventory().getContents());
			player.openInventory(inv);
			SoundPlayer.playSound(player, SoundPlayer.ACTION.OPEN);
			return;
		}

		if (targetBlock.getType().equals(Material.DROPPER)) {
			Inventory inv = Bukkit.createInventory(player, InventoryType.DROPPER, containerTitle);
			Dropper dropper = (Dropper) targetBlock.getState();
			inv.setContents(dropper.getInventory().getContents());
			player.openInventory(inv);
			SoundPlayer.playSound(player, SoundPlayer.ACTION.OPEN);
			return;
		}

		if (targetBlock.getType().equals(Material.DISPENSER)) {
			Inventory inv = Bukkit.createInventory(player, InventoryType.DROPPER, containerTitle);
			Dispenser dispenser = (Dispenser) targetBlock.getState();
			inv.setContents(dispenser.getInventory().getContents());
			player.openInventory(inv);
			SoundPlayer.playSound(player, SoundPlayer.ACTION.OPEN);
			return;
		}

		if (targetBlock.getType().equals(Material.BARREL)) {
			Inventory inv = Bukkit.createInventory(player, InventoryType.BARREL, containerTitle);
			Barrel barrel = (Barrel) targetBlock.getState();
			inv.setContents(barrel.getInventory().getContents());
			player.openInventory(inv);
			SoundPlayer.playSound(player, SoundPlayer.ACTION.OPEN);
			return;
		}

		if (targetBlock.getState() instanceof ShulkerBox shulkerBox) {
			Inventory inv = Bukkit.createInventory(player, InventoryType.SHULKER_BOX, containerTitle);
			inv.setContents(shulkerBox.getInventory().getContents());
			player.openInventory(inv);
			SoundPlayer.playSound(player, SoundPlayer.ACTION.OPEN);
		}
	}

	private void viewContainer(Player player, Location shulkerLocation, ChatColor teamColor, Block targetBlock, CommandView.CONTAINER_TYPE type) {

		int x = shulkerLocation.getBlockX();
		int y = shulkerLocation.getBlockY();
		int z = shulkerLocation.getBlockZ();
		String icon;

		if (type == CommandView.CONTAINER_TYPE.SINGLE) {
			icon = ConfigHandler.Msg.CHAT_ICON_SINGLE;
		} else {
			icon = ConfigHandler.Msg.CHAT_ICON_DOUBLE;
		}
		String containerTitle = Translate.color(teamColor + Main.GUIIdentifier + ConfigHandler.Msg.GUI_TITLE
				.replace("{icon}", icon)
				.replace("{x}", String.valueOf(x))
				.replace("{y}", String.valueOf(y))
				.replace("{z}", String.valueOf(z)));

		openContainer(player, targetBlock, containerTitle);
	}
}
