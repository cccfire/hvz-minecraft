package edu.uw.calebcha.hvz;

import org.bukkit.entity.Player;

/**
 * Represents a human player in Humans vs Zombies.
 * @author Caleb Chan (cccfire)
 *
 */
public class HumanPlayer extends HvZPlayer {
	
	/**
	 * Creates HumanPlayer wrapping a Bukkit Player.
	 * @param player Bukkit Player to be wrapped
	 */
	public HumanPlayer(Player player, HvZGame game) {
		super(player, game);
	}

}
