package com.rs2hd.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.rs2hd.Constants;
import com.rs2hd.model.Player;
import com.rs2hd.model.PlayerDetails;
import com.rs2hd.util.XStreamUtil;
import com.thoughtworks.xstream.XStream;

/**
 * XML player loader/saver.
 * @author Graham
 *
 */
public class XStreamPlayerLoader implements PlayerLoader {
	
	@Override
	public PlayerLoadResult load(PlayerDetails p) {
		XStream xstream = XStreamUtil.getXStream();
		PlayerLoadResult result = new PlayerLoadResult();
		// by default wrong login
		result.returnCode = Constants.ReturnCodes.INVALID_PASSWORD;
		try {
			Player player = (Player) xstream.fromXML(new FileInputStream("data/savedgames/"+p.getUsername()+".xml"));
			// set the session
			player.getPlayerDetails().setSession(p.getSession());
			if(!player.getPlayerDetails().getPassword().equalsIgnoreCase(p.getPassword())) {
				// wrong password
				result.returnCode = Constants.ReturnCodes.INVALID_PASSWORD;
			} else {
				// login ok
				result.player = player;
				result.returnCode = Constants.ReturnCodes.LOGIN_OK;
			}
		} catch (FileNotFoundException e) {
			// no user with that name
			result.returnCode = Constants.ReturnCodes.LOGIN_OK;
			result.player = new Player(p);
			result.player = (Player) result.player.readResolve();
		}
		return result;
	}
	
	@Override
	public boolean save(Player p) {
		boolean flag = true;
		try {
			XStream xstream = XStreamUtil.getXStream();
			xstream.toXML(p, new FileOutputStream("data/savedgames/"+p.getPlayerDetails().getUsername()+".xml"));
		} catch(Exception e) {
			flag = false;
		}
		return flag;
	}
	
}
