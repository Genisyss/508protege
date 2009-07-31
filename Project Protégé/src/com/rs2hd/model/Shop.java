package com.rs2hd.model;

/**
 * Represents a shop.
 * @author Graham
 *
 */
public class Shop {
	
	public static final int SIZE = 500;
	private String name;
	private int id;
	private boolean isGeneralStore;
	private Container<Item> stock   = new Container<Item>(SIZE, false);
	private transient Container<ShopItem> mainStock;
	private transient Container<ShopItem> playerStock;
	private transient boolean isUpdateRequired;
	
	public Shop() {
	}
	
	public int getId() {
		return id;
	}
	
	public void refresh(Player player) {
		player.getActionSender().sendItems(-1, 64209, 93, player.getInventory().getContainer());
		player.getActionSender().sendShopTab(player.getShopConfiguration().isInMainStock(), isGeneralStore);
		if(player.getShopConfiguration().isInMainStock()) {
			player.getActionSender().sendInterfaceConfig(620, 23, false);
			player.getActionSender().sendInterfaceConfig(620, 24, true);
			player.getActionSender().sendInterfaceConfig(620, 29, false);
			player.getActionSender().sendInterfaceConfig(620, 25, true);
			player.getActionSender().sendInterfaceConfig(620, 27, true);
			player.getActionSender().sendInterfaceConfig(620, 26, false);
			player.getActionSender().sendAccessMask(1278, 23, 620, 0, 40);
		} else {
			player.getActionSender().sendInterfaceConfig(620, 23, true);
			player.getActionSender().sendInterfaceConfig(620, 24, false);
			player.getActionSender().sendInterfaceConfig(620, 29, true);
			player.getActionSender().sendInterfaceConfig(620, 25, false);
			player.getActionSender().sendInterfaceConfig(620, 27, false);
			player.getActionSender().sendInterfaceConfig(620, 26, true);
			player.getActionSender().sendAccessMask(1278,24, 620, 0, 40);
		}
		player.getActionSender().sendItems(-1, 64209, 93, player.getInventory().getContainer());
		player.getActionSender().sendItems(-1, 64271, 31, playerStock.asItemContainer());
	}
	
	public Object readResolve() {
		isUpdateRequired = false;
		mainStock   = new Container<ShopItem>(SIZE, false);
		playerStock = new Container<ShopItem>(SIZE, true);
		int idx = 0;
		for(Item i : stock.getItems()) {
			mainStock.set(idx++, new ShopItem(i.getId(), i.getAmount(), true, i.getAmount()));
		}
		if(!isGeneralStore) {
			for(Item i : stock.getItems()) {
				playerStock.set(idx++, new ShopItem(i.getId(), i.getAmount(), true, 0));
			}
		}
		return this;
	}
	
	public void updateAmounts() {
		for(int i = 0; i < playerStock.getSize(); i++) {
			ShopItem si = playerStock.get(i);
			if(si != null) {
				if(si.getAmount() > si.getStandardAmount()) {
					si.setAmount(si.getAmount()-1);
					this.setUpdateRequired(true);
				} else if(si.getAmount() < si.getStandardAmount()) {
					si.setAmount(si.getAmount()+1);
					this.setUpdateRequired(true);
				}
				if(si.getAmount() == 0 && si.getStandardAmount() == 0 && !si.isStandard()) {
					playerStock.set(i, null);
					this.setUpdateRequired(true);
				}
			}
		}
	}
	
	public boolean isUpdateRequired() {
		return this.isUpdateRequired;
	}
	
	public void setUpdateRequired(boolean b) {
		this.isUpdateRequired = b;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isGeneralStore() {
		return isGeneralStore;
	}
	
	public Container<ShopItem> getMainStock() {
		return mainStock;
	}
	
	public Container<ShopItem> getPlayerStock() {
		return playerStock;
	}

}
