package com.rs2hd.model;

/**
 * A shop item.
 * @author Graham
 *
 */
public class ShopItem extends Item {
	
	private boolean isStandard;
	private int standardAmount = 0;

	public ShopItem(int id, int amount, boolean isStandard, int std) {
		super(id, amount);
		this.isStandard = isStandard;
		this.standardAmount = std;
	}

	public boolean isStandard() {
		return this.isStandard;
	}
	
	public int getStandardAmount() {
		return standardAmount;
	}

	public boolean updateAmount() {
		if(getAmount() > standardAmount) {
			setAmount(getAmount()-1);
			return true;
		} else if(getAmount() < standardAmount) {
			setAmount(getAmount()+1);
			return true;
		}
		return false;
	}

}
