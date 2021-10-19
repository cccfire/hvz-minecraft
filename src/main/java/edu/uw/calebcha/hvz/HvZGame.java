package edu.uw.calebcha.hvz;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

/**
 * Represents a game of Humans vs Zombies.
 * @author Caleb Chan (cccfire)
 *
 */
public class HvZGame implements Listener {

	private HvZPlayerRegistry playerRegistry;
	private HvZGraveyard graveyard;
	private List<Player> gamers = new ArrayList<>();
	private String creatorName;	
	private int timeLimit = 12000;
	
	/**
	 * Initializes the HvZGame with existing players.
	 * <p>
	 * Mainly used for when humans win and decide to continue the game
	 * with the same remaining players as humans.
	 * 
	 * @param name           name of the player who created this game
	 * @param map            the map used in this game
	 * @param playerRegistry existing player registry from previous game
	 * @param graveyard      existing graveyard from previous game
	 */
	public HvZGame(String name, HvZMap map, HvZPlayerRegistry playerRegistry, HvZGraveyard graveyard) {
		this.creatorName = name;
		this.playerRegistry = playerRegistry;
		this.graveyard = graveyard;
	}
	
	/**
	 * Initializes the HvZGame
	 * 
	 * @param name name of the player who created this game
	 * @param map            the map used in this game
	 */
	public HvZGame(String name, HvZMap map) {
		this(name, map, new HvZPlayerRegistry(), new HvZGraveyard());
	}
	
	/**
	 * Get the name of player who created this game.
	 * Mainly used for distinguishing games when listing them in a menu.
	 * 
	 * @return name of player who created this game
	 */
	public String getCreatorName() {
		return creatorName;
	}
	
	/**
	 * Returns number of players
	 * @return number of players
	 */
	public int size() {
		return gamers.size();
	}
	
	/**
	 * Adds player to this HvZ game
	 * @param player player to be added to game
	 */
	public void add(Player player) {
		gamers.add(player);
	}
	
	/**
	 * Removes given player from this HvZ game
	 * @param player player to be removed from game
	 */
	public void remove(Player player) {
		gamers.remove(player);
		if (playerRegistry.contains(player)) 
			playerRegistry.getPlayermap().remove(player);
		if (graveyard.contains(player))
			graveyard.getPlayermap().remove(player);
	}
	
	/**
	 * Starts countdown to start the game.
	 */
	public void countdown() {
		new BukkitRunnable() {
			public void run() {
				start();
			}
		}.runTaskLater(HvZ.plugin, 200);
	}
	
	/**
	 * Starts this game.
	 */
	public void start() {
		new BukkitRunnable() {
			public void run() {
				endGame();
			}
		}.runTaskLater(HvZ.plugin, getTimeLimit());
	}
	
	/**
	 * Ends the game.
	 */
	public void endGame() {
		boolean humansWon = winnerIsHuman();
		
		for (HvZPlayer player : playerRegistry.getPlayermap().values()) {
			if (humansWon) {
				if (player instanceof HumanPlayer) {
					player.getPlayer().sendTitle(ChatColor.DARK_BLUE + "VICTORY!", ChatColor.DARK_BLUE + "the humans have won!", 10, 70, 20);
				}else if (player instanceof ZombiePlayer) {
					player.getPlayer().sendTitle(ChatColor.DARK_RED + "DEFEAT!", ChatColor.DARK_RED + "the humans have won!", 10, 70, 20);
				} 
			}else {
				if (player instanceof HumanPlayer) {
					// this actually never happens rn lmao bruh
					player.getPlayer().sendTitle(ChatColor.DARK_RED + "DEFEAT!", ChatColor.DARK_RED + "the zombies have won!", 10, 70, 20);			
				}else if (player instanceof ZombiePlayer) {
					player.getPlayer().sendTitle(ChatColor.DARK_BLUE + "VICTORY!", ChatColor.DARK_BLUE + "the zombies have won!", 10, 70, 20);
				} 
			}
			player.getPlayer().teleport(HvZ.mainland.getSpawnLocation());
		}
	}
	
	/**
	 * Returns whether the humans win.
	 * To be overridden for missions with different win conditions.
	 * @return true if humans win, false otherwise
	 */
	public boolean winnerIsHuman() {
		// TODO: If game is not over throw an exception.
		for (HvZPlayer player : playerRegistry.getPlayermap().values()) {
			if (player instanceof HumanPlayer) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets this game's player registry.
	 * @return this HvZ game's player registry
	 */
	public HvZPlayerRegistry getPlayerRegistry() {
		return playerRegistry;
	}
	
	/**
	 * Gets this game's graveyard.
	 * @return this HvZ game's graveyard
	 */
	public HvZGraveyard getGraveyard() {
		return graveyard;
	}
	
	/**
	 * Sets the time limit for this game
	 * @param timeLimit value of time limit
	 */
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	/**
	 * Gets the time limit for this game
	 * @return time limit of this game
	 */
	public int getTimeLimit() {
		return this.timeLimit;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(gamers.contains(event.getPlayer()))
			remove(event.getPlayer());
	}
}
