package com.rs2hd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.rs2hd.model.Player;

/**
 * Manages displaying of help screens.
 * @author Graham
 *
 */
public class HelpManager {

	public static void display(Player player, String file) {
		if(!file.matches("[A-Za-z ]+")) {
			file = "index";
		}
		String name = "data/help/"+file+".txt";
		File f = new File(name);
		if(!f.exists()) {
			player.getActionSender().sendMessage("Type ::help for help.");
			return;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String title = reader.readLine();
			if(title == null) {
				return;
			}
			List<String> lines = new ArrayList<String>();
			String line = null;
			while((line = reader.readLine()) != null) {
				lines.add(line);
			}
			String[] data = lines.toArray(new String[0]);
			player.getActionSender().sendQuestInterface(title, data);
		} catch(Exception ex) {
			return;
		}
	}

}
