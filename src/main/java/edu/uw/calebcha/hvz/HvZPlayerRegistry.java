package edu.uw.calebcha.hvz;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

/**
 * A structure storing all Humans vs Zombies player data.
 * @author Caleb Chan (cccfire)
 *
 */
public class HvZPlayerRegistry {
	
	//private static final HvZPlayerRegistry INSTANCE = new HvZPlayerRegistry();
	
	private Map<Player, HvZPlayer> playermap = new HashMap<>();
	
	public HvZPlayerRegistry() {
		
	}
	
	/*
	 * Gets the singleton instance for HvZPlayerRegistry.
	 * @return this class's singleton instance
	 */
	/*
	public static HvZPlayerRegistry getInstance() {
		return HvZPlayerRegistry.INSTANCE;
	}
	*/

	/**
	 * Gets the map storing the HvZPlayers
	 * @return map of Players to HvZPlayers
	 */
	public Map<Player, HvZPlayer> getPlayermap() {
		return playermap;
	}
	
	/**
	 * Puts entry Player, HvZPlayer to registry.
	 * @param hvzPlayer HvZPlayer being put into registry.
	 */
	public void put(HvZPlayer hvzPlayer) {
		playermap.put(hvzPlayer.getPlayer(), hvzPlayer);
	}
	
	/**
	 * Checks if Bukkit Player is in registry.
	 * @param player Bukkit Player to query for
	 * @return true if player is in registry, false otherwise
	 */
	public boolean contains(Player player) {
		return playermap.containsKey(player);
	}
	
	/**
	 * Checks if HvZPlayer is in registry.
	 * @param player HvZPlayer to query for
	 * @return true if player is in registry, false otherwise
	 */
	public boolean contains(HvZPlayer player) {
		return playermap.containsValue(player);
	}
}
