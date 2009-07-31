package com.rs2hd.model;

import com.rs2hd.net.Packet;
import com.rs2hd.packetbuilder.StaticPacketBuilder;

/**
 * Settings.
 * @author Graham
 *
 */
public class Settings {
	
	private boolean chat = true, split = false, mouse = true, aid = false;
	private boolean hideDeathInterface = false, autoRetaliate = true;
	private transient Player player;
	
	public void setDefaultSettings() {
		chat = true;
		split = false;
		mouse = true;
		aid = false;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void refresh() {
		player.getActionSender().sendConfig(171, !chat ? 1 : 0);
		if(!player.getSettings().isPrivateChatSplit()) {
    		StaticPacketBuilder spb = new StaticPacketBuilder().setId(152).setSize(Packet.Size.VAR_SHORT).addString("s").addInt(83);
			player.getSession().write(spb.toPacket());
			player.getSettings().setPrivateChatSplit(true);
			player.getActionSender().sendConfig(287, 1);
		} else {
			player.getSettings().setPrivateChatSplit(false);
			player.getActionSender().sendConfig(287, 0);
		}
		player.getActionSender().sendConfig(170, !mouse ? 1 : 0);
		player.getActionSender().sendConfig(427, aid ? 1 : 0);
		player.getActionSender().sendConfig(172, !autoRetaliate ? 1 : 0);
	}
	
	public boolean isHidingDeathInterface() {
		return hideDeathInterface;
	}
	
	public void setHideDeathInterface(boolean b) {
		this.hideDeathInterface = b;
	}
	
	public boolean isMouseTwoButtons() {
		return mouse;
	}
	
	public boolean isChatEffectsEnabled() {
		return chat;
	}
	
	public boolean isPrivateChatSplit() {
		return split;
	}
	
	public boolean isAcceptAidEnabled() {
		return aid;
	}
	
	public void setMouseTwoButtons(boolean mouse) {
		this.mouse = mouse;
	}
	
	public void setChatEffectsEnabled(boolean chat) {
		this.chat = chat;
	}
	
	public void setPrivateChatSplit(boolean split) {
		this.split = split;
	}
	
	public void setAcceptAidEnabled(boolean aid) {
		this.aid = aid;
	}

	public boolean isAutoRetaliate() {
		return this.autoRetaliate;
	}
	
	public void setAutoRetaliate(boolean retaliate) {
		this.autoRetaliate = retaliate;
	}

}
