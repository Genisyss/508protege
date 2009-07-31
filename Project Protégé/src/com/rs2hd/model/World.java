package com.rs2hd.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rs2hd.Configuration;
import com.rs2hd.Constants;
import com.rs2hd.GameEngine;
import com.rs2hd.content.Combat;
import com.rs2hd.content.HealEvent;
import com.rs2hd.content.RestoreEvent;
import com.rs2hd.content.RunEnergyEvent;
import com.rs2hd.content.SkullUpdateEvent;
import com.rs2hd.content.SpecialUpdateEvent;
import com.rs2hd.content.WildernessUpdateEvent;
import com.rs2hd.event.CoordinateEvent;
import com.rs2hd.event.Event;
import com.rs2hd.packetbuilder.NPCUpdate;
import com.rs2hd.packetbuilder.PlayerUpdate;
import com.rs2hd.script.ScriptManager;
import com.rs2hd.util.AutoSaveEvent;
import com.rs2hd.util.BanManager;
import com.rs2hd.util.EntityList;
import com.rs2hd.util.GrandExchangeManager;
import com.rs2hd.util.ItemManager;
import com.rs2hd.util.Misc;
import com.rs2hd.util.ProjectileManager;
import com.rs2hd.util.ShopManager;
import com.rs2hd.util.XStreamUtil;
import com.rs2hd.util.log.Logger;

/**
 * Represents the 'game world'.
 * @author Graham
 *
 */
public class World {

	/**
	 * Logger instance.
	 */
	private Logger logger = Logger.getInstance();
	
	/**
	 * The world instance.
	 */
	private static World instance = new World();
	
	/**
	 * A list of connected players.
	 */
	private EntityList<Player> players;
	
	/**
	 * A list of npcs.
	 */
	private EntityList<NPC> npcs;
	
	/**
	 * A list of pending events.
	 */
	private List<Event> events;
	private List<Event> eventsToAdd;
	private List<Event> eventsToRemove;
	
	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	/**
	 * The item manager.
	 */
	private ItemManager itemManager;
	
	/**
	 * The shop manager.
	 */
	private ShopManager shopManager;
	
	/**
	 * The projectile manager.
	 */
	private ProjectileManager projectileManager;
	
	/**
	 * The configuration.
	 */
	private Configuration configuration;
	
	/**
	 * The ban manager.
	 */
	private BanManager banManager;
	
	/**
	 * Does ge stuff.
	 */
	private GrandExchangeManager grandExchangeManager = new GrandExchangeManager();
	
	/**
	 * We create the world here.
	 */
	private World() {
		try {
			configuration = (Configuration) XStreamUtil.getXStream().fromXML(new FileInputStream("data/config.xml"));
		} catch (FileNotFoundException e1) {
			System.err.println("FATAL: Could not load configuration file. Aborting server immediately.");
			System.exit(0);
		}
		logger.info("Loaded configuration [" + configuration.getName() + "].");
		players = new EntityList<Player>(Constants.PLAYER_CAP);
		npcs = new EntityList<NPC>(Constants.NPC_CAP);
		events = new ArrayList<Event>();
		eventsToAdd = new ArrayList<Event>();
		eventsToRemove = new ArrayList<Event>();
		itemManager = new ItemManager();
		projectileManager = new ProjectileManager();
		try {
			banManager = (BanManager) XStreamUtil.getXStream().fromXML(new FileInputStream("data/bans.xml"));
		} catch (FileNotFoundException e) {
			banManager = new BanManager();
		}
		registerGlobalEvents();
	}
	
	/**
	 * Gets the game engine.
	 * @return
	 */
	public GameEngine engine() {
		return engine;
	}
	
	/**
	 * Gets the configuration.
	 * @return
	 */
	public Configuration getConfiguration() {
		return configuration;
	}
	
	/**
	 * Register our global events.
	 */
	public void registerGlobalEvents() {
		// apparently some of these aren't global
		registerEvent(new RunEnergyEvent());
		registerEvent(new HealEvent());
		registerEvent(new WildernessUpdateEvent());
		registerEvent(new SkullUpdateEvent());
		registerEvent(new SpecialUpdateEvent());
		registerEvent(new RestoreEvent());
		registerEvent(new AutoSaveEvent());
	}
	
	/**
	 * Registers an event.
	 * @param event
	 */
	public void registerEvent(Event event) {
		eventsToAdd.add(event);
	}
	
	/**
	 * Registers a 'coordiante' event.
	 * @param event
	 */
	public void registerCoordinateEvent(final CoordinateEvent event) {
		registerEvent(new Event(0) {
			@Override
			public void execute() {
				boolean isAtTarget = event.getPlayer().getLocation().withinDistance(event.getTargetLocation(), event.getDistance());
				if(event.getPlayer().getUpdateFlags().didTeleport()) {
					this.stop();
				}
				if((isAtTarget && event.getPlayer().getLocation().equals(event.getOldLocation())) || event.getFailedAttempts() >= 2) {
					if(this.getTick() == 0) {
						event.run();
						this.stop();
					} else {
						if(!event.hasReached()) {
							event.setReached(true);
						} else {
							event.run();
							this.stop();
						}
					}
				} else {
					if(!event.getPlayer().getLocation().equals(event.getOldLocation())) {
						event.setOldLocation(event.getPlayer().getLocation());
					} else {
						event.incrementFailedAttempts();
					}
				}
				this.setTick(500);
			}
		});
	}
	
	/**
	 * Processes any pending events.
	 */
	public void processEvents() {
		for(Event e : eventsToAdd) {
			events.add(e);
		}
		eventsToAdd.clear();
		for(Event e : events) {
			if(e.isStopped()) {
				eventsToRemove.add(e);
			} else if(e.isReady()) {
				e.run();
			}
		}
		for(Event e : eventsToRemove) {
			events.remove(e);
		}
		eventsToRemove.clear();
	}
	
	/**
	 * Get the world instance.
	 * @return
	 */
	public static World getInstance() {
		return instance;
	}
	
	/**
	 * Called whenever there is a major update.
	 */
	public void majorUpdate() {
		for(Player p : players) {
			p.tick();
			p.processQueuedHits();
			p.getWalkingQueue().getNextPlayerMovement();
			Combat.handleCombat(p);
		}
		for(NPC n : npcs) {
			n.processQueuedHits();
			n.tick();
			Combat.handleCombat(n);
		}
		for(Player p : players) {
			// sometimes players aren't removed always: do that here
			if(!p.getPlayerDetails().getSession().isConnected()) {
				unregister(p);
			} else {
				PlayerUpdate.update(p);
				NPCUpdate.update(p);
			}
		}
		for(Player p : players) {
			p.getUpdateFlags().clear();
			p.getHits().clear();
			p.setRebuildNpcList(false);
			if(p.getUpdateFlags().isClearFaceTo()) {
				p.getUpdateFlags().setFaceToUpdateRequired(true, 0);
				p.getUpdateFlags().setClearFaceTo(false);
			}
			shopManager.tick(p);
		}
		for(NPC n : npcs) {
			n.getUpdateFlags().clear();
			n.getHits().clear();
			if(n.getUpdateFlags().isClearFaceTo()) {
				n.getUpdateFlags().setFaceToUpdateRequired(true, 0);
				n.getUpdateFlags().setClearFaceTo(false);
			}
		}
	}
	
	/**
	 * Called whenever there is a minor update.
	 */
	public void minorUpdate() {
		
	}
	
	/**
	 * Called every tick.
	 */
	public void tick() {
		for(Player p : players) {
			p.processQueuedPackets();
		}
		processEvents();
	}

	/**
	 * Gets the players list.
	 * @return
	 */
	public EntityList<Player> getPlayerList() {
		return players;
	}
	
	/**
	 * Gets the npcs list.
	 * @return
	 */
	public EntityList<NPC> getNpcList() {
		return npcs;
	}
	
	/**
	 * Register a player.
	 * @param p
	 * @return the player slot
	 */
	public int register(Player p) {
		int slot = -1;
		// TODO make it so this works better
		players.add(p);
		slot = p.getIndex();
		if(slot != -1) {
			logger.info("Registered " + p.getPlayerDetails().getDisplayName() + " [idx="+slot+",online="+players.size()+"]");
			p.getFriends().registered();
		} else {
			logger.info("Could not register " + p.getPlayerDetails().getDisplayName() + " - too many online [online="+players.size()+"]");
		}
		return slot;
	}
	
	/**
	 * Unregister a player.
	 * @param p
	 */
	public void unregister(Player p) {
		players.remove(p);
		p.destroy();
		engine.getWorkerThread().savePlayer(p);
		logger.info("Unregistered " + p.getPlayerDetails().getDisplayName() + " [online="+players.size()+"]");
		p.getFriends().unregistered();
		ScriptManager.getInstance().invoke("logout", p);
	}
	
	/**
	 * Register an NPC.
	 * @param n
	 */
	public void register(NPC n) {
		npcs.add(n);
	}
	
	/**
	 * Unregister an NPC.
	 * @param n
	 */
	public void unregister(NPC n) {
		npcs.remove(n);
	}

	/**
	 * Sets the game engine.
	 * @param gameEngine
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void setEngine(GameEngine gameEngine) throws IOException {
		this.engine = gameEngine;
		logger.info("Loading npcs spawns...");
		List<NPC> spawns = (List<NPC>) XStreamUtil.getXStream().fromXML(new FileInputStream("data/npcs.xml"));
		for(NPC n : spawns) {
			npcs.add(n);
		}
		logger.info("Loaded " + spawns.size() + " npc spawns.");
		logger.info("Loading item spawns...");
		List<Spawn> items = (List<Spawn>) XStreamUtil.getXStream().fromXML(new FileInputStream("data/spawns.xml"));
		for(Spawn s : items) {
			itemManager.createSpawn(s);
		}
		logger.info("Loaded " + items.size() + " item spawns.");
		shopManager = new ShopManager();
		ScriptManager.getInstance().reload();
	}
	
	/**
	 * Gets the world game engine.
	 * @return
	 */
	public GameEngine getEngine() {
		return engine;
	}

	/**
	 * Gets mapdata for a region.
	 * @param region
	 * @return
	 */
	public int[] getMapData(int region) {
		return engine.getMapData(region);
	}
	
	/**
	 * Checks if a player is online.
	 * @param name
	 * @return
	 */
	public boolean isOnline(String name) {
		name = Misc.formatPlayerNameForProtocol(name);
		for(Player p : players) {
			if(p != null) {
				if(Misc.formatPlayerNameForProtocol(p.getPlayerDetails().getUsername()).equals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Gets the item manager.
	 * @return
	 */
	public ItemManager getItemManager() {
		return itemManager;
	}
	
	/**
	 * Gets the shop manager.
	 * @return
	 */
	public ShopManager getShopManager() {
		return shopManager;
	}

	public ProjectileManager getProjectileManager() {
		return projectileManager;
	}
	
	public BanManager getBanManager() {
		return banManager;
	}
	
	public GrandExchangeManager getGrandExchangeManager() {
		return grandExchangeManager;
	}

	public Player getPlayer(String name) {
		name = Misc.formatPlayerNameForProtocol(name);
		for(Player player : this.players) {
			if(Misc.formatPlayerNameForProtocol(player.getUsername()).equals(name)) {
				return player;
			}
		}
		return null;
	}

}
