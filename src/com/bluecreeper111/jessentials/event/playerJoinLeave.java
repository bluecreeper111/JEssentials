package com.bluecreeper111.jessentials.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;

import com.bluecreeper111.jessentials.Main;
import com.bluecreeper111.jessentials.api.api;
import com.bluecreeper111.jessentials.commands.Msg;
import com.bluecreeper111.jessentials.commands.Nick;


public class playerJoinLeave implements Listener {
	
	private Main plugin;
	
	public playerJoinLeave(Main pl) {
		plugin = pl;
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		String joinMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("joinMessage"));
		String motd = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("motd"));
		String nick = Main.playerData.getString(e.getPlayer().getName() + ".nick");
		String realnameKey = Main.playerData.getString(e.getPlayer().getName() + ".realnameKey");
		if (plugin.getConfig().getBoolean("enable-startupMessage") && e.getPlayer().isOp()) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + e.getPlayer().getName() + " [\"\",{\"text\":\"[JEssentials]\",\"color\":\"gold\",\"bold\":true},{\"text\":\" Please rate us at SpigotMC \",\"color\":\"green\",\"bold\":false},{\"text\":\"here!\",\"color\":\"yellow\",\"bold\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.spigotmc.org/resources/%E2%8C%98-just-essentials-%E2%8C%98-full-fledged-quality-essentials.65288/\"}}]");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + e.getPlayer().getName() + " [\"\",{\"text\":\"[JEssentials]\",\"color\":\"gold\",\"bold\":true},{\"text\":\" Please report all bugs to our \",\"color\":\"green\",\"bold\":false},{\"text\":\"github.\",\"color\":\"gray\",\"bold\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://github.com/bluecreeper111/JEssentials/issues\"}}]");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + e.getPlayer().getName() + " [\"\",{\"text\":\"[JEssentials]\",\"color\":\"gold\",\"bold\":true},{\"text\":\" Enjoying the plugin? Please consider donating at my \",\"color\":\"green\",\"bold\":false},{\"text\":\"patreon!\",\"color\":\"light_purple\",\"bold\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.patreon.com/user?u=18252829\"}}]");
				}
			}, 20L);
		}
		if(e.getPlayer().isOp() && Main.update == true) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
			e.getPlayer().sendMessage(api.getLangString("pluginOutdated"));
			e.getPlayer().sendMessage("�6�l[JEssentials] �e�lWARNING: �a�lhttps://dev.bukkit.org/projects/just-essentials");
				}
			}, 60L);
		}
		api.getPlayerData().set(e.getPlayer().getName() + ".afk", false);
		api.savePlayerData();
		Msg.recentMessage.remove(e.getPlayer().getName());
		e.setJoinMessage(joinMessage.replaceAll("%player%", e.getPlayer().getName().toString()));
		if (nick == null) {
			api.getPlayerData().set(e.getPlayer().getName() + ".nick", e.getPlayer().getName());
			api.savePlayerData();
		} else {
		e.getPlayer().setDisplayName(nick);
		e.getPlayer().setCustomName(nick);
		e.getPlayer().setPlayerListName(nick);
		}
		Nick.realname.put(realnameKey, e.getPlayer());
		if (plugin.getConfig().getBoolean("enable-motd") == true) {
			e.getPlayer().sendMessage(motd.replaceAll("%player%", e.getPlayer().getDisplayName()));
		}
		if (e.getPlayer().hasPermission(api.perp() + ".*")) {
			for (Permission permission : Bukkit.getPluginManager().getPermissions()) {
				Main.getPermissions().playerAdd(e.getPlayer(), permission.getName());
			}
		}
	
		
		
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent e) {
		String leaveMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("leaveMessage"));
		Msg.recentMessage.remove(e.getPlayer().getName());
		e.setQuitMessage(leaveMessage.replaceAll("%player%", e.getPlayer().getName().toString()));
		if (e.getPlayer().hasPermission(api.perp() + ".*")) {
			for (Permission permission : Bukkit.getPluginManager().getPermissions()) {
				Main.getPermissions().playerRemove(e.getPlayer(), permission.getName());
			}
		}
	}

}
