package fr.dingepantere.search.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.dingepantere.search.Main;

@SuppressWarnings("deprecation")
public class inventory {

	static ArrayList<Integer> emp = new ArrayList<Integer>(Arrays.asList(
			10, 11, 12, 13, 14, 15, 16, 
			19, 20, 21, 22, 23, 24,	25,
			28, 29, 30, 31, 32, 33, 34,
			37, 38, 39, 40, 41, 42, 43));

	public static Inventory getInventory(String name, Player p) {
		ItemStack no = Item.getItemStack("no", p);
		ItemStack suivant = Item.getItemStack("suivant", p);
		ItemStack precedent = Item.getItemStack("precedent", p);
		ItemStack page = Item.getItemStack("page", p);

		Inventory inv = (Inventory) Bukkit.createInventory(null, 54, "§rList Block");

		inv.setItem(0, no);
		inv.setItem(1, no);
		inv.setItem(2, no);
		inv.setItem(3, no);
		inv.setItem(4, no);
		inv.setItem(5, no);
		inv.setItem(6, no);
		inv.setItem(7, no);
		inv.setItem(8, no);

		inv.setItem(9, no);
		inv.setItem(17, no);

		inv.setItem(18, no);
		inv.setItem(26, no);

		inv.setItem(27, no);
		inv.setItem(35, no);

		inv.setItem(36, no);
		inv.setItem(44, no);

		inv.setItem(45, precedent);
		inv.setItem(53, suivant);

		inv.setItem(49, page);

		int a = 28 * (Main.page.get(p) - 1);
		int b = a + 28;

		while (a != b) {
			Location loc;
			try {
				loc = Main.blocktofind.get(a);
			} catch (Exception e) {
				break;
			}
			Block bloc = null;
			try {
				bloc = loc.getBlock();
			} catch (Exception e) {
			}

			ItemStack item;
			
			if(bloc == null) {
				item = new ItemStack(Material.BARRIER, 1);
			} else {
				item = new ItemStack(bloc.getType(), 1);
			}
			
			if (item == null || item.getType().equals(Material.AIR) || item.getType().equals(Material.CAVE_AIR) || item.getType().equals(Material.VOID_AIR)) {
				item = new ItemStack(Material.BARRIER, 1);
				ItemMeta Mitem = item.getItemMeta();
				Mitem.setDisplayName("§rAIR");
				item.setItemMeta(Mitem);
			}
			
			if (item.getType().equals(Material.PLAYER_HEAD) || item.getType().equals(Material.PLAYER_WALL_HEAD)) {
				Collection<ItemStack> drop = bloc.getDrops();
				for (ItemStack item2 : drop) {
					if (item2.getType() == Material.PLAYER_HEAD) {
						item = item2;
					}
				}
			}
			
			if (item.getType().equals(Material.WATER)) {
				item = new ItemStack(Material.WATER_BUCKET, 1);
			}
			
			if (item.getType().equals(Material.LAVA)) {
				item = new ItemStack(Material.LAVA_BUCKET, 1);
			}
			
			if (item.getType().equals(Material.TRIPWIRE)) {
				item = new ItemStack(Material.STRING, 1);
			}
			
			if (item.getType().equals(Material.REDSTONE_WIRE)) {
				item = new ItemStack(Material.REDSTONE, 1);
			}
			
			if (item.getType().equals(Material.SWEET_BERRY_BUSH)) {
				item = new ItemStack(Material.SWEET_BERRIES, 1);
			}

			ItemMeta Mitem = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§r§eWorld: §f" + loc.getWorld().getName());
			lore.add("§r§eX: §f" + loc.getBlockX());
			lore.add("§r§eY: §f" + loc.getBlockY());
			lore.add("§r§eZ: §f" + loc.getBlockZ());
			lore.add(" ");
			lore.add("§r§eLeft Click: §fRemove block from list");
			lore.add("§r§eRight Click: §fTeleporte to the block");
			lore.add("§r§eShift Left Click: §fRemove block in the world and in list");
			Mitem.setLore(lore);
			item.setItemMeta(Mitem);

			inv.setItem(emp.get(a % 28), item);

			a++;
		}

		if (name.equalsIgnoreCase("blocklist")) {
			return inv;
		}

		return null;
	}
}
