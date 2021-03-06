package xyz.mcvintage.hempfest.buddyup.gui.buddy;

import java.util.ArrayList;

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

public class BuddyIncomingList extends MenuPaginated {
	
	public BuddyIncomingList(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return this.color("&3&oFriend Request List |&m▬▬▬▬▬▬▬▬▬");
	}

	@Override
	public int getSlots() {
		return 54;
	}
	
	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		
		ArrayList<String> requests = new ArrayList<String>(api.getRequests(p));
		Material mat = e.getCurrentItem().getType();
		if (mat.equals(Material.END_CRYSTAL)) {

			Manager manager = BuddyUp.getMenuView(p);
			manager.setPlayerToEdit(e.getCurrentItem().getItemMeta().getPersistentDataContainer()
					.get(new NamespacedKey(BuddyUp.get(), "uuid"), PersistentDataType.STRING));

			new BuddyInvite(manager).open();
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
				if (!((index + 1) >= requests.size())) {
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
			new BuddyMenu(view).open();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 8, 1);
		}
	}

	@Override
	public void setMenuItems() {
		ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, this.color("&a&oGo back."), "");
		inventory.setItem(45, back);
		addMenuBorder();
		// The thing you will be looping through to place items
		ArrayList<String> requests = new ArrayList<String>(api.getRequests(manager.getPlayerToFetch()));
		
		///////////////////////////////////// Pagination loop template
		if (requests != null && !requests.isEmpty()) {
			for (int i = 0; i < getMaxItemsPerPage(); i++) {
				index = getMaxItemsPerPage() * page + i;
				if (index >= requests.size())
					break;
				if (requests.get(index) != null) {
					///////////////////////////
					// Create an item from our collection and place it into the inventory
					ItemStack playerItem = new ItemStack(Material.END_CRYSTAL, 1);
					ItemMeta playerMeta = playerItem.getItemMeta();
					playerMeta.setDisplayName(this.color("&b&lWaiting: &3&o" + requests.get(index)));
						playerMeta.getPersistentDataContainer().set(new NamespacedKey(BuddyUp.get(), "uuid"),
								PersistentDataType.STRING, requests.get(index));
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
