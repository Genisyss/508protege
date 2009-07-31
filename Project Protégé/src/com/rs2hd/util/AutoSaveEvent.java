package com.rs2hd.util;

import com.rs2hd.Constants;
import com.rs2hd.event.Event;
import com.rs2hd.model.Player;
import com.rs2hd.model.World;

/**
 * Performs automatic player saving.
 * 
 * @author blake
 */
public class AutoSaveEvent extends Event {

	/**
	 * Constructs a new auto save event.
	 */
	public AutoSaveEvent() {
		super(Constants.AUTO_SAVE_RATE * 1000);
	}

	/**
	 * Executes the save sequence.
	 */
	public void execute() {
		for (Player player : World.getInstance().getPlayerList()) {
			World.getInstance().getEngine().getWorkerThread()
					.savePlayer(player);
		}
	}

}
