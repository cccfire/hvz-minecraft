package edu.uw.calebcha.hvz;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

/**
 * Represents a zombie player in Humans vs Zombies.
 * @author Caleb Chan (cccfire)
 *
 */
public class ZombiePlayer extends HvZPlayer {
	
	private boolean koed = false;
	private long kotime = 0;
	
	/**
	 * Creates ZombiePlayer wrapping a Bukkit Player.
	 * @param player Bukkit Player to be wrapped
	 */
	public ZombiePlayer(Player player, HvZGame game) {
		super(player, game);
	}
	
	/**
	 * Creates a ZombiePlayer from a HumanPlayer.
	 * @param player HumanPlayer to be turned into ZombiePlayer.
	 */
	public ZombiePlayer(HumanPlayer player) {
		this(player.getPlayer(), player.getGame());
	}
	
	public void loadout() {
		
	}
	
	/**
	 * Called when a Zombie Player is knocked out by a Human
	 */
	public void knock() {
		koed = true;
		kotime = System.currentTimeMillis();
		this.getPlayer().setGameMode(GameMode.SPECTATOR);
		final BossBar bossbar = Bukkit.createBossBar("Time to recover", BarColor.BLUE, BarStyle.SOLID);
		bossbar.setProgress(1);
		bossbar.addPlayer(this.getPlayer());
		
		new BukkitRunnable() {
			double progress = 300.0;
			public void run() {
				progress -= 1.0;
				bossbar.setProgress(progress/300.0);
				if (progress < 0) {
					bossbar.removeAll();
					getPlayer().sendMessage(ChatColor.BLUE + "Right click a door to recover!");
				}
			}
		}.runTaskTimer(HvZ.plugin, 0, 1);
	}
	
	/**
	 * Called when a Zombie Player recovers from their knock-out
	 */
	public void recover() {
		koed = false;
		this.getPlayer().setGameMode(GameMode.SURVIVAL);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() == this.getPlayer()) {
			if (event.getDamager() instanceof Projectile) {
				ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
				if (shooter instanceof Player 
				 && this.getGame().getPlayerRegistry().getPlayermap().containsKey(shooter)) {
					if (this.getGame().getPlayerRegistry().getPlayermap().get(shooter) instanceof ZombiePlayer) {
					    event.setCancelled(true);
					} else if (this.getGame().getPlayerRegistry().getPlayermap().get(shooter) instanceof HumanPlayer) {
						knock();
						this.getPlayer().sendTitle(ChatColor.RED + "You have been knocked out!", 
								                   ChatColor.RED + "Wait for the timer to end and right-click a door to recover", 10, 70, 20);
					}
				}
			}else if (event.getDamager() instanceof Player
				   && this.getGame().getPlayerRegistry().getPlayermap().containsKey(event.getDamager())) {
				if (this.getGame().getPlayerRegistry().getPlayermap().get(event.getDamager()) instanceof ZombiePlayer) {
				    event.setCancelled(true);	   
				}else if (this.getGame().getPlayerRegistry().getPlayermap().get(event.getDamager()) instanceof HumanPlayer) {
					knock();
					this.getPlayer().sendTitle(ChatColor.RED + "You have been knocked out!", 
							                   ChatColor.RED + "Wait for the timer to end and right-click a door to recover", 10, 70, 20);
				}
		    }
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (koed && System.currentTimeMillis() - kotime > 15000 && event.getPlayer() == this.getPlayer()) {
			if (event.getClickedBlock() != null && 
				(event.getClickedBlock().getType().equals(Material.ACACIA_DOOR) 
			  || event.getClickedBlock().getType().equals(Material.CRIMSON_DOOR)
			  || event.getClickedBlock().getType().equals(Material.BIRCH_DOOR)
			  || event.getClickedBlock().getType().equals(Material.DARK_OAK_DOOR)
			  || event.getClickedBlock().getType().equals(Material.JUNGLE_DOOR)
			  || event.getClickedBlock().getType().equals(Material.SPRUCE_DOOR)
			  || event.getClickedBlock().getType().equals(Material.OAK_DOOR)
			  || event.getClickedBlock().getType().equals(Material.IRON_DOOR))) {
				recover();
			}
		}
	}
}
