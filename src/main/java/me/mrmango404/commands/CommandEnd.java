package me.mrmango404.commands;

import me.mrmango404.ConfigHandler;
import me.mrmango404.util.CheckActive;
import me.mrmango404.util.ClearShulker;
import me.mrmango404.util.MsgPlayer;
import org.bukkit.entity.Player;

public class CommandEnd implements ICommand {

	@Override
	public void run(Player player) {

		if (!player.hasPermission(ConfigHandler.Permission.USE)) {
			MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NO_PERMISSION);
			return;
		}

		if (!CheckActive.isPlayerActive(player)) {
			MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.NOT_ENABLED);
			return;
		}

		new ClearShulker(player);
	}
}
