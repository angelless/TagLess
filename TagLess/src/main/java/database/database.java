package database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import entity.EntityTag;
import tagless.Main;

public class database {
	static LinkedHashMap<Level, List<EntityTag>> tagList = new LinkedHashMap<>();

	public database(Main main) throws IOException {
		main.getDataFolder().getPath();
		for (File f : new File(Server.getInstance().getDataPath() + "/worlds").listFiles()) {
			new File(f.getPath(), "tags").mkdirs();
			if (new File(f.getPath(), "tags").listFiles() == null) {
				break;
				
			}
			for (File file : new File(f.getPath(), "tags").listFiles()) {
			
				CompoundTag tag = NBTIO.read(file);

				Position pos = new Position(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"),
						Server.getInstance().getLevelByName(tag.getString("level")));

				String message = tag.getString("message");

				EntityTag e = new EntityTag(pos, message);
				if (!tagList.containsKey(e.getPosition().getLevel())) {
					tagList.put(e.getPosition().getLevel(), new ArrayList<>());
				}
				tagList.get(e.getPosition().getLevel()).add(e);
			}

		}

	}

	public static String toString(Position pos) {
		StringBuffer str = new StringBuffer();
		str.append(pos.getFloorX() + "").append("_" + pos.getFloorY()).append("_" + pos.getFloorZ());
		return str.toString();
	}

	public static List<EntityTag> getLevelbyTag(Level level) {
		return tagList.getOrDefault(level, new ArrayList<>());

	}

	public static void registerTag(EntityTag tag) {
		if (getLevelbyTag(tag.getPosition().getLevel()) == null) {
			ArrayList<EntityTag> list = new ArrayList<>();
			list.add(tag);
			tagList.put(tag.getPosition().getLevel(), list);
		}
		getLevelbyTag(tag.getPosition().getLevel()).add(tag);
	}

	public static void deregisterTag(EntityTag tag) {

		getLevelbyTag(tag.getPosition().getLevel()).remove(tag);
	}

	public static void saveNBT() throws IOException {
		for (Level l : tagList.keySet()) {
			File f = new File(Server.getInstance().getDataPath() + "/worlds", l.getFolderName());
			File file = new File(f, "tags");
			file.mkdirs();
			for (File fi : file.listFiles()) {
				fi.delete();
			}
			for (EntityTag tag : getLevelbyTag(l)) {

				NBTIO.write(new CompoundTag().putFloat("x", (float) tag.getPosition().x)
						.putFloat("y", (float) tag.getPosition().y).putFloat("z", (float) tag.getPosition().z)
						.putString("level", tag.getPosition().getLevel().getFolderName())
						.putString("message", tag.getMessage()), new File(file, toString(tag.getPosition()) + ".dat"));

			}
		}
	}

}
