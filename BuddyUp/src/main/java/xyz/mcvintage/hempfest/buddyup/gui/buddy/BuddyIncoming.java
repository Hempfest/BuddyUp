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


public class BuddyIncoming extends Menu {
	
	public BuddyIncoming(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return color("&5&oAdd " + manager.getPlayerToEdit() + " as a friend!");
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
			api.perform(BuddyAction.BEFRIEND, p, manager.getPlayerToEdit());
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 8, 1);
		}
		
		if (mat == Material.DARK_OAK_BUTTON) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new OnlinePlayers(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 8, 1);
		}

	}

	@Override
	public void setMenuItems() {
		ItemStack add = makeItem(Material.EMERALD, color("&a&l&oAdd &r" + manager.getPlayerToEdit() + " &aas a friend."), "");
		ItemStack back = makeItem(Material.DARK_OAK_BUTTON, color("&a&oGo back."), "");
		inventory.setItem(3, add);
		inventory.setItem(8, back);
		setFillerGlass();

	}

}
