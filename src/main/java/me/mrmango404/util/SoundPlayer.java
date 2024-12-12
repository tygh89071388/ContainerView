package me.mrmango404.util;

import me.mrmango404.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundPlayer {

	public enum ACTION {
		OPEN,
		CLOSE
	}

	public static void playSound(Player player, ACTION action) {

		Location playerLocation = player.getLocation();
		boolean playOrNot = ConfigHandler.Sound.ENABLE;
		Sound openSound = Sound.valueOf(ConfigHandler.Sound.OPEN_SOUND);
		Sound closeSound = Sound.valueOf(ConfigHandler.Sound.CLOSE_SOUND);

		if (!playOrNot) {
			return;
		}

		if (action.equals(ACTION.OPEN)) {
			player.playSound(playerLocation, openSound, 1, 1);
		}

		if (action.equals(ACTION.CLOSE)) {
			player.playSound(playerLocation, closeSound, 1, 1);
		}
	}
}
