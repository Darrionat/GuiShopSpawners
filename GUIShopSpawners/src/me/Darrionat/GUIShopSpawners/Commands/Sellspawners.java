package me.Darrionat.GUIShopSpawners.Commands;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Darrionat.GUIShopSpawners.Main;
import me.Darrionat.GUIShopSpawners.Maps;
import me.Darrionat.GUIShopSpawners.Utils;
import me.Darrionat.GUIShopSpawners.Files.FileManager;
import net.milkbowl.vault.economy.EconomyResponse;

public class Sellspawners implements CommandExecutor {

	private Main plugin;

	public Sellspawners(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("sellspawner").setExecutor(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		FileManager fileManager = new FileManager((Main) plugin);
		FileConfiguration messages = fileManager.getDataConfig("messages");
		String noPermission = messages.getString("noPermission");
		ConfigurationSection config = plugin.getConfig();

		if (config.getBoolean("SellSpawnersEnabled") == false) {
			String sellingDisabled = messages.getString("sellingDisabled");
			sender.sendMessage(Utils.chat(sellingDisabled));
			return true;
		}
		if (!(sender instanceof Player)) {
			String consoleAttemptSell = messages.getString("consoleAttemptSell");
			sender.sendMessage(Utils.chat(consoleAttemptSell));
			return true;
		}
		Player p = (Player) sender;
		String sellPerm = "guishopspawners.sell";
		if (!p.hasPermission(sellPerm)) {
			p.sendMessage(Utils.chat(noPermission.replace("%permission%", sellPerm)));
			return true;
		}
		ItemStack hand = p.getItemInHand();
		Integer amt = hand.getAmount();
		if (hand.getType() != Material.SPAWNER) {
			String notASpawner = messages.getString("notASpawner");
			p.sendMessage(Utils.chat(notASpawner));
			return true;
		}
		String name = hand.getItemMeta().getDisplayName();
		Maps map = new Maps();
		HashMap<Integer, String> sellmobs = map.getSellMobMap();

		
		for (int i = 1; i <= sellmobs.size(); i++) {

			String mob = sellmobs.get(i);

			if (!(name.contains(Utils.chat(mob)))) {

				continue;
			}
			HashMap<Integer, String> mobs = map.getMobMap();
			ConfigurationSection mobSection = config.getConfigurationSection(mobs.get(i));
			int sellprice = mobSection.getInt("Sell");
			int total = sellprice * amt;
			EconomyResponse sell = Main.econ.depositPlayer(p.getName(), total);
			if (sell.transactionSuccess()) {
				hand.setAmount(0);
				p.sendMessage(Utils.chat("&aYou just sold x" + amt + "&e " + mob + " Spawner &afor $" + total));
			} else {
				String generalError = messages.getString("generalError");
				p.sendMessage(Utils.chat(generalError));
				System.out.println(Utils.chat("&c[GUIShopSpawners] " + p
						+ "'s economy deposit failed, do you have economy plugins (Like Vault and Essentials)? Contact Darrion (the developer) of this plugin."));
			}
			break;
		}
		return true;
	}
}
