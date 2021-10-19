package edu.uw.calebcha.hvz;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;


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
	private HvZMap map;
	private int timeLimit = 12000;
	private World gameWorld;
	private World lobbyWorld;
	private boolean started = false;
	private BukkitRunnable endTimer = null;
	private NPC zombieTeam;
	private NPC humanTeam;
	private NPCRegistry npcRegistry;
	
	
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
		this.map = map;
		this.creatorName = name;
		this.playerRegistry = playerRegistry;
		this.graveyard = graveyard;
		UUID uuid = UUID.randomUUID();
		this.gameWorld = Bukkit.createWorld(new WorldCreator(uuid.toString() + "_hvzgame").generator(new VoidGenerator()));
		this.lobbyWorld = Bukkit.createWorld(new WorldCreator(uuid.toString() + "_hvzlobby").generator(new VoidGenerator()));
		this.npcRegistry = CitizensAPI.createNamedNPCRegistry(uuid.toString(), new MemoryNPCDataStore());
		this.humanTeam = npcRegistry.createNPC(EntityType.PLAYER, "Human Team");
		this.zombieTeam = npcRegistry.createNPC(EntityType.ZOMBIE, "Zombie Team");
		/*
		new BukkitRunnable() {
			public void run() {
				Clipboard clipboard;
				File file = new File(HvZ.BASE_PATH + "zomblobby.schem");
				ClipboardFormat format = ClipboardFormats.findByFile(file);
				try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
					
				    clipboard = reader.read();
				    
				    Region region = clipboard.getRegion();
				    BlockArrayClipboard proxclip = new BlockArrayClipboard(region);

				    try (EditSession editSession = new EditSessionBuilder(new BukkitWorld(lobbyWorld)).build()) {

				    	ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
				            editSession, region, proxclip, region.getMinimumPoint()
				        );
				        // configure here
				        Operations.complete(forwardExtentCopy);
				        
				        Operation operation = new ClipboardHolder(clipboard)
				                .createPaste(editSession)
				                .to(BlockVector3.at(0, 50, 0))
				                .ignoreAirBlocks(true)
				                    // configure here
				                .build();
				        Operations.complete(operation);
				        
				        
				        reader.close();
				        
				        
				        
				        
				    }catch(Exception ex) {
				    	System.out.println(ex);
				    }
				  
				}catch(IOException ex) {
					System.out.println("error");
					System.out.println(ex);
				}
			}
		}.runTaskAsynchronously(HvZ.plugin);
		*/
		
		new BukkitRunnable() {
			public void run() {
				humanTeam.spawn(new Location(lobbyWorld, 14, 52, -13));
		        zombieTeam.spawn(new Location(lobbyWorld, 15, 52, 12));
			}
		}.runTaskLater(HvZ.plugin, 100);
		
		GameRegistry.getInstance().getGames().add(this);
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
	 * Get list of players in this game.
	 * @return list of players of this game
	 */
	public List<Player> getPlayers() {
		return gamers;
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
	public void add(final Player player) {
		gamers.add(player);
		if (started) {
			player.teleport(HvZ.findFirstAir(new Location(gameWorld, this.map.getHumanSpawn().getX(), 50, this.map.getHumanSpawn().getZ())));
			new HumanPlayer(player, this);
		}else {
			new BukkitRunnable() {
				public void run() {
					player.teleport(new Location(lobbyWorld, 0, 52, 0));
				}
			}.runTaskLater(HvZ.plugin, 40);
		}
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
		started = true;
		
		endTimer = new BukkitRunnable() {
			public void run() {
				endGame();
			}
		};
		endTimer.runTaskLater(HvZ.plugin, getTimeLimit());
		
		for (Player player : gamers) {
			if (!playerRegistry.contains(player))
				new HumanPlayer(player, this);
		}
		
		Clipboard clipboard;
		File file = new File(this.map.getPath());
		ClipboardFormat format = ClipboardFormats.findByFile(file);
		try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
			
		    clipboard = reader.read();
		    
		    Region region = clipboard.getRegion();
		    BlockArrayClipboard proxclip = new BlockArrayClipboard(region);

		    try (EditSession editSession = new EditSessionBuilder(new BukkitWorld(gameWorld)).build()) {

		    	ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
		            editSession, region, proxclip, region.getMinimumPoint()
		        );
		        // configure here
		        Operations.complete(forwardExtentCopy);
		        
		        Operation operation = new ClipboardHolder(clipboard)
		                .createPaste(editSession)
		                .to(BlockVector3.at(0, 50, 0))
		                .ignoreAirBlocks(true)
		                    // configure here
		                .build();
		        Operations.complete(operation);
		        
		        
		        reader.close();
		        
		        
		    }catch(Exception ex) {
		    	System.out.println(ex);
		    }
		  
		}catch(IOException ex) {
			System.out.println("error");
			System.out.println(ex);
		}
		
		for (HvZPlayer player : playerRegistry.getPlayermap().values()) {
			if (player instanceof HumanPlayer) {
				player.getPlayer().teleport(HvZ.findFirstAir(new Location(gameWorld, this.map.getHumanSpawn().getX(), 50, this.map.getHumanSpawn().getZ())));
			}else if (player instanceof ZombiePlayer) {
				player.getPlayer().teleport(HvZ.findFirstAir(new Location(gameWorld, this.map.getZombieSpawn().getX(), 50, this.map.getZombieSpawn().getZ())));
			}
		}
	}
	
	/**
	 * Ends the game.
	 */
	public void endGame() {
		if (endTimer != null) {
			endTimer.cancel();
		}
		
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
		
		cleanup();
		GameRegistry.getInstance().getGames().remove(this);
	}
	
	/**
	 * Cleans up files at the end of the game.
	 */
	public void cleanup() {
		Bukkit.unloadWorld(gameWorld, false);
		Bukkit.unloadWorld(lobbyWorld, false);
		deleteWorld(lobbyWorld.getWorldFolder());
		deleteWorld(gameWorld.getWorldFolder());
	}
	
	
	/**
	 * Delete world directory
	 * @param path File object representing folder
	 * @return true on successful deletion, false otherwise
	 */
	public boolean deleteWorld(File path) {
	      if(path.exists()) {
	          File files[] = path.listFiles();
	          for(int i=0; i<files.length; i++) {
	              if(files[i].isDirectory()) {
	                  deleteWorld(files[i]);
	              } else {
	                  files[i].delete();
	              }
	          }
	      }
	      return(path.delete());
	}
	
	/**
	 * Returns whether the humans win.
	 * To be overridden for missions with different win conditions.
	 * @return true if humans win, false otherwise
	 */
	public boolean winnerIsHuman() {
		// TODO: If game is not over throw an exception.
		return areHumansLeft();
	}
	
	/**
	 * Gets whether there are humans left.
	 * @return true if there are humans left, false otherwise
	 */
	public boolean areHumansLeft() {
		for (HvZPlayer player : playerRegistry.getPlayermap().values()) {
			if (player instanceof HumanPlayer) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks for a zombie win.
	 * This is when there are no humans left.
	 */
	public void checkZombieWin() {
		if (!areHumansLeft()) {
			endGame();
		}
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
		
		if(gamers.size() == 1 && started == true) {
			gamers.get(0).teleport(HvZ.mainland.getSpawnLocation());
			cleanup();
		}
			
		
		if(gamers.size() == 0)
			cleanup();
	}
}
