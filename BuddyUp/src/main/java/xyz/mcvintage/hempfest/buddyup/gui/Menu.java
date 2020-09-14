package xyz.mcvintage.hempfest.buddyup.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import xyz.mcvintage.hempfest.buddyup.Buddy;


public abstract class Menu implements InventoryHolder {
	private String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	/*
	 * Defines the behavior and attributes of all menus in our plugin
	 */
	// Protected values that can be accessed in the menus
	protected Manager manager;
	protected Inventory inventory;
	protected Buddy api = new Buddy();
	protected ItemStack FILLER_GLASS = makeItem(Material.BLACK_STAINED_GLASS_PANE, " ");
	protected ItemStack FILLER_GLASS_LIGHT = makeItem(Material.RED_STAINED_GLASS_PANE,
			this.color("&7&oEmpty space."));

	// Constructor for Menu. Pass in a manager so that
	// we have information on who's menu this is and
	// what info is to be transfered
	public Menu(Manager manager) {
		this.manager = manager;
	}

	// let each menu decide their name
	public abstract String getMenuName();

	// let each menu decide their slot amount
	public abstract int getSlots();

	// let each menu decide how the items in the menu will be handled when clicked
	public abstract void handleMenu(InventoryClickEvent e);

	// let each menu decide what items are to be placed in the inventory menu
	public abstract void setMenuItems();

	// When called, an inventory is created and opened for the player
	public void open() {
		// The owner of the inventory created is the Menu itself,
		// so we are able to reverse engineer the Menu object from the
		// inventoryHolder in the MenuListener class when handling clicks
		inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

		// grab all the items specified to be used for this menu and add to inventory
		this.setMenuItems();

		// open the inventory for the player
		manager.getOwner().openInventory(inventory);
	}

	// Overridden method from the InventoryHolder interface
	public Inventory getInventory() {
		return inventory;
	}

	// Helpful utility method to fill all remaining slots with "filler glass"
	public void setFillerGlass() {
		for (int i = 0; i < getSlots(); i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, FILLER_GLASS);
			}
		}
	}
	
	public void sendHelpMenu(Player p) {
		ArrayList<String> help = new ArrayList<String>();
		help.add(api.prefix + "- Command list&b&l|&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		help.add("&b|&7)&r /buddy add <playerName> - &7&oSend or accept a friend request.");
		help.add("&b|&7)&r /buddy remove <playerName> - &7&oRemove a user from your friend list.");
		help.add("&b|&7)&r /buddies - &7&oView your friendlist");
		help.add("&b|&7)&r /buddy - &a&oGo back to the GUI.");
		help.add("&b&l&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		for (String s : help) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
		}
		
	}

	public ItemStack makeItem(Material material, String displayName, String... lore) {

		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(color(displayName));

		itemMeta.setLore(Arrays.asList(lore));
		item.setItemMeta(itemMeta);

		return item;
	}
	
	public ItemStack makeItem(Material material, String displayName, List<String> lore) {

		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(color(displayName));

		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);

		return item;
	}

}
