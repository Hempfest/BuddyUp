package xyz.mcvintage.hempfest.buddyup.gui;

import org.bukkit.entity.Player;

/*
Companion class to all menus. This is needed to pass information across the entire
 menu system no matter how many inventories are opened or closed.

 Each player has one of these objects, and only one.
 */

public class Manager {

	private Player owner;
	
	private Player playerToFetch;

	private String playerToEdit;
	

	public Manager(Player p) {
		this.owner = p;
	}

	public Player getOwner() {
		return owner;
	}

	public Player getPlayerToFetch() {
		return playerToFetch;
	}

	public String getPlayerToEdit() {
		return playerToEdit;
	}

	public void setPlayerToEdit(String playerOb) {
		this.playerToEdit = playerOb;
	}

	public void setPlayerToFetch(Player playerToFetch) {
		this.playerToFetch = playerToFetch;
	}
}
