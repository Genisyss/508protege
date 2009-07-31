package com.rs2hd.packethandler;

import org.apache.mina.common.IoSession;

import com.rs2hd.model.Player;
import com.rs2hd.net.Packet;
import com.rs2hd.script.ScriptManager;

/**
 * Handles any commands sent to the client.
 * @author Graham
 *
 */
public class CommandPacketHandler implements PacketHandler {

	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		String command = packet.readRS2String();
		String[] cmd = command.split(" ");
		cmd[0] = cmd[0].toLowerCase();
		try {
			ScriptManager.getInstance().invoke("command_"+cmd[0], player, cmd);
		} catch(Exception e) {
			player.getActionSender().sendMessage("Malformed command or error: " + e.getMessage() + ".");
			e.printStackTrace();
		}
	}

}
