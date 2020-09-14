package xyz.mcvintage.hempfest.buddyup.gui.party;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import xyz.mcvintage.hempfest.buddyup.BuddyUp;
import xyz.mcvintage.hempfest.buddyup.gui.Menu;
import xyz.mcvintage.hempfest.buddyup.gui.Manager;
import xyz.mcvintage.hempfest.buddyup.util.Config;


public class PartyInfo extends Menu {
	
	public PartyInfo(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return color("|&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬&r " + "&3&oPARTY " + "&r&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬&r|");
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
		if (mat.equals(Material.SKELETON_SKULL)) {
			if (api.isInParty(data, p)) {
				api.leaveParty(p);
				Manager view = BuddyUp.getMenuView(p);
				view.setPlayerToFetch(p);
				new PartyMenu(view).open();
			}
			return;
		}
		if (mat.equals(Material.PLAYER_HEAD)) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new PartyInviter(view).open();
			return;
		}
		
		if (mat.equals(Material.TOTEM_OF_UNDYING)) {
			Manager manager = BuddyUp.getMenuView(p);
			manager.setPlayerToFetch(p);
			new PartyMenu(manager).open();
		}
		
		if (mat.equals(Material.PURPLE_STAINED_GLASS_PANE)) {
			p.closeInventory();
			Bukkit.dispatchCommand(p, "party chat");
		}
		
		if (mat.equals(Material.ACACIA_FENCE_GATE)) {
			Manager manager = BuddyUp.getMenuView(p);
			manager.setPlayerToFetch(p);
			new PartyTeleport(manager).open();
		}
	}

	@Override
	public void setMenuItems() {
		ArrayList<String> members = new ArrayList<String>(api.PartyMembers(api.getParty(manager.getPlayerToFetch())));
		ItemStack c = makeItem(Material.ACACIA_BOAT, "&r[&6&oMembers&r] - &e" + api.PartyMembers(api.getParty(manager.getPlayerToFetch())).size(), members);
		ItemStack j = makeItem(Material.REDSTONE_TORCH, "&r[&6&oLeader&r] - &e" + api.getPartyLeader(manager.getPlayerToFetch()), "");
		ItemStack n = makeItem(Material.PLAYER_HEAD, "&e&oInvite players.", "");
		ItemStack p = makeItem(Material.ACACIA_FENCE_GATE, "&e&oTeleport to members.", "");
		ItemStack l = makeItem(Material.SKELETON_SKULL, "&a&oLeave the &3party", "");
		ItemStack o = makeItem(Material.PURPLE_STAINED_GLASS_PANE, "&b&o&nEnter &3Party &b&o&nchat.", "");
		ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, this.color("&a&oGo back."), "");
		inventory.setItem(19, back);
		inventory.setItem(25, o);
			inventory.setItem(7, l);
			inventory.setItem(5, n);
			inventory.setItem(4, p);
		inventory.setItem(1, c);
		inventory.setItem(2, j);
		setFillerGlass();

	}

}
