package com.rs2hd.io;

import com.rs2hd.model.Player;
import com.rs2hd.model.PlayerDetails;

/**
 * Player load/save interface.
 * @author Graham
 *
 */
public interface PlayerLoader {
	
	/**
	 * Attempts to load a player.
	 * @param p The player to load.
	 * @return The result.
	 */
	public PlayerLoadResult load(PlayerDetails p);
	
	/**
	 * Attempts to save a player.
	 * @param p The player to save.
	 * @return <code>true</code> on success, <code>false</code> on failure.
	 */
	public boolean save(Player p);

}
