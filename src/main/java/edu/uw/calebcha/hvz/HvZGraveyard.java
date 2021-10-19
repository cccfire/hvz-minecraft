package edu.uw.calebcha.hvz;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

/**
 * Stores data for dead HumanPlayers.
 * For end-of-game stat-tracking purposes.
 * @author Caleb Chan (cccfire)
 *
 */
public class HvZGraveyard {
	//private static final HvZGraveyard INSTANCE = new HvZGraveyard();
	
	private Map<Player, HumanPlayer> playermap = new HashMap<>();
	
	public HvZGraveyard() {
		
	}
	
	/*
	 * Gets the singleton instance for HvZGraveyard.
	 * @return this class's singleton instance
	 */
	/*
	public static HvZGraveyard getInstance() {
		return HvZGraveyard.INSTANCE;
	}
	*/

	/**
	 * Gets the map storing the HumanPlayers
	 * @return map of Players to HumanPlayers
	 */
	public Map<Player, HumanPlayer> getPlayermap() {
		return playermap;
	}
	
	/**
	 * Puts entry Player, HumanPlayer to registry.
	 * @param humanPlayer HumanPlayer being put into registry.
	 */
	public void put(HumanPlayer humanPlayer) {
		playermap.put(humanPlayer.getPlayer(), humanPlayer);
	}
	
	/**
	 * Checks if Bukkit Player is in graveyard.
	 * @param player Bukkit Player to query for
	 * @return true if player is in graveyard, false otherwise
	 */
	public boolean contains(Player player) {
		return playermap.containsKey(player);
	}
	
	/**
	 * Checks if HumanPlayer is in graveyard.
	 * @param player HumanPlayer to query for
	 * @return true if player is in graveyard, false otherwise
	 */
	public boolean contains(HumanPlayer player) {
		return playermap.containsValue(player);
	}
}
