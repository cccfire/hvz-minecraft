package edu.uw.calebcha.hvz;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

import net.md_5.bungee.api.ChatColor;

/**
 * Represents a human player in Humans vs Zombies.
 * @author Caleb Chan (cccfire)
 *
 */
public class HumanPlayer extends HvZPlayer implements Listener{
	
	/**
	 * Creates HumanPlayer wrapping a Bukkit Player.
	 * @param player Bukkit Player to be wrapped
	 * @param game          the game this player is in
	 */
	public HumanPlayer(Player player, HvZGame game) {
		super(player, game);
	}
	

	public void loadout() {
		
	}
	
	/**
	 * Turn into a ZombiePlayer.
	 */
	public void turn() {
		this.getGame().getGraveyard().put(this);
		ZombiePlayer zombiePlayer = new ZombiePlayer(this);
		zombiePlayer.knock();
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() == this.getPlayer()) {
			if (event.getDamager() instanceof Projectile) {
				ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
				if (shooter instanceof Player 
				 && this.getGame().getPlayerRegistry().getPlayermap().containsKey(shooter)) {
					if (this.getGame().getPlayerRegistry().getPlayermap().get(shooter) instanceof HumanPlayer) {
					    event.setCancelled(true);
					} else if (this.getGame().getPlayerRegistry().getPlayermap().get(shooter) instanceof ZombiePlayer) {
						turn();
						this.getPlayer().sendTitle(ChatColor.RED + "You have been turned into a zombie!", 
		                    ChatColor.RED + "Wait for the timer to end and right-click a door to turn into a zombie", 10, 70, 20);
					}
				}
			}else if (event.getDamager() instanceof Player
				   && this.getGame().getPlayerRegistry().getPlayermap().containsKey(event.getDamager())) {
				if (this.getGame().getPlayerRegistry().getPlayermap().get(event.getDamager()) instanceof HumanPlayer) {
				    event.setCancelled(true);	   
				}else if (this.getGame().getPlayerRegistry().getPlayermap().get(event.getDamager()) instanceof ZombiePlayer) {
					turn();
					this.getPlayer().sendTitle(ChatColor.RED + "You have been turned into a zombie!", 
			            ChatColor.RED + "Wait for the timer to end and right-click a door to turn into a zombie", 10, 70, 20);
				}
		    }
		}
	}

}
