package xyz.mcvintage.hempfest.buddyup;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.mcvintage.hempfest.buddyup.commands.BuddiesCommand;
import xyz.mcvintage.hempfest.buddyup.commands.BuddyCommand;
import xyz.mcvintage.hempfest.buddyup.commands.PartyCommand;
import xyz.mcvintage.hempfest.buddyup.gui.Manager;
import xyz.mcvintage.hempfest.buddyup.listener.Players;
import xyz.mcvintage.hempfest.buddyup.util.Placeholders;



public class BuddyUp extends JavaPlugin {

	//Instance
	public static BuddyUp instance;
	private static final Logger log = Logger.getLogger("Minecraft");
	private static final HashMap<Player, Manager> GuiMap = new HashMap<Player, Manager>();
	//Start server
	public void onEnable() {
		log.info(String.format("[BuddyUp] - Time to make some friends! Haha... ha....", getDescription().getName()));
		setInstance(this);
		registerCommand(new BuddyCommand());
		registerCommand(new BuddiesCommand());
		registerCommand(new PartyCommand());
		new Placeholders(this).register();
		getServer().getPluginManager().registerEvents(new Players(), get());
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.closeInventory();
		}
	}
	
	public void onDisable() {
		log.warning(String.format("[BuddyUp] - Oh im so sad to see you go! Bye friend...", getDescription().getName()));
	}
	

	public static BuddyUp get() {
		return instance;
	}

	public static void setInstance(BuddyUp instance) {
		BuddyUp.instance = instance;
	}
	
	public static Manager getMenuView(Player p) {
		Manager playerMenuUtility;
		if (!(GuiMap.containsKey(p))) { // See if the player has a playermenuutility "saved" for them

			// This player doesn't. Make one for them add add it to the hashmap
			playerMenuUtility = new Manager(p);
			GuiMap.put(p, playerMenuUtility);

			return playerMenuUtility;
		} else {
			return GuiMap.get(p); // Return the object by using the provided player
		}
	}
	
	public void registerCommand(BukkitCommand command) {
		try {

			final Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(getServer());
			commandMap.register(command.getLabel(), command);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	

}
