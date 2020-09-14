package xyz.mcvintage.hempfest.buddyup.commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.mcvintage.hempfest.buddyup.Buddy;
import xyz.mcvintage.hempfest.buddyup.BuddyUp;
import xyz.mcvintage.hempfest.buddyup.gui.Manager;
import xyz.mcvintage.hempfest.buddyup.gui.party.PartyMenu;
import xyz.mcvintage.hempfest.buddyup.util.Config;
import xyz.mcvintage.hempfest.buddyup.util.enums.BuddyAction;

public class PartyCommand extends BukkitCommand {

	public PartyCommand() {
		super("party");
		setDescription("Primary command for BuddyUp.");
		setAliases(Arrays.asList("group"));
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
		Config data = new Config(p.getUniqueId().toString());
		int length = args.length;
		if (length == 0) {
			Manager view = BuddyUp.getMenuView(p);
			view.setPlayerToFetch(p);
			new PartyMenu(view).open();
			return true;
		}
		// *
		// ---- Variable creation |
		
		
		if (length == 1) {
			if (args[0].equalsIgnoreCase("new")) {
				api.formParty(p, BuddyAction.CLOSED);
				return true;
			}
			if (args[0].equalsIgnoreCase("chat")) {
				if (api.inPartyChat(p)) {
					sendMessage(p, api.prefix + "Now in &lGLOBAL &rchat.");
					data.getConfig().set("party-chat", null);
					data.saveConfig();
				} else if (!api.inPartyChat(p)) {
					sendMessage(p, api.prefix + "Now in &3PARTY &rchat.");
					data.getConfig().set("party-chat", (Boolean) true);
					data.saveConfig();
					return true;
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("join")) {
				sendMessage(p, api.prefix + "&c&oYou need to specify a player to join.");
				return true;
			}
			if (args[0].equalsIgnoreCase("leave")) {
				if (api.isInParty(data, p)) {
					api.leaveParty(p);
					return true;
				}
			}
			sendMessage(p, api.prefix + "Unknown sub-command. Help Menu:");
			ArrayList<String> help = new ArrayList<String>();
			help.add(api.prefix + "- Command list&b&l|&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			help.add("&b|&7)&r /party join <playerName> - &7&oJoin an online players party.");
			help.add("&b|&7)&r /party leave - &7&oLeave your current party.");
			help.add("&b|&7)&r /party new <&aopen&7:&cclosed&r> - &7&oCreate a new open or closed party.");
			help.add("&b|&7)&r /party chat - &7&oSwitch to party chat");
			help.add("&b|&7)&r /party - &a&oGo back to the GUI.");
			help.add("&b&l&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			for (String s : help) {
			 sendMessage(p, ChatColor.translateAlternateColorCodes('&', s));
			}
			return true;
		}
		
		if (length == 2) {
			if (args[0].equalsIgnoreCase("new") && args[1].equalsIgnoreCase("open")) {
				api.formParty(p, BuddyAction.OPEN);
				return true;
			}
			if (args[0].equalsIgnoreCase("new") && args[1].equalsIgnoreCase("closed")) {
				api.formParty(p, BuddyAction.CLOSED);
				return true;
			}
			if (args[0].equalsIgnoreCase("join")) {
				Player target = Bukkit.getPlayer(args[1]);
				if (target == null) {
					sendMessage(p, api.prefix + "&c&oThat player was not found or is not online.");
					return true;
				}
				api.joinParty(p, target);
			}
			sendMessage(p, api.prefix + "Unknown sub-command. Help Menu:");
			ArrayList<String> help = new ArrayList<String>();
			help.add(api.prefix + "- Command list&b&l|&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			help.add("&b|&7)&r /party join <playerName> - &7&oJoin an online players party.");
			help.add("&b|&7)&r /party leave - &7&oLeave your current party.");
			help.add("&b|&7)&r /party new <&aopen&7:&cclosed&r> - &7&oCreate a new open or closed party.");
			help.add("&b|&7)&r /party chat - &7&oSwitch to party chat");
			help.add("&b|&7)&r /party - &a&oGo back to the GUI.");
			help.add("&b&l&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			for (String s : help) {
			 sendMessage(p, ChatColor.translateAlternateColorCodes('&', s));
			}
			return true;
		}

		sendMessage(p, api.prefix + "Unknown sub-command. Help Menu:");
		ArrayList<String> help = new ArrayList<String>();
		help.add(api.prefix + "- Command list&b&l|&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		help.add("&b|&7)&r /party join <playerName> - &7&oJoin an online players party.");
		help.add("&b|&7)&r /party leave - &7&oLeave your current party.");
		help.add("&b|&7)&r /party new <&aopen&7:&cclosed&r> - &7&oCreate a new open or closed party.");
		help.add("&b|&7)&r /party chat - &7&oSwitch to party chat");
		help.add("&b|&7)&r /party - &a&oGo back to the GUI.");
		help.add("&b&l&o&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		for (String s : help) {
		 sendMessage(p, ChatColor.translateAlternateColorCodes('&', s));
		}
		return true;
	}

}
