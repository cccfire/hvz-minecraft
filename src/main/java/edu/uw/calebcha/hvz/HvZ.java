package edu.uw.calebcha.hvz;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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
	
	public static final String BASE_PATH = "plugins/HvZ/worlds/";
	
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
	}

	/**
	 * Get this plugin's NPC registry
	 * @return Citizens NPC registry for this plugin
	 */
	public NPCRegistry getRegistry() {
		return registry;
	}
	
}
