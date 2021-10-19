package edu.uw.calebcha.hvz;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HvZCommandExecutor implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("hvz") && sender instanceof Player) { // If the player typed /basic then do the following...
            if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					List<HvZGame> games = GameRegistry.getInstance().getGames();
					sender.sendMessage(ChatColor.AQUA + "Current Games:");
					sender.sendMessage(ChatColor.AQUA + "--------------------");
				    for (int i = 0; i < games.size(); i++) {
				    	HvZGame game = games.get(i);
				    	sender.sendMessage("Game " + i + "Players: " + game.size() + " Created by " + game.getCreatorName());
				    }
				}else if (args[0].equalsIgnoreCase("new")) {
					HvZGame game = new HvZGame(sender.getName(), new HvZMap("", new Vector(30, 0, 30), new Vector(0, 0, 0)));
					game.add((Player) sender);
				}
				return true;
			}
            else if (args.length == 2) {
            	if (args[0].equalsIgnoreCase("join")) {
            		List<HvZGame> games = GameRegistry.getInstance().getGames();
            		try {
						if (Integer.parseInt(args[1]) < games.size()) {
							HvZGame game = games.get(Integer.parseInt(args[1]));
							game.add((Player) sender);
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "That game doesn't exist!");
						}
            		}catch (NumberFormatException ex) {
            			System.out.println(ex);
            		}
				}
            }
			
			
			
			
			return true;
		 
		
		}
		return false;
	}

}
