package me.mrmango404;

import me.mrmango404.util.DebugManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class ConfigHandler {

	// Config overall settings
	public static int VIEW_RANGE;
	public static long VIEW_SESSION_DURATION;
	public static boolean GUI_AUTO_CLOSE;
	public static List<String> CONTAINERS;
	public static List<String> COLORS;
	public static boolean DEBUG;

	// Config sound settings
	public final static class Sound {

		public static boolean ENABLE;
		public static String OPEN_SOUND;
		public static String CLOSE_SOUND;
	}

	// Config permission settings
	public final static class Permission {

		public static String USE;
		public static String RELOAD;
	}

	// Overall Messages
	public final static class Msg {

		public static String PREFIX;
		public static String GUI_TITLE;
		public static String CHAT_ICON_SINGLE;
		public static String CHAT_ICON_DOUBLE;
		public static String CHAT_BODY;
		public static String CHAT_CLICKABLE;
		public static String CHAT_HOVER;
		public static String END_BODY;
		public static String END_CLICKABLE;
		public static String END_HOVER;
		public static String ENABLED;
		public static String DISABLED;
		public static String NOT_ENABLED;
		public static String NOT_VIEWABLE;
		public static String NOT_FOUND;
		public static String NO_PERMISSION;
		public static String UNKNOWN_USAGE;
		public static String RELOADED;
		public static List<String> HELP;
	}

	public static void load() {

		Main mainClass = Main.getMain();
		String configPath = "config.yml";
		String msgPath = "message.yml";
		File configFile = new File(mainClass.getDataFolder(), configPath);
		File msgFile = new File(mainClass.getDataFolder(), msgPath);

		if (!configFile.exists()) {
			mainClass.saveResource(configPath, false);
		}

		if (!msgFile.exists()) {
			mainClass.saveResource(msgPath, false);
		}

		YamlConfiguration msg = YamlConfiguration.loadConfiguration(msgFile);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		YamlConfiguration defaultMsg = YamlConfiguration.loadConfiguration(new InputStreamReader(mainClass.getResource(msgPath)));
		YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(mainClass.getResource(configPath)));

		config.addDefaults(defaultConfig);
		config.addDefaults(defaultMsg);

		VIEW_RANGE = config.getInt("view_range");
		VIEW_SESSION_DURATION = config.getLong("view_session_duration");
		GUI_AUTO_CLOSE = config.getBoolean("gui_auto_close");
		CONTAINERS = config.getStringList("containers");
		COLORS = config.getStringList("colors");
		DEBUG = config.getBoolean("debug");
		Sound.ENABLE = config.getBoolean("sound.enable");
		Sound.OPEN_SOUND = config.getString("sound.open_sound");
		Sound.CLOSE_SOUND = config.getString("sound.close_sound");
		Permission.USE = config.getString("permissions.use");
		Permission.RELOAD = config.getString("permissions.reload");
		Msg.PREFIX = msg.getString("prefix");
		Msg.GUI_TITLE = msg.getString("gui_title");
		Msg.CHAT_ICON_SINGLE = msg.getString("chat.icon_single");
		Msg.CHAT_ICON_DOUBLE = msg.getString("chat.icon_double");
		Msg.CHAT_BODY = msg.getString("chat.body");
		Msg.CHAT_CLICKABLE = msg.getString("chat.clickable");
		Msg.CHAT_HOVER = msg.getString("chat.hover");
		Msg.END_BODY = msg.getString("end.body");
		Msg.END_CLICKABLE = msg.getString("end.clickable");
		Msg.END_HOVER = msg.getString("end.hover");
		Msg.ENABLED = msg.getString("enabled");
		Msg.DISABLED = msg.getString("disabled");
		Msg.NOT_ENABLED = msg.getString("not_enabled");
		Msg.NOT_VIEWABLE = msg.getString("not_viewable");
		Msg.NOT_FOUND = msg.getString("not_found");
		Msg.NO_PERMISSION = msg.getString("no_permission");
		Msg.UNKNOWN_USAGE = msg.getString("unknown_usage");
		Msg.RELOADED = msg.getString("reloaded");
		Msg.HELP = msg.getStringList("help");

		// Debug Section
		DebugManager.log("VIEW_RANGE: " + VIEW_RANGE);
		DebugManager.log("VIEW_SESSION_DURATION: " + VIEW_SESSION_DURATION);
		DebugManager.log("GUI_AUTO_CLOSE: " + GUI_AUTO_CLOSE);
		DebugManager.log("CONTAINERS: " + CONTAINERS);
		DebugManager.log("COLORS: " + COLORS);
		DebugManager.log("SOUND.ENABLE: " + Sound.ENABLE);
		DebugManager.log("SOUND.OPEN_SOUND: " + Sound.OPEN_SOUND);
		DebugManager.log("SOUND.CLOSE_SOUND: " + Sound.CLOSE_SOUND);
		DebugManager.log("PERMISSION.USE: " + Permission.USE);
		DebugManager.log("PERMISSION.RELOAD: " + Permission.RELOAD);
		DebugManager.log("MSG.PREFIX: " + Msg.PREFIX);
		DebugManager.log("MSG.GUI_TITLE: " + Msg.GUI_TITLE);
		DebugManager.log("MSG.CHAT_ICON_SINGLE: " + Msg.CHAT_ICON_SINGLE);
		DebugManager.log("MSG.CHAT_ICON_DOUBLE: " + Msg.CHAT_ICON_DOUBLE);
		DebugManager.log("MSG.CHAT_BODY: " + Msg.CHAT_BODY);
		DebugManager.log("MSG.CHAT_CLICKABLE: " + Msg.CHAT_CLICKABLE);
		DebugManager.log("MSG.CHAT_HOVER: " + Msg.CHAT_HOVER);
		DebugManager.log("MSG.END_BODY: " + Msg.END_BODY);
		DebugManager.log("MSG.END_CLICKABLE: " + Msg.END_CLICKABLE);
		DebugManager.log("MSG.END_HOVER: " + Msg.END_HOVER);
		DebugManager.log("MSG.ENABLED: " + Msg.ENABLED);
		DebugManager.log("MSG.DISABLED: " + Msg.DISABLED);
		DebugManager.log("MSG.NOT_ENABLED: " + Msg.NOT_ENABLED);
		DebugManager.log("MSG.NOT_VIEWABLE: " + Msg.NOT_VIEWABLE);
		DebugManager.log("MSG.NOT_FOUND: " + Msg.NOT_FOUND);
		DebugManager.log("MSG.NO_PERMISSION: " + Msg.NO_PERMISSION);
		DebugManager.log("MSG.UNKNOWN_USAGE: " + Msg.UNKNOWN_USAGE);
		DebugManager.log("MSG.RELOADED: " + Msg.RELOADED);
		DebugManager.log("MSG.HELP: " + Msg.HELP);
	}
}
