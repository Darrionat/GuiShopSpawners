package me.Darrionat.GUIShopSpawners.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.Darrionat.GUIShopSpawners.Main;

public class InventoryClick implements Listener {
	private Main plugin;

	public InventoryClick(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		String title = e.getInventory().getTitle();
		if (title.equals(Main.inventory_name)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}
			if (title.equals(Main.inventory_name)) {
				Main.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), plugin);
			}
		}
	}
}