package edu.uw.calebcha.hvz;

import org.bukkit.entity.Player;

/**
 * Represents a Humans vs Zombies player.
 * Wrapper class of org.bukkit.entity.Player
 * @author Caleb Chan (cccfire)
 *
 */
public abstract class HvZPlayer {
    private Player player;
	private HvZGame game;
	
	/**
	 * Creates a Humans vs Zombies player from a Bukkit Player.
	 * @param player the Bukkit Player joining the game.
	 */
	public HvZPlayer(Player player, HvZGame game) {
		this.player = player;
		this.game = game;
		this.game.getPlayerRegistry().put(this);
	}

	/**
	 * Get Bukkit Player corresponding to this HvZPlayer. 
	 * @return corresponding Bukkit Player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Get the game this HvZPlayer is in.
	 * @return the game this player is in
	 */
	public HvZGame getGame() {
		return game;
	}
	
	/**
	 * Equips the player with starter items.
	 */
	public abstract void loadout();
}
