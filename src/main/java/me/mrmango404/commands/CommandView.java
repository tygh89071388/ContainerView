package me.mrmango404.commands;

import me.mrmango404.ConfigHandler;
import me.mrmango404.Main;
import me.mrmango404.util.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class CommandView implements ICommand {

	private final Main mainClass = Main.getMain();

	public enum CONTAINER_TYPE {
		SINGLE,
		DOUBLE
	}

	@Override
	public void run(Player player) {

		if (!player.hasPermission(ConfigHandler.Permission.USE)) {
			MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NO_PERMISSION);
			return;
		}

		String teamName;
		UUID playerUUID = player.getUniqueId();
		ArrayList<Team> teamsList = new ArrayList<>();
		Location playerLocation = player.getLocation();
		ArrayList<Block> blockList = Cuboid.getBlocks(player, ConfigHandler.VIEW_RANGE);
		ArrayList<Location> singleContainerList = new ArrayList<>();
		HashMap<Location, Location> doubleContainerList = new HashMap<>();
		ArrayList<LivingEntity> singleShulkers = new ArrayList<>();
		HashMap<LivingEntity, LivingEntity> doubleShulkers = new HashMap<>();

		/*
		Add each container's location to according lists.
		 */
		for (Block block : blockList) {
			for (Material material : getContainers()) {
				if (getBlockType(block).equals(material)) {
					singleContainerList.add(block.getLocation());
				}
			}
			if (getBlockType(block).equals(Material.CHEST)) {
				Chest chest = (Chest) block.getState();
				InventoryHolder holder = chest.getInventory().getHolder();
				if (holder instanceof DoubleChest doubleChest) {
					Chest leftSide = (Chest) doubleChest.getLeftSide();
					Chest rightSide = (Chest) doubleChest.getRightSide();
					doubleContainerList.put(leftSide.getLocation(), rightSide.getLocation());
				}
			}
		}

		if (singleContainerList.isEmpty() && doubleContainerList.isEmpty()) {
			MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NOT_FOUND);
			return;
		}

		// If another view session is created before the previous one can end,
		// clear the shulkers to prevent entity stacking.
		new ClearShulker(player);

		MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.ENABLED);

		// The plugin uses this location to hide the shulkers from players who join after the view session is created.
		// It will hide the shulkers if another player joining is within range of this location.
		Main.globalActiveViewSessionLocations.put(playerUUID, playerLocation);

		/*
		Remove the double chests in the single-type containers list.
		 */
		for (Map.Entry<Location, Location> set : doubleContainerList.entrySet()) {
			singleContainerList.removeIf(aLocationOfSingleChest -> aLocationOfSingleChest.equals(set.getKey()) || aLocationOfSingleChest.equals(set.getValue()));
		}

		/*
		Spawn shulkers
		 */
		// Spawning for single containers
		int i = 0;
		for (Location location : singleContainerList) {
			if (i < getColors().size()) {
				LivingEntity shulkerSingle = player.getWorld().spawn(location, Shulker.class, shulker -> shulker.setInvisible(true));
				shulkerSingle = setEntityAttribute(shulkerSingle);
				shulkerSingle.addScoreboardTag(getColorsInString().get(i));

				teamName = "ContainerView_" + player.getDisplayName() + "_Single_No_" + i;
				Team team = Main.scoreboard.registerNewTeam(teamName);
				team.setColor(getColors().get(i));
				team.addEntry(shulkerSingle.getUniqueId().toString());

				sendClickableView(player, location, CONTAINER_TYPE.SINGLE, i);

				i++;
				teamsList.add(team);
				singleShulkers.add(shulkerSingle);
				new HideShulker(playerUUID, shulkerSingle);
			}
		}

		// Spawning for Double Chest
		int j = 0;
		for (Map.Entry<Location, Location> set : doubleContainerList.entrySet()) {
			if (j < getColors().size()) {
				LivingEntity shulkerLeft = player.getWorld().spawn(set.getKey(), Shulker.class, shulker -> shulker.setInvisible(true));
				LivingEntity shulkerRight = player.getWorld().spawn(set.getValue(), Shulker.class, shulker -> shulker.setInvisible(true));
				shulkerLeft.addScoreboardTag(getColorsInString().get(j));
				shulkerRight.addScoreboardTag(getColorsInString().get(j));

				teamName = "ContainerView_" + player.getDisplayName() + "_Double_No_" + j;
				Team team = Main.scoreboard.registerNewTeam(teamName);
				team.setColor(getColors().get(j));
				team.addEntry(shulkerLeft.getUniqueId().toString());
				team.addEntry(shulkerRight.getUniqueId().toString());

				sendClickableView(player, set.getKey(), CONTAINER_TYPE.DOUBLE, j);

				j++;
				teamsList.add(team);
				shulkerLeft = setEntityAttribute(shulkerLeft);
				shulkerRight = setEntityAttribute(shulkerRight);
				new HideShulker(playerUUID, shulkerLeft);
				new HideShulker(playerUUID, shulkerRight);
				doubleShulkers.put(shulkerLeft, shulkerRight);
			}
		}
		sendClickableEnd(player);

		player.setScoreboard(Main.scoreboard);
		Main.globalTeamList.put(playerUUID, teamsList);
		Main.globalSingleShulkers.put(playerUUID, singleShulkers);
		Main.globalDoubleShulkers.put(playerUUID, doubleShulkers);

		/*
		When time is up, do this!
		 */
		BukkitTask task = Bukkit.getScheduler().runTaskLater(mainClass, () -> {
			new ClearShulker(player);

			// auto close part
			if (ConfigHandler.GUI_AUTO_CLOSE && player.getOpenInventory().getTitle().contains(Main.GUIIdentifier)) {
				player.closeInventory();
			}

		}, ConfigHandler.VIEW_SESSION_DURATION * 20);
		Main.globalSchedulers.put(playerUUID, task);
	}

	// Return a type of the block
	private Material getBlockType(Block block) {
		return block.getType();
	}

	private LivingEntity setEntityAttribute(LivingEntity entity) {

		entity.setAI(false);
		entity.setGravity(false);
		entity.setGlowing(true);
		entity.setInvulnerable(true);
		return entity;
	}

	private List<Material> getContainers() {

		List<Material> material = new ArrayList<>();
		for (String string : ConfigHandler.CONTAINERS) {
			material.add(Material.valueOf(string));
		}
		return material;
	}

	private List<ChatColor> getColors() {

		List<ChatColor> colors = new ArrayList<>();
		for (String string : ConfigHandler.COLORS) {
			colors.add(ChatColor.valueOf(string));
		}
		return colors;
	}

	private List<String> getColorsInString() {
		return new ArrayList<>(ConfigHandler.COLORS);
	}

	private void sendClickableView(Player player, Location containerLocation, CONTAINER_TYPE type, int i) {

		String body;
		String hover;
		String command;
		TextComponent icon;
		TextComponent bodyComponent;
		TextComponent hoverComponent;
		int x = containerLocation.getBlockX();
		int y = containerLocation.getBlockY();
		int z = containerLocation.getBlockZ();
		ChatColor teamColor = getColors().get(i);

		body = Translate.color(ConfigHandler.Msg.CHAT_BODY
				.replace("{x}", String.valueOf(x))
				.replace("{y}", String.valueOf(y))
				.replace("{z}", String.valueOf(z)));
		hover = Translate.color(ConfigHandler.Msg.CHAT_CLICKABLE);

		if (type == CONTAINER_TYPE.SINGLE) {
			command = "/cview open single " + i;
			icon = new TextComponent(teamColor + ConfigHandler.Msg.CHAT_ICON_SINGLE);
		} else {
			command = "/cview open double " + i;
			icon = new TextComponent(teamColor + ConfigHandler.Msg.CHAT_ICON_DOUBLE);
		}
		bodyComponent = new TextComponent(body);
		hoverComponent = new TextComponent(hover);
		hoverComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		hoverComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Translate.color(ConfigHandler.Msg.CHAT_HOVER))));

		icon.addExtra(bodyComponent);
		icon.addExtra(hoverComponent);

		player.spigot().sendMessage(icon);
	}

	private void sendClickableEnd(Player player) {

		TextComponent bodyComponent = new TextComponent(Translate.color(ConfigHandler.Msg.END_BODY));
		TextComponent hoverComponent = new TextComponent(Translate.color(ConfigHandler.Msg.END_CLICKABLE));
		hoverComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cview end"));
		hoverComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Translate.color(Translate.color(ConfigHandler.Msg.END_HOVER)))));

		bodyComponent.addExtra(hoverComponent);
		player.spigot().sendMessage(bodyComponent);
	}
}



