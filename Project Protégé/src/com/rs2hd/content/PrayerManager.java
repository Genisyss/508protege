package com.rs2hd.content;

import com.rs2hd.model.Player;

/**
 * Prayer support for combat.
 * 
 * @author blakeman8192
 */
public class PrayerManager {

	public void enablePrayer(Player player, Prayer prayer) {
		if (prayer instanceof DefencePrayer)
			player.setDefencePrayer((DefencePrayer) prayer);
		if (prayer instanceof StrengthPrayer)
			player.setStrengthPrayer((StrengthPrayer) prayer);
		if (prayer instanceof AttackPrayer)
			player.setAttackPrayer((AttackPrayer) prayer);
		if (prayer instanceof RangedPrayer)
			player.setRangedPrayer((RangedPrayer) prayer);
		if (prayer instanceof MagicPrayer)
			player.setMagicPrayer((MagicPrayer) prayer);
		if (prayer instanceof ProtectionPrayer)
			player.setProtectionPrayer((ProtectionPrayer) prayer);
		if (prayer instanceof OtherPrayer)
			player.setOtherPrayer((OtherPrayer) prayer);
	}

	/**
	 * Used for prayer types.
	 * 
	 * @author blakeman8192
	 */
	private interface Prayer {

	}

	public static enum DefencePrayer implements Prayer {
		DISABLED, THICK_SKIN, ROCK_SKIN, STEEL_SKIN
	}

	public static enum StrengthPrayer implements Prayer {
		DISABLED, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH
	}

	public static enum AttackPrayer implements Prayer {
		DISABLED, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES
	}

	public static enum RangedPrayer implements Prayer {
		DISABLED, SHARP_EYE, HAWK_EYE, EAGLE_EYE
	}

	public static enum MagicPrayer implements Prayer {
		DISABLED, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT
	}

	public static enum ProtectionPrayer implements Prayer {
		DISABLED, PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE,
		/*
		 * Include the following prayers in this category because they cannot be
		 * used concurrently with the protection prayers.
		 */
		RETRIBUTION, REDEMPTION, SMITE, CHIVALRY, PIETY
	}

	public static enum OtherPrayer implements Prayer {
		/*
		 * Used as an "other" prayer because although it is a protection prayer,
		 * it can be used concurrently with other protection prayers.
		 */
		DISABLED, PROTECT_FROM_SUMMONING
	}

}
