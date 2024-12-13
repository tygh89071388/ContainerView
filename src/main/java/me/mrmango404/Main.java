package me.mrmango404;

import de.Linus122.SafariNet.API.SafariNet;
import me.mrmango404.commands.CommandExecutor;
import me.mrmango404.events.*;
import me.mrmango404.hooks.Safarinet;
import me.mrmango404.util.ClearShulker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

	private static Main instance;
	// The " ꞌ " character was hard-coded here to identify if a gui belongs to this plugin.
	public static String GUIIdentifier = "ꞌ";
	public static Scoreboard scoreboard;
	public static HashMap<UUID, BukkitTask> globalSchedulers = new HashMap<>();
	public static HashMap<UUID, ArrayList<Team>> globalTeamList = new HashMap<>();
	public static HashMap<UUID, Location> globalActiveViewSessionLocations = new HashMap<>();
	public static HashMap<UUID, ArrayList<LivingEntity>> globalSingleShulkers = new HashMap<>();
	public static HashMap<UUID, HashMap<LivingEntity, LivingEntity>> globalDoubleShulkers = new HashMap<>();

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

		if (Bukkit.getPluginManager().getPlugin("SafariNet") != null) {
			Safarinet safarinet = new Safarinet();
			SafariNet.addListener(safarinet);
		}
	}

	@Override
	public void onDisable() {
		ClearShulker.clear();
	}

	public static Main getMain() {
		return instance;
	}
}