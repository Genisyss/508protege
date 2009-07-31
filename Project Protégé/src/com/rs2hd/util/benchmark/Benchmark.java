package com.rs2hd.util.benchmark;

import java.util.Deque;
import java.util.LinkedList;

import com.rs2hd.event.Event;
import com.rs2hd.model.World;
import com.rs2hd.util.log.Logger;

public class Benchmark {
	
	private static Deque<Long> memoryByteSamples = new LinkedList<Long>();
	private static long memoryBytes;
	private static long baseMemory;
	
	private static Deque<Integer> networkByteSamples = new LinkedList<Integer>();
	private static int networkBytes;
	
	public static void init() {
		if(!World.getInstance().getConfiguration().isDebugEnabled()) {
			return;
		}
		baseMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}
	
	static {
		if(World.getInstance().getConfiguration().isDebugEnabled()) {
			World.getInstance().registerEvent(new Event(1000) {
				@Override
				public void execute() {
					if(World.getInstance().getPlayerList().size() == 0) {
						baseMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					}
					{
						memoryBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
						if(memoryByteSamples.size() >= 60) {
							memoryByteSamples.removeFirst();
						}
						memoryByteSamples.addLast(memoryBytes);
						int averageBytes = 0;
						for(Long integer : memoryByteSamples) {
							averageBytes += integer;
						}
						averageBytes /= memoryByteSamples.size();
						Logger.getInstance().debug("RAM: Current: " + memoryBytes + " bytes, Average (last minute): " + averageBytes + " bytes");
						int players = World.getInstance().getPlayerList().size();
						if(players > 0) {
							memoryBytes -= baseMemory;
							averageBytes -= baseMemory;
							memoryBytes /= players;
							averageBytes /= players;
							Logger.getInstance().debug("RAM: Current: " + memoryBytes + " bytes per player, Average (last minute): " + averageBytes + " bytes per player");
						}
					}
					synchronized(Benchmark.class) {
						if(networkByteSamples.size() >= 60) {
							 networkByteSamples.removeFirst();
						}
						networkByteSamples.addLast(networkBytes);
						int averageBytes = 0;
						for(Integer integer : networkByteSamples) {
							averageBytes += integer;
						}
						averageBytes /= networkByteSamples.size();
						Logger.getInstance().debug("Networking: Current: " + networkBytes + " bytes, Average (last minute): " + averageBytes + " bytes");
						int players = World.getInstance().getPlayerList().size();
						if(players > 0) {
							networkBytes /= players;
							averageBytes /= players;
							Logger.getInstance().debug("Networking: Current: " + networkBytes + " bytes per player, Average (last minute): " + averageBytes + " bytes per player");
						}
						networkBytes = 0;
					}
				}
			});
		}
	}
	
	public static void addNetworkBytes(int b) {
		if(!World.getInstance().getConfiguration().isDebugEnabled()) {
			return;
		}
		synchronized(Benchmark.class) {
			networkBytes += b;
		}
	}

}
