package com.rs2hd.content;

import com.rs2hd.event.Event;
import com.rs2hd.model.Player;
import com.rs2hd.model.World;

/**
 * Server-wide event to update special timers.
 * @author Graham
 *
 */
public class SpecialUpdateEvent extends Event {

	/**
	 * Creates the event to run every 10 seconds.
	 */
	public SpecialUpdateEvent() {
		super(10000);
	}

	@Override
	public void execute() {
		for(Player p : World.getInstance().getPlayerList()) {
			p.getSpecials().tick();
		}
	}

}
