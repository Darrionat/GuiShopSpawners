package me.Darrionat.GUIShopSpawners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Darrionat.GUIShopSpawners.Commands.Sellspawners;
import me.Darrionat.GUIShopSpawners.Commands.Spawners;
import me.Darrionat.GUIShopSpawners.Listeners.InventoryClick;
import me.Darrionat.GUIShopSpawners.Listeners.PlayerJoin;
import me.Darrionat.GUIShopSpawners.UI.Qty;
import me.Darrionat.GUIShopSpawners.UI.SpawnersUI;
import me.Darrionat.GUIShopSpawners.bStats.Metrics;
import net.milkbowl.vault.economy.Economy;

public class GuiShopSpawners extends JavaPlugin {

	public static Economy econ = null;

	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			getLogger().severe(
					String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		new Spawners(this);
		SpawnersUI.initialize(this);
		Qty.initialize();
		new InventoryClick(this);
		new Sellspawners(this);
		new PlayerJoin(this);

		Metrics metrics = new Metrics(this);
		metrics.addCustomChart(new Metrics.SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
		ConfigurationSection config = this.getConfig();
		Maps map = new Maps();
		HashMap<Integer, String> mobs = map.getMobMap();

		for (int i = 1; i <= mobs.size(); i = i + 1) {
			ConfigurationSection mobSection = config.getConfigurationSection(mobs.get(i));
			if (mobSection.getBoolean("Enabled") == true) {
				System.out.println(Utils.chat("&e[" + this.getName() + "] &a" + mobs.get(i) + " enabled."));
			}
			if (mobSection.getBoolean("Enabled") == false) {
				System.out.println(Utils.chat("&e[" + this.getName() + "] &c" + mobs.get(i) + " disabled."));
			}
		}

		saveDefaultConfig();
		loadConfig();
		this.saveResource("messages.yml", false);

		if (getConfig().getBoolean("checkUpdates") == true) {
			int id = 69279;
			UpdateChecker updater = new UpdateChecker(this, id);
			try {
				if (updater.checkForUpdates()) {
					String name = getDescription().getName();
					String version = "v" + getDescription().getVersion();
					getServer().getConsoleSender()
							.sendMessage(Utils.chat("&7" + name + ": &bYou are currently running version " + version));
					getServer().getConsoleSender().sendMessage(Utils.chat("&bAn update for &7" + name + "&f("
							+ UpdateChecker.getLatestVersion() + ") &bis available at"));
					getServer().getConsoleSender().sendMessage(Utils.chat(updater.getResourceURL()));
				} else {
					getServer().getConsoleSender().sendMessage(
							getDescription().getName() + " Plugin is up to date! - " + getDescription().getVersion());
				}
			} catch (Exception e) {
				getLogger().info("Could not check for updates! Stacktrace:");
				e.printStackTrace();
			}
		}
	}

	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	// End of inventory
	public void onDisable() {

	}
}
