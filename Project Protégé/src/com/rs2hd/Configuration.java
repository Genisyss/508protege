package com.rs2hd;

import com.rs2hd.model.Location;

/**
 * Holds some server configuration.
 * @author Graham
 *
 */
public class Configuration {
	
	private String name;
	private Location spawnPoint;
	private double experienceRate;
	private int maxConnectionsPerIp;
	private boolean debug;
	
	/**
	 * Gets the name of the server.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the spawn point of the server.
	 * @return
	 */
	public Location getSpawnPoint() {
		return spawnPoint;
	}
	
	/**
	 * Gets the experience rate of the server.
	 * @return
	 */
	public double getExperienceRate() {
		return experienceRate;
	}

	public int getMaxConnectionsPerIp() {
		return maxConnectionsPerIp;
	}

	public boolean isDebugEnabled() {
		return debug;
	}

}
