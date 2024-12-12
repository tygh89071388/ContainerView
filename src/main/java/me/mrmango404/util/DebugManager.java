package me.mrmango404.util;

import me.mrmango404.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class DebugManager {

	private static final ConsoleCommandSender console = Bukkit.getConsoleSender();

	/**
	 * If debug option in config is enabled,
	 * the plugin will send the debug message.
	 *
	 * @param message Log message to send in console.
	 */
	public static void log(String message) {

		if (ConfigHandler.DEBUG) {
			console.sendMessage("[DEBUG] " + message);
		}
	}
}
