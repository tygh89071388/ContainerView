package me.mrmango404.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Cuboid {

	/*
	Based on the player's location and specified half-width (int range),
	this function returns a list of blocks within the cuboid-shaped area centered on the player.
	The cuboid extends from the player's position in all directions (x, y, z) by the specified range.
	 */

	public static ArrayList<Block> getBlocks(Player player, int range) {

		World world = player.getWorld();
		ArrayList<Block> blockList = new ArrayList<>();

		int playerLocationTopX = player.getLocation().getBlockX() + range;
		int playerLocationTopY = player.getLocation().getBlockY() + range;
		int playerLocationTopZ = player.getLocation().getBlockZ() + range;

		int playerLocationBottomX = player.getLocation().getBlockX() - range;
		int playerLocationBottomY = player.getLocation().getBlockY() - range;
		int playerLocationBottomZ = player.getLocation().getBlockZ() - range;

		for (int x = playerLocationBottomX; x <= playerLocationTopX; x++) {
			for (int y = playerLocationBottomY; y <= playerLocationTopY; y++) {
				for (int z = playerLocationBottomZ; z <= playerLocationTopZ; z++) {

					Location location = new Location(world, x, y, z);
					Block block = world.getBlockAt(location);
					blockList.add(block);
				}
			}
		}

		return blockList;
	}
}
