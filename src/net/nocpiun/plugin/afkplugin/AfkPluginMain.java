package net.nocpiun.plugin.afkplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

public class AfkPluginMain extends JavaPlugin {
	public JavaPlugin plugin = this;
	
	@Override
	public void onEnable() {
		plugin.saveDefaultConfig();
		plugin.getLogger().info("Afk Plugin is Running!");
		
		Bukkit.getPluginManager().registerEvents(new GameEvents(), this);
		
		Bukkit.getPluginCommand("afk").setExecutor(new Afk(plugin));
		Bukkit.getPluginCommand("suicide").setExecutor(new Suicide());
	}
}

class Afk implements CommandExecutor {
	private JavaPlugin main = null;
	private FileConfiguration config = null;
	
	public Afk(JavaPlugin plugin) {
		main = plugin;
		config = main.getConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		String playerName = sender.getName();
		if(config.get(playerName) == null || !config.getBoolean(playerName)) {
			config.set(playerName, true);
			sender.getServer().dispatchCommand(sender.getServer().getConsoleSender(), "execute at "+ playerName +" as "+ playerName +" run me afk");
			main.getLogger().info(playerName +" afk");
		} else if(config.getBoolean(playerName)) {
			config.set(playerName, false);
			sender.getServer().dispatchCommand(sender.getServer().getConsoleSender(), "execute at "+ playerName +" as "+ playerName +" run me no longer afk");
			main.getLogger().info(playerName +" afk");
		}
		
		return true;
	}
}

class Suicide implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		sender.getServer().getPlayer(sender.getName()).setHealth(0);
		
		return true;
	}
}

final class GameEvents implements Listener {
	@EventHandler
	public void onSleep(PlayerBedEnterEvent event) {
		Player player = event.getPlayer();

		if(player.getServer().getWorld("serv").getTime() >= 12550 || player.getServer().getWorld("serv").getTime() < 900) {
			player.getServer().getWorld("serv").setTime(1000);
		}
	}
	
	@EventHandler
	public void onEnterBoat(VehicleEnterEvent event) {
		Entity player = event.getEntered();
		
		if(player instanceof Player) {
			player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "advancement grant "+ player.getName() +" only minecraft:cao/jingshen");
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		event.setMessage(message.replace('&', '¡ì'));
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		String content_0 = event.getLine(0);
		String content_1 = event.getLine(1);
		String content_2 = event.getLine(2);
		String content_3 = event.getLine(3);
		
		event.setLine(0, content_0.replace('&', '¡ì'));
		event.setLine(1, content_1.replace('&', '¡ì'));
		event.setLine(2, content_2.replace('&', '¡ì'));
		event.setLine(3, content_3.replace('&', '¡ì'));
	}
}
