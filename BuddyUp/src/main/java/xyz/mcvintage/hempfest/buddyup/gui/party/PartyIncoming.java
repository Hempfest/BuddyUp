package xyz.mcvintage.hempfest.buddyup.gui.party;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.mcvintage.hempfest.buddyup.BuddyUp;
import xyz.mcvintage.hempfest.buddyup.gui.Menu;
import xyz.mcvintage.hempfest.buddyup.gui.Manager;


public class PartyIncoming extends Menu {
	
	public PartyIncoming(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return manager.getPlayerToEdit() + "'s Party";
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
			new PartyFinder(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 8, 1);
			return;
		}
		
		
		if (mat == Material.DIRT) {
			p.closeInventory();
			
			api.joinParty(p, Bukkit.getPlayer(manager.getPlayerToEdit()));
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 8, 1);
			return;
		}

	}

	@Override
	public void setMenuItems() {
		ItemStack yes = new ItemStack(Material.DIRT, 1);
		ItemMeta yes_meta = yes.getItemMeta();
		yes_meta.setDisplayName(this.color("&a&oJoin this players party."));
		yes_meta.setLore(Arrays.asList(this.color("&a&oClick to join their party.")));
		yes.setItemMeta(yes_meta);
		ItemStack back = new ItemStack(Material.DARK_OAK_BUTTON, 1);
		ItemMeta back_meta = back.getItemMeta();
		back_meta.setDisplayName(ChatColor.DARK_RED + "Go back");
		back.setItemMeta(back_meta);

		inventory.setItem(3, yes);
		inventory.setItem(8, back);

		setFillerGlass();

	}

}
