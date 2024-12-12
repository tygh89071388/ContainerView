package me.mrmango404.commands;

import me.mrmango404.ConfigHandler;
import me.mrmango404.util.MsgPlayer;
import org.bukkit.entity.Player;

public class CommandReload implements ICommand {

	@Override
	public void run(Player player) {

		if (!player.hasPermission(ConfigHandler.Permission.RELOAD)) {
			MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NO_PERMISSION);
			return;
		}

		try {
			ConfigHandler.load();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.RELOADED);
	}
}
