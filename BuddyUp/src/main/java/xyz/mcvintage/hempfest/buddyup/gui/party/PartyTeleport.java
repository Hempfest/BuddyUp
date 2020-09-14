package xyz.mcvintage.hempfest.buddyup.gui.party;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import xyz.mcvintage.hempfest.buddyup.BuddyUp;
import xyz.mcvintage.hempfest.buddyup.gui.Manager;
import xyz.mcvintage.hempfest.buddyup.gui.MenuPaginated;

public class PartyTeleport extends MenuPaginated {
	
	public PartyTeleport(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return color("|&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬&r " + "&3&n&oPARTY " + "&r&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬&r|");
	}

	@Override
	public int getSlots() {
		return 54;
	}
	
	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		
		ArrayList<String> members = new ArrayList<String>(api.PartyMembers(api.getParty(p)));
		Material mat = e.getCurrentItem().getType();
		if (mat.equals(Material.PLAYER_HEAD)) {
				Player pl = Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getPersistentDataContainer()
						.get(new NamespacedKey(BuddyUp.get(), "uuid"), PersistentDataType.STRING));
				p.teleport(pl.getLocation());
				p.sendMessage(color(api.prefix + "&aTeleported to party member: " + pl.getName()));
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 8, 1);
		} else if (mat.equals(Material.BARRIER)) {

			// close inventory
			p.closeInventory();

		} else if (mat.equals(Material.DARK_OAK_BUTTON)) {
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
				if (page == 0) {
					p.sendMessage(ChatColor.GRAY + "You are already on the first page.");
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 8, 1);
				} else {
					page = page - 1;
					super.open();
				}
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
					.equalsIgnoreCase("Right")) {
				if (!((index + 1) >= members.size())) {
					page = page + 1;
					super.open();
				} else {
					p.sendMessage(ChatColor.GRAY + "You are on the last page.");
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 8, 1);
				}
			}
		}

		if (mat.equals(Material.TOTEM_OF_UNDYING)) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new PartyInfo(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 8, 1);
		}
	}

	@Override
	public void setMenuItems() {
		ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, this.color("&a&oGo back."), "");
		inventory.setItem(45, back);
		addMenuBorder();
		// The thing you will be looping through to place items
		ArrayList<String> members = new ArrayList<String>(api.PartyMembers(api.getParty(manager.getPlayerToFetch())));
		
		///////////////////////////////////// Pagination loop template
		if (members != null && !members.isEmpty()) {
			for (int i = 0; i < getMaxItemsPerPage(); i++) {
				index = getMaxItemsPerPage() * page + i;
				if (index >= members.size())
					break;
				if (members.get(index) != null) {
					///////////////////////////
					// Create an item from our collection and place it into the inventory
					ItemStack playerItem = new ItemStack(Material.PLAYER_HEAD, 1);
					ItemMeta playerMeta = playerItem.getItemMeta();
					if (members.get(index).equals(manager.getPlayerToFetch().getName())) {
						playerMeta.setDisplayName(this.color("&a&o&n&lYOU&b: &f&o" + members.get(index)));
					} else {
						playerMeta.setDisplayName(this.color("&3&o&nParty Member&b: &f&o" + members.get(index)));
					}
					
						playerMeta.getPersistentDataContainer().set(new NamespacedKey(BuddyUp.get(), "uuid"),
								PersistentDataType.STRING, members.get(index));
						playerItem.setItemMeta(playerMeta);
						
						inventory.addItem(playerItem);
					
					////////////////////////
				}
			}
			setFillerGlass();
		}
		////////////////////////

	}
}
