package tagless;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Event;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerBucketFillEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import commands.AddTagCommand;
import commands.DestoryTagCommand;
import database.database;
import entity.EntityTag;

public class Main extends PluginBase implements Listener {
	public static Map<Player, String> addStep;
	public static List<Player> destoryPlayer;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		addStep = new LinkedHashMap<>();
		destoryPlayer = new ArrayList<>();
		this.getServer().getCommandMap().register("addtag", new AddTagCommand());
		this.getServer().getCommandMap().register("deltag", new DestoryTagCommand());
		try {
			new database(this);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		try {
			database.saveNBT();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onteleport(PlayerTeleportEvent event) {
		if (event.getFrom().getLevel().getFolderName().equals(event.getTo().getLevel().getFolderName())) {
			database.getLevelbyTag(event.getFrom().getLevel())
				.forEach((EntityTag tag) -> tag.despawnFrom(event.getPlayer()));
			
		}
		database.getLevelbyTag(event.getTo().getLevel()).forEach((EntityTag tag) -> tag.spawnTo(event.getPlayer()));

		

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (database.getLevelbyTag(event.getPlayer().getLevel()).isEmpty()) {
			return;
		}
		database.getLevelbyTag(event.getPlayer().getLevel()).forEach((EntityTag tag) -> tag.spawnTo(event.getPlayer()));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		database.getLevelbyTag(event.getPlayer().getLevel())
				.forEach((EntityTag tag) -> tag.despawnFrom(event.getPlayer()));
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void destroyTag(BlockBreakEvent event) {
		if (destoryPlayer.contains(event.getPlayer())) {
			Block block = event.getBlock();
			for (EntityTag tag : database.getLevelbyTag(block.level)) {
				if (tag.getBlock().equals(block)) {
					database.deregisterTag(tag);
					destoryPlayer.remove(event.getPlayer());
					event.getPlayer().sendMessage(Main.success("Success : Tag-Destory"));
					return;
				}
			}
		}
	}

	@EventHandler
	public void onManama(PlayerBucketFillEvent event) {
		if (event.getBlockClicked().getId() == Block.LAVA)
			event.setCancelled();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void addTag(BlockBreakEvent event) {
		if (addStep.containsKey(event.getPlayer())) {
			Position pos = event.getBlock();

			EntityTag tag = new EntityTag(pos, addStep.get(event.getPlayer()));
			database.registerTag(tag);
			addStep.remove(event.getPlayer());
			event.getPlayer().sendMessage(Main.success("Success : Tag-Create"));
			tag.spawnTo(event.getPlayer());
		}
	}

	public static String message(String message) {
		return "§a§l[알림] §r§7" + message;
	}

	public static String alert(String message) {
		return "§c§l[알림] §r§7" + message;
	}

	public static String command(String message) {
		return "§l§6[알림]§r§7 " + message;
	}

	public static String success(String message) {
		return "§l§b[안내]§r§7 " + message;
	}
}
