package com.rs2hd.tools;

import java.io.File;

/**
 * Represents an individual package.
 * @author Graham
 *
 */
public class Package {
	
	public String key;
	public String name;
	public String author;
	public int version;
	public String[] dependencies;
	public String description;
	
	public boolean isInstalled() {
		return getFile().exists();
	}

	public File getFile() {
		return new File("packages/"+key+".py");
	}

}
