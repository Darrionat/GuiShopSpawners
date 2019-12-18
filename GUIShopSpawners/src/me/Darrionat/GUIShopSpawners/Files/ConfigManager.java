package me.Darrionat.GUIShopSpawners.Files;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Darrionat.GUIShopSpawners.Main;
import me.Darrionat.GUIShopSpawners.Utils;

public class ConfigManager {

	private Main plugin;

	public ConfigManager(Main plugin) {
		this.plugin = plugin;
	}

	// Files and File configurations here
	public static FileConfiguration messagesConfig;
	public File messagesFile;
	// -------------------------------------

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		messagesFile = new File(plugin.getDataFolder(), "messages.yml");

		if (!messagesFile.exists()) {
			try {
				messagesFile.createNewFile();
				messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
				String successMessage = "&e[" + plugin.getName() + "] &aCreated the messages.yml file";
				Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(successMessage));
				saveMessages();
			} catch (IOException exe) {
				String failMessage = "&e[" + plugin.getName() + "] &cFailed to create the messages.yml file";
				Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(failMessage));
				exe.printStackTrace();
			}
		}

	}

	public FileConfiguration getMessages() {

		return messagesConfig;
	}

	public void saveMessages() {
		try {
			messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

			messagesConfig.set("successfulTransaction", "&a$%amt% &ehas been removed from your account");
			messagesConfig.set("notEnoughMoney", "&cYou do not have enough money for this purchase.");
			messagesConfig.set("noPermission", "&cYou do not have the permission '%permission%'");
			messagesConfig.set("openingGUI", "&aOpening the shop...");
			messagesConfig.set("consoleAttemptOpen", "&cOnly players may open a GUI!");
			messagesConfig.set("consoleAttemptSell", "&cOnly players may sell a spawner!");
			messagesConfig.set("useNumbersError", "&cYou must use a number for the price!");
			messagesConfig.set("sellingDisabled",
					"&cSelling spawners is disabled! Enable it in the config.yml of GUIShopSpawners.");
			messagesConfig.set("notASpawner", "&cYou must be holding a mob spawner for this!");
			messagesConfig.set("generalError", "Something went wrong here. Please contact an Admin.");
			messagesConfig.set("updatePt1",
					"&7GUIShopSpawners: &bYou are currently running version %CurrentVersion%");
			messagesConfig.set("updatePt2",
					"&bAn update for &7GUIShopSpawners &f(%UpdatedVersion%) &bis available at");
			messagesConfig.save(messagesFile);
			String successMessage = "&e[" + plugin.getName() + "] &aSaved the messages.yml file";
			Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(successMessage));

		} catch (IOException exe) {
			String failMessage = "&e[" + plugin.getName() + "] &cFailed to save the messages.yml file";
			Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(failMessage));
			exe.printStackTrace();
		}
	}

	public String getMessage(String messageName) {
		String s = messagesConfig.getString(messageName);
		return s;
	}

	public void reloadMessages() {
		messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
		String reloadMessage = "&e[" + plugin.getName() + "] &aReloaded the messages.yml file";
		Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(reloadMessage));
	}
}
