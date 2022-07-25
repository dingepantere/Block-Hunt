package fr.dingepantere.search;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.dingepantere.search.other.Message;

public class Ranking implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

		Main.updatetop3();
		int c = 0;
		while (c != Main.Classement.size()) {
			sender.sendMessage(Message.get("cmd.ranking", Integer.toString(c + 1), Main.Classement.get(c)));
			c++;
		}
		return false;
	}

}
