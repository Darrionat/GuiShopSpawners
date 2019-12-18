package me.Darrionat.GUIShopSpawners.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.Darrionat.GUIShopSpawners.Main;
import me.Darrionat.GUIShopSpawners.UpdateChecker;
import me.Darrionat.GUIShopSpawners.Utils;
import me.Darrionat.GUIShopSpawners.Files.ConfigManager;

public class PlayerJoin implements Listener {

	private Main plugin;
	private static ConfigManager messages;

	public PlayerJoin(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		messages = new ConfigManager((Main) plugin);

		if (plugin.getConfig().getBoolean("checkUpdates") != true) {
			return;
		}
		UpdateChecker updater = new UpdateChecker(plugin, 69279);
		try {
			if (updater.checkForUpdates()) {
				Player p = e.getPlayer();
				if (!p.isOp()) {
					return;
				}
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

					public void run() {
						String version = "v" + plugin.getDescription().getVersion();
						String currentVersionMsg = messages.getMessage("updatePt1").replace("%CurrentVersion%",
								version);
						String updateAvailable = messages.getMessage("updatePt2").replace("%UpdatedVersion%",
								UpdateChecker.getLatestVersion());
						p.sendMessage(Utils.chat(currentVersionMsg));
						p.sendMessage(Utils.chat(updateAvailable));
						p.sendMessage(Utils.chat("&ehttps://www.spigotmc.org/resources/gui-shop-spawners.69279/"));
					}
				}, 30L);// 60 L == 3 sec, 20 ticks == 1 sec

			}
		} catch (Exception exe) {
			plugin.getLogger().info("Could not check for updates! Stacktrace:");
			exe.printStackTrace();
		}

	}
}