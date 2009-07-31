package com.rs2hd.event;

import com.rs2hd.model.Location;
import com.rs2hd.model.Player;

/**
 * Abstract CoordinateEvent class that carrys out an event when the player
 * walks to a specific coordinate.
 * @author Graham
 *
 */
public abstract class CoordinateEvent {
	
	private Player player;
	private Location location;
	private Location oldLocation;
	private int failedAttempts = 0, distance = 0;
	private boolean reached = false;
	
	/**
	 * Creates the coordinate event.
	 * @param player Player to listen.
	 * @param location The exact tile they need to be on.
	 */
	public CoordinateEvent(Player player, Location location) {
		this.player   = player;
		this.location = location;
	}
	
	/**
	 * Creates the coordinate event.
	 * @param player Player to listen.
	 * @param location The tile.
	 * @param distance The distance (in tiles) they need to be from the target tile.
	 */
	public CoordinateEvent(Player player, Location location, int distance) {
		this.player   = player;
		this.location = location;
		this.distance = distance;
	}
	
	/**
	 * Sets the target location.
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * Gets the require distance.
	 * @return
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @return <code>true</code> if the player reached the tile yet, <code>false</code> if not.
	 */
	public boolean hasReached() {
		return reached;
	}
	
	/**
	 * Sets if the player has reached the target yet.
	 * @param reached
	 */
	public void setReached(boolean reached) {
		this.reached = reached;
	}
	
	/**
	 * Gets the player.
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets the number of failed attempts (timeout system).
	 * @return
	 */
	public int getFailedAttempts() {
		return failedAttempts;
	}
	
	/**
	 * Increments the number of failed attempts.
	 */
	public void incrementFailedAttempts() {
		failedAttempts++;
	}
	
	/**
	 * Sets the player's old location.
	 * @param location
	 */
	public void setOldLocation(Location location) {
		this.oldLocation = location;
	}
	
	/**
	 * Gets the old location.
	 * @return
	 */
	public Location getOldLocation() {
		return this.oldLocation;
	}
	
	/**
	 * Gets the target location.
	 * @return
	 */
	public Location getTargetLocation() {
		return this.location;
	}

	/**
	 * Called when the player is near enough to the target tile.
	 */
	public abstract void run();

}
