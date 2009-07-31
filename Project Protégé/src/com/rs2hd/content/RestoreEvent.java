package com.rs2hd.content;

import com.rs2hd.event.Event;
import com.rs2hd.model.Player;
import com.rs2hd.model.Skills;
import com.rs2hd.model.World;

/**
 * Restores player stats.
 * @author Graham
 *
 */
public class RestoreEvent extends Event {

	public RestoreEvent() {
		super(30000);
	}

	@Override
	public void execute() {
		for(Player player : World.getInstance().getPlayerList()) {
			for(int i = 0; i < Skills.SKILL_COUNT; i++) {
				if(i == Skills.HITPOINTS || i == Skills.PRAYER) {
					continue;
				}
				int current = player.getSkills().getLevel(i);
				int normal = player.getSkills().getLevelForXp(i);
				if(current > normal) {
					player.getSkills().set(i, current - 1);
				} else if(normal > current) {
					player.getSkills().set(i, current + 1);
				}
			}
		}
	}

}
