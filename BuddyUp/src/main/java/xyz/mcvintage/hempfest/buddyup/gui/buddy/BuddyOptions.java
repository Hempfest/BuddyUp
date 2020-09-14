package xyz.mcvintage.hempfest.buddyup.gui.buddy;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.mcvintage.hempfest.buddyup.BuddyUp;
import xyz.mcvintage.hempfest.buddyup.gui.Menu;
import xyz.mcvintage.hempfest.buddyup.util.enums.BuddyAction;
import xyz.mcvintage.hempfest.buddyup.gui.Manager;


public class BuddyOptions extends Menu {
	
	public BuddyOptions(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return "";
	}

	@Override
	public int getSlots() {
		return 9;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Material mat = e.getCurrentItem().getType();
		if (mat == Material.DARK_OAK_BUTTON) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new BuddyList(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 8, 1);
		}
		
		if (mat == Material.BOOKSHELF) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToEdit(manager.getPlayerToEdit());
			new BuddyMailer(view).open();
		}
		
		if (mat == Material.DIRT) {
			p.closeInventory();
			api.perform(BuddyAction.UNFRIEND, p, manager.getPlayerToEdit());
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 8, 1);
		}

	}

	@Override
	public void setMenuItems() {
		ItemStack yes = new ItemStack(Material.DIRT, 1);
		ItemMeta yes_meta = yes.getItemMeta();
		yes_meta.setDisplayName(this.color("&a&oRemove friend"));
		yes_meta.setLore(Arrays.asList(this.color("&a&oClick to remove friendship.")));
		yes.setItemMeta(yes_meta);
		ItemStack back = new ItemStack(Material.DARK_OAK_BUTTON, 1);
		ItemMeta back_meta = back.getItemMeta();
		back_meta.setDisplayName(ChatColor.DARK_RED + "Go back");
		back.setItemMeta(back_meta);
		ItemStack info = new ItemStack(Material.BOOKSHELF, 1);
		ItemMeta info_meta = info.getItemMeta();
		info_meta.setDisplayName(this.color("&e&oSend mail"));
		info_meta.setLore(Arrays.asList(this.color("&e&oClick to send mail to your friend.")));
		info.setItemMeta(info_meta);

		inventory.setItem(3, yes);
		inventory.setItem(5, info);
		inventory.setItem(8, back);

		setFillerGlass();

	}

}
