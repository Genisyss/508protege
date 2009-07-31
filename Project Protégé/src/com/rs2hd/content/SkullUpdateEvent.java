package com.rs2hd.content;

import com.rs2hd.event.Event;
import com.rs2hd.model.Player;
import com.rs2hd.model.World;

/**
 * Handles skull updates.
 * @author Graham
 *
 */
public class SkullUpdateEvent extends Event {

	/**
	 * Default gap on skull update events.
	 */
	public static final int TIME = 5000;

	/**
	 * Runs the skill update event every TIME ms.
	 */
	public SkullUpdateEvent() {
		super(TIME);
	}

	@Override
	public void execute() {
		for(Player p : World.getInstance().getPlayerList()) {
			p.getHeadIcons().decCycle();
			if(p.getHeadIcons().removeSkulls()) {
				p.getUpdateFlags().setAppearanceUpdateRequired(true);
			}
		}
	}

}
