package xyz.mcvintage.hempfest.buddyup.listener;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.mcvintage.hempfest.buddyup.Buddy;




public class Announcer extends BukkitRunnable
{
	Player p;
	
	public Announcer(Player player) {
		this.p = player;
	}

	
    public void run() {
    	
		
		
    	Buddy api = new Buddy();
    		
    		if (api.getRequests(p).size() > 0) {
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', api.prefix + "&e&oYou have &f&l" + api.getRequests(p).size() + " &e&onew friend requests!\nUse &7/buddy&e&o to respond to them."));
    			return;
    		}
    		
    		if (api.hasUpdate(p)) {
    			api.updateUser(p);
    			api.handleUpdate(p);
    			return;
    		}
    		
    	
       
    }
}
