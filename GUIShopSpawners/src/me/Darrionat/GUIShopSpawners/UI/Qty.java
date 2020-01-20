package me.Darrionat.GUIShopSpawners.UI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Darrionat.GUIShopSpawners.Main;
import me.Darrionat.GUIShopSpawners.Utils;
import me.Darrionat.GUIShopSpawners.Files.FileManager;
import net.milkbowl.vault.economy.EconomyResponse;

public class Qty {

	public static Main plugin;
	public static String mob;
	public static ItemStack skull;

	public Qty(Main plugin, String mob, ItemStack skull) {
		Qty.plugin = plugin;
		Qty.mob = mob;
		Qty.skull = skull;
	}

	public static Inventory inv;
	public static String inventory_name;
	public static int inv_rows = 6 * 9;

	public static void initialize() {
		inventory_name = Utils.chat("&2&lQuantity");
		inv = Bukkit.createInventory(null, inv_rows);
	}

	public static Inventory GUI(Player p, JavaPlugin plugin) {
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, inventory_name);
		ConfigurationSection config = plugin.getConfig();
		/*
		 * Maps map = new Maps(); HashMap<String, String> skullname =
		 * map.getNameSkullMap(); String skinname = skullname.get(mob); ItemStack skull
		 * = new ItemStack(Material.PLAYER_HEAD, 1); SkullMeta skullMeta = (SkullMeta)
		 * skull.getItemMeta(); OfflinePlayer skullplayer =
		 * plugin.getServer().getOfflinePlayer(skinname);
		 * skullMeta.setOwningPlayer(skullplayer); skull.setItemMeta(skullMeta);
		 */
		ConfigurationSection mobSection = config.getConfigurationSection(mob);
		int price = mobSection.getInt("Buy");
		int sellprice = mobSection.getInt("Sell");
		// DecimalFormat formatter = new DecimalFormat("#,###");
		String fillItemID = config.getString("Fill Item ID").toUpperCase();
		Material fillItemMaterial = Material.getMaterial(fillItemID);

		if (config.getBoolean("Fill Item Enabled") == true) {
			for (int i = 1; i <= inv_rows; i++) {
				Utils.createItem(inv, fillItemMaterial, 1, i, "&f");
			}
		}

		Utils.createskullItem(inv, skull, 1, 5, "&e" + mob + " &fSpawner", "&aBuy: $" + (price),
				"&cSell: $" + (sellprice));

		Material redglasspane = Material.RED_STAINED_GLASS_PANE;
		List<Integer> qtys = new ArrayList<Integer>();
		qtys.add(1);
		qtys.add(2);
		qtys.add(8);
		qtys.add(32);
		qtys.add(64);
		int slot = 19;
		for (int qty : qtys) {
			Utils.createItem(inv, redglasspane, 1, slot, "&ePurchase &a" + qty + " &eSpawners",
					"&aBuy: $" + (price * qty), "&cSell: $" + (sellprice * qty));
			slot = slot + 2;
		}

		if (p.hasPermission("guishopspawners.sell")) {
			Utils.createItem(inv, Material.HOPPER, 1, 51, Utils.chat(config.getString("SellSpawnerItem")));
		}
		Utils.createItem(inv, Material.NETHER_STAR, 1, 50, Utils.chat(config.getString(("CloseMenuItem"))));
		Utils.createItem(inv, Material.ARROW, 1, 49, Utils.chat(config.getString("GoBackItem")));

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static void clicked(Player p, int slot, ItemStack clicked, Inventory inv, JavaPlugin plugin) {

		if (clicked.getItemMeta().getDisplayName()
				.equalsIgnoreCase(Utils.chat(plugin.getConfig().getString("GoBackItem")))) {
			p.closeInventory();
			p.openInventory(Main.GUI(p, plugin));
			return;
		}
		if (clicked.getItemMeta().getDisplayName()
				.equalsIgnoreCase(Utils.chat(plugin.getConfig().getString("CloseMenuItem")))) {
			p.closeInventory();
		}
		if (clicked.getItemMeta().getDisplayName()
				.equalsIgnoreCase(Utils.chat(plugin.getConfig().getString("SellSpawnerItem")))) {
			p.chat("/sellspawner");
			return;
		}
		// Quantity clicks

		purchaseSpawner(clicked, p, plugin);
	}

	private static void purchaseSpawner(ItemStack clicked, Player p, JavaPlugin plugin) {
		FileManager fileManager = new FileManager((Main) plugin);
		FileConfiguration messages = fileManager.getDataConfig("messages");
		// DecimalFormat formatter = new DecimalFormat("#,###");
		ConfigurationSection config = plugin.getConfig();
		ConfigurationSection mobSection = config.getConfigurationSection(mob);
		int price = mobSection.getInt("Buy");
		String notEnoughMoney = messages.getString("notEnoughMoney");
		if (!clicked.getItemMeta().getDisplayName().contains("Purchase ")) {
			return;
		}
		int qty = Integer.parseInt(clicked.getItemMeta().getDisplayName().replace(Utils.chat("&ePurchase &a"), "")
				.replace(Utils.chat(" &eSpawners"), ""));
		@SuppressWarnings("deprecation")
		EconomyResponse sell = Main.econ.withdrawPlayer(p.getName(), price * qty);
		if (sell.transactionSuccess()) {
			if (mob.equalsIgnoreCase("Zombie_Pigman")) {
				Qty.zombiePigman(p, qty, plugin);
				return;
			}
			if (mob == "Mooshroom") {
				Qty.mooshroom(p, qty, plugin);
				return;
			}
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
					"spawnergive " + p.getName() + " " + mob + " " + qty);
			String successfulTransaction = messages.getString("successfulTransaction").replace("%amt%",
					String.valueOf(price * qty));
			p.sendMessage(Utils.chat(successfulTransaction));
			p.closeInventory();
			return;
		} else {
			p.sendMessage(Utils.chat(notEnoughMoney));
			System.out.println(Utils.chat("&c[" + plugin.getName() + "] " + p.getName() + " attempted to purchase "
					+ qty + " " + mob + " spawners without having enough money."));
			p.closeInventory();
			return;
		}

	}

	public static void zombiePigman(Player p, int qty, JavaPlugin plugin) {
		ConfigurationSection config = plugin.getConfig();
		FileManager fileManager = new FileManager((Main) plugin);
		FileConfiguration messages = fileManager.getDataConfig("messages");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"spawnergive " + p.getName() + " pig_zombie " + qty);
		p.sendMessage("spawnergive " + p.getName() + " pig_zombie " + qty);
		// DecimalFormat formatter = new DecimalFormat("#,###");

		int price = config.getConfigurationSection("Zombie_pigman").getInt("Buy") * qty;
		String successfulTransaction = messages.getString("successfulTransaction").replace("%amt%",
				String.valueOf(price));
		p.sendMessage(Utils.chat(successfulTransaction));
		return;
	}

	public static void mooshroom(Player p, int qty, JavaPlugin plugin) {
		ConfigurationSection config = plugin.getConfig();
		FileManager fileManager = new FileManager((Main) plugin);
		FileConfiguration messages = fileManager.getDataConfig("messages");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"spawnergive " + p.getName() + " mushroom_cow " + qty);
		// DecimalFormat formatter = new DecimalFormat("#,###");

		int price = config.getConfigurationSection("Mooshroom").getInt("Buy") * qty;
		String successfulTransaction = messages.getString("successfulTransaction").replace("%amt%",
				String.valueOf(price));
		p.sendMessage(Utils.chat(successfulTransaction));
	}
}