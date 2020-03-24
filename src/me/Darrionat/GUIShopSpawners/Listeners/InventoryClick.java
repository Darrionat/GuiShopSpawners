package me.Darrionat.GUIShopSpawners.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.Darrionat.GUIShopSpawners.GuiShopSpawners;
import me.Darrionat.GUIShopSpawners.UI.Qty;
import me.Darrionat.GUIShopSpawners.UI.SpawnersUI;

public class InventoryClick implements Listener {
	private GuiShopSpawners plugin;

	public InventoryClick(GuiShopSpawners plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		String title = e.getView().getTitle();
		if (title.equals(SpawnersUI.inventory_name)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}
			SpawnersUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), plugin);
		}
		if (title.equals(Qty.inventory_name)) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}
			Qty.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory(), plugin);
		}

	}
}