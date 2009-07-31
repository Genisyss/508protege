package com.rs2hd;

/**
 * Holds global server constants.
 * @author Graham
 *
 */
public class Constants {
	
	/**
	 * Prevent an instance being created.
	 */
	private Constants() {}
	
	/**
	 * Initial idle time - before the protocol version exchange/login/update has done.
	 */
	public static final int SESSION_INITIAL_IDLE_TIME = 1;
	
	/**
	 * Standard session idle time, in seconds.
	 * 
	 * We make this slightly longer to allow for in-game lag.
	 */
	public static final int SESSION_IDLE_TIME = 90;
	
	/**
	 * Connection throttle interval, in seconds.
	 * 
	 * This needs to be kept relatively low as an update request then a login request will come soon after each other.
	 */
	public static final long THROTTLE_FILTER_INTERVAL = 1;
	
	/**
	 * RS2 incoming packet lengths.
	 */
	public static final int[] PACKET_LENGTHS = new int[] {
		// 	0	1	2	3	4	5	6	7	8	9
		-3,	-3,	8,	8,	-3,	-3,	-3,	2,	-3,	-3,	// 0
		-3,	-3,	-3,	10,	-3,	-3,	-3,	-3,	-3,	-3,	// 1
		-3,	6,	4,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	// 2
		8,	-3,	-3,	-3,	-3,	-3,	-3,	2,	2,	-3,	// 3
		16,	-3,	-3,	-3,	-3,	-3,	-3,	0,	-3,	-1,	// 4
		-3,	-3,	2,	-3,	-3,	-3,	-3,	-3,	-3,	6,	// 5
		0,	8,	-3,	6,	-3,	-3,	-3,	-3,	-3,	-3,	// 6
		-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	// 7
		-3,	-3,	-3,	-3,	2,	-3,	-3,	-3,	2,	-3,	// 8
		6,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	4,	// 9
		-3,	-3,	-3,	-3,	-3,	-3,	-3,	-1,	0,	-3,	// 10
		-3,	-3, -3,	4,	-3,	0,	-3,	-1,	-3,	-1,	// 11
		-3,	-3,	-3,	2,	-3,	-3,	-3,	-3,	-3,	6,	// 12
		-3,	10,	8,	6,	-3,	-3,	-3,	-3,	-1,	-3,	// 13
		-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	// 14
		-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	6,	-3,	// 15
		2,	-3,	-3,	-3,	-3,	4,	-3,	9,	-3,	6,	// 16
		-3,	-3,	-3,	6,	-3,	-3,	-3,	-3,	-1,	12,	// 17
		-3,	-3,	-3,	-3,	-3,	-3,	8,	-3,	-3,	-3,	// 18
		-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	-3,	2,	// 19
		-3,	6,	-3,	8,	-3,	-3,	-3,	-3,	-3,	-3,	// 20
		-3,	8,	-3,	-3,	6,	-3,	-3,	-3,	-3,	-3,	// 21
		8,	-3,	-1,	-3,	14,	-3,	-3,	2,	6,	-3,	// 22
		-3,	-3,	6,	6,	-3,	-3,	-3,	-3,	-3,	-3,	// 23
		-3,	-3,	-3,	-3,	-3,	-3,	-3,	4,	1,	-3,	// 24
		4,	-3,	-3,	2,	-3,						// 25
	};
	
	/**
	 * 508 cache 'update keys'.
	 */
	public static final int[] UPDATE_KEYS = {
		0xff, 0x00, 0xff, 0x00, 0x00, 0x00, 0x00, 0xd8, 
		0x84, 0xa1, 0xa1, 0x2b, 0x00, 0x00, 0x00, 0xba, 
		0x58, 0x64, 0xe8, 0x14, 0x00, 0x00, 0x00, 0x7b, 
		0xcc, 0xa0, 0x7e, 0x23, 0x00, 0x00, 0x00, 0x48, 
		0x20, 0x0e, 0xe3, 0x6e, 0x00, 0x00, 0x01, 0x88, 
		0xec, 0x0d, 0x58, 0xed, 0x00, 0x00, 0x00, 0x71, 
		0xb9, 0x4c, 0xc0, 0x50, 0x00, 0x00, 0x01, 0x8b, 
		0x5b, 0x61, 0x79, 0x20, 0x00, 0x00, 0x00, 0x0c, 
		0x0c, 0x69, 0xb1, 0xc8, 0x00, 0x00, 0x02, 0x31, 
		0xc8, 0x56, 0x67, 0x52, 0x00, 0x00, 0x00, 0x69, 
		0x78, 0x17, 0x7b, 0xe2, 0x00, 0x00, 0x00, 0xc3, 
		0x29, 0x76, 0x27, 0x6a, 0x00, 0x00, 0x00, 0x05, 
		0x44, 0xe7, 0x75, 0xcb, 0x00, 0x00, 0x00, 0x08, 
		0x7d, 0x21, 0x80, 0xd5, 0x00, 0x00, 0x01, 0x58, 
		0xeb, 0x7d, 0x49, 0x8e, 0x00, 0x00, 0x00, 0x0c, 
		0xf4, 0xdf, 0xd6, 0x4d, 0x00, 0x00, 0x00, 0x18, 
		0xec, 0x33, 0x31, 0x7e, 0x00, 0x00, 0x00, 0x01, 
		0xf7, 0x7a, 0x09, 0xe3, 0x00, 0x00, 0x00, 0xd7, 
		0xe6, 0xa7, 0xa5, 0x18, 0x00, 0x00, 0x00, 0x45, 
		0xb5, 0x0a, 0xe0, 0x64, 0x00, 0x00, 0x00, 0x75, 
		0xba, 0xf2, 0xa2, 0xb9, 0x00, 0x00, 0x00, 0x5f, 
		0x31, 0xff, 0xfd, 0x16, 0x00, 0x00, 0x01, 0x48, 
		0x03, 0xf5, 0x55, 0xab, 0x00, 0x00, 0x00, 0x1e, 
		0x85, 0x03, 0x5e, 0xa7, 0x00, 0x00, 0x00, 0x23, 
		0x4e, 0x81, 0xae, 0x7d, 0x00, 0x00, 0x00, 0x18, 
		0x67, 0x07, 0x33, 0xe3, 0x00, 0x00, 0x00, 0x14, 
		0xab, 0x81, 0x05, 0xac, 0x00, 0x00, 0x00, 0x03, 
		0x24, 0x75, 0x85, 0x14, 0x00, 0x00, 0x00, 0x36
	};
	
	/**
	 * Player cap.
	 */
	public static final int PLAYER_CAP = 2000;

	/**
	 * NPC cap.
	 */
	public static final int NPC_CAP = 4000;

	/**
	 * Server->client direction translation.
	 */
	public static final byte[] XLATE_DIRECTION_TO_CLIENT = new byte[] { 1, 2, 4, 7, 6, 5, 3, 0 };
	
	/**
	 * The player auto-saving rate (in seconds).
	 */
	public static final int AUTO_SAVE_RATE = 30;
	
	/**
	 * Return codes.
	 * @author Graham
	 *
	 */
	public static final class ReturnCodes {
		public static final int LOGIN_OK = 2;
		public static final int INVALID_PASSWORD = 3;
		public static final int BANNED = 4;
		public static final int ALREADY_ONLINE = 5;
		public static final int WORLD_FULL = 7;
		public static final int TRY_AGAIN = 11;
	}

}
