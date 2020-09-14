package xyz.mcvintage.hempfest.buddyup.gui.party;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import xyz.mcvintage.hempfest.buddyup.BuddyUp;
import xyz.mcvintage.hempfest.buddyup.gui.Menu;
import xyz.mcvintage.hempfest.buddyup.gui.Manager;
import xyz.mcvintage.hempfest.buddyup.gui.buddy.BuddyMenu;
import xyz.mcvintage.hempfest.buddyup.util.Config;
import xyz.mcvintage.hempfest.buddyup.util.enums.BuddyAction;


public class PartyMenu extends Menu {
	
	public PartyMenu(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return color("|&m▬▬▬▬▬▬▬▬▬▬▬&r " + "&3&oPARTY HELP " + "&r&m▬▬▬▬▬▬▬▬▬▬▬&r|");
	}

	@Override
	public int getSlots() {
		return 27;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Config data = new Config(p.getUniqueId().toString());
		Material mat = e.getCurrentItem().getType();
		if (mat.equals(Material.CRAFTING_TABLE)) {
			p.closeInventory();
			api.formParty(p, BuddyAction.OPEN);
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new PartyInfo(view).open();
			return;
		}
		if (mat.equals(Material.FURNACE)) {
			p.closeInventory();
			api.formParty(p, BuddyAction.CLOSED);
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new PartyInfo(view).open();
			return;
		}
		if (mat.equals(Material.IRON_DOOR)) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new PartyFinder(view).open();
			return;
		}
		if (mat.equals(Material.SKELETON_SKULL)) {
			if (api.isInParty(data, p)) {
				api.leaveParty(p);
				Manager view = BuddyUp.getMenuView(p);
				view.setPlayerToFetch(p);
				new PartyMenu(view).open();
			}
			return;
		}
		if (mat.equals(Material.TORCH)) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new PartyInfo(view).open();
		}
		if (mat.equals(Material.TOTEM_OF_UNDYING)) {
			Manager manager = BuddyUp.getMenuView(p);
			manager.setPlayerToFetch(p);
			new BuddyMenu(manager).open();
		}
	}

	@Override
	public void setMenuItems() {
		ItemStack c = makeItem(Material.CRAFTING_TABLE, "&e&oCreate a &3OPEN &e&oparty.", color("&a&lANYONE &2can join."));
		ItemStack cc = makeItem(Material.FURNACE, "&e&oCreate a &3CLOSED &e&oparty.", color("&6&lONLY &e&ofriends can join."));
		ItemStack j = makeItem(Material.IRON_DOOR, "&a&oJoin a &3party", "");
		ItemStack k = makeItem(Material.TORCH, "&3&oParty &a&oinformation", "");
		ItemStack l = makeItem(Material.SKELETON_SKULL, "&a&oLeave your &ccurrent &3party", "");
		ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, this.color("&a&oGo back."), "");
		inventory.setItem(19, back);
		Config data = new Config(manager.getPlayerToFetch().getUniqueId().toString());
		if (api.isInParty(data, manager.getPlayerToFetch())) {
			inventory.setItem(7, l);
		}
		if (api.isInParty(data, manager.getPlayerToFetch())) {
			inventory.setItem(5, k);
		}
		inventory.setItem(1, c);
		inventory.setItem(2, cc);
		inventory.setItem(3, j);
		setFillerGlass();

	}

}
