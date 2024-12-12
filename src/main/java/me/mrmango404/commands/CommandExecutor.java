package me.mrmango404.commands;

import me.mrmango404.ConfigHandler;
import me.mrmango404.Main;
import me.mrmango404.util.MsgPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor implements org.bukkit.command.CommandExecutor, TabCompleter {

	//TODO
	// -Help page

	private Main mainClass = Main.getMain();

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if (!(commandSender instanceof Player player)) {
			mainClass.getLogger().warning("This command is only available to the player!");
			return true;
		}

		if (strings.length == 1) {
			switch (strings[0]) {
				case "end" -> {
					new CommandEnd().run(player);
					return true;
				}
				case "view" -> {
					new CommandView().run(player);
					return true;
				}
				case "help" -> {
					new CommandHelp().run(player);
					return true;
				}
				case "reload" -> {
					new CommandReload().run(player);
					return true;
				}
			}
		}

		if (strings.length == 3) {
			if (strings[0].equalsIgnoreCase("open")) {
				new CommandViewGUI().run(player, (strings[1].equalsIgnoreCase("single")) ? CommandView.CONTAINER_TYPE.SINGLE : CommandView.CONTAINER_TYPE.DOUBLE, Integer.parseInt(strings[2]));
				return true;
			}
		}

		MsgPlayer.send(player, MsgPlayer.MESSAGE_FIELD.UNKNOWN_USAGE);
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

		if (!(commandSender instanceof Player player)) {
			return null;
		}

		if (strings.length == 1) {
			ArrayList<String> list = new ArrayList<>();

			if (player.hasPermission(ConfigHandler.Permission.USE)) {
				list.add("view");
				list.add("end");
				list.add("help");
			}

			if (player.hasPermission(ConfigHandler.Permission.RELOAD)) {
				list.add("reload");
			}

			list.removeIf(str -> !str.startsWith(strings[0]));
			return list;
		}

		return new ArrayList<>();
	}
}
