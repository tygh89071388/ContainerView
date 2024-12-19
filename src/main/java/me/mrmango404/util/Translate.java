package me.mrmango404.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translate {

	public static String color(String message) {

		Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
		Matcher matcher = pattern.matcher(message);

		while (matcher.find()) {
			String color = message.substring(matcher.start(), matcher.end());
			message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color.substring(1)) + "");
			matcher = pattern.matcher(message);
		}

		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
