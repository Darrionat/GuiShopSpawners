package me.Darrionat.GUIShopSpawners.Files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Darrionat.GUIShopSpawners.GuiShopSpawners;
import me.Darrionat.GUIShopSpawners.Utils;

public class FileManager {

	private GuiShopSpawners plugin;

	public FileManager(GuiShopSpawners plugin) {
		this.plugin = plugin;
	}

	// Files and File configurations here
	public static FileConfiguration dataConfig;
	public File dataFile;
	// -------------------------------------

	public void setup(String fileName) {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		dataFile = new File(plugin.getDataFolder(), fileName + ".yml");

		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
				dataConfig = YamlConfiguration.loadConfiguration(dataFile);
				String successMessage = "&e[" + plugin.getName() + "] &aCreated the " + fileName + ".yml file";
				Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(successMessage));
			} catch (IOException exe) {
				String failMessage = "&e[" + plugin.getName() + "] &cFailed to create the " + fileName + ".yml file";
				Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(failMessage));
				exe.printStackTrace();
			}
		}

	}

	public boolean fileExists(String fileName) {
		dataFile = new File(plugin.getDataFolder(), fileName + ".yml");
		if (dataFile.exists()) {
			return true;
		}
		return false;
	}

	public void deleteFile(String fileName) {
		dataFile = new File(plugin.getDataFolder(), fileName + ".yml");
		dataFile.delete();
		return;
	}

	public FileConfiguration getDataConfig(String fileName) {
		dataFile = new File(plugin.getDataFolder(), fileName + ".yml");
		dataConfig = YamlConfiguration.loadConfiguration(dataFile);
		return dataConfig;
	}

	public File getDataFile(String fileName) {
		dataFile = new File(plugin.getDataFolder(), fileName + ".yml");
		return dataFile;
	}

	public void reloadDataFile(String fileName) {
		dataFile = new File(plugin.getDataFolder(), fileName + ".yml");
		dataConfig = YamlConfiguration.loadConfiguration(dataFile);
		String reloadMessage = "&e[" + plugin.getName() + "] &aReloaded the " + fileName + ".yml file";
		Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat(reloadMessage));
	}

	public void saveConfigFile(String fileName, FileConfiguration dataConfig) {
		dataFile = new File(plugin.getDataFolder(), fileName + ".yml");
		try {
			dataConfig.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}