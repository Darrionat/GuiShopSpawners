package me.Darrionat.GUIShopSpawners.UI;

import java.text.DecimalFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Darrionat.GUIShopSpawners.Main;
import me.Darrionat.GUIShopSpawners.Utils;
import me.Darrionat.GUIShopSpawners.Files.ConfigManager;
import net.milkbowl.vault.economy.EconomyResponse;

public class Qty {

	public static Main plugin;
	public static String mob;
	public static ItemStack skull;
	private static ConfigManager messages;

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
		DecimalFormat formatter = new DecimalFormat("#,###");
		String fillItemID = config.getString("Fill Item ID").toUpperCase();
		Material fillItemMaterial = Material.getMaterial(fillItemID);

		if (config.getBoolean("Fill Item Enabled") == true) {
			for (int i = 1; i <= inv_rows; i++) {
				Utils.createItem(inv, fillItemMaterial, 1, i, "&f");
			}
		}

		Utils.createskullItem(inv, skull, 1, 5, "&e" + mob + " &fSpawner", "&aBuy: $" + formatter.format(price),
				"&cSell: $" + formatter.format(sellprice));

		Material redglasspane = Material.RED_STAINED_GLASS_PANE;

		Utils.createItem(inv, redglasspane, 1, 19, "&ePurchase &a1 &eSpawner", "&aBuy: $" + formatter.format(price),
				"&cSell: $" + formatter.format(sellprice));

		Utils.createItem(inv, redglasspane, 2, 21, "&ePurchase &a2 &eSpawners",
				"&aBuy: $" + formatter.format(price * 2), "&cSell: $" + formatter.format(sellprice * 2));

		Utils.createItem(inv, redglasspane, 8, 23, "&ePurchase &a8 &eSpawners",
				"&aBuy: $" + formatter.format(price * 8), "&cSell: $" + formatter.format(sellprice * 8));

		Utils.createItem(inv, redglasspane, 32, 25, "&ePurchase &a32 &eSpawners",
				"&aBuy: $" + formatter.format(price * 32), "&cSell: $" + formatter.format(sellprice * 32));

		Utils.createItem(inv, redglasspane, 64, 27, "&ePurchase &a64 &eSpawners",
				"&aBuy: $" + formatter.format(price * 64), "&cSell: $" + formatter.format(sellprice * 64));

		if (p.hasPermission("guishopspawners.sell")) {
			Utils.createItem(inv, Material.HOPPER, 1, 51, "&cSell spawners in hand");
		}

		Utils.createItem(inv, Material.ARROW, 1, 49, "&7Go Back");
		Utils.createItem(inv, Material.NETHER_STAR, 1, 50, "&eClose Menu");

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	@SuppressWarnings("deprecation")
	public static void clicked(Player p, int slot, ItemStack clicked, Inventory inv, JavaPlugin plugin) {

		messages = new ConfigManager((Main) plugin);
		String notEnoughMoney = messages.getMessage("notEnoughMoney");
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&7Go Back"))) {
			p.closeInventory();
			p.openInventory(Main.GUI(p, plugin));
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eClose Menu"))) {
			p.closeInventory();
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&cSell spawners in hand"))) {
			p.chat("/sellspawner");
			return;
		}
		// Quantity clicks

		DecimalFormat formatter = new DecimalFormat("#,###");
		ConfigurationSection config = plugin.getConfig();
		ConfigurationSection mobSection = config.getConfigurationSection(mob);
		int price = mobSection.getInt("Buy");
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&ePurchase &a1 &eSpawner"))) {
			// Throws a memory error if it's above everything for some reason when a player
			// clicks close menu/back.

			//mob variable is the mob that they chose
			//My code

			//The player either hasn't bought this item before or they haven't bought any spawners at all
			int max = mobSection.getInt("Max");
			int playerMobQty = config.getInt("Players." + p.getName() + "." + mob);
			
			if(max != playerMobQty) {
				if(playerMobQty == 0) {
					if(max < 1) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit!");
						return;
					}
					config.set("Players." + p.getName() + "." + mob, 1);
					plugin.saveConfig();
				}else {
					int beforeAdd = plugin.getConfig().getInt("Players." + p.getName() + "." + mob);
					int qty = config.getInt("Players." + p.getName() + "." + mob) + 1;
					
					if(qty > max) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit! You are at " + beforeAdd);
						return;
					}else {
						config.set("Players." + p.getName() + "." + mob, qty);
						plugin.saveConfig();
					}
					
				}
			}else {
				p.sendMessage("Sorry, but you have already bought the max amount of spawners for this mob!");
				return;
			}
			
			EconomyResponse sell = Main.econ.withdrawPlayer(p.getName(), price);
			if (sell.transactionSuccess()) {
				if (mob == "Zombie_Pigman") {
					Qty.zombiePigman(p, 1);
					return;
				}
				if (mob == "Mooshroom") {
					Qty.mooshroom(p, 1);
					return;
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"spawnergive " + p.getName() + " " + mob + " 1");
				String successfulTransaction = messages.getMessage("successfulTransaction").replace("%amt%",
						formatter.format(price));
				p.sendMessage(Utils.chat(successfulTransaction));
				
				return;
			} else {
				p.sendMessage(Utils.chat(notEnoughMoney));
				System.out.println(Utils.chat("&c[" + plugin.getName() + "] " + p.getName()
						+ " attempted to purchase a " + mob + " spawner without having enough money."));
				return;
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&ePurchase &a2 &eSpawners"))) {

			//mob variable is the mob that they chose
			//My code

			//The player either hasn't bought this item before or they haven't bought any spawners at all
			int max = mobSection.getInt("Max");
			int playerMobQty = config.getInt("Players." + p.getName() + "." + mob);
			
			if(max != playerMobQty) {
				if(playerMobQty == 0) {
					if(max < 2) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit!");
						return;
					}
					config.set("Players." + p.getName() + "." + mob, 2);
					plugin.saveConfig();
				}else {
					int beforeAdd = plugin.getConfig().getInt("Players." + p.getName() + "." + mob);
					int qty = config.getInt("Players." + p.getName() + "." + mob) + 2;
					
					if(qty > max) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit! You are at " + beforeAdd);
						return;
					}else {
						config.set("Players." + p.getName() + "." + mob, qty);
						plugin.saveConfig();
					}
					
				}
			}else {
				p.sendMessage("Sorry, but you have already bought the max amount of spawners for this mob!");
				return;
			}
			
			EconomyResponse sell = Main.econ.withdrawPlayer(p.getName(), price * 2);
			if (sell.transactionSuccess()) {
				if (mob == "Zombie_Pigman") {
					Qty.zombiePigman(p, 2);
					return;
				}
				if (mob == "Mooshroom") {
					Qty.mooshroom(p, 2);
					return;
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"spawnergive " + p.getName() + " " + mob + " 2");
				String successfulTransaction = messages.getMessage("successfulTransaction").replace("%amt%",
						formatter.format(price * 2));
				p.sendMessage(Utils.chat(successfulTransaction));
				return;
			} else {
				p.sendMessage(Utils.chat(notEnoughMoney));
				System.out.println(Utils.chat("&c[" + plugin.getName() + "] " + p.getName()
						+ " attempted to purchase 2 " + mob + " spawners without having enough money."));
				return;
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&ePurchase &a8 &eSpawners"))) {
			
			//mob variable is the mob that they chose
			//My code

			//The player either hasn't bought this item before or they haven't bought any spawners at all
			int max = mobSection.getInt("Max");
			int playerMobQty = config.getInt("Players." + p.getName() + "." + mob);
			
			if(max != playerMobQty) {
				if(playerMobQty == 0) {
					if(max < 8) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit!");
						return;
					}
					config.set("Players." + p.getName() + "." + mob, 8);
					plugin.saveConfig();
				}else {
					int beforeAdd = plugin.getConfig().getInt("Players." + p.getName() + "." + mob);
					int qty = config.getInt("Players." + p.getName() + "." + mob) + 8;
					
					if(qty > max) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit! You are at " + beforeAdd);
						return;
					}else {
						config.set("Players." + p.getName() + "." + mob, qty);
						plugin.saveConfig();
					}
					
				}
			}else {
				p.sendMessage("Sorry, but you have already bought the max amount of spawners for this mob!");
				return;
			}
			
			EconomyResponse sell = Main.econ.withdrawPlayer(p.getName(), price * 8);
			if (sell.transactionSuccess()) {
				if (mob == "Zombie_Pigman") {
					Qty.zombiePigman(p, 8);
					return;
				}
				if (mob == "Mooshroom") {
					Qty.mooshroom(p, 8);
					return;
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"spawnergive " + p.getName() + " " + mob + " 8");
				String successfulTransaction = messages.getMessage("successfulTransaction").replace("%amt%",
						formatter.format(price * 8));
				p.sendMessage(Utils.chat(successfulTransaction));
				return;
			} else {
				p.sendMessage(Utils.chat(notEnoughMoney));
				System.out.println(Utils.chat("&c[" + plugin.getName() + "] " + p.getName()
						+ " attempted to purchase 8 " + mob + " spawners without having enough money."));
				return;
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&ePurchase &a32 &eSpawners"))) {

			//mob variable is the mob that they chose
			//My code

			//The player either hasn't bought this item before or they haven't bought any spawners at all
			int max = mobSection.getInt("Max");
			int playerMobQty = config.getInt("Players." + p.getName() + "." + mob);
			
			if(max != playerMobQty) {
				if(playerMobQty == 0) {
					if(max < 32) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit!");
						return;
					}
					config.set("Players." + p.getName() + "." + mob, 32);
					plugin.saveConfig();
				}else {
					int beforeAdd = plugin.getConfig().getInt("Players." + p.getName() + "." + mob);
					int qty = config.getInt("Players." + p.getName() + "." + mob) + 32;
					
					if(qty > max) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit! You are at " + beforeAdd);
						return;
					}else {
						config.set("Players." + p.getName() + "." + mob, qty);
						plugin.saveConfig();
					}
					
				}
			}else {
				p.sendMessage("Sorry, but you have already bought the max amount of spawners for this mob!");
				return;
			}
			
			EconomyResponse sell = Main.econ.withdrawPlayer(p.getName(), price * 32);
			if (sell.transactionSuccess()) {
				if (mob == "Zombie_Pigman") {
					Qty.zombiePigman(p, 32);
					return;
				}
				if (mob == "Mooshroom") {
					Qty.mooshroom(p, 32);
					return;
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"spawnergive " + p.getName() + " " + mob + " 32");
				String successfulTransaction = messages.getMessage("successfulTransaction").replace("%amt%",
						formatter.format(price * 32));
				p.sendMessage(Utils.chat(successfulTransaction));
				return;
			} else {
				p.sendMessage(Utils.chat(notEnoughMoney));
				System.out.println(Utils.chat("&c[" + plugin.getName() + "] " + p.getName()
						+ " attempted to purchase 32 " + mob + " spawners without having enough money."));
				return;
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&ePurchase &a64 &eSpawners"))) {

			//mob variable is the mob that they chose
			//My code

			//The player either hasn't bought this item before or they haven't bought any spawners at all
			int max = mobSection.getInt("Max");
			int playerMobQty = config.getInt("Players." + p.getName() + "." + mob);
			
			if(max != playerMobQty) {
				if(playerMobQty == 0) {
					if(max < 64) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit!");
						return;
					}
					config.set("Players." + p.getName() + "." + mob, 64);
					plugin.saveConfig();
				}else {
					int beforeAdd = plugin.getConfig().getInt("Players." + p.getName() + "." + mob);
					int qty = config.getInt("Players." + p.getName() + "." + mob) + 64;
					
					if(qty > max) {
						p.sendMessage("Sorry, but the amount that you are trying to buy puts you over the spawner limit! You are at " + beforeAdd);
						return;
					}else {
						config.set("Players." + p.getName() + "." + mob, qty);
						plugin.saveConfig();
					}
					
				}
			}else {
				p.sendMessage("Sorry, but you have already bought the max amount of spawners for this mob!");
				return;
			}
			
			EconomyResponse sell = Main.econ.withdrawPlayer(p.getName(), price * 64);
			if (sell.transactionSuccess()) {
				if (mob == "Zombie_Pigman") {
					Qty.zombiePigman(p, 64);
					return;
				}
				if (mob == "Mooshroom") {
					Qty.mooshroom(p, 64);
					return;
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"spawnergive " + p.getName() + " " + mob + " 64");
				String successfulTransaction = messages.getMessage("successfulTransaction").replace("%amt%",
						formatter.format(price * 64));
				p.sendMessage(Utils.chat(successfulTransaction));
				return;
			} else {
				p.sendMessage(Utils.chat(notEnoughMoney));
				System.out.println(Utils.chat("&c[" + plugin.getName() + "] " + p.getName()
						+ " attempted to purchase 64 " + mob + " spawners without having enough money."));
				return;
			}
		}
	}

	public static void zombiePigman(Player p, int qty) {
		ConfigurationSection config = plugin.getConfig();
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"spawnergive " + p.getName() + " pig_zombie " + qty);
		DecimalFormat formatter = new DecimalFormat("#,###");

		int price = config.getConfigurationSection("Zombie_Pigman").getInt("Buy") * qty;
		String successfulTransaction = messages.getMessage("successfulTransaction").replace("%amt%",
				formatter.format(price));
		p.sendMessage(Utils.chat(successfulTransaction));
		return;
	}

	public static void mooshroom(Player p, int qty) {
		ConfigurationSection config = plugin.getConfig();
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"spawnergive " + p.getName() + " mushroom_cow " + qty);
		DecimalFormat formatter = new DecimalFormat("#,###");

		int price = config.getConfigurationSection("Mooshroom").getInt("Buy") * qty;
		String successfulTransaction = messages.getMessage("successfulTransaction").replace("%amt%",
				formatter.format(price));
		p.sendMessage(Utils.chat(successfulTransaction));
	}
}