package fr.dingepantere.search;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dingepantere.search.other.Message;

public class placer implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!Main.placerman.contains(p)) {
				Main.placerman.add(p);
				Message.sendMessage(p, "cmd.cannowplace");
				if(Main.Classementfinish.size() != 0) {
					Message.sendMessage(p, "cmd.placerwarmfinish");
				}
			} else {
				Main.placerman.remove(p);
				Message.sendMessage(p, "cmd.cantnowplace");
			}
		} else {
			sender.sendMessage(Message.get("cubaraibe.cmdcant"));
		}
		return false;
	}

}
