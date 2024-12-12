package me.mrmango404.util;

import org.bukkit.ChatColor;

public class Translate {

	public static String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
