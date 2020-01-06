package me.Darrionat.GUIShopSpawners.Commands;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.Darrionat.GUIShopSpawners.Main;
import me.Darrionat.GUIShopSpawners.Maps;
import me.Darrionat.GUIShopSpawners.Utils;
import me.Darrionat.GUIShopSpawners.Files.FileManager;

public class Spawners implements CommandExecutor {

	private Main plugin;

	public Spawners(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("spawners").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		FileManager fileManager = new FileManager((Main) plugin);
		FileConfiguration messages = fileManager.getDataConfig("messages");
		String noPermission = messages.getString("noPermission");

		String reloadPerm = "guishopspawners.reload";
		String adminPerm = "guishopspawners.admin";
		String useNumbersError = messages.getString("useNumbersError");
		Maps map = new Maps();
		HashMap<Integer, String> mobs = map.getMobMap();
		FileConfiguration config = plugin.getConfig();

		if (args.length > 0) {
			if (!(args[0].equalsIgnoreCase("reload"))
					&& (!(args[0].equalsIgnoreCase("list")) && (!(args[0].equalsIgnoreCase("edit"))))) {
				sender.sendMessage(Utils
						.chat("&6---GUIShopSpawners v" + plugin.getDescription().getVersion() + " By Darrionat---"));
				sender.sendMessage(Utils.chat("&e/spawners - Opens up the shop"));
				sender.sendMessage(Utils.chat("&e/spawners reload - Reloads the config"));
				sender.sendMessage(Utils.chat("&e/spawners list - See what spawners are true/false and the price"));
				sender.sendMessage(Utils.chat("&e/spawners edit price [SpawnerType] [NewPrice]"));
				sender.sendMessage(Utils.chat("&e/spawners edit sellprice [SpawnerType] [NewPrice]"));
				sender.sendMessage(Utils.chat("&e/spawners edit status [SpawnerType] [True/False]"));
				sender.sendMessage(Utils.chat("&e/sellspawner - Hold a spawner to sell it"));
				sender.sendMessage(Utils.chat("&6---Support Here: &e&nhttps://discord.gg/xNKrH5Z&6---"));
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (!(sender instanceof Player)) {
					Bukkit.getPluginManager().getPlugin(plugin.getName()).reloadConfig();
					sender.sendMessage(Utils.chat("&a" + plugin.getName() + "'s config reloaded!"));
					return true;
				}
				Player p = (Player) sender;

				if (!p.hasPermission(reloadPerm)) {
					p.sendMessage(Utils.chat(noPermission.replace("%permission%", reloadPerm)));
					return true;
				}
				Bukkit.getPluginManager().getPlugin(plugin.getName()).reloadConfig();
				p.sendMessage(Utils.chat("&a" + plugin.getName() + "'s config reloaded!"));
				return true;
			}
			if (args[0].equalsIgnoreCase("list")) {
				if (!(sender instanceof Player)) {
					for (int i = 1; i <= 38; i = i + 1) {
						DecimalFormat formatter = new DecimalFormat("#,###");
						ConfigurationSection mobSection = config.getConfigurationSection(mobs.get(i));
						String buyprice = formatter.format(mobSection.getInt("Buy"));
						String sellprice = formatter.format(mobSection.getInt("Sell"));

						if (mobSection.getBoolean("Enabled") == true) {
							sender.sendMessage(Utils.chat(
									"&a" + mobs.get(i) + " true &eBuy: &a$" + buyprice + " &eSell: &c$" + sellprice));
						}
						if (mobSection.getBoolean("Enabled") == false) {
							sender.sendMessage(Utils.chat(
									"&c" + mobs.get(i) + " false &eBuy: &a$" + buyprice + " &eSell: &c$" + sellprice));
						}
					}
					return true;
				}
				Player p = (Player) sender;

				if (!p.hasPermission(adminPerm)) {
					p.sendMessage(Utils.chat(noPermission.replace("%permission%", adminPerm)));
					return true;
				}
				for (int i = 1; i <= 38; i = i + 1) {
					DecimalFormat formatter = new DecimalFormat("#,###");
					ConfigurationSection mobSection = config.getConfigurationSection(mobs.get(i));
					String buyprice = formatter.format(mobSection.getInt("Buy"));
					String sellprice = formatter.format(mobSection.getInt("Sell"));
					if (mobSection.getBoolean("Enabled") == true) {
						sender.sendMessage(Utils
								.chat("&a" + mobs.get(i) + " true &eBuy: &a$" + buyprice + " &eSell: &c$" + sellprice));
					}
					if (mobSection.getBoolean("Enabled") == false) {
						sender.sendMessage(Utils.chat(
								"&c" + mobs.get(i) + " false &eBuy: &a$" + buyprice + " &eSell: &c$" + sellprice));
					}
				}

				return true;
			}
			if (args[0].equalsIgnoreCase("edit")) {
				if (args.length == 1) {
					sender.sendMessage(Utils.chat("&e/spawners edit price [SpawnerType] [NewPrice]"));
					sender.sendMessage(Utils.chat("&e/spawners edit sellprice [SpawnerType] [NewPrice]"));
					sender.sendMessage(Utils.chat("&e/spawners edit status [SpawnerType] [True/False]"));
					return true;
				}
				if ((!(args[1].equalsIgnoreCase("price")) && !(args[1].equalsIgnoreCase("status"))
						&& !(args[1].equalsIgnoreCase("sellprice")))) {
					sender.sendMessage(Utils.chat("&e/spawners edit price [SpawnerType] [NewPrice]"));
					sender.sendMessage(Utils.chat("&e/spawners edit sellprice [SpawnerType] [NewPrice]"));
					sender.sendMessage(Utils.chat("&e/spawners edit status [SpawnerType] [True/False]"));
					return true;
				}
				// / no arg arg0 arg1 arg2 arg3
				// /spawners edit price Creeper 1000
				if (args[1].equalsIgnoreCase("price")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						if (!p.hasPermission(adminPerm)) {
							p.sendMessage(Utils.chat(noPermission.replace("%permission%", adminPerm)));
							return true;
						}
					}
					if (args.length == 2) {
						sender.sendMessage(Utils.chat("&e/spawners edit price [SpawnerType] [NewPrice]"));
						return true;
					}
					for (int i = 1; i <= 38; i = i + 1) {
						if (args.length == 3) {
							sender.sendMessage(Utils.chat("&cValid mob types"));
							sender.sendMessage(Utils.chat(mobs.values().toString()));
							return true;
						}
						// Args length 4
						if (mobs.get(i).equalsIgnoreCase(args[2])) {
							try {
								int newPrice = Integer.parseInt(args[3]);
								ConfigurationSection mobSection = config.getConfigurationSection(mobs.get(i));
								mobSection.set("Buy", newPrice);

								sender.sendMessage(
										Utils.chat("&e" + mobs.get(i) + "'s price has been changed to &a$" + newPrice));
								Bukkit.getPluginManager().getPlugin(plugin.getName()).saveConfig();

							} catch (NumberFormatException e) {
								sender.sendMessage(Utils.chat(useNumbersError));
								return true;
							}
							return true;
						}

					}

				}
				// not done yet
				if (args[1].equalsIgnoreCase("sellprice")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						if (!p.hasPermission(adminPerm)) {
							p.sendMessage(Utils.chat(noPermission.replace("%permission%", adminPerm)));
							return true;
						}
					}
					if (args.length == 2) {
						sender.sendMessage(Utils.chat("&e/spawners edit sellprice [SpawnerType] [NewPrice]"));
						return true;
					}
					for (int i = 1; i <= mobs.size(); i = i + 1) {
						if (args.length == 3) {
							sender.sendMessage(Utils.chat("&cValid mob types"));
							sender.sendMessage(Utils.chat(mobs.values().toString()));
							return true;
						}
						// Args length 4
						if (mobs.get(i).equalsIgnoreCase(args[2])) {
							try {
								int price = Integer.parseInt(args[3]);
								ConfigurationSection mobSection = config.getConfigurationSection(mobs.get(i));
								mobSection.set("Sell", price);
								sender.sendMessage(Utils
										.chat("&e" + mobs.get(i) + "'s sell amount has been changed to &c$" + price));
								Bukkit.getPluginManager().getPlugin(plugin.getName()).saveConfig();

							} catch (NumberFormatException e) {
								sender.sendMessage(Utils.chat(useNumbersError));
								return true;
							}
							return true;
						}

					}

				}
				if (args[1].equalsIgnoreCase("status")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						if (!p.hasPermission(adminPerm)) {
							p.sendMessage(Utils.chat(noPermission.replace("%permission%", adminPerm)));
							return true;
						}
					}
					if (args.length == 2) {
						sender.sendMessage(Utils.chat("&e/spawners edit status [SpawnerType] [True/False]"));
						return true;
					}
					for (int i = 1; i <= mobs.size(); i = i + 1) {
						if (args.length == 3) {
							sender.sendMessage(Utils.chat("&cValid mob types"));
							sender.sendMessage(Utils.chat(mobs.values().toString()));
							return true;
						}
						// Args length 4
						if (mobs.get(i).equalsIgnoreCase(args[2])) {
							if (!args[3].equalsIgnoreCase("true") && !args[3].equalsIgnoreCase("false")) {
								sender.sendMessage(Utils.chat("&e/spawners edit status [SpawnerType] [True/False]"));
								return true;
							}
							boolean status = Boolean.parseBoolean(args[3]);
							ConfigurationSection mobSection = config.getConfigurationSection(mobs.get(i));
							mobSection.set("Enabled", status);
							if (status == false) {
								sender.sendMessage(
										Utils.chat("&e" + mobs.get(i) + "'s status has been changed to &cfalse"));
							}
							if (status == true) {
								sender.sendMessage(
										Utils.chat("&e" + mobs.get(i) + "'s status has been changed to &atrue"));
							}
							Bukkit.getPluginManager().getPlugin(plugin.getName()).saveConfig();
							return true;
						}

					}

				}
			}
		}
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				String consoleAttemptOpen = messages.getString("consoleAttemptOpen");
				sender.sendMessage(Utils.chat(consoleAttemptOpen));
				return true;
			}
			Player p = (Player) sender;

			String shopPerm = "guishopspawners.shop";
			if (p.hasPermission(shopPerm)) {
				String openingGUI = messages.getString("openingGUI");
				p.sendMessage(Utils.chat(openingGUI));
				p.openInventory(Main.GUI(p, plugin));
				// Just to refresh/update the inventory.
				p.getOpenInventory().countSlots();
				return true;
			} else {
				p.sendMessage(Utils.chat(noPermission.replace("%permission%", shopPerm)));
				return true;
			}
		}
		return false;
	}
}
