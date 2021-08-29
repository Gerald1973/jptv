package com.smilesmile1973.jptv;

public class Constants {

	public static final double STAGE_WIDTH = 960;

	public static final double STAGE_HEIGHT = 540;
	public static final double CHANNEL_LIST_WIDTH = 260;
	public static final double INFO_VIEW_WIDTH = 300;
	public static final double INFO_VIEW_HEIGHT = 400;
	/**
	 * Defines the height of the hover zone to see the stream information. On the
	 * right corner of the video.
	 */
	public static final double INFO_ZONE_HEIGHT = 100;
	/**
	 * Defines the width of the hover zone to see the stream information. On the
	 * right corner of the video.
	 */
	public static final double INFO_ZONE_WIDTH = 100;
	/**
	 * refresh period for the info about the stream.
	 */
	public static final double REFRESH_INFO_INTERVAL_MS = 1000;

	/**
	 * robot action to avoid computer sleep in ms
	 */
	public static final double ROBOT_ACTION_INTERVAL_MS = 10000;

	private Constants() {
	}

}
