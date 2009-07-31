package com.rs2hd.content;

import com.rs2hd.event.Event;
import com.rs2hd.model.Player;
import com.rs2hd.model.World;

/**
 * Updates wilderness levels.
 * @author Graham
 *
 */
public class WildernessUpdateEvent extends Event {

	/**
	 * Creates the event that updates wilderness levels every 500ms.
	 */
	public WildernessUpdateEvent() {
		super(500);
	}

	@Override
	public void execute() {
		for(Player p : World.getInstance().getPlayerList()) {
			p.updateWildernessState();
		}
	}

}
