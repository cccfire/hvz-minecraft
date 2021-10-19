package edu.uw.calebcha.hvz;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;

/**
 * Main plugin class for Humans vs Zombies
 * @author Caleb Chan (cccfire)
 *
 */
public class HvZ extends JavaPlugin implements Listener {
	
	public static final String BASE_PATH = "plugins/HvZ/schematics/";
	
	// in theory there should never be more than one HvZ instance at one time.
	public static HvZ plugin;
	public static World mainland;
	
	private NPCRegistry registry;
	
	
	@Override
	public void onEnable() {
		plugin = this;
		mainland = getServer().getWorlds().get(0);
		this.registry = CitizensAPI.createNamedNPCRegistry("hvz", new MemoryNPCDataStore());
		getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("hvz").setExecutor(new HvZCommandExecutor());
	}
	
	@Override
	public void onDisable() {
		for (HvZGame game : GameRegistry.getInstance().getGames()) {
			game.cleanup();
		}
	}

	@EventHandler
	public void onWorldInit(WorldInitEvent event) {
		Bukkit.getLogger().log(Level.INFO, "hi");
		event.getWorld().setKeepSpawnInMemory(false);
	}
	
	/**
	 * Get this plugin's NPC registry
	 * @return Citizens NPC registry for this plugin
	 */
	public NPCRegistry getRegistry() {
		return registry;
	}

	
	
	public static Location findFirstAir(Location location) {	
		int Y;
		if(location.getWorld().getBlockAt(location).getType() == Material.AIR) { 
			for (Y = location.getWorld().getMaxHeight()-1; Y > 0 && location.getWorld().getBlockAt(location.getBlockX(), Y, location.getBlockZ()).getType() == Material.AIR; Y--);
			Y++;
		}else {
			for(Y = location.getBlockY(); Y < location.getWorld().getMaxHeight() && location.getWorld().getBlockAt(location.getBlockX(), Y, location.getBlockZ()).getType() != Material.AIR; Y++); 
		}
		//System.out.println("X: " + location.getBlockX() + "; Y: " + Y + "; Z: " + location.getBlockZ());
		return new Location(location.getWorld(), location.getBlockX(), Y, location.getBlockZ());
	}
	
}
