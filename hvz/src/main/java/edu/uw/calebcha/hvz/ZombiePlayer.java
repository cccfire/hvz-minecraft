package edu.uw.calebcha.hvz;

import org.bukkit.entity.Player;

/**
 * Represents a zombie player in Humans vs Zombies.
 * @author Caleb Chan (cccfire)
 *
 */
public class ZombiePlayer extends HvZPlayer {
	/**
	 * Creates ZombiePlayer wrapping a Bukkit Player.
	 * @param player Bukkit Player to be wrapped
	 */
	public ZombiePlayer(Player player, HvZGame game) {
		super(player, game);
	}
	
	/**
	 * Creates a ZombiePlayer from a HumanPlayer.
	 * @param player HumanPlayer to be turned into ZombiePlayer.
	 */
	public ZombiePlayer(HumanPlayer player) {
		this(player.getPlayer(), player.getGame());
	}
}
