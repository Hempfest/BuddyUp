package xyz.mcvintage.hempfest.buddyup.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.mcvintage.hempfest.buddyup.Buddy;
import xyz.mcvintage.hempfest.buddyup.BuddyUp;
import xyz.mcvintage.hempfest.buddyup.gui.Manager;
import xyz.mcvintage.hempfest.buddyup.gui.buddy.BuddyList;

public class BuddiesCommand extends BukkitCommand {

	public BuddiesCommand() {
		super("buddies");
		setDescription("Primary command for BuddyUp.");
		setAliases(Arrays.asList("friends"));
	}

	public void sendMessage(CommandSender player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		return;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Buddy api = new Buddy();
		if (!(sender instanceof Player)) {
			sendMessage(sender, api.prefix + " " + "This is a player only command.");
			return true;
		}

		// *
		// ---- Variable creation |
		Player p = (Player) sender;

		int length = args.length;
		if (length == 0) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new BuddyList(view).open();
			return true;
		}
		// *
		// ---- Variable creation |


		sendMessage(p, "Try " + '"' + "/buddy" + '"' + " for help - Unknown sub-command.");
		return true;
	}

}
