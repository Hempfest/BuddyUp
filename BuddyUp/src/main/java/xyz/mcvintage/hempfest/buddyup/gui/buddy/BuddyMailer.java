package xyz.mcvintage.hempfest.buddyup.gui.buddy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import xyz.mcvintage.hempfest.buddyup.gui.Manager;
import xyz.mcvintage.hempfest.buddyup.gui.Menu;
import xyz.mcvintage.hempfest.buddyup.util.enums.MailResponse;


public class BuddyMailer extends Menu {
	
	public BuddyMailer(Manager manager) {
		super(manager);
	}
	
	String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@Override
	public String getMenuName() {
		return color(api.prefix + "&e&oSend mail to &a" + manager.getPlayerToEdit());
	}

	@Override
	public int getSlots() {
		return 27;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Material mat = e.getCurrentItem().getType();
		if (mat == Material.BLACK_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.ONE, p, manager.getPlayerToEdit());
		}
		if (mat == Material.BLUE_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.TWO, p, manager.getPlayerToEdit());
		}
		if (mat == Material.BROWN_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.THREE, p, manager.getPlayerToEdit());
		}
		if (mat == Material.CYAN_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.FOUR, p, manager.getPlayerToEdit());
		}
		if (mat == Material.GRAY_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.FIVE, p, manager.getPlayerToEdit());
		}
		if (mat == Material.GREEN_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.SIX, p, manager.getPlayerToEdit());
		}
		if (mat == Material.LIGHT_BLUE_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.SEVEN, p, manager.getPlayerToEdit());
		}
		if (mat == Material.LIGHT_GRAY_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.EIGHT, p, manager.getPlayerToEdit());
		}
		if (mat == Material.LIME_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.NINE, p, manager.getPlayerToEdit());
		}
		if (mat == Material.MAGENTA_DYE) {
			p.closeInventory();
			api.sendMail(MailResponse.TEN, p, manager.getPlayerToEdit());
		}

	}

	@Override
	public void setMenuItems() {
		ItemStack one = makeItem(Material.BLACK_DYE, "&e&oResponse &7#&e1", api.getMailResponse(MailResponse.ONE));
		ItemStack two = makeItem(Material.BLUE_DYE, "&e&oResponse &7#&e2", api.getMailResponse(MailResponse.TWO));
		ItemStack three = makeItem(Material.BROWN_DYE, "&e&oResponse &7#&e3", api.getMailResponse(MailResponse.THREE));
		ItemStack four = makeItem(Material.CYAN_DYE, "&e&oResponse &7#&e4", api.getMailResponse(MailResponse.FOUR));
		ItemStack five = makeItem(Material.GRAY_DYE, "&e&oResponse &7#&e5", api.getMailResponse(MailResponse.FIVE));
		ItemStack six = makeItem(Material.GREEN_DYE, "&e&oResponse &7#&e6", api.getMailResponse(MailResponse.SIX));
		ItemStack seven = makeItem(Material.LIGHT_BLUE_DYE, "&e&oResponse &7#&e7", api.getMailResponse(MailResponse.SEVEN));
		ItemStack eight = makeItem(Material.LIGHT_GRAY_DYE, "&e&oResponse &7#&e8", api.getMailResponse(MailResponse.EIGHT));
		ItemStack nine = makeItem(Material.LIME_DYE, "&e&oResponse &7#&e9", api.getMailResponse(MailResponse.NINE));
		ItemStack ten = makeItem(Material.MAGENTA_DYE, "&e&oResponse &7#&e10", api.getMailResponse(MailResponse.TEN));
		inventory.setItem(1, one);
		inventory.setItem(2, two);
		inventory.setItem(3, three);
		inventory.setItem(4, four);
		inventory.setItem(5, five);
		inventory.setItem(6, six);
		inventory.setItem(7, seven);
		inventory.setItem(12, eight);
		inventory.setItem(13, nine);
		inventory.setItem(14, ten);
		setFillerGlass();

	}

}
