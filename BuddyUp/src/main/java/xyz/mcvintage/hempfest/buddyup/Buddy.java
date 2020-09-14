package xyz.mcvintage.hempfest.buddyup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.mcvintage.hempfest.buddyup.util.Config;
import xyz.mcvintage.hempfest.buddyup.util.enums.BuddyAction;
import xyz.mcvintage.hempfest.buddyup.util.enums.MailResponse;

public class Buddy {
	public Player t;
	public Config data;
	public Config pData;
	private String partyID;
	public HashMap<Player, Boolean> chat = new HashMap<Player, Boolean>();
	public String prefix = "&f(&b&oBuddyUp&f)&r ";
	
	
	/**
	 * Messages that players can send buddies.
	 */
	public String firstMsg = "Get ahold of me when you get online";
	public String secondMsg = "We need to talk, its serious";
	public String thirdMsg = "You've been ignoring my calls, are you seeing someone else?";
	public String fourthMsg = "I've done something i think you're gonna like. Get back to me";
	public String fifthMsg = "WHAT ARE YOU DOING";
	public String sixthMsg = "You wanna do something?";
	public String seventhMsg = "Hiiiiiiiiiii";
	public String eighthMsg = "I left something at your place :)";
	public String ninethMsg = "Check your chests";
	public String tenthMsg = "Where did you go? Ill be back";
	
	/**
	 * Translate a players message to color
	 * 
	 * @param p = player to message
	 * @param msg = String to translate 
	 */
	public void msg(Player p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	
	/**
	 * 
	 * Add or remove a friend from a player
	 * 
	 * 
	 * @param action = BuddyAction type (Enum) [BEFRIEND, UNFRIEND, TALKTO]
	 * @param p = the player sending the command
	 * @param t = the target to grab.
	 */
	@SuppressWarnings("deprecation")
	public void perform(BuddyAction action, Player p, String t) {
		Player target = Bukkit.getPlayer(t);
		this.t = target;
		pData = new Config(p.getUniqueId().toString());
		switch (action) {
		case BEFRIEND:
			if (target == null) {
				OfflinePlayer tar = Bukkit.getOfflinePlayer(t);
				data = new Config(tar.getUniqueId().toString());
				if (!data.exists()) {
					msg(p, prefix + "&c&oThe player " + '"' + t + '"' + " was not found.");
					return;
				}
				if (hasFile(tar) && hasFile(p)) {
					if (isWaiting(p, tar)) {
						makeFriendship(p, tar);
						return;
					}
					if (tar.getName().equals(p.getName())) {
						msg(p, prefix + "&c&oYou cannot befriend yourself... Nice try though");
						return;
					}
					if (getRequests(tar).contains(p.getName())) {
						msg(p, prefix + "&c&oYou have already sent a request to: " + tar.getName());
						return;
					}
					if (getFriends(p).contains(tar.getName())) {
						msg(p, prefix + "&c&oYou are already friends with player: " + tar.getName());
						return;
					}
						ArrayList<String> requests = new ArrayList<String>(getRequests(tar));
						ArrayList<String> sent = new ArrayList<String>(); 
						requests.add(p.getName());
						sent.add(tar.getName());
						updateRequests(data, requests);
						updateSentRequests(pData, sent);
						msg(p, prefix + "&a&oYou sent a friend request to: " + tar.getName());
					
				} else {
					Config newFile = new Config(tar.getUniqueId().toString());
					Config file = new Config(p.getUniqueId().toString());
					ArrayList<String> requests = new ArrayList<String>(getRequests(tar));
					ArrayList<String> sent = new ArrayList<String>(); 
					requests.add(p.getName());
					sent.add(tar.getName());
					updateRequests(newFile, requests);
					updateSentRequests(file, sent);
					msg(p, prefix + "&a&oYou sent a friend request to: " + tar.getName());
				}
				return;
			}
			if (hasFile(this.t) && hasFile(p)) {
				data = new Config(this.t.getUniqueId().toString());
				if (isWaiting(p, this.t)) {
					makeFriendship(p, this.t);
					return;
				}
				if (this.t.getName().equals(p.getName())) {
					msg(p, prefix + "&c&oYou cannot befriend yourself... Nice try though");
					return;
				}
				if (getRequests(this.t).contains(p.getName())) {
					msg(p, prefix + "&c&oYou have already sent a request to: " + this.t.getName());
					return;
				}
				if (getFriends(p).contains(this.t.getName())) {
					msg(p, prefix + "&c&oYou are already friends with player: " + this.t.getName());
					return;
				}
					ArrayList<String> requests = new ArrayList<String>(getRequests(this.t));
					ArrayList<String> sent = new ArrayList<String>(); 
					requests.add(p.getName());
					sent.add(this.t.getName());
					updateRequests(data, requests);
					updateSentRequests(pData, sent);
					msg(p, prefix + "&a&oYou sent a friend request to: " + this.t.getName());
					msg(this.t, prefix + "&e&oYou recieved a friend request from: " + p.getName());
				
			} else {
				Config newFile = new Config(this.t.getUniqueId().toString());
				Config file = new Config(p.getUniqueId().toString());
				ArrayList<String> requests = new ArrayList<String>(getRequests(this.t));
				ArrayList<String> sent = new ArrayList<String>(); 
				requests.add(p.getName());
				sent.add(this.t.getName());
				updateRequests(newFile, requests);
				updateSentRequests(file, sent);
				msg(p, prefix + "&a&oYou sent a friend request to: " + this.t.getName());
				msg(this.t, prefix + "&e&oYou recieved a friend request from: " + p.getName());
			}
			break;
		case TALKTO:
			if (target == null) {
				msg(p, prefix + "&c&oThe player " + '"' + t + '"' + " was not found.");
				return;
			}
			break;
		case UNFRIEND:
			if (target == null) {
				OfflinePlayer tar = Bukkit.getOfflinePlayer(t);
				Config conf = new Config(tar.getUniqueId().toString());
				if (!conf.exists()) {
					msg(p, prefix + "&c&oThe player " + '"' + t + '"' + " was not found.");
					return;
				}
				removeFriend(p, tar);
				return;
			}
			removeFriend(p, this.t);
			break;
		case CANCEL_REQUEST:
			cancelRequest(p, t);
		default:
			break;
		
		}
	}
	
	/**
	 * 
	 * Confirm friendship between two online players
	 * 
	 * @param p = first online traget
	 * @param target = second online target
	 */
	public void makeFriendship(Player p, Player target) {
		this.data = new Config(target.getUniqueId().toString());
		this.pData = new Config(p.getUniqueId().toString());
		ArrayList<String> friend = new ArrayList<String>();
		ArrayList<String> friends = new ArrayList<String>();
		ArrayList<String> request = new ArrayList<String>();
		ArrayList<String> requestt = new ArrayList<String>();
		ArrayList<String> requests = new ArrayList<String>(getSentRequests(target));
		ArrayList<String> requestss = new ArrayList<String>(getSentRequests(p));
		requests.remove(p.getName());
		requestss.remove(target.getName());
		request.addAll(getRequests(p));
		if (request.contains(target.getName())) {
		request.remove(target.getName());
		}
		requestt.addAll(getRequests(target));
		if (requestt.contains(p.getName())) {
			requestt.remove(p.getName());
			}
		friend.addAll(getFriends(p));
		friends.addAll(getFriends(target));
		friends.add(p.getName());
		friend.add(target.getName());
		updateFriends(data, friends);
		updateFriends(pData, friend);
		updateSentRequests(data, requests);
		updateSentRequests(pData, requestss);
		updateRequests(pData, request);
		updateRequests(data, requestt);
		msg(p, prefix + "&a&oYou are now friends with: " + target.getName());
		msg(target, prefix + "&e&oYou are now friends with: " + p.getName());
		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 8, 1);
		target.playSound(target.getLocation(), Sound.ENTITY_VILLAGER_YES, 8, 1);
	}
	
	/**
	 * 
	 * Confirm friendship between an online target and one offline target
	 * 
	 * @param p = the online target to get
	 * @param target = the offline target to get
	 */
	public void makeFriendship(Player p, OfflinePlayer target) {
		this.data = new Config(target.getUniqueId().toString());
		this.pData = new Config(p.getUniqueId().toString());
		ArrayList<String> friend = new ArrayList<String>();
		ArrayList<String> friends = new ArrayList<String>();
		ArrayList<String> request = new ArrayList<String>();
		ArrayList<String> requestt = new ArrayList<String>();
		ArrayList<String> requests = new ArrayList<String>(getSentRequests(target));
		ArrayList<String> requestss = new ArrayList<String>(getSentRequests(p));
		requests.remove(p.getName());
		requestss.remove(target.getName());
		request.addAll(getRequests(p));
		if (request.contains(target.getName())) {
		request.remove(target.getName());
		}
		requestt.addAll(getRequests(target));
		if (requestt.contains(p.getName())) {
			requestt.remove(p.getName());
			}
		friend.addAll(getFriends(p));
		friends.addAll(getFriends(target));
		friends.add(p.getName());
		friend.add(target.getName());
		updateFriends(data, friends);
		updateFriends(pData, friend);
		updateSentRequests(data, requests);
		updateSentRequests(pData, requestss);
		updateRequests(pData, request);
		updateRequests(data, requestt);
		msg(p, prefix + "&a&oYou are now friends with: " + target.getName());
		addToList(data, "accepted-requests", p.getName());
		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 8, 1);
	}
	
	/**
	 * Check if a user is in party chat
	 * 
	 * @param player = the target to check
	 * @return returns true if the needed section exists and equals false other-wise false
	 */
	public boolean inPartyChat(Player player) {
		Config data = new Config(player.getUniqueId().toString());
		return sectionExists(data, "party-chat")
				? data.getConfig().getBoolean("party-chat")
				: false;
	}
	
	/**
	 * Checks if a given path exists within a config file
	 * 
	 * @param conf = the data file to use
	 * @param path = the path to check
	 * @return returns true if sections exists or false if it does not
	 */
	private boolean sectionExists(Config conf, String path) {
		if (conf.getConfig().getString(path) != null) {
			return true;
		}
		return false;
	}
	
	public boolean isPlayersAddable(Player p) {
		for (Player target : Bukkit.getOnlinePlayers()) {
				if (!isFriends(p, target) || !getRequests(target).contains(p.getName())) {
					return true;
				}
			
		}
		return false;
	}
	
	public boolean isFriends(Player p, Player target) {
		if (getFriends(p).contains(target.getName()) && getFriends(target).contains(p.getName())) {
			return true;
		}
		return false;
	}
	
	public boolean isFriends(Player p, OfflinePlayer target) {
		if (getFriends(p).contains(target.getName()) && getFriends(target).contains(p.getName())) {
			return true;
		}
		return false;
	}
	
	public void removeFriend(Player p, Player target) {
		
		if (isFriends(p, target)) {
			updateFriends(p, target);
			msg(p, prefix + "&4&oYou are no longer friends with: " + target.getName());
			msg(target, prefix + p.getName() + " &4&ohas decided to... part ways...");
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 8, 1);
			target.playSound(target.getLocation(), Sound.ENTITY_VILLAGER_YES, 8, 1);
			return;
		}
	}
	
	public void removeFriend(Player p, OfflinePlayer target) {
		if (isFriends(p, target)) {
			updateFriends(p, target);
			msg(p, prefix + "&4&oYou are no longer friends with: " + target.getName());
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 8, 1);
			Config conf = new Config(target.getUniqueId().toString());
			addToList(conf, "unfriended-people", p.getName());
			return;
		}
	}
	
	public List<String> getFriends(Player target) {
		data = new Config(target.getUniqueId().toString());
		return getStringList(data, "friends-list");
	}
	
	public List<String> getRequests(Player target) {
		data = new Config(target.getUniqueId().toString());
		return getStringList(data, "friends-request-list");
	}
	
	public List<String> getSentRequests(Player target) {
		data = new Config(target.getUniqueId().toString());
		return getStringList(data, "sent-requests");
	}
	
	public List<String> getFriends(OfflinePlayer target) {
		data = new Config(target.getUniqueId().toString());
		return getStringList(data, "friends-list");
	}
	
	public List<String> getRequests(OfflinePlayer target) {
		data = new Config(target.getUniqueId().toString());
		return getStringList(data, "friends-request-list");
	}
	
	public List<String> getSentRequests(OfflinePlayer target) {
		data = new Config(target.getUniqueId().toString());
		return getStringList(data, "sent-requests");
	}
	
	public boolean hasFile(Player target) {
		this.data = new Config(target.getUniqueId().toString());
		if (data.exists()) {
			return true;
		}
		return false;
	}
	
	public String isNearybyFriends(Player p) {
		for (Entity e : p.getNearbyEntities(10, 10, 10)) {
			if (e instanceof Player) {
				Player t = ((Player) e).getPlayer();
				if (isFriends(p, t)) {
					return "&f(&6Friend&f) " + t.getName();
				} else {
					return t.getName();
				}
			}
		}
		return "&cNo one near.";
	}
	
	public boolean hasFile(OfflinePlayer target) {
		this.data = new Config(target.getUniqueId().toString());
		if (data.exists()) {
			return true;
		}
		return false;
	}
	
	public boolean hasUpdate(Player target) {
		Config conf = new Config(target.getUniqueId().toString());
		ArrayList<String> f1 = new ArrayList<String>(conf.getConfig().getStringList("unfriended-people"));
		ArrayList<String> f2 = new ArrayList<String>(conf.getConfig().getStringList("accepted-requests"));
		ArrayList<String> f3;
		if (conf.getConfig().getConfigurationSection("incoming-mail") != null) {
			f3 = new ArrayList<String>(conf.getConfig().getConfigurationSection("incoming-mail").getKeys(false));
			if (!f3.isEmpty()) {
				return true;
			}
		}
		if (f1.equals(null) || f2.equals(null)) {
			return false;
		}
		if (!f1.isEmpty() || !f2.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public boolean isWaiting(Player p, Player target) {
		if (getRequests(p).contains(target.getName())) {
			return true;
		}
		return false;
	}
	
	public boolean isWaiting(Player p, OfflinePlayer target) {
		if (getRequests(p).contains(target.getName())) {
			return true;
		}
		return false;
	}
	
	public boolean isPartyLeader(Player p) {
		Config parties = new Config("party_list");
		if (parties.getConfig().getString(getParty(p) + ".leader").equals(p.getName())) {
			return true;
		}
		return false;
	}
	
	public String getPartyLeader(Player p) {
		Config parties = new Config("party_list");
		return parties.getConfig().getString(getParty(p) + ".leader");
	}
	
	public boolean isInParty(Config conf, Player p) {
		if (conf.getConfig().getString("Party") != null) {
			return true;
		}
		return false;
	}
	
	public List<String> getStringList(Config conf, String section) {
		return conf.getConfig().getStringList(section);
	}
	
	public String getMailResponse(MailResponse response) {
		String result = "";
		switch (response) {
		case EIGHT:
			result = this.eighthMsg;
			break;
		case FIVE:
			result = this.fifthMsg;
			break;
		case FOUR:
			result = this.fourthMsg;
			break;
		case NINE:
			result = this.ninethMsg;
			break;
		case ONE:
			result = this.firstMsg;
			break;
		case SEVEN:
			result = this.seventhMsg;
			break;
		case SIX:
			result = this.sixthMsg;
			break;
		case TEN:
			result = this.tenthMsg;
			break;
		case THREE:
			result = this.thirdMsg;
			break;
		case TWO:
			result = this.secondMsg;
			break;
		default:
			break;
		}
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public void sendMail(MailResponse response, Player p, String t) {
		String result = "";
		switch (response) {
		case EIGHT:
			result = this.eighthMsg;
			break;
		case FIVE:
			result = this.fifthMsg;
			break;
		case FOUR:
			result = this.fourthMsg;
			break;
		case NINE:
			result = this.ninethMsg;
			break;
		case ONE:
			result = this.firstMsg;
			break;
		case SEVEN:
			result = this.seventhMsg;
			break;
		case SIX:
			result = this.sixthMsg;
			break;
		case TEN:
			result = this.tenthMsg;
			break;
		case THREE:
			result = this.thirdMsg;
			break;
		case TWO:
			result = this.secondMsg;
			break;
		default:
			break;
		}
		Config conf;
		Player target = Bukkit.getPlayer(t);
		if (target == null) {
			OfflinePlayer tar = Bukkit.getOfflinePlayer(t);
			conf = new Config(tar.getUniqueId().toString());
			if (!conf.exists()) {
				msg(p, prefix + "&c&oThe player " + '"' + t + '"' + " was not found.");
				return;
			}
			addToList(conf, "incoming-mail." + p.getName(), result);
			msg(p, prefix + "&e&oSent mail to friend: " + t);
			return;
		}
		conf = new Config(target.getUniqueId().toString());
		addToList(conf, "incoming-mail." + p.getName(), result);
		msg(p, prefix + "&e&oSent mail to friend: " + t);
	}
	
	public void addToList(Config con, String path, String value) {
		ArrayList<String> content = new ArrayList<String>(con.getConfig().getStringList(path));
		if (!content.contains(value)) {
			content.add(value);
		}
		con.getConfig().set(path, content);
		con.saveConfig();
	}
	
	public void newString(Config con, String section, String value, String section2, String value2) {
		if (section2.isEmpty() || value2.isEmpty()) {
			con.getConfig().set(section, value);
			con.saveConfig();
			return;
		}
		con.getConfig().set(section, value);
		con.saveConfig();
		con.getConfig().set(section2, value2);
		con.saveConfig();
	}
	
	public void updateSentRequests(Config conf, List<String> requestList) {
		conf.getConfig().set("sent-requests", requestList);
		conf.saveConfig();
	}
	
	public void updateFriends(Config conf, List<String> friendList) {
		conf.getConfig().set("friends-list", friendList);
		conf.saveConfig();
	}
	
	public void updateFriends(Player p, Player target) {
		Config conf = new Config(p.getUniqueId().toString());
		Config conf2 = new Config(target.getUniqueId().toString());
		ArrayList<String> f1 = new ArrayList<String>(conf.getConfig().getStringList("friends-list"));
		ArrayList<String> f2 = new ArrayList<String>(conf2.getConfig().getStringList("friends-list"));
		f1.remove(target.getName());
		conf.getConfig().set("friends-list", f1);
		conf.saveConfig();
		f2.remove(p.getName());
		conf2.getConfig().set("friends-list", f2);
		conf2.saveConfig();
	}
	
	public void handleUpdate(Player target) {
		Config conf = new Config(target.getUniqueId().toString());
		ArrayList<String> f1 = new ArrayList<String>(conf.getConfig().getStringList("unfriended-people"));
		ArrayList<String> f2 = new ArrayList<String>(conf.getConfig().getStringList("accepted-requests"));
		f1.clear();
		f2.clear();
		conf.getConfig().set("unfriended-people", f1);
		conf.getConfig().set("accepted-requests", f2);
		conf.saveConfig();
	}
	
	public void updateFriends(Player p, OfflinePlayer target) {
		Config conf = new Config(p.getUniqueId().toString());
		Config conf2 = new Config(target.getUniqueId().toString());
		ArrayList<String> f1 = new ArrayList<String>(conf.getConfig().getStringList("friends-list"));
		ArrayList<String> f2 = new ArrayList<String>(conf2.getConfig().getStringList("friends-list"));
		f1.remove(target.getName());
		conf.getConfig().set("friends-list", f1);
		conf.saveConfig();
		f2.remove(p.getName());
		conf2.getConfig().set("friends-list", f2);
		conf2.saveConfig();
	}
	
	public String getParty(Player p) {
		Config data = new Config(p.getUniqueId().toString());
		return data.getConfig().getString("Party");
	}
	
	public String getPartyStatus(String partyID) {
		data = new Config("party_list");
		return data.getConfig().getString(partyID + ".status");
	}
	
	public void joinParty(Player p, Player target) {
		Config data = new Config("party_list");
		pData = new Config(p.getUniqueId().toString());
		this.data = new Config(target.getUniqueId().toString());
		if (!isInParty(this.data, target)) {
			msg(p, prefix + "&c&o" + target.getName() + " isn't in a party.");
			return;
		}
		if (getPartyStatus(getParty(target)).equals("LOCKED") && !isFriends(p, target)) {
			msg(p, prefix + target.getName() + " &cis in a &r" + getPartyStatus(getParty(target)) + " &cparty and you are not friends.");
			return;
		}
		if (PartyMembers(getParty(target)).size() == maxPartySize(getParty(target))) {
			msg(p, prefix + target.getName() + "'s &c&oparty is full. &r[&4" + PartyMembers(getParty(target)).size() + "&r]");
			return;
		}
		if (isInParty(pData, p)) {
			msg(p, prefix + "&c&oYou can only be in one party at a time.");
			return;
		}
		ArrayList<String> members = new ArrayList<String>(data.getConfig().getStringList(getParty(target) + ".members"));
		members.add(p.getName());
		pData.getConfig().set("Party", getParty(target));
		pData.saveConfig();
		data.getConfig().set(getParty(target) + ".members", members);
		data.saveConfig();
		msg(p, prefix + "&e&oYou have joined " + target.getName() + "'s party.");
		for (String players : PartyMembers(getParty(target))) {
			
			msg(Bukkit.getPlayer(players), prefix + p.getName() + " &a&ohas joined the party.");
		}
	}
	
	public void leaveParty(Player p) {
		data = new Config(p.getUniqueId().toString());
		Config parties = new Config("party_list");
		if (isPartyLeader(p)) {
			disbandParty(p);
			msg(p, prefix + "&4&oYou disbanded your party.");
			return;
		}
		ArrayList<String> members = new ArrayList<String>(PartyMembers(getParty(p)));
		members.remove(p.getName());
		parties.getConfig().set(getParty(p) + ".members", members);
		parties.saveConfig();
		for (String players : PartyMembers(getParty(p))) {
			
			msg(Bukkit.getPlayer(players), prefix + p.getName() + " &c&oleft the party.");
		}
		data.getConfig().set("Party", null);
		data.saveConfig();
		msg(p, prefix + "&c&oYou left the party.");
	}
	
	public int maxPartySize(Player target) {
	    int returnv = 0;
	    if (target == null)
	      return 0; 
	    for (int i = 100; i >= 0; i--) {
	      if (target.hasPermission("buddyup.party.infinite")) {
	        returnv = -1;
	        break;
	      } 
	      if (target.hasPermission("buddyup.party." + i)) {
	        returnv = i;
	        break;
	      } 
	    } 
	    if (returnv == -1) 
	    	return 999;
	    	
	    return returnv;
	  }
	
	 public int maxFriends(Player target) {
		    int returnv = 0;
		    if (target == null)
		      return 0; 
		    for (int i = 100; i >= 0; i--) {
		      if (target.hasPermission("buddyup.friends.infinite")) {
		        returnv = -1;
		        break;
		      } 
		      if (target.hasPermission("buddyup.friends." + i)) {
		        returnv = i;
		        break;
		      } 
		    } 
		    if (returnv == -1) 
		    	return 999;
		    	
		    return returnv;
		  }
	 
	public int maxPartySize(String partyID) {
		data = new Config("party_list");
		return data.getConfig().getInt(partyID + ".max-size");
	}
	
	public List<String> PartyMembers(String partyID) {
		Config parties = new Config("party_list");
		return parties.getConfig().getStringList(partyID + ".members");
	}
	
	@SuppressWarnings("deprecation")
	public void disbandAllMembers(Player p) {
		for (String player : PartyMembers(getParty(p))) {
			OfflinePlayer pl = Bukkit.getOfflinePlayer(player);
			if (pl.isOnline()) {
				Player pla = Bukkit.getPlayer(player);
				msg(pla, prefix + "&4&oYour party was disband by &e&o" + p.getName());
				Config data = new Config(pla.getUniqueId().toString()); 
				if (sectionExists(data, "party-chat")) {
					data.getConfig().set("party-chat", null);
				}
				data.getConfig().set("Party", null);
				data.saveConfig();
			}
			data = new Config(pl.getUniqueId().toString());
			data.getConfig().set("Party", null);
			data.saveConfig();
		}
	}
	
	private void setParty(String partyID) {
		this.partyID = partyID;
	}
	
	private String getPartyID() {
		return partyID;
	}
	
	public void disbandParty(Player p) {
		Config parties = new Config("party_list");
		setParty(getParty(p));
		disbandAllMembers(p);
		parties.getConfig().set((String) getPartyID(), null);
		parties.saveConfig();
	}
	
	@SuppressWarnings("deprecation")
	public void formParty(Player p, BuddyAction action) {
		String status = "";
		if (action.equals(BuddyAction.OPEN)) {
			status = "OPEN";
		}
		if (action.equals(BuddyAction.CLOSED)) {
			status = "LOCKED";
		}
		data = new Config(p.getUniqueId().toString());
		Config parties = new Config("party_list");
		if (isInParty(data, p)) {
			msg(p, prefix + "&c&oYou can only be in one party at a time.");
			return;
		}
			UUID partyID = UUID.randomUUID();
			ArrayList<String> members = new ArrayList<String>();
			members.add(p.getName());
			parties.getConfig().set(partyID.toString() + ".leader", p.getName());
			parties.getConfig().set(partyID.toString() + ".members", members);
			parties.getConfig().set(partyID.toString() + ".status", status);
			parties.getConfig().set(partyID.toString() + ".max-size", maxPartySize(p));
			parties.saveConfig();
			data.getConfig().set("Party", partyID.toString());
			data.saveConfig();
			for (String friends : getFriends(p)) {
				OfflinePlayer friend = Bukkit.getOfflinePlayer(friends);
				if (friend.isOnline()) {
					Player f = Bukkit.getPlayer(friends);
				msg(f, prefix + "&e&oYour friend " + '"' + "&6" + p.getName() + "&e&o" + '"' + " created a &f" + status + "&e&o party!");
				}
			}
			msg(p, prefix + "&3&oYou have created a new party.");
	}
	
	public void updateUser(Player target) {
		Config conf = new Config(target.getUniqueId().toString());
		ArrayList<String> msg = new ArrayList<String>();
		ArrayList<String> f1 = new ArrayList<String>(conf.getConfig().getStringList("unfriended-people"));
		ArrayList<String> f2 = new ArrayList<String>(conf.getConfig().getStringList("accepted-requests"));
		ArrayList<String> f3 = new ArrayList<String>(conf.getConfig().getConfigurationSection("incoming-mail").getKeys(false));
		
		for (String player : f1) {
			msg.add(player + " &4&ohas decided to... part ways...");
		}
		
		for (String player : f2) {
			msg.add("&a&oYou are now friends with: " + player);
		}
		
		for (String player : f3) {
			ArrayList<String> mail = new ArrayList<String>(conf.getConfig().getStringList("incoming-mail." + player));
			for (String m : mail) {
				msg.add("&6&oMessage from player " + '"' + player + '"' + " &r: &7&o " + m);
			}
			conf.getConfig().createSection("incoming-mail." + player);
		}
		//send message
		for (String list : msg) {
			msg(target, prefix + list);
		}
		
		conf.saveConfig();
		target.playSound(target.getLocation(), Sound.ENTITY_WANDERING_TRADER_YES, 8, 1);
	}
	
	@SuppressWarnings("deprecation")
	public void cancelRequest(Player p, String t) {
		Player target = Bukkit.getPlayer(t);
		if (target == null) {
			OfflinePlayer tar = Bukkit.getOfflinePlayer(t);
			if (tar == null) {
				msg(p, prefix + "&c&oThe player " + '"' + t + '"' + " was not found.");
				return;
			}
			if (getRequests(tar).contains(p.getName())) {
				Config conf = new Config(p.getUniqueId().toString());
				Config conf2 = new Config(tar.getUniqueId().toString());
				ArrayList<String> f1 = new ArrayList<String>(conf.getConfig().getStringList("sent-requests"));
				ArrayList<String> f2 = new ArrayList<String>(conf2.getConfig().getStringList("friends-request-list"));
				f1.remove(tar.getName());
				conf.getConfig().set("sent-requests", f1);
				conf.saveConfig();
				f2.remove(p.getName());
				conf2.getConfig().set("friends-request-list", f2);
				conf2.saveConfig();
				msg(p, prefix + "&e&oYou cancelled your request of friendship to: " + t);
				p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 8, 1);
				return;
			}
			return;
		}
		if (getRequests(target).contains(p.getName())) {
			Config conf = new Config(p.getUniqueId().toString());
			Config conf2 = new Config(target.getUniqueId().toString());
			ArrayList<String> f1 = new ArrayList<String>(conf.getConfig().getStringList("sent-requests"));
			ArrayList<String> f2 = new ArrayList<String>(conf2.getConfig().getStringList("friends-request-list"));
			f1.remove(target.getName());
			conf.getConfig().set("sent-requests", f1);
			conf.saveConfig();
			f2.remove(p.getName());
			conf2.getConfig().set("friends-request-list", f2);
			conf2.saveConfig();
			msg(p, prefix + "&e&oYou cancelled your request of friendship to: " + t);
			msg(target, prefix + p.getName() + ": &c&oNevermind... I don't want to be friends.");
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 8, 1);
			target.playSound(target.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 8, 1);
			return;
		}
	}
	
	
	
	public void updateRequests(Config conf, List<String> friendList) {
		conf.getConfig().set("friends-request-list", friendList);
		conf.saveConfig();
	}
	
	//public void createSection(Config conf, String section) {
		//if (!sectionExists(conf, section)) {
		//conf.getConfig().createSection(section);
		//conf.saveConfig();
		//}
	//}
	
	
}
