package me.mrmango404;

import de.Linus122.SafariNet.API.Listener;
import de.Linus122.SafariNet.API.SafariNet;
import me.mrmango404.commands.CommandExecutor;
import me.mrmango404.events.*;
import me.mrmango404.hooks.BSkyblockFlag;
import me.mrmango404.util.ClearShulker;
import me.mrmango404.util.DebugManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.managers.RanksManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Main extends JavaPlugin {
	public static Flag flag;
	public static Plugin safariPlug;
	public static Plugin bentoBoxPlug;
	private static Main instance;
	public static Scoreboard scoreboard;
	public static HashMap<UUID, BukkitTask> globalSchedulers = new HashMap<>();
	public static HashMap<UUID, ArrayList<Team>> globalTeamList = new HashMap<>();
	public static HashMap<UUID, Location> globalActiveViewSessionLocations = new HashMap<>();
	public static HashMap<UUID, ArrayList<LivingEntity>> globalSingleShulkers = new HashMap<>();
	public static HashMap<UUID, LinkedHashMap<LivingEntity, LivingEntity>> globalDoubleShulkers = new HashMap<>();

	@Override
	public void onEnable() {

		instance = this;
		this.getCommand("cview").setExecutor(new CommandExecutor());
		Bukkit.getPluginManager().registerEvents(new GUISoundPlayer(), this);
		Bukkit.getPluginManager().registerEvents(new LeaveCanceller(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClickCanceller(), this);
		Bukkit.getPluginManager().registerEvents(new ShulkerJoinHider(), this);
		Bukkit.getPluginManager().registerEvents(new LootCanceller(), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakDeletion(), this);
		Bukkit.getPluginManager().registerEvents(new DamageCanceller(), this);
		ConfigHandler.load();
		instance.saveDefaultConfig();
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		safariPlug = Bukkit.getPluginManager().getPlugin("SafariNet");
		bentoBoxPlug = Bukkit.getPluginManager().getPlugin("BentoBox");

		if (bentoBoxPlug != null) {
			flag = new Flag.Builder(BSkyblockFlag.CONTAINER_VIEW_PROTECTION.getFlagName(), Material.YELLOW_SHULKER_BOX)
					.type(Flag.Type.PROTECTION)
					.mode(Flag.Mode.ADVANCED)
					.defaultRank(RanksManager.MEMBER_RANK)
					.defaultSetting(true).build();

			BentoBox.getInstance().getFlagsManager().registerFlag(flag);
		}

		if (safariPlug != null) {
			try {
				Class<?> listenerClass = Class.forName("me.mrmango404.hooks.Safarinet");
				Listener listener = (de.Linus122.SafariNet.API.Listener) listenerClass.getDeclaredConstructor().newInstance();
				SafariNet.addListener(listener);
				DebugManager.log("SafariNet hook loaded.");
			} catch (Exception e) {
				DebugManager.log("SafariNet hook could not be loaded.");
			}
		} else {
			DebugManager.log("SafariNet hook could not be found.");
		}

		new Metrics(this, 26011);
	}

	@Override
	public void onDisable() {
		ClearShulker.clear();
	}

	public static Main getMain() {
		return instance;
	}
}