package fr.dingepantere.search.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fr.dingepantere.search.Main;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import net.minecraft.server.v1_16_R3.PlayerConnection;

@SuppressWarnings("rawtypes")
public class Message {
	public static String get(String path) {
		return get(path, null, null, null);
	}

	public static String get(String path, String str) {
		return get(path, str, null, null);
	}

	public static String get(String path, String str, String str2) {
		return get(path, str, str2, null);
	}

	public static String get(String path, String str, String str2, String str3) {
		String msg = Main.getConfigMsg().getString(path);
		
		if (str != null) {
			msg = msg.replace("%text%", str);
		}
		
		if (str2 != null) {
			msg = msg.replace("%text2%", str2);
		}
		
		if (str3 != null) {
			msg = msg.replace("%text3%", str3);
		}
		
		if (msg.startsWith("%JSON%")) {
			msg = roadtoJSON(msg);
			
		} else {
			msg = msg.replace("&", "§");
		}
		return msg;
	}

	public static void sendMessage(Player sender, String path) {
		sendMessage(sender, path, null, null, null);
	}

	public static void sendMessage(Player sender, String path, String text) {
		sendMessage(sender, path, text, null, null);
	}

	public static void sendMessage(Player sender, String path, String text, String text2) {
		sendMessage(sender, path, text, text2, null);
	}

	public static void sendMessage(Player sender, String path, String text, String text2, String text3) {
		String msg = get(path, text, text2, text3);
		if (msg.startsWith("[\"\",{\"text\":\"")) {
			try {
				CraftPlayer cp = (CraftPlayer) sender;
				PlayerConnection connection = (cp.getHandle()).playerConnection;
				PacketPlayOutChat packet = new PacketPlayOutChat(
						(IChatBaseComponent) IChatBaseComponent.ChatSerializer.a(msg), ChatMessageType.a((byte) 0),
						sender.getUniqueId());
				if (path.equalsIgnoreCase("cubaraibe.playertchat"))
					System.out.println(tocmd(msg));
				connection.sendPacket((Packet) packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			sender.sendMessage(msg);
		}
	}

	private static String roadtoJSON(String message) {
		String JSON;
		message = (String) message.subSequence(6, message.length());
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = -1; (i = message.indexOf("%%", i + 1)) != -1; i++)
			list.add(Integer.valueOf(i));
		HashMap<Integer, ArrayList<Integer>> posdebfin = new HashMap<>();
		for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
			int pos = ((Integer) iterator.next()).intValue();
			ArrayList<Integer> ALpos = new ArrayList<>();
			int a = 0;
			for (int j = pos; (j = message.indexOf("%", j + 1)) != -1; j++) {
				a++;
				ALpos.add(Integer.valueOf(j));
				if (a == 4) {
					posdebfin.put(Integer.valueOf(pos), ALpos);
					break;
				}
			}
		}
		if (list.size() > 0) {
			JSON = "[\"\",";
			if (((Integer) list.get(0)).intValue() != 1) {
				String pretext = message.substring(1, ((Integer) list.get(0)).intValue());
				HashMap<String, String> hashMap = fontstyle(pretext);
				JSON = String.valueOf(JSON) + "{\"text\":\"";
				JSON = String.valueOf(JSON) + pretext;
				JSON = String.valueOf(JSON) + "\",";
				if (((String) hashMap.get("&l")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"bold\":" + (String) hashMap.get("&l") + ",";
				if (((String) hashMap.get("&o")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"italic\":" + (String) hashMap.get("&o") + ",";
				if (((String) hashMap.get("&m")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"strikethrough\":" + (String) hashMap.get("&m") + ",";
				if (((String) hashMap.get("&n")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"underlined\":" + (String) hashMap.get("&n") + ",";
				if (((String) hashMap.get("&k")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"obfuscated\":" + (String) hashMap.get("&k") + ",";
				if (hashMap.get("&color") != null)
					JSON = String.valueOf(JSON) + "\"color\":\"" + (String) hashMap.get("&color") + "\"";
				JSON = String.valueOf(JSON) + "},";
			}
			for (Iterator<Integer> iterator1 = list.iterator(); iterator1.hasNext();) {
				int pos = ((Integer) iterator1.next()).intValue();
				String text = message.substring(pos + 2,
						((Integer) ((ArrayList<Integer>) posdebfin.get(Integer.valueOf(pos))).get(1)).intValue());
				String cmd = message.substring(
						((Integer) ((ArrayList<Integer>) posdebfin.get(Integer.valueOf(pos))).get(1)).intValue() + 1,
						((Integer) ((ArrayList<Integer>) posdebfin.get(Integer.valueOf(pos))).get(2)).intValue());
				String hover = message.substring(
						((Integer) ((ArrayList<Integer>) posdebfin.get(Integer.valueOf(pos))).get(2)).intValue() + 1,
						((Integer) ((ArrayList<Integer>) posdebfin.get(Integer.valueOf(pos))).get(3)).intValue());
				HashMap<String, String> hashMap = fontstyle(text);
				JSON = String.valueOf(JSON) + "{\"text\":\"";
				JSON = String.valueOf(JSON) + text;
				JSON = String.valueOf(JSON) + "\",";
				if (!cmd.equalsIgnoreCase(" ")) {
					JSON = String.valueOf(JSON) + "\"clickEvent\":{";
					JSON = String.valueOf(JSON) + "\"action\":\"run_command\",";
					JSON = String.valueOf(JSON) + "\"value\":\"";
					JSON = String.valueOf(JSON) + cmd;
					JSON = String.valueOf(JSON) + "\"";
					JSON = String.valueOf(JSON) + "},";
				}
				if (((String) hashMap.get("&l")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"bold\":" + (String) hashMap.get("&l") + ",";
				if (((String) hashMap.get("&o")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"italic\":" + (String) hashMap.get("&o") + ",";
				if (((String) hashMap.get("&m")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"strikethrough\":" + (String) hashMap.get("&m") + ",";
				if (((String) hashMap.get("&n")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"underlined\":" + (String) hashMap.get("&n") + ",";
				if (((String) hashMap.get("&k")).equalsIgnoreCase("true"))
					JSON = String.valueOf(JSON) + "\"obfuscated\":" + (String) hashMap.get("&k") + ",";
				if (hashMap.get("&color") != null)
					JSON = String.valueOf(JSON) + "\"color\":\"" + (String) hashMap.get("&color") + "\"";
				if (!cmd.equalsIgnoreCase(" ")) {
					JSON = String.valueOf(JSON) + ",\"hoverEvent\":{";
					JSON = String.valueOf(JSON) + "\"action\":\"show_text\",";
					JSON = String.valueOf(JSON) + "\"value\":\"";
					JSON = String.valueOf(JSON) + hover;
					JSON = String.valueOf(JSON) + "\"}";
				}
				if (((Integer) ((ArrayList<Integer>) posdebfin.get(Integer.valueOf(pos))).get(3))
						.intValue() == message.length() - 1) {
					JSON = String.valueOf(JSON) + "}";
				} else {
					JSON = String.valueOf(JSON) + "},";
					if (pos == ((Integer) list.get(list.size() - 1)).intValue()) {
						int fin = ((Integer) ((ArrayList<Integer>) posdebfin.get(Integer.valueOf(pos)))
								.get(((ArrayList) posdebfin.get(Integer.valueOf(pos))).size() - 1)).intValue();
						int finfin = message.length() - 1;
						if (fin != finfin) {
							String midtext = message.substring(fin + 1, finfin + 1);
							HashMap<String, String> tablefin = fontstyle(midtext);
							JSON = String.valueOf(JSON) + "{\"text\":\"";
							JSON = String.valueOf(JSON) + midtext;
							JSON = String.valueOf(JSON) + "\",";
							if (((String) tablefin.get("&l")).equalsIgnoreCase("true"))
								JSON = String.valueOf(JSON) + "\"bold\":" + (String) tablefin.get("&l") + ",";
							if (((String) tablefin.get("&o")).equalsIgnoreCase("true"))
								JSON = String.valueOf(JSON) + "\"italic\":" + (String) tablefin.get("&o") + ",";
							if (((String) tablefin.get("&m")).equalsIgnoreCase("true"))
								JSON = String.valueOf(JSON) + "\"strikethrough\":" + (String) tablefin.get("&m") + ",";
							if (((String) tablefin.get("&n")).equalsIgnoreCase("true"))
								JSON = String.valueOf(JSON) + "\"underlined\":" + (String) tablefin.get("&n") + ",";
							if (((String) tablefin.get("&k")).equalsIgnoreCase("true"))
								JSON = String.valueOf(JSON) + "\"obfuscated\":" + (String) tablefin.get("&k") + ",";
							if (tablefin.get("&color") != null)
								JSON = String.valueOf(JSON) + "\"color\":\"" + (String) hashMap.get("&color") + "\"";
							JSON = String.valueOf(JSON) + "}";
						}
					}
				}
				if (pos != ((Integer) list.get(list.size() - 1)).intValue()) {
					int fin = ((Integer) ((ArrayList<Integer>) posdebfin.get(Integer.valueOf(pos)))
							.get(((ArrayList) posdebfin.get(Integer.valueOf(pos))).size() - 1)).intValue();
					int deb = ((Integer) ((ArrayList<Integer>) posdebfin
							.get(list.get(list.indexOf(Integer.valueOf(pos)) + 1))).get(0)).intValue();
					if (fin != deb) {
						String midtext = message.substring(fin + 1, deb - 1);
						HashMap<String, String> tablefin = fontstyle(midtext);
						JSON = String.valueOf(JSON) + "{\"text\":\"";
						JSON = String.valueOf(JSON) + midtext;
						JSON = String.valueOf(JSON) + "\",";
						if (((String) tablefin.get("&l")).equalsIgnoreCase("true"))
							JSON = String.valueOf(JSON) + "\"bold\":" + (String) tablefin.get("&l") + ",";
						if (((String) tablefin.get("&o")).equalsIgnoreCase("true"))
							JSON = String.valueOf(JSON) + "\"italic\":" + (String) tablefin.get("&o") + ",";
						if (((String) tablefin.get("&m")).equalsIgnoreCase("true"))
							JSON = String.valueOf(JSON) + "\"strikethrough\":" + (String) tablefin.get("&m") + ",";
						if (((String) tablefin.get("&n")).equalsIgnoreCase("true"))
							JSON = String.valueOf(JSON) + "\"underlined\":" + (String) tablefin.get("&n") + ",";
						if (((String) tablefin.get("&k")).equalsIgnoreCase("true"))
							JSON = String.valueOf(JSON) + "\"obfuscated\":" + (String) tablefin.get("&k") + ",";
						if (tablefin.get("&color") != null)
							JSON = String.valueOf(JSON) + "\"color\":\"" + (String) hashMap.get("&color") + "\"";
						JSON = String.valueOf(JSON) + "},";
					}
				}
			}
			JSON = String.valueOf(JSON) + "]";
			HashMap<String, String> table = fontstyle(JSON);
			for (String fontstyle : table.keySet())
				JSON = JSON.replace(fontstyle, "");
		} else {
			JSON = message.substring(1);
		}
		return JSON;
	}

	private static HashMap<String, String> fontstyle(String text) {
		HashMap<String, String> table = new HashMap<>();
		if (text.contains("&k")) {
			table.put("&k", "true");
		} else {
			table.put("&k", "false");
		}
		if (text.contains("&l")) {
			table.put("&l", "true");
		} else {
			table.put("&l", "false");
		}
		if (text.contains("&m")) {
			table.put("&m", "true");
		} else {
			table.put("&m", "false");
		}
		if (text.contains("&n")) {
			table.put("&n", "true");
		} else {
			table.put("&n", "false");
		}
		if (text.contains("&o")) {
			table.put("&o", "true");
		} else {
			table.put("&o", "false");
		}
		if (text.contains("&0")) {
			table.put("&color", "black");
			table.put("&0", null);
		}
		if (text.contains("&1")) {
			table.put("&color", "dark_blue");
			table.put("&1", null);
		}
		if (text.contains("&2")) {
			table.put("&color", "dark_green");
			table.put("&2", null);
		}
		if (text.contains("&3")) {
			table.put("&color", "dark_aqua");
			table.put("&3", null);
		}
		if (text.contains("&4")) {
			table.put("&color", "dark_red");
			table.put("&4", null);
		}
		if (text.contains("&5")) {
			table.put("&color", "dark_purple");
			table.put("&5", null);
		}
		if (text.contains("&6")) {
			table.put("&color", "gold");
			table.put("&6", null);
		}
		if (text.contains("&7")) {
			table.put("&color", "gray");
			table.put("&7", null);
		}
		if (text.contains("&8")) {
			table.put("&color", "dark_gray");
			table.put("&8", null);
		}
		if (text.contains("&9")) {
			table.put("&color", "blue");
			table.put("&9", null);
		}
		if (text.contains("&a")) {
			table.put("&color", "green");
			table.put("&a", null);
		}
		if (text.contains("&b")) {
			table.put("&color", "aqua");
			table.put("&b", null);
		}
		if (text.contains("&c")) {
			table.put("&color", "red");
			table.put("&c", null);
		}
		if (text.contains("&d")) {
			table.put("&color", "light_purple");
			table.put("&d", null);
		}
		if (text.contains("&e")) {
			table.put("&color", "yellow");
			table.put("&e", null);
		}
		if (text.contains("&f") || text.contains("&r")) {
			table.put("&color", "white");
			table.put("&f", null);
			table.put("&r", null);
		}
		if (text.contains("##")) {
			int hexind = text.indexOf("#");
			String hex = text.substring(hexind + 1, hexind + 8);
			table.put("&color", hex);
			table.put("#" + hex, null);
		}
		try {
			if (!table.keySet().contains("&color"))
				table.put("&color", "white");
		} catch (Exception e) {
			System.out.println("error " + (String) table.get("&color"));
			table.put("&color", "white");
		}
		return table;
	}

	public static String tocmd(String str) {
		try {
			JSONParser parse = new JSONParser();
			JSONArray json = (JSONArray) parse.parse(str);
			String ok = "";
			int a = 1;
			while (a != json.size()) {
				Object instanceOfPatternExpressionValue = json.get(a);
				JSONObject jSONObject;
				if (instanceOfPatternExpressionValue instanceof JSONObject
						&& (jSONObject = (JSONObject) instanceOfPatternExpressionValue) == (JSONObject) instanceOfPatternExpressionValue)
					ok = String.valueOf(ok) + (String) jSONObject.get("text");
				a++;
			}
			return ok;
		} catch (Exception e) {
			return str;
		}
	}
}