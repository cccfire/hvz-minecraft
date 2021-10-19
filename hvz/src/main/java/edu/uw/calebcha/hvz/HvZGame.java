package edu.uw.calebcha.hvz;

/**
 * Represents a game of Humans vs Zombies.
 * @author caleb
 *
 */
public class HvZGame {

	private HvZPlayerRegistry playerRegistry;
	private HvZGraveyard graveyard;
	
	/**
	 * Initializes the HvZGame
	 */
	public HvZGame() {
		this.playerRegistry = new HvZPlayerRegistry();
		this.graveyard = new HvZGraveyard();
	}
	
	/**
	 * Initializes the HvZGame with existing players.
	 * <p>
	 * Mostly used for when humans win and decide to continue the game
	 * with the same remaining players as humans.
	 * 
	 * @param playerRegistry existing player registry from previous game
	 * @param graveyard      existing graveyard from previous game
	 */
	public HvZGame(HvZPlayerRegistry playerRegistry, HvZGraveyard graveyard) {
		this.playerRegistry = playerRegistry;
		this.graveyard = graveyard;
	}
	
	/**
	 * Starts the game.
	 */
	public void start() {
		
	}
}
