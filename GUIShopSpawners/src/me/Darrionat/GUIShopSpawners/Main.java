package me.Darrionat.GUIShopSpawners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Darrionat.GUIShopSpawners.Commands.Spawners;
import me.Darrionat.GUIShopSpawners.Listeners.InventoryClick;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Main extends JavaPlugin {

	public static Economy econ = null;

	public void onEnable() {
		if (!setupEconomy()) {
			getLogger().severe(
					String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		new Spawners(this);
		Main.initialize(this);
		new InventoryClick(this);
		saveDefaultConfig();
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

	// Pasted
	public static Inventory inv;
	public static String inventory_name;
	public static int inv_rows = 5 * 9;

	public static void initialize(JavaPlugin plugin) {
		inventory_name = Utils.chat(plugin.getConfig().getString("GUIName"));

		inv = Bukkit.createInventory(null, inv_rows);
	}

	public static Inventory GUI(Player p, JavaPlugin plugin) {
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, inventory_name);

		Utils.createItem(inv, 52, 1, 1, "&eCreeper &fSpawner", "&a$" + plugin.getConfig().getInt("Creeper"));
		Utils.createItem(inv, 52, 1, 2, "&eSkeleton &fSpawner", "&a$" + plugin.getConfig().getInt("Skeleton"));
		Utils.createItem(inv, 52, 1, 3, "&eSpider &fSpawner", "&a$" + plugin.getConfig().getInt("Spider"));
		Utils.createItem(inv, 52, 1, 4, "&eIron Golem &fSpawner", "&a$" + plugin.getConfig().getInt("IronGolem"));
		Utils.createItem(inv, 52, 1, 5, "&eZombie &fSpawner", "&a$" + plugin.getConfig().getInt("Zombie"));
		Utils.createItem(inv, 52, 1, 6, "&eSlime &fSpawner", "&a$" + plugin.getConfig().getInt("Slime"));
		Utils.createItem(inv, 52, 1, 7, "&eWitch &fSpawner", "&a$" + plugin.getConfig().getInt("Witch"));
		Utils.createItem(inv, 52, 1, 8, "&eZombie Pigman &fSpawner", "&a$" + plugin.getConfig().getInt("ZombiePigman"));
		Utils.createItem(inv, 52, 1, 9, "&eEnderman &fSpawner", "&a$" + plugin.getConfig().getInt("Enderman"));
		Utils.createItem(inv, 52, 1, 10, "&eCave Spider &fSpawner", "&a$" + plugin.getConfig().getInt("CaveSpider"));
		Utils.createItem(inv, 52, 1, 11, "&eVindicator &fSpawner", "&a$" + plugin.getConfig().getInt("Vindicator"));
		Utils.createItem(inv, 52, 1, 12, "&eCow &fSpawner", "&a$" + plugin.getConfig().getInt("Cow"));
		Utils.createItem(inv, 52, 1, 13, "&eRabbit &fSpawner", "&a$" + plugin.getConfig().getInt("Rabbit"));
		Utils.createItem(inv, 52, 1, 14, "&eSheep &fSpawner", "&a$" + plugin.getConfig().getInt("Sheep"));
		Utils.createItem(inv, 52, 1, 15, "&eHorse &fSpawner", "&a$" + plugin.getConfig().getInt("Horse"));
		Utils.createItem(inv, 52, 1, 16, "&eBlaze &fSpawner", "&a$" + plugin.getConfig().getInt("Blaze"));
		Utils.createItem(inv, 52, 1, 17, "&ePig &fSpawner", "&a$" + plugin.getConfig().getInt("Pig"));
		Utils.createItem(inv, 52, 1, 18, "&eChicken &fSpawner", "&a$" + plugin.getConfig().getInt("Chicken"));
		Utils.createItem(inv, 52, 1, 19, "&eSquid &fSpawner", "&a$" + plugin.getConfig().getInt("Squid"));
		Utils.createItem(inv, 52, 1, 20, "&eWolf &fSpawner", "&a$" + plugin.getConfig().getInt("Wolf"));
		Utils.createItem(inv, 52, 1, 21, "&eMooshroom &fSpawner", "&a$" + plugin.getConfig().getInt("Mooshroom"));
		Utils.createItem(inv, 166, 1, 41, "&4Close Menu");

		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	@SuppressWarnings({ "deprecation" })
	public static void clicked(Player p, int slot, ItemStack clicked, Inventory inv, JavaPlugin plugin) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&4Close Menu"))) {
			p.closeInventory();

		}

		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eCreeper &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Creeper"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("creeper")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("CreeperMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Creeper") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eSkeleton &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Skeleton"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("skeleton")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("SkeletonMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Skeleton") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eSpider &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Spider"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("spider")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("SpiderMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Spider") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eIron Golem &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("IronGolem"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("irongolem")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("IronGolemMsg")));
					p.sendMessage(Utils.chat(
							"&a$" + plugin.getConfig().getInt("IronGolem") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eZombie &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Zombie"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("zombie")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("ZombieMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Zombie") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eSlime &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Slime"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("slime")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("SlimeMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Slime") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eWitch &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Witch"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("witch")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("WitchMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Witch") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eZombie Pigman &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("ZombiePigman"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("zombiepigman")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("ZombiePigmanMsg")));
					p.sendMessage(Utils.chat(
							"&a$" + plugin.getConfig().getInt("ZombiePigman") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eEnderman &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Enderman"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("enderman")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("EndermanMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Enderman") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eCave Spider &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("CaveSpider"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("cavespider")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("CaveSpiderMsg")));
					p.sendMessage(Utils.chat(
							"&a$" + plugin.getConfig().getInt("CaveSpider") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eVindicator &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Vindicator"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("vindicator")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("VindicatorMsg")));
					p.sendMessage(Utils.chat(
							"&a$" + plugin.getConfig().getInt("Vindicator") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eCow &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Cow"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("cow")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("CowMsg")));
					p.sendMessage(
							Utils.chat("&a$" + plugin.getConfig().getInt("Cow") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eRabbit &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Rabbit"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("rabbit")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("RabbitMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Rabbit") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eSheep &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Sheep"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("sheep")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("SheepMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Sheep") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eHorse &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Horse"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("horse")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("HorseMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Horse") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eBlaze &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Blaze"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("blaze")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("BlazeMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Blaze") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&ePig &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Pig"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("pig")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("PigMsg")));
					p.sendMessage(
							Utils.chat("&a$" + plugin.getConfig().getInt("Pig") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eChicken &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Chicken"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("chicken")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("ChickenMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Chicken") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eSquid &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Squid"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("squid")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("SquidMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Squid") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eWolf &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Wolf"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("wolf")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("WolfMsg")));
					p.sendMessage(Utils
							.chat("&a$" + plugin.getConfig().getInt("Wolf") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&eMooshroom &fSpawner"))) {
			EconomyResponse r = econ.withdrawPlayer(p.getName(), plugin.getConfig().getInt("Mooshroom"));
			if (r.transactionSuccess()) {
				for (String remove : plugin.getConfig().getStringList("mooshroom")) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
							remove.replace("%player%", p.getName()));
					p.sendMessage(Utils.chat(plugin.getConfig().getString("MooshroomMsg")));
					p.sendMessage(Utils.chat(
							"&a$" + plugin.getConfig().getInt("Mooshroom") + " &ewas removed from your account."));
				}
			} else {
				p.sendMessage(Utils.chat("&cYou cannot afford this spawner."));
			}
		}
	}

	// End of paste
	public void onDisable() {

	}
}
