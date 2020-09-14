package xyz.mcvintage.hempfest.buddyup.gui.buddy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import xyz.mcvintage.hempfest.buddyup.BuddyUp;
import xyz.mcvintage.hempfest.buddyup.gui.Menu;
import xyz.mcvintage.hempfest.buddyup.gui.Manager;
import xyz.mcvintage.hempfest.buddyup.gui.OnlinePlayers;
import xyz.mcvintage.hempfest.buddyup.util.enums.BuddyAction;


public class BuddyOutgoing extends Menu {
	
	public BuddyOutgoing(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return color("&5&oCancel your request to " + manager.getPlayerToEdit() + " ?");
	}

	@Override
	public int getSlots() {
		return 9;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Material mat = e.getCurrentItem().getType();
		if (mat == Material.EMERALD) {
			api.perform(BuddyAction.CANCEL_REQUEST, p, manager.getPlayerToEdit());
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 8, 1);
			p.closeInventory();
		}
		
		if (mat == Material.DARK_OAK_BUTTON) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new OnlinePlayers(view).open();
		}

	}

	@Override
	public void setMenuItems() {
		ItemStack add = makeItem(Material.EMERALD, color("&a&l&oYes"), "");
		ItemStack back = makeItem(Material.DARK_OAK_BUTTON, color("&4&oNo, &a&oGo back."), "");
		inventory.setItem(3, add);
		inventory.setItem(8, back);
		setFillerGlass();

	}

}
