package edu.uw.calebcha.hvz;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;


/**
 * Stores all active HvZ games.
 * Singleton.
 * @author Caleb Chan (cccfire)
 *
 */
public class GameRegistry {
	
	private static final GameRegistry INSTANCE = new GameRegistry();
	
	private List<HvZGame> games = new ArrayList<>();
	
	private GameRegistry() {
		
	}
	
	/**
	 * Get GameRegistry's singleton instance.
	 * @return GameRegistry instance
	 */
	public static GameRegistry getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Gets GameRegistry's stored games.
	 * @return GameRegistry's stored games
	 */
	public List<HvZGame> getGames() {
		return games;
	}
	
	/**
	 * Search for the game that a given player is in.
	 * @param player player to search for
	 * @return game the player is in or null if player is not in a game
	 */
	public HvZGame searchPlayer(Player player) {
		for (HvZGame game : games) {
			if (game.getPlayers().contains(player)) {
				return game;
			}
		}
		return null;
	}
	
	/**
	 * Gets if player is in game.
	 * @param player player to search for
	 * @return true if player is in a game, false otherwise
	 */
	public boolean isPlayerInGame(Player player) {
		return searchPlayer(player) != null;
	}

}
