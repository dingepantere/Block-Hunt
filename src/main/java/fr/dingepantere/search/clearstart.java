package fr.dingepantere.search;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.dingepantere.search.other.Message;

public class clearstart implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (args.length > 0) {
			if (Main.finded.get(args[0]) != null) {
				sender.sendMessage(Message.get("cmd.playerfindedclear", args[0]));
			}
			
		} else {
			Main.finded.clear();
			sender.sendMessage(Message.get("cmd.listfindedclear"));
		}
		Main.updatetop3();
		return false;
	}

}
