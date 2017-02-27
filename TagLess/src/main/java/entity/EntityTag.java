package entity;

import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

public class EntityTag {
	Position pos = null;
	String message = null;
	private long eid;

	public EntityTag(Position pos, String message) {
		this.pos = pos;
		this.message = message;
		this.eid = Entity.entityCount++;
	}

	public void spawnTo(Player player) {
		if (pos.getLevel().getFolderName().equals(player.getLevel().getFolderName())) {
			AddPlayerPacket pk = new AddPlayerPacket();
			pk.entityRuntimeId = eid;
			pk.entityUniqueId = eid;
			pk.item = Item.get(Item.AIR);
			pk.username = "";
			pk.x = (float) (pos.getFloorX() + 0.5f);
			pk.y = (float) pos.getFloorY();
			pk.z = (float) pos.getFloorZ() + 0.5f;
			pk.speedX = 0;
			pk.speedY = 0;
			pk.speedZ = 0;
			pk.yaw = 0;
			pk.pitch = 0;
			pk.uuid = UUID.randomUUID();
			long flags = 0;
			flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
			flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
			flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
			flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
			flags |= 1 << Entity.DATA_FLAG_NO_AI;
			EntityMetadata meta = new EntityMetadata();
			meta.putLong(Entity.DATA_FLAGS, flags);
			meta.putString(Entity.DATA_NAMETAG, message);
			meta.putLong(Entity.DATA_LEAD_HOLDER_EID, -1);
			meta.putByte(Entity.DATA_LEAD, 0);
			meta.putFloat(Entity.DATA_SCALE, 0.001f);
			pk.metadata = meta;
			player.dataPacket(pk);
		}
	}

	public void spawnToAll() {
		Server.getInstance().getOnlinePlayers().forEach((uuid, player) -> {
			spawnTo(player);
		});
	}

	public void despawnFrom(Player player) {
		RemoveEntityPacket pk = new RemoveEntityPacket();
		pk.eid = eid;
		player.dataPacket(pk);
	}

	public void despawnFromAll() {
		Server.getInstance().getOnlinePlayers().forEach((uuid, player) -> {
			despawnFrom(player);
		});
	}

	public Position getPosition() {
		return pos;
	}

	public Block getBlock() {
		return pos.getLevelBlock();
	}

	public String getMessage() {
		return this.message;
	}

}
