package me.Darrionat.GUIShopSpawners.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Darrionat.GUIShopSpawners.Main;
import me.Darrionat.GUIShopSpawners.Utils;

public class Spawners implements CommandExecutor {

	private Main plugin;

	public Spawners(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("spawners").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length > 0) {
			if (!(args[0].equalsIgnoreCase("reload"))) {
				sender.sendMessage(Utils.chat("&cAvailable commands"));
				sender.sendMessage(Utils.chat("&e/spawners - Opens up the shop"));
				sender.sendMessage(Utils.chat("&e/spawners reload - Reloads the config"));
				return true;
			}
			if (!(sender instanceof Player)) {
				Bukkit.getPluginManager().getPlugin(plugin.getName()).reloadConfig();
				sender.sendMessage(Utils.chat("&a" + plugin.getName() + "'s config reloaded!"));
				return true;
			}
			Player p = (Player) sender;
			if (!p.hasPermission("guishopspawners.reload")) {
				p.sendMessage(Utils.chat("&cYou do not have the permission 'guishopspawners.reload'"));
				return true;
			}
			Bukkit.getPluginManager().getPlugin(plugin.getName()).reloadConfig();
			p.sendMessage(Utils.chat("&a" + plugin.getName() + "'s config reloaded!"));
			return true;

		}
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Utils.chat("&cOnly players may open a GUI!"));
				return true;
			}
			Player p = (Player) sender;
			p.openInventory(Main.GUI(p, plugin));
			return true;
		}
		return false;
	}
}
