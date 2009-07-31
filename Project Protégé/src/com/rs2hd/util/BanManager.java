package com.rs2hd.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import com.rs2hd.model.Player;
import com.rs2hd.model.PlayerDetails;
import com.rs2hd.model.World;

/**
 * Manages banning, muting, etc.
 * @author Graham
 *
 */
public class BanManager {
	
	private Set<String> bannedNames = new HashSet<String>();
	private Set<String> bannedAddresses = new HashSet<String>();
	private Set<String> muted = new HashSet<String>();
	
	public boolean canLogin(PlayerDetails details) {
		if(bannedNames.contains(Misc.formatPlayerNameForProtocol(details.getUsername()))) {
			return false;
		}
		InetSocketAddress addr = (InetSocketAddress) details.getSession().getRemoteAddress();
		if(bannedAddresses.contains(addr.getAddress().getHostAddress())) {
			return false;
		}
		if(bannedAddresses.contains(addr.getAddress().getHostName())) {
			return false;
		}
		return true;
	}
	
	public void save() {
		try {
			// TODO do in workerthread
			XStreamUtil.getXStream().toXML(this, new FileOutputStream("data/bans.xml"));
		} catch (FileNotFoundException e) {}
	}
	
	public boolean canTalk(Player player) {
		return !muted.contains(Misc.formatPlayerNameForProtocol(player.getUsername()));
	}
	
	public void mute(String name) {
		muted.add(Misc.formatPlayerNameForProtocol(name));
		save();
	}

	public void unmute(String name) {
		muted.remove(Misc.formatPlayerNameForProtocol(name));
		save();
	}
	
	public void ban(String name) {
		bannedNames.add(Misc.formatPlayerNameForProtocol(name));
		Player player = World.getInstance().getPlayer(name);
		if(player != null) {
			player.getActionSender().sendLogout();
		}
		save();
	}
	
	public void unban(String name) {
		bannedNames.remove(Misc.formatPlayerNameForProtocol(name));
		save();
	}
	
	public void banHost(String name) {
		Player player = World.getInstance().getPlayer(name);
		if(player != null) {
			InetSocketAddress addr = (InetSocketAddress) player.getSession().getRemoteAddress();
			bannedAddresses.add(addr.getAddress().getHostAddress());
			bannedAddresses.add(addr.getAddress().getHostName());
			player.getActionSender().sendLogout();
			save();
		}
	}

}
