package edu.uw.calebcha.hvz;

import java.util.ArrayList;
import java.util.List;


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
	public GameRegistry getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Gets GameRegistry's stored games.
	 * @return GameRegistry's stored games
	 */
	public List<HvZGame> getGames() {
		return games;
	}

}
