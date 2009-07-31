package com.rs2hd.packethandler;

import org.apache.mina.common.IoSession;

import com.rs2hd.Constants;
import com.rs2hd.model.Player;
import com.rs2hd.model.World;
import com.rs2hd.net.Packet;

public class RequestPacketHandler implements PacketHandler {

	public static final int TRADE_ANSWER = 253;
	
	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		switch(packet.getId()) {
		case TRADE_ANSWER:
			answerTrade(player, session, packet);
			break;
		}
	}

	public void answerTrade(Player player, IoSession session, Packet packet) {
		int id = packet.readLEShortA() & 0xFFFF;
		if(id < 0 || id >= Constants.PLAYER_CAP) {
			return;
		}
		Player other = World.getInstance().getPlayerList().get(id);
		if(other == null) {
			return;
		}
		player.getRequests().answerTrade(other);
	}

}
