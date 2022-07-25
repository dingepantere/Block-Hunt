package fr.dingepantere.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import fr.dingepantere.search.other.Item;
import fr.dingepantere.search.other.Message;
import fr.dingepantere.search.other.inventory;

@SuppressWarnings({ "deprecation", "unchecked" })
public class Main extends JavaPlugin implements Listener {

	public static ArrayList<Player> placerman = new ArrayList<Player>();
	public static ArrayList<Location> blocktofind = new ArrayList<Location>();
	public static HashMap<Player, Integer> page = new HashMap<Player, Integer>();
	private static HashMap<Player, Boolean> inter = new HashMap<Player, Boolean>();

	public static boolean started;

	public static HashMap<String, ArrayList<Integer>> finded = new HashMap<String, ArrayList<Integer>>();

	public static Scoreboard scoreboard;
	public static Team team;
	public static Objective objective;

	public static ArrayList<String> Classement = new ArrayList<>();
	public static HashMap<String, Date> Classementfinish = new HashMap<String, Date>();

	static int classmentnbmax = 0;
	static int classementminscore = 0;

	String perm = "";

	@Override
	public void onEnable() {
		createConfig();

		getCommand("placer").setExecutor(new placer());
		getCommand("showblock").setExecutor(new showblock());
		getCommand("startsearch").setExecutor(new start());
		getCommand("clearsearch").setExecutor(new clearstart());
		getCommand("ranking").setExecutor(new Ranking());

		getServer().getPluginManager().registerEvents(this, this);

		getLocationList();

		getPlayerfind();

		initFinalRank();

		started = getConfigCubaraibe().getBoolean("open");

		for (Player p : Bukkit.getOnlinePlayers()) {
			inter.put(p, true);
		}

		initScoreboard();

		updatetop3();

		perm = Message.get("global.namepermshowscoreboard");

		System.out.println("\033[1;32mBlock Hunt enabled\033[0m");
	}

	private void initScoreboard() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		scoreboard = manager.getNewScoreboard();
		team = scoreboard.registerNewTeam("CubaTeam");

		objective = scoreboard.registerNewObjective("BlockSearch", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(Message.get("scoreboard.title"));

		int f = 1;

		boolean fin = true;

		while (fin) {
			try {
				Message.get("scoreboard.lf" + f);

				objective.getScore("§Z" + Message.get("scoreboard.lf" + f)).setScore(f - 1);
				f++;

			} catch (Exception e) {
				fin = false;
			}
			classementminscore = f - 1;
		}

		int r = 0;
		classmentnbmax = Integer.parseInt(Message.get("scoreboard.toprank"));

		while (r != classmentnbmax) {

			objective.getScore("§Y" + Message.get("scoreboard.lvoid")).setScore(f + r - 1);
			System.out.println((f + r) + "§Y" + Message.get("scoreboard.lvoid"));

			r++;
		}

		int d = 1;

		boolean deb = true;

		int max = 1;
		while (true) {
			try {
				Message.get("scoreboard.ld" + max);

				max++;
			} catch (Exception e) {
				break;
			}
		}

		while (deb) {
			try {
				Message.get("scoreboard.ld" + d);

				objective.getScore("§X" + Message.get("scoreboard.ld" + d)).setScore(((max - d - 1) + f + r) - 1);
				System.out.println((max - d - 1 + f + r) + "§X" + Message.get("scoreboard.ld" + d));
				d++;

			} catch (Exception e) {
				deb = false;
			}
		}
	}

	@Override
	public void onDisable() {
		saveLocationList();
		savefinded();
		saveFinalRank();
	}

	public static void updatetop3() {
		Classement.clear();
		if (finded.size() != 0) {
			if (Classementfinish.size() != 0) {

				ArrayList<String> list = new ArrayList<>(Classementfinish.keySet());

				ArrayList<String> classmentfinish = new ArrayList<>();

				int a = 0;
				while (a != list.size()) {

					if (classmentfinish.size() != 0) {

						Date date = Classementfinish.get(list.get(a));

						int b = 0;
						while (b != classmentfinish.size()) {

							if (date.getTime() < Classementfinish.get(classmentfinish.get(b)).getTime()) {
								classmentfinish.add(b, list.get(a));
								break;
							}

							if (classmentfinish.size() == b + 1) {
								classmentfinish.add(list.get(a));
								break;
							}

							b++;
						}

					} else {
						classmentfinish.add(list.get(a));
					}

					a++;
				}

				Classement = (ArrayList<String>) classmentfinish.clone();
			}

			ArrayList<String> listjoueuur = new ArrayList<>(finded.keySet());
			ArrayList<String> listjoueur = new ArrayList<>();
			for (String str : listjoueuur) {
				if (!Classement.contains(str)) {
					listjoueur.add(str);
				}
			}

			int player = 0;

			while (player != listjoueur.size()) {
				String name = listjoueur.get(player);

				int nb = finded.get(name).size();
				if (nb != 0) {
					if (Classement.size() != 0) {
						int a = 0;
						while (a != Classement.size()) {

							if (nb > finded.get(Classement.get(a)).size()) {
								Classement.add(a, name);
								break;

							}
							if (Classement.size() == a + 1) {
								Classement.add(name);
								break;
							}

							a++;
						}

					} else {
						Classement.add(name);
					}
				}
				player++;
			}
		}
		UpdateScoreboard();
	}

	public static void UpdateScoreboard() {

		for (String str : scoreboard.getEntries()) {
			if (str.startsWith("§Y")) {
				scoreboard.resetScores(str);
			}
		}

		int a = 0;

		while (a != classmentnbmax) {

			if (a < Classement.size()) {

				objective.getScore("§Y" + Message.get("scoreboard.lrank", Classement.get(a),
						Integer.toString(finded.get(Classement.get(a)).size()), Integer.toString(blocktofind.size())))
						.setScore(classmentnbmax + classementminscore - a - 1);

			} else {
				String preline = "";
				if (classmentnbmax >= 10) {
					if (a >= 10) {
						preline = "§" + a;
						preline = preline.substring(0, 2) + "§" + preline.substring(2);
						System.out.println(preline);

					} else {
						preline = "§" + a;
					}

				} else {
					preline = "§" + a;
				}

				objective.getScore("§Y" + preline + Message.get("scoreboard.lvoid"))
						.setScore(classmentnbmax + classementminscore - a - 1);
			}

			a++;
		}
	}

	private void getLocationList() {
		int a = 0;
		int nb = getConfigCubaraibe().getInt("nb");
		while (a != nb) {
			String string = "loc" + a;
			String World = getConfigCubaraibe().getConfigurationSection("blocktofind." + string).getString("World");
			int X = getConfigCubaraibe().getConfigurationSection("blocktofind." + string).getInt("X");
			int Y = getConfigCubaraibe().getConfigurationSection("blocktofind." + string).getInt("Y");
			int Z = getConfigCubaraibe().getConfigurationSection("blocktofind." + string).getInt("Z");
			blocktofind.add(new Location(getServer().getWorld(World), X, Y, Z));
			a++;
		}
	}

	private void saveLocationList() {
		int a = 0;
		while (a != blocktofind.size()) {
			try {
				Location loc = blocktofind.get(a);

				String string = "loc" + a;

				if (getConfigCubaraibe() != null) {

					if (getConfigCubaraibe().getConfigurationSection("blocktofind") == null) {
						getConfigCubaraibe().createSection("blocktofind");
					}

					if (getConfigCubaraibe().getConfigurationSection("blocktofind." + string) == null) {
						getConfigCubaraibe().getConfigurationSection("blocktofind").createSection(string);
					}

					getConfigCubaraibe().set("nb", blocktofind.size());
					getConfigCubaraibe().getConfigurationSection("blocktofind." + string).set("World",
							loc.getWorld().getName());
					getConfigCubaraibe().getConfigurationSection("blocktofind." + string).set("X", loc.getBlockX());
					getConfigCubaraibe().getConfigurationSection("blocktofind." + string).set("Y", loc.getBlockY());
					getConfigCubaraibe().getConfigurationSection("blocktofind." + string).set("Z", loc.getBlockZ());

					saveConfig(ConfigFileCubaraibe, ConfigCubaraibe);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			a++;
		}
	}

	private void getPlayerfind() {
		if (getConfigfind().getConfigurationSection("players") != null) {
			if (getConfigfind().getConfigurationSection("players").getKeys(false).size() > 0) {
				for (String string : getConfigfind().getConfigurationSection("players").getKeys(false)) {
					List<?> list = getConfigfind().getConfigurationSection("players").getList(string);
					if (list != null) {
						if (list.size() > 0) {
							for (Object oid : list) {
								if (oid instanceof Integer) {
									int id = (Integer) oid;

									if (id < blocktofind.size()) {
										if (!finded.containsKey(string)) {
											finded.put(string, new ArrayList<Integer>());
										}
										finded.get(string).add(id);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void initFinalRank() {
		if (getConfigFinalRank().getKeys(false) == null) {
			for (String str : getConfigFinalRank().getKeys(false)) {
				Date date = new Date(getConfigFinalRank().getLong(str));
				if (date.getTime() != 0) {
					Classementfinish.put(str, new Date(getConfigFinalRank().getLong(str)));
				}
			}
		}
	}

	private void savefinded() {
		int a = 0;
		ArrayList<String> listp = new ArrayList<String>(finded.keySet());
		while (a != listp.size()) {
			String pseudo = listp.get(a);
			ArrayList<Integer> listi = finded.get(pseudo);
			if (getConfigfind().getConfigurationSection("players") == null) {
				getConfigfind().createSection("players");
			}
			getConfigfind().getConfigurationSection("players").set(pseudo, listi);
			saveConfig(ConfigFilefind, Configfind);
			a++;
		}
	}

	public void saveFinalRank() {
		for (String str : getConfigFinalRank().getKeys(false)) {
			getConfigFinalRank().set(str, 0);
			;
		}
		for (String str : Classementfinish.keySet()) {
			getConfigFinalRank().set(str, Classementfinish.get(str).getTime());
		}
		saveConfig(ConfigFileFinalRank, ConfigFinalRank);
	}

	public void saveConfig(File file, FileConfiguration fileconfig) {
		try {
			getConfigCubaraibe().options().copyDefaults(true);
			fileconfig.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File ConfigFilefind;
	private static FileConfiguration Configfind;

	public static FileConfiguration getConfigfind() {
		return Main.Configfind;
	}

	private File ConfigFileCubaraibe;
	private static FileConfiguration ConfigCubaraibe;

	public static FileConfiguration getConfigCubaraibe() {
		return Main.ConfigCubaraibe;
	}

	private File ConfigFileMsg;
	private static FileConfiguration ConfigMsg;

	public static FileConfiguration getConfigMsg() {
		return Main.ConfigMsg;
	}

	private File ConfigFileFinalRank;
	private static FileConfiguration ConfigFinalRank;

	public static FileConfiguration getConfigFinalRank() {
		return Main.ConfigFinalRank;
	}

	private void createConfig() {
		ConfigFilefind = new File(getDataFolder(), "listFind.yml");
		if (!ConfigFilefind.exists()) {
			ConfigFilefind.getParentFile().mkdirs();
			saveResource("listFind.yml", false);
		}

		Configfind = new YamlConfiguration();
		try {
			Configfind.load(ConfigFilefind);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\033[1;31m Fail load listFind.yml\033[0m");
		}

		ConfigFileCubaraibe = new File(getDataFolder(), "Config.yml");
		if (!ConfigFileCubaraibe.exists()) {
			ConfigFileCubaraibe.getParentFile().mkdirs();
			saveResource("Config.yml", false);
		}

		ConfigCubaraibe = new YamlConfiguration();
		try {
			ConfigCubaraibe.load(ConfigFileCubaraibe);
		} catch (Exception e) {
			System.out.println("\033[1;31m Fail load Config.yml\033[0m");
		}

		ConfigFileMsg = new File(getDataFolder(), "message.yml");
		if (!ConfigFileMsg.exists()) {
			ConfigFileMsg.getParentFile().mkdirs();
			saveResource("message.yml", false);
		}

		ConfigMsg = new YamlConfiguration();
		try {
			ConfigMsg.load(ConfigFileMsg);
		} catch (Exception e) {
			System.out.println("\033[1;31m Fail load message.yml\033[0m");
		}

		ConfigFileFinalRank = new File(getDataFolder(), "FinalRank.yml");
		if (!ConfigFileFinalRank.exists()) {
			ConfigFileFinalRank.getParentFile().mkdirs();
			saveResource("FinalRank.yml", false);
		}

		ConfigFinalRank = new YamlConfiguration();
		try {
			ConfigFinalRank.load(ConfigFileFinalRank);
		} catch (Exception e) {
			System.out.println("\033[1;31m Fail load FinalRank.yml\033[0m");
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (placerman.contains(e.getPlayer())) {
			blocktofind.add(e.getBlock().getLocation());
			if (Main.Classementfinish.size() != 0) {
				Classementfinish.clear();
				Message.sendMessage(e.getPlayer(), "cmd.placerFinalClear");
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (placerman.contains(e.getPlayer())) {
			if (blocktofind.contains(e.getBlock().getLocation())) {
				blocktofind.remove(e.getBlock().getLocation());

				if (Main.Classementfinish.size() != 0) {
					Classementfinish.clear();
					Message.sendMessage(e.getPlayer(), "cmd.placerFinalClear");
				}
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if ((e.getView().getTitle().equalsIgnoreCase("§rList Block"))) {
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if (item != null) {
				Player p = (Player) e.getView().getPlayer();

				int nbpage = Main.blocktofind.size() / 28 + 1;
				if (Main.blocktofind.size() % 28 == 0) {
					nbpage = nbpage - 1;
				}

				if (item.equals(Item.getItemStack("suivant", p))) {
					if (page.get(p) < nbpage) {
						page.put(p, (page.get(p) + 1));
						p.openInventory(inventory.getInventory("blocklist", p));
					}

				} else if (item.equals(Item.getItemStack("precedent", p))) {
					if (page.get(p) > 1) {
						page.put(p, (page.get(p) - 1));
						p.openInventory(inventory.getInventory("blocklist", p));
					}

				} else if (e.getCurrentItem().equals(Item.getItemStack("page", p))
						|| e.getCurrentItem().equals(Item.getItemStack("no", p))) {
					e.setCancelled(true);

				} else {
					ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
					String W = lore.get(0).substring(11);
					String sX = lore.get(1).substring(7);
					String sY = lore.get(2).substring(7);
					String sZ = lore.get(3).substring(7);

					int X = Integer.parseInt(sX);
					int Y = Integer.parseInt(sY);
					int Z = Integer.parseInt(sZ);

					Location loc = new Location(Bukkit.getWorld(W), X, Y, Z);

					if (e.getClick().isLeftClick()) {
						if (e.getClick().isShiftClick()) {
							loc.getWorld().getBlockAt(loc).setType(Material.AIR);
							if (blocktofind.contains(loc)) {
								blocktofind.remove(loc);
							}

						} else {

							if (blocktofind.contains(loc)) {
								blocktofind.remove(loc);
							}
						}
						p.openInventory(inventory.getInventory("blocklist", p));

					} else if (e.getClick().isRightClick()) {
						p.teleport(new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() + 0.5,
								loc.getBlockZ() + 0.5));
					}
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		try {
			if (inter.get(e.getPlayer())) {
				System.out.println("inter ok");
				inter.put(e.getPlayer(), false);
				if (started) {
					System.out.println("started ok");
					Player p = e.getPlayer();
					if (e.getClickedBlock() != null) {
						System.out.println("block != null");
						if (blocktofind.contains(e.getClickedBlock().getLocation())) {
							System.out.println("is block to find");
							if (!finded.containsKey(p.getName())) {
								finded.put(p.getName(), new ArrayList<Integer>());
							}

							if (e.getHand() == EquipmentSlot.OFF_HAND) {

								if (finded.get(p.getName())
										.contains(blocktofind.indexOf(e.getClickedBlock().getLocation()))) {
									p.sendMessage(Message.get("global.blockalreadyfind"));
								} else {
									finded.get(p.getName()).add(blocktofind.indexOf(e.getClickedBlock().getLocation()));
									if (finded.get(p.getName()).size() == blocktofind.size()) {
										p.sendMessage(Message.get("global.allblockfind"));
										Classementfinish.put(p.getName(), new Date());
										updatetop3();
									} else {
										p.sendMessage(Message.get("global.newblockfind",
												Integer.toString(finded.get(p.getName()).size()),
												Integer.toString(blocktofind.size())));
										updatetop3();
									}
								}
							}
						}
					}
				}
			}
			inter.put(e.getPlayer(), true);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {

		Thread t = new Thread(new Runnable() {

			@SuppressWarnings("static-access")
			public void run() {

				inter.put(e.getPlayer(), true);

				updatetop3();
				try {
					Thread.currentThread().sleep(10000L);
					if (started || e.getPlayer().hasPermission(perm)) {

						if (!team.hasPlayer(e.getPlayer())) {
							team.addPlayer(e.getPlayer());
						}

						e.getPlayer().setScoreboard(scoreboard);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		t.start();
	}
}
