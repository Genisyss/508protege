package com.rs2hd;

import com.rs2hd.util.benchmark.Benchmark;
import com.rs2hd.util.log.Logger;

/**
 * Generic main class which starts the server.
 * @author Graham
 *
 */
public class Main {
	
	/**
	 * Logger instance.
	 */
	private static Logger logger = Logger.getInstance();
	
	/**
	 * Entry point of the program.
	 * 
	 * Sets everything rolling.
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Server s = null;
				try {
					s = new Server();
				} catch (Exception e) {
					logger.error(e.toString());
					logger.stackTrace(e);
					return;
				}
				System.gc();
				Benchmark.init();
				s.go();
				
			}
		}, "GameEngine").start();
	}

}
