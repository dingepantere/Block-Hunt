package fr.dingepantere.search;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.dingepantere.search.other.inventory;

public class showblock implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;

			Main.page.put(p, 1);
			
			Inventory inv = inventory.getInventory("blocklist", p);
			
			p.openInventory(inv);
		} else {
			for(Location loc : Main.blocktofind) {
				String w = loc.getWorld().getName();
				int x = loc.getBlockX();
				int y = loc.getBlockY();
				int z = loc.getBlockZ();
				sender.sendMessage("World : "+w+"\n"
						+ "X: " +x +"   Y: "+y +"   Z: "+z);
			}
		}
		return false;
	}

}
