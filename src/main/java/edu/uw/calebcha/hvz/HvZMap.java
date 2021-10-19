package edu.uw.calebcha.hvz;

import org.bukkit.util.Vector;

/**
 * Represents a map that can be used in Humans vs Zombies
 * @author Caleb Chan (cccfire)
 *
 */
public class HvZMap {
	private String path;
	private Vector humanSpawn;
	private Vector zombieSpawn;
	
	/**
	 * Constructor
	 * @param path   path of worldedit schem of the map
	 * @param hSpawn position vector for human spawn location
	 * @param zSpawn position vector for zombie spawn location
	 */
	public HvZMap(String path, Vector hSpawn, Vector zSpawn) {
		this.path = path;
		this.humanSpawn = hSpawn;
		this.zombieSpawn = zSpawn;
	}
	
	/**
	 * Gets the path of the worldedit schem
	 * @return path of worldedit schem
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Gets where zombies spawn on this map.
	 * @return zombie spawn position vector
	 */
	public Vector getZombieSpawn() {
		return this.zombieSpawn;
	}
	
	/**
	 * Gets where humans spawn on this map.
	 * @return human spawn position vector
	 */
	public Vector getHumanSpawn() {
		return this.humanSpawn;
	}
}
