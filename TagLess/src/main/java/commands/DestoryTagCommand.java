package commands;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import tagless.Main;

public class DestoryTagCommand extends Command {

	public DestoryTagCommand() {
		super("deltag", "destroy Tag... command -> Tag Block-break", "/deltag", new String[] { "테그삭제" });
		this.setPermission(Permission.DEFAULT_OP);
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("deltag", new CommandParameter[] {});
			}
		});
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.isPlayer()) {
			sender.sendMessage(Main.alert("YOU NOT PLAYER"));
			return true;
		}

		Main.destoryPlayer.add((Player) sender);
		sender.sendMessage(Main.alert("Destroy the tag block"));
		return true;

	}
}
