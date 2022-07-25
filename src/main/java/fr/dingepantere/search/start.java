package fr.dingepantere.search;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dingepantere.search.other.Message;

public class start implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(Main.started) {
			Main.started = false;
			Main.getConfigCubaraibe().set("open", Main.started);
			for(Player p : Bukkit.getOnlinePlayers()) {
				Message.sendMessage(p, "cmd.searchend");
			}
		} else {
			Main.started = true;
			Main.getConfigCubaraibe().set("open", Main.started);
			for(Player p : Bukkit.getOnlinePlayers()) {
				Message.sendMessage(p, "cmd.searchstart");
				p.setScoreboard(Main.scoreboard);
			}
		}
		return false;
	}

}
