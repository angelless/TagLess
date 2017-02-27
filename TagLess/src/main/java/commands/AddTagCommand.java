package commands;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import tagless.Main;

public class AddTagCommand extends Command {

	public AddTagCommand() {
		super("tag", "add Tag", "/tag <message>", new String[] { "태그" });
		this.setPermission(Permission.DEFAULT_OP);
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("addTag", new CommandParameter[] { new CommandParameter("tagMessage: (new line = <br>)",
						CommandParameter.ARG_TYPE_RAW_TEXT, true) });
				put("테그추가", new CommandParameter[] {
						new CommandParameter("테그메세지 : (다음줄 = <br>)", CommandParameter.ARG_TYPE_RAW_TEXT, true) });
			}
		});

	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!sender.isPlayer()) {
			sender.sendMessage(Main.alert("YOU ARE NOT PLAYER"));
			return true;
		}

		if (args.length < 1) {
			sender.sendMessage(Main.alert("Please typing tag-message"));
			return true;
		}

		Player player = (Player) sender;
		String message =  String.join(" ", args);
		Main.addStep.put(player, message.replaceAll("<br>", "\n"));
		player.sendMessage(Main.success("Success init tag-data"));
		player.sendMessage(Main.success("Position setting -> Block-break"));
		return true;
	}
}
