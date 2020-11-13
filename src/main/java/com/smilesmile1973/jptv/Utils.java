package com.smilesmile1973.jptv;

import com.google.common.eventbus.EventBus;

public class Utils {

	private Utils() {
	}

	private static final EventBus eventBus = new EventBus();

	public static EventBus getEventBus() {
		return eventBus;
	}

}
