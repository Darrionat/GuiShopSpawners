package me.Darrionat.GUIShopSpawners;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.Darrionat.GUIShopSpawners.Commands.Sellspawners;
import me.Darrionat.GUIShopSpawners.Commands.Spawners;
import me.Darrionat.GUIShopSpawners.Listeners.InventoryClick;
import me.Darrionat.GUIShopSpawners.Listeners.PlayerJoin;
import me.Darrionat.GUIShopSpawners.UI.Qty;
import me.Darrionat.GUIShopSpawners.bStats.Metrics;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

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
		Main.initialize(this);
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
			UpdateChecker updater = new UpdateChecker(this, 69279);
			try {
				if (updater.checkForUpdates()) {

					String version = "v" + this.getDescription().getVersion();
					getServer().getConsoleSender().sendMessage(
							Utils.chat("&7GUIShopSpawners: &bYou are currently running version " + version));
					getServer().getConsoleSender().sendMessage(Utils.chat("&bAn update for &7GUIShopSpawners &f("
							+ UpdateChecker.getLatestVersion() + ") &bis available at:"));
					getServer().getConsoleSender()
							.sendMessage(Utils.chat("&ehttps://www.spigotmc.org/resources/gui-shop-spawners.69279/"));
				} else {
					getServer().getConsoleSender()
							.sendMessage("[GUIShopSpawners] Plugin is up to date! - " + getDescription().getVersion());
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

	// Start of inventory
	public static Inventory inv;
	public static String inventory_name;
	public static int inv_rows = 6 * 9;

	public static void initialize(JavaPlugin plugin) {
		inventory_name = Utils.chat(plugin.getConfig().getString("GUIName"));

		inv = Bukkit.createInventory(null, inv_rows);
	}

	public static Inventory GUI(Player p, JavaPlugin plugin) {
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, inventory_name);

		ConfigurationSection config = plugin.getConfig();
		Maps map = new Maps();
		HashMap<Integer, String> mobs = map.getMobMap();
		HashMap<Integer, String> skulls = map.getSkullMap();

		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
		// SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		// DecimalFormat formatter = new DecimalFormat("#,###");

		String fillItemID = config.getString("Fill Item ID").toUpperCase();
		Material fillItemMaterial = Material.getMaterial(fillItemID);

		if (config.getBoolean("Fill Item Enabled") == true) {
			for (int i = 1; i <= inv_rows; i++) {
				Utils.createItem(inv, fillItemMaterial, 1, i, "&f");
			}
		}
		for (int i = 1; i <= skulls.size(); i = i + 1) {

			ConfigurationSection mobSection = config.getConfigurationSection(mobs.get(i));
			if (mobSection.getBoolean("Enabled") == false) {
				continue;
			}
			// @SuppressWarnings("deprecation")
			/*
			 * OfflinePlayer skullplayer =
			 * plugin.getServer().getOfflinePlayer(skulls.get(i));
			 * skullMeta.setOwningPlayer(skullplayer); skull.setItemMeta(skullMeta);
			 */
			String URL = skulls.get(i);
			createSkull(skull, URL);

			int slot = mobSection.getInt("Slot");
			String name = "&e" + mobs.get(i) + " &fSpawner";
			int price = mobSection.getInt("Buy");
			int sellprice = mobSection.getInt("Sell");

			String buy = "&aBuy: $" + price;
			String sell = "&cSell: $" + sellprice;

			Utils.createskullItem(inv, skull, 1, slot, name, buy, sell);

		}

		// The clicked code will be processed by Qty.java
		if (p.hasPermission("guishopspawners.sell")) {
			Utils.createItem(inv, Material.HOPPER, 1, 51, Utils.chat(config.getString("SellSpawnerItem")));
		}
		Utils.createItem(inv, Material.NETHER_STAR, 1, 50, Utils.chat(config.getString(("CloseMenuItem"))));
		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static void clicked(Player p, int slot, ItemStack clicked, Inventory inv, JavaPlugin plugin) {
		if (clicked.getItemMeta().getDisplayName()
				.equalsIgnoreCase(Utils.chat(plugin.getConfig().getString("CloseMenuItem")))) {
			p.closeInventory();
		}
		if (clicked.getItemMeta().getDisplayName()
				.equalsIgnoreCase(Utils.chat(plugin.getConfig().getString("SellSpawnerItem")))) {
			p.chat("/sellspawner");
			return;
		}
		Maps map = new Maps();
		HashMap<Integer, String> mobs = map.getMobMap();
		HashMap<Integer, String> skulls = map.getSkullMap();

		for (int i = 1; i <= mobs.size(); i = i + 1) {

			if (clicked.getItemMeta().getDisplayName()
					.equalsIgnoreCase(Utils.chat("&e" + mobs.get(i) + " &fSpawner"))) {

				String mob = mobs.get(i);
				String URL = skulls.get(i);
				ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
				createSkull(skull, URL);

				new Qty((Main) plugin, mob, skull);
				p.openInventory(Qty.GUI(p, plugin));
				break;
			}
		}
	}

	public static ItemStack createSkull(ItemStack head, String url) {
		if (url.isEmpty())
			return head;

		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);

		profile.getProperties().put("textures", new Property("textures", url));

		try {
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);

		} catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
			error.printStackTrace();
		}
		head.setItemMeta(headMeta);
		return head;
	}

	// End of inventory
	public void onDisable() {

	}
}
