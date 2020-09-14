package xyz.mcvintage.hempfest.buddyup.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

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
import xyz.mcvintage.hempfest.buddyup.gui.buddy.BuddyInvite;
import xyz.mcvintage.hempfest.buddyup.gui.buddy.BuddyMenu;

public class OnlinePlayers extends MenuPaginated {
	
	public OnlinePlayers(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return this.color("&2&oOnline Player List |&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
	}

	@Override
	public int getSlots() {
		return 54;
	}
	
	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		
		ArrayList<Player> clans = new ArrayList<Player>(Bukkit.getOnlinePlayers());
		if (e.getCurrentItem().getType().equals(Material.TOTEM_OF_UNDYING)) {
			Manager manager = BuddyUp.getMenuView(p);
			manager.setPlayerToFetch(p);
			new BuddyMenu(manager).open();
		}
		if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {

			Manager manager = BuddyUp.getMenuView(p);
			manager.setPlayerToEdit(e.getCurrentItem().getItemMeta().getPersistentDataContainer()
					.get(new NamespacedKey(BuddyUp.get(), "uuid"), PersistentDataType.STRING));
			if (manager.getPlayerToEdit().equals(p.getName())) {
				p.closeInventory();
				api.msg(p, api.prefix + "&c&oYou cannot befriend yourself... Nice try though");
				p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 8, 1);
				return;
			}
			new BuddyInvite(manager).open();

		} else if (e.getCurrentItem().getType().equals(Material.BARRIER)) {

			// close inventory
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 8, 1);
		} else if (e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)) {
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
				if (!((index + 1) >= clans.size())) {
					page = page + 1;
					super.open();
				} else {
					p.sendMessage(ChatColor.GRAY + "You are on the last page.");
					p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 8, 1);
				}
			}
		}
	}

	@Override
	public void setMenuItems() {
		
		ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, this.color("&a&oGo back."), "");
		inventory.setItem(45, back);
		addMenuBorder();
		// The thing you will be looping through to place items
		ArrayList<Player> players = new ArrayList<Player>(Bukkit.getOnlinePlayers());
		
		///////////////////////////////////// Pagination loop template
		
		if (players != null && !players.isEmpty()) {
			for (int i = 0; i < getMaxItemsPerPage(); i++) {
				index = getMaxItemsPerPage() * page + i;
				if (index >= players.size())
					break;
				if (players.get(index) != null) {
					
					Date date = new Date(players.get(index).getLastPlayed());
					Date date2 = new Date(players.get(index).getFirstPlayed());
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy z");
					sdf.setTimeZone(TimeZone.getTimeZone("PST"));
					String firstPlayed = sdf.format(date2);
					String lastPlayed = sdf.format(date);
					///////////////////////////
					// Create an item from our collection and place it into the inventory
					ItemStack playerItem = new ItemStack(Material.PLAYER_HEAD, 1);
					ItemMeta playerMeta = playerItem.getItemMeta();
					if (api.getRequests(players.get(index)).contains(manager.getPlayerToFetch().getName())) {
						playerMeta.setDisplayName(this.color("(&b&lWAITING&r) &a&lOnline: &e&o" + players.get(index).getName()));
					} else
					if (api.getFriends(manager.getPlayerToFetch()).contains(players.get(index).getName())) {
						playerMeta.setDisplayName(this.color("(&6&lFRIENDS&r) &a&lOnline: &e&o" + players.get(index).getName()));
					} else if (players.get(index).getName().equals(manager.getPlayerToFetch().getName())) {
						playerMeta.setDisplayName(this.color("&a&lYOU"));
					} else {
						playerMeta.setDisplayName(this.color("&a&lOnline: &e&o" + players.get(index).getName()));
					}
					
					
						
						playerMeta.setLore(Arrays.asList(this.color("&a&l&oFirst joined: &r" + firstPlayed), this.color("&a&l&oLast on: &r" + lastPlayed)));
						playerMeta.getPersistentDataContainer().set(new NamespacedKey(BuddyUp.get(), "uuid"),
								PersistentDataType.STRING, players.get(index).getName());
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
