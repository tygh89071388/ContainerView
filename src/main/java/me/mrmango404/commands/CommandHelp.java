package me.mrmango404.commands;

import me.mrmango404.ConfigHandler;
import me.mrmango404.util.MsgPlayer;
import me.mrmango404.util.Translate;
import org.bukkit.entity.Player;

public class CommandHelp implements ICommand {

	@Override
	public void run(Player player) {

		if (!player.hasPermission(ConfigHandler.Permission.USE)) {
			MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NO_PERMISSION);
			return;
		}

		if (ConfigHandler.Msg.HELP.isEmpty()) {
			return;
		}

		for (String message : ConfigHandler.Msg.HELP) {
			if ((message.contains("reload") && !player.hasPermission(ConfigHandler.Permission.RELOAD))) {
				continue;
			}

			player.sendMessage(Translate.color(message));
		}
	}
}
