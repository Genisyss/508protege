package com.rs2hd.util;

import com.rs2hd.model.Player;

/**
 * Does grand exchange stuff. Clever.
 * @author Graham
 *
 */
public class GrandExchangeManager {

	/**
	 * Opens the grand exchange for the specified player.
	 * @param player
	 */
	public void openExchange(Player player) {
		// TODO is this related to GE? player.getActionSender().sendRunScript(29, new Object[] {}, "");
		// TODO dunno what this does :/ player.getActionSender().sendConfig2(563, 4194304);
		player.getActionSender().sendConfig1(1112, -1);
		player.getActionSender().sendConfig1(1113, -1);
		player.getActionSender().sendConfig1(1111, 1);
		player.getActionSender().sendConfig1(1109, -1);
		player.getActionSender().sendInterface(105, true);
	}

	/**
	 * Handles a button in the Grand Exchange interfaces.
	 * @param player
	 * @param interfaceId
	 * @param buttonId
	 * @param buttonId2
	 */
	public void handleButton(Player player, int interfaceId, int buttonId, int buttonId2) {
		if(interfaceId == 105) {
			handleMainButton(player, buttonId, buttonId2);
		} else if(interfaceId == 389) {
			handleItemSearchButton(player, buttonId, buttonId2);
		}
	}

	/**
	 * Handles a button in the item search interface.
	 * @param player
	 * @param buttonId
	 * @param buttonId2
	 */
	public void handleItemSearchButton(Player player, int buttonId, int buttonId2) {
		if(buttonId == 10) {
			player.getActionSender().sendCloseChatboxInterface();
		}
	}

	/**
	 * Handles a button in the main exchange interface.
	 * @param player
	 * @param buttonId
	 * @param buttonId2
	 */
	public void handleMainButton(Player player, int buttonId, int buttonId2) {
		if(buttonId == 30) {
			openBuyOffer(player, 0);
		} else if(buttonId == 31) {
			openSellOffer(player, 0);
		} else if(buttonId == 46) {
			openBuyOffer(player, 1);
		} else if(buttonId == 47) {
			openSellOffer(player, 1);
		} else if(buttonId == 127) {
			openExchange(player);
			player.getActionSender().sendCloseChatboxInterface();
		} else if(buttonId == 190) {
			confirmOffer(player);
		} else if(buttonId == 194) {
			openSearch(player);
		}
	}

	/**
	 * Opens the search.
	 * @param player
	 */
	public void openSearch(Player player) {
		player.getActionSender().sendChatboxInterface(389);
		// TODO fix it player.getActionSender().sendRunScript(570, new Object[] { "Grand Exchange Item Search" }, "s");
	}

	/**
	 * Confirms an offer.
	 * @param player
	 */
	public void confirmOffer(Player player) {
		// TODO do the offer!
		player.getActionSender().sendConfig1(1112, -1);
		player.getActionSender().sendConfig1(1113, -1);
		player.getActionSender().sendConfig1(1111, 1);
		player.getActionSender().sendConfig1(1109, -1);
		// ALSO: 1110, 1 ?? NEEDED?
		player.getActionSender().sendInterface(105, true);
	}

	/**
	 * Opens a new buy offer.
	 * @param player
	 * @param slot
	 */
	public void openBuyOffer(Player player, int slot) {
		player.getActionSender().sendConfig1(1109, -1);
		player.getActionSender().sendConfig1(1112, 0);
		player.getActionSender().sendConfig1(1113, 0);
		openSearch(player);
	}
	
	/**
	 * Opens a new sell offer.
	 * @param player
	 * @param slot
	 */
	public void openSellOffer(Player player, int slot) {
		player.getActionSender().sendConfig1(1109, -1);
		player.getActionSender().sendConfig1(1112, 0);
		player.getActionSender().sendConfig1(1113, 1);
	}

	/**
	 * Closes the GE interfaces.
	 * @param player
	 */
	public void close(Player player) {
		// TODO check if they had it open!
		player.getActionSender().sendCloseChatboxInterface();
		player.getActionSender().sendCloseInventoryInterface();
		player.getActionSender().sendCloseInterface();
	}

}
