package me.mrmango404.commands;

import me.mrmango404.ConfigHandler;
import me.mrmango404.GUI;
import me.mrmango404.Main;
import me.mrmango404.util.ClearShulker;
import me.mrmango404.util.HideShulker;
import me.mrmango404.util.MsgPlayer;
import me.mrmango404.util.Translate;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.IslandsManager;

import java.util.*;

public class CommandView implements ICommand {

	private final Main mainClass = Main.getMain();

	public enum CONTAINER_TYPE {
		SINGLE,
		DOUBLE
	}

	@Override
	public void run(Player player) {

		if (!canRun(player)) {
			MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NO_PERMISSION);
			return;
		}

		final int HOOK_RESIDENCE_HEIGHT = 400;
		Plugin resPlug = Main.getMain().getServer().getPluginManager().getPlugin("Residence");

		String teamName;
		UUID playerUUID = player.getUniqueId();
		ArrayList<Team> teamsList = new ArrayList<>();
		Location playerLocation = player.getLocation();
		// ★★★ 修改：使用 getBlocksInFront，只抓玩家前方 180 度範圍內的方塊 ★★★
		ArrayList<Block> blockList = getBlocksInFront(player, ConfigHandler.VIEW_RANGE);
		ArrayList<Location> singleLocation = new ArrayList<>();
		HashMap<Location, Location> doubleLocation = new HashMap<>();
		ArrayList<LivingEntity> singleShulkers = new ArrayList<>();
		LinkedHashMap<LivingEntity, LivingEntity> doubleShulkers = new LinkedHashMap<>();

		/*
		Add each container's location to according lists.
		 */
		for (Block block : blockList) {
			for (Material material : getContainers()) {
				if (!getBlockType(block).equals(material)) {
					continue;
				}

				// ★★★ 新增：若此容器下方是金磚，則略過 ★★★
				Block belowBlock = block.getLocation().clone().subtract(0, 1, 0).getBlock();
				if (belowBlock.getType() == Material.GOLD_BLOCK) {
					continue;
				}

				singleLocation.add(block.getLocation());

				if (getBlockType(block).equals(Material.CHEST)) {
					Chest chest = (Chest) block.getState();
					InventoryHolder holder = chest.getInventory().getHolder();
					if (holder instanceof DoubleChest doubleChest) {
						Chest leftSide = (Chest) doubleChest.getLeftSide();
						Chest rightSide = (Chest) doubleChest.getRightSide();
						doubleLocation.put(leftSide.getLocation(), rightSide.getLocation());
					}
				}
			}
		}

		if (singleLocation.isEmpty() && doubleLocation.isEmpty()) {
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
		for (Map.Entry<Location, Location> set : doubleLocation.entrySet()) {
			singleLocation.removeIf(aLocationOfSingleChest -> aLocationOfSingleChest.equals(set.getKey()) || aLocationOfSingleChest.equals(set.getValue()));
		}

		/*
		Spawn shulkers
		 */
		// Spawning for single containers
		int i = 0;
		for (Location location : singleLocation) {
			if (resPlug != null) {
				location = location.add(0, HOOK_RESIDENCE_HEIGHT, 0);
			}

			if (i < getColors().size()) {
				LivingEntity shulkerSingle = player.getWorld().spawn(location, Shulker.class, shulker -> shulker.setInvisible(true));

				if (resPlug != null) {
					location = location.subtract(0, HOOK_RESIDENCE_HEIGHT, 0);
					shulkerSingle.teleport(location);
				}

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
		for (Map.Entry<Location, Location> set : doubleLocation.entrySet()) {
			Location locationLeft = set.getValue();
			Location locationRight = set.getKey();

			if (resPlug != null) {
				locationLeft = locationLeft.add(0, HOOK_RESIDENCE_HEIGHT, 0);
				locationRight = locationRight.add(0, HOOK_RESIDENCE_HEIGHT, 0);
			}

			if (j < getColors().size()) {
				LivingEntity shulkerLeft = player.getWorld().spawn(locationLeft, Shulker.class, shulker -> shulker.setInvisible(true));
				LivingEntity shulkerRight = player.getWorld().spawn(locationRight, Shulker.class, shulker -> shulker.setInvisible(true));

				if (resPlug != null) {
					locationLeft = locationLeft.subtract(0, HOOK_RESIDENCE_HEIGHT, 0);
					locationRight = locationRight.subtract(0, HOOK_RESIDENCE_HEIGHT, 0);
					shulkerLeft.teleport(locationLeft);
					shulkerRight.teleport(locationRight);
				}

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
			if (ConfigHandler.GUI_AUTO_CLOSE && player.getOpenInventory().getTopInventory().getHolder() instanceof GUI) {
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
		entity.setPersistent(false);
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

	/**
	 * Check if the player has enough permission to run this command.
	 *
	 * @param player Player to check.
	 * @return Has enough permission or not.
	 */
	private boolean canRun(Player player) {
		if (!player.hasPermission(ConfigHandler.Permission.USE)) {
			return false;
		}

		return hasFlagPermission(player);
	}

	/**
	 * Check if player has flag permission from Bentobox's BSkyblock.
	 *
	 * @param player Player to check.
	 * @return Has permission or not.
	 */
	private boolean hasFlagPermission(Player player) {
		if (Main.bentoBoxPlug != null) {
			User user = User.getInstance(player.getUniqueId());
			IslandsManager manager = BentoBox.getInstance().getIslandsManager();
			Optional<Island> island = manager.getIslandAt(user.getLocation());

			if (island.isPresent()) {
				return island.get().isAllowed(user, Main.flag);
			}
		}
		return true;
	}

	// ★★★ 新增：只抓取玩家前方 180 度範圍內的所有非空氣方塊 ★★★
	private ArrayList<Block> getBlocksInFront(Player player, int range) {
		ArrayList<Block> blocks = new ArrayList<>();
		Location eyeLoc = player.getEyeLocation();
		// 玩家朝向向量
		Vector dir = eyeLoc.getDirection().normalize();

		// 在三維空間裡，x, y, z 各從 -range 到 range
		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				for (int z = -range; z <= range; z++) {
					// 計算每個偏移量對應的實際座標
					Location checkLoc = eyeLoc.clone().add(x, y, z);

					// 距離判斷，若超出 range，不處理
					double distSq = eyeLoc.distanceSquared(checkLoc);
					if (distSq > range * range) {
						continue;
					}

					// 計算 [玩家眼睛 -> 目標方塊] 的向量
					Vector toBlock = checkLoc.toVector().subtract(eyeLoc.toVector()).normalize();
					// 與玩家面向方向 (dir) 的夾角 (弧度)
					double angle = dir.angle(toBlock);
					// 把弧度轉成角度，若 <= 90 則代表此方塊位於玩家前方 180 度之內
					if (Math.toDegrees(angle) <= 90) {
						Block block = checkLoc.getBlock();
						// 過濾空氣方塊
						if (block.getType() != Material.AIR) {
							blocks.add(block);
						}
					}
				}
			}
		}
		return blocks;
	}

	/**
	 * Based on the player's location and specified half-width (int range),
	 * this function returns a list of blocks within the cuboid-shaped area centered on the player.
	 * The cuboid extends from the player's position in all directions (x, y, z) by the specified range.
	 *
	 * @param player
	 * @param range
	 * @return
	 */
	public static ArrayList<Block> getBlocks(Player player, int range) {

		World world = player.getWorld();
		ArrayList<Block> blockList = new ArrayList<>();

		int playerLocationTopX = player.getLocation().getBlockX() + range;
		int playerLocationTopY = player.getLocation().getBlockY() + range;
		int playerLocationTopZ = player.getLocation().getBlockZ() + range;

		int playerLocationBottomX = player.getLocation().getBlockX() - range;
		int playerLocationBottomY = player.getLocation().getBlockY() - range;
		int playerLocationBottomZ = player.getLocation().getBlockZ() - range;

		for (int x = playerLocationBottomX; x <= playerLocationTopX; x++) {
			for (int y = playerLocationBottomY; y <= playerLocationTopY; y++) {
				for (int z = playerLocationBottomZ; z <= playerLocationTopZ; z++) {

					Location location = new Location(world, x, y, z);
					Block block = world.getBlockAt(location);
					blockList.add(block);
				}
			}
		}

		return blockList;
	}
}
