package fr.dingepantere.search.other;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.dingepantere.search.Main;

@SuppressWarnings("deprecation")
public class Item {

	public static ItemStack getItemStack(String name, Player p) {

		if (name.equalsIgnoreCase("no")) {
			ItemStack no = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
			ItemMeta Mno = no.getItemMeta();
			Mno.setDisplayName("§c§l");
			no.setItemMeta(Mno);
			return no;
		}
		if (name.equalsIgnoreCase("suivant")) {
			ItemStack suivant = new ItemStack(Material.ARROW, 1);
			ItemMeta Msuivant = suivant.getItemMeta();
			Msuivant.setDisplayName("§rNext Page");
			suivant.setItemMeta(Msuivant);
			return suivant;
		}
		if (name.equalsIgnoreCase("precedent")) {
			ItemStack precedent = new ItemStack(Material.ARROW, 1);
			ItemMeta Mprecedent = precedent.getItemMeta();
			Mprecedent.setDisplayName("§rPrevious Page");
			precedent.setItemMeta(Mprecedent);
			return precedent;
		}
		if (name.equalsIgnoreCase("page")) {
			int nbpage = Main.blocktofind.size() / 28 + 1;
			if (Main.blocktofind.size() % 28 == 0) {
				nbpage = nbpage - 1;
			}
			if(nbpage == 0) {
				nbpage++;
			}

			ItemStack page = new ItemStack(Material.BLACK_CONCRETE, 1);
			ItemMeta Mpage = page.getItemMeta();
			Mpage.setDisplayName("§rPage " + Main.page.get(p) + "/" + nbpage);
			page.setItemMeta(Mpage);
			return page;
		}

		return null;
	}
}
