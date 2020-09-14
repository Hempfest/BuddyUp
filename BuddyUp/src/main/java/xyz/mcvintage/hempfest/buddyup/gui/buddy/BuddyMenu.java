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
import xyz.mcvintage.hempfest.buddyup.gui.party.PartyMenu;


public class BuddyMenu extends Menu {
	
	public BuddyMenu(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return this.color("|&m▬▬▬▬▬▬▬▬▬▬▬▬&r " + api.prefix + "&m▬▬▬▬▬▬▬▬▬▬▬▬&r|");
	}

	@Override
	public int getSlots() {
		return 54;
	}

	
	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Material mat = e.getCurrentItem().getType();
		if (mat == Material.COMPASS) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new OnlinePlayers(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 8, 1);
		}
		
		if (mat == Material.GOLD_NUGGET) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new BuddyList(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 8, 1);
		}
		
		if (mat == Material.BOOK) {
			p.closeInventory();
			sendHelpMenu(p);
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 8, 1);
		}
		
		if (mat == Material.ENCHANTED_BOOK) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new BuddyIncomingList(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 8, 1);
		}
		
		if (mat == Material.SPECTRAL_ARROW) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new BuddyOutgoingList(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 8, 1);
		}
		
		if (mat == Material.FIREWORK_STAR) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new PartyMenu(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 8, 1);
		}

	}

	@Override
	public void setMenuItems() {
		ItemStack author = makeItem(Material.WRITABLE_BOOK, this.color(api.prefix + "&3&oAuthor"), this.color("&n&2&oH&ae&2&om&ap&ffest"));
		ItemStack yes = makeItem(Material.COMPASS, this.color("Add an online user as a friend"), this.color("Click me to view the online user list"));
		ItemStack no = makeItem(Material.GOLD_NUGGET, this.color("Manage your friends list"), this.color("Click me to view your friend list"));
		ItemStack party = makeItem(Material.FIREWORK_STAR, this.color("View the party GUI"), this.color("Click me to change GUI's"));
		ItemStack list = makeItem(Material.BOOK, this.color("View the list of commands via text."), this.color("Click me to view the full command list."));
		ItemStack list2 = makeItem(Material.ENCHANTED_BOOK, this.color("&oFriend Requests [&b" + api.getRequests(manager.getPlayerToFetch()).size() + "&r&o]"), this.color("Click me to view your current requests."));
		ItemStack sent = makeItem(Material.SPECTRAL_ARROW, this.color("&oSent Requests [&a" + api.getSentRequests(manager.getPlayerToFetch()).size() + "&r&o]"), this.color("Click me to manage sent friend requests."));
		inventory.setItem(13, list);
		inventory.setItem(20, yes);
		inventory.setItem(24, no);
		inventory.setItem(31, list2);
		inventory.setItem(43, party);
		inventory.setItem(49, author);

		if (api.getSentRequests(manager.getPlayerToFetch()).size() > 0) {
			inventory.setItem(37, sent);
		}
		setFillerGlass();

	}

}
